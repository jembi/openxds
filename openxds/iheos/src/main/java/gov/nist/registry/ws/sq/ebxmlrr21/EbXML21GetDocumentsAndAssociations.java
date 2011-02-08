package gov.nist.registry.ws.sq.ebxmlrr21;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataParser;
import gov.nist.registry.common2.registry.storedquery.StoredQuerySupport;
import gov.nist.registry.ws.sq.GetDocumentsAndAssociations;

import java.util.List;

import org.apache.axiom.om.OMElement;
import org.openhealthtools.openexchange.syslog.LoggerException;

/**
 * Implementation specific class for GetDocumentsAndAssociations stored query. 
 * All the logic is in the runImplementation() method.
 * @author bill
 *
 */
public class EbXML21GetDocumentsAndAssociations extends GetDocumentsAndAssociations {

	EbXML21QuerySupport eb;

	/**
	 * Constructor
	 * @param sqs
	 * @throws MetadataValidationException
	 */
	public EbXML21GetDocumentsAndAssociations(StoredQuerySupport sqs) {
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
		Metadata metadata;
		OMElement doc_ele;
		if (uids != null) {
			doc_ele = eb.getDocByUid(uids);
		} else {
			doc_ele = eb.getDocById(uuids);
		}


		metadata = MetadataParser.parseNonSubmission(doc_ele);

		List<String> doc_ids;
		if (sqs.return_leaf_class) {

			doc_ids = metadata.getExtrinsicObjectIds();

		} else {
			doc_ids = metadata.getObjectRefIds();
		}
		
		// for documents, get associations
		if (sqs.log_message != null)
			sqs.log_message.addOtherParam("Doc count", Integer.toString(doc_ids.size()));
		if (doc_ids.size() == 0)
			return metadata;
		
		OMElement ele = eb.getAssociations(doc_ids, null);
		metadata.addMetadata(ele, true);

		return metadata;
	}


}
