package gov.nist.registry.ws;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.AdhocQueryResponse;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.RegistryUtility;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.XdsCommon;

import java.util.List;

import org.apache.axiom.om.OMElement;

public class EchoV2Metadata extends XdsCommon {

	public EchoV2Metadata() {

	}

	public OMElement echo(OMElement in) {
		try {
			AdhocQueryResponse ahqr = new AdhocQueryResponse(Response.version_2);
			Metadata m = new Metadata(in);
			List parts = m.getV2();
			for (int i=0; i<parts.size(); i++) {
				OMElement ele = (OMElement) parts.get(i);
				ahqr.addQueryResults(ele);
			}
			return ahqr.getResponse();
		} 
		catch (MetadataException e) {
			System.out.println(RegistryUtility.exception_details(e));
			return null;
		}
		catch (XdsInternalException e) {
			System.out.println(RegistryUtility.exception_details(e));
			return null;
		}
	}

}
