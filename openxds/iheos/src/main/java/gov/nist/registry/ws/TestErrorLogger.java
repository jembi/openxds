package gov.nist.registry.ws;

import gov.nist.registry.common2.registry.ErrorLogger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.syslog.LogMessage;

public class TestErrorLogger implements ErrorLogger {
	private final static Log logger = LogFactory.getLog(TestErrorLogger.class);


	public void add_error(String code, String msg, String location,
			LogMessage log_message) {
		System.out.println("ERROR: " + msg);
		logger.fatal(msg + "\n" + location);
	}

	public void add_warning(String code, String msg, String location,
			LogMessage log_message) {
		System.out.println("WARNING: " + msg);
		logger.fatal(msg + "\n" + location);
	}

}
