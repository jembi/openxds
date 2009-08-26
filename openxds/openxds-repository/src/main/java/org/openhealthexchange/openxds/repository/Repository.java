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

import java.io.Serializable;

/**
 *
 * @author <a href="mailto:Rasakannu.Palaniyandi@misys.com">Raja</a>
 * 
 */
@SuppressWarnings("serial") 	// Suppress the static serial version - Spring recommended

public class Repository implements Serializable{
    
    private byte binaryContent[];
    private String documentUniqueId;
    private String mimeType;
    
    /** Creates a new instance of RepositoryItemBean */
    public Repository() {
    }
    
    /**
     * Getter for property binaryContent.
     * @return Value of property binaryContent.
     */
    public byte[] getBinaryContent() {
        return this.binaryContent;
    }
    
    /**
     * Setter for property binaryContent.
     * @param binaryContent New value of property binaryContent.
     */
    public void setBinaryContent(byte[] binaryContent) {
        this.binaryContent = binaryContent;
    }
    
    /**
     * Getter for documentUniqueId.
     * @return String.
     */
    public String getDocumentUniqueId() {
        return documentUniqueId;
    }
    
    /**
     * Setter for documentUniqueId.
     * @param documentUniqueId New value of property documentUniqueId.
     */
    public void setDocumentUniqueId(String documentUniqueId) {
        this.documentUniqueId = documentUniqueId;
    }

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}        
}
