package gov.nist.registry.common2.registry.Objects;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.registry.MetadataSupport;

import org.apache.axiom.om.OMElement;

public class RegistryObject extends RegistryItem {
	String id;

	public RegistryObject(OMElement ro) throws MetadataException { 
		super(ro);
		parse();
		unchanged();
	}

	void parse() throws MetadataException {
		((RegistryItem)this).parse();
		id = get().getAttributeValue(MetadataSupport.id_qname);
	}
	
	public void save() {
		if ( !isChanged())
			return;
		get().addAttribute("id", this.id, null);
		unchanged();
	}
}
