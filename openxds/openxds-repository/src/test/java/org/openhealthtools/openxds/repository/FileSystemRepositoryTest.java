/**
 *  Copyright (c) 2009 Misys Open Source Solutions (MOSS) and others
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
 *
 */
package org.openhealthtools.openxds.repository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import junit.framework.TestCase;

import org.openhealthexchange.openpixpdq.ihe.IPixManagerAdapter;
import org.openhealthexchange.openpixpdq.ihe.impl_v2.PixManager;
import org.openhealthtools.common.configuration.ModuleManager;
import org.openhealthtools.openxds.repository.api.IXdsRepositoryItem;
import org.openhealthtools.openxds.repository.api.IXdsRepositoryManager;
import org.openhealthtools.openxds.repository.api.RepositoryRequestContext;

import com.misyshealthcare.connect.net.ConnectionFactory;
import com.misyshealthcare.connect.net.IConnectionDescription;
import com.misyshealthcare.connect.util.OID;

/**
 * This class is used to test the file system based repository manager implementation.
 *  
 * @author <a href="mailto:Rasakannu.Palaniyandi@misys.com">Raja</a>
 *
 */
public class FileSystemRepositoryTest extends TestCase {

   private static File content1K;
   private static File content1M;
   private static File content2M;
   private IXdsRepositoryManager repositoryManager;
 //  private FileSystemRepositoryManager repositoryManager;
   private RepositoryRequestContext requestContext = new RepositoryRequestContext();
   private static final String id = Utility.getInstance().createId();
   String documentId = Utility.getInstance().stripId(id);
   private IConnectionDescription connection = null;
   private IPixManagerAdapter pixAdapter = null;   
	private PixManager actor = null;
   
   protected void setUp() throws Exception {
	   repositoryManager = ModuleManager.getXdsRepositoryManager();	   
	   ConnectionFactory.loadConnectionDescriptionsFromFile(FileSystemRepositoryTest.class.getResource("XdsRepositoryConnectionsTest.xml").getPath());
	   connection = ConnectionFactory.getConnectionDescription("xds-repository");
	   requestContext.setConnection(connection);
       	 if (content1K == null) {			   
	            // initialize test content
			    
	            char content1KArray[] = new char[1024]; //1Kb
	            char content1MArray[] = new char[1024*1024]; //1Mb
	            char content2MArray[] = new char[1024*1024*2]; //2Mb
	            Arrays.fill(content1KArray, 'a');
	            Arrays.fill(content1MArray, 'b');
	            Arrays.fill(content1MArray, 'c');
	            content1K = createTempFile(true, new String(content1KArray));
	            content1M = createTempFile(true, new String(content1MArray));
	            content2M = createTempFile(true, new String(content2MArray));
	    	  }    
	}

   /**
	 * Test FileSystemRepositoryManager: insert method
	 */
	public void testInsertRepoItem(){
		try {
			IXdsRepositoryItem ro = createRepositoryItem(id, content1M);
			repositoryManager.insert(ro, requestContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	/**
	 * Test FileSystemRepositoryManager: getRepoItem method
	 */
    public void testgetRepoItem(){
    	IXdsRepositoryItem invalidRepositoryId =null;
    	try {    		
    		IXdsRepositoryItem repositoryItem = repositoryManager.getRepositoryItem(documentId, requestContext);
    		assertEquals(repositoryItem.getDocumentUniqueId(),documentId);
    		invalidRepositoryId = repositoryManager.getRepositoryItem("3d1a4aa5-e353-4d97-ae60-aa3ca9c96515", new RepositoryRequestContext());
    	} catch (Exception e) {
			assertNull(invalidRepositoryId);
		}
    	System.out.println("completed");
    	
	}
    
    /**
	 * Test FileSystemRepositoryManager: deleteDocumentID method
	 */
    public void testDeleteDocumentId(){
    	try {		
    		repositoryManager.delete(documentId, requestContext);    		
    	} catch (Exception e) {
		   e.printStackTrace();
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
    
    public class OidMock implements OID.OidSource {
        public synchronized String generateId() {
            return Long.toString( System.currentTimeMillis() );
        }
    }
}
