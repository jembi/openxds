package gov.nist.registry.ws.sq.test.testdata;

import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.xml.Util;

import java.io.File;

import javax.xml.parsers.FactoryConfigurationError;

import org.apache.axiom.om.OMElement;

public class TestLogSupport {

	String patientId;
	protected OMElement logEle = null;

	protected void connectToLog(String logfileName)  {
		String testLogDirPath = System.getenv("XDSTESTLOGDIR");
		assert testLogDirPath != null;
		
		String logPath = testLogDirPath + "/" + logfileName;
		File logFile = new File(logPath);
		assert logFile.canRead();
		
		try {
			 logEle = Util.parse_xml(logFile);
		} catch (XdsInternalException e) {
			assert false;
		} catch (FactoryConfigurationError e) { 
			assert false;
		}
		
		String status = null;
		try {
			status = Util.getAttributeValue(logEle, "/TestResults/@status");
		} catch (Exception e1) {}
		assert status.equals("Pass");
		
		// make sure test points to localhost
		// usually one of the first two TestSteps has an endpoint
		String endpoint0 = null;
		String endpoint1 = null;
		try {
			endpoint0 = Util.getElementValue(logEle, "//TestStep[1]/RegisterTransaction/Endpoint");
		} catch (Exception e) {}
		try {
			endpoint1 = Util.getElementValue(logEle, "//TestStep[2]/RegisterTransaction/Endpoint");
		} catch (Exception e) {}
		
		assert (endpoint0 != null && endpoint0.startsWith("http://localhost")) ||
		(endpoint1 != null && endpoint1.startsWith("http://localhost"));
		
		// make sure test has real patient id
		// usually one of the first two TestSteps has one
		String patientId0 = null;
		String patientId1 = null;
		 try {
			patientId0 = Util.getAttributeValue(logEle,
					"//TestStep[1]/RegisterTransaction/AssignedPatientId/Assign[@symbol='Document01']/@id");
		} catch (Exception e) {}
		 try {
			patientId1 = Util.getAttributeValue(logEle,
			"//TestStep[2]/RegisterTransaction/AssignedPatientId/Assign[@symbol='Document01']/@id");
		} catch (Exception e) {}
		
		assert (patientId0 != null && patientId0.endsWith("ISO")) ||
		(patientId1 != null && patientId1.endsWith("ISO"));
		
		if (patientId0 != null)
			patientId = patientId0;
		else
			patientId = patientId1;
	}

	public String getPatientId() {
		return patientId;
	}

}
