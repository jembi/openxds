package gov.nist.registry.ws.sq.ebxmlrr21;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataParser;
import gov.nist.registry.common2.registry.storedquery.StoredQuerySupport;
import gov.nist.registry.ws.sq.GetFolders;

import org.openhealthtools.openxds.log.LoggerException;

/**
 * Implementation specific class for GetFolders stored query. 
 * All the logic is in the runImplementation() method.
 * @author bill
 *
 */
public class EbXML21GetFolders extends GetFolders {

	EbXML21QuerySupport eb;

	/**
	 * Constructor
	 * @param sqs
	 * @throws MetadataValidationException
	 */
	public EbXML21GetFolders(StoredQuerySupport sqs) {
		super(sqs);
		eb = new EbXML21QuerySupport(sqs);
	}

	/**
	 * Main method, runs query logic
	 * @return Metadata
	 * @throws MetadataException
	 * @throws XdsException
	 */
	public Metadata runImplementation() throws MetadataException, XdsException, LoggerException {
		Metadata metadata;
		if (fol_uuid != null) {
			// starting from uuid
			metadata = MetadataParser.parseNonSubmission(eb.getFolByUuid(fol_uuid));
		} else {
			// starting from uniqueid
			metadata = MetadataParser.parseNonSubmission(eb.getFolByUid(fol_uid));
		}
		return metadata;
	}

}
