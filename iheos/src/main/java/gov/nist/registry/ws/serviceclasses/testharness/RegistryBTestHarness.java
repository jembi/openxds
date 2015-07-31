package gov.nist.registry.ws.serviceclasses.testharness;

import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.XdsCommon;
import gov.nist.registry.ws.AdhocQueryRequest;
import gov.nist.registry.ws.SubmitObjectsRequest;
import gov.nist.registry.ws.sq.test.MockLog;

import org.apache.axiom.om.OMElement;

public class RegistryBTestHarness {
	
	public OMElement AdhocQueryRequest(OMElement ahqr) throws XdsInternalException {
		AdhocQueryRequest q = new AdhocQueryRequest(
				new MockLog(),
				null /* MessageContext */,
				false /* isSecure */,
				XdsCommon.xds_b);
		
		return q.adhocQueryRequest(ahqr);
	}

	public OMElement SubmitObjectsRequest(OMElement sor) {
		SubmitObjectsRequest s = new SubmitObjectsRequest(
				new MockLog(),
				XdsCommon.xds_b,
				null /* MessageContext */
				);
		
		return s.submitObjectsRequest(sor);
	}
}
