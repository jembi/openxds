package gov.nist.registry.ws.sq.ebxmlrr21;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataParser;
import gov.nist.registry.common2.registry.storedquery.StoredQuerySupport;
import gov.nist.registry.ws.sq.FindFolders;

import java.util.ArrayList;

import org.openhealthtools.openxds.log.LoggerException;

/**
 * Implementation specific class for FindDocuments stored query. 
 * All the logic is in the runImplementation() method.
 * @author bill
 *
 */
public class EbXML21FindFolders extends FindFolders {

	EbXML21QuerySupport eb;

	/**
	 * Constructor
	 * @param sqs
	 * @throws MetadataValidationException
	 */
	public EbXML21FindFolders(StoredQuerySupport sqs) {
		super(sqs);
		eb = new EbXML21QuerySupport(sqs);
	}
	
	/**
	 * Main method, runs query logic
	 * @return Metadata
	 * @throws MetadataException
	 * @throws XdsException
	 */
	public Metadata runImplementation() throws MetadataException,
			XdsException, LoggerException {
		
		eb.init();


		if (patient_id == null || patient_id.length() == 0) throw new XdsException("Patient ID parameter empty");
		if (status.size() == 0) throw new XdsException("Status parameter empty");

		String status_ns_prefix = "urn:oasis:names:tc:ebxml-regrep:StatusType:";
		
//		ArrayList<String> new_status = new ArrayList<String>();
		for (int i=0; i<status.size(); i++) {
			String stat = (String) status.get(i);
			
			if ( ! stat.startsWith(status_ns_prefix)) 
				throw new MetadataValidationException("Status parameter must have namespace prefix " + status_ns_prefix + " found " + stat);
//			new_status.add(stat.replaceFirst(status_ns_prefix, ""));
		}
//		status = new_status;


		if (sqs.return_leaf_class) {
		     eb.a("SELECT *  "); eb.n();
		} else {
		     eb.a("SELECT fol.id  "); eb.n();
		}
        
		eb.a("FROM RegistryPackage fol, ExternalIdentifier patId"); eb.n();
        if (update_time_from != null)         eb.a(", Slot updateTimef"); eb.n();
        if (update_time_to != null)         eb.a(", Slot updateTimet"); eb.n();
        if (codes != null)                  eb.a(eb.declareClassifications(codes)); eb.n();
                                 eb.a("WHERE"); eb.n();

                         		// patientID
        eb.a("(fol.id = patId.registryobject AND	"); eb.n();
        eb.a("  patId.identificationScheme='urn:uuid:f64ffdf0-4b97-4e06-b79f-a52b38ec2f8a' AND "); eb.n();
        eb.a("  patId.value = '"); eb.a(patient_id); eb.a("' ) AND"); eb.n();
        eb.a("  fol.status IN "); eb.a(status); eb.n();
		eb.addTimes("lastUpdateTime",     "updateTimef",       "updateTimet",       update_time_from,      update_time_to, "fol");
		eb.addCode(codes, "fol");
        
		return MetadataParser.parseNonSubmission(eb.query(sqs.return_leaf_class));
	}

}
