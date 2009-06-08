package gov.nist.registry.common2.registry;

import gov.nist.registry.xdslog.Message;

public abstract class ErrorLogger {

	public abstract void add_warning(String code, String msg, String location, Message log_message);
	public abstract void add_error(String code, String msg, String location, Message log_message);

	
}
