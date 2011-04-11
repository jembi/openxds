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
import static org.junit.Assert.assertTrue;
import gov.nist.registry.common2.registry.MetadataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.client.ServiceClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openhealthtools.openexchange.config.PropertyFacade;
import org.openhealthtools.openexchange.utils.OMUtil;
import org.openhealthtools.openxds.common.XdsFactory;
import org.openhealthtools.openxds.repository.api.XdsRepositoryService;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.xml.bind.StringInputStream;


public class CrossGatewayRetrieveTest extends XdsTest{

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
	 * Issue Cross Gateway Retrieve for a single document.
	 * @throws Exception
	 */
	@Test
	public void testSingleDocumet() throws Exception {
		//1. Submit a document first for a random patientId
		String patientId = generateAPatientId();
		String uuids = submitMultipleDocuments(patientId);
		
		//2. Generate StoredQuery request message
		String message = new CrossGatewayQueryTest().findDocumentsQuery(patientId, "Approved", "LeafClass");
		OMElement request = OMUtil.xmlStringToOM(message);			
		System.out.println("Request:\n" +request);

		//3. Send a StoredQuery
		ServiceClient sender = getRegistryGateWayClient();															 
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 
		
		//4. Get DocumentUniqueId from the response.
		List extrinsicObjects = getExtrinsicObjects(response);
		List<String> ids = getDocumentId(extrinsicObjects);
		
		//5. Get RepositoryUniqueId from the response.
		XdsRepositoryService xdsService = (XdsRepositoryService) XdsFactory.getInstance().getBean("repositoryService");
		String reposiotryUniqueId = xdsService.getRepositoryUniqueId();

		//6. Generate Retrieve document request message
		String retrieveDoc = retrieveDocuments(reposiotryUniqueId, ids.get(0), homeCommunityId);
		OMElement retrieveDocRequest = OMUtil.xmlStringToOM(retrieveDoc);
		System.out.println("Request:\n" +retrieveDoc);
		
		//7. Send a Retrieve document set request
		ServiceClient retrieveDocSender = getRepositoryGateWayClient();
		OMElement retrieveDocResponse = retrieveDocSender.sendReceive(retrieveDocRequest);

		assertNotNull(retrieveDocResponse);

		String responseStatus;
		//8. Verify the response is correct
		List registryResponse = new ArrayList();
		for (Iterator it = retrieveDocResponse.getChildElements(); it.hasNext();) {
			OMElement obj = (OMElement) it.next();
			String type = obj.getLocalName();
			if (type.equals("RegistryResponse")) {
				registryResponse.add(obj);
			}
		}
		responseStatus = getRetrieveDocumentStatus(registryResponse);
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", responseStatus);
		
		//8. Verify the response returning 2 Documents
		List docs = new ArrayList();
		for (Iterator it = retrieveDocResponse.getChildElements(); it.hasNext();) {
			OMElement obj = (OMElement) it.next();
			String type = obj.getLocalName();
			if (type.equals("DocumentResponse")) {
				docs.add(obj);
			}
		}
		assertTrue(docs.size() == 1); 
		
		String result = retrieveDocResponse.toString();
		System.out.println("Result:\n" +result);
	}
	
   /**
    * XGR Retrieve multiple document, Generate request for the retrieval of a single document based on metadata returned.
    * Based on the XDSDocument.uniqueId, repositoryUniqueId, and homeCommunityId returned in metadata, 
    * issue a RetrieveDocumentSet transaction to retrieve two documents.
    * @throws Exception
    */	
	@Test
	public void testMultipleDocuments() throws Exception {
		//1. Submit a document first for a random patientId
		String patientId = generateAPatientId();
		String uuids = submitMultipleDocuments(patientId);
		
		//2. Generate StoredQuery request message
		String message = new CrossGatewayQueryTest().findDocumentsQuery(patientId, "Approved", "LeafClass");
		OMElement request = OMUtil.xmlStringToOM(message);			
		System.out.println("Request:\n" +request);

		//3. Send a StoredQuery
		ServiceClient sender = getRegistryGateWayClient();															 
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 
		
		//4. Get DocumentUniqueId from the response.
		List extrinsicObjects = getExtrinsicObjects(response);
		List<String> ids = getDocumentId(extrinsicObjects);
		
		//5. Get RepositoryUniqueId from the response.
		XdsRepositoryService xdsService = (XdsRepositoryService) XdsFactory.getInstance().getBean("repositoryService");
		String reposiotryUniqueId = xdsService.getRepositoryUniqueId();

		//6. Generate Retrieve document request message
		String retrieveDoc = retrieveDocuments(reposiotryUniqueId, ids, homeCommunityId);
		OMElement retrieveDocRequest = OMUtil.xmlStringToOM(retrieveDoc);
		System.out.println("Request:\n" +retrieveDoc);
		
		//7. Send a Retrieve document set request
		ServiceClient retrieveDocSender = getRepositoryGateWayClient();
		OMElement retrieveDocResponse = retrieveDocSender.sendReceive(retrieveDocRequest);

		assertNotNull(retrieveDocResponse);

		String responseStatus;
		//8. Verify the response is correct
		List registryResponse = new ArrayList();
		for (Iterator it = retrieveDocResponse.getChildElements(); it.hasNext();) {
			OMElement obj = (OMElement) it.next();
			String type = obj.getLocalName();
			if (type.equals("RegistryResponse")) {
				registryResponse.add(obj);
			}
		}
		responseStatus = getRetrieveDocumentStatus(registryResponse);
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", responseStatus);
		
		//8. Verify the response returning 2 Documents
		List docs = new ArrayList();
		for (Iterator it = retrieveDocResponse.getChildElements(); it.hasNext();) {
			OMElement obj = (OMElement) it.next();
			String type = obj.getLocalName();
			if (type.equals("DocumentResponse")) {
				docs.add(obj);
			}
		}
		assertTrue(docs.size() == 2); 
		
		String result = retrieveDocResponse.toString();
		System.out.println("Result:\n" +result);
	}
	

   /**
    * XGR Retrieve multiple document, Generate request for the retrieval of multiple documents.
    * Some from the local community, and some from a remote community. 
    * Based on the XDSDocument.uniqueId, repositoryUniqueId, and homeCommunityId returned in metadata, 
    * issue a RetrieveDocumentSet transaction to retrieve two documents.
    * <p>
    * Note:
    * This test submits the RetrieveDocumentSet request to the initiating gateway instead of the responding gateway.
    * 
    * @throws Exception
    */	
	@Test
	public void testIGMultipleDocuments() throws Exception {
		//1. Load one or more documents for the default patient: patientId
		
		//2. Generate StoredQuery request message
		String message = new CrossGatewayQueryTest().findDocumentsQuery(patientId, "Approved", "LeafClass");
		OMElement request = OMUtil.xmlStringToOM(message);			
		System.out.println("Request:\n" +request);

		//3. Send a StoredQuery
		ServiceClient sender = getRegistryGateWayClient();															 
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 
		
		//4. Get DocumentUniqueId from the response.
		List extrinsicObjects = getExtrinsicObjects(response);
		List<String> docIds = getDocumentId(extrinsicObjects);
		
		//5. Get RepositoryUniqueId from the response.
		XdsRepositoryService xdsService = (XdsRepositoryService) XdsFactory.getInstance().getBean("repositoryService");
		String reposiotryUniqueId = xdsService.getRepositoryUniqueId();

		//6. Generate Retrieve document request message
		List<String> ids = new ArrayList<String>();
		//Add the documents from the local community repository
		for (String docId : docIds) {
			ids.add(homeCommunityId);
			ids.add(reposiotryUniqueId);
			ids.add(docId);
		}		
//String 	reposiotryUniqueId = "1.3.6.1.4.1.21367.2010.1.2.1125";
//ids.add("urn:oid:1.3.6.1.4.1.21367.2010.1.2.2045"); 
////use the same repository as in the local community. In the real world, it should not be the same.
//ids.add(reposiotryUniqueId);
//// docId - hard coded for now
//ids.add("2.16.840.1.113883.3.65.2.1262392348530");
		
//		//Add the documents from a remote community (in the 198.160.211.53 server)
//		//remote home id
		String remoteHomeId = "urn:oid:1.3.6.1.4.1.21367.2010.1.2.2800";
//		ids.add(remoteHomeId); 
//		//use the same repository as in the local community. In the real world, it should not be the same.
//		ids.add(reposiotryUniqueId);
//		// docId - hard coded for now
//		ids.add("2.16.840.1.113883.3.65.2.1262451539643");
		
		String retrieveDoc = retrieveDocuments(ids);
		OMElement retrieveDocRequest = OMUtil.xmlStringToOM(retrieveDoc);
		System.out.println("Request:\n" +retrieveDoc);
		
		//7. Send a Retrieve document set request
		ServiceClient retrieveDocSender = getIGRetrieveServiceClient();
		OMElement retrieveDocResponse = retrieveDocSender.sendReceive(retrieveDocRequest);

		assertNotNull(retrieveDocResponse);

		String responseStatus;
		//8. Verify the response is correct
		List registryResponse = new ArrayList();
		for (Iterator it = retrieveDocResponse.getChildElements(); it.hasNext();) {
			OMElement obj = (OMElement) it.next();
			String type = obj.getLocalName();
			if (type.equals("RegistryResponse")) {
				registryResponse.add(obj);
			}
		}
		responseStatus = getRetrieveDocumentStatus(registryResponse);
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", responseStatus);
		
		//9. Verify the response returning more than 1 Document and each has a valid HomeCommunityId
		List<OMElement> docRequests = MetadataSupport.childrenWithLocalName(retrieveDocResponse, "DocumentResponse");
		assertNotNull (docRequests);

		for (OMElement documentRequest : docRequests) {
			OMElement homeElem = MetadataSupport.firstChildWithLocalName(documentRequest, "HomeCommunityId");
			assertNotNull (homeElem);
			String home = homeElem.getText();
			assertNotNull (home);
			assertTrue(home.equals(homeCommunityId) || home.equals(remoteHomeId));
		}
		assertTrue(docRequests.size() >= 1); 
		
		String result = retrieveDocResponse.toString();
		System.out.println("Result:\n" +result);
	}
	
	/**
	 * 
	 * @param ids A list of triplets ordered by homeId, repositoryUniqueId and documentUniqueId 
	 * @return
	 */
	private String retrieveDocuments(List<String> ids) {

		String request = "<xdsb:RetrieveDocumentSetRequest xmlns:xdsb=\"urn:ihe:iti:xds-b:2007\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:ihe:iti:xds-b:2007 ../schema/IHE/XDS.b_DocumentRepository.xsd\">\n";
			for(int i=0; i<ids.size(); i=i+3) {
				request+= "  <xdsb:DocumentRequest>\n"
				+ "    <xdsb:HomeCommunityId>"
				+ ids.get(i)
				+ "</xdsb:HomeCommunityId>\n"
				+ "    <xdsb:RepositoryUniqueId>"
				+ ids.get(i+1)
				+ "</xdsb:RepositoryUniqueId>\n"
				+ "    <xdsb:DocumentUniqueId>"
				+ ids.get(i+2)
				+ "</xdsb:DocumentUniqueId>\n"
				+ "  </xdsb:DocumentRequest>\n";
			}
			request	+= "</xdsb:RetrieveDocumentSetRequest>\n";
		return request;
	}
		

	private String retrieveDocuments(String repoId, List<String> docIds, String home) {

		String request = "<xdsb:RetrieveDocumentSetRequest xmlns:xdsb=\"urn:ihe:iti:xds-b:2007\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:ihe:iti:xds-b:2007 ../schema/IHE/XDS.b_DocumentRepository.xsd\">\n";
			for(String docId: docIds){
				request+= "  <xdsb:DocumentRequest>\n"
				+ "    <xdsb:HomeCommunityId>"
				+ home
				+ "</xdsb:HomeCommunityId>\n"
				+ "    <xdsb:RepositoryUniqueId>"
				+ repoId
				+ "</xdsb:RepositoryUniqueId>\n"
				+ "    <xdsb:DocumentUniqueId>"
				+ docId
				+ "</xdsb:DocumentUniqueId>\n"
				+ "  </xdsb:DocumentRequest>\n";
			}
			request	+= "</xdsb:RetrieveDocumentSetRequest>\n";
		return request;
	}
	

	private String retrieveDocuments(String repoId, String docId, String home) {

		String request = "<xdsb:RetrieveDocumentSetRequest xmlns:xdsb=\"urn:ihe:iti:xds-b:2007\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:ihe:iti:xds-b:2007 ../schema/IHE/XDS.b_DocumentRepository.xsd\">\n"
				+ "  <xdsb:DocumentRequest>\n"
				+ "    <xdsb:HomeCommunityId>"
				+ home
				+ "</xdsb:HomeCommunityId>\n"
				+ "    <xdsb:RepositoryUniqueId>"
				+ repoId
				+ "</xdsb:RepositoryUniqueId>\n"
				+ "    <xdsb:DocumentUniqueId>"
				+ docId
				+ "</xdsb:DocumentUniqueId>\n"
				+ "  </xdsb:DocumentRequest>\n"
				+ "</xdsb:RetrieveDocumentSetRequest>\n";
		return request;
	}
		
	private NodeList getNodeCount(OMElement response, String type)throws ParserConfigurationException, IOException, SAXException, XPathExpressionException{
			
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		    domFactory.setNamespaceAware(false); // never forget this!
		    DocumentBuilder builder = domFactory.newDocumentBuilder();
		    Document doc = builder.parse(new StringInputStream(response.toString()));
		    
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = null;
			if (type.equalsIgnoreCase("ExtrinsicObject"))
				expr = xpath.compile("//AdhocQueryResponse/RegistryObjectList/ExtrinsicObject"); 
			if (type.equalsIgnoreCase("ObjectRef"))
				expr = xpath.compile("//AdhocQueryResponse/RegistryObjectList/ObjectRef"); 
			Object res = expr.evaluate(doc, XPathConstants.NODESET);
		    NodeList nodes = (NodeList) res;
			return nodes;
		}
	
	private List getExtrinsicObjects(OMElement element) {
		List extrinsicObjects = new ArrayList();
		for (Iterator it = element.getChildElements(); it.hasNext();) {
			OMElement obj = (OMElement) it.next();
			for (Iterator it1 = obj.getChildElements(); it1.hasNext();) {
				OMElement obj1 = (OMElement) it1.next();
				for (Iterator it2 = obj1.getChildElements(); it2.hasNext();) {
					OMElement obj2 = (OMElement) it2.next();
					String type = obj2.getLocalName();
					if (type.equals("ExternalIdentifier")) {
						extrinsicObjects.add(obj2);
					}
				}
			}
		}
		return extrinsicObjects;
	}
	
	private List<String> getDocumentId(List extrinsicObjects) {
		List<String> dicIds = new ArrayList<String>();
		for (Iterator<OMElement> it = extrinsicObjects.iterator(); it.hasNext();) {
			OMElement ele = it.next();
			String documentId = null;
			if (ele.getAttributeValue(new QName("identificationScheme")).equals("urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab")) {
				documentId = ele.getAttributeValue(new QName("value"));
			}
			if(documentId != null){
				dicIds.add(documentId);
			}
		}
		return dicIds;
	}
	
	private String getRetrieveDocumentStatus(List retrieveDoc) {
		String retrieveDocStatus = null;
		for (Iterator<OMElement> it = retrieveDoc.iterator(); it.hasNext();) {
			OMElement ele = it.next();
			retrieveDocStatus = ele.getAttributeValue(new QName("status"));
		}
		return retrieveDocStatus;

	}
}
