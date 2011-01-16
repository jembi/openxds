package gov.nist.registry.ws;

import gov.nist.registry.common2.MetadataTypes;
import gov.nist.registry.common2.datatypes.Hl7Date;
import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.SchemaValidationException;
import gov.nist.registry.common2.exception.XMLParserException;
import gov.nist.registry.common2.exception.XdsDeprecatedException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsFormatException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.exception.XdsNonIdenticalHashException;
import gov.nist.registry.common2.exception.XdsPatientIdDoesNotMatchException;
import gov.nist.registry.common2.exception.XdsUnknownPatientIdException;
import gov.nist.registry.common2.registry.BackendRegistry;
import gov.nist.registry.common2.registry.IdParser;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataParser;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.RegistryResponse;
import gov.nist.registry.common2.registry.RegistryUtility;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.XdsCommon;
import gov.nist.registry.common2.registry.storedquery.StoredQuerySupport;
import gov.nist.registry.common2.registry.validation.Structure;
import gov.nist.registry.common2.registry.validation.Validator;
import gov.nist.registry.common2.util.PidHelper;
import gov.nist.registry.ws.config.Registry;
import gov.nist.registry.ws.sq.RegistryObjectValidator;
import gov.nist.registry.ws.sq.RegistryValidations;
import gov.nist.registry.ws.sq.SQFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerConfigurationException;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.opendsub.DsubException;
import org.openhealthtools.opendsub.producer.DsubPublisher;
import org.openhealthtools.openexchange.actorconfig.Configuration;
import org.openhealthtools.openexchange.actorconfig.IheConfigurationException;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.audit.ActiveParticipant;
import org.openhealthtools.openexchange.audit.AuditCodeMappings;
import org.openhealthtools.openexchange.audit.IheAuditTrail;
import org.openhealthtools.openexchange.audit.ParticipantObject;
import org.openhealthtools.openexchange.audit.AuditCodeMappings.AuditTypeCodes;
import org.openhealthtools.openexchange.config.PropertyFacade;
import org.openhealthtools.openexchange.datamodel.Identifier;
import org.openhealthtools.openexchange.datamodel.PatientIdentifier;
import org.openhealthtools.openexchange.syslog.LogMessage;
import org.openhealthtools.openexchange.syslog.LoggerException;
import org.openhealthtools.openexchange.utils.OMUtil;
import org.openhealthtools.openexchange.utils.hl7.HL7;
import org.openhealthtools.openxds.common.AssigningAuthorityUtil;
import org.openhealthtools.openxds.common.ConnectionUtil;
import org.openhealthtools.openxds.common.XdsConstants;
import org.openhealthtools.openxds.common.XdsFactory;
import org.openhealthtools.openxds.dsub.Publisher;
import org.openhealthtools.openxds.registry.api.RegistryLifeCycleContext;
import org.openhealthtools.openxds.registry.api.RegistryLifeCycleException;
import org.openhealthtools.openxds.registry.api.RegistryPatientException;
import org.openhealthtools.openxds.registry.api.XdsRegistry;
import org.openhealthtools.openxds.registry.api.XdsRegistryLifeCycleService;
import org.openhealthtools.openxds.registry.api.XdsRegistryPatientService;

import proto.notification.wsa.EndpointReference;


public class SubmitObjectsRequest extends XdsCommon {
	boolean submit_raw = false;
	ContentValidationService validater;
	short xds_version;
	private final static Log logger = LogFactory.getLog(SubmitObjectsRequest.class);
	private XdsRegistry actor = null;
	static ArrayList<String> sourceIds = null;
	String clientIPAddress;
	/* The IHE Audit Trail for this actor. */
	private IheAuditTrail auditLog = null;

	public void setClientIPAddress(String addr) {
		clientIPAddress = addr;
	}
	
	boolean asBoolean(String value) {
		if (value == null) return false;
		if (value.equals("true")) return true;
		return false;
	}
	
	boolean returnTestLogId() {
		//return asBoolean(properties.getString("returnTestLogId"));
		return false;
	}
	
	public SubmitObjectsRequest(LogMessage log_message, short xds_version, MessageContext messageContext) {
		this.log_message = log_message;
		this.messageContext = messageContext;
		this.xds_version = xds_version;
		this.clientIPAddress = null;
		transaction_type = R_transaction;

		try {
			actor = XdsFactory.getRegistryActor(); 
			if (actor == null) {
				throw new XdsInternalException("Cannot find XdsRegistry actor configuration.");			
			}
			
			auditLog = (IheAuditTrail)actor.getAuditTrail();	
			init(new RegistryResponse( (xds_version == xds_a) ?	Response.version_2 : Response.version_3), xds_version, messageContext);
			
			loadSourceIds();
		} catch (XdsInternalException e) {
            logger.fatal(logger_exception_details(e));
		} 
	}

	public SubmitObjectsRequest() {
		// TODO Auto-generated constructor stub
	}

	void loadSourceIds() throws XdsInternalException {
		if (sourceIds != null) return;
		String[] sids = PropertyFacade.getStringArray(XdsConstants.DOC_SOURCE_IDS);
		if (sids == null || sids.length == 0)
			throw new XdsInternalException("Registry: sourceIds not configured");
		
		sourceIds = new ArrayList<String>();
		for (int i=0; i<sids.length; i++) {
			sourceIds.add(sids[i]);
		}
	}

	public void setContentValidationService(ContentValidationService validater) {
		this.validater = validater;
	}

	public OMElement submitObjectsRequest(OMElement sor) {
		this.validater = validater;

		if (logger.isDebugEnabled()) {
			logger.debug("Request from the Repository:");
			logger.debug(sor.toString());
		}
		try {
			sor.build();

			SubmitObjectsRequestInternal(sor);

		} 
		catch (XdsFormatException e) {
			response.add_error("XDSRegistryError", "SOAP Format Error: " + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
		} 
		catch (XdsNonIdenticalHashException e) {
			response.add_error("XDSNonIdenticalHash", e.getMessage(), RegistryUtility.exception_trace(e), log_message);
		} 
		catch (XdsDeprecatedException e) {
			response.add_error("XDSRegistryDeprecatedDocumentError", "XDS Deprecated Document Error:\n " + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
			logger.warn(logger_exception_details(e));
		} 
		catch (XdsUnknownPatientIdException e) {
			response.add_error("XDSUnknownPatientId", "XDS Unknown Patient Id:\n " + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
			logger.warn(logger_exception_details(e));
		} 
		catch (XdsPatientIdDoesNotMatchException e) {
			response.add_error("XDSPatientIdDoesNotMatch", "Patient ID does not match:\n " + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
			logger.warn(logger_exception_details(e));
		} 
		catch (XdsInternalException e) {
			response.add_error("XDSRegistryError", "XDS Internal Error:\n " + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
			logger.fatal(RegistryUtility.exception_trace(e));
		} 
		catch (MetadataValidationException e) {
			response.add_error("XDSRegistryMetadataError", "Metadata Validation Errors:\n " + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
		} 
		catch (LoggerException e) {
			response.add_error("XDSRegistryError", "Internal Logging error: LoggerException: " + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
			logger.fatal(logger_exception_details(e));
		} 
		catch (SchemaValidationException e) {
			response.add_error("XDSRegistryMetadataError", "Schema Validation Errors:\n" + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
		} 
		catch (XdsException e) {
			response.add_error("XDSRegistryError", "Exception:\n " + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
			logger.warn(logger_exception_details(e));
		} 
		catch (TransformerConfigurationException e) {
			response.add_error("XDSRegistryError", "Internal Error: Transformer Configuration Error: " + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
			logger.fatal(logger_exception_details(e));
		} 
		catch (SQLException e) {
			response.add_error("XDSRegistryError", "Internal Logging error: SQLException: " + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
			logger.fatal(logger_exception_details(e));
		} 
		catch (Exception e) {
			response.add_error("XDSRegistryError", "XDS General Error: " + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
			logger.fatal(logger_exception_details(e));
		} 

		log_response();


		OMElement res = null;
		try {
			res =  response.getResponse();			
			if (logger.isDebugEnabled()) {
				logger.debug("Response from the Registry");
				logger.debug(res.toString());
			}
		} catch (XdsInternalException e) {

		}
		
		// Notify document submission to subscribers
		if (!response.has_errors()) {
			//todo: remove hardcoded url
//			EndpointReference endpoint = new EndpointReference("http://localhost:8885/opendsub/services/NotificationBroker");
//			EndpointReference producerEndpoint = new EndpointReference("http://localhost:8010/openxds/services/DocumentRegistry");
//			NotificationProducer producer = new DocumentMetadataProducer(endpoint, );
//			LocalDsubPublisher publisher = new LocalDsubPublisher(endpoint); 

			//Document Metadata Publish
			Publisher.getInstance().publish(sor, actor);
		}
		
		// return test log message id only if request from internal Repository
		if (returnTestLogId() && "127.0.0.1".equals(clientIPAddress)) {
			logger.info("Adding testLogId");
			res.addAttribute("testLogId", log_message.getMessageID(), null);
		}
		if (logger.isInfoEnabled()){
			logger.info("response is " + res.toString());
		}
		return res;

	}

	void SubmitObjectsRequestInternal(OMElement sor) 
	throws SQLException, SchemaValidationException, MetadataValidationException, XdsInternalException, TransformerConfigurationException,
	LoggerException, MetadataValidationException, XdsException {
		boolean status;

		//String sor_string = sor.toString();

		if (submit_raw ) {
			status = submit_to_backend_registry(sor.toString());
			return;
		}

		if (xds_version == xds_b)
			RegistryUtility.schema_validate_local(sor, MetadataTypes.METADATA_TYPE_Rb);
		else
			RegistryUtility.schema_validate_local(sor, MetadataTypes.METADATA_TYPE_R);


		//			try {
		Metadata m = new Metadata(sor);

		StoredQuerySupport sqs = new StoredQuerySupport(response, log_message);
		RegistryObjectValidator rov = Registry.getRegistryObjectValidator(sqs);


		generateAuditLog(m);

		logIds(m);

		Validator val = new Validator(m, response.registryErrorList, true, xds_version == xds_b, log_message, false, actor.getActorDescription());
		val.run();

		RegistryValidations vals = null;
		if (PropertyFacade.getBoolean("validate.metadata"))
			vals = Registry.getRegistryValidations(response, log_message);

		if (vals != null)
			vals.validateProperUids(m);

		if (response.has_errors())
			logger.error("metadata validator failed");

		if (response.has_errors())
			return;

		if (this.validater != null && !this.validater.runContentValidationService(m, response))
			return;


		String patient_id = m.getSubmissionSetPatientId();
		log_message.addOtherParam("Patient ID", patient_id);

		validate_patient_id(patient_id);


		if (vals != null)
			validateSourceId(m);



		// check for references to registry contents
		List<String> referenced_objects = m.getIdsOfReferencedObjects();
		if (referenced_objects.size() > 0 && vals != null) {
			List<String> missing = vals.validateApproved(referenced_objects);
			if (missing != null) 
				throw new XdsDeprecatedException("The following registry objects were referenced by this submission but are not present, as Approved documents, in the registry: " +
						missing);

			// make allowance for by reference inclusion
			missing = rov.validateSamePatientId(m.getReferencedObjectsThatMustHaveSamePatientId(), patient_id);
			if (missing != null) 
				throw new XdsPatientIdDoesNotMatchException("The following registry objects were referenced by this submission but do not reference the same patient ID: " +
						missing);

		}
		
		//Get SSUID before UUID allocation  
		String ssUid = m.getSubmissionSetUniqueId();

		// allocate uuids for symbolic ids
		IdParser ra = new IdParser(m);
		ra.compileSymbolicNamesIntoUuids();

		// check that submission does not include any object ids that are already in registry
		List<String> ids_in_submission = m.getAllDefinedIds();
		List<String> ids_already_in_registry = rov.validateNotExists(ids_in_submission);
		if ( ids_already_in_registry.size() != 0) 
			response.add_error(MetadataSupport.XDSRegistryMetadataError, 
					"The following UUIDs which are present in the submission are already present in registry: " + ids_already_in_registry,
					"SubmitObjectsRequest.java", 
					log_message);

		// Set XDSFolder.lastUpdateTime
		// this will set time on empty folders too.  This is not required nor forbidden by XDS
		if (m.getFolders().size() != 0) {
			String timestamp = new Hl7Date().now();
			for (OMElement fol : m.getFolders()) {
				m.setSlot(fol, "lastUpdateTime", timestamp);
			}
		}

		// If this submission includes a DocumentEntry replace and the original DocumentEntry is in a folder
		// then the replacement document must be put into the folder as well.  This must happen here
		// so the following logic to update folder lastUpdateTime can be triggered.

		addRPLCToFolder(m);


		// if this submission adds a document to a folder then update that folder's lastUpdateTime Slot
		updateFolderTimes(m);

		// submit to backend registry
		String to_backend = m.getV3SubmitObjectsRequest().toString();

		log_message.addOtherParam("From Registry Adaptor", to_backend);

		status = submit_to_backend_registry(to_backend);
		if (!status) {
			return;
		}

		auditLog(patient_id, ssUid, AuditTypeCodes.RegisterDocumentSet_b);

		// Approve
		List<String> approvable_object_ids = ra.approvable_object_ids(m);

		if (approvable_object_ids.size() > 0) {

			OMElement approve = ra.getApproveObjectsRequest(approvable_object_ids);

			log_message.addOtherParam("Approve", approve.toString());

			submit_to_backend_registry(approve.toString());
		}

		// Deprecate
		List<String> deprecatable_object_ids = m.getObjectIdsToDeprecate();
		// add to the list of things to deprecate, any XFRM or APND documents hanging off documents
		// in the deprecatable_object_ids list
		deprecatable_object_ids.addAll(rov.getXFRMandAPNDDocuments(deprecatable_object_ids));

		if (deprecatable_object_ids.size() > 0) {

			// validate that these are documents first
			List<String> missing = rov.validateDocuments(deprecatable_object_ids);
			if (missing != null) 
				throw new XdsException("The following documents were referenced by this submission but are not present in the registry: " +
						missing);


			OMElement deprecate = ra.getDeprecateObjectsRequest(deprecatable_object_ids);

			log_message.addOtherParam("Deprecate", deprecate.toString());

			submit_to_backend_registry(deprecate.toString());
		}

		log_response();


		//			} 
		//			catch (MetadataException e) {
		//				response.add_error("XDSRegistryError", e.getMessage(), RegistryUtility.exception_details(e), log_message);
		//				return;
		//			}
		//			catch (ParserConfigurationException e) {
		//			response.add_error("XDSRegistryError", e.getMessage(), exception_details(e), log_message);
		//			return;
		//			}
		//			catch (ATNAException e) {
		//			response.add_error("XDSRegistryError", e.getMessage(), exception_details(e), log_message);
		//			return;
		//			}



	}

	// this applies only to folders in the registry.  folders in the submission are handled separately
	private void updateFolderTimes(Metadata m) throws MetadataException,
	LoggerException, XdsException, XdsInternalException,
	XMLParserException, MetadataValidationException {
		log_message.addOtherParam("start update folder","");
		for (OMElement assoc : m.getAssociations()) {
			log_message.addOtherParam("assoc type is ", m.getSimpleAssocType(assoc));
			if ( !m.getSimpleAssocType(assoc).equals("HasMember")) 
				continue;
			String sourceId = m.getAssocSource(assoc);
			log_message.addOtherParam("sourceid  ", sourceId);
			if (m.getSubmissionSetId().equals(sourceId))
				continue;  // sourceObject is SS
			if (m.getFolderIds().contains(sourceId))
				continue;  // sourceObject is folder in submission - handled elsewhere

			// sourceObject must be folder in registry, no other possibilities
			log_message.addOtherParam("Adding to Registry Folder ", sourceId);
			Metadata fm = fetchFolderbyId(sourceId);
			if (fm.getFolders().size() == 0)
				throw new XdsException("Adding to folder, sourceObject, " + sourceId + ", is not a folder in submission or in Registry");
			// Set XDSFolder.lastUpdateTime
			String timestamp = new Hl7Date().now();
			for (OMElement fol : fm.getFolders()) {
				fm.setSlot(fol, "lastUpdateTime", timestamp);
			}

			m.addMetadata(fm);   // add to metadata collection, will get submitted back along with new metadata

		}
	}

	private Metadata fetchFolderbyId(String sourceId)
	throws XMLParserException, LoggerException, XdsInternalException,
	MetadataException, MetadataValidationException {
		BackendRegistry reg = new BackendRegistry(response,log_message);
		OMElement res = reg.basic_query("SELECT * from RegistryPackage rp WHERE rp.id='" + sourceId + "'", 
				true /* leaf_class */);

		Metadata fm = MetadataParser.parseNonSubmission(res);
		return fm;
	}

	private boolean folderInRegistry(String sourceId) throws LoggerException, XdsException {
		StoredQuerySupport sqs = new StoredQuerySupport(response, log_message);
		RegistryObjectValidator rov = Registry.getRegistryObjectValidator(sqs);
		List<String> ids = new ArrayList<String>();
		ids.add(sourceId);
		List<String> arenot =  rov.validateAreFolders(ids);
		return ! arenot.contains(sourceId);
	}

	private boolean folderInSubmission(String sourceId) throws LoggerException,
	XdsException, XdsInternalException {
		return new Structure(new Metadata(), false).isFolder(sourceId);
	}

	private void addRPLCToFolder(Metadata m) throws MetadataException,
	LoggerException, XdsException {
		Map<String, String> rplcToOrigIds = new HashMap<String, String>();
		for (OMElement assoc : m.getAssociations()) {
			if (m.getSimpleAssocType(assoc).equals("RPLC")) {
				rplcToOrigIds.put(m.getAssocSource(assoc), m.getAssocTarget(assoc));
			}
		}

		log_message.addOtherParam("RPLC assocs", rplcToOrigIds.toString());
		for (String replacementDocumentId : rplcToOrigIds.keySet()) {
			String originalDocumentId = rplcToOrigIds.get(replacementDocumentId);
			// for each original document, find the collection of folders it belongs to
			Metadata me = new SQFactory(this).findFoldersForDocumentByUuid(originalDocumentId, false /*LeafClass*/);
			List<String> folderIds = me.getObjectIds(me.getObjectRefs());
			log_message.addOtherParam("RPLC containing folders=", folderIds.toString());
			// for each folder, add an association placing replacement in that folder
			for (String fid : folderIds) {
				OMElement assoc = m.add_association(m.mkAssociation("HasMember", fid, replacementDocumentId));
				m.add_association(m.mkAssociation("HasMember", m.getSubmissionSetId(), assoc.getAttributeValue(MetadataSupport.id_qname)));
			}
		}
	}

	private void logIds(Metadata m) throws LoggerException, MetadataException {
		log_message.addOtherParam("SSuid", m.getSubmissionSetUniqueId());

		List<String> doc_uids = new ArrayList<String>();
		for (String id : m.getExtrinsicObjectIds()) {
			String uid = m.getUniqueIdValue(id);
			if (uid != null && !uid.equals(""))
				doc_uids.add(uid);
		}
		log_message.addOtherParam("DOCuids", doc_uids.toString());

		List<String> fol_uids = new ArrayList<String>();
		for (String id : m.getFolderIds()) {
			String uid = m.getUniqueIdValue(id);
			if (uid != null && !uid.equals(""))
				fol_uids.add(uid);
		}
		log_message.addOtherParam("FOLuids", fol_uids.toString());
		log_message.addOtherParam("Structure", m.structure());
	}

	private void validateSourceId(Metadata m) throws MetadataException,
	MetadataValidationException {
		String sourceId = m.getExternalIdentifierValue(m.id(m.getSubmissionSet()), MetadataSupport.XDSSubmissionSet_sourceid_uuid);
		boolean sourceIdValid = false;
		for (int i=0; i<sourceIds.size(); i++) {
			if (sourceId.startsWith(sourceIds.get(i)))
				sourceIdValid = true;
		}
		if ( !sourceIdValid)
			throw new MetadataValidationException("sourceId " + sourceId + " is not acceptable to this Document Registry." + 
					" It must have one of these prefixes: " + sourceIds.toString());
	}

	private void validate_patient_id(String patient_id) throws SQLException,
	XdsException, XdsInternalException {
		if (PropertyFacade.getBoolean("validate.patient.id")) {
			try {
				XdsRegistryPatientService patientMan = XdsFactory.getXdsRegistryPatientService();
				PatientIdentifier pid = getPatientIdentifier(patient_id);
				boolean known_patient_id = patientMan.isValidPatient(pid, null);
				if ( !known_patient_id)
				{
					String aa = "unknown";
					if (pid.getAssigningAuthority() != null)
					{
						aa = "'" + pid.getAssigningAuthority().getAuthorityNameString() + "'";
					}
					throw new XdsUnknownPatientIdException("PatientId " + patient_id + " (Assigning Authority " + aa + ") is not known to the Registry");
				}
			} 
			catch (RegistryPatientException e) {
				throw new XdsInternalException("RegistryPatientException: " + e.getMessage(), e);
			}
		}
	}

	/**
	 * Converts a patientId from CX format to an {@link PatientIdentifier} object.
	 * 
	 * @param patientId the patient id, exp. 12321^^^&1.3.6.1.4.1.21367.2009.1.2.300&ISO
	 * @return the {@link PatientIdentifier}
	 */
	private PatientIdentifier getPatientIdentifier(String patientId){
   		String patId = HL7.getIdFromCX(patientId);
   	
    	Identifier assigningAuthority = HL7.getAssigningAuthorityFromCX(patientId);
    	Identifier aa = AssigningAuthorityUtil.reconcileIdentifier(assigningAuthority, actor.getActorDescription());

    	PatientIdentifier pid = new PatientIdentifier();
    	pid.setId(patId);
    	pid.setAssigningAuthority(aa);
    	return pid;
    }
   	
	//	private void validate_assigning_authority(String patient_id, Validator val)
	//			throws XdsException {
	//		int ups = patient_id.indexOf("^^^");
	//		if (ups == -1)
	//			throw new XdsException("Cannot parse patient id " + patient_id);
	//		String assigning_authority = patient_id.substring(ups + 3);
	////		String config_assigning_authority = Properties.loader().getString("assigning_authority");
	//		String config_assigning_authority = val.getAssigningAuthority();
	//		if (config_assigning_authority != null)
	//			if ( !config_assigning_authority.equals(assigning_authority)) 
	//				throw new XdsException("Unknown Patient ID Assigning Authority " + 
	//						assigning_authority + " found in submission " +
	//						" this Affinity Domain requires " + config_assigning_authority);
	//	}

	private boolean submit_to_backend_registry(String sor_string) throws XdsInternalException {
		boolean status = true;

		XdsRegistryLifeCycleService lcm = XdsFactory.getXdsRegistryLifeCycleService();
		OMElement result = null;
		try {
			OMElement request = OMUtil.xmlStringToOM(sor_string);
			if(request.getLocalName().equalsIgnoreCase("SubmitObjectsRequest")){
				result = lcm.submitObjects(request, new RegistryLifeCycleContext());
			}else if(request.getLocalName().equalsIgnoreCase("ApproveObjectsRequest")){
				result = lcm.approveObjects(request, new RegistryLifeCycleContext());	
			}else if(request.getLocalName().equalsIgnoreCase("DeprecateObjectsRequest")){
				result = lcm.deprecateObjects(request, new RegistryLifeCycleContext());
			}
		}catch(XMLStreamException e) {
			response.add_error("XDSRegistryError", e.getMessage(), RegistryUtility.exception_details(e),log_message);
			status = false;
		}catch(RegistryLifeCycleException e) {
			response.add_error("XDSRegistryError", e.getMessage(), RegistryUtility.exception_details(e),log_message);
			status = false;			
		}
		
		if (!status) {
			return status;
		}
		
		String statusCode = result.getAttributeValue(MetadataSupport.status_qname);
		if (!statusCode.equals(MetadataSupport.response_status_type_namespace + "Success")) {
			OMElement errorList = MetadataSupport.firstChildWithLocalName(result, "RegistryErrorList") ;
			response.addRegistryErrorList(errorList, log_message);
			status = false;
		}

		return status;
	}

	public void setSubmitRaw(boolean val) {
		submit_raw = val;
	}

	/**
	 * Audit Logging of Register Document Set message.
	 */
	private void auditLog(String patientId, String submissionSetUid, AuditCodeMappings.AuditTypeCodes typeCode) {
		if (auditLog == null)
			return;
		String replyto = getMessageContext().getReplyTo().getAddress();
		String remoteIP = (String)getMessageContext().getProperty(MessageContext.REMOTE_ADDR);
		String localIP = (String)getMessageContext().getProperty(MessageContext.TRANSPORT_ADDR);
		
		ParticipantObject set = new ParticipantObject("SubmissionSet", submissionSetUid);
		ParticipantObject patientObj = new ParticipantObject("PatientIdentifier", patientId);
		
		ActiveParticipant source = new ActiveParticipant();
		source.setUserId(replyto);
		source.setAccessPointId(remoteIP);
		String userid = actor.getServiceEndpoint(isHttps()); 
		ActiveParticipant dest = new ActiveParticipant();
		dest.setUserId(userid);
		// the Alternative User ID should be set to our Process ID, see
		// section TF-2b section 3.42.7.1.2
		dest.setAltUserId(PidHelper.getPid());
		
		dest.setAccessPointId(localIP);
		auditLog.logDocumentImport(source, dest, patientObj, set, typeCode);		
	}


}
