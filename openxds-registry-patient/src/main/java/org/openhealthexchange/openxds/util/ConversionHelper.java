package org.openhealthexchange.openxds.util;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthexchange.openpixpdq.data.Patient;
import org.openhealthexchange.openpixpdq.data.PatientIdentifier;
import org.openhealthexchange.openpixpdq.data.PersonName;
import org.openhealthexchange.openpixpdq.ihe.pdq.PdqQuery;
import org.openhie.openempi.model.EthnicGroup;
import org.openhie.openempi.model.Gender;
import org.openhie.openempi.model.IdentifierDomain;
import org.openhie.openempi.model.Language;
import org.openhie.openempi.model.Nationality;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.PersonIdentifier;
import org.openhie.openempi.model.Race;
import org.openhie.openempi.model.Religion;

import com.misyshealthcare.connect.base.SharedEnums.SexType;
import com.misyshealthcare.connect.base.demographicdata.Address;
import com.misyshealthcare.connect.base.demographicdata.PhoneNumber;
import com.misyshealthcare.connect.net.Identifier;

public class ConversionHelper
{
	private static final Log log = LogFactory.getLog(ConversionHelper.class);

	public static Person getPerson(PdqQuery query) {
		Person person = new Person();
		if (query.getPersonName() != null) {
			populatePersonName(person, query);
		}
		if (query.getSsn() != null ) {
			person.setSsn(query.getSsn());
		}
		if (query.getSex() != null) {
			Gender gender = new Gender();
			gender.setGenderCode(query.getSex().getCDAValue());
		}
		if (query.getBirthDate() != null) {
			person.setDateOfBirth(query.getBirthDate().getTime());
		}
		if (query.getAddress() != null ) {
			populateAddress(person, query.getAddress());
		}
		if(query.getPhone() != null) {
			populatePhone(person, query.getPhone());
		}
		if (query.getPatientIdentifier() != null) {
			person.addPersonIdentifier(getPersonIdentifier(query.getPatientIdentifier()));
		}
		log.trace("Converted object: " + query + " to " + person);
		return person;
	}

	public static Patient getPatient(Person person) {
		Patient patient = new Patient();
		patient.setBirthPlace(person.getBirthPlace());
		patient.setBirthOrder(person.getBirthOrder());
		patient.setDeathIndicator((person.getDeathInd() != null && person.getDeathInd().equalsIgnoreCase("Y")) ? true : false);
		if (person.getNationality() != null) {
			patient.setCitizenship(person.getNationality().getNationalityCode());
		}
		if (person.getLanguage() != null) {
			patient.setPrimaryLanguage(person.getLanguage().getLanguageCode());
		}
		patient.setPatientName(getPersonName(person));
		patient.setSsn(person.getSsn());
		if (person.getGender() != null) {
			patient.setAdministrativeSex(SexType.valueOf(person.getGender().getGenderCode()));
		}
		if (person.getDateOfBirth() != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(person.getDateOfBirth());
			patient.setBirthDateTime(calendar);
		}
		if (person.getRace() != null) {
			patient.setRace(person.getRace().getRaceCode());
		}
		if (person.getReligion() != null) {
			patient.setReligion(person.getReligion().getReligionCode());
		}
		patient.setMaritalStatus(person.getMaritalStatusCode());
		if (person.getEthnicGroup() != null) {
			patient.setEthnicGroup(person.getEthnicGroup().getEthnicGroupCode());
		}
		patient.addAddress(getAddress(person));
		patient.addPhoneNumber(getPhoneNumber(person));
		for (PersonIdentifier identifier : person.getPersonIdentifiers()) {
			patient.addPatientId(ConversionHelper.getPatientIdentifier(identifier));
		}
		log.trace("Converted object: " + person + " to " + patient);
		return patient;
	}

	public static Person getPerson(Patient patient) {
		Person person = new Person();
		person.setBirthOrder(patient.getBirthOrder());
		person.setBirthPlace(patient.getBirthPlace());
		if (patient.isDeathIndicator()) {
			person.setDeathInd("Y");
		}
		if (patient.getCitizenship() != null) {
			Nationality nationality = new Nationality();
			nationality.setNationalityCode(patient.getCitizenship());
			person.setNationality(nationality);
		}
		if (patient.getPrimaryLanguage() != null) {
			Language language = new Language();
			language.setLanguageCode(patient.getPrimaryLanguage());
			person.setLanguage(language);
		}
		populatePersonName(person, patient.getPatientName());
		person.setSsn(patient.getSsn());
		if (patient.getAdministrativeSex() != null) {
			Gender gender = new Gender();
			gender.setGenderCode(patient.getAdministrativeSex().getCDAValue());
			person.setGender(gender);
		}
		person.setDateOfBirth(patient.getBirthDateTime().getTime());
		if (patient.getRace() != null) {
			Race race = new Race();
			race.setRaceCode(patient.getRace());
			person.setRace(race);
		}
		if (patient.getReligion() != null) {
			Religion religion = new Religion();
			religion.setReligionCode(patient.getReligion());
			person.setReligion(religion);
		}
		if (patient.getMaritalStatus() != null) {
			person.setMaritalStatusCode(patient.getMaritalStatus().substring(0,1));
		}
		if (patient.getEthnicGroup() != null) {
			EthnicGroup ethnicGroup = new EthnicGroup();
			ethnicGroup.setEthnicGroupCode(patient.getEthnicGroup());
			person.setEthnicGroup(ethnicGroup);
		}
		if (patient.getAddresses().size() > 0) {
			Address address = patient.getAddresses().get(0);
			person.setAddress1(address.getAddLine1());
			person.setAddress2(address.getAddLine2());
			person.setCity(address.getAddCity());
			person.setState(address.getAddState());
			person.setPostalCode(address.getAddZip());
			person.setCountry(address.getAddCountry());
		}
		if (patient.getPhoneNumbers().size() > 0) {
			PhoneNumber number = patient.getPhoneNumbers().get(0);
			person.setPhoneAreaCode(number.getAreaCode());
			person.setPhoneCountryCode(number.getCountryCode());
			person.setPhoneNumber(number.getNumber());
			person.setPhoneExt(number.getExtension());
		}
		for (PatientIdentifier pid : patient.getPatientIds()) {
			person.addPersonIdentifier(getPersonIdentifier(pid));
		}
		log.trace("Converted object: " + patient + " to " + person);
		return person;
	}

	private static void populatePhone(Person person, PhoneNumber phone) {
		if (phone == null) {
			return;
		}
		person.setPhoneAreaCode(phone.getAreaCode());
		person.setPhoneCountryCode(phone.getCountryCode());
		person.setPhoneExt(phone.getExtension());
		person.setPhoneNumber(phone.getNumber());
	}

	private static PhoneNumber getPhoneNumber(Person person) {
		PhoneNumber number = new PhoneNumber();
		number.setAreaCode(person.getPhoneAreaCode());
		number.setCountryCode(person.getPhoneCountryCode());
		number.setExtension(person.getPhoneExt());
		number.setNumber(person.getPhoneNumber());
		return number;
	}

	public static void populateAddress(Person person, Address address) {
		if (address == null) {
			return;
		}
		person.setAddress1(address.getAddLine1());
		person.setAddress2(address.getAddLine2());
		person.setCity(address.getAddCity());
		person.setState(address.getAddState());
		person.setPostalCode(address.getAddZip());
		person.setCountry(address.getAddCountry());
	}


	private static Address getAddress(Person person) {
		Address address = new Address();
		address.setAddLine1(person.getAddress1());
		address.setAddLine2(person.getAddress2());
		address.setAddCity(person.getCity());
		address.setAddState(person.getState());
		address.setAddZip(person.getPostalCode());
		address.setAddCountry(person.getCountry());
		return address;
	}

	public static PersonIdentifier getPersonIdentifier(org.openhealthexchange.openpixpdq.data.PersonIdentifier personIdentifier) {
		PersonIdentifier pi = new PersonIdentifier();
		pi.setIdentifier(personIdentifier.getId());
		IdentifierDomain id = new IdentifierDomain();
		id.setNamespaceIdentifier(personIdentifier.getAssigningAuthority().getNamespaceId());
		id.setUniversalIdentifier(personIdentifier.getAssigningAuthority().getUniversalId());
		id.setUniversalIdentifierTypeCode(personIdentifier.getAssigningAuthority().getUniversalIdType());
		return pi;
	}


	public static PatientIdentifier getPatientIdentifier(PersonIdentifier identifier) {
		Identifier aa = new Identifier(identifier.getIdentifierDomain().getNamespaceIdentifier(),
				identifier.getIdentifierDomain().getUniversalIdentifier(),
				identifier.getIdentifierDomain().getUniversalIdentifierTypeCode());
		return new PatientIdentifier(identifier.getIdentifier(), aa);
	}

	public static void populatePersonName(Person person, PdqQuery query) {
		PersonName from = query.getPersonName();
		if (from == null) {
			return;
		}
		person.setGivenName(from.getFirstName());
		person.setFamilyName(from.getLastName());
		person.setMiddleName(from.getSecondName());
		person.setPrefix(from.getPrefix());
		person.setSuffix(from.getSuffix());
		person.setDegree(from.getDegree());
	}

	public static PersonName getPersonName(Person person) {
		PersonName personName = new PersonName();
		personName.setDegree(person.getDegree());
		personName.setFirstName(person.getGivenName());
		personName.setLastName(person.getFamilyName());
		personName.setPrefix(person.getPrefix());
		personName.setSecondName(person.getMiddleName());
		personName.setSuffix(person.getSuffix());
		return personName;
	}

	public static void populatePersonName(Person person, PersonName personName) {
		String lastName = personName.getLastName();
		if (lastName != null) {
			lastName = lastName.toUpperCase();
		}
		String firstName = personName.getFirstName();
		if (firstName != null) {
			firstName = firstName.toUpperCase();
		}
		person.setGivenName(firstName);
		person.setFamilyName(lastName);
		person.setPrefix(personName.getPrefix());
		person.setSuffix(personName.getSuffix());
		person.setDegree(personName.getDegree());
	}


	public static PatientIdentifier convertPatientIdentifier(PersonIdentifier pid) {
		if (pid == null) {
			return null;
		}

		PatientIdentifier ret = new PatientIdentifier();
		ret.setId(pid.getIdentifier());
		ret.setAssigningAuthority(convertIdentifierDomain(pid.getIdentifierDomain()));
		return ret;
	}


	public static Identifier convertIdentifierDomain(IdentifierDomain identifierDomain) {
		if (identifierDomain == null) {
			return null;
		}
		Identifier ret = new Identifier(identifierDomain.getNamespaceIdentifier(),
				identifierDomain.getUniversalIdentifier(),
				identifierDomain.getUniversalIdentifierTypeCode());
		return ret;
	}
}
