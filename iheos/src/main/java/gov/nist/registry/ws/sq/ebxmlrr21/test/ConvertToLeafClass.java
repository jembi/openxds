package gov.nist.registry.ws.sq.ebxmlrr21.test;

import java.util.List;

import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.storedquery.StoredQuerySupport;
import gov.nist.registry.ws.sq.ebxmlrr21.EbXML21QuerySupport;
import gov.nist.registry.ws.sq.test.TestBase;
import gov.nist.registry.ws.sq.test.testdata.FindDocsTestData;

import org.testng.annotations.Test;

public class ConvertToLeafClass extends TestBase {
	FindDocsTestData testdata;

	public ConvertToLeafClass() throws Exception {
		super();
		testdata = new FindDocsTestData();
	}

	@Test
	public void objectRefToObjectRef() throws Exception {
		StoredQuerySupport sqs = new StoredQuerySupport(this , log);
		EbXML21QuerySupport eqs = new EbXML21QuerySupport(sqs);
		sqs.return_leaf_class = false;
		Metadata m = new Metadata();

		m.mkObjectRefs(testdata.getDocUuids());
		assert m.getObjectRefs().size() == 2;
		assert m.getLeafClassObjects().size() == 0;

		Metadata m2 = eqs.convertToObjectRefs(m, false);
		assert m2.getObjectRefs().size() == 2;
		assert m2.getLeafClassObjects().size() == 0;

		Metadata m3 = eqs.convertToObjectRefs(m, true);
		assert m3.getObjectRefs().size() == 0;
		assert m3.getLeafClassObjects().size() == 0;
	}
	
	@Test
	public void objectRefToLeafClass() throws Exception {
		StoredQuerySupport sqs = new StoredQuerySupport(this , log);
		EbXML21QuerySupport eqs = new EbXML21QuerySupport(sqs);
		sqs.return_leaf_class = true;
		Metadata m = new Metadata();
		
		m.mkObjectRefs(testdata.getDocUuids());

		Metadata m2 = eqs.convertToLeafClass(m);
		assert m.getObjectRefs().size() == 2;
		assert m.getLeafClassObjects().size() == 0;
		assert m2.getLeafClassObjects().size() == 2;
		
		List<String> objectRefIds = m.getObjectRefIds();
		for (String id : objectRefIds) 
			assert m2.containsObject(id);
	}
	
	@Test
	public void leafClassToLeafClass() throws Exception  {
		StoredQuerySupport sqs = new StoredQuerySupport(this , log);
		EbXML21QuerySupport eqs = new EbXML21QuerySupport(sqs);
		sqs.return_leaf_class = true;
		Metadata m = new Metadata();
		
		m.mkObjectRefs(testdata.getDocUuids());
		assert m.getObjectRefs().size() == 2;
		assert m.getLeafClassObjects().size() == 0;

		Metadata m2 = eqs.convertToLeafClass(m);
		assert m2.getLeafClassObjects().size() == 2;

		Metadata m3 = eqs.convertToLeafClass(m);
		assert m3.getLeafClassObjects().size() == 2;
		
		List<String> objectRefIds = m.getObjectRefIds();
		for (String id : objectRefIds) 
			assert m3.containsObject(id);
	}
	
	@Test
	public void leafClassToObjectRef() throws Exception {
		StoredQuerySupport sqs = new StoredQuerySupport(this , log);
		EbXML21QuerySupport eqs = new EbXML21QuerySupport(sqs);
		sqs.return_leaf_class = true;
		Metadata m = new Metadata();
		
		m.mkObjectRefs(testdata.getDocUuids());
		assert m.getObjectRefs().size() == 2;
		assert m.getLeafClassObjects().size() == 0;
		
		Metadata m2 = eqs.convertToLeafClass(m);
		assert m2.getLeafClassObjects().size() == 2;
		
		Metadata m3 = eqs.convertToObjectRefs(m2, true);
		assert m3.getLeafClassObjects().size() == 0;
		assert m3.getObjectRefs().size() == 2;

		m2.clearObjectRefs();
		Metadata m4 = eqs.convertToObjectRefs(m2, false);
		assert m4.getLeafClassObjects().size() == 0;
		assert m4.getObjectRefs().size() == 2;
	}

}
