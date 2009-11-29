package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.Validator;

import org.apache.axiom.om.OMElement;

public class Test11827 extends AbstractRegistryA {

	public OMElement AdhocQueryRequest(OMElement ahqr) {
		return start_up_error(ahqr, null, REGISTRY_ACTOR, "Test does not implement this transaction");
	}

	public String getServiceName() {
		return "R.a 11827";
	}

	public boolean runContentValidationService(Metadata m, Response response)
			throws MetadataException {
		Validator v = new Validator(m);
		v.ss1Doc();
		String errs = v.getErrors();
		if (errs.length() > 0) {
			response.add_error(MetadataSupport.XDSRegistryMetadataError, errs, "Test input incorrect", log_message);
			return false;
		}
		OMElement eo = m.getExtrinsicObject(0);
		String size = m.getSlotValue(eo, "size", 0);
		String hash = m.getSlotValue(eo, "hash", 0);
		hash = hash.toLowerCase();
		errs = "";
		//String expected_hash = "fbe2351a6a8ceba1a04ba3f832a12a53befeb04c";
		String expected_hash = "e543712c0e10501972de13a5bfcbe826c49feb75";
		if (size == null || Integer.parseInt(size) != 36) 
			errs = errs + "Size is wrong for this test, must be 36 (found " + size + ")\n";
		if ( hash == null|| !hash.equals(expected_hash)) 
			errs = errs + "Hash is wrong for this test, must be " + expected_hash + " (found " + hash + ")\n";
		if (errs.length() > 0) {
			response.add_error(MetadataSupport.XDSRegistryMetadataError, errs, "Test input incorrect", log_message);
			return false;
		}
		return true;
	}

}
