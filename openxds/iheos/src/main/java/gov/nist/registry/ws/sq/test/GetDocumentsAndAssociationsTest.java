package gov.nist.registry.ws.sq.test;

import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.StoredQuery;
import gov.nist.registry.common2.testsupport.Linkage;
import gov.nist.registry.ws.sq.GetDocumentsAndAssociations;

import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.TestCase;

public class GetDocumentsAndAssociationsTest extends TestCase {

	HashMap<String, Object> params;
	StoredQuery gd;
	Linkage l;

	public void setUp() {
		params = new HashMap<String, Object>();
		l = new Linkage();
	}

	public void test_single_uid_object() throws Exception {
		ArrayList<String> a = new ArrayList<String>();
		String value = l.get_value(
				"../xds/selftest/FindDocuments2/submit1", 
				"Document01", 
				"submit_doc", 
		"AssignedUids");
		this.assertNotNull(value);
		a.add(value);
		params.put("$XDSDocumentEntryUniqueId", a);


		gd = new GetDocumentsAndAssociations(params, true, new TestResponse(Response.version_3), null, false);
		Metadata m = gd.run_internal();
		assertTrue("Found " + m.getExtrinsicObjects().size() + " documents", m.getExtrinsicObjects().size() == 1);
		assertTrue("Found " + m.getAssociations().size() + " associations", m.getAssociations().size() == 1);
	}


}
