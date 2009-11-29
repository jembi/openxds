package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.XdsWSException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.Validator;
import gov.nist.registry.common2.registry.XdsCommon;
import gov.nist.registry.ws.ProvideAndRegisterDocumentSet;

import org.apache.axiom.om.OMElement;

public class Test11971 extends RepositoryB {
	
	public Test11971() {
		super();
	}

	public OMElement RetrieveDocumentSetRequest(OMElement rdsr) {
		return start_up_error(rdsr, null, REPOSITORY_ACTOR, "Test does not implement this transaction");
	}

	public String getServiceName() {
		return "PnR.b 11971";
	}

	public boolean runContentValidationService(Metadata m, Response response)
	throws MetadataException {
		Validator v = new Validator(m);
		v.addDocToExistingFolder();
		String errs = v.getErrors();
		if (errs.length() > 0) {
			response.add_error(MetadataSupport.XDSRepositoryMetadataError, errs, "Test input incorrect", log_message);
			return false;
		}
		return true;
	}

	protected String getPnRTransactionName() {
		return "PnR.b 11971";
	}


}
