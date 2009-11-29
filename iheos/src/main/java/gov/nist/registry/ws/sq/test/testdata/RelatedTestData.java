package gov.nist.registry.ws.sq.test.testdata;

import gov.nist.registry.common2.xml.Util;

public class RelatedTestData extends TestLogSupport {
	String origDocId;
	String rplcDocId;

	public RelatedTestData() throws Exception {
		connectToLog("selftest/related/log.xml");

		origDocId = Util.getAttributeValue(logEle, 
		"//TestStep[@id='submit_doc_for_rplc']/RegisterTransaction/AssignedUuids/Assign[@symbol='Document01']/@id");
		assert origDocId != null;

		rplcDocId = Util.getAttributeValue(logEle, 
		"//TestStep[@id='rplc']/RegisterTransaction/AssignedUuids/Assign[@symbol='Document01']/@id");
		assert rplcDocId != null;
	}

	public String getOrigDocId() {
		return origDocId;
	}

	public String getRplcDocId() {
		return rplcDocId;
	}
}
