package gov.nist.registry.ws.sq.ebxmlrr21;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataParser;
import gov.nist.registry.common2.registry.storedquery.StoredQuerySupport;
import gov.nist.registry.ws.sq.FindDocumentsForMultiplePatients;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.syslog.LoggerException;

/**
 * Implementation specific class for FindDocuments stored query. 
 * All the logic is in the runImplementation() method.
 * @author bill
 *
 */
public class EbXML21FindDocumentsForMultiplePatients extends FindDocumentsForMultiplePatients {
	private static final Log logger = LogFactory.getLog(EbXML21FindDocumentsForMultiplePatients.class);

	EbXML21QuerySupport eb;

	/**
	 * Constructor
	 * @param sqs
	 * @throws MetadataValidationException
	 */
	public EbXML21FindDocumentsForMultiplePatients(StoredQuerySupport sqs) throws MetadataValidationException {
		super(sqs);
		eb = new EbXML21QuerySupport(sqs);
	}

	/**
	 * Main method, runs query logic
	 * @return Metadata
	 * @throws MetadataException
	 * @throws XdsException
	 */
	public Metadata runImplementation() throws MetadataException, XdsException,
	LoggerException {
		
		eb.init();

		if (sqs.return_leaf_class) {
			eb.a("SELECT *  "); eb.n();
		} else {
			eb.a("SELECT doc.id  "); eb.n();
		}
		eb.a("FROM ExtrinsicObject doc");
		if (patient_id != null && patient_id.size() > 0) {
			eb.a(", ExternalIdentifier patId"); eb.n();
		}
		if (class_codes != null)                          eb.a(eb.declareClassifications(class_codes));
		if (type_codes != null)                          eb.a(eb.declareClassifications(type_codes));
		if (practice_setting_codes != null)               eb.a(eb.declareClassifications(practice_setting_codes));
		if (hcft_codes != null)                           eb.a(eb.declareClassifications(hcft_codes));  // $XDSDocumentEntryHealthcareFacilityTypeCode
		if (event_codes != null)                          eb.a(eb.declareClassifications(event_codes)); // $XDSDocumentEntryEventCodeList
		if (creation_time_from != null)                   eb.a(", Slot crTimef"); eb.n();                       // $XDSDocumentEntryCreationTimeFrom
		if (creation_time_to != null)                     eb.a(", Slot crTimet"); eb.n();                       // $XDSDocumentEntryCreationTimeTo
		if (service_start_time_from != null)              eb.a(", Slot serStartTimef"); eb.n();                 // $XDSDocumentEntryServiceStartTimeFrom
		if (service_start_time_to != null)                eb.a(", Slot serStartTimet"); eb.n();                 // $XDSDocumentEntryServiceStartTimeTo
		if (service_stop_time_from != null)               eb.a(", Slot serStopTimef"); eb.n();                  // $XDSDocumentEntryServiceStopTimeFrom
		if (service_stop_time_to != null)                 eb.a(", Slot serStopTimet"); eb.n();                  // $XDSDocumentEntryServiceStopTimeTo
		if (conf_codes != null)                           eb.a(eb.declareClassifications(conf_codes));  // $XDSDocumentEntryConfidentialityCode
		if (format_codes != null)                         eb.a(", Classification fmtCode"); eb.n();             // $XDSDocumentEntryFormatCode
		if (author_person != null)                        eb.a(", Classification author"); eb.n();
		if (author_person != null)                        eb.a(", Slot authorperson"); eb.n();


		eb.where(); eb.n();
		
		// patient id
		if (patient_id != null && patient_id.size() > 0) {
			eb.a("(doc.id = patId.registryobject AND	"); eb.n();
			eb.a("  patId.identificationScheme='urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427' AND "); eb.n();
			eb.a("  patId.value IN "); eb.a(patient_id); eb.a(" ) "); eb.n();
		}

		eb.addCode(class_codes);
		eb.addCode(type_codes);
		eb.addCode(practice_setting_codes);

		eb.addTimes("creationTime",     "crTimef",       "crTimet",       creation_time_from,      creation_time_to, "doc");
		eb.addTimes("serviceStartTime", "serStartTimef", "serStartTimet", service_start_time_from, service_start_time_to, "doc");
		eb.addTimes("serviceStopTime",  "serStopTimef",  "serStopTimet",  service_stop_time_from,  service_stop_time_to, "doc");

		eb.addCode(hcft_codes);
		eb.addCode(event_codes);
		eb.addCode(conf_codes);

		eb.addCode(format_codes);

		if (author_person != null) {
			for (String ap : author_person) {
				eb.and(); eb.n();
				eb.a("(doc.id = author.classifiedObject AND "); eb.n();
				eb.a("  author.classificationScheme='urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d' AND "); eb.n();
				eb.a("  authorperson.parent = author.id AND"); eb.n();
				eb.a("  authorperson.name = 'authorPerson' AND"); eb.n();
				eb.a("  authorperson.value LIKE '" + ap + "' )"); eb.n();
			}
		}

		eb.and(); eb.a(" doc.status IN "); eb.a(status);

		if (logger.isDebugEnabled()) {
			logger.debug(eb.getQuery().toString());
		}

		return MetadataParser.parseNonSubmission(eb.query(sqs.return_leaf_class));
	}


}
