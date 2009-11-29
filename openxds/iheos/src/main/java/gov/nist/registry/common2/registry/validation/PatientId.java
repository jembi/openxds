package gov.nist.registry.common2.registry.validation;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.RegistryErrorList;

import java.util.ArrayList;
import java.util.List;

public class PatientId {
	List<String> patient_ids;
	RegistryErrorList rel;
	Metadata m;


	public PatientId( Metadata m, RegistryErrorList rel, boolean is_submit, boolean is_xdsb) {
		this.rel = rel;
		this.m = m;
		patient_ids = new ArrayList<String>();
	}

	public void run() throws XdsInternalException, MetadataValidationException, MetadataException {

		gather_patient_ids(m, m.getSubmissionSetIds(),   MetadataSupport.XDSSubmissionSet_patientid_uuid);
		gather_patient_ids(m, m.getExtrinsicObjectIds(), MetadataSupport.XDSDocumentEntry_patientid_uuid);
		gather_patient_ids(m, m.getFolderIds(),          MetadataSupport.XDSFolder_patientid_uuid);

		if (patient_ids.size() > 1)
			rel.add_error(MetadataSupport.XDSPatientIdDoesNotMatch, "Multiple Patient IDs found in submission: " + patient_ids, "registry/validation/PatientId.java", null);
//			rel.add_error(MetadataSupport.XDSResultNotSinglePatient, "Multiple Patient IDs found in submission: " + patient_ids, "registry/validation/PatientId.java", null);
}


	void gather_patient_ids(Metadata m, List<String> parts, String uuid) throws MetadataException {
		String patient_id;
		for (String id : parts) {		
			patient_id = m.getExternalIdentifierValue(id, uuid);
			if (patient_id == null) continue;
			if ( ! patient_ids.contains(patient_id)) 
				patient_ids.add(patient_id);
		}
	}

	void err(String msg) {
		rel.add_error(MetadataSupport.XDSRegistryMetadataError, msg, "PatientId.java", null);
	}
}
