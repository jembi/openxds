package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.Validator;
import gov.nist.registry.common2.registry.XdsCommon;
import gov.nist.registry.common2.service.AppendixV;
import gov.nist.registry.ws.ProvideAndRegisterDocumentSet;

import org.apache.axiom.om.OMElement;

public class Test11748  extends AbstractRepositoryA {

	public OMElement RetrieveDocumentSetRequest(OMElement rdsr) {
		return start_up_error(rdsr, null, REPOSITORY_ACTOR, "Test does not implement this transaction");
	}

	public String getServiceName() {
		return "PnR.a 11748";
	}

	public OMElement SubmitObjectsRequest(OMElement sor) {
		try {
			OMElement startup_error = beginTransaction("11748", sor, AppendixV.REPOSITORY_ACTOR);
			if (startup_error != null)
				return startup_error;
			
			String service_label = this.getServiceName();
			log_message.setTestMessage(service_label);
			
			ProvideAndRegisterDocumentSet s = new ProvideAndRegisterDocumentSet(log_message, XdsCommon.xds_a, getMessageContext());
			
			s.setRegistryEndPoint("http://localhost:9080/" + technicalFramework + "/services/xdsregistryainternal");
			
			OMElement result = s.provideAndRegisterDocumentSet(sor, this);
			
			endTransaction(s.getStatus());
			return result;
		} catch (Exception e) {
			System.out.println("Exception: " + exception_details(e));
			endTransaction(false);
			return null;
		}
	}

	public boolean runContentValidationService(Metadata m, Response response)
	throws MetadataException {
		Validator v = new Validator(m);
		v.replaceDocument();
		String errs = v.getErrors();
		if (errs.length() > 0) {
			response.add_error(MetadataSupport.XDSRepositoryMetadataError, errs, "Test input incorrect", log_message);
			return false;
		}
		return true;
	}


}
