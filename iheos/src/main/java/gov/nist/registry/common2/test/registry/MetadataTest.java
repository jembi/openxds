package gov.nist.registry.common2.test.registry;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataParser;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.xml.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.apache.axiom.om.OMElement;

public class MetadataTest extends TestCase {
	String metadata_filename = "/Users/bill/IheOs/workspace_prod/xds/testdata/submit_fdd.xml";
	String metadata_filename2 = "/Users/bill/IheOs/workspace_prod/xds/testdata/submit_fdd1.xml";
	Metadata m;

	public void setUp() throws MetadataException, XdsInternalException, MetadataValidationException {
		m = new Metadata(new File(metadata_filename));
	}
	
	public void test_get_object_by_id() throws Exception {
		assertTrue(m.getExtrinsicObjectIds().size() == 2);

		String id = m.getExtrinsicObjectIds().get(0);
		
		assertFalse(m.getObjectById(id) == null);
	}

	public void test_get_object_by_id_with_addMetadata() throws Exception {
		assertTrue(m.getExtrinsicObjectIds().size() == 2);

		String id = m.getExtrinsicObjectIds().get(0);
		
		assertFalse(m.getObjectById(id) == null);

		Metadata m2 = new Metadata(new File(metadata_filename2));
		m.setGrokMetadata(false);
		m.addMetadata(m, true);

		assertFalse(m.getObjectById(id) == null);
}

	public void test_add_metadata_duplicate() throws Exception {
		assertTrue(m.getSubmissionSetIds().size() == 1);
		assertTrue(m.getExtrinsicObjectIds().size() == 2);

		String doc_1_id = m.getExtrinsicObjectIds().get(0);
		
		assertFalse(m.getObjectTypeById(doc_1_id) == null);

		Metadata m2 = new Metadata(new File(metadata_filename));
		m.setGrokMetadata(false);
		m.addMetadata(m, true);
		assertTrue(m.getSubmissionSetIds().size() + " SS", m.getSubmissionSetIds().size() == 1);
		assertTrue(m.getExtrinsicObjectIds().size() + " Docs", m.getExtrinsicObjectIds().size() == 2);

		assertFalse(m.getObjectTypeById(doc_1_id) == null);
	}

	public void test_add_metadata_no_duplicate() throws Exception {
		assertTrue(m.getSubmissionSetIds().size() == 1);
		assertTrue(m.getExtrinsicObjectIds().size() == 2);

		String doc_1_id = m.getExtrinsicObjectIds().get(0);
		
		assertFalse(m.getObjectTypeById(doc_1_id) == null);

		Metadata m2 = new Metadata(new File(metadata_filename));
		m.setGrokMetadata(false);
		m.addMetadata(m, false);
		assertTrue(m.getSubmissionSetIds().size() + " SS", m.getSubmissionSetIds().size() == 2);
		assertTrue(m.getExtrinsicObjectIds().size() + " Docs", m.getExtrinsicObjectIds().size() == 4);

		assertFalse(m.getObjectTypeById(doc_1_id) == null);
	}

	public void test_count_major() throws XdsInternalException, MetadataException {
		List major = m.getMajorObjects();
		//System.out.println("test_count_major");
		for (int i=0; i<major.size(); i++) {
			OMElement e = (OMElement) major.get(i);
			//System.out.println(i+1 + " " + e.getLocalName());
		}
		assertTrue("major size is " + major.size(), major.size() == 26);
	}

	public void test_find_doc_external_identifier() throws MetadataException, XdsInternalException {
		List major_objects = m.getMajorObjects();
		List ei = new ArrayList();
		for (int i=0; i<major_objects.size(); i++) {
			OMElement major_object = (OMElement) major_objects.get(i);
			List results = m.getExternalIdentifiers(major_object, "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab");
			ei.addAll(results);
		}
		assertTrue(ei.size() == 2);
	}

	public void test_find_fol_external_identifier()  throws MetadataException, XdsInternalException {
		List major_objects = m.getMajorObjects();
		List ei = new ArrayList();
		for (int i=0; i<major_objects.size(); i++) {
			OMElement major_object = (OMElement) major_objects.get(i);
			List results = m.getExternalIdentifiers(major_object, "urn:uuid:75df8f67-9973-4fbe-a900-df66cefecc5a");
			ei.addAll(results);
		}
		assertTrue(ei.size() == 1);
	}

	public void test_find_ss_external_identifier() throws MetadataException, XdsInternalException {
		List major_objects = m.getMajorObjects();
		List ei = new ArrayList();
		for (int i=0; i<major_objects.size(); i++) {
			OMElement major_object = (OMElement) major_objects.get(i);
			List results = m.getExternalIdentifiers(major_object, "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8");
			ei.addAll(results);
		}
		assertTrue(ei.size() == 1);
	}

	public void test_count_ExtrinsicObjects() {
		List eo = m.getExtrinsicObjects();
		assertTrue(eo.size() == 2);
	}

	public void test_count_SubmissionSets() throws MetadataException {
		OMElement ss = m.getSubmissionSet();
		assertTrue(ss != null);
	}

	public void test_count_folders() throws MetadataException {
		List folders = m.getFolders();
		assertTrue(folders.size() == 1);
	}

	public void test_object_type_by_id() throws MetadataException {
		assertTrue(m.getObjectTypeById("SubmissionSet01").equals("SubmissionSet"));
		assertTrue(m.getObjectTypeById("folder1").equals("Folder"));
		assertTrue(m.getObjectTypeById("Document01").equals("ExtrinsicObject"));
		assertTrue(m.getObjectTypeById("Document02").equals("ExtrinsicObject"));
	}

	// required by_id
	public void test_getexternalidentifiervalue() throws MetadataException {
		assertTrue(m.getExternalIdentifierValue("Document01", "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab").equals("$uniqueId01"));
		assertTrue(m.getExternalIdentifierValue("SubmissionSet01", "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8").equals("$uniqueId02"));
		assertTrue(m.getExternalIdentifierValue("Document02", "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab").equals("$uniqueId03"));
	}

	public void test_getexternalidentifiervalue2() throws MetadataException {
		assertTrue(m.getExternalIdentifierValue("Document01", MetadataSupport.XDSDocumentEntry_uniqueid_uuid).equals("$uniqueId01"));
		assertTrue(m.getExternalIdentifierValue("SubmissionSet01", MetadataSupport.XDSSubmissionSet_uniqueid_uuid).equals("$uniqueId02"));
		assertTrue(m.getExternalIdentifierValue("Document02", MetadataSupport.XDSDocumentEntry_uniqueid_uuid).equals("$uniqueId03"));
	}


//	public void test_classification_compile()  throws XdsInternalException, MetadataException {
//	String metadata_filename = "/Users/bill/IheOs/workspace_prod/xds/testdata/class_test.xml";
//	try {
//	m = new Metadata(new File(metadata_filename));
//	m.runAnalysis();
//	fail("Did not throw MetadataValidationException");
//	} 
//	catch (MetadataValidationException e) {

//	}

//	}

	public void test_compile_dup() throws XdsInternalException, MetadataException, MetadataValidationException {
		String metadata_filename = "/Users/bill/IheOs/workspace_prod/xds/testdata/minimal.xml";
		m = new Metadata(new File(metadata_filename));
		OMElement t = m.getRoot();
		assertTrue(t.getLocalName().equals("SubmitObjectsRequest"));
		assertTrue(t.getFirstElement().getLocalName().equals("LeafRegistryObjectList"));
		assertTrue(t.getFirstChildWithName(new QName(MetadataSupport.ebRIMns2_uri,"LeafRegistryObjectList")) != null);
		List order = new ArrayList();
		OMElement dup = m.dup();
		System.out.println("test_compile_dup:\n" + dup.toString());
		this.assertTrue(dup.getLocalName().equals("SubmitObjectsRequest"));
		OMElement lrol =  dup.getFirstChildWithName(new QName(MetadataSupport.ebRIMns2_uri,"LeafRegistryObjectList"));
		assertNotNull(lrol);
		OMElement eo = lrol.getFirstChildWithName(new QName(MetadataSupport.ebRIMns2_uri,"RegistryPackage"));
		assertNotNull(eo);
		for (Iterator it=eo.getChildElements(); it.hasNext(); ) {
			OMElement e = (OMElement) it.next();
			order.add(e);
		}
		//System.out.println("test_compile_dup");
		//System.out.println(dup.toString());

		assertTrue("size is " + order.size(), order.size() == 5);
		assertTrue("0 Name: found " +               ((OMElement)order.get(0)).getLocalName(), ((OMElement)order.get(0)).getLocalName().equals("Name"));
		assertTrue("1 Description: found " +        ((OMElement)order.get(1)).getLocalName(), ((OMElement)order.get(1)).getLocalName().equals("Description"));
		assertTrue("2 Slot: found " +               ((OMElement)order.get(2)).getLocalName(), ((OMElement)order.get(2)).getLocalName().equals("Slot"));
		assertTrue("3 Classification: found " +     ((OMElement)order.get(3)).getLocalName(), ((OMElement)order.get(3)).getLocalName().equals("Classification"));
		assertTrue("4 ExternalIdentifier: found " + ((OMElement)order.get(4)).getLocalName(), ((OMElement)order.get(4)).getLocalName().equals("ExternalIdentifier"));
	}

	public void test_v3_to_v2() throws XdsInternalException{
		OMElement v3 = m.getV3SubmitObjectsRequest();
		System.out.println(v3.toString());
	}

	public void test_rmObject() throws XdsInternalException,MetadataValidationException, MetadataException {
		assertTrue(m.getAssociations().size() == 1);
		OMElement assoc = m.getAssociations().get(0);
		assertTrue(assoc != null);
		m.rmObject(assoc);
		assertTrue(m.getAssociations().size() == 0);

		String v2_metadata = m.getV2SubmitObjectsRequest().toString();
		Metadata m2 = MetadataParser.parseNonSubmission(Util.parse_xml(v2_metadata));
		assertTrue("found " + m2.getAssociations().size() + " associations", m2.getAssociations().size() == 0);

		String v3_metadata = m.getV3SubmitObjectsRequest().toString();
		Metadata m3 = MetadataParser.parseNonSubmission(Util.parse_xml(v3_metadata));
		assertTrue("found " + m3.getAssociations().size() + " associations", m3.getAssociations().size() == 0);
	}
	
}
