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
		if (type.equalsIgnoreCase("findDocuments"))
			expr = xpath.compile("//AdhocQueryResponse/RegistryObjectList/ExtrinsicObject/ExternalIdentifier[@identificationScheme='urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427' and @value='"+patientId+"']"); 
		else if (type.equalsIgnoreCase("findFolders"))
			expr = xpath.compile("//AdhocQueryResponse/RegistryObjectList/RegistryPackage/ExternalIdentifier[@identificationScheme='urn:uuid:f64ffdf0-4b97-4e06-b79f-a52b38ec2f8a' and @value='"+patientId+"']"); 
		else if (type.equalsIgnoreCase("findSubmissionSets"))
			expr = xpath.compile("//AdhocQueryResponse/RegistryObjectList/RegistryPackage/ExternalIdentifier[@identificationScheme='urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427' and @value='"+patientId+"']"); 
		
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

}
