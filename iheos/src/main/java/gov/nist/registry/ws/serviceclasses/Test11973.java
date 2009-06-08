package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.Validator;

public class Test11973 extends RepositoryB {

	public Test11973() {
		super();
	}

	public String getServiceName() {
		return "PnR.b 11973";
	}

	public boolean runContentValidationService(Metadata m, Response response)
	throws MetadataException {
		Validator v = new Validator(m);
		v.addExistingDocToExistingFolder();
		String errs = v.getErrors();
		if (errs.length() > 0) {
			response.add_error(MetadataSupport.XDSRepositoryMetadataError, errs, "Test input incorrect", log_message);
			return false;
		}
		return true;
	}


	protected String getPnRTransactionName() {
		return "PnR.b 11973";
	}


}
