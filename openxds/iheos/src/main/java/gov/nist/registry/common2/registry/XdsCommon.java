package gov.nist.registry.common2.registry;

import gov.nist.registry.common2.exception.ExceptionUtil;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.logging.LogMessage;
import gov.nist.registry.common2.logging.LoggerException;

import org.apache.axis2.context.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class XdsCommon  {

	public Response response = null;
	public LogMessage log_message = null;
	public static final short xds_none = 0;
	public static final short xds_a = 2;
	public static final short xds_b = 3;
	public short xds_version = xds_none;
	MessageContext messageContext = null;
	boolean isXCA = false;
	private final static Log logger = LogFactory.getLog(XdsCommon.class);
	
	public static final short UNKNOWN_transaction = 0;
	public static final short PR_transaction = 1;
	public static final short R_transaction = 2;
	public static final short SQ_transaction = 3;
	public static final short RET_transaction = 4;
	public static final short OTHER_transaction = 5;
	public short transaction_type = UNKNOWN_transaction;

	public MessageContext getMessageContext() {
		return messageContext;
	}
	
	public void setIsXCA() {
		isXCA = true;
		if (response != null)
			response.setIsXCA();
	}
	
	public void init(Response response, short xds_version, MessageContext messageContext) throws XdsInternalException {
		if (transaction_type == UNKNOWN_transaction)
			throw new XdsInternalException("transaction_type is UNKNOWN");
		this.response = response;
		if (isXCA)
			response.setIsXCA();
		this.xds_version = xds_version;
		this.messageContext = messageContext;

	}
	
	public boolean getStatus() {
		return ! response.has_errors();
	}

	protected void log_status() {
		try {
			String e_and_w = response.getErrorsAndWarnings();
			if (e_and_w != null && !e_and_w.equals(""))
				log_message.addErrorParam("Error", e_and_w);
		} catch (Exception e) {
			response.error("Internal Error: cannot set final status in test log on transaction");
		}
	}

	protected void init_log() throws LoggerException {
	}


	public static String logger_exception_details(Exception e) {
		if (e == null) 
			return "";

		return e.getClass().getName() + "  " + e.getMessage() + ExceptionUtil.exception_details(e, 10);
	}

	protected void log_response()  {
		
		generateAuditLog(response);
		
		if (log_message == null) {
			System.out.println("\nFATAL ERROR: XdsCommon.log_response(): log_message is null\n");
			return;
		}
		try {
			if (response.has_errors()) {
				log_message.setPass(false);
				log_message.addErrorParam("Errors", response.getErrorsAndWarnings());
			} else
				log_message.setPass(true);

			log_message.addOtherParam("Response", response.getResponse().toString());
		}
		catch (LoggerException e) {
			System.out.println("**************ERROR: Logger exception attempting to return to user");
		}
		catch (XdsInternalException e) {
			System.out.println("**************ERROR: Internal exception attempting to return to user");
		}
	}
	
	protected void generateAuditLog(Response response)  {

	}
	
	protected void generateAuditLog(Metadata m) {
		
	}

}
