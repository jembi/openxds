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
import gov.nist.registry.common2.registry.MetadataSupport;

import java.util.List;
import java.util.Random;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.io.IOUtils;
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
 *  <li>Registered a surviving patient, and submit a document on this patient</li>
 *  <li>Registered a subsumed patient, and submit a document on this patient</li>
 *  <li>Merge the two patients.</li>
 *  <li>Query the surviving patient, and two documents should be returned</li>
 *  <li>Query the subsumed patient, and no documents should be returned (This is not an error case)</li>
 *  <li>Submit a document on the subsumed patient. The Registry is expected to reject the submission.
 *  And return the UnknowPatientID error.
 *  </li>
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
		String id = Integer.toString(new Random().nextInt());
		String survivingPatientId = "S" + id ;
		String subsumedPatientId = "N" + id;
        //1. Registry a surviving patient
		String msg = "MSH|^~\\&|OTHER_KIOSK|HIMSSSANDIEGO|XDSb_REG_MISYS|MISYS|20090512132906-0300||ADT^A04^ADT_A01|7723510070655179915|P|2.3.1\r" + 
	      "EVN||20090512132906-0300\r" +
	      "PID|||"+survivingPatientId+"^^^IHENA&1.3.6.1.4.1.21367.2010.1.2.300&ISO||IDENTITY^REGISTRY^S||19781208|M|||820 JORIE BLVD^^CHICAGO^IL^60523\r" +
	      "PV1||O|";
		SendPIX (msg);
		submitOneDocument(survivingPatientId + "^^^IHENA&amp;1.3.6.1.4.1.21367.2010.1.2.300&amp;ISO");
		//2. Registry a non-surviving patient
		msg = "MSH|^~\\&|OTHER_KIOSK|HIMSSSANDIEGO|XDSb_REG_MISYS|MISYS|20090512132906-0300||ADT^A04^ADT_A01|7723510070655179915|P|2.3.1\r" + 
	      "EVN||20090512132906-0300\r" +
	      "PID|||"+subsumedPatientId+"^^^IHENA&1.3.6.1.4.1.21367.2010.1.2.300&ISO||IDENTITY^REGISTRY^N||19781208|M|||820 JORIE BLVD^^CHICAGO^IL^60523\r" +
	      "PV1||O|";
		SendPIX (msg);
		submitOneDocument(subsumedPatientId+"^^^IHENA&amp;1.3.6.1.4.1.21367.2010.1.2.300&amp;ISO");
        
		//3. Merge the both patients
		msg = "MSH|^~\\&|OTHER_KIOSK|HIMSSSANDIEGO|XDSb_REG_MISYS|MISYS|20090512132906-0300||ADT^A40^ADT_A39|4143361005927619863|P|2.3.1\r" + 
	      "EVN||20090512132906-0300\r" +
	      "PID|||"+survivingPatientId+"^^^IHENA&1.3.6.1.4.1.21367.2010.1.2.300&ISO||IDENTITY^REGISTRY^S||19781208|M|\r" +
	      "MRG|"+subsumedPatientId+"^^^IHENA&1.3.6.1.4.1.21367.2010.1.2.300&ISO\r" + 
	      "PV1||O|";
		SendPIX (msg);

		//4 Verify that the surviving patient has 2 documents:
		int docNumber = verifyDocuments(survivingPatientId);
		assertEquals(2, docNumber);

		//5 Verify that the subsumed patient has 0 document:
		docNumber = verifyDocuments(subsumedPatientId);
		assertEquals(0, docNumber);

		//6 Submit a document which is expected to be rejected by the Registry
		
		String message = IOUtils.toString( ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/submit_document.xml"));
		String document = IOUtils.toString(ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/referral_summary.xml"));
		//replace document and submission set uniqueId variables with actual uniqueIds. 
		message = message.replace("$XDSDocumentEntry.uniqueId", "2.16.840.1.113883.3.65.2." + System.currentTimeMillis());
		message = message.replace("$XDSSubmissionSet.uniqueId", "1.3.6.1.4.1.21367.2009.1.2.108." + System.currentTimeMillis());
		message = message.replace("$patientId", subsumedPatientId+"^^^IHENA&amp;1.3.6.1.4.1.21367.2010.1.2.300&amp;ISO");
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
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure", status.getAttributeValue());

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
	
	public int verifyDocuments(String patientId) throws Exception {
		String message = findDocumentsQuery(patientId + "^^^IHENA&amp;1.3.6.1.4.1.21367.2010.1.2.300&amp;ISO");
		OMElement request = OMUtil.xmlStringToOM(message);			

		ServiceClient sender = getRegistryServiceClient();															 
		OMElement response = sender.sendReceive( request );
		assertNotNull(response); 

		// Verify the response is correct
		OMAttribute status = response.getAttribute(new QName("status"));
		assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
		System.out.println("Response:\n" +response.toString());
		
		OMElement robList = MetadataSupport.firstChildWithLocalName(response, "RegistryObjectList");
		List objects = MetadataSupport.childrenWithLocalName(robList, "ObjectRef");
		return objects.size();
	}

	
	private String findDocumentsQuery(String patientId){
		String request = "<query:AdhocQueryRequest xsi:schemaLocation=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0 ../schema/ebRS/query.xsd\" xmlns:query=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:rim=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\" xmlns:rs=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0\">\n"+
		              	 " <query:ResponseOption returnComposedObjects=\"true\" returnType=\"ObjectRef\"/>\n"+
		              	 "  <rim:AdhocQuery id=\"urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d\">\n";
		if (patientId != null) {
			request +=   "   <rim:Slot name=\"$XDSDocumentEntryPatientId\">\n"+
			         	 "     <rim:ValueList>\n" + 
			             "       <rim:Value>'"+patientId+"'</rim:Value>\n" +
			             "     </rim:ValueList>\n"+
			             "   </rim:Slot>\n";
		}
		
			request +=   "   <rim:Slot name=\"$XDSDocumentEntryStatus\">\n" +
						 "     <rim:ValueList>\n" + 
						 "       <rim:Value>('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved')</rim:Value>\n" +
						 "     </rim:ValueList>\n" +
						 "   </rim:Slot>\n";			
			
        request +=       "  </rim:AdhocQuery>\n" +
                         "</query:AdhocQueryRequest>";
		
		return request;
	}
 
}
