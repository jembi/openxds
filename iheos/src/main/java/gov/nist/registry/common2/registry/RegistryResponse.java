package gov.nist.registry.common2.registry;

import gov.nist.registry.common2.exception.XdsInternalException;

import org.apache.axiom.om.OMElement;

public class RegistryResponse extends Response {
	
	public RegistryResponse(short version) throws XdsInternalException {
		super(version);
		response = MetadataSupport.om_factory.createOMElement("RegistryResponse", ebRSns);
	}
	
	public RegistryResponse(short version, RegistryErrorList rel) throws XdsInternalException {
		super(version, rel);
		response = MetadataSupport.om_factory.createOMElement("RegistryResponse", ebRSns);
	}
	
	public void addQueryResults(OMElement metadata) {
		
	}
	
	public OMElement getRoot() { return response; }

}
