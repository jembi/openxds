package gov.nist.registry.common2.registry;

import gov.nist.registry.common2.exception.XdsInternalException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;

public class IdParser {
	Metadata m;
	// symbolic compiler
	List referencingAttributes = null;
	List identifyingAttributes = null;
	List symbolicIdReplacements = null;
	HashMap assignedUuids = null;

	public IdParser(Metadata m) {
		referencingAttributes = new ArrayList();
		identifyingAttributes = new ArrayList();
		this.m = m;
		parse();
	}

	public List getDefinedIds() {
		List defined = new ArrayList();

		for (Iterator it=this.identifyingAttributes.iterator(); it.hasNext(); ) {
			OMAttribute attr = (OMAttribute) it.next();
			String id = attr.getAttributeValue();

			if ( !defined.contains(id))
				defined.add(id);
		}

		return defined;
	}

	public List getReferencedIds() {
		List refer = new ArrayList();
		for (Iterator it=this.referencingAttributes.iterator(); it.hasNext(); ) {
			OMAttribute attr = (OMAttribute) it.next();
			String id = attr.getAttributeValue();

			if ( !refer.contains(id))
				refer.add(id);
		}

		return refer;
	}

	public List getUndefinedIds() {
		List referenced = this.getReferencedIds();
		List defined = this.getDefinedIds();
		List undefined = new ArrayList();

		for (Iterator it=referenced.iterator(); it.hasNext(); ) {
			String id = (String) it.next();

			if ( ! defined.contains(id))
				undefined.add(id);

		}

		return undefined;
	}

	void parse() {
		List<OMElement> allObjects = m.getAllObjects();

		for (int i=0; i<allObjects.size(); i++) {
			OMElement obj = allObjects.get(i);
			OMAttribute idAtt = obj.getAttribute(MetadataSupport.id_qname);
			String type = obj.getLocalName();

			if (idAtt != null)
				identifyingAttributes.add(idAtt);

			if (type.equals("Classification")) {
				OMAttribute a = obj.getAttribute(MetadataSupport.classified_object_qname);
				if (a != null)
					referencingAttributes.add(a);
				a = obj.getAttribute(MetadataSupport.classificationscheme_qname);
				if (a != null)
					referencingAttributes.add(a);
			} else if (type.equals("Association")) {
				OMAttribute a = obj.getAttribute(MetadataSupport.source_object_qname);
				if (a != null)
					referencingAttributes.add(a);
				a = obj.getAttribute(MetadataSupport.target_object_qname);
				if (a != null)
					referencingAttributes.add(a);
			}

			for (Iterator it1=obj.getChildElements(); it1.hasNext(); ) {
				OMElement objI = (OMElement) it1.next();
				String typeI = objI.getLocalName();

				if (typeI.equals("ExternalIdentifier")) {
					OMAttribute att = objI.getAttribute(MetadataSupport.registry_object_qname);
					if (att != null) 
						referencingAttributes.add(att);
					att = objI.getAttribute(MetadataSupport.identificationscheme_qname);
					if (att != null) 
						referencingAttributes.add(att);
				} else if (typeI.equals("Classification")) {
					OMAttribute a = objI.getAttribute(MetadataSupport.classified_object_qname);
					if (a != null)
						referencingAttributes.add(a);
					a = objI.getAttribute(MetadataSupport.classificationscheme_qname);
					if (a != null)
						referencingAttributes.add(a);
				}
			}
		}

	}

	/*
	 * Symbol Compiler
	 */
	public void compileSymbolicNamesIntoUuids () throws XdsInternalException {

		// make list of all symbolic names used in metadata
		// allocate UUID for these names
		// update attributes that define these symbolic names with UUIDs
		List symbolicNames = new ArrayList();
		List uuids = new ArrayList();
		assignedUuids = new HashMap();
		for (int i=0; i<identifyingAttributes.size(); i++) {
			OMAttribute att = (OMAttribute) identifyingAttributes.get(i);
			String name = att.getAttributeValue();
			if (name.startsWith("urn:uuid:"))
				continue;
			symbolicNames.add(name);
			String uuid = UuidAllocator.allocate();
			uuids.add(uuid);    // can index uuids like symbolic_names
			att.setAttributeValue(uuid);
			assignedUuids.put(name, uuid);
		}

		// update all references to objects that we just allocated uuids for
		for (int i=0; i<referencingAttributes.size(); i++) {
			OMAttribute att = (OMAttribute) referencingAttributes.get(i);
			String symbolicName = att.getAttributeValue();
			if (symbolicName.startsWith("urn:uuid:"))
				continue;
			int idIndex = symbolicNames.indexOf(symbolicName);
			if (idIndex == -1)
				throw new XdsInternalException("Metadata:compileSymbolicNamesIntoUuids(): cannot find symbolic name " + symbolicName + " in tables");
			String uuid = (String) uuids.get(idIndex);
			att.setAttributeValue(uuid);
		}
	}

	public HashMap getSymbolicNameUuidMap() {
		return assignedUuids;
	}

	public OMElement getApproveObjectsRequest(List uuids) {
		OMElement req = MetadataSupport.om_factory.createOMElement("ApproveObjectsRequest", MetadataSupport.ebLcm3);
		req.declareNamespace(MetadataSupport.ebRSns3);
		req.declareNamespace(MetadataSupport.ebRIMns3);
		req.declareNamespace(MetadataSupport.ebLcm3);
		req.declareNamespace(MetadataSupport.xdsB);
		req.addChild(mk_object_ref_list(uuids));
		return req;
	}

	public OMElement getDeprecateObjectsRequest(List uuids) {
		OMElement req = MetadataSupport.om_factory.createOMElement("DeprecateObjectsRequest", MetadataSupport.ebLcm3);
		req.addChild(mk_object_ref_list(uuids));
		return req;

	}

	private OMElement mk_object_ref_list(List uuids) {
		OMElement object_ref_list = MetadataSupport.om_factory.createOMElement("ObjectRefList", MetadataSupport.ebRIMns3);
		for (Iterator it=uuids.iterator(); it.hasNext(); ) {
			String uuid = (String) it.next();
			OMAttribute att = MetadataSupport.om_factory.createOMAttribute("id", null, uuid);
			OMElement object_ref = MetadataSupport.om_factory.createOMElement("ObjectRef", MetadataSupport.ebRIMns3);
			object_ref.addAttribute(att);
			object_ref_list.addChild(object_ref);
		}
		return object_ref_list;
	}

	/*
	 * Registry adaptor - approve objects
	 */

	List<OMElement> approveable_objects(Metadata m) {
		List<OMElement> o = new ArrayList<OMElement>();
		o.addAll(m.getExtrinsicObjects());
		o.addAll(m.getRegistryPackages());
		return o;
	}

	public List<String> approvable_object_ids(Metadata m) {
		return m.getObjectIds(approveable_objects(m));
	}

	boolean is_approveable_object(Metadata m, OMElement o) {
		if (m.getExtrinsicObjects().contains(o))
			return true;
		if (m.getRegistryPackages().contains(o))
			return true;
		return false;
	}



}
