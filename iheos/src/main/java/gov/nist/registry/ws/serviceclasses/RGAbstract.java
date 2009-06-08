package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsValidationException;
import gov.nist.registry.common2.exception.XdsWSException;
import gov.nist.registry.common2.registry.AdhocQueryResponse;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.Properties;
import gov.nist.registry.common2.registry.RegistryErrorList;
import gov.nist.registry.common2.registry.RegistryResponse;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.RetrieveDocumentSetResponse;
import gov.nist.registry.common2.registry.XdsCommon;
//import gov.nist.registry.common2.xca.HomeAttribute;
import gov.nist.registry.ws.AdhocQueryRequest;
import gov.nist.registry.ws.ContentValidationService;
import gov.nist.registry.ws.RetrieveDocumentSet;

import java.util.ArrayList;

import org.apache.axiom.om.OMElement;

public abstract class RGAbstract extends XdsService implements ContentValidationService {
	boolean optimize = true;
	static String homeProperty;
	String home;

	static {
		homeProperty = Properties.loader().getString("home_community_id");
	}

	abstract public boolean runContentValidationService(Metadata request, Response response);
	abstract public OMElement AdhocQueryRequest(OMElement ahqr);
	abstract protected void validateWS() throws XdsWSException;
	abstract protected void validateQueryTransaction(OMElement sor) throws XdsValidationException;
	abstract protected void validateRetrieveTransaction(OMElement sor) throws XdsValidationException;
	abstract protected String getQueryTransactionName();
	abstract protected String getRetTransactionName();

	public RGAbstract() {
		home = homeProperty;  // allows sub-classes to override
	}

	protected void setHome(String home) {
		this.home = home;
	}

	public String getServiceName() {	return "RG";   }


	public void setOptimize(boolean opt) { this.optimize = opt; }

	public OMElement AdhocQueryRequest(OMElement ahqr, String profile_name, String service_name) {
		try {
			OMElement startup_error = beginTransaction(getQueryTransactionName(), ahqr, XdsService.registry_actor);
			if (startup_error != null)
				return startup_error;
			log_message.setTestMessage(getQueryTransactionName());
			OMElement ahq = MetadataSupport.firstChildWithLocalName(ahqr, "AdhocQuery") ;
			if (ahq == null) {
				endTransaction(false);
				return this.start_up_error(ahqr, null, registry_actor, profile_name + " only accepts Stored Query - AdhocQuery element not found");
			}

			validateWS();

			validateQueryTransaction(ahqr);

			RegistryErrorList rel = new RegistryErrorList(RegistryErrorList.version_3,  true /* log */);

			AdhocQueryRequest a = new AdhocQueryRequest(log_message, getMessageContext(), isSecure(), XdsCommon.xds_b );
			a.setServiceName(service_name);

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


			OMElement result = a.adhocQueryRequest(ahqr);


			setHomeOnSQResponse(result, home);
			endTransaction(a.getStatus());
			return result;
		} 
		catch (Exception e) {
			return endTransaction(ahqr, e, XdsService.repository_actor, "");
		}
	}

	public OMElement RetrieveDocumentSetRequest(OMElement rdsr) {
		try {
			OMElement startup_error = beginTransaction(getRetTransactionName(), rdsr, XdsService.repository_actor);
			if (startup_error != null)
				return startup_error;
			log_message.setTestMessage(getRetTransactionName());

			validateWS();

			validateRetrieveTransaction(rdsr);

			RegistryErrorList rel = new RegistryErrorList(RegistryErrorList.version_3,  true /* log */);

			verifyHomeOnRetrieve(rdsr, rel, home);
			if (rel.has_errors()) {
				OMElement response = new RetrieveDocumentSetResponse(new RegistryResponse(RegistryErrorList.version_3, rel)).getResponse();
				addOther("Response", response.toString());
				endTransaction(false);
				return response;
			}


			RetrieveDocumentSet s = new RetrieveDocumentSet(log_message, XdsCommon.xds_b, getMessageContext());

			System.out.println("RBAbstract:Retrieve(): optimize is " + optimize);
			OMElement result = s.retrieveDocumentSet(rdsr, this, optimize /* optimize */, this);

			setHomeOnRetResponse(result);

			log_message.addOtherParam("Result", result.toString());

			endTransaction(s.getStatus());
			return result;
		} catch (Exception e) {
			return endTransaction(rdsr, e, XdsService.repository_actor, "");
		}

	}
	protected void verifyHomeOnRetrieve(OMElement rdsr, RegistryErrorList rel, String home) {
		ArrayList<OMElement> doc_requests = MetadataSupport.decendentsWithLocalName(rdsr, "DocumentRequest");
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
			OMElement startup_error = beginTransaction(service_name, ahqr, XdsService.registry_actor);
			if (startup_error != null)
				return startup_error;
			log_message.setTestMessage(service_name);
			OMElement ahq = MetadataSupport.firstChildWithLocalName(ahqr, "AdhocQuery") ;
			if (ahq == null) {
				endTransaction(false);
				return this.start_up_error(ahqr, null, registry_actor, profile_name + " only accepts Stored Query - AdhocQuery element not found");
			}

			OMElement respOption = MetadataSupport.firstChildWithLocalName(ahqr, "ResponseOption");
			if (respOption == null) {
				endTransaction(false);
				return this.start_up_error(ahqr, null, registry_actor, profile_name + " ResponseOption element missing");
			}

			String returnType = respOption.getAttributeValue(MetadataSupport.return_type_qname);
			if ( !returnType.equals("LeafClass")) {
				endTransaction(false);
				return this.start_up_error(ahqr, null, registry_actor, profile_name + " This endpoint only accepts LeafClass returnType requests");
			}

			RegistryErrorList rel = new RegistryErrorList(RegistryErrorList.version_3,  true /* log */);

			rel.add_error(MetadataSupport.XDSTooManyResults, 
					"Too many documents were requested", 
					"RGAbstract.java", log_message);
			OMElement response = new AdhocQueryResponse(Response.version_3, rel).getResponse();
			addOther("Response", response.toString());
			endTransaction(false);
			return response;
		} 
		catch (Exception e) {
			return endTransaction(ahqr, e, XdsService.repository_actor, "");
		}
	}

	void setHomeOnSQResponse(OMElement root, String home) {
//TODO:
//		HomeAttribute homeAtt = new HomeAttribute(home);
//		homeAtt.set(root);
	}

	void setHomeOnRetResponse(OMElement result) {
		ArrayList<OMElement> doc_responses = MetadataSupport.decendentsWithLocalName(result, "DocumentResponse");
		for (OMElement doc_response : doc_responses) {
			if (MetadataSupport.firstChildWithLocalName(doc_response, MetadataSupport.home_community_id_qname.getLocalPart()) == null) {
				OMElement hci = MetadataSupport.om_factory.createOMElement(MetadataSupport.home_community_id_qname);
				hci.setText(home);
				doc_response.getFirstElement().insertSiblingBefore(hci);
			}
		}
	}


}
