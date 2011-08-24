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

package org.openhealthtools.openxds.repository.dao;

import org.openhealthtools.openxds.repository.Repository;
import org.openhealthtools.openxds.repository.api.RepositoryException;
/**
 * This class provides a Relational Database XDS repository manager implementation.
 *
 * @author <a href="mailto:Rasakannu.Palaniyandi@misys.com">Raja</a>
 * 
 */
public interface XdsRepositoryManagerDao {


    /**
     * Returns the Repository associated with the given document unique id.
     *
     * @param documentUniqueId the document unique id whose Repository bean is desired     
     * 
     * @return the Repository instance that matches specified document uniqueId
     * 
     * @throws RepositoryException
     */
	
	public Repository getXdsRepositoryBean(String documentUniqueId) throws RepositoryException;
	
	 /**
     * Inserts an Repository object.
     *
     * @param Reposiotry bean to be inserted     
     * 
     * @return void
     * 
     * @throws RepositoryException
     */
	public void insert(Repository bean) throws RepositoryException;
	
	 /**
     * Deletes the repository associated with the given document unique id.
     *
     * @param documentUniqueId the document unique id whose repository item is to be deleted
     * 
     * @return void
     * 
     * @throws RepositoryException
     */
	public void delete(String documentUniqueId) throws RepositoryException;
	
}
