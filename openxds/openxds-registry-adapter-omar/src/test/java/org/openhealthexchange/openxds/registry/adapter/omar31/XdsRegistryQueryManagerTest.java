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
package org.openhealthexchange.openxds.registry.adapter.omar31;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.log4j.Logger;
import org.freebxml.omar.common.BindingUtility;
import org.openhealthexchange.openxds.registry.api.IXdsRegistryQueryManager;
import org.openhealthexchange.openxds.registry.api.RegistryStoredQueryContext;

  /**
   * The XDSRegistryQueryManagerTest which tests the query operations to 
   * XDS Registry objects.
   * 
   * @author <a href="mailto:anilkumar.reddy@misys.com">Anil kumar</a>
   */
  public class XdsRegistryQueryManagerTest extends TestCase {
	private static final Logger log =  Logger.getLogger(XdsRegistryQueryManagerTest.class);
	protected static BindingUtility bu = BindingUtility.getInstance();
	protected static IXdsRegistryQueryManager queryManager = new  XdsRegistryQueryManager();
	protected static ConversionHelper helper = ConversionHelper.getInstance();
	public void testStoredQuery() {
		try{
			String id = "urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d";
			Map queryParameters = new HashMap();
			queryParameters.put("$XDSDocumentEntryPatientId", "SELF-5^^^&amp;1.3.6.1.4.1.21367.2005.3.7&amp;ISO");
			queryParameters.put("$XDSDocumentEntryStatus", "urn:oasis:names:tc:ebxml-regrep:StatusType:Approved");
			RegistryStoredQueryContext context = new RegistryStoredQueryContext(id, queryParameters, false);
			OMElement request = getAdhocQueryRequest();
			OMElement queryResponse = queryManager.storedQuery(context);
		
			InputStream is = new ByteArrayInputStream(queryResponse.toString().getBytes("UTF-8"));
			Object response = helper.getUnmarsheller().unmarshal(is);
		
			System.out.println("final result " + bu.marshalObject(response));
			}catch (Exception e) {
			 log.debug(e.getMessage());
		}
	}
	
  /**
   * Creating AdhocQueryRequest as a OMElement.
   * @return OMElement
   */
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
			addRimSlotElement(query, "$XDSDocumentEntryHealthcareFacilityTypeCode",	"(‘Outpatient’)", helper.nsRim);
		} catch (Exception e) {
			log.debug(e.getMessage());
		}
		return queryRequest;
	}

	/**
	 * Add a new ebXML Slot element to the parent element.
	 *
	 * @param parent The parent element this should be added to
	 * @param name The name of the slot
	 * @param values The collection of values to put into the slot value list
	 * @param rimNameSpace the rim name space of the root element
	 * @return The new slot element
	 */
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

	/**
	 * Trim a string to the length specified in the RIM V.2 spec.
	 *
	 * @param input The input string
	 * @param size The maximum allowed length
	 * @return The input, trimmed to the specified length, if necssary
	 */
	private static String trimRimString(String input, int size) {
		if (input == null)
			return null;
		if (input.length() <= size) {
			return input;
		} else {
			// log.warn("Trimming ebXML RIM string to " + size + "characters: '" + input + "'");
			return input.substring(0, size);
		}
	}
}