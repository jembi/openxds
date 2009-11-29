package gov.nist.registry.ws.sq.test;

import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.And;
import gov.nist.registry.common2.registry.storedquery.ParamParser;
import gov.nist.registry.common2.registry.storedquery.SqParams;

import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.TestCase;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMText;

public class ParamParserTest  extends TestCase {
	OMElement query = null;
	OMElement adhocquery = null;
	protected OMFactory fac = null;
	ParamParser parser = null;
	
	OMFactory om_factory() {
		if (fac == null)
			fac = OMAbstractFactory.getOMFactory();
		return fac;
	}
	
	public void setUp() {
		query = om_factory().createOMElement("AdhocQueryRequest", null);
		OMElement response_option = om_factory().createOMElement("ResponseOption", null);
		response_option.addAttribute("returnComposedObjects", "true", null);
		response_option.addAttribute("returnType", "LeafClass", null);
		query.addChild(response_option);
		adhocquery = om_factory().createOMElement("AdhocQuery", null);
		query.addChild(adhocquery);
		
		parser = new ParamParser();
	}
	
	void addSlot(String name, String value_string) {
		OMElement slot = om_factory().createOMElement("Slot", null);
		adhocquery.addChild(slot);
		slot.addAttribute("name", name, null);
		OMElement valuelist = om_factory().createOMElement("ValueList", null);
		slot.addChild(valuelist);
		OMElement value = om_factory().createOMElement("Value", null);
		valuelist.addChild(value);
		OMText text = om_factory().createOMText(value_string);
		value.addChild(text);
	}
	
	void addSlot(String name, String value_string1, String value_string2) {
		OMElement slot = om_factory().createOMElement("Slot", null);
		adhocquery.addChild(slot);
		slot.addAttribute("name", name, null);
		OMElement valuelist = om_factory().createOMElement("ValueList", null);
		slot.addChild(valuelist);
		
		OMElement value1 = om_factory().createOMElement("Value", null);
		valuelist.addChild(value1);
		OMText text1 = om_factory().createOMText(value_string1);
		value1.addChild(text1);
		
		OMElement value2 = om_factory().createOMElement("Value", null);
		valuelist.addChild(value2);
		OMText text2 = om_factory().createOMText(value_string2);
		value2.addChild(text2);
	}
	
	public void test_parse_int()  throws MetadataValidationException, XdsInternalException {
		addSlot("foo", "2");
		SqParams map = parser.parse(query);
		assertTrue(map.size() == 1);
		Object value_object = map.getParm("foo");
		assertTrue("class is " + value_object.getClass().getName(), value_object.getClass().getName().equals("java.util.ArrayList"));
		ArrayList<Object> al = (ArrayList<Object>) value_object;
		Object val = al.get(0);
		assertTrue("class is " + val.getClass().getName(), val.getClass().getName().equals("java.lang.Integer"));
		Integer in = (Integer) val;
		int value_int = in.intValue();
		assertTrue(value_int == 2);
	}
		
	public void test_parse_string()  throws MetadataValidationException, XdsInternalException {
		addSlot("foo", "'bar'");
		SqParams map = parser.parse(query);
		assertTrue("map size is " + map.size(), map.size() == 1);
		Object value_object = map.getParm("foo");
		assertTrue("class is " + value_object.getClass().getName(), value_object.getClass().getName().equals("java.util.ArrayList"));
		ArrayList<Object> al = (ArrayList<Object>) value_object;
		Object val = al.get(0);
		assertTrue("class is " + val.getClass().getName(), val.getClass().getName().equals("java.lang.String"));
		
		String in = (String) val;
		assertTrue("in is " + in, in.equals("bar"));
	}
	
	public void test_parse_string_with_single_quote()  throws MetadataValidationException, XdsInternalException {
		addSlot("foo", "'ba''r'");
		SqParams map = parser.parse(query);
		assertTrue("map size is " + map.size(), map.size() == 1);
		Object value_object = map.getParm("foo");
		assertTrue("class is " + value_object.getClass().getName(), value_object.getClass().getName().equals("java.util.ArrayList"));
		ArrayList<Object> al = (ArrayList<Object>) value_object;
		Object val = al.get(0);
		assertTrue("class is " + val.getClass().getName(), val.getClass().getName().equals("java.lang.String"));
		String in = (String) val;
		assertTrue("in is " + in, in.equals("ba''r"));
	}
	
	public void test_parse_int_array()  throws MetadataValidationException, XdsInternalException {
		addSlot("foo", "(2,3)");
		SqParams map = parser.parse(query);
		assertTrue("map size is " + map.size(), map.size() == 1);
		Object value_object = map.getParm("foo");
		assertTrue("class is " + value_object.getClass().getName(), value_object.getClass().getName().equals("java.util.ArrayList"));
		ArrayList in = (ArrayList) value_object;		
		assertTrue("size is " + in.size() + "\nvalues are " + in.toString(), in.size() == 2);
		Object val1_object =  in.get(0);
		assertTrue("class is " + val1_object.getClass().getName(), val1_object.getClass().getName().equals("java.lang.Integer"));
		int val1 = ((Integer)val1_object).intValue();
		assertTrue(val1 == 2);
		Object val2_object =  in.get(1);
		assertTrue("class is " + val2_object.getClass().getName(), val2_object.getClass().getName().equals("java.lang.Integer"));
		int val2 = ((Integer)val2_object).intValue();
		assertTrue("val2 is " + val2, val2 == 3);
	}
	
	public void test_parse_string_array() throws MetadataValidationException, XdsInternalException {
		addSlot("foo", "('2', '3' )");
		SqParams map = parser.parse(query);
		assertTrue("map size is " + map.size(), map.size() == 1);
		Object value_object = map.getParm("foo");
		assertTrue("class is " + value_object.getClass().getName(), value_object.getClass().getName().equals("java.util.ArrayList"));
		ArrayList in = (ArrayList) value_object;		
		assertTrue("size is " + in.size() + "\nvalues are " + in.toString(), in.size() == 2);
		Object val1_object =  in.get(0);
		assertTrue("class is " + val1_object.getClass().getName(), val1_object.getClass().getName().equals("java.lang.String"));
		String val1 = (String)val1_object;
		assertTrue("val1 is " + val1, val1.equals("2"));
		Object val2_object =  in.get(1);
		assertTrue("class is " + val2_object.getClass().getName(), val2_object.getClass().getName().equals("java.lang.String"));
		String val2 = (String)val2_object;
		assertTrue("val2 is " + val2, val2.equals("3"));
	}
		
	public void test_multiple_int_slot_values() throws MetadataValidationException, XdsInternalException {
		addSlot("foo", "2", "3");
		SqParams map = parser.parse(query);
		assertTrue("map size is " + map.size(), map.size() == 1);
		Object value_object = map.getParm("foo");
		assertTrue("class is " + value_object.getClass().getName(), value_object.getClass().getName().equals("java.util.ArrayList"));
		ArrayList in = (ArrayList) value_object;		
		assertTrue("size is " + in.size() + "\nvalues are " + in.toString(), in.size() == 2);
		Object val1_object =  in.get(0);
		assertTrue("class is " + val1_object.getClass().getName(), val1_object.getClass().getName().equals("java.lang.Integer"));
		int val1 = ((Integer)val1_object).intValue();
		assertTrue(val1 == 2);
		Object val2_object =  in.get(1);
		assertTrue("class is " + val2_object.getClass().getName(), val2_object.getClass().getName().equals("java.lang.Integer"));
		int val2 = ((Integer)val2_object).intValue();
		assertTrue("val2 is " + val2, val2 == 3);
	}

	public void test_multiple_string_slot_values() throws MetadataValidationException, XdsInternalException {
		addSlot("foo", "('foo','bar')", "('fuz', 'baz')");
		SqParams map =  parser.parse(query);
		//System.out.println(map.toString());
		assertTrue("map size is " + map.size(), map.size() == 1);
		Object value_object = map.getParm("foo");
		assertTrue("class is " + value_object.getClass().getName(), value_object.getClass().getName().equals("java.util.ArrayList"));
		ArrayList in = (ArrayList) value_object;		
		assertTrue("size is " + in.size() + "\nvalues are " + in.toString(), in.size() == 4);
		Object val1_object =  in.get(0);
		assertTrue("class is " + val1_object.getClass().getName(), val1_object.getClass().getName().equals("java.lang.String"));
		Object val2_object =  in.get(1);
		assertTrue("class is " + val2_object.getClass().getName(), val2_object.getClass().getName().equals("java.lang.String"));
	}

	public void test_multiple_string_slots() throws MetadataValidationException, XdsInternalException {
		addSlot("floo", "('foo', 'fax')");
		addSlot("floo", "('bar')");
		SqParams map =  parser.parse(query);
		//System.out.println("and is " + map.toString());
		Object floo = map.getParm("floo");
		String flooClass = floo.getClass().getName();
		assertTrue("Class is " + flooClass, flooClass.endsWith((".And")));
		And and = (And) floo;
		assertTrue("size is " + and.size(), and.size() == 2);
		Object and0 = and.get(0);
		String and0Class = and0.getClass().getName();
		assertTrue("and0Class is " + and0Class, and0Class.endsWith(".ArrayList"));
		ArrayList<String> and0Al = (ArrayList<String>) and0;
		assertTrue("@0 is " + and0Al.get(0), and0Al.get(0).equals("foo"));
	}

	public void test_multiple_param() throws MetadataValidationException, XdsInternalException {
		addSlot("foo", "2");
		addSlot("bar", "3");
		SqParams map1 = parser.parse(query);
		//System.out.println("map1 is " + map1.toString());
		assertTrue(map1.size() == 2);
		
		Object value_object1 = map1.getParm("foo");
		String class1 = value_object1.getClass().getName();
		assertTrue("class is " + class1 + " value is " + value_object1.toString(), class1.endsWith(".ArrayList"));
		ArrayList<Integer> al1 = (ArrayList<Integer>) value_object1;
		assertTrue("size is " + al1.size(), al1.size() == 1);
		assertTrue("@0 = " + al1.get(0), al1.get(0).intValue() == 2);
	}
	
}
