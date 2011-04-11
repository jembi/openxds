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

package org.openhealthtools.openxds.webapp.control;

import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.soap.Soap;

import java.util.ArrayList;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMText;
import org.apache.log4j.Logger;

/**
 * Utility class for building query elements and submits the request to endpoint. 
 * @author <a href="anilkumar.reddy@misys.com">Anil Kumar</a>
 */
public abstract class Sq {
	private final static Logger logger = Logger.getLogger(Sq.class);
	abstract OMElement build(ArrayList<String> ids);

	abstract String query_details();

	protected ArrayList<OMElement> build_query_wrapper(String query_id) {
		OMElement query = MetadataSupport.om_factory.createOMElement("AdhocQueryRequest", MetadataSupport.ebQns3);
		query.declareNamespace(MetadataSupport.ebRIMns3);
		OMElement response_option = MetadataSupport.om_factory.createOMElement("ResponseOption", MetadataSupport.ebQns3);
		query.addChild(response_option);
		response_option.addAttribute("returnComposedObjects", "true", null);
		response_option.addAttribute("returnType", "LeafClass", null);
		OMElement adhoc_query = MetadataSupport.om_factory.createOMElement("AdhocQuery", MetadataSupport.ebRIMns3);
		query.addChild(adhoc_query);
		adhoc_query.addAttribute("id", query_id, null);
		ArrayList<OMElement> elements = new ArrayList<OMElement>();
		elements.add(query);
		elements.add(adhoc_query);
		return elements;
	}

	protected ArrayList<OMElement> build_objectref_query_wrapper(String query_id) {
		OMElement query = MetadataSupport.om_factory.createOMElement("AdhocQueryRequest", MetadataSupport.ebQns3);
		query.declareNamespace(MetadataSupport.ebRIMns3);
		OMElement response_option = MetadataSupport.om_factory.createOMElement("ResponseOption", MetadataSupport.ebQns3);
		query.addChild(response_option);
		response_option.addAttribute("returnComposedObjects", "true", null);
		response_option.addAttribute("returnType", "ObjectRef", null);
		OMElement adhoc_query = MetadataSupport.om_factory.createOMElement("AdhocQuery", MetadataSupport.ebRIMns3);
		query.addChild(adhoc_query);
		adhoc_query.addAttribute("id", query_id, null);
		ArrayList<OMElement> elements = new ArrayList<OMElement>();
		elements.add(query);
		elements.add(adhoc_query);
		return elements;
	}

	protected void add_slot(ArrayList<OMElement> query, String name, String value) {
		OMElement parent = query.get(1);
		OMElement slot = MetadataSupport.om_factory.createOMElement("Slot", MetadataSupport.ebRIMns3);
		parent.addChild(slot);
		slot.addAttribute("name", name, null);
		OMElement valuelist = MetadataSupport.om_factory.createOMElement("ValueList", MetadataSupport.ebRIMns3);
		slot.addChild(valuelist);
		OMElement value_ele = MetadataSupport.om_factory.createOMElement("Value", MetadataSupport.ebRIMns3);
		valuelist.addChild(value_ele);
		OMText text = MetadataSupport.om_factory.createOMText(value);
		value_ele.addChild(text);
	}

	protected void add_slot(ArrayList<OMElement> query, String name, ArrayList<String> values) {
		OMElement parent = query.get(1);
		OMElement slot = MetadataSupport.om_factory.createOMElement("Slot", MetadataSupport.ebRIMns3);
		parent.addChild(slot);
		slot.addAttribute("name", name, null);
		OMElement valuelist = MetadataSupport.om_factory.createOMElement("ValueList", MetadataSupport.ebRIMns3);
		slot.addChild(valuelist);
		for (String value : values) {
			OMElement value_ele = MetadataSupport.om_factory.createOMElement("Value", MetadataSupport.ebRIMns3);
			valuelist.addChild(value_ele);
			OMText text = MetadataSupport.om_factory.createOMText(value);
			value_ele.addChild(text);
		}
	}

	public OMElement run(String endpoint, ArrayList<String> ids, String queryAction, String queryResultAction) throws Exception {
		OMElement query = build(ids);
		logger.info("QUERY: " + query_details());
		Soap soap = new Soap();
		soap.soapCall(query, null /*Protocol*/, endpoint, false, true, true, queryAction, queryResultAction);
		OMElement result = soap.getResult();
		return result;
	}

	String query_array_list(ArrayList<String> strings) {
		StringBuffer buf = new StringBuffer();
		buf.append("(");
		boolean first = true;
		for (String id : strings) {
			if ( !first ) 
				buf.append(", ");
			else
				first = false;
			buf.append("'" + id + "'");
		}
		buf.append(")");
		return buf.toString();
	}

	String query_singleton(ArrayList<String> strings) {
		return "'" + strings.get(0) + "'";
	}

}
