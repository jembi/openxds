package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.XdsCommon;
import gov.nist.registry.ws.ContentValidationService;
import gov.nist.registry.ws.ProvideAndRegisterDocumentSet;
import gov.nist.registry.ws.RetrieveDocumentSet;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;

public class RepositoryBopt extends XdsService implements ContentValidationService  {

	public OMElement ProvideAndRegisterDocumentSetRequest(OMElement sor) {
		try {
			OMElement startup_error = beginTransaction("PnR.b", sor, XdsService.repository_actor);
			if (startup_error != null)
				return startup_error;

			log_message.setTestMessage("PnR.b");

			ProvideAndRegisterDocumentSet s = new ProvideAndRegisterDocumentSet(log_message, XdsCommon.xds_b, getMessageContext());
			
			OMElement result = s.provideAndRegisterDocumentSet(sor, this);

			endTransaction(s.getStatus());
			return result;
		} catch (Exception e) {
			return endTransaction(sor, e, XdsService.repository_actor, "");
		}
	}

	public OMElement RetrieveDocumentSetRequest(OMElement rdsr) {
		try {
			OMElement startup_error = beginTransaction("RET.b", rdsr, XdsService.repository_actor);
			if (startup_error != null)
				return startup_error;
			log_message.setTestMessage("RET.b");

			OMNamespace ns = rdsr.getNamespace();
			String ns_uri =  ns.getNamespaceURI();
			if (ns_uri == null || ! ns_uri.equals(MetadataSupport.xdsB.getNamespaceURI())) {
				OMElement res = this.start_up_error(rdsr, "RepositoryB.java", XdsService.repository_actor, "Invalid namespace on RetrieveDocumentSetRequest (" + ns_uri + ")", true);
				endTransaction(false);
				return res;
			}

			RetrieveDocumentSet s = new RetrieveDocumentSet(log_message, XdsCommon.xds_b, getMessageContext());
			
			OMElement result = s.retrieveDocumentSet(rdsr, this, true /* optimize */, this);
			endTransaction(s.getStatus());
			return result;
		} catch (Exception e) {
			return endTransaction(rdsr, e, XdsService.repository_actor, "");
		}
	}

	public boolean runContentValidationService(Metadata m, Response response) {
		return true;
	}


}
