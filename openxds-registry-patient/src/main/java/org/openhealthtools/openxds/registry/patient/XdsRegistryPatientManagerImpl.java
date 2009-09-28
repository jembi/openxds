/**
 *  Copyright (c) 2009 Sysnet International and others
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
 *    Sysnet International   - initial API and implementation
 *    Misys Open Source Solutions - modification and bug fixes
 */
package org.openhealthtools.openxds.registry.patient;

import java.util.List;

import org.apache.log4j.Logger;
import org.openhealthexchange.openpixpdq.data.Patient;
import org.openhealthexchange.openpixpdq.data.PatientIdentifier;
import org.openhealthtools.openxds.registry.api.XdsRegistryPatientManager;
import org.openhealthtools.openxds.registry.api.RegistryPatientContext;
import org.openhealthtools.openxds.registry.api.RegistryPatientException;
import org.openhealthtools.openxds.util.ConversionHelper;
import org.openhie.openempi.context.Context;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;
import org.openhie.openempi.service.PersonManagerService;
import org.openhie.openempi.service.PersonQueryService;

/**
 * The class is the core of XDS Registry Patient Manager and 
 * provides the patient life cycle operations such as createPatient,
 * updatePatient, mergePatients and unmergePatients.
 *  
 * @author <a href="mailto:support@sysnet.com">Odysseas Pentakalos</a>
 *
 */
public class XdsRegistryPatientManagerImpl implements XdsRegistryPatientManager
{
	private static Logger log = Logger.getLogger(XdsRegistryPatientManagerImpl.class);
	
	public boolean isValidPatient(PatientIdentifier pid, RegistryPatientContext context) throws RegistryPatientException {
		PersonQueryService personQueryService = Context.getPersonQueryService();
		PersonIdentifier identifier = ConversionHelper.getPersonIdentifier(pid);
		try {
			Person person = personQueryService.findPersonById(identifier);
			if (person == null) {
				return false;
			}
			return true;
		} catch (Exception e) {
			log.error("Failed while trying to determine if the patient with the given identifier is known." + e, e);
			throw new RegistryPatientException(e.getMessage());
		}
	}

	public void createPatient(Patient patient, RegistryPatientContext context) throws RegistryPatientException {
		PersonManagerService personManagerService = Context.getPersonManagerService();
		Person person = ConversionHelper.getPerson(patient);
		try {
			personManagerService.addPerson(person);
		} catch (Exception e) {
			log.error("Failed while trying to save a new patient record in the patient registry." + e, e);
			throw new RegistryPatientException(e.getMessage());
		}
	}

	public void updatePatient(Patient patient, RegistryPatientContext context) throws RegistryPatientException {
		PersonManagerService personManagerService = Context.getPersonManagerService();
		Person person = ConversionHelper.getPerson(patient);
		try {
			personManagerService.updatePerson(person);
		} catch (Exception e) {
			log.error("Failed while trying to update a patient record in the patient registry." + e, e);
			throw new RegistryPatientException(e.getMessage());
		}
	}

	public void mergePatients(Patient survivingPatient, Patient mergePatient, RegistryPatientContext context) throws RegistryPatientException {
		PersonManagerService personManagerService = Context.getPersonManagerService();
		PersonIdentifier survivingPersonId = findExistingPersonIdForPatient(personManagerService, survivingPatient);
		PersonIdentifier retiredPersonId = findExistingPersonIdForPatient(personManagerService, mergePatient);
		if (survivingPersonId == null || retiredPersonId == null) {
			log.error("Unable to locate one of the two patient records that need to be merged.");
			throw new RegistryPatientException("Unable to identify the two patient records that need to be merged.");
		}
		try {
			personManagerService.mergePersons(retiredPersonId, survivingPersonId);
		} catch (Exception e) {
			log.error("Failed while trying to merge two patient records in the patient registry." + e, e);
			throw new RegistryPatientException(e.getMessage());
		}
	}

	private PersonIdentifier findExistingPersonIdForPatient(PersonManagerService personManagerService, Patient patient) {
		List<PersonIdentifier> pids =null;
		if(patient.getPatientIds() != null){
			pids = new java.util.ArrayList<PersonIdentifier>(patient.getPatientIds().size());
			for (PatientIdentifier pid : patient.getPatientIds()) {
			 pids.add(ConversionHelper.getPersonIdentifier(pid));
		}
		Person person = personManagerService.getPerson(pids);
		if (person == null) {
			return null;
		}
		return person.getPersonIdentifiers().iterator().next();
		}
		return null;
	}
	public void unmergePatients(Patient survivingPatient, Patient mergePatient, RegistryPatientContext context) throws RegistryPatientException {
		//TODO:
	}
	
}
