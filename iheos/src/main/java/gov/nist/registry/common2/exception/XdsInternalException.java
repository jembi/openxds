package gov.nist.registry.common2.exception;

public class XdsInternalException extends XdsException {
	
	public XdsInternalException(String reason) {
		super(reason);
	}


	public XdsInternalException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
