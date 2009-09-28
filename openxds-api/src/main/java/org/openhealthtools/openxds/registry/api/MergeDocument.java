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


/**
 * This interface defines the operations to merge XDS documents,submissionSet and folders. 
 *
 * @author <a href="mailto:Rasakannu.Palaniyandi@misys.com">Raja</a>
 * 
 */
public interface MergeDocument {
	
	/*
	 * Merges two patients together because they have been found to be
	 * the same patient.  The first argument describes the surviving patient 
	 * demographics; the second argument represents the patient to be merged
	 * with the surviving patient. After successful merge, the registry 
	 * objects such as ExtrinsicObject and RegistryPackage of the merge patient
	 * will be moved to the surviving patient.
	 * 
	 * 
	 * @param survivingPatient the surviving patient id
	 * @param mergePatient the id of patient to be replaced, and merged with the surviving patient
	 * @InvalidPatientException
	 */
	public void mergedocuments(String survivingPatient,String mergePatient) throws InvalidPatientException;

}
