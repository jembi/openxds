package gov.nist.registry.common2.registry;


import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.xml.Util;

import java.util.ArrayList;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMException;
import org.apache.axiom.om.OMNode;

public class OmLogger {

	public OMElement add_simple_element(OMElement parent, String name) {
		OMElement ele = MetadataSupport.om_factory.createOMElement(name, null);
		parent.addChild(ele);
		return ele;
	}

	public OMElement add_simple_element_with_id(OMElement parent, String name,
			String id) {
		OMElement ele = add_simple_element(parent, name);
		ele.addAttribute("id", id, null);
		return ele;
	}

	public OMElement add_simple_element_with_id(OMElement parent, String name,
			String id, String value) {
		OMElement ele = add_simple_element(parent, name);
		ele.addAttribute("id", id, null);
		ele.addChild(MetadataSupport.om_factory.createOMText(value));
		return ele;
	}

	public void add_name_value(OMElement parent, String name, ArrayList<OMElement> data) {
		for (OMElement ele : data) {
			OMElement elel = MetadataSupport.om_factory.createOMElement(name, null);
			try {
				elel.addChild(Util.deep_copy(ele));
			} catch (XdsInternalException e) {
				e.printStackTrace();
			}
			parent.addChild(elel);
		}
	}

	public OMElement add_name_value(OMElement parent, String name, String value) {
		OMElement ele = MetadataSupport.om_factory.createOMElement(name, null);
		ele.addChild(MetadataSupport.om_factory.createOMText(value));
		parent.addChild(ele);
		return ele;
	}

	public OMElement add_name_value_with_id(OMElement parent, String name, String id, String value) {
		OMElement ele = MetadataSupport.om_factory.createOMElement(name, null);
		ele.addAttribute("id", id, null);
		ele.addChild(MetadataSupport.om_factory.createOMText(value));
		parent.addChild(ele);
		return ele;
	}

	public OMElement add_name_value(OMElement parent, String name, OMElement value) {
		OMNode val = value;
		OMElement ele = MetadataSupport.om_factory.createOMElement(name, null);
		if (val == null)
			val = MetadataSupport.om_factory.createOMText("null");
		else {
			try {
				val = Util.deep_copy(value);
			} catch (Exception e) {}
		}
		try {
			ele.addChild(val);
		}
		catch (OMException e) {
			Util.mkElement("Exception", e.getMessage(), ele);
		}
		parent.addChild(ele);
		return ele;
	}

	public OMElement add_name_value(OMElement parent, String name, OMElement value1, OMElement value2) {
		OMElement ele = MetadataSupport.om_factory.createOMElement(name, null);
		OMNode val1 = value1;
		if (val1 == null)
			val1 = MetadataSupport.om_factory.createOMText("null");
		ele.addChild(val1);
		OMNode val2 = value2;
		if (val2 == null)
			val2 = MetadataSupport.om_factory.createOMText("null");
		ele.addChild(val2);
		parent.addChild(ele);
		return ele;
	}

	public OMElement add_name_value(OMElement parent, String name, OMElement value1, OMElement value2, OMElement value3) {
		OMElement ele = MetadataSupport.om_factory.createOMElement(name, null);
		OMNode val1 = value1;
		if (val1 == null)
			val1 = MetadataSupport.om_factory.createOMText("null");
		ele.addChild(val1);
		OMNode val2 = value2;
		if (val2 == null)
			val2 = MetadataSupport.om_factory.createOMText("null");
		ele.addChild(val2);
		OMNode val3 = value3;
		if (val3 == null)
			val3 = MetadataSupport.om_factory.createOMText("null");
		ele.addChild(val3);
		parent.addChild(ele);
		return ele;
	}

	public OMElement create_name_value(String name, OMElement value) {
		OMNode val = value;
		OMElement ele = MetadataSupport.om_factory.createOMElement(name, null);
		if (val == null)
			val = MetadataSupport.om_factory.createOMText("null");
		ele.addChild(val);
		return ele;
	}

	public OMElement create_name_value(String name, String value) {
		OMElement ele = MetadataSupport.om_factory.createOMElement(name, null);
		ele.addChild(MetadataSupport.om_factory.createOMText(value));
		return ele;
	}

	public OMElement add_name_value(OMElement parent, OMElement element) {
		parent.addChild(element);
		return element;
	}

}
