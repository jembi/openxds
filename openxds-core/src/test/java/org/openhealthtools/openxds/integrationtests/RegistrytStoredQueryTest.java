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

package org.openhealthtools.openxds.integrationtests;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Random;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.client.ServiceClient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openhealthtools.common.utils.OMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.xml.bind.StringInputStream;

/**
 * This class is an integrated test for IHE transaction ITI-18, namely,
 * RegistryStoredQuery. 
 * <p>
 * Before running this test case, be sure to configure the following:
 * <ul>
 *  <li>Both the XDS Repository and Registry servers have to be configured and started.</li>
 *  <li>The repositoryUrl and registryUrl needs be to set.</li>
 * </ul> 
 * 
 * Each test method can be run independently, so the order of each test method 
 * is not important.
 * 
 *  
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class RegistrytStoredQueryTest extends XdsTest {

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

	@Test
	public void testFindDocuments() throws Exception {
		//1. Submit a document first for a random patientId
		String patientId = generateAPatientId();
		String uuid = submitOneDocument(patientId);
		
		//2. Generate StoredQuery request message
		String message = findDocumentsQuery(patientId, "Approved");
		OMElement request = OMUtil.xmlStringToOM(message);			
		System.out.println("Request:\n" +request);

		//3. Send a StoredQuery
		ServiceClient sender = getRegistryServiceClient();															 
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		//4. Verify the response is correct
		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
		
		//5. Verify that the given patientId is found from the StoredQuery response. 
		NodeList nodes = getPatientIdNodes(response, patientId, "findDocuments");
		assertTrue(nodes.getLength() == 1); 

		String result = response.toString();
		System.out.println("Result:\n" +result);
	}

	@Test
	public void testFindFolders() throws Exception {
		//1. Submit a document first for a random patientId
		String patientId = generateAPatientId();
		String folderUniqeId = submitOneDocument2Folder(patientId);
		
		//2. Generate StoredQuery request message
		String message = findFoldersQuery(patientId, "Approved");
		OMElement request = OMUtil.xmlStringToOM(message);			
		System.out.println("Request:\n" +request);

		//3. Send a StoredQuery
		ServiceClient sender = getRegistryServiceClient();															 
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		//4. Verify the response is correct
		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
		
		//5. Verify that the given FolderPatientId is found from the StoredQuery response. 
		NodeList nodes = getPatientIdNodes(response, patientId, "findFolders");
		assertTrue(nodes.getLength() == 1); 

		String result = response.toString();
		System.out.println("Result:\n" +result);
	}
	
	@Test
	public void testSubmissionSets() throws Exception {
		//1. Submit a document first for a random patientId
		String patientId = generateAPatientId();
		submitOneDocument(patientId);
		
		//2. Generate StoredQuery request message
		String message = findSubmissionSetsQuery(patientId, "Approved");
		OMElement request = OMUtil.xmlStringToOM(message);			
		System.out.println("Request:\n" +request);

		//3. Send a StoredQuery
		ServiceClient sender = getRegistryServiceClient();															 
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		//4. Verify the response is correct
		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
		
		//5. Verify that the given PatientId is found from the StoredQuery response. 
		NodeList nodes = getPatientIdNodes(response, patientId, "findSubmissionSets");
		assertTrue(nodes.getLength() == 1); 

		String result = response.toString();
		System.out.println("Result:\n" +result);
	}
	
	@Test
	public void testGetDocuments() throws Exception {
		//1. Submit a document first for a random patientId
		String patientId = generateAPatientId();
		String uuid = submitOneDocument(patientId);
		
		//2. Generate StoredQuery request message
		String message = GetDocumentsQuery(uuid, false, "urn:uuid:5c4f972b-d56b-40ac-a5fc-c8ca9b40b9d4");
		OMElement request = OMUtil.xmlStringToOM(message);			
		System.out.println("Request:\n" +request);

		//3. Send a StoredQuery
		ServiceClient sender = getRegistryServiceClient();															 
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		//4. Verify the response is correct
		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
		
		//5. Verify that the given PatientId is found from the StoredQuery response. 
		NodeList nodes = getPatientIdNodes(response, patientId, "getDocuments");
		assertTrue(nodes.getLength() == 1); 

		String result = response.toString();
		System.out.println("Result:\n" +result);
	}
	
	@Test
	public void testGetFolders() throws Exception {
		//1. Submit a document first for a random patientId
		String patientId = generateAPatientId();
		String folderUniqueId = submitOneDocument2Folder(patientId);
		
		//2. Generate StoredQuery request message
		String message = GetFoldersQuery(folderUniqueId, true);
		OMElement request = OMUtil.xmlStringToOM(message);			
		System.out.println("Request:\n" +request);

		//3. Send a StoredQuery
		ServiceClient sender = getRegistryServiceClient();															 
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		//4. Verify the response is correct
		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
		
		//5. Verify that the given FolderPatientId is found from the StoredQuery response. 
		NodeList nodes = getPatientIdNodes(response, patientId, "getFolders");
		assertTrue(nodes.getLength() == 1); 

		String result = response.toString();
		System.out.println("Result:\n" +result);
	}
	
	@Test
	public void testGetSubmissionSets() throws Exception {
		//1. Submit a document first for a random patientId
		String patientId = generateAPatientId();
		String uuid = submitOneDocument(patientId);
		
		//2. Generate StoredQuery request message
		String message = GetSubmissionSetsQuery(uuid);
		OMElement request = OMUtil.xmlStringToOM(message);			
		System.out.println("Request:\n" +request);

		//3. Send a StoredQuery
		ServiceClient sender = getRegistryServiceClient();															 
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		//4. Verify the response is correct
		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
		
		//5. Verify that the given PatientId is found from the StoredQuery response. 
		NodeList nodes = getPatientIdNodes(response, patientId, "getSubmissionSets");
		assertTrue(nodes.getLength() == 1); 

		String result = response.toString();
		System.out.println("Result:\n" +result);
	}
	
	@Test
	public void testGetAssociations() throws Exception {
		//1. Submit a document first for a random patientId
		String patientId = generateAPatientId();
		String uuid = submitOneDocument(patientId);
		
		//2. Generate StoredQuery request message
		String message = GetAssociationsQuery(uuid);
		OMElement request = OMUtil.xmlStringToOM(message);			
		System.out.println("Request:\n" +request);

		//3. Send a StoredQuery
		ServiceClient sender = getRegistryServiceClient();															 
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		//4. Verify the response is correct
		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
		
		//5. Verify that the Association object found from the StoredQuery response. 
		NodeList nodes = getPatientIdNodes(response, patientId, "getAssociations");
		assertTrue(nodes.getLength() == 1); 

		String result = response.toString();
		System.out.println("Result:\n" +result);
	}
	
	@Test
	public void testGetDocumentsAndAssociations() throws Exception {
		//1. Submit a document first for a random patientId
		String patientId = generateAPatientId();
		String uuid = submitOneDocument(patientId);
		
		//2. Generate StoredQuery request message
		String message = GetDocumentsQuery(uuid, false,"urn:uuid:bab9529a-4a10-40b3-a01f-f68a615d247a");
		OMElement request = OMUtil.xmlStringToOM(message);			
		System.out.println("Request:\n" +request);

		//3. Send a StoredQuery
		ServiceClient sender = getRegistryServiceClient();															 
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		//4. Verify the response is correct
		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
		
		//5. Verify that the given PatientId is found from the StoredQuery response. 
		NodeList nodes = getPatientIdNodes(response, patientId, "getDocuments");
		assertTrue(nodes.getLength() == 1); 
		
		//6. Verify that the Association object found from the StoredQuery response. 
		NodeList nodes1 = getPatientIdNodes(response, patientId, "getAssociations");
		assertTrue(nodes1.getLength() == 1); 

		String result = response.toString();
		System.out.println("Result:\n" +result);
	}
	
	@Test
	public void testGetRelatedDocuments() throws Exception {
		//1. Submit a document first for a random patientId
		String patientId = generateAPatientId();
		String uuid = replaceDocument(patientId);
		
		//2. Generate StoredQuery request message
		String message = GetRelatedDocumentsQuery(uuid, false, "RPLC");
		OMElement request = OMUtil.xmlStringToOM(message);			
		System.out.println("Request:\n" +request);

		//3. Send a StoredQuery
		ServiceClient sender = getRegistryServiceClient();															 
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		//4. Verify the response is correct
		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
		
		//5. Verify that the Association object found from the StoredQuery response. 
		NodeList nodes = getPatientIdNodes(response, patientId, "getReplaceDocument");
		assertTrue(nodes.getLength() == 1); 

		String result = response.toString();
		System.out.println("Result:\n" +result);
	}
	
	@Test
	@Ignore
	public void testGetAll() throws Exception {
		//1. Submit a document first for a random patientId
		String patientId = generateAPatientId();
		String uuid = submitOneDocument(patientId);
		
		//2. Generate StoredQuery request message
		String message = GetAllsQuery(patientId, "Approved");
		OMElement request = OMUtil.xmlStringToOM(message);			
		System.out.println("Request:\n" +request);

		//3. Send a StoredQuery
		ServiceClient sender = getRegistryServiceClient();															 
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		//4. Verify the response is correct
		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
		
		//5. Verify that the given patientId is found from the StoredQuery response. 
		NodeList nodes = getPatientIdNodes(response, patientId, "findDocuments");
		assertTrue(nodes.getLength() == 1); 
		
		//NodeList nodes1 = getPatientIdNodes(response, patientId, "findSubmissionSets");
		//assertTrue(nodes1.getLength() == 1); 

		String result = response.toString();
		System.out.println("Result:\n" +result);
	}
	
	@Test
	public void testGetFoldersForDocument() throws Exception {
		//1. Submit a document first for a random patientId
		String patientId = generateAPatientId();
		String uuid = submitDocument2Folder(patientId);
		
		//2. Generate StoredQuery request message
		String message = GetFoldersForDocumentQuery(uuid, false);
		OMElement request = OMUtil.xmlStringToOM(message);			
		System.out.println("Request:\n" +request);

		//3. Send a StoredQuery
		ServiceClient sender = getRegistryServiceClient();															 
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		//4. Verify the response is correct
		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
		
		//5. Verify that the given PatientId is found from the StoredQuery response. 
		NodeList nodes = getPatientIdNodes(response, patientId, "getFolders");
		assertTrue(nodes.getLength() == 1); 

		String result = response.toString();
		System.out.println("Result:\n" +result);
	}
	
	@Test
	public void testGetFoldersAndContents() throws Exception {
		//1. Submit a document first for a random patientId
		String patientId = generateAPatientId();
		String folderUniqueId = submitOneDocument2Folder(patientId);
		
		//2. Generate StoredQuery request message
		String message = GetFoldersAndContentsQuery(folderUniqueId, true);
		OMElement request = OMUtil.xmlStringToOM(message);			
		System.out.println("Request:\n" +request);

		//3. Send a StoredQuery
		ServiceClient sender = getRegistryServiceClient();															 
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		//4. Verify the response is correct
		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
		
		//5. Verify that the given PatientId is found from the StoredQuery response. 
		NodeList nodes = getPatientIdNodes(response, patientId, "getFolders");
		assertTrue(nodes.getLength() == 1); 
		
		NodeList asso_nodes = getPatientIdNodes(response, patientId, "getAssociations");
		assertTrue(asso_nodes.getLength() == 1); 

		String result = response.toString();
		System.out.println("Result:\n" +result);
	}
	
	@Test
	public void testGetSubmissionSetAndContents() throws Exception {
		//1. Submit a document first for a random patientId
		String patientId = generateAPatientId();
		String subUniqueId = submitDocument2SubmissionSet(patientId);
		
		//2. Generate StoredQuery request message
		String message = GetSubmissionSetAndContentsQuery(subUniqueId, true);
		OMElement request = OMUtil.xmlStringToOM(message);			
		System.out.println("Request:\n" +request);

		//3. Send a StoredQuery
		ServiceClient sender = getRegistryServiceClient();															 
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		//4. Verify the response is correct
		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
		
		//5. Verify that the given PatientId is found from the StoredQuery response. 
		NodeList nodes = getPatientIdNodes(response, patientId, "getDocuments");
		assertTrue(nodes.getLength() == 1); 
		
		//NodeList asso_nodes = getPatientIdNodes(response, patientId, "getAssociations");
		//assertTrue(asso_nodes.getLength() == 1); 

		String result = response.toString();
		System.out.println("Result:\n" +result);
	}
	
	//Search the ExternalIdentifier for the given patientId
	private NodeList getPatientIdNodes(OMElement response, String patientId, String type) 
	throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
		patientId = patientId.replaceAll("&amp;", "&");
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
	    domFactory.setNamespaceAware(false); // never forget this!
	    DocumentBuilder builder = domFactory.newDocumentBuilder();
	    Document doc = builder.parse(new StringInputStream(response.toString()));
	    
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		XPathExpression expr = null; 
		if (type.equalsIgnoreCase("findDocuments") || type.equalsIgnoreCase("getDocuments"))
			expr = xpath.compile("//AdhocQueryResponse/RegistryObjectList/ExtrinsicObject/ExternalIdentifier[@identificationScheme='urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427' and @value='"+patientId+"']"); 
		else if (type.equalsIgnoreCase("findFolders") || type.equalsIgnoreCase("getFolders"))
			expr = xpath.compile("//AdhocQueryResponse/RegistryObjectList/RegistryPackage/ExternalIdentifier[@identificationScheme='urn:uuid:f64ffdf0-4b97-4e06-b79f-a52b38ec2f8a' and @value='"+patientId+"']"); 
		else if (type.equalsIgnoreCase("findSubmissionSets") || type.equalsIgnoreCase("getSubmissionSets"))
			expr = xpath.compile("//AdhocQueryResponse/RegistryObjectList/RegistryPackage/ExternalIdentifier[@identificationScheme='urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446' and @value='"+patientId+"']"); 
		else if (type.equalsIgnoreCase("getAssociations"))
			expr = xpath.compile("//AdhocQueryResponse/RegistryObjectList/Association[@targetObject !='' and @sourceObject !='']");
		else if (type.equalsIgnoreCase("getReplaceDocument"))
			expr = xpath.compile("//AdhocQueryResponse/RegistryObjectList/Association[@associationType='urn:ihe:iti:2007:AssociationType:RPLC' and @targetObject !='' and @sourceObject !='']"); 
		Object res = expr.evaluate(doc, XPathConstants.NODESET);
	    NodeList nodes = (NodeList) res;
//	    for (int i = 0; i < nodes.getLength(); i++) {
//	        System.out.println(nodes.item(i)); 
//	    }
		return nodes;
	}
	
	public String findDocumentsQuery(String patientId, String status){
		String request = "<query:AdhocQueryRequest xsi:schemaLocation=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0 ../schema/ebRS/query.xsd\" xmlns:query=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\" xmlns:rs=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0\">\n"+
		              	 " <query:ResponseOption returnComposedObjects=\"true\" returnType=\"LeafClass\"/>\n"+
		              	 "  <rim:AdhocQuery id=\"urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d\">\n";
		if (patientId != null) {
			request +=   "   <rim:Slot name=\"$XDSDocumentEntryPatientId\">\n"+
			         	 "     <rim:ValueList>\n" + 
			             "       <rim:Value>'"+patientId+"'</rim:Value>\n" +
			             "     </rim:ValueList>\n"+
			             "   </rim:Slot>\n";
		}
		if (status != null) {
			request +=   "   <rim:Slot name=\"$XDSDocumentEntryStatus\">\n" +
						 "     <rim:ValueList>\n" + 
						 "       <rim:Value>('urn:oasis:names:tc:ebxml-regrep:StatusType:"+status+"')</rim:Value>\n" +
						 "     </rim:ValueList>\n" +
						 "   </rim:Slot>\n";	
		}
        request +=       "  </rim:AdhocQuery>\n" +
                         "</query:AdhocQueryRequest>";
		
		return request;
	}

	private String findFoldersQuery(String folderPatientId, String status){
		String request = "<query:AdhocQueryRequest xsi:schemaLocation=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0 ../schema/ebRS/query.xsd\" xmlns:query=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\" xmlns:rs=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0\">\n"+
		              	 " <query:ResponseOption returnComposedObjects=\"true\" returnType=\"LeafClass\"/>\n"+
		              	 "  <rim:AdhocQuery id=\"urn:uuid:958f3006-baad-4929-a4de-ff1114824431\">\n";
		if (folderPatientId != null) {
			request +=   "   <rim:Slot name=\"$XDSFolderPatientId\">\n"+
			         	 "     <rim:ValueList>\n" + 
			             "       <rim:Value>'"+folderPatientId+"'</rim:Value>\n" +
			             "     </rim:ValueList>\n"+
			             "   </rim:Slot>\n";
		}		
		if (status != null) {
			request +=   "   <rim:Slot name=\"$XDSFolderStatus\">\n" +
						 "     <rim:ValueList>\n" + 
						 "       <rim:Value>('urn:oasis:names:tc:ebxml-regrep:StatusType:"+status+"')</rim:Value>\n" +
						 "     </rim:ValueList>\n" +
						 "   </rim:Slot>\n";			
		}
        request +=       "  </rim:AdhocQuery>\n" +
                         "</query:AdhocQueryRequest>";
		
		return request;
	}
	
	private String findSubmissionSetsQuery(String SubmissionSetPatientId, String status){
		String request = "<query:AdhocQueryRequest xsi:schemaLocation=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0 ../schema/ebRS/query.xsd\" xmlns:query=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\" xmlns:rs=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0\">\n"+
		              	 " <query:ResponseOption returnComposedObjects=\"true\" returnType=\"LeafClass\"/>\n"+
		              	 "  <rim:AdhocQuery id=\"urn:uuid:f26abbcb-ac74-4422-8a30-edb644bbc1a9\">\n";
		if (SubmissionSetPatientId != null) {
			request +=   "   <rim:Slot name=\"$XDSSubmissionSetPatientId\">\n"+
			         	 "     <rim:ValueList>\n" + 
			             "       <rim:Value>'"+SubmissionSetPatientId+"'</rim:Value>\n" +    //Multiple values are not allowed
			             "     </rim:ValueList>\n"+
			             "   </rim:Slot>\n";
		}		
		if (status != null) {
			request +=   "   <rim:Slot name=\"$XDSSubmissionSetStatus\">\n" +
						 "     <rim:ValueList>\n" + 
						 "       <rim:Value>('urn:oasis:names:tc:ebxml-regrep:StatusType:"+status+"')</rim:Value>\n" +  //Multiple values are allowed
						 "     </rim:ValueList>\n" +
						 "   </rim:Slot>\n";			
		}
        request +=       "  </rim:AdhocQuery>\n" +
                         "</query:AdhocQueryRequest>";
		
		return request;
	}
	
	private String GetFoldersQuery(String id, boolean uniqueId){
		String request = "<query:AdhocQueryRequest xsi:schemaLocation=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0 ../schema/ebRS/query.xsd\" xmlns:query=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\" xmlns:rs=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0\">\n"+
		              	 " <query:ResponseOption returnComposedObjects=\"true\" returnType=\"LeafClass\"/>\n"+
		              	 "  <rim:AdhocQuery id=\"urn:uuid:5737b14c-8a1a-4539-b659-e03a34a5e1e4\">\n";
		if (id != null && uniqueId == true) {
			request +=   "   <rim:Slot name=\"$XDSFolderUniqueId\">\n"+
			         	 "     <rim:ValueList>\n" + 
			             "       <rim:Value>('"+id+"')</rim:Value>\n" +    //Multiple values are allowed
			             "     </rim:ValueList>\n"+
			             "   </rim:Slot>\n";
		} else {
			request +=   "   <rim:Slot name=\"$XDSFolderEntryUUID\">\n" +
						 "     <rim:ValueList>\n" + 
						 "       <rim:Value>('"+id+"')</rim:Value>\n" +     //Multiple values are allowed
						 "     </rim:ValueList>\n" +
						 "   </rim:Slot>\n";			
		}
        request +=       "  </rim:AdhocQuery>\n" +
                         "</query:AdhocQueryRequest>";
		
		return request;
	}
	
	private String GetFoldersForDocumentQuery(String id, boolean uniqueId){
		String request = "<query:AdhocQueryRequest xsi:schemaLocation=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0 ../schema/ebRS/query.xsd\" xmlns:query=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\" xmlns:rs=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0\">\n"+
		              	 " <query:ResponseOption returnComposedObjects=\"true\" returnType=\"LeafClass\"/>\n"+
		              	 "  <rim:AdhocQuery id=\"urn:uuid:10cae35a-c7f9-4cf5-b61e-fc3278ffb578\">\n";
		if (id != null && uniqueId == true) {
			request +=   "   <rim:Slot name=\"$XDSDocumentEntryUniqueId\">\n"+
			         	 "     <rim:ValueList>\n" + 
			             "       <rim:Value>'"+id+"'</rim:Value>\n" +     //Multiple values are not allowed
			             "     </rim:ValueList>\n"+
			             "   </rim:Slot>\n";
		} else {
			request +=   "   <rim:Slot name=\"$XDSDocumentEntryEntryUUID\">\n" +
						 "     <rim:ValueList>\n" + 
						 "       <rim:Value>'"+id+"'</rim:Value>\n" +   //Multiple values are not allowed
						 "     </rim:ValueList>\n" +
						 "   </rim:Slot>\n";			
		}
        request +=       "  </rim:AdhocQuery>\n" +
                         "</query:AdhocQueryRequest>";
		
		return request;
	}
	
	private String GetFoldersAndContentsQuery(String id, boolean uniqueId){
		String request = "<query:AdhocQueryRequest xsi:schemaLocation=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0 ../schema/ebRS/query.xsd\" xmlns:query=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\" xmlns:rs=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0\">\n"+
		              	 " <query:ResponseOption returnComposedObjects=\"true\" returnType=\"LeafClass\"/>\n"+
		              	 "  <rim:AdhocQuery id=\"urn:uuid:b909a503-523d-4517-8acf-8e5834dfc4c7\">\n";
		if (id != null && uniqueId == true) {
			request +=   "   <rim:Slot name=\"$XDSFolderUniqueId\">\n"+
			         	 "     <rim:ValueList>\n" + 
			             "       <rim:Value>'"+id+"'</rim:Value>\n" +     //Multiple values are not allowed
			             "     </rim:ValueList>\n"+
			             "   </rim:Slot>\n";
		} else {
			request +=   "   <rim:Slot name=\"$XDSFolderEntryUUID\">\n" +
						 "     <rim:ValueList>\n" + 
						 "       <rim:Value>'"+id+"'</rim:Value>\n" +   //Multiple values are not allowed
						 "     </rim:ValueList>\n" +
						 "   </rim:Slot>\n";			
		}
        request +=       "  </rim:AdhocQuery>\n" +
                         "</query:AdhocQueryRequest>";
		
		return request;
	}
	
	private String GetSubmissionSetAndContentsQuery(String id, boolean uniqueId){
		String request = "<query:AdhocQueryRequest xsi:schemaLocation=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0 ../schema/ebRS/query.xsd\" xmlns:query=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\" xmlns:rs=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0\">\n"+
		              	 " <query:ResponseOption returnComposedObjects=\"true\" returnType=\"LeafClass\"/>\n"+
		              	 "  <rim:AdhocQuery id=\"urn:uuid:e8e3cb2c-e39c-46b9-99e4-c12f57260b83\">\n";
		if (id != null && uniqueId == true) {
			request +=   "   <rim:Slot name=\"$XDSSubmissionSetUniqueId\">\n"+
			         	 "     <rim:ValueList>\n" + 
			             "       <rim:Value>'"+id+"'</rim:Value>\n" +     //Multiple values are not allowed
			             "     </rim:ValueList>\n"+
			             "   </rim:Slot>\n";
		} else {
			request +=   "   <rim:Slot name=\"$XDSSubmissionSetEntryUUID\">\n" +
						 "     <rim:ValueList>\n" + 
						 "       <rim:Value>'"+id+"'</rim:Value>\n" +   //Multiple values are not allowed
						 "     </rim:ValueList>\n" +
						 "   </rim:Slot>\n";			
		}
        request +=       "  </rim:AdhocQuery>\n" +
                         "</query:AdhocQueryRequest>";
		
		return request;
	}
	
	private String GetSubmissionSetsQuery(String id){
		String request = "<query:AdhocQueryRequest xsi:schemaLocation=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0 ../schema/ebRS/query.xsd\" xmlns:query=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\" xmlns:rs=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0\">\n"+
		              	 " <query:ResponseOption returnComposedObjects=\"true\" returnType=\"LeafClass\"/>\n"+
		              	 "  <rim:AdhocQuery id=\"urn:uuid:51224314-5390-4169-9b91-b1980040715a\">\n";
		if (id != null) {
			request +=   "   <rim:Slot name=\"$uuid\">\n"+
			         	 "     <rim:ValueList>\n" + 
			             "       <rim:Value>('"+id+"')</rim:Value>\n" +
			             "     </rim:ValueList>\n"+
			             "   </rim:Slot>\n";
		} 
        request +=       "  </rim:AdhocQuery>\n" +
                         "</query:AdhocQueryRequest>";
		
		return request;
	}
	
	private String GetAssociationsQuery(String id){
		String request = "<query:AdhocQueryRequest xsi:schemaLocation=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0 ../schema/ebRS/query.xsd\" xmlns:query=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\" xmlns:rs=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0\">\n"+
		              	 " <query:ResponseOption returnComposedObjects=\"true\" returnType=\"LeafClass\"/>\n"+
		              	 "  <rim:AdhocQuery id=\"urn:uuid:a7ae438b-4bc2-4642-93e9-be891f7bb155\">\n";
		if (id != null) {
			request +=   "   <rim:Slot name=\"$uuid\">\n"+
			         	 "     <rim:ValueList>\n" + 
			             "       <rim:Value>('"+id+"')</rim:Value>\n" +
			             "     </rim:ValueList>\n"+
			             "   </rim:Slot>\n";
		} 
        request +=       "  </rim:AdhocQuery>\n" +
                         "</query:AdhocQueryRequest>";
		
		return request;
	}
	
	private String GetDocumentsQuery(String id, boolean uniqueId, String queryId){
		String request = "<query:AdhocQueryRequest xsi:schemaLocation=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0 ../schema/ebRS/query.xsd\" xmlns:query=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\" xmlns:rs=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0\">\n"+
		              	 " <query:ResponseOption returnComposedObjects=\"true\" returnType=\"LeafClass\"/>\n"+
		              	 "  <rim:AdhocQuery id=\""+queryId+"\">\n";
		if (id != null && uniqueId == true) {
			request +=   "   <rim:Slot name=\"$XDSDocumentEntryUniqueId\">\n"+
			         	 "     <rim:ValueList>\n" + 
			             "       <rim:Value>('"+id+"')</rim:Value>\n" +   //Multiple values are allowed
			             "     </rim:ValueList>\n"+
			             "   </rim:Slot>\n";
		} else {
			request +=   "   <rim:Slot name=\"$XDSDocumentEntryEntryUUID\">\n" +
						 "     <rim:ValueList>\n" + 
						 "       <rim:Value>('"+id+"')</rim:Value>\n" +   //Multiple values are allowed
						 "     </rim:ValueList>\n" +
						 "   </rim:Slot>\n";			
		}
        request +=       "  </rim:AdhocQuery>\n" +
                         "</query:AdhocQueryRequest>";
		
		return request;
	}
	
	
	private String GetRelatedDocumentsQuery(String id, boolean uniqueId, String type){
		String request = "<query:AdhocQueryRequest xsi:schemaLocation=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0 ../schema/ebRS/query.xsd\" xmlns:query=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\" xmlns:rs=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0\">\n"+
		              	 " <query:ResponseOption returnComposedObjects=\"true\" returnType=\"LeafClass\"/>\n"+
		              	 "  <rim:AdhocQuery id=\"urn:uuid:d90e5407-b356-4d91-a89f-873917b4b0e6\">\n";
		if (id != null && uniqueId == true) {
			request +=   "   <rim:Slot name=\"$XDSDocumentEntryUniqueId\">\n"+
			         	 "     <rim:ValueList>\n" + 
			             "       <rim:Value>'"+id+"'</rim:Value>\n" +     //Multiple values are not allowed
			             "     </rim:ValueList>\n"+
			             "   </rim:Slot>\n";
		} else {
			request +=   "   <rim:Slot name=\"$XDSDocumentEntryEntryUUID\">\n" +
						 "     <rim:ValueList>\n" + 
						 "       <rim:Value>'"+id+"'</rim:Value>\n" +   //Multiple values are not allowed
						 "     </rim:ValueList>\n" +
						 "   </rim:Slot>\n";			
		}
		/*if (type != null && type.equalsIgnoreCase("HasMember")) {
			request +=   "   <rim:Slot name=\"$AssociationTypes\">\n" +
						 "     <rim:ValueList>\n" + 
						 "       <rim:Value>('urn:oasis:names:tc:ebxml-regrep:AssociationType:"+type+"')</rim:Value>\n" +  //Multiple values are allowed
						 "     </rim:ValueList>\n" +
						 "   </rim:Slot>\n";			
		} else {*/
			request +=   "   <rim:Slot name=\"$AssociationTypes\">\n" +
						 "     <rim:ValueList>\n" + 
						 "       <rim:Value>('urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember','urn:ihe:iti:2007:AssociationType:"+type+"')</rim:Value>\n" +  //Multiple values are allowed
						 "     </rim:ValueList>\n" +
						 "   </rim:Slot>\n";	
		//}
        request +=       "  </rim:AdhocQuery>\n" +
                         "</query:AdhocQueryRequest>";
		
		return request;
	}
	
	private String GetAllsQuery(String patientId, String status){
		String request = "<query:AdhocQueryRequest xsi:schemaLocation=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0 ../schema/ebRS/query.xsd\" xmlns:query=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\" xmlns:rs=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0\">\n"+
		              	 " <query:ResponseOption returnComposedObjects=\"true\" returnType=\"LeafClass\"/>\n"+
		              	 "  <rim:AdhocQuery id=\"urn:uuid:10b545ea-725c-446d-9b95-8aeb444eddf3\">\n";
		if (patientId != null) {
			request +=   "   <rim:Slot name=\"$patientId\">\n"+
			         	 "     <rim:ValueList>\n" + 
			             "       <rim:Value>'"+patientId+"'</rim:Value>\n" +
			             "     </rim:ValueList>\n"+
			             "   </rim:Slot>\n";
		}
		if (status != null) {
			request +=   "   <rim:Slot name=\"$XDSDocumentEntryStatus\">\n" +
						 "     <rim:ValueList>\n" + 
						 "       <rim:Value>('urn:oasis:names:tc:ebxml-regrep:StatusType:"+status+"')</rim:Value>\n" +
						 "     </rim:ValueList>\n" +
						 "   </rim:Slot>\n";	
		}
		if (status != null) {
			request +=   "   <rim:Slot name=\"$XDSSubmissionSetStatus\">\n" +
						 "     <rim:ValueList>\n" + 
						 "       <rim:Value>('urn:oasis:names:tc:ebxml-regrep:StatusType:"+status+"')</rim:Value>\n" +
						 "     </rim:ValueList>\n" +
						 "   </rim:Slot>\n";	
		}
		if (status != null) {
			request +=   "   <rim:Slot name=\"$XDSFolderStatus\">\n" +
						 "     <rim:ValueList>\n" + 
						 "       <rim:Value>('urn:oasis:names:tc:ebxml-regrep:StatusType:"+status+"')</rim:Value>\n" +
						 "     </rim:ValueList>\n" +
						 "   </rim:Slot>\n";	
		}
        request +=       "  </rim:AdhocQuery>\n" +
                         "</query:AdhocQueryRequest>";
		
		return request;
	}
}
