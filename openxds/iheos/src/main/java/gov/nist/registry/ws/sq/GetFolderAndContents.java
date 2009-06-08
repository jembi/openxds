package gov.nist.registry.ws.sq;

import gov.nist.registry.common2.exception.NoSubmissionSetException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataParser;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.StoredQuery;
import gov.nist.registry.xdslog.LoggerException;
import gov.nist.registry.xdslog.Message;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.axiom.om.OMElement;

public class GetFolderAndContents extends StoredQuery {

	public GetFolderAndContents(HashMap params, boolean return_objects, Response response, Message log_message, boolean is_secure) {
		super(params, return_objects, response, log_message,  is_secure);


		//                         param name,                             required?, multiple?, is string?,   same size as,    alternative
		validate_parm(params, "$XDSFolderEntryUUID",                         true,      false,     true,         null,            "$XDSFolderUniqueId");
		validate_parm(params, "$XDSFolderUniqueId",                          true,      false,     true,         null,            "$XDSFolderEntryUUID");
		validate_parm(params, "$XDSDocumentEntryFormatCode",                 false,     true,      true,         null,            null);
		validate_parm(params, "$XDSDocumentEntryConfidentialityCode",        false,     true,      true,         null,            null);
	}

	public Metadata run_internal() throws XdsException, LoggerException {
		Metadata metadata;

		String fol_uuid = get_string_parm("$XDSFolderEntryUUID");
		if (fol_uuid != null) {
			// starting from uuid
			OMElement x = get_fol_by_uuid(fol_uuid);
			metadata = MetadataParser.parseNonSubmission(x);
			if (metadata.getFolders().size() == 0) return metadata;
		} else {
			// starting from uniqueid
			String fol_uid = get_string_parm("$XDSFolderUniqueId");
			OMElement x = get_fol_by_uid(fol_uid);
				metadata = MetadataParser.parseNonSubmission(x);
			if (metadata.getFolders().size() == 0) return metadata;

			fol_uuid = metadata.getFolder(0).getAttributeValue(MetadataSupport.id_qname);
		}

		// ss_uuid has now been set

		ArrayList<String> content_ids = new ArrayList<String>();
		ArrayList<String> conf_codes = this.get_arraylist_parm("$XDSDocumentEntryConfidentialityCode");
		ArrayList<String> format_codes = this.get_arraylist_parm("$XDSDocumentEntryFormatCode");

		OMElement doc_metadata = get_fol_docs(fol_uuid, format_codes, conf_codes);
		metadata.addMetadata(doc_metadata);
		content_ids.addAll(get_ids_from_registry_response(doc_metadata));

		ArrayList<String> folder_ids = metadata.getFolderIds();

		if (content_ids.size() > 0) {
			OMElement assoc_metadata = get_assocs(folder_ids, content_ids);
			metadata.addMetadata(assoc_metadata);
		}


		return metadata;
	}


}
