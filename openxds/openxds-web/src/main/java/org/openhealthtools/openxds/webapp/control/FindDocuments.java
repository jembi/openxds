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

package org.openhealthtools.openxds.webapp.control;

import java.util.ArrayList;

import org.apache.axiom.om.OMElement;

/**
 * This class builds FindDocuments query. 
 * @author <a href="anilkumar.reddy@misys.com">Anil Kumar</a>
 */
public class FindDocuments extends Sq {
	ArrayList<String> ids = null;
	boolean useLeafClass = true;
	QueryContents queryContents;

	String query_details() {
		return "FindDocuments " + ids;
	}

	public FindDocuments(boolean useLeafClass, QueryContents queryContents) {
		this.useLeafClass = useLeafClass;
		this.queryContents = queryContents;
	}

	OMElement build(ArrayList<String> ids) {
		this.ids = ids;
		ArrayList<OMElement> query;
		if (useLeafClass)
			query = build_query_wrapper("urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d");
		else
			query = build_objectref_query_wrapper("urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d");
		String stat_list = "('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved')";
		add_slot(query, "$XDSDocumentEntryPatientId", this.query_singleton(ids));
		add_slot(query, "$XDSDocumentEntryStatus", stat_list);
		queryContents.setRequestXML(query.get(0));
		return query.get(0);
	}

}
