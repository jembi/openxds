package gov.nist.registry.common2.registry;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.NoMetadataException;
import gov.nist.registry.common2.exception.NoSubmissionSetException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.xml.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.openhealthtools.openxds.log.LogMessage;

public class Metadata {
	protected OMFactory fac;
	boolean interpretAsSubmission = true;
	// private final static Logger logger = Logger.getLogger(Metadata.class);

	OMElement metadata;

	// wrapper
	OMElement wrapper; // current metadata document being parsed
	List<OMElement> wrappers; // the collection of all metadata documents
								// included in current tables

	// parsing metadata means
	// 1) loading these structures
	// 2) allocating id attributes for objects that don't already have them
	boolean parsed;
	List<OMElement> extrinsicObjects = null;
	List<OMElement> folders = null;
	OMElement submissionSet = null;
	List<OMElement> submissionSets = null;
	List<OMElement> registryPackages = null;
	List<OMElement> associations = null;
	List<OMElement> objectRefs = null;
	List<OMElement> classifications = null;
	List<OMElement> allObjects = null;
	List<String> objectsToDeprecate = null;
	List<String> objectsReferenced = null;
	// for id, list of classification uuids
	HashMap<String, List<String>> classificationsOfId = null;

	public OMElement getMetadata() {
		return metadata;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();

		buf.append("ExtrinsicObjects\n");
		for (OMElement e : extrinsicObjects)
			buf.append(e).append("\n");

		buf.append("Folders\n");
		for (OMElement e : folders)
			buf.append(e).append("\n");

		buf.append("SubmissionSets\n");
		for (OMElement e : submissionSets)
			buf.append(e).append("\n");

		buf.append("Associations\n");
		for (OMElement e : associations)
			buf.append(e).append("\n");

		buf.append("ObjectRefs\n");
		for (OMElement e : objectRefs)
			buf.append(e).append("\n");

		return buf.toString();
	}

	boolean version2;

	OMElement metadataDup = null; // both of these are set by dup_wrapper which
									// is used by metadata_copy
	OMElement wrapperDup = null;
	boolean mustDup = false;
	int idAllocation = 0;

	IdIndex idIndex = null;

	public static final List<String> iheAssocTypes = new ArrayList<String>() {
		{
			add("APND");
			add("XFRM");
			add("RPLC");
			add("XFRM_RPLC");
			add("signs");
		}
	};

	public void clearObjectRefs() {
		objectRefs = new ArrayList<OMElement>();
	}

	public List<OMElement> getAllLeafClasses() {
		List<OMElement> lc = new ArrayList<OMElement>();

		lc.addAll(extrinsicObjects);
		lc.addAll(registryPackages);
		lc.addAll(associations);

		return lc;
	}

	public Metadata mkClone() throws XdsInternalException, MetadataException,
			MetadataValidationException {
		Metadata m = new Metadata();

		m.interpretAsSubmission = this.interpretAsSubmission;
		if (wrappers != null) {
			if (m.wrappers == null)
				m.wrappers = new ArrayList<OMElement>();
			for (OMElement ele : wrappers) {
				m.wrappers.add(ele);
			}
		}

		m.extrinsicObjects.addAll(extrinsicObjects);
		m.folders.addAll(folders);
		m.submissionSets.addAll(submissionSets);
		m.registryPackages.addAll(registryPackages);
		m.associations.addAll(associations);
		m.objectRefs.addAll(objectRefs);
		m.classifications.addAll(classifications);
		m.allObjects.addAll(allObjects);
		m.submissionSet = submissionSet;

		return m;
	}

	String allocate_id() {
		idAllocation += 1;
		return ("ID_" + String.valueOf(this.hashCode()) + "_" + idAllocation);
	}

	public void rmDuplicates() {
		rmDuplicates(extrinsicObjects);
		rmDuplicates(folders);
		rmDuplicates(submissionSets);
		rmDuplicates(registryPackages);
		rmDuplicates(associations);
		rmDuplicates(objectRefs);
		rmDuplicates(classifications);

		rmFromObjectRefs(extrinsicObjects);
		rmFromObjectRefs(registryPackages);
		rmFromObjectRefs(associations);
		rmFromObjectRefs(classifications);

		allObjects = new ArrayList<OMElement>();
		allObjects.addAll(extrinsicObjects);
		allObjects.addAll(registryPackages);
		allObjects.addAll(associations);
		allObjects.addAll(classifications);
		allObjects.addAll(objectRefs);
	}

	void rmFromObjectRefs(List<OMElement> set) {
		for (int i = 0; i < set.size(); i++) {
			String id = id(set.get(i));
			boolean restart = true;
			while (restart) {
				restart = false;
				for (int j = 0; j < objectRefs.size(); j++) {
					if (id.equals(id(objectRefs.get(j)))) {
						objectRefs.remove(j);
						restart = true;
						break;
					}
				}
			}
		}
	}

	void rmDuplicates(List<OMElement> set) {
		boolean running = true;
		while (running) {
			running = false;
			for (int targetI = 0; targetI < set.size(); targetI++) {
				OMElement target = set.get(targetI);
				String targetId = id(target);
				for (int i = targetI + 1; i < set.size(); i++) {
					OMElement it = set.get(i);
					if (targetId.equals(id(it))) {
						set.remove(i);
						running = true;
						break;
					}
				}
			}
		}
	}

	public boolean isObjectRefsOnly() {
		return submissionSets.size() == 0 && extrinsicObjects.size() == 0
				&& folders.size() == 0 && associations.size() == 0
				&& classifications.size() == 0 && objectRefs.size() != 0;
	}

	public void filter(List<String> ids) {
		submissionSets = filter(submissionSets, ids);
		extrinsicObjects = filter(extrinsicObjects, ids);
		folders = filter(folders, ids);
		associations = filter(associations, ids);
		allObjects = filter(allObjects, ids);
	}

	List<OMElement> filter(List<OMElement> objects, List<String> ids) {
		List<OMElement> out = new ArrayList<OMElement>();
		for (OMElement object : objects) {
			String id = id(object);
			if (ids.contains(id))
				out.add(object);
		}
		return out;
	}

	public List<OMElement> getNonObjectRefs() {
		List<OMElement> objs = new ArrayList<OMElement>();

		objs.addAll(this.submissionSets);
		objs.addAll(this.folders);
		objs.addAll(this.extrinsicObjects);
		objs.addAll(this.associations);
		objs.addAll(this.classifications);

		return objs;

	}

	/**
	 * Move top-level classifications under the objects they classification.
	 * Classification object can be top-level objects or nested inside the
	 * objects they classify. Move all top-level classifications under the
	 * objects they classify.
	 */
	public void embedClassifications() throws MetadataException {
		for (OMElement classification : classifications) {
			String classified_object_id = classification
					.getAttributeValue(MetadataSupport.classified_object_qname);
			OMElement classified_object = this
					.getObjectById(classified_object_id);
			classification.detach();
			classified_object.addChild(classification);
			allObjects.remove(classification);
		}
		classifications.clear();
	}

	List<List<OMElement>> get_metadata_containers() {
		List<List<OMElement>> containers = new ArrayList<List<OMElement>>();

		containers.add(extrinsicObjects);
		containers.add(folders);
		containers.add(submissionSets);
		containers.add(associations);
		containers.add(objectRefs);
		containers.add(classifications);
		containers.add(allObjects);
		containers.add(registryPackages);

		return containers;
	}

	OMNamespace current_namespace() {
		if (version2)
			return MetadataSupport.ebRIMns2;
		return MetadataSupport.ebRIMns3;
	}

	public void setGrokMetadata(boolean x) {
		interpretAsSubmission = x;
	}

	void setMetadata(OMElement metadata) {
		this.metadata = metadata;
		init();
	}

	void runParser() throws MetadataException, MetadataValidationException {
		runParser(false /* do not remove duplicates */);
	}

	void runParser(boolean rm_duplicates) throws MetadataException,
			MetadataValidationException {
		if (wrapper == null)
			wrapper = find_metadata_wrapper();
		if (wrappers == null)
			wrappers = new ArrayList<OMElement>();
		wrappers.add(wrapper);
		parse(rm_duplicates);
	}

	public Metadata() {
		wrapper = null;
		metadata = null;
		init();
		this.setGrokMetadata(false);
	}

	public Metadata(OMElement metadata) throws MetadataException,
			MetadataValidationException {
		this.metadata = metadata;
		runParser();
	}

	public Metadata(File metadata_file) throws XdsInternalException,
			MetadataException, MetadataValidationException {
		metadata = Util.parse_xml(metadata_file);
		runParser();
	}

	public Metadata(File metadata_file, boolean parse)
			throws XdsInternalException, MetadataException,
			MetadataValidationException {
		metadata = Util.parse_xml(metadata_file);
		wrapper = null;
		wrappers = new ArrayList();
		if (parse) {
			wrapper = find_metadata_wrapper();
			wrappers.add(wrapper);
			parse(false);
		} else {
			init();
		}
	}

	public Metadata(File metadata_file, boolean parse, boolean find_wrapper)
			throws XdsInternalException, MetadataException,
			MetadataValidationException {
		metadata = Util.parse_xml(metadata_file);
		wrapper = null;
		wrappers = new ArrayList();
		if (find_wrapper) {
			wrapper = find_metadata_wrapper();
			wrappers.add(wrapper);
		}
		if (parse)
			parse(false);
		else
			init();
	}

	public Metadata(OMElement metadata, boolean parse, boolean find_wrapper)
			throws XdsInternalException, MetadataException,
			MetadataValidationException {
		this.metadata = metadata;
		wrapper = null;
		wrappers = new ArrayList();
		if (find_wrapper) {
			wrapper = find_metadata_wrapper();
			wrappers.add(wrapper);
		}
		if (parse)
			parse(false);
		else
			init();
	}

	public void addMetadata(Metadata m) throws MetadataException,
			MetadataValidationException {
		if (m.getRoot() != null)
			addMetadata(m.getRoot(), false);
	}

	public void addMetadata(Metadata m, boolean discard_duplicates)
			throws MetadataException, MetadataValidationException {
		addMetadata(m.getRoot(), discard_duplicates);
	}

	public void addMetadata(OMElement metadata) throws MetadataException,
			MetadataValidationException {
		addMetadata(metadata, false);
	}

	public void addMetadata(OMElement metadata, boolean discard_duplicates)
			throws MetadataException, MetadataValidationException {
		init();
		if (wrappers == null)
			wrappers = new ArrayList();
		this.metadata = metadata;
		wrapper = find_metadata_wrapper();
		wrappers.add(wrapper);
		re_index();

		parse(discard_duplicates);
	}

	/**
	 * Add to metadata collection. If collection empty then initialize it.
	 * 
	 * @param metadata
	 *            - a submission or a single metadata object. Will be wrapped
	 *            internally (made into single XML document)
	 * @param discard_duplicates
	 * @throws XdsInternalException
	 * @throws MetadataValidationException
	 * @throws MetadataException
	 */
	public Metadata addToMetadata(OMElement metadata, boolean discard_duplicates)
			throws XdsInternalException, MetadataException,
			MetadataValidationException {
		boolean run_parse = true;
		return addToMetadata(metadata, discard_duplicates, run_parse);
	}

	private Metadata addToMetadata(OMElement new_metadata,
			boolean discard_duplicates, boolean run_parse)
			throws XdsInternalException, MetadataException,
			MetadataValidationException {
		boolean hasExistingData = false;

		if (wrapper == null) {
			wrapper = mkWrapper();
			metadata = new_metadata;
			if (wrappers == null)
				wrappers = new ArrayList<OMElement>();
			wrappers.add(wrapper);
		} else
			hasExistingData = true;

		wrapper.addChild(Util.deep_copy(new_metadata));

		if (run_parse)
			if (hasExistingData)
				reParse(discard_duplicates);
			else
				parse(discard_duplicates);
		return this;
	}

	/**
	 * Add to metadata collection. If collection empty then initialize it.
	 * 
	 * @param metadata
	 *            - a collection of metadata objects. Will be wrapped internally
	 *            (made into single XML document)
	 * @param discard_duplicates
	 * @throws XdsInternalException
	 * @throws MetadataValidationException
	 * @throws MetadataException
	 */
	public Metadata addToMetadata(List<OMElement> metadata,
			boolean discard_duplicates) throws XdsInternalException,
			MetadataException, MetadataValidationException {
		for (OMElement ele : metadata) {
			addToMetadata(ele, discard_duplicates, false);
		}
		parse(discard_duplicates);
		return this;
	}

	OMElement mkWrapper() {
		return MetadataSupport.om_factory.createOMElement("root", null);
	}

	private void re_index() {
		idIndex = null; // lazy
	}

	/*
	 * Basic Operation
	 */

	public OMElement addExtrinsicObject(OMElement eo) {
		extrinsicObjects.add(eo);
		allObjects.add(eo);
		return eo;
	}

	public void addExtrinsicObjects(List<OMElement> eos) {
		extrinsicObjects.addAll(eos);
		allObjects.addAll(eos);
	}

	@SuppressWarnings("unchecked")
	public void addObjectRefs(List<?> object_refs_or_ids)
			throws MetadataException {
		if (object_refs_or_ids.size() == 0)
			return;
		Object ele = object_refs_or_ids.get(0);
		if (ele instanceof OMElement)
			objectRefs.addAll((List<OMElement>) object_refs_or_ids);
		else if (ele instanceof String)
			mkObjectRefs((List<String>) object_refs_or_ids);
		else
			throw new MetadataException("Don't understand format "
					+ ele.getClass().getName());
	}

	public boolean isVersion2() {
		return version2;
	}

	public OMFactory om_factory() {
		if (fac == null)
			fac = OMAbstractFactory.getOMFactory();
		return fac;
	}

	public OMElement getRoot() {
		return metadata;
	}

	public String structure() {
		return this.getSubmissionSetIds().size() + " SS + "
				+ this.extrinsicObjects.size() + " EO + " + this.folders.size()
				+ " Fol + " + this.associations.size() + " A + "
				+ this.objectRefs.size() + " OR";
	}

	public List<OMElement> getExternalIdentifiers(OMElement registry_object,
			String id_scheme) {
		List<OMElement> results = new ArrayList<OMElement>();
		QName idscheme = MetadataSupport.identificationscheme_qname;
		for (Iterator it = registry_object.getChildElements(); it.hasNext();) {
			OMElement ele = (OMElement) it.next();
			if (!ele.getLocalName().equals("ExternalIdentifier"))
				continue;
			String this_id_scheme = ele.getAttributeValue(idscheme);
			if (id_scheme == null || id_scheme.equals(this_id_scheme))
				results.add(ele);
		}
		return results;
	}

	boolean has_external_identifier(OMElement registry_object, String id_scheme) {
		QName idscheme = new QName("identificationScheme");
		for (Iterator it = registry_object.getChildElements(); it.hasNext();) {
			OMElement ele = (OMElement) it.next();
			if (!ele.getLocalName().equals("ExternalIdentifier"))
				continue;
			String this_id_scheme = ele.getAttributeValue(idscheme);
			if (id_scheme.equals(this_id_scheme))
				return true;
		}
		return false;

	}

	public boolean hasSlot(OMElement obj, String slot_name) {
		if (obj == null)
			return false;
		for (OMElement slot : MetadataSupport
				.childrenWithLocalName(obj, "Slot")) {
			String name = slot
					.getAttributeValue(MetadataSupport.slot_name_qname);
			if (name.equals(slot_name))
				return true;
		}
		return false;
	}

	public boolean hasClassification(OMElement obj, String uuid) {
		if (obj == null)
			return false;
		if (uuid == null)
			return false;
		for (OMElement classif_ele : MetadataSupport.childrenWithLocalName(obj,
				"Classification")) {
			String classification_node = classif_ele
					.getAttributeValue(MetadataSupport.classificationnode_qname);
			if (uuid.equals(classification_node))
				return true;
		}
		return false;
	}

	public String getSlotValue(OMElement obj, String slot_name, int value_index) {
		if (obj == null)
			return null;
		for (OMElement slot : MetadataSupport
				.childrenWithLocalName(obj, "Slot")) {
			String name = slot
					.getAttributeValue(MetadataSupport.slot_name_qname);
			if (!name.equals(slot_name))
				continue;
			OMElement value_list = MetadataSupport.firstChildWithLocalName(
					slot, "ValueList");
			if (value_list == null)
				continue;
			int value_count = 0;
			for (OMElement value_ele : MetadataSupport.childrenWithLocalName(
					value_list, "Value")) {
				if (value_count != value_index) {
					value_count++;
					continue;
				}
				return value_ele.getText();
			}
		}
		return null;
	}

	public List<String> getSlotValues(OMElement obj, String slot_name) {
		List<String> values = new ArrayList<String>();
		if (obj == null)
			return values;
		for (OMElement slot : MetadataSupport
				.childrenWithLocalName(obj, "Slot")) {
			String name = slot
					.getAttributeValue(MetadataSupport.slot_name_qname);
			if (!name.equals(slot_name))
				continue;
			OMElement value_list = MetadataSupport.firstChildWithLocalName(
					slot, "ValueList");
			if (value_list == null)
				continue;
			for (OMElement value_ele : MetadataSupport.childrenWithLocalName(
					value_list, "Value")) {
				values.add(value_ele.getText());
			}
		}
		return values;
	}

	public String getSlotValue(String id, String slot_name, int value_index)
			throws MetadataException {
		return getSlotValue(getObjectById(id), slot_name, value_index);
	}

	/**
	 * Set new value into existing Slot.
	 * 
	 * @param obj
	 * @param slot_name
	 * @param value_index
	 * @param value
	 */
	public void setSlotValue(OMElement obj, String slot_name, int value_index,
			String value) {
		if (obj == null)
			return;
		for (OMElement slot : MetadataSupport
				.childrenWithLocalName(obj, "Slot")) {
			String name = slot
					.getAttributeValue(MetadataSupport.slot_name_qname);
			if (!name.equals(slot_name))
				continue;
			OMElement value_list = MetadataSupport.firstChildWithLocalName(
					slot, "ValueList");
			if (value_list == null)
				continue;
			int value_count = 0;
			for (OMElement value_ele : MetadataSupport.childrenWithLocalName(
					value_list, "Value")) {
				if (value_count != value_index) {
					value_count++;
					continue;
				}
				value_ele.setText(value);
			}
		}
	}

	public String getStatus(OMElement ele) {
		return ele.getAttributeValue(MetadataSupport.status_qname);
	}

	public String getPatientId(OMElement ele) throws MetadataException {
		if (ele == null)
			return null;
		String id = getId(ele);
		if (isDocument(id))
			return getExternalIdentifierValue(id,
					MetadataSupport.XDSDocumentEntry_patientid_uuid);
		if (isSubmissionSet(id))
			return getExternalIdentifierValue(id,
					MetadataSupport.XDSSubmissionSet_patientid_uuid);
		if (isFolder(id))
			return getExternalIdentifierValue(id,
					MetadataSupport.XDSFolder_patientid_uuid);
		return null;
	}

	
	/**
	 * Does the metadata belong to a single Patient ID?
	 * @return
	 * @throws MetadataException
	 */
	public boolean isPatientIdConsistent() throws MetadataException {
		String patientID = null;
		for (OMElement ele : allObjects) {
			String pid = getPatientId(ele);
			if (patientID == null) {
				patientID = pid;
				continue;
			}
			if (pid == null)
				continue;
			if (!patientID.equals(pid))
				return false;
		}
		return true;
	}

	public List<String> getPatientIds() throws MetadataException {
		List<String> pids = new ArrayList<String>();

		for (OMElement obj : getMajorObjects()) {
			String pid = getPatientId(obj);
			if (pid != null && !pids.contains(pid))
				pids.add(pid);
		}

		return pids;
	}

	public List<OMElement> getMajorObjects() {
		return allObjects;
	}

	public List<OMElement> getLeafClassObjects() {
		List<OMElement> objs = new ArrayList<OMElement>();

		objs.addAll(registryPackages);
		objs.addAll(extrinsicObjects);
		objs.addAll(associations);

		return objs;
	}

	public void clearLeafClassObjects() {
		registryPackages.clear();
		submissionSet = null;
		submissionSets.clear();
		extrinsicObjects.clear();
		associations.clear();
		re_index();
	}

	public Metadata getMetadataAsObjectRefs() {
		// Request is for ObjectRefs only so build new metadata object with
		// objectrefs
		Metadata orMetadata = new Metadata();
		orMetadata.mkObjectRefs(getIds(getLeafClassObjects()));
		return orMetadata;
	}

	public void mkObjectRefs(List<String> ids) {
		for (String id : ids) {
			mkObjectRef(id);
		}
	}

	public void mkObjectRef(String id) {
		OMElement objRef = MetadataSupport.om_factory
				.createOMElement(MetadataSupport.objectRefQName);
		objRef.addAttribute("id", id, null);
		objectRefs.add(objRef);
	}

	// public List<OMElement> getMajorObjects(String type) {
	// List<OMElement> objs = new ArrayList<OMElement>();
	//
	// if (wrapper != null) {
	// for (Iterator it=wrapper.getChildElements(); it.hasNext(); ) {
	// OMElement obj = (OMElement) it.next();
	// if (type == null || type.equals(obj.getLocalName()))
	// objs.add(obj);
	// }
	// }
	// return objs;
	// }

	/**
	 * This will return duplicates if they exist.
	 * 
	 * @param type
	 * @return
	 */
	public List<String> getMajorObjectIds(String type) {
		List<String> objs = new ArrayList<String>();

		if (wrapper != null) {
			for (Iterator it = wrapper.getChildElements(); it.hasNext();) {
				OMElement obj = (OMElement) it.next();
				if (type == null || type.equals(obj.getLocalName()))
					objs.add(getId(obj));
			}
		}
		return objs;
	}

	public String getId(OMElement ele) {
		return ele.getAttributeValue(MetadataSupport.id_qname);
	}

	public String getSourceObject(OMElement assoc) {
		return assoc.getAttributeValue(MetadataSupport.source_object_qname);
	}

	public String getTargetObject(OMElement assoc) {
		return assoc.getAttributeValue(MetadataSupport.target_object_qname);
	}

	public String getAssocType(OMElement assoc) {
		return assoc.getAttributeValue(MetadataSupport.association_type_qname);
	}

	public String getSimpleAssocType(OMElement assoc) throws MetadataException {
		String type = getAssocType(assoc);
		if (type == null)
			throw new MetadataException(
					"Invalid Association object: no associationType attribute: "
							+ assoc.toString());
		String[] parts = type.split(":");
		if (parts.length < 1)
			return type;
		return parts[parts.length - 1];
	}

	public String getAssocSource(OMElement assoc) {
		return assoc.getAttributeValue(MetadataSupport.source_object_qname);
	}

	public String getAssocTarget(OMElement assoc) {
		return assoc.getAttributeValue(MetadataSupport.target_object_qname);
	}

	public List<OMElement> getAllObjects() { // probably the same as Major
												// Objects
		return allObjects;
	}

	public List<String> getIdsForObjects(List<OMElement> objects) {
		List<String> ids = new ArrayList<String>();

		for (int i = 0; i < objects.size(); i++) {
			OMElement object = (OMElement) objects.get(i);
			String id = object.getAttributeValue(MetadataSupport.id_qname);
			ids.add(id);
		}

		return ids;
	}

	public String type(String id) throws MetadataException {
		OMElement ele = this.getObjectById(id);
		if (ele == null)
			return null;
		return ele.getLocalName();
	}

	public boolean contains(String id) {
		return getIdsForObjects(getAllObjects()).contains(id);
	}

	void addIds(List<String> ids, OMElement ele) {
		if (!ele.getLocalName().equals("ObjectRef")) {
			String id = ele.getAttributeValue(MetadataSupport.id_qname);
			if (id != null && !ids.contains(id))
				ids.add(id);
		}
		for (Iterator it = ele.getChildElements(); it.hasNext();) {
			OMElement ele2 = (OMElement) it.next();
			addIds(ids, ele2);
		}
	}

	public List<String> getAllDefinedIds() {
		List<String> ids = new ArrayList<String>();
		List<OMElement> objects = getAllObjects();

		for (int i = 0; i < objects.size(); i++) {
			OMElement object = (OMElement) objects.get(i);
			addIds(ids, object);
		}
		return ids;
	}

	public List<OMElement> getRegistryPackages() {
		return registryPackages;
	}

	public List<OMElement> getSubmissionSets() {
		return submissionSets;
	}

	public List<OMElement> getExtrinsicObjects() {
		return extrinsicObjects;
	}

	public List<OMElement> getObjectRefs() {
		return objectRefs;
	}

	public void rmObjectRefs() {
		for (OMElement or : objectRefs) {
			if (or.getParent() != null)
				or.detach();
		}
	}

	public void rmObject(OMElement ele) {
		if (ele.getParent() != null)
			ele.detach();
		List<List<OMElement>> containers = this.get_metadata_containers();
		for (List<OMElement> container : containers)
			container.remove(ele);
	}

	public OMElement getExtrinsicObject(int i) {
		return (OMElement) getExtrinsicObjects().get(i);
	}

	public List<String> getExtrinsicObjectIds() {
		List<String> ids = new ArrayList<String>();

		for (Iterator<OMElement> it = getExtrinsicObjects().iterator(); it
				.hasNext();) {
			OMElement ele = it.next();
			ids.add(ele.getAttributeValue(MetadataSupport.id_qname));
		}

		return ids;
	}

	public List<String> getObjectRefIds() {
		List<String> ids = new ArrayList<String>();

		for (Iterator<OMElement> it = getObjectRefs().iterator(); it.hasNext();) {
			OMElement ele = it.next();
			ids.add(ele.getAttributeValue(MetadataSupport.id_qname));
		}

		return ids;
	}

	public HashMap<String, OMElement> getDocumentUidMap()
			throws MetadataException {
		HashMap<String, OMElement> map = new HashMap<String, OMElement>();

		for (Iterator<String> it = getExtrinsicObjectIds().iterator(); it
				.hasNext();) {
			String id = it.next();
			String uid = this.getUniqueIdValue(id);
			map.put(uid, this.getObjectById(id));
		}
		return map;
	}

	public String getMimeType(OMElement eo) {
		return eo.getAttributeValue(MetadataSupport.mime_type_qname);
	}

	public String getHome(OMElement ro) {
		return ro.getAttributeValue(MetadataSupport.home_qname);
	}

	public List<OMElement> getAssociations() {
		return associations;
	}

	public List<String> getAssociationIds() {
		// Associations without an id are not represented
		List<String> ids = new ArrayList<String>();
		for (OMElement ele : getAssociations()) {
			String id = ele.getAttributeValue(MetadataSupport.id_qname);
			if (id != null && !id.equals(""))
				ids.add(id);
		}
		return ids;
	}

	public void addSlot(OMElement ele, String slot_name, String slot_value) {
		OMElement slot = this.om_factory().createOMElement("Slot", null);
		slot.addAttribute("name", slot_name, null);
		OMElement value_list = this.om_factory().createOMElement("ValueList",
				null);
		slot.addChild(value_list);
		OMElement value = this.om_factory().createOMElement("Value", null);
		value_list.addChild(value);
		value.setText(slot_value);
		ele.addChild(slot);
		mustDup = true;
	}

	public OMElement addSlot(OMElement ele, String slot_name) {
		OMElement slot = this.om_factory().createOMElement("Slot", null);
		slot.addAttribute("name", slot_name, null);
		OMElement value_list = this.om_factory().createOMElement("ValueList",
				null);
		slot.addChild(value_list);
		ele.addChild(slot);
		mustDup = true;
		return slot;
	}

	public OMElement addSlotValue(OMElement slot, String value) {
		OMElement value_list = MetadataSupport.firstChildWithLocalName(slot,
				"ValueList");

		OMElement valueEle = this.om_factory().createOMElement("Value", null);
		valueEle.setText(value);

		value_list.addChild(valueEle);
		mustDup = true;
		return slot;
	}

	public String id(OMElement ele) {
		return ele.getAttributeValue(MetadataSupport.id_qname);
	}

	public void setSlot(OMElement ele, String slot_name, String slot_value)
			throws MetadataException {
		OMElement slot = getSlot(id(ele), slot_name);
		if (slot == null)
			addSlot(ele, slot_name, slot_value);
		else {
			OMElement value_list = MetadataSupport.firstChildWithLocalName(
					slot, "ValueList");
			if (value_list == null)
				throw new MetadataException(
						"Slot without ValueList - slot name is " + slot_name
								+ " of object " + id(ele));
			for (Iterator it = value_list.getChildElements(); it.hasNext();) {
				OMElement v = (OMElement) it.next();
				v.detach();
			}
			OMElement value = MetadataSupport.om_factory.createOMElement(
					"Value", current_namespace());
			value_list.addChild(value);
			value.addChild(MetadataSupport.om_factory.createOMText(slot_value));
		}
	}

	public void reParse() throws MetadataException, MetadataValidationException {
		re_init();
		re_index();
		runParser();
	}

	public void reParse(boolean rm_dups) throws MetadataException,
			MetadataValidationException {
		re_init();
		re_index();
		parse(rm_dups);
	}

	void re_init() {
		registryPackages = null;
		objectsReferenced = null;
		objectsToDeprecate = null;
		wrappers = null;
		init();
	}

	void init() {
		if (registryPackages == null) {
			associations = new ArrayList<OMElement>();
			extrinsicObjects = new ArrayList<OMElement>();
			registryPackages = new ArrayList<OMElement>();
			submissionSets = new ArrayList<OMElement>();
			objectRefs = new ArrayList<OMElement>();
			folders = new ArrayList<OMElement>();
			submissionSet = null;
			classifications = new ArrayList<OMElement>();
			allObjects = new ArrayList<OMElement>();
			classificationsOfId = new HashMap<String, List<String>>();
			// objects_to_deprecate = new ArrayList();
			// objects_referenced = new ArrayList();
			parsed = false;
		}
	}

	// referencedObjects are:
	// RegistryObjects referenced by Associations (sourceObject or targetObject)
	// That are not contained in the package that was parsed to create this
	// instance of Metadata
	public List<String> getIdsOfReferencedObjects()
			throws MetadataValidationException, MetadataException {

		if (objectsReferenced == null) {
			objectsReferenced = new ArrayList<String>();
			objectsToDeprecate = new ArrayList<String>();

			for (Iterator<OMElement> it = associations.iterator(); it.hasNext();) {
				OMElement association = it.next();

				String association_type = stripNamespace(association
						.getAttributeValue(MetadataSupport.association_type_qname));
				String target_object = association
						.getAttributeValue(MetadataSupport.target_object_qname);
				String source_object = association
						.getAttributeValue(MetadataSupport.source_object_qname);

				// this validation does not belong here
				// new Metadata Rule
				if (association_type == null)
					throw new MetadataValidationException(
							"Association has no associationType attribute");
				if (source_object == null)
					throw new MetadataValidationException(association_type
							+ " Association has no sourceObject attribute");
				if (target_object == null)
					throw new MetadataValidationException(association_type
							+ " Association has no targetObject attribute");

				// the isUuid() predicate does not belong
				// new Metadata Rule
				if (isUuid(source_object) && !containsObject(source_object))
					objectsReferenced.add(source_object);

				if (isUuid(target_object) && !containsObject(target_object))
					objectsReferenced.add(target_object);

				// this validation does not belong here!
				// new Metadata Rule
				if (association_type.equals("RPLC")
						|| association_type.equals("XFRM_RPLC")) {
					if (!target_object.startsWith("urn:uuid:"))
						throw new MetadataValidationException(
								"RPLC association has targetObject attribute which is not a UUID: "
										+ target_object);
					objectsToDeprecate.add(target_object);
				}
			}
		}
		return objectsReferenced;
	}

	/**
	 * Contains LeafClass of object?
	 * 
	 * @param id
	 * @return
	 * @throws MetadataException
	 */
	public boolean containsObject(String id) throws MetadataException {
		OMElement ele = id_index().getObjectById(id);
		if (ele == null)
			return false;
		if (ele.getLocalName().equals("ObjectRef"))
			return false;
		return true;
	}

	public List<String> idsForObjects(List<OMElement> objects) {
		List<String> ids = new ArrayList<String>();
		for (OMElement ele : objects)
			ids.add(ele.getAttributeValue(MetadataSupport.id_qname));
		return ids;
	}

	public boolean isUuid(String id) {
		return id.startsWith("urn:uuid:");
	}

	public List<String> getReferencedObjectsThatMustHaveSamePatientId()
			throws MetadataValidationException, MetadataException {
		List<String> objects = new ArrayList<String>();

		for (Iterator<OMElement> it = associations.iterator(); it.hasNext();) {
			OMElement association = it.next();

			String association_type = stripNamespace(association
					.getAttributeValue(MetadataSupport.association_type_qname));
			String target_object = association
					.getAttributeValue(MetadataSupport.target_object_qname);
			String source_object = association
					.getAttributeValue(MetadataSupport.source_object_qname);

			if (association_type == null)
				throw new MetadataValidationException(
						"Association has no associationType attribute");
			if (source_object == null)
				throw new MetadataValidationException(association_type
						+ " Association has no sourceObject attribute");
			if (target_object == null)
				throw new MetadataValidationException(association_type
						+ " Association has no targetObject attribute");

			if (assoc_type(association_type).equals("HasMember")
					&& "Reference".equals(getSlotValue(association,
							"SubmissionSetStatus", 0))) {
				continue;
			}

			if (source_object.startsWith("urn:uuid:")
					&& id_index().getObjectById(source_object) == null) {
				objects.add(source_object);
			}

			if (target_object.startsWith("urn:uuid:")) {
				OMElement o = id_index().getObjectById(target_object);
				if (o == null)
					objects.add(target_object);
				else if (o.getLocalName().equals("ObjectRef")) {
					objects.add(target_object);
				}
			}

		}
		return objects;
	}

	String assoc_type(String type) {
		if (isVersion2())
			return type;
		return "urn:oasis:names:tc:ebxml-regrep:AssociationType:" + type;
	}

	public boolean isReferencedObject(String id)
			throws MetadataValidationException, MetadataException {
		if (!id.startsWith("urn:uuid:"))
			return false;
		return getIdsOfReferencedObjects().contains(id);
	}

	public List<String> getObjectIdsToDeprecate()
			throws MetadataValidationException, MetadataException {
		getIdsOfReferencedObjects();
		return objectsToDeprecate;
	}

	void add_to_classifications_of_id(List<OMElement> classifications) {
		for (OMElement classification : classifications)
			add_to_classifications_of_id(classification);
	}

	void add_to_classifications_of_id(OMElement classification) {
		String classifiedObject = classification
				.getAttributeValue(MetadataSupport.classified_object_qname);
		String classificationScheme = classification
				.getAttributeValue(MetadataSupport.classificationscheme_qname);
		List<String> old = classificationsOfId.get(classifiedObject);
		if (old == null) {
			old = new ArrayList<String>();
			classificationsOfId.put(classifiedObject, old);
		}
		old.add(classificationScheme);
	}

	public boolean isClassifiedBy(String id, String classification_scheme) {
		List<String> al = classificationsOfId.get(id);
		if (al == null)
			return false;
		return al.contains(classification_scheme);
	}

	public OMElement add_association(OMElement a) {
		associations.add(a);
		allObjects.add(a);
		return a;
	}

	public OMElement mkAssociation(String type, String sourceUuid,
			String targetUuid) {
		OMElement assoc = MetadataSupport.om_factory.createOMElement(
				"Association", MetadataSupport.ebRIMns2);
		assoc.addAttribute(MetadataSupport.om_factory.createOMAttribute(
				"associationType", null, type));
		assoc.addAttribute(MetadataSupport.om_factory.createOMAttribute(
				"sourceObject", null, sourceUuid));
		assoc.addAttribute(MetadataSupport.om_factory.createOMAttribute(
				"targetObject", null, targetUuid));
		assoc.addAttribute(MetadataSupport.om_factory.createOMAttribute("id",
				null, allocate_id()));
		return assoc;
	}

	public boolean listContains(List<String> list, String value) {
		for (Iterator<String> it = list.iterator(); it.hasNext();) {
			String val = it.next();
			if (value.equals(val))
				return true;
		}
		return false;
	}

	public void rmFromList(List<String> list, String value) {
		String found = null;
		for (Iterator<String> it = list.iterator(); it.hasNext();) {
			String val = it.next();
			if (value.equals(val)) {
				found = val;
				break;
			}
		}
		if (found != null)
			list.remove(found);
	}

	public List<String> getAssocReferences() {
		List<String> ids = new ArrayList<String>();

		for (OMElement assoc : associations) {
			String obj1_id = assoc
					.getAttributeValue(MetadataSupport.source_object_qname);
			String obj2_id = assoc
					.getAttributeValue(MetadataSupport.target_object_qname);
			if (!listContains(ids, obj1_id))
				ids.add(obj1_id);
			if (!listContains(ids, obj2_id))
				ids.add(obj2_id);
		}

		return ids;
	}

	public OMElement addInternalClassification(OMElement classifiedObject,
			String classificationNode) {
		OMElement classification = MetadataSupport.om_factory.createOMElement(
				"Classification", null);
		classification.addAttribute("classificationNode", classificationNode,
				null);
		this.add_classification(classifiedObject, classification);
		return classification;
	}

	void parse(boolean discard_duplicates) throws MetadataException,
			MetadataValidationException {
		init();
		parsed = true;

		detect_metadata_version();

		if (wrapper == null)
			return;

		for (Iterator<OMElement> it = wrapper.getChildElements(); it.hasNext();) {
			OMElement obj = it.next();
			String type = obj.getLocalName();
			OMAttribute id_att = obj.getAttribute(MetadataSupport.id_qname);

			// obj has no id attribute - assign it one
			if (id_att == null) {
				String id = allocate_id();
				id_att = obj.addAttribute("id", id, null);
			} else {
				String id = id_att.getAttributeValue();
				if (id == null || id.equals(""))
					id_att.setAttributeValue(allocate_id());
			}

			if (!discard_duplicates || !getIds(allObjects).contains(id(obj)))
				allObjects.add(obj);

			add_to_classifications_of_id(findClassifications(obj));

			if (type.equals("RegistryPackage")) {
				if (has_external_identifier(obj,
						"urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8")) {
					if (!discard_duplicates
							|| !getIds(submissionSets).contains(id(obj)))
						submissionSets.add(obj);

					if (submissionSet != null
							&& this.interpretAsSubmission == true) {
						throw new MetadataException(
								"Metadata: Submission has multiple SubmissionSets");
					}
					submissionSet = obj;

				} else if (has_external_identifier(obj,
						"urn:uuid:75df8f67-9973-4fbe-a900-df66cefecc5a")) {
					if (!discard_duplicates
							|| !getIds(folders).contains(id(obj)))
						folders.add(obj);
				}
				if (!discard_duplicates
						|| !getIds(registryPackages).contains(id(obj)))
					registryPackages.add(obj);

			} else if (type.equals("ExtrinsicObject")) {
				if (!discard_duplicates
						|| !getIds(extrinsicObjects).contains(id(obj)))
					extrinsicObjects.add(obj);

			} else if (type.equals("ObjectRef")) {
				// if (!discard_duplicates ||
				// !getIds(objectRefs).contains(id(obj))) {
				// System.out.println("adding");
				// objectRefs.add(obj);
				// }
				if (!discard_duplicates
						|| !contains(getIds(objectRefs), id(obj))) {
					objectRefs.add(obj);
				}

			} else if (type.equals("Classification")) {
				if (!discard_duplicates
						|| !getIds(classifications).contains(id(obj)))
					classifications.add(obj);
				add_to_classifications_of_id(obj);

			} else if (type.equals("Association")) {
				if (!discard_duplicates
						|| !getIds(associations).contains(id(obj)))
					associations.add(obj);
			} else {
				throw new MetadataException(
						"Metadata: parse(): did not expect a " + type
								+ " object at the top level");
			}

			for (Iterator it1 = obj.getChildElements(); it1.hasNext();) {
				OMElement obj_i = (OMElement) it1.next();
				String type_i = obj_i.getLocalName();
				if (type_i.equals("Classification")) {
					if (!discard_duplicates
							|| !getIds(classifications).contains(id(obj_i)))
						classifications.add(obj_i);
				}
			}
		}
		if (interpretAsSubmission && submissionSet == null)
			throw new NoSubmissionSetException(
					"Metadata: No Submission Set found");

	}

	boolean contains(List<String> list, String string) {
		for (String l : list) {
			if (l.equals(string))
				return true;
		}
		return false;
	}

	private void detect_metadata_version() throws MetadataException {
		if (wrapper == null) {
			version2 = true;
		} else {
			OMNamespace namespace = wrapper.getNamespace();
			String namespace_uri = (namespace != null) ? namespace
					.getNamespaceURI() : "";

			detect_metadata_version(namespace_uri);
		}
	}

	private void detect_metadata_version(String namespace_uri)
			throws MetadataException {
		// if this class later accepts v3 metadata as well we may have to worry
		// about intermixing v2 and v3
		if (namespace_uri.equals("urn:oasis:names:tc:ebxml-regrep:rim:xsd:2.1"))
			version2 = true;
		else if (namespace_uri
				.equals("urn:oasis:names:tc:ebxml-regrep:query:xsd:2.1"))
			version2 = true;
		else if (namespace_uri
				.equals("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0"))
			version2 = false;
		else if (namespace_uri
				.equals("urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0"))
			version2 = false;
		else
			version2 = true;
		// throw new
		// MetadataException("Metadata.parse(): Cannot identify version of metadata from namespace "
		// + namespace_uri);
	}

	public String getNameValue(OMElement ele) {
		OMElement name_ele = MetadataSupport.firstChildWithLocalName(ele,
				"Name");
		if (name_ele == null)
			return null;
		OMElement loc_st = MetadataSupport.firstChildWithLocalName(name_ele,
				"LocalizedString");
		if (loc_st == null)
			return null;
		return loc_st.getAttributeValue(MetadataSupport.value_qname);
	}

	public String getDescriptionValue(OMElement ele) {
		OMElement name_ele = MetadataSupport.firstChildWithLocalName(ele,
				"Description");
		if (name_ele == null)
			return null;
		OMElement loc_st = MetadataSupport.firstChildWithLocalName(name_ele,
				"LocalizedString");
		if (loc_st == null)
			return null;
		return loc_st.getAttributeValue(MetadataSupport.value_qname);
	}

	void add_classification(OMElement ele, OMElement classification) {
		ele.addChild(classification);
		mustDup = true;
	}

	String om_get_id(OMElement ele) {
		return ele.getAttributeValue(MetadataSupport.id_qname);
	}

	public OMElement getSubmissionSet() {
		return submissionSet;
	}

	public String getSubmissionSetId() {
		OMElement ss = getSubmissionSet();
		if (ss == null)
			return "";
		return ss.getAttributeValue(MetadataSupport.id_qname);
	}

	public List<OMElement> getAssociationsInclusive(List<String> ids) {
		List<OMElement> assocs = new ArrayList<OMElement>();

		for (OMElement a : this.getAssociations()) {
			if (ids.contains(getAssocSource(a))
					&& ids.contains(getAssocTarget(a)))
				assocs.add(a);
		}

		return assocs;
	}

	public List<String> getSubmissionSetIds() {
		List ids = new ArrayList();
		List sss = getSubmissionSets();
		for (int i = 0; i < sss.size(); i++) {
			OMElement ss = (OMElement) sss.get(i);
			String f_id = ss.getAttributeValue(MetadataSupport.id_qname);
			ids.add(f_id);
		}
		return ids;
	}

	public boolean isSubmissionSet(String id) {
		return getSubmissionSetIds().contains(id);
	}

	public boolean isFolder(String id) {
		return getFolderIds().contains(id);
	}

	public boolean isDocument(String id) {
		return this.getExtrinsicObjectIds().contains(id);
	}

	public List<OMElement> getFolders() {
		return folders;
	}

	public List<String> getIds(List<OMElement> parts) {
		List<String> ids = new ArrayList<String>();
		for (int i = 0; i < parts.size(); i++) {
			OMElement part = (OMElement) parts.get(i);
			String f_id = part.getAttributeValue(MetadataSupport.id_qname);
			ids.add(f_id);
		}
		return ids;
	}

	public List<String> getFolderIds() {
		List ids = new ArrayList();
		List folders = getFolders();
		for (int i = 0; i < folders.size(); i++) {
			OMElement folder = (OMElement) folders.get(i);
			String f_id = folder.getAttributeValue(MetadataSupport.id_qname);
			ids.add(f_id);
		}
		return ids;
	}

	public OMElement getFolder(int i) {
		return (OMElement) getFolders().get(i);
	}

	public List getRegistryPackageIds() {
		List ids = new ArrayList();
		List rps = this.getRegistryPackages();
		for (int i = 0; i < rps.size(); i++) {
			OMElement rp = (OMElement) rps.get(i);
			String f_id = rp.getAttributeValue(MetadataSupport.id_qname);
			ids.add(f_id);
		}
		return ids;
	}

	public String getIdentifyingString(OMElement ele) {
		StringBuffer b = new StringBuffer();
		b.append(ele.getLocalName());

		OMElement name_ele = MetadataSupport.firstChildWithLocalName(ele,
				"Name");
		if (name_ele != null) {
			OMElement loc = MetadataSupport.firstChildWithLocalName(name_ele,
					"LocalizedString");
			if (loc != null) {
				String name = loc.getAttributeValue(new QName("value"));
				b.append(" Name=\"" + name + "\"");
			}
		}

		b.append(" id=\"" + ele.getAttributeValue(MetadataSupport.id_qname)
				+ "\"");

		return "<" + b.toString() + ">";
	}

	public List getObjectNames(List objects) {
		List names = new ArrayList();
		for (int i = 0; i < objects.size(); i++) {
			OMElement obj = (OMElement) objects.get(i);
			names.add(obj.getLocalName());
		}
		return names;
	}

	OMElement find_metadata_wrapper() throws MetadataException {
		if (metadata == null || metadata.getLocalName() == null)
			throw new NoMetadataException(
					"find_metadata_wrapper: Cannot find a wrapper element, top element is NULL"
							+ ". A wrapper is one of the XML elements that holds metadata (ExtrinsicObject, RegistryPackage, Association etc.)");

		if (metadata.getLocalName().equals("TestResults")) {
			OMElement test_step = MetadataSupport.firstChildWithLocalName(
					metadata, "TestStep");
			if (test_step != null) {
				OMElement sqt = MetadataSupport.firstChildWithLocalName(
						test_step, "StoredQueryTransaction");
				if (sqt != null) {
					OMElement result = MetadataSupport.firstChildWithLocalName(
							sqt, "Result");
					if (result != null) {
						OMElement ahqr = MetadataSupport
								.firstChildWithLocalName(result,
										"AdhocQueryResponse");
						if (ahqr != null) {
							OMElement rol = MetadataSupport
									.firstChildWithLocalName(ahqr,
											"RegistryObjectList");
							if (rol != null) {
								return rol;
							}
						}
					}
				}
			}
		}

		if (metadata.getLocalName().equals("LeafRegistryObjectList"))
			return metadata;

		if (metadata.getLocalName().equals(
				"ProvideAndRegisterDocumentSetRequest")) {
			OMElement sor = MetadataSupport.firstChildWithLocalName(metadata,
					"SubmitObjectsRequest");
			if (sor != null) {
				return MetadataSupport.firstChildWithLocalName(sor,
						"RegistryObjectList");
			}
		}

		for (Iterator it = metadata.getChildElements(); it.hasNext();) {
			OMElement child = (OMElement) it.next();
			if (child.getLocalName().equals("RegistryObjectList"))
				return child;
			if (child.getLocalName().equals("AdhocQueryResponse")) {
				OMElement achild = MetadataSupport.firstChildWithLocalName(
						child, "SQLQueryResult");
				if (achild != null)
					return achild;
			}
			if (child.getLocalName().equals("LeafRegistryObjectList"))
				return child;
		}
		OMElement ele2 = find_metadata_wrapper2(metadata);
		if (ele2 != null)
			return ele2;

		String metadataString = metadata.toString();
		throw new NoMetadataException(
				"find_metadata_wrapper: Cannot find a wrapper element, top element is "
						+ metadata.getLocalName()
						+ ". A wrapper is one of the XML elements that holds metadata (ExtrinsicObject, RegistryPackage, Association etc.)\n"
						+ "input starts with:\n"
						+ metadataString.substring(0, min(400, metadataString
								.length() - 1)));
	}

	OMElement find_metadata_wrapper2(OMElement ele) throws MetadataException {
		for (Iterator<OMElement> it = ele.getChildElements(); it.hasNext();) {
			OMElement e = it.next();
			String name = e.getLocalName();
			if (name == null)
				continue;
			if (name.equals("ObjectRef") || name.equals("ExtrinsicObject")
					|| name.equals("RegistryPackage")
					|| name.equals("Association")
					|| name.equals("Classification"))
				return ele;
			OMElement e2 = find_metadata_wrapper2(e);
			if (e2 != null)
				return e2;
		}
		return null;
	}

	public OMElement getWrapper() {
		return wrapper;
	}

	public OMElement findSlot(OMElement registry_object, String slot_name) {
		for (Iterator it = registry_object.getChildElements(); it.hasNext();) {
			OMElement s = (OMElement) it.next();
			if (!s.getLocalName().equals("Slot"))
				continue;
			String val = s.getAttributeValue(MetadataSupport.slot_name_qname);
			if (val != null && val.equals(slot_name))
				return s;
		}
		return null;
	}

	public List findClassifications(OMElement registry_object,
			String classificationScheme) {
		List cl = new ArrayList();

		for (Iterator it = registry_object.getChildElements(); it.hasNext();) {
			OMElement s = (OMElement) it.next();
			if (!s.getLocalName().equals("Classification"))
				continue;
			String val = s
					.getAttributeValue(MetadataSupport.classificationscheme_qname);
			if (val != null && val.equals(classificationScheme))
				cl.add(s);
		}

		return cl;
	}

	public List findClassifications(OMElement registry_object) {
		List cl = new ArrayList();

		for (Iterator it = registry_object.getChildElements(); it.hasNext();) {
			OMElement s = (OMElement) it.next();
			if (!s.getLocalName().equals("Classification"))
				continue;
			cl.add(s);
		}

		return cl;
	}

	public List findChildElements(OMElement registry_object, String element_name) {
		List al = new ArrayList();
		for (Iterator it = registry_object.getChildElements(); it.hasNext();) {
			OMElement s = (OMElement) it.next();
			if (s.getLocalName().equals(element_name))
				al.add(s);
		}
		return al;
	}

	public List<String> getObjectIds(List<OMElement> registry_objects) {
		List<String> ids = new ArrayList<String>();

		for (int i = 0; i < registry_objects.size(); i++) {
			OMElement ele = registry_objects.get(i);
			String id = ele.getAttributeValue(MetadataSupport.id_qname);
			ids.add(id);
		}

		return ids;
	}

	public List<OMElement> getObjectRefs(List<OMElement> registry_objects,
			boolean version2) {
		List<OMElement> ors = new ArrayList<OMElement>();

		for (int i = 0; i < registry_objects.size(); i++) {
			OMElement ele = registry_objects.get(i);
			String id = ele.getAttributeValue(MetadataSupport.id_qname);
			String home = ele.getAttributeValue(MetadataSupport.home_qname);
			OMElement or = MetadataSupport.om_factory.createOMElement(
					"ObjectRef", (version2) ? MetadataSupport.ebRIMns2
							: MetadataSupport.ebRIMns3);
			or.addAttribute("id", id, null);
			if (home != null)
				or.addAttribute("home", home, null);
			ors.add(or);
		}

		return ors;
	}

	public List<OMElement> getClassifications() {
		return classifications;
	}

	/*
	 * by ID
	 */

	IdIndex id_index() throws MetadataException {
		if (idIndex == null) {
			idIndex = new IdIndex(this);
		}
		return idIndex;
	}

	IdIndex id_index(LogMessage log_message) throws MetadataException {
		if (idIndex == null) {
			idIndex = new IdIndex();
			idIndex.set_log_message(log_message);
			idIndex.setMetadata(this);
		}
		return idIndex;
	}

	public String getNameValue(String object_id) throws MetadataException {
		return id_index().getNameValue(object_id);
	}

	public String getDescriptionValue(String object_id)
			throws MetadataException {
		return id_index().getDescriptionValue(object_id);
	}

	public List<OMElement> getSlots(String object_id) throws MetadataException {
		return id_index().getSlots(object_id);
	}

	public OMElement getSlot(String object_id, String name)
			throws MetadataException {
		return id_index().getSlot(object_id, name);
	}
	
	public String getSlotName(OMElement slot) {
		return slot.getAttributeValue(MetadataSupport.name_qname);
	}

	public void rmSlot(String object_id, String name) throws MetadataException {
		OMElement slot = getSlot(object_id, name);
		if (slot != null) {
			slot.detach();
			re_index();
		}
	}

	public String getMetadataDescription() {
		StringBuffer buf = new StringBuffer();

		buf.append(this.getSubmissionSets().size() + " SubmissionSets\n");
		buf.append(this.getExtrinsicObjects().size() + " DocumentEntries\n");
		buf.append(this.getFolders().size() + " Folders\n");
		buf.append(this.getAssociations().size() + " Associations\n");
		buf.append(this.getObjectRefs().size() + " ObjectRefs\n");

		return buf.toString();
	}

	public boolean isRegistryPackageClassificationBroken(String id)
			throws MetadataException {
		if (!(isSubmissionSet(id) || isFolder(id)))
			return false; // not a registrypackage

		List<OMElement> classifications = getClassifications(id);
		for (OMElement classification : classifications) {
			if (classification
					.getAttribute(MetadataSupport.classificationscheme_qname) != null)
				continue;

			if (classification
					.getAttribute(MetadataSupport.classificationnode_qname) == null) {
				return true;
			}
		}
		return false;
	}

	public boolean isRegistryPackageClassificationBroken()
			throws MetadataException {
		List<String> rpIds = getRegistryPackageIds();
		for (int i = 0; i < rpIds.size(); i++) {
			String id = rpIds.get(i);
			if (isRegistryPackageClassificationBroken(id))
				return true;
		}
		return false;
	}

	// this no longer uses getRegistryPackageIds() since it would not allow
	// fixing all copies of a RegistryPackage in the case where multiple copies
	// are returned
	public void fixClassifications() throws MetadataException {
		List<OMElement> rps = getRegistryPackages();
		for (OMElement rp : rps) {
			String id = getId(rp);
			List<OMElement> classifications = getClassifications(rp);
			for (OMElement classification : classifications) {
				if (classification
						.getAttribute(MetadataSupport.classificationscheme_qname) != null)
					continue;

				// not a code - must be classification of RP as SS or Fol, make
				// sure
				// classificationNode is present

				// first ebxmlrr bug to fix - classificationNode attribute is
				// dropped - add it back in
				if (classification
						.getAttribute(MetadataSupport.classificationnode_qname) == null) {
					// add classification, first figure out if this is SS or Fol
					if (isSubmissionSet(id))
						classification
								.addAttribute(
										"classificationNode",
										MetadataSupport.XDSSubmissionSet_classification_uuid,
										null);
					else
						classification.addAttribute("classificationNode",
								MetadataSupport.XDSFolder_classification_uuid,
								null);
				}
			}

			// now handle case where internal classification on RP is missing
			// entirely
			OMElement typeClassification = null;

			for (OMElement classification : classifications) {
				if (classification
						.getAttribute(MetadataSupport.classificationscheme_qname) != null)
					continue;

				typeClassification = classification;

			}

			if (typeClassification == null) {
				// need to add it in
				String classificationNode = (isSubmissionSet(id)) ? MetadataSupport.XDSSubmissionSet_classification_uuid
						: MetadataSupport.XDSFolder_classification_uuid;
				OMElement classifiedObject = this.getObjectById(id);
				this.addInternalClassification(classifiedObject,
						classificationNode);
			}

		}

	}

	public List<OMElement> getClassifications(String object_id)
			throws MetadataException {
		return id_index().getClassifications(object_id);
	}

	public List<OMElement> getClassifications(OMElement object)
			throws MetadataException {
		return id_index().getClassifications(this.getId(object));
	}

	public List<OMElement> getClassifications(OMElement object,
			String classification_scheme) throws MetadataException {
		return getClassifications(this.getId(object), classification_scheme);
	}

	public String getClassificationValue(OMElement classification) {
		return classification
				.getAttributeValue(MetadataSupport.noderepresentation_qname);
	}

	public String getClassificationScheme(OMElement classification) {
		return getSlotValue(classification, "codingScheme", 0);
	}

	public List<String> getClassificationsValues(OMElement object,
			String classification_scheme) throws MetadataException {
		List<OMElement> classes = getClassifications(object,
				classification_scheme);
		List<String> values = new ArrayList<String>();
		for (OMElement e : classes) {
			values
					.add(e
							.getAttributeValue(MetadataSupport.noderepresentation_qname));
		}
		return values;
	}

	public List<String> getClassificationsCodes(OMElement object,
			String classification_scheme) throws MetadataException {
		List<OMElement> classes = getClassifications(object,
				classification_scheme);
		List<String> values = new ArrayList<String>();
		for (OMElement e : classes) {
			String code_value = e
					.getAttributeValue(MetadataSupport.noderepresentation_qname);
			String code_scheme = getSlotValue(e, "codingScheme", 0);
			values.add(code_value + "^^" + code_scheme);
		}
		return values;
	}

	public List<String> getClassificationsCodesWithDisplayName(OMElement object,
			String classification_scheme) throws MetadataException {
		List<OMElement> classes = getClassifications(object,
				classification_scheme);
		List<String> values = new ArrayList<String>();
		for (OMElement e : classes) {
			String code_value = e
					.getAttributeValue(MetadataSupport.noderepresentation_qname);
			String code_scheme = getSlotValue(e, "codingScheme", 0);
			String displayName = this.getNameValue(e);
			values.add(code_value + "^" + displayName + "^" + code_scheme);
		}
		return values;
	}

	/**
	 * 
	 * @param object
	 *            - ExtrinsicObject or RegistryPackage
	 * @param classification_schemes
	 *            - list of UUIDs representing the classification schemes of
	 *            interest
	 * @return map from classification scheme UUID => code values where a code
	 *         value is code^^code_set.
	 * @throws MetadataException
	 */
	public Map<String, List<String>> getCodes(OMElement object,
			List<String> classification_schemes) throws MetadataException {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (String class_scheme : classification_schemes) {
			List<String> codes = getClassificationsCodes(object, class_scheme);
			map.put(class_scheme, codes);
		}
		return map;
	}

	public Map<String, List<String>> getCodesWithDisplayName(OMElement object,
			List<String> classification_schemes) throws MetadataException {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (String class_scheme : classification_schemes) {
			List<String> codes = getClassificationsCodesWithDisplayName(object, class_scheme);
			map.put(class_scheme, codes);
		}
		return map;
	}

	public List<String> getClassificationsValues(String id,
			String classification_scheme) throws MetadataException {
		return getClassificationsValues(this.getObjectById(id),
				classification_scheme);
	}

	public List<OMElement> getClassifications(String id,
			String classification_scheme) throws MetadataException {
		List<OMElement> cls = getClassifications(id);
		List<OMElement> cls_2 = new ArrayList<OMElement>();
		for (OMElement cl : cls) {
			String cl_scheme = cl
					.getAttributeValue(MetadataSupport.classificationscheme_qname);
			if (cl_scheme != null && cl_scheme.equals(classification_scheme))
				cls_2.add(cl);
		}
		return cls_2;
	}

	public List getExternalIdentifiers(String object_id)
			throws MetadataException {
		return id_index().getExternalIdentifiers(object_id);
	}

	public String getExternalIdentifierValue(String object_id,
			String identifier_scheme) throws MetadataException {
		return id_index().getExternalIdentifierValue(object_id,
				identifier_scheme);
	}
	
	public String getUniqueIdValue(OMElement ele) throws MetadataException {
		return getUniqueIdValue(getId(ele));
	}

	public String getUniqueIdValue(String id) throws MetadataException {
		String uid;
		uid = id_index().getExternalIdentifierValue(id,
				MetadataSupport.XDSDocumentEntry_uniqueid_uuid);
		if (uid != null && !uid.equals(""))
			return uid;
		uid = id_index().getExternalIdentifierValue(id,
				MetadataSupport.XDSSubmissionSet_uniqueid_uuid);
		if (uid != null && !uid.equals(""))
			return uid;
		uid = id_index().getExternalIdentifierValue(id,
				MetadataSupport.XDSFolder_uniqueid_uuid);
		if (uid != null && !uid.equals(""))
			return uid;
		return null;
	}

	public OMAttribute getUniqueIdAttribute(String id) throws MetadataException {
		OMAttribute att;
		att = id_index().getExternalIdentifierAttribute(id,
				MetadataSupport.XDSDocumentEntry_uniqueid_uuid);
		if (att != null && !att.equals(""))
			return att;
		att = id_index().getExternalIdentifierAttribute(id,
				MetadataSupport.XDSSubmissionSet_uniqueid_uuid);
		if (att != null && !att.equals(""))
			return att;
		att = id_index().getExternalIdentifierAttribute(id,
				MetadataSupport.XDSFolder_uniqueid_uuid);
		if (att != null && !att.equals(""))
			return att;
		return null;
	}

	public List<String> getAllUids() throws MetadataException {
		List<String> all_ids = this.getAllDefinedIds();
		List<String> all_uids = new ArrayList<String>();
		for (String id : all_ids) {
			String uid = getUniqueIdValue(id);
			if (uid != null)
				all_uids.add(uid);
		}
		return all_uids;
	}

	void addToUidHashMap(HashMap<String, List<String>> map, String uid,
			String hash) {
		if (uid == null)
			return;
		List<String> hash_list = map.get(uid);
		if (hash_list == null) {
			hash_list = new ArrayList<String>();
			map.put(uid, hash_list);
		}
		hash_list.add(hash);
	}

	// get map of uid ==> ArrayList of hashes
	// for folder and ss, hash is null
	// Some docs may not have a hash either, depending on where this use used
	public HashMap<String, List<String>> getUidHashMap()
			throws MetadataException {
		HashMap<String, List<String>> hm = new HashMap<String, List<String>>();

		List<String> ids;

		ids = this.getExtrinsicObjectIds();
		for (String id : ids) {
			OMElement registry_object = this.getObjectById(id);
			String uid;

			List<OMElement> eis = this.getExternalIdentifiers(id);
			List<OMElement> eid_eles = this.getExternalIdentifiers(
					registry_object,
					MetadataSupport.XDSDocumentEntry_uniqueid_uuid);
			if (eid_eles.size() > 0)
				uid = eid_eles.get(0).getAttributeValue(
						MetadataSupport.value_qname);
			else
				throw new MetadataException("Metadata.getUidHashMap(): Doc "
						+ id + " has no uniqueId\nfound " + eis.size()
						+ " external identifiers");
			String hash = this.getSlotValue(id, "hash", 0);
			if (hash != null && hash.equals(""))
				hash = null;
			addToUidHashMap(hm, uid, hash);
		}

		ids = this.getSubmissionSetIds();
		for (String id : ids) {
			String uid;
			uid = id_index().getExternalIdentifierValue(id,
					MetadataSupport.XDSSubmissionSet_uniqueid_uuid);
			if (uid == null || uid.equals(""))
				throw new MetadataException("Metadata.getUidHashMap(): SS "
						+ id + " has no uniqueId");
			addToUidHashMap(hm, uid, null);
		}

		ids = this.getFolderIds();
		for (String id : ids) {
			String uid;
			uid = id_index().getExternalIdentifierValue(id,
					MetadataSupport.XDSFolder_uniqueid_uuid);
			if (uid == null || uid.equals(""))
				throw new MetadataException("Metadata.getUidHashMap(): Fol "
						+ id + " has no uniqueId");
			addToUidHashMap(hm, uid, null);
		}

		return hm;
	}

	public String getSubmissionSetUniqueId() throws MetadataException {
		return id_index().getSubmissionSetUniqueId();
	}

	public String getSubmissionSetPatientId() throws MetadataException {
		return id_index().getSubmissionSetPatientId();
	}

	public OMElement getObjectById(String id) throws MetadataException {
		return id_index().getObjectById(id);
	}

	public String getIdentifyingString(String id) throws MetadataException {
		return id_index().getIdentifyingString(id);
	}

	public String getObjectTypeById(String id) throws MetadataException {
		return id_index().getObjectTypeById(id);
	}

	public String getSubmissionSetSourceId(OMElement ss) throws MetadataException {
		List<OMElement> eids = getExternalIdentifiers(ss, MetadataSupport.XDSSubmissionSet_sourceid_uuid);
		OMElement eid = eids.get(0);
		return eid.getAttributeValue(MetadataSupport.value_qname);
	}

	/* return null if id not found, home or "" if id found */
	public String getHome(String id) throws MetadataException {
		OMElement ele = getObjectById(id);
		if (ele == null)
			return null;
		String home = getHome(ele);
		if (home == null)
			home = "";
		return home;
	}

	/*
	 * Dup
	 */

	public OMElement dup() throws XdsInternalException {
		// set metadata_dup to the new top element and wrapper_dup to the new
		// wrapper element
		dup_wrapper(); // ==> wrapper_dup, metadata_dup are set
		List contents = (version2) ? getV2() : getV3();
		for (int i = 0; i < contents.size(); i++) {
			OMElement clone = ((OMElement) contents.get(i));
			wrapperDup.addChild(clone);
		}
		return metadataDup;
	}

	void dup_wrapper() throws XdsInternalException {
		String wrapper_name = wrapper.getLocalName();
		wrapperDup = null;
		metadataDup = dup_wrapper1(metadata);
		if (wrapperDup == null)
			throw new XdsInternalException(
					"Metadata.dup_wrapper(): cannot find wrapper element "
							+ wrapper_name);
	}

	OMElement dup_wrapper1(OMElement e) {
		OMElement e_dup = om_factory().createOMElement(e.getLocalName(),
				e.getNamespace());
		for (Iterator it = e.getAllAttributes(); it.hasNext();) {
			OMAttribute a = (OMAttribute) it.next();
			String name = a.getLocalName();
			String value = a.getAttributeValue();
			e_dup.addAttribute(name, value, null);
		}
		if (e.getLocalName().equals(wrapper.getLocalName())) {
			wrapperDup = e_dup;
			return e_dup;
		}
		for (Iterator it = e.getChildElements(); it.hasNext();) {
			OMElement ele = (OMElement) it.next();
			OMElement ele_dup = dup_wrapper1(ele);
			e_dup.addChild(ele_dup);
		}
		return e_dup;
	}

	public List getOriginal() throws XdsInternalException {
		if (!mustDup)
			return allObjects;

		if (version2)
			return getV2();
		else
			return getV3();

	}

	// return List of OMElements
	// returns a copy of the original
	public List getV2() throws XdsInternalException {
		List al;
		IdParser ip = new IdParser(this);
		List undefinedIds = ip.getUndefinedIds();

		TranslateToV2 v = new TranslateToV2();
		al = v.translate(allObjects, mustDup);

		for (Iterator it = undefinedIds.iterator(); it.hasNext();) {
			String id = (String) it.next();
			if (!id.startsWith("urn:uuid:"))
				continue;
			OMElement or;
			al.add(or = (OMElement) MetadataSupport.om_factory.createOMElement(
					"ObjectRef", MetadataSupport.ebRIMns2));
			or.addAttribute("id", id, null);
		}

		return al;
	}

	// returns a copy of the original
	public List<OMElement> getV3() throws XdsInternalException {
		List al;
		if (version2) {
			TranslateToV3 v = new TranslateToV3();
			al = v.translate(allObjects, mustDup);
		} else {
			TranslateToV3 v = new TranslateToV3();
			al = v.translate(allObjects, mustDup);
		}
		return al;
	}

	// breadth first search for element with localname
	OMElement find_element(OMElement root, String localname) {
		if (root.getLocalName().equals(localname))
			return root;
		for (Iterator it = root.getChildElements(); it.hasNext();) {
			OMElement child = (OMElement) it.next();
			if (child.getLocalName().equals(localname))
				return child;
		}
		for (Iterator it = root.getChildElements(); it.hasNext();) {
			OMElement child = (OMElement) it.next();
			OMElement ele = find_element(child, localname);
			if (ele != null)
				return ele;
		}
		return null;
	}

	void fix_v2_ns_recursive(OMElement ele, OMNamespace ns) {
		ele.setNamespace(ns);
		for (Iterator it = ele.getAllAttributes(); it.hasNext();) {
			OMAttribute a = (OMAttribute) it.next();
			if (a.getLocalName().equals("lang"))
				a.setOMNamespace(MetadataSupport.xml_namespace);
		}
		for (Iterator it = ele.getChildElements(); it.hasNext();) {
			OMElement child = (OMElement) it.next();
			fix_v2_ns_recursive(child, MetadataSupport.ebRIMns2);
		}
	}

	// Problem here - ebxmlrr returns wrong namespaces. The following fixes
	public OMElement fixSQLQueryResponse(String return_type)
			throws XdsInternalException, MetadataException,
			MetadataValidationException {
		OMElement s_q_r = find_element(getRoot(), "SQLQueryResult");
		if (s_q_r != null) {
			for (Iterator it = s_q_r.getChildElements(); it.hasNext();) {
				OMElement child = (OMElement) it.next();
				fix_v2_ns_recursive(child, MetadataSupport.ebRIMns2);
			}
		}

		if (return_type != null && return_type.equals("LeafClass")) {
			// force getV2() to regenerate ObjectRefs since ebxmlrr doesn't do
			// it right
			this.rmObjectRefs();
			// re-init metadata class
			this.reParse();
		}

		// this is needed to fix ebxmlrr metadata
		List objects = this.getV2();

		// remove all the top level children from the wrapper
		OMElement wrapper = find_metadata_wrapper();
		for (Iterator it = wrapper.getChildElements(); it.hasNext();) {
			OMElement child = (OMElement) it.next();
			child.detach();
		}

		// add the top level children back into the wrapper
		for (int i = 0; i < objects.size(); i++) {
			OMElement ele = (OMElement) objects.get(i);
			wrapper.addChild(ele);
		}

		return getRoot();
	}

	public OMElement getV2SubmitObjectsRequest() throws XdsInternalException {
		OMNamespace rs = MetadataSupport.ebRSns2;
		OMNamespace rim = MetadataSupport.ebRIMns2;
		OMElement sor = this.om_factory().createOMElement(
				"SubmitObjectsRequest", rs);
		OMElement lrol = this.om_factory().createOMElement(
				"LeafRegistryObjectList", rim);
		sor.addChild(lrol);

		List objects = this.getV2();
		for (int i = 0; i < objects.size(); i++) {
			OMElement ele = (OMElement) objects.get(i);
			lrol.addChild(ele);
		}

		return sor;

	}

	public OMElement getV3SubmitObjectsRequest() throws XdsInternalException {
		OMNamespace rs = MetadataSupport.ebRSns3;
		OMNamespace lcm = MetadataSupport.ebLcm3;
		OMNamespace rim = MetadataSupport.ebRIMns3;
		OMElement sor = this.om_factory().createOMElement(
				"SubmitObjectsRequest", lcm);
		OMElement lrol = this.om_factory().createOMElement(
				"RegistryObjectList", rim);
		sor.addChild(lrol);

		List objects = this.getV3();
		for (int i = 0; i < objects.size(); i++) {
			OMElement ele = (OMElement) objects.get(i);
			lrol.addChild(ele);
		}

		return sor;

	}

	public String stripNamespace(String value) {
		if (value == null)
			return null;
		if (value.indexOf(":") == -1)
			return value;
		String[] parts = value.split(":");
		return parts[parts.length - 1];
	}

	public boolean hasNamespace(String value) {
		if (value.indexOf(":") == -1)
			return false;
		return true;
	}

	public String addNamespace(String value, String namespace) {
		if (hasNamespace(value))
			return value;
		if (namespace.endsWith(":"))
			return namespace + value;
		return namespace + ":" + value;
	}

	public String v2AssocType(String atype) {
		String[] parts = atype.split(":");
		if (parts.length == 0)
			return atype;
		return parts[parts.length - 1];
	}

	public HashMap<String, OMElement> getUidMap(List<OMElement> objects)
			throws MetadataException {
		HashMap<String, OMElement> map = new HashMap<String, OMElement>(); // uid
																			// ->
																			// OMElement
		for (OMElement ele : objects) {
			String ele_id = getId(ele);
			String ele_uid = getUniqueIdValue(ele_id);
			if (ele_uid != null) {
				map.put(ele_uid, ele);
			}
		}
		return map;
	}

	public HashMap<String, OMElement> getUidMap() throws MetadataException {
		return getUidMap(this.getNonObjectRefs());
	}

	public boolean isRetrievable_a(String id) throws MetadataException {
		String uri = this.getSlotValue(id, "URI", 0);
		return uri != null;
	}

	public boolean isRetrievable_b(String id) throws MetadataException {
		String uid = this.getSlotValue(id, "repositoryUniqueId", 0);
		return uid != null;
	}

	public String v3AssociationNamespace(String simpleAssociationType) {
		if (Metadata.iheAssocTypes.contains(simpleAssociationType))
			return MetadataSupport.xdsB_ihe_assoc_namespace_uri;
		else
			return MetadataSupport.xdsB_eb_assoc_namespace_uri;
	}

	boolean isURIExtendedFormat(OMElement eo) {
		String uri = getSlotValue(eo, "URI", 0);
		String uri2 = getSlotValue(eo, "URI", 1);

		if (uri == null)
			return false;

		if (uri2 != null)
			return true;

		String[] parts = uri.split("\\|");
		return (parts.length >= 2);

	}

	public String getURIAttribute(OMElement eo) throws MetadataException {
		String eoId = getId(eo);
		String value = null;
		if (!isURIExtendedFormat(eo))
			value = getSlotValue(eo, "URI", 0);
		else {
			HashMap<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < 1000; i++) {
				String slotValue = getSlotValue(eoId, "URI", i);
				if (slotValue == null)
					break;
				String[] parts = slotValue.split("\\|");
				if (parts.length != 2 || parts[0].length() == 0)
					throw new MetadataException("URI value does not parse: "
							+ slotValue + " must be num|string format");
				map.put(parts[0], parts[1]);
			}

			StringBuffer buf = new StringBuffer();

			int i = 1;
			for (;; i++) {
				String iStr = String.valueOf(i);
				String part = map.get(iStr);
				if (part == null)
					break;
				buf.append(part);
			}

			if (map.size() != i - 1)
				throw new MetadataException("URI value does not parse: index "
						+ i + "  not found but Slot has " + map.size()
						+ " values. Slot is\n"
						+ getSlot(eoId, "URI").toString());

			value = buf.toString();

		}

		if (value == null)
			return null;

		if (!value.startsWith("http://") && !value.startsWith("https://"))
			throw new MetadataException(
					"URI must have http:// or https:// prefix. URI was calculated to be\n"
							+ value + "\nand original slot is\n"
							+ getSlot(eoId, "URI").toString());

		return value;
	}

	int uriChunkSize = 100;

	public void setUriChunkSize(int size) {
		uriChunkSize = size;
	}

	int min(int a, int b) {
		if (a < b)
			return a;
		return b;
	}

	public void setURIAttribute(OMElement eo, String uri) {
		try {
			rmSlot(this.getId(eo), "URI");
		} catch (MetadataException e) {
		}

		if (uri.length() < uriChunkSize) {
			addSlot(eo, "URI", uri);
			return;
		}

		OMElement slot = addSlot(eo, "URI");
		StringBuffer buf = new StringBuffer();

		int chunkIndex = 1;
		int uriSize = uri.length();
		int strStart = 0;
		int strEnd = min(uriChunkSize, uriSize);

		while (true) {
			buf.setLength(0);
			buf.append(String.valueOf(chunkIndex++)).append("|").append(
					uri.substring(strStart, strEnd));

			addSlotValue(slot, buf.toString());

			if (strEnd == uriSize)
				break;

			strStart = strEnd;
			strEnd = min(strStart + uriChunkSize, uriSize);
		}

	}

}
