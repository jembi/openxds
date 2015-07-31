package gov.nist.registry.common2.testkit;

import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.xml.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.parsers.FactoryConfigurationError;

import org.apache.axiom.om.OMElement;

public class LogFile {
	OMElement log;
	boolean success;
	List<TestStepLog> steps;
	String test;

	public List<TestStepLog> getStepLogs() throws Exception {
		parseTestSteps();
		return steps;
	}

	public LogFile(File logfile) throws FactoryConfigurationError, Exception {
		log = Util.parse_xml(logfile);
		init();
	}

	public LogFile(OMElement testresults) throws NotALogFileException, Exception {
		log = testresults;
		init();
	}

	public OMElement getLog() { 
		return log;
	}

	public Metadata getAllMetadata()  {
		Metadata m = new Metadata();

		for (TestStepLog step : steps) {
			try {
				Metadata m1 = step.getMetadata();
				m.addMetadata(m1);
			} catch (Exception e) {}
		}

		return m;
	}

	String firstNChars(String s, int n) {
		if (s.length() > n) 
			return s.substring(0, n);
		return s;
	}

	private void init() throws NotALogFileException, Exception {
		steps = new ArrayList<TestStepLog>();
		try {
			parseStatus();
		} catch (Exception e) {
			// not a TestLog file - ignore
			throw new NotALogFileException(e.getMessage() + " log is [" + firstNChars(log.toString(), 20) + "]");
		}
		parseTest();
		parseTestSteps();
	}

	public String getFatalError() {
		OMElement ele = MetadataSupport.firstChildWithLocalName(log, "FatalError");
		if (ele == null)
			return null;
		return ele.getText();
	}

	public boolean hasFatalError() {
		return getFatalError() != null;
	}

	public boolean isSuccess() {
		return success;
	}

	public TestStepLog getTestStepLog(int index) throws XdsInternalException {
		if (index >= steps.size())
			throw new XdsInternalException("Step index " + index + " is illegal, there are " + steps.size() + " steps");
		return steps.get(index);
	}

	public List<String> getAssertionErrors(int index) throws XdsInternalException {
		return getTestStepLog(index).getAssertionErrors();
	}

	public List<String> getSoapFaults(int index) throws XdsInternalException {
		return getTestStepLog(index).getSoapFaults();
	}

	void parseStatus() throws Exception {
		String stat = log.getAttributeValue(new QName("status"));
		if (stat == null)
			throw new Exception("Log file status not available. Log element is " + log.getLocalName());
		if (stat.equals("Pass"))
			success = true;
		else
			success = false;
	}

	void parseTest() {
		test = MetadataSupport.firstChildWithLocalName(log, "Test").getText();
	}

	void parseTestSteps() throws Exception {
		steps = new ArrayList<TestStepLog>();
		List<OMElement> stepEles = MetadataSupport.childrenWithLocalName(log, "TestStep");
		for (OMElement step : stepEles) {
			steps.add(new TestStepLog(step));
		}
	}

	public RegistryResponseLog getUnexpectedErrors(int step) throws Exception {
		if (steps == null || step < 0 || step >= steps.size())
			throw new Exception("LogFile#getUnexpectedErrors: step index " + step + " does not exist");
		return steps.get(step).getUnexpectedErrors();
	}

	public String stepName(int step) throws Exception {
		if (step < 0 || step >= steps.size())
			throw new Exception("LogFile#stepName: step index " + step + " does not exist");
		return steps.get(step).id;
	}

	public int size() {
		return steps.size();
	}

	public boolean hasStep(String stepname) {
		for (int i=0; i<size(); i++) {
			if (stepname.equals(steps.get(i).id))
				return true;
		}
		return false;
	}

	public String getTest() {
		return test;
	}


}
