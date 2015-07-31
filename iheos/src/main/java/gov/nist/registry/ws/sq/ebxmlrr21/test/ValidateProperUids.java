package gov.nist.registry.ws.sq.ebxmlrr21.test;

import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.storedquery.StoredQuerySupport;
import gov.nist.registry.ws.sq.ebxmlrr21.EbXML21QuerySupport;
import gov.nist.registry.ws.sq.test.TestBase;
import gov.nist.registry.ws.sq.test.testdata.FindDocsTestData;

import java.util.List;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.testng.annotations.Test;

public class ValidateProperUids extends TestBase {

	FindDocsTestData testdata;

	public ValidateProperUids() throws Exception {
		super();
	}

	@Test
	public void newUids() throws Exception {
		StoredQuerySupport sqs = new StoredQuerySupport(this , log);
		EbXML21QuerySupport eqs = new EbXML21QuerySupport(sqs);
		testdata = new FindDocsTestData();

		List<OMElement> eoElements = testdata.getDocElements();
		Metadata m = new Metadata();
		m.addToMetadata(eoElements, false);

		// assign new uids that don't exist in registry
		String uid_prefix = "999.999.9.";
		int counter = 1;
		for (OMElement ele : eoElements) {
			String id = m.getId(ele);
			OMAttribute uid_att = m.getUniqueIdAttribute(id);
			uid_att.setAttributeValue(uid_prefix + Integer.toString(counter));
			counter++;
		}

		try {
			eqs.validateProperUids(m);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			assert false;
		}
	}

	@Test
	public void sameUids() throws Exception {
		StoredQuerySupport sqs = new StoredQuerySupport(this , log);
		EbXML21QuerySupport eqs = new EbXML21QuerySupport(sqs);
		testdata = new FindDocsTestData();

		List<OMElement> eoElements = testdata.getDocElements();
		Metadata m = new Metadata();
		m.addToMetadata(eoElements, false);

		try {
			eqs.validateProperUids(m);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			assert false;
		}
	}

	@Test
	public void differentHash() throws Exception {
		StoredQuerySupport sqs = new StoredQuerySupport(this , log);
		EbXML21QuerySupport eqs = new EbXML21QuerySupport(sqs);
		testdata = new FindDocsTestData();

		List<OMElement> eoElements = testdata.getDocElements();
		Metadata m = new Metadata();
		m.addToMetadata(eoElements, false);
		
		for (OMElement eo : eoElements) {
			m.setSlotValue(eo, "hash", 0, "ee34");
		}

		try {
			eqs.validateProperUids(m);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			assert false;
		}
		assert false;
	}
}
