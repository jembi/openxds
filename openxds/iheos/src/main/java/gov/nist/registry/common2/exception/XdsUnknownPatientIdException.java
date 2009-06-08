package gov.nist.registry.common2.exception;

public class XdsUnknownPatientIdException extends XdsException {
	
	public XdsUnknownPatientIdException(String message) {
		super(message);
	}

	public XdsUnknownPatientIdException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
