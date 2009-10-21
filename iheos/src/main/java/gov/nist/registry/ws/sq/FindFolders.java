package gov.nist.registry.ws.sq;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataParser;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.StoredQuery;
import gov.nist.registry.xdslog.LoggerException;
import gov.nist.registry.xdslog.Message;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.axiom.om.OMElement;

public class FindFolders extends StoredQuery {

	public FindFolders(HashMap<String, Object> params, boolean return_objects, Response response, Message log_message, boolean is_secure) {
		super(params, return_objects, response, log_message,  is_secure);


		//                    param name,                      required?, multiple?, is string?,   same size as,                        alternative
		validate_parm(params, "$XDSFolderPatientId",             true,      false,     true,         null,                                  null);
		validate_parm(params, "$XDSFolderLastUpdateTimeFrom",    false,     false,     false,        null,                                  null);
		validate_parm(params, "$XDSFolderLastUpdateTimeTo",      false,     false,     false,        null,                                  null);
		validate_parm(params, "$XDSFolderCodeList",              false,     true,      true,         "$XDSFolderCodeListScheme",            null);
		validate_parm(params, "$XDSFolderCodeListScheme",        false,     true,      true,         "$XDSFolderCodeList",                  null);
		validate_parm(params, "$XDSFolderStatus",                true,      true,      true,         null,                                  null);
	}

	public Metadata run_internal() throws XdsException, LoggerException {
		OMElement results = impl();
		
		Metadata m = MetadataParser.parseNonSubmission(results);
		
		if (log_message != null)
			log_message.addOtherParam("Results structure", m.structure());

		return m;
	}
	
	
	OMElement impl() throws XdsInternalException, MetadataException, XdsException, LoggerException {
		String patient_id              = get_string_parm("$XDSFolderPatientId");
		String update_time_from        = get_int_parm("$XDSFolderLastUpdateTimeFrom");
		String update_time_to          = get_int_parm("$XDSFolderLastUpdateTimeTo");
		ArrayList<String> codes        = get_arraylist_parm("$XDSFolderCodeList");
		ArrayList<String> code_schemes = get_arraylist_parm("$XDSFolderCodeListScheme");
		ArrayList<String> status       = get_arraylist_parm("$XDSFolderStatus");

		if (patient_id == null || patient_id.length() == 0) throw new XdsException("Patient ID parameter empty");
		if (status.size() == 0) throw new XdsException("Status parameter empty");

		String status_ns_prefix = "urn:oasis:names:tc:ebxml-regrep:StatusType:";
		
		//ArrayList<String> new_status = new ArrayList<String>();
		for (int i=0; i<status.size(); i++) {
			String stat = (String) status.get(i);
			
			if ( ! stat.startsWith(status_ns_prefix)) 
				throw new MetadataValidationException("Status parameter must have namespace prefix " + status_ns_prefix + " found " + stat);
			//new_status.add(stat.replaceFirst(status_ns_prefix, ""));
		}
		//status = new_status;


		init();
		
		if (this.return_leaf_class) {
		     a("SELECT *  "); n();
		} else {
		     a("SELECT fol.id  "); n();
		}
        
		a("FROM RegistryPackage fol, ExternalIdentifier patId"); n();
        if (update_time_from != null)         a(", Slot updateTimef"); n();
        if (update_time_to != null)         a(", Slot updateTimet"); n();
        if (codes != null)                  a(", Classification code"); n();
        if (code_schemes != null)           a(", Slot codescheme"); n();           
                                 a("WHERE"); n();

                         		// patientID
        a("(fol.id = patId.registryobject AND	"); n();
        a("  patId.identificationScheme='urn:uuid:f64ffdf0-4b97-4e06-b79f-a52b38ec2f8a' AND "); n();
        a("  patId.value = '"); a(patient_id); a("' ) AND"); n();
        a("  fol.status IN "); a(status); n();
		add_times("lastUpdateTime",     "updateTimef",       "updateTimet",       update_time_from,      update_time_to, "fol");
        
        System.out.println(query.toString());

		return query(this.return_leaf_class);

	}
}
