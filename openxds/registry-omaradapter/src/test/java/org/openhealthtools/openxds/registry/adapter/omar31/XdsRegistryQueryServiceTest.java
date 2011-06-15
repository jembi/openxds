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

package org.openhealthtools.openxds.registry.adapter.omar31;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freebxml.omar.common.BindingUtility;
import org.openhealthtools.openxds.registry.api.RegistryStoredQueryContext;
import org.openhealthtools.openxds.registry.api.XdsRegistryQueryService;

  /**
   * The XDSRegistryQueryManagerTest which tests the query operations to 
   * XDS Registry objects.
   * 
   * @author <a href="mailto:anilkumar.reddy@misys.com">Anil kumar</a>
   */
  public class XdsRegistryQueryServiceTest extends TestCase {
	private static final Log log =  LogFactory.getLog(XdsRegistryQueryServiceTest.class);
	protected static BindingUtility bu = BindingUtility.getInstance();
	protected static XdsRegistryQueryService queryManager = new  XdsRegistryQueryServiceImpl();
	protected static ConversionHelper helper = ConversionHelper.getInstance();
	private static final String FindDocumentsId = "urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d";
	private static final String FindFoldersId = "urn:uuid:958f3006-baad-4929-a4de-ff1114824431";
	private static final String FindSubmissionSetsId = "urn:uuid:f26abbcb-ac74-4422-8a30-edb644bbc1a9";
	//private static final String GetAllId = "urn:uuid:10b545ea-725c-446d-9b95-8aeb444eddf3";
	private static final String GetDocumentsId = "urn:uuid:5c4f972b-d56b-40ac-a5fc-c8ca9b40b9d4";
	private static final String GetFoldersId = "urn:uuid:5737b14c-8a1a-4539-b659-e03a34a5e1e4";
	private static final String GetAssociationsId = "urn:uuid:a7ae438b-4bc2-4642-93e9-be891f7bb155";
	//private static final String GetDocumentsAndAssociationsId = "urn:uuid:bab9529a-4a10-40b3-a01f-f68a615d247a";
	private static final String GetSubmissionSetsId = "urn:uuid:51224314-5390-4169-9b91-b1980040715a";
	//private static final String GetSubmissionSetAndContentsId = "urn:uuid:e8e3cb2c-e39c-46b9-99e4-c12f57260b83";
	//private static final String GetFolderAndContentsId = "urn:uuid:b909a503-523d-4517-8acf-8e5834dfc4c7";
	private static final String GetFoldersForDocumentId = "urn:uuid:10cae35a-c7f9-4cf5-b61e-fc3278ffb578";
	//private static final String GetRelatedDocumentsId = "urn:uuid:d90e5407-b356-4d91-a89f-873917b4b0e6";
	/**
	 * Test Find Documents StoresQuery
	 */
	@SuppressWarnings("unchecked")
	public void testFindDocuments() {
		try{
			String id = FindDocumentsId;
			Map queryParameters = new HashMap();
			queryParameters.put("$XDSDocumentEntryPatientId", "SELF-5^^^&amp;1.3.6.1.4.1.21367.2005.3.7&amp;ISO");
			queryParameters.put("$XDSDocumentEntryStatus", "urn:oasis:names:tc:ebxml-regrep:StatusType:Deprecated");
			RegistryStoredQueryContext context = new RegistryStoredQueryContext(id, queryParameters, false);
			OMElement queryResponse = queryManager.storedQuery(context);
		
			/*InputStream is = new ByteArrayInputStream(queryResponse.toString().getBytes("UTF-8"));
			Object response = helper.getUnmarsheller().unmarshal(is);
		
			System.out.println("final result " + bu.marshalObject(response));*/
			assertNotNull(queryResponse); 

			//4. Verify the response is correct
			OMAttribute status = queryResponse.getAttribute(new QName("status"));
			assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
			}catch (Exception e) {
			 log.debug(e.getMessage());
		}
	}
	
	/**
	 * Test Find Folders StoredQuery
	 */
	@SuppressWarnings("unchecked")
	public void testFindFolders(){
		try{
			String id = FindFoldersId;
			Map queryParameters = new HashMap();
			queryParameters.put("$XDSFolderPatientId", "ad479512dd91412^^^&1.3.6.1.4.1.21367.2005.3.7&ISO");//R
			//queryParameters.put("$XDSFolderLastUpdateTimeFrom", "");//O
			//queryParameters.put("$XDSFolderLastUpdateTimeTo", "");//O
			//queryParameters.put("$XDSFolderCodeList", "");//O
			//queryParameters.put("$XDSFolderCodeListScheme", "");//O
			queryParameters.put("$XDSFolderStatus", "urn:oasis:names:tc:ebxml-regrep:StatusType:Approved");//R
			RegistryStoredQueryContext context = new RegistryStoredQueryContext(id, queryParameters, false);
			OMElement queryResponse = queryManager.storedQuery(context);
		/*	InputStream is = new ByteArrayInputStream(queryResponse.toString().getBytes("UTF-8"));
			Object response = helper.getUnmarsheller().unmarshal(is);
		
			System.out.println("final result " + bu.marshalObject(response));*/
			assertNotNull(queryResponse); 

			//4. Verify the response is correct
			OMAttribute status = queryResponse.getAttribute(new QName("status"));
			assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
			}catch (Exception e) {
			 log.debug(e.getMessage());
		}
	}
	
	/**
	 * Test FindSubmissionSets StoresQuery
	 */
	@SuppressWarnings("unchecked")
	public void testFindSubmissionSets(){
		try{
			String id = FindSubmissionSetsId;
			Map queryParameters = new HashMap();
			queryParameters.put("$XDSSubmissionSetPatientId", "SELF-5^^^&amp;1.3.6.1.4.1.21367.2005.3.7&amp;ISO");//R
			//queryParameters.put("$XDSSubmissionSetSourceId", "");//O
			//queryParameters.put("$XDSSubmissionSetSubmissionTimeFrom", "");//O
			//queryParameters.put("$XDSSubmissionSetSubmissionTimeTo", "");//O
			//queryParameters.put("$XDSSubmissionSetAuthorPerson", "");//O
			//queryParameters.put("$XDSSubmissionSetContentType", "");//O
			queryParameters.put("$XDSSubmissionSetStatus", "urn:oasis:names:tc:ebxml-regrep:StatusType:Approved");//R
			RegistryStoredQueryContext context = new RegistryStoredQueryContext(id, queryParameters, true);
			OMElement queryResponse = queryManager.storedQuery(context);
		
		/*	InputStream is = new ByteArrayInputStream(queryResponse.toString().getBytes("UTF-8"));
			Object response = helper.getUnmarsheller().unmarshal(is);
		
			System.out.println("final result " + bu.marshalObject(response));*/
			assertNotNull(queryResponse); 

			//4. Verify the response is correct
			OMAttribute status = queryResponse.getAttribute(new QName("status"));
			assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
			}catch (Exception e) {
			 log.debug(e.getMessage());
		}
	}
	
	/**
	 * Test GetAll StoredQuery
	 *//*
	@SuppressWarnings("unchecked")
	public void testGetAll(){
		try{
			String id = GetAllId;
			Map queryParameters = new HashMap();
			queryParameters.put("$patientId", "");//R -XDSFolder.patientId, XDSSunmissionSet.pstientId, XDSDocumentEntry.patientId
			queryParameters.put("$XDSDocumentEntryStatus","");//R
			queryParameters.put("$XDSSubmissionSetStatus","");//R
			queryParameters.put("$XDSFolderStatus","");//R
			//queryParameters.put("$XDSDocumentEntryFormatCode","");//O
			//queryParameters.put("$XDSDocumentEntryConfidentialityCode","");//O
			RegistryStoredQueryContext context = new RegistryStoredQueryContext(id, queryParameters, false);
			OMElement queryResponse = queryManager.storedQuery(context);
		
			InputStream is = new ByteArrayInputStream(queryResponse.toString().getBytes("UTF-8"));
			Object response = helper.getUnmarsheller().unmarshal(is);
		
			System.out.println("final result " + bu.marshalObject(response));
			}catch (Exception e) {
			 log.debug(e.getMessage());
		}
	}*/
	
	/**
	 * Test GetDocuments StoredQuery
	 */
	@SuppressWarnings("unchecked")
	public void testGetDocuments(){
		try{
			String id = GetDocumentsId;
			Map queryParameters = new HashMap();
			//Either $XDSDocumentEntryEntryUUID or $XDSDocumentEntryUniqueId shall be specified. 
			//This transaction shall return an error if both parameters are specified.
			//queryParameters.put("$XDSDocumentEntryEntryUUID", "");//O 
			queryParameters.put("$XDSDocumentEntryUniqueId", "1.3.6.1.4.1.21367.2005.3.9999.32");//O
			RegistryStoredQueryContext context = new RegistryStoredQueryContext(id, queryParameters, true);
			OMElement queryResponse = queryManager.storedQuery(context);
		
			/*InputStream is = new ByteArrayInputStream(queryResponse.toString().getBytes("UTF-8"));
			Object response = helper.getUnmarsheller().unmarshal(is);
		
			System.out.println("final result " + bu.marshalObject(response));*/
			assertNotNull(queryResponse); 

			//4. Verify the response is correct
			OMAttribute status = queryResponse.getAttribute(new QName("status"));
			assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
			}catch (Exception e) {
			 log.debug(e.getMessage());
		}
	}
	
	/**
	 * Test GetFolders StoredQuery
	 */
	@SuppressWarnings("unchecked")
	public void testGetFolders(){
		try{
			String id = GetFoldersId;
			Map queryParameters = new HashMap();
			//Either $XDSFolderEntryUUID or $XDSFolderntryUniqueId shall be specified. 
			//This transaction shall return an error if both parameters are specified.
			//queryParameters.put("$XDSFolderEntryUUID", "");//O
			queryParameters.put("$XDSFolderUniqueId", "2.16.840.1.113883.3.65.3.1250603667434");//O
			RegistryStoredQueryContext context = new RegistryStoredQueryContext(id, queryParameters, false);
			OMElement queryResponse = queryManager.storedQuery(context);
		
		/*	InputStream is = new ByteArrayInputStream(queryResponse.toString().getBytes("UTF-8"));
			Object response = helper.getUnmarsheller().unmarshal(is);
		
			System.out.println("final result " + bu.marshalObject(response));*/
			assertNotNull(queryResponse); 

			//4. Verify the response is correct
			OMAttribute status = queryResponse.getAttribute(new QName("status"));
			assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
			}catch (Exception e) {
			 log.debug(e.getMessage());
		}
	}
	
	/**
	 * Test GetAssociations StoredQuery
	 */
	@SuppressWarnings("unchecked")
	public void testGetAssociations(){
		try{
			String id = GetAssociationsId;
			Map queryParameters = new HashMap();
			queryParameters.put("$uuid", "urn:uuid:0520abda-8944-4463-b715-844a6785f2ab");//R
			RegistryStoredQueryContext context = new RegistryStoredQueryContext(id, queryParameters, false);
			OMElement queryResponse = queryManager.storedQuery(context);
		
/*			InputStream is = new ByteArrayInputStream(queryResponse.toString().getBytes("UTF-8"));
			Object response = helper.getUnmarsheller().unmarshal(is);
		
			System.out.println("final result " + bu.marshalObject(response));*/
			assertNotNull(queryResponse); 

			//4. Verify the response is correct
			OMAttribute status = queryResponse.getAttribute(new QName("status"));
			assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
			}catch (Exception e) {
			 log.debug(e.getMessage());
		}
	}
	/*
	*//**
	 * Test GetDocumentsAndAssociations StoredQuery
	 *//*
	@SuppressWarnings("unchecked")
	public void testGetDocumentsAndAssociations(){
		try{
			String id = GetDocumentsAndAssociationsId;
			Map queryParameters = new HashMap();
			//Either $XDSDocumentEntryEntryUUID or $XDSDocumentEntryUniqueId shall be specified. 
			//This transaction shall return an error if both parameters are specified.
			//queryParameters.put("$XDSDocumentEntryEntryUUID", "");//O
			queryParameters.put("$XDSDocumentEntryUniqueId", "1.3.6.1.4.1.21367.2005.3.9999.32");//O
			RegistryStoredQueryContext context = new RegistryStoredQueryContext(id, queryParameters, false);
			OMElement queryResponse = queryManager.storedQuery(context);
		
			InputStream is = new ByteArrayInputStream(queryResponse.toString().getBytes("UTF-8"));
			Object response = helper.getUnmarsheller().unmarshal(is);
		
			System.out.println("final result " + bu.marshalObject(response));
			}catch (Exception e) {
			 log.debug(e.getMessage());
		}
	}*/
	
	/**
	 * Test GetSubmissionSets StoredQuery
	 */
	@SuppressWarnings("unchecked")
	public void testGetSubmissionSets(){
		try{
			String id = GetSubmissionSetsId;
			Map queryParameters = new HashMap();
			queryParameters.put("$uuid", "urn:uuid:0520abda-8944-4463-b715-844a6785f2ab");//R
			RegistryStoredQueryContext context = new RegistryStoredQueryContext(id, queryParameters, true);
			OMElement queryResponse = queryManager.storedQuery(context);
		
			/*InputStream is = new ByteArrayInputStream(queryResponse.toString().getBytes("UTF-8"));
			Object response = helper.getUnmarsheller().unmarshal(is);
		
			System.out.println("final result " + bu.marshalObject(response));*/
			assertNotNull(queryResponse); 

			//4. Verify the response is correct
			OMAttribute status = queryResponse.getAttribute(new QName("status"));
			assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
			}catch (Exception e) {
			 log.debug(e.getMessage());
		}
	}
	
	/**
	 * Test GetSubmissionSetAndContents StoredQuery
	 *//*
	@SuppressWarnings("unchecked")
	public void testGetSubmissionSetAndContents(){
		try{
			String id = GetSubmissionSetAndContentsId;
			Map queryParameters = new HashMap();
			//Either $XDSSubmissionSetEntryUUID or $XDSSubmissionSetUniqueId shall be specified. 
			//This transaction shall return an error if both parameters are specified.
			queryParameters.put("$XDSSubmissionSetEntryUUID", "");//O
			queryParameters.put("$XDSSubmissionSetUniqueId", "");//O
			queryParameters.put("$XDSDocumentEntryFormatCode", "");//O
			queryParameters.put("$XDSDocumentEntryConfidentialityCode", "");//O
			RegistryStoredQueryContext context = new RegistryStoredQueryContext(id, queryParameters, false);
			OMElement queryResponse = queryManager.storedQuery(context);
		
			InputStream is = new ByteArrayInputStream(queryResponse.toString().getBytes("UTF-8"));
			Object response = helper.getUnmarsheller().unmarshal(is);
		
			System.out.println("final result " + bu.marshalObject(response));
			}catch (Exception e) {
			 log.debug(e.getMessage());
		}
	}*/
	
	/**
	 * Test GetFolderAndContents StoredQuery
	 *//*
	@SuppressWarnings("unchecked")
	public void testGetFolderAndContents(){
		try{
			String id = GetFolderAndContentsId;
			Map queryParameters = new HashMap();
			//Either $XDSFolderEntryUUID or $XDSFolderntryUniqueId shall be specified. 
			//This transaction shall return an error if both parameters are specified.
			queryParameters.put("$XDSFolderEntryUUID", "");//O
			queryParameters.put("$XDSFolderUniqueId", "");//O
			queryParameters.put("$XDSDocumentEntryFormatCode", "");//O
			queryParameters.put("$XDSDocumentEntryConfidentialityCode", "u");//O
			RegistryStoredQueryContext context = new RegistryStoredQueryContext(id, queryParameters, false);
			OMElement queryResponse = queryManager.storedQuery(context);
		
			InputStream is = new ByteArrayInputStream(queryResponse.toString().getBytes("UTF-8"));
			Object response = helper.getUnmarsheller().unmarshal(is);
		
			System.out.println("final result " + bu.marshalObject(response));
			}catch (Exception e) {
			 log.debug(e.getMessage());
		}
	}*/
	
	/**
	 * Test GetFoldersForDocument StoredQuery
	 */
	@SuppressWarnings("unchecked")
	public void testGetFoldersForDocument(){
		try{
			String id = GetFoldersForDocumentId;
			Map queryParameters = new HashMap();
			//Either $XDSDocumentEntryEntryUUID or $XDSDocumentEntryUniqueId shall be specified. 
			//This transaction shall return an error if both parameters are specified.
			//queryParameters.put("$XDSDocumentEntryEntryUUID", "");//O
			queryParameters.put("$XDSDocumentEntryUniqueId", "1.3.6.1.4.1.21367.2005.3.9999.32");//O
			RegistryStoredQueryContext context = new RegistryStoredQueryContext(id, queryParameters, false);
			OMElement queryResponse = queryManager.storedQuery(context);
		
		/*	InputStream is = new ByteArrayInputStream(queryResponse.toString().getBytes("UTF-8"));
			Object response = helper.getUnmarsheller().unmarshal(is);
		
			System.out.println("final result " + bu.marshalObject(response));*/
			assertNotNull(queryResponse); 

			//4. Verify the response is correct
			OMAttribute status = queryResponse.getAttribute(new QName("status"));
			assertEquals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success", status.getAttributeValue()); 
			
			}catch (Exception e) {
			 log.debug(e.getMessage());
		}
	}
	
	/**
	 * Test GetRelatedDocuments StoredQuery
	 *//*
	@SuppressWarnings("unchecked")
	public void testGetRelatedDocuments(){
		try{
			String id = GetRelatedDocumentsId;
			Map queryParameters = new HashMap();
			//Either $XDSDocumentEntryEntryUUID or $XDSDocumentEntryUniqueId shall be specified. 
			//This transaction shall return an error if both parameters are specified.
			//queryParameters.put("$XDSDocumentEntryEntryUUID", "");//O
			queryParameters.put("$XDSDocumentEntryUniqueId", "1.3.6.1.4.1.21367.2005.3.9999.32");//O
			queryParameters.put("$AssociationTypes", "urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember");//R - Not a named attribute
			RegistryStoredQueryContext context = new RegistryStoredQueryContext(id, queryParameters, true);
			OMElement queryResponse = queryManager.storedQuery(context);
		
			InputStream is = new ByteArrayInputStream(queryResponse.toString().getBytes("UTF-8"));
			Object response = helper.getUnmarsheller().unmarshal(is);
		
			System.out.println("final result " + bu.marshalObject(response));
			}catch (Exception e) {
			 log.debug(e.getMessage());
		}
	}
	*/
 /* *//**
   * Creating AdhocQueryRequest as a OMElement.
   * @return OMElement
   *//*
	private OMElement getAdhocQueryRequest() {

		OMElement queryRequest = helper.omFactory.createOMElement("AdhocQueryRequest", helper.nsQuery);
		queryRequest.declareNamespace(helper.ns);
		queryRequest.declareNamespace(helper.nsXsi);
		queryRequest.declareNamespace(helper.nsQuery);
		queryRequest.declareNamespace(helper.nsRim);
		queryRequest.declareNamespace(helper.nsRs);
		//submitObjectsRequest.addAttribute("schemaLocation", XDS_b_REGISTRY_SCHEMA_LOCATION, nsXsi);
		try {

			OMElement responseOption = helper.omFactory().createOMElement("ResponseOption", helper.nsQuery);
			responseOption.addAttribute("returnType", "LeafClass", null);
			responseOption.addAttribute("returnComposedObjects", "true", null);
			queryRequest.addChild(responseOption);
			OMElement query = helper.omFactory().createOMElement("AdhocQuery", helper.nsRim);
			queryRequest.addChild(query);
			query.addAttribute("id", "urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d", null);
			addRimSlotElement(query, "$XDSDocumentEntryPatientId", "SELF-5^^^&amp;1.3.6.1.4.1.21367.2005.3.7&amp;ISO", helper.nsRim);
			addRimSlotElement(query, "$XDSDocumentEntryStatus",	"urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted", helper.nsRim);
			//addRimSlotElement(query, "$XDSDocumentEntryCreationTimeFrom", "200412252300", helper.nsRim);
			//addRimSlotElement(query, "$XDSDocumentEntryCreationTimeTo",	"200501010800", helper.nsRim);
			addRimSlotElement(query, "$XDSDocumentEntryHealthcareFacilityTypeCode",	"('Outpatient')", helper.nsRim);
		} catch (Exception e) {
			log.debug(e.getMessage());
		}
		return queryRequest;
	}

	*//**
	 * Add a new ebXML Slot element to the parent element.
	 *
	 * @param parent The parent element this should be added to
	 * @param name The name of the slot
	 * @param values The collection of values to put into the slot value list
	 * @param rimNameSpace the rim name space of the root element
	 * @return The new slot element
	 *//*
	public OMElement addRimSlotElement(OMElement parent, String name, String value, OMNamespace rimNameSpace) {
		int RIM_LONG_NAME = 128;
		OMElement slotElement = helper.omFactory.createOMElement("Slot", rimNameSpace);
		slotElement.addAttribute("name", name, null);
		parent.addChild(slotElement);
		OMElement valueList = helper.omFactory.createOMElement("ValueList",	rimNameSpace);
		slotElement.addChild(valueList);
		OMElement valueNode = helper.omFactory.createOMElement("Value",rimNameSpace);
		valueList.addChild(valueNode);
		valueNode.addChild(helper.omFactory.createOMText(trimRimString(value, RIM_LONG_NAME)));
		return slotElement;
	}

	*//**
	 * Trim a string to the length specified in the RIM V.2 spec.
	 *
	 * @param input The input string
	 * @param size The maximum allowed length
	 * @return The input, trimmed to the specified length, if necssary
	 *//*
	private static String trimRimString(String input, int size) {
		if (input == null)
			return null;
		if (input.length() <= size) {
			return input;
		} else {
			// log.warn("Trimming ebXML RIM string to " + size + "characters: '" + input + "'");
			return input.substring(0, size);
		}
	}*/
}