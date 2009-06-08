package gov.nist.registry.common2.exception;

public class HttpCodeException extends XdsException {
	
	public HttpCodeException(String msg) {
		super(msg);
	}

	public HttpCodeException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
