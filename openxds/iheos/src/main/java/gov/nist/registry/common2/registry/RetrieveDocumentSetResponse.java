package gov.nist.registry.common2.registry;

import gov.nist.registry.common2.exception.XdsInternalException;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;

public class RetrieveDocumentSetResponse {
	RegistryResponse rr;
	
	public RetrieveDocumentSetResponse(RegistryResponse rr) {
		this.rr = rr;
	}
	
	public OMElement getResponse() throws XdsInternalException {
		OMElement response = MetadataSupport.om_factory.createOMElement(new QName(MetadataSupport.xdsB_uri, "RetrieveDocumentSetResponse"));
		
		response.addChild(rr.getResponse());
		
		System.out.println("response is \n" + response.toString());
	
		return response;
	}

}
