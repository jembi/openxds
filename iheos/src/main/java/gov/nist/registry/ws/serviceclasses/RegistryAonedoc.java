package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.Validator;

import org.apache.axiom.om.OMElement;

public class RegistryAonedoc extends AbstractRegistryA {

	@Override
	public String getServiceName() {
		return "R.a 1Doc";
	}

	@Override
	public boolean runContentValidationService(Metadata m, Response response)
	throws MetadataException {
		Validator v = new Validator(m);
		v.ss1Doc();
		String errs = v.getErrors();
		if (errs.length() > 0) {
			response.add_error(MetadataSupport.XDSRegistryMetadataError, errs, "Test input incorrect", log_message);
			return false;
		}
		return true;
	}

	public OMElement AdhocQueryRequest(OMElement ahqr) {
		return start_up_error(ahqr, null, REGISTRY_ACTOR, "Test does not implement this transaction");
	}

}
