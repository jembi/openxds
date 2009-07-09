package gov.nist.registry.ws;

import gov.nist.registry.common2.MetadataTypes;
import gov.nist.registry.common2.datatypes.Hl7Date;
import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.SchemaValidationException;
import gov.nist.registry.common2.exception.XdsDeprecatedException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsFormatException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.exception.XdsPatientIdDoesNotMatchException;
import gov.nist.registry.common2.exception.XdsUnknownPatientIdException;
import gov.nist.registry.common2.registry.BackendRegistry;
import gov.nist.registry.common2.registry.IdParser;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataParser;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.Properties;
import gov.nist.registry.common2.registry.RegistryObjectValidator;
import gov.nist.registry.common2.registry.RegistryResponse;
import gov.nist.registry.common2.registry.RegistryUtility;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.XdsCommon;
import gov.nist.registry.common2.registry.validation.Structure;
import gov.nist.registry.common2.registry.validation.Validator;
import gov.nist.registry.ws.sq.SQFactory;
import gov.nist.registry.xdslog.LoggerException;
import gov.nist.registry.xdslog.Message;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerConfigurationException;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.MessageContext;
import org.apache.log4j.Logger;
import org.openhealthexchange.common.utils.OMUtil;
import org.openhealthexchange.common.ws.server.IheHTTPServer;
import org.openhealthexchange.openpixpdq.data.PatientIdentifier;
import org.openhealthexchange.openxds.configuration.ModuleManager;
import org.openhealthexchange.openxds.registry.api.IXdsRegistryLifeCycleManager;
import org.openhealthexchange.openxds.registry.api.IXdsRegistryPatientManager;
import org.openhealthexchange.openxds.registry.api.RegistryLifeCycleContext;
import org.openhealthexchange.openxds.registry.api.RegistryLifeCycleException;
import org.openhealthexchange.openxds.registry.api.RegistryPatientException;

import com.misyshealthcare.connect.net.IConnectionDescription;

public class SubmitObjectsRequest extends XdsCommon {
	boolean submit_raw = false;
	ContentValidationService validater;
	short xds_version;
	private final static Logger logger = Logger.getLogger(SubmitObjectsRequest.class);
	private IConnectionDescription connection = null;
	static ArrayList<String> sourceIds = null;
	private static String hasMember = "urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember";

	public SubmitObjectsRequest(Message log_message, short xds_version, MessageContext messageContext) {
		this.log_message = log_message;
		this.xds_version = xds_version;
		try {
			IheHTTPServer httpServer = (IheHTTPServer)messageContext.getTransportIn().getReceiver();
			connection = httpServer.getConnection();
			if (connection == null) {
				throw new XdsInternalException("Cannot find XdsRegistry connection configuration.");			
			}
			init(new RegistryResponse( (xds_version == xds_a) ?	Response.version_2 : Response.version_3), xds_version, messageContext);
			loadSourceIds();
		} catch (XdsInternalException e) {
			logger.fatal(logger_exception_details(e));
		}
	}
	
	void loadSourceIds() throws XdsInternalException {
		if (sourceIds != null) return;
		String sids = connection.getProperty("sourceIds");
		if (sids == null || sids.equals(""))
			throw new XdsInternalException("Registry: sourceIds not configured");
		String[] parts = sids.split(",");
		sourceIds = new ArrayList<String>();
		for (int i=0; i<parts.length; i++) {
			sourceIds.add(parts[i].trim());
		}
	}

	public OMElement submitObjectsRequest(OMElement sor, ContentValidationService validater) {
		this.validater = validater;

		try {
			sor.build();
			
			mustBeSimpleSoap();

			SubmitObjectsRequestInternal(sor);

		} 
		catch (XdsFormatException e) {
			response.add_error("XDSRegistryError", "SOAP Format Error: " + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
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
			response.add_error("XDSRegistryError", "XDS General Error:\n " + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
			logger.fatal(logger_exception_details(e));
		} 

		this.log_response();


		OMElement res = null;
		try {
			res =  response.getResponse();
		} catch (XdsInternalException e) {

		}
		return res;

	}

	void SubmitObjectsRequestInternal(OMElement sor) 
	throws SQLException, SchemaValidationException, MetadataValidationException, XdsInternalException, TransformerConfigurationException,
	LoggerException, MetadataValidationException, XdsException {
		boolean status;

		String sor_string = sor.toString();
		
		if (submit_raw ) {
			status = submit_to_backend_registry(sor.toString());
		} else {

			if (xds_version == xds_b)
				RegistryUtility.schema_validate_local(sor, MetadataTypes.METADATA_TYPE_Rb);
			else
				RegistryUtility.schema_validate_local(sor, MetadataTypes.METADATA_TYPE_R);


			try {
				Metadata m = new Metadata(sor);
				
				generateAuditLog(m);

				log_message.addOtherParam("SSuid", m.getSubmissionSetUniqueId());
				
				ArrayList<String> doc_uids = new ArrayList<String>();
				for (String id : m.getExtrinsicObjectIds()) {
					String uid = m.getUniqueIdValue(id);
					if (uid != null && !uid.equals(""))
						doc_uids.add(uid);
				}
				log_message.addOtherParam("DOCuids", doc_uids.toString());
				
				ArrayList<String> fol_uids = new ArrayList<String>();
				for (String id : m.getFolderIds()) {
					String uid = m.getUniqueIdValue(id);
					if (uid != null && !uid.equals(""))
						fol_uids.add(uid);
				}
				log_message.addOtherParam("FOLuids", fol_uids.toString());
				log_message.addOtherParam("Structure", m.structure());

				Validator val = new Validator(m, response.registryErrorList, true, xds_version == xds_b, log_message, connection);
				val.run();
				
				RegistryObjectValidator rov = new RegistryObjectValidator(response, log_message);
				rov.validateProperUids(m);

				if (response.has_errors())
					System.out.println("metadata validator failed");

				if (response.has_errors())
					return;

				if (this.validater != null && !this.validater.runContentValidationService(m, response))
					return;


				String patient_id = m.getSubmissionSetPatientId();
				log_message.addOtherParam("Patient ID", patient_id);

				validate_patient_id(patient_id);
				
				
				validateSourceId(m);
				
				
				
				// check for references to registry contents
				ArrayList referenced_objects = m.getReferencedObjects();
				if (referenced_objects.size() > 0) {
					ArrayList missing = rov.validateApproved(referenced_objects);
					if (missing != null) 
						throw new XdsDeprecatedException("The following registry objects were referenced by this submission but are not present, as Approved documents, in the registry: " +
								missing);

					// make allowance for by reference inclusion
					missing = rov.validateSamePatientId(m.getReferencedObjectsThatMustHaveSamePatientId(), patient_id);
					if (missing != null) 
						throw new XdsPatientIdDoesNotMatchException("The following registry objects were referenced by this submission but do not reference the same patient ID: " +
								missing);

				}

				// allocate uuids for symbolic ids
				IdParser ra = new IdParser(m);

				ra.compileSymbolicNamesIntoUuids();
				
				// check that submission does not include any object ids that are already in registry
				ArrayList<String> ids_in_submission = m.getAllDefinedIds();
				RegistryObjectValidator roval = new RegistryObjectValidator(response, log_message);
				ArrayList<String> ids_already_in_registry = roval.validateNotExists(ids_in_submission);
				if ( ids_already_in_registry.size() != 0) 
					response.add_error(MetadataSupport.XDSRegistryMetadataError, 
							"The following UUIDs which are present in the submission are already present in registry: " + ids_already_in_registry,
							"SubmitObjectsRequest.java", 
							log_message);

				// Set XDSFolder.lastUpdateTime
				if (m.getFolders().size() != 0) {
					String timestamp = new Hl7Date().now();
					for (OMElement fol : m.getFolders()) {
						m.setSlot(fol, "lastUpdateTime", timestamp);
					}
				}
				
				// If this submission includes a DocumentEntry replace and the original DocumentEntry is in a folder
				// then the replacement document must be put into the folder as well.  This must happen here
				// so the following logic to update folder lastUpdateTime can be triggered.
				
				HashMap<String, String> rplcToOrigIds = new HashMap<String, String>();
				for (OMElement assoc : m.getAssociations()) {
					if (m.getSimpleAssocType(assoc).equals("RPLC")) {
						rplcToOrigIds.put(m.getAssocSource(assoc), m.getAssocTarget(assoc));
					}
				}
				
				for (String replacementDocumentId : rplcToOrigIds.keySet()) {
					String originalDocumentId = rplcToOrigIds.get(replacementDocumentId);
					// for each original document, find the collection of folders it belongs to
					Metadata me = new SQFactory(this, false).findFoldersForDocumentByUuid(originalDocumentId);
					ArrayList<String> folderIds = me.getObjectIds(me.getObjectRefs());
					// for each folder, add an association placing replacment in that folder
					// This brings up interesting question, should the Assoc between SS and Assoc be generated also?  YES!
					for (String fid : folderIds) {
						OMElement assoc = m.add_association(m.mkAssociation(hasMember, fid, replacementDocumentId));
						OMElement assoc2 = m.add_association(m.mkAssociation(hasMember, m.getSubmissionSetId(), assoc.getAttributeValue(MetadataSupport.id_qname)));
					}
				}
				
				
				
				
				// if this submission adds a document to a folder then update that folder's lastUpdateTime Slot
				ArrayList<String> registryPackagesToUpdate = new ArrayList<String>();
				for (OMElement assoc : m.getAssociations()) {
					if (m.getSimpleAssocType(assoc).equals(hasMember)) {
						String sourceId = m.getAssocSource(assoc);
						if ( !m.getSubmissionSetId().equals(sourceId) &&
								!m.getFolderIds().contains(sourceId)) {
							// Assoc src not part of the submission
							logger.info("Adding to Folder (1)" + sourceId);
							if (new Structure(new Metadata(), false).isFolder(sourceId)) {
								logger.info("Adding to Folder (2)" + sourceId);
								BackendRegistry reg = new BackendRegistry(response,log_message);
								OMElement res = reg.basic_query("SELECT * from RegistryPackage rp WHERE rp.id='" + sourceId + "'", 
										true /* leaf_class */);
								
								Metadata fm = MetadataParser.parseNonSubmission(res);
								// Set XDSFolder.lastUpdateTime
								if (fm.getFolders().size() != 0) {
									String timestamp = new Hl7Date().now();
									for (OMElement fol : fm.getFolders()) {
										fm.setSlot(fol, "lastUpdateTime", timestamp);
									}
								}
								
								// submit back to backend registry
								String to_backend = fm.getV3SubmitObjectsRequest().toString();

								log_message.addOtherParam("From Registry Adaptor", to_backend);

								status = submit_to_backend_registry(to_backend);
								if (!status) {
									return;
								}
								
							}
						}
					}
				}

				// submit to backend registry
				String to_backend = m.getV3SubmitObjectsRequest().toString();

				log_message.addOtherParam("From Registry Adaptor", to_backend);

				status = submit_to_backend_registry(to_backend);
				if (!status) {
					return;
				}

				// Approve
				ArrayList approvable_object_ids = ra.approvable_object_ids(m);

				if (approvable_object_ids.size() > 0) {

					OMElement approve = ra.getApproveObjectsRequest(approvable_object_ids);

					log_message.addOtherParam("Approve", approve.toString());

					//TODO: submit_to_backend_registry currently handles only submit, not approve and deprecate
					submit_to_backend_registry(approve.toString());
				}

				// Deprecate
				ArrayList deprecatable_object_ids = m.getObjectIdsToDeprecate();
				// add to the list of things to deprecate, any XFRM or APND documents hanging off documents
				// in the deprecatable_object_ids list
				deprecatable_object_ids.addAll(new RegistryObjectValidator(response, log_message).getXFRMandAPNDDocuments(deprecatable_object_ids));

				if (deprecatable_object_ids.size() > 0) {

					// validate that these are documents first
					ArrayList missing = rov.validateDocuments(deprecatable_object_ids);
					if (missing != null) 
						throw new XdsException("The following documents were referenced by this submission but are not present in the registry: " +
								missing);


					OMElement deprecate = ra.getDeprecateObjectsRequest(deprecatable_object_ids);

					log_message.addOtherParam("Deprecate", deprecate.toString());

					submit_to_backend_registry(deprecate.toString());
				}

				log_response();


			} 
			catch (MetadataException e) {
				response.add_error("XDSRegistryError", e.getMessage(), RegistryUtility.exception_details(e), log_message);
				return;
			}
//TODO: remove old exceptions			
//			catch (ParserConfigurationException e) {
//			response.add_error("XDSRegistryError", e.getMessage(), exception_details(e), log_message);
//			return;
//			}
//			catch (ATNAException e) {
//			response.add_error("XDSRegistryError", e.getMessage(), exception_details(e), log_message);
//			return;
//			}

		}


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
		if (Properties.loader().getBoolean("validate_patient_id")) {
			try {
				IXdsRegistryPatientManager patientMan = (IXdsRegistryPatientManager)ModuleManager.getInstance().getBean("registryPatientManager");

				//TODO:get the patient id
				PatientIdentifier pid = null; 
				//PatientIdentifier pid = new PatientIdentifier(patient_id);
				boolean known_patient_id = patientMan.isValidPatient(pid, null);
				if ( !known_patient_id)
					throw new XdsUnknownPatientIdException("PatientId " + patient_id + " is not known to the Registry");
			} 
			catch (RegistryPatientException e) {
				throw new XdsInternalException("RegistryPatientException: " + e.getMessage());
			}
		}
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

		IXdsRegistryLifeCycleManager lcm = (IXdsRegistryLifeCycleManager)ModuleManager.getInstance().getBean("registryLifeCycleManager");
		OMElement result = null;
		try {
			OMElement request = OMUtil.xmlStringToOM(sor_string);
			if(request.getLocalName().equalsIgnoreCase("SubmitObjectsRequest")){
				result = lcm.submitObjects(request, new RegistryLifeCycleContext());
			}else if(request.getLocalName().equalsIgnoreCase("ApproveObjectsRequest")){
				result = lcm.approveObjects(request, new RegistryLifeCycleContext());	
			}else if(request.getLocalName().equalsIgnoreCase("DeprecateObjectsRequest")){
				//TODO: Implementation is required
				//lcm.deprecateObjects(request, new RegistryLifeCycleContext());
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
		if (!statusCode.equals("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success")) {
			OMElement errorList = MetadataSupport.firstChildWithLocalName(result, "RegistryErrorList") ;
			response.addRegistryErrorList(errorList, log_message);
			status = false;
		}

		return status;
	}

	public void setSubmitRaw(boolean val) {
		submit_raw = val;
	}


}
