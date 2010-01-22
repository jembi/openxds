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
import gov.nist.registry.common2.registry.MetadataParser;
import gov.nist.registry.common2.xml.XmlFormatter;

import org.apache.axiom.om.OMElement;

/**
 * This class logs for the querys. 
 * @author <a href="anilkumar.reddy@misys.com">Anil Kumar</a>
 */
public class LogFileQueryContents extends QueryContents {
	String step_status;
	OMElement input;
	OMElement result;
	OMElement teststep;
	String result_status;
	String expected_status;
	boolean metadata_found_in_input;
	boolean metadata_found = false;

	public void displayStructure(QueryControl q_cntl, int index, HttpUtils h, Xdsview xv, String cntl) 
	throws MetadataValidationException, MetadataException {
		QueryContents ssac = new SSandContentsQueryContents();
		ssac.dup(this);

		if (result != null ) {
			if ( result.getLocalName().equals("AdhocQueryResponse"))
				this.setMetadata(MetadataParser.parseNonSubmission(result));
		}
		if (input != null ) {
			if ( input.getLocalName().equals("SubmitObjectsRequest"))
				this.setMetadata(MetadataParser.parseNonSubmission(input));
		}

		ssac.displayStructure(q_cntl, index, h, xv, String.valueOf(index));



	/*	StringBuffer buf = new StringBuffer();
		//buf.append("Step " + this.reference_id);
		if (teststep != null)
			buf.append(" " + xv.h().link("stepxml", "/openxds-web/innerquery.action?verb=stepxml&cntl=" + index));
		if (input != null)
			buf.append(" " + xv.h().link("inputxml", "/openxds-web/innerquery.action?verb=inputxml&cntl=" + index));
		if (result != null)
			buf.append(" " + xv.h().link("resultxml", "/openxds-web/innerquery.action?verb=resultxml&cntl=" + index));
		xv.h().indent1(buf.toString());
		xv.h().indent1("StepStatus " + step_status);
		xv.h().indent1("ExpectedStatus " + expected_status);*/

	}

	public void displayInput(Xdsview xv) {
		System.out.println("displayInput");
		if (input != null)
			xv.out(XmlFormatter.htmlize(input.toString(), false));
//		else
//		xv.out("null");
	}

	public void displayTestStep(Xdsview xv) {
		System.out.println("displayTestStep");
		if (teststep != null)
			xv.out(XmlFormatter.htmlize(teststep.toString(), false));
//		else
//		xv.out("null");
	}

	public void displayResult(Xdsview xv) {
		System.out.println("displayResult");
		if (result != null)
			xv.out(XmlFormatter.htmlize(result.toString(), false));
//		else
//		xv.out("null");
	}

	// not sure this works
	void findMetadata() {
		try {
			Metadata m1=null, m2=null;
			if (this.input != null) {
				m1 = MetadataParser.parseNonSubmission(this.input);
				if (m1.getAllObjects().size() > 0) {
					this.setMetadata(m1);
					this.metadata_found_in_input = true;
					this.metadata_found = true;
					return;
				}
			}
			if (this.result != null) {
				m2 = MetadataParser.parseNonSubmission(this.result);
				if (m2.getAllObjects().size() > 0) {
					this.setMetadata(m2);
					this.metadata_found_in_input = false;
					this.metadata_found = true;
					return;
				}
			}
		}
		catch (Exception e) {
		}
	}

	public void validate_inputs() {
		StringBuffer buf = new StringBuffer();

		buf.append("Validate Inputs\n");
		
		if (this.input == null)
			buf.append("Input is null\n");

		if (this.result == null)
			buf.append("Result is null\n");

		buf.append("Metadata found is " + ((this.metadata_found) ? "true" : "false") + "\n");
		if (this.metadata_found)
			buf.append("...in " + ((this.metadata_found_in_input) ? "input" : "result") + "\n");
		
		if (this.getMetadata() != null)
			buf.append("Metadata found\n");
		else
			buf.append("Metadata not found\n");


		this.addError(buf.toString());
		
		System.out.println(buf.toString());
		System.out.flush();
	}

}
