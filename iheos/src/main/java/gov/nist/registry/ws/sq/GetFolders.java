package gov.nist.registry.ws.sq;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.axiom.om.OMElement;

import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataParser;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.StoredQuery;
import gov.nist.registry.xdslog.LoggerException;
import gov.nist.registry.xdslog.Message;

public class GetFolders extends StoredQuery {


	public GetFolders(HashMap params, boolean return_objects, Response response, Message log_message, boolean is_secure) {
		super(params, return_objects, response, log_message,  is_secure);


		//                         param name,                             required?, multiple?, is string?,   same size as,    alternative
		validate_parm(params, "$XDSFolderEntryUUID",                         true,      true,     true,         null,            "$XDSFolderUniqueId");
		validate_parm(params, "$XDSFolderUniqueId",                          true,      true,     true,         null,            "$XDSFolderEntryUUID");
	}

	public Metadata run_internal() throws XdsException, LoggerException {
		Metadata metadata;

		ArrayList<String> fol_uuid = get_arraylist_parm("$XDSFolderEntryUUID");
		if (fol_uuid != null) {
			// starting from uuid
			OMElement x = get_fol_by_uuid(fol_uuid);
			metadata = MetadataParser.parseNonSubmission(x);
			if (metadata.getFolders().size() == 0) return metadata;
		} else {
			// starting from uniqueid
			ArrayList<String> fol_uid = get_arraylist_parm("$XDSFolderUniqueId");
			OMElement x = get_fol_by_uid(fol_uid);
			metadata = MetadataParser.parseNonSubmission(x);
		}
		
		return metadata;

	}


}
