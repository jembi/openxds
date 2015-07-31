package gov.nist.registry.common2.test.registry;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.registry.MetadataModel;
import junit.framework.TestCase;

import org.apache.axiom.om.OMElement;

public class MetadataModelTest extends TestCase {
	MetadataModel mm;
	
	public void setUp() {
		mm = new MetadataModel();
	}
	
	public void xtest_me_stack() throws Exception {
		mm.addDocument();
		OMElement x1 = mm.get_me();
		assertTrue(x1.getLocalName().equals("ExtrinsicObject"));

		mm.classification("a", "b", "c");
		assertTrue(mm.size_me() == 1);
		OMElement x2 = mm.get_me();
		assertTrue(x2.getLocalName().equals("Classification"));
		
		OMElement m = mm.pop_me();
		assertTrue(mm.size_me() == 0);
		
		assertTrue(mm.get_me().getLocalName(), mm.get_me().getLocalName().equals("ExtrinsicObject"));
	}
	
	public void xtest_1() throws MetadataException,  Exception  {
		mm.addDocument()
		.id("Document1")
		.mimeType("text/plain")
		.name("Testdata")
		.slot("size", "1234")
		.description("Generated Metadata")
		.externalIdentifier("eid", "eidvlaue")
			.name("uniqueid")
			.end()
		.classification("scheme", mm.myId(), "foo")
			.name("my classification")
			.description("my description")
		    .end()
		.grok(false)
		.compile()
		.print()
		;
		
		
		//System.out.println(XmlFormatter.format(mm.get().toString(), false));
		
		//mm.get();
		
		mm.withExtrinsicObject("Document1");
		assertTrue(mm.get_me().getLocalName().equals("ExtrinsicObject"));
		
		assertTrue(mm.withExtrinsicObject("Document1")
				.startAnd()
					.hasSlot("size")
					.hasAtt("mimeType", "text/plain")
				.endAnd("size or mimeType missing")
				.showErrors()
				.testResult()
				);
		
		mm.withExtrinsicObject("Document1")
				.removeSlot("size")
				.startAnd()
					.hasSlot("size")
				.endAndNot()
		;
			
	}
	
	public void test_submission()  {
		try {
		ss_single_doc();
		
		mm.compile();
		
		mm
		.withExtrinsicObject("MyDoc")
			.removeSlot("creationTime")
			.removeSlot("hash")
			.removeClassifications("urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a")
		;
		
		assertTrue(
		mm
		.grok(true)
		.print()
		.schemaCheck()
		.showErrors()
		.testResult()
		)
		;
			
		} catch (Exception e) {
			fail("Exception: " + e.getMessage());
		}
	}

	private void ss_single_doc() throws Exception {
		mm
		.addRegistryPackage()
			.id("SubmissionSet")
			.name("My Submission Set")
			.description("My first generated submission set")
			.classification("urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d", mm.myId(), "")
				.slot("authorPerson", "joe")
				.slot("authorInstitution", "NIST")
				.slot("authorRole", "doc")
				.slot("authorSpecialty", "none")
				.end()
			.classification("urn:uuid:aa543740-bdda-424e-8c96-df4873be8500", mm.myId(), "code")
				.name("displayname")
				.slot("codingScheme", "mine")
				.end()
			.externalIdentifier("urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427", "mypid^^^domain")
				.name("XDSSubmissionSet.patientId")
				.end()
			.slot("submissionTime", "19991231")
			.externalIdentifier("urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8", "myuniqueid")
				.name("XDSSubmissionSet.uniqueId")
				.end()
			.end()
		.addDocument()
			.id("MyDoc")
			.classification("urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a", mm.myId(), "myclasscode")
				.name("display name")
				.slot("codingScheme", "a scheme")
				.end()
			.classification("urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f", mm.myId(), "very confidential")
				.name("display name")
				.slot("codingScheme", "a scheme")
				.end()
			.slot("creationTime", "00")
			.classification("urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d", mm.myId(), "my format")
				.name("display name")
				.slot("codingScheme", "a scheme")
				.end()
			.slot("hash", "abababa")
			.classification("urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1", mm.myId(), "my type")
				.name("display name")
				.slot("codingScheme", "a scheme")
				.end()
			.slot("languageCode", "en-us")
			.mimeType("text/plain")
			.externalIdentifier("urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446", "mypid^^^domain")
				.name("XDSDocumentEntry.patientId")
				.end()
			.classification("urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead", mm.myId(), "a practice setting")
				.name("display name")
				.slot("codingScheme", "a scheme")
				.end()
			.slot("serviceStartTime", "00")
			.slot("serviceStopTime", "00")
			.slot("size", "0")
			.slot("sourcePatientId", "ljs")
			.slot("sourcePatientInfo", "spi")
			.classification("urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a", mm.myId(), "my type code")
				.name("display name")
				.slot("codingScheme", "a scheme")
				.end()
			.externalIdentifier("urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab", "my unique id")
				.name("XDSDocumentEntry.patientId")
				.end()
			.slot("uri", "www")
			.end()
		.addAssociation()
			.associationType("HasMember")
			.sourceObject("SubmissionSet")
			.targetObject("MyDoc")
			.end();
	}

}
