package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.exception.XdsValidationException;
import gov.nist.registry.common2.exception.XdsWSException;
import gov.nist.registry.common2.registry.AdhocQueryResponse;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.RegistryErrorList;
import gov.nist.registry.common2.registry.RegistryResponse;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.RetrieveDocumentSetResponse;
import gov.nist.registry.common2.registry.XdsCommon;
import gov.nist.registry.common2.service.AppendixV;
import gov.nist.registry.common2.soap.Soap;
import gov.nist.registry.common2.xca.HomeAttribute;
import gov.nist.registry.ws.AdhocQueryRequest;
import gov.nist.registry.ws.ContentValidationService;
import gov.nist.registry.ws.RetrieveDocumentSet;

import java.util.List;

import org.apache.axiom.om.OMElement;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.common.ihe.IheActor;
import org.openhealthtools.common.utils.ConnectionUtil;
import org.openhealthtools.common.ws.server.IheHTTPServer;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.config.PropertyFacade;
import org.openhealthtools.openxds.log.LoggerException;
import org.openhealthtools.openxds.xca.api.XcaRG;


public abstract class RGAbstract extends XdsService implements ContentValidationService {
	private final static Log logger = LogFactory.getLog(RGAbstract.class);
	boolean optimize = true;
	static String homeProperty;
	String home;

	static {
		homeProperty = PropertyFacade.getString("home.community.id");
	}
    IConnectionDescription connection = null;
    IConnectionDescription registryClientConnection = null;
    IConnectionDescription repositoryClientConnection = null;

	abstract public boolean runContentValidationService(Metadata request, Response response);
	abstract public OMElement AdhocQueryRequest(OMElement ahqr);
	abstract protected void validateWS() throws XdsWSException;
	abstract protected void validateQueryTransaction(OMElement sor) throws XdsValidationException, MetadataValidationException, XdsInternalException;
	abstract protected void validateRetrieveTransaction(OMElement sor) throws XdsValidationException;
	abstract protected String getQueryTransactionName();
	abstract protected String getRetTransactionName();

	public RGAbstract() {
		home = homeProperty;  // allows sub-classes to override
		IheHTTPServer httpServer = (IheHTTPServer)getMessageContext().getTransportIn().getReceiver();
		try {
			IheActor actor = httpServer.getIheActor();
			if (actor == null) {
				throw new XdsInternalException("Cannot find XcaRG actor configuration.");			
			}
			connection = actor.getConnection();
			if (connection == null) {
				throw new XdsInternalException("Cannot find XcaRG connection configuration.");			
			}
			registryClientConnection = ((XcaRG)actor).getRegistryClientConnection();
			if (registryClientConnection == null) {
				throw new XdsInternalException("Cannot find XcaRG XdsRegistryClient connection configuration.");			
			}
			repositoryClientConnection = ((XcaRG)actor).getRepositoryClientConnection();
			if (repositoryClientConnection == null) {
				throw new XdsInternalException("Cannot find XcaRG XdsRepositoryClient connection configuration.");			
			}
		} catch (XdsInternalException e) {
			logger.fatal("Internal Error getting XcaRG actor configuration: " + e.getMessage());
		}
	}

	protected void setHome(String home) {
		this.home = home;
	}

	public String getServiceName() {	return "RG";   }


	public void setOptimize(boolean opt) { this.optimize = opt; }

	public OMElement AdhocQueryRequest(OMElement ahqr, String profile_name, String service_name) {
		try {
			OMElement startup_error = beginTransaction(getQueryTransactionName(), ahqr, AppendixV.REGISTRY_ACTOR);
			if (startup_error != null)
				return startup_error;
			log_message.setTestMessage(getQueryTransactionName());
			OMElement ahq = MetadataSupport.firstChildWithLocalName(ahqr, "AdhocQuery") ;
			if (ahq == null) {
				endTransaction(false);
				return this.start_up_error(ahqr, null, REGISTRY_ACTOR, profile_name + " only accepts Stored Query - AdhocQuery element not found");
			}

			validateWS();

			validateQueryTransaction(ahqr);

			RegistryErrorList rel = new RegistryErrorList(RegistryErrorList.version_3,  true /* log */);
			rel.setIsRG();

			AdhocQueryRequest a = new AdhocQueryRequest(log_message, getMessageContext(), isSecure(), XdsCommon.xds_b );
			a.setServiceName(service_name);
			a.setIsRG();

			if (a.requiresHomeInXGQ(ahqr)) {
				String home = a.getHome(ahqr);
				if (home == null || home.equals("")) {
					rel.add_error(MetadataSupport.XDSStoredQueryMissingParam, 
							"homeCommunityId is missing but is required on this type of Stored Query when used in XCA", 
							"RGAbstract.java", log_message);
					OMElement response = new AdhocQueryResponse(Response.version_3, rel).getResponse();
					addOther("Response", response.toString());
					endTransaction(false);
					return response;
				}
			}


			/*OMElement result = a.adhocQueryRequest(ahqr);


			setHomeOnSQResponse(result, home);
			endTransaction(a.getStatus());
			return result;*/
			Protocol protocol = ConnectionUtil.getProtocol(registryClientConnection);
			String epr = ConnectionUtil.getTransactionEndpoint(registryClientConnection);
			Soap soap = new Soap();
			soap.soapCall(ahqr, protocol, epr, false, true, true, "urn:ihe:iti:2007:RegistryStoredQuery", null);
			
			OMElement result = soap.getResult();
			
			return processRegResult(result);
		} 
		catch (Exception e) {
			return endTransaction(ahqr, e, AppendixV.REGISTRY_ACTOR, "");
		}
	}

	public OMElement RetrieveDocumentSetRequest(OMElement rdsr) {
		try {
			OMElement startup_error = beginTransaction(getRetTransactionName(), rdsr, AppendixV.REPOSITORY_ACTOR);
			if (startup_error != null)
				return startup_error;
			log_message.setTestMessage(getRetTransactionName());

			validateWS();

			validateRetrieveTransaction(rdsr);

			RegistryErrorList rel = new RegistryErrorList(RegistryErrorList.version_3,  true /* log */);

			verifyHomeOnRetrieve(rdsr, rel, home);
			if (rel.has_errors()) {
				rel.setIsRG();
				OMElement response = new RetrieveDocumentSetResponse(new RegistryResponse(RegistryErrorList.version_3, rel)).getResponse();
				addOther("Response", response.toString());
				endTransaction(false);
				return response;
			}

//			RetrieveDocumentSet s = new RetrieveDocumentSet(log_message, XdsCommon.xds_b, getMessageContext());
//			s.setIsXCA();
//			System.out.println("RBAbstract:Retrieve(): optimize is " + optimize);
//			OMElement result = s.retrieveDocumentSet(rdsr, this, optimize /* optimize */, this);
//			setHomeOnRetResponse(result);
//			log_message.addOtherParam("Result", result.toString());			
//			endTransaction(s.getStatus());

			Protocol protocol = ConnectionUtil.getProtocol(repositoryClientConnection);
			String epr = ConnectionUtil.getTransactionEndpoint(repositoryClientConnection);
			Soap soap = new Soap();
			soap.soapCall(rdsr, protocol, epr, true, true, true, "urn:ihe:iti:2007:RetrieveDocumentSet", null);
			
			OMElement result = soap.getResult();
			
			return processResult(result);
		} catch (Exception e) {
			return endTransaction(rdsr, e, AppendixV.REPOSITORY_ACTOR, "");
		}

	}
	private OMElement processResult(OMElement result) throws XdsInternalException, LoggerException {
		RegistryErrorList rel = new RegistryErrorList(RegistryErrorList.version_3,  true /* log */);
		rel.setIsRG();
		
		if (result == null) {
			rel.add_error(MetadataSupport.XDSRepositoryError, "Null response message from Repository", "RGAbstract.java", log_message);
			OMElement response = new RetrieveDocumentSetResponse(new RegistryResponse(RegistryErrorList.version_3, rel)).getResponse();
			addOther("Response", response.toString());
			endTransaction(false);
			return response;
		}
		
		OMElement rr = MetadataSupport.firstChildWithLocalName(result, "RegistryResponse");
		if (rr == null) {
			rel.add_error(MetadataSupport.XDSRepositoryError, "Null RegistryResponse from Repository", "RGAbstract.java", log_message);
			OMElement response = new RetrieveDocumentSetResponse(new RegistryResponse(RegistryErrorList.version_3, rel)).getResponse();
			addOther("Response", response.toString());
			endTransaction(false);
			return response;				
		}
		String status = rr.getAttributeValue(MetadataSupport.status_qname);
		if (status == null) {
			rel.add_error(MetadataSupport.XDSRepositoryError, "Null status from Repository", "RGAbstract.java", log_message);
			OMElement response = new RetrieveDocumentSetResponse(new RegistryResponse(RegistryErrorList.version_3, rel)).getResponse();
			addOther("Response", response.toString());
			endTransaction(false);
			return response;				
		}

		if ( !status.equals(MetadataSupport.response_status_type_namespace + "Success")) {
			OMElement registry_error_list = MetadataSupport.firstChildWithLocalName(result, "RegistryErrorList"); 
			rel.addRegistryErrorList(registry_error_list, log_message);
			OMElement response = new RetrieveDocumentSetResponse(new RegistryResponse(RegistryErrorList.version_3, rel)).getResponse();
			addOther("Response", response.toString());
			endTransaction(false);
			return response;
		} else {
			setHomeOnRetResponse(result);
			log_message.addOtherParam("Result", result.toString());
			log_message.setPass(true);
			endTransaction(true);
			return result;
		}		
	}
	
	private OMElement processRegResult(OMElement result) throws XdsInternalException, LoggerException {
		RegistryErrorList rel = new RegistryErrorList(RegistryErrorList.version_3,  true /* log */);
		rel.setIsRG();
		
		if (result == null) {
			rel.add_error(MetadataSupport.XDSRegistryError, "Null response message from Registry", "RGAbstract.java", log_message);
			OMElement response = new RetrieveDocumentSetResponse(new RegistryResponse(RegistryErrorList.version_3, rel)).getResponse();
			addOther("Response", response.toString());
			endTransaction(false);
			return response;
		}
		
		OMElement rol = MetadataSupport.firstChildWithLocalName(result, "RegistryObjectList");
		if (rol == null) {
			rel.add_error(MetadataSupport.XDSRegistryError, "Null RegistryObjectList from Registry", "RGAbstract.java", log_message);
			OMElement response = new RetrieveDocumentSetResponse(new RegistryResponse(RegistryErrorList.version_3, rel)).getResponse();
			result.getFirstElement();
			addOther("Response", response.toString());
			endTransaction(false);
			return response;				
		}
		String status = result.getAttributeValue(MetadataSupport.status_qname);
		if (status == null) {
			rel.add_error(MetadataSupport.XDSRegistryError, "Null status from Registry", "RGAbstract.java", log_message);
			OMElement response = new RetrieveDocumentSetResponse(new RegistryResponse(RegistryErrorList.version_3, rel)).getResponse();
			addOther("Response", response.toString());
			endTransaction(false);
			return response;				
		}

		if ( !status.equals(MetadataSupport.response_status_type_namespace + "Success")) {
			OMElement registry_error_list = MetadataSupport.firstChildWithLocalName(result, "RegistryErrorList"); 
			rel.addRegistryErrorList(registry_error_list, log_message);
			OMElement response = new RetrieveDocumentSetResponse(new RegistryResponse(RegistryErrorList.version_3, rel)).getResponse();
			addOther("Response", response.toString());
			endTransaction(false);
			return response;
		} else {
			setHomeOnSQResponse(result, home);
			log_message.addOtherParam("Result", result.toString());
			log_message.setPass(true);
			endTransaction(true);
			return result;
		}		
	}
	
	protected void verifyHomeOnRetrieve(OMElement rdsr, RegistryErrorList rel, String home) {
		List<OMElement> doc_requests = MetadataSupport.decendentsWithLocalName(rdsr, "DocumentRequest");
		for (OMElement doc_request : doc_requests) {
			OMElement home_att_ele = MetadataSupport.firstChildWithLocalName(doc_request, "HomeCommunityId");
			if (home_att_ele == null) {
				rel.add_error(MetadataSupport.XDSMissingHomeCommunityId, "homeCommunityId missing or empty" , null, log_message);
				continue;
			}
			String home_att = home_att_ele.getText();
			if (home_att == null || home_att.equals(""))
				rel.add_error(MetadataSupport.XDSMissingHomeCommunityId, "homeCommunityId missing or empty" , null, log_message);
			else if ( !home_att.equals(home))
				rel.add_error(MetadataSupport.XDSUnknownCommunity, "Do not understand homeCommunityId " + home_att , null, log_message);
		}
	}

	public OMElement AdhocQueryRequestTooManyDocs(OMElement ahqr, String profile_name, String service_name) {
		try {
			OMElement startup_error = beginTransaction(service_name, ahqr, AppendixV.REGISTRY_ACTOR);
			if (startup_error != null)
				return startup_error;
			log_message.setTestMessage(service_name);
			OMElement ahq = MetadataSupport.firstChildWithLocalName(ahqr, "AdhocQuery") ;
			if (ahq == null) {
				endTransaction(false);
				return this.start_up_error(ahqr, null, REGISTRY_ACTOR, profile_name + " only accepts Stored Query - AdhocQuery element not found");
			}

			OMElement respOption = MetadataSupport.firstChildWithLocalName(ahqr, "ResponseOption");
			if (respOption == null) {
				endTransaction(false);
				return this.start_up_error(ahqr, null, REGISTRY_ACTOR, profile_name + " ResponseOption element missing");
			}

			String returnType = respOption.getAttributeValue(MetadataSupport.return_type_qname);
			if ( !returnType.equals("LeafClass")) {
				endTransaction(false);
				return this.start_up_error(ahqr, null, REGISTRY_ACTOR, profile_name + " This endpoint only accepts LeafClass returnType requests");
			}

			RegistryErrorList rel = new RegistryErrorList(RegistryErrorList.version_3,  true /* log */);
			rel.setIsRG();

			rel.add_error(MetadataSupport.XDSTooManyResults, 
					"Too many documents were requested", 
					"RGAbstract.java", log_message);
			OMElement response = new AdhocQueryResponse(Response.version_3, rel).getResponse();
			addOther("Response", response.toString());
			endTransaction(false);
			return response;
		} 
		catch (Exception e) {
			return endTransaction(ahqr, e, AppendixV.REPOSITORY_ACTOR, "");
		}
	}

	public OMElement AdhocQueryRequestForceError(OMElement ahqr, String profile_name, String service_name, String errorCode, String errorMessage) {
		try {
			OMElement startup_error = beginTransaction(service_name, ahqr, AppendixV.REGISTRY_ACTOR);
			if (startup_error != null)
				return startup_error;
			log_message.setTestMessage(service_name);
			OMElement ahq = MetadataSupport.firstChildWithLocalName(ahqr, "AdhocQuery") ;
			if (ahq == null) {
				endTransaction(false);
				return this.start_up_error(ahqr, null, REGISTRY_ACTOR, profile_name + " only accepts Stored Query - AdhocQuery element not found");
			}

			OMElement respOption = MetadataSupport.firstChildWithLocalName(ahqr, "ResponseOption");
			if (respOption == null) {
				endTransaction(false);
				return this.start_up_error(ahqr, null, REGISTRY_ACTOR, profile_name + " ResponseOption element missing");
			}

			String returnType = respOption.getAttributeValue(MetadataSupport.return_type_qname);
			if ( !returnType.equals("LeafClass")) {
				endTransaction(false);
				return this.start_up_error(ahqr, null, REGISTRY_ACTOR, profile_name + " This endpoint only accepts LeafClass returnType requests");
			}

			RegistryErrorList rel = new RegistryErrorList(RegistryErrorList.version_3,  true /* log */);
			rel.setIsRG();

			rel.add_error(errorCode, 
					errorMessage, 
					"RGAbstract.java", log_message);
			OMElement response = new AdhocQueryResponse(Response.version_3, rel).getResponse();
			addOther("Response", response.toString());
			endTransaction(false);
			return response;
		} 
		catch (Exception e) {
			return endTransaction(ahqr, e, AppendixV.REPOSITORY_ACTOR, "");
		}
	}
	
	public OMElement RetrieveDocumentForceError(OMElement rdsr, String errorCode, String errorMessage) {
		try {
			OMElement startup_error = beginTransaction(getRetTransactionName(), rdsr, AppendixV.REPOSITORY_ACTOR);
			if (startup_error != null)
				return startup_error;
			log_message.setTestMessage(getRetTransactionName());

			validateWS();

//			validateRetrieveTransaction(rdsr);

			RegistryErrorList rel = new RegistryErrorList(RegistryErrorList.version_3,  true /* log */);

//			verifyHomeOnRetrieve(rdsr, rel, home);
			
			rel.add_error(errorCode, 
					errorMessage, 
					"RGAbstract.java", log_message);
			
			if (rel.has_errors()) {
				rel.setIsRG();
				OMElement response = new RetrieveDocumentSetResponse(new RegistryResponse(RegistryErrorList.version_3, rel)).getResponse();
				addOther("Response", response.toString());
				endTransaction(false);
				return response;
			}


			RetrieveDocumentSet s = new RetrieveDocumentSet(log_message, XdsCommon.xds_b, getMessageContext());
			s.setIsRG();

			if (logger.isDebugEnabled()) {	
				logger.debug("RBAbstract:Retrieve(): optimize is " + optimize);
			}
			
			OMElement result = s.retrieveDocumentSet(rdsr, this, optimize /* optimize */, this);

			setHomeOnRetResponse(result);

			log_message.addOtherParam("Result", result.toString());

			endTransaction(s.getStatus());
			return result;
		} catch (Exception e) {
			return endTransaction(rdsr, e, AppendixV.REPOSITORY_ACTOR, "");
		}

	}
	
	void setHomeOnSQResponse(OMElement root, String home) {
		HomeAttribute homeAtt = new HomeAttribute(home);
		homeAtt.set(root);
	}

	void setHomeOnRetResponse(OMElement result) {
		List<OMElement> doc_responses = MetadataSupport.decendentsWithLocalName(result, "DocumentResponse");
		for (OMElement doc_response : doc_responses) {
			if (MetadataSupport.firstChildWithLocalName(doc_response, MetadataSupport.home_community_id_qname.getLocalPart()) == null) {
				OMElement hci = MetadataSupport.om_factory.createOMElement(MetadataSupport.home_community_id_qname);
				hci.setText(home);
				doc_response.getFirstElement().insertSiblingBefore(hci);
			}
		}
	}


}
