package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.ws.TestImpl11710;

import org.apache.axiom.om.OMElement;

public class Test11710 extends XdsService {

	
	//RegisterDocumentSetRequest
	public OMElement hello(OMElement sor) {
		try {
			OMElement startup_error = beginTransaction("11710", sor, XdsService.registry_actor);
			if (startup_error != null)
				return startup_error;

			log_message.setTestMessage("Test 11710");

			
			TestImpl11710 ti = new TestImpl11710(log_message, XdsService.registry_actor, getMessageContext());
			OMElement result = ti.testImpl11710(sor);
			
			endTransaction(ti.getStatus());
			
			return result;
		} catch (Exception e) {
			System.out.println("Exception: " + exception_details(e));
			endTransaction(false);
			return null;
		}
	}

}
