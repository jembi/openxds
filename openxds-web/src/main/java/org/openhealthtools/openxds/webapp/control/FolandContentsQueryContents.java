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

import java.util.ArrayList;

import org.apache.axiom.om.OMElement;

/**
 * This class creates links structure for the GetFoldesAndContents query. 
 * @author <a href="anilkumar.reddy@misys.com">Anil Kumar</a>
 */
public class FolandContentsQueryContents  extends QueryContents {
	public void displayStructure(QueryControl q_cntl, int index, HttpUtils h, Xdsview xv, String cntl) 
	throws MetadataValidationException, MetadataException  {
		System.out.println("FolandContents Query Contents");

		Metadata m = getMetadata();		
		boolean object_refs = m.isObjectRefsOnly();
		if (m.getWrapper() != null) {
			xv.getMetadata().addMetadata(m);
			displayStructureHeader(index, xv, this.initial_evidence);
		} else {
			displayStructureHeader(index, xv, this.initial_evidence);
			return;
		}

		ArrayList<String> ids_displayed = new ArrayList<String>();

		if (m.getFolderIds().size() != 0) {
			String fol_id = m.getFolderIds().get(0);
			ids_displayed.add(fol_id);

			h.indent1(xv.build_details_link(fol_id, cntl) + " (" + xv.build_xml_link(fol_id, cntl) + ")");

			ids_displayed.add(fol_id);
			for (OMElement a_ele : m.getAssociations()) {
				ids_displayed.add(m.getId(a_ele));
				String a_id = m.getId(a_ele);
				String src_id = m.getAssocSource(a_ele);
				String tgt_id = m.getAssocTarget(a_ele);
				if ( src_id.equals(fol_id)) {
					h.indent2(xv.build_assoc_link(a_id, "contains", cntl) + " " + 
							xv.build_details_link(tgt_id, cntl) + " (" + 
							xv.build_xml_link(tgt_id, cntl) + " " + ") " +
							document_actions(q_cntl, m, xv, tgt_id)
					);
					ids_displayed.add(tgt_id);
				} 
			}
		}
		for (OMElement ele : m.getAllObjects()) {
			String id = m.getId(ele);
			String type = m.type(id);
			if ( !ids_displayed.contains(id)) {
				if (type == null) {
					//h.indent1("null");
				}
				else 
					if (type.equals("Association")) {
					h.indent1(xv.symbolic(m.getAssocSource(ele)) + " " + type + " " + xv.symbolic(m.getAssocTarget(ele)));
				} else if (!object_refs && type.equals("ObjectRef")) {

				} else {
					h.indent1(type + " " + " (" + xv.build_xml_link(id, cntl) + ")");
				}
			}
		}

	}

}
