package org.openhealthexchange.openxds.registry.patient;

import org.openhealthexchange.openpixpdq.data.Patient;
import org.openhealthexchange.openpixpdq.data.PatientIdentifier;
import org.openhealthexchange.openxds.registry.api.IXdsRegistryPatientManager;
import org.openhealthexchange.openxds.registry.api.RegistryPatientContext;
import org.openhealthexchange.openxds.registry.api.RegistryPatientException;

public class MockupXdsRegistryPatientManager implements
		IXdsRegistryPatientManager {

	public void createPatient(Patient patient, RegistryPatientContext context)
			throws RegistryPatientException {
		// TODO Auto-generated method stub

	}

	public boolean isValidPatient(PatientIdentifier pid,
			RegistryPatientContext context) throws RegistryPatientException {
		// TODO Auto-generated method stub
		return true;
	}

	public void mergePatients(Patient survivingPatient, Patient mergePatient,
			RegistryPatientContext context) throws RegistryPatientException {
		// TODO Auto-generated method stub

	}

	public void unmergePatients(Patient survivingPatient, Patient mergePatient,
			RegistryPatientContext context) throws RegistryPatientException {
		// TODO Auto-generated method stub

	}

	public void updatePatient(Patient patient, RegistryPatientContext context)
			throws RegistryPatientException {
		// TODO Auto-generated method stub

	}

}
