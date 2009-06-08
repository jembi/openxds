package gov.nist.registry.ws.sq.test;

import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.StoredQuery;
import gov.nist.registry.common2.testsupport.Linkage;
import gov.nist.registry.ws.sq.GetDocuments;

import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.TestCase;

public class GetDocumentsTest extends TestCase {
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


		gd = new GetDocuments(params, true, new TestResponse(Response.version_3), null, false);
		Metadata m = gd.run_internal();
		assertTrue("found " + m.getExtrinsicObjects().size() + " documents", m.getExtrinsicObjects().size() == 1);
	}

	public void test_multi_uid_object() throws Exception {
		ArrayList<String> a = new ArrayList<String>();
		String value;
		value = l.get_value(
				"../xds/selftest/FindDocuments2/submit1", 
				"Document01", 
				"submit_doc", 
		"AssignedUids");
		this.assertNotNull(value);
		a.add(value);

		value = l.get_value(
				"../xds/selftest/FindDocuments2/submit1", 
				"Document02", 
				"submit_2doc_w_fol", 
		"AssignedUids");
		this.assertNotNull(value);
		a.add(value);
		
		params.put("$XDSDocumentEntryUniqueId", a);


		try {
			gd = new GetDocuments(params, true, new TestResponse(Response.version_3), null, false);
			Metadata m = gd.run_internal();
			assertTrue(m.getExtrinsicObjects().size() == 2);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
	}

	public void test_single_uid_objectref()  throws Exception {
		ArrayList<String> a = new ArrayList<String>();
		String value = l.get_value(
				"../xds/selftest/FindDocuments2/submit1", 
				"Document01", 
				"submit_doc", 
		"AssignedUids");
		this.assertNotNull(value);
		a.add(value);
		//a.add("129.6.58.91.6703");
		params.put("$XDSDocumentEntryUniqueId", a);


		try {
			gd = new GetDocuments(params, false, new TestResponse(Response.version_3), null, false);
			Metadata m = gd.run_internal();
			assertTrue(m.getExtrinsicObjects().size() == 0);
			assertTrue(m.getObjectRefs().size() == 1);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
	}

	public void test_single_uuid_object() throws Exception {
		ArrayList<String> a = new ArrayList<String>();
		String value = l.get_value(
				"../xds/selftest/FindDocuments2/submit1", 
				"Document01", 
				"submit_doc", 
		"AssignedUuids");
		this.assertNotNull(value);
		a.add(value);
		//a.add("urn:uuid:18440370-9cc9-4220-8d9a-906d117f326c");
		params.put("$XDSDocumentEntryEntryUUID", a);


		try {
			gd = new GetDocuments(params, true, new TestResponse(Response.version_3), null, false);
			Metadata m = gd.run_internal();
			assertTrue(m.getExtrinsicObjects().size() == 1);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
	}

	public void test_single_uuid_objectref() throws Exception {
		ArrayList<String> a = new ArrayList<String>();
		String value = l.get_value(
				"../xds/selftest/FindDocuments2/submit1", 
				"Document01", 
				"submit_doc", 
		"AssignedUuids");
		this.assertNotNull(value);
		a.add(value);
//		a.add("urn:uuid:18440370-9cc9-4220-8d9a-906d117f326c");
		params.put("$XDSDocumentEntryEntryUUID", a);


		try {
			gd = new GetDocuments(params, false, new TestResponse(Response.version_3), null, false);
			Metadata m = gd.run_internal();
			assertTrue(m.getExtrinsicObjects().size() == 0);
			assertTrue(m.getObjectRefs().size() == 1);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
