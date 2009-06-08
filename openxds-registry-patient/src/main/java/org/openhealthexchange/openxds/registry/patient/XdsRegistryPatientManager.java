/**
 *  Copyright (c) 2009 Misys plc, Sysnet International, Medem and others
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
 *     Sysnet International - Initial API and Implementation
 */
package org.openhealthexchange.openxds.registry.patient;

import org.openhealthexchange.openpixpdq.data.Patient;
import org.openhealthexchange.openpixpdq.data.PatientIdentifier;
import org.openhealthexchange.openxds.registry.api.IXdsRegistryPatientManager;
import org.openhealthexchange.openxds.registry.api.RegistryPatientContext;
import org.openhealthexchange.openxds.registry.api.RegistryPatientException;

/**
 * The class is the core of XDS Registry Patient Manager and 
 * provides the patient life cycle operations such as createPatient,
 * updatePatient, mergePatients and unmergePatients.
 *  
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 *
 */
public class XdsRegistryPatientManager implements IXdsRegistryPatientManager {
    
	public boolean isValidPatient(PatientIdentifier pid, RegistryPatientContext context) throws RegistryPatientException {
		//TODO:
		return true;
	}

	public void createPatient(Patient patient, RegistryPatientContext context) throws RegistryPatientException {
		//TODO:
	}

	public void updatePatient(Patient patient, RegistryPatientContext context) throws RegistryPatientException {
		//TODO:
	}

	public void mergePatients(Patient survivingPatient, Patient mergePatient, RegistryPatientContext context) throws RegistryPatientException {
		//TODO:
	}

	public void unmergePatients(Patient survivingPatient, Patient mergePatient, RegistryPatientContext context) throws RegistryPatientException {
		//TODO:
	}
	
}
