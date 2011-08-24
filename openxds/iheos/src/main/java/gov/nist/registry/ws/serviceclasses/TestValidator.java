package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.RegistryUtility;
import gov.nist.registry.ws.testvalidator.LogModel;
import gov.nist.registry.ws.testvalidator.Response;

import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.openhealthtools.openxds.log.LoggerException;

public class TestValidator extends XdsService {
	boolean log;
	public boolean success;
	int test;

	public OMElement ValidateTestRequest(OMElement request) {

		this.beginTransaction("TestValidator", request, REGISTRY_ACTOR);

		log_message.setTestMessage("TestValidator");

		return  validate(request);
	}

	public TestValidator() { log = true; success = true; }

	public TestValidator(boolean log) {
		this.log = log;
		success = true;
	}

	public OMElement validate(OMElement request) {
		OMElement test_ele = MetadataSupport.firstChildWithLocalName(request, "Test");
		if (test_ele == null) return fatal_error("Test element not present in request"); 
		String test_str = test_ele.getText();
		if (test_str == null) return fatal_error("Test element does not contain a test number"); 
		test = 0;
		try {
			test = Integer.parseInt(test_str);
		}
		catch (NumberFormatException e) {

			return fatal_error("Test number, " + test_str + ", is not an integer");
		}

		List<OMElement> logs = MetadataSupport.childrenWithLocalName(request, "Log");

		LogModel lm = new LogModel(test_str);
		for (OMElement log : logs) {
			OMElement test_result = log.getFirstChildWithName(new QName("TestResults"));
			if (test_result == null)
				test_result = log.getFirstChildWithName(new QName("XdsEvsResult"));
			try {
				lm.addLog(test_result);
			} 
			catch (XdsInternalException e) {
				return fatal_error(RegistryUtility.exception_details(e));
			}
			catch (XdsException e) {
				return error(RegistryUtility.exception_details(e));
			}
		}
		try {
			if (log)
				log_message.addOtherParam("testnum", test_str);
		} catch (LoggerException e) {}

		return evaluateTestDetails(test_str, lm);
	}

	private OMElement evaluateTestDetails(String test_str, LogModel lm) {
		try {
			String errors = "";
			switch (test) {
			case 11733:
			case 11735:
			case 11751:
			case 11879:
			case 11880:
			case 11877:
			case 11885:
			case 11883:
			case 11827:
			case 11982:
			case 11990:
			case 11991:
			case 11999:
			case 12000:
			case 11997:
			case 12004:
			case 12084:
			case 11966:
			case 11979:
			case 11983:
				errors = eval_basic(lm, Arrays.asList("submit", "eval"));
				break;
			case 12048:
				errors = eval_basic(lm, Arrays.asList("submit", "apnd"));
				break;
			case 11875:
			case 11878:
			case 11992:
			case 11995:
				errors = eval_basic(lm, Arrays.asList("submit", "rplc", "eval"));
				break;
			case 11:
				errors = eval_basic(lm, Arrays.asList("submit", "retrieve"));
				break;
			case 12021:
			case 12024:
			case 12028:
			case 12029:
			case 12045:
			case 12088:
			case 12089:
				errors = eval_basic(lm, Arrays.asList("submit", "query", "retrieve"));
				break;
			case 11873:
			case 11993:
			case 12022:
				errors = eval_basic(lm, Arrays.asList("submit", "rplc", "apnd", "eval"));
				break;
			case 11874:
			case 11994:
				errors = eval_basic(lm, Arrays.asList("submit", "rplc", "xfrm", "eval"));
				break;
			case 11871:
				errors = eval_basic(lm, Arrays.asList("submit", "rplc", "rpl2", "eval"));
				break;
			case 11881:
			case 11882:
			case 12001:
			case 12002:
			case 12325:
			case 12326:
				errors = eval_basic(lm, Arrays.asList("create_folder", "add_to_folder", "eval"));
				break;
			case 11897:
			case 11898:
			case 11903:
			case 11905:
			case 11906:
				errors = eval_basic(lm, Arrays.asList("SOAP12"));
				break;
			case 12321:
				errors = eval_basic(lm, Arrays.asList("optimized", "unoptimized"));
				break;
			case 11746:
			case 11728:
			case 11729:
			case 11730:
			case 11801:
			case 11802:
			case 11876:
			case 11887:
			case 11901:
			case 11904:
			case 11909:
			case 11980:
			case 11996:
			case 12049:
			case 12047:
			case 11970:
			case 11981:
			case 11969:
			case 11972:
			case 11973:
			case 11974:
			case 11986:
			case 11907:
			case 11908:
			case 11902:
			case 11899:
			case 12038:
			case 12083:
			case 12093:
			case 12309:
			case 12310:
			case 12311:
			case 12312:
			case 12320:
			case 12322:
			case 12323:
			case 12324:
			case 12327:
			case 12328:
			case 12051:
			case 12052:
			case 12053:
			case 12054:
			case 12055:
			case 12087:
				errors = eval_basic(lm, Arrays.asList(test_str));
				break;
			case 11734:
				return success();
			default:
				return not_implemented();
			}
			if (errors.length() != 0)
				return error(errors);
			return success();
		}
		catch (XdsInternalException e) {
			return fatal_error(RegistryUtility.exception_details(e));
		}
		catch (XdsException e) {
			return error(RegistryUtility.exception_details(e));
		}
	}

	OMElement error(String msg) {
		Response res = new Response();
		res.addError(msg);
		if (log)
			this.addError(msg);
		if (log)
			this.endTransaction(false);
		success = false;
		return res.toXml();
	}

	OMElement fatal_error(String msg) {
		Response res = new Response();
		res.addFatalError(msg);
		if (log)
			this.addError(msg);
		if (log)
			this.endTransaction(false);
		success = false;
		return res.toXml();
	}

	OMElement not_implemented() {
		Response res = new Response();
		res.setNotImplemented();
		if (log)
			this.addOther("status", Response.status_notimplemented);
		if (log)
			log_message.setPass(true);
		if (log)
			this.endTransaction(true);
		success = false;
		return res.toXml();
	}

	OMElement success() {
		Response res = new Response();
		if (log)
			this.addOther("status", Response.status_success);
		if (log)
			log_message.setPass(true);
		if (log)
			this.endTransaction(true);
		success = true;
		return res.toXml();
	}

	String eval_basic(LogModel lm, List<String> subtests)  throws XdsInternalException, XdsException {
		StringBuffer sb = new StringBuffer();
		for (String subtest : subtests) {
			lm.checkFatalError(subtest);
			if ( !lm.hasSubtest(subtest)) sb.append("Log for sub-test " + subtest + " not found\n");
			if ( !lm.isPass(subtest)) sb.append("Log for sub-test " + subtest + " shows failure");
		}
		return sb.toString();
	}

}
