package gov.nist.registry.ws.sq;

import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.XdsCommon;
import gov.nist.registry.xdslog.LoggerException;

import java.util.HashMap;

public class SQFactory {
	boolean leafClass = true;
	XdsCommon common;
	
	public SQFactory(XdsCommon common, boolean leaf_class) { leafClass = leaf_class; this.common = common; }
	
	public Metadata findFoldersForDocumentByUuid(String uuid)
	throws LoggerException, XdsException
	{
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put("$XDSDocumentEntryEntryUUID", uuid);
		//Response response, Message log_message
		GetFoldersForDocument sffd = new GetFoldersForDocument(parms, leafClass, common.response, common.log_message, false);
		return sffd.run_internal();
	}

}
