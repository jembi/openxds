package gov.nist.registry.ws.serviceclasses;

import org.apache.axiom.om.OMElement;

import gov.nist.registry.common2.exception.XdsWSException;

public class AsyncRegistryB extends RegistryB {

	protected void validateWS(boolean isSQ) throws XdsWSException {
		checkSOAP12();
		if (!isAsync())
			throw new XdsWSException("Asynchronous web service request required on this endpoint" + 
					" - replyTo is " + getMessageContext().getReplyTo().getAddress());
	}

	 protected String getRTransactionName(OMElement ahqr) {
		 return super.getRTransactionName(ahqr) + " ASync";
	 }


}
