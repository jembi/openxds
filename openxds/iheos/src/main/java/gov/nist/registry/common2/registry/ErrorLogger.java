package gov.nist.registry.common2.registry;

import org.openhealthtools.openexchange.syslog.LogMessage;



public interface ErrorLogger {

	public void add_warning(String code, String msg, String location, LogMessage log_message);
	public void add_error(String code, String msg, String location, LogMessage log_message);

	
}
