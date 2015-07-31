package gov.nist.registry.ws.sq.test.getRelated;

import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.RegistryResponse;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.storedquery.SqParams;
import gov.nist.registry.common2.registry.storedquery.StoredQuerySupport;
import gov.nist.registry.ws.configurations.ebxmlrr21.Ebxmlrr21StoredQueryFactory;
import gov.nist.registry.ws.sq.test.TestBase;
import gov.nist.registry.ws.sq.test.testdata.FindDocsTestData;
import gov.nist.registry.ws.sq.test.testdata.RelatedTestData;

import org.testng.annotations.Test;

public class BasicGetRelated extends TestBase {
	RelatedTestData testdata;
	FindDocsTestData testdata2;
	
	public BasicGetRelated() throws Exception {
		super();
		testdata = new RelatedTestData();
		testdata2 = new FindDocsTestData();
	}
	
	
	@Test
	public void basicObjectRefQuery() throws Exception {
		StoredQuerySupport sqs = new StoredQuerySupport(this , log);
		SqParams params = new SqParams();
		sqs.params = params;
		sqs.return_leaf_class = false;
		params.addStringParm("$XDSDocumentEntryEntryUUID", testdata.getOrigDocId());
		params.addListParm("$AssociationTypes", MetadataSupport.xdsB_ihe_assoc_namespace_uri + ":RPLC");
		
		// implementation specific factory
		Ebxmlrr21StoredQueryFactory eFact = new Ebxmlrr21StoredQueryFactory(params, new RegistryResponse(Response.version_2),log);
		Metadata results = eFact.GetRelatedDocuments(sqs);
				
		log.addOtherParam("results size", String.valueOf(results.getObjectRefs().size()));
		log.addOtherParam("Ids", results.getObjectIds(results.getObjectRefs()).toString());
		
		assert sqs.has_validation_errors == false;
		assert results.getObjectRefIds().contains(testdata.getOrigDocId());
		assert results.getObjectRefIds().contains(testdata.getRplcDocId());
		assert results.getObjectRefs().size() == 3;
		assert results.getRegistryPackages().size() == 0;
		assert results.getAssociations().size() == 0;
		assert results.getExtrinsicObjects().size() == 0;
	}

	@Test
	public void basicLeafClassQuery() throws Exception {
		StoredQuerySupport sqs = new StoredQuerySupport(this , log);
		SqParams params = new SqParams();
		sqs.params = params;
		sqs.return_leaf_class = true;
		params.addStringParm("$XDSDocumentEntryEntryUUID", testdata.getOrigDocId());
		params.addListParm("$AssociationTypes", MetadataSupport.xdsB_ihe_assoc_namespace_uri + ":RPLC");
		
		// implementation specific factory
		Ebxmlrr21StoredQueryFactory eFact = new Ebxmlrr21StoredQueryFactory(params, new RegistryResponse(Response.version_2),log);
		Metadata results = eFact.GetRelatedDocuments(sqs);
				
		log.addOtherParam("results size", String.valueOf(results.getObjectRefs().size()));
		log.addOtherParam("Ids", results.getObjectIds(results.getObjectRefs()).toString());
		
		assert sqs.has_validation_errors == false;
		assert results.getExtrinsicObjectIds().contains(testdata.getOrigDocId());
		assert results.getExtrinsicObjectIds().contains(testdata.getRplcDocId());
		assert results.getRegistryPackages().size() == 0;
		assert results.getAssociations().size() == 1;
		assert results.getExtrinsicObjects().size() == 2;
	}
	
	@Test
	public void noRelatedObjectRefQuery() throws Exception {
		StoredQuerySupport sqs = new StoredQuerySupport(this , log);
		SqParams params = new SqParams();
		sqs.params = params;
		sqs.return_leaf_class = false;
		params.addStringParm("$XDSDocumentEntryEntryUUID", testdata2.getDocUuids().get(0));
		params.addListParm("$AssociationTypes", MetadataSupport.xdsB_ihe_assoc_namespace_uri + ":RPLC");
		
		// implementation specific factory
		Ebxmlrr21StoredQueryFactory eFact = new Ebxmlrr21StoredQueryFactory(params, new RegistryResponse(Response.version_2), log);
		Metadata results = eFact.GetRelatedDocuments(sqs);
				
		log.addOtherParam("results size", String.valueOf(results.getObjectRefs().size()));
		log.addOtherParam("Ids", results.getObjectIds(results.getObjectRefs()).toString());
		
		assert sqs.has_validation_errors == false;
		assert results.getObjectRefs().size() == 0;
		assert results.getRegistryPackages().size() == 0;
		assert results.getAssociations().size() == 0;
		assert results.getExtrinsicObjects().size() == 0;
	}

	@Test
	public void noRelatedLeafClassQuery() throws Exception {
		StoredQuerySupport sqs = new StoredQuerySupport(this , log);
		SqParams params = new SqParams();
		sqs.params = params;
		sqs.return_leaf_class = true;
		params.addStringParm("$XDSDocumentEntryEntryUUID", testdata2.getDocUuids().get(0));
		params.addListParm("$AssociationTypes", MetadataSupport.xdsB_ihe_assoc_namespace_uri + ":RPLC");
		
		// implementation specific factory
		Ebxmlrr21StoredQueryFactory eFact = new Ebxmlrr21StoredQueryFactory(params, new RegistryResponse(Response.version_2),log);
		Metadata results = eFact.GetRelatedDocuments(sqs);
				
		log.addOtherParam("results size", String.valueOf(results.getObjectRefs().size()));
		log.addOtherParam("Ids", results.getObjectIds(results.getObjectRefs()).toString());
		
		assert sqs.has_validation_errors == false;
		assert results.getRegistryPackages().size() == 0;
		assert results.getAssociations().size() == 0;
		assert results.getExtrinsicObjects().size() == 0;
	}
	
}
