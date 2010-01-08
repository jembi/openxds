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

import java.util.UUID;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.client.ServiceClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openhealthtools.common.utils.OMUtil;

import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.ConnectionHub;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.MinLowerLayerProtocol;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v231.segment.MSA;
import ca.uhn.hl7v2.parser.PipeParser;

/**
 * This is a Connectathon XDS_Patient_Feed test to load patient identifiers 
 * in the XDS Registry and to test the Registry's response to a merge.
 * <p>
 * The test scenario steps:
 * <ul>
 *  <li>Registered a surving patient.</li>
 *  <li>Registered a subsumed patient.</li>
 *  <li>Merge the two patients.</li>
 *  <li>Submit a document on the subsumed patient. The Registry is expected to reject the submission.</li>
 * </ul> 
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class MergePatientTest extends XdsTest {

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
	public void testPatientMerge() throws Exception {
        //1. Registry a surviving patient
		String msg = "MSH|^~\\&|OTHER_KIOSK|HIMSSSANDIEGO|XDSb_REG_MISYS|MISYS|20090512132906-0300||ADT^A04^ADT_A01|7723510070655179915|P|2.3.1\r" + 
	      "EVN||20090512132906-0300\r" +
	      "PID|||S123321^^^IHENA&1.3.6.1.4.1.21367.2010.1.2.300&ISO||IDENTITY^REGISTRY^S||19781208|M|||820 JORIE BLVD^^CHICAGO^IL^60523\r" +
	      "PV1||O|";
		SendPIX (msg);

		//2. Registry a non-surviving patient
		msg = "MSH|^~\\&|OTHER_KIOSK|HIMSSSANDIEGO|XDSb_REG_MISYS|MISYS|20090512132906-0300||ADT^A04^ADT_A01|7723510070655179915|P|2.3.1\r" + 
	      "EVN||20090512132906-0300\r" +
	      "PID|||N123321^^^IHENA&1.3.6.1.4.1.21367.2010.1.2.300&ISO||IDENTITY^REGISTRY^N||19781208|M|||820 JORIE BLVD^^CHICAGO^IL^60523\r" +
	      "PV1||O|";
		SendPIX (msg);
        
		//3. Merge the both patients
		msg = "MSH|^~\\&|OTHER_KIOSK|HIMSSSANDIEGO|XDSb_REG_MISYS|MISYS|20090512132906-0300||ADT^A40^ADT_A39|4143361005927619863|P|2.3.1\r" + 
	      "EVN||20090512132906-0300\r" +
	      "PID|||S123321^^^IHENA&1.3.6.1.4.1.21367.2010.1.2.300&ISO||IDENTITY^REGISTRY^S||19781208|M|\r" +
	      "MRG|N123321^^^IHENA&1.3.6.1.4.1.21367.2010.1.2.300&ISO\r" + 
	      "PV1||O|";
		SendPIX (msg);

 		//4 Submit a document which is expected to be rejected by the Registry
		
		String message = getStringFromInputStream( ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/submit_document.xml"));
		String document = getStringFromInputStream(ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/referral_summary.xml"));
		//replace document and submission set uniqueId variables with actual uniqueIds. 
		message = message.replace("$XDSDocumentEntry.uniqueId", "2.16.840.1.113883.3.65.2." + System.currentTimeMillis());
		message = message.replace("$XDSSubmissionSet.uniqueId", "1.3.6.1.4.1.21367.2009.1.2.108." + System.currentTimeMillis());
		message = message.replace("$patientId", "N123321^^^IHENA&amp;1.3.6.1.4.1.21367.2010.1.2.300&amp;ISO");
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
	
    private void SendPIX(String msg) throws Exception {
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
	
}
