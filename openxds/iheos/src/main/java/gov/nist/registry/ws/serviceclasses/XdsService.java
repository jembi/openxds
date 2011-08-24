package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.XdsErrorCodeException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.AdhocQueryResponse;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.Properties;
import gov.nist.registry.common2.registry.RegistryErrorList;
import gov.nist.registry.common2.registry.RegistryResponse;
import gov.nist.registry.common2.registry.RetrieveMultipleResponse;
import gov.nist.registry.common2.service.AppendixV;
import gov.nist.registry.ws.log.Fields;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMException;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.Constants;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openxds.XdsFactory;
import org.openhealthtools.openxds.log.Log;
import org.openhealthtools.openxds.log.LoggerException;

public class XdsService extends AppendixV {
	protected Log log = null;
	private final static org.apache.commons.logging.Log logger = LogFactory.getLog(XdsService.class);
	String service_name;
	boolean is_secure;
	static public String technicalFramework = null;
	static public String registerAEndpoint = null;
	static public String registerBEndpoint = null;
	String incoming_ip_address;

	static {
//		BasicConfigurator.configure();
		if (registerAEndpoint == null)
			registerAEndpoint = "http://localhost:9080/" + XdsService.technicalFramework + "/services/xdsregistryainternal";
		if (registerBEndpoint == null)
			registerBEndpoint = "http://localhost:9080/" + XdsService.technicalFramework + "/services/xdsregistryb";
	}

	protected boolean isSecure() { return is_secure; }

	public void useXop() {
		this.return_message_context = MessageContext.getCurrentMessageContext();
		if (return_message_context != null)
			return_message_context.getOptions().setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
	}

	protected String getClientIPAddress() {
		return incoming_ip_address;
	}

	protected OMElement beginTransaction(String service_name, OMElement request, short actor) {

		// This gets around a bug in Leopard (MacOS X 10.5) on Macs
		// Though removing this property fixes a Mac problem, but it disables transactions via proxy server in a production environment
		//System.setProperty("http.nonProxyHosts", "");


		this.service_name = service_name;

		String incoming_ip_address = null;
		if (getMessageContext().getFrom() != null) {
			incoming_ip_address = getMessageContext().getFrom().getAddress();
		} else {
			incoming_ip_address = (String)getMessageContext().getProperty(MessageContext.REMOTE_ADDR);
		}

//		incoming_ip_address = getMessageContext().getFrom().getAddress();
//		for (int i=1; i<100; i++) {
//			String entry = this.properties.getString("BlockIp" + i);
//			if (entry == null || entry.equals(""))
//				break;
//			if (entry.equals(incoming_ip_address))
//				return start_up_error(request, null, actor, "Continuous jamming from IP " + incoming_ip_address + " - access blocked");
//		}


		logger.info("Start " + service_name + " : " + incoming_ip_address  + " : " + getMessageContext().getTo().toString());
		try {
			startTransactionLog();
			if (log != null && log_message == null) {
//				log_message = log.createMessage(getMessageContext().getFrom().getAddress());
				log_message = log.createMessage(incoming_ip_address);
			}
			log_message.addOtherParam(Fields.service, service_name);
			is_secure = getMessageContext().getTo().toString().indexOf("https://") != -1;
			log_message.addHTTPParam(Fields.isSecure, (is_secure) ? "true" : "false");
			log_message.addHTTPParam(Fields.date, getDateTime());
			log_message.setSecure(is_secure);
			if (request != null)
				log_message.addOtherParam("Request", request.toString());
			else {
				log_message.addErrorParam("Error", "Cannot access request body in XdsService.begin_service()");
				return start_up_error(request, null, actor, "Request body is null");
			}

            HashMap transportHeaders = (HashMap)getMessageContext().getProperty("TRANSPORT_HEADERS");
			for (Object o_key : transportHeaders.keySet()) {
				String key = (String) o_key;
				String value = (String) transportHeaders.get(key);
                
				if (logger.isDebugEnabled()) {
					logger.debug("request header=" + key +", value=" + value );
				}

				Vector<String> thdrs = new Vector<String>();
				thdrs.add(key + " : " + value);
				addHttp( "HTTP Header", thdrs ) ;
			}


			if ( getMessageContext().getEnvelope().getHeader() != null )
			{

				try {
					addSoap ( "Soap Header", getMessageContext().getEnvelope().getHeader().toStringWithConsume() ) ;
				}  catch (OMException e) {} catch (XMLStreamException e) {}
			}

			if ( getMessageContext().getEnvelope().getBody() != null )
			{
				try
				{
					addSoap( "Soap Envelope", getMessageContext().getEnvelope().toStringWithConsume() );
				}  catch (OMException e) {} catch (XMLStreamException e) {}
			}
//			log_message.addHTTPParam(Fields.fromIpAddress , getMessageContext().getFrom().getAddress() ) ;  
			log_message.addHTTPParam(Fields.fromIpAddress , incoming_ip_address ) ;  			
			log_message.addHTTPParam(Fields.endpoint , getMessageContext().getTo().toString() ) ; 

			return null;  // no error
		} catch (LoggerException e) {
			logger.error("LoggerException: new Log: " + e.getMessage());
			e.printStackTrace();
			stopTransactionLog();
			return start_up_error(request, e, actor, "Internal Error:Cannot access Test Log Facility");
		}
	}

	protected void endTransaction(boolean status) {
		logger.info(service_name + " " + " : "
				+ ((status) ? "Pass" : "Fail"));

		stopTransactionLog();
	}
	
	public void generateLogMessage(OMElement response) {
		try {
			log_message.addOtherParam("Response", response.toString());
		} catch (Exception e) {
			
		}
	}

	protected OMElement endTransaction(OMElement request, Exception e, short actor, String message) {
		if (message == null || message.equals(""))
			message = e.getMessage();
		logger.error("Exception: " + exception_details(e));
		OMElement response = start_up_error(request, e, actor, message);
		generateLogMessage(response);
		endTransaction(false);
		return response;
	}

	protected OMElement endTransaction(OMElement request, Exception e, short actor, String message, String error_type) {
		if (message == null || message.equals(""))
			message = e.getMessage();
		logger.error("Exception: " + exception_details(e));
		OMElement response = start_up_error(request, e, actor, message, false, error_type);
		generateLogMessage(response);
		endTransaction(false);
		return response;
	}

	public OMElement start_up_error(OMElement request, Exception e, short actor,String message) {
		return start_up_error(request, e, actor, message, false);
	}

	public OMElement start_up_error(OMElement request, Object e, short actor,String message, boolean log) {
		String error_type = (actor == REGISTRY_ACTOR) ? MetadataSupport.XDSRegistryError : MetadataSupport.XDSRepositoryError;

		if (e instanceof XdsErrorCodeException) {
			XdsErrorCodeException ec = (XdsErrorCodeException) e;
			error_type = ec.getErrorCode();
			message = "Forced Error";
		}

		return start_up_error(request, e, actor, message, log, error_type);
	}


	public OMElement start_up_error(OMElement request, Object e, short actor,String message, boolean log, String error_type) {
		try {
			String request_type = (request != null) ? request.getLocalName() : "None";
			OMNamespace ns = (request != null) ? request.getNamespace() : MetadataSupport.ebRSns2;

			if (ns.getNamespaceURI().equals(MetadataSupport.ebRSns2.getNamespaceURI())) {
				// xds.a submitobjectsrequest
				RegistryErrorList rel = new RegistryErrorList(RegistryErrorList.version_2, log);
				rel.add_error(error_type, message, exception_details(e), log_message);
				return new RegistryResponse(RegistryErrorList.version_2, rel).getResponse();
			}
			if (ns.getNamespaceURI().equals(MetadataSupport.ebRSns3.getNamespaceURI())) {
				// xds.b submitobjectsrequest (could be xds.b retrieve)
				RegistryErrorList rel = new RegistryErrorList(RegistryErrorList.version_3, log);
				rel.add_error(error_type, message, exception_details(e), log_message);
				return new RegistryResponse(RegistryErrorList.version_3, rel).getResponse();
			}
			if (ns.getNamespaceURI().equals(MetadataSupport.xdsB.getNamespaceURI())) {
				// RetrieveDocumentSet
				RegistryErrorList rel = new RegistryErrorList(RegistryErrorList.version_3, log);
				rel.add_error(error_type, message, exception_details(e), log_message);
				if (request.getLocalName().equals("RetrieveDocumentSetRequest")) {
					OMElement res = new RetrieveMultipleResponse(rel).getResponse();
					return res;
				} else {
					return new RegistryResponse(RegistryErrorList.version_3, rel).getResponse();
				}

			}
			if (ns.getNamespaceURI().equals(MetadataSupport.ebQns3.getNamespaceURI())) {
				// stored query
				RegistryErrorList rel = new RegistryErrorList(RegistryErrorList.version_3, log);
				rel.add_error(error_type, message, exception_details(e), log_message);
				return new AdhocQueryResponse(RegistryErrorList.version_3, rel).getResponse();
			}
			if (ns.getNamespaceURI().equals(MetadataSupport.ebQns2.getNamespaceURI())) {
				// sql query
				RegistryErrorList rel = new RegistryErrorList(RegistryErrorList.version_2, log);
				rel.add_error(error_type, message, exception_details(e), log_message);
				return new AdhocQueryResponse(RegistryErrorList.version_2, rel).getResponse();
			}

			// the default when all else fails
			RegistryErrorList rel = new RegistryErrorList(RegistryErrorList.version_2, log);
			rel.add_error(error_type, message, exception_details(e), log_message);
			return new RegistryResponse(RegistryErrorList.version_2, rel).getResponse();

		} catch (XdsInternalException e1) {
			try {
				RegistryErrorList rel = new RegistryErrorList(RegistryErrorList.version_2, log);
				rel.add_error(error_type, e1.getMessage(), exception_details(e1), log_message);
				return new RegistryResponse(RegistryErrorList.version_2, rel).getResponse();
			} catch (Exception e2) {
				return null;
			}
		}

	}

	protected void startTransactionLog() throws LoggerException {
		if (log == null) {
			logger.info("+++++++++++++++++++++ start transaction log");
			try {
				log = (Log) XdsFactory.getInstance().getBean("logsService");
			} catch (Exception e) {
				new LoggerException("Exception while creating logsService bean object:" + e.getMessage(), e);
			}
			
		}
	}

	protected void stopTransactionLog() {
		try {
			if (log != null) { 
				if (log_message != null)
					log.writeMessage(log_message);
				logger.info("end " + service_name + " :" +((log_message == null) ? "null"  : log_message.getMessageID()));
				log = null;
				log_message = null;
				logger.info("+++++++++++++++++++++ stop transaction log");
			} 
		} catch (LoggerException e) {
			logger.error("LoggerException: " + exception_details(e));
		}
	}

	protected String exception_details(Object e) {
		if (e == null)
			return "No Additional Details Available";
		if (e instanceof Exception)
			return exception_details( (Exception) e );
		if (e instanceof String)
			return exception_details( (String) e);
		return exception_details(e.toString());
	}

	protected String exception_details(Exception e) {
		if (e == null)
			return "No stack trace available";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		e.printStackTrace(ps);

		return "Exception thrown: " + e.getClass().getName() + "\n" + e.getMessage() + "\n" + new String(baos.toByteArray());
	}

	protected String exception_details(String e) {
		return e;
	}

	private void addHttp ( String title , Vector<String> t )
	{
		int i = 0 ;
		StringBuffer buffer = new StringBuffer() ;
		for ( String s : t ) 
		{		
			buffer.append(s + "  ") ;
		}

		try 
		{
			log_message.addHTTPParam( title , buffer.toString() ) ;
		} catch (LoggerException e) {}
	}
	private void addSoap ( String t , String s )
	{
		try {
			log_message.addSoapParam( t , s ) ;
		} catch (LoggerException e) {}
	}

	protected void addError ( String s )
	{
		try {
			log_message.addErrorParam( "Error" , s ) ;
		} catch (LoggerException e) {}
	}

	protected void addOther ( String name, String s )
	{
		try {
			log_message.addOtherParam( name , s ) ;
		} catch (LoggerException e) {}
	}

	private String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	protected String getForcedErrorCode() {
		String code = null;
		try {
			code = getMessageContext().getParameter("ForceErrorCode").getValue().toString();
		} catch (Exception e) {

		}
		if (code == null) return null;
		if ("".equals(code)) return null;
		return code;
	}

	protected void forceForcedError() throws XdsErrorCodeException {
		String code = getForcedErrorCode();
		if (code == null) return;
		throw new XdsErrorCodeException("Forced Error", code);
	}

}
