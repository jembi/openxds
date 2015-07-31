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

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.registry.Metadata;

import org.apache.axiom.om.OMElement;

/**
 * This class creates links structure for the GetRelatedDocuments query. 
 * @author <a href="anilkumar.reddy@misys.com">Anil Kumar</a>
 */
public class RelatedQueryContents extends QueryContents {

	public void displayStructure(QueryControl q_cntl, int index, HttpUtils h, Xdsview xv, String cntl) 
	throws MetadataValidationException, MetadataException {
		System.out.println("Related QueryContents");
		this.displayStructureHeader(index, xv);

		Metadata m = getMetadata();		
		boolean object_refs = m.isObjectRefsOnly();
		if (m.getWrapper() == null) return; 

		xv.getMetadata().addMetadata(m);

		
		if (object_refs) {
			// no idea what to do here
		} else {
			String ref_id = this.getReferenceId();
			h.indent1(xv.build_details_link(ref_id, cntl) + " (" + xv.build_xml_link(ref_id, cntl) + " " +
					")");

			for (OMElement a_ele : m.getAssociations()) {
				String src_id = m.getAssocSource(a_ele);
				String a_type = m.getSimpleAssocType(a_ele);
				String a_id = m.getId(a_ele);
				String tgt_id = m.getAssocTarget(a_ele);
				if (src_id == null) {
					
				} else if (src_id != null && !src_id.equals(this.getReferenceId())) {
					if (a_type.equals("RPLC")) a_type = "replaced by";
					else if (a_type.equals("APND")) a_type = "has appended";
					else if (a_type.equals("XFRM")) a_type = "transformed to";
					else a_type = a_type + " by";
					h.indent2(xv.build_assoc_link(a_id, a_type, cntl) + " " +
							xv.build_details_link(src_id, cntl) + 
							" (" + xv.build_xml_link(src_id, cntl)  + " " +
							") " +
							" [" + ((q_cntl.hasEndpoint()) ? xv.build_related_link(src_id) : "") +
					"]");
				} else {
					if (a_type.equals("RPLC")) a_type = "replaces";
					else if (a_type.equals("APND")) a_type = "appended to";
					else if (a_type.equals("XFRM")) a_type = "transform of";
					h.indent2(xv.build_assoc_link(a_id, a_type, cntl) + " " +
							xv.build_details_link(tgt_id, cntl) + 
							" (" + xv.build_xml_link(tgt_id, cntl) + " " + 
							") " +
							" [" + ((q_cntl.hasEndpoint()) ? xv.build_related_link(tgt_id) : "") +
					"]");
				}
			}
		}

	}

}
