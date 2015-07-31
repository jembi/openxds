package gov.nist.registry.ws;

import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.XdsCommon;
import gov.nist.registry.common2.registry.validation.ValidationEngine;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis2.context.MessageContext;

public class StoredQueryRequestSoapValidator extends ValidationEngine {
	short xds_version;
	MessageContext messageContext;
	
	protected String getTFReferences() {
		return "3.18.3 Referenced Standards";
	}


	
	public StoredQueryRequestSoapValidator(short xds_version, MessageContext messageContext) {
		this.xds_version = xds_version;
		this.messageContext = messageContext;
	}
	
	public void run() {
		SoapHeader h = new SoapHeader(messageContext);
		
//		SOAPEnvelope env = messageContext.getEnvelope();
//		OMNamespace ns = env.getNamespace();
//		boolean isSOAP12 = ns.getNamespaceURI().contains("http://www.w3.org/2003/05/soap-envelope");
//		boolean isSOAP11 = ns.getNamespaceURI().contains("http://schemas.xmlsoap.org/soap/envelope/");

		if (xds_version == XdsCommon.xds_a) {
			if ( h.isSOAP12()) 
				newError("A SOAP 1.1 endpoint (XDS.a SQ) was used but message was in SOAP 1.2 format. SOAP Namespace is ")
				.appendError(h.getNs().getNamespaceURI());
			else if ( h.isSOAP11() )
				;
			else 
				newError("A SOAP 1.1 endpoint (XDS.a SQ) was used but message was in unknown SOAP format. SOAP Namespace is ")
				.appendError(h.getNs().getNamespaceURI());
				
		} else if (xds_version == XdsCommon.xds_b) {
			if ( h.isSOAP11() ) 
				newError("A SOAP 1.2 endpoint (XDS.b SQ) was used but message was in SOAP 1.1 format. SOAP Namespace is ")
				.appendError(h.getNs().getNamespaceURI());
			else if ( h.isSOAP12() )
				;
			else
				newError("A SOAP 1.2 endpoint (XDS.b SQ) was used but message was in unknown SOAP format. SOAP Namespace is ")
				.appendError(h.getNs().getNamespaceURI());
			
			OMElement hdr = MetadataSupport.firstChildWithLocalName(h.getEnv(), "Header");
			if (hdr == null) {
				newError("A SOAP 1.2 endpoint (XDS.b SQ) was used but no SOAP Header was found.");
			}
			OMElement to = MetadataSupport.firstChildWithLocalName(hdr, "To");
			if (to == null) {
				newError("A SOAP 1.2 endpoint (XDS.b SQ) was used but SOAP Header <To> is missing.");
			} else {
				try {
					new URL(to.getText());
				} catch (MalformedURLException e) {
					newError("A SOAP 1.2 endpoint (XDS.b SQ) was used but SOAP Header <To> does not parse as a URL.");
					appendError(" Value is <");
					appendError(to.getText());
					appendError(">");
				}
			}
			
			OMElement action = MetadataSupport.firstChildWithLocalName(hdr, "Action");
			if (action == null) 
				newError("A SOAP 1.2 endpoint (XDS.b SQ) was used but SOAP Header <Action> is missing.");

				
		}
		else
			newError("StoredQueryRequestSoapValidator: internal error - request is labeled as neither XDS.a nor XDS.b");
	}


}
