package gov.nist.registry.common2.registry;

import gov.nist.registry.common2.exception.XdsInternalException;

import java.util.ArrayList;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.openhealthtools.openxds.log.LogMessage;

public abstract class Response implements ErrorLogger {
	public final static short version_2 = 2;
	public final static short version_3 = 3;
	short version;
	/**Is Responding Gateway*/
	boolean isRG = false;
	/**for figuring out particalSuccess*/
	boolean hasSuccess = false; 
	//String status = "Success";
	protected OMNamespace ebRSns;
	protected OMNamespace ebRIMns;
	protected OMNamespace ebQns;

	ArrayList query_results = null;

	boolean errors_and_warnings_included = false;

	//String errors_and_warnings = "";
	//boolean has_errors = false;

	public abstract OMElement getRoot() throws XdsInternalException;
	
	OMElement response = null;

	OMElement content = null;
	public RegistryErrorList registryErrorList;
	
	public void setIsRG() { 
		isRG = true;
	}
	
	public void setHasSuccess() {
		hasSuccess = true;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("Response:\n");
		buf.append("\tversion is ").append(version).append("\n");
		buf.append("\tquery_results is ").append(query_results).append("\n");
		buf.append("\tregistryErrorList is ").append(registryErrorList).append("\n");
		
		return buf.toString();
	}

	public Response(short version) throws XdsInternalException {
		init(version, new RegistryErrorList(version, true /* log */));
	} 

	public Response() throws XdsInternalException {
		init(version_3, new RegistryErrorList(version_3, true /* log */));
	} 

	public Response(short version, RegistryErrorList rel)  throws XdsInternalException {
		init(version, rel);
	}

	public Response(RegistryErrorList rel)  throws XdsInternalException {
		init(version_3, rel);
	}

	void init(short version, RegistryErrorList rel)  throws XdsInternalException {
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

		registryErrorList = rel;		

	}

	abstract public void addQueryResults(OMElement metadata) throws XdsInternalException;

	public OMElement getResponse()  throws XdsInternalException {
		if (version == version_2) {
			response.addAttribute("status", registryErrorList.getStatus(), null);
			if (registryErrorList.hasContent()) {
				OMElement error_list = registryErrorList.getRegistryErrorList();
				if (error_list != null)
					response.addChild(error_list);
			}
		}
		else {
			if (registryErrorList.hasContent()) {
				OMElement error_list = registryErrorList.getRegistryErrorList();
				if (error_list != null)
					response.addChild(error_list);
			}

			String status = registryErrorList.getStatus();
			if (hasSuccess && this.has_errors()) {
				status = "PartialSuccess";
			} 
			response.addAttribute("status", MetadataSupport.response_status_type_namespace + status, null);
			
			setLocationForRG();
			
			if (this instanceof RetrieveMultipleResponse) {
				return ((RetrieveMultipleResponse) this).rdsr;
			} 
			else if (this instanceof RegistryResponse) {
				
			} 
			else if (this instanceof AdhocQueryResponse) {
				AdhocQueryResponse a = (AdhocQueryResponse) this;
				OMElement query_result = a.getQueryResult();
				if (query_result != null)
					response.addChild(query_result);
			} else {
				throw new XdsInternalException("Response.getResponse(): unknown extending class: " + getClass().getName());
			}
			
			
		}
		return response;
	}
	
	void setLocationForRG() {
		if (isRG && registryErrorList != null)
			registryErrorList.setIsRG();
		
	}

	public void add_error(String code, String msg, String location, LogMessage log_message) {
		registryErrorList.add_error(code, msg, location, log_message);
	}

	public void addRegistryErrorList(OMElement rel, LogMessage log_message) throws XdsInternalException {
		registryErrorList.addRegistryErrorList(rel, log_message);
	}

	public void add_warning(String code, String msg, String location, LogMessage log_message) {
		registryErrorList.add_warning(code, msg, location, log_message);
	}

	public String getErrorsAndWarnings() {
		return registryErrorList.getErrorsAndWarnings();
	}

	public void error(String msg) {
		registryErrorList.error(msg);
	}

	public void warning(String msg) {
		registryErrorList.warning(msg);
	}

	public boolean has_errors() {
		return registryErrorList.has_errors();
	}




}
