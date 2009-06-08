package gov.nist.registry.common2.registry.Objects;

import gov.nist.registry.common2.exception.MetadataException;

import org.apache.axiom.om.OMElement;

public class RegistryItem {
	boolean edited;
	OMElement ele;
	
	public RegistryItem(OMElement ele) throws MetadataException {
		if (ele == null) 
			throw new MetadataException(this.getClass().getName() + ": cannot create from null element");
		set(ele);
		edited = false;
	}
	
	void parse() throws MetadataException {   }
	
	public void changed() {
		edited = true;
	}
	
	public boolean isChanged() {
		return edited;
	}
	
	public void unchanged() {
		edited = false;
	}
	
	public void save() {
		edited = false;
	}
	
	public void set(OMElement ele) {
		this.ele = ele;
	}
	
	public OMElement get() {
		return ele;
	}
}
