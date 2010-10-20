package gov.nist.registry.common2.registry;

import gov.nist.registry.common2.exception.ExceptionUtil;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.ws.SoapHeader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.transport.http.AxisServlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.actorconfig.IActorDescription;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.actorconfig.net.SecureConnection;
import org.openhealthtools.openxds.XdsConstants;
import org.openhealthtools.openxds.XdsFactory;
import org.openhealthtools.openexchange.syslog.LogMessage;
import org.openhealthtools.openexchange.syslog.LoggerException;
import org.openhealthtools.openxds.registry.api.ResourceingFilter;
import org.openhealthtools.openxds.registry.api.XdsRegistry;
import org.openhealthtools.openxds.repository.api.XdsRepository;
import org.openhealthtools.openxua.api.AssertionException;
import org.openhealthtools.openxua.api.XServiceProvider;

import com.misyshealthcare.connect.net.Identifier;

public class XdsCommon  {

	public Response response = null;
	public LogMessage log_message = null;
	public static final short xds_none = 0;
	public static final short xds_a = 2;
	public static final short xds_b = 3;
	public short xds_version = xds_none;
	protected MessageContext messageContext = null;
	/**Is Responding Gateway*/
	boolean isRG = false;
	private final static Log logger = LogFactory.getLog(XdsCommon.class);
	
	public static final short UNKNOWN_transaction = 0;
	public static final short PR_transaction = 1;
	public static final short R_transaction = 2;
	public static final short SQ_transaction = 3;
	public static final short RET_transaction = 4;
	public static final short OTHER_transaction = 5;
	public short transaction_type = UNKNOWN_transaction;

	public MessageContext getMessageContext() {
		return messageContext;
	}
	
	/**whether this transaction request is secure*/
	protected boolean isHttps() {
		String protocol = this.messageContext.getTransportIn().getName();
		if (protocol.equalsIgnoreCase("https")) {
			return true;
		} 
		return false;
	}
	
	public void setIsRG() {
		isRG = true;
		if (response != null)
			response.setIsRG();
	}
	
	public void init(Response response, short xds_version, MessageContext messageContext) throws XdsInternalException {
		if (transaction_type == UNKNOWN_transaction)
			throw new XdsInternalException("transaction_type is UNKNOWN");
		this.response = response;
		if (isRG)
			response.setIsRG();
		this.xds_version = xds_version;
		this.messageContext = messageContext;

	}
	
	public boolean getStatus() {
		return ! response.has_errors();
	}

	protected void log_status() {
		try {
			String e_and_w = response.getErrorsAndWarnings();
			if (e_and_w != null && !e_and_w.equals(""))
				log_message.addErrorParam("Error", e_and_w);
		} catch (Exception e) {
			response.error("Internal Error: cannot set final status in test log on transaction");
		}
	}

	protected void init_log() throws LoggerException {
	}


	public static String logger_exception_details(Exception e) {
		if (e == null) 
			return "";

		return e.getClass().getName() + "  " + e.getMessage() + ExceptionUtil.exception_details(e, 10);
	}

	protected void log_response()  {
		
		generateAuditLog(response);
		
		if (log_message == null) {
			logger.fatal("\nFATAL ERROR: XdsCommon.log_response(): log_message is null\n");
			return;
		}
		try {
			if (response.has_errors()) {
				log_message.setPass(false);
				log_message.addErrorParam("Errors", response.getErrorsAndWarnings());
			} else
				log_message.setPass(true);

			log_message.addOtherParam("Response", response.getResponse().toString());
		}
		catch (LoggerException e) {
			logger.error("**************ERROR: Logger exception attempting to return to user");
		}
		catch (XdsInternalException e) {
			logger.error("**************ERROR: Internal exception attempting to return to user");
		}
	}
	
	protected void generateAuditLog(Response response)  {

	}
	
	protected void generateAuditLog(Metadata m) {
		
	}
	
	private OMElement getUserAssertion(SoapHeader header) throws XdsException{
		// Get Identity Assertion from web-services security header
		OMElement security = MetadataSupport.firstChildWithLocalName(header.getHdr(), "Security");
		if (security == null)
			throw new XdsException("Security element does not exists in web-services security header");
			
		OMElement element = MetadataSupport.firstChildWithLocalName(security, "Assertion");
		if (element == null)
			throw new XdsException("Identity Assertion does not exists in web-services security header");
		
		return element;
	}
	
	protected boolean validateAssertion(SoapHeader header)throws XdsException, AssertionException {
		OMElement assertion = getUserAssertion(header);
		XServiceProvider provider = (XServiceProvider) XdsFactory.getInstance().getBean("xuaServiceProvider");
		if (provider == null)
			throw new XdsException("Exception while getting XServiceProvider instance");
		
		boolean status = provider.validateToken(assertion);
		return status;
	}
	
	protected Metadata filter(Metadata metadata, SoapHeader header) throws XdsException, Exception{
		ResourceingFilter filter = null;
		try {
			filter = (ResourceingFilter)XdsFactory.getInstance().getBean("resourceingFilter");
			if(filter == null){
				throw new Exception("ResourceingFilter bean is null");
			}
		}catch (Exception e) {
			logger.error("Exception while getting filtering resource bean",e);
			throw e;
		}
		
		try {
			if(metadata == null)
				return null;
			List<OMElement> extrinsicObjects = metadata.getExtrinsicObjects();
			OMElement userAssertion = getUserAssertion(header);
			logger.info("Filtering extrinsicObjects:"+extrinsicObjects.size());
			List<OMElement> filteredList = filter.filterExtrinsicObjects(extrinsicObjects, userAssertion);
			
			if(filteredList != null){
				Metadata metadataRef = new Metadata();
				metadataRef.addExtrinsicObjects(filteredList);
				List<String> extrinsicObjectIds = metadataRef.getExtrinsicObjectIds();
				
				//Remove extrinsicObjects from metadata those are not available in extrinsicObjectIds(user does not have permission to view)
				List<OMElement> duplicateExtList = new ArrayList<OMElement>(extrinsicObjects);
				for (OMElement extrinsicObject : duplicateExtList) {
					String id = metadata.id(extrinsicObject);
					if (!extrinsicObjectIds.contains(id)){
						extrinsicObjects.remove(extrinsicObject);
						metadata.getAllObjects().remove(extrinsicObject);
					}
				}
				
				//Remove associations those are related to deleted extrinsicObjects
				List<OMElement> associations = metadata.getAssociations();
				List<OMElement> duplicateAssociations = new ArrayList<OMElement>(associations);
				for(OMElement association : duplicateAssociations){
					if(!extrinsicObjectIds.contains(metadata.getAssocSource(association)) || !extrinsicObjectIds.contains(metadata.getAssocTarget(association))){
						associations.remove(association);
						metadata.getAllObjects().remove(association);
					}
				}
			}
			logger.info("no. of extrinsicObjects returned:"+metadata.getExtrinsicObjects().size());
		}catch(XdsException e){
			logger.debug("Exception while getting User Assertion:" + e.getMessage(), e);
			throw e;
		}
		catch(Exception e){
			logger.error("Exception filtering metadata");
			throw e;
		}
		
		return metadata;
	}

    /**
     * Reconciles authority with the ConnectionDescritpion configuration. An authority
     * can have NameSpace and/or UniversalId/UniversalIdType. For example, in the data source such as
     * database, if an authority is represented by NameSpace only, while in the xml configuration, the authority is configured
     * with both NameSpace and UnviersalId/UniversalIdType. The authority in the datasource has to be mapped
     * to the authority configured in the XML files.
     *
     * @param authority The authority
     * @param connection
     * @param adapter the adapter from where to get the domains
     * @return The authority according the configuration
     */
    protected Identifier reconcileIdentifier(Identifier authority, IConnectionDescription connection) {
        List<org.openhealthtools.openexchange.patient.data.Identifier> identifiers = connection.getAllIdentifiersByType("domain");
        for (org.openhealthtools.openexchange.patient.data.Identifier id : identifiers) {
    
        	//TODO: Fix the Identifier type
        	//Temporary conversion during the library migration
        	Identifier identifier = new Identifier(id.getNamespaceId(), id.getUniversalId(), id.getUniversalIdType()); 
            if ( identifier.equals(authority) ) {
                return identifier;
            }
        }
        //no identifier is found, just return the original authority
        return authority;
    }
    protected Identifier reconcileIdentifier(Identifier authority, IActorDescription actorDescription) {
        List<org.openhealthtools.openexchange.patient.data.Identifier> identifiers = actorDescription.getAllIdentifiersByType("domain");
        for (org.openhealthtools.openexchange.patient.data.Identifier id : identifiers) {
    
        	//TODO: Fix the Identifier type
        	//Temporary conversion during the library migration
        	Identifier identifier = new Identifier(id.getNamespaceId(), id.getUniversalId(), id.getUniversalIdType()); 
            if ( identifier.equals(authority) ) {
                return identifier;
            }
        }
        //no identifier is found, just return the original authority
        return authority;
    }

	protected XdsRepository getRepositoryActor() throws XdsInternalException {
		AxisServlet server = (AxisServlet)this.messageContext.getTransportIn().getReceiver();
		Collection<XdsRepository> actors = (Collection<XdsRepository>)server.getServletConfig().getServletContext().getAttribute(XdsConstants.REPOSITORY_ACTORS);

		if (actors == null || actors.isEmpty() ) {
			throw new XdsInternalException("No XdsRepository Actor is configured.");
		}
		
		if (actors.size() == 1) { 
			return actors.iterator().next();
		}

		//Select one that is most appropriate
		XdsRepository ret = null;
		for (XdsRepository actor : actors) {
			boolean isSecureActor = actor.getRegistryClientConnection() instanceof SecureConnection;
			
			ret = actor;
			if ( isHttps() && isSecureActor ||
			  	!isHttps() && !isSecureActor) {
			    return actor;
			}
		} 
		return ret;
	}
 
	protected XdsRegistry getRegistryActor() throws XdsInternalException {
		AxisServlet server = (AxisServlet)this.messageContext.getTransportIn().getReceiver();
		Collection<XdsRegistry> actors = (Collection<XdsRegistry>)server.getServletConfig().getServletContext().getAttribute(XdsConstants.REGISTRY_ACTORS);

		if (actors == null || actors.isEmpty() ) {
			throw new XdsInternalException("No XdsRepository Actor is configured.");
		}
		
		if (actors.size() == 1) { 
			return actors.iterator().next();
		}

		//Select one that is most appropriate
		XdsRegistry ret = null;
		for (XdsRegistry actor : actors) {
			
			boolean isSecureActor = actor.getPixRegistryConnection() instanceof SecureConnection;
			
			ret = actor;
			if ( isHttps() && isSecureActor ||
			  	!isHttps() && !isSecureActor) {
			    return actor;
			}
		} 
		return ret;
	}
	
}
