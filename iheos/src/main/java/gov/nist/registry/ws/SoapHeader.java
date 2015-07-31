package gov.nist.registry.ws;

import gov.nist.registry.common2.registry.MetadataSupport;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.context.MessageContext;

public class SoapHeader {
	SOAPEnvelope env;
	boolean isSOAP12;
	boolean isSOAP11;
	OMElement hdr;
	OMElement to;
	OMElement action;
	OMNamespace ns;

	public SoapHeader(MessageContext messageContext) {
		env = null;
		hdr = null;
		to = null;
		action = null;
		isSOAP12 = false;
		isSOAP11 = false;

		env = messageContext.getEnvelope();
		if (env == null) return;
		ns = env.getNamespace();
		if (ns != null) {
			isSOAP12 = ns.getNamespaceURI().contains("http://www.w3.org/2003/05/soap-envelope");
			isSOAP11 = ns.getNamespaceURI().contains("http://schemas.xmlsoap.org/soap/envelope/");
		}
		hdr = MetadataSupport.firstChildWithLocalName(env, "Header");
		if (hdr == null) return;
		to = MetadataSupport.firstChildWithLocalName(hdr, "To");
		action = MetadataSupport.firstChildWithLocalName(hdr, "Action");
	}

	public SOAPEnvelope getEnv() {
		return env;
	}

	public boolean isSOAP12() {
		return isSOAP12;
	}

	public boolean isSOAP11() {
		return isSOAP11;
	}

	public OMElement getHdr() {
		return hdr;
	}

	public OMElement getTo() {
		return to;
	}

	public OMElement getAction() {
		return action;
	}
	
	public String getActionValue() {
		if (action == null) return null;
		return action.getText();
	}
	
	public String getToValue() {
		if (to == null) return null;
		return to.getText();
	}

	public OMNamespace getNs() {
		return ns;
	}

}
