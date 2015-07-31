package gov.nist.registry.ws.testvalidator;

import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.MetadataSupport;

import java.util.HashMap;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;

public class LogModel {
	String test_num;
	HashMap<String, OMElement> logs;

	public LogModel(String test_num) {
		logs = new HashMap<String, OMElement>();
		this.test_num = test_num;
	}

	public void addLog(OMElement log) throws XdsInternalException, XdsException {
		String root_element_name = log.getLocalName();
		if (root_element_name == null || ( !root_element_name.equals("TestResults") &&   !root_element_name.equals("XdsEvsResult")   ) )
			throw new XdsException("Root of test log is " + root_element_name + " instead of TestResults or XdsEvsResult");
		if (root_element_name.equals("TestResults")) {
			OMElement test_ele = MetadataSupport.firstChildWithLocalName(log, "Test");
			if (test_ele == null) throw new XdsInternalException("Log has no Test element");
			String test = test_ele.getText();
			if (test == null || test.equals("")) throw new XdsInternalException("Log has empty Test element");
			String[] test_parts = test.split("/");
			if (test_parts.length > 1) {
				if ( !this.test_num.equals(test_parts[0])) 
					throw new XdsException("Logfile Test element value, " + test + ", does not belong to test " + this.test_num);
				String section_name = test_parts[1]; 
				if (section_name == null || section_name.equals(""))
					throw new XdsException("Logfile Test element value, " + test + ", has invalid sub-test name (text following / character)");

				logs.put(section_name, log);
			} else {
				logs.put(test, log);
			}
		}
		else if (root_element_name.equals("XdsEvsResult")) {
			OMElement testEle = MetadataSupport.firstChildWithLocalName(log, "Test");
			String test = testEle.getText();
			if (test == null || test.equals("")) throw new XdsInternalException("EVS has empty Test element");
			if ( !test_num.equals(test)) {
				if (test_num.equals("12328") && 
						( 
								test.equals("11936") ||
								test.equals("11937") ||
								test.equals("11938") ||
								test.equals("11939") ||
								test.equals("11940") ||
								test.equals("11941") ||
								test.equals("11942") ||
								test.equals("11943") ||
								test.equals("11944") ||
								test.equals("11945") ||
								test.equals("11946") ||
								test.equals("11947") ||
								test.equals("11948") 
								))
					; // ok - all these SQ tests get reported under test 12328
				else
					throw new XdsException("EVSfile Test element value, " + test + ", does not belong to test " + this.test_num);
			}

			OMElement resultEle = MetadataSupport.firstChildWithLocalName(log, "Result");
			String result = (resultEle != null) ? resultEle.getText() : null;
			if (result == null || result.equals("")) throw new XdsInternalException("EVS has empty Result element");
			logs.put(test, log);
			if (test_num.equals("12328"))
				logs.put("12328", log);
		}
	}

	public boolean hasSubtest(String subtest_name) { return logs.containsKey(subtest_name); }

	public boolean isPass(String subtest_name) throws XdsInternalException, XdsException {
		OMElement log = get_log(subtest_name);
		String eleName = log.getLocalName();
		if (eleName.equals("TestResults")) {
			String status =  log.getAttributeValue(new QName("status"));
			if (status == null || status.equals("")) throw new XdsException("isPass: no status found for subtest " + subtest_name);
			return status.equals("Pass");
		}
		else if (eleName.equals("XdsEvsResult")) {
			OMElement resultEle = MetadataSupport.firstChildWithLocalName(log, "Result");
			String result = (resultEle != null) ? resultEle.getText() : null;
			if (result == null || result.equals("")) throw new XdsInternalException("EVS has empty Result element");
			return result.equals("Pass");
		}
		throw new XdsInternalException("Not TestResults or XdsEvsRequest");
	}

	public void checkFatalError(String subtest_name) throws XdsInternalException, XdsException {
		OMElement log = get_log(subtest_name);
		OMElement fatal_ele = MetadataSupport.firstChildWithLocalName(log, "FatalError");
		if (fatal_ele != null)
			throw new XdsException("Subtest " + subtest_name + "  log file has fatal error: " + fatal_ele.getText());
	}

	OMElement get_log(String subtest_name) throws XdsInternalException, XdsException {
		OMElement log = logs.get(subtest_name);
		if (log == null) throw new XdsInternalException("Internal Error: cannot find subtest name " + subtest_name);
		return log;
	}

}
