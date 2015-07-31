//package gov.nist.registry.ws.sq;
//
//import gov.nist.registry.common2.exception.MetadataValidationException;
//import gov.nist.registry.common2.exception.XDSRegistryOutOfResourcesException;
//import gov.nist.registry.common2.exception.XdsException;
//import gov.nist.registry.common2.exception.XdsInternalException;
//import gov.nist.registry.common2.logging.LogMessage;
//import gov.nist.registry.common2.logging.LoggerException;
//import gov.nist.registry.common2.registry.Metadata;
//import gov.nist.registry.common2.registry.MetadataParser;
//import gov.nist.registry.common2.registry.Response;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import org.apache.axiom.om.OMElement;
//import org.freebxml.omar.common.exceptions.UnimplementedException;
//
//public class GetAll extends StoredQuery {
//
//	public GetAll(HashMap params, boolean return_objects, Response response, LogMessage log_message, boolean is_secure) {
//		super(params, return_objects, response, log_message,  is_secure);
//
//		//                         param name,                             required?, multiple?, is string?,   same size as,    alternative
//		validate_parm(params, "$patientId",         		                 true,      false,     true,         null,            null);
//		validate_parm(params, "$XDSDocumentEntryStatus",                     true,      true,      true,         null,            "$XDSDocumentEntryStatus");
//		validate_parm(params, "$XDSSubmissionSetStatus",                     true,      true,      true,         null,            "$XDSSubmissionSetStatus");
//		validate_parm(params, "$XDSFolderStatus",                     		 true,      true,      true,         null,            "$XDSFolderStatus");
//		validate_parm(params, "$XDSDocumentEntryFormatCode",                 false,     true,      true,         null,            null);
//		validate_parm(params, "$XDSDocumentEntryConfidentialityCode",        false,     true,      true,         null,            null);
//	}
//	
//	@Override
//	public Metadata run_internal() throws XdsException, LoggerException, XDSRegistryOutOfResourcesException {
//		Metadata metadata ;
//		
//		String patientId 			   = get_string_parm("$patientId");
//		ArrayList<String> docStatus    = get_arraylist_parm("$XDSDocumentEntryStatus");
//		ArrayList<String> setStatus    = get_arraylist_parm("$XDSSubmissionSetStatus");
//		ArrayList<String> folStatus    = get_arraylist_parm("$XDSFolderStatus");
//		ArrayList<String> confCodes    = this.get_arraylist_parm("$XDSDocumentEntryConfidentialityCode");
//		ArrayList<String> formatCodes  = this.get_arraylist_parm("$XDSDocumentEntryFormatCode");
//		
//		if (patientId != null && docStatus != null && setStatus != null && folStatus != null) {
//			
//			validateStatus(docStatus);
//			validateStatus(setStatus);
//			validateStatus(folStatus);
//			
//			OMElement ele = getDocByPatientId(patientId, docStatus, confCodes, formatCodes );
//			metadata = MetadataParser.parseNonSubmission(ele);
//			
//			
//			OMElement fols = getFolOrSetByPatientId(patientId, setStatus, folStatus);
//			metadata.addMetadata(fols);
//			
//			OMElement asso = getAssociationByPatientId(patientId, docStatus, setStatus, folStatus);
//			metadata.addMetadata(asso);
//			
//			// some document may have been filtered out, remove the unnecessary Associations
//			ArrayList<String> content_ids = new ArrayList<String>();
//			content_ids.addAll(metadata.getSubmissionSetIds());
//			content_ids.addAll(metadata.getExtrinsicObjectIds());
//			content_ids.addAll(metadata.getFolderIds());
//
//			// add in Associations that link the above parts
//			content_ids.addAll(metadata.getIds(metadata.getAssociationsInclusive(content_ids)));
//
//			// Assocs can link to Assocs to so repeat
//			content_ids.addAll(metadata.getIds(metadata.getAssociationsInclusive(content_ids)));
//
//			metadata.filter(content_ids);
//
//			return metadata;
//		}
//		else throw new XdsInternalException("Required Fields are null");
//			
//	}
//	
//	private void validateStatus(ArrayList<String> status) throws MetadataValidationException{
//	String status_ns_prefix = "urn:oasis:names:tc:ebxml-regrep:StatusType:";
//
////	ArrayList new_status = new ArrayList();
//	for (int i=0; i<status.size(); i++) {
//		String stat = (String) status.get(i);
//
//		if ( ! stat.startsWith(status_ns_prefix)) 
//			throw new MetadataValidationException("Status parameter must have namespace prefix " + status_ns_prefix + " found " + stat);
//	}
//   }
//
//  public Metadata runSpecific() throws XdsException, LoggerException, XDSRegistryOutOfResourcesException {
//	  throw new UnsupportedOperationException("The runSpecific is no implemented.");	   
//  }
//
//}
