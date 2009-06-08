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

public class GetDocuments extends StoredQuery {

	public GetDocuments(HashMap<String, Object> params, boolean return_objects, Response response, Message log_message, boolean is_secure) {
		super(params, return_objects, response, log_message,  is_secure);


		//                         param name,                             required?, multiple?, is string?,   same size as,    alternative
		validate_parm(params, "$XDSDocumentEntryUniqueId",                 true,      true,     true,         null,            "$XDSDocumentEntryEntryUUID");
		validate_parm(params, "$XDSDocumentEntryEntryUUID",                true,      true,     true,         null,            "$XDSDocumentEntryUniqueId");
	}

	public Metadata run_internal() throws XdsException, LoggerException {
		Metadata metadata;

		ArrayList<String> uids = get_arraylist_parm("$XDSDocumentEntryUniqueId");
		ArrayList<String> uuids = get_arraylist_parm("$XDSDocumentEntryEntryUUID");

		if (uids != null) {
			OMElement ele = get_doc_by_uid(uids);
			metadata = MetadataParser.parseNonSubmission(ele);
		} else
			if ( uuids != null ) {
				OMElement ele = get_doc_by_uuid(uuids);
				metadata = MetadataParser.parseNonSubmission(ele);
			}
			else throw new XdsInternalException("GetDocuments Stored Query: uuid not found, uid not found");

		return metadata;
	}


}
