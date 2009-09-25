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

import static org.junit.Assert.assertNotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import org.junit.Before;
import org.junit.Test;
import org.openhealthexchange.common.configuration.ModuleManager;
import org.openhealthexchange.openxds.repository.api.IXdsRepositoryItem;
import org.openhealthexchange.openxds.repository.api.IXdsRepositoryManager;
import org.openhealthexchange.openxds.repository.api.RepositoryRequestContext;
import com.misyshealthcare.connect.net.ConnectionFactory;
import com.misyshealthcare.connect.net.IConnectionDescription;

/**
 * This class is used to test the relational database repository manager implementation.
 *  
 * @author <a href="mailto:Rasakannu.Palaniyandi@misys.com">Raja</a>
 *
 */
public class HibernateRepositoryManagerTest {
	private static File content1K;
    private IXdsRepositoryManager repositoryManager;
	private RepositoryRequestContext requestContext = new RepositoryRequestContext();
    private static final String id = Utility.getInstance().createId();
    String documentId = Utility.getInstance().stripId(id);
    private IConnectionDescription connection = null;
 
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		try{
		 repositoryManager =(IXdsRepositoryManager)ModuleManager.getInstance().getBean("repositoryManager");
		}catch (Exception e) {
			System.out.println(e);
		}
		   ConnectionFactory.loadConnectionDescriptionsFromFile(FileSystemRepositoryTest.class.getResource("XdsRepositoryConnectionsTest.xml").getPath());
		   connection = ConnectionFactory.getConnectionDescription("xds-repository");
		   requestContext.setConnection(connection);
	       char content1KArray[] = new char[1024]; //1Kb
		   Arrays.fill(content1KArray, 'a');
		   content1K = createTempFile(true, new String(content1KArray));  
		      
	}
	
	/**
	 * Test method for {@link org.openhealthexchange.openxds.repository.service.XdsRepositoryManagerImpl#insert(org.openhealthexchange.openxds.repository.api.IXdsRepositoryItem, org.openhealthexchange.openxds.repository.api.RepositoryRequestContext)}.
	 */
	@Test
	public void testInsertIXdsRepositoryItemRepositoryRequestContext() {
		try {
		
			IXdsRepositoryItem ro = createRepositoryItem(id, content1K);
			repositoryManager.insert(ro, requestContext);				
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	
	/**
	 * Test method for {@link org.openhealthexchange.openxds.repository.service.XdsRepositoryManagerImpl#getRepositoryItem(java.lang.String, org.openhealthexchange.openxds.repository.api.RepositoryRequestContext)}.
	 */
	@Test
	public void testGetRepositoryItem() {
		try{
		IXdsRepositoryItem repositoryItem = repositoryManager.getRepositoryItem(documentId, requestContext);
		assertNotNull(repositoryItem);
		}catch (Exception e) {
		   System.out.println("" +e);
		}
	}
	/**
	 * Test method for {@link org.openhealthexchange.openxds.repository.service.XdsRepositoryManagerImpl#delete(java.lang.String, org.openhealthexchange.openxds.repository.api.RepositoryRequestContext)}.
	 */
	@Test
	public void testDeleteStringRepositoryRequestContext() {
		try{
			repositoryManager.delete("58a2562c-0dcb-45c4-9223-b817bf095f92", requestContext);
		}catch (Exception e) {
			
		}
	}
	
	private static File createTempFile(boolean deleteOnExit, String content) throws IOException {
        // Create temp file.
        File temp = File.createTempFile("omar", ".txt");
        // Delete temp file when program exits.
        if (deleteOnExit) {
            temp.deleteOnExit();
        }        
        // Write to temp file
        BufferedWriter out = new BufferedWriter(new FileWriter(temp));
        out.write(content);
        out.close();
        
        return temp;
    }
	
	 private IXdsRepositoryItem createRepositoryItem(String id, File content) throws Exception {    	
	        DataHandler contentDataHandler = new DataHandler(new FileDataSource(content));
	        IXdsRepositoryItem repositoryItem = new XdsRepositoryItem(id, contentDataHandler);
	        return repositoryItem;
	        
	    }
	
}
