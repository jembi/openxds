package gov.nist.registry.ws.sq;

import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataParser;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.StoredQuery;
import gov.nist.registry.xdslog.LoggerException;
import gov.nist.registry.xdslog.Message;

import java.util.HashMap;

import org.apache.axiom.om.OMElement;

public class GetFoldersForDocument extends StoredQuery {

	public GetFoldersForDocument(HashMap<String, Object> params, boolean return_objects, Response response, Message log_message, boolean is_secure) {
		super(params, return_objects, response, log_message,  is_secure);


		//                         param name,                             required?, multiple?, is string?,   same size as,    alternative
		validate_parm(params, "$XDSDocumentEntryUniqueId",                 true,      false,     true,         null,            "$XDSDocumentEntryEntryUUID");
		validate_parm(params, "$XDSDocumentEntryEntryUUID",                true,      false,     true,         null,            "$XDSDocumentEntryUniqueId");
	}

	public Metadata run_internal() throws XdsException, LoggerException {

		String uid 			= get_string_parm("$XDSDocumentEntryUniqueId");
		String uuid 		= get_string_parm("$XDSDocumentEntryEntryUUID");
		
		if (uuid == null || uuid.equals(""))
			uuid = this.get_doc_id_from_uid(uid);
		
		if (uuid == null)
			throw new XdsException("Cannot identify referenced document (uniqueId = " + uid + ")");
		
		OMElement folders = this.get_folders_for_document(uuid);
		
		return MetadataParser.parseNonSubmission(folders);
		
	}
}
