package gov.nist.registry.common2.service;

import gov.nist.registry.common2.exception.ExceptionUtil;
import gov.nist.registry.ws.evs.Evs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMException;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.RelatesTo;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.i18n.Messages;
import org.apache.axis2.receivers.AbstractInOutMessageReceiver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

abstract public class AbstractXDSRawXMLINoutMessageReceiver extends
AbstractInOutMessageReceiver {

	private static final Log logger = LogFactory.getLog(AbstractXDSRawXMLINoutMessageReceiver.class);

	abstract public void validate_action(MessageContext msgContext, MessageContext newmsgContext) throws Exception ;

	private Method findOperation(AxisOperation op, Class implClass) {

		Method method = (Method) (op.getParameterValue("myMethod"));

		if (method != null)
			return method;

		String methodName = op.getName().getLocalPart();

		try {

			// Looking for a method of the form "OMElement method(OMElement)"

			method = implClass.getMethod(methodName,
					new Class[] { OMElement.class });

			if (method.getReturnType().equals(OMElement.class)) {
				try {
					op.addParameter("myMethod", method);
				} catch (AxisFault axisFault) {
					// Do nothing here
				}
				return method;
			}

		} catch (NoSuchMethodException e) {
			// Fault through
		}
		return null;

	}

	/**
	 * Invokes the bussiness logic invocation on the service implementation class
	 *
	 * @param msgContext    the incoming message context
	 * @param newmsgContext the response message context
	 * @throws AxisFault on invalid method (wrong signature) or behaviour (return null)
	 */
	public void invokeBusinessLogic(MessageContext msgContext, MessageContext newmsgContext)
	throws AxisFault {

		Object obj = null;
		try {

			String in_action = msgContext.getWSAAction();

			if (logger.isDebugEnabled()) {
				logger.debug("in_action = " + in_action);
			}
			
			newmsgContext.setRelationships(new RelatesTo[] { new RelatesTo(msgContext.getMessageID()) });

			validate_action(msgContext, newmsgContext);  

			// get the implementation class for the Web Service

			obj = getTheImplementationObject(msgContext);


			// make return message context available to service
			if (obj instanceof AppendixV) {
				AppendixV xo = (AppendixV) obj;
				xo.setReturnMessageContext(newmsgContext);
			}


		} catch (Exception e) {

			logger.error("Error in XDSRawXMLInOut:\n" +  ExceptionUtil.exception_details(e) + "\ngetSoapAction = " + msgContext.getSoapAction());

			throw AxisFault.makeFault(e);

		}


		// find the WebService method

		Class implClass = obj.getClass();

		AxisOperation opDesc = msgContext.getAxisOperation();

		Method method = findOperation(opDesc, implClass);

		
		Method methodDisplay;
		try {
			methodDisplay = implClass.getMethod("setMessageContextIn",
					new Class[] { MessageContext.class });
		} catch (SecurityException e) {
			logger.error("Error in XDSRawXMLInOut:\n" +  
					"class is " + implClass.getName() + " method is setMessageContextIn" + 
					ExceptionUtil.exception_details(e) + "\ngetSoapAction = " + msgContext.getSoapAction());

			throw AxisFault.makeFault(e);
		} catch (NoSuchMethodException e) {
			logger.error("Error in XDSRawXMLInOut:\n" +  
					"class is " + implClass.getName() + " method is setMessageContextIn" + 
					ExceptionUtil.exception_details(e) + "\ngetSoapAction = " + msgContext.getSoapAction());

			throw AxisFault.makeFault(e);
		}

		try {
			methodDisplay.invoke(obj, new Object[] { msgContext });
		} 
		catch (Exception e) {
			logger.error("Error in XDSRawXMLInOut:\n" +  
					"class is " + implClass.getName() + " method is setMessageContextIn" + 
					ExceptionUtil.exception_details(e) + "\ngetSoapAction = " + msgContext.getSoapAction());

			throw AxisFault.makeFault(e);
		}

		if (method == null) {
			throw new AxisFault(Messages.getMessage(
					"methodDoesNotExistInOut",
					opDesc.getName().toString()));
		}

		OMElement result;
		try {
			result = (OMElement) method.invoke(

					obj, new Object[] { msgContext.getEnvelope().getBody()
							.getFirstElement() });
		} 
		catch (Exception e) {
			logger.error("Error in XDSRawXMLInOut:\n" +  
					"class is " + implClass.getName() + " method is setMessageContextIn" + 
					ExceptionUtil.exception_details(e) + "\ngetSoapAction = " + msgContext.getSoapAction());

			throw AxisFault.makeFault(e);
		}

		SOAPFactory fac = getSOAPFactory(msgContext);

		SOAPEnvelope envelope = fac.getDefaultEnvelope();

		if (result != null) {

			envelope.getBody().addChild(result);

		}

		newmsgContext.setEnvelope(envelope);






	}
}
