package gov.nist.registry.ws;

import gov.nist.registry.common2.MetadataTypes;
import gov.nist.registry.common2.exception.ExceptionUtil;
import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.SchemaValidationException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsFormatException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.RegistryUtility;
import gov.nist.registry.common2.registry.RetrieveMultipleResponse;
import gov.nist.registry.common2.registry.XdsCommon;
import gov.nist.registry.common2.service.AppendixV;
import gov.nist.registry.common2.util.PidHelper;
import gov.nist.registry.ws.config.Repository;
import gov.nist.registry.ws.serviceclasses.XdsService;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMText;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.audit.ActiveParticipant;
import org.openhealthtools.openexchange.audit.AuditCodeMappings;
import org.openhealthtools.openexchange.audit.IheAuditTrail;
import org.openhealthtools.openexchange.audit.ParticipantObject;
import org.openhealthtools.openexchange.audit.TypeValuePair;
import org.openhealthtools.openexchange.config.PropertyFacade;
import org.openhealthtools.openexchange.syslog.LogMessage;
import org.openhealthtools.openexchange.syslog.LoggerException;
import org.openhealthtools.openexchange.utils.Pair;
import org.openhealthtools.openexchange.utils.Triple;
import org.openhealthtools.openxds.common.XdsFactory;
import org.openhealthtools.openxds.repository.api.RepositoryException;
import org.openhealthtools.openxds.repository.api.RepositoryRequestContext;
import org.openhealthtools.openxds.repository.api.XdsRepository;
import org.openhealthtools.openxds.repository.api.XdsRepositoryItem;
import org.openhealthtools.openxds.repository.api.XdsRepositoryService;
import org.openhealthtools.openxua.api.XuaException;

public class RetrieveDocumentSet extends XdsCommon {
    ContentValidationService validater;
    String registry_endpoint = null;
    boolean optimize = true;
	private XdsRepository actor = null;
    /* The IHE Audit Trail for this actor. */
    private IheAuditTrail auditLog = null;
    private final static Log logger = LogFactory.getLog(RetrieveDocumentSet.class);

	public RetrieveDocumentSet(LogMessage log_message, short xds_version, MessageContext messageContext) {
        this.log_message = log_message;
        this.messageContext = messageContext;
		transaction_type = RET_transaction;

		try {
			actor = XdsFactory.getRepositoryActor(); 
    		if (actor == null) {
    			throw new XdsInternalException("Cannot find XdsRepository actor configuration.");			
    		}
    		
            auditLog = (IheAuditTrail)actor.getAuditTrail();
            init(new RetrieveMultipleResponse(), xds_version, messageContext);
        } catch (XdsInternalException e) {
            logger.fatal(logger_exception_details(e));
            response.add_error("XDSRepositoryError", e.getMessage(), ExceptionUtil.exception_details(e), log_message);
        }
    }

    public OMElement retrieveDocumentSet(OMElement rds, ContentValidationService validater, boolean optimize, XdsService service) throws SchemaValidationException, XdsInternalException {
        this.validater = validater;
        this.optimize = optimize;

        OMNamespace ns = rds.getNamespace();
        String ns_uri = ns.getNamespaceURI();
        if (ns_uri == null || !ns_uri.equals(MetadataSupport.xdsB.getNamespaceURI())) {
			return service.start_up_error(rds, "RetrieveDocumentSet.java", AppendixV.REPOSITORY_ACTOR, "Invalid namespace on RetrieveDocumentSetRequest (" + ns_uri + ")", true);
        }

        try {
            RegistryUtility.schema_validate_local(rds, MetadataTypes.METADATA_TYPE_RET);
        } catch (Exception e) {
			return service.start_up_error(rds, "RetrieveDocumentSet.java", AppendixV.REPOSITORY_ACTOR, "Schema validation errors:\n" + e.getMessage(), true);
        }

        ArrayList<OMElement> retrieve_documents = null;

     // Call X-Service Provider Actor to validate X-User Assertion with X-Assertion Provider
        try {
        	boolean validateUserAssertion = PropertyFacade.getBoolean("validate.userassertion");
        	if(validateUserAssertion){
		        SoapHeader header = new SoapHeader(messageContext);
		        boolean status = validateAssertion(header);
	        	if (!status)
					throw new XdsException("Invalid Identity Assertion");
	    	}
			retrieve_documents = retrieve_documents(rds);
        }
        catch (XdsFormatException e) {
            response.add_error("XDSRepositoryError", "SOAP Format Error: " + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
        }
        catch (MetadataException e) {
            response.add_error("XDSRepositoryError", "Request Validation Error:\n " + e.getMessage(), RegistryUtility.exception_details(e), log_message);
        }
        catch (XdsException e) {
            response.add_error("XDSRepositoryError", e.getMessage(), RegistryUtility.exception_details(e), log_message);
            logger.fatal(logger_exception_details(e));
        } catch (XuaException e) {
        	response.add_error("XDSRepositoryError", "XUA Error" + e.getMessage(), RegistryUtility.exception_details(e), log_message);
		}


        OMElement registry_response = null;
        try {
            registry_response = response.getResponse();
        } catch (XdsInternalException e) {
            logger.fatal(logger_exception_details(e));
            try {
            	if (log_message != null)
            		log_message.addErrorParam("Internal Error", "Error generating response from Ret.b");
            }
            catch (LoggerException e1) {
                logger.fatal(logger_exception_details(e1));
            }
        }

        //OMElement rdsr = MetadataSupport.om_factory.createOMElement("RetrieveDocumentSetResponse", MetadataSupport.xds_b);

        //rdsr.addChild(registry_response);

        if (retrieve_documents != null)
            for (OMElement ret_doc : retrieve_documents)
                registry_response.addChild(ret_doc);

        this.log_response();

        return response.getRoot();
    }

    ArrayList<OMElement> retrieve_documents(OMElement rds) throws MetadataException, XdsException {
        ArrayList<OMElement> document_responses = new ArrayList<OMElement>();
        ArrayList<Triple<String,String,String>> doclist = new ArrayList<Triple<String,String,String>>();
        for (OMElement doc_request : MetadataSupport.childrenWithLocalName(rds, "DocumentRequest")) {
            //HashMap<String, String> docMap = new HashMap<String, String>();
            String rep_id = null;
            String doc_id = null;
            String home = null;

            try {
                rep_id = MetadataSupport.firstChildWithLocalName(doc_request, "RepositoryUniqueId").getText();
                if (rep_id == null || rep_id.equals(""))
                    throw new Exception("");
            }
            catch (Exception e) {
                throw new MetadataException("Cannot extract RepositoryUniqueId from DocumentRequest");
            }

            try {
                doc_id = MetadataSupport.firstChildWithLocalName(doc_request, "DocumentUniqueId").getText();
                if (doc_id == null || doc_id.equals("")) throw new Exception("");
            }
            catch (Exception e) {
                throw new MetadataException("Cannot extract DocumentUniqueId from DocumentRequest");
            }

            OMElement home_ele = MetadataSupport.firstChildWithLocalName(doc_request, "HomeCommunityId");
            if (home_ele != null)
                home = home_ele.getText();
            
        	Triple<String,String,String> doc = new Triple<String,String,String>(doc_id, rep_id, home);
            doclist.add(doc);
            OMElement document_response = retrieve_document(rep_id, doc_id, home);

            if (document_response != null)
                document_responses.add(document_response);
        }
        
        auditLog(doclist, AuditCodeMappings.AuditTypeCodes.RetrieveDocumentSet);
        
        return document_responses;
    }

    OMElement retrieve_document(String rep_id, String doc_id, String home) throws XdsException {
        XdsRepositoryItem repositoryItem;
        XdsRepositoryService rm = XdsFactory.getXdsRepositoryService();
        if (!rep_id.equals(rm.getRepositoryUniqueId())) {
            response.add_error(MetadataSupport.XDSRepositoryWrongRepositoryUniqueId, "Repository Unique ID in request " +
                    rep_id +
                    " does not match this repository's id " +
                    Repository.getRepositoryUniqueId(),
                    RegistryUtility.exception_details(null), log_message);
            return null;
        }
        
        try {
            RepositoryRequestContext context = new RepositoryRequestContext();
            context.setActorDescription(actor.getActorDescription());
            repositoryItem = rm.getRepositoryItem(doc_id, context);
        } catch (RepositoryException e) {
            throw new XdsException("Cannot find repository item for document id, " + doc_id);
        }
        
        if (repositoryItem == null || repositoryItem.getDataHandler() == null)
            throw new XdsException("Document is not found in Repository");

        OMText t = MetadataSupport.om_factory.createOMText(repositoryItem.getDataHandler(), optimize);
        
        if (logger.isDebugEnabled()) {
        	logger.debug("OPTIMIZE IS " + optimize);
        }
        
        t.setOptimize(optimize);
        OMElement document_response = MetadataSupport.om_factory.createOMElement("DocumentResponse", MetadataSupport.xdsB);

        if (home != null && !home.equals("")) {
            OMElement home_ele = MetadataSupport.om_factory.createOMElement("HomeCommunityId", MetadataSupport.xdsB);
            home_ele.addChild(MetadataSupport.om_factory.createOMText(home));
            document_response.addChild(home_ele);
        }
        OMElement repid_ele = MetadataSupport.om_factory.createOMElement("RepositoryUniqueId", MetadataSupport.xdsB);
        repid_ele.addChild(MetadataSupport.om_factory.createOMText(rep_id));
        document_response.addChild(repid_ele);

        OMElement docid_ele = MetadataSupport.om_factory.createOMElement("DocumentUniqueId", MetadataSupport.xdsB);
        docid_ele.addChild(MetadataSupport.om_factory.createOMText(doc_id));
        document_response.addChild(docid_ele);

        OMElement mimetype_ele = MetadataSupport.om_factory.createOMElement("mimeType", MetadataSupport.xdsB);
        mimetype_ele.addChild(MetadataSupport.om_factory.createOMText(repositoryItem.getMimeType()));
        document_response.addChild(mimetype_ele);

        OMElement document_ele = MetadataSupport.om_factory.createOMElement("Document", MetadataSupport.xdsB);
        document_ele.addChild(t);
        document_response.addChild(document_ele);


        return document_response;
    }

    /**
     * Audit Logging of Retrieve Document.
     *
     * @throws MetadataException
     */
    private void auditLog(ArrayList<Triple<String,String,String>> doclist, AuditCodeMappings.AuditTypeCodes eventTypeCode) throws MetadataException {
        if (auditLog == null)
            return;

        String replyto = getMessageContext().getReplyTo().getAddress();
        String remoteIP = (String) getMessageContext().getProperty(MessageContext.REMOTE_ADDR);
        String localIP = (String) getMessageContext().getProperty(MessageContext.TRANSPORT_ADDR);

        ActiveParticipant source = new ActiveParticipant();
        source.setUserId(replyto);
        // 'Retrieve Document Set' requires the altUserId of the source to be set to the Process
        // ID of the JVM. TF6 vol 2b section 3.43.6.1.1
        source.setAltUserId(PidHelper.getPid());
        source.setAccessPointId(remoteIP);
        
        ActiveParticipant dest = new ActiveParticipant();
        dest.setAccessPointId(localIP);
        String userid = actor.getServiceEndpoint(isHttps());
        dest.setUserId(userid);
        //Document Info
        Collection<ParticipantObject> docs = new ArrayList<ParticipantObject>();
        for (Triple<String,String,String> doc : doclist) {
            ParticipantObject docObj = new ParticipantObject();
            docObj.setId(doc.first);
            docObj.addDetail(new TypeValuePair("RepositoryUniqueId", doc.second));
            if (doc.third != null) {
            	docObj.addDetail(new TypeValuePair("ihe:homeCommunityID", doc.third));
            }
            docs.add(docObj);
        }
        //Finally Log it.
        auditLog.logRepositoryQuery(source, dest, docs, eventTypeCode);
    }


}
