/**
 *  Copyright (c) 2009-2010 Misys Open Source Solutions (MOSS) and others
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
 *    -
 */

package org.openhealthtools.openxds.registry.patient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthexchange.openpixpdq.data.Patient;
import org.openhealthtools.openxds.dao.XdsRegistryPatientDao;
import org.openhealthtools.openxds.registry.PersonIdentifier;
import org.openhealthexchange.openpixpdq.data.PatientIdentifier;
import org.openhealthtools.openxds.registry.api.RegistryPatientContext;
import org.openhealthtools.openxds.registry.api.RegistryPatientException;
import org.openhealthtools.openxds.registry.api.XdsRegistryPatientService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * The class is the core of XDS Registry Patient Manager and 
 * provides the patient life cycle operations such as createPatient,
 * updatePatient, mergePatients and unmergePatients.
 *  
 * @author <a href="mailto:Rasakannu.Palaniyandi@misys.com">Raja</a>
 *
 */
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class XdsRegistryPatientServiceImpl implements XdsRegistryPatientService
{
	private static Log log = LogFactory.getLog(XdsRegistryPatientServiceImpl.class);
	
	public XdsRegistryPatientDao xdsRegistryPatientDao;
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean isValidPatient(PatientIdentifier pid, RegistryPatientContext context) throws RegistryPatientException {
		try {
			PersonIdentifier identifier = getPersonIdentifier(pid);	
			PersonIdentifier personIdentifier = xdsRegistryPatientDao.getPersonById(identifier);
			if (personIdentifier == null) {
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Failed while trying to determine if the patient with the given identifier is known." + e, e);
			throw new RegistryPatientException(e.getMessage());
		}
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void createPatient(Patient patient, RegistryPatientContext context) throws RegistryPatientException {
		try {
		for (PatientIdentifier pid : patient.getPatientIds()) {
			PersonIdentifier identifier = getPersonIdentifier(pid,patient.isDeathIndicator());
			PersonIdentifier personIdentifier = xdsRegistryPatientDao.getPersonById(identifier);
			if(personIdentifier != null){
				if(personIdentifier.getMerge().equals("Y")){
					personIdentifier.setMerge("N");
					xdsRegistryPatientDao.updatePersonIdentifier(personIdentifier);
				}
			}else{
				xdsRegistryPatientDao.savePersonIdentifier(identifier);
			} 
		}
		}
		catch (Exception e) {
			log.error("Failed while trying to save a new patient record in the patient registry." + e, e);
			throw new RegistryPatientException(e.getMessage());
		}
	}
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updatePatient(Patient patient, RegistryPatientContext context) throws RegistryPatientException {
		for (PatientIdentifier pid : patient.getPatientIds()) {
			PersonIdentifier identifier = getPersonIdentifier(pid);	
			PersonIdentifier personIdentifier = xdsRegistryPatientDao.getPersonById(identifier);
			PersonIdentifier updateidentifier = getPersonIdentifier(personIdentifier,pid,patient.isDeathIndicator());			
		try {
			xdsRegistryPatientDao.updatePersonIdentifier(updateidentifier);
		} catch (Exception e) {
			log.error("Failed while trying to update a patient record in the patient registry." + e, e);
			throw new RegistryPatientException(e.getMessage());
		}
		}
	}	
	@Transactional(propagation = Propagation.REQUIRED)
	public void mergePatients(Patient survivingPatient, Patient mergePatient, RegistryPatientContext context) throws RegistryPatientException {
		PatientIdentifier survivingId = survivingPatient.getPatientIds().get(0);
		PersonIdentifier personIdentifier = getPersonIdentifier(survivingId);	
		PersonIdentifier survivingPersonId = xdsRegistryPatientDao.getPersonById(personIdentifier);
		for (PatientIdentifier pid : mergePatient.getPatientIds()) {
			PersonIdentifier retiredId = getPersonIdentifier(pid);		
			PersonIdentifier retiredPersonId = xdsRegistryPatientDao.getPersonById(retiredId);
		if (retiredPersonId == null ||  survivingPersonId == null) {
			log.error("Unable to locate one of the two patient records that need to be merged.");
			throw new RegistryPatientException("Unable to identify the two patient records that need to be merged.");
		}
		retiredPersonId.setSurvivingPatientId(survivingPersonId.getPatientId());
		retiredPersonId.setMerge("Y");
		try {
			xdsRegistryPatientDao.mergePersonIdentifier(retiredPersonId);
		} catch (Exception e) {
			log.error("Failed while trying to merge two patient records in the patient registry." + e, e);
			throw new RegistryPatientException(e.getMessage());
		}
		}
	}
	@Transactional(propagation = Propagation.REQUIRED)
	public void unmergePatients(Patient survivingPatient, Patient mergePatient, RegistryPatientContext context) throws RegistryPatientException {
		PatientIdentifier survivingId = survivingPatient.getPatientIds().get(0);
		PersonIdentifier personIdentifier = getPersonIdentifier(survivingId);	
		PersonIdentifier survivingPersonId = xdsRegistryPatientDao.getPersonById(personIdentifier);
		for (PatientIdentifier pid : mergePatient.getPatientIds()) {
			PersonIdentifier retiredId = getPersonIdentifier(pid);		
			PersonIdentifier retiredPersonId = xdsRegistryPatientDao.getPersonById(retiredId);
		if (retiredPersonId == null ||  survivingPersonId == null) {
			log.error("Unable to locate one of the two patient records that need to be unmerged.");
			throw new RegistryPatientException("Unable to identify the two patient records that need to be unmerged.");
		}
		if(retiredPersonId.getSurvivingPatientId().equals(survivingPersonId.getPatientId())){
			retiredPersonId.setSurvivingPatientId("");
			retiredPersonId.setMerge("N");
		}else{
			log.error("Unable to unmerge the patient because surviving_patient_id of merge patient is not matched with surviving patient");
			throw new RegistryPatientException("Unable to unmerge the patient because surviving_patient_id of merge patient is not matched with surviving patient");
		}
		try {
			xdsRegistryPatientDao.mergePersonIdentifier(retiredPersonId);
		} catch (Exception e) {
			log.error("Failed while trying to unmerge two patient records in the patient registry." + e, e);
			throw new RegistryPatientException(e.getMessage());
		}
		}
	}

	public XdsRegistryPatientDao getXdsRegistryPatientDao() {
		return xdsRegistryPatientDao;
	}

	public void setXdsRegistryPatientDao(XdsRegistryPatientDao xdsRegistryPatientDao) {
		this.xdsRegistryPatientDao = xdsRegistryPatientDao;
	}
	public static PersonIdentifier getPersonIdentifier(PatientIdentifier patientIdentifier,boolean deleted) {
		PersonIdentifier pi = getPersonIdentifier(patientIdentifier);		
		pi.setDelete(deleted ? "Y" : "N");
		pi.setMerge("N");
		return pi;
	}
	public static PersonIdentifier getPersonIdentifier(PatientIdentifier patientIdentifier){
		PersonIdentifier pi = new PersonIdentifier();
		pi.setPatientId(patientIdentifier.getId());
		String assignAuth = getAssigningAuthority(patientIdentifier);
		pi.setAssigningAuthority(assignAuth);
		return pi;		
	}
	public static PersonIdentifier getPersonIdentifier(PersonIdentifier pi, PatientIdentifier patientIdentifier,boolean deleted) {
		pi.setPatientId(patientIdentifier.getId());
		String assignAuth = getAssigningAuthority(patientIdentifier);
		pi.setAssigningAuthority(assignAuth);	
		pi.setDelete(deleted ? "Y" : "N");
		return pi;
	}
	
	/**
	 * @param patientIdentifier The patient identifier object.
	 * @return The assigning authority for this patient identifier, formatted as a String
	 */
	private static String getAssigningAuthority(PatientIdentifier patientIdentifier){
		 if (patientIdentifier.getAssigningAuthority() == null)
		 {
			 return null;
		 }
		 String assignFacNam = patientIdentifier.getAssigningAuthority().getNamespaceId();
		 String assignFacUniversal = patientIdentifier.getAssigningAuthority().getUniversalId();
		 String assignFacUniversaltype = patientIdentifier.getAssigningAuthority().getUniversalIdType();
		 if(assignFacNam != null && assignFacUniversal != null && assignFacUniversaltype !=null)
			 return assignFacNam +"&"+ assignFacUniversal + "&"+ assignFacUniversaltype;
		 else if(assignFacNam == null && assignFacUniversal != null && assignFacUniversaltype !=null){
			 return  "&"+ assignFacUniversal + "&"+ assignFacUniversaltype;
		 }else if(assignFacNam != null && assignFacUniversaltype == null){
			 return  assignFacNam + "&"+ assignFacUniversal + "&";
		 }
		return null;
	 }
	
}
