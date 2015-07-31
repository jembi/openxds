package gov.nist.registry.ws;

import gov.nist.registry.common2.MetadataTypes;
import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.SchemaValidationException;
import gov.nist.registry.common2.exception.XDSMissingDocumentException;
import gov.nist.registry.common2.exception.XDSMissingDocumentMetadataException;
import gov.nist.registry.common2.exception.XDSRepositoryMetadataException;
import gov.nist.registry.common2.exception.XdsConfigurationException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsFormatException;
import gov.nist.registry.common2.exception.XdsIOException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.io.Sha1Bean;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.RegistryResponse;
import gov.nist.registry.common2.registry.RegistryUtility;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.XdsCommon;
import gov.nist.registry.common2.soap.Soap;
import gov.nist.registry.ws.config.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.OMText;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.common.ihe.IheActor;
import org.openhealthtools.common.utils.ConnectionUtil;
import org.openhealthtools.common.ws.server.IheHTTPServer;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.audit.ActiveParticipant;
import org.openhealthtools.openexchange.audit.AuditCodeMappings;
import org.openhealthtools.openexchange.audit.IheAuditTrail;
import org.openhealthtools.openexchange.audit.ParticipantObject;
import org.openhealthtools.openexchange.audit.AuditCodeMappings.AuditTypeCodes;
import org.openhealthtools.openexchange.config.PropertyFacade;
import org.openhealthtools.openxds.XdsFactory;
import org.openhealthtools.openxds.log.LogMessage;
import org.openhealthtools.openxds.log.LoggerException;
import org.openhealthtools.openxds.repository.api.RepositoryException;
import org.openhealthtools.openxds.repository.api.RepositoryRequestContext;
import org.openhealthtools.openxds.repository.api.XdsRepository;
import org.openhealthtools.openxds.repository.api.XdsRepositoryItem;
import org.openhealthtools.openxds.repository.api.XdsRepositoryService;
import org.openhealthtools.openxua.api.XuaException;


public class ProvideAndRegisterDocumentSet extends XdsCommon {
	ContentValidationService validater;
	String registry_endpoint = null;
	MessageContext messageContext;
	boolean accept_xop = true;
    IConnectionDescription connection = null;
    IConnectionDescription registryClientConnection = null;
	/* The IHE Audit Trail for this actor. */
	private IheAuditTrail auditLog = null;
	private final static Log logger = LogFactory.getLog(ProvideAndRegisterDocumentSet.class);

//	static {
//		BasicConfigurator.configure();
//	}

	public ProvideAndRegisterDocumentSet(LogMessage log_message, short xds_version, MessageContext messageContext) {
		this.log_message = log_message;
		this.messageContext = messageContext;
		this.xds_version = xds_version;
		transaction_type = PR_transaction;

		try {
			if (messageContext == null) {
				throw new XdsInternalException("Cannot find MessageContext");
			}
			IheHTTPServer httpServer = (IheHTTPServer)messageContext.getTransportIn().getReceiver();
			
			IheActor actor = httpServer.getIheActor();
			if (actor == null) {
				throw new XdsInternalException("Cannot find XdsRepository actor configuration.");			
			}
			connection = actor.getConnection();
			if (connection == null) {
				throw new XdsInternalException("Cannot find Server connection configuration.");			
			}
			registryClientConnection = ((XdsRepository)actor).getRegistryClientConnection();
			if (registryClientConnection == null) {
				throw new XdsInternalException("Cannot find XdsRepository Registry connection configuration.");			
			}
			auditLog = actor.getAuditTrail();	
			init(new RegistryResponse( (xds_version == xds_a) ?	Response.version_2 : Response.version_3), xds_version, messageContext);
		} catch (XdsInternalException e) {
			logger.fatal("Internal Error creating RegistryResponse: " + e.getMessage());
		}
	}

	public OMElement provideAndRegisterDocumentSet(OMElement pnr, ContentValidationService validater) {
		this.validater = validater;

		// Call X-Service Provider Actor to validate X-User Assertion with X-Assertion Provider
		try {
			boolean validateUserAssertion = PropertyFacade.getBoolean("validate.userassertion");
			if(validateUserAssertion){
		        SoapHeader header = new SoapHeader(messageContext);
		        boolean status = validateAssertion(header);
		        if (!status)
					throw new XdsException("Invalid Identity Assertion");
			}
			pnr.build();
			provide_and_register(pnr);
		}
		catch (XDSRepositoryMetadataException e) {
			response.add_error("XDSRepositoryMetadataError", e.getMessage(), RegistryUtility.exception_trace(e), log_message);
		} 
		catch (XdsFormatException e) {
			response.add_error("XDSRepositoryError", "SOAP Format Error: " + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
		} 
		catch (XDSMissingDocumentException e) {
			response.add_error("XDSMissingDocument", e.getMessage(), RegistryUtility.exception_details(e), log_message);
			logger.warn(logger_exception_details(e));
		} 
		catch (XDSMissingDocumentMetadataException e) {
			response.add_error("XDSMissingDocumentMetadata", e.getMessage(), RegistryUtility.exception_details(e), log_message);
			logger.warn(logger_exception_details(e));
		} 
		catch (XdsInternalException e) {
			response.add_error("XDSRepositoryError", "XDS Internal Error:\n " + e.getMessage(), RegistryUtility.exception_details(e), log_message);
			logger.fatal(logger_exception_details(e));
		} 
		catch (XdsIOException e) {
			response.add_error("XDSRepositoryError", "XDS IO Error:\n " + e.getMessage(), RegistryUtility.exception_details(e), log_message);
			logger.fatal(logger_exception_details(e));
		} 
		catch (XdsConfigurationException e) {
			response.add_error("XDSRepositoryError", "XDS Configuration Error:\n " + e.getMessage(), RegistryUtility.exception_details(e), log_message);
			logger.fatal(logger_exception_details(e));
		} 
		catch (MetadataValidationException e) {			
			response.add_error(MetadataSupport.XDSRepositoryMetadataError, "Test input incorrect:\n " + e.getMessage(), RegistryUtility.exception_details(e), log_message);
		} 
		catch (MetadataException e) {
			response.add_error("XDSRepositoryError", "Metadata Validation Errors:\n " + e.getMessage(), RegistryUtility.exception_details(e), log_message);
		} 
		catch (LoggerException e) {
			response.add_error("XDSRegistryError", "Internal Logging error: LoggerException: " + e.getMessage(), RegistryUtility.exception_details(e), log_message);
			logger.fatal(logger_exception_details(e));
		} 
		catch (SchemaValidationException e) {
			response.add_error("XDSRepositoryError", "Schema Validation Errors:\n" + e.getMessage(), RegistryUtility.exception_details(e), log_message);
		} 
		catch (XdsException e) {
			response.add_error("XDSRepositoryError", "XDS Internal Error:\n " + e.getMessage(), RegistryUtility.exception_details(e), log_message);
			logger.warn(logger_exception_details(e));
		}
		catch (XuaException e) {
        	response.add_error("XDSRepositoryError", "XUA Error" + e.getMessage(), RegistryUtility.exception_details(e), log_message);
		}
		catch (Exception e) {
			response.add_error("XDSRepositoryError", "Input Error - no SOAP Body:\n " + e.getMessage(), RegistryUtility.exception_details(e), log_message);
			logger.fatal(logger_exception_details(e));
		} 

		this.log_response();

		OMElement res = null;
		try {
			res =  response.getResponse();

			if (logger.isDebugEnabled()) {
				logger.debug("Response from the Repository: ");
				logger.debug(res.toString());
			}
		} catch (XdsInternalException e) {
			logger.error("Error generating response");
			try {
				log_message.addErrorParam("Internal Error", "Error generating response from PnR");
			}
			catch (LoggerException e1) {

			}
		}
		return res;

	}

	void validate_docs_and_metadata_b(OMElement pnr, Metadata m) throws XDSMissingDocumentException, XDSMissingDocumentMetadataException {
		List<OMElement> docs = MetadataSupport.childrenWithLocalName(pnr, "Document");
		List<String> doc_ids = new ArrayList<String>();

		for (OMElement doc : docs) {
			String id = doc.getAttributeValue(MetadataSupport.id_qname);
			// if id == null or id ==""
			doc_ids.add(id);
		}

		List<String> eo_ids = m.getExtrinsicObjectIds();

		for (String id : eo_ids) {
			if ( ! doc_ids.contains(id))
				throw new XDSMissingDocumentException("Document with id " + id + " is missing");
		}

		for (String id : doc_ids) {
			if ( ! eo_ids.contains(id))
				throw new XDSMissingDocumentMetadataException("XDSDocumentEntry with id " + id + " is missing");
		}

	}

	void provide_and_register(OMElement pnr) 
	throws MetadataValidationException, SchemaValidationException, 
	XdsInternalException, MetadataException, XdsConfigurationException,
	XdsIOException, LoggerException, XdsException, IOException, XDSRepositoryMetadataException {

		RegistryUtility.schema_validate_local(	
				pnr, 
				(xds_version == xds_a) ? MetadataTypes.METADATA_TYPE_R : MetadataTypes.METADATA_TYPE_RET);

		OMElement sor = find_sor(pnr);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Request from the Source:");
			logger.debug(sor.toString());
		}
		
		Metadata m = new Metadata(sor);
		
		generateAuditLog(m);

		log_message.addOtherParam("SSuid", m.getSubmissionSetUniqueId());
		log_message.addOtherParam("Structure", m.structure());

		if (xds_version == xds_b)
			this.validate_docs_and_metadata_b(pnr, m);

//		Validator val = new Validator(m, response.registryErrorList, true, xds_version == xds_b, log_message, true, connection);
//		val.run();

		if (this.validater != null && !this.validater.runContentValidationService(m, response))
			return;

		int eo_count = m.getExtrinsicObjectIds().size();

		int doc_count = 0;
		if (this.xds_version == xds_b) {
			for (OMElement document : MetadataSupport.childrenWithLocalName(pnr, "Document")) {
				doc_count++;
				String id = document.getAttributeValue(MetadataSupport.id_qname);
				OMText binaryNode = getBinaryNode(document);
				
				if (logger.isDebugEnabled()) {
					logger.debug("isOptimized: " + binaryNode.isOptimized());
				}
				
				if ( ! accept_xop ) {
					if (binaryNode.isOptimized() == true) {
						throw new XdsIOException("Submission uses XOP for optimized encoding - not acceptable on this endpoint");
					}
				}

				boolean optimized = false;
				javax.activation.DataHandler datahandler = null;
				try {
					datahandler = (javax.activation.DataHandler) binaryNode.getDataHandler();
					optimized = true;
				}
				catch (Exception e) {
					// good - message not optimized
				}

				if ( ! accept_xop ) {
					if (optimized) 
						throw new XdsIOException("Submission uses XOP for optimized encoding - this is not acceptable on this endpoint");
				}

				if (optimized) {
					store_document_swa_xop(m, id, datahandler, datahandler.getContentType(), false /* validate_mime_type */);
				} else {
					String base64 = binaryNode.getText();
					byte[] ba = Base64.decodeBase64(base64.getBytes());
					store_document_mtom(m, id, ba);
				}


			}
		} else { // xds.a
			// need message context to implement
			if (messageContext == null)
				throw new XdsInternalException("XDS.a: PnR: no MessageContext");

			doc_count = messageContext.getAttachmentMap().getContentIDSet().size() - 1;  // metadata counts too
			if (doc_count == -1)
				doc_count = 0;


			for (OMElement eo : m.getExtrinsicObjects()) {
				String id = eo.getAttributeValue(MetadataSupport.id_qname);
				DataHandler dh = messageContext.getAttachment(id);
				if (dh == null) 
					throw new XDSMissingDocumentException("Cannot find attachment for id " + id);

				store_document_swa_xop(m, id, dh, dh.getContentType(), true /* validate_mime_type */);
			}
		}

		if (eo_count != doc_count)
			throw new XDSMissingDocumentMetadataException("Submission contained " + doc_count + " documents but " + eo_count +
			" ExtrinsicObjects in metadata - they must match");

		setRepositoryUniqueId(m);

		OMElement register_transaction = (this.xds_version == xds_a) ? m.getV2SubmitObjectsRequest() : m.getV3SubmitObjectsRequest();

		String epr = registry_endpoint();

		Protocol protocol = ConnectionUtil.getProtocol(registryClientConnection);

		log_message.addOtherParam("Register transaction endpoint", epr);

		log_message.addOtherParam("Register transaction", register_transaction.toString());

		boolean success = false;
		Soap soap = new Soap();
		try {
			OMElement result;
			try {
				if (this.xds_version == XdsCommon.xds_b) {
					soap.soapCall(register_transaction, protocol, epr, false, true, true, "urn:ihe:iti:2007:RegisterDocumentSet-b", null);
				} else {
					soap.soapCall(register_transaction, protocol, epr, false, true, false, "urn:anonOutInOp" ,null);				
				}
			}
			catch (XdsException e) {
				response.add_error(MetadataSupport.XDSRepositoryError, e.getMessage(), RegistryUtility.exception_details(e), log_message);
			}
			
			result = soap.getResult();
			if (result != null) {
				QName testlogid = new QName("testLogId");
				String registryTestLogId = result.getAttributeValue(testlogid);
				if (registryTestLogId != null) {
					log_message.addOtherParam("Registry Test Log ID", registryTestLogId);
					// remove attribute - just private communitication
					OMAttribute tlidA = result.getAttribute(testlogid);
					if (tlidA != null)
						result.removeAttribute(tlidA);
				}
			}
			log_headers(soap);

			if (result == null) {
				response.add_error(MetadataSupport.XDSRepositoryError, "Null response message from Registry", "ProvideAndRegistryDocumentSet.java", log_message);
				log_message.addOtherParam("Register transaction response", "null");
			} else {
				log_message.addOtherParam("Register transaction response", result.toString());

				String status = result.getAttributeValue(MetadataSupport.status_qname);
				if (status == null) {
					response.add_error(MetadataSupport.XDSRepositoryError, "Null status from Registry", "ProvideAndRegistryDocumentSet.java", log_message);
				} else {
					status = m.stripNamespace(status);
					if ( !status.equals("Success")) {
						OMElement registry_error_list = MetadataSupport.firstChildWithLocalName(result, "RegistryErrorList"); 
						if (registry_error_list != null)
							response.addRegistryErrorList(registry_error_list, log_message);
						else
							response.add_error(MetadataSupport.XDSRepositoryError, "Registry returned Failure but no error list", "ProvideAndRegistryDocumentSet.java", log_message);
					} else {
						success = true;
					}
				}
			}
		}
		catch (Exception e) {
			response.add_error(MetadataSupport.XDSRepositoryError, e.getMessage(), "ProvideAndRegistryDocumentSet.java", log_message);
		}
		
		if (success) {
			//ITI-42 Succeed, log a success message
			auditLog(m, AuditTypeCodes.RegisterDocumentSet_b, false);
		} else {		
			List<String> rollbackDocs = new ArrayList<String>();
			
			for (OMElement document : MetadataSupport.childrenWithLocalName(pnr, "Document")) {
				doc_count++;
				String id = document.getAttributeValue(MetadataSupport.id_qname);
				String uid = m.getExternalIdentifierValue(id, "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab");  // doc uniqueid
				if (uid != null) {
					rollbackDocs.add( uid );
				}
			}

			rollbackDocument( rollbackDocs );
		}
	}

	static OMText getBinaryNode(OMElement document) {
		Iterator<OMNode> childrenIterator = document.getChildren();
		while (childrenIterator.hasNext())
		{
			OMNode container = childrenIterator.next();
			if (container instanceof OMText && StringUtils.isNotBlank(((OMText)container).getText()))
			{
				return (OMText)container;
			}
		}		
		return null;
	}

	private void rollbackDocument(List<String> docs) {
		try {
			XdsRepositoryService rs = XdsFactory.getXdsRepositoryService();
			
			rs.delete(docs, new RepositoryRequestContext());
			
		}catch(RepositoryException e) {
			logger.error("Error rolling back document from the repository - " + e.getMessage(), e);
		}
	}
	
	String registry_endpoint() {
		return (registry_endpoint == null) ? ConnectionUtil.getTransactionEndpoint(registryClientConnection) : registry_endpoint;
	}

	void log_headers(Soap soap) throws LoggerException, XdsInternalException {
		OMElement in_hdr = soap.getInHeader();
		OMElement out_hdr = soap.getOutHeader();
		log_message.addSoapParam("Header sent to Registry", (out_hdr == null) ? "Null" : out_hdr.toString());
		log_message.addSoapParam("Header received from Registry", (in_hdr == null) ? "Null" : in_hdr.toString());
	}


	private OMElement find_sor(OMElement pnr) throws MetadataValidationException {
		//		OMElement first = pnr.getFirstElement();
		OMElement sor;
		if (pnr.getLocalName().equals("SubmitObjectsRequest"))
			sor = pnr;  // xds.a
		else {  // xds.b
			sor = pnr.getFirstElement();
			if (sor == null || !sor.getLocalName().equals("SubmitObjectsRequest")) 
				throw new MetadataValidationException("Cannot find SubmitObjectsRequest element in submission - top level element is " +
						pnr.getLocalName());
		}
		return sor;
	}

	public void setRegistryEndPoint(String endpoint) {
		this.registry_endpoint = endpoint;
	}

	private void store_document_swa_xop(Metadata m, String id, DataHandler dataHandler, String content_type, boolean validate_content_type) 
    throws MetadataException, XdsIOException, XdsInternalException, XdsConfigurationException, XdsException, XDSRepositoryMetadataException {
		OMElement extrinsic_object = m.getObjectById(id);
		String actualDocSize = null;
		String actualDocHash = null;
		// content type is not guaranteed by the standard so this validation has been removed
		validate_content_type = false;
		
		if (extrinsic_object == null) 
			throw new MetadataException("Document submitted with id of " + id + " but no ExtrinsicObject exists in metadata with same id");

		String uid = m.getExternalIdentifierValue(id, "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab");  // doc uniqueid
		if (uid == null)
			throw new MetadataException("Document " + id + " does not have a Unique ID");
		
		XdsRepositoryService rm = XdsFactory.getXdsRepositoryService();
		XdsRepositoryItem item = XdsFactory.getXdsReposiotryItem();
		item.setDocumentUniqueId(uid);
		item.setDataHandler(dataHandler);
		
		String mime_type = extrinsic_object.getAttributeValue(MetadataSupport.mime_type_qname);

		int isize = -1; 
        try{
            isize = item.getSize(); 
        	actualDocSize = Integer.toString(isize);          			
		}catch (Exception e) {throw new XdsInternalException("Error calculating size on repository file");}
		
	    try{
	    	actualDocHash = (new Sha1Bean()).getSha1(item.getDataHandler(), isize );
        } catch (Exception e) {	throw new XdsInternalException("Error calculating hash on repository file");}
            
		validate_size_and_hash(m, extrinsic_object, actualDocSize, actualDocHash);

		if (mime_type == null || mime_type.equals(""))
			throw new MetadataException("ExtrinsicObject " + id + " does not have a mimeType");

		if ( validate_content_type && !mime_type.equals(content_type))
			throw new MetadataException("ExtrinsicObject " + id + " metadata has mimeType is " + mime_type +
					" but document content type is " + content_type);

		item.setMimeType(mime_type);

		try {
			RepositoryRequestContext context = new RepositoryRequestContext();
			context.setConnection(connection);
			rm.insert(item, context);
		}catch(RepositoryException e) {
			throw new XdsException("Error saving document to the repository - " + e.getMessage(), e);
		}
		
		auditLog(m, AuditTypeCodes.ProvideAndRegisterDocumentSet_b, true);

		// set size, hash, URI into metadata
		m.setSlot(extrinsic_object, "size", actualDocSize);
		m.setSlot(extrinsic_object, "hash", actualDocHash);
		//m.setURIAttribute(extrinsic_object, document_uri (uid, mime_type));
	}

	private void validate_size_and_hash(Metadata m, OMElement extrinsic_object,
			String size_str, String hash_value)
	throws XDSRepositoryMetadataException {
		// if size already in metadata, must match data or error
		String pnr_size = m.getSlotValue(extrinsic_object, "size", 0);
		if (pnr_size != null) {
			if (!pnr_size.equals(size_str)) {
				throw new XDSRepositoryMetadataException("Size attribute in Provide and Register metadata does not match supplied document: Metadata has " + pnr_size +" and contents has " + size_str);
			}
		}

		// if hash already in metadata, must match data or error
		String pnr_hash = m.getSlotValue(extrinsic_object, "hash", 0);
		if (pnr_hash != null) {
			if (!pnr_hash.equalsIgnoreCase(hash_value)) {
				throw new XDSRepositoryMetadataException("Hash attribute in Provide and Register metadata does not match supplied document: Metadata has " + pnr_hash +" and contents has " + hash_value);
			}
		}
	}

	private void store_document_mtom(Metadata m, String id, byte[] bytes) 
	throws MetadataException, XdsIOException, XdsInternalException, XdsConfigurationException, XdsException, XDSRepositoryMetadataException {
		OMElement extrinsic_object = m.getObjectById(id);

		if (extrinsic_object == null) 
			throw new MetadataException("Document submitted with id of " + id + " but no ExtrinsicObject exists in metadata with same id");

		String uid = m.getExternalIdentifierValue(id, "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab");  // doc uniqueid
		if (uid == null)
			throw new MetadataException("Document " + id + " does not have a Unique ID");

		String mime_type = extrinsic_object.getAttributeValue(MetadataSupport.mime_type_qname);

		if (mime_type == null || mime_type.equals(""))
			throw new MetadataException("ExtrinsicObject " + id + " does not have a mimeType");

        DataHandler dataHandler = new DataHandler(new String(bytes), mime_type);

		String size_str = Integer.toString(bytes.length);
		String hash_value = null;
		try {
			hash_value = (new Sha1Bean()).getSha1(dataHandler, bytes.length);
		} catch (Exception e) {
			throw new XdsInternalException("Error calculating hash on repository file");
		}

		validate_size_and_hash(m, extrinsic_object, size_str, hash_value);

		XdsRepositoryService rm = XdsFactory.getXdsRepositoryService();
		XdsRepositoryItem item = XdsFactory.getXdsReposiotryItem();
		item.setDocumentUniqueId(uid);
		item.setDataHandler(dataHandler); 
		item.setMimeType(mime_type);
		try {
			RepositoryRequestContext context = new RepositoryRequestContext();
			context.setConnection(connection);
			rm.insert(item, context);
		}catch(RepositoryException e) {
			throw new XdsException("Error saving document to the repository - " + e.getMessage(), e);
		}

		auditLog(m, AuditTypeCodes.ProvideAndRegisterDocumentSet_b, true);		

		m.setSlot(extrinsic_object, "size", size_str);
		m.setSlot(extrinsic_object, "hash", hash_value);
		//m.setURIAttribute(extrinsic_object, document_uri (uid, mime_type));
	}

	void setRepositoryUniqueId(Metadata m) throws MetadataException {
		for (OMElement eo : m.getExtrinsicObjects()) {
			m.setSlot(eo, "repositoryUniqueId", Repository.getRepositoryUniqueId());
		}
	}

	String document_uri(String uid, String mime_type)throws MetadataException, XdsConfigurationException, XdsException {
		return Repository.getBaseUri() + uid + "." + (new DocumentTypes(connection)).fileExtension(mime_type);
	}
	
	/**
	 * Audit Logging of ProvodeAndRegisterDocumentSet message.
	 * 
	 * @throws MetadataException 
	 */
	private void auditLog(Metadata meatdata, AuditCodeMappings.AuditTypeCodes typeCode, boolean isITI41) throws MetadataException {
		if (auditLog == null)
			return;
		String replyto = getMessageContext().getReplyTo().getAddress();
		String remoteIP = (String)getMessageContext().getProperty(MessageContext.REMOTE_ADDR);
		String localIP = (String)getMessageContext().getProperty(MessageContext.TRANSPORT_ADDR);

	
		
		ParticipantObject set = new ParticipantObject("SubmissionSet",  meatdata.getSubmissionSetUniqueId());
		ParticipantObject patientObj = new ParticipantObject("PatientIdentifier", meatdata.getSubmissionSetPatientId());
		if(isITI41){
			ActiveParticipant source = new ActiveParticipant();
			source.setUserId(replyto);
			source.setAccessPointId(remoteIP);
			
			ActiveParticipant dest = new ActiveParticipant();
			//TODO: Needs to be improved
			String userid = "http://"+connection.getHostname()+":"+connection.getPort()+"/axis2/services/xdsrepositoryb"; 
			dest.setUserId(userid);
			dest.setAccessPointId(localIP);
			auditLog.logDocumentImport(source, dest, patientObj, set, typeCode);
		}else{
			ActiveParticipant source = new ActiveParticipant();
			source.setUserId(replyto);
			source.setAccessPointId(localIP);
			
			ActiveParticipant dest = new ActiveParticipant();
			dest.setUserId(registry_endpoint());
			dest.setAccessPointId(localIP);			
			auditLog.logDocumentExport(source, dest, patientObj, set, typeCode);	
		}
	}
	

}
