package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.exception.XdsValidationException;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.RegistryErrorList;
import gov.nist.registry.common2.registry.RetrieveDocumentSetResponse;
import gov.nist.registry.common2.xml.Util;
import gov.nist.registry.ws.RetrieveDocumentSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.syslog.LoggerException;
import org.openhealthtools.openxds.common.XdsFactory;
import org.openhealthtools.openxds.xca.Aggregator;
import org.openhealthtools.openxds.xca.RetrieveAggregator;
import org.openhealthtools.openxds.xca.SoapCall;
import org.openhealthtools.openxds.xca.api.XcaIG;

public class XcaRepository extends RepositoryB {
	private final static Log logger = LogFactory.getLog(XcaRepository.class);
	
	private XcaIG actor = null;
	
	public XcaRepository() {
		actor = XdsFactory.getIGActor();
		if (actor == null) {
			logger.fatal("Cannot find XcaIG actor configuration.");
		}
	}

	protected void validateRequest(OMElement rdsr, RegistryErrorList rel)
	throws XdsValidationException {
		
		verifyHomeOnRetrieve(rdsr, rel, Ig.home);
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
			else if ( !home_att.equals(home) && ! actor.getRGRetrieveClientConnections().keySet().contains(home_att) )
				rel.add_error(MetadataSupport.XDSUnknownCommunity, "Do not understand homeCommunityId " + home_att , null, log_message);
		}
	}

	protected OMElement processRetrieveDocumentSet(RetrieveDocumentSet s, OMElement rdsr) throws XdsException, LoggerException {
		Map<String, List<OMElement>> requestByHome = new HashMap<String, List<OMElement>>(); 
		
		//Sort and group doc requests by homeId
		List<OMElement> doc_requests = MetadataSupport.decendentsWithLocalName(rdsr, "DocumentRequest");
		for (OMElement doc_request : doc_requests) {
			String home_att = MetadataSupport.firstChildWithLocalName(doc_request, "HomeCommunityId").getText();
			List<OMElement> requests = requestByHome.get(home_att);
			if (requests == null ) {
				requests = new ArrayList<OMElement>();
				requestByHome.put(home_att, requests);
			}
			OMElement request2 = Util.deep_copy(doc_request);
			requests.add(request2);
		}
		
		Set<String> requestHomeIds = requestByHome.keySet();
		Aggregator ag = new RetrieveAggregator(requestHomeIds, log_message);
		
		for(String requestHomeId :requestHomeIds) {
			List<OMElement> requests = requestByHome.get( requestHomeId );
			OMElement rootRequest = null;	
			for (OMElement request : requests) {
				rootRequest = MetadataSupport.createElement("RetrieveDocumentSetRequest", MetadataSupport.xdsB);
				rootRequest.addChild(request);
			}
			if (requestHomeId.equals(Ig.home)) {
				//send to the local community repository
				SoapCall sc = new SoapCall(actor.getRepositoryClientConnection(), rootRequest,  "urn:ihe:iti:2007:RetrieveDocumentSet", requestHomeId, true /*mtom*/, ag, this);
				Ig.exec.execute( sc );
			} else {
				//forward to the gateway
				IConnectionDescription rgConnection =  actor.getRGRetrieveClientConnections().get(requestHomeId);
				SoapCall sc = new SoapCall(rgConnection, rootRequest,  "urn:ihe:iti:2007:CrossGatewayRetrieve", requestHomeId, true/*mtom*/, ag, this);
				Ig.exec.execute( sc );
			}			
		}
		//wait for and aggregate responses	
		ag.waitForAll();

		RetrieveDocumentSetResponse response = ((RetrieveAggregator)ag).getRetrieveDocumentSetResponse();

		log_response(response.getRegistryResponse());

		return response.getResponse();
	}
	
}
