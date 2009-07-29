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
package org.openhealthexchange.openxds.repository.api;

import javax.activation.DataHandler;

/**
 * This class represents a document or repository item in the 
 * XDS Repository.
 *  
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public interface IXdsRepositoryItem {

	/**
	 * Gets the document unique Id.
	 * 
	 * @return the document unique Id
	 */
	public String getDocumentUniqueId();
    
	/**
	 * Sets the document unique Id.
	 * 
	 * @param uniqueId the document unique Id to be set
	 */
    public void setDocumentUniqueId(String uniqueId);
    
    /**
     * Gets the size of this repository item in bytes.
     * 
     * @return the size of the repository item in bytes
     * @throw RepositoryException if there is any problem to get the size
     */
    public int getSize() throws RepositoryException;

    /**
     * Gets the hash code of this repository item.
     * 
     * @return the hash code
     * @throw RepositoryException if there is any problem to calculate the hashcode
     */
    public long getHash() throws RepositoryException;

    /**
     * Gets the mimeType of this repository item.
     * 
     * @return the mimeType of the repository item.
     * @throw RepositoryException if there is any problem to get the mimeType
     */
    public String getMimeType();

    /**
     * Gets the unique id of the repository that this repository 
     * item is in.
     * 
     * @return the repository unique id.
     * @throws RepositoryException if there is any problem to get the 
     * 		repository unique id.
     */
    public String getRepositoryUniqueID() throws RepositoryException;

    /**
     * Gets the document content in the format of <code>DataHandler</code>.
     * 
     * @return the <code>DataHandler</code>.
     */
    public DataHandler getDataHandler();
            
    /**
     * Sets the document content in the format of <code>DataHandler</code>.
     * 
     * @param dataHandler the <code>DataHandler</code>.
     */
    public void setDataHandler(DataHandler dataHandler);
}
