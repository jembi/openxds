package gov.nist.registry.ws.sq;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XDSRegistryOutOfResourcesException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.AdhocQueryResponse;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.storedquery.ParamParser;
import gov.nist.registry.common2.registry.storedquery.SqParams;
import gov.nist.registry.common2.registry.storedquery.StoredQuerySupport;

import org.apache.axiom.om.OMElement;
import org.openhealthtools.openxds.log.LogMessage;
import org.openhealthtools.openxds.log.LoggerException;

/**
 * Generic Stored Query Factory class that is sub-classed to define a specific stored query implementation.
 * The generic/specific nature relates to the underlying implementation.  The key method, 
 * buildStoredQueryHandler(), which is to be defined in the sub-class, decides which stored queries
 * are implemented and what the implementation classes are. This class provides the generic stored
 * query parsing and support.
 * @author bill
 *
 */
abstract public class StoredQueryFactory {

	/**
	 * Returns an object of generic type StoredQuery which implements a single stored query 
	 * type implemented against a specific registry implementation. The sub-class that implements 
	 * this method is specific to an implementation.
	 * @param sqs
	 * @throws MetadataValidationException
	 * @throws LoggerException 
	 */
	abstract public StoredQueryFactory buildStoredQueryHandler(StoredQuerySupport sqs) throws MetadataValidationException, LoggerException;

	OMElement ahqr;
	boolean return_objects = false;
	SqParams params;
	protected String query_id;
	protected LogMessage log_message = null;
	protected StoredQuery storedQueryImpl;
	String service_name;
	boolean is_secure = false;
	protected Response response = null;

	public void setIsSecure(boolean is) { is_secure = is; }
	public void setServiceName(String serviceName) { serviceName = service_name; }
	public void setQueryId(String qid) { query_id = qid; }
	
	public String getQueryId() {
		return query_id; 
	}
	
	public boolean isLeafClassReturnType() {
		OMElement response_option = MetadataSupport.firstChildWithLocalName(ahqr, "ResponseOption");
		if (response_option == null) return true;
		String return_type = response_option.getAttributeValue(MetadataSupport.return_type_qname);
		if (return_type == null || return_type.equals("") || !return_type.equals("LeafClass")) return false;
		return true;
	}

	public StoredQueryFactory(OMElement ahqr) throws XdsException, LoggerException {
		this.ahqr = ahqr;
		this.params = null;
		this.log_message = null;

		build();
	}

	public StoredQueryFactory(OMElement ahqr, Response response, LogMessage log_message) throws XdsInternalException, MetadataException, XdsException, LoggerException {
		this.ahqr = ahqr;
		this.log_message = log_message;
		this.response = response;
		this.params = null;

		build();
	}

	public StoredQueryFactory(SqParams params, Response response, LogMessage log_message)  throws XdsInternalException, MetadataException, XdsException, LoggerException {
		this.params = params;
		this.response = response;
		ahqr = null;
		this.log_message = log_message;
	}

	public StoredQueryFactory(SqParams params)  throws XdsInternalException, MetadataException, XdsException, LoggerException {
		this.params = params;
		ahqr = null;
		this.log_message = null;
	}


	void build() throws XdsException, LoggerException {

		OMElement response_option = MetadataSupport.firstChildWithLocalName(ahqr, "ResponseOption") ;
		if (response_option == null) {
			throw new XdsInternalException("Cannot find /AdhocQueryRequest/ResponseOption element");
		}

		String return_type = response_option.getAttributeValue(MetadataSupport.return_type_qname);

		if (return_type == null) throw new XdsException("Attribute returnType not found on query request");
		if (return_type.equals("LeafClass"))
			return_objects = true;
		else if (return_type.equals("ObjectRef"))
			return_objects = false;
		else
			throw new MetadataException("/AdhocQueryRequest/ResponseOption/@returnType must be LeafClass or ObjectRef. Found value "
					+ return_type);

		OMElement adhoc_query = MetadataSupport.firstChildWithLocalName(ahqr, "AdhocQuery") ;
		if (adhoc_query == null) {
			throw new XdsInternalException("Cannot find /AdhocQueryRequest/AdhocQuery element");
		}

		ParamParser parser = new ParamParser();
		params = parser.parse(ahqr);

		if (log_message != null)
			log_message.addOtherParam("Parameters", params.toString());

		if (response == null) {
			log_message.addOtherParam("XXXX Allocating new Response!!!!", "");
			response = new AdhocQueryResponse(Response.version_3);
		}

		query_id = adhoc_query.getAttributeValue(MetadataSupport.id_qname).trim();

		StoredQuerySupport sqs = new StoredQuerySupport(params, return_objects, response, log_message, is_secure);

		buildStoredQueryHandler(sqs); // this goes to a sub-class that knows about a specific implementation



	}
	protected void setTestMessage(String sqName) {
		if (log_message == null)
			return;

		if (service_name == null)
			log_message.setTestMessage(sqName);
		else
			log_message.setTestMessage(service_name + " " + sqName);

	}
	
	public StoredQuery getImpl() {
		return storedQueryImpl;
	}

	public void setLogMessage(LogMessage log_message) { this.log_message = log_message; }

	public Metadata run() throws XDSRegistryOutOfResourcesException, XdsException, LoggerException {
		if (storedQueryImpl == null)
			throw new XdsInternalException("storedQueryImpl is null");
		return storedQueryImpl.run();
	}

	/**
	 * Stored Query API call. Relies on implementation specific sub-class to implement.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 * @throws XDSRegistryOutOfResourcesException 
	 */
	abstract public Metadata FindDocuments(StoredQuerySupport sqs) throws XdsException, LoggerException, XDSRegistryOutOfResourcesException;
	/**
	 * Stored Query API call. Relies on implementation specific sub-class to implement.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	abstract public Metadata FindFolders(StoredQuerySupport sqs) throws XdsException, LoggerException;
	/**
	 * Stored Query API call. Relies on implementation specific sub-class to implement.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	abstract public Metadata FindSubmissionSets(StoredQuerySupport sqs) throws XdsException, LoggerException;
	/**
	 * Stored Query API call. Relies on implementation specific sub-class to implement.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	abstract public Metadata GetAssociations(StoredQuerySupport sqs) throws XdsException, LoggerException;
	/**
	 * Stored Query API call. Relies on implementation specific sub-class to implement.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	abstract public Metadata GetDocuments(StoredQuerySupport sqs) throws XdsException, LoggerException;
	/**
	 * Stored Query API call. Relies on implementation specific sub-class to implement.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	abstract public Metadata GetDocumentsAndAssociations(StoredQuerySupport sqs) throws XdsException, LoggerException;
	/**
	 * Stored Query API call. Relies on implementation specific sub-class to implement.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	abstract public Metadata GetFolderAndContents(StoredQuerySupport sqs) throws XdsException, LoggerException;
	/**
	 * Stored Query API call. Relies on implementation specific sub-class to implement.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	abstract public Metadata GetFolders(StoredQuerySupport sqs) throws XdsException, LoggerException;
	/**
	 * Stored Query API call. Relies on implementation specific sub-class to implement.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	abstract public Metadata GetFoldersForDocument(StoredQuerySupport sqs) throws XdsException, LoggerException;
	/**
	 * Stored Query API call. Relies on implementation specific sub-class to implement.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	abstract public Metadata GetRelatedDocuments(StoredQuerySupport sqs) throws XdsException, LoggerException;
	/**
	 * Stored Query API call. Relies on implementation specific sub-class to implement.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	abstract public Metadata GetSubmissionSetAndContents(StoredQuerySupport sqs) throws XdsException, LoggerException;
	/**
	 * Stored Query API call. Relies on implementation specific sub-class to implement.
	 * @param sqs
	 * @return Metadata object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	abstract public Metadata GetSubmissionSets(StoredQuerySupport sqs) throws XdsException, LoggerException;
}
