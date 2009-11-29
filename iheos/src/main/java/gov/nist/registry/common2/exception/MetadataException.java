package gov.nist.registry.common2.exception;

public class MetadataException extends XdsException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MetadataException(String msg) {
		super(msg);
	}

	public MetadataException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
