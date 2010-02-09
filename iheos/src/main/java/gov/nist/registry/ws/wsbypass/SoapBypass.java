package gov.nist.registry.ws.wsbypass;

import gov.nist.registry.common2.exception.ExceptionUtil;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.soap.SoapInterface;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SoapBypass implements SoapInterface {
	private static final Log log = LogFactory.getLog(SoapBypass.class);
	OMElement result = null;

	public String getExpectedReturnAction() {
		return null;
	}

	public OMElement getInHeader() throws XdsInternalException {
		return null;
	}

	public OMElement getOutHeader() throws XdsInternalException {
		return null;
	}

	public OMElement getResult() {
		return result;
	}

	public boolean isAddressing() {
		return false;
	}

	public boolean isAsync() {
		return false;
	}

	public boolean isMtom() {
		return false;
	}

	public boolean isSoap12() {
		return false;
	}

	public void setAddressing(boolean addressing) {
	}

	public void setAsync(boolean async) {
	}

	public void setExpectedReturnAction(String expectedReturnAction) {
	}

	public void setMtom(boolean mtom) {
	}

	public void setSoap12(boolean soap12) {
	}

	public OMElement soapCall(OMElement body, Protocol protocol, String endpoint, boolean mtom,
			boolean addressing, boolean soap12, String action,
			String expected_return_action) throws XdsException {
		result = soapCall(body, protocol, endpoint, action);
		return result;
	}

	public OMElement soapCall(OMElement body, Protocol protocol, String endpoint, String action)
			throws XdsException {
		try {
			return new ServiceFinder(endpoint, action).invoke(body);
		}
		catch (Exception e) {
			log.error(ExceptionUtil.exception_details(e));
			throw new XdsException("SoapBypass call failed", e);
		}
	}

	public void soapSend(OMElement metadata_element, Protocol protocol, String endpoint,
			boolean useMtom, boolean useAddressing, boolean soap_1_2,
			String requestAction) throws XdsException, AxisFault {
		// TODO Auto-generated method stub
		
	}
	
	public void addHeader(OMElement header) {
		// TODO Auto-generated method stub
		
	}

	public void clearHeaders() {
		// TODO Auto-generated method stub
		
	}

}
