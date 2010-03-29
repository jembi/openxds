package gov.nist.registry.ws.sq.ebxmlrr21;

import org.openhealthtools.openxds.log.LoggerException;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataParser;
import gov.nist.registry.common2.registry.storedquery.StoredQuerySupport;
import gov.nist.registry.ws.sq.FindSubmissionSets;

/**
 * Implementation specific class for FindDocuments stored query. 
 * All the logic is in the runImplementation() method.
 * @author bill
 *
 */
public class EbXML21FindSubmissionSets extends FindSubmissionSets {

	EbXML21QuerySupport eb;

	/**
	 * Constructor
	 * @param sqs
	 * @throws MetadataValidationException
	 */
	public EbXML21FindSubmissionSets(StoredQuerySupport sqs)
	throws MetadataValidationException {
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
		eb.a("FROM RegistryPackage doc, ExternalIdentifier patId"); eb.n();
		if (source_id != null)                            eb.a(", ExternalIdentifier srcId"); eb.n();
		if (submission_time_from != null)                 eb.a(", Slot sTimef"); eb.n();              
		if (submission_time_to != null)                   eb.a(", Slot sTimet"); eb.n();              
		if (author_person != null)                        eb.a(", Classification author"); eb.n();
		if (author_person != null)                        eb.a(", Slot authorperson"); eb.n();
		if (content_type != null)                         eb.a(eb.declareClassifications(content_type)); eb.n();


		eb.a("WHERE"); eb.n();
		// patientID
		eb.a("(doc.id = patId.registryobject AND	"); eb.n();
		eb.a("  patId.identificationScheme='urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446' AND "); eb.n();
		eb.a("  patId.value = '"); eb.a(patient_id); eb.a("' ) "); eb.n();

		if (source_id != null) {
			eb.a("AND"); eb.n();
			eb.a("(doc.id = srcId.registryobject AND	"); eb.n();
			eb.a("  srcId.identificationScheme='urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832' AND "); eb.n();
			eb.a("  srcId.value IN "); eb.a(source_id); eb.a(" ) "); eb.n();
		}
		eb.addTimes("submissionTime",     "sTimef",       "sTimet",       submission_time_from,      submission_time_to, "doc");

		if (author_person != null) {
			eb.a("AND"); eb.n();
			eb.a("(doc.id = author.classifiedObject AND "); eb.n();
			eb.a("  author.classificationScheme='urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d' AND "); eb.n();
			eb.a("  authorperson.parent = author.id AND"); eb.n();
			eb.a("  authorperson.name = 'authorPerson' AND"); eb.n();
			eb.a("  authorperson.value LIKE '" + author_person + "' )"); eb.n();
		}

		eb.addCode(content_type);

		eb.a("AND doc.status IN "); eb.a(status);


		return MetadataParser.parseNonSubmission(eb.query(sqs.return_leaf_class));
	}

}
