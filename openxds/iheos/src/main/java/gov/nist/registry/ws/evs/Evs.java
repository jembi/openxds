package gov.nist.registry.ws.evs;

import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.ws.serviceclasses.XdsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.syslog.LoggerException;
import org.openhealthtools.openexchange.syslog.Message;


public class Evs extends XdsService {
	private static final Log logger = LogFactory.getLog(Evs.class);

	void init() throws LoggerException { startTransactionLog(); }
	void done() { stopTransactionLog(); }

	public String getReport(String messageId) {
		String reportTxt = null;
		try {
			init();
			Message m = log.readMessage(messageId);
			OMElement report = buildReport(m);

			reportTxt = report.toString();
			done();
		} catch (Exception e) {
			if (reportTxt == null)
				reportTxt = "<XdsEvsResult><InternalError>" + e.getMessage() + "</InternalError></XdsEvsResult>";
		} 
		finally {
			done();
		}
		return reportTxt;
	}

	boolean verifyTables(Message m) {
		HashSet <String> tableList = m.getTableList();
		boolean hasMain = tableList.contains("main");
		boolean hasOther = tableList.contains("other");
		boolean hasHttp = tableList.contains("http");
		boolean hasSoap = tableList.contains("soap");
		boolean hasError = tableList.contains("error");
		return hasMain && hasOther && hasHttp && hasSoap && hasError;		
	}

	String index(Message m) {
		HashMap<String, HashMap<String, Object>> mm = m.toHashMap();
		StringBuffer buf = new StringBuffer();

		for (String key : mm.keySet()) {
			buf.append("\n" + key + "\n" + mm.get(key).keySet());
		}
		return buf.toString();
	}
	
	Object getParm(HashMap<String, HashMap<String, Object>> mm,String section, String parm) {
		return mm.get(section).get(parm);
	}

	void addParm(OMElement report, HashMap<String, HashMap<String, Object>> mm, String name, String section, String parm) {
		Object value = getParm(mm, section, parm);
		if (value instanceof String) {
			OMElement ele = MetadataSupport.om_factory.createOMElement(new QName(name));
			String txt = (String) value;
			txt = extractPrefix(txt);
			ele.setText(txt);
			report.addChild(ele);
		} else {
			ArrayList<String> txts = (ArrayList<String>) value;
			for (String txt : txts) {
				OMElement ele = MetadataSupport.om_factory.createOMElement(new QName(name));
				txt = extractPrefix(txt);
				ele.setText(txt);
				report.addChild(ele);
			}
		}
	}
	private String extractPrefix(String txt) {
		int colonI = txt.indexOf(':');
		if (colonI != -1) {
			if (txt.charAt(colonI+1) == ' ' || txt.charAt(colonI+1) == '\t')
				txt = txt.substring(colonI+2).trim();
		}
		return txt;
	}

	OMElement buildReport(Message m) throws XdsInternalException {
		if (!verifyTables(m)) {
			throw new XdsInternalException("Missing Table");
		}

		if (logger.isDebugEnabled()) {
			logger.debug(index(m));
		}
		HashMap<String, HashMap<String, Object>> mm = m.toHashMap();

		OMElement report = MetadataSupport.om_factory.createOMElement(new QName("XdsEvsResult"));

		addParm(report, mm, "Test", "other", "Service");
		addParm(report, mm, "Date", "main", "Timestamp");
		addParm(report, mm, "Source", "http", "IP_address_From");

		OMElement ele = MetadataSupport.om_factory.createOMElement(new QName("Target"));
		ele.setText(localIPAddress());
		report.addChild(ele);		
		
		addParm(report, mm, "Endpoint", "http", "URI_To");
		addParm(report, mm, "Result", "main", "Pass");
		addParm(report, mm, "LogEvent", "main", "MessageId");
		
		addParm(report, mm, "Error", "error", "Error");
		
		logger.error("errors are:\n" + mm.get("error"));
		
		return report;
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: Evs messageId");
			System.exit(-1);
		}
		String messageId = args[0];
		Evs evs = new Evs();
		System.out.println(evs.getReport(messageId));
	}

	String localIPAddress() {
		try
		{
			java.net.InetAddress localMachine =
				java.net.InetAddress.getLocalHost();
			return localMachine.getHostAddress();
		}
		catch(java.net.UnknownHostException uhe)
		{
			//handle exception
		}
		return "unknown";

	}

}
