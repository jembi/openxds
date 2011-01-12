package gov.nist.registry.common2.soap;


import gov.nist.registry.common2.exception.ExceptionUtil;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsFormatException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.xml.Util;
import gov.nist.registry.ws.serviceclasses.XdsService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.OperationContext;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.logging.LogFactory;

public class Soap implements SoapInterface {
	private final static org.apache.commons.logging.Log logger = LogFactory.getLog(Soap.class);
    ServiceClient serviceClient = null;
	OMElement result = null;
	boolean async;
	String expectedReturnAction;
	boolean mtom;
	boolean addressing;
	boolean soap12;
	List<OMElement> additionalHeaders = null;
	
	public Soap() {
		async = false;
		expectedReturnAction = null;
		mtom = false;
		addressing = true;
		soap12 = true;
	}
	
	public void addHeader(OMElement header) {
		if (additionalHeaders == null)
			additionalHeaders = new ArrayList<OMElement>();
		additionalHeaders.add(header);
	}
	
	public void clearHeaders() {
		additionalHeaders = null;
	}

	public void setAsync(boolean async) {
		this.async = async;
	}

	public void soapSend(OMElement body, Protocol protocol, String endpoint, boolean mtom, 
			boolean addressing, boolean soap12, String action) 
	throws  XdsException, AxisFault {
		
		this.expectedReturnAction = null;
		this.mtom = mtom;
		this.addressing = addressing;
		this.soap12 = soap12;
		
		soapSend(body, protocol, endpoint, action);
	}
	
	public OMElement soapCall(OMElement body, Protocol protocol, String endpoint, boolean mtom, 
			boolean addressing, boolean soap12, String action, String expected_return_action) 
	throws  XdsException, AxisFault {
		
		this.expectedReturnAction = expected_return_action;
		this.mtom = mtom;
		this.addressing = addressing;
		this.soap12 = soap12;
		
		return soapCall(body, protocol, endpoint, action);
	}
	
	public void soapSend(OMElement body, Protocol protocol, String endpoint,
			  String action) 
	throws  XdsException, AxisFault {
		
//		if (1 == 1) {
//			soapCall(body, endpoint, action);
//			return;
//		}

//		try {
			if (serviceClient == null)
				serviceClient = new ServiceClient();

			serviceClient.getOptions().setTo(new EndpointReference(endpoint));
			try {
				serviceClient.getOptions().setFrom(new EndpointReference(InetAddress.getLocalHost().getHostAddress()));
			} catch (UnknownHostException e) {
				throw new XdsException(ExceptionUtil.exception_details(e));
			}

			if (System.getenv("XDSHTTP10") != null) {
				logger.info("Generating HTTP 1.0");

				serviceClient.getOptions().setProperty
				(org.apache.axis2.transport.http.HTTPConstants.HTTP_PROTOCOL_VERSION,
						org.apache.axis2.transport.http.HTTPConstants.HEADER_PROTOCOL_10);

				serviceClient.getOptions().setProperty
				(org.apache.axis2.transport.http.HTTPConstants.CHUNKED,
						Boolean.FALSE);

			}

			serviceClient.getOptions().setProperty(Constants.Configuration.ENABLE_MTOM, 
					((mtom) ? Constants.VALUE_TRUE : Constants.VALUE_FALSE));

			serviceClient.getOptions().setAction(action);
			if (addressing) {
				serviceClient.engageModule("addressing");
			} else {
				serviceClient.disengageModule("addressing");    // this does not work in Axis2 yet
			}

			serviceClient.getOptions().setSoapVersionURI(
					((soap12) ? SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI : SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI)
			);
//			System.out.println("fire and forget " + endpoint);
//			serviceClient.fireAndForget(body);

			logger.info("sendRobust " + endpoint);
			serviceClient.sendRobust(body);

	}

	public OMElement soapCall(OMElement body, Protocol protocol, String endpoint,
			  String action) 
	throws  XdsException, AxisFault {

//		try {
			if (serviceClient == null){
/*				ConfigurationContext configurationContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem( 
(); 

		        MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager = new MultiThreadedHttpConnectionManager(); 
	
		        HttpConnectionManagerParams params = new HttpConnectionManagerParams(); 
		        params.setDefaultMaxConnectionsPerHost(20); 
		        multiThreadedHttpConnectionManager.setParams(params); 
		        HttpClient httpClient = new HttpClient(multiThreadedHttpConnectionManager); 
		        configurationContext.setProperty(HTTPConstants.CACHED_HTTP_CLIENT, httpClient); 
				serviceClient = new ServiceClient(configurationContext, null);*/
				serviceClient = new ServiceClient();
			}
			serviceClient.getOptions().setTimeOutInMilliSeconds(60000);

			serviceClient.getOptions().setTo(new EndpointReference(endpoint));

			if (System.getenv("XDSHTTP10") != null) {
				logger.info("Generating HTTP 1.0");

				serviceClient.getOptions().setProperty
				(org.apache.axis2.transport.http.HTTPConstants.HTTP_PROTOCOL_VERSION,
						org.apache.axis2.transport.http.HTTPConstants.HEADER_PROTOCOL_10);

				serviceClient.getOptions().setProperty
				(org.apache.axis2.transport.http.HTTPConstants.CHUNKED,
						Boolean.FALSE);

			}
			serviceClient.getOptions().setProperty(Constants.Configuration.ENABLE_MTOM, 
					((mtom) ? Constants.VALUE_TRUE : Constants.VALUE_FALSE));

			serviceClient.getOptions().setProperty(HTTPConstants.CUSTOM_PROTOCOL_HANDLER, protocol);
			
			serviceClient.getOptions().setAction(action);
			if (addressing) {
				serviceClient.engageModule("addressing");
			} else {
				serviceClient.disengageModule("addressing");    // this does not work in Axis2 yet
			}
			
			if (additionalHeaders != null) {
				for (OMElement hdr : additionalHeaders) {
					serviceClient.addHeader(hdr);
				}
			}			

			serviceClient.getOptions().setSoapVersionURI(
					((soap12) ? SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI : SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI)
			);

			if ( async && !serviceClient.getOptions().isUseSeparateListener())
				serviceClient.getOptions().setUseSeparateListener(async);
			
			if (logger.isInfoEnabled()) {
				logger.info("Call " + endpoint);
				logger.info("Action " + action);
			}
			OMElement temp = serviceClient.sendReceive(body);
			result = Util.deep_copy(temp);
			
			//if (async)
				serviceClient.cleanupTransport();

			Object in = serviceClient.getServiceContext().getLastOperationContext().getMessageContexts().get("In");
			if ( ! (in instanceof MessageContext))
				throw new XdsInternalException("Soap: In MessageContext of type " + in.getClass().getName() + " instead of MessageContext");
			MessageContext inMsgCxt = (MessageContext) in;
			boolean responseMtom = inMsgCxt.isDoingMTOM();

			if ( mtom != responseMtom)
				if (mtom) {
					throw new XdsFormatException("Request was MTOM format but response was SIMPLE SOAP");
				} else {
					throw new XdsFormatException("Request was SIMPLE SOAP but response was MTOM");
				}

			this.result = result;

			if (async)
				verify_returned_action(expectedReturnAction, "urn:mediateResponse");
			else
				verify_returned_action(expectedReturnAction, null);

			return result;

//		} catch (AxisFault e) {
//			throw new XdsException(ExceptionUtil.exception_details(e));
//		}
	}
	public OMElement getResult() { return result; }

	void verify_returned_action(String expected_return_action, String alternate_return_action) throws XdsException {
		if (expected_return_action == null)
			return;

		OMElement hdr = getInHeader();
		if (hdr == null && expected_return_action != null) 
			throw new XdsInternalException("No SOAPHeader returned: expected header with action = " + expected_return_action);
		OMElement action = MetadataSupport.firstChildWithLocalName(hdr, "Action");
		if (action == null && expected_return_action != null) 
			throw new XdsInternalException("No action returned in SOAPHeader: expected action = " + expected_return_action);
		String action_value = action.getText();
		if (alternate_return_action == null) {
			if ( action_value == null || !action_value.equals(expected_return_action))
				throw new XdsInternalException("Wrong action returned in SOAPHeader: expected action = " + expected_return_action + 
						" returned action = " + action_value);
		} else {
			if ( action_value == null || 
					( (!action_value.equals(expected_return_action)) && (!action_value.equals(alternate_return_action))  )
			)
				throw new XdsInternalException("Wrong action returned in SOAPHeader: expected action = " + expected_return_action + 
						" returned action = " + action_value);
		}
	}

	public OMElement getInHeader() throws XdsInternalException {
		OperationContext oc = serviceClient.getLastOperationContext();
		HashMap<String, MessageContext> ocs = oc.getMessageContexts();
		MessageContext in = ocs.get("In");

		if (in == null)
			return null;

		if (in.getEnvelope() == null)
			return null;

		if (in.getEnvelope().getHeader() == null)
			return null;

		return Util.deep_copy( in.getEnvelope().getHeader());
	}

	public OMElement getOutHeader()  throws XdsInternalException {
		OperationContext oc = serviceClient.getLastOperationContext();
		HashMap<String, MessageContext> ocs = oc.getMessageContexts();
		MessageContext out = ocs.get("Out");

		if (out == null)
			return null;

		return Util.deep_copy( out.getEnvelope().getHeader());
	}

	public String getExpectedReturnAction() {
		return expectedReturnAction;
	}

	public void setExpectedReturnAction(String expectedReturnAction) {
		this.expectedReturnAction = expectedReturnAction;
	}

	public boolean isMtom() {
		return mtom;
	}

	public void setMtom(boolean mtom) {
		this.mtom = mtom;
	}

	public boolean isAddressing() {
		return addressing;
	}

	public void setAddressing(boolean addressing) {
		this.addressing = addressing;
	}

	public boolean isSoap12() {
		return soap12;
	}

	public void setSoap12(boolean soap12) {
		this.soap12 = soap12;
	}

	public boolean isAsync() {
		return async;
	}


}
