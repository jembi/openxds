package gov.nist.registry.ws;

import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.RegistryResponse;
import gov.nist.registry.common2.registry.RegistryUtility;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.XdsCommon;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.MessageContext;
import org.openhealthtools.openexchange.syslog.LogMessage;
import org.openhealthtools.openexchange.syslog.LoggerException;

public class TestImpl11710  extends XdsCommon {
	MessageContext messageContext;

	public TestImpl11710(LogMessage log_message, short xds_version, MessageContext messageContext) {
		this.log_message = log_message;
		this.xds_version = xds_version;
		this.messageContext = messageContext;
		transaction_type = OTHER_transaction;
		try {
			init(new RegistryResponse( (xds_version == xds_a) ?	Response.version_2 : Response.version_3), xds_version, messageContext);
		} catch (XdsInternalException e) {
			System.out.println("Internal Error creating RegistryResponse: " + e.getMessage());
		}
	}

	public OMElement testImpl11710(OMElement input) {

		try {
			if ( !input.getLocalName().equals("hello")) {
				response.add_error("XDSRegistryError", "Don't understand input format", null, log_message);
				return response.getResponse();
			}
			
			OMElement caller = input.getFirstElement();
			if ( !caller.getLocalName().equals("caller")) {
				response.add_error("XDSRegistryError", "Don't understand input format", null, log_message);
				return response.getResponse();
			}
			String vendor = caller.getAttributeValue(new QName("vendor"));
			String tester = caller.getAttributeValue(new QName("tester"));
			String email = caller.getAttributeValue(new QName("email"));
			
			if (vendor.equals("your company name")) {
				response.add_error("XDSRegistryError", "Your company name must be filled in to be valid", null, log_message);
				return response.getResponse();
			}
			
			String ip = messageContext.getFrom().getAddress();
			if (log_message != null)
				log_message.setCompany(vendor);
			
			this.log_response();
			
			return response.getResponse();
		}
		catch (XdsInternalException e) {
			response.add_error("XDSRegistryError", "XDS Internal Error:\n " + e.getMessage(), RegistryUtility.exception_details(e), log_message);
			try {
				return  response.getResponse();
			} catch (XdsInternalException e1) {
				return null;
			}
		} 
		catch (LoggerException e) {
			response.add_error("XDSRegistryError", "Logger Exception:\n " + e.getMessage(), RegistryUtility.exception_details(e), log_message);
			try {
				return  response.getResponse();
			} catch (XdsInternalException e1) {
				return null;
			}
		} 

	}

}
