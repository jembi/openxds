package gov.nist.registry.ws.test;

import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.ws.SqlRepair;
import gov.nist.registry.ws.SqlRepairException;
import junit.framework.TestCase;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMText;

public class SqlRepairTest extends TestCase {
	OMFactory om_factory = null;
	SqlRepair sr;

	OMFactory om_factory() {
		if (om_factory == null)
			om_factory = OMAbstractFactory.getOMFactory();
		return om_factory;
	}

	public void test_get_query_text_ref() throws XdsInternalException, SqlRepairException {
		SqlRepair sr = new SqlRepair();
		String query = "select * from a;";
		OMElement test_data = build_test_data_ref(query);
		String query2 = sr.get_query_text(test_data);
		assertTrue(query.equals(query2));
	}

	public void test_get_query_text_leaf() throws XdsInternalException, SqlRepairException {
		SqlRepair sr = new SqlRepair();
		String query = "select * from a;";
		OMElement test_data = build_test_data_leaf(query);
		String query2 = sr.get_query_text(test_data);
		assertTrue(query.equals(query2));
	}


	public void test_star_leaf() {
		try {
			run_test_leaf("select * from foo;");
		} catch (SqlRepairException e) {
			return;
		}
		fail("no exception");
	}

	public void test_star_ref() {
		try {
			run_test_ref("select * from foo;");
		} catch (SqlRepairException e) {
			return;
		}
		fail("no exception");
	}

	public void test_no_sqlquery() {
		OMElement doc = om_factory().createOMElement("foo", null);
		SqlRepair sr = new SqlRepair();
		try {
			sr.repair(doc);
		} catch (SqlRepairException e) {
			return;
		} catch (XdsInternalException e) {
			fail(e.getMessage());
		}
		fail("No exception");
	}

	public void test_id_leaf() throws SqlRepairException, XdsInternalException {
		OMElement result = null;
		try {
			result = run_test_leaf("select foo.id from foo");
		} catch (SqlRepairException e) {
			fail(e.getMessage());
		}
		String sql = sr.get_query_text(result);
		assertTrue (sql.indexOf("foo.id") == -1) ;
		assertTrue(sql, sql.equals(" select * from foo"));
	}
	
	public void test_id_ref() throws SqlRepairException, XdsInternalException {
		OMElement result = null;
		try {
			result = run_test_ref("select foo.id from foo");
		} catch (SqlRepairException e) {
			fail(e.getMessage());
		}
		String sql = sr.get_query_text(result);
		assertTrue ("sql is " + sql, sql.indexOf("foo.id") != -1) ;
		assertTrue(sql.equals(" select foo.id from foo"));
	}
	
	OMElement build_test_data_leaf(String sql) { return build_test_data(sql, "LeafClass"); }
	OMElement build_test_data_ref(String sql) { return build_test_data(sql, "ObjectRef"); }

	OMElement build_test_data(String sql, String return_type) {
		OMElement doc = om_factory().createOMElement("AdhocQueryRequest", null);
		OMElement ro = om_factory().createOMElement("ResponseOption", null);
		doc.addChild(ro);
		ro.addAttribute("returnType", return_type, null);
		OMElement sqlquery = om_factory().createOMElement("SQLQuery", null);
		ro.addChild(sqlquery);
		OMText text = om_factory().createOMText(sql);
		sqlquery.addChild(text);
		return doc;
	}

	OMElement run_test_leaf(String sql)  throws SqlRepairException {
		OMElement doc = build_test_data_leaf(sql);
		OMElement results = null;
		sr = new SqlRepair();
		try {
			sr.repair(doc);
			results = doc;
		} catch (XdsInternalException e) {
			fail(e.getMessage());
		}
		return results;
	}

	OMElement run_test_ref(String sql)  throws SqlRepairException {
		OMElement doc = build_test_data_ref(sql);
		OMElement results = null;
		sr = new SqlRepair();
		try {
			sr.repair(doc);
			results = doc;
		} catch (XdsInternalException e) {
			fail(e.getMessage());
		}
		return results;
	}


	public void test_find_sql_query_element() throws XdsInternalException, SqlRepairException {
		OMElement stuff = om_factory().createOMElement("foo", null);
		OMElement fluss = om_factory().createOMElement("bar", null);
		stuff.addChild(fluss);
		OMElement flub = om_factory().createOMElement("SQLQuery", null);
		fluss.addChild(flub);

		SqlRepair sr = new SqlRepair();
		OMElement query1 = sr.find_sql_query_element(stuff);

		assertTrue("No element returned", query1 != null);
		assertTrue(query1.getLocalName().equals("SQLQuery"));
	}
	
	public String repair_query_text_leaf(String sql) throws XdsInternalException, SqlRepairException {
		SqlRepair sr = new SqlRepair();
		String query = sql;
		OMElement test_data = build_test_data_leaf(query);
		sr.repair(test_data);
		return sr.get_query_text(test_data);
	}

	public String repair_query_text_ref(String sql) throws XdsInternalException, SqlRepairException {
		SqlRepair sr = new SqlRepair();
		String query = sql;
		OMElement test_data = build_test_data_ref(query);
		sr.repair(test_data);
		return sr.get_query_text(test_data);
	}

	
	public void test_nested_select_leaf()  throws XdsInternalException, SqlRepairException {
		String sql = "SELECT doc.id FROM ExtrinsicObject doc WHERE doc.id IN (SELECT doc.id FROM ExtrinsicObject doc, ExternalIdentifier uId WHERE (uId.registryObject = doc.id AND uId.identificationScheme = 'urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab' AND uId.value = '1.2.3.4.100000022002209036.1196211173506.1') )";
		String repaired = repair_query_text_leaf(sql);
		assertTrue("Repaired text is: " + repaired, repaired.startsWith(" SELECT * FROM ExtrinsicObject doc WHERE doc.id IN  ( SELECT * FROM ExtrinsicObject"));
	}

	public void test_nested_select_ref()  throws XdsInternalException, SqlRepairException {
		String sql = "SELECT doc.id FROM ExtrinsicObject doc WHERE doc.id IN (SELECT doc.id FROM ExtrinsicObject doc, ExternalIdentifier uId WHERE (uId.registryObject = doc.id AND uId.identificationScheme = 'urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab' AND uId.value = '1.2.3.4.100000022002209036.1196211173506.1') )";
		String repaired = repair_query_text_ref(sql);
		assertTrue("Repaired text is: " + repaired, repaired.startsWith(" SELECT doc.id FROM ExtrinsicObject doc WHERE doc.id IN  ( SELECT doc.id FROM ExtrinsicObject doc"));
	}


}
