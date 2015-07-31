package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.exception.XdsValidationException;
import gov.nist.registry.common2.exception.XdsWSException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.Response;

import org.apache.axiom.om.OMElement;

public class RG extends RGAbstract {
	
	public RG() {
		super();
	}

	public boolean runContentValidationService(Metadata request,
			Response response) {
		return true;
	}
	
	public OMElement AdhocQueryRequest(OMElement ahqr) {
		return AdhocQueryRequest(ahqr, "XCA", "XGQ");
	}

	public OMElement RetrieveDocumentSetRequest(OMElement rdsr) {
		OMElement result = super.RetrieveDocumentSetRequest(rdsr);
		return result;
	}

	protected void validateQueryTransaction(OMElement sor)
			throws XdsValidationException, MetadataValidationException, XdsInternalException {
		new RegistryB().validateQueryTransaction(sor);
	}

	protected void validateRetrieveTransaction(OMElement sor)
			throws XdsValidationException {
		new RepositoryB().validateRetTransaction(sor);
	}

	protected void validateWS() throws XdsWSException {
		new RegistryB().validateWS(true);
	}

	protected String getQueryTransactionName() {
		return "XGQ";
	}

	protected String getRetTransactionName() {
		return "XGR";
	}

}
