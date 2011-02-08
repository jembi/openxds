package gov.nist.registry.ws.sq;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.SQCodedTerm;
import gov.nist.registry.common2.registry.storedquery.StoredQuerySupport;

import java.util.List;

import org.openhealthtools.openexchange.syslog.LoggerException;

/**
Generic implementation of FindSubmissionSets Stored Query. This class knows how to parse a 
 * FindSubmissionSets Stored Query request producing a collection of instance variables describing
 * the request.  A sub-class must provide the runImplementation() method that uses the pre-parsed
 * information about the stored query and queries a metadata database.
 * @author bill
 *
 */
abstract public class FindSubmissionSets extends StoredQuery {

	/**
	 * Method required in subclasses (implementation specific class) to define specific
	 * linkage to local database
	 * @return matching metadata
	 * @throws MetadataException
	 * @throws XdsException
	 * @throws LoggerException
	 */
	abstract protected Metadata runImplementation() throws MetadataException, XdsException, LoggerException;

	public FindSubmissionSets(StoredQuerySupport sqs) throws MetadataValidationException {
		super(sqs);
	}
	
	void validateParameters() throws MetadataValidationException {
		
		//                         param name,                                 required?, multiple?, is string?,   is code?,     support AND/OR                          alternative
		sqs.validate_parm("$XDSSubmissionSetPatientId",                         true,      false,     true,         false,         false,                                 (String[])null												);
		sqs.validate_parm("$XDSSubmissionSetSourceId",                          false,     true,      true,         false,         false,                                 (String[])null												);
		sqs.validate_parm("$XDSSubmissionSetSubmissionTimeFrom",                false,     false,     true,         false,         false,                                 (String[])null												);
		sqs.validate_parm("$XDSSubmissionSetSubmissionTimeTo",                  false,     false,     true,         false,         false,                                 (String[])null												);
		sqs.validate_parm("$XDSSubmissionSetAuthorPerson",                      false,     false,     true,         false,         false,                                 (String[])null												);
		sqs.validate_parm("$XDSSubmissionSetContentType",                       false,     true,      true,         true,          false,                               (String[])null												);
		sqs.validate_parm("$XDSSubmissionSetStatus",                            true,      true,      true,         false,         false,                                 (String[])null												);
		
		if (sqs.has_validation_errors) 
			throw new MetadataValidationException("Metadata Validation error present");
	}

	public Metadata runSpecific() throws XdsInternalException, XdsException, LoggerException {
		
		if (sqs.log_message != null)
			sqs.log_message.addOtherParam("SqParams", sqs.params.toString());
		
		validateParameters();

		parseParameters();

		return runImplementation();
	}
	
	protected String               patient_id;
	protected List<String>    source_id;
	protected String               submission_time_from;
	protected String               submission_time_to;
	protected String               author_person;
	protected SQCodedTerm         content_type;
	protected List<String>    status;

	void parseParameters() throws XdsInternalException, XdsException, LoggerException {
		
		patient_id                               = sqs.params.getStringParm   ("$XDSSubmissionSetPatientId");
		source_id                                = sqs.params.getListParm("$XDSSubmissionSetSourceId");
		submission_time_from                     = sqs.params.getIntParm      ("$XDSSubmissionSetSubmissionTimeFrom");
		submission_time_to                       = sqs.params.getIntParm      ("$XDSSubmissionSetSubmissionTimeTo");
		author_person                            = sqs.params.getStringParm   ("$XDSSubmissionSetAuthorPerson");
		content_type                             = sqs.params.getCodedParm("$XDSSubmissionSetContentType");
		status                                   = sqs.params.getListParm("$XDSSubmissionSetStatus");

		String status_ns_prefix = MetadataSupport.status_type_namespace;
		
//		ArrayList new_status = new ArrayList();
		for (int i=0; i<status.size(); i++) {
			String stat = (String) status.get(i);
			
			if ( ! stat.startsWith(status_ns_prefix)) 
				throw new MetadataValidationException("Status parameter must have namespace prefix " + status_ns_prefix + " found " + stat);
//			new_status.add(stat.replaceFirst(status_ns_prefix, ""));
		}
//		status = new_status;
	}

	
}
