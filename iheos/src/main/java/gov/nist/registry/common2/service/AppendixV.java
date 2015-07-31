package gov.nist.registry.common2.service;

import gov.nist.registry.common2.exception.XdsFormatException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.exception.XdsWSException;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.ws.evs.Evs;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openxds.log.LogMessage;
import org.openhealthtools.openxds.log.LoggerException;

public abstract class AppendixV {
	private static final Log logger = LogFactory.getLog(AppendixV.class);

	abstract protected OMElement beginTransaction(String service_name, OMElement request, short actor);
	abstract protected OMElement endTransaction(OMElement request, Exception e, short actor, String message);
	abstract protected OMElement endTransaction(OMElement request, Exception e, short actor, String message, String error_type);
	abstract protected void endTransaction(boolean status);
	
	
	protected LogMessage log_message;

	protected MessageContext return_message_context = null;
	
	public static short REGISTRY_ACTOR = 1;
	public static short REPOSITORY_ACTOR = 2;
	public static short BROKER_ACTOR = 3;
	
	protected void mustBeSimpleSoap() throws XdsFormatException {
		if (getMessageContext().isDoingMTOM())
			throw new XdsFormatException("This transaction must use SIMPLE SOAP, MTOM found");
	}
	
	protected void mustBeMTOM() throws XdsFormatException {
		if ( !getMessageContext().isDoingMTOM())
			throw new XdsFormatException("This transaction must use MTOM, SIMPLE SOAP found");
	}
	
	protected void checkAction(String actionValue) throws XdsWSException {
		String action = getMessageContext().getWSAAction();
		if (action == null ||
				!action.equals(actionValue))
			throw new XdsWSException("Wrong WS:Action received: found " + action + " but " + actionValue + " is required");
	}
	
	protected void setResponseAction(String action) {
		getMessageContext().setWSAAction(action);
	}
	
	protected void checkSOAP12() throws XdsWSException {
		
		if (MessageContext.getCurrentMessageContext().isSOAP11()) {
			throwFault("SOAP 1.1 not supported");
		}
		SOAPEnvelope env = MessageContext.getCurrentMessageContext().getEnvelope();
		if (env == null)
			throwFault("No SOAP envelope found");
		SOAPHeader hdr = env.getHeader();
		if (hdr == null)
			throwFault("No SOAP header found");
		if ( !hdr.getChildrenWithName(new QName("http://www.w3.org/2005/08/addressing","Action")).hasNext()) {
			throwFault("WS-Action required in header");
		}
	
	}
	protected void checkSOAP11() throws XdsWSException {
	
		if ( !MessageContext.getCurrentMessageContext().isSOAP11()) {
			throwFault("SOAP 1.2 not supported");
		}
		SOAPEnvelope env = MessageContext.getCurrentMessageContext().getEnvelope();
		if (env == null)
			throwFault("No SOAP envelope found");
	}
	protected void checkSOAPAny() throws XdsWSException {
		if ( MessageContext.getCurrentMessageContext().isSOAP11())
			checkSOAP11();
		else
			checkSOAP12();
	}
	protected boolean isAsync() {
		MessageContext mc = getMessageContext();
		return 
		mc.getMessageID() != null && 
		!mc.getMessageID().equals("") &&
		mc.getReplyTo() != null &&
		!mc.getReplyTo().hasAnonymousAddress();
	}
	boolean isSync() {
		return !isAsync();
	}
	
	public MessageContext getMessageContext() {
		return MessageContext.getCurrentMessageContext();
	}

	public void throwFault(String msg) throws XdsWSException {
		try {
			if (log_message != null) {
				log_message.addErrorParam("SOAPError", msg);
				log_message.addOtherParam("Response", "SOAPFault: " + msg);
				//endTransaction(false);
			}
		} catch (Exception e) {}
		throw new XdsWSException(msg);
	}
	public void setReturnMessageContext(MessageContext return_context) {
		this.return_message_context = return_context;
	}
	public void setMessageContextIn(MessageContext inMessage) {
		//currentMessageContext = inMessage ;
	}

	protected void log_response(Response response)  {
		
//		generateAuditLog(response);
		
		if (log_message == null) {
			logger.fatal("\nFATAL ERROR: AppendixV.log_response(): log_message is null\n");
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
			logger.error("**************ERROR: Logger exception attempting to return to user");
		}
		catch (XdsInternalException e) {
			logger.error("**************ERROR: Internal exception attempting to return to user");
		}
	}

}
