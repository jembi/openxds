package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.XdsCommon;
import gov.nist.registry.common2.service.AppendixV;
import gov.nist.registry.ws.ContentValidationService;
import gov.nist.registry.ws.ProvideAndRegisterDocumentSet;

import org.apache.axiom.om.OMElement;

abstract public class AbstractRepositoryA extends XdsService implements ContentValidationService {
	
	public AbstractRepositoryA() {
		super();
	}

	public OMElement SubmitObjectsRequest(OMElement sor) {
		try {
			OMElement startup_error = beginTransaction("PnR.a", sor, AppendixV.REPOSITORY_ACTOR);
			if (startup_error != null)
				return startup_error;
			
			String service_label = this.getServiceName();
			if (log_message != null)
				log_message.setTestMessage(service_label);
			
			ProvideAndRegisterDocumentSet s = new ProvideAndRegisterDocumentSet(log_message, XdsCommon.xds_a, getMessageContext());
			OMElement result = s.provideAndRegisterDocumentSet(sor, this);
			
			endTransaction(s.getStatus());
			return result;
		} catch (Exception e) {
			endTransaction(false);
			System.out.println("Exception: " + exception_details(e));
			return null;
		}
	}

	abstract public boolean runContentValidationService(Metadata m, Response response) throws MetadataException;

	abstract public OMElement RetrieveDocumentSetRequest(OMElement rdsr);
	
	abstract public String getServiceName();
	
	
}
