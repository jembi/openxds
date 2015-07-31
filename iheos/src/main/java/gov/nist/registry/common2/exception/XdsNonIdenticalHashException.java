package gov.nist.registry.common2.exception;

public class XdsNonIdenticalHashException extends MetadataValidationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public XdsNonIdenticalHashException(String msg) {
		super(msg);
	}

	public XdsNonIdenticalHashException(String msg, Throwable cause) {
		super(msg, cause);
	}


}
