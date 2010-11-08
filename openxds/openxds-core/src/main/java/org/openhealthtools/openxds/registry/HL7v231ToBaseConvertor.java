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
 */
package org.openhealthtools.openxds.registry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.openhealthtools.common.utils.AssigningAuthorityUtil;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.datamodel.Address;
import org.openhealthtools.openexchange.datamodel.DriversLicense;
import org.openhealthtools.openexchange.datamodel.Identifier;
import org.openhealthtools.openexchange.datamodel.PatientIdentifier;
import org.openhealthtools.openexchange.datamodel.PersonName;
import org.openhealthtools.openexchange.datamodel.PhoneNumber;
import org.openhealthtools.openexchange.datamodel.Problem;
import org.openhealthtools.openexchange.datamodel.Provider;
import org.openhealthtools.openexchange.datamodel.SharedEnums;
import org.openhealthtools.openexchange.datamodel.Visit;
import org.openhealthtools.openexchange.datamodel.SharedEnums.AddressType;
import org.openhealthtools.openexchange.datamodel.SharedEnums.SexType;
import org.openhealthtools.openexchange.utils.DateUtil;
import org.openhealthtools.openexchange.utils.StringUtil;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v231.datatype.CX;
import ca.uhn.hl7v2.model.v231.datatype.XAD;
import ca.uhn.hl7v2.model.v231.datatype.XCN;
import ca.uhn.hl7v2.model.v231.datatype.XPN;
import ca.uhn.hl7v2.model.v231.datatype.XTN;
import ca.uhn.hl7v2.model.v231.group.ADT_A39_PIDPD1MRGPV1;
import ca.uhn.hl7v2.model.v231.message.ADT_A39;
import ca.uhn.hl7v2.model.v231.segment.DG1;
import ca.uhn.hl7v2.model.v231.segment.MRG;
import ca.uhn.hl7v2.model.v231.segment.MSH;
import ca.uhn.hl7v2.model.v231.segment.PD1;
import ca.uhn.hl7v2.model.v231.segment.PID;
import ca.uhn.hl7v2.model.v231.segment.PV1;
import ca.uhn.hl7v2.model.v231.segment.PV2;



/**
 * This class parses the PIX Feed message
 *
 * @author Rasakannu Palaniyandi
 * @date Nov 25, 2008
 */
public class HL7v231ToBaseConvertor{

    private MSH msh;
    private PID pid;
    private PV1 pv1;
    private PV2 pv2;
    private PD1 pd1;
    private DG1 dg1;
    private MRG mrg;
    private ADT_A39_PIDPD1MRGPV1 pidpd1mrgpv1;

    IConnectionDescription connection;

    public HL7v231ToBaseConvertor(Message in, IConnectionDescription connection) {
    	this.connection = connection;
    	try {
        	msh = (MSH)in.get("MSH");

        	 if (in instanceof ADT_A39) {
        		 // this is for merge patients
        		 pidpd1mrgpv1 = (ADT_A39_PIDPD1MRGPV1)in.get("PIDPD1MRGPV1");
        		 pid = (PID)pidpd1mrgpv1.get("PID");
                 pv1 = (PV1)pidpd1mrgpv1.get("PV1");
                 try {
                    pv2 = (PV2)pidpd1mrgpv1.get("PV2");
                 } catch (HL7Exception e) {
                    pv2 = null; //Find no PV2, ok, it is fine.
                 }
                 try {
                     dg1 = (DG1)pidpd1mrgpv1.get("DG1");
                 } catch (HL7Exception e) {
                     dg1 = null;
                 }
                 try {
                     pd1 = (PD1)pidpd1mrgpv1.get("PD1");
                 } catch (HL7Exception e) {
                     pd1 = null;
                 }
                 try {
                 	mrg = (MRG)pidpd1mrgpv1.get("MRG");
     			} catch (HL7Exception e) {
     				mrg = null;
     			}
             	 }
         else{
		    pid = (PID)in.get("PID");
            pv1 = (PV1)in.get("PV1");
            try {
               pv2 = (PV2)in.get("PV2");
            } catch (HL7Exception e) {
               pv2 = null; //Find no PV2, ok, it is fine.
            }
            try {
                dg1 = (DG1)in.get("DG1");
            } catch (HL7Exception e) {
                dg1 = null;
            }
            try {
                pd1 = (PD1)in.get("PD1");
            } catch (HL7Exception e) {
                pd1 = null;
            }
            try {
            	mrg = (MRG)in.get("MRG");
			} catch (HL7Exception e) {
				mrg = null;
			}
        	 }

        }catch (HL7Exception e) {
              throw new ExceptionInInitializerError(e);
        }
        if(msh == null || pid == null) {
            throw new ExceptionInInitializerError();
        }
    }


    /**
	 * Gets a list of patient identifiers from the PID segment.
	 *
	 * @return a list of {@link PatientIdentifier}
	 */
    public List<PatientIdentifier> getPatientIds(){
    	List<PatientIdentifier> patientIds = new ArrayList<PatientIdentifier>();
    	
    	CX[] cxs = pid.getPatientIdentifierList();
    	for(CX cx: cxs) {
            PatientIdentifier identifier =new PatientIdentifier();
    		Identifier assignAuth= new Identifier(cx.getAssigningAuthority().getNamespaceID().getValue(),cx.getAssigningAuthority().getUniversalID().getValue(),cx.getAssigningAuthority().getUniversalIDType().getValue());
            Identifier assignFac =new Identifier(cx.getAssigningFacility().getNamespaceID().getValue(),cx.getAssigningFacility().getUniversalID().getValue(),cx.getAssigningFacility().getUniversalIDType().getValue());
    		//Need to reconcile assigning authority to fill up any missing components
            Identifier reconciledAssignAuth = AssigningAuthorityUtil.reconcileIdentifier(assignAuth, connection);
            identifier.setAssigningAuthority(reconciledAssignAuth);
        	identifier.setAssigningFacility(assignFac);
    		identifier.setId(cx.getID().getValue());
            identifier.setIdentifierTypeCode(cx.getIdentifierTypeCode().getValue());
           // identifier.setEffectiveDate(cx.get);
        //	identifier.setExpirationDate(expirationDate);
        	patientIds.add(identifier);
        }
    	return patientIds;
    }


    /**
	 * Gets a list of patient identifiers from the MRG segment
	 *
	 * @return a list of {@link PatientIdentifier} of the merge patient
	 */
    public List<PatientIdentifier> getMrgPatientIds(){
    	List<PatientIdentifier> patientIds = new ArrayList<PatientIdentifier>();
    	PatientIdentifier identifier =new PatientIdentifier();
    	CX[] cxs = mrg.getPriorPatientIdentifierList();
    	for(CX cx: cxs) {
    		Identifier assignAuth= new Identifier(cx.getAssigningAuthority().getNamespaceID().getValue(),cx.getAssigningAuthority().getUniversalID().getValue(),cx.getAssigningAuthority().getUniversalIDType().getValue());
            Identifier assignFac = new Identifier(cx.getAssigningFacility().getNamespaceID().getValue(),cx.getAssigningFacility().getUniversalID().getValue(),cx.getAssigningFacility().getUniversalIDType().getValue());

    		//Need to reconcile assigning authority to fill up any missing components
            Identifier reconciledAssignAuth = AssigningAuthorityUtil.reconcileIdentifier(assignAuth, connection);
            identifier.setAssigningAuthority(reconciledAssignAuth);
        	identifier.setAssigningFacility(assignFac);
    		identifier.setId(cx.getID().getValue());
            identifier.setIdentifierTypeCode(cx.getIdentifierTypeCode().getValue());
           // identifier.setEffectiveDate(cx.get);
        //	identifier.setExpirationDate(expirationDate);
        	patientIds.add(identifier);
        }
    	return patientIds;
    }


    /**
	 * Gets the patient account number from the PID segment.
	 *
	 * @return the patient account number
	 */
    public PatientIdentifier getpatientAccountNumber(){
    	PatientIdentifier identifier =new PatientIdentifier();
    	CX cx =pid.getPatientAccountNumber();
    	Identifier assignAuth= new Identifier(cx.getAssigningAuthority().getNamespaceID().getValue(),cx.getAssigningAuthority().getUniversalID().getValue(),cx.getAssigningAuthority().getUniversalIDType().getValue());
        Identifier assignFac =new Identifier(cx.getAssigningFacility().getNamespaceID().getValue(),cx.getAssigningFacility().getUniversalID().getValue(),cx.getAssigningFacility().getUniversalIDType().getValue());
		identifier.setAssigningAuthority(assignAuth);
    	identifier.setAssigningFacility(assignFac);
		identifier.setId(cx.getID().getValue());
        identifier.setIdentifierTypeCode(cx.getIdentifierTypeCode().getValue());
       //identifier.setEffectiveDate(cx.get);
       //identifier.setExpirationDate(expirationDate);
    	return identifier;
    }


    /**
	 * Gets the merge patient account number from the MRG segment
	 *
	 * @return the patient account number of the merge patient
	 */
    public PatientIdentifier getMrgpatientAccountNumber(){
    	PatientIdentifier identifier =new PatientIdentifier();
    	CX cx =mrg.getPriorPatientAccountNumber();
    	Identifier assignAuth= new Identifier(cx.getAssigningAuthority().getNamespaceID().getValue(),cx.getAssigningAuthority().getUniversalID().getValue(),cx.getAssigningAuthority().getUniversalIDType().getValue());
        Identifier assignFac =new Identifier(cx.getAssigningFacility().getNamespaceID().getValue(),cx.getAssigningFacility().getUniversalID().getValue(),cx.getAssigningFacility().getUniversalIDType().getValue());
		identifier.setAssigningAuthority(assignAuth);
    	identifier.setAssigningFacility(assignFac);
		identifier.setId(cx.getID().getValue());
        identifier.setIdentifierTypeCode(cx.getIdentifierTypeCode().getValue());
       //identifier.setEffectiveDate(cx.get);
       //identifier.setExpirationDate(expirationDate);
    	return identifier;
    }

    /**
	 * Gets the monther's id from the PID segment
	 *
	 * @return the monther's id
	 */
    public PatientIdentifier getMonthersId(){
    	PatientIdentifier identifier =new PatientIdentifier();
    	CX[] cxs = pid.getMotherSIdentifier();
    	for(CX cx: cxs) {
    		Identifier assignAuth= new Identifier(cx.getAssigningAuthority().getNamespaceID().getValue(),cx.getAssigningAuthority().getUniversalID().getValue(),cx.getAssigningAuthority().getUniversalIDType().getValue());
            Identifier assignFac =new Identifier(cx.getAssigningFacility().getNamespaceID().getValue(),cx.getAssigningFacility().getUniversalID().getValue(),cx.getAssigningFacility().getUniversalIDType().getValue());
    		identifier.setAssigningAuthority(assignAuth);
        	identifier.setAssigningFacility(assignFac);
    		identifier.setId(cx.getID().getValue());
            identifier.setIdentifierTypeCode(cx.getIdentifierTypeCode().getValue());
           // identifier.setEffectiveDate(cx.get);
        //	identifier.setExpirationDate(expirationDate);
        }
    	return identifier;
    }

    /**
	 * Gets the PersonName from the PID segment
	 *
	 * @return the {@link PersonName} of the patient
	 */
    public PersonName getPatientName() throws HL7Exception {
    	PersonName pName=new PersonName();

    	pName.setSuffix(pid.getPatientName(0).getSuffixEgJRorIII().getValue()); //patient name suffix
    	pName.setSecondName(pid.getPatientName(0).getMiddleInitialOrName().getValue()); //patient middle name
    	pName.setLastName(pid.getPatientName(0).getFamilyLastName().getFamilyName().getValue()); //patient last name
    	pName.setFirstName(pid.getPatientName(0).getGivenName().getValue()); //patient name first
    	pName.setPrefix(pid.getPatientName(0).getPrefixEgDR().getValue());
    	pName.setNameTypeCode(pid.getPatientName(0).getNameTypeCode().getValue());
    	pName.setNameRepresentationCode(pid.getPatientName(0).getNameRepresentationCode().getValue());
    	pName.setDegree(pid.getPatientName(0).getDegreeEgMD().getValue());

    	return pName;
    }


    /**
	 * Gets the PersonName from the MRG segment
	 *
	 * @return the {@link PersonName} of the merge patient
	 */
    public PersonName getMrgPatientName() throws HL7Exception {
    	PersonName pName=new PersonName();
    	pName.setSuffix(mrg.getPriorPatientName(0).getSuffixEgJRorIII().getValue()); //patient name suffix
    	pName.setSecondName(mrg.getPriorPatientName(0).getMiddleInitialOrName().getValue()); //patient middle name
    	pName.setLastName(mrg.getPriorPatientName(0).getFamilyLastName().getFamilyName().getValue()); //patient last name
    	pName.setFirstName(mrg.getPriorPatientName(0).getGivenName().getValue()); //patient name first
    	pName.setPrefix(mrg.getPriorPatientName(0).getPrefixEgDR().getValue());
    	pName.setNameTypeCode(mrg.getPriorPatientName(0).getNameTypeCode().getValue());
    	pName.setNameRepresentationCode(mrg.getPriorPatientName(0).getNameRepresentationCode().getValue());
    	pName.setDegree(mrg.getPriorPatientName(0).getDegreeEgMD().getValue());
    	return pName;
    }

    /**
	 * Get the mother's PersonName from the PID segment
	 *
	 * @return the {@link PersonName} of the monther's maiden name
	 */
    public PersonName getMotherMaidenName() throws HL7Exception {
    	PersonName mName=new PersonName();
		mName.setLastName(pid.getMotherSMaidenName(0).getFamilyLastName().getFamilyName().getValue());
		mName.setSecondName(pid.getMotherSMaidenName(0).getMiddleInitialOrName().getValue());
		mName.setFirstName(pid.getMotherSMaidenName(0).getGivenName().getValue());
		mName.setPrefix(pid.getMotherSMaidenName(0).getPrefixEgDR().getValue());
		mName.setSuffix(pid.getMotherSMaidenName(0).getSuffixEgJRorIII().getValue());
		mName.setDegree(pid.getMotherSMaidenName(0).getDegreeEgMD().getValue());
		mName.setNameTypeCode(pid.getMotherSMaidenName(0).getNameTypeCode().getValue());
		mName.setNameRepresentationCode(pid.getMotherSMaidenName(0).getNameRepresentationCode().getValue());
		return mName;
    }

    /**
	 * Gets the Patient Alias Name from the PID segment
	 *
	 * @return the patient alias name
	 */
    public List<PersonName> getPatientAliases() throws HL7Exception {
        XPN[] aliases = pid.getPatientAlias();
        List<PersonName> aNames = null;
        if (aliases != null && aliases.length > 0) {
            aNames = new ArrayList<PersonName>(aliases.length);
            for (XPN alias : aliases) {
                PersonName aName=new PersonName();
                aName.setLastName(alias.getFamilyLastName().getFamilyName().getValue());
                aName.setSecondName(alias.getMiddleInitialOrName().getValue());
                aName.setFirstName(alias.getGivenName().getValue());
                aName.setPrefix(alias.getPrefixEgDR().getValue());
                aName.setSuffix(alias.getSuffixEgJRorIII().getValue());
                aName.setDegree(alias.getDegreeEgMD().getValue());
                aName.setNameTypeCode(alias.getNameTypeCode().getValue());
                aName.setNameRepresentationCode(alias.getNameRepresentationCode().getValue());
                aNames.add(aName);
            }
        }
		return aNames;
    }

    /**
	 * Gets the DriversLincense from the PID segment
	 *
	 * @return the DriversLicense
	 */
    public DriversLicense getDriversLicense() {
    	DriversLicense lic = new DriversLicense();
    	lic.setLicenseNumber(pid.getDriverSLicenseNumberPatient().getDriverSLicenseNumber().getValue());
    	lic.setExpirationDate(DateUtil.convertHL7DateToCalender(pid.getDriverSLicenseNumberPatient().getExpirationDate().getValue()));
    	lic.setIssuingState(pid.getDriverSLicenseNumberPatient().getIssuingStateProvinceCountry().getValue());
        return lic;
    }

    /**
	 * Gets the race from the PID segment for a particular patient
	 *
	 * @return the race of the patient
	 */
    public String getRace()throws HL7Exception{
    	String race = pid.getRace(0).getText().getValue();
    	return race;
    }

    /**
	 * Gets the primary language from the PID segment for a particular patient
	 *
	 * @return the primary language
	 */
    public String getPrimaryLanguage(){
    	return pid.getPrimaryLanguage().getText().getValue();
    }

    /**
	 * Gets the martial status from the PID segment for a particular patient
	 *
	 * @return the martial status
	 */
    public String getMartialStatus(){
    	return pid.getMaritalStatus().getText().getValue();
    }

    /**
	 * Gets the Religion from the PID segment for a particular patient
	 *
	 * @return the religion
	 */
    public String getReligion(){
    	return pid.getReligion().getText().getValue();
    }

    /**
	 * Gets the ethnic group from the PID segment for a particular patient
	 *
	 * @return the ethnic group
	 */
    public String getEthnicGroup() throws HL7Exception{
    	String ethnicGroup = pid.getEthnicGroup(0).getText().getValue();
    	return ethnicGroup;
    }


    /**
	 * Gets the birth place from the PID segment for a particular patient
	 *
	 * @return the birth place
	 */
    public String getBirthPlace(){
		return pid.getBirthPlace().getValue();
    }

    /**
	 * Gets the birth order from the PID segment for a particular patient
	 *
	 * @return the birth order
	 */
    public int getBirthOrder(){
    	if (pid.getBirthOrder().getValue()==null)
    		return 0;

    	return Integer.valueOf(pid.getBirthOrder().getValue());

    }

    /**
	 * Gets the citizen ship from the PID segment for a particular patient
	 *
	 * @return the citizenship
	 */
    public String getCitizenShip() throws HL7Exception{
    	String citizenShip = pid.getCitizenship(0).getIdentifier().getValue();
    	return citizenShip;

    }

    /**
	 * Gets the death date from the PID segment for a particular patient
	 *
	 * @return the death date
	 */
    public Calendar getDeathDate(){
    	return DateUtil.convertHL7DateToCalender(pid.getPatientDeathDateAndTime().getTimeOfAnEvent().getValue());
    }

    /**
	 * Gets the death indicator from the PID segment for a particular patient
	 *
	 * @return <code>true</code> if dead
	 */
    public String getDeathIndicator(){
    	return pid.getPatientDeathIndicator().getValue();
    }

    /**
	 * Gets the Address list from the PID segment for a particular patient
	 *
	 * @return the list {@link Address}
	 */
    public List<Address> getAddressList() {
        List<Address> addressList = new ArrayList<Address>();
        XAD[] xads = pid.getPatientAddress();
        for(XAD xad: xads) {
            Address address = new Address();
            address.setAddCity(xad.getCity().getValue());
            address.setAddCountry(xad.getCountry().getValue());
            address.setAddCounty(xad.getCountyParishCode().getValue());
            address.setAddLine1(xad.getStreetAddress().getValue());
            address.setAddLine2(xad.getOtherDesignation().getValue());
            address.setAddState(xad.getStateOrProvince().getValue());
            address.setAddType(_mapAddressToBase(xad.getAddressType().getValue()));
            address.setAddZip(xad.getZipOrPostalCode().getValue());
            addressList.add(address);
        }
        return addressList;
    }

    /**
     * Converts an address type from a HL7 string encode to {@link AddressType}.
     *
     * @param value the address type in HL7 string encode to be converted from
     * @return an address type in {@link AddressType}
     */
    private SharedEnums.AddressType _mapAddressToBase(String value) {
    	return SharedEnums.AddressType.hl7ValueOf(value);
    }

    /**
     * Gets the Sex Type.
     *
     * @return the {@link SexType} of the patient
     */
    public SharedEnums.SexType getSexType() {
        String sex = pid.getSex().getValue();
        if (sex == null) return null;
    	return SharedEnums.SexType.getByString( sex );
    }

    /**
     * Gets the birth date
     *
     * @return the birth date
     */
    public Calendar getBirthDate() {
        String dob = pid.getDateTimeOfBirth().getTimeOfAnEvent().getValue();
        return DateUtil.convertHL7DateToCalender(dob);
    }

    /**
	 * Gets the list of vists from the PID segment for a particular patient
	 *
	 * @return a list of {@link Visit}
	 */
    public List<Visit> getVisitList() {
        if(pv1 == null) {
            return null;
        }
        List<Visit> visitList = new ArrayList<Visit>();

        String systemId = getHomeSystem();
        String visitId = pv1.getVisitNumber().getID().getValue();
        if (!StringUtil.goodString(visitId)) {
            //generate one if null
        }
        Visit visit = new Visit(systemId, visitId);
        visit.setReason(getVisitReason());
        visit.setVisitEndTimestamp(getEndDate());
        Date startDate = getStartDate();
        visit.setVisitStartTimestamp( startDate == null ? new GregorianCalendar().getTime() : startDate );
        visitList.add(visit);
        return visitList;
    }

    /**
	 * Gets the Visit list from the pv1 segment for a particular patient
	 *
	 * @return a list of {@link Visit}
	 */
    public List<Visit> getMrgVisitList() {

        List<Visit> visitList = new ArrayList<Visit>();
        String systemId = mrg.getPriorVisitNumber().getAssigningFacility().getNamespaceID().getValue();
        String visitId = mrg.getPriorAlternateVisitID().getID().getValue();
        if (!StringUtil.goodString(visitId)) {
            //generate one if null
        }
        Visit visit = new Visit(systemId, visitId);
        visit.setReason(getVisitReason());
        visit.setVisitEndTimestamp(getEndDate());
        Date startDate = getStartDate();
        visit.setVisitStartTimestamp( startDate == null ? new GregorianCalendar().getTime() : startDate );
        visitList.add(visit);
        return visitList;
    }

    /**
	 * Gets the ProblemList from the PID segment for a particular patient
	 * @return List<Problem>
	 */
    private List<Problem> getProblemList() {
        List<Problem> probList = new ArrayList<Problem>();
        if(dg1 != null) {
            Problem problem = new Problem();

            problem.setProbCode(dg1.getDiagnosisCodeDG1().getIdentifier().getValue());
            problem.setProbCodeSystem(dg1.getDiagnosisCodingMethod().getValue());
            //problem.setProbCodeVersion();
            problem.setProbName(dg1.getDiagnosisDescription().getValue());
            probList.add(problem);
        }

        return probList;
    }


    /**
	 * Gets the ProviderList from the PID segment for a particular patient
	 * @return List<Provider>
	 */
    private List<Provider> getProviderList() {
        List<Provider> providerList = new ArrayList<Provider>();
        XCN[] xcns = pv1.getAttendingDoctor();
        for(XCN xcn: xcns){
            Provider provider = new Provider();
            provider.setProviderId(xcn.getIDNumber().getValue());
            provider.setProvNameFirst(xcn.getGivenName().getValue());
            provider.setProvNameLast(xcn.getFamilyLastName().getName());
            provider.setProvNameMiddle(xcn.getMiddleInitialOrName().getValue());
            provider.setProvNameSuffix(xcn.getSuffixEgJRorIII().getValue());
            provider.setProvNameTitle(xcn.getPrefixEgDR().getValue());

            providerList.add(provider);
        }
        return providerList;
    }

    private String getVisitReason() {
        if(pv2 == null) {
            return  null;
        }
        return pv2.getAdmitReason().getText().getValue();
    }

    private Date getEndDate() {
        String dob = pv1.getDischargeDateTime().getTimeOfAnEvent().getValue();
        return DateUtil.convertHL7Date(dob);
    }

    private Date getStartDate() {
        return DateUtil.convertHL7Date(pv1.getAdmitDateTime().getTimeOfAnEvent().getValue());
    }

    /**
	 * Gets the ssn from the PID segment for a particular patient
	 * @return String
	 */

    public String getSsn() {
        return pid.getSSNNumberPatient().getValue();
    }
    
	/**
	 * Return the VIP indicator.  
	 * 
	 * @return the VIP indicator
	 */
	public String getVipIndicator() {
		if (pv1 == null) return null;
		return pv1.getVIPIndicator().getValue();
	}

    /**
	 * Gets the PhoneList from the PID segment for a particular patient
	 * @return List<PhoneNumber>
	 */
    public List<PhoneNumber> getPhoneList() {
        List<PhoneNumber> phoneList = new ArrayList<PhoneNumber>();

        try {
            PhoneNumber home = _getPhoneNumber(pid.getPhoneNumberHome(0));
            if(home != null){
                home.setType(SharedEnums.PhoneType.HOME);
                phoneList.add(home);
            }
        } catch (HL7Exception e) {
        }

        try {
            PhoneNumber business = _getPhoneNumber(pid.getPhoneNumberBusiness(0));
            if(business != null){
            	business.setType(SharedEnums.PhoneType.WORK);
            	phoneList.add(business);
            }
        } catch (HL7Exception e) {
        }
        return phoneList;
    }

    private PhoneNumber _getPhoneNumber(XTN xtn) {
        PhoneNumber number = new PhoneNumber();
        // number being sent as string by CPR
        if(xtn == null)return null;
        String sNum = xtn.get9999999X99999CAnyText().getValue();
        if(sNum == null) return null;
        number.setAreaCode(_parseAreaCode(sNum));
        number.setExtension(_parseExtension(sNum));
        number.setNote(_parseNote(sNum));
        number.setNumber(_parseNumber(sNum));
        if(xtn.getEmailAddress() != null)
        	number.setEmail(xtn.getEmailAddress().getValue());
        return number;
    }

    private String _parseNumber(String sNum) {
        int sIndex = sNum.indexOf(")");
        if(sIndex < 0)
            return sNum.substring(0, 8);
        else
            return sNum.substring(sIndex+1, sIndex+9);
    }

    private String _parseNote(String sNum) {
        int sIndex = sNum.indexOf("C");
        if(sIndex < 0 ) return null;
        else return sNum.substring(sIndex+ 1);
    }

    private String _parseExtension(String sNum) {
        int sIndex = sNum.indexOf("X");
        if(sIndex < 0 ) return null;
        else {
            int eIndex = sNum.indexOf("C");
            if(eIndex < 0 )
                return sNum.substring(sIndex +1);
            else
                return sNum.substring(sIndex + 1, eIndex );
        }
    }

    private String _parseAreaCode(String sNum) {
        int sIndex = sNum.indexOf("(");
        if(sIndex < 0 ) return null;
        else
            return sNum.substring(sIndex+1, sIndex + 4);
    }




    public String getHomeSystem() {
        return msh.getSendingFacility().getNamespaceID().getValue();
    }
}
