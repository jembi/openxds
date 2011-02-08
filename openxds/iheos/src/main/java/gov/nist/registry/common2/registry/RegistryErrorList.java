package gov.nist.registry.common2.registry;

import gov.nist.registry.common2.exception.ExceptionUtil;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.xml.Util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jaxen.JaxenException;
import org.openhealthtools.openexchange.config.PropertyFacade;
import org.openhealthtools.openexchange.syslog.LogMessage;
import org.openhealthtools.openexchange.syslog.LoggerException;

public class RegistryErrorList implements ErrorLogger {
	public final static short version_2 = 2;
	public final static short version_3 = 3;
	String errors_and_warnings = "";
	boolean has_errors = false;
	boolean has_warnings = false;
	OMElement rel = null;
	StringBuffer validations = null;
	short version;
	protected OMNamespace ebRSns;
	protected OMNamespace ebRIMns;
	protected OMNamespace ebQns;
	boolean format_for_html = false;
	private final static Log logger = LogFactory.getLog(RegistryErrorList.class);
	boolean verbose = true;
	boolean log;
	/**Is Responding Gateway*/
	boolean isRG = false;
	
	public void setIsRG() { isRG = true; }
	
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}
	
	public String toString() {
		if (rel == null) return "Null";
		return getRegistryErrorList().toString();
	}

	public RegistryErrorList(short version) throws XdsInternalException {
		init(version, true /* log */);
	}

	public RegistryErrorList(short version, boolean log) throws XdsInternalException {
		init(version, log);
	}

	public void format_for_html(boolean value) { this.format_for_html = value; }

	void init(short version, boolean log)  throws XdsInternalException {
		if (version != version_2 && version != version_3) {
			throw new XdsInternalException("Class gov.nist.registry.ws.Response created without valid version");
		}
		this.version = version;
		if (version == version_2) {
			ebRSns =  MetadataSupport.ebRSns2;  
			ebRIMns = MetadataSupport.ebRIMns2;
			ebQns = MetadataSupport.ebQns2;
		} else {
			ebRSns =  MetadataSupport.ebRSns3;
			ebRIMns = MetadataSupport.ebRIMns3;
			ebQns = MetadataSupport.ebQns3;
		}
		this.log = log;
		this.validations = new StringBuffer();
	}

	public boolean has_errors() {
		return has_errors;
	}

	public String getStatus() {
		if (has_errors())
			return "Failure";
		return "Success";
	}

	public short getVersion() {
		return version;
	}

	public OMElement getRegistryErrorList() {
		//System.out.println(this.validations.toString());
		return registryErrorList();
	}

	OMElement registryErrorList() {
		if (rel == null)
			rel = MetadataSupport.om_factory.createOMElement("RegistryErrorList", ebRSns);
		if (isRG)
			setHomeAsLocation();
		return rel;
	}
	
	static final QName codeContextQName = new QName("codeContext");

	public String getErrorsAndWarnings() {
		//		if ( !format_for_html)
		//			return errors_and_warnings;

		StringBuffer buf = new StringBuffer();
		for (Iterator<OMElement> it=getRegistryErrorList().getChildElements(); it.hasNext(); ) {
			OMElement ele = it.next();
			if (format_for_html)
				buf.append("<p>" + ele.getAttributeValue(codeContextQName) + "</p>\n");
			else
				buf.append(ele.getAttributeValue(codeContextQName)).append("\n");
		}
		return buf.toString();
	}

	public void add_warning(String code, String msg, String location, LogMessage log_message) {
		errors_and_warnings += "Warning: " + msg + "\n";
		warning(msg);
		addWarning(msg, code, location);

		if (log) {
			try {
				if (log_message != null)
					log_message.addErrorParam("Warning", msg);
			} catch (Exception e) {
				// oh well - can't fix it from here
			}
		}
	}

	public void add_validation(String topic, String msg, String location) {
		validations.append(topic + ": " +
				((msg != null) ? msg + " " : "") +
				((location != null) ? " @" + location : "") +
				"\n"
		);
	}

	public void add_error(String code, String msg, String location, LogMessage log_message) {
		errors_and_warnings += "Error: " + code + " " + msg + "\n";
		error(msg);
		addError(msg, code, location);

		if (log) {
			try {
				if (log_message != null)
					log_message.addErrorParam("Error",  msg + "\n" + location);
			} catch (Exception e) {
				// oh well - can't fix it from here
			}
		}
	}

	HashMap<String, String> getErrorDetails(OMElement registryError) {
		HashMap<String, String>  err = new HashMap<String, String>();

		for (Iterator<OMAttribute> it=registryError.getAllAttributes(); it.hasNext(); ) {
			OMAttribute att = it.next();
			String name = att.getLocalName();
			String value = att.getAttributeValue();
			err.put(name, value);
		}

		return err;
	}

	public void addRegistryErrorList(OMElement rel, LogMessage log_message) throws XdsInternalException {
		for (Iterator it=rel.getChildElements(); it.hasNext(); ) {
			OMElement registry_error = (OMElement) it.next();

			if (log_message != null) {
				HashMap<String, String> err = getErrorDetails(registry_error);
				try {
					log_message.addErrorParam("Error", err.get("codeContext"));
				} catch (LoggerException e) {
					throw new XdsInternalException(ExceptionUtil.exception_details(e));
				}
			}


			OMElement registry_error_2 = Util.deep_copy(registry_error);

			logger.error("registry_error2 is \n" + registry_error_2.toString());

			if (this.getVersion() == RegistryErrorList.version_3)
				registry_error_2.setNamespace(MetadataSupport.ebRSns3);
			registryErrorList().addChild(registry_error_2);
			String severity = registry_error.getAttributeValue(MetadataSupport.severity_qname);
			severity = new Metadata().stripNamespace(severity);
			if (severity.equals("Error")) 
				has_errors = true;
			else
				has_warnings = true;
		}
	}


	public boolean hasContent() {
		return this.has_errors || this.has_warnings;
	}


	public void addError(String context, String code, String location)  {
		if (context == null) context = "";
		if (code == null) code = "";
		if (location == null) location = "";
		OMElement error = MetadataSupport.om_factory.createOMElement("RegistryError", ebRSns);
		error.addAttribute("codeContext", context, null);
		error.addAttribute("errorCode", code, null);
		error.addAttribute("location", location, null);
		String severity;
		if (version == version_3) 
			severity = MetadataSupport.error_severity_type_namespace + "Error";
		else
			severity = "Error";
		error.addAttribute("severity", severity, null);
		registryErrorList().addChild(error);
		this.has_errors = true;
	}


	public void delError(String context) {
		OMElement errs = registryErrorList();

		for (Iterator<OMElement> it = errs.getChildElements(); it.hasNext(); ) {
			OMElement e = it.next();
			if (context != null) {
				String ctx = e.getAttributeValue(codeContextQName);
				if (ctx != null && ctx.indexOf(context) != -1)
					e.detach();
				continue;
			}
		}
	}

	public void addWarning(String context, String code, String location) {
		if (context == null) context = "";
		if (code == null) code = "";
		if (location == null) location = "";
		OMElement error = MetadataSupport.om_factory.createOMElement("RegistryError", ebRSns);
		error.addAttribute("codeContext", context, null);
		error.addAttribute("errorCode", code, null);
		error.addAttribute("location", location, null);
		error.addAttribute("severity", "Warning", null);
		registryErrorList().addChild(error);
		this.has_warnings = true;
	}

	public void error(String msg) {
		if (verbose)
		 logger.error("ERROR: " + msg);
	}

	public void warning(String msg) {
		if (verbose)
		 logger.warn("WARNING: " + msg);
	}

	public static String exception_details(Exception e) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		e.printStackTrace(ps);

		return "Exception thrown: " + e.getClass().getName() + "\n" + e.getMessage() + "\n" + new String(baos.toByteArray());
	}

	@SuppressWarnings("unchecked")
	void setHomeAsLocation() {
		String reXPath = "//*[local-name()='RegistryError']";
		String home = PropertyFacade.getString("home.community.id");;
		try {
			AXIOMXPath xpathExpression = new AXIOMXPath (reXPath);
			List<?> nodes = xpathExpression.selectNodes(rel);
			for (OMElement node : (List<OMElement>) nodes) {
				node.addAttribute("location", home, null);
			}
		} catch (JaxenException e) {
		}
	}

}
