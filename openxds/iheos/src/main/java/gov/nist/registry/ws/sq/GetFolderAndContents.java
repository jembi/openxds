package gov.nist.registry.ws.sq;

import org.openhealthtools.openxds.log.LoggerException;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XDSRegistryOutOfResourcesException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.storedquery.StoredQuerySupport;

/**
Generic implementation of GetAssociations Stored Query. This class knows how to parse a 
 * GetAssociations Stored Query request producing a collection of instance variables describing
 * the request.  A sub-class must provide the runImplementation() method that uses the pre-parsed
 * information about the stored query and queries a metadata database.
 * @author bill
 *
 */
abstract public class GetFolderAndContents extends StoredQuery {
	
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
	public GetFolderAndContents(StoredQuerySupport sqs) {
		super(sqs);
	}
	
	void validateParameters() throws MetadataValidationException {
		//                         param name,                             required?, multiple?, is string?,   is code?,  AND/OR ok?,   alternative
		sqs.validate_parm("$XDSFolderEntryUUID",                         true,      false,     true,         false,     false,       "$XDSFolderUniqueId");
		sqs.validate_parm("$XDSFolderUniqueId",                          true,      false,     true,         false,     false,       "$XDSFolderEntryUUID");
		sqs.validate_parm("$XDSDocumentEntryFormatCode",                 false,     true,      true,         true,      false,      (String[])null);
		sqs.validate_parm("$XDSDocumentEntryConfidentialityCode",        false,     true,      true,         true,      true,      (String[])null);
		
		System.out.println("GFAC: validating parms response: " + sqs.response);

		if (sqs.has_validation_errors) 
			throw new MetadataValidationException("Metadata Validation error present");
	}
	
	protected String fol_uuid;
	protected String fol_uid;

	void parseParameters() throws XdsInternalException, XdsException, LoggerException {
		fol_uuid = sqs.params.getStringParm("$XDSFolderEntryUUID");
		fol_uid = sqs.params.getStringParm("$XDSFolderUniqueId");
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

		if (fol_uuid == null && fol_uid == null) 
			throw new XdsInternalException("GetFolderAndContents Stored Query");
	
		return runImplementation();
	}



}
