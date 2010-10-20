package gov.nist.registry.ws.sq.ebxmlrr21;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.storedquery.StoredQuerySupport;
import gov.nist.registry.ws.sq.GetSubmissionSets;

import org.apache.axiom.om.OMElement;
import org.openhealthtools.openexchange.syslog.LoggerException;

/**
 * Implementation specific class for FindDocuments stored query. 
 * All the logic is in the runImplementation() method.
 * @author bill
 *
 */
public class EbXML21GetSubmissionSets extends GetSubmissionSets {

	EbXML21QuerySupport eb;

	/**
	 * Constructor
	 * @param sqs
	 * @throws MetadataValidationException
	 */
	public EbXML21GetSubmissionSets(StoredQuerySupport sqs) {
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
		if (uuids != null) {
			Metadata metadata;
			OMElement ele = eb.get_submissionsets_of_contents(uuids);
			// this may contain duplicates - parse differently
			metadata = new Metadata();
			metadata.addMetadata(ele, true);

			if (sqs.isLeafClass()) {
				if (metadata.getSubmissionSetIds().size() > 0) {
					OMElement assocs_ele = eb.get_associations("HasMember", metadata.getSubmissionSetIds(), uuids);
					metadata.addMetadata(assocs_ele, true);
				}
			} else {
				if (metadata.getObjectRefIds().size() > 0) {
					OMElement assocs_ele = eb.get_associations("HasMember", metadata.getObjectRefIds(), uuids);
					metadata.addMetadata(assocs_ele, true);
				}
			}
			return metadata;
		}
		else {
			throw new XdsException("EbXML21GetSubmissionSets#runImplementation: internal error: no format selected");
		}
	}

}
