package gov.nist.registry.ws.test;

import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.xml.Util;
import gov.nist.registry.ws.serviceclasses.Xds;

import java.io.File;

import junit.framework.TestCase;

import org.apache.axiom.om.OMElement;

public class AdhocQueryRequestTest extends TestCase {

	public void test_get_ss_and_contents() throws XdsInternalException {
		OMElement query = Util.parse_xml(new File("/Users/bill/IheOs/workspace_prod/xds/testdata/getss_1.xml"));
		Xds xds = new Xds();
		OMElement response = xds.AdhocQueryRequest(query);
		System.out.println("final result " + response.toString());
	}
}
