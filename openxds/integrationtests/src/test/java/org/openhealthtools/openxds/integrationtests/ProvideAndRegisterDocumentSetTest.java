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

package org.openhealthtools.openxds.integrationtests;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openhealthtools.openexchange.utils.OMUtil;

/**
 * This class is an integrated test for IHE transaction ITI-41, namely,
 * ProvideAndRegisterDocumentSet-b. 
 * <p>
 * Before running this test case, be sure to configure the following:
 * <ul>
 *  <li>Both the XDS Repository and Registry servers have to be configured and started.</li>
 *  <li>The repositoryUrl needs be to set.</li>
 * </ul> 
 * 
 * Each test method can be run independently, so the order of each test method 
 * is not important.
 * 
 *  
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 * @author <a href="mailto:rasakannu.palaniyandi@misys.com">raja</a>
 */
public class ProvideAndRegisterDocumentSetTest extends XdsTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for ProvideAndRegisterDocumentSet-b (ITI-41)
	 * 
	 * @throws  
	 * @throws Exception 
	 */
	@Test
	public void testSubmitDocument() throws Exception {
		createPatient(patientId);
		
		String message = IOUtils.toString( ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/submit_document.xml"));
		String document = IOUtils.toString(ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/referral_summary.xml"));
		//replace document and submission set uniqueId variables with actual uniqueIds. 
		message = message.replace("$XDSDocumentEntry.uniqueId", "2.16.840.1.113883.3.65.2." + System.currentTimeMillis());
		message = message.replace("$XDSSubmissionSet.uniqueId", "1.3.6.1.4.1.21367.2009.1.2.108." + System.currentTimeMillis());
		message = message.replace("$patientId", patientId);
		//replace the document uuid.
		String uuid = getUUID();
		message = message.replace("$doc1", uuid);
		
		ServiceClient sender = getRepositoryServiceClient();			
		
		OMElement request = OMUtil.xmlStringToOM(message);			
		
		//Add a document
		request = addOneDocument(request, document, uuid);
        
		System.out.println("Request:\n" +request);

		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue());

		String result = response.toString();
		System.out.println("Result:\n" +result);
	}
	
	
	@Test
	public void testSubmitMultipleDocument() throws Exception {
		createPatient(patientId);

		String message = IOUtils.toString( ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/submit_multiple_documents.xml"));
		String document1 = IOUtils.toString(ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/referral_summary.xml"));
		String document2 = IOUtils.toString(ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/medical_summary.xml"));
		//replace document and submission set uniqueId variables with actual uniqueIds. 
		message = message.replace("$XDSDocumentEntry.uniqueId", "2.16.840.1.113883.3.65.2." + System.currentTimeMillis());
		message = message.replace("$XDSDocumentEntry.uniqueId1", "2.16.840.1.113883.3.65.2." + System.currentTimeMillis());
		message = message.replace("$XDSSubmissionSet.uniqueId", "1.3.6.1.4.1.21367.2009.1.2.108." + System.currentTimeMillis());
		message = message.replace("$patientId", patientId);
		String uuid1 = getUUID();
		String uuid2 = getUUID();
		message = message.replace("$doc1", uuid1);
		message = message.replace("$doc2", uuid2);
		ServiceClient sender = getRepositoryServiceClient();			
		
		OMElement request = OMUtil.xmlStringToOM(message);			
		
		//Add a referral summary document
		request = addOneDocument(request, document1, uuid1);
		//Add a Discharge summary documents
		request = addOneDocument(request, document1, uuid2);

		System.out.println("Request:\n" +request);
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue());
		
		String result = response.toString();
		System.out.println("Result:\n" +result);
	}
	
	
	@Test
	public void testAddDocument2Folder() throws Exception {
		String message = IOUtils.toString( ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/add_document2_folder.xml"));
		String document1 = IOUtils.toString(ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/referral_summary.xml"));
		//replace document , submission set and folder uniqueId variables with actual uniqueIds. 
		message = message.replace("$XDSDocumentEntry.uniqueId", "2.16.840.1.113883.3.65.2." + System.currentTimeMillis());
		message = message.replace("$XDSSubmissionSet.uniqueId", "1.3.6.1.4.1.21367.2009.1.2.108." + System.currentTimeMillis());
		message = message.replace("$folder_uniqueid", "2.16.840.1.113883.3.65.3." + System.currentTimeMillis());
		message = message.replace("$patientId", patientId);
		
		ServiceClient sender = getRepositoryServiceClient();			
		
		OMElement request = OMUtil.xmlStringToOM(message);			
		
		//Add a referral summary document
		request = addOneDocument(request, document1, "doc1");
		
        System.out.println("Request:\n" +request);
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue());

		String result = response.toString();
		System.out.println("Result:\n" +result);
	}	
	
	
	@Test
	public void testAddendumDocument() throws Exception {
		//First submit a document
		String doc_uuid = submitOneDocument(patientId);
		
		//Then add an Addendum
		String message = IOUtils.toString( ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/document_addendum.xml"));
		String document1 = IOUtils.toString(ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/referral_summary.xml"));
		//replace document , submission set variables with actual uniqueIds. 
		message = message.replace("$XDSDocumentEntry.uniqueId", "2.16.840.1.113883.3.65.2." + System.currentTimeMillis());
		message = message.replace("$XDSSubmissionSet.uniqueId", "1.3.6.1.4.1.21367.2009.1.2.108." + System.currentTimeMillis());
		message = message.replace("$patientId", patientId);
		//populate the document uuid to be appended.
		message = message.replace("$appendum_doc_uuid", doc_uuid);
		
		ServiceClient sender = getRepositoryServiceClient();			
		
		OMElement request = OMUtil.xmlStringToOM(message);			
		
		//Add a referral summary document
		request = addOneDocument(request, document1, "doc1");

		System.out.println("Request:\n" +request);
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue());

		String result = response.toString();
		System.out.println("Result:\n" +result);
	}
	
	@Test
	public void testReplaceDocument() throws Exception {
		//First submit a document
		String doc_uuid = submitOneDocument(patientId);

		//Then add a replacement doc
		String message = IOUtils.toString( ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/document_replacement.xml"));
		String document1 = IOUtils.toString(ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/medical_summary.xml"));
		//replace document and submission set variables with actual uniqueIds. 
		message = message.replace("$XDSDocumentEntry.uniqueId", "2.16.840.1.113883.3.65.2." + System.currentTimeMillis());
		message = message.replace("$XDSSubmissionSet.uniqueId", "1.3.6.1.4.1.21367.2009.1.2.108." + System.currentTimeMillis());
		message = message.replace("$patientId", patientId);
		//populate the document uuid to be replaced.
		message = message.replace("$rplc_doc_uuid", doc_uuid);
		
		ServiceClient sender = getRepositoryServiceClient();			
		
		OMElement request = OMUtil.xmlStringToOM(message);			
		
		//Add a medical summary document
		request = addOneDocument(request, document1, "doc1");

		System.out.println("Request:\n" +request);
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue());
		
		String result = response.toString();
		System.out.println("Result:\n" +result);
	}
	
	@Test
	public void testTransformationDocument() throws Exception {
		//First submit a document
		String doc_uuid = submitOneDocument(patientId);

		//Then add a transformation doc
		String message = IOUtils.toString( ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/document_transformation.xml"));
		String document1 = IOUtils.toString(ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/medical_summary.xml"));
		//replace document and submission set uniqueId variables with actual uniqueIds. 
		message = message.replace("$XDSDocumentEntry.uniqueId", "2.16.840.1.113883.3.65.2." + System.currentTimeMillis());
		message = message.replace("$XDSSubmissionSet.uniqueId", "1.3.6.1.4.1.21367.2009.1.2.108." + System.currentTimeMillis());
		message = message.replace("$patientId", patientId);
		//populate the document uuid to be transformed
		message = message.replace("$tran_doc_uuid", doc_uuid);

		ServiceClient sender = getRepositoryServiceClient();			
		
		OMElement request = OMUtil.xmlStringToOM(message);			
		
		//Add a transformation document
		request = addOneDocument(request, document1, "doc1");

        System.out.println("Request:\n" +request);
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue());

		String result = response.toString();
		System.out.println("Result:\n" +result);
	}
	
	
	@Test
	public void testTranformWithReplaceDocument() throws Exception {
		//First submit a document
		String doc_uuid = submitOneDocument(patientId);

		//Then add a transform with replace doc
		String message = IOUtils.toString( ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/document_trans_replace.xml"));
		String document1 = IOUtils.toString(ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/medical_summary.xml"));
		//replace document and submission set uniqueId variables with actual uniqueIds. 
		message = message.replace("$XDSDocumentEntry.uniqueId", "2.16.840.1.113883.3.65.2." + System.currentTimeMillis());
		message = message.replace("$XDSSubmissionSet.uniqueId", "1.3.6.1.4.1.21367.2009.1.2.108." + System.currentTimeMillis());
		message = message.replace("$patientId", patientId);
		//populate the document uuid to be transformed and replaced
		message = message.replace("$tran_rplc_doc_uuid", doc_uuid);

		ServiceClient sender = getRepositoryServiceClient();			
		
		OMElement request = OMUtil.xmlStringToOM(message);			
		
		//Add a transformation document
		request = addOneDocument(request, document1, "doc1");

		System.out.println("Request:\n" +request);
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue());

		String result = response.toString();
		System.out.println("Result:\n" +result);
	}
	
}
