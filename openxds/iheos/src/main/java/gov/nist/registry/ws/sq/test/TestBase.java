package gov.nist.registry.ws.sq.test;

import gov.nist.registry.common2.logging.LogMessage;
import gov.nist.registry.common2.logging.LoggerException;
import gov.nist.registry.common2.registry.ErrorLogger;

public class TestBase  implements ErrorLogger {
	protected LogMessage log = null;
	
	public TestBase() {
		this.log = new MockLog();
	}

	public void add_error(String code, String msg, String location, LogMessage log_message) {
		try {
			log.addErrorParam("ERROR", code + "\n\tMessage " + msg + "\n\tLocation " + location);
		} catch (LoggerException e) {
			System.out.println("LoggerException");
		}
	}

	public void add_warning(String code, String msg, String location,
			LogMessage log_message) {
				try {
					log.addErrorParam("WARNING", code + "\n\tMessage " + msg + "\n\tLocation " + location);
				} catch (LoggerException e) {
					System.out.println("LoggerException");
				}
			}

}
