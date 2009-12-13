package gov.nist.registry.common2.testkit;

import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataParser;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.xml.XmlFormatter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.axiom.om.OMElement;

public class TestStepLog {
	String id;
	boolean success;
	boolean expectedSuccess;
	OMElement root;

	public TestStepLog(OMElement root) throws Exception {
		this.root = root;
		String stat = root.getAttributeValue(MetadataSupport.status_qname);

		// hack until NewPatientId instructions generates proper status
		if (stat == null)
			success = true;
		else
			success = "Pass".equals(stat);
		id = root.getAttributeValue(MetadataSupport.id_qname);

		OMElement expectedStatusEle = MetadataSupport.firstChildWithLocalName(root, "ExpectedStatus");
		if (expectedStatusEle == null)
			throw new Exception("TestStep: Error parsing log.xml file: cannot extract ExpectedStatus from test step " + id );
		String expStat = expectedStatusEle.getText();
		if ("Success".equals(expStat))
			expectedSuccess = true;
		else if ("Failure".equals(expStat))
			expectedSuccess = false;
		else
			throw new Exception("TestStep: Error parsing log.xml file: illegal value (" + expStat + ") for ExpectedStatus element of step " + id);
	}

	public boolean getStatus() {
		return success;
	}

	public String getName() {
		return id;
	}

	/**
	 * Get response message for this test step.
	 * @return OMElement of message
	 * @throws Exception if no result
	 */
	@SuppressWarnings("unchecked")
	public OMElement getRawResult() throws Exception {
		for (Iterator<OMElement> it=root.getChildElements(); it.hasNext(); ) {
			OMElement ele1 = it.next();
			for (Iterator<OMElement> it2=ele1.getChildElements(); it2.hasNext(); ) {
				OMElement ele2 = it2.next();
				if ("Result".equals(ele2.getLocalName())) {
					return ele2.getFirstElement();
				}
			}
		}
		throw new Exception("Step: " + id + " has no <Result/> block");
	}

	public RegistryResponseLog getRegistryResponse() throws Exception {
		return new RegistryResponseLog(getRawResult());
	}

	public Metadata getMetadata() throws Exception {
		return MetadataParser.parseNonSubmission(getRawResult());
	}

	public RegistryResponseLog getUnexpectedErrors() throws Exception {
		return new RegistryResponseLog(getRegistryResponse().getErrorsDontMatch(getExpectedErrorMessage()));
	}

	public String getExpectedErrorMessage() {
		OMElement expEle = root.getFirstChildWithName(MetadataSupport.expected_error_message_qname);
		if (expEle == null)
			return null;
		return expEle.getText();
	}

	public String getEndpoint() {
		List<OMElement> endpoints = MetadataSupport.decendentsWithLocalName(root, "Endpoint");
		if (endpoints.isEmpty())
			return null;
		return endpoints.get(0).getText();
	}

	public List<String> getAssertionErrors() {
		List<OMElement> errorEles = MetadataSupport.decendentsWithLocalName(root, "Error");
		List<String> errors = new ArrayList<String>();
		
		for (OMElement errorEle : errorEles ){
			errors.add(errorEle.getText());
		}
		
		return errors;
	}

	public List<String> getErrors() throws Exception {
		List<String> errors = new ArrayList<String>();

		RegistryResponseLog rrl = getUnexpectedErrors();
		for (int i=0; i<rrl.size(); i++) {
			RegistryErrorLog rel = rrl.getError(i);
			errors.add(rel.getSummary());
		}

		errors.addAll(getAssertionErrors());

		return errors;
	}


	public List<String> getSoapFaults() {
		List<String> errs = new ArrayList<String>();

		for (OMElement errEle : MetadataSupport.childrenWithLocalName(root, "SOAPFault")) {
			String err = errEle.getText();
			errs.add(id + ": " + err);
		}

		return errs;
	}

	public OMElement getInputMetadata() {
		try {
			return MetadataSupport.firstDecendentWithLocalName(root, "InputMetadata").getFirstElement();
		} catch (Exception e) {
			return null;
		}
	}

	public OMElement getResult() {
		try {
			return MetadataSupport.firstDecendentWithLocalName(root, "Result").getFirstElement();
		} catch (Exception e) {
			return null;
		}
	}

	public OMElement getInHeader() {
		try {
			return MetadataSupport.firstDecendentWithLocalName(root, "InHeader").getFirstElement();
		} catch (Exception e) {
			return null;
		}
	}

	public OMElement getOutHeader() {
		try {
			return MetadataSupport.firstDecendentWithLocalName(root, "OutHeader").getFirstElement();
		} catch (Exception e) {
			return null;
		}
	}
	
	public OMElement getRoot() {
		return root;
	}
}
