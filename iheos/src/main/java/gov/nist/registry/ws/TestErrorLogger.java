package gov.nist.registry.ws;

import gov.nist.registry.common2.registry.ErrorLogger;
import gov.nist.registry.xdslog.Message;

import org.apache.log4j.Logger;

public class TestErrorLogger extends ErrorLogger {
	private final static Logger logger = Logger.getLogger(TestErrorLogger.class);


	public void add_error(String code, String msg, String location,
			Message log_message) {
		System.out.println("ERROR: " + msg);
		logger.fatal(msg + "\n" + location);
	}

	public void add_warning(String code, String msg, String location,
			Message log_message) {
		System.out.println("WARNING: " + msg);
		logger.fatal(msg + "\n" + location);
	}

}
