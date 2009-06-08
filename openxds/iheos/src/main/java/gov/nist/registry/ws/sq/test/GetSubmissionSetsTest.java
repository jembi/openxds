package gov.nist.registry.ws.sq.test;

import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.StoredQuery;
import gov.nist.registry.common2.testsupport.Linkage;
import gov.nist.registry.ws.sq.GetSubmissionSets;

import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.TestCase;

public class GetSubmissionSetsTest extends TestCase {


	HashMap<String, Object> params;
	StoredQuery ga;
	Linkage l;

	public void setUp() {
		params = new HashMap<String, Object>();
		l = new Linkage();
	}

	public void test_single_doc() throws Exception {
		ArrayList<String> parms = new ArrayList<String>();
		String value = l.get_value(
				"../xds/selftest/FindDocuments2/submit1", 
				"Document01", 
				"submit_doc", 
		"AssignedUuids");
		this.assertNotNull(value);
		parms.add(value);
		params.put("$uuid", parms );


		ga = new GetSubmissionSets(params, true, new TestResponse(Response.version_3), null, false);
		Metadata m = ga.run_internal();
		assertTrue(m.getRegistryPackages().size() == 1);
	}

	public void test_doc_in_folder() throws Exception {
		ArrayList<String> parms = new ArrayList<String>();
		String value = l.get_value(
				"../xds/selftest/FindDocuments2/submit1", 
				"Document01", 
				"submit_doc_w_fol", 
		"AssignedUuids");
		this.assertNotNull(value);
		parms.add(value);
		params.put("$uuid", parms );


		ga = new GetSubmissionSets(params, true, new TestResponse(Response.version_3), null, false);
		Metadata m = ga.run_internal();
		assertTrue("found " +  m.getRegistryPackages().size() + " submission sets", m.getRegistryPackages().size() == 1);
	}

	public void test_single_fol() throws Exception {
		ArrayList<String> parms = new ArrayList<String>();
		String value = l.get_value(
				"../xds/selftest/FindDocuments2/submit1", 
				"Folder", 
				"submit_doc_w_fol", 
		"AssignedUuids");
		this.assertNotNull(value);
		parms.add(value);
		params.put("$uuid", parms );


		ga = new GetSubmissionSets(params, true, new TestResponse(Response.version_3), null, false);
		Metadata m = ga.run_internal();
		assertTrue(m.getRegistryPackages().size() == 1);
	}

	public void test_single_fol_single_doc() throws Exception {
		ArrayList<String> parms = new ArrayList<String>();
		String value;
		value = l.get_value(
				"../xds/selftest/FindDocuments2/submit1", 
				"Folder", 
				"submit_doc_w_fol", 
		"AssignedUuids");
		this.assertNotNull(value);
		parms.add(value);

		
		value = l.get_value(
				"../xds/selftest/FindDocuments2/submit1", 
				"Document01", 
				"submit_doc_w_fol", 
		"AssignedUuids");
		this.assertNotNull(value);
		parms.add(value);

		params.put("$uuid", parms );


		ga = new GetSubmissionSets(params, true, new TestResponse(Response.version_3), null, false);
		Metadata m = ga.run_internal();
		assertTrue("found " +   m.getSubmissionSets().size() + " submission sets: " +
				m.getSubmissionSetIds(), 
				m.getSubmissionSets().size() == 1);
	}

}
