package gov.nist.registry.ws.sq;

import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataParser;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.StoredQuery;
import gov.nist.registry.xdslog.LoggerException;
import gov.nist.registry.xdslog.Message;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.axiom.om.OMElement;

public class GetSubmissionSets extends StoredQuery {

	public GetSubmissionSets(HashMap<String, Object> params, boolean return_objects, Response response, Message log_message, boolean is_secure) {
		super(params, return_objects, response, log_message,  is_secure);


		//                    param name,             required?, multiple?, is string?,   same size as,    alternative
		validate_parm(params, "$uuid",                 true,      true,     true,         null,            null);
	}

	public Metadata run_internal() throws XdsException, LoggerException {
		Metadata metadata;

		ArrayList<String> uuids = get_arraylist_parm("$uuid");

		if (uuids != null && uuids.size() > 0) {
			OMElement ele = get_submissionsets(uuids);
			// this may contain duplicates - parse differently
			metadata = new Metadata();
			//metadata.setGrokMetadata(false);
			metadata.addMetadata(ele, true);

			if (metadata.getSubmissionSetIds().size() > 0) {
				OMElement assocs_ele = this.get_associations("urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember", metadata.getSubmissionSetIds(), uuids);
				metadata.addMetadata(assocs_ele, true);
			}
		} 
		else throw new XdsInternalException("GetSubmissionSets Stored Query: $uuid not found");

		return metadata;
	}

	protected OMElement get_submissionsets(ArrayList<String> uuids) throws XdsException,
	LoggerException {
		init();
		if (this.return_leaf_class)
			a("SELECT * FROM RegistryPackage rp, Association a, ExternalIdentifier ei");
		else
			a("SELECT rp.id FROM RegistryPackage rp, Association a, ExternalIdentifier ei");  
		n();
		a("WHERE ");  n();
		a("	  a.sourceObject = rp.id AND");  n(); 
		a("   a.associationType = 'urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember' AND"); n();
		a("	  a.targetObject IN "); a(uuids);  a(" AND"); n(); 
		a("   ei.registryObject = rp.id AND"); n();
		a("   ei.identificationScheme = 'urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446'"); n();

		return query(this.return_leaf_class);
	}

	protected OMElement get_associations(String type, ArrayList<String> froms, ArrayList<String> tos) 
	throws XdsException, LoggerException {
		init();
		if (this.return_leaf_class)
			a("SELECT * FROM Association a");
		else
			a("SELECT a.id FROM Association a");  
		n();
		a("WHERE ");  n();
		a("  a.associationType = '" + type + "' AND"); n();
		a("  a.sourceObject IN"); a(froms); a(" AND"); n();
		a("  a.targetObject IN"); a(tos); n();


		return query(this.return_leaf_class);
	}

}
