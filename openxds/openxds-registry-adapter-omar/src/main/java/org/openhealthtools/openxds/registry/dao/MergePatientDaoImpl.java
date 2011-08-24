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

package org.openhealthtools.openxds.registry.dao;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openxds.registry.ExternalIdentifier;
import org.openhealthtools.openxds.registry.api.InvalidPatientException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
/**
 * This class defines the operations to merge XDS documents,submissionSet and folders. 
 *
 * @author <a href="mailto:Rasakannu.Palaniyandi@misys.com">Raja</a>
 * 
 */
public class MergePatientDaoImpl extends HibernateDaoSupport implements MergePatientDao {
	private static final Log log = LogFactory.getLog(MergePatientDaoImpl.class);
	
	 /* (non-Javadoc)
	 * @see org.openhealthtools.openxds.registry.dao.MergeDocumentDao#mergedocuments()
	 */
	public void mergeDocument(String survivingPatient, String mergePatient)throws InvalidPatientException { 
		List<ExternalIdentifier> list = null;		
		try{
			list = this.getHibernateTemplate().find(
					"from ExternalIdentifier where value=?", mergePatient);
			}catch (Exception e) {
				log.error("Failed to retrieve external identifier",e);
				throw new InvalidPatientException(e);
			}
		Iterator<ExternalIdentifier> iterator= list.iterator();
		while(iterator.hasNext()){
			ExternalIdentifier identifier =(ExternalIdentifier)iterator.next();
			identifier.setValue(survivingPatient);
			this.getHibernateTemplate().update(identifier);
		}
		log.debug("******************Patient Document is merged succussfully ****************************"); 
	}
}
