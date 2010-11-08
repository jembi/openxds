package gov.nist.registry.common2.registry.validation;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.RegistryErrorList;
import gov.nist.registry.common2.registry.RegistryUtility;

import java.util.Iterator;
import java.util.List;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.openhealthtools.openexchange.actorconfig.IActorDescription;
import org.openhealthtools.openexchange.datamodel.Identifier;
import org.openhealthtools.openexchange.syslog.LogMessage;
import org.openhealthtools.openexchange.syslog.LoggerException;

public class Validator {
	RegistryErrorList rel;
	Metadata m;
	boolean is_submit;
	boolean is_xdsb;
	Structure s;
	Attribute a;
	CodeValidation cv;
	PatientId pid;
	UniqueId uid;
	List<Identifier> assigning_authorities;
	LogMessage log_message;
	boolean isPnR = false;

	public Validator(Metadata m, RegistryErrorList rel, boolean is_submit, boolean is_xdsb, LogMessage log_message, boolean isPnR, IActorDescription actorDescription)
	throws LoggerException, XdsException {
		this.rel = rel;
		this.m = m;
		this.is_submit = is_submit;
		this.is_xdsb = is_xdsb;
		this.log_message = log_message;
		this.isPnR = isPnR;
		
		s = new Structure(m, is_submit, rel, log_message);
		a = new Attribute(m, is_submit, is_xdsb, rel, isPnR);
		try {
			cv = new CodeValidation(m, is_submit, is_xdsb, rel, actorDescription);
		}
		catch (XdsInternalException e) {
			rel.add_error(MetadataSupport.XDSRegistryError, e.getMessage(), RegistryUtility.exception_details(e), null);
			throw new XdsInternalException(e.getLocalizedMessage(), e);
		}
		assigning_authorities = cv.getAssigningAuthorities();

		pid = new PatientId(m, rel, is_submit, is_xdsb);
		uid = new UniqueId(m, rel, is_xdsb);
	}
	
	public List<Identifier> getAssigningAuthority() {
		return this.assigning_authorities;
	}


	public void run() throws XdsInternalException, MetadataValidationException, LoggerException, XdsException {

		try {
			s.run();

			a.run();

			cv.run();
		}
		catch (XdsInternalException e) {
			rel.add_error(MetadataSupport.XDSRegistryError, e.getMessage(), RegistryUtility.exception_details(e), null);
		}
		catch (MetadataException e) {
			rel.add_error(MetadataSupport.XDSRegistryError, e.getMessage(), RegistryUtility.exception_details(e), null);
		}

		pid.run();

		for (OMElement ele : m.getRegistryPackages()) 
			validate_internal_classifications(ele);
		for (OMElement ele : m.getExtrinsicObjects()) 
			validate_internal_classifications(ele);

		uid.run();

		rel.getRegistryErrorList(); // forces output of validation report
		//System.out.println("Metadata Validator Done");
	}


	// internal classifications must point to object that contains them

	void validate_internal_classifications(OMElement e) throws MetadataValidationException, MetadataException {
		String e_id = e.getAttributeValue(MetadataSupport.id_qname);
		if (e_id == null || e_id.equals(""))
			return;
		for (Iterator it=e.getChildElements(); it.hasNext(); ) {
			OMElement child = (OMElement) it.next();
			OMAttribute classified_object_att = child.getAttribute(MetadataSupport.classified_object_qname);
			if (classified_object_att != null) {
				String value = classified_object_att.getAttributeValue();
				if ( !e_id.equals(value)) {
					throw new MetadataValidationException("Classification " + m.getIdentifyingString(child) + 
							"\n   is nested inside " + m.getIdentifyingString(e) +
							"\n   but classifies object " + m.getIdentifyingString(value));
				}
			}
		}
	}

	void val(String topic, String msg ) {
		if (msg == null) msg = "Ok";
		rel.add_validation(topic, msg, "Validator.java");
	}



	void err(String msg) {
		rel.add_error(MetadataSupport.XDSRegistryMetadataError, msg, "Validator.java", null);
	}
}
