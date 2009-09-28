/**
 *  Copyright (c) 2009 Misys Open Source Solutions (MOSS) and others
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  Contributors:
 *    Misys Open Source Solutions - initial API and implementation
 *
 */
package org.openhealthtools.openxds.registry.api;

import org.openhealthexchange.openpixpdq.data.Patient;
import org.openhealthexchange.openpixpdq.data.PatientIdentifier;


/**
 * This interface defines the operations of Patient Manager in the
 * XDS Registry.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public interface XdsRegistryPatientManager {
    /**
     * Whether the given patient is a valid patient in the patient manager implementation.
     *
     * @param pid the {@link PatientIdentifier} to be checked
     * @param context the {@link RegistryPatientContext}
     * @return <code>true</code> if the patient id is valid; <code>false</code> otherwise.
	 * @throws RegistryPatientException when there is trouble validating the patient
     */
    public boolean isValidPatient(PatientIdentifier pid, RegistryPatientContext context) throws RegistryPatientException;

	/**
	 * Creates a new patient.  This method sends the patient demographics contained
	 * in the <code>Patient</code> to the patient manager implementation.
	 * <p>
	 * 
	 * @param patient the demographics of the patient to be created
     * @param context the {@link RegistryPatientContext}
	 * @throws RegistryPatientException When there is trouble creating the patient
	 */
	public void createPatient(Patient patient, RegistryPatientContext context) throws RegistryPatientException;

	/**
	 * Updates the patient's demographics in the patient manager implementation. 
	 * This method sends the updated patient demographics contained
	 * in the <code>Patient</code> to the patient manager implementation.
	 * 
	 * @param patient the new demographics of the patient to be updated
     * @param context the {@link RegistryPatientContext}
	 * @throws RegistryPatientException when there is trouble updating the patient
	 */
	public void updatePatient(Patient patient, RegistryPatientContext context) throws RegistryPatientException;

	/**
	 * Merges two patients together because they have been found to be
	 * the same patient.  The first argument describes the surviving patient 
	 * demographics; the second argument represents the patient to be merged
	 * with the surviving patient.
	 * 
	 * @param survivingPatient the surviving patient
	 * @param mergePatient the patient to be replaced, and merged with the surviving patient
     * @param context the {@link RegistryPatientContext}
	 * @throws RegistryPatientException when there is trouble merging the patients
	 */
	public void mergePatients(Patient survivingPatient, Patient mergePatient, RegistryPatientContext context) throws RegistryPatientException;

	/**
	 * Un-merges two patients previously merged successfully as a result of a 
	 * failure in a merge related transaction. This method is called whenever
	 * there is a failure in the registry to move the metadata from the mergePatient
	 * the surviving patient. The first argument describes the surviving patient. 
	 * The second argument represents the patient merged previously 
	 * with the surviving patient.
	 * 
	 * @param survivingPatient the surviving patient
	 * @param mergePatient the patient merged with the surviving patient
     * @param context the {@link RegistryPatientContext}
	 * @throws RegistryPatientException when there is trouble un-merging the patients
	 */
	public void unmergePatients(Patient survivingPatient, Patient mergePatient, RegistryPatientContext context) throws RegistryPatientException;
}
