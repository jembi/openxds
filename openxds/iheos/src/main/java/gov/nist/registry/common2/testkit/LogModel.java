package gov.nist.registry.common2.testkit;

import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.MetadataSupport;

import java.util.HashMap;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.apache.log4j.Logger;
import org.jaxen.JaxenException;

public class LogModel {
	String test_num;
	HashMap<String, OMElement> logs;
	protected final static Logger logger = Logger.getLogger(LogModel.class);


	public LogModel(String test_num) {
		logs = new HashMap<String, OMElement>();
		this.test_num = test_num;
	}
	
	String getEndpoint(OMElement log) throws XdsInternalException {
		String endpoint = null;
		try {
			AXIOMXPath xpathExpression = new AXIOMXPath ("//Endpoint");
			endpoint = xpathExpression.stringValueOf(log);
		} catch (JaxenException e) {
			throw new XdsInternalException ("XPath error", e);
		}
		return endpoint;
	}

	boolean hasNode(OMElement log, String name) throws XdsInternalException {
		try {
			AXIOMXPath xpathExpression = new AXIOMXPath ("//Endpoint");
			if (xpathExpression.selectSingleNode(log) == null)
				return false;
			return true;
		} catch (JaxenException e) {
			throw new XdsInternalException ("XPath error", e);
		}
	}
	
	boolean isPnR(OMElement log) throws XdsInternalException {
		return hasNode(log, "ProvideAndRegisterTransaction");
	}

	boolean isR(OMElement log) throws XdsInternalException {
		return hasNode(log, "RegisterTransaction");
	}

	public void addLog(OMElement log) throws XdsInternalException, XdsException {
		String root_element_name = log.getLocalName();
		if (root_element_name == null || ( !root_element_name.equals("TestResults") &&   !root_element_name.equals("XdsEvsResult")   ) )
			throw new XdsException("Root of test log is " + root_element_name + " instead of TestResults or XdsEvsResult");
		if (root_element_name.equals("TestResults")) {
			OMElement test_ele = MetadataSupport.firstChildWithLocalName(log, "Test");
			if (test_ele == null) throw new XdsInternalException("Log has no Test element");
			String test_result_num = test_ele.getText();
			if (test_result_num == null || test_result_num.equals("")) throw new XdsInternalException("Log has empty Test element");

			logger.info("test = " + test_result_num + " test_num = " + test_num);
			
			String endpoint = getEndpoint(log);


			if (test_num.equals("11882") && test_result_num.equals("11881/create_folder")) {
				test_result_num = "11882/create_folder"; // 
				test_ele.setText("11882/create_folder");
			}
			else if (test_num.equals("11882") && test_result_num.equals("11881/eval")) {
				test_result_num = "11882/eval"; // 
				test_ele.setText("11882/eval");
			}
			else if (test_num.equals("11882") && test_result_num.equals("11881/add_to_folder")) {
				test_result_num = "11882/add_to_folder"; // 
				test_ele.setText("11882/add_to_folder");
			}
			else if (test_num.equals("11983") && test_result_num.equals("11982/eval")) {
				test_result_num = "11983/eval"; //
				test_ele.setText("11983/eval");
			}
			else if (test_num.equals("11983") && test_result_num.equals("11986/submit")) {
				test_result_num = "11983"; // 
				test_ele.setText("11983/submit");
			}
			else if (test_num.equals("11986") && test_result_num.equals("11986/submit")) {
				test_result_num = "11986"; // 
				test_ele.setText("11986");
			}
			else if (test_num.equals("12002") && test_result_num.equals("11881/create_folder")) {
				test_result_num = "12002/create_folder"; // 
				test_ele.setText("12002/create_folder");
			}
			else if (test_num.equals("12002") && test_result_num.equals("11881/add_to_folder")) {
				test_result_num = "12002/add_to_folder"; // 
				test_ele.setText("12002/add_to_folder");
			}
			else if (test_num.equals("12002") && test_result_num.equals("11881/eval")) {
				test_result_num = "12002/eval"; // 
				test_ele.setText("12002/eval");
			}
			else if (test_num.equals("12029") && test_result_num.startsWith("12330")) {
				test_result_num = test_result_num.replaceAll("12330", "12029"); // 
				test_ele.setText(test_result_num);
			}
			
			// tls
			else if (test_num.equals("12005") && test_result_num.startsWith("11990")) {
				test_result_num = "12005"; // 
				test_ele.setText(test_result_num);
			}
			else if (test_num.equals("12005") && test_result_num.startsWith("11897")) {
				test_result_num = "12005"; // 
				test_ele.setText(test_result_num);
			}
			else if (test_num.equals("12301") ) 
				throw new XdsException("Cannot get credit for running the example against the Public Registry");
			else if (test_num.equals("12306") ) 
				throw new XdsException("Cannot get credit for running the example against the Public Registry");
			
			else if (test_num.equals("12037") && (test_result_num.equals("12038")))
				test_result_num = "12037"; // 
			else if (test_num.equals("12083") && (test_result_num.startsWith("12038"))) {
				if (endpoint == null || endpoint.indexOf("https") == -1)
					throw new XdsException("Endpoint must be https");

				test_result_num = "12083"; // 
			}
			else if (test_num.equals("12085") && (test_result_num.startsWith("11827/submit"))) {
				if (endpoint == null || endpoint.indexOf("https") == -1)
					throw new XdsException("Endpoint must be https");

				test_result_num = "12085"; // 
			}
			else if (test_num.equals("12086")) {
				if (!isPnR(log))
					throw new XdsException("Transaction must be ProvideAndRegister");
				if (endpoint == null || endpoint.indexOf("https") == -1)
					throw new XdsException("Endpoint must be https");

				test_result_num = "12086"; // 
			}
			else if (test_num.equals("11740")) {
				if (!isR(log))
					throw new XdsException("Transaction must be Register");
				if (endpoint == null || endpoint.indexOf("https") == -1)
					throw new XdsException("Endpoint must be https");

				test_result_num = "11740"; // 
			}

			
			String[] test_parts = test_result_num.split("/");
			String section_name = (test_parts.length>1) ? test_parts[1] : ""; 
			
			if (test_parts.length > 1) {
				if ( test_num.equals(test_parts[0]) && section_name != null && !section_name.equals("")) 
					logs.put(section_name, log);
			} else {
				logs.put(test_result_num, log);
			}
		}
		else if (root_element_name.equals("XdsEvsResult")) {
			boolean is_a_result = true;
			OMElement testEle = MetadataSupport.firstChildWithLocalName(log, "Test");
			String test_result_num = testEle.getText();
			
			
			OMElement endpointEle = MetadataSupport.firstChildWithLocalName(log, "Endpoint");
			if (endpointEle == null) throw new XdsInternalException("EVS has empty Endpoint element");
			String endpoint = endpointEle.getText();
			boolean is_tls = endpoint.indexOf("https") != -1;
			
			if (test_result_num == null || test_result_num.equals("")) throw new XdsInternalException("EVS has empty Test element");
			
			if (test_result_num.startsWith("Test"))
				test_result_num = test_result_num.substring(4);
			
			logger.info("test = " + test_result_num + " test_num = " + test_num);
			if ( !test_num.equals(test_result_num)) {
				if (test_num.equals("12328") && 
						( 
								test_result_num.equals("11936") ||
								test_result_num.equals("11937") ||
								test_result_num.equals("11938") ||
								test_result_num.equals("11939") ||
								test_result_num.equals("11940") ||
								test_result_num.equals("11941") ||
								test_result_num.equals("11942") ||
								test_result_num.equals("11943") ||
								test_result_num.equals("11944") ||
								test_result_num.equals("11945") ||
								test_result_num.equals("11946") ||
								test_result_num.equals("11947") ||
								test_result_num.equals("11948") ||
								test_result_num.startsWith("SQ")
						))
					test_result_num = test_num; // ok - all these SQ tests get reported under test 12328
				
				else if (test_num.equals("11936") && test_result_num.startsWith("SQ")) 
					test_result_num = test_num;
				else if (test_num.equals("11937") && test_result_num.startsWith("SQ")) 
					test_result_num = test_num;
				else if (test_num.equals("11938") && test_result_num.startsWith("SQ")) 
					test_result_num = test_num;
				else if (test_num.equals("11939") && test_result_num.startsWith("SQ")) 
					test_result_num = test_num;
				else if (test_num.equals("11940") && test_result_num.startsWith("SQ")) 
					test_result_num = test_num;
				else if (test_num.equals("11941") && test_result_num.startsWith("SQ")) 
					test_result_num = test_num;
				else if (test_num.equals("11942") && test_result_num.startsWith("SQ")) 
					test_result_num = test_num;
				else if (test_num.equals("11943") && test_result_num.startsWith("SQ")) 
					test_result_num = test_num;
				else if (test_num.equals("11944") && test_result_num.startsWith("SQ")) 
					test_result_num = test_num;
				else if (test_num.equals("11945") && test_result_num.startsWith("SQ")) 
					test_result_num = test_num;
				else if (test_num.equals("11946") && test_result_num.startsWith("SQ")) 
					test_result_num = test_num;
				else if (test_num.equals("11947") && test_result_num.startsWith("SQ")) 
					test_result_num = test_num;
				else if (test_num.equals("11948") && test_result_num.startsWith("SQ")) 
					test_result_num = test_num;
				
				
				
				else if (test_num.equals("11746") && test_result_num.startsWith("PnR.a")) 
					test_result_num = "11746";
				else if (test_num.equals("11747") && test_result_num.equals("PnR.a 2Doc")) 
					test_result_num = "11747";
				else if (test_num.equals("11802") && test_result_num.equals("SQ.b"))
					test_result_num = "11802"; // 
				else if (test_num.equals("11898") && test_result_num.equals("11898"))
					test_result_num = "11898"; // 
				else if (test_num.equals("11903") && test_result_num.equals("11903"))
					test_result_num = "11903"; // 
				else if (test_num.equals("11905") && test_result_num.equals("11905"))
					test_result_num = "11905"; // 
				else if (test_num.equals("11906") && test_result_num.equals("11906"))
					test_result_num = "11906"; // 
				else if (test_num.equals("11923") && test_result_num.equals("SQ.b"))
					test_result_num = "11923"; // 
				else if (test_num.equals("11936") && test_result_num.equals("SQ.a"))
					test_result_num = "11936"; // 
				else if (test_num.equals("11940") && test_result_num.equals("SQ.a"))
					test_result_num = "11940"; // 
				else if (test_num.equals("11948") && test_result_num.equals("SQ.a"))
					test_result_num = "11948"; // 
				else if (test_num.equals("11969") && test_result_num.equals("PnR.b 11969"))
					test_result_num = "11969"; // 
				else if (test_num.equals("11970") && (test_result_num.equals("PnR.b") || test_result_num.equals("PnR.b 11970")))
					test_result_num = "11970"; // 
				else if (test_num.equals("11973") && test_result_num.equals("PnR.b"))
					test_result_num = "11973"; // 
				else if (test_num.equals("11971") && test_result_num.equals("PnR.b"))
					test_result_num = "11971"; // 
				else if (test_num.equals("11974") && (test_result_num.equals("PnR.b")))
					test_result_num = "11974"; // 
				else if (test_num.equals("12306") && (test_result_num.equals("XGQ")))
					test_result_num = "12306"; // 
				else if (test_num.equals("12308") && (test_result_num.equals("XGR")))
					test_result_num = "12308"; // 
				else if (test_num.equals("12307") && (test_result_num.equals("XGQ")))
					test_result_num = "12307"; // 
				else if (test_num.equals("12300") && (test_result_num.equals("XGQ")))
					test_result_num = "12300"; // 
				else if (test_num.equals("12328") && (test_result_num.startsWith("SQ")))
					test_result_num = "12328"; // 
				else if (test_num.equals("12338") && (test_result_num.startsWith("PnR.b")))
					test_result_num = "12338"; // 
				else if (test_num.equals("12339") && (test_result_num.startsWith("SQ.b")))
					test_result_num = "12339"; // 
				else if (test_num.equals("12301") && (test_result_num.equals("XGR")))
					test_result_num = "12301"; // 
				else if (test_num.equals("11927") && (test_result_num.equals("SQL.a")))
					test_result_num = test_num; // 
				else if (test_num.equals("11923") && (test_result_num.equals("SQL.a")))
					test_result_num = test_num; // 
				else if (test_num.equals("11924") && (test_result_num.equals("SQL.a")))
					test_result_num = test_num; //
				else if (test_num.equals("11925") && (test_result_num.equals("SQL.a")))
					test_result_num = test_num; //
				else if (test_num.equals("11926") && (test_result_num.equals("SQL.a")))
					test_result_num = test_num; //
				else if (test_num.equals("11927") && (test_result_num.equals("SQL.a")))
					test_result_num = test_num; //
				else if (test_num.equals("11928") && (test_result_num.equals("SQL.a")))
					test_result_num = test_num; //
				else if (test_num.equals("11929") && (test_result_num.equals("SQL.a")))
					test_result_num = test_num; //
				else if (test_num.equals("11930") && (test_result_num.equals("SQL.a")))
					test_result_num = test_num; //
				else if (test_num.equals("11931") && (test_result_num.equals("SQL.a")))
					test_result_num = test_num; //
				else if (test_num.equals("11932") && (test_result_num.equals("SQL.a")))
					test_result_num = test_num; //
				else if (test_num.equals("11933") && (test_result_num.equals("SQL.a")))
					test_result_num = test_num; //
				else if (test_num.equals("11934") && (test_result_num.equals("SQL.a")))
					test_result_num = test_num; //
				else if (test_num.equals("11935") && (test_result_num.equals("SQL.a")))
					test_result_num = test_num; //
				
				else if (test_num.equals("11801") && (test_result_num.startsWith("SQ")))
					test_result_num = test_num; //
				
				
				
				else if (test_num.equals("12049") && (test_result_num.startsWith("PnR.b"))) {
					if (endpoint.indexOf("repositoryBonedoc") == -1) 
						throw new XdsException("repositoryBonedoc endpoint must be used");
					test_result_num = "12049"; // 
				}
				else if (test_num.equals("12023") && (test_result_num.equals("RET.b")))
					test_result_num = "12023"; // 
				else if (test_num.equals("12047") && (test_result_num.startsWith("PnR.b"))) {
					if (endpoint.indexOf("repositoryB2doc") == -1) 
						throw new XdsException("repositoryB2doc endpoint must be used");
					test_result_num = "12047"; // 
				}
				else if (test_num.equals("12020") && (test_result_num.equals("RET.b"))) {
					test_result_num = "12020"; // 
					if ( !is_tls )
						throw new XdsException("Endpoint must be https - this is a TLS test");
				}
				else if (test_num.equals("12046") && (test_result_num.startsWith("PnR.b"))) {
					test_result_num = "12046"; // 
					if ( !is_tls )
						throw new XdsException("Endpoint must be https - this is a TLS test");
				}
				else if (test_num.equals("11743") && (test_result_num.startsWith("PnR.a"))) {
					test_result_num = "11743"; // 
					if ( !is_tls )
						throw new XdsException("Endpoint must be https - this is a TLS test");
				}
				else {
					is_a_result = false;
					//throw new XdsException("EVSfile Test element value, " + test + ", does not belong to test " + this.test_num);
				}
			}

			if (is_a_result) {
				OMElement resultEle = MetadataSupport.firstChildWithLocalName(log, "Result");
				String result = (resultEle != null) ? resultEle.getText() : null;
				if (result == null || result.equals("")) throw new XdsInternalException("EVS has empty Result element");
				logs.put(test_result_num, log);
				if (test_num.equals("12328"))
					logs.put("12328", log);
			} else {
				logger.info("Skipping " + test_result_num);
			}
		}
	}

	public boolean hasSubtest(String subtest_name) { return logs.containsKey(subtest_name); }

	public boolean isPass(String subtest_name) throws XdsInternalException, XdsException {
		OMElement log = get_log(subtest_name);
		String eleName = log.getLocalName();
		if (eleName.equals("TestResults")) {
			String status =  log.getAttributeValue(new QName("status"));
			if (status == null || status.equals("")) throw new XdsException("isPass: no status found for subtest " + subtest_name);
			return status.equals("Pass");
		}
		else if (eleName.equals("XdsEvsResult")) {
			OMElement resultEle = MetadataSupport.firstChildWithLocalName(log, "Result");
			String result = (resultEle != null) ? resultEle.getText() : null;
			if (result == null || result.equals("")) throw new XdsInternalException("EVS has empty Result element");
			return result.equals("Pass");
		}
		throw new XdsInternalException("Not TestResults or XdsEvsRequest");
	}

	public void checkFatalError(String subtest_name) throws XdsInternalException, XdsException {
		OMElement log = get_log(subtest_name);
		OMElement fatal_ele = MetadataSupport.firstChildWithLocalName(log, "FatalError");
		if (fatal_ele != null)
			throw new XdsException("Subtest " + subtest_name + "  log file has fatal error: " + fatal_ele.getText());
	}

	OMElement get_log(String subtest_name) throws XdsInternalException, XdsException {
		OMElement log = logs.get(subtest_name);
		if (log == null) throw new XdsInternalException("Results Error: No evidence supporing test/subtest <" + subtest_name + 
				"> found in submission, only (" +  logs.keySet().toString() + ") were found");
		return log;
	}

}
