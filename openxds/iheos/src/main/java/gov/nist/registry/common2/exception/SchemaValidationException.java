package gov.nist.registry.common2.exception;

public class SchemaValidationException extends XdsException {
	
	public SchemaValidationException(String msg) {
		super(msg);
	}

	public SchemaValidationException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
