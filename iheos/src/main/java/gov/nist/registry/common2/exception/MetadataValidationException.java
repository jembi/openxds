package gov.nist.registry.common2.exception;

public class MetadataValidationException extends MetadataException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MetadataValidationException(String msg) {
		super(msg);
	}

	public MetadataValidationException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
