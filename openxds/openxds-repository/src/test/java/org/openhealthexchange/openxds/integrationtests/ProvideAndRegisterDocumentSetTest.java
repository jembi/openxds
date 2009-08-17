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
package org.openhealthexchange.openxds.integrationtests;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMAbstractFactory;
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
import org.junit.Before;
import org.junit.Test;
import org.openhealthexchange.common.utils.OMUtil;

/**
 * This class is an integrated test for IHE transaction ITI-43, namely,
 * RetrieveDocumentSet-b.
 *  
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 * @author <a href="mailto:rasakannu.palaniyandi@misys.com">raja</a>
 */
public class ProvideAndRegisterDocumentSetTest {
	private static final String repositoryUrl = "http://localhost:8020/axis2/services/xdsrepositoryb";
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
	 * Test method for RetrieveDocumentSet-b (ITI-43)
	 * @throws  
	 * @throws Exception 
	 */
	@Test
	public void testSubmitDocument() throws Exception {
		String message = getStringFromInputStream( ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/submit_document.xml"));
		String document = getStringFromInputStream(ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/referral_summary.xml"));
		//replace document and submission set uniqueId variables with actual uniqueIds. 
		message = message.replace("$XDSDocumentEntry.uniqueId", "2.16.840.1.113883.3.65.2." + System.currentTimeMillis());
		message = message.replace("$XDSSubmissionSet.uniqueId", "1.3.6.1.4.1.21367.2009.1.2.108." + System.currentTimeMillis());
		
		Options options = new Options();
		options.setAction("urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b");		
	    options.setProperty(WSDL2Constants.ATTRIBUTE_MUST_UNDERSTAND,"1");
	    options.setTo( new EndpointReference(repositoryUrl) );
		options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
		options.setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
		//use SOAP12, 
		options.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
		try{
			
			  String repository = "c:\\axis-1.4.1\\repository\\modules\\addressing-1.41.mar";        
	        ConfigurationContext configctx = ConfigurationContextFactory
	        .createConfigurationContextFromFileSystem(repository, null);
			ServiceClient sender = new ServiceClient(configctx,null);
			sender.setOptions(options);
			sender.engageModule("addressing");	
			
			OMElement request = OMUtil.xmlStringToOM(message);			
			
			//Add a document
			OMFactory fac = OMAbstractFactory.getOMFactory();
			OMNamespace ns = fac.createOMNamespace("urn:ihe:iti:xds-b:2007" , null);
			OMElement docElem = fac.createOMElement("Document", ns);
			docElem.addAttribute("id", "doc1", null);

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
            
			System.out.println("Request:\n" +request);
			OMElement response = sender.sendReceive( request );
			assertNotNull(response); 
			String result = response.toString();
			System.out.println("Result:\n" +result);
		} catch(AxisFault e) {
			e.printStackTrace();
			fail("tstSubmitDocument Failed");
		} catch(XMLStreamException e) {
			e.printStackTrace();
			fail("tstSubmitDocument Failed");
		}catch (Exception e) {
			System.out.println("error"+e);
		}
	}
	@Test
	public void testSubmitMultipleDocument() throws Exception {
		String message = getStringFromInputStream( ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/submit_multiple_documents.xml"));
		String document1 = getStringFromInputStream(ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/referral_summary.xml"));
		String document2 = getStringFromInputStream(ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/MedicalSummary.xml"));
		//replace document and submission set uniqueId variables with actual uniqueIds. 
		message = message.replace("$XDSDocumentEntry.uniqueId", "2.16.840.1.113883.3.65.2." + System.currentTimeMillis());
		message = message.replace("$XDSDocumentEntry.uniqueId1", "2.16.840.1.113883.3.65.2." + System.currentTimeMillis());
		message = message.replace("$XDSSubmissionSet.uniqueId", "1.3.6.1.4.1.21367.2009.1.2.108." + System.currentTimeMillis());
		
		Options options = new Options();
		options.setAction("urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b");		
	    options.setProperty(WSDL2Constants.ATTRIBUTE_MUST_UNDERSTAND,"1");
	    options.setTo( new EndpointReference(repositoryUrl) );
		options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
		options.setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
		//use SOAP12, 
		options.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
		try{
			
			String repository = "c:\\axis-1.4.1\\repository\\modules\\addressing-1.41.mar";        
	        ConfigurationContext configctx = ConfigurationContextFactory
	        .createConfigurationContextFromFileSystem(repository, null);
			ServiceClient sender = new ServiceClient(configctx,null);
			sender.setOptions(options);
			sender.engageModule("addressing");	
			
			OMElement request = OMUtil.xmlStringToOM(message);			
			
			//Add a referral summary document
			OMFactory fac = OMAbstractFactory.getOMFactory();
			OMNamespace ns = fac.createOMNamespace("urn:ihe:iti:xds-b:2007" , null);
			OMElement docElem = fac.createOMElement("Document", ns);
			docElem.addAttribute("id", "doc1", null);
			
			//Add a Discharge summary documents
			OMElement docElem1 = fac.createOMElement("Document", ns);
			docElem1.addAttribute("id", "doc2", null);
			

            // A string, turn it into an StreamSource
		    DataSource ds = new ByteArrayDataSource(document1, "text/xml"); 
			DataHandler handler = new DataHandler(ds);			 
            OMText binaryData = fac.createOMText(handler, true);
            
            DataSource ds1 = new ByteArrayDataSource(document2, "text/xml"); 
			DataHandler handler1 = new DataHandler(ds1);			 
            OMText binaryData1 = fac.createOMText(handler1, true);
            
            docElem.addChild(binaryData);
            docElem1.addChild(binaryData1);
            
            Iterator iter = request.getChildrenWithLocalName("SubmitObjectsRequest");
            OMElement submitObjectsRequest = null;
            for (;iter.hasNext();) {
            	submitObjectsRequest = (OMElement)iter.next();
            	if (submitObjectsRequest != null)
            		break;
            }
            submitObjectsRequest.insertSiblingAfter(docElem);
            submitObjectsRequest.insertSiblingAfter(docElem1);
            
			System.out.println("Request:\n" +request);
			OMElement response = sender.sendReceive( request );
			assertNotNull(response); 
			String result = response.toString();
			System.out.println("Result:\n" +result);
		} catch(AxisFault e) {
			e.printStackTrace();
			fail("tstSubmitDocument Failed");
		} catch(XMLStreamException e) {
			e.printStackTrace();
			fail("tstSubmitDocument Failed");
		}catch (Exception e) {
			System.out.println("error"+e);
		}
	}
	
	
	@Test
	public void testAddDocument2Folder() throws Exception {
		String message = getStringFromInputStream( ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/add_document2_folder.xml"));
		String document1 = getStringFromInputStream(ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/referral_summary.xml"));
		//replace document , submission set and folder uniqueId variables with actual uniqueIds. 
		message = message.replace("$XDSDocumentEntry.uniqueId", "2.16.840.1.113883.3.65.2." + System.currentTimeMillis());
		message = message.replace("$XDSSubmissionSet.uniqueId", "1.3.6.1.4.1.21367.2009.1.2.108." + System.currentTimeMillis());
		message = message.replace("$folder_uniqueid", "2.16.840.1.113883.3.65.3." + System.currentTimeMillis());
		
		Options options = new Options();
		options.setAction("urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b");		
	    options.setProperty(WSDL2Constants.ATTRIBUTE_MUST_UNDERSTAND,"1");
	    options.setTo( new EndpointReference(repositoryUrl) );
		options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
		options.setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
		//use SOAP12, 
		options.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
		try{
			
			String repository = "c:\\axis-1.4.1\\repository\\modules\\addressing-1.41.mar";        
	        ConfigurationContext configctx = ConfigurationContextFactory
	        .createConfigurationContextFromFileSystem(repository, null);
			ServiceClient sender = new ServiceClient(configctx,null);
			sender.setOptions(options);
			sender.engageModule("addressing");	
			
			OMElement request = OMUtil.xmlStringToOM(message);			
			
			//Add a referral summary document
			OMFactory fac = OMAbstractFactory.getOMFactory();
			OMNamespace ns = fac.createOMNamespace("urn:ihe:iti:xds-b:2007" , null);
			OMElement docElem = fac.createOMElement("Document", ns);
			docElem.addAttribute("id", "doc1", null);
			
		
            // A string, turn it into an StreamSource
		    DataSource ds = new ByteArrayDataSource(document1, "text/xml"); 
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
            System.out.println("Request:\n" +request);
			OMElement response = sender.sendReceive( request );
			assertNotNull(response); 
			String result = response.toString();
			System.out.println("Result:\n" +result);
		} catch(AxisFault e) {
			e.printStackTrace();
			fail("tstSubmitDocument Failed");
		} catch(XMLStreamException e) {
			e.printStackTrace();
			fail("tstSubmitDocument Failed");
		}catch (Exception e) {
			System.out.println("error"+e);
		}
	}
	
	
	static public String getStringFromInputStream(InputStream in) throws java.io.IOException {
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
