package gov.nist.registry.common2.Dsub;

import org.apache.axiom.om.OMElement;

import gov.nist.registry.common2.service.AppendixV;

public class DsubService extends AppendixV {

	protected OMElement beginTransaction(String service_name,
			OMElement request, short actor) {
		return null;
	}

	protected OMElement endTransaction(OMElement request, Exception e,
			short actor, String message) {
		return null;
	}

	protected OMElement endTransaction(OMElement request, Exception e,
			short actor, String message, String error_type) {
		return null;
	}

	protected void endTransaction(boolean status) {
		
	}

}
