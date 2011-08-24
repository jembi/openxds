package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.XdsCommon;
import gov.nist.registry.common2.service.AppendixV;
import gov.nist.registry.ws.ContentValidationService;
import gov.nist.registry.ws.SubmitObjectsRequest;

import org.apache.axiom.om.OMElement;

abstract public class AbstractRegistryA extends XdsService implements
		ContentValidationService {

	//RegisterDocumentSetRequest
	public OMElement SubmitObjectsRequest(OMElement sor) {
		try {
			OMElement startup_error = beginTransaction("SOR.a", sor, AppendixV.REGISTRY_ACTOR);
			if (startup_error != null)
				return startup_error;
			
			String service_label = this.getServiceName();
			log_message.setTestMessage(service_label);
			
			SubmitObjectsRequest s = new SubmitObjectsRequest(log_message, XdsCommon.xds_a, getMessageContext());
			
			s.setContentValidationService(this);
			OMElement result = s.submitObjectsRequest(sor);
			
			endTransaction(s.getStatus());
			return result;
		} catch (Exception e) {
			System.out.println("Exception: " + exception_details(e));
			endTransaction(false);
			return null;
		}
	}


	abstract public boolean runContentValidationService(Metadata m, Response response) throws MetadataException;
	
	abstract public String getServiceName();

	abstract public OMElement AdhocQueryRequest(OMElement ahqr);

}
