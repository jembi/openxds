package gov.nist.registry.common2.registry.Objects;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.registry.MetadataSupport;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;

public class Name extends RegistryItem {
	OMAttribute name_att;

	public Name(OMElement ro) throws MetadataException { 
		super(ro);
		parse();
		unchanged();
	}

	void parse() throws MetadataException {
		((RegistryItem)this).parse();
		OMElement loc = MetadataSupport.firstChildWithLocalName(get(), "LocalizedString");
		if (loc == null)
			throw new MetadataException("Name.java: Name element does not have a 'LocalizedString' sub-element");
		this.name_att = loc.getAttribute(MetadataSupport.value_qname);
	}

	public String getValue() {
		return name_att.getAttributeValue();
	}
	
	public void setValue(String name) {
		name_att.setAttributeValue(name);
	}

}
