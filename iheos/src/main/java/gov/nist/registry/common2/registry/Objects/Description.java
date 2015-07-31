package gov.nist.registry.common2.registry.Objects;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.registry.MetadataSupport;

import org.apache.axiom.om.OMElement;

public class Description extends Name {

	public Description(OMElement ro) throws MetadataException { 
		super(ro);
	}

	void parse() throws MetadataException {
		((RegistryItem)this).parse();
		OMElement loc = MetadataSupport.firstChildWithLocalName(get(), "LocalizedString");
		if (loc == null)
			throw new MetadataException("Description.java: Description element does not have a 'LocalizedString' sub-element");
		this.name_att = loc.getAttribute(MetadataSupport.value_qname);
	}


}
