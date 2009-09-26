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
package org.openhealthtools.openxds.registry.dao;

import org.openhealthtools.openxds.registry.InvalidPatientException;
/**
 * This interface defines the operations to merge XDS documents,submissionSet and folders. 
 *
 * @author <a href="mailto:Rasakannu.Palaniyandi@misys.com">Raja</a>
 * 
 */
public interface MergeDocumentDao {
	
	
	/*
	 * This method check whether patient is available in Xds registry or not
	 * @Param Patient object
	 * @return Boolean
	 * @throws InvalidPatientException.
	 */
	public boolean isValidPatient(String patient) throws InvalidPatientException;
	
	/*
	 * merge dcouments,submissionSet and folder from mergepatient to surviving patient.
	 * @param surviving patient
	 * @param merge patient.
	 * @throws InvalidPatientException. 
	 * 
	 */
	public void mergeDocument(String survivingPatient,String mergePatient)throws InvalidPatientException;

}
