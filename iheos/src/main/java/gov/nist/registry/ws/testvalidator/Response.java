package gov.nist.registry.ws.testvalidator;

import gov.nist.registry.common2.registry.MetadataSupport;

import java.util.ArrayList;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;

public class Response {
	public final static String status_success = "Success";
	public final static String status_notimplemented = "NotImplemented";
	public final static String status_failure = "Failure";
	public final static String status_fatalerror = "FatalError";

	String status;
	ArrayList<String> errors = null;
	ArrayList<String> warnings = null;

	public Response() {
		status = status_success;
	}

	public void addError(String msg) {
		if ( ! status.equals(status_fatalerror))
			status = status_failure;
		if (errors == null)
			errors = new ArrayList<String>();
		errors.add(msg);
	}

	public void addWarning(String msg) {
		if (warnings == null)
			warnings = new ArrayList<String>();
		warnings.add(msg);
	}

	public void addFatalError(String msg) {
		status = status_fatalerror;
		if (errors == null)
			errors = new ArrayList<String>();
		errors.add(msg);
	}

	public void setNotImplemented() {
		status = status_notimplemented;
	}

	public OMElement toXml() {
		OMElement doc = MetadataSupport.om_factory.createOMElement("ValidateTestResponse", null);
		OMAttribute status_att = MetadataSupport.om_factory.createOMAttribute("status", null, status);
		doc.addAttribute(status_att);

		if (errors != null) {
			for ( String error_text : errors) {
				OMElement error_ele = MetadataSupport.om_factory.createOMElement("Error", null);
				error_ele.setText(error_text);
				doc.addChild(error_ele);
			}
		}

		if (warnings != null) {
			for ( String warning_text : warnings) {
				OMElement warning_ele = MetadataSupport.om_factory.createOMElement("Warning", null);
				warning_ele.setText(warning_text);
				doc.addChild(warning_ele);
			}
		}

		return doc;
	}
}
