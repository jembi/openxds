package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.XdsValidationException;
import gov.nist.registry.common2.exception.XdsWSException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.XdsCommon;
import gov.nist.registry.ws.StoredQueryRequestSoapValidator;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;

public class RegistryA  extends AbstractRegistry  {

	public boolean runContentValidationService(Metadata m, Response response) {
		return true;
	}

	public String getServiceName() {
		return "R.a";
	}

	protected short getXdsVersion() {
		return XdsCommon.xds_a;
	}

	protected String getRTransactionName(OMElement ahqr) {
		return super.getRTransactionName(ahqr) + ".a";
	}



	protected void validateQueryTransaction(OMElement ahqr) throws XdsValidationException {
		OMNamespace ns = ahqr.getNamespace();
		String ns_uri =  ns.getNamespaceURI();
		if (ns_uri == null || ! 
				( ns_uri.equals(MetadataSupport.ebQns3.getNamespaceURI())  ||
						ns_uri.equals(MetadataSupport.ebQns2.getNamespaceURI())	 )) {
			throw new XdsValidationException("Invalid namespace on AdhocQueryRequest (" + ns_uri + ")");
		}


		String type = getRTransactionName(ahqr);


		if (type.startsWith("Unknown")) 
			throw new XdsValidationException("WS:Action indicates a Query, neither AdhocQuery or SQLQuery request found");
		if (type.startsWith("SubmitObjectsRequest")) 
			throw new XdsValidationException("WS:Action indicates a Query, SubmitObjectsRequest found");
		if (type.startsWith("SQL") &&
				!ns_uri.equals(MetadataSupport.ebQns2.getNamespaceURI()))
			throw new XdsValidationException("SQL query must have namespace " + MetadataSupport.ebQns2.getNamespaceURI());

		if (type.startsWith("SQ"))
			new StoredQueryRequestSoapValidator(getXdsVersion(), getMessageContext()).runWithException();
	}

	protected void validateWS(boolean isSQ) throws XdsWSException {
		if (isSQ)
			checkSOAPAny();
		else
			checkSOAP11();

		if (isAsync())
			throw new XdsWSException("Asynchronous web service request not acceptable on this endpoint");

	}

	protected void validateSubmitTransaction(OMElement sor)
	throws XdsValidationException {
		OMNamespace ns = sor.getNamespace();
		String ns_uri =  ns.getNamespaceURI();
		if (ns_uri == null || ! ns_uri.equals(MetadataSupport.ebRSns2.getNamespaceURI())) {
			throw new XdsValidationException("Invalid namespace on SubmitObjectsRequest (" + ns_uri + ")");
		}

	}

	protected void validateQueryInputDecoration(OMElement sor,
			gov.nist.registry.ws.AdhocQueryRequest a)
			throws XdsValidationException {
	}

	protected void decorateQueryOutput(OMElement sor,
			gov.nist.registry.ws.AdhocQueryRequest a, OMElement result)
			throws XdsValidationException {
	}


}
