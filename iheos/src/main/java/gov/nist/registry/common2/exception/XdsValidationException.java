package gov.nist.registry.common2.exception;

public class XdsValidationException extends Exception {

	public XdsValidationException(String reason) {
		super(reason);
	}


	public XdsValidationException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
