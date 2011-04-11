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
 * This class builds GetRelatedDocuments query. 
 * @author <a href="anilkumar.reddy@misys.com">Anil Kumar</a>
 */
public class GetRelatedDocuments extends Sq {
	ArrayList<String> ids = null;

	String query_details() {
		return "GetRelatedDocuments " + ids;
	}

	OMElement build(ArrayList<String> ids) {
		this.ids = ids;
		ArrayList<OMElement> query = build_query_wrapper("urn:uuid:d90e5407-b356-4d91-a89f-873917b4b0e6");

		if (ids.get(0).startsWith("urn:uuid:")) 
			add_slot(query, "$XDSDocumentEntryEntryUUID", this.query_singleton(ids));
		else
			add_slot(query, "$XDSDocumentEntryUniqueId", this.query_singleton(ids));

		ArrayList<String> atypes = new ArrayList<String>();


		atypes.add("('urn:ihe:iti:2007:AssociationType:RPLC')");
		atypes.add("('urn:ihe:iti:2007:AssociationType:XFRM')");
		atypes.add("('urn:ihe:iti:2007:AssociationType:APND')");
		atypes.add("('urn:ihe:iti:2007:AssociationType:XFRM_RPLC')" );
		atypes.add("('urn:ihe:iti:2007:AssociationType:signs')");
		atypes.add("('urn:ihe:iti:2007:AssociationType:HasMember')" );

		add_slot(query, "$AssociationTypes", atypes);
		return query.get(0);
	}


}
