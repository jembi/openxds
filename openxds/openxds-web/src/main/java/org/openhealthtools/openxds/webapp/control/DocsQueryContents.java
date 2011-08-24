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

/**
 * This class builds structure for document display.
 * @author <a href="anilkumar.reddy@misys.com">Anil Kumar</a>
 */
public class DocsQueryContents extends PagedQueryContents {

		public void displayStructure(QueryControl q_cntl, int index, HttpUtils h, Xdsview xv, String cntl) 
		throws MetadataValidationException, MetadataException {
			System.out.println("Docs QueryContents");

			Metadata m = getMetadata();		
			boolean object_refs = m.isObjectRefsOnly();
			if (m.getWrapper() != null) {
				xv.getMetadata().addMetadata(m);
				this.displayStructureHeader(index, xv, this.initial_evidence);
			} else {
				this.displayStructureHeader(index, xv, this.initial_evidence);
				return;
			}

			if (object_refs) {
			} else {
				
				for (String id : m.getExtrinsicObjectIds()) {
					h.indent1(xv.build_details_link(id, cntl) + 
							" (" + xv.build_xml_link(id, cntl) + 
							")" + 
							" {" + 
							//((m.isRetrievable_a(id)) ? xv.build_ret_a_link(id) + " "  : ""  ) +
							//((m.isRetrievable_b(id)) ? xv.build_ret_b_link(id) + " "  : ""  ) +
							((q_cntl.hasEndpoint()) ? xv.build_related_link(id) : "") + " " +
							((q_cntl.hasEndpoint()) ? xv.build_ss_link(id) : "") +
							"}");
				}
			}

		}

}
