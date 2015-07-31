package gov.nist.registry.ws.serviceclasses;

import org.apache.axiom.om.OMElement;

public class RGnoopt extends RG {

	public OMElement RetrieveDocumentSetRequest(OMElement rdsr) {
		setOptimize(false);
		return super.RetrieveDocumentSetRequest(rdsr);
	}



}
