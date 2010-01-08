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
package org.openhealthtools.openxds.repository.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.openhealthtools.openxds.repository.Repository;
import org.openhealthtools.openxds.repository.api.RepositoryException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * This class provides a Relational Database XDS repository manager implementation.
 *
 * @author <a href="mailto:Rasakannu.Palaniyandi@misys.com">Raja</a>
 * 
 */
public class XdsRepositoryManagerDaoImpl extends HibernateDaoSupport implements XdsRepositoryManagerDao{
	
	private static final Log log = LogFactory.getLog(XdsRepositoryManagerDaoImpl.class);

	/* (non-Javadoc)
	 * @see org.openhealthtools.openxds.repository.dao.XdsRepositoryManagerDao#insert()
	 */
	public void insert(Repository bean) throws RepositoryException{
		 try {
	        	this.getHibernateTemplate().save(bean);	        	
	        } catch (Exception e){
	        	log.error("Failed to insert Repository bean",e);
	        	throw new RepositoryException(e);
	        }
		}
	
	/* (non-Javadoc)
	 * @see org.openhealthtools.openxds.repository.dao.XdsRepositoryManagerDao#getXdsRepositoryBean()
	 */
	public Repository getXdsRepositoryBean(String documentUniqueId) throws RepositoryException{
		List<?> list = null;
		Repository xdsRepositoryBean = null;
		String parameters[] = {documentUniqueId};
		try{
		list = this.getHibernateTemplate().find(
				"from Repository where documentuniqueid=?", parameters);
		}catch (Exception e) {
			log.error("Failed to retrieve Repository bean from repository",e);
			throw new RepositoryException(e);
		}
	
		if (list.size() > 0)
			xdsRepositoryBean = (Repository) list.get(0);
		return xdsRepositoryBean;		
	}

	/* (non-Javadoc)
	 * @see org.openhealthtools.openxds.repository.dao.XdsRepositoryManagerDao#delete()
	 */
	public void delete(final String documentUniqueId) throws RepositoryException{
		
		try {
			Repository repository = getXdsRepositoryBean(documentUniqueId);
			if(repository != null)
			this.getHibernateTemplate().delete(repository);			
		} catch (Exception e) {
			log.error("Failed to delete Repository bean in repository",e);
			throw new RepositoryException(e);
		}
		
	}
}
