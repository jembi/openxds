package gov.nist.registry.common2.registry;

import gov.nist.registry.common2.exception.XdsInternalException;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;

public class RetrieveDocumentSetResponse {
	RegistryResponse rr;
	List<OMElement> documentResponses = new ArrayList<OMElement>();
	
	public RetrieveDocumentSetResponse(RegistryResponse rr) {
		this.rr = rr;
	}
	
	public OMElement getResponse() throws XdsInternalException {
		OMElement response = MetadataSupport.om_factory.createOMElement(new QName(MetadataSupport.xdsB_uri, "RetrieveDocumentSetResponse"));
		
		response.addChild(rr.getResponse());
		
		//Add DocumentResponse
		for(OMElement docResponse : documentResponses) {
			response.addChild(docResponse);		
		}
		
		System.out.println("response is \n" + response.toString());
	
		return response;
	}
	
	public void addDocumentResponse(OMElement docResponse) {
		documentResponses.add( docResponse );
	}

}
