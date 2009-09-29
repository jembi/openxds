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
package org.openhealthtools.openxds.registry.adapter.omar31;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.log4j.Logger;
import org.freebxml.omar.common.BindingUtility;
import org.oasis.ebxml.registry.bindings.rs.RegistryResponse;
import org.openhealthtools.openxds.registry.adapter.omar31.ConversionHelper;
import org.openhealthtools.openxds.registry.adapter.omar31.XdsRegistryLifeCycleServiceImpl;
import org.openhealthtools.openxds.registry.api.XdsRegistryLifeCycleService;
import org.openhealthtools.openxds.registry.api.RegistryLifeCycleContext;


/**
 * The XDSRegistryLifeCycleManagerTest which defines the test cases for Submit, Approve and Deprecate 
 * methods of XDS RegistryLifeCycle.
 * 
 * @author <a href="mailto:anilkumar.reddy@misys.com">Anil kumar</a>
 */
public class XdsRegistryLifeCycleServiceTest extends TestCase{
	 private static final Logger log =  Logger.getLogger(XdsRegistryLifeCycleServiceTest.class);
	//DocumentEntry
	private static final String XDS_DOCUMENT_ENTRY = "urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1";
	
	private static final String XDS_DOCUMENT_ENTRY_UNIQUE_ID = "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab";
    private static final String XDS_DOCUMENT_ENTRY_PATIENT_ID = "urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427";

    private static final String XDS_DOCUMENT_ENTRY_CLASS_CODE = "urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a";
    private static final String XDS_DOCUMENT_ENTRY_FORMAT_CODE = "urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d";
    private static final String XDS_DOCUMENT_ENTRY_PRACTICE_SETTING_CODE = "urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead";
    private static final String XDS_DOCUMENT_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE = "urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1";
    private static final String XDS_DOCUMENT_ENTRY_CONFIDENTIALITY_CODE = "urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f";
    private static final String XDS_DOCUMENT_ENTRY_TYPE_CODE = "urn:uuid:f0306f51-975f-434e-a61c-c59651d33983";
   // private static final String XDS_DOCUMENT_ENTRY_EVENT_CODE_LIST = "urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4";
    private static final String XDS_DOCUMENT_ENTRY_AUTHOR_PERSON = "urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d";
    //SubmissionSet
    private static final String XDS_SUBMISSION_SET = "urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd";

	private static final String XDS_SUBMISSION_SET_UNIQUE_ID = "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8";
	private static final String XDS_SUBMISSION_SET_PATIENT_ID = "urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446";
	private static final String XDS_SUBMISSION_SET_SOURCE_ID = "urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832";
	
	private static final String XDS_SUBMISSION_SET_CONTENT_TYPE_CODE = "urn:uuid:aa543740-bdda-424e-8c96-df4873be8500";
    private static final String XDS_SUBMISSION_SET_AUTHOR_PERSON = "urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d";
    
	 protected static ConversionHelper helper = ConversionHelper.getInstance();
	 protected static BindingUtility bu = BindingUtility.getInstance();
	 protected static XdsRegistryLifeCycleService registryManager =new  XdsRegistryLifeCycleServiceImpl();
	 
	 /**
	  * Test SubmitObjedctsRequest
	  * 
	  */
	 public void testSubmitObjects(){
			
			RegistryResponse res = null;
			RegistryLifeCycleContext context = new RegistryLifeCycleContext();
			OMElement registryResponse;		
			try {
				//OMElement request = Util.parse_xml(new File("D:\\XDS\\schema\\RegisterDocumentSet-bRequest.xml"));
				OMElement request = getsubmitObjectsRequest();
				registryResponse = registryManager.submitObjects(request, context);
				InputStream is = new ByteArrayInputStream(registryResponse.toString().getBytes("UTF-8"));
				Object response =helper.getUnmarsheller().unmarshal(is);
				if (response instanceof RegistryResponse) {
					 res = (RegistryResponse) response;
				}
				System.out.println("final result " + bu.marshalObject(res));
			} 
			catch (Exception e) {
				log.debug(e.getMessage());
			}
			
		}
	 
	 /**
	  * Test ApproveObjectsRequest
	  */
	 public void testApproveObjects() {
			//OMElement request = Util.parse_xml(new File("D:\\XDS\\schema\\RegisterDocumentSet-bRequest.xml"));
			RegistryResponse res = null;
			RegistryLifeCycleContext context = new RegistryLifeCycleContext();
			OMElement response;			
			try {
				OMElement request = getApproveObjectsRequest();
				response = registryManager.approveObjects(request, context);
				InputStream is = new ByteArrayInputStream(response.toString().getBytes("UTF-8"));
				Object temp =helper.getUnmarsheller().unmarshal(is);
				if (temp instanceof RegistryResponse) {
					 res = (RegistryResponse) temp;
				}
				System.out.println("final result " + bu.marshalObject(res));
				
			} 
			catch (Exception e) {
				log.debug(e.getMessage());
			}
			
		}
	 
	 /**
	  * Test ApproveObjectsRequest
	  */
	 public void testDeprecateObjects() {
			//OMElement request = Util.parse_xml(new File("D:\\XDS\\schema\\RegisterDocumentSet-bRequest.xml"));
			RegistryResponse res = null;
			RegistryLifeCycleContext context = new RegistryLifeCycleContext();
			OMElement response;			
			try {
				OMElement request = getDeprecateObjectsRequest();
				response = registryManager.deprecateObjects(request, context);
				InputStream is = new ByteArrayInputStream(response.toString().getBytes("UTF-8"));
				Object temp =helper.getUnmarsheller().unmarshal(is);
				if (temp instanceof RegistryResponse) {
					 res = (RegistryResponse) temp;
				}
				System.out.println("final result " + bu.marshalObject(res));
				
			} 
			catch (Exception e) {
				log.debug(e.getMessage());
			}
			
		}
	 
	 /**
		 * Create a new SubmitObjectsRequest as an Axiom OMElement
		 * 
		 * @param submissionSet
		 *            The xds submission set for the request
		 * @param documentEntries
		 *            The xds document entries for the request
		 * @param connection
		 *            The connection that will be used for the request
		 * @throws XdsRimException
		 *             When the metadata cannot be encoded into a valid ebRIM
		 *             XML
		 */
	private OMElement getsubmitObjectsRequest() {
		
		OMElement submitObjectsRequest = helper.omFactory.createOMElement("SubmitObjectsRequest", helper.nsLcm);
		submitObjectsRequest.declareNamespace(helper.ns);
		submitObjectsRequest.declareNamespace(helper.nsXsi);
		submitObjectsRequest.declareNamespace(helper.nsLcm);
		submitObjectsRequest.declareNamespace(helper.nsRim);		
		submitObjectsRequest.declareNamespace(helper.nsRs);
		//submitObjectsRequest.addAttribute("schemaLocation", XDS_b_REGISTRY_SCHEMA_LOCATION, nsXsi);
		try {
			OMElement registryObjectList = helper.omFactory().createOMElement("RegistryObjectList", helper.nsRim);
			submitObjectsRequest.addChild(registryObjectList);
			String docId = "urn:uuid:0520abda-8944-4463-b715-844a6785f2ab";
			registryObjectList.addChild(getExtrinsicObject(docId, "text/xml"));
			String setId = "urn:uuid:08e6330f-1a7d-4099-be06-96cf8e6edae2";
			registryObjectList.addChild(getRegistryPackage(setId));
			RimAxiom.addRimClassificationElement(registryObjectList, "cl-ss0", setId, XDS_SUBMISSION_SET, helper.nsRim);
			// Link the document entries to the submission set
			RimAxiom.addRimSubmissionDocumentAssociationElement(registryObjectList, setId, docId, helper.nsRim);

		} catch (Exception e) {
			log.debug(e.getMessage());
		}
		return submitObjectsRequest;
	}
	
	 /**
		 * Get an ExtrinsicObject as an XML Axiom version of an ebRim.
		 * 
		 * @param id
		 *            The id to give this XDSDocumentEntry
		 * @param mimeType
		 *            The mimetype of this XDSDocumentEntry
		 * @return The XML Axiom OMElement holding this ExtrinsicObject
		 * @throws XdsRimException
		 *             When this ExtrinsicObject cannot be turned into valid
		 *             metadata
		 */
	private OMElement getExtrinsicObject(String id, String mimeType)throws XdsRimException {
		// First, create the Extrinsic object
		RimAxiom xml = RimAxiom.newXdsDocumentEntryBuilder(id, log);
		// add attributes
		xml.addAttribute("objectType", XDS_DOCUMENT_ENTRY, true);
		xml.addAttribute("mimeType", mimeType, true);
		
		// Now a bunch of slots
		xml.addSlot("creationTime", "20051224", true);
		xml.addSlot("languageCode", "en-us", true);
		xml.addSlot("serviceStartTime", "200412230800", false);
		xml.addSlot("serviceStopTime", "200412230801", false);
		xml.addSlot("sourcePatientId",
				"ST-1000^^^&amp;1.3.6.1.4.1.21367.2003.3.9&amp;ISO", true);
		List<String> sourcePatientInfo = new ArrayList<String>();
		sourcePatientInfo.add("PID-3|ST-1000^^^&amp;1.3.6.1.4.1.21367.2003.3.9&amp;ISO");
		sourcePatientInfo.add("PID-5|Doe^John^^^");
		sourcePatientInfo.add("PID-7|19560527");
		sourcePatientInfo.add("PID-8|M");
		sourcePatientInfo.add("PID-11|100 Main St^^Metropolis^Il^44130^USA");
		xml.addSlot("sourcePatientInfo", sourcePatientInfo, true);
		// Next, add the name
		xml.addName("Physical", false);
		xml.addDescription("", false);
		// Add Author Classification - implement XDS CP-122
		Map<String, List<String>> valsmap = new HashMap<String, List<String>>();
		List<String> list1 = new ArrayList<String>();
		list1.add("Cleveland Clinic");
		list1.add("Parma Community");
		valsmap.put("authorInstitution", list1);
		List<String> list2 = new ArrayList<String>();
		list2.add("Attending");
		valsmap.put("authorRole", list2);
		List<String> list3 = new ArrayList<String>();
		list3.add("Orthopedic");
		valsmap.put("authorSpecialty", list3);

		xml.addClassification("authorPerson", "Gerald Smitty", valsmap,	XDS_DOCUMENT_ENTRY_AUTHOR_PERSON, "c01", false);
		xml.addClassification("History and Physical", "Connect-a-thon classCodes","History and Physical", XDS_DOCUMENT_ENTRY_CLASS_CODE, "c02", true);
		xml.addClassification("CDAR2/IHE 1.0", "Connect-a-thon formatCodes","CDAR2/IHE 1.0", XDS_DOCUMENT_ENTRY_FORMAT_CODE, "c03", true);
		xml.addClassification("General Medicine","Connect-a-thon practiceSettingCodes", "General Medicine",	XDS_DOCUMENT_ENTRY_PRACTICE_SETTING_CODE, "c04", true);
		xml.addClassification("Outpatient","Connect-a-thon healthcareFacilityTypeCodes","Outpatient", XDS_DOCUMENT_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE, "c05", true);
		xml.addClassification("Outpatient Evaluation And Management", "LOINC","34108-1", XDS_DOCUMENT_ENTRY_TYPE_CODE, "c06", true);
		xml.addClassification("Connect-a-thon confidentialityCodes", "Clinical-Staff","1.3.6.1.4.1.21367.2006.7.101",XDS_DOCUMENT_ENTRY_CONFIDENTIALITY_CODE, "c07", true);
		// And a couple of identifiers
		xml.addExternalIdentifier("XDSDocumentEntry.patientId",	"SELF-5^^^&amp;1.3.6.1.4.1.21367.2005.3.7&amp;ISO",	XDS_DOCUMENT_ENTRY_PATIENT_ID, id, "ei01", true);
		xml.addExternalIdentifier("XDSDocumentEntry.uniqueId", "1.3.6.1.4.1.21367.2005.3.9999.32", XDS_DOCUMENT_ENTRY_UNIQUE_ID, id, "ei02", true);
		// Done
		return xml.getRootElement();
	}
	
	/**
	 * Get an RegistryPackage as an XML Axiom version of an ebRim.
	 * 
	 * @param id
	 *            The id to give this XDSSubmissionSet
	 * @return The XML Axiom OMElement holding this RegistryPackage
	 * @throws XdsRimException
	 *             When this XDSSubmissionSet cannot be turned into valid
	 *             metadata
	 */
	public OMElement getRegistryPackage(String id) throws XdsRimException {
		// First, create the RegistryPackage object
		RimAxiom xml= RimAxiom.newXdsSubmissionSetBuilder(id, log);
		// Now a bunch of slots
		xml.addSlot("submissionTime", "20041225235050", true);
		//Next add name
        xml.addName("physical", true);
		// Next, add the description
		xml.addDescription("Annual physical", false);
		//Add Author Classification   - implement XDS CP-122
		Map<String, List<String>> valsmap= new HashMap<String, List<String>>();
        List<String> list1 = new ArrayList<String>(); 
        list1.add("Cleveland Clinic");
        list1.add("Berea Community");
        valsmap.put("authorInstitution", list1);
        List<String> list2 = new ArrayList<String>();
        list2.add("Primary Surgon");
        valsmap.put("authorRole", list2);
        List<String> list3 = new ArrayList<String>();
        list3.add("Orthopedic");
        valsmap.put("authorSpecialty", list3);	 
        xml.addClassification("authorPerson", "Sherry Dopplemeyer", valsmap, XDS_SUBMISSION_SET_AUTHOR_PERSON, "c08", false);
		xml.addClassification("History and Physical","Connect-a-thon contentTypeCodes", "History and Physical", XDS_SUBMISSION_SET_CONTENT_TYPE_CODE, "c09", true);
		// And identifiers
		xml.addExternalIdentifier("XDSSubmissionSet.uniqueId", "1.3.6.1.4.1.21367.2005.3.9999.33", XDS_SUBMISSION_SET_UNIQUE_ID, id, "ei085", true);
		xml.addExternalIdentifier("XDSSubmissionSet.sourceId", "3670984664", XDS_SUBMISSION_SET_SOURCE_ID, id, "ei04", true);
		xml.addExternalIdentifier("XDSSubmissionSet.patientId", "SELF-5^^^&amp;1.3.6.1.4.1.21367.2005.3.7&amp;ISO", XDS_SUBMISSION_SET_PATIENT_ID, id, "ei03", true);
		// Done
		return xml.getRootElement();
	}
		
	public OMElement getApproveObjectsRequest() {
		OMElement req = helper.omFactory.createOMElement("ApproveObjectsRequest", helper.nsLcm);
		req.declareNamespace(helper.ns);
		req.declareNamespace(helper.nsXsi);
		req.declareNamespace(helper.nsLcm);
		req.declareNamespace(helper.nsRim);		
		req.declareNamespace(helper.nsRs);
		String docId = "urn:uuid:0520abda-8944-4463-b715-844a6785f2ab";
		String setId = "urn:uuid:08e6330f-1a7d-4099-be06-96cf8e6edae2";
		List<String> uuids = new ArrayList<String>();
		uuids.add(docId);
		uuids.add(setId);
		req.addChild(makeObjectRefList(uuids));
		return req;
	}
	
	public OMElement getDeprecateObjectsRequest() {
		OMElement req = helper.omFactory.createOMElement("DeprecateObjectsRequest", helper.nsLcm);
		req.declareNamespace(helper.ns);
		req.declareNamespace(helper.nsXsi);
		req.declareNamespace(helper.nsLcm);
		req.declareNamespace(helper.nsRim);		
		req.declareNamespace(helper.nsRs);
		String docId = "urn:uuid:0520abda-8944-4463-b715-844a6785f2ab";
		String setId = "urn:uuid:08e6330f-1a7d-4099-be06-96cf8e6edae2";
		List<String> uuids = new ArrayList<String>();
		uuids.add(docId);
		uuids.add(setId);
		req.addChild(makeObjectRefList(uuids));
		return req;
	}

	public OMElement getDeprecateObjectsRequest(ArrayList uuids) {
		OMElement req = helper.omFactory.createOMElement("DeprecateObjectsRequest", helper.nsRim);
		req.addChild(makeObjectRefList(uuids));
		return req;

	}

	private OMElement makeObjectRefList(List<String> uuids) {
		OMElement objectRefList = helper.omFactory.createOMElement("ObjectRefList", helper.nsRim);
		for (String uuid: uuids ) {
			OMAttribute att = helper.omFactory.createOMAttribute("id", null, uuid);
			OMElement objectRef = helper.omFactory.createOMElement("ObjectRef", helper.nsRim);
			objectRef.addAttribute(att);
			objectRefList.addChild(objectRef);
		}
		return objectRefList;
	}
}
