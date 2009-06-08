package gov.nist.registry.ws.sq.test;

import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.StoredQuery;
import gov.nist.registry.common2.testsupport.Linkage;
import gov.nist.registry.ws.sq.GetAssociations;
import gov.nist.registry.ws.sq.GetDocuments;

import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.TestCase;

public class GetAssociationsTest extends TestCase {

	HashMap<String, Object> params;
	StoredQuery ga;
	Linkage l;

	public void setUp() {
		params = new HashMap<String, Object>();
		l = new Linkage();
	}

	public void test_single_doc() throws Exception {
		String value = l.get_value(
				"../xds/selftest/FindDocuments2/submit1", 
				"SubmissionSet01", 
				"submit_doc", 
		"AssignedUuids");
		this.assertNotNull(value);
		params.put("$uuid", value );


		ga = new GetAssociations(params, true, new TestResponse(Response.version_3), null, false);
		Metadata m = ga.run_internal();
		assertTrue(m.getAssociations().size() == 1);
	}


}
