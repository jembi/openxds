package gov.nist.registry.ws.serviceclasses;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;

public class Ig {

	public OMElement AdhocQueryRequest(OMElement ahqr) throws AxisFault {
		XcaRegistry reg = new XcaRegistry();
		
		return reg.AdhocQueryRequest(ahqr);
	}
	
	public OMElement RetrieveDocumentSetRequest(OMElement rdsr) throws AxisFault {
		XcaRepository rep = new XcaRepository();
		
		return rep.RetrieveDocumentSetRequest(rdsr);
	}
	
	public void setMessageContextIn(MessageContext inMessage) {
	}

}
