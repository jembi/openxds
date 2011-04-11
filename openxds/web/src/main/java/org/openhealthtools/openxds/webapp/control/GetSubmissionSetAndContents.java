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
 * This class builds GetSubmissionSetAndContents query. 
 * @author <a href="anilkumar.reddy@misys.com">Anil Kumar</a>
 */
public class GetSubmissionSetAndContents extends Sq {
	ArrayList<String> ids = null;

	String query_details() {
		return "GetSubmissionSetAndContents " + ids;
	}

	OMElement build(ArrayList<String> ids) {
		this.ids = ids;
		ArrayList<OMElement> query = build_query_wrapper("urn:uuid:e8e3cb2c-e39c-46b9-99e4-c12f57260b83");
		String id_parm = "'" + ids.get(0) + "'";
		if (ids.get(0).startsWith("urn:uuid:")) {
			add_slot(query, "$XDSSubmissionSetEntryUUID", id_parm);
		}
		else {
			add_slot(query, "$XDSSubmissionSetUniqueId", id_parm); 
		}
		return query.get(0);
	}

}
