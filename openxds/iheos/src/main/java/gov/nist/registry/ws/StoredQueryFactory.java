package gov.nist.registry.ws;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.XDSRegistryOutOfResourcesException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.AdhocQueryResponse;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.StoredQuery;
import gov.nist.registry.ws.sq.FindDocuments;
import gov.nist.registry.ws.sq.FindFolders;
import gov.nist.registry.ws.sq.FindSubmissionSets;
import gov.nist.registry.ws.sq.GetAssociations;
import gov.nist.registry.ws.sq.GetDocuments;
import gov.nist.registry.ws.sq.GetDocumentsAndAssociations;
import gov.nist.registry.ws.sq.GetFolderAndContents;
import gov.nist.registry.ws.sq.GetFolders;
import gov.nist.registry.ws.sq.GetFoldersForDocument;
import gov.nist.registry.ws.sq.GetRelatedDocuments;
import gov.nist.registry.ws.sq.GetSubmissionSetAndContents;
import gov.nist.registry.ws.sq.GetSubmissionSets;
import gov.nist.registry.ws.sq.ParamParser;
import gov.nist.registry.xdslog.LoggerException;
import gov.nist.registry.xdslog.Message;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.axiom.om.OMElement;

public class StoredQueryFactory {
	OMElement ahqr;
	boolean return_objects = false;
	HashMap<String, Object> params;
	String query_id;
	Message log_message = null;
	StoredQuery x;
	String service_name;
	boolean is_secure = false;
	Response response = null;

	public void setResponse(Response response) { this.response = response; }
	public void setIsSecure(boolean is) { is_secure = is; }
	public void setServiceName(String serviceName) { serviceName = service_name; }

	public boolean isLeafClassReturnType() {
		OMElement response_option = MetadataSupport.firstChildWithLocalName(ahqr, "ResponseOption");
		if (response_option == null) return true;
		String return_type = response_option.getAttributeValue(MetadataSupport.return_type_qname);
		if (return_type == null || return_type.equals("") || !return_type.equals("LeafClass")) return false;
		return true;
	}

	public boolean hasParm(String parmName) { return params.containsKey(parmName); }
	public Object getParm(String parmName) { return params.get(parmName); }

	public StoredQueryFactory(OMElement ahqr) throws XdsInternalException, MetadataException, XdsException, LoggerException {
		this.ahqr = ahqr;

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

		if (response == null)
			response = new AdhocQueryResponse(Response.version_3);

		query_id = adhoc_query.getAttributeValue(MetadataSupport.id_qname);

		if (query_id.equals(MetadataSupport.SQ_FindDocuments)) {
			// FindDocuments
			if (log_message != null)
				log_message.setTestMessage(service_name + " " + "FindDocuments");
			x = new FindDocuments(params, return_objects, response, log_message, is_secure);
		} 
		else if (query_id.equals(MetadataSupport.SQ_FindSubmissionSets)) {
			if (log_message != null)
				log_message.setTestMessage(service_name + " " + "FindSubmissionSets");
			// FindSubmissionSets
			x = new FindSubmissionSets(params, return_objects, response, log_message, is_secure);
		}
		else if (query_id.equals(MetadataSupport.SQ_FindFolders)) {
			// FindFolders
			if (log_message != null)
				log_message.setTestMessage(service_name + " " + "FindFolders");
			x = new FindFolders(params, return_objects, response, log_message, is_secure);
		}
		else if (query_id.equals(MetadataSupport.SQ_GetAll)) {
			// GetAll
			if (log_message != null)
				log_message.setTestMessage(service_name + " " + "GetAll");
			response.add_error("XDSRegistryError", "UnImplemented Stored Query query id = " + query_id, "AdhocQueryRequest.java", log_message);
		}
		else if (query_id.equals(MetadataSupport.SQ_GetDocuments)) {
			// GetDocuments
			if (log_message != null)
				log_message.setTestMessage(service_name + " " + "GetDocuments");
			x = new GetDocuments(params, return_objects, response, log_message, is_secure);
		}
		else if (query_id.equals(MetadataSupport.SQ_GetFolders)) {
			// GetFolders
			if (log_message != null)
				log_message.setTestMessage(service_name + " " + "GetFolders");
			x = new GetFolders(params, return_objects, response, log_message, is_secure);
		}
		else if (query_id.equals(MetadataSupport.SQ_GetAssociations)) {
			// GetAssociations
			if (log_message != null)
				log_message.setTestMessage(service_name + " " + "GetAssociations");
			x = new GetAssociations(params, return_objects, response, log_message, is_secure);
		}
		else if (query_id.equals(MetadataSupport.SQ_GetDocumentsAndAssociations)) {
			// GetDocumentsAndAssociations
			if (log_message != null)
				log_message.setTestMessage("GetDocumentsAndAssociations");
			x = new GetDocumentsAndAssociations(params, return_objects, response, log_message, is_secure);
		}
		else if (query_id.equals(MetadataSupport.SQ_GetSubmissionSets)) {
			// GetSubmissionSets
			if (log_message != null)
				log_message.setTestMessage(service_name + " " + "GetSubmissionSets");
			x = new GetSubmissionSets(params, return_objects, response, log_message, is_secure);
		}
		else if (query_id.equals(MetadataSupport.SQ_GetSubmissionSetAndContents)) {
			// GetSubmissionSetAndContents
			if (log_message != null)
				log_message.setTestMessage(service_name + " " + "GetSubmissionSetAndContents");
			x = new GetSubmissionSetAndContents(params, return_objects, response, log_message, is_secure);
		}
		else if (query_id.equals(MetadataSupport.SQ_GetFolderAndContents)) {
			// GetFolderAndContents
			if (log_message != null)
				log_message.setTestMessage(service_name + " " + "GetFolderAndContents");
			x = new GetFolderAndContents(params, return_objects, response, log_message, is_secure);
		}
		else if (query_id.equals(MetadataSupport.SQ_GetFoldersForDocument)) {
			// GetFoldersForDocument
			if (log_message != null)
				log_message.setTestMessage(service_name + " " + "GetFoldersForDocument");
			x = new GetFoldersForDocument(params, return_objects, response, log_message, is_secure);
		}
		else if (query_id.equals(MetadataSupport.SQ_GetRelatedDocuments)) {
			// GetRelatedDocuments
			if (log_message != null)
				log_message.setTestMessage(service_name + " " + "GetRelatedDocuments");
			x = new GetRelatedDocuments(params, return_objects, response, log_message, is_secure);
		}
		else {
			if (log_message != null)
				log_message.setTestMessage(service_name + " " + query_id);
			response.add_error("XDSRegistryError", "Unknown Stored Query query id = " + query_id, "AdhocQueryRequest.java", log_message);
		}



	}

	public void setLogMessage(Message log_message) { this.log_message = log_message; }

	public ArrayList<OMElement> run() throws XDSRegistryOutOfResourcesException, XdsException, LoggerException {
		return x.run();
	}

}
