package gov.nist.registry.ws.sq.test.findSS;

import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.RegistryResponse;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.storedquery.SqParams;
import gov.nist.registry.common2.registry.storedquery.StoredQuerySupport;
import gov.nist.registry.ws.configurations.ebxmlrr21.Ebxmlrr21StoredQueryFactory;
import gov.nist.registry.ws.sq.test.TestBase;
import gov.nist.registry.ws.sq.test.testdata.FindSSTestData;

import org.junit.Test;

public class BasicFindSS  extends TestBase {
	FindSSTestData testdata;

	public BasicFindSS() throws Exception {
		super();
		testdata = new FindSSTestData();
	}

	@Test
	public void basicObjectRefQuery() throws Exception {
		StoredQuerySupport sqs = new StoredQuerySupport(this , log);
		SqParams params = new SqParams();
		sqs.params = params;
		sqs.return_leaf_class = false;
		params.addStringParm("$XDSSubmissionSetPatientId", testdata.getPatientId());
		params.addListParm("$XDSSubmissionSetStatus", MetadataSupport.status_type_namespace + "Approved");
		
		log.addOtherParam("Patient ID", testdata.getPatientId());

		// implementation specific factory
		Ebxmlrr21StoredQueryFactory eFact = new Ebxmlrr21StoredQueryFactory(params, new RegistryResponse(Response.version_2),log);
		Metadata results = eFact.FindSubmissionSets(sqs);
				
		log.addOtherParam("results size", String.valueOf(results.getObjectRefs().size()));
		log.addOtherParam("Ids", results.getObjectIds(results.getObjectRefs()).toString());
		
		assert sqs.has_validation_errors == false;
		assert results.getObjectRefs().size() == 2;
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
		params.addStringParm("$XDSSubmissionSetPatientId", testdata.getPatientId());
		params.addListParm("$XDSSubmissionSetStatus", MetadataSupport.status_type_namespace + "Approved");
		
		log.addOtherParam("Patient ID", testdata.getPatientId());

		// implementation specific factory
		Ebxmlrr21StoredQueryFactory eFact = new Ebxmlrr21StoredQueryFactory(params,new RegistryResponse(Response.version_2), log);
		Metadata results = eFact.FindSubmissionSets(sqs);
				
		log.addOtherParam("results size", String.valueOf(results.getObjectRefs().size()));
		log.addOtherParam("Ids", results.getObjectIds(results.getObjectRefs()).toString());
		
		assert sqs.has_validation_errors == false;
		assert results.getRegistryPackages().size() == 2;
		assert results.getAssociations().size() == 0;
		assert results.getExtrinsicObjects().size() == 0;
	}


}
