package gov.nist.registry.ws.sq.ebxmlrr21;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataParser;
import gov.nist.registry.common2.registry.storedquery.StoredQuerySupport;
import gov.nist.registry.ws.sq.GetFoldersForDocument;

import org.apache.axiom.om.OMElement;
import org.openhealthtools.openxds.log.LoggerException;

/**
 * Implementation specific class for GetFoldersForDocument stored query. 
 * All the logic is in the runImplementation() method.
 * @author bill
 *
 */
public class EbXML21GetFoldersForDocument extends GetFoldersForDocument {


	EbXML21QuerySupport eb;

	/**
	 * Constructor
	 * @param sqs
	 * @throws MetadataValidationException
	 */
	public EbXML21GetFoldersForDocument(StoredQuerySupport sqs) {
		super(sqs);
		eb = new EbXML21QuerySupport(sqs);
	}

	/**
	 * Main method, runs query logic
	 * @return Metadata
	 * @throws MetadataException
	 * @throws XdsException
	 */
	public Metadata runImplementation() throws XdsException, LoggerException,
			MetadataException, MetadataValidationException {
				if (uuid == null || uuid.equals(""))
					uuid = eb.getDocIdFromUid(uid);
				
				if (uuid == null)
					throw new XdsException("Cannot identify referenced document (uniqueId = " + uid + ")");
				
				OMElement folders = eb.getFoldersForDocument(uuid);
				
				return MetadataParser.parseNonSubmission(folders);
			}

}
