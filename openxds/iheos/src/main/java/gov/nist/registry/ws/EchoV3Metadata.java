package gov.nist.registry.ws;

import gov.nist.registry.common2.registry.AdhocQueryResponse;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.RegistryUtility;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.XdsCommon;

import java.util.ArrayList;

import org.apache.axiom.om.OMElement;

public class EchoV3Metadata extends XdsCommon {
	
	public EchoV3Metadata() {

	}

	public OMElement echo(OMElement in) {
		try {
			AdhocQueryResponse ahqr = new AdhocQueryResponse(Response.version_3);
			Metadata m = new Metadata(in);
			ArrayList parts = m.getV3();
			for (int i=0; i<parts.size(); i++) {
				OMElement ele = (OMElement) parts.get(i);
				ahqr.addQueryResults(ele);
			}
			return ahqr.getResponse();
		} 
		catch (Exception e) {
			System.out.println(RegistryUtility.exception_details(e));
			return null;
		}
	}


}
