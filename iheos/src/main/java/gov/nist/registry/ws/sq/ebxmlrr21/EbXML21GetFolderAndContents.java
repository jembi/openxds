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
import gov.nist.registry.ws.sq.GetFolderAndContents;

import java.util.ArrayList;
import java.util.List;

import org.apache.axiom.om.OMElement;
import org.openhealthtools.openxds.log.LoggerException;

/**
 * Implementation specific class for GetFolderAndContents stored query. 
 * All the logic is in the runImplementation() method.
 * @author bill
 *
 */
public class EbXML21GetFolderAndContents extends GetFolderAndContents {

	EbXML21QuerySupport eb;

	/**
	 * Constructor
	 * @param sqs
	 * @throws MetadataValidationException
	 */
	public EbXML21GetFolderAndContents(StoredQuerySupport sqs) {
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
		if (fol_uuid != null) {
			// starting from uuid
			OMElement x = eb.getFolByUuid(fol_uuid);
			metadata = MetadataParser.parseNonSubmission(x);
			if (sqs.return_leaf_class) {
				if (metadata.getFolders().size() == 0) return metadata;
			} else {
				if (metadata.getObjectRefs().size() == 0) return metadata;
			}
		} else {
			// starting from uniqueid
			OMElement x = eb.getFolByUid(fol_uid);
			metadata = MetadataParser.parseNonSubmission(x);
			if (sqs.return_leaf_class) {
				if (metadata.getFolders().size() == 0) return metadata;
			} else {
				if (metadata.getObjectRefs().size() == 0) return metadata;
			}

			fol_uuid = metadata.getFolder(0).getAttributeValue(MetadataSupport.id_qname);
		}
		
		sqs.log_message.addOtherParam("Folder id", fol_uuid);

		List<String> folder_ids = new ArrayList<String>();
		folder_ids.add(fol_uuid);

		// fol_uuid has now been set

		List<String> content_ids = new ArrayList<String>();
		SQCodedTerm conf_codes = sqs.params.getCodedParm("$XDSDocumentEntryConfidentialityCode");
		SQCodedTerm format_codes = sqs.params.getCodedParm("$XDSDocumentEntryFormatCode");

		OMElement doc_metadata = eb.getFolDocs(fol_uuid, format_codes, conf_codes);
		metadata.addMetadata(doc_metadata);
		List<String> docIds = eb.getIdsFromAdhocQueryResponse(doc_metadata); 
		content_ids.addAll(docIds);

		sqs.log_message.addOtherParam("Doc ids", docIds.toString());

		

		List<String> assocIds;

		if (content_ids.size() > 0 && folder_ids.size() > 0) {
			OMElement assoc_metadata = eb.getRegistryPackageAssocs(folder_ids, content_ids);
			assocIds = eb.getIdsFromAdhocQueryResponse(assoc_metadata);
			sqs.log_message.addOtherParam("Assoc ids", assocIds.toString());
			metadata.addMetadata(assoc_metadata);
		}

		sqs.log_message.addOtherParam("Assoc ids", "None");

		return metadata;
	}

}
