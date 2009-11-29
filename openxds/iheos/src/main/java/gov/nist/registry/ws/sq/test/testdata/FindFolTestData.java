package gov.nist.registry.ws.sq.test.testdata;

import gov.nist.registry.common2.xml.Util;

import java.util.ArrayList;
import java.util.List;

public class FindFolTestData  extends TestLogSupport {
	List<String> ssUuids;
	List<String> docUuids;
	List<String> folUuids;

	public FindFolTestData() throws Exception {
		connectToLog("selftest/findfol/log.xml");
				
		ssUuids = new ArrayList<String>();
		docUuids = new ArrayList<String>();
		folUuids = new ArrayList<String>();
		
		ssUuids.add(Util.getAttributeValue(logEle,
				"//TestStep[@id='submit_doc_w_fol']/RegisterTransaction/AssignedUuids/Assign[@symbol='SubmissionSet01']/@id"));
		ssUuids.add(Util.getAttributeValue(logEle,
			"//TestStep[@id='submit_doc_w_fol2']/RegisterTransaction/AssignedUuids/Assign[@symbol='SubmissionSet01']/@id"));

		docUuids.add(Util.getAttributeValue(logEle,
		"//TestStep[@id='submit_doc_w_fol']/RegisterTransaction/AssignedUuids/Assign[@symbol='Document01']/@id"));
		docUuids.add(Util.getAttributeValue(logEle,
		"//TestStep[@id='submit_doc_w_fol2']/RegisterTransaction/AssignedUuids/Assign[@symbol='Document01']/@id"));

		folUuids.add(Util.getAttributeValue(logEle,
		"//TestStep[@id='submit_doc_w_fol']/RegisterTransaction/AssignedUuids/Assign[@symbol='Folder']/@id"));
		folUuids.add(Util.getAttributeValue(logEle,
		"//TestStep[@id='submit_doc_w_fol2']/RegisterTransaction/AssignedUuids/Assign[@symbol='Folder']/@id"));


		assert ssUuids.size() == 2;
		assert docUuids.size() == 2;
		assert folUuids.size() == 2;
		
	}

	public String getPatientId() {
		return patientId;
	}

	public List<String> getSsUuids() {
		return ssUuids;
	}

	public List<String> getDocUuids() {
		return docUuids;
	}

	public List<String> getFolUuids() {
		return folUuids;
	}
}
