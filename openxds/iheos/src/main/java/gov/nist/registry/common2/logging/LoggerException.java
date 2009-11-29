package gov.nist.registry.common2.logging;

public class LoggerException extends Exception 
{
	
	public LoggerException(String string)
	{
		super(string);
	}
	
	public LoggerException(String string, Exception e) {
		super(string, e);
	}
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**TODO :
	 *   Define exception numbers 
	 */

}
