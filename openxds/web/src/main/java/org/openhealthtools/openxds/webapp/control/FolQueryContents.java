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
import gov.nist.registry.common2.registry.Metadata;

import org.apache.axiom.om.OMElement;

/**
 * This class builds links for FolQueryContents result.
 * @author <a href="anilkumar.reddy@misys.com">Anil Kumar</a>
 */
public class FolQueryContents  extends PagedQueryContents {
	
	// this class is totally untested
	public void displayStructure(QueryControl q_cntl, int index, HttpUtils h, Xdsview xv, String cntl) 
	throws MetadataValidationException, MetadataException  {
		System.out.println("Fol Query Contents");

		Metadata m = getMetadata();	
		if (m == null) {
			displayStructureHeader(index, xv);
			return;
		}
		
		if (m.getWrapper() != null) {
			xv.getMetadata().addMetadata(m);
			displayStructureHeader(index, xv);
		} else {
			displayStructureHeader(index, xv);
			return;
		}

		
		boolean object_refs = m.isObjectRefsOnly();
		if (object_refs) {
			//h.indent1(this.size() + " elements " + xv.build_next_page_link(String.valueOf(index)));
		} else {
			String ref_id = this.getReferenceId();
			for (OMElement assoc_ele : m.getAssociations()) {
				String source_id = m.getAssocSource(assoc_ele);
				h.indent1(xv.build_details_link(ref_id, cntl) + " (" + xv.build_xml_link(ref_id, cntl) + ")");

				h.indent2(xv.build_assoc_link(m.getId(assoc_ele), "member of", cntl) +
						" (" + xv.build_details_link(source_id, cntl) +
						" " + xv.build_xml_link(source_id, cntl) +
						") {" + xv.build_sscontents_link(source_id) +
						"} "
				);
			}
		}

	}

}
