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

import java.util.ArrayList;

import org.apache.axiom.om.OMElement;

/**
 * This class builds FindSubmissionSets query. 
 * @author <a href="anilkumar.reddy@misys.com">Anil Kumar</a>
 */
public class FindSubmissionSets extends Sq {
	ArrayList<String> ids = null;

	String query_details() {
		return "FindSubmissionSets " + ids;
	}

	OMElement build(ArrayList<String> ids) {
		this.ids = ids;
		ArrayList<OMElement> query = build_objectref_query_wrapper("urn:uuid:f26abbcb-ac74-4422-8a30-edb644bbc1a9");
		String stat_list = "('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved', 'urn:oasis:names:tc:ebxml-regrep:StatusType:Deprecated')";
		add_slot(query, "$XDSSubmissionSetPatientId", this.query_singleton(ids));
		add_slot(query, "$XDSSubmissionSetStatus", stat_list);
		return query.get(0);
	}

}
