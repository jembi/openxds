package gov.nist.registry.common2.exception;

public class XdsPatientIdDoesNotMatchException extends XdsException {

	public XdsPatientIdDoesNotMatchException(String message) {
		super(message);
	}

	public XdsPatientIdDoesNotMatchException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
