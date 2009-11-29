package gov.nist.registry.ws.sq.ebxmlrr21.test;

import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.storedquery.SqParams;
import gov.nist.registry.common2.registry.storedquery.StoredQuerySupport;
import gov.nist.registry.ws.sq.ebxmlrr21.EbXML21QuerySupport;
import gov.nist.registry.ws.sq.test.TestBase;
import gov.nist.registry.ws.sq.test.testdata.FindDocsTestData;

import org.testng.annotations.Test;

public class BasicQuerySupport  extends TestBase { 
	FindDocsTestData testdata;
	
	public BasicQuerySupport() throws Exception {
		super();
		testdata = new FindDocsTestData();
	}

	@Test
	public void basicLoadLeafClass() throws Exception {
		StoredQuerySupport sqs = new StoredQuerySupport(this , log);
		SqParams params = new SqParams();
		sqs.params = params;
		sqs.return_leaf_class = false;
		
		EbXML21QuerySupport qs = new EbXML21QuerySupport(sqs);
		
		String id = testdata.getDocUuids().get(0);
		
		Metadata m = new Metadata();
		m.mkObjectRef(id);
		
		assert m.getObjectRefs().size() == 1;
		
		Metadata m2 = qs.convertToLeafClass(m);
		
		assert m2.getExtrinsicObjects().size() == 1;
		assert m2.getExtrinsicObjectIds().get(0).equals(id);
	}
}
