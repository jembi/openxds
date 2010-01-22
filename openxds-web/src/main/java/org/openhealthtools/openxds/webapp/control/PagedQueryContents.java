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

import gov.nist.registry.common2.registry.Metadata;

import java.util.ArrayList;
import java.util.List;

import org.apache.axiom.om.OMElement;

/**
 * This class handles the pagination on query results. 
 * @author <a href="anilkumar.reddy@misys.com">Anil Kumar</a>
 */
abstract public class PagedQueryContents extends QueryContents {

	List<String> ids;
	List<OMElement> elements;
	short type = 0;
	int pagesize=5;
	int current;
	QueryControl query_control;

	void init(QueryControl q_cntl) {
		this.query_control = q_cntl;

		this.elements = this.getMetadata().getObjectRefs();
		this.ids = this.getMetadata().getObjectIds(elements);

		current = 0;
		type = 0;
	}

	int size() { if (ids == null) return 0; else return ids.size(); }

	public void next() {
		current += pagesize;
		preload();
	}

	void display_paging(Xdsview xv, String cntl, HttpUtils h) {

		for (int i=current; i<current+pagesize && i<ids.size(); i++) {
			String id = ids.get(i);
			h.indent1(xv.build_details_link(id, cntl) +
					" " + xv.build_xml_link(id, cntl));
		}

	}

	void preload() {
		boolean loaded = true;
		for (int i=current; i<current+pagesize && i<ids.size(); i++) {
			if (elements.get(i).getLocalName().equals("ObjectRef")) {
				loaded = false;
				break;
			}
		}
		if ( !loaded) 
			load();
	}

	void load() {
		String header = current+1 + " to " + (current+pagesize) + " of " + size();

		ArrayList<String> lids = new ArrayList<String>(pagesize);
		for (int i=current; i<current+pagesize && i<ids.size(); i++) 
			lids.add(ids.get(i));

		QueryContents qc = null;
		try {
			if (is_ss())  {
				for (String aid : lids) {
					qc = query_control.queryGetSSandContents(query_control.singleton(aid));
					qc.header_extra = header;
					Metadata m = qc.getMetadata();
					for (String id : m.getSubmissionSetIds()) {
						int index = ids.indexOf(id);
						if (index != -1) {
							elements.set(index, m.getObjectById(id));
						}
					}
				}
			}
			else if (is_doc())  {
				qc = query_control.queryGetDocuments(lids);
				qc.addHeaderExtra(header);
				int controller_index = query_control.getQueryContentsIndex(this);
				if (controller_index != -1) {
					//qc.addHeaderExtra("  " + Xdsview.build_next_page_link(String.valueOf(controller_index)));
				}
				Metadata m = qc.getMetadata();
				for (String id : m.getExtrinsicObjectIds()) {
					int index = ids.indexOf(id);
					if (index != -1) {
						elements.set(index, m.getObjectById(id));
					}
				}
			} else {
				this.addError("Unknown type of content - cannot display");
			}
		}
		catch (Exception e) {
			this.addException(e);
		}

	}

	void type_doc() { type = 1; }
	void type_fol()   { type = 2; }
	void type_ss()       { type = 3; }

	boolean is_doc() { return type == 1; }
	boolean is_fol()   { return type == 2; }
	boolean is_ss()       { return type == 3; } 

}
