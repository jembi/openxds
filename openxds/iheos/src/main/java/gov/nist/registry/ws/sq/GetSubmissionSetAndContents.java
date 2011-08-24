package gov.nist.registry.ws.sq;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XDSRegistryOutOfResourcesException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.SQCodedTerm;
import gov.nist.registry.common2.registry.storedquery.StoredQuerySupport;

import org.apache.log4j.Logger;
import org.openhealthtools.openxds.log.LoggerException;

/**
Generic implementation of GetAssociations Stored Query. This class knows how to parse a 
 * GetAssociations Stored Query request producing a collection of instance variables describing
 * the request.  A sub-class must provide the runImplementation() method that uses the pre-parsed
 * information about the stored query and queries a metadata database.
 * @author bill
 *
 */
abstract public class GetSubmissionSetAndContents extends StoredQuery {
	
	/**
	 * Method required in subclasses (implementation specific class) to define specific
	 * linkage to local database
	 * @return matching metadata
	 * @throws MetadataException
	 * @throws XdsException
	 * @throws LoggerException
	 */
	abstract protected Metadata runImplementation() throws MetadataException, XdsException, LoggerException;


	/**
	 * Basic constructor
	 * @param sqs
	 * @throws MetadataValidationException
	 */
	public GetSubmissionSetAndContents(StoredQuerySupport sqs) {
		super(sqs);
	}
	
	void validateParameters() throws MetadataValidationException {


		//                         param name,                        required?, multiple?, is string?,   is code?,  AND/OR ok?,   alternative
		sqs.validate_parm("$XDSSubmissionSetEntryUUID",                true,      false,     true,         false,     false,       "$XDSSubmissionSetUniqueId");
		sqs.validate_parm("$XDSSubmissionSetUniqueId",                 true,      false,     true,         false,     false,       "$XDSSubmissionSetEntryUUID");
		sqs.validate_parm("$XDSDocumentEntryFormatCode",               false,     true,      true,         true,      false,      (String[])null);
		sqs.validate_parm("$XDSDocumentEntryConfidentialityCode",      false,     true,      true,         true,      true,      (String[])null);

		if (sqs.has_validation_errors) 
			throw new MetadataValidationException("Metadata Validation error present");
	}
	
	protected String ss_uuid;
	protected String ss_uid;
	protected SQCodedTerm format_code;
	protected SQCodedTerm conf_code;
	
	void parseParameters() throws XdsInternalException, XdsException, LoggerException {
		ss_uuid = sqs.params.getStringParm("$XDSSubmissionSetEntryUUID");
		ss_uid = sqs.params.getStringParm("$XDSSubmissionSetUniqueId");
		format_code = sqs.params.getCodedParm("$XDSDocumentEntryFormatCode");
		conf_code = sqs.params.getCodedParm("$XDSDocumentEntryConfidentialityCode");
	}


	/**
	 * Implementation of Stored Query specific logic including parsing and validating parameters.
	 * @throws XdsInternalException
	 * @throws XdsException
	 * @throws LoggerException
	 * @throws XDSRegistryOutOfResourcesException
	 */
	public Metadata runSpecific() throws XdsException, LoggerException {

		validateParameters();
		parseParameters();

		if (ss_uuid == null && ss_uid == null) 
			throw new XdsInternalException("GetSubmissionSetAndContents Stored Query");
		return runImplementation();
	}



}
