package gov.nist.registry.xdslog;

public class LoggerException extends Exception 
{
   String message ;
   int    number  ;
	
	public LoggerException(String string)
	{
		 message = string ;
	}
	public String getMessage()
	{
		return ( message ) ;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**TODO :
	 *   Define exception numbers 
	 */

}
