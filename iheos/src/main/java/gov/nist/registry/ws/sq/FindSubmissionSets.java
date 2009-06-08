package gov.nist.registry.ws.sq;

import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataParser;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.StoredQuery;
import gov.nist.registry.xdslog.LoggerException;
import gov.nist.registry.xdslog.Message;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.axiom.om.OMElement;

public class FindSubmissionSets extends StoredQuery {

	public FindSubmissionSets(HashMap params, boolean return_objects, Response response, Message log_message, boolean is_secure) throws MetadataValidationException {
		super(params, return_objects, response, log_message,  is_secure);


		//                         param name,                                      required?, multiple?, is string?,   same size as,                                alternative
		validate_parm(params, "$XDSSubmissionSetPatientId",                         true,      false,     true,         null,                                          null												);
		validate_parm(params, "$XDSSubmissionSetSourceId",                          false,     true,      true,         null,                                          null												);
		validate_parm(params, "$XDSSubmissionSetSubmissionTimeFrom",                false,     false,     true,         null,                                          null												);
		validate_parm(params, "$XDSSubmissionSetSubmissionTimeTo",                  false,     false,     true,         null,                                          null												);
		validate_parm(params, "$XDSSubmissionSetAuthorPerson",                      false,     false,     true,         null,                                          null												);
		validate_parm(params, "$XDSSubmissionSetContentType",                       false,     true,      true,         null,                                          null												);
		validate_parm(params, "$XDSSubmissionSetStatus",                            true,      true,      true,         null,                                          null												);
		
		if (this.has_validation_errors) 
			throw new MetadataValidationException("Metadata Validation error present");
	}

	public Metadata run_internal() throws XdsInternalException, XdsException, LoggerException {

		OMElement results = impl();
				
		Metadata m = MetadataParser.parseNonSubmission(results);
		
		if (log_message != null)
			log_message.addOtherParam("Results structure", m.structure());

		return m;
	}
	
	OMElement impl() throws XdsInternalException, XdsException, LoggerException {
		
		String               patient_id                               = this.get_string_parm   ("$XDSSubmissionSetPatientId");
		ArrayList<String>    source_id                                = this.get_arraylist_parm("$XDSSubmissionSetSourceId");
		String               submission_time_from                     = this.get_int_parm      ("$XDSSubmissionSetSubmissionTimeFrom");
		String               submission_time_to                       = this.get_int_parm      ("$XDSSubmissionSetSubmissionTimeTo");
		String               author_person                            = this.get_string_parm   ("$XDSSubmissionSetAuthorPerson");
		ArrayList<String>    content_type                             = this.get_arraylist_parm("$XDSSubmissionSetContentType");
		ArrayList<String>    status                                   = this.get_arraylist_parm("$XDSSubmissionSetStatus");

		String status_ns_prefix = "urn:oasis:names:tc:ebxml-regrep:StatusType:";
		
		ArrayList new_status = new ArrayList();
		for (int i=0; i<status.size(); i++) {
			String stat = (String) status.get(i);
			
			if ( ! stat.startsWith(status_ns_prefix)) 
				throw new MetadataValidationException("Status parameter must have namespace prefix " + status_ns_prefix + " found " + stat);
			new_status.add(stat.replaceFirst(status_ns_prefix, ""));
		}
		status = new_status;
		
		
		// add test of ('')
		
		
		init();

		if (this.return_leaf_class) {
		     a("SELECT *  "); n();
		} else {
		     a("SELECT doc.id  "); n();
		}
		                                                  a("FROM RegistryPackage doc, ExternalIdentifier patId"); n();
		if (source_id != null)                            a(", ExternalIdentifier srcId"); n();
		if (submission_time_from != null)                 a(", Slot sTimef"); n();              
		if (submission_time_to != null)                   a(", Slot sTimet"); n();              
		if (author_person != null)                        a(", Classification author"); n();
		if (author_person != null)                        a(", Slot authorperson"); n();
		if (content_type != null)                         a(", Classification contentT"); n();
				
		
		                                                  a("WHERE"); n();
		// patientID
		                                                  a("(doc.id = patId.registryobject AND	"); n();
		                                                  a("  patId.identificationScheme='urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446' AND "); n();
		                                                  a("  patId.value = '"); a(patient_id); a("' ) "); n();
		                      
		if (source_id != null) {
			a("AND"); n();
            a("(doc.id = srcId.registryobject AND	"); n();
            a("  srcId.identificationScheme='urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832' AND "); n();
            a("  srcId.value IN "); a(source_id); a(" ) "); n();
		}
		this.add_times("submissionTime",     "sTimef",       "sTimet",       submission_time_from,      submission_time_to, "doc");
		
		if (author_person != null) {
			a("AND"); n();
			a("(doc.id = author.classifiedObject AND "); n();
			a("  author.classificationScheme='urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d' AND "); n();
			a("  authorperson.parent = author.id AND"); n();
			a("  authorperson.name = 'authorPerson' AND"); n();
			a("  authorperson.value LIKE '" + author_person + "' )"); n();
		}
		                                                  
		this.add_code("contentT", null, "urn:uuid:aa543740-bdda-424e-8c96-df4873be8500", content_type,            null);
		
		a("AND doc.status IN "); a(status);
		
		
			return query(this.return_leaf_class);
		

	}
	
	
}
