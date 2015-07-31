package gov.nist.registry.ws.sq.test.testdata;

import gov.nist.registry.common2.xml.Util;

import java.util.ArrayList;
import java.util.List;

import org.apache.axiom.om.OMElement;



public class FindDocsTestData extends TestLogSupport {
	List<String> docUuids;
	List<OMElement> docElements;
	
	public FindDocsTestData() throws Exception {
		connectToLog("selftest/finddocs/log.xml");
		
		docUuids = new ArrayList<String>();
		docUuids.add(Util.getAttributeValue(logEle,
				"//TestStep[@id='submit_2doc']/RegisterTransaction/AssignedUuids/Assign[@symbol='Document01']/@id"));
		docUuids.add(Util.getAttributeValue(logEle,
		"//TestStep[@id='submit_2doc']/RegisterTransaction/AssignedUuids/Assign[@symbol='Document02']/@id"));
		
		assert docUuids.size() == 2;
		
		docElements = Util.getElements(logEle, 
				"/TestResults/TestStep[2]/RegisterTransaction[1]/InputMetadata[1]/*[namespace-uri()='urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0' and local-name()='SubmitObjectsRequest'][1]/*[namespace-uri()='urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0' and local-name()='RegistryObjectList'][1]/*[namespace-uri()='urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0' and local-name()='ExtrinsicObject']");
		
		assert docElements.size() == 2;
		
	}

	public List<String> getDocUuids() {
		return docUuids;
	}

	public List<OMElement> getDocElements() {
		return docElements;
	}



}
