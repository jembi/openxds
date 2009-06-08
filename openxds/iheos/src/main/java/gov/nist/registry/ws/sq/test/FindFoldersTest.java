package gov.nist.registry.ws.sq.test;

import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.StoredQuery;
import gov.nist.registry.common2.testsupport.Linkage;
import gov.nist.registry.ws.sq.FindFolders;

import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.TestCase;

public class FindFoldersTest extends TestCase {

	HashMap<String, Object> params;
	StoredQuery gd;
	Linkage l;

	public void setUp() {
		params = new HashMap<String, Object>();
		l = new Linkage();
	}

	public void test_simple() throws Exception {
		String value; 
		
		value = l.get_value(
				"../xds/selftest/FindDocuments2/submit1", 
				"Folder", 
				"submit_doc_w_fol", 
		"AssignedPatientId");
		assertNotNull(value);
		params.put("$XDSFolderPatientId", value);
		
		ArrayList<String> status = new ArrayList<String>();
		status.add("Approved");
		params.put("$XDSFolderStatus", status);


		gd = new FindFolders(params, false, new TestResponse(Response.version_3), null, false);
		Metadata m = gd.run_internal();
		assertTrue("Found " + m.getObjectRefs().size() + " folders: " +
				m.getObjectRefs(), 
				m.getObjectRefs().size() == 2);
	}

	public void test_last_update_time() throws Exception {
		String value; 
		
		value = l.get_value(
				"../xds/selftest/FindDocuments2/submit1", 
				"Folder", 
				"submit_doc_w_fol", 
		"AssignedPatientId");
		assertNotNull(value);
		params.put("$XDSFolderPatientId", value);
		
		params.put("$XDSFolderLastUpdateTimeFrom", "20041223");
		params.put("$XDSFolderLastUpdateTimeTo", "20041225");
		
		ArrayList<String> status = new ArrayList<String>();
		status.add("Approved");
		params.put("$XDSFolderStatus", status);


		gd = new FindFolders(params, true, new TestResponse(Response.version_3), null, false);
		Metadata m = gd.run_internal();
		assertTrue("Found " + m.getObjectRefs().size() + " folders: " +
				m.getObjectRefs(), 
				m.getObjectRefs().size() == 1);
	}

}
