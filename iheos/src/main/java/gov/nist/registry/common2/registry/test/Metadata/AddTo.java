package gov.nist.registry.common2.registry.test.Metadata;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;

import java.util.ArrayList;
import java.util.List;

import org.apache.axiom.om.OMElement;
import org.testng.annotations.Test;

public class AddTo {
	
	@Test
	public void addToSingle() throws XdsInternalException, MetadataException, MetadataValidationException {
		Metadata m = new Metadata();
		
		assert m.getObjectRefs().size() == 0;
		
		m.addToMetadata(mkObjectRef(), true);
		
		assert m.getObjectRefs().size() == 1;
	}
	
	@Test
	public void addToMultiple() throws XdsInternalException, MetadataException, MetadataValidationException {
		Metadata m = new Metadata();

		m.addToMetadata(mkObjectRefList(), true);
		
		assert m.getObjectRefs().size() == 2;
		
	}
	
	@Test
	public void addToSingleDup() throws XdsInternalException, MetadataException, MetadataValidationException {
		Metadata m = new Metadata();
		
		assert m.getObjectRefs().size() == 0;
		
		m.addToMetadata(mkObjectRef(), true);

		m.addToMetadata(mkObjectRef(), true);
		
		assert m.getObjectRefs().size() == 1;
	}
	
	OMElement mkObjectRef() {
		OMElement or;
		
		or = MetadataSupport.om_factory.createOMElement("ObjectRef", null);
		or.addAttribute(MetadataSupport.om_factory.createOMAttribute("id", null, "foo"));
		return or;
	}

	List<OMElement> mkObjectRefList() {
		List<OMElement> list = new ArrayList<OMElement>();
		
		OMElement or;
		
		or = MetadataSupport.om_factory.createOMElement("ObjectRef", null);
		or.addAttribute(MetadataSupport.om_factory.createOMAttribute("id", null, "foo"));
		list.add(or);
		
		or = MetadataSupport.om_factory.createOMElement("ObjectRef", null);
		or.addAttribute(MetadataSupport.om_factory.createOMAttribute("id", null, "bar"));
		list.add(or);
		
		return list;
	}

}
