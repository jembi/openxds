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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.client.ServiceClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openhealthtools.openexchange.utils.OMUtil;
import org.openhealthtools.openxds.common.XdsFactory;
import org.openhealthtools.openxds.repository.api.XdsRepositoryService;

/**
 * This class is an integrated test for IHE transaction ITI-43, namely,
 * RetrieveDocumentSet-b.
 *  
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 * @author <a href="mailto:rasakannu.palaniyandi@misys.com">Raja</a>
 */
public class RetrieveDocumentSetTest extends XdsTest {

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
	public void testRetrieveDocumentSetb() throws Exception {

		createPatient(patientId);
		
		String uuid = submitOneDocument(patientId);

		//2. Generate StoredQuery request message
		String message = new RegistrytStoredQueryTest().findDocumentsQuery(patientId, "Approved");
		OMElement request = OMUtil.xmlStringToOM(message);
		System.out.println("Request:\n" + request);

		//3. Send a StoredQuery
		ServiceClient sender = getRegistryServiceClient();
		OMElement response = sender.sendReceive(request);

		//4. Get DocumentUniqueId from the response.
		List extrinsicObjects = getExtrinsicObjects(response);
		String documentUniqueId = getDocumentId(extrinsicObjects);

		//5. Get RepositoryUniqueId from the response.
		XdsRepositoryService xdsService = (XdsRepositoryService) XdsFactory.getInstance().getBean("repositoryService");
		String reposiotryUniqueId = xdsService.getRepositoryUniqueId();

		//6. Generate Retrieve document request message
		String retrieveDoc = retrieveDocuments(reposiotryUniqueId,
				documentUniqueId);
		OMElement retrieveDocRequest = OMUtil.xmlStringToOM(retrieveDoc);

		//7. Send a Retrieve document set request
		ServiceClient retrieveDocSender = getRetrieveDocumentServiceClient();
		OMElement retrieveDocResponse = retrieveDocSender
				.sendReceive(retrieveDocRequest);

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
		assertEquals(
				"urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success",
				responseStatus);

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

	private String getDocumentId(List extrinsicObjects) {
		String documentId = null;
		for (Iterator<OMElement> it = extrinsicObjects.iterator(); it.hasNext();) {
			OMElement ele = it.next();
			if (ele.getAttributeValue(new QName("identificationScheme"))
					.equals("urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab")) {
				documentId = ele.getAttributeValue(new QName("value"));
			}
		}
		return documentId;
	}

	private String getRetrieveDocumentStatus(List retrieveDoc) {
		String retrieveDocStatus = null;
		for (Iterator<OMElement> it = retrieveDoc.iterator(); it.hasNext();) {
			OMElement ele = it.next();
			retrieveDocStatus = ele.getAttributeValue(new QName("status"));
		}
		return retrieveDocStatus;

	}

	private String retrieveDocuments(String repoId, String docId) {

		String request = "<xdsb:RetrieveDocumentSetRequest xmlns:xdsb=\"urn:ihe:iti:xds-b:2007\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:ihe:iti:xds-b:2007 ../schema/IHE/XDS.b_DocumentRepository.xsd\">\n"
				+ "  <xdsb:DocumentRequest>\n"
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

}
