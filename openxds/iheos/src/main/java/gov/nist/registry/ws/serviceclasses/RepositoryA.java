package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.XdsValidationException;
import gov.nist.registry.common2.exception.XdsWSException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.RegistryErrorList;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.XdsCommon;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;

public class RepositoryA extends AbstractRepository {

	 public boolean runContentValidationService(Metadata m, Response response) throws MetadataException{
		 return true;
	 }

	 public OMElement DocumentRepository_RetrieveDocumentSet(OMElement rdsr) {
		 return null;
	 }
	 
	 public String getServiceName() {
		 return "PnR.a";
	 }

	protected String getPnRTransactionName() {
		return "PnR.a";
	}

	protected String getRetTransactionName() {
		return null;
	}

	protected short getXdsVersion() {
		return XdsCommon.xds_a;
	}

	protected void validateWS() throws XdsWSException {
	}

	protected void validatePnRTransaction(OMElement sor)
			throws XdsValidationException {
		OMNamespace ns = sor.getNamespace();
		String ns_uri =  ns.getNamespaceURI();
		if (ns_uri == null || ! ns_uri.equals(MetadataSupport.ebRSns2.getNamespaceURI())) 
			throw new XdsValidationException("Invalid namespace on " + sor.getLocalName() + " (" + ns_uri + ")");
	}

	// never used
	protected void validateRetTransaction(OMElement rds)
			throws XdsValidationException {
	}

	protected void validateRequest(OMElement rdsr, RegistryErrorList rel)
			throws XdsValidationException {
	}

}
