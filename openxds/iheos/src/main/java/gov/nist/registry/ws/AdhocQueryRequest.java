package gov.nist.registry.ws;

import gov.nist.registry.common2.MetadataTypes;
import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.SchemaValidationException;
import gov.nist.registry.common2.exception.XDSRegistryOutOfResourcesException;
import gov.nist.registry.common2.exception.XMLParserException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsFormatException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.exception.XdsResultNotSinglePatientException;
import gov.nist.registry.common2.exception.XdsValidationException;
import gov.nist.registry.common2.registry.AdhocQueryResponse;
import gov.nist.registry.common2.registry.BackendRegistry;
import gov.nist.registry.common2.registry.BasicQuery;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataParser;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.RegistryUtility;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.XdsCommon;
import gov.nist.registry.common2.registry.storedquery.ParamParser;
import gov.nist.registry.common2.registry.storedquery.SqParams;
import gov.nist.registry.ws.config.Registry;
import gov.nist.registry.ws.sq.StoredQuery;
import gov.nist.registry.ws.sq.StoredQueryFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.audit.ActiveParticipant;
import org.openhealthtools.openexchange.audit.IheAuditTrail;
import org.openhealthtools.openexchange.audit.ParticipantObject;
import org.openhealthtools.openexchange.audit.TypeValuePair;
import org.openhealthtools.openexchange.config.PropertyFacade;
import org.openhealthtools.openexchange.syslog.LogMessage;
import org.openhealthtools.openexchange.syslog.LoggerException;
import org.openhealthtools.openxds.common.XdsFactory;
import org.openhealthtools.openxds.registry.api.XdsRegistry;
import org.openhealthtools.openxua.api.XuaException;

public class AdhocQueryRequest extends XdsCommon {
	String service_name = "";
	boolean is_secure;
	private XdsRegistry actor = null;
	/* The IHE Audit Trail for this actor. */
	private IheAuditTrail auditLog = null;
	private final static Log logger = LogFactory.getLog(AdhocQueryRequest.class);
	

	public AdhocQueryRequest(LogMessage log_message, MessageContext messageContext, boolean is_secure, short xds_version) {
		this.log_message = log_message;
		this.messageContext = messageContext;
		this.is_secure = is_secure;
		this.xds_version = xds_version;
		
		try {
			actor = XdsFactory.getRegistryActor(); 
			if (actor == null) {
				throw new XdsInternalException("Cannot find XdsRegistry actor configuration.");			
			}
			
			auditLog = (IheAuditTrail)actor.getAuditTrail();	
		} catch (XdsInternalException e) {
            logger.fatal(logger_exception_details(e));
		} 
	}
	
	public void setServiceName(String service_name) {
		this.service_name = service_name;
	}
	
	public OMElement adhocQueryRequest(OMElement ahqr) {
		if (logger.isDebugEnabled()) {
			logger.debug("Request from the XDS Consumer:");
			logger.debug(ahqr.toString());
		}

		ahqr.build();

		OMNamespace ns = ahqr.getNamespace();
		String ns_uri = ns.getNamespaceURI();

		transaction_type = SQ_transaction;
		
		try {
			if (ns_uri.equals(MetadataSupport.ebQns3.getNamespaceURI())) {
				init(new AdhocQueryResponse(Response.version_3), xds_version, messageContext);
			} else if (ns_uri.equals(MetadataSupport.ebQns2.getNamespaceURI())) {
				init(new AdhocQueryResponse(Response.version_2), xds_version, messageContext);
			} else {
				init(new AdhocQueryResponse(Response.version_3), xds_version, messageContext);
				response.add_error("XDSRegistryError", "Invalid XML namespace on AdhocQueryRequest: " + ns_uri, "AdhocQueryRequest.java", log_message);
				return response.getResponse();
			}
		} catch (XdsInternalException e) {
			logger.fatal("Internal Error initializing AdhocQueryRequest transaction: " + e.getMessage());
			return null;
		}

		// Call X-Service Provider Actor to validate X-User Assertion with X-Assertion Provider
		try {
			boolean validateUserAssertion = PropertyFacade.getBoolean("validate.userassertion");
			if(validateUserAssertion){
				SoapHeader header = new SoapHeader(messageContext);
				boolean status = validateAssertion(header);
				if (!status)
					throw new XdsException("Invalid Identity Assertion");
			}
			AdhocQueryRequestInternal(ahqr);
		}
		catch (XdsResultNotSinglePatientException e) {
			response.add_error("XDSResultNotSinglePatient", e.getMessage(), RegistryUtility.exception_trace(e), log_message);
		} 
		catch (XdsValidationException e) {
			response.add_error("XDSRegistryError", "Validation Error: " + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
		} 
		catch (XdsFormatException e) {
			response.add_error("XDSRegistryError", "SOAP Format Error: " + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
		} 
		catch (XDSRegistryOutOfResourcesException e) {
			// query return limitation
			response.add_error("XDSRegistryOutOfResources", e.getMessage(), RegistryUtility.exception_trace(e), log_message);
		} 
		catch (SchemaValidationException e) {
			response.add_error("XDSRegistryMetadataError", "SchemaValidationException: " + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
		} 
		catch (XdsInternalException e) {
			response.add_error("XDSRegistryError", "Internal Error: " + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
			logger.fatal(logger_exception_details(e));
		}
		catch (MetadataValidationException e) {
			response.add_error("XDSRegistryError", "Metadata Error: " + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
		}  
		catch (SqlRepairException e) {
			response.add_error("XDSRegistryError", "Could not decode SQL: " + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
			logger.warn(logger_exception_details(e));
		}  
		catch (MetadataException e) {
			response.add_error("XDSRegistryError", "Metadata error: " + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
		}  
		catch (LoggerException e) {
			response.add_error("XDSRegistryError", "Logger error: " + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
			logger.fatal(logger_exception_details(e));
		}  
		catch (SQLException e) {
			response.add_error("XDSRegistryError", "SQL error: " + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
			logger.fatal(logger_exception_details(e));
		}  
		catch (XdsException e) {
			response.add_error("XDSRegistryError", "XDS Error: " + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
			logger.warn(logger_exception_details(e));
		}
		catch (XuaException e) {
        	response.add_error("XDSRegistryError", "XUA Error" + e.getMessage(), RegistryUtility.exception_details(e), log_message);
		}
		catch (Exception e) {
			response.add_error("General Exception", "Internal Error: " + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
			logger.fatal(logger_exception_details(e));
		}  

		this.log_response();

		OMElement res = null;
		try {
			res =  response.getResponse();

			if (logger.isDebugEnabled()) {
				logger.debug("Response from the Registry");
				logger.debug(res.toString());
			}
		} catch (XdsInternalException e) {

		}
		return res;
	}

	public boolean isStoredQuery(OMElement ahqr) {
		for (Iterator it=ahqr.getChildElements(); it.hasNext(); ) {
			OMElement ele = (OMElement) it.next();
			String ele_name = ele.getLocalName();

			if (ele_name.equals("AdhocQuery")) 
				return true;
		}
		return false;
	}

	void AdhocQueryRequestInternal(OMElement ahqr) 
	throws SQLException, XdsException, LoggerException, XDSRegistryOutOfResourcesException, AxisFault, 
	SqlRepairException, XdsValidationException {

		boolean found_query = false;

		for (Iterator it=ahqr.getChildElements(); it.hasNext(); ) {
			OMElement ele = (OMElement) it.next();
			String ele_name = ele.getLocalName();

			if (ele_name.equals("SQLQuery")) {
				if (log_message != null)
					log_message.setTestMessage("SQL");
				RegistryUtility.schema_validate_local(ahqr, MetadataTypes.METADATA_TYPE_Q);
				found_query=true;
				OMElement result =  sql_query(ahqr);
				// move result elements to response
				if (result != null) {
					Metadata m = new Metadata(result, false /* parse */, true /* find_wrapper */);
					OMElement sqr = m.getWrapper();
					if (sqr != null) {
						for (Iterator it2=sqr.getChildElements(); it2.hasNext(); ) {
							OMElement e = (OMElement) it2.next();
							((AdhocQueryResponse)response).addQueryResults(e);
						}
					}
				}
			} else if (ele_name.equals("AdhocQuery")) {
				if (log_message != null)
					log_message.setTestMessage(service_name);

				RegistryUtility.schema_validate_local(ahqr, MetadataTypes.METADATA_TYPE_SQ);
				found_query = true;

				Metadata results =  stored_query(ahqr);


				//response.query_results = results;
				if (results != null)
					((AdhocQueryResponse)response).addQueryResults(results.getAllObjects());
			} 

		}
		if (!found_query)
			response.add_error("XDSRegistryError", "Only SQLQuery and AdhocQuery accepted",  "AdhocQueryRequest.java", log_message);

		this.log_response();

	}


	public String getStoredQueryId(OMElement ahqr) {
		OMElement adhoc_query = MetadataSupport.firstChildWithLocalName(ahqr, "AdhocQuery") ;
		if (adhoc_query == null) return null;
		return adhoc_query.getAttributeValue(MetadataSupport.id_qname);
	}

	public String getHome(OMElement ahqr) throws XdsInternalException, MetadataException, XdsException, LoggerException {
		OMElement adhocQuery = MetadataSupport.firstChildWithLocalName(ahqr, "AdhocQuery");
		return adhocQuery.getAttributeValue(MetadataSupport.home_qname);
//		return (String) new StoredQueryFactory(ahqr).getParm("$homeCommunityId");
		
//		OMElement ahquery = MetadataSupport.firstChildWithLocalName(ahqr, "AdhocQuery");
//		if (ahquery == null) return null;
//		return ahquery.getAttributeValue(MetadataSupport.id_qname);
	}

	// Initiating Gateway shall specify the homeCommunityId attribute in all Cross-Community 
	// Queries which do not contain a patient identifier.
	public boolean requiresHomeInXGQ(OMElement ahqr) {
		boolean requires = true;
		String query_id = getStoredQueryId(ahqr);
		if (query_id == null) requires = false;
		if (query_id.equals(MetadataSupport.SQ_FindDocuments)) requires = false; 
		if (query_id.equals(MetadataSupport.SQ_FindFolders)) requires = false; 
		if (query_id.equals(MetadataSupport.SQ_FindSubmissionSets)) requires = false; 
		if (query_id.equals(MetadataSupport.SQ_GetAll)) requires = false; 
		logger.info("query " + query_id + " requires home = " + requires);
		return requires;
	}
	
	public boolean isMPQ(OMElement ahqr) {
		String query_id = getStoredQueryId(ahqr);
		if (query_id == null) return false;
		if ("urn:uuid:3d1bdb10-39a2-11de-89c2-2f44d94eaa9f".equals(query_id)) return true;
		if ("urn:uuid:50d3f5ac-39a2-11de-a1ca-b366239e58df".equals(query_id)) return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	Metadata stored_query(OMElement ahqr) 
	throws XdsException, LoggerException, XDSRegistryOutOfResourcesException, XdsValidationException {
		boolean isMPQ = true;
		boolean isSQ = false;
	
		//		new StoredQueryRequestSoapValidator(xds_version, messageContext).runWithException();

		//try {
		// Registry holds the configuration for this implementation and selected
		// the correct Stored Query implementation by returning a factory object
		StoredQueryFactory fact = Registry.getStoredQueryFactory(ahqr, response, log_message); 
		fact.setServiceName(service_name);
		fact.setIsSecure(is_secure);
		StoredQuery sq = fact.getImpl();
		Metadata m = sq.run();

		//Filter metadata
		boolean filteringMetadataEnabled = PropertyFacade.getBoolean("filter.metadata");
		if(filteringMetadataEnabled){
			SoapHeader header = new SoapHeader(messageContext);
			try {
				m = filter(m, header);
			} catch (Exception e) {
				throw new XdsException("Exception while filtering metadata", e);
			}
		}
		if (!isMPQ(ahqr)) {	
			isMPQ = false;
			isSQ = true;
			if ( !m.isPatientIdConsistent() )
				throw new XdsResultNotSinglePatientException("More than one Patient ID in Stored Query result");
			
		}
		auditLog(ahqr, isSQ, fact.getQueryId(), isMPQ, m);
			
		return m;
		//		}
		//		catch (Exception e) {
		//			response.add_error("XDSRegistryError", ExceptionUtil.exception_details(e), "StoredQueryFactory.java", log_message);
		//			return null;
		//
		//		}

	}

	private OMElement sql_query(OMElement ahqr) 
	throws XdsInternalException, SchemaValidationException, LoggerException, SqlRepairException, MetadataException, MetadataValidationException, XMLParserException {
		// validate against schema - throws exception on error

		RegistryUtility.schema_validate_local(ahqr, MetadataTypes.METADATA_TYPE_Q);

		logger.debug("sql_query");
		// check and repair SQL
		boolean isleafClass = false; 
		SqlRepair sr = new SqlRepair();
		String return_type = sr.returnType(ahqr);
		if (return_type.equals("LeafClass"))
			isleafClass = true;
		
		try {
			sr.repair(ahqr);
		} catch (SqlRepairException e) {
			response.add_error("XDSSqlError", e.getMessage(),  RegistryUtility.exception_details(e), log_message);
			return null;
		} catch (XdsInternalException e) {
			response.add_error("XDSRegistryError", e.getMessage(), RegistryUtility.exception_details(e), log_message);
			return null;
		}

		if (log_message != null)
			log_message.addOtherParam("SQL", sr.get_query_text(ahqr));

		if (logger.isDebugEnabled()) {
			logger.debug("SQLQuery:\n" + sr.get_query_text(ahqr));
		}
		
		//return backend_query(ahqr);

		BackendRegistry br = new BackendRegistry(response, log_message);
		OMElement results = br.query(ahqr.toString(), isleafClass);

		Metadata metadata = MetadataParser.parseNonSubmission(results);

		auditLog(ahqr, false, null, false, null);
		
		if (is_secure) {
			BasicQuery bq = new BasicQuery();
			bq.secure_URI(metadata);
		}

		// Problem here - ebxmlrr returns wrong namespaces.  The following fixes
		results = metadata.fixSQLQueryResponse(return_type);

		return results;
	}

	 /**
      * Audit Logging of Document Query messages.
	 * @throws XdsInternalException, MetadataException 
      * 
      */
      private void auditLog(OMElement aqr, boolean isStoredQuery, String id, boolean isMPQ, Metadata m) throws XdsInternalException, XdsInternalException, MetadataException {
	       if (auditLog == null)
	       	 return;
	   
	       String replyto = getMessageContext().getReplyTo().getAddress();
			String remoteIP = (String)getMessageContext().getProperty(MessageContext.REMOTE_ADDR);
			String localIP = (String)getMessageContext().getProperty(MessageContext.TRANSPORT_ADDR);
			
	       ActiveParticipant source = new ActiveParticipant();
			source.setUserId(replyto);
			source.setAccessPointId(remoteIP);
			String userid = actor.getServiceEndpoint(isHttps()); 
			ActiveParticipant dest = new ActiveParticipant();
			dest.setUserId(userid);
			dest.setAccessPointId(localIP);
			
			//Patient Info
			List<ParticipantObject> patientObjs = new ArrayList<ParticipantObject>();
			ParticipantObject patientObj = null;
			
			//Query Info
			if (isStoredQuery) {
				ParamParser parser = new ParamParser();
				SqParams params = parser.parse(aqr);
				String patientId = params.getStringParm("$XDSDocumentEntryPatientId");				
				if(patientId != null) patientObj = new ParticipantObject("PatientIdentifier", patientId);
				patientObjs.add(patientObj);
			}
			
			if(isMPQ){
				List<String> patientIds = m.getPatientIds();
				for(String patientId :patientIds){
					patientObj = new ParticipantObject("PatientIdentifier", patientId);
					patientObjs.add(patientObj);
				}
			}
			
			ParticipantObject queryObj = new ParticipantObject();
			queryObj.setQuery(aqr.toString());
			if(isStoredQuery || isMPQ)
				queryObj.setId(id);
			
			//ITI CP 429
			try {
				String homeCommunityId = getHome(aqr);
				if (homeCommunityId != null) {
					queryObj.addDetail(new TypeValuePair("ihe:homeCommunityID", homeCommunityId));
				}
			}catch(Exception e) {
				logger.error("Failed to get homeCommunityID", e);
			}
			
			//Finally Log it.
			auditLog.logRegistryQuery(source, dest, patientObjs, queryObj, isStoredQuery, isMPQ);
	   }
      

}
