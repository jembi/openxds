package gov.nist.registry.ws.serviceclasses;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.MessageContext;

public class RGForceError extends RG {

	public OMElement AdhocQueryRequest(OMElement ahqr) {
		String errorCode = (String) MessageContext.getCurrentMessageContext().getParameter("forcedError").getValue();
		String errorMessage = (String) MessageContext.getCurrentMessageContext().getParameter("forcedErrorMessage").getValue();
		
		return AdhocQueryRequestForceError(ahqr, "XCA", "XGQ", errorCode, errorMessage);
	}

	public OMElement RetrieveDocumentSetRequest(OMElement rdsr) {
		String errorCode = (String) MessageContext.getCurrentMessageContext().getParameter("forcedError").getValue();
		String errorMessage = (String) MessageContext.getCurrentMessageContext().getParameter("forcedErrorMessage").getValue();
		
		return RetrieveDocumentForceError(rdsr, errorCode, errorMessage);
	}
}
	