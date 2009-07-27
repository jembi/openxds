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

package org.openhealthexchange.openxds.repository;

import java.io.File;
import java.io.FileOutputStream;
import java.net.ConnectException;
import java.util.Iterator;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.log4j.Logger;
import org.openhealthexchange.openxds.repository.api.IXdsRepositoryItem;
import org.openhealthexchange.openxds.repository.api.IXdsRepositoryManager;
import org.openhealthexchange.openxds.repository.api.RepositoryException;
import org.openhealthexchange.openxds.repository.api.RepositoryRequestContext;

import com.misyshealthcare.connect.net.CodeSet;
import com.misyshealthcare.connect.net.IConnectionDescription;

/**
 * This class provides a file system based repository manager implementation.
 *  
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 * @author <a href="mailto:Rasakannu.Palaniyandi@misys.com">Raja</a>
 * 
 */
public class FileSystemRepositoryManager implements IXdsRepositoryManager {
	private static final Logger LOG = Logger
			.getLogger(FileSystemRepositoryManager.class);
	
	/**The repository root folder*/
	private String repositoryRoot = null;
    
	/**The repository unique Id that represents this Repository.*/
	private String repositoryUniqueId = null;
	
	/**
	 * Gets the repository unique Id that represents this repository.
	 * 
	 * @return the repository unique Id
	 */
	public String getRepositoryUniqueId() {
		return this.repositoryUniqueId;
	}

	/**
	 * Sets the repository unique id that represents this repository.
	 * 
	 * @param repositoryUniqueId the repository unique Id to be set.
	 */
	public void setRepositoryUniqueId(String repositoryUniqueId) {
		this.repositoryUniqueId = repositoryUniqueId;
	}
	
	/**
	 * Gets the root folder of this repository.
	 * 
	 * @return repositoryRoot folder
	 */
	
	public String getRepositoryRoot() {
		return repositoryRoot;
	}
	/**
	 * Sets the root folder of this repository.
	 * 
	 * @param repositoryRoot the root folder
	 */
	public void setRepositoryRoot(String repositoryRoot) {
		this.repositoryRoot = repositoryRoot;
	}
	
	/* (non-Javadoc)
	 * @see org.openhealthexchange.openxds.repository.api.IXdsRepositoryManager#insert()
	 */
	public void insert(IXdsRepositoryItem item, RepositoryRequestContext context)
			throws RepositoryException {
		IConnectionDescription connection = context.getConnection();
		CodeSet mimeTypeCodeSet = connection.getCodeSet("mimeType");		
		if (mimeTypeCodeSet == null) throw new RepositoryException("Configuration Error: Cannot find mime type table");
		
		try {
			String id = item.getDocumentUniqueId();
    		String mimeTypeCode = item.getDataHandler().getContentType();
            String ext = mimeTypeCodeSet.getExt(mimeTypeCode);

            if(id == null || ext == null)
            	throw new RepositoryException("Invalid document");
			// Strip off the "urn:uuid:"
			id = Utility.getInstance().stripId(id);

			String itemPath = getRepositoryItemPath(id,ext);

			LOG.debug("itemPath = " + itemPath);

			File itemFile = new File(itemPath);

			if (itemFile.exists()) {
				String errmsg = "RepositoryItem does not exist: id" + id;
				LOG.error(errmsg);
				throw new RepositoryException(errmsg);
			}
			itemFile.createNewFile();
			//Writing out the RepositoryItem itself			
			FileOutputStream fos = new FileOutputStream(itemPath);
			item.getDataHandler().writeTo(fos);
			fos.flush();
			fos.close();

		} catch (Exception e) {
			LOG.error(e);
			throw new RepositoryException(e);
		}

	}

	/* (non-Javadoc)
	 * @see org.openhealthexchange.openxds.repository.api.IXdsRepositoryManager#insert()
	 */
	public void insert(List<IXdsRepositoryItem> items,
			RepositoryRequestContext context) throws RepositoryException {

		try {
			if (items != null) {
				Iterator<IXdsRepositoryItem> item = items.iterator();
				while (item.hasNext()) {
					IXdsRepositoryItem repositoryItem = item.next();
					insert(repositoryItem, context);
				}
			}
		} catch (Exception e) {
			LOG.error(e);
			throw new RepositoryException(e);
		}

	}

	/* (non-Javadoc)
	 * @see org.openhealthexchange.openxds.repository.api.IXdsRepositoryManager#getRepositoryItem()
	 */
	public IXdsRepositoryItem getRepositoryItem(String documentUniqueId)
			throws RepositoryException {
		IXdsRepositoryItem repositoryItem = null;
		// Strip off the "urn:uuid:"
		documentUniqueId = Utility.getInstance().stripId(documentUniqueId);		
		try {
			String path = getRepositoryItemPath(documentUniqueId);
			File riFile = new File(path);

			if (!riFile.exists()) {
				String errmsg = "Document ID is not exist in repository";
				LOG.error(errmsg);
				throw new RepositoryException(errmsg);
			}

			DataHandler contentDataHandler = new DataHandler(
					new FileDataSource(riFile));
			
			repositoryItem = new XdsRepositoryItem(documentUniqueId,
					contentDataHandler);

		} catch (Exception e) {
			throw new RepositoryException(e);
		}
		return repositoryItem;

	}

	/* (non-Javadoc)
	 * @see org.openhealthexchange.openxds.repository.api.IXdsRepositoryManager#getRepositoryItem()
	 */
	public List<IXdsRepositoryItem> getRepositoryItems(
			List<String> documentUniqueIds) throws RepositoryException {
		List<IXdsRepositoryItem> repositoryItems = null;
		try {
			if (documentUniqueIds != null) {
				Iterator<String> item = documentUniqueIds.iterator();
				while (item.hasNext()) {
					String repositoryItem = item.next();
					IXdsRepositoryItem xdsRepositoryItem = getRepositoryItem(repositoryItem);
					if (xdsRepositoryItem != null)
						repositoryItems.add(xdsRepositoryItem);
				}

			}
		} catch (Exception e) {
			LOG.error(e);
			throw new RepositoryException(e);
		}
		return repositoryItems;
	}

	/* (non-Javadoc)
	 * @see org.openhealthexchange.openxds.repository.api.IXdsRepositoryManager#delete()
	 */
	public void delete(String documentUniqueId) throws RepositoryException {
		String id = Utility.getInstance().stripId(documentUniqueId);

		String path = getRepositoryItemPath(documentUniqueId);

		LOG.debug("path = " + path);

		//RepositoryItem file
		File riFile = new File(path);

		if (!riFile.exists()) {
			String errmsg = "Document id is not existed";
			LOG.error(errmsg);
			throw new RepositoryException(errmsg);
		}

		boolean deletedOK = riFile.delete();

		if (deletedOK) {
			String msg = "deleted OK";
			LOG.debug(msg);
		} else {
			String msg = null;
			LOG.error(msg);
		}
	}

	/* (non-Javadoc)
	 * @see org.openhealthexchange.openxds.repository.api.IXdsRepositoryManager#delete()
	 */
	public void delete(List<String> ids) throws RepositoryException {
		try {
			if (ids != null) {
				Iterator<String> item = ids.iterator();
				while (item.hasNext()) {
					String repositoryItem = item.next();
					delete(repositoryItem);
				}
			}
		} catch (Exception e) {
			LOG.error(e);
			throw new RepositoryException(e);
		}
	}

	/**
	 * Gets the path for a RepositoryItem given its id.
	 * @param id - Document Id
	 * @param ext - Extension of document	 
	 */
	private String getRepositoryItemPath(String id,String ext){
		//Strip urn:uuid since that is not part of file name
		id = Utility.getInstance().stripId(id);
		File file = new File(repositoryRoot);
		if (!file.exists())
			file.mkdirs();
		String repositoryItemPath = repositoryRoot + "/" + id + "." +ext;
		return repositoryItemPath;
	}
	
	
	/**
	 * Gets the path for a RepositoryItem given its id.
	 * @param id Document ID
	 * @throws RepositoryException.
	 */
	private String getRepositoryItemPath(String id) throws RepositoryException {
		String ext = null;
		//Strip urn:uuid since that is not part of file name
		id = Utility.getInstance().stripId(id);
		File file = new File(repositoryRoot);
		if (!file.exists())
			file.mkdirs();
		String[] files =  file.list();
		for (int i = 0; i < files.length; i++) {
			if(files[i].startsWith(id)){			
			ext =files[i].substring(files[i].length()-4);
			}
		}
		if(ext == null){
			throw new RepositoryException("DocumentId is not found in the repository");
		}
		String repositoryItemPath = repositoryRoot + "/" + id + ext;
		return repositoryItemPath;
	}

	
	
}
