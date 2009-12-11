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

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMText;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.description.WSDL2Constants;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openhealthexchange.openpixpdq.data.PatientIdentifier;
import org.openhealthexchange.openpixpdq.ihe.registry.HL7;
import org.openhealthtools.common.utils.OMUtil;
import org.openhealthtools.openxds.XdsFactory;
import org.openhealthtools.openxds.registry.api.XdsRegistryPatientService;

import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.ConnectionHub;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.MinLowerLayerProtocol;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v231.segment.MSA;
import ca.uhn.hl7v2.parser.PipeParser;

import com.misyshealthcare.connect.net.Identifier;

/**
 * The base class of all XDS TestCases.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public abstract class XdsTest {
	protected static String hostName;
	protected static String repositoryUrl;
	protected static String registryUrl;
	protected static String xcaRegistryUrl;
	protected static String xcaRepositoryUrl;
	protected static int pixRegistryPort;
	protected static String patientId;
	protected static String assigningAuthority;
	protected static boolean validatePatient = false;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		InputStream is = XdsTest.class.getClassLoader().getResourceAsStream("test.properties");
		if (is == null) { 
			throw new Exception("Cannot load test.propertises"); 
		}
		Properties properties = new java.util.Properties();
		try {
			properties.load(is);
		}
		catch (Exception e) {
			throw new Exception("Cannot load test.properties", e); 
		}
		hostName = properties.getProperty("pixRegistryHostName");
		repositoryUrl = properties.getProperty("repositoryUrl");
		registryUrl = properties.getProperty("registryUrl");
		pixRegistryPort = Integer.parseInt(properties.getProperty("pixRegistryPort"));
		patientId = properties.getProperty("patientId");
		assigningAuthority = properties.getProperty("assigningAuthority");
		validatePatient = (properties.getProperty("validatePatient").equals("false")) ? false : true;
		xcaRegistryUrl = properties.getProperty("xcaRegistryUrl");
		xcaRepositoryUrl = properties.getProperty("xcaRepositoryUrl");
		//Initialize openEMPI 
//		XdsRegistryPatientService ps = XdsFactory.getXdsRegistryPatientService();
//		XdsFactory.getInstance().getBean("context");
//		org.openhie.openempi.context.Context.startup();
//		org.openhie.openempi.context.Context.authenticate("admin", "admin");

	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

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

	/*
	 * Generates a new patient Id for testing 
	 */
	protected String generateAPatientId() throws Exception {
		String patientId = new Random().nextInt() + "^^^" + assigningAuthority;
		createPatient(patientId);
		return patientId;
	}

	
	protected void createPatient(String patientId) throws Exception {
		if (!validatePatient)
			return ;
		
		patientId = patientId.replace("&amp;", "&");
		//If it is a valid patient, then no need to re-create.
		if (isValidPatient(patientId))
			return; 
		
		String msg = "MSH|^~\\&|OTHER_KIOSK|HIMSSSANDIEGO|PAT_IDENTITY_X_REF_MGR_MISYS|ALLSCRIPTS|20090512132906-0300||ADT^A04^ADT_A01|7723510070655179915|P|2.3.1\r" + 
	      "EVN||20090512132906-0300\r" +
	      "PID|||"+ patientId +"||FARNSWORTH^STEVE||19781208|M|||820 JORIE BLVD^^CHICAGO^IL^60523\r" +
	      "PV1||O|";
		PipeParser pipeParser = new PipeParser();
		Message adt = pipeParser.parse(msg);
		ConnectionHub connectionHub = ConnectionHub.getInstance();
		Connection connection = connectionHub.attach(hostName, pixRegistryPort, new PipeParser(), MinLowerLayerProtocol.class);
		Initiator initiator = connection.getInitiator();
		Message response = initiator.sendAndReceive(adt);
		String responseString = pipeParser.encode(response);	        
		System.out.println("Received response:\n" + responseString);
 		MSA msa = (MSA)response.get("MSA");
		assertEquals("AA", msa.getAcknowledgementCode().getValue());
	}
	
	//patientId format example: 12321^^^&1.3.6.1.4.1.21367.2009.1.2.300&ISO
	private boolean isValidPatient(String patientId) throws Exception {
		String id = HL7.getIdFromCX(patientId);
		Identifier aa = HL7.getAssigningAuthorityFromCX(patientId);
		PatientIdentifier pi = new PatientIdentifier();
		pi.setId(id);
		pi.setAssigningAuthority(aa);

		XdsRegistryPatientService ps = XdsFactory.getXdsRegistryPatientService();
		boolean isValid = ps.isValidPatient(pi, null);	
		return isValid;
	}
	
	/**
	 * Submit one document to support ProvideAndRegisterDocumentSet-b (ITI-41)
	 * 
	 * @return XDSDocumentEntry.uuid
	 * @throws  
	 * @throws Exception 
	 */
	protected String submitOneDocument(String patientId) throws Exception {		
		String message = getStringFromInputStream( ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/submit_document.xml"));
		String document = getStringFromInputStream(ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/referral_summary.xml"));
		//replace document and submission set uniqueId variables with actual uniqueIds. 
		message = message.replace("$XDSDocumentEntry.uniqueId", "2.16.840.1.113883.3.65.2." + System.currentTimeMillis());
		message = message.replace("$XDSSubmissionSet.uniqueId", "1.3.6.1.4.1.21367.2009.1.2.108." + System.currentTimeMillis());
		//replace the document uuid.
		String uuid = "urn:uuid:" + UUID.randomUUID().toString();
		message = message.replace("$doc1", uuid);
		//replace the patient id
		if (patientId != null) 
			message = message.replace("$patientId", patientId);

		
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
		
		return uuid;
	}
	
	public String submitMultipleDocuments(String patientId) throws Exception {

		String message = getStringFromInputStream( ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/submit_multiple_documents.xml"));
		String document1 = getStringFromInputStream(ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/referral_summary.xml"));
		String document2 = getStringFromInputStream(ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/medical_summary.xml"));
		//replace document and submission set uniqueId variables with actual uniqueIds. 
		message = message.replace("$XDSDocumentEntry.uniqueId", "2.16.840.1.113883.3.65.2." + System.currentTimeMillis());
		message = message.replace("$XDSDocumentEntry.uniqueId1", "2.16.840.1.113883.3.65.2." + System.currentTimeMillis());
		message = message.replace("$XDSSubmissionSet.uniqueId", "1.3.6.1.4.1.21367.2009.1.2.108." + System.currentTimeMillis());
		//replace the document uuid.
		String uuid1 = getUUID();
		message = message.replace("$doc1", uuid1);
		String uuid2 = getUUID();
		message = message.replace("$doc2", uuid2);
		
		
		//replace the patient id
		if (patientId != null) 
			message = message.replace("$patientId", patientId);
		
		ServiceClient sender = getRepositoryServiceClient();			
		
		OMElement request = OMUtil.xmlStringToOM(message);			
		
		//Add a referral summary document
		request = addOneDocument(request, document1, uuid1);
		//Add a Discharge summary documents
		request = addOneDocument(request, document2, uuid2);

		System.out.println("Request:\n" +request);
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue());
		
		String result = response.toString();
		System.out.println("Result:\n" +result);
		
		String uuids = uuid1 +"','"+uuid2;
		return uuids;
	}
	
	protected String getUUID(){
		return "urn:uuid:" + UUID.randomUUID().toString();
	}
	//return folder unique Id
	protected String submitOneDocument2Folder(String patientId) throws Exception {
		String message = getStringFromInputStream( ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/add_document2_folder.xml"));
		String document1 = getStringFromInputStream(ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/referral_summary.xml"));
		//replace document , submission set and folder uniqueId variables with actual uniqueIds. 
		message = message.replace("$XDSDocumentEntry.uniqueId", "2.16.840.1.113883.3.65.2." + System.currentTimeMillis());
		message = message.replace("$XDSSubmissionSet.uniqueId", "1.3.6.1.4.1.21367.2009.1.2.108." + System.currentTimeMillis());
		String folderUniqueId = "2.16.840.1.113883.3.65.3." + System.currentTimeMillis();
		message = message.replace("$folder_uniqueid", folderUniqueId);
		//replace the documentId doc1 with an UUID
		String uuid = "urn:uuid:" + UUID.randomUUID().toString();
		message = message.replace("doc1", uuid);
		//replace the patient id
		message = message.replace("$patientId", patientId);
		
		ServiceClient sender = getRepositoryServiceClient();			
		
		OMElement request = OMUtil.xmlStringToOM(message);			
		
		//Add a referral summary document
		request = addOneDocument(request, document1, uuid);
		
        System.out.println("Request:\n" +request);
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue());

		String result = response.toString();
		System.out.println("Result:\n" +result);

		return folderUniqueId;
	}	
	/**
	 * Submits new document and replaces the document
	 * 
	 * @param patientId
	 * @return Document UUID
	 * @throws Exception
	 */
	
	public String replaceDocument(String patientId) throws Exception {
		//First submit a document
		String doc_uuid = submitOneDocument(patientId);

		//Then add a replacement doc
		String message = getStringFromInputStream( ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/document_replacement.xml"));
		String document1 = getStringFromInputStream(ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/medical_summary.xml"));
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
		
		return doc_uuid;
	}
	
	/**
	 * Submits document to the folder.
	 * @param patientId
	 * @return document UUID
	 * @throws Exception
	 */
	protected String submitDocument2Folder(String patientId) throws Exception {
		String message = getStringFromInputStream( ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/add_document2_folder.xml"));
		String document1 = getStringFromInputStream(ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/referral_summary.xml"));
		//replace document , submission set and folder uniqueId variables with actual uniqueIds. 
		message = message.replace("$XDSDocumentEntry.uniqueId", "2.16.840.1.113883.3.65.2." + System.currentTimeMillis());
		message = message.replace("$XDSSubmissionSet.uniqueId", "1.3.6.1.4.1.21367.2009.1.2.108." + System.currentTimeMillis());
		String folderUniqueId = "2.16.840.1.113883.3.65.3." + System.currentTimeMillis();
		message = message.replace("$folder_uniqueid", folderUniqueId);
		//replace the documentId doc1 with an UUID
		String uuid = "urn:uuid:" + UUID.randomUUID().toString();
		message = message.replace("doc1", uuid);
		//replace the patient id
		message = message.replace("$patientId", patientId);
		
		ServiceClient sender = getRepositoryServiceClient();			
		
		OMElement request = OMUtil.xmlStringToOM(message);			
		
		//Add a referral summary document
		request = addOneDocument(request, document1, uuid);
		
        System.out.println("Request:\n" +request);
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue());

		String result = response.toString();
		System.out.println("Result:\n" +result);

		return uuid;
	}	
	
	/**
	 * Submits document to the SubmisstionSet And Folder.
	 * @param patientId
	 * @return document UUID
	 * @throws Exception
	 */
	protected String submitDocument2SubmissionSet(String patientId) throws Exception {
		String message = getStringFromInputStream( ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/add_document2_folder.xml"));
		String document1 = getStringFromInputStream(ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/referral_summary.xml"));
		//replace document , submission set and folder uniqueId variables with actual uniqueIds. 
		message = message.replace("$XDSDocumentEntry.uniqueId", "2.16.840.1.113883.3.65.2." + System.currentTimeMillis());
		String subUniqueId =  "1.3.6.1.4.1.21367.2009.1.2.108." + System.currentTimeMillis();
		message = message.replace("$XDSSubmissionSet.uniqueId", subUniqueId);
		String folderUniqueId = "2.16.840.1.113883.3.65.3." + System.currentTimeMillis();
		message = message.replace("$folder_uniqueid", folderUniqueId);
		//replace the documentId doc1 with an UUID
		String uuid = "urn:uuid:" + UUID.randomUUID().toString();
		message = message.replace("doc1", uuid);
		//replace the patient id
		message = message.replace("$patientId", patientId);
		
		ServiceClient sender = getRepositoryServiceClient();			
		
		OMElement request = OMUtil.xmlStringToOM(message);			
		
		//Add a referral summary document
		request = addOneDocument(request, document1, uuid);
		
        System.out.println("Request:\n" +request);
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue());

		String result = response.toString();
		System.out.println("Result:\n" +result);

		return subUniqueId;
	}	

	
	protected ServiceClient getRepositoryServiceClient() throws AxisFault {
		ConfigurationContext configctx = getContext();
		ServiceClient sender = new ServiceClient(configctx,null);
		String action = "urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b";
		boolean enableMTOM = true;
		sender.setOptions(getOptions(action, enableMTOM, repositoryUrl));
		sender.engageModule("addressing");			
		return sender;
	}

	protected ServiceClient getRegistryServiceClient() throws AxisFault {
        ConfigurationContext configctx = getContext();
		ServiceClient sender = new ServiceClient(configctx,null);
		String action = "urn:ihe:iti:2007:RegistryStoredQuery";
		boolean enableMTOM = false;
		sender.setOptions(getOptions(action, enableMTOM, registryUrl));
		sender.engageModule("addressing");				
		return sender;
	}

	protected ServiceClient getRetrieveDocumentServiceClient() throws AxisFault{
		ConfigurationContext configctx = getContext();
		ServiceClient sender = new ServiceClient(configctx,null);
		String action = "urn:ihe:iti:2007:RetrieveDocumentSet";
		boolean enableMTOM = true;
		sender.setOptions(getOptions(action, enableMTOM, repositoryUrl));
		sender.engageModule("addressing");
		return sender;
	}
	
	protected ServiceClient getRegistryGateWayClient() throws AxisFault {
        ConfigurationContext configctx = getContext();
		ServiceClient sender = new ServiceClient(configctx,null);
		String action = "urn:ihe:iti:2007:CrossGatewayQuery";
		boolean enableMTOM = false;
		sender.setOptions(getOptions(action, enableMTOM, xcaRegistryUrl));
		sender.engageModule("addressing");				
		return sender;
	}
	
	protected ServiceClient getRepositoryGateWayClient() throws AxisFault {
        ConfigurationContext configctx = getContext();
		ServiceClient sender = new ServiceClient(configctx,null);
		String action = "urn:ihe:iti:2007:CrossGatewayRetrieve";
		boolean enableMTOM = true;
		sender.setOptions(getOptions(action, enableMTOM, xcaRepositoryUrl));
		sender.engageModule("addressing");				
		return sender;
	}
	private ConfigurationContext getContext() throws AxisFault {
		//String repository = "c:\\tools\\axis2-1.5\\repository\\modules\\addressing-1.5.mar";        
//		String repository = "c:\\tools\\axis2-1.5\\repository";        
//		String axis2xml = "c:\\tools\\axis2-1.5\\conf\\axis2.xml";        
//      ConfigurationContext configctx = ConfigurationContextFactory
//      .createConfigurationContextFromFileSystem(repository, axis2xml);
        URL axis2repo = XdsTest.class.getResource("/axis2repository");
        URL axis2testxml = XdsTest.class.getResource("/axis2_test.xml");
         ConfigurationContext configctx = ConfigurationContextFactory
         .createConfigurationContextFromFileSystem(axis2repo.getPath(), axis2testxml.getPath());
        return configctx; 
	}
	
	protected Options getOptions(String action, boolean enableMTOM, String url) {
		Options options = new Options();
		options.setAction(action);		
	    options.setProperty(WSDL2Constants.ATTRIBUTE_MUST_UNDERSTAND,"1");
	    options.setTo( new EndpointReference(url) );
		options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
//		try {
//			String from = InetAddress.getLocalHost().getHostAddress();	
//			options.setFrom(new EndpointReference(from));
//		}catch(UnknownHostException e) {
//			//ignore From
//		}
		if (enableMTOM)
			options.setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
		else
			options.setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_FALSE);
		//use SOAP12, 
		options.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
		return options;
	}
	
	protected OMElement addOneDocument(OMElement request, String document, String documentId) throws IOException {
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace ns = fac.createOMNamespace("urn:ihe:iti:xds-b:2007" , null);
		OMElement docElem = fac.createOMElement("Document", ns);
		docElem.addAttribute("id", documentId, null);

        // A string, turn it into an StreamSource
	    DataSource ds = new ByteArrayDataSource(document, "text/xml"); 
		DataHandler handler = new DataHandler(ds);
		 
        OMText binaryData = fac.createOMText(handler, true);
        docElem.addChild(binaryData);

        Iterator iter = request.getChildrenWithLocalName("SubmitObjectsRequest");
        OMElement submitObjectsRequest = null;
        for (;iter.hasNext();) {
        	submitObjectsRequest = (OMElement)iter.next();
        	if (submitObjectsRequest != null)
        		break;
        }
        submitObjectsRequest.insertSiblingAfter(docElem);
        return request;
	}
	
	protected String getStringFromInputStream(InputStream in) throws java.io.IOException {
		int count;
		byte[] by = new byte[256];

		StringBuffer buf = new StringBuffer();
		while ( (count=in.read(by)) > 0 ) {
			for (int i=0; i<count; i++) {
				by[i] &= 0x7f;
			}
			buf.append(new String(by,0,count));
		}
		return new String(buf);
	}
	
}
