package gov.nist.registry.common2.exception;

public class XdsResultNotSinglePatientException extends XdsException {

	public XdsResultNotSinglePatientException(String reason) {
		super(reason);
	}


	public XdsResultNotSinglePatientException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
