package gov.nist.registry.ws.sq.ebxmlrr21;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataParser;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.SQCodedTerm;
import gov.nist.registry.common2.registry.storedquery.StoredQuerySupport;
import gov.nist.registry.ws.sq.GetSubmissionSetAndContents;

import java.util.ArrayList;
import java.util.List;

import org.apache.axiom.om.OMElement;
import org.openhealthtools.openxds.log.LoggerException;

/**
 * Implementation specific class for FindDocuments stored query. 
 * All the logic is in the runImplementation() method.
 * @author bill
 *
 */
public class EbXML21GetSubmissionSetAndContents extends GetSubmissionSetAndContents {

	EbXML21QuerySupport eb;

	/**
	 * Constructor
	 * @param sqs
	 * @throws MetadataValidationException
	 */
	public EbXML21GetSubmissionSetAndContents(StoredQuerySupport sqs) {
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
	MetadataException, MetadataValidationException,
	XdsInternalException {
		Metadata metadata;
		if (ss_uuid != null) {
			// starting from uuid
			OMElement x = eb.getRpById(ss_uuid, "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8");
			metadata = MetadataParser.parseNonSubmission(x);
			if (sqs.return_leaf_class && metadata.getSubmissionSets().size() != 1)
				return metadata;
			if (!sqs.return_leaf_class && metadata.getObjectRefs().size() != 1)
				return metadata;
		} else {
			// starting from uniqueid
			OMElement x = eb.getRpByUid(ss_uid, "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8");
			metadata = MetadataParser.parseNonSubmission(x);
			if (sqs.return_leaf_class && metadata.getSubmissionSets().size() != 1)
				return metadata;
			if (!sqs.return_leaf_class && metadata.getObjectRefs().size() != 1)
				return metadata;

			if (sqs.return_leaf_class)
				ss_uuid = metadata.getSubmissionSet().getAttributeValue(MetadataSupport.id_qname);
			else
				ss_uuid = metadata.getObjectRefs().get(0).getAttributeValue(MetadataSupport.id_qname);
		}

		// ss_uuid has now been set

		SQCodedTerm conf_codes = sqs.params.getCodedParm("$XDSDocumentEntryConfidentialityCode");
		SQCodedTerm format_codes = sqs.params.getCodedParm("$XDSDocumentEntryFormatCode");

		OMElement doc_metadata = eb.getSsDocs(ss_uuid, format_codes, conf_codes);
		metadata.addMetadata(doc_metadata);

		OMElement fol_metadata = eb.getSsFolders(ss_uuid);
		metadata.addMetadata(fol_metadata);

		List<String> ssUuids = new ArrayList<String>();
		ssUuids.add(ss_uuid);
		OMElement assoc1_metadata = eb.getRegistryPackageAssocs(ssUuids);
		if (assoc1_metadata != null)
			metadata.addMetadata(assoc1_metadata);

		List<String> folder_ids = metadata.getFolderIds();
		OMElement assoc2_metadata = eb.getRegistryPackageAssocs(folder_ids);
		if (assoc2_metadata != null)
			metadata.addMetadata(assoc2_metadata);

		//ArrayList<String> ss_and_folder_ids = new ArrayList<String>(folder_ids);
		//ss_and_folder_ids.add(ss_uuid);

		metadata.rmDuplicates();

		// some document may have been filtered out, remove the unnecessary Associations
		List<String> content_ids = new ArrayList<String>();
		content_ids.addAll(metadata.getSubmissionSetIds());
		content_ids.addAll(metadata.getExtrinsicObjectIds());
		content_ids.addAll(metadata.getFolderIds());

		// add in Associations that link the above parts
		content_ids.addAll(metadata.getIds(metadata.getAssociationsInclusive(content_ids)));

		// Assocs can link to Assocs to so repeat
		content_ids.addAll(metadata.getIds(metadata.getAssociationsInclusive(content_ids)));

		metadata.filter(content_ids);

		return metadata;
	}

}
