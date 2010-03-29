package gov.nist.registry.ws.sq;

import gov.nist.registry.common2.exception.XDSRegistryOutOfResourcesException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.RegistryResponse;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.XdsCommon;
import gov.nist.registry.common2.registry.storedquery.SqParams;
import gov.nist.registry.common2.registry.storedquery.StoredQuerySupport;
import gov.nist.registry.ws.config.Registry;

import java.util.ArrayList;
import java.util.List;

import org.openhealthtools.openxds.log.LoggerException;

/**
 * This class offers short-cut methods for using Stored Queries within the XDS registry front end.  It uses the generic
 * interface for stored queries so it is not backend-registry implementation
 * dependent.
 * @author bill
 *
 */
public class SQFactory {
	boolean leafClass = true;
	XdsCommon common;
	
	public SQFactory(XdsCommon common) { this.common = common; }
	
	public Metadata findFoldersForDocumentByUuid(String uuid, boolean leaf_class)
	throws LoggerException, XdsException
	{
		SqParams parms = new SqParams();
		parms.addParm("$XDSDocumentEntryEntryUUID", uuid);
		Response response = new RegistryResponse(RegistryResponse.version_3);
		StoredQuerySupport sqs = new StoredQuerySupport(parms, leaf_class, response /* Response */, common.log_message, false /* isSecure */);
		StoredQueryFactory sqf = Registry.getStoredQueryFactory(parms, response, common.log_message);
		Metadata m = sqf.GetFoldersForDocument(sqs);
		if (response.has_errors()) 
			throw new XdsException(response.getErrorsAndWarnings());
		return m;
	}
	
	public Metadata findDocuments(String patientId, boolean leaf_class) throws XdsException, LoggerException, XDSRegistryOutOfResourcesException {
		SqParams parms = new SqParams();
		parms.addParm("$XDSDocumentEntryPatientId", patientId);
		
		List<String> status = new ArrayList<String>();
		status.add(MetadataSupport.status_type_namespace + "Submitted");
		status.add(MetadataSupport.status_type_namespace + "Approved");
		status.add(MetadataSupport.status_type_namespace + "Deprecated");
		parms.addListParm("$XDSDocumentEntryStatus", status);
		
		StoredQuerySupport sqs = new StoredQuerySupport(parms, leaf_class, null /* Response */, common.log_message, false /* isSecure */);
		Response response = new RegistryResponse(RegistryResponse.version_3);
		StoredQueryFactory sqf = Registry.getStoredQueryFactory(parms, response, common.log_message);
		Metadata metadata = sqf.buildStoredQueryHandler(sqs).FindDocuments(sqs);
		if (response.has_errors()) 
			throw new XdsException(response.getErrorsAndWarnings());
		return metadata;
	}

	public Metadata findFolders(String patientId, boolean leaf_class) throws XdsException, LoggerException, XDSRegistryOutOfResourcesException {
		SqParams parms = new SqParams();
		parms.addParm("$XDSFolderPatientId", patientId);
		
		List<String> status = new ArrayList<String>();
		status.add(MetadataSupport.status_type_namespace + "Submitted");
		status.add(MetadataSupport.status_type_namespace + "Approved");
		status.add(MetadataSupport.status_type_namespace + "Deprecated");
		parms.addListParm("$XDSFolderStatus", status);
		
		StoredQuerySupport sqs = new StoredQuerySupport(parms, leaf_class, null /* Response */, common.log_message, false /* isSecure */);
		Response response = new RegistryResponse(RegistryResponse.version_3);
		StoredQueryFactory sqf = Registry.getStoredQueryFactory(parms, response, common.log_message);
		Metadata metadata =  sqf.buildStoredQueryHandler(sqs).FindFolders(sqs);
		if (response.has_errors()) 
			throw new XdsException(response.getErrorsAndWarnings());
		return metadata;
	}

	public Metadata findSubmissionSets(String patientId, boolean leaf_class) throws XdsException, LoggerException, XDSRegistryOutOfResourcesException {
		SqParams parms = new SqParams();
		parms.addParm("$XDSSubmissionSetPatientId", patientId);
		
		List<String> status = new ArrayList<String>();
		status.add(MetadataSupport.status_type_namespace + "Submitted");
		status.add(MetadataSupport.status_type_namespace + "Approved");
		status.add(MetadataSupport.status_type_namespace + "Deprecated");
		parms.addListParm("$XDSSubmissionSetStatus", status);
		
		StoredQuerySupport sqs = new StoredQuerySupport(parms, leaf_class, null /* Response */, common.log_message, false /* isSecure */);
		Response response = new RegistryResponse(RegistryResponse.version_3);
		StoredQueryFactory sqf = Registry.getStoredQueryFactory(parms, response, common.log_message);
		Metadata metadata = sqf.buildStoredQueryHandler(sqs).FindSubmissionSets(sqs);
		if (response.has_errors()) 
			throw new XdsException(response.getErrorsAndWarnings());
		return metadata;
	}

	public Metadata getDocumentsByUuid(List<String> uuids, boolean leaf_class) throws XdsException, LoggerException, XDSRegistryOutOfResourcesException {
		SqParams parms = new SqParams();
		parms.addListParm("$XDSDocumentEntryUUID", uuids);
		
		StoredQuerySupport sqs = new StoredQuerySupport(parms, leaf_class, null /* Response */, common.log_message, false /* isSecure */);
		Response response = new RegistryResponse(RegistryResponse.version_3);
		StoredQueryFactory sqf = Registry.getStoredQueryFactory(parms, response, common.log_message);
		Metadata metadata =  sqf.buildStoredQueryHandler(sqs).GetDocuments(sqs);
		if (response.has_errors()) 
			throw new XdsException(response.getErrorsAndWarnings());
		return metadata;
	}

	public Metadata getFoldersByUuid(List<String> uuids, boolean leaf_class) throws XdsException, LoggerException, XDSRegistryOutOfResourcesException {
		SqParams parms = new SqParams();
		parms.addListParm("$XDSFolderEntryUUID", uuids);
		
		StoredQuerySupport sqs = new StoredQuerySupport(parms, leaf_class, null /* Response */, common.log_message, false /* isSecure */);
		Response response = new RegistryResponse(RegistryResponse.version_3);
		StoredQueryFactory sqf = Registry.getStoredQueryFactory(parms, response, common.log_message);
		Metadata metadata = sqf.buildStoredQueryHandler(sqs).GetFolders(sqs);
		if (response.has_errors()) 
			throw new XdsException(response.getErrorsAndWarnings());
		return metadata;
	}

	// 
	public Metadata getSubmissionSetsByUuid(List<String> uuids, boolean leaf_class) throws XdsException, LoggerException, XDSRegistryOutOfResourcesException {
		SqParams parms = new SqParams();
		parms.addListParm("$uuid", uuids);
		
		StoredQuerySupport sqs = new StoredQuerySupport(parms, leaf_class, null /* Response */, common.log_message, false /* isSecure */);
		Response response = new RegistryResponse(RegistryResponse.version_3);
		StoredQueryFactory sqf = Registry.getStoredQueryFactory(parms, response, common.log_message);
		Metadata metadata = sqf.buildStoredQueryHandler(sqs).GetFolders(sqs);
		if (response.has_errors()) 
			throw new XdsException(response.getErrorsAndWarnings());
		return metadata;
	}

}
