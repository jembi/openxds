package gov.nist.registry.common2.registry;

import gov.nist.registry.common2.exception.ExceptionUtil;
import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XMLParserException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.xml.Parse;
import gov.nist.registry.xdslog.LoggerException;
import gov.nist.registry.xdslog.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.openhealthexchange.openxds.configuration.ModuleManager;
import org.openhealthexchange.openxds.registry.api.IXdsRegistryQueryManager;
import org.openhealthexchange.openxds.registry.api.RegistryQueryException;
import org.openhealthexchange.openxds.registry.api.RegistrySQLQueryContext;
import org.openhealthexchange.openxds.registry.api.RegistryStoredQueryContext;

public class BackendRegistry {
	ErrorLogger response;
	Message log_message;
	String reason = "";
	static Properties properties = null;

	static {
		properties = Properties.loader();
	}

	public BackendRegistry(ErrorLogger response, Message log_message) {
		this.response = response;
		this.log_message = log_message;
	}

	public BackendRegistry(ErrorLogger response, Message log_message, String reason) {
		this.response = response;
		this.log_message = log_message;
		this.reason = reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	static QName object_ref_qname = new QName("ObjectRef");
	static QName id_qname = new QName("id");

	public ArrayList<String> queryForObjectRefs(String sql) throws XMLParserException, LoggerException, XdsException {
		OMElement result = query(sql, false /* leaf_class */);

		ArrayList<String> ors = new ArrayList<String>();

		if (result == null)  // error occured
			return ors;

		OMElement sql_query_result = MetadataSupport.firstChildWithLocalName(result, "RegistryObjectList");

		if (sql_query_result != null) {
			for (OMElement or : MetadataSupport.childrenWithLocalName(sql_query_result, "ObjectRef")) {
				String id = or.getAttributeValue(id_qname);
				if (id != null && !id.equals(""))
					ors.add(id);
			}
		}

		return ors;
	}

	static String sql_query_header = 
		"<AdhocQueryRequest\n" +
		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
		"xmlns:rs=\"urn:oasis:names:tc:ebxml-regrep:registry:xsd:2.1\"\n" +
		"xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:rim:xsd:2.1\"\n" +
		"xmlns:q = \"urn:oasis:names:tc:ebxml-regrep:query:xsd:2.1\"\n" +
		"xmlns=\"urn:oasis:names:tc:ebxml-regrep:query:xsd:2.1\"\n" +
		">\n";

	public OMElement query(String sql, boolean leaf_class) throws LoggerException, XMLParserException, MetadataException, MetadataValidationException, XdsInternalException {
		OMElement result = basic_query(sql, leaf_class);

		// add in homeCommunityId to all major
		Metadata m = MetadataParser.parseNonSubmission(result);
		
		m.fixClassifications();
		
		for (OMElement ele : m.getAllObjects()) {
			add_v3_attributes(ele);
			for (Iterator it=ele.getChildElements(); it.hasNext(); ) {
				OMElement ele2 = (OMElement) it.next();
				if (ele2.getLocalName().equals("ExternalIdentifier")) add_v3_attributes(ele2);
				if (ele2.getLocalName().equals("Classification")) add_v3_attributes(ele2);
			}
		}

		return result;
	}
		
	public ArrayList<String> objectRefQuery(String sql) 
	throws MetadataValidationException, XMLParserException, LoggerException, XdsException {
		OMElement response = basic_query(sql, true);
		Metadata m = MetadataParser.parseNonSubmission(response);
		return m.getObjectIds(m.getObjectRefs());
	}

	public OMElement basic_query(String sql, boolean leaf_class)
	throws XMLParserException, LoggerException, XdsInternalException {
		if (log_message != null)
			log_message.addOtherParam("ebxmlrr request (" + reason + ")", sql);

		IXdsRegistryQueryManager qm = (IXdsRegistryQueryManager)ModuleManager.getInstance().getBean("registryQueryManager");

		RegistrySQLQueryContext context = new RegistrySQLQueryContext(sql, leaf_class);
		OMElement response_xml = null;
		try {
			response_xml = qm.sqlQuery(context);
		}catch(RegistryQueryException e) {
			throw new XdsInternalException("Failed to query the Registry. \nQuery was:\n"+ sql, e);
		}
		if (log_message != null)
			log_message.addOtherParam("ebxmlrr response", response.toString());

		if (! response_xml.getLocalName().equals("AdhocQueryResponse")) {
			try {
				throw new XdsInternalException("Return from ebxmlrr is '" + response_xml.getLocalName() + "' but should be 'AdhocQueryResponse'");
			} catch (Exception e) {
				response.add_error("XDSRegistryError", "Return from ebxmlrr is '" + response_xml.getLocalName() + "' but should be 'AdhocQueryResponse'", RegistryUtility.exception_details(e), log_message);
			}
			return null;
		}

		if (! response_xml.getAttributeValue(new QName("status")).equals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success")) {
			String msg = "Status from ebxmlrr is '" + response_xml.getAttributeValue(new QName("status")) + 
			"' but should be 'Success'. Error message(s) are:\n";	
			String msg_copy = msg;
			RegistryResponseParser rr = new RegistryResponseParser(response_xml);
			ArrayList<String> errs = rr.get_error_code_contexts();
			for (int i=0; i<errs.size(); i++) {
				String err = (String) errs.get(i);
				if (err.indexOf("QueryManagerImpl") > -1) {
					// ebxmlrr could not parse query
					msg = msg_copy + "Query engine error: " + ExceptionUtil.firstNLines(rr.get_regrep_error_msg(),4);  // + rr.get_regrep_error_msg();
					msg = ExceptionUtil.firstNLines(msg, 4);
					break;
				}
				//msg += (String) err + "\n";
			}
			msg += "\nQuery was:\n" + sql;
			throw new XdsInternalException(msg);
		}
		return response_xml;
	}

	private void add_v3_attributes(OMElement ele) {
		ele.addAttribute("home", "", null);
		ele.addAttribute("lid", ele.getAttributeValue(MetadataSupport.id_qname), null);
		insert_version_info(ele);
	}

	void insert_version_info(OMElement parent) {
		if (MetadataSupport.firstChildWithLocalName(parent, "VersionInfo") != null) return;
		OMElement vi = MetadataSupport.om_factory.createOMElement("VersionInfo", MetadataSupport.ebRIMns3);
		vi.addAttribute("versionName", "1.1", null);
		parent.addChild(vi);
	}


	public OMElement query(OMElement ahqr) throws LoggerException, XdsInternalException {

		if (log_message != null)
			log_message.addOtherParam("ebxmlrr request (" + reason + ")", ahqr.toString());

		//TODO: initiate RegistryQueryManager
		IXdsRegistryQueryManager qm = null;
		//TODO: convert ahqr OMElement to context, extract id and query parameters
		String id = "123";  //
		Map params = new HashMap();		
		RegistryStoredQueryContext context = new RegistryStoredQueryContext(id, params, true /*LeafClass*/);
		OMElement response = null;
		try {
			response = qm.storedQuery(context);
		}catch(RegistryQueryException e) {
			throw new XdsInternalException("Failed to query the Registry", e);
		}
		if (log_message != null)
			log_message.addOtherParam("ebxmlrr response", response.toString());

		OMElement response_xml = null;

		if (! response.getLocalName().equals("RegistryResponse")) {
//			try {
			throw new XdsInternalException("Return from ebxmlrr is '" + response_xml.getLocalName() + "' but should be 'RegistryResponse'");
//			} catch (Exception e) {
//			response.add_error("XDSRegistryError", "Return from ebxmlrr is '" + response_xml.getLocalName() + "' but should be 'RegistryResponse'", RegistryUtility.exception_details(e), log_message);
//			}
//			return null;
		}

		if (! response_xml.getAttributeValue(new QName("status")).equals("Success")) {
			String msg = "Status from ebxmlrr is '" + response_xml.getAttributeValue(new QName("status")) + 
			"' but should be 'Success'. Error message(s) are:\n";	
			String msg_copy = msg;
			RegistryResponseParser rr = new RegistryResponseParser(response_xml);
			ArrayList<String> errs = rr.get_error_code_contexts();
			for (int i=0; i<errs.size(); i++) {
				String err = (String) errs.get(i);
				if (err.indexOf("QueryManagerImpl") > -1) {
					// ebxmlrr could not parse query
					msg = msg_copy + "Query engine error: " + ExceptionUtil.firstNLines(rr.get_regrep_error_msg(),4);  // + rr.get_regrep_error_msg();
					msg = ExceptionUtil.firstNLines(msg, 4);
					break;
				}
				//msg += (String) err + "\n";
			}
			msg += "\nQuery was:\n" + ahqr.toString();
			throw new XdsInternalException(msg);
		}
		return response_xml;
	}

}
