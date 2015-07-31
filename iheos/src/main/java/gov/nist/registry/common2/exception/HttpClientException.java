package gov.nist.registry.common2.exception;

public class HttpClientException extends XdsException {
	
	public HttpClientException(String msg) {
		super(msg);
	}

	public HttpClientException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
