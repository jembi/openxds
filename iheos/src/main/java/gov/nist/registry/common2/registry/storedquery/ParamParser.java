package gov.nist.registry.common2.registry.storedquery;

import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.SQCodeAnd;
import gov.nist.registry.common2.registry.SQCodeOr;
import gov.nist.registry.common2.registry.SQCodedTerm;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ParamParser {
	String queryid;
	OMElement query;
	SqParams params;
	
	private final static Log logger = LogFactory.getLog(ParamParser.class);

	QName name_qname = new QName("name");
	QName valuelist_qname = new QName("ValueList");
	
	public ParamParser() {
		this.queryid = null;
		this.query = null;
	}
	
	public ParamParser(OMElement query) throws MetadataValidationException, XdsInternalException {
		this.queryid = null;
		this.query = query;
		
		parse(query);
	}
	
	public boolean isSQ() {
		return MetadataSupport.isSQId(queryid);
	}
	
	public boolean isMPQ() {
		return MetadataSupport.isMPQId(queryid);
	}

	public SqParams parse(OMElement query)  throws MetadataValidationException, XdsInternalException {
		HashMap<String, Object> parms = new HashMap<String, Object>();

		for (Iterator it=query.getChildElements(); it.hasNext(); ) {
			OMElement child1 = (OMElement) it.next();
			if (child1.getLocalName().equals("AdhocQuery")) {
				queryid = child1.getAttributeValue(MetadataSupport.id_qname);
				ArrayList names = new ArrayList();
				for (Iterator it2=child1.getChildElements(); it2.hasNext(); ) {
					OMElement slot = (OMElement) it2.next();
					String name = parse_slot(slot, parms);
					if (names.contains(name)) {
						//throw new MetadataValidationException("Parameter " + name + " is defined in multiple Slots");
					} else
						names.add(name);
				}
				
				if (logger.isDebugEnabled()) {
					logger.debug("SQParams: " + parms);
				}
			}
		}

		params = new SqParams(queryid, parms);
		return params;
	}

	void add_parm(HashMap<String, Object> parms, String name, Object value) throws XdsInternalException {
		Object existing_value = parms.get(name);
		if (existing_value == null) {
			parms.put(name, value);
		} else if (existing_value instanceof Integer || existing_value instanceof String) {
			ArrayList a = new ArrayList(2);
			a.add(existing_value);
			a.add(value);
			parms.put(name, a);
		} else if (existing_value instanceof ArrayList) {
			ArrayList a = (ArrayList) existing_value;
			if (value instanceof ArrayList) 
				a.addAll((ArrayList) value);
			else
				a.add(value);
		} else {
			throw new XdsInternalException("Stored Query parameter parser: add_parm: existing_value is of type " + existing_value.getClass().getName());
		}
	}

	class SlotParse {
		boolean isList;
		List<Object> values;
	}

	/*
	 * slot  the XML of the SQ parameter being parsed
	 * parms resulting structure describing parsed parameters
	 * 
	 * The resulting value of the value in parms is either
	 * 		a List of acceptable values (input for an SQL OR)
	 * 		a List of And structures. An And structure is a list of values, all of which must be present to satisfy the query (AND logic). Each of
	 * 		the values is really a List of OR values.
	 * 		This AND logic is nested inside the OR logic to get the needed AND/OR logic required by the Stored Query specification.
	 * A Slot, representing a SQ parameter, holds a list of acceptable values for a parameter (OR logic).  If multiple Slots of the
	 * same name are present in the query request, then each must be satisfied by the query (AND logic).
	 * 
	 *  When reading parms later, if the value is either instanceof And (interpreted as AND) or not (interpreted as OR).  An OR
	 *  is a List of values.  An AND is always a List of ORs.
	 *  
	 *  This proves that Stored Query has gotten way too complicated!
	 */
	String parse_slot(OMElement slot, HashMap<String, Object> parms) throws MetadataValidationException, XdsInternalException {
		String name = slot.getAttributeValue(name_qname);
		
		SlotParse sp = parse_slot(slot);
		
		if (SQCodedTerm.isCodeParameter(name)) {
			parse_code_param(name, sp, parms);
		} else {
			parse_noncode_param(name, sp, parms);
		}
		
		return name;
	}
	
	void parse_code_param(String name, SlotParse sp, HashMap<String, Object> parms) throws MetadataValidationException, XdsInternalException {
		// all values must be strings
		for (Object o : sp.values) {
			if (! (o instanceof String))
				throw new MetadataValidationException("Parameter " + name + " is a code type parameter and has non-string type value of " + 
						o + " which is of type " + o.getClass().getName());
		}
		
		// build OR structure to represent the values in this parameter slot
		SQCodeOr or = new SQCodeOr(name, SQCodedTerm.codeUUID(name));
		for (Object o : sp.values) {
			String v = (String) o;
			or.addValue(v);
		}

		Object current = parms.get(name);
		
		if (current == null) {
			parms.put(name, or);
		} else if (current instanceof SQCodeOr) {
			// old and new OR structures belong inside new AND structure
			SQCodeAnd and = new SQCodeAnd();
			parms.put(name, and);
			and.add((SQCodeOr)current);
			and.add(or);
		} else if (current instanceof SQCodeAnd) {
			SQCodeAnd and = (SQCodeAnd) current;
			and.add(or);
		}
	}
	
	void parse_noncode_param(String name, SlotParse sp, HashMap<String, Object> parms) throws MetadataValidationException {
		if (parms.containsKey(name)) 
			throw new MetadataValidationException("Stored Query parameter " + name + " defined multiple times");
		
		// this is necessary so that formatting mistakes can be detected
		if (sp.isList)
			parms.put(name, sp.values);
		else
			parms.put(name, sp.values.get(0));
	}
	
	SlotParse parse_slot(OMElement slot) throws MetadataValidationException {
		String name = slot.getAttributeValue(name_qname);

		List<Object> slotValues = new ArrayList<Object>();
		boolean isList = false;

		OMElement value_list = MetadataSupport.firstChildWithLocalName(slot, "ValueList"); 
		for (Iterator it=value_list.getChildElements(); it.hasNext(); ) {
			OMElement value_element = (OMElement) it.next();
			if (!value_element.getLocalName().equals("Value"))
				continue;

			// Integers
			String value_string = value_element.getText().trim();
			try {
				Integer value_int = Integer.decode(value_string);
				slotValues.add(value_int);
				continue;
			} catch (NumberFormatException e) {
			}

			// Big Integers like dates

			// date strings are technically numeric but too large to be parsed as integers
			try {
				BigInteger value_int = new BigInteger(value_string);
				slotValues.add(value_int);
				continue;
			} catch (NumberFormatException e) {
			}

			// Strings

			if (value_string.charAt(0) == '\'' &&
					value_string.charAt(value_string.length()-1) == '\'') {
				String val = value_string.substring(1, value_string.length()-1);
				slotValues.add(val);
				continue;
			}

			// list of strings or ints
			if (value_string.charAt(0) == '(' &&
					value_string.charAt(value_string.length()-1) == ')') {
				String val_list = value_string.substring(1, value_string.length()-1);
				String[] vals = val_list.split(",");
				
				isList = true;

				for (int i=0; i<vals.length; i++ ) {
					String value_string1 = vals[i].trim();
					if (value_string1 == null || value_string1.length() == 0)
						throw new MetadataValidationException("Error decoding Slot " + name + " - empty value");
					// each value could be 'string' or int
					if (value_string1.charAt(0) == '\'' &&
							value_string1.charAt(value_string1.length()-1) == '\'') {

						// Strings - could be codes
						String v = value_string1.substring(1, value_string1.length()-1);
						if (v.indexOf("'") != -1)
							throw new MetadataValidationException("Could not decode the value " + 
									value_string1 + " of Slot " + name + " as part of Stored Query parameter value " +
									value_string);
						
						slotValues.add(v);

					} else {

						// Integers
						try {
							Integer value_int = Integer.decode(value_string1);
							slotValues.add(value_int);
						} catch (NumberFormatException e) {
							throw new MetadataValidationException("Could not decode the value " + 
									value_string1 + " of Slot " + name + " as part of Stored Query parameter value " +
									value_string);
						}
					}
				}
				continue;
			}
			throw new MetadataValidationException("Could not decode the value " +
					value_string + ". It does not parse as an integer, a '' delimited string or a () delimited list.");
		}
		
		SlotParse sp = new SlotParse();
		sp.isList = isList;
		sp.values = slotValues;
		return sp;
	}

	public String getQueryid() {
		return queryid;
	}

	//	String parse_slot(OMElement slot, HashMap<String, Object> parms) throws MetadataValidationException, XdsInternalException {
	//		String name = slot.getAttributeValue(name_qname);
	//		
	//		ArrayList<Object> newHome;  // where to stuff new values
	//		if (parms.containsKey(name)) {
	//			// this is not first slot with this name - AND logic applies here
	//			Object value = parms.get(name);
	//			if (value instanceof And) {
	//				// existing And - add to it
	//				And and = (And) value;
	//				newHome = new ArrayList<Object>();
	//				and.add(newHome);
	//			} else if (value instanceof ArrayList) {
	//				// ArrayList is an implied OR
	//				// create And
	//				And and = new And();
	//				and.add(value);
	//				newHome = new ArrayList<Object>();
	//				and.add(newHome);
	//				parms.put(name, and);  // replace old ArrayList with And
	//			} else {
	//				throw new XdsInternalException("ParamParser:parse_slot(): unknown data type in parms database: " + 
	//						value.getClass().getName() + 
	//						" found while parsing slot " +
	//						slot.toString());
	//			}
	//		} else {
	//			newHome = null;
	//			parms.put(name, newHome);
	//		}
	//		
	//		OMElement value_list = MetadataSupport.firstChildWithLocalName(slot, "ValueList"); 
	//		for (Iterator it=value_list.getChildElements(); it.hasNext(); ) {
	//			OMElement value_element = (OMElement) it.next();
	//			if (!value_element.getLocalName().equals("Value"))
	//				continue;
	//			String value_string = value_element.getText();
	//			try {
	//				Integer value_int = Integer.decode(value_string);
	//				//add_parm(parms, name, value_int);
	//				
	//				if (newHome == null) parms.put(name, value_int);
	//				else newHome.add(value_int);
	//				
	//				continue;
	//			} catch (NumberFormatException e) {
	//			}
	//			
	//			// date strings are technically numeric but too large to be parsed as integers
	//			try {
	//				BigInteger value_int = new BigInteger(value_string);
	//				//add_parm(parms, name, value_int);
	//				
	//				if (newHome == null) parms.put(name, value_int);
	//				else newHome.add(value_int);
	//				
	//				continue;
	//			} catch (NumberFormatException e) {
	//			}
	//			
	//			if (value_string.charAt(0) == '\'' &&
	//					value_string.charAt(value_string.length()-1) == '\'') {
	//				String val = value_string.substring(1, value_string.length()-1);
	//				//add_parm(parms, name, val);
	//				
	//				if (newHome == null) parms.put(name, val);
	//				else newHome.add(val);
	//				
	//				continue;
	//			}
	//			if (value_string.charAt(0) == '(' &&
	//					value_string.charAt(value_string.length()-1) == ')') {
	//				String val_list = value_string.substring(1, value_string.length()-1);
	//				String[] vals = val_list.split(",");
	//				//ArrayList a = new ArrayList();
	//				for (int i=0; i<vals.length; i++ ) {
	//					String value_string1 = vals[i].trim();
	//					if (value_string1 == null || value_string1.length() == 0)
	//						throw new MetadataValidationException("Error decoding Slot " + name + " - empty value");
	//					// each value could be 'string' or int
	//					if (value_string1.charAt(0) == '\'' &&
	//							value_string1.charAt(value_string1.length()-1) == '\'') {
	//						String v = value_string1.substring(1, value_string1.length()-1);
	//						if (v.indexOf("'") != -1)
	//							throw new MetadataValidationException("Could not decode the value " + 
	//									value_string1 + " of Slot " + name + " as part of Stored Query parameter value " +
	//									value_string);
	//						if (newHome == null) {
	//							newHome = new ArrayList<Object>();
	//							parms.put(name, newHome);  
	//						}
	//						newHome.add(v);
	//					} else {
	//						try {
	//							Integer value_int = Integer.decode(value_string1);
	//							newHome.add(value_int);
	//						} catch (NumberFormatException e) {
	//							throw new MetadataValidationException("Could not decode the value " + 
	//									value_string1 + " of Slot " + name + " as part of Stored Query parameter value " +
	//									value_string);
	//						}
	//					}
	//				}
	//				//add_parm(parms, name, a);
	//				continue;
	//			}
	//			throw new MetadataValidationException("Could not decode the value " +
	//					value_string + ". It does not parse as an integer, a '' delimited string or a () delimited list.");
	//		}
	//		return name;
	//	}

}
