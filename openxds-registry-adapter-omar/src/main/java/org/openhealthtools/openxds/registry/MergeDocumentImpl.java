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
package org.openhealthtools.openxds.registry;

import org.apache.log4j.Logger;
import org.openhealthtools.openxds.registry.api.InvalidPatientException;
import org.openhealthtools.openxds.registry.api.MergeDocument;
import org.openhealthtools.openxds.registry.dao.MergeDocumentDao;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
/**
 * This class defines the operations to merge XDS documents,submissionSet and folders. 
 *
 * @author <a href="mailto:Rasakannu.Palaniyandi@misys.com">Raja</a>
 * 
 */
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MergeDocumentImpl implements MergeDocument {
	private static final Logger LOG = Logger.getLogger(MergeDocumentImpl.class);
    public MergeDocumentDao mergeDocumentDao;
    
    /* (non-Javadoc)
	 * @see org.openhealthtools.openxds.registry.MergeDocument#mergedocuments()
	 */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
	public void mergedocuments(String survivingPatient, String mergePatient) throws InvalidPatientException {
		System.out.println("merge Patient");
		boolean flag = false;
		try{
		    flag = mergeDocumentDao.isValidPatient(survivingPatient);
		    if(!flag){
		    	LOG.debug("surviving patient is not available in registry");
		    	throw new InvalidPatientException(
						"surviving patient is not available in registry");
		    }	
		    flag = mergeDocumentDao.isValidPatient(mergePatient);
		    if(!flag){
		    	LOG.debug("merge patient is not available in registry");
		    	throw new InvalidPatientException(
				"merge patient is not available in registry");
		    }
		    mergeDocumentDao.mergeDocument(survivingPatient, mergePatient);
		}catch (Exception e) {
			LOG.debug("error while merging document in XDS Regsitry");
			throw new InvalidPatientException(e);
		}
	}

	public MergeDocumentDao getMergeDocumentDao() {
		return mergeDocumentDao;
	}

	public void setMergeDocumentDao(MergeDocumentDao mergeDocumentDao) {
		this.mergeDocumentDao = mergeDocumentDao;
	}

}
