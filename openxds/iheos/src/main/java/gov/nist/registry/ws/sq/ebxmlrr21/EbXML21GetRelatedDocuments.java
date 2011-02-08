package gov.nist.registry.ws.sq.ebxmlrr21;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataParser;
import gov.nist.registry.common2.registry.storedquery.StoredQuerySupport;
import gov.nist.registry.ws.sq.GetRelatedDocuments;

import java.util.ArrayList;
import java.util.List;

import org.apache.axiom.om.OMElement;
import org.openhealthtools.openexchange.syslog.LoggerException;

/**
 * Implementation specific class for GetRelatedDocuments stored query. 
 * All the logic is in the runImplementation() method.
 * @author bill
 *
 */
public class EbXML21GetRelatedDocuments extends GetRelatedDocuments {

	EbXML21QuerySupport eb;

	/**
	 * Constructor
	 * @param sqs
	 * @throws MetadataValidationException
	 */
	public EbXML21GetRelatedDocuments(StoredQuerySupport sqs) {
		super(sqs);
		eb = new EbXML21QuerySupport(sqs);
	}

	/**
	 * Main method, runs query logic
	 * @return Metadata
	 * @throws MetadataException
	 * @throws XdsException
	 */
	public Metadata runImplementation() throws XdsInternalException,
	XdsException, LoggerException, MetadataException,
	MetadataValidationException {
		Metadata result_metadata = new Metadata();
		// this object will be build carefully containing no extra ObjectRefs. At the
		// end, its contents will be converted to LeafClass or ObjectRef depending on
		// the requested return type.
		
		// the queries that this code depends on are controlled by the StoredQuerySupport
		// object.  It specifies whether LeafClass or ObjectRef return is 
		// requested.  So, when a specific low level query is issued, it could
		// return ObjectRefs or LeafClasses.  For some objects, like Associations,
		// we must get LeafClass so to get access to the attributes. The
		// last operation is to convert everything over to LeafClass or ObjectRef
		// objects to conform to the requested return type.

		
//		// are these needed?
//		List<String> doc_ids_to_query_for = new ArrayList<String>();
//		List<String> doc_ids = new ArrayList<String>();

		if (assoc_types == null || assoc_types.size() == 0) {
			throw new XdsInternalException("No $AssociationTypes specified in query");
		}

		// filter HasMember out of assoc_types if it exists
		// because we return only related documents and assocs
		// and HasMember never exists between documents
		String excludeAssocType = "HasMember";
		assoc_types = removeAssocType(assoc_types, excludeAssocType);

		// if uuid supplied, save it in originalDocId
		// if uid supplied, query to get it and save its id in originalDocId
		String originalDocId;
		if (uid != null) {
			OMElement ele = eb.getDocByUid(uid);
			Metadata orig_doc_metadata = MetadataParser.parseNonSubmission(ele);
			if (orig_doc_metadata.getExtrinsicObjects().size() > 0) {
				result_metadata.addExtrinsicObjects(orig_doc_metadata.getExtrinsicObjects());
				originalDocId = orig_doc_metadata.getExtrinsicObjectIds().get(0);
			}
			else if (orig_doc_metadata.getObjectRefs().size() > 0) {
				result_metadata.addObjectRefs(orig_doc_metadata.getObjectRefs());
				originalDocId = orig_doc_metadata.getObjectRefIds().get(0);
			}
			else
				return result_metadata;   // original document not found - return empty
		} else {
			originalDocId = uuid;
		}
		if (sqs.log_message != null){
			sqs.log_message.addOtherParam("originalDocId", originalDocId);
			sqs.log_message.addOtherParam("structure", result_metadata.structure());
		}
		// at this point result_metadata contains either a single ObjectRef or a single
		// ExtrinsicObject representing the target document.  originalDocId has its
		// id


		// load all associations related to combined_ids
		List<String> targetIds = new ArrayList<String>();
		targetIds.add(originalDocId);
		
		sqs.forceLeafClassQueryType();  // fetch LeafClass for Associations
		OMElement associations = eb.getAssociations(targetIds, assoc_types);
		sqs.restoreOriginalQueryType();
		
		Metadata association_metadata = MetadataParser.parseNonSubmission(associations);

		// no associations => return nothing
		if (association_metadata.getAssociations().size() == 0) {
			return new Metadata();
		}
		
		// add associations to final result
		result_metadata.addToMetadata(association_metadata.getLeafClassObjects(), true);
		if (sqs.log_message != null)
		sqs.log_message.addOtherParam("with Associations", result_metadata.structure());

		// discover ids (potentially for documents) that are referenced by the associations
		List<String> assocReferences = association_metadata.getAssocReferences();

		result_metadata.addObjectRefs(assocReferences);
		
		if (sqs.return_leaf_class) {
			return eb.convertToLeafClass(result_metadata);
		}
		else
			return eb.convertToObjectRefs(result_metadata, false);
	}

	private List<String> removeAssocType(List<String> assoc_types, String excludeAssocType) {
		Metadata meta = new Metadata();
		List<String> assoc_types2 = new ArrayList<String>();
		for (String type : assoc_types) {
			String v2_type = meta.v2AssocType(type);
			if ( !v2_type.equals(excludeAssocType))
				assoc_types2.add(type);
		}
		return assoc_types2;
	}


}
