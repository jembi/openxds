/**
 *  Copyright (c) 2009-2011 Misys Open Source Solutions (MOSS) and others
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

import javax.activation.DataHandler;
import org.apache.axiom.om.OMElement;

/**
 * This class represents a document or repository item in the 
 * XDS Repository.
 *  
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public interface XdsRepositoryItem {

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
     * Gets the Sha1 hash code of this repository item.
     * 
     * @return the Sha1 hash code
     * @throw RepositoryException if there is any problem to calculate the hashcode
     */
    public String getHash() throws RepositoryException;

    /**
     * Gets the mimeType of this repository item.
     * 
     * @return the mimeType of the repository item.
     */
    public String getMimeType();

    /**
     * Sets the mimeType of this repository item.
     * 
     * @param the mimeType of this repository item.
     */
    public void setMimeType(String mimeType);

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
    
    /**
     * Gets the document extrinsic object.
     * The extrinsic object is only available during provide and register
     * 
     * @return the extrinsic object
     */
    public OMElement getExtrinsicObject();
        
    /**
     * Sets the extrinsic object.
     * The extrinsic object is only needed during provide and register
     * 
     * @param extrinsic object of the document to be set
     */
    public void setExtrinsicObject(OMElement metadata);
    
}
