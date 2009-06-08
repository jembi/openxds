package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.XdsWSException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.Validator;
import gov.nist.registry.common2.registry.XdsCommon;

public class Test11966a extends RepositoryB {
	
	public Test11966a() {
		super();
		setAlternateRegistryEndpoint("http://localhost:9080/" + technicalFramework + "/services/test11966");
	}

	public String getServiceName() {
		return "PnR.b 11966a";
	}

	public boolean runContentValidationService(Metadata m, Response response)
	throws MetadataException {
		Validator v = new Validator(m);
		v.ss1Doc();
		String errs = v.getErrors();
		if (errs.length() > 0) {
			response.add_error(MetadataSupport.XDSRepositoryMetadataError, errs, "Test input incorrect", log_message);
			return false;
		}
		return true;
	}


	protected String getPnRTransactionName() {
		return "PnR.b 11966a";
	}

}
