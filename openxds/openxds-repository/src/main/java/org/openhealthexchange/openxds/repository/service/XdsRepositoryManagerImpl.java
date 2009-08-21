/**
 *  Copyright © 2009 Misys plc, Sysnet International, Medem and others
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
 *     Misys plc - Initial API and Implementation
 */

package org.openhealthexchange.openxds.repository.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.openhealthexchange.openxds.repository.api.IXdsRepositoryItem;
import org.openhealthexchange.openxds.repository.api.IXdsRepositoryManager;
import org.openhealthexchange.openxds.repository.api.RepositoryException;
import org.openhealthexchange.openxds.repository.api.RepositoryRequestContext;
import org.openhealthexchange.openxds.repository.dao.XdsRepositoryManagerDao;

/**
 * This class provides a xds repository manager service implementation.
 *
 * @author <a href="mailto:Rasakannu.Palaniyandi@misys.com">Raja</a>
 * 
 */

public class XdsRepositoryManagerImpl implements IXdsRepositoryManager {
	private static final Logger LOG = Logger.getLogger(XdsRepositoryManagerImpl.class);
	
	
	private XdsRepositoryManagerDao xdsRepositoryManagerDao;

	/* (non-Javadoc)
	 * @see org.openhealthexchange.openxds.repository.api.IXdsRepositoryManager#delete()
	 */
	public void delete(String documentUniqueId, RepositoryRequestContext context)
			throws RepositoryException {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see org.openhealthexchange.openxds.repository.api.IXdsRepositoryManager#delete()
	 */
	public void delete(List<String> ids, RepositoryRequestContext context)
			throws RepositoryException {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see org.openhealthexchange.openxds.repository.api.IXdsRepositoryManager#getRepositoryItem()
	 */
	public IXdsRepositoryItem getRepositoryItem(String documentUniqueId,
			RepositoryRequestContext context) throws RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openhealthexchange.openxds.repository.api.IXdsRepositoryManager#getRepositoryItems()
	 */
	public List<IXdsRepositoryItem> getRepositoryItems(
			List<String> documentUniqueIds, RepositoryRequestContext context)
			throws RepositoryException {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see org.openhealthexchange.openxds.repository.api.IXdsRepositoryManager#getRepositoryUniqueId()
	 */
	public String getRepositoryUniqueId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.openhealthexchange.openxds.repository.api.IXdsRepositoryManager#insert()
	 */
	public void insert(IXdsRepositoryItem item, RepositoryRequestContext context)
			throws RepositoryException {
		// TODO Auto-generated method stub
		
	}
	
	/* (non-Javadoc)
	 * @see org.openhealthexchange.openxds.repository.api.IXdsRepositoryManager#insert()
	 */
	public void insert(List<IXdsRepositoryItem> items,
			RepositoryRequestContext context) throws RepositoryException {
		// TODO Auto-generated method stub
		
	}
	public XdsRepositoryManagerDao getXdsRepositoryManagerDao() {
		return xdsRepositoryManagerDao;
	}
	public void setXdsRepositoryManagerDao(
			XdsRepositoryManagerDao xdsRepositoryManagerDao) {
		this.xdsRepositoryManagerDao = xdsRepositoryManagerDao;
	}

}
