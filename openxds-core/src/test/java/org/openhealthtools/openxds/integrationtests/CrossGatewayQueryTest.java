package org.openhealthtools.openxds.integrationtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.Properties;

import java.io.IOException;
import java.util.Iterator;

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
import org.junit.Before;
import org.junit.Test;
import org.openhealthtools.common.utils.OMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.xml.bind.StringInputStream;


public class CrossGatewayQueryTest extends XdsTest{
	static String homeProperty;

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
	
	static {
		homeProperty = Properties.loader().getString("home.community.id");
	}

	/**
	 * This test initiate a FindDocuments Cross-Gateway Query (XGQ) to the XDS
	 * 	Registry server's Responding Gateway for a pre-determined Patient ID.
	 * 	Request LeafClass (full metadata) be returned.
	 * @throws Exception
	 */
	@Test
	public void testFindDocsLeafClass() throws Exception {
		//1. Submit a document first for a random patientId
		String patientId = generateAPatientId();
		String uuids = submitMultipleDocuments(patientId);
		
		//2. Generate StoredQuery request message
		String message = findDocumentsQuery(patientId, "Approved", "LeafClass");
		OMElement request = OMUtil.xmlStringToOM(message);			
		System.out.println("Request:\n" +request);

		//3. Send a StoredQuery
		ServiceClient sender = getRegistryGateWayClient();															 
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		//4. Verify the response is correct
		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
		
		//5. Verify that the 2 Documents found from the StoredQuery response. 
		NodeList count = getNodeCount(response, "ExtrinsicObject");
		assertTrue(count.getLength() == 2); 

		String result = response.toString();
		System.out.println("Result:\n" +result);
	}
	
	/**
	 * This test issues a FindDocuments Cross Gateway Query (XGQ)
	 *  to the XDS Registry's Receiving Gateway requesting ObjectRefs (document references) be returned. 
	 * @throws Exception
	 */
	@Test
	public void testFindDocsObjectRef() throws Exception {
		//1. Submit a document first for a random patientId
		String patientId = generateAPatientId();
		String uuids = submitMultipleDocuments(patientId);
		
		//2. Generate StoredQuery request message
		String message = findDocumentsQuery(patientId, "Approved", "ObjectRef");
		OMElement request = OMUtil.xmlStringToOM(message);			
		System.out.println("Request:\n" +request);

		//3. Send a StoredQuery
		ServiceClient sender = getRegistryGateWayClient();															 
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 
		
		//4. Verify the response is correct
		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
		
		//5. Verify that the 2 ObjectRefs found from the StoredQuery response. 
		NodeList count = getNodeCount(response, "ObjectRef");
		assertTrue(count.getLength() == 2); 
		
		String result = response.toString();
		System.out.println("Result:\n" +result);
	}
	
	/**
	 * Tis test initiate a GetDocuments Cross-Gateway Query (XGQ) to the XDS
	 * Registry server's Responding Gateway for documents discovered in
	 * test 12306. Request LeafClass be returned.
	 * @throws Exception
	 */
	@Test
	public void testGetDocuments() throws Exception {
		//1. Submit a document first for a random patientId
		String patientId = generateAPatientId();
		String uuids = submitMultipleDocuments(patientId);
		
		//2. Generate StoredQuery request message
		String message = GetDocumentsQuery(uuids, false, homeProperty);
		OMElement request = OMUtil.xmlStringToOM(message);			
		System.out.println("Request:\n" +request);

		//3. Send a StoredQuery
		ServiceClient sender = getRegistryGateWayClient();															 
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		//4. Verify the response is correct
		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
		
		//5. Verify that the 2 Documents found from the StoredQuery response. 
		NodeList count = getNodeCount(response, "ExtrinsicObject");
		assertTrue(count.getLength() == 2); 
		
		String result = response.toString();
		System.out.println("Result:\n" +result);
	}
	
	/**
	 * This test initiate a FindDocuments Cross-Gateway Query (XGQ) to the XDS
	 * 	Registry server's Responding Gateway for a pre-determined Patient ID.
	 * 	Request ObjectRefs be returned.
	 * @throws Exception
	 */
	@Test
	public void testFindDocusMulObjectRefs() throws Exception {
		//1. Submit a document first for a random patientId
		String patientId = generateAPatientId();
		String uuids = submitMultipleDocuments(patientId);
		
		//2. Generate StoredQuery request message
		String message = findDocumentsQuery(patientId, "Approved", "ObjectRef");
		OMElement request = OMUtil.xmlStringToOM(message);			
		System.out.println("Request:\n" +request);

		//3. Send a StoredQuery
		ServiceClient sender = getRegistryGateWayClient();															 
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		//4. Verify the response is correct
		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
		
		//5. Verify that the 2 Documents found from the StoredQuery response. 
		NodeList count = getNodeCount(response, "ObjectRef");
		assertTrue(count.getLength() == 2); 

		String result = response.toString();
		System.out.println("Result:\n" +result);
	}
	
	/**
	 * This test initiate a FindDocuments Cross-Gateway Query (XGQ) to the XDS
	 * 	Registry server's Responding Gateway for a pre-determined Patient ID.
	 * 	Request LeafClass (full metadata) be returned with Approved status.
	 * @throws Exception
	 */
	@Test
	public void testFindDocsApproved() throws Exception {
		//1. Submit a document first for a random patientId
		String patientId = generateAPatientId();
		String uuids = submitMultipleDocuments(patientId);
		
		//2. Generate StoredQuery request message
		String message = findDocumentsQuery(patientId, "Approved", "LeafClass");
		OMElement request = OMUtil.xmlStringToOM(message);			
		System.out.println("Request:\n" +request);

		//3. Send a StoredQuery
		ServiceClient sender = getRegistryGateWayClient();															 
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		//4. Verify the response is correct
		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
		
		//5. Verify that the 2 Documents found from the StoredQuery response. 
		NodeList count = getNodeCount(response, "ExtrinsicObject");
		assertTrue(count.getLength() == 2); 

		//6. Verify that the Document Status is Approved.
		NodeList statusCount = getElementValue(response, "ExtrinsicObject", "status", "urn:oasis:names:tc:ebxml-regrep:StatusType:Approved");
		assertTrue(statusCount.getLength() == 2); 
		
		String result = response.toString();
		System.out.println("Result:\n" +result);
	}
	
	/**
	 * This test initiate a FindDocuments Cross-Gateway Query (XGQ) to the XDS
	 * 	Registry server's Initiating Gateway for a pre-determined Patient ID.
	 * 	Request LeafClass (full metadata) be returned.
	 * @throws Exception
	 */
	@Test
	public void testIGFindDocsLeafClass() throws Exception {
		//1. Submit one or more document first for the default patientId
		
		//2. Generate StoredQuery request message
		String message = findDocumentsQuery(patientId, "Approved", "LeafClass");
		OMElement request = OMUtil.xmlStringToOM(message);			
		System.out.println("Request:\n" +request);

		//3. Send a StoredQuery
		ServiceClient sender = getIGQueryServiceClient();															 
		OMElement response = sender.sendReceive( request );		
		assertNotNull(response); 

		//4. Verify the response is correct
		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
		
		//5. Verify document count
		NodeList count = getNodeCount(response, "ExtrinsicObject");
		String result = response.toString();
		System.out.println("Result:\n" +result);

		assertTrue(count.getLength() >= 1); 

		System.out.println("Result:\n" +result);
		
		//6. Verify all ExtrinsicObject have a home attribute from the XGQ response
		OMElement registry_object_list = MetadataSupport.firstChildWithLocalName(response, "RegistryObjectList"); 
		for (Iterator it=registry_object_list.getChildElements(); it.hasNext(); ) {
			OMElement registry_object = (OMElement) it.next();

			String objectHome = registry_object.getAttributeValue(MetadataSupport.home_qname);
			assertTrue("homeId is NULL", objectHome!=null && !objectHome.equals("")); 
		}    	

	}
	
	/**
	 * Tis test initiate a GetDocuments Cross-Gateway Query (XGQ) to the XDS
	 * Registry server's Initiating Gateway for documents discovered in
	 * test 12306. Request LeafClass be returned.
	 * @throws Exception
	 */
	@Test
	public void testIGGetDocuments() throws Exception {
		//1. Submit a document first for a random patientId
		String patientId = generateAPatientId();
		String uuids = submitMultipleDocuments(patientId);
		
		//2. Generate StoredQuery request message
		String message = GetDocumentsQuery(uuids, false, homeProperty);
		OMElement request = OMUtil.xmlStringToOM(message);			
		System.out.println("Request:\n" +request);

		//3. Send a StoredQuery
		ServiceClient sender = getIGQueryServiceClient();															 
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		//4. Verify the response is correct
		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
		
		//5. Verify that the 2 Documents found from the StoredQuery response. 
		NodeList count = getNodeCount(response, "ExtrinsicObject");
		assertTrue(count.getLength() == 2); 
		
		String result = response.toString();
		System.out.println("Result:\n" +result);
	}
	public String findDocumentsQuery(String patientId, String status, String retType){
		String request = "<query:AdhocQueryRequest xsi:schemaLocation=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0 ../schema/ebRS/query.xsd\" xmlns:query=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\" xmlns:rs=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0\">\n"+
		              	 " <query:ResponseOption returnComposedObjects=\"true\" returnType=\""+retType+"\"/>\n"+
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
	
	private String GetDocumentsQuery(String ids, boolean uniqueId, String home){
		String request = "<query:AdhocQueryRequest xsi:schemaLocation=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0 ../schema/ebRS/query.xsd\" xmlns:query=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\" xmlns:rs=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0\">\n"+
		              	 " <query:ResponseOption returnComposedObjects=\"true\" returnType=\"LeafClass\"/>\n"+
		              	 "  <rim:AdhocQuery id=\"urn:uuid:5c4f972b-d56b-40ac-a5fc-c8ca9b40b9d4\" home=\""+home+"\">\n";
		if (ids != null && uniqueId == true) {
			request +=   "   <rim:Slot name=\"$XDSDocumentEntryUniqueId\">\n"+
			         	 "     <rim:ValueList>\n" + 
			             "       <rim:Value>('"+ids+"')</rim:Value>\n" +  
			             "     </rim:ValueList>\n"+
			             "   </rim:Slot>\n";
		} else {
			request +=   "   <rim:Slot name=\"$XDSDocumentEntryEntryUUID\">\n" +
						 "     <rim:ValueList>\n" + 
						 "       <rim:Value>('"+ids+"')</rim:Value>\n" +  
						 "     </rim:ValueList>\n" +
						 "   </rim:Slot>\n";			
		}
        request +=       "  </rim:AdhocQuery>\n" +
                         "</query:AdhocQueryRequest>";
		
		return request;
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
		
	private NodeList getElementValue(OMElement response, String type, String element, String value)throws ParserConfigurationException, IOException, SAXException, XPathExpressionException{
			
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		    domFactory.setNamespaceAware(false); // never forget this!
		    DocumentBuilder builder = domFactory.newDocumentBuilder();
		    Document doc = builder.parse(new StringInputStream(response.toString()));
		    
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = null;
			if (type.equalsIgnoreCase("ExtrinsicObject"))
				expr = xpath.compile("//AdhocQueryResponse/RegistryObjectList/ExtrinsicObject[@"+element+"='"+value+"']"); 
			if (type.equalsIgnoreCase("ExternalIdentifier"))
				expr = xpath.compile("//AdhocQueryResponse/RegistryObjectList/ExtrinsicObject/ExternalIdentifier[@"+element+"='"+value+"']"); 
			Object res = expr.evaluate(doc, XPathConstants.NODESET);
		    NodeList nodes = (NodeList) res;
			return nodes;
	}
	
	
}
