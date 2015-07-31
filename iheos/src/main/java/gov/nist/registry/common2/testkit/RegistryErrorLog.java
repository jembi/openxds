package gov.nist.registry.common2.testkit;

import gov.nist.registry.common2.registry.MetadataSupport;

import org.apache.axiom.om.OMElement;

public class RegistryErrorLog {
	String errorCode;
	String codeContext;
	String location;
	String severity;
	OMElement registryErrorEle;
	
	public RegistryErrorLog(OMElement registryErrorEle) {
		this.registryErrorEle = registryErrorEle;
		errorCode = registryErrorEle.getAttributeValue(MetadataSupport.error_code_qname);
		codeContext = registryErrorEle.getAttributeValue(MetadataSupport.code_context_qname);
		location = registryErrorEle.getAttributeValue(MetadataSupport.location_qname);
		severity = registryErrorEle.getAttributeValue(MetadataSupport.severity_qname);
	}
	
	public String getSummary() {
		return errorCode + " : " + codeContext;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("[RegistryErrorLog:\n");
		
		buf.append("\terrorCode=");
		buf.append(errorCode);
		buf.append("\n");
		
		buf.append("\tcodeContext=");
		buf.append(codeContext);
		buf.append("\n");

		buf.append("\tlocation=");
		buf.append(location);
		buf.append("\n");

		buf.append("\tseverity=");
		buf.append(severity);
		buf.append("\n");
		
//		buf.append("ErrorElement=\n");
//		buf.append(registryErrorEle);

		buf.append("]\n");
		
		return buf.toString();
	}
}
