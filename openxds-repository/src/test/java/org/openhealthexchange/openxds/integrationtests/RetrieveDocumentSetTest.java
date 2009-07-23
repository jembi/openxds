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


import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
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
 */
public class RetrieveDocumentSetTest {
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
	 */
	@Test
	public void testRetrieveDocumentSetb() {
		String message = "<xdsb:RetrieveDocumentSetRequest xmlns:xdsb=\"urn:ihe:iti:xds-b:2007\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:ihe:iti:xds-b:2007 ../schema/IHE/XDS.b_DocumentRepository.xsd\">" +
				"  <xdsb:DocumentRequest>" +
				"    <xdsb:RepositoryUniqueId>1.3.6.1.4.1.21367.2009.1.2.701</xdsb:RepositoryUniqueId>" +
				"    <xdsb:DocumentUniqueId>2.16.840.1.113883.3.65.2.1248349777382.1</xdsb:DocumentUniqueId>" +
				"  </xdsb:DocumentRequest>" +
				"</xdsb:RetrieveDocumentSetRequest>";
		
		Options options = new Options();
		options.setAction("urn:ihe:iti:2007:RetrieveDocumentSet");		
	    options.setProperty(WSDL2Constants.ATTRIBUTE_MUST_UNDERSTAND,"1");
	    options.setTo( new EndpointReference(repositoryUrl) );
		options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
		options.setProperty(Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
		//use SOAP12, 
		options.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
//		options.setMessageId("id");
//		options.setReplyTo(replyTo); 
		try{
			ServiceClient sender = new ServiceClient();
			sender.setOptions(options);
			sender.engageModule("addressing");			
			
			OMElement response = sender.sendReceive( OMUtil.xmlStringToOM(message) );
			assertNotNull(response); 
			
			String result = response.toString();
			System.out.println("Result:\n" +result);
		} catch(AxisFault e) {
			e.printStackTrace();
			fail("testRetrieveDocumentSetb Failed");
		} catch(XMLStreamException e) {
			e.printStackTrace();
			fail("testRetrieveDocumentSetb Failed");
		}
	}

}
