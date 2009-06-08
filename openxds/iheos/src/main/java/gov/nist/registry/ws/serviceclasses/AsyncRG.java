package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.XdsWSException;

public class AsyncRG extends RG {

	protected void validateWS() throws XdsWSException {
		checkSOAP12();
		if (!isAsync())
			throw new XdsWSException("Asynchronous web service request required on this endpoint" + 
					" - replyTo is " + getMessageContext().getReplyTo().getAddress());
	}

	protected String getQueryTransactionName() {
		return "XGQ ASync";
	}

	protected String getRetTransactionName() {
		return "XGR ASync";
	}

}
