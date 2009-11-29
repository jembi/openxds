package gov.nist.registry.common2.testkit;

public class NotALogFileException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotALogFileException(String msg) {
		super(msg);
	}
	
	public NotALogFileException(String msg, Exception e) {
		super(msg, e);
	}
	
}
