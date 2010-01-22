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

package org.openhealthtools.openxds.repository.api;

import java.util.List;

/**
 * This interface defines the operations of an XDS Repository Manager.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public interface XdsRepositoryService {
	
	/**
	 * Get the repository unique Id that represents this Repository.
	 * 
	 * @return a unique string Id
	 */
	public String getRepositoryUniqueId();  
	
		
    /**
     * Inserts an {@link XdsRepositoryItem}.
     *
     * @param item the {@link XdsRepositoryItem} to be inserted
     * @param context the {@link RepositoryRequestContext} associated with this request.
     * 
     * @throws RepositoryException if failed to insert
     */
    public void insert(XdsRepositoryItem item, RepositoryRequestContext context) throws RepositoryException;

    /**
     * Inserts a list of {@link XdsRepositoryItem}s. This is possible when a 
     * submission set includes multiple documents. All the repository item will
     * be insert in an atomic mode, either all will be inserted or non will be
     * inserted.
     *
     * @param items a List of {@link XdsRepositoryItem}s to be inserted 
     * @param context the {@link RepositoryRequestContext} associated with this request.
     * 
     * @throws RepositoryException if failed to insert any repository item
     */
    public void insert(List<XdsRepositoryItem> items, RepositoryRequestContext context) throws RepositoryException;

    /**
     * Returns the {@link XdsRepositoryItem} associated with the given document unique id.
     *
     * @param documentUniqueId the document unique id whose {@link XdsRepositoryItem} item is desired
     * @param context the {@link RepositoryRequestContext} associated with this request.
     * 
     * @return the {@link XdsRepositoryItem} instance that matches specified document uniqueId
     * 
     * @throws RegistryException if there are any processing errors getting repository item
     */
    public XdsRepositoryItem getRepositoryItem(String documentUniqueId, RepositoryRequestContext context) throws RepositoryException;

    /**
     * Returns the List of {@link XdsRepositoryItem} associated with the given document unique ids.
     * The IHE XDS transaction ITI-43 allows for multiple document retrieval.
     * 
     * @param documentUniqueIds the document unique id whose {@link XdsRepositoryItem} item are desired
     * @param context the {@link RepositoryRequestContext} associated with this request.
     * 
     * @return the List of {@link XdsRepositoryItem} instances that match specified ids
     * 
     * @throws RegistryException if there are any processing errors getting repository items
     */
    public List<XdsRepositoryItem> getRepositoryItems(List<String> documentUniqueIds, RepositoryRequestContext context) throws RepositoryException;

    /**
     * Deletes the repository item associated with the given document unique id.
     *
     * @param documentUniqueId the document unique id whose repository item is to be deleted
     * @param context the {@link RepositoryRequestContext} associated with this request.
     * 
     * @throws RepositoryException if the item does not exist or fails to delete
     */
    public void delete(String documentUniqueId, RepositoryRequestContext context) throws RepositoryException;
    
    /**
     * Deletes multiple repository items.
     * 
     * @param ids a List of document unique ids whose repository items are to be deleted
     * @param context the {@link RepositoryRequestContext} associated with this request.
     * 
     * @throws RepositoryException if any of the item do not exist or fails to delete
     */
    public void delete(List<String> ids, RepositoryRequestContext context) throws RepositoryException;

}
