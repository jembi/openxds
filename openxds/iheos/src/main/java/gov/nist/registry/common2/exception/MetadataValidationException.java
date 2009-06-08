package gov.nist.registry.common2.exception;

public class MetadataValidationException extends XdsException {
	
	public MetadataValidationException(String msg) {
		super(msg);
	}

	public MetadataValidationException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
