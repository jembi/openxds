package gov.nist.registry.ws.configurations.ebxmlrr21;

import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XDSRegistryOutOfResourcesException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.storedquery.SqParams;
import gov.nist.registry.common2.registry.storedquery.StoredQuerySupport;
import gov.nist.registry.ws.sq.StoredQueryFactory;
import gov.nist.registry.ws.sq.ebxmlrr21.EbXML21FindDocuments;
import gov.nist.registry.ws.sq.ebxmlrr21.EbXML21FindDocumentsForMultiplePatients;
import gov.nist.registry.ws.sq.ebxmlrr21.EbXML21FindFolders;
import gov.nist.registry.ws.sq.ebxmlrr21.EbXML21FindFoldersForMultiplePatients;
import gov.nist.registry.ws.sq.ebxmlrr21.EbXML21FindSubmissionSets;
import gov.nist.registry.ws.sq.ebxmlrr21.EbXML21GetAssociations;
import gov.nist.registry.ws.sq.ebxmlrr21.EbXML21GetDocuments;
import gov.nist.registry.ws.sq.ebxmlrr21.EbXML21GetDocumentsAndAssociations;
import gov.nist.registry.ws.sq.ebxmlrr21.EbXML21GetFolderAndContents;
import gov.nist.registry.ws.sq.ebxmlrr21.EbXML21GetFolders;
import gov.nist.registry.ws.sq.ebxmlrr21.EbXML21GetFoldersForDocument;
import gov.nist.registry.ws.sq.ebxmlrr21.EbXML21GetRelatedDocuments;
import gov.nist.registry.ws.sq.ebxmlrr21.EbXML21GetSubmissionSetAndContents;
import gov.nist.registry.ws.sq.ebxmlrr21.EbXML21GetSubmissionSets;

import org.apache.axiom.om.OMElement;
import org.openhealthtools.openxds.log.LogMessage;
import org.openhealthtools.openxds.log.LoggerException;

/**
 * Factory class for Stored Queries going to the ebxmlrr2.1-final1 registry implementation. 
 * This specific factory is coded/selected in the class gov.nist.registry.ws.config.Registry. The
 * key method is buildStoredQueryHandler which returns an object of generic type StoredQuery.  This
 * object implements a single stored query type implemented against a specific registry implementation.
 * @author bill
 *
 */
public class Ebxmlrr21StoredQueryFactory extends StoredQueryFactory {

	/**
	 * 
	 * @param ahqr
	 * @throws XdsException
	 * @throws LoggerException
	 */
	public Ebxmlrr21StoredQueryFactory(OMElement ahqr) throws XdsException,
	LoggerException {
		super(ahqr);
	}

	/**
	 * 
	 * @param ahqr
	 * @param log_message
	 * @throws XdsException
	 * @throws LoggerException
	 */
	public Ebxmlrr21StoredQueryFactory(OMElement ahqr, Response response, LogMessage log_message) throws XdsException,
	LoggerException {
		super(ahqr, response, log_message);
	}

	/**
	 * 
	 * @param params 
	 * @throws XdsException
	 * @throws LoggerException
	 */
	public Ebxmlrr21StoredQueryFactory(SqParams params) throws XdsException,
	LoggerException {
		super(params);
	}

	/**
	 * 
	 * @param ahqr
	 * @param log_message
	 * @throws XdsException
	 * @throws LoggerException
	 */
	public Ebxmlrr21StoredQueryFactory(SqParams params, Response response, LogMessage log_message) throws XdsException,
	LoggerException {
		super(params, response, log_message);
	}

	/**
	 * Returns an object of generic type StoredQuery which implements a single stored query 
	 * type implemented against the ebxmlrr21-final-1 registry implementation.
	 * @throws LoggerException 
	 */
	public StoredQueryFactory buildStoredQueryHandler(StoredQuerySupport sqs)
	throws MetadataValidationException, LoggerException {

		if (query_id == null) {
			throw new MetadataValidationException("Null Query ID");
		}
		
		if (query_id.equals(MetadataSupport.SQ_FindDocuments)) {
			setTestMessage("FindDocuments");
			storedQueryImpl = new EbXML21FindDocuments(sqs);
		} 
		else if (query_id.equals(MetadataSupport.SQ_FindSubmissionSets)) {
			setTestMessage("FindSubmissionSets");
			storedQueryImpl = new EbXML21FindSubmissionSets(sqs);
		}
		else if (query_id.equals(MetadataSupport.SQ_FindFolders)) {
			setTestMessage("FindFolders");
			storedQueryImpl = new EbXML21FindFolders(sqs);
		}
		else if (query_id.equals(MetadataSupport.SQ_GetAll)) {
			setTestMessage("GetAll");
			response.add_error("XDSRegistryError", "UnImplemented Stored Query query id = " + query_id, "AdhocQueryRequest.java", log_message);
		}
		else if (query_id.equals(MetadataSupport.SQ_GetDocuments)) {
			setTestMessage("GetDocuments");
			storedQueryImpl = new EbXML21GetDocuments(sqs);
		}
		else if (query_id.equals(MetadataSupport.SQ_GetFolders)) {
			setTestMessage("GetFolders");
			storedQueryImpl = new EbXML21GetFolders(sqs);
		}
		else if (query_id.equals(MetadataSupport.SQ_GetAssociations)) {
			setTestMessage("GetAssociations");
			storedQueryImpl = new EbXML21GetAssociations(sqs);
		}
		else if (query_id.equals(MetadataSupport.SQ_GetDocumentsAndAssociations)) {
			setTestMessage("GetDocumentsAndAssociations");
			storedQueryImpl = new EbXML21GetDocumentsAndAssociations(sqs);
		}
		else if (query_id.equals(MetadataSupport.SQ_GetSubmissionSets)) {
			setTestMessage("GetSubmissionSets");
			storedQueryImpl = new EbXML21GetSubmissionSets(sqs);
		}
		else if (query_id.equals(MetadataSupport.SQ_GetSubmissionSetAndContents)) {
			setTestMessage("GetSubmissionSetAndContents");
			storedQueryImpl = new EbXML21GetSubmissionSetAndContents(sqs);
		}
		else if (query_id.equals(MetadataSupport.SQ_GetFolderAndContents)) {
			setTestMessage("GetFolderAndContents");
			storedQueryImpl = new EbXML21GetFolderAndContents(sqs);
		}
		else if (query_id.equals(MetadataSupport.SQ_GetFoldersForDocument)) {
			setTestMessage("GetFoldersForDocument");
			storedQueryImpl = new EbXML21GetFoldersForDocument(sqs);
		}
		else if (query_id.equals(MetadataSupport.SQ_GetRelatedDocuments)) {
			setTestMessage("GetRelatedDocuments");
			storedQueryImpl = new EbXML21GetRelatedDocuments(sqs);
		}
		else if (query_id.equals(MetadataSupport.SQ_FindDocumentsForMultiplePatients)) {
			setTestMessage("FindDocumentsForMulitplePatients");
			storedQueryImpl = new EbXML21FindDocumentsForMultiplePatients(sqs);
		}
		else if (query_id.equals(MetadataSupport.SQ_FindFoldersForMultiplePatients)) {
			setTestMessage("FindFoldersForMulitplePatients");
			storedQueryImpl = new EbXML21FindFoldersForMultiplePatients(sqs);
		}
		else {
			setTestMessage(query_id);
			response.add_error("XDSRegistryError", "Unknown Stored Query query id = " + query_id, "AdhocQueryRequest.java", log_message);
		}

		if (log_message != null) {
			if (storedQueryImpl == null)
				log_message.addOtherParam("storedQueryImpl  not defined for query id = ", query_id);
			else
				log_message.addOtherParam("storedQueryImpl", storedQueryImpl.getClass().getName());
		}

		return this;
	}

	/**
	 * Stored Query API call.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 * @throws XDSRegistryOutOfResourcesException 
	 */
	public Metadata FindDocuments(StoredQuerySupport sqs) throws XdsException, LoggerException, XDSRegistryOutOfResourcesException {
		return new EbXML21FindDocuments(sqs).runSpecific();
	}

	/**
	 * Stored Query API call.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	public Metadata FindFolders(StoredQuerySupport sqs) throws XdsException, LoggerException {
		return new EbXML21FindFolders(sqs).runSpecific();
	}

	/**
	 * Stored Query API call.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	public Metadata FindSubmissionSets(StoredQuerySupport sqs) throws XdsException, LoggerException {
		return new EbXML21FindSubmissionSets(sqs).runSpecific();
	}

	/**
	 * Stored Query API call.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	public Metadata GetAssociations(StoredQuerySupport sqs) throws XdsException, LoggerException {
		return new EbXML21GetAssociations(sqs).runSpecific();
	}

	/**
	 * Stored Query API call.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	public Metadata GetDocuments(StoredQuerySupport sqs) throws XdsException, LoggerException {
		return new EbXML21GetDocuments(sqs).runSpecific();
	}

	/**
	 * Stored Query API call.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	public Metadata GetDocumentsAndAssociations(StoredQuerySupport sqs) throws XdsException, LoggerException {
		return new EbXML21GetDocumentsAndAssociations(sqs).runSpecific();
	}

	/**
	 * Stored Query API call.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	public Metadata GetFolderAndContents(StoredQuerySupport sqs) throws XdsException, LoggerException {
		return new EbXML21GetFolderAndContents(sqs).runSpecific();
	}

	/**
	 * Stored Query API call.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	public Metadata GetFolders(StoredQuerySupport sqs) throws XdsException, LoggerException {
		return new EbXML21GetFolders(sqs).runSpecific();
	}

	/**
	 * Stored Query API call.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	public Metadata GetFoldersForDocument(StoredQuerySupport sqs) throws XdsException, LoggerException {
		return new EbXML21GetFoldersForDocument(sqs).runSpecific();
	}

	/**
	 * Stored Query API call.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	public Metadata GetRelatedDocuments(StoredQuerySupport sqs) throws XdsException, LoggerException {
		return new EbXML21GetRelatedDocuments(sqs).runSpecific();
	}

	/**
	 * Stored Query API call.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	public Metadata GetSubmissionSetAndContents(StoredQuerySupport sqs) throws XdsException, LoggerException {
		return new EbXML21GetSubmissionSetAndContents(sqs).runSpecific();
	}

	/**
	 * Stored Query API call.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	public Metadata GetSubmissionSets(StoredQuerySupport sqs) throws XdsException, LoggerException {
		return new EbXML21GetSubmissionSets(sqs).runSpecific();
	}

}
