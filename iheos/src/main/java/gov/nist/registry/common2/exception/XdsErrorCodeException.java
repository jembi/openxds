package gov.nist.registry.common2.exception;

public class XdsErrorCodeException extends XdsValidationException {
	String errorCode;

	public XdsErrorCodeException(String reason, String errorCode) {
		super(reason);
		this.errorCode = errorCode;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	

}
