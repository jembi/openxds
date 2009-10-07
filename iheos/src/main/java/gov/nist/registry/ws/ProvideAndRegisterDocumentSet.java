package gov.nist.registry.ws;

import gov.nist.registry.common2.MetadataTypes;
import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.SchemaValidationException;
import gov.nist.registry.common2.exception.XDSMissingDocumentException;
import gov.nist.registry.common2.exception.XDSMissingDocumentMetadataException;
import gov.nist.registry.common2.exception.XdsConfigurationException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsFormatException;
import gov.nist.registry.common2.exception.XdsIOException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.io.ByteBuffer;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.RegistryResponse;
import gov.nist.registry.common2.registry.RegistryUtility;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.XdsCommon;
import gov.nist.registry.common2.soap.Soap;
import gov.nist.registry.ws.config.Repository;
import gov.nist.registry.xdslog.LoggerException;
import gov.nist.registry.xdslog.Message;

import java.io.IOException;
import java.util.ArrayList;

import javax.activation.DataHandler;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMText;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.openhealthtools.common.audit.IheAuditTrail;
import org.openhealthtools.common.audit.ParticipantObject;
import org.openhealthtools.common.ihe.IheActor;
import org.openhealthtools.common.ws.server.IheHTTPServer;
import org.openhealthtools.openxds.XdsFactory;
import org.openhealthtools.openxds.repository.api.RepositoryException;
import org.openhealthtools.openxds.repository.api.RepositoryRequestContext;
import org.openhealthtools.openxds.repository.api.XdsRepository;
import org.openhealthtools.openxds.repository.api.XdsRepositoryItem;
import org.openhealthtools.openxds.repository.api.XdsRepositoryService;

import sun.misc.BASE64Decoder;

import com.misyshealthcare.connect.base.audit.ActiveParticipant;
import com.misyshealthcare.connect.base.audit.AuditCodeMappings;
import com.misyshealthcare.connect.base.audit.AuditCodeMappings.AuditTypeCodes;
import com.misyshealthcare.connect.net.IConnectionDescription;

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

	static {
		BasicConfigurator.configure();
	}

	public ProvideAndRegisterDocumentSet(Message log_message, short xds_version, MessageContext messageContext) {
		this.log_message = log_message;
		this.messageContext = messageContext;
		this.xds_version = xds_version;
		IheHTTPServer httpServer = (IheHTTPServer)messageContext.getTransportIn().getReceiver();

		try {
			IheActor actor = httpServer.getIheActor();
			if (actor == null) {
				throw new XdsInternalException("Cannot find XdsRepository actor configuration.");			
			}
			connection = actor.getConnection();
			if (connection == null) {
				throw new XdsInternalException("Cannot find XdsRepository connection configuration.");			
			}
			registryClientConnection = ((XdsRepository)actor).getRegistryClientConnection();
			if (registryClientConnection == null) {
				throw new XdsInternalException("Cannot find XdsRepository XdsRegistryClient connection configuration.");			
			}
			auditLog = actor.getAuditTrail();	
			init(new RegistryResponse( (xds_version == xds_a) ?	Response.version_2 : Response.version_3), xds_version, messageContext);
		} catch (XdsInternalException e) {
			logger.fatal("Internal Error creating RegistryResponse: " + e.getMessage());
		}
	}

	public OMElement provideAndRegisterDocumentSet(OMElement pnr, ContentValidationService validater) {
		this.validater = validater;

		//System.out.println("PnR started -- version = " + xds_version);

		try {

			pnr.build();
			
			if (xds_version == xds_b) {
				mustBeMTOM();
			}
			
			provide_and_register(pnr);


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
			response.add_error("XDSRepositoryError", "Metadata Validation Errors:\n " + e.getMessage(), RegistryUtility.exception_details(e), log_message);
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
			//System.out.println("Error generating response");
			try {
				log_message.addErrorParam("Internal Error", "Error generating response from PnR");
			}
			catch (LoggerException e1) {

			}
		}
		return res;

	}

	void validate_docs_and_metadata_b(OMElement pnr, Metadata m) throws XDSMissingDocumentException, XDSMissingDocumentMetadataException {
		ArrayList<OMElement> docs = MetadataSupport.childrenWithLocalName(pnr, "Document");
		ArrayList<String> doc_ids = new ArrayList<String>();

		for (OMElement doc : docs) {
			String id = doc.getAttributeValue(MetadataSupport.id_qname);
			// if id == null or id ==""
			doc_ids.add(id);
		}

		ArrayList<String> eo_ids = m.getExtrinsicObjectIds();

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
	XdsIOException, LoggerException, XdsException, IOException {

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

		if (this.validater != null && !this.validater.runContentValidationService(m, response))
			return;

		int eo_count = m.getExtrinsicObjectIds().size();

		int doc_count = 0;
		if (this.xds_version == this.xds_b) {
			for (OMElement document : MetadataSupport.childrenWithLocalName(pnr, "Document")) {
				doc_count++;
				String id = document.getAttributeValue(MetadataSupport.id_qname);
				OMText binaryNode = (OMText) document.getFirstOMChild();
				//System.out.println("isOptimized: " + binaryNode.isOptimized());

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
					this.store_document_swa_xop(m, id, datahandler, datahandler.getContentType(), false /* validate_mime_type */);
				} else {
					String base64 = binaryNode.getText();
					BASE64Decoder d  = new BASE64Decoder();
					byte[] ba = d.decodeBuffer(base64);
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

		log_message.addOtherParam("Register transaction endpoint", epr);

		log_message.addOtherParam("Register transaction", register_transaction.toString());

		Soap soap = new Soap();
		try {
			OMElement result;
			try {
				if (this.xds_version == XdsCommon.xds_b) {
					soap.soapCall(register_transaction, epr, false, true, true, "urn:ihe:iti:2007:RegisterDocumentSet-b","urn:ihe:iti:2007:RegisterDocumentSet-bResponse");
				} else {
					soap.soapCall(register_transaction, epr, false, true, false, "urn:anonOutInOp" ,null);				
				}
			}
			catch (XdsException e) {
				response.add_error(MetadataSupport.XDSRepositoryError, e.getMessage(), RegistryUtility.exception_details(e), log_message);
			}
			result = soap.getResult();
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
					}
					//ITI-42 Succeed, log a success message
					if(auditLog != null)
					auditLog(m, AuditTypeCodes.RegisterDocumentSet_b, false);
				}
			}
		}
		catch (Exception e) {
			response.add_error(MetadataSupport.XDSRepositoryError, e.getMessage(), "ProvideAndRegistryDocumentSet.java", log_message);
		}

	}

	String registry_endpoint() {
		return (registry_endpoint == null) ? Repository.getRegisterTransactionEndpoint(registryClientConnection) : registry_endpoint;
	}

	void log_headers(Soap soap) throws LoggerException, XdsInternalException {
		OMElement in_hdr = soap.getInHeader();
		OMElement out_hdr = soap.getOutHeader();
		log_message.addSoapParam("Header sent to Registry", (out_hdr == null) ? "Null" : out_hdr.toString());
		log_message.addSoapParam("Header received from Registry", (in_hdr == null) ? "Null" : in_hdr.toString());
	}


	private OMElement find_sor(OMElement pnr) throws MetadataValidationException {
		OMElement first = pnr.getFirstElement();
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
	throws MetadataException, XdsIOException, XdsInternalException, XdsConfigurationException, XdsException {
		OMElement extrinsic_object = m.getObjectById(id);
		
		// content type is not guaranteed by the standard so this validation has been removed
		validate_content_type = false;

		if (extrinsic_object == null) 
			throw new MetadataException("Document submitted with id of " + id + " but no ExtrinsicObject exists in metadata with same id");

		String uid = m.getExternalIdentifierValue(id, "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab");  // doc uniqueid
		if (uid == null)
			throw new MetadataException("Document " + id + " does not have a Unique ID");

		String mime_type = extrinsic_object.getAttributeValue(MetadataSupport.mime_type_qname);

		if (mime_type == null || mime_type.equals(""))
			throw new MetadataException("ExtrinsicObject " + id + " does not have a mimeType");

		if ( validate_content_type && !mime_type.equals(content_type))
			throw new MetadataException("ExtrinsicObject " + id + " metadata has mimeType is " + mime_type +
					" but document content type is " + content_type);
					
		XdsRepositoryService rm = XdsFactory.getXdsRepositoryService();
		XdsRepositoryItem item = XdsFactory.getXdsReposiotryItem();
		item.setDocumentUniqueId(uid);
		item.setDataHandler(dataHandler); 
		try {
			RepositoryRequestContext context = new RepositoryRequestContext();
			context.setConnection(connection);
			rm.insert(item, context);
		}catch(RepositoryException e) {
			throw new XdsException("Error saving document to the repository - " + e.getMessage(), e);
		}
		if(auditLog != null)
		auditLog(m, AuditTypeCodes.ProvideAndRegisterDocumentSet_b, true);
//TODO: remove the old code			
//		String doc_path = document_path(uid, mime_type);
//		ByteBuffer buffer = new ByteBuffer();
//		int length = 4000;
//		byte[] buf = new byte[length];
//		FileOutputStream fos = null;
//		try { fos = new FileOutputStream(new File(doc_path)); } catch (FileNotFoundException e) { throw new XdsIOException("Error creating file " + doc_path + " for repository item");  }
//		int size = 0;
//		try { size = is.read(buf, 0, length); }  catch (IOException e) {   throw new XdsIOException("Error when starting to read document content");   }
//		buffer.append(buf, 0, size);
//		while (size > 0) {
//			try { fos.write(buf, 0, size); } catch (IOException e) {   throw new XdsIOException("Error writing document content");   }
//			try { size = is.read(buf, 0, length); }  catch (IOException e) {   throw new XdsIOException("Error reading document content");  }
//			buffer.append(buf,0, size);
//		}
//		try { fos.close(); } catch (IOException e) {   throw new XdsIOException("Error closing repository item file");   }
//		try { is.close();  } catch (IOException e) {   throw new XdsIOException("Error closing repository item input stream");   }

		// set size, hash, URI into metadata
		try {
			m.setSlot(extrinsic_object, "size", Integer.toString(item.getSize()));
		}catch(RepositoryException e) {
			throw new XdsInternalException("Error calculating size on repository file"); 
		}
		try {
//TODO: remove the old code			
//			m.setSlot(extrinsic_object, "hash", (new Sha1Bean()).getSha1File(new File(doc_path)));
			m.setSlot(extrinsic_object, "hash", Long.toString(item.getHash()));
		} catch (Exception e) { throw new XdsInternalException("Error calculating hash on repository file"); }
        //TODO: either remove URI attribute, or make the uri work. URI is an attribute required by XDS.a
		//m.setSlot(extrinsic_object, "URI",  document_uri (uid, mime_type));
		m.setURIAttribute(extrinsic_object, document_uri (uid, mime_type));

	}

	private void store_document_mtom(Metadata m, String id, byte[] bytes) 
	throws MetadataException, XdsIOException, XdsInternalException, XdsConfigurationException, XdsException {
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

		XdsRepositoryService rm = XdsFactory.getXdsRepositoryService();
		XdsRepositoryItem item = XdsFactory.getXdsReposiotryItem();
		item.setDocumentUniqueId(uid);
		item.setDataHandler(dataHandler); 
		try {
			RepositoryRequestContext context = new RepositoryRequestContext();
			context.setConnection(connection);
			rm.insert(item, context);
		}catch(RepositoryException e) {
			throw new XdsException("Error saving document to the repository - " + e.getMessage(), e);
		}
		if(auditLog != null)
		auditLog(m, AuditTypeCodes.ProvideAndRegisterDocumentSet_b, true);
//TODO: remove the old code
//		String doc_path = document_path(uid, mime_type);
//		FileOutputStream fos = null;
//		try { fos = new FileOutputStream(new File(doc_path)); } catch (FileNotFoundException e) { throw new XdsIOException("Error creating file " + doc_path + " for repository item");  }
//		try { fos.write(bytes); } catch (IOException e) {   throw new XdsIOException("Error writing document content");   }
//		try { fos.close(); } catch (IOException e) {   throw new XdsIOException("Error closing repository item file");   }

		ByteBuffer bb = new ByteBuffer();
		bb.append(bytes, 0, bytes.length);
		// set size, hash, URI into metadata
		m.setSlot(extrinsic_object, "size", Integer.toString(bytes.length));
		try {
//TODO: remove the old code			
//			m.setSlot(extrinsic_object, "hash", (new Sha1Bean()).getSha1File(new File(doc_path)));
			m.setSlot(extrinsic_object, "hash", Long.toString(item.getHash()));
		} catch (Exception e) { throw new XdsInternalException("Error calculating hash on repository file"); }
		//m.setSlot(extrinsic_object, "URI",  document_uri (uid, mime_type));
		m.setURIAttribute(extrinsic_object, document_uri (uid, mime_type));
	}

	void setRepositoryUniqueId(Metadata m) throws MetadataException {
		for (OMElement eo : m.getExtrinsicObjects()) {
			m.setSlot(eo, "repositoryUniqueId", Repository.getRepositoryUniqueId());
		}
	}

	String document_path(String uid, String mime_type)  throws MetadataException, XdsException {
		return Repository.getBaseDirectory() + uid + "." + (new DocumentTypes(connection)).fileExtension(mime_type);
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
