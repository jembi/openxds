package gov.nist.registry.ws.sq.test.testdata;

import gov.nist.registry.common2.xml.Util;

import java.util.ArrayList;
import java.util.List;

public class FindSSTestData extends TestLogSupport {
	List<String> ssUuids;
	List<String> docUuids;

	public FindSSTestData() throws Exception {
		connectToLog("selftest/findss/log.xml");
		
		ssUuids = new ArrayList<String>();
		docUuids = new ArrayList<String>();
		
		ssUuids.add(Util.getAttributeValue(logEle,
				"//TestStep[@id='sub1']/RegisterTransaction/AssignedUuids/Assign[@symbol='SubmissionSet01']/@id"));
		ssUuids.add(Util.getAttributeValue(logEle,
		"//TestStep[@id='sub2']/RegisterTransaction/AssignedUuids/Assign[@symbol='SubmissionSet01']/@id"));

		docUuids.add(Util.getAttributeValue(logEle,
		"//TestStep[@id='sub1']/RegisterTransaction/AssignedUuids/Assign[@symbol='Document01']/@id"));

		docUuids.add(Util.getAttributeValue(logEle,
		"//TestStep[@id='sub2']/RegisterTransaction/AssignedUuids/Assign[@symbol='Document01']/@id"));

		assert ssUuids.size() == 2;
		assert docUuids.size() == 2;
		
	}

	public List<String> getSSUuids() {
		return ssUuids;
	}

	public List<String> getDocUuids() {
		return docUuids;
	}
	

}
