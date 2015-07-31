package gov.nist.registry.common2.registry.Objects;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.registry.MetadataSupport;

import java.util.ArrayList;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMText;

public class Slot extends RegistryItem {
	String name;
	ArrayList<OMText> values;

	public Slot(OMElement ro) throws MetadataException { 
		super(ro);
		parse();
		unchanged();
	}

	void parse() throws MetadataException {
		((RegistryItem)this).parse();
		OMElement vlist = MetadataSupport.firstChildWithLocalName(get(), "ValueList");
		if (vlist == null)
			throw new MetadataException("Slot.java: Slot element does not have a 'ValueList' sub-element");
		values = new ArrayList<OMText>();
	}


}
