package gov.nist.registry.common2.soap;

import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsInternalException;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.commons.httpclient.protocol.Protocol;

public interface SoapInterface {
	public void setAsync(boolean async);
	public boolean isAsync();
	public OMElement soapCall(OMElement body, Protocol protocol, String endpoint, boolean mtom, 
			boolean addressing, boolean soap12, String action, String expected_return_action) 
	throws  XdsException, AxisFault;
	public OMElement soapCall(OMElement body, Protocol protocol, String endpoint,
			  String action) 
	throws  XdsException, AxisFault;
	public OMElement getResult();
	public OMElement getInHeader() throws XdsInternalException;
	public OMElement getOutHeader()  throws XdsInternalException;
	public String getExpectedReturnAction();
	public void setExpectedReturnAction(String expectedReturnAction);
	public boolean isMtom();
	public void setMtom(boolean mtom);
	public boolean isAddressing();
	public void setAddressing(boolean addressing);
	public boolean isSoap12();
	public void setSoap12(boolean soap12);
	public void soapSend(OMElement metadata_element, Protocol protocol, String endpoint,
			boolean useMtom, boolean useAddressing, boolean soap_1_2,
			String requestAction) throws  XdsException, AxisFault;
	public void addHeader(OMElement header);
	public void clearHeaders();
}
