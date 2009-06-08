package gov.nist.registry.ws.sq;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.axiom.om.OMElement;

import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataParser;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.StoredQuery;
import gov.nist.registry.xdslog.LoggerException;
import gov.nist.registry.xdslog.Message;

public class GetAssociations extends StoredQuery {

	public GetAssociations(HashMap<String, Object> params, boolean return_objects, Response response, Message log_message, boolean is_secure) {
		super(params, return_objects, response, log_message,  is_secure);


		//                    param name,             required?, multiple?, is string?,   same size as,    alternative
		validate_parm(params, "$uuid",                 true,      true,     true,         null,            null);
	}

	public Metadata run_internal() throws XdsException, LoggerException {
		Metadata metadata;

		ArrayList<String> uuids = get_arraylist_parm("$uuid");

		if (uuids!= null) {
			OMElement ele = get_associations(uuids, null);
			metadata = MetadataParser.parseNonSubmission(ele);
		} 
		else throw new XdsInternalException("GetAssociations Stored Query: $uuid not found as a multi-value parameter");

		return metadata;
	}



}
