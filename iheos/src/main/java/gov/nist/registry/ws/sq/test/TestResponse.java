package gov.nist.registry.ws.sq.test;

import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.Response;

import org.apache.axiom.om.OMElement;

public class TestResponse extends Response {
	
	public TestResponse(short version) throws XdsInternalException { 
		super(version);
	}
	
	public void addQueryResults(OMElement e) { }
	
	public OMElement getRoot() throws XdsInternalException { return this.getResponse(); }

}
