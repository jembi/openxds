package gov.nist.registry.common2.test.registry;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.IdIndex;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

public class IdIndexTest  extends TestCase {
	String metadata_filename = "/Users/bill/IheOs/workspace_prod/xds/testdata/submit_fdd.xml";
	Metadata m;

	public void setUp() throws MetadataException, XdsInternalException, MetadataValidationException {
		m = new Metadata(new File(metadata_filename));
	}

	public void xtest_external_identifier_value() throws MetadataException {
		IdIndex ii = new IdIndex(m);
		
		assertTrue(ii.getExternalIdentifiers("SubmissionSet01").size() == 3);
		
		String uid = ii.getExternalIdentifierValue("SubmissionSet01", MetadataSupport.XDSSubmissionSet_uniqueid_uuid);
		List<String> ids = m.getAllDefinedIds();
		assertTrue(uid != null);
		assertTrue(uid.equals("$uniqueId02"));
		
	}
	
	public void test_ii()  throws MetadataException {
		IdIndex ii = new IdIndex(m);
		System.out.println("Result:\n" + ii.toString());
		
	}

}
