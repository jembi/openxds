package gov.nist.registry.ws.sq.test.getFolsForDoc;

import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.RegistryResponse;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.storedquery.SqParams;
import gov.nist.registry.common2.registry.storedquery.StoredQuerySupport;
import gov.nist.registry.ws.configurations.ebxmlrr21.Ebxmlrr21StoredQueryFactory;
import gov.nist.registry.ws.sq.test.TestBase;
import gov.nist.registry.ws.sq.test.testdata.FindFolTestData;

import org.testng.annotations.Test;

public class BasicGetFolsForDoc extends TestBase {

	FindFolTestData testdata;
	
	public BasicGetFolsForDoc() throws Exception {
		super();
		testdata = new FindFolTestData();
	}
	@Test
	public void basicObjectRefQuery() throws Exception {
		StoredQuerySupport sqs = new StoredQuerySupport(this , log);
		SqParams params = new SqParams();
		sqs.params = params;
		sqs.return_leaf_class = false;
		params.addStringParm("$XDSDocumentEntryEntryUUID", testdata.getDocUuids().get(0));
		
		// implementation specific factory
		Ebxmlrr21StoredQueryFactory eFact = new Ebxmlrr21StoredQueryFactory(params, new RegistryResponse(Response.version_2),log);
		Metadata results = eFact.GetFoldersForDocument(sqs);
				
		log.addOtherParam("results size", String.valueOf(results.getObjectRefs().size()));
		log.addOtherParam("Ids", results.getObjectIds(results.getObjectRefs()).toString());
		log.addOtherParam("contents", results.structure());
		
		assert sqs.has_validation_errors == false;
		assert results.getObjectRefs().size() == 1;
		assert results.getRegistryPackages().size() == 0;
		assert results.getAssociations().size() == 0;
		assert results.getExtrinsicObjects().size() == 0;
	}
}
