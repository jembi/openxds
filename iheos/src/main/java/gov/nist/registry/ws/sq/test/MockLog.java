package gov.nist.registry.ws.sq.test;

import gov.nist.registry.common2.exception.ExceptionUtil;

import java.sql.Timestamp;

import org.openhealthtools.openxds.log.LogMessage;
import org.openhealthtools.openxds.log.LoggerException;

public class MockLog implements LogMessage {

	public void addErrorParam(String name, String value) throws LoggerException {
		try {
			throw new Exception("");
		} catch (Exception e) {
			System.out.println("ERROR: " + 
					name + " = " + value +
			"\nLogged From\n" +
			ExceptionUtil.stack_trace(e, 7));
		}
	}

	public void addHTTPParam(String name, String value) throws LoggerException {
		// TODO Auto-generated method stub

	}

	public void addOtherParam(String name, String value) throws LoggerException {
		try {
			throw new Exception("");
		} catch (Exception e) {
			System.out.println("OTHER: " + 
					name + " = " + value +
			"\nLogged From\n" +
			ExceptionUtil.stack_trace(e, 7));
		}
	}

	public void addParam(String tableName, String name, String value)
	throws LoggerException {
		// TODO Auto-generated method stub

	}

	public void addSoapParam(String name, String value) throws LoggerException {
		// TODO Auto-generated method stub

	}

	public void setCompany(String companyName) throws LoggerException {
		// TODO Auto-generated method stub

	}

	public void setIP(String ip) throws LoggerException {
		// TODO Auto-generated method stub

	}

	public void setPass(boolean pass) {
		// TODO Auto-generated method stub

	}

	public void setSecure(boolean isSecure) {
		// TODO Auto-generated method stub

	}

	public void setTestMessage(String testMessage) {
		// TODO Auto-generated method stub

	}

	public void setTimeStamp(Timestamp timestamp) {
		// TODO Auto-generated method stub

	}

	public String getMessageID() {
		// TODO Auto-generated method stub
		return null;
	}

	public void writeMessage(LogMessage x) {
		// TODO Auto-generated method stub
		
	}

	public void writeMessage() throws LoggerException {
		// TODO Auto-generated method stub
		
	}

}
