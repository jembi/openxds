package gov.nist.registry.common2.errormgr;

import gov.nist.registry.common2.exception.ExceptionUtil;
import gov.nist.registry.common2.registry.MetadataSupport;

import java.util.ArrayList;
import java.util.List;

import org.apache.axiom.om.OMElement;

public class ErrorManager {
	
	List<String> errors;
	List<String> info;
	List<String> warnings;
	List <AssertionResult> assertions;
	boolean fatal;

	public ErrorManager() {
		errors = null;
		info = null;
		warnings = null;
		fatal = false;
	}
	
	public void setFatal () {
		fatal = true;
	}
	
	public boolean isFatal() {
		return fatal;
	}
	
	public ErrorManager(String err) {
		add(err);
	}

	public ErrorManager add(String msg) {
		if (errors == null)
			errors = new ArrayList<String>();
		errors.add(msg);
		return this;
	}

	public ErrorManager add(Exception e) {
		return add(ExceptionUtil.exception_details(e));
	}

	public ErrorManager addInfo(String s) {
		if (info == null)
			info = new ArrayList<String>();
		info.add(s);
		return this;
	}

	public ErrorManager addWarning(String s) {
		if (warnings == null)
			warnings = new ArrayList<String>();
		warnings.add(s);
		return this;
	}

	public String getInfoString() {
		if (info == null)
			return "";
		StringBuffer buf = new StringBuffer();

		for (String e : info) {
			buf.append(e).append("\n");
		}

		return buf.toString();
	}

	void addElement(OMElement root, String name, String value) {
		OMElement ele = MetadataSupport.om_factory.createOMElement(name, null);
		ele.setText(value.replace("<", "[").replace(">", "]"));
		root.addChild(ele);
	}

	public void asXml(OMElement root, String errorElementName, String infoElementName) {
		if (errors != null ) {
			for (String e : errors) {
				addElement(root, errorElementName, e);
			}
		}

		if (warnings != null && infoElementName != null) {
			for (String e : warnings) {
				addElement(root, infoElementName, "Warning: " + e);
			}
		}

		if (info != null && infoElementName != null) {
			for (String e : info) {
				addElement(root, infoElementName, e);
			}
		}
	}

	public List<String> getErrors() {
		return errors;
	}

	public boolean hasErrors() {
		return errors != null && errors.size() > 0;
	}

	public String getErrorString() {
		if (errors == null)
			return "";

		StringBuffer buf = new StringBuffer();

		for (String e : errors) {
			buf.append(e).append("\n");
		}

		return buf.toString();
	}

	public String toString() {
		return getErrorString();
	}
}
