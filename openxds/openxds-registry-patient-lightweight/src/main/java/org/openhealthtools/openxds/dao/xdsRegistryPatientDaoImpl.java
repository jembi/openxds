/* Copyright 2009 Misys PLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License. 
 */
package org.openhealthtools.openxds.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openxds.registry.PersonIdentifier;
import org.openhealthtools.openxds.registry.api.RegistryPatientException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/* 
* @author <a href="mailto:Rasakannu.Palaniyandi@misys.com">Raja</a>
*/

public class xdsRegistryPatientDaoImpl extends HibernateDaoSupport implements XdsRegistryPatientDao{
	private static final Log log = LogFactory.getLog(xdsRegistryPatientDaoImpl.class);
	

	public PersonIdentifier getPersonById(String personId) throws RegistryPatientException{
		List list = new ArrayList();
		PersonIdentifier personIdentifier = null;
		try{
		list = this.getHibernateTemplate().find(
				"from PersonIdentifier where patient_id = '"+ personId +"' and deleted = 'FALSE' and merged = 'FALSE'");
		}catch (Exception e) {
			log.error("Failed to retrieve person identifier from registry patient service",e);
			throw new RegistryPatientException(e);
		}
	
		if (list.size() > 0)
			personIdentifier = (PersonIdentifier) list.get(0);
		return personIdentifier;
	
	}

	public PersonIdentifier getMergedPersonId(String personId) throws RegistryPatientException{
		List list = new ArrayList();
		PersonIdentifier personIdentifier = null;
		try{
		list = this.getHibernateTemplate().find(
				"from PersonIdentifier where patient_id = '"+ personId +"' and deleted = 'FALSE' and merged = 'TRUE'");
		}catch (Exception e) {
			log.error("Failed to retrieve person identifier from registry patient service",e);
			throw new RegistryPatientException(e);
		}
	
		if (list.size() > 0)
			personIdentifier = (PersonIdentifier) list.get(0);
		return personIdentifier;
	
	}
	public void mergePersonIdentifier(PersonIdentifier mergePersonIdentifier) throws RegistryPatientException{
		try {
			 this.getHibernateTemplate().update(mergePersonIdentifier);
		} catch (Exception e) {
			throw new RegistryPatientException(e);
		}		
	}

	public void savePersonIdentifier(PersonIdentifier identifier) throws RegistryPatientException {
		try {
			  this.getHibernateTemplate().save(identifier);
		} catch (Exception e) {
			throw new RegistryPatientException(e);
		}
		
	}

	public void updatePersonIdentifier(PersonIdentifier identifier) throws RegistryPatientException {
		try {
			 this.getHibernateTemplate().update(identifier);
		} catch (Exception e) {
			throw new RegistryPatientException(e);
		}
		
	}

}
