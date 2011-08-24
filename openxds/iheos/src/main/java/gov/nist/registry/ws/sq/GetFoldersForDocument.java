package gov.nist.registry.ws.sq;

import org.openhealthtools.openxds.log.LoggerException;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XDSRegistryOutOfResourcesException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.storedquery.StoredQuerySupport;


/**
Generic implementation of GetAssociations Stored Query. This class knows how to parse a 
 * GetAssociations Stored Query request producing a collection of instance variables describing
 * the request.  A sub-class must provide the runImplementation() method that uses the pre-parsed
 * information about the stored query and queries a metadata database.
 * @author bill
 *
 */
abstract public class GetFoldersForDocument extends StoredQuery {
	
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
	public GetFoldersForDocument(StoredQuerySupport sqs) {
		super(sqs);
	}
	
	void validateParameters() throws MetadataValidationException {
		//                         param name,                             required?, multiple?, is string?,   same size as,    alternative
		sqs.validate_parm("$XDSDocumentEntryUniqueId",                 true,      false,     true,         null,            "$XDSDocumentEntryEntryUUID");
		sqs.validate_parm("$XDSDocumentEntryEntryUUID",                true,      false,     true,         null,            "$XDSDocumentEntryUniqueId");

		if (sqs.has_validation_errors) 
			throw new MetadataValidationException("Metadata Validation error present");
	}

	protected String uid;
	protected String uuid;

	void parseParameters() throws XdsInternalException, XdsException, LoggerException {
		uid 			= sqs.params.getStringParm("$XDSDocumentEntryUniqueId");
		uuid 		    = sqs.params.getStringParm("$XDSDocumentEntryEntryUUID");
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

		if (uuid == null && uid == null) 
			throw new XdsInternalException("GetFoldersForDocument Stored Query");
		return runImplementation();
	}
}
