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

package org.openhealthtools.openxds.repository.relationaldb;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import javax.activation.DataHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openxds.repository.ByteArrayDataSource;
import org.openhealthtools.openxds.repository.Repository;
import org.openhealthtools.openxds.repository.Utility;
import org.openhealthtools.openxds.repository.XdsRepositoryItemImpl;
import org.openhealthtools.openxds.repository.api.RepositoryException;
import org.openhealthtools.openxds.repository.api.RepositoryRequestContext;
import org.openhealthtools.openxds.repository.api.XdsRepositoryItem;
import org.openhealthtools.openxds.repository.api.XdsRepositoryService;
import org.openhealthtools.openxds.repository.dao.XdsRepositoryManagerDao;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class provides a xds repository manager service implementation.
 *
 * @author <a href="mailto:Rasakannu.Palaniyandi@misys.com">Raja</a>
 * 
 */
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RelationalDBRepositoryServiceImpl implements XdsRepositoryService {
	private static final Log log = LogFactory.getLog(RelationalDBRepositoryServiceImpl.class);
	
	
	private XdsRepositoryManagerDao xdsRepositoryManagerDao;
	
	private String repositoryUniqueId;
    
	public XdsRepositoryManagerDao getXdsRepositoryManagerDao() {
		return xdsRepositoryManagerDao;
	}
	
	public void setXdsRepositoryManagerDao(
			XdsRepositoryManagerDao xdsRepositoryManagerDao) {
		this.xdsRepositoryManagerDao = xdsRepositoryManagerDao;
	}
	
	public void setRepositoryUniqueId(String repositoryUniqueId) {
		this.repositoryUniqueId = repositoryUniqueId;
	}
	
	public String getRepositoryUniqueId() {		
		return repositoryUniqueId;
	}

	/* (non-Javadoc)
	 * @see org.openhealthtools.openxds.repository.api.IXdsRepositoryManager#getRepositoryItem()
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public XdsRepositoryItem getRepositoryItem(String documentUniqueId,
			RepositoryRequestContext context) throws RepositoryException {
		XdsRepositoryItemImpl repositoryItem = null;
		// Strip off the "urn:uuid:"
		String id = Utility.getInstance().stripId(documentUniqueId);		
		try {
			Repository repository = xdsRepositoryManagerDao.getXdsRepositoryBean(id);
			if(repository != null){
	        DataHandler contentDataHandler = new DataHandler(new ByteArrayDataSource(repository.getBinaryContent(), repository.getMimeType()));
			repositoryItem = new XdsRepositoryItemImpl();
			repositoryItem.setDataHandler(contentDataHandler);
			repositoryItem.setDocumentUniqueId(repository.getDocumentUniqueId());
			repositoryItem.setMimeType(repository.getMimeType());
			}
		} catch (Exception e) {
			log.error(e);
			throw new RepositoryException(e);
		}
		return repositoryItem;
	
	}

	/* (non-Javadoc)
	 * @see org.openhealthtools.openxds.repository.api.IXdsRepositoryManager#getRepositoryItems()
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<XdsRepositoryItem> getRepositoryItems(
			List<String> documentUniqueIds, RepositoryRequestContext context)
			throws RepositoryException {
		List<XdsRepositoryItem> repositoryItems = null;
		try {
			if (documentUniqueIds != null) {
				Iterator<String> item = documentUniqueIds.iterator();
				while (item.hasNext()) {
					String repositoryItem = item.next();
					XdsRepositoryItem xdsRepositoryItem = getRepositoryItem(repositoryItem, context);
					if (xdsRepositoryItem != null)
						repositoryItems.add(xdsRepositoryItem);
				}

			}
		} catch (Exception e) {
			log.error(e);
			throw new RepositoryException(e);
		}
		return repositoryItems;
	}
	
	/* (non-Javadoc)
	 * @see org.openhealthtools.openxds.repository.api.IXdsRepositoryManager#insert()
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void insert(XdsRepositoryItem item, RepositoryRequestContext context)
			throws RepositoryException {
		Repository bean =null;
		byte contentBytes[]= null;
		// get the mime type of the document.
		String mimeTypeCode = item.getMimeType();
		
		// Strip off the "urn:uuid:"
        String id = Utility.getInstance().stripId(item.getDocumentUniqueId());
        
		//check whether document unique id is already exists in repository or not
		bean = xdsRepositoryManagerDao.getXdsRepositoryBean(id); 
		if(bean != null){
		    
			String newHash = item.getHash();
			if (bean.getHash().equals(newHash)) {
				//The same document is saved previously. Allow it to proceed.
				return ;
			}
			log.debug("document unique id already exist");
			throw new RepositoryException("document unique id already exist in repository");
		}
		try {
			contentBytes = readBytes(item.getDataHandler().getInputStream());
		} catch (Exception e) {
			log.error(e);
		    throw new RepositoryException("error while converting datahandler object into byte array");
		}		     
        bean = new Repository();
        bean.setDocumentUniqueId(id);
        bean.setBinaryContent(contentBytes);
        bean.setMimeType(mimeTypeCode);
        bean.setSize(item.getSize());
        bean.setHash(item.getHash());
		xdsRepositoryManagerDao.insert(bean);		
	}
	
	/* (non-Javadoc)
	 * @see org.openhealthtools.openxds.repository.api.IXdsRepositoryManager#insert()
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void insert(List<XdsRepositoryItem> items,
			RepositoryRequestContext context) throws RepositoryException {
		try {
			if (items != null) {
				Iterator<XdsRepositoryItem> item = items.iterator();
				while (item.hasNext()) {
					XdsRepositoryItem repositoryItem = item.next();
					insert(repositoryItem, context);
				}
			}
		} catch (Exception e) {
			log.error(e);
			throw new RepositoryException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.openhealthtools.openxds.repository.api.IXdsRepositoryManager#delete()
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void delete(String documentUniqueId, RepositoryRequestContext context)
			throws RepositoryException {
		// Strip off the "urn:uuid:"
		String id = Utility.getInstance().stripId(documentUniqueId);		
		try {
			xdsRepositoryManagerDao.delete(id);
		 }
		 catch (Exception e) {
			 log.error(e); 
			throw new RepositoryException(e);
		}
		log.debug("Reposiotry bean deleted successfully"); 
				
	}
	/* (non-Javadoc)
	 * @see org.openhealthtools.openxds.repository.api.IXdsRepositoryManager#delete()
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void delete(List<String> ids, RepositoryRequestContext context)
			throws RepositoryException {
		try {
			if (ids != null) {
				Iterator<String> item = ids.iterator();
				while (item.hasNext()) {
					String repositoryItem = item.next();
					delete(repositoryItem, context);
				}
			}
		} catch (Exception e) {
			log.error(e);
			throw new RepositoryException(e);
		}
		
	}
	
	/**
     * Reads bytes from InputStream until the end of the stream.
     *
     * @param in The InputStream to be read.
     * @return the read bytes
     * @throws Exception 
     */
    private byte[] readBytes(InputStream in) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStreamReader inr = new InputStreamReader(in);
        byte bbuf[] = new byte[1024];
        int read;
        while ((read = in.read(bbuf)) > 0) {
            baos.write(bbuf, 0, read);
        }
        return baos.toByteArray();
    }
	
}
