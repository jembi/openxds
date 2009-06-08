package gov.nist.registry.ws.receivers;

import gov.nist.registry.ws.serviceclasses.XdsService;

import java.lang.reflect.Method;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.RelatesTo;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.i18n.Messages;
import org.apache.axis2.receivers.AbstractInOutMessageReceiver;

abstract public class AbstractXDSRawXMLINoutMessageReceiver extends
AbstractInOutMessageReceiver {


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
			
				try {
								
					String in_action = msgContext.getWSAAction();
					
					//System.out.println("in_action = " + in_action);
					
					newmsgContext.setRelationships(new RelatesTo[] { new RelatesTo(msgContext.getMessageID()) });
					
					validate_action(msgContext, newmsgContext);  
					
					// get the implementation class for the Web Service
			
					Object obj = getTheImplementationObject(msgContext);
					
					
					// make return message context available to service
					if (obj instanceof XdsService) {
						XdsService xo = (XdsService) obj;
						xo.setReturnMessageContext(newmsgContext);
					}
					
					
					
			
					// find the WebService method
			
					Class implClass = obj.getClass();
			
					AxisOperation opDesc = msgContext.getAxisOperation();
			
					Method method = findOperation(opDesc, implClass);
			
					Method methodDisplay = implClass.getMethod("setMessageContextIn",
							new Class[] { MessageContext.class });
			
					methodDisplay.invoke(obj, new Object[] { msgContext });
			
					if (method == null) {
						throw new AxisFault(Messages.getMessage(
								"methodDoesNotExistInOut",
								opDesc.getName().toString()));
					}
			
					OMElement result = (OMElement) method.invoke(
			
							obj, new Object[] { msgContext.getEnvelope().getBody()
									.getFirstElement() });
			
					SOAPFactory fac = getSOAPFactory(msgContext);
								
					SOAPEnvelope envelope = fac.getDefaultEnvelope();
			
					if (result != null) {
			
						envelope.getBody().addChild(result);
						
					}
			
					newmsgContext.setEnvelope(envelope);
			

					
					
					
				} catch (Exception e) {
			
					System.out.println("Error in XDSRawXMLInOut:\n" +  e.getMessage() + "\ngetSoapAction = " + msgContext.getSoapAction());
			
					throw AxisFault.makeFault(e);
								
				}
			
			}
}
