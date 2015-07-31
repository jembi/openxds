package gov.nist.registry.ws.sq.ebxmlrr21;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataParser;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.storedquery.StoredQuerySupport;
import gov.nist.registry.ws.sq.FindDocuments;

import org.apache.axiom.om.OMElement;
import org.openhealthtools.openxds.log.LoggerException;

import java.util.Iterator;

/**
 * Implementation specific class for FindDocuments stored query. 
 * All the logic is in the runImplementation() method.
 * @author bill
 *
 */
public class EbXML21FindDocuments extends FindDocuments {

	EbXML21QuerySupport eb;

	/**
	 * Constructor
	 * @param sqs
	 * @throws MetadataValidationException
	 */
	public EbXML21FindDocuments(StoredQuerySupport sqs) throws MetadataValidationException {
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

		boolean includeStable = false;
		boolean includeODD = false;

		for (String type : object_type) {
			if (type.equals(MetadataSupport.XDSDocumentEntry_objectType_stable_uuid)) {
				includeStable = true;
			}
			if (type.equals(MetadataSupport.XDSDocumentEntry_objectType_odd_uuid)) {
				includeODD = true;
			}
		}

		OMElement finalResult = null;
		OMElement oddResult = null;
		if (includeODD) {
			createQuery(true);
			oddResult = eb.query(sqs.return_leaf_class);
			finalResult = oddResult;
		}

		OMElement stableResult = null;
		if (includeStable) {
			createQuery(false);
			stableResult = eb.query(sqs.return_leaf_class);
			finalResult = stableResult;
		}

		if (oddResult != null && stableResult != null) {
			// join both result sets
			OMElement registryObjectListStable = (OMElement) stableResult.getChildElements().next();
			OMElement registryObjectListODD = (OMElement) oddResult.getChildElements().next();
			Iterator<OMElement> children = registryObjectListODD.getChildElements();
			while (children.hasNext()) {
				// add from odd result to stable
				registryObjectListStable.addChild(children.next().detach());
			}
			// set final results as combined results
			finalResult = stableResult;
		}

		return MetadataParser.parseNonSubmission(finalResult);
	}

	private void createQuery(boolean isODD) throws MetadataException {
		eb.init();

		if (sqs.return_leaf_class) {
			eb.a("SELECT *  "); eb.n();
		} else {
			eb.a("SELECT doc.id  "); eb.n();
		}
		eb.a("FROM ExtrinsicObject doc, ExternalIdentifier patId");
		eb.n();
		if (class_codes != null)                          eb.a(eb.declareClassifications(class_codes));
		if (type_codes != null)                           eb.a(eb.declareClassifications(type_codes));
		if (practice_setting_codes != null)               eb.a(eb.declareClassifications(practice_setting_codes));
		if (hcft_codes != null)                           eb.a(eb.declareClassifications(hcft_codes));  // $XDSDocumentEntryHealthcareFacilityTypeCode
		if (event_codes != null)                          eb.a(eb.declareClassifications(event_codes)); // $XDSDocumentEntryEventCodeList
		if (conf_codes != null)                           eb.a(eb.declareClassifications(conf_codes));  // $XDSDocumentEntryConfidentialityCode
		if (format_codes != null)                         eb.a(eb.declareClassifications(format_codes));             // $XDSDocumentEntryFormatCode
		if (creation_time_from != null)                   eb.a(", Slot crTimef"); eb.n();                       // $XDSDocumentEntryCreationTimeFrom
		if (creation_time_to != null)                     eb.a(", Slot crTimet"); eb.n();                       // $XDSDocumentEntryCreationTimeTo
		if (service_start_time_from != null)              eb.a(", Slot serStartTimef"); eb.n();                 // $XDSDocumentEntryServiceStartTimeFrom
		if (service_start_time_to != null)                eb.a(", Slot serStartTimet"); eb.n();                 // $XDSDocumentEntryServiceStartTimeTo
		if (service_stop_time_from != null)               eb.a(", Slot serStopTimef"); eb.n();                  // $XDSDocumentEntryServiceStopTimeFrom
		if (service_stop_time_to != null)                 eb.a(", Slot serStopTimet"); eb.n();                  // $XDSDocumentEntryServiceStopTimeTo
		if (author_person != null)                        eb.a(", Classification author"); eb.n();
		if (author_person != null)                        eb.a(", Slot authorperson"); eb.n();


		eb.a("WHERE"); eb.n();
		eb.a("(doc.id = patId.registryobject AND	"); eb.n();
		eb.a("  patId.identificationScheme='urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427' AND "); eb.n();
		eb.a("  patId.value = '"); eb.a(patient_id);
		eb.a("' ) "); eb.n();

		eb.addCode(class_codes);
		eb.addCode(type_codes);
		eb.addCode(practice_setting_codes);

		if (!isODD) {
			eb.addTimes("creationTime", "crTimef", "crTimet", creation_time_from, creation_time_to, "doc");
		}
		eb.addTimes("serviceStartTime", "serStartTimef", "serStartTimet", service_start_time_from, service_start_time_to, "doc");
		eb.addTimes("serviceStopTime", "serStopTimef", "serStopTimet", service_stop_time_from, service_stop_time_to, "doc");

		eb.addCode(hcft_codes);
		eb.addCode(event_codes);
		eb.addCode(conf_codes);

		eb.addCode(format_codes);

		if (author_person != null) {
			for (String ap : author_person) {
				eb.a("AND"); eb.n();
				eb.a("(doc.id = author.classifiedObject AND "); eb.n();
				eb.a("  author.classificationScheme='urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d' AND "); eb.n();
				eb.a("  authorperson.parent = author.id AND"); eb.n();
				eb.a("  authorperson.name = 'authorPerson' AND"); eb.n();
				eb.a("  authorperson.value LIKE '" + ap + "' )"); eb.n();
			}
		}

		eb.a("AND doc.status IN "); eb.a(status); eb.n();
		if (isODD) {
			eb.a("AND doc.objecttype = '" + MetadataSupport.XDSDocumentEntry_objectType_odd_uuid + "'"); eb.n();
		} else {
			eb.a("AND doc.objecttype = '" + MetadataSupport.XDSDocumentEntry_objectType_stable_uuid + "'"); eb.n();
		}
	}

}
