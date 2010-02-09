package gov.nist.registry.common2.service;

import gov.nist.registry.common2.exception.ExceptionUtil;

import java.lang.reflect.Method;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.RelatesTo;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.i18n.Messages;
import org.apache.axis2.receivers.AbstractInMessageReceiver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

abstract public class AbstractXDSRawXMLInMessageReceiver extends
AbstractInMessageReceiver {

	private static final Log logger = LogFactory.getLog(AbstractXDSRawXMLInMessageReceiver.class);

	abstract public void validate_action(MessageContext msgContext) throws Exception ;

	private Method findOperation(AxisOperation op, Class implClass) throws AxisFault {

		Method method = (Method) (op.getParameterValue("myMethod"));

		if (method != null)
			return method;

		String methodName = op.getName().getLocalPart();

		try {

			// Looking for a method of the form "void method(OMElement)"

			
			method = implClass.getMethod(methodName,
					new Class[] { OMElement.class });

//			if (1 == 1) 
//				throw new Exception("return type is " + method.getReturnType().getName() + "\n" +
//						"methodName is " + methodName + "\n" +
//						"class is " + implClass.getName());
			
			if (method.getReturnType().getName().equals("void")) {
				try {
					op.addParameter("myMethod", method);
				} catch (AxisFault axisFault) {
					throw AxisFault.makeFault(axisFault);
				}
				return method;
			}

		} catch (Exception e) {
			throw AxisFault.makeFault(e);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("return class is " + method.getReturnType().getName());
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
	public void invokeBusinessLogic(MessageContext msgContext)
	throws AxisFault {

		Object obj = null;
		try {

			String in_action = msgContext.getWSAAction();

			if (logger.isDebugEnabled()) {
				logger.debug("In MEP: in_action = " + in_action);
			}
			
			validate_action(msgContext);  

			// get the implementation class for the Web Service

			obj = getTheImplementationObject(msgContext);


			// make return message context available to service
			if (obj instanceof AppendixV) {
				AppendixV xo = (AppendixV) obj;
			}


		} catch (Exception e) {

			logger.error("Error in XDSRawXMLIn:\n" +  ExceptionUtil.exception_details(e) + "\ngetSoapAction = " + msgContext.getSoapAction());

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
			logger.error("Error in XDSRawXMLIn:\n" +  
					"class is " + implClass.getName() + " method is setMessageContextIn" + 
					ExceptionUtil.exception_details(e) + "\ngetSoapAction = " + msgContext.getSoapAction());

			throw AxisFault.makeFault(e);
		} catch (NoSuchMethodException e) {
			logger.error("Error in XDSRawXMLIn:\n" +  
					"class is " + implClass.getName() + " method is setMessageContextIn" + 
					ExceptionUtil.exception_details(e) + "\ngetSoapAction = " + msgContext.getSoapAction());

			throw AxisFault.makeFault(e);
		}

		try {
			methodDisplay.invoke(obj, new Object[] { msgContext });
		} 
		catch (Exception e) {
			logger.error("Error in XDSRawXMLIn:\n" +  
					"class is " + implClass.getName() + " method is setMessageContextIn" + 
					ExceptionUtil.exception_details(e) + "\ngetSoapAction = " + msgContext.getSoapAction());

			throw AxisFault.makeFault(e);
		}

		if (method == null) {
			throw new AxisFault(Messages.getMessage(
					"methodDoesNotExistIn",
					opDesc.getName().toString()));
		}

		try {
			method.invoke(

					obj, new Object[] { msgContext.getEnvelope().getBody()
							.getFirstElement() });
		} 
		catch (Exception e) {
			logger.error("Error in XDSRawXMLIn:\n" +  
					"class is " + implClass.getName() + " method is setMessageContextIn" + 
					ExceptionUtil.exception_details(e) + "\ngetSoapAction = " + msgContext.getSoapAction());

			throw AxisFault.makeFault(e);
		}

	}
}
