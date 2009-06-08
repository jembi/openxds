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

import java.util.List;

import org.openhealthexchange.openxds.repository.api.IXdsRepositoryItem;
import org.openhealthexchange.openxds.repository.api.IXdsRepositoryManager;
import org.openhealthexchange.openxds.repository.api.RepositoryException;
import org.openhealthexchange.openxds.repository.api.RepositoryRequestContext;

/**
 * This class provides a file system based repository manager implementation.
 *  
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 *
 */
public class FileSystemRepositoryManager implements IXdsRepositoryManager {
    
	public void insert(IXdsRepositoryItem item, RepositoryRequestContext context) throws RepositoryException {
		//TODO:
	}

    public void insert(List<IXdsRepositoryItem> items, RepositoryRequestContext context) throws RepositoryException {
    	//TODO:
    }

    public IXdsRepositoryItem getRepositoryItem(String documentUniqueId) throws RepositoryException {
    	//TODO:
    	return null;
    }

    public List<IXdsRepositoryItem> getRepositoryItems(List<String> documentUniqueIds) throws RepositoryException {
    	//TODO:
    	return null;
    }

    public void delete(String documentUniqueId) throws RepositoryException {
    	//TODO:
    }
    
    public void delete(List<String> ids) throws RepositoryException {
    	//TODO:
    }

}
