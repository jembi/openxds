package gov.nist.registry.ws.sq.ebxmlrr21;

import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataParser;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.storedquery.StoredQuerySupport;
import gov.nist.registry.ws.sq.RegistryObjectValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axiom.om.OMElement;
import org.openhealthtools.openexchange.syslog.LoggerException;

public class EbXML21RegistryObjectValidator implements RegistryObjectValidator  {
	StoredQuerySupport sqs;
	EbXML21QuerySupport eb;

	public EbXML21RegistryObjectValidator(StoredQuerySupport sqs) {
		this.sqs = sqs;
		this.eb = new EbXML21QuerySupport(sqs);
	}

	/* (non-Javadoc)
	 * @see gov.nist.registry.ws.sq.ebxmlrr21.RegistryObjectValidator#validateExists(java.util.List)
	 */
	public List<String> validateExists(List<String> uuids)  throws XdsException, LoggerException {
		eb.init();
		eb.a("SELECT * FROM RegistryObject ro"); eb.n();
		eb.a("WHERE"); eb.n();
		eb.a("  ro.id IN "); eb.a(uuids); eb.n();

		List<String> results = eb.query_for_object_refs();

		List<String> missing = null;
		for (int i=0; i<uuids.size(); i++) {
			String uuid = (String) uuids.get(i);
			if ( !results.contains(uuid)) {
				if (missing == null)
					missing = new ArrayList<String>();
				missing.add(uuid);
			}
		}
		return missing;

	}
	
	List<String> uuidsOnly(List<String> ids) {
		List<String> uuids = new ArrayList<String>();
		for (String id : ids) {
			if (id.startsWith("urn:uuid:"))
				uuids.add(id);
		}
		return uuids;
	}

	// returns UUIDs that do exist in registry
	/* (non-Javadoc)
	 * @see gov.nist.registry.ws.sq.ebxmlrr21.RegistryObjectValidator#validateNotExists(java.util.ArrayList)
	 */
	public List<String> validateNotExists(List<String> ids)  throws XdsException, LoggerException {
		List<String> uuids = uuidsOnly(ids);
		eb.init();
		eb.a("SELECT * FROM RegistryObject ro"); eb.n();
		eb.a("WHERE"); eb.n();
		eb.a("  ro.id IN "); eb.a(uuids); eb.n();

		List<String> results = eb.query_for_object_refs();

		return results;		
	}


	// uid_hash is uid => hash (null for non documents)
	/* (non-Javadoc)
	 * @see gov.nist.registry.ws.sq.ebxmlrr21.RegistryObjectValidator#validateProperUids(gov.nist.registry.common2.registry.Metadata)
	 */
	public void validateProperUids(Metadata metadata)  throws XdsException, LoggerException {

		Map<String, List<String>> uid_hash = metadata.getUidHashMap();
		
		List<String> uids = new ArrayList<String>();
		uids.addAll(uid_hash.keySet());

		List<String> uid_id_schemes = new ArrayList<String>();
		uid_id_schemes.add(MetadataSupport.XDSFolder_uniqueid_uuid);
		uid_id_schemes.add(MetadataSupport.XDSSubmissionSet_uniqueid_uuid);
		uid_id_schemes.add(MetadataSupport.XDSDocumentEntry_uniqueid_uuid);

		eb.init();
		eb.a("SELECT ro.id from RegistryObject ro, ExternalIdentifier ei"); eb.n();
		eb.a("WHERE"); eb.n();
		eb.a(" ei.registryobject = ro.id AND "); eb.n(); 
		eb.a("  ei.identificationScheme IN "); eb.a(uid_id_schemes); eb.a(" AND"); eb.n();
		eb.a("  ei.value IN "); eb.a(uids); eb.n();

		// these uuids identify objects that carry one of the uids passed in in the map
		List<String> uuids = eb.query_for_object_refs();
		
		if (uuids.size() == 0)
			return;

		// at least one uniqueId is already present in the registry. If it is from a document
		// and the hashes are the same.  Otherwise it is an error.
		
		sqs.return_leaf_class = true;
		OMElement objects = eb.getObjectsByUuid(uuids);   // LeafClass for offending objects
		if (objects == null) 
			throw new XdsInternalException("RegistryObjectValidator.validateProperUids(): could not retrieve LeafClass for ObjectRef obtained from registry: UUIDs were " + uuids);
		
		Metadata m = MetadataParser.parseNonSubmission(objects);
		ArrayList<String> dup_uids = new ArrayList<String>();
		HashMap<String, OMElement> dup_objects = m.getUidMap();
		dup_uids.addAll(dup_objects.keySet());
		
		if (sqs.log_message != null){
			sqs.log_message.addOtherParam("dup uuids", uuids.toString());
			sqs.log_message.addOtherParam("dup uids", dup_uids.toString());
		}
		for (String suuid : metadata.getSubmissionSetIds()) {
			String sid = metadata.getExternalIdentifierValue(suuid, MetadataSupport.XDSSubmissionSet_uniqueid_uuid);
			if (sqs.log_message != null)
				sqs.log_message.addOtherParam("ssuid", sid);
			if (dup_uids.contains(sid)) {
				throw new MetadataValidationException("SubmissionSet uniqueId " + 
						sid + 
						" ( id = " + suuid + " ) " +
						" already present in the registry");
				
			}
		}
		
		for (String fuuid : metadata.getFolderIds()) {
			String fuid = metadata.getExternalIdentifierValue(fuuid, MetadataSupport.XDSFolder_uniqueid_uuid);
			if (sqs.log_message != null)
				sqs.log_message.addOtherParam("fuid", fuid);
			if (dup_uids.contains(fuid)) {
				throw new MetadataValidationException("Folder uniqueId " + 
						fuid + 
						" ( id = " + fuuid + " ) " +
						" already present in the registry");
				
			}
		}

		HashMap<String, OMElement> docs_submit_uid_map = metadata.getUidMap(metadata.getExtrinsicObjects());
		for (String doc_uid : docs_submit_uid_map.keySet()) {
			if (dup_uids.contains(doc_uid)) {
				OMElement reg_obj = dup_objects.get(doc_uid);
				String type = reg_obj.getLocalName();
				if ( !type.equals("ExtrinsicObject")) 
					throw new MetadataValidationException("Document uniqueId " + 
							doc_uid + 
							" already present in the registry on a non-document object");
				OMElement sub_obj = docs_submit_uid_map.get(doc_uid);
				String sub_hash = m.getSlotValue(sub_obj, "hash", 0);
				String reg_hash = m.getSlotValue(reg_obj, "hash", 0);
				if (sub_hash != null && reg_hash != null && !sub_hash.equals(reg_hash))
					sqs.response.add_error(MetadataSupport.XDSNonIdenticalHash,
							"UniqueId " + doc_uid + " exists in both the submission and Registry and the hash value is not the same: " +
							"Submission Hash Value = " + sub_hash + " and " +
							"Registry Hash Value = " + reg_hash,
							"RegistryObjectValidation.java", sqs.log_message);

			}
		}
		
	}


	/* (non-Javadoc)
	 * @see gov.nist.registry.ws.sq.ebxmlrr21.RegistryObjectValidator#validateDocuments(java.util.List)
	 */
	public List<String> validateDocuments(List<String> uuids)  throws  XdsException,LoggerException {
		eb.init();
		eb.a("SELECT * FROM ExtrinsicObject eo"); eb.n();
		eb.a("WHERE"); eb.n();
		eb.a("  eo.id IN "); eb.a(uuids);  eb.n();

		List<String> results = eb.query_for_object_refs();

		List<String> missing = null;
		for (int i=0; i<uuids.size(); i++) {
			String uuid = (String) uuids.get(i);
			if ( !results.contains(uuid)) {
				if (missing == null)
					missing = new ArrayList<String>();
				missing.add(uuid);
			}
		}
		return missing;

	}
	
	// validate the ids are in registry and belong to folders
	// return any that aren't
	/* (non-Javadoc)
	 * @see gov.nist.registry.ws.sq.ebxmlrr21.RegistryObjectValidator#validateAreFolders(java.util.List)
	 */
	public List<String> validateAreFolders(List<String> ids) throws  XdsException,LoggerException {
		eb.init();
		eb.a("SELECT rp.id FROM RegistryPackage rp, ExternalIdentifier ei"); eb.n();
		eb.a("WHERE"); eb.n();
		eb.a("  rp.status = '"+ addStatusTypeNamespace("Approved")+"' AND"); eb.n();
		eb.a("  rp.id IN "); eb.a(ids); eb.a(" AND"); eb.n();
		eb.a("  ei.registryObject = rp.id AND"); eb.n();
		eb.a("  ei.identificationScheme = '" + MetadataSupport.XDSFolder_patientid_uuid + "'"); eb.n();
		
		//br.setReason("Verify are Folders");

		List<String> results1 = eb.query_for_object_refs();

		List<String> missing = null;
		for (String id : ids) {
			if ( !results1.contains(id)) {
				if (missing == null)
					missing = new ArrayList<String>();
				missing.add(id);
			}
		}
		
		return missing;
	}


	// these selects cannot work!!!
	/* (non-Javadoc)
	 * @see gov.nist.registry.ws.sq.ebxmlrr21.RegistryObjectValidator#validateApproved(java.util.List)
	 */
	public List<String> validateApproved(List<String> uuids)  throws  XdsException,LoggerException {
		eb.init();
		eb.a("SELECT * FROM ExtrinsicObject eo"); eb.n();
		eb.a("WHERE"); eb.n();
		eb.a("  eo.status = '"+ addStatusTypeNamespace("Approved")+"' AND"); eb.n();
		eb.a("  eo.id IN "); eb.a(uuids);  eb.n();

		List<String> results1 = eb.query_for_object_refs();

		eb.init();
		eb.a("SELECT * FROM RegistryPackage eo"); eb.n();
		eb.a("WHERE"); eb.n();
		eb.a("  eo.status = '"+ addStatusTypeNamespace("Approved") +"' AND"); eb.n();
		eb.a("  eo.id IN "); eb.a(uuids);  eb.n();

		List<String> results = eb.query_for_object_refs();

		results.addAll(results1); 

		List<String> missing = null;
		for (int i=0; i<uuids.size(); i++) {
			String uuid = (String) uuids.get(i);
			if ( !results.contains(uuid)) {
				if (missing == null)
					missing = new ArrayList<String>();
				missing.add(uuid);
			}
		}
		return missing;
	}

	/* (non-Javadoc)
	 * @see gov.nist.registry.ws.sq.ebxmlrr21.RegistryObjectValidator#validateSamePatientId(java.util.List, java.lang.String)
	 */
	public List<String> validateSamePatientId(List<String> uuids, String patient_id)  throws  XdsException,LoggerException {
		if (uuids.size() == 0)
			return null;
		eb.init();
		eb.a("SELECT eo.id FROM ExtrinsicObject eo, ExternalIdentifier pid"); eb.n();
		eb.a("WHERE"); eb.n();
		eb.a("  eo.id IN "); eb.a(uuids); eb.a(" AND "); eb.n();
		eb.a("  pid.registryobject = eo.id AND");  eb.n();
		eb.a("  pid.identificationScheme='urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427' AND"); eb.n();
		eb.a("  pid.value = '"); eb.a(patient_id); eb.a("'"); eb.n();

		List<String> results1 = eb.query_for_object_refs();

		eb.init();
		eb.a("SELECT eo.id FROM RegistryPackage eo, ExternalIdentifier pid"); eb.n();
		eb.a("WHERE"); eb.n();
		eb.a("  eo.id IN "); eb.a(uuids);  eb.a(" AND"); eb.n();
		eb.a("  pid.registryobject = eo.id AND");  eb.n();
		eb.a("  pid.identificationScheme IN ('urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446','urn:uuid:f64ffdf0-4b97-4e06-b79f-a52b38ec2f8a') AND"); eb.n();
		eb.a("  pid.value = '"); eb.a(patient_id); eb.a("'"); eb.n();

		List<String> results = eb.query_for_object_refs();

		results.addAll(results1); 

		List<String> missing = null;
		for (int i=0; i<uuids.size(); i++) {
			String uuid = (String) uuids.get(i);
			if ( !results.contains(uuid)) {
				if (missing == null)
					missing = new ArrayList<String>();
				missing.add(uuid);
			}
		}
		return missing;
	}

	/* (non-Javadoc)
	 * @see gov.nist.registry.ws.sq.ebxmlrr21.RegistryObjectValidator#getXFRMandAPNDDocuments(java.util.List)
	 */
	public List<String> getXFRMandAPNDDocuments(List<String> uuids) throws  XdsException, LoggerException {
		if (uuids.size() == 0)
			return new ArrayList<String>();
		eb.init();
		eb.a("SELECT eo.id FROM ExtrinsicObject eo, Association a"); eb.n();
		eb.a("WHERE"); eb.n();
		eb.a("  a.associationType in ('"+ addAssociationTypeNamespace("XFRM") +"', '"+ addAssociationTypeNamespace("APND") +"') AND"); eb.n();
		eb.a("  a.targetObject IN "); eb.a(uuids); eb.a(" AND"); eb.n();
		eb.a("  a.sourceObject = eo.id"); eb.n();

		return eb.query_for_object_refs();
	}
	
	private String addAssociationTypeNamespace(String type) {
		return MetadataSupport.association_type_namespace + type; 
	}
	private String addStatusTypeNamespace(String type) {
		return MetadataSupport.status_type_namespace + type; 
	}


}
