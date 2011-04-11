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

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;

/**
 * Utility class creates header links structure for all the query results. 
 * @author <a href="anilkumar.reddy@misys.com">Anil Kumar</a>
 */

public class RetrieveBQueryContents extends QueryContents {

	String content_type;
	byte[] contents;
	String id;
	
	public void displayStructure(QueryControl q_cntl, int index, HttpUtils h, Xdsview xv, String cntl) 
	throws MetadataValidationException, MetadataException {

		displayStructureHeader(index, xv, this.initial_evidence);

		h.indent1(xv.build_details_link(id, cntl) + 
				" (" + xv.build_xml_link(id, cntl) + " " +
				xv.build_display_doc_link(String.valueOf(index), id) +
				")"
				);
	}	
	
	public void set_contents(byte[] contents, String content_type) {
		this.contents = contents;
		this.content_type = content_type;
	}
	
	public void set_id(String id) {
		this.id = id;
	}
	
	public String get_content_type() {
		return this.content_type;
	}
	
	public byte[] get_content() {
		return this.contents;
	}
	

}
