package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.exception.XdsWSException;
import gov.nist.registry.common2.registry.AdhocQueryResponse;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.Properties;
import gov.nist.registry.common2.registry.RegistryErrorList;
import gov.nist.registry.common2.registry.RegistryResponse;
import gov.nist.registry.common2.registry.RetrieveMultipleResponse;
import gov.nist.registry.ws.log.Fields;
import gov.nist.registry.xdslog.Log;
import gov.nist.registry.xdslog.LoggerException;
import gov.nist.registry.xdslog.Message;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMException;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.Constants;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.logging.LogFactory;

public class XdsService {
	protected Log log = null;
	Message log_message;
	public static short registry_actor = 1;
	public static short repository_actor = 2;
	private final static org.apache.commons.logging.Log logger = LogFactory.getLog(XdsService.class);
	String service_name;
	static Properties properties = null;
	boolean is_secure;
	MessageContext return_message_context = null;
	static public String technicalFramework = null;
	
	static {
//		BasicConfigurator.configure();
		properties = Properties.loader();
		technicalFramework = properties.getString("tf");
	}

	protected boolean isSecure() { return is_secure; }

	MessageContext getMessageContext() {
		return MessageContext.getCurrentMessageContext();
	}

	public void setReturnMessageContext(MessageContext return_context) {
		this.return_message_context = return_context;
	}

	public void useXop() {
		this.return_message_context = MessageContext.getCurrentMessageContext();
		if (return_message_context != null)
			return_message_context.getOptions().setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
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

//		String incoming_ip_address = getMessageContext().getFrom().getAddress();
//		for (int i=1; i<100; i++) {
//			String entry = this.properties.getString("BlockIp" + i);
//			if (entry == null || entry.equals(""))
//				break;
//			if (entry.equals(incoming_ip_address))
//				return start_up_error(request, null, actor, "Continuous jamming from IP " + incoming_ip_address + " - access blocked");
//		}

		logger.info("Start " + service_name + " : " + incoming_ip_address  + " : " + getMessageContext().getTo().toString());
		try {
			startTestLog();
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
			System.out.println("LoggerException: new Log: " + e.getMessage());
			e.printStackTrace();
			stopTestLog();
			return start_up_error(request, e, actor, "Internal Error:Cannot access Test Log Facility");
		}
	}

	protected void endTransaction(boolean status) {
		logger.info("end " + service_name + " " +
				((log_message == null) ? "null"  : log_message.getMessageID()) + " : " + 
				((status) ? "Pass" : "Fail")
		);

		stopTestLog();
	}

	protected OMElement endTransaction(OMElement request, Exception e, short actor, String message) {
		if (message == null || message.equals(""))
			message = e.getMessage();
		System.out.println("Exception: " + exception_details(e));
		endTransaction(false);
		return start_up_error(request, e, actor, message);
	}

	protected OMElement endTransaction(OMElement request, Exception e, short actor, String message, String error_type) {
		if (message == null || message.equals(""))
			message = e.getMessage();
		System.out.println("Exception: " + exception_details(e));
		endTransaction(false);
		return start_up_error(request, e, actor, message, false, error_type);
	}

	protected OMElement start_up_error(OMElement request, Exception e, short actor,String message) {
		return start_up_error(request, e, actor, message, false);
	}

	public OMElement start_up_error(OMElement request, Object e, short actor,String message, boolean log) {
		String error_type = (actor == registry_actor) ? MetadataSupport.XDSRegistryError : MetadataSupport.XDSRepositoryError;
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

	protected void startTestLog() throws LoggerException {
		if (log == null) {
			System.out.println("+++++++++++++++++++++ start test log");
			try {
				log = new Log("jdbc:postgresql://localhost/log2", "logs", "xdslogs") ;
			}
			catch (LoggerException e) {
				// happens intermittently - this may help
				log = new Log("jdbc:postgresql://localhost/log2", "logs", "xdslogs") ;

			}
		}
	}

	protected void stopTestLog() {
		try {
			if (log != null) { 
				System.out.println("+++++++++++++++++++++ stop test log");
				if (log_message != null)
					log.writeMessage(log_message);
				log.close();
				log = null;
				log_message = null;
			} 
		} catch (LoggerException e) {
			System.out.println("LoggerException: " + exception_details(e));
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

	public void setMessageContextIn ( MessageContext inMessage )
	{
		//currentMessageContext = inMessage ;
	}

	private String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	protected void checkSOAP12() throws XdsWSException {

		if (MessageContext.getCurrentMessageContext().isSOAP11()) {
			throwFault("SOAP 1.1 not supported");
		}
		SOAPEnvelope env = MessageContext.getCurrentMessageContext().getEnvelope();
		if (env == null)
			throwFault("No SOAP envelope found");
		SOAPHeader hdr = env.getHeader();
		if (hdr == null)
			throwFault("No SOAP header found");
		if ( !hdr.getChildrenWithName(new QName("http://www.w3.org/2005/08/addressing","Action")).hasNext()) {
			throwFault("WS-Action required in header");
		}

	}

	protected void checkSOAP11() throws XdsWSException {

		if ( !MessageContext.getCurrentMessageContext().isSOAP11()) {
			throwFault("SOAP 1.2 not supported");
		}
		SOAPEnvelope env = MessageContext.getCurrentMessageContext().getEnvelope();
		if (env == null)
			throwFault("No SOAP envelope found");
	}
	
	protected void checkSOAPAny() throws XdsWSException {
		if ( MessageContext.getCurrentMessageContext().isSOAP11())
			checkSOAP11();
		else
			checkSOAP12();
	}

	private void throwFault(String msg) throws XdsWSException {
		try {
			if (log_message != null) {
				log_message.addErrorParam("SOAPError", msg);
				log_message.addOtherParam("Response", "SOAPFault: " + msg);
				endTransaction(false);
			}
		} catch (LoggerException e) {}
		throw new XdsWSException(msg);
	}

	protected boolean isAsync() {
		MessageContext mc = getMessageContext();
		return 
		mc.getMessageID() != null && 
		!mc.getMessageID().equals("") &&
		mc.getReplyTo() != null &&
		!mc.getReplyTo().hasAnonymousAddress();
	}

	boolean isSync() {
		return !isAsync();
	}


}
