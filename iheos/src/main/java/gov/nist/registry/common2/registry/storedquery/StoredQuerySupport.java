package gov.nist.registry.common2.registry.storedquery;

import gov.nist.registry.common2.registry.ErrorLogger;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.SQCodeAnd;
import gov.nist.registry.common2.registry.SQCodedTerm;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openxds.log.LogMessage;

public class StoredQuerySupport {
	private static final Log log = LogFactory.getLog(StoredQuerySupport.class);
	public ErrorLogger response;
	public LogMessage log_message;
	public SqParams params;
	public StringBuffer query;
	public boolean return_leaf_class;
	private boolean original_query_type;  // storage to allow temporary settings of return_leaf_class
	public boolean has_validation_errors = false;
	public boolean has_alternate_validation_errors = false;
	public boolean is_secure;

	/**
	 * Constructor
	 * @param response
	 * @param log_message
	 * @throws LoggerException 
	 */
	public StoredQuerySupport(ErrorLogger response, LogMessage log_message)  {
		this.response = response;
		this.log_message = log_message;
		init();
	}

	/**
	 * Constructor
	 * @param params (SqParams)
	 * @param return_objects (boolean true = LeafClass)
	 * @param response (Response class)
	 * @param log_message (Message)
	 * @param is_secure
	 */
	public StoredQuerySupport(SqParams params, boolean return_objects, Response response, LogMessage log_message, boolean is_secure) {
		this.response = response;
		this.log_message = log_message;
		this.params = params;
		this.is_secure = is_secure;
		this.return_leaf_class = return_objects;
		init();
	}
	
	public boolean isLeafClass() {
		return return_leaf_class;
	}

	void init() {
		query = new StringBuffer();
		has_validation_errors = false;
	}

	public void forceLeafClassQueryType() {
		original_query_type = return_leaf_class;
		return_leaf_class = true;
	}

	public void forceObjectRefQueryType() {
		original_query_type = return_leaf_class;
		return_leaf_class = false;
	}

	public void restoreOriginalQueryType() {
		return_leaf_class = original_query_type;

	}

	boolean isAlternativePresent(String[] alternatives) {
		if (alternatives == null)
			return false;
		for (String alternative : alternatives) {
			Object value = params.getParm(alternative);
			if (value != null)
				return true;
		}
		return false;
	}

	String valuesAsString(String mainName, String...alternatives) {
		StringBuffer buf = new StringBuffer();
		buf.append("[");

		if (mainName != null)
			buf.append(mainName);

		if (alternatives != null)
			for (int i=0; i<alternatives.length; i++)
				buf.append(" ").append(alternatives[i]);

		buf.append("]");

		return buf.toString();
	}

	// general
	public void validate_parm(String name, boolean required, boolean multiple, boolean is_string, boolean is_code, boolean and_or_ok, String... alternatives) {
		Object value = params.getParm(name);

		if (log.isDebugEnabled()) {
			log.debug("validate_parm: name=" + name + " value=" + value + " required=" + required + " multiple=" + multiple + " is_string=" + is_string + " is_code=" + is_code + " alternatives=" + valuesAsString(null, alternatives));
		}
		
		if (value == null && alternatives == null) {
			if (required ) {
				response.add_error("XDSRegistryError", "Parameter " + name + " is required but not present in query", "StoredQuery.java", log_message);
				this.has_validation_errors = true;
				return;
			} 
			return;
		}

		if (value == null && alternatives != null) {
			log.debug("looking for alternatives");
			if (! isAlternativePresent(alternatives)) {
				if ( ! has_alternate_validation_errors) {
					response.add_error("XDSRegistryError", "One of these parameters must be present in the query: " + valuesAsString(name, alternatives), "StoredQuery.java", log_message);
					has_alternate_validation_errors = true;  // keeps from generating multiples of this message
				}
				has_validation_errors = true;
				return;
			}
		}

		if (value == null)
			return;

		if (is_code) {
 			if ( !(value instanceof SQCodedTerm)) {
				response.add_error("XDSRegistryError", "Parameter, " + name + 
						", must be a coded term", "StoredQuery.java", log_message);
				this.has_validation_errors = true;
				return;
 			}
 			
 			if ( (value instanceof SQCodeAnd) && !and_or_ok) {
				response.add_error("XDSRegistryError", "Parameter, " + name + 
						", is coded with AND/OR semantics which are not allowed on this parameter", "StoredQuery.java", log_message);
				this.has_validation_errors = true;
				return;
 			}
				
		} else {

			if (multiple && !(value instanceof ArrayList)) {
				response.add_error("XDSRegistryError", "Parameter, " + name + ", accepts multiple values but (  ) syntax is missing", "StoredQuery.java", log_message);
				this.has_validation_errors = true;
				return;
			}
			if (!multiple && (value instanceof ArrayList)) {
				response.add_error("XDSRegistryError", "Parameter, " + name + ", accepts single value value only but (  )  syntax is present", "StoredQuery.java", log_message);
				this.has_validation_errors = true;
				return;
			}
			if (multiple && (value instanceof ArrayList) && ((ArrayList) value).size() == 0) {
				response.add_error("XDSRegistryError", "Parameter, " + name + ", (  )  syntax is present but list is empty", "StoredQuery.java", log_message);
				this.has_validation_errors = true;
				return;
			}

			if ( ! (value instanceof ArrayList) )
				return;

			ArrayList values = (ArrayList) value;

			for (int i=0; i<values.size(); i++) {
				Object a_o = values.get(i);
				if (	is_string && 
						!(a_o instanceof String) && 
						!(     (a_o instanceof ArrayList)   &&   
								((ArrayList)a_o).size() > 0    &&   
								( ((ArrayList)a_o).get(0) instanceof String) 
						)
				) {
					response.add_error("XDSRegistryError", "Parameter, " + name + ", is not coded as a string (is type " + a_o.getClass().getName() + ") (single quotes missing?)", "StoredQuery.java", log_message);
					this.has_validation_errors = true;
				}
				if (!is_string && !(a_o instanceof Integer)) {
					response.add_error("XDSRegistryError", "Parameter, " + name + " is not coded as a number (is type " + a_o.getClass().getName() + ") (single quotes present)", "StoredQuery.java", log_message);
					this.has_validation_errors = true;
				}
			}
		}

	}

	public void validate_parm(String name, boolean required, boolean multiple, boolean is_string, String same_size_as, String... alternatives) {
		Object value = params.getParm(name);

		if (log.isDebugEnabled()) {
			log.debug("validate_parm: name=" + name + " value=" + value + " required=" + required + " multiple=" + multiple + " is_string=" + is_string + " same_size_as=" + same_size_as + " alternatives=" + valuesAsString(null, alternatives));
		}
		
		if (value == null && alternatives == null) {
			if (required ) {
				response.add_error("XDSRegistryError", "Parameter " + name + " is required but not present in query", "StoredQuery.java", log_message);
				this.has_validation_errors = true;
				return;
			} 
			return;
		}

		if (value == null && alternatives != null) {
			log.debug("looking for alternatives");
			if (! isAlternativePresent(alternatives)) {
				if ( ! has_alternate_validation_errors) {
					response.add_error("XDSRegistryError", "One of these parameters must be present in the query: " + valuesAsString(name, alternatives), "StoredQuery.java", log_message);
					has_alternate_validation_errors = true;  // keeps from generating multiples of this message
				}
				has_validation_errors = true;
				return;
			}
		}

		if (value == null)
			return;

		if (multiple && !(value instanceof ArrayList)) {
			response.add_error("XDSRegistryError", "Parameter, " + name + ", accepts multiple values but (  ) syntax is missing", "StoredQuery.java", log_message);
			this.has_validation_errors = true;
			return;
		}
		if (!multiple && (value instanceof ArrayList)) {
			response.add_error("XDSRegistryError", "Parameter, " + name + ", accepts single value value only but (  )  syntax is present", "StoredQuery.java", log_message);
			this.has_validation_errors = true;
			return;
		}
		if (multiple && (value instanceof ArrayList) && ((ArrayList) value).size() == 0) {
			response.add_error("XDSRegistryError", "Parameter, " + name + ", (  )  syntax is present but list is empty", "StoredQuery.java", log_message);
			this.has_validation_errors = true;
			return;
		}

		if ( ! (value instanceof ArrayList) )
			return;

		ArrayList values = (ArrayList) value;

		for (int i=0; i<values.size(); i++) {
			Object a_o = values.get(i);
			if (	is_string && 
					!(a_o instanceof String) && 
					!(     (a_o instanceof ArrayList)   &&   
							((ArrayList)a_o).size() > 0    &&   
							( ((ArrayList)a_o).get(0) instanceof String) 
					)
			) {
				response.add_error("XDSRegistryError", "Parameter, " + name + ", is not coded as a string (is type " + a_o.getClass().getName() + ") (single quotes missing?)", "StoredQuery.java", log_message);
				this.has_validation_errors = true;
			}
			if (!is_string && !(a_o instanceof Integer)) {
				response.add_error("XDSRegistryError", "Parameter, " + name + " is not coded as a number (is type " + a_o.getClass().getName() + ") (single quotes present)", "StoredQuery.java", log_message);
				this.has_validation_errors = true;
			}
		}

		if (same_size_as == null)
			return;

		Object same_as_value = params.getParm(same_size_as);
		if ( !(same_as_value instanceof ArrayList)) {
			response.add_error("XDSRegistryError", "Parameter, " + same_size_as + " must have same number of values as parameter " + name, "StoredQuery.java", log_message);
			this.has_validation_errors = true;
			return;
		}
		ArrayList same_as_values = (ArrayList) same_as_value;

		if ( !(value instanceof ArrayList)) {
			response.add_error("XDSRegistryError", "Parameter, " + same_size_as + " must have same number of values as parameter " + name, "StoredQuery.java", log_message);
			this.has_validation_errors = true;
			return;
		}

		if (same_as_values.size() != values.size()) {
			response.add_error("XDSRegistryError", "Parameter, " + same_size_as + " must have same number of values as parameter " + name, "StoredQuery.java", log_message);
			this.has_validation_errors = true;
			return;
		}

	}

	public SqParams getParams() {
		return params;
	}

	public void setParams(SqParams params) {
		this.params = params;
	}


}
