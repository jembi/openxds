package gov.nist.registry.ws.test;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.IdParser;
import gov.nist.registry.common2.registry.Metadata;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.util.XPathEvaluator;

public class RegistryAdaptorTest extends TestCase {
	String metadata_filename = "/Users/bill/IheOs/workspace_prod/xds/testdata/submit_fdd.xml";
	Metadata m;
 
	public void setUp() throws MetadataException, XdsInternalException, MetadataValidationException {
		m = new Metadata(new File(metadata_filename));
	}


	public void test_compile_symbolic_names() throws Exception {
		// Document1
		String[] document1 = {
				"//*[local-name()='SubmitObjectsRequest']/*[local-name()='LeafRegistryObjectList'][1]/*[local-name()='ExtrinsicObject'][1]/@id",
				"//*[local-name()='SubmitObjectsRequest']/*[local-name()='LeafRegistryObjectList'][1]/*[local-name()='ExtrinsicObject'][1]/*[local-name()='Classification'][1]/@classifiedObject",
				"//*[local-name()='SubmitObjectsRequest']/*[local-name()='LeafRegistryObjectList'][1]/*[local-name()='ExtrinsicObject'][1]/*[local-name()='Classification'][2]/@classifiedObject",
				"//*[local-name()='SubmitObjectsRequest']/*[local-name()='LeafRegistryObjectList'][1]/*[local-name()='ExtrinsicObject'][1]/*[local-name()='Classification'][3]/@classifiedObject",
				"//*[local-name()='SubmitObjectsRequest']/*[local-name()='LeafRegistryObjectList'][1]/*[local-name()='ExtrinsicObject'][1]/*[local-name()='Classification'][4]/@classifiedObject",
				"//*[local-name()='SubmitObjectsRequest']/*[local-name()='LeafRegistryObjectList'][1]/*[local-name()='ExtrinsicObject'][1]/*[local-name()='Classification'][5]/@classifiedObject",
				"//*[local-name()='SubmitObjectsRequest']/*[local-name()='LeafRegistryObjectList'][1]/*[local-name()='ExtrinsicObject'][1]/*[local-name()='Classification'][6]/@classifiedObject",
				"//*[local-name()='SubmitObjectsRequest']/*[local-name()='LeafRegistryObjectList'][1]/*[local-name()='ExtrinsicObject'][1]/*[local-name()='Classification'][7]/@classifiedObject",
				"//*[local-name()='SubmitObjectsRequest']/*[local-name()='LeafRegistryObjectList'][1]/*[local-name()='ExtrinsicObject'][1]/*[local-name()='Classification'][8]/@classifiedObject",
				"//*[local-name()='SubmitObjectsRequest']/*[local-name()='LeafRegistryObjectList'][1]/*[local-name()='Association'][1]/@targetObject"
		};

		verify_xpath_against_constant(document1, "Document01");
		// Document2
		String[] document2 = {
				"//*[local-name()='SubmitObjectsRequest']/*[local-name()='LeafRegistryObjectList'][1]/*[local-name()='ExtrinsicObject'][2]/@id",
				"//*[local-name()='SubmitObjectsRequest']/*[local-name()='LeafRegistryObjectList'][1]/*[local-name()='ExtrinsicObject'][2]/*[local-name()='Classification'][1]/@classifiedObject",
				"//*[local-name()='SubmitObjectsRequest']/*[local-name()='LeafRegistryObjectList'][1]/*[local-name()='ExtrinsicObject'][2]/*[local-name()='Classification'][2]/@classifiedObject",
				"//*[local-name()='SubmitObjectsRequest']/*[local-name()='LeafRegistryObjectList'][1]/*[local-name()='ExtrinsicObject'][2]/*[local-name()='Classification'][3]/@classifiedObject",
				"//*[local-name()='SubmitObjectsRequest']/*[local-name()='LeafRegistryObjectList'][1]/*[local-name()='ExtrinsicObject'][2]/*[local-name()='Classification'][4]/@classifiedObject",
				"//*[local-name()='SubmitObjectsRequest']/*[local-name()='LeafRegistryObjectList'][1]/*[local-name()='ExtrinsicObject'][2]/*[local-name()='Classification'][5]/@classifiedObject",
				"//*[local-name()='SubmitObjectsRequest']/*[local-name()='LeafRegistryObjectList'][1]/*[local-name()='ExtrinsicObject'][2]/*[local-name()='Classification'][6]/@classifiedObject",
				"//*[local-name()='SubmitObjectsRequest']/*[local-name()='LeafRegistryObjectList'][1]/*[local-name()='ExtrinsicObject'][2]/*[local-name()='Classification'][7]/@classifiedObject",
				"//*[local-name()='SubmitObjectsRequest']/*[local-name()='LeafRegistryObjectList'][1]/*[local-name()='ExtrinsicObject'][2]/*[local-name()='Classification'][8]/@classifiedObject",
		};
		verify_xpath_against_constant(document2, "Document02");
		// SubmissionSet01
		String[] ss = {  // classifications no longer remain as top level objects
				"//*[local-name()='SubmitObjectsRequest']/*[local-name()='LeafRegistryObjectList'][1]/*[local-name()='RegistryPackage'][1]/@id",
				"//*[local-name()='SubmitObjectsRequest']/*[local-name()='LeafRegistryObjectList'][1]/*[local-name()='RegistryPackage'][1]/*[local-name()='Classification'][1]/@classifiedObject",
				"//*[local-name()='SubmitObjectsRequest']/*[local-name()='LeafRegistryObjectList'][1]/*[local-name()='RegistryPackage'][1]/*[local-name()='Classification'][2]/@classifiedObject",
				//"//*[local-name()='SubmitObjectsRequest']/*[local-name()='LeafRegistryObjectList'][1]/*[local-name()='Classification'][2]/@classifiedObject",
				"//*[local-name()='SubmitObjectsRequest']/*[local-name()='LeafRegistryObjectList'][1]/*[local-name()='Association'][1]/@sourceObject",
		};
		verify_xpath_against_constant(ss, "SubmissionSet01");


		IdParser ra = new IdParser(m);
		ra.compileSymbolicNamesIntoUuids();


		String[] doc1_values = new String[document1.length];
		String[] doc2_values = new String[document2.length];
		String[] ss_values = new String[ss.length];

		load_xpath_values(document1, doc1_values);
		load_xpath_values(document2, doc2_values);
		load_xpath_values(ss, ss_values);

		for (int i=1; i<doc1_values.length; i++) {
			assertTrue(doc1_values[0].equals(doc1_values[i]));
		}

		for (int i=1; i<doc2_values.length; i++) {
			assertTrue(doc2_values[0].equals(doc2_values[i]));
		}

		for (int i=1; i<ss_values.length; i++) {
			assertTrue(ss_values[0].equals(ss_values[i]));
		}

		assertFalse(doc1_values[0].equals(doc2_values[0]));
		assertFalse(doc2_values[0].equals(ss_values[0]));

		assertTrue(doc1_values[0].startsWith("urn:uuid:"));
		assertTrue(doc2_values[0].startsWith("urn:uuid:"));
		assertTrue(ss_values[0].startsWith("urn:uuid:"));
	}

	private void verify_xpath_against_constant(String[] document1, String value) throws Exception {
		XPathEvaluator eval = new XPathEvaluator();
		for (int i=0; i<document1.length; i++) {
			String path = document1[i];
			List node_list = eval.evaluateXpath(path, m.getRoot(), null);
			assertTrue("path not found: " + path, node_list.size() == 1);
			for (Iterator it=node_list.iterator(); it.hasNext(); ) {
				OMAttribute attr = (OMAttribute) it.next();
				assertTrue("path is " + path + " value is " + attr.getAttributeValue() + " should be " + value, attr.getAttributeValue().equals(value));
			}
		}
	}

	private void load_xpath_values(String[] paths, String[] values) throws Exception {
		XPathEvaluator eval = new XPathEvaluator();
		for (int i=0; i<paths.length; i++) {
			String path = paths[i];
			List node_list = eval.evaluateXpath(path, m.getRoot(), null);
			assertTrue(node_list.size() == 1);
			for (Iterator it=node_list.iterator(); it.hasNext(); ) {
				OMAttribute attr = (OMAttribute) it.next();
				values[i] = attr.getAttributeValue();
			}
		}
	}

}
