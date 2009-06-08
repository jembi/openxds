package gov.nist.registry.common2.registry;

import gov.nist.registry.common2.exception.XdsInternalException;

import org.apache.axiom.om.OMElement;

public class RetrieveMultipleResponse extends Response {
	OMElement rdsr = null;
	
	public OMElement getRoot() { return rdsr; }

	public RetrieveMultipleResponse() throws XdsInternalException {
		super();
		response = MetadataSupport.om_factory.createOMElement("RegistryResponse", ebRSns);
		rdsr = MetadataSupport.om_factory.createOMElement("RetrieveDocumentSetResponse", MetadataSupport.xdsB);
		rdsr.addChild(response);
	}
	
	public RetrieveMultipleResponse(RegistryErrorList rel) throws XdsInternalException {
		super(rel);
		response = MetadataSupport.om_factory.createOMElement("RegistryResponse", ebRSns);
		rdsr = MetadataSupport.om_factory.createOMElement("RetrieveDocumentSetResponse", MetadataSupport.xdsB);
		rdsr.addChild(response);
	}
	
	public void addQueryResults(OMElement metadata) {
		
	}


}
