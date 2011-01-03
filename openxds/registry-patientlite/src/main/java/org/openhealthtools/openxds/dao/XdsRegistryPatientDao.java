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

package org.openhealthtools.openxds.dao;

import org.openhealthtools.openxds.registry.PersonIdentifier;
import org.openhealthtools.openxds.registry.api.RegistryPatientException;
/**
 * This interface defines the operations of Patient Manager in the
 * XDS Registry.
 * 
 * @author <a href="mailto:Rasakannu.Palaniyandi@misys.com">Raja</a>
 */
public interface XdsRegistryPatientDao {
	

	/**
	 * Gets PersonIdentifier of a patient neither deleted nor merged.
	 * 
	 * @param personId
	 * @return the PersonIdentifier
	 * @throws RegistryPatientException
	 */
	public PersonIdentifier getPersonById(PersonIdentifier personId) throws RegistryPatientException;
	
	/**
	 * Gets PersonIdentifier of a patient not deleted, but may or may not merged.
	 * 
	 * @param personId 
	 * @param merged whether the patiented is merged or not
	 * @return the PersonIdentifier
	 * @throws RegistryPatientException
	 */
	public PersonIdentifier getPersonById(PersonIdentifier personId, boolean merged) throws RegistryPatientException;
		
	/**
	 * Saves the PersonIdentifier to the database
	 *  
	 * @param newPersonIdentifier the PersonIdentifier to be saved
	 * @throws RegistryPatientException
	 */
	public void savePersonIdentifier(PersonIdentifier newPersonIdentifier) throws RegistryPatientException;
	

	/**
	 * Updated the PersonIdentifier in the database
	 * 
	 * @param updatePersonIdentifier
	 * @throws RegistryPatientException
	 */
	public void updatePersonIdentifier(PersonIdentifier updatePersonIdentifier) throws RegistryPatientException;
	
}
