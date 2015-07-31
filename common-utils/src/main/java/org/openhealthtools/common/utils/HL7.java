/**
 *  Copyright (c) 2009-2010 Misys Open Source Solutions (MOSS) and others
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  Contributors:
 *    Misys Open Source Solutions - initial API and implementation
 *    -
 */
package org.openhealthtools.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;

import org.openhealthexchange.openpixpdq.data.PatientIdentifier;
import org.openhealthexchange.openpixpdq.data.PersonIdentifier;
import org.openhealthexchange.openpixpdq.data.PersonName;
import org.openhealthexchange.openpixpdq.ihe.impl_v2.hl7.HL7v231;

import ca.uhn.hl7v2.parser.EncodingCharacters;
import ca.uhn.hl7v2.parser.Escape;

import com.misyshealthcare.connect.base.PatientID;
import com.misyshealthcare.connect.base.SharedEnums.SexType;
import com.misyshealthcare.connect.base.clinicaldata.Provider;
import com.misyshealthcare.connect.base.demographicdata.Address;
import com.misyshealthcare.connect.net.Identifier;


/**
 * This class implements a number of HL7 parsing and generation utilities
 * used by the XDS Repository Actors.  The HAPI code could not be used in
 * this class because the repository uses partial message pieces.  It was easier
 * just to implement them directly.
 * 
 * @author Jim Firby
 * @version 1.0 - Nov 14, 2005
 */
public class HL7 {
	
	private static EncodingCharacters encodingCharacters = new EncodingCharacters('|', "^~\\&");
	
	/**
	 * Generates a CX data type from an ID and an AssigningAuthority.
	 * 
	 * @param id The ID
	 * @return The CX formatted type (conforming to XDS requirements)
	 */
	public static String toCX(String id, Identifier authority) {
		if (id == null) return null;
		StringBuffer sb = new StringBuffer();
		addHL7Text(sb, id);
		sb.append("^^^");
		String value = authority.getNamespaceId();
		if (value != null) sb.append(value);
		sb.append("&");
		value = authority.getUniversalId();
		if (value != null) sb.append(value);
		sb.append("&");
		value = authority.getUniversalIdType();
		if (value != null) sb.append(value);
		return sb.toString();
	}
	
	/**
	 * Generates a CX list from a list of PatientIdentifier.
	 * 
	 * @param pids The PatientIdentifier List 
	 * @return The CX formatted type (conforming to XDS requirements)
	 */
	public static String toCX(List<PatientIdentifier> pids) {
		if (pids == null || pids.size() == 0)
			return null;

		StringBuffer sb = new StringBuffer();
		for (int i=0; i<pids.size(); i++) {
			PersonIdentifier pid = pids.get(i);
			String id = toCX(pid.getId(), pid.getAssigningAuthority());
			sb.append(id);
			if (i <= pids.size()-2) sb.append("~");
		}
		return sb.toString();
	}
		
	/**
	 * Generates a CX data type from an ID and an AssigningAuthority.
	 *
	 * @param id The ID
	 * @param authority The assigning authority
	 * @return The CX formatted type (conforming to XDS requirements)
	 */
    public static String toCX(String id, String authority) {
        if (id == null || authority == null) return null;
        StringBuffer sb = new StringBuffer();
        addHL7Text(sb, id);
        sb.append("^^^");
        sb.append(authority);
        return sb.toString();
    }

	/**
	 * Generates a CX data type from an AssigningAuthority.
	 *
	 * @param authority The assigning authority
	 * @return The CX formatted type  
	 */
    public static String toCXAuth(Identifier authority) {
        if(authority == null)
            return null;
        StringBuffer sb = new StringBuffer();
        String value = authority.getNamespaceId();
        if (value != null) sb.append(value);

        value = authority.getUniversalId();
        if (value != null)  {
            sb.append("&" + value);
            value = authority.getUniversalIdType();
            if (value != null) sb.append("&" + value);
        }

        return sb.toString();

    }

    /**
	 * Decodes an HL7 CX identifier string into a PatientID.  
	 * <p>
	 * This method is used directly in some
	 * Mesa testing programs.
	 * 
	 * @param pid The PatientID to hold the decoded patient ID
	 * @param cx The CX string holding the patient ID to decode
	 */
	public static void fromCX(PatientID pid, String cx) {
		String id = getIdFromCX(cx);
		Identifier authority = getAssigningAuthorityFromCX(cx);
		if ((id == null) || (authority == null)) return;
		authority.addPatientId(pid, id);
	}
	
	/**
	 * Gets the ID component from an HL7 CX identifier string.  
	 * 
	 * @param cx The CX string holding the ID to decode
	 */
	public static String getIdFromCX(String cx) {
		return getFieldText(cx, 1);
	}
	
	/**
	 * Gets the Assigning Authority component from an HL7 CX identifier string.  
	 * 
	 * @param cx The CX string holding the Assigning Authority to decode
	 */
	public static Identifier getAssigningAuthorityFromCX(String cx) {
		String aa = getFieldText(cx, 4);
		if (aa == null) return null;
		return new Identifier(getComponentText(aa, 1), getComponentText(aa, 2), getComponentText(aa, 3));
	}

    /**
	 * Gets the Assigning Authority component from an HL7 CX identifier Authority string.
	 *
	 * @param aa The CXAuth string holding the Assigning Authority to decode
	 */
	public static Identifier getAssigningAuthorityFromCXAuth(String aa) {
		if (aa == null) return null;
		return new Identifier(getComponentText(aa, 1), getComponentText(aa, 2), getComponentText(aa, 3));
	}

    /**
	 * Generates a DTM date/time type from a Java date object.  The date
	 * and time is converted to GMT before formatting.
	 * 
	 * @param date The Java date to format
	 * @return The DTM formatted date/time
	 */
	public static String toDTM(Date date) {
		if (date == null) return null;
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		format.setTimeZone(new SimpleTimeZone(0, "GMT"));
		return format.format(date);
	}
	
	/**
	 * Extracts a Java date object from a DTM date/time type.  The date
	 * is converted from GMT to the local time zone.
	 * 
	 * @param dtm The HL7 encoded DTM
	 * @return The Java date
	 */
	public static Date fromDTM(String dtm) {
		if (dtm == null) return null;
		return HL7v231.parseDateTimeGMT(dtm);
	}
	
	/**
	 * Generates a DT date type from a Java date object.  The date
	 * is formatted in local time.
	 * 
	 * @param date The Java date to format
	 * @return The DT formatted date
	 */
	public static String toDT(Date date) {
		if (date == null) return null;
		return HL7v231.formatDate(date);
	}
	
	/**
	 * Decodes a DT data type into a Java date object.
	 * 
	 * @param date The DT date string
	 * @return The Java date object
	 */
	public static Date fromDT(String date) {
		if (date == null) return null;
		return HL7v231.parseDate(date);
	}
	
	/**
	 * Generates an XAD data type from an Address object.
	 * 
	 * @param address The address to format
	 * @return The XAD formatted address string
	 */
	public static String toXAD(Address address) {
		if (address == null) return null;
		StringBuffer sb = new StringBuffer();
		if (address.getAddLine1() != null) addHL7Text(sb, address.getAddLine1());
		sb.append('^');
		if (address.getAddLine2() != null) addHL7Text(sb, address.getAddLine2());
		sb.append('^');
		if (address.getAddCity() != null) addHL7Text(sb, address.getAddCity());
		sb.append('^');
		if (address.getAddState() != null) addHL7Text(sb, address.getAddState());
		sb.append('^');
		if (address.getAddZip() != null) addHL7Text(sb, address.getAddZip());
		sb.append('^');
		if (address.getAddCountry() != null) addHL7Text(sb, address.getAddCountry());
		sb.append("^^^");
		if (address.getAddCounty() != null) addHL7Text(sb, address.getAddCounty());
		return sb.toString();
	}
	
	/**
	 * Decodes an HL7 XAD data type into an Address.
	 * 
	 * @param xad The XAD encoded address
	 * @return The decoded address
	 */
	public static Address fromXAD(String xad) {
		if (xad == null) return null;
		Address address = new Address();
		address.setAddLine1(getFieldText(xad, 1));
		address.setAddLine2(getFieldText(xad, 2));
		address.setAddCity(getFieldText(xad, 3));
		address.setAddState(getFieldText(xad, 4));
		address.setAddZip(getFieldText(xad, 5));
		address.setAddCountry(getFieldText(xad, 6));
        address.setAddCounty(getFieldText(xad, 9));
		return address;
	}
	
	/**
	 * Generates an XON data type from a string.
	 * 
	 * @param institution The string to be formatted
	 * @return The XON formatted string
	 */
	public static String toXON(String institution) {
		return toHL7Text(institution);
	}
	
	/**
	 * Gets the institution string from an HL7 encoded XON.
	 * 
	 * @param xon The XON to decode
	 * @return The institution
	 */
	public static String fromXON(String xon) {
		return getFieldText(xon, 1);
	}
	
	/**
	 * Generates an XCN name data type from a Provider.
	 * 
	 * @param person The person's name to be formatted
	 * @return The XCN formatted name string
	 */
	public static String toXCN(Provider person) {
		if (person == null) return null;
		StringBuffer sb = new StringBuffer();
		addHL7Text(sb, person.getProviderId());
		sb.append('^');
		addHL7NameParts(sb, person.getProvNameFirst(), person.getProvNameMiddle(), person.getProvNameLast(),
				person.getProvNameTitle(), person.getProvNameSuffix());
		return sb.toString();
	}
	
	/**
	 * Extracts a Provide from an HL7 XCN data type.
	 * 
	 * @param xcn The XCN to decode
	 * @return The resulting Provider
	 */
	public static Provider fromXCN(String xcn) {
		if (xcn == null) return null;
		Provider provider = new Provider();
		provider.setProviderId(getFieldText(xcn, 1));
		provider.setProvNameLast(getFieldText(xcn, 2));
		provider.setProvNameFirst(getFieldText(xcn, 3));
		provider.setProvNameMiddle(getFieldText(xcn, 4));
		provider.setProvNameSuffix(getFieldText(xcn, 5));
		provider.setProvNameTitle(getFieldText(xcn, 6));
		return provider;
	}
	
	/**
	 * Generates an XPN name data type from a Patient.
	 * 
	 * @param personName The patient's name to be formatted
	 * @return The XPN formatted name string
	 */
	public static String toXPN(PersonName personName) {
		if (personName == null) return null;
		StringBuffer sb = new StringBuffer();
		addHL7NameParts(sb, personName.getFirstName(), personName.getSecondName(), personName.getLastName(),
				personName.getPrefix(), personName.getSuffix());
		return sb.toString();
	}
	
	/**
	 * Decodes an HL7 XPN data type and put the name fields into a <code>PersonName</code>
	 * 
	 * @param personName The <code>PersonName</code> to get the name
	 * @param xpn The XPN data type to decode
	 * @return True if the name was decoded
	 */
	public static boolean fromXPN(PersonName personName, String xpn) {
		if (personName == null) return false;
		if (xpn == null) return false;
		personName.setLastName(getFieldText(xpn, 1));
		personName.setFirstName(getFieldText(xpn, 2));
		personName.setSecondName(getFieldText(xpn, 3));
		personName.setSuffix(getFieldText(xpn, 4));
		personName.setPrefix(getFieldText(xpn, 5));	
		return true;
	}
	
	/**
	 * Converts an enum gender value to the coded value used in XDS
	 * HL7 PID-8 messages.
	 * 
	 * @param sex The gender enum to convert
	 * @return The XDS HL7 value
	 */
	public static String toSex(SexType sex) {
		if (sex == null) return "U";
		if (sex == SexType.MALE) return "M";
		if (sex == SexType.FEMALE) return "F";
		if (sex == SexType.OTHER) return "O";
		return "U";
	}
	
	/**
	 * Decodes and HL7 adminstrative sex value into a SexType enum.
	 * 
	 * @param code The adminstrative sex code to decode
	 * @return The gender enum value
	 */
	public static SexType fromSex(String code) {
		if (code == null) return SexType.UNKNOWN;
		if (code.equalsIgnoreCase("M")) return SexType.MALE;
		if (code.equalsIgnoreCase("F")) return SexType.FEMALE;
		if (code.equalsIgnoreCase("O")) return SexType.OTHER;
		return SexType.UNKNOWN;
	}
	
	/**
	 * Helper function to format a name
	 * 
	 * @param sb The string buffer to add the name to
	 * @param f The first name
	 * @param m The middle name
	 * @param l The last name
	 * @param p The name prefix
	 * @param s The name suffix
	 */
	private static void addHL7NameParts(StringBuffer sb, String f, String m, String l, String p, String s) {
		if (l != null) addHL7Text(sb, l);
		if ((f == null) && (m == null) && (s == null) && (p == null)) return;
		sb.append('^');
		if (f != null) addHL7Text(sb, f);
		if ((m == null) && (s == null) && (p == null)) return;
		sb.append('^');
		if (m != null) addHL7Text(sb, m);
		if ((s == null) && (p == null)) return;
		sb.append('^');
		if (s != null) addHL7Text(sb, s);
		if (p == null) return;
		sb.append('^');
		addHL7Text(sb, p);
	}
	
	/**
	 * Converts a string to acceptable HL7 by turning protected characters into
	 * spaces.
	 * 
	 * @param sb The string buffer to put the result into
	 * @param input The input string to be converted
	 */
	private static void addHL7Text(StringBuffer sb, String input) {
		if (input != null) {
			sb.append(toHL7Text(input));
		}
	}
	
	/**
	 * Encodes a string using HL7 escape characters.
	 * 
	 * @param input The input string to encode
	 * @return The encoded HL7 string
	 */
	private static String toHL7Text(String input) {
		if (input == null) return null;
		return Escape.escape(input, encodingCharacters);
	}
	
	/**
	 * Decodes an HL7 string so that it contains no escape characters
	 * 
	 * @param text The HL7 string to decode
	 * @return The decoded plain text string
	 */
	private static String fromHL7Text(String text) {
		if (text == null) return null;
		return Escape.unescape(text, encodingCharacters);
	}
	
	/**
	 * Gets the 'id' component from an HL7 CX string.
	 * 
	 * @param input The CX string
	 * @return The 'id' component of the string
	 */
	public static String getCXid(String input) {
		return fromHL7Text(getField(input, 1));
	}
	
	/**
	 * Gets the 'assigning authority' component from an HL7 CX string.
	 * 
	 * @param input The CX string
	 * @return The 'assigning authority' component
	 */
	public static String getCXauthority(String input) {
		String authority = getField(input, 4);
		if (authority == null) return null;
		if (authority.startsWith("&")) return authority.substring(1);
		return authority;
	}
	
	/**
	 * Gets a field from an HL7 string
	 * 
	 * @param input The input string
	 * @param field The field to extract
	 * @return
	 */
	private static String getField(String input, int field) {
		return getPart(input, field, "\\^");
	}
	
	/**
	 * Gets the field text from an HL7 string.
	 * 
	 * @param input The input string
	 * @param field The field to extract
	 * @return
	 */
	private static String getFieldText(String input, int field) {
		String value = getField(input, field);
		if (value == null) return null;
		return fromHL7Text(value);
	}

	/**
	 * Gets a component from an HL7 field
	 * 
	 * @param field The input field string
	 * @param component The component to extract
	 * @return
	 */
	private static String getComponent(String field, int component) {
		return getPart(field, component, "\\&");
	}
	
	/**
	 * Gets a component text from an HL7 field.
	 * 
	 * @param field The input field string
	 * @param component The component to extract
	 * @return
	 */
	private static String getComponentText(String input, int component) {
		String value = getComponent(input, component);
		if (value == null) return null;
		return fromHL7Text(value);
	}

	/**
	 * Gets a piece of a delimited string.
	 * 
	 * @param input The input string
	 * @param field The desired component (1 based)
	 * @param separator The component separator regex
	 * @return The value of the desired component
	 */
	private static String getPart(String input, int field, String separator) {
		if (input == null) return null;
		String fields[] = input.split(separator);
		if (fields == null) return null;
		if (fields.length < field) return null;
		String result = fields[field - 1];
		if (result == null) return null;
		if (result.equals("")) return null;
		return result;
	}
	
	
	
}
