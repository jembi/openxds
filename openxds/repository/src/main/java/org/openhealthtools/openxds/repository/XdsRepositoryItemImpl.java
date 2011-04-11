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

package org.openhealthtools.openxds.repository;



import java.io.InputStream;
import java.security.MessageDigest;

import javax.activation.DataHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openxds.repository.api.RepositoryException;
import org.openhealthtools.openxds.repository.api.XdsRepositoryItem;

/**
 * This class represents a repository item which includes the content
 * of a document.  
 *  
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 *
 */
public class XdsRepositoryItemImpl implements XdsRepositoryItem {

	private static final Log log = LogFactory.getLog(XdsRepositoryItemImpl.class);

    private String id;
    private DataHandler handler;
    private String mimeType;
    
    public XdsRepositoryItemImpl(){   	
    }
	    
    /**
    * Constructor.
    */
    public XdsRepositoryItemImpl(String id, DataHandler handler) {
	        this.id = id;
	        this.handler = handler;
	    }
	/* (non-Javadoc)
	 * @see org.openhealthtools.openxds.repository.api.IXdsRepositoryItem#getDataHandler()
	 */
	public DataHandler getDataHandler() {
		return handler;
	}

	/* (non-Javadoc)
	 * @see org.openhealthtools.openxds.repository.api.IXdsRepositoryItem#setDataHandler(DataHandler)
	 */
	public void setDataHandler(DataHandler dataHandler) {
		this.handler = dataHandler;
		
	}

	/* (non-Javadoc)
	 * @see org.openhealthtools.openxds.repository.api.IXdsRepositoryItem#getDocumentUniqueId()
	 */
	public String getDocumentUniqueId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.openhealthtools.openxds.repository.api.XdsRepositoryItem#getHash()
	 */
	public String getHash() throws RepositoryException {
		try {
			return getSha1String();
		}catch(Exception e) {
			throw new RepositoryException( "Fail to get hash - " + e.getMessage(), e);
		}
	}

    /**
     * Getter for property sha1.
     * @return Value of property sha1.
     */
    private byte[] getSha1() throws Exception {
		InputStream is = this.getDataHandler().getInputStream();
		byte[] inb = new byte[this.getSize()];
		is.read(inb);
		
        MessageDigest md = MessageDigest.getInstance("SHA1"); 
        byte[] result = md.digest(inb);
        return result;
    }
    
    /**
     * Getter for property sha1String.
     * @return Value of property sha1String.
     */
    private String getSha1String() throws Exception {
        byte[] sha1 = getSha1();
        StringBuffer buf = new StringBuffer();
        for(int i=0; i<sha1.length; i++) {
            String h = Integer.toHexString(sha1[i] & 0xff);
            if(h.length() == 1) h = "0" + h;
            buf.append(h);
        }
        return new String(buf);
    }
    
	/* (non-Javadoc)
	 * @see org.openhealthtools.openxds.repository.api.IXdsRepositoryItem#getRepositoryUniqueID()
	 */
	public String getRepositoryUniqueID() throws RepositoryException {
		return null;//Repository.getRepositoryUniqueId();
	}

	/* (non-Javadoc)
	 * @see org.openhealthtools.openxds.repository.api.IXdsRepositoryItem#getSize()
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
	 * @see org.openhealthtools.openxds.repository.api.IXdsRepositoryItem#setDocumentUniqueId(java.lang.String)
	 */
	public void setDocumentUniqueId(String uniqueId) {
		this.id =uniqueId;
	}

	/**
	 * @return the mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * @param mimeType the mimeType to set
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
	

}
