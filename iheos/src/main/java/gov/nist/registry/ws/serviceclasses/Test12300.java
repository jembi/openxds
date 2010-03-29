package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.ExceptionUtil;
import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataParser;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.RegistryErrorList;
import gov.nist.registry.common2.registry.RegistryResponse;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.ws.config.Registry;
import gov.nist.registry.ws.sq.StoredQueryFactory;

import org.apache.axiom.om.OMElement;
import org.openhealthtools.openxds.log.Message;

public class Test12300 extends RG {

	public OMElement AdhocQueryRequest(OMElement ahqr) {
		return AdhocQueryRequest(ahqr, "XCA", "Test12300");
	}

	public boolean runRequestValidation(OMElement request, RegistryErrorList rel, Message log_message) {
		try {
			StoredQueryFactory fact = Registry.getStoredQueryFactory(request, new RegistryResponse(Response.version_3), log_message);
			if ( !fact.isLeafClassReturnType()) {
				rel.add_error(MetadataSupport.XDSRegistryMetadataError, "This test requires returnType=\"LeafClass\"", "RG.java", log_message);
				return false;
			}
			return true;
		}
		catch (Exception e) { 
			rel.add_error(MetadataSupport.XDSRegistryMetadataError, ExceptionUtil.exception_details(e), "RG.java", log_message);
			return false; 
		}
	}

	public boolean runContentValidationService(Metadata request,
			Response response) {
		try {
			Metadata m = MetadataParser.parseNonSubmission(response.getResponse());
			if (    m.getExtrinsicObjects().size() != 2 ||
					m.getSubmissionSets().size() != 0 ||
					m.getFolders().size() != 0 ||
					m.getAssociations().size() != 0) {
				throw new MetadataException("Expected 2 ExtrinsicObjects, found " + m.getMetadataDescription());
			}
		}
		catch (Exception e) {
			response.registryErrorList.add_error(MetadataSupport.XDSRegistryMetadataError, ExceptionUtil.exception_details(e), "RG.java", log_message);
			return false;
		}
		return true;
	}




}
