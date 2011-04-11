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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openhealthtools.openexchange.actorconfig.ActorDescriptionLoader;
import org.openhealthtools.openexchange.actorconfig.IActorDescription;
import org.openhealthtools.openxds.repository.api.RepositoryRequestContext;
import org.openhealthtools.openxds.repository.api.XdsRepositoryItem;
import org.openhealthtools.openxds.repository.api.XdsRepositoryService;


/**
 * This class is used to test the file system based repository manager implementation.
 *  
 * @author <a href="mailto:Rasakannu.Palaniyandi@misys.com">Raja</a>
 *
 */
public class FileSystemRepositoryTest{

   private static File content1K;
   private static File content1M;
   private static File content2M;
   private static XdsRepositoryService repositoryManager;
   private static RepositoryRequestContext requestContext = new RepositoryRequestContext();
   private static final String id = Utility.getInstance().createId();
   private static String documentId = Utility.getInstance().stripId(id);
   
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	   try{
		   repositoryManager = ModuleManager.getXdsFileRepository();
		   URL url = FileSystemRepositoryTest.class.getResource("/IheActors.xml");
		   List<IActorDescription> actors = ActorDescriptionLoader.loadActorDescriptions(url.getFile());
		   for(IActorDescription actor : actors) {
			   if (actor.getType().equals("XdsRepository") ) {
				   requestContext.setActorDescription(actor);
			   }
		   }
	   
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
	   }catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
   
   /**
	 * Test FileSystemRepositoryManager: insert method
	 */
	@Test	
	public void testInsertRepoItem(){
		try {
			XdsRepositoryItem ro = createRepositoryItem(id, content1M);
			repositoryManager.insert(ro, requestContext);
			
			XdsRepositoryItem repositoryItem = repositoryManager.getRepositoryItem(documentId, requestContext);
			assertNotNull(repositoryItem);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	/**
	 * Test FileSystemRepositoryManager: getRepoItem method
	 */
	@Test
    public void testgetRepoItem(){
    	XdsRepositoryItem invalidRepositoryId =null;
    	try {    		
    		XdsRepositoryItem repositoryItem = repositoryManager.getRepositoryItem(documentId, requestContext);
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
	@Test
    public void testDeleteDocumentId(){
    	try {		
			XdsRepositoryItem repositoryItem = repositoryManager.getRepositoryItem(documentId, requestContext);
			assertNotNull(repositoryItem);

			repositoryManager.delete(documentId, requestContext);    		
    		
		    repositoryItem = repositoryManager.getRepositoryItem(documentId, requestContext);
			assertNull(repositoryItem);
    	} catch (Exception e) {
		   e.printStackTrace();
    	   assertFalse(e.getMessage(), true);
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
    
    private XdsRepositoryItem createRepositoryItem(String id, File content) throws Exception {    	
        DataHandler contentDataHandler = new DataHandler(new FileDataSource(content));
        XdsRepositoryItem repositoryItem = new XdsRepositoryItemImpl(id, contentDataHandler);
        return repositoryItem;
        
    }
    
}
