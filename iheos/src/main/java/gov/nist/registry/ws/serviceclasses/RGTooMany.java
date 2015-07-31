package gov.nist.registry.ws.serviceclasses;

import org.apache.axiom.om.OMElement;

public class RGTooMany extends RG {

	public OMElement AdhocQueryRequest(OMElement ahqr) {
		return AdhocQueryRequestTooManyDocs(ahqr, "XCA", "XGQ");
	}


}
