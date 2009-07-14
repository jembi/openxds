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



import java.io.InputStream;
import javax.activation.DataHandler;
import org.apache.log4j.Logger;
import org.openhealthexchange.openxds.repository.api.IXdsRepositoryItem;
import org.openhealthexchange.openxds.repository.api.RepositoryException;

/**
 * This class represents a repository item which includes the content
 * of a document.  
 *  
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 *
 */
public class XdsRepositoryItem implements IXdsRepositoryItem {

	private static final Logger LOG = Logger.getLogger(XdsRepositoryItem.class);

    private String id;
    private DataHandler handler;
    
    public XdsRepositoryItem(){   	
    }
	    
    /**
    * Constructor.
    */
    public XdsRepositoryItem(String id, DataHandler handler) {
	        this.id = id;
	        this.handler = handler;
	    }
	/* (non-Javadoc)
	 * @see org.openhealthexchange.openxds.repository.api.IXdsRepositoryItem#getDataHandler()
	 */
	public DataHandler getDataHandler() {
		return handler;
	}

	/* (non-Javadoc)
	 * @see org.openhealthexchange.openxds.repository.api.IXdsRepositoryItem#setDataHandler(DataHandler)
	 */
	public void setDataHandler(DataHandler dataHandler) {
		this.handler = dataHandler;
		
	}

	/* (non-Javadoc)
	 * @see org.openhealthexchange.openxds.repository.api.IXdsRepositoryItem#getDocumentUniqueId()
	 */
	public String getDocumentUniqueId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.openhealthexchange.openxds.repository.api.IXdsRepositoryItem#getHash()
	 */
	public long getHash() throws RepositoryException {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((handler == null) ? 0 : handler.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see org.openhealthexchange.openxds.repository.api.IXdsRepositoryItem#getRepositoryUniqueID()
	 */
	public String getRepositoryUniqueID() throws RepositoryException {
		return null;//Repository.getRepositoryUniqueId();
	}

	/* (non-Javadoc)
	 * @see org.openhealthexchange.openxds.repository.api.IXdsRepositoryItem#getSize()
	 */
	public int getSize() throws RepositoryException {
        int size = 0;
		try {
			InputStream is = handler.getInputStream();
	        while (is.read() != -1) {
	            size++;
	        }
		} catch (Exception e) {
			throw new RepositoryException(e);
		}		
		return size;
	}

	/* (non-Javadoc)
	 * @see org.openhealthexchange.openxds.repository.api.IXdsRepositoryItem#setDocumentUniqueId(java.lang.String)
	 */
	public void setDocumentUniqueId(String uniqueId) {
		this.id =uniqueId;
	}

}
