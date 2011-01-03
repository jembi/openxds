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

package org.openhealthtools.openxds.repository.filesystem;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.actorconfig.IActorDescription;
import org.openhealthtools.openexchange.actorconfig.net.CodeSet;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.utils.Pair;
import org.openhealthtools.openxds.repository.Utility;
import org.openhealthtools.openxds.repository.XdsRepositoryItemImpl;
import org.openhealthtools.openxds.repository.api.RepositoryException;
import org.openhealthtools.openxds.repository.api.RepositoryRequestContext;
import org.openhealthtools.openxds.repository.api.XdsRepositoryItem;
import org.openhealthtools.openxds.repository.api.XdsRepositoryService;


/**
 * This class provides a file system based repository manager implementation.
 *  
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 * @author <a href="mailto:Rasakannu.Palaniyandi@misys.com">Raja</a>
 * 
 */
public class FileSystemRepositoryServiceImpl implements XdsRepositoryService {
	private static final Log log = LogFactory
			.getLog(FileSystemRepositoryServiceImpl.class);
	
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
	 * @see org.openhealthtools.openxds.repository.api.IXdsRepositoryManager#insert()
	 */
	public void insert(XdsRepositoryItem item, RepositoryRequestContext context)
			throws RepositoryException {
		IActorDescription actorDescription = context.getActorDescription();
		CodeSet mimeTypeCodeSet = actorDescription.getCodeSet("mimeType");		
		if (mimeTypeCodeSet == null) throw new RepositoryException("Configuration Error: Cannot find mime type table");
		
		try {
			String id = item.getDocumentUniqueId();
    		String mimeTypeCode = item.getDataHandler().getContentType();
            String ext = mimeTypeCodeSet.getExt(mimeTypeCode, null);

            if(id == null || ext == null)
            	throw new RepositoryException("Invalid document");
			// Strip off the "urn:uuid:"
			id = Utility.getInstance().stripId(id);

			String itemPath = getRepositoryItemPath(id,ext);

			log.debug("itemPath = " + itemPath);

			File itemFile = new File(itemPath);

			if (itemFile.exists()) {
				String errmsg = "RepositoryItem already exist, id=" + id;
				log.error(errmsg);
				throw new RepositoryException(errmsg);
			}
			itemFile.createNewFile();
			//Writing out the RepositoryItem itself			
			FileOutputStream fos = new FileOutputStream(itemPath);
			item.getDataHandler().writeTo(fos);
			fos.flush();
			fos.close();

		} catch (Exception e) {
			log.error(e);
			throw new RepositoryException(e);
		}

	}

	/* (non-Javadoc)
	 * @see org.openhealthtools.openxds.repository.api.IXdsRepositoryManager#insert()
	 */
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
	 * @see org.openhealthtools.openxds.repository.api.IXdsRepositoryManager#getRepositoryItem()
	 */
	public XdsRepositoryItem getRepositoryItem(String documentUniqueId, RepositoryRequestContext context)
			throws RepositoryException {
		XdsRepositoryItemImpl repositoryItem = null;
		// Strip off the "urn:uuid:"
		documentUniqueId = Utility.getInstance().stripId(documentUniqueId);		
		try {
			IActorDescription actorDescription = context.getActorDescription();
			if (actorDescription == null) {
				throw new RepositoryException("Missing required ActorDescription in RepositoryRequestContext");
			}
			
			CodeSet mimeTypeCodeSet = actorDescription.getCodeSet("mimeType");
			Set<Pair<String,String>> mimeTypes = mimeTypeCodeSet.getCodeSetKeys();
			String targetFileMimeType = null;
			File targetFile = null;
			
			//look up the file
			for (Pair<String,String> code : mimeTypes) {
				String mimeType = code.first;
				String ext = mimeTypeCodeSet.getExt(mimeType, null);
				File file = new File(repositoryRoot, documentUniqueId + "." + ext);
				if (file.exists()) {
					targetFileMimeType = mimeType;
					targetFile = file;
					if (log.isDebugEnabled())
						log.debug("Repository File " + file.getPath() + " found");
				} else
					if (log.isDebugEnabled())
						log.debug("Repository File " + file.getPath() + " does not exist");
			}

			if (targetFile == null) {
				return null;
			}

			DataHandler contentDataHandler = new DataHandler(
					new FileDataSource(targetFile));
			repositoryItem = new XdsRepositoryItemImpl(documentUniqueId,
					contentDataHandler);
			repositoryItem.setMimeType(targetFileMimeType);

		} catch (Exception e) {
			throw new RepositoryException(e);
		}
		return repositoryItem;

	}

	/* (non-Javadoc)
	 * @see org.openhealthtools.openxds.repository.api.IXdsRepositoryManager#getRepositoryItem()
	 */
	public List<XdsRepositoryItem> getRepositoryItems(
			List<String> documentUniqueIds, RepositoryRequestContext context) throws RepositoryException {
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
	 * @see org.openhealthtools.openxds.repository.api.IXdsRepositoryManager#delete()
	 */
	public void delete(String documentUniqueId, RepositoryRequestContext context) throws RepositoryException {
		String id = Utility.getInstance().stripId(documentUniqueId);

		IActorDescription actorDescription = context.getActorDescription();
		if (actorDescription == null) {
			throw new RepositoryException("Missing required ActorDescription in RepositoryRequestContext");
		}

		CodeSet mimeTypeCodeSet = actorDescription.getCodeSet("mimeType");
		Set<Pair<String,String>> mimeTypes = mimeTypeCodeSet.getCodeSetKeys();
		File targetFile = null;
		
		//look up the file
		for (Pair<String,String> code : mimeTypes) {
			String mimeType = code.first;
			String ext = mimeTypeCodeSet.getExt(mimeType, null);
			File file = new File(repositoryRoot, documentUniqueId + "." + ext);
			if (file.exists()) {
				targetFile = file;
				if (log.isDebugEnabled())
					log.debug("Repository File " + file.getPath() + " found");
			} else
				if (log.isDebugEnabled())
					log.debug("Repository File " + file.getPath() + " does not exist");
		}
		
		if (targetFile == null) {
			String errmsg = "The repository does not contain a file with document id:" + documentUniqueId;
			log.error(errmsg);
			throw new RepositoryException(errmsg);
		}

		boolean deletedOK = targetFile.delete();

		if (deletedOK) {
			String msg = "deleted OK";
			log.debug(msg);
		} else {
			String msg = null;
			log.error(msg);
		}
	}

	/* (non-Javadoc)
	 * @see org.openhealthtools.openxds.repository.api.IXdsRepositoryManager#delete()
	 */
	public void delete(List<String> ids, RepositoryRequestContext context) throws RepositoryException {
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
	
}
