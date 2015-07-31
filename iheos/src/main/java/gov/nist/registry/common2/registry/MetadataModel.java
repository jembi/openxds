package gov.nist.registry.common2.registry;

import gov.nist.registry.common2.MetadataTypes;
import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.xml.SchemaValidation;
import gov.nist.registry.common2.xml.XmlFormatter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;

public class MetadataModel {
	Metadata m;
	OMNamespace rimNS = MetadataSupport.ebRIMns2;
	OMNamespace rsNS = MetadataSupport.ebRSns2;
	int version = 2;
	String wrapper_name = "LeafRegistryObjectList";
	OMElement wrapper = null;
	OMElement me = null;
	List me_stack = new ArrayList();
	OMFactory factory = MetadataSupport.om_factory;
	int id_allocation = 0;
	boolean and_accumulator;
	boolean or_accumulator;
	boolean test_result = true;
	List errors = new ArrayList();
	List object_refs = new ArrayList();
	boolean grok = false;

	// keywords
	// with - choose  - sets me
	// add  - add
	// remove
	// set - operates on me

	String me_type() {
		return me.getLocalName();
	}

	String me_id() {
		return me.getAttributeValue(MetadataSupport.id_qname);
	}

	public String newId() {
		return "id" + Integer.toString(id_allocation++);
	}

	public String parentId() throws Exception {
		return peek_me(0).getAttributeValue(MetadataSupport.id_qname);
	}

	public String myId() {
		return me.getAttributeValue(MetadataSupport.id_qname);
	}

	public void push_me() {
			push_me_stack(me);
	}

	public OMElement pop_me() throws Exception {
		me = (OMElement) pop_me_stack();
		return me;
	}

	public int size_me() {
		return me_stack.size();
	}

	public OMElement get_me() {
		return me;
	}

	void Must(String msg, boolean tst) throws Exception {
		if (!tst)
			throw_error(msg);
	}

	void must(String msg, boolean tst) {
		if (!tst)
			errors.add(msg);
	}

	public MetadataModel showErrors() {
		for (int i=0; i<errors.size(); i++) {
			System.out.println((String) errors.get(i) + "\n");
		}
		return this;
	}

	public boolean hasErrors() {
		return errors.size() > 0;
	}

	void throw_error(String msg) throws Exception {
//		try {
			throw new XdsInternalException(msg);
//		} catch (Exception e) {
//			System.out.println(exception_details(e));
//			System.exit(1);
//		}

	}

	public OMElement peek_me(int distance) throws Exception {
		// distance == 0  is top of stack
		//            -1  is next
		int i = me_stack.size() - 1 - distance;
		Must("peek_me stack underflow", i >= 0);
		return (OMElement) me_stack.get(i); 
	}

	Object push_me_stack(Object o) {
		if (me_stack == null)
			me_stack = new ArrayList();
		me_stack.add(o);
		me = (OMElement) o;
		return o;
	}

	Object pop_me_stack() throws Exception {
		Must("me_stack not initialized", me_stack != null);
		Must("me_stack underflow", me_stack.size() > 0);
		Object o = me_stack.get(me_stack.size()-1);
		me_stack.remove(me_stack.size()-1);
		return o;
	}

	public MetadataModel() {
		this.reinitialize();
	}

	void reinitialize() {
		m = new Metadata();
		m.setGrokMetadata(grok);
	}

	public MetadataModel addDocument() {
		push_me();
		me = factory.createOMElement("ExtrinsicObject", rimNS);
		add_me_to_wrapper();
		return this;
	}
	
	public MetadataModel addAssociation() {
		push_me();
		me = factory.createOMElement("Association", rimNS);
		add_me_to_wrapper();
		return this;
	}
	
	public MetadataModel sourceObject(String id) {
		this.attribute("sourceObject", id);
		return this;
	}

	public MetadataModel targetObject(String id) {
		this.attribute("targetObject", id);
		return this;
	}

	public MetadataModel associationType(String type) {
		this.attribute("associationType", type);
		return this;
	}

	public MetadataModel addRegistryPackage() {
		push_me();
		me = factory.createOMElement("RegistryPackage", rimNS);
		add_me_to_wrapper();
		return this;
	}
	
	public MetadataModel grok(boolean gr) {
		m.setGrokMetadata(gr);
		grok = gr;
		return this;
	}

	public MetadataModel mimeType(String mimetype) {
		me.addAttribute("mimeType", mimetype, null);
		return this;
	}

	public MetadataModel id(String id) {
		me.addAttribute("id", id, null);
		return this;
	}

	public MetadataModel name(String name) {
		OMElement n = factory.createOMElement("Name", rimNS);
		n.addChild(localizedString(name));
		me.addChild(n);
		return this;
	}

	public MetadataModel description(String des) {
		OMElement n = factory.createOMElement("Description", rimNS);
		n.addChild(localizedString(des));
		me.addChild(n);
		return this;
	}

	OMElement localizedString(String value) {
		OMElement ls = factory.createOMElement("LocalizedString", rimNS);
		ls.addAttribute("value", value, null);
		return ls;
	}

	public MetadataModel slot(String name, String value) {
		OMElement s = factory.createOMElement("Slot", rimNS);
		s.addAttribute("name", name, null);
		OMElement vl = factory.createOMElement("ValueList", rimNS);
		s.addChild(vl);
		OMElement v = factory.createOMElement("Value", rimNS);
		vl.addChild(v);
		v.addChild(factory.createOMText(value));
		me.addChild(s);
		return this;
	}

	OMElement element(String name) {
		return factory.createOMElement(name, rimNS);
	}

	MetadataModel attribute(String name, String value) {
		me.addAttribute(name, value, null);
		return this;
	}
	
	void add_object_ref(String uuid) {
		if ( !uuid.startsWith("urn:uuid:"))
			return;
		if (object_refs.contains(uuid))
			return;
		object_refs.add(uuid);
	}

	public MetadataModel classification(String classificationScheme, String classifiedObjectId, String nodeRepresentation) throws Exception {
		push_me();
		me = element("Classification");
		attribute("classificationScheme", classificationScheme);
		attribute("classifiedObject", classifiedObjectId);
		attribute("nodeRepresentation", nodeRepresentation);
		add_object_ref(classificationScheme);
		peek_me(0).addChild(me);
		return this;
	}

	public MetadataModel externalIdentifier(String identificationScheme, String value) throws Exception {
		push_me();
		me = element("ExternalIdentifier");
		attribute("identificationScheme", identificationScheme);
		attribute("value", value);
		add_object_ref(identificationScheme);
		peek_me(0).addChild(me);
		return this;
	}

	public MetadataModel uniqueIdSS(String uid) throws Exception {
		this.externalIdentifier("urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8", uid);
		return this;
	}

	public MetadataModel uniqueIdDoc(String uid) throws Exception {
		this.externalIdentifier("urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab", uid);
		return this;
	}

	public MetadataModel uniqueIdFol(String uid) throws Exception {
		this.externalIdentifier("urn:uuid:75df8f67-9973-4fbe-a900-df66cefecc5a", uid);
		return this;
	}

	public MetadataModel patientIdSS(String pid) throws Exception {
		this.externalIdentifier("urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446", pid);
		return this;
	}

	public MetadataModel patientIdDoc(String pid) throws Exception {
		this.externalIdentifier("urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427", pid);
		return this;
	}

	public MetadataModel patientIdFol(String pid) throws Exception {
		this.externalIdentifier("urn:uuid:f64ffdf0-4b97-4e06-b79f-a52b38ec2f8a", pid);
		return this;
	}

	public MetadataModel end() throws Exception {
		pop_me();
		return this;
	}

	OMElement wrapper() {
		return factory.createOMElement(wrapper_name, rimNS);
	}

//	OMElement wrap() {
//		OMElement wrapper = wrapper();
//		wrapper.addChild(me);
//		return wrapper;
//	}
	
	void add_me_to_wrapper() {
		if (wrapper == null) 
			wrapper = wrapper();
		wrapper.addChild(me);
	}
	
	public MetadataModel compile() throws Exception {
//		for (int i=0; i<object_refs.size(); i++) {
//			String uuid = (String) object_refs.get(i);
//			OMElement or = factory.createOMElement("ObjectRef", rimNS);
//			or.addAttribute("id", uuid, null);
//			wrapper.addChild(or);
//		}
		try {
			//this.reinitialize();
			m.addMetadata(wrapper);
		} 
		catch (MetadataValidationException e) {
			Must(exception_details(e), false);
		}
		catch (MetadataException e) {
			Must(exception_details(e), false);
		}
		
		return this;
	}

	public OMElement get() throws Exception {
		OMElement wrapper = wrapper();
		List contents=null;
		try {
			contents = (version == 2) ? m.getV2() : m.getV3();
		} catch (XdsInternalException e) {
			Must(exception_details(e), false);
		}
		for (int i=0; i<contents.size(); i++) {
			wrapper.addChild((OMElement) contents.get(i));
		}
//		// getV2 does not generate object refs
//		for (int i=0; i<object_refs.size(); i++) {
//			String uuid = (String) object_refs.get(i);
//			OMElement or = factory.createOMElement("ObjectRef", rimNS);
//			or.addAttribute("id", uuid, null);
//			wrapper.addChild(or);
//		}
		
		
		return wrapper;
	}
	
	public MetadataModel print() throws Exception {
		System.out.println(XmlFormatter.format(get().toString(), false));
		return this;
	}

	public MetadataModel withExtrinsicObject(String id) throws MetadataException , Exception {
		me = m.getObjectById(id);
		Must("ExtrinsicObject " + id + " not found", me != null);
		must("Expected ExtrinsicObject - got " + me.getLocalName() + " in \n" + XmlFormatter.format(me.toString(), false), 
				me.getLocalName().equals("ExtrinsicObject"));
		return this;
	}

	public MetadataModel removeSlot(String slot_name) {
		OMElement s = m.findSlot(me, slot_name);
		must("Cannot find slot named " + slot_name + " in " + me_type() + " with id = " + me_id() + " in \n" + XmlFormatter.format(me.toString(), false),
				s != null);
		s.detach();
		return this;
	}
	
	public MetadataModel removeClassifications(String classificationScheme) {
		List cls = m.findClassifications(me, classificationScheme);
		for (int i=0; i<cls.size(); i++) {
			OMElement cl = (OMElement) cls.get(i);
			cl.detach();
		}
		return this;
	}
	
	boolean _hasSlot(String slot_name) {
		OMElement s = m.findSlot(me, slot_name);
		return (s != null);
	}

	public MetadataModel hasSlot(String slot_name) {
		boolean val = _hasSlot(slot_name);
		this.update_accumulators(val);
		return this;
	}

	public MetadataModel withExternalIdentifier(String identificationScheme) {
		boolean done = false;
		List al = m.findChildElements(me, "ExternalIdentifier");
		for (int i=0; i<al.size(); i++) {
			OMElement ei = (OMElement) al.get(i);
			if (ei.getAttributeValue(MetadataSupport.identificationscheme_qname).equals(identificationScheme)) {
				push_me();
				me = ei;
				must("Multiple ExternalIdentifiers with identificationScheme = " + identificationScheme + " found in " + om_desc(me), !done);
				done = true;
			}
		}
		if (done)
			return this;
		must("ExternalIdentifier with identificationScheme of " + identificationScheme + " not found in " + om_desc(me), true); 
		return this;
	}

	boolean _hasAtt(String name, String value) {
		String val = me.getAttributeValue(new QName(name));
		if (val == null) return false;
		return val.equals(value);
	}

	public MetadataModel hasAtt(String name, String value) {
		boolean val = _hasAtt(name, value);
		update_accumulators(val);
		return this;
	}

	private void update_accumulators(boolean val) {
		if ( !val ) this.and_accumulator = false;
		if ( val )  this.or_accumulator = true;
	}

	public MetadataModel startAnd() {
		and_accumulator = true;
		return this;
	}

	public MetadataModel endAnd() {
		test_result = and_accumulator;
		return this;
	}

	public MetadataModel endAnd(String msg) {
		endAnd();
		must(msg, test_result);
		return this;
	}

	public MetadataModel endAndNot() {
		test_result = !and_accumulator;
		return this;
	}

	public MetadataModel startOr() {
		or_accumulator = false;
		return this;
	}

	public MetadataModel endOr() {
		must("Or failed", or_accumulator);
		return this;
	}

	public boolean testResult() {
		return test_result;
	}

	public MetadataModel schemaCheck() throws Exception {
		OMElement sor = factory.createOMElement("SubmitObjectsRequest", rsNS);
		sor.addChild(get());
		try {
			String errs = SchemaValidation.validate(sor, MetadataTypes.METADATA_TYPE_R);
			if (errs != null && errs.length() > 0)
				must(errs, false);
		} catch (XdsInternalException e) {
			Must("Schema validation threw exception", false);
		}
		return this;
	}


	String om_desc(OMElement e) {
		return e.getLocalName() + " with id=" + e.getAttributeValue(MetadataSupport.id_qname);
	}

	protected String exception_details(Exception e) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		e.printStackTrace(ps);

		return "Exception thrown: " + e.getClass().getName() + "\n" + e.getMessage() + "\n" + new String(baos.toByteArray());
	}



}
