/* Copyright 2009 Misys PLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License. 
 */

package org.openhealthexchange.common.audit;

import java.io.StringWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.openhealthexchange.common.audit.jaxb.ActiveParticipantType;
import org.openhealthexchange.common.audit.jaxb.AuditMessage;
import org.openhealthexchange.common.audit.jaxb.AuditSourceIdentificationType;
import org.openhealthexchange.common.audit.jaxb.CodedValueType;
import org.openhealthexchange.common.audit.jaxb.EventIdentificationType;
import org.openhealthexchange.common.audit.jaxb.ObjectFactory;
import org.openhealthexchange.common.audit.jaxb.ParticipantObjectIdentificationType;
import org.openhealthexchange.common.audit.jaxb.TypeValuePairType;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.misyshealthcare.connect.base.audit.ActiveParticipant;
import com.misyshealthcare.connect.base.audit.AuditCodeMappings;

/** 
 * A class for generating IHE ATNA log messages.
 * 
 * The exact format of the messages is described in RFC 3881, Dicomm 
 * Supplimental 95 sec A.1.2, and the IHE IT Technical Framework Vol 2 sec. 3.20. <p />
 * 
 * This class is used by the different audit message classes to generate the
 * appropriately formatted XML which is then sent using the configured audit
 * transport.  A new factory should  be generated for each message you send
 * since the factory maintains internal state to build the messsage. <p />
 * 
 * To use: make a new audit object factory, add the approriate data, call
 * getAuditMessage to get the complete JAXB audit message.  This last step
 * will normally be done by the IheAuditTrail base class. <p />
 * 
 * In general, the data required for an appropriately formatted audit are as follows:
 * <ol>
 * <li>Event Identification - what was done (1)</li>
 * <li>Active Participant Identification - by whom (1 or more)</li>
 * <li>Network Access Point Identification - initiated from where(paired 1:1 with active participant)</li>
 * <li>Audit Source Identification - using which server (1)</li>
 * <li>Participant Object Identification - to what record (0 or more)</li>
 * </ol>
 * Each of the appropritate audit object factory member functions should be called
 * with the required data to load the factory with all the appropriate data.  
 * The specifics of which data elements are required for the different possible
 * message types are defined in the above documents.<p />
 * 
 * Basically this class is simply a layer on top of the JAXB generated classes to make
 * building formatted messages much easier.  The factory deals with all of the 
 * details of getting the correct data in the correct places.  It also takes care of
 * generating all of the correct JAXB parts and putting them all together.  As such
 * all of the functions throw JAXB exceptions.  Please check these as it does internal
 * error checking and throws the exceptions when incorrrect data is sent in.  (Or when
 * not enough data is given.)
 * 
 * @see IheAuditTrail
 * @author Josh Flachsbart
 * @version 1.0 - Nov 13, 2005
 */
public class AuditObjectFactory {
	private ObjectFactory messageFactory;
	private EventIdentificationType eventId = null;
	private List<ActiveParticipantType> activeParticipants;
	private List<AuditSourceIdentificationType> sourceIds;
	private List<ParticipantObjectIdentificationType> objectIds;
	
	private String     dIE; // Default Id Encoding
	private String     dNE; // Default NameEncoding
	private IMessageTransmitter messenger;
	
	/** 
	 * Generates and initializes a blank audit message factory. 
	 * 
	 * @param messenger the {@link IMessageTransmitter} 
	 */
	public AuditObjectFactory(IMessageTransmitter messenger) {
		messageFactory = new ObjectFactory();
		activeParticipants = new ArrayList<ActiveParticipantType>();
		sourceIds = new ArrayList<AuditSourceIdentificationType>();
		objectIds = new ArrayList<ParticipantObjectIdentificationType>();
		
		this.messenger = messenger;
		dIE  = messenger.getAuditTrailDescription().getIdEncoding();
		dNE  = messenger.getAuditTrailDescription().getNameEncoding();
	}
	
	/** 
	 * Gets the audit message from the properly formatted
	 * audit object factory. <p />
	 * 
	 * Please note that to be properly formatted, you need to
	 * have added the event id type, at least one active
	 * participant and at least one audit source id.  Optionally
	 * you may have participant object ids, and multiple source ids
	 * and multiple active participants. <p />
	 * 
	 * Please see the documents mentioned in the class description
	 * to know which message types require which events.
	 * 
	 * @return The complete Audit Message ready to be marshalled.
	 * @throws JAXBException if there is a formatting error, e.g. 
	 * did not call the appropriate add functions. 
	 */
	@SuppressWarnings("unchecked")
	public AuditMessage getAuditMessage() 
		throws JAXBException
	{
		// Check that the current state is valid.
		if (eventId == null) throw new JAXBException("Set event id before getting audit message.");
		if (activeParticipants.isEmpty()) throw new JAXBException("Add at least one active participant.");
		if (sourceIds.isEmpty()) throw new JAXBException("Add at least one source ID.");
			
		// Build the message
		AuditMessage auditMessage = messageFactory.createAuditMessage();
		auditMessage.setEventIdentification(eventId);
		List list = auditMessage.getActiveParticipant();
		for (ActiveParticipantType apt: activeParticipants) list.add(apt);
		list = auditMessage.getAuditSourceIdentification();
		for (AuditSourceIdentificationType asit: sourceIds) list.add(asit);
		list = auditMessage.getParticipantObjectIdentification();
		for (ParticipantObjectIdentificationType poit: objectIds) list.add(poit);
		return auditMessage;
	}
	
	/** 
	 * Sends the properly formatted audit object. <p />
	 * 
	 * Please note that to be properly formatted, you need to
	 * have added the event id type, at least one active
	 * participant and at least one audit source id.  Optionally
	 * you may have participant object ids, and multiple source ids
	 * and multiple active participants. <p />
	 * 
	 * Please see the documents mentioned in the class description
	 * to know which message types require which events.
	 * 
	 * @return The complete Audit Message ready to be marshalled.
	 * @throws JAXBException if there is a formatting error, e.g. 
	 * did not call the appropriate add functions. 
	 */
	public void sendAuditMessage()
		throws JAXBException
	{
		// get the message:
		AuditMessage auditMessage = getAuditMessage();
		JAXBContext messageContext = JAXBContext.newInstance("org.openhealthexchange.common.audit.jaxb");
		Marshaller marshaller = messageContext.createMarshaller();
		marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );

		// format the message as a string:
		StringWriter messageWriter = new StringWriter();
		marshaller.marshal( auditMessage, messageWriter );
		String message = messageWriter.toString();
		
		// send the message to the configured
		messenger.sendMessage(message);
	}
	
	/** 
	 * Generates the source id for this message.  Should be parameterized from the 
	 * configuration file.
	 * 
	 * @throws JAXBException Thrown if the factory is unable to create the ID.
	 */
	protected void addAuditSourceId(String enterpriseSiteID, String auditSourceID, Collection<AuditCodeMappings.AuditSourceType> sourceTypes)
		throws JAXBException
	{
		AuditSourceIdentificationType sourceId = messageFactory.createAuditSourceIdentificationType();
		if (enterpriseSiteID != null) sourceId.setAuditEnterpriseSiteID(enterpriseSiteID);
		sourceId.setAuditSourceID(auditSourceID);
        //add auditSourceTypes
        List auditSourceTypes = sourceId.getAuditSourceTypeCode();
        for(AuditCodeMappings.AuditSourceType type : sourceTypes) {
            AuditSourceIdentificationType.AuditSourceTypeCodeType auditSourceType = messageFactory.createAuditSourceIdentificationTypeAuditSourceTypeCodeType();
            auditSourceType.setCode( type.getValue() );
            auditSourceTypes.add( auditSourceType );
        }
        sourceIds.add(sourceId);
	}
	
	/** 
	 * Adds an active participant for the messages that require it. <p />
	 * 
	 * In general there will always be one for the actual machine.  In general
	 * there will be another for the doctor doing the action of the other
	 * machine involved in the case of security audits.
	 * 
	 * @param activeParticipant The active participant to add.
	 * @throws JAXBException If there is an error in the format of the active participant.
	 */
	@SuppressWarnings("unchecked")
	protected void addActiveParticipant( ActiveParticipant activeParticipant ) 
		throws JAXBException 
	{
		ActiveParticipantType participant = messageFactory.createActiveParticipantType();
		// Required:
		participant.setUserID(activeParticipant.getUserId());
		participant.setUserIsRequestor(activeParticipant.isRequestor());
		participant.getRoleIDCode().add(createCodedValueType(activeParticipant.role.getValue()));
		// Optional:
		if (activeParticipant.getAltUserId() != null)
			participant.setAlternativeUserID(activeParticipant.getAltUserId());
		if (activeParticipant.getAccessPointId() != null)   
			participant.setNetworkAccessPointID(activeParticipant.getAccessPointId());
		if (activeParticipant.getAccessPointTypeCode() != null)   
			participant.setNetworkAccessPointTypeCode(activeParticipant.getAccessPointTypeCode().getValue());
		activeParticipants.add(participant);
	}

	/** 
	 * Adds a participant object for the messages that require it. <p />
	 * 
	 * In general there will always be one for the patient when a patient's 
	 * records are being accessed.  In general there will be on for the 
	 * record when a patient's records are being accessed. <p />
	 * 
	 * This only takes patients for now.  This should probably change in the future.
	 * 
	 * @param activeParticipant The active participant to add.
	 * @throws JAXBException If there is an error in the format of the active participant.
	 */
	@SuppressWarnings("unchecked")
	protected void addParticipantObject( ParticipantObject participantObject ) 
		throws JAXBException 
	{
		ParticipantObjectIdentificationType participant = messageFactory.createParticipantObjectIdentificationType();
		
		ParticipantObjectIdentificationType.ParticipantObjectIDTypeCodeType participantID = messageFactory.createParticipantObjectIdentificationTypeParticipantObjectIDTypeCodeType();
		participant.setParticipantObjectTypeCode(participantObject.typeCode.getValue());
		participant.setParticipantObjectTypeCodeRole(participantObject.role.getValue());
		participant.setParticipantObjectID(participantObject.getId(dIE));
		participant.setParticipantObjectName(participantObject.getName(dNE));
		participantID.setCode(participantObject.idTypeCode.getValue());
		if (participantObject.idTypeCode.getCodeSystem() != null) {
			participantID.setCodeSystem(participantObject.idTypeCode.getCodeSystem());
			participantID.setDisplayName(participantObject.idTypeCode.getName());
		}
		
		if (participantObject.getDetail() != null) {
			TypeValuePairType tvpt = messageFactory.createTypeValuePairType();
			tvpt.setType("MessageID");
			tvpt.setValue(participantObject.getDetail().getBytes());
			participant.getParticipantObjectDetail().add(tvpt);
		}
		if (participantObject.getSensitivity() != null)
			participant.setParticipantObjectSensitivity(participantObject.getSensitivity());
		participant.setParticipantObjectIDTypeCode(participantID);
		
		//Somehow, if ParticipantObjectName is present, it suppresses ParticipantObjectQuery
		if (participantObject.getQuery() != null) {
			participant.setParticipantObjectQuery(participantObject.getQuery().getBytes());
		}		
		
		objectIds.add(participant);
	}

	/** 
	 * Used to set the type of the event. <p />
	 * 
	 * Each event require one and only one event id.  The may
	 * have a type code, to tell you what type of that event
	 * it is, but it is not required and may be null.
	 * 
	 * @param eventIdType A class describing this event.
	 * @throws JAXBException If there is a formatting error in the event id.
	 */
	@SuppressWarnings("unchecked")
	protected void setEventIdType( EventId eventIdType )
		throws JAXBException 
	{		
		eventId = messageFactory.createEventIdentificationType();
		eventId.setEventID(createCodedValueType(AuditCodeMappings.getCodeMapping(eventIdType.eventId)));
		eventId.setEventActionCode( eventIdType.actionCode.getValue() );
		eventId.setEventDateTime(Calendar.getInstance());
		eventId.setEventOutcomeIndicator(new BigInteger(eventIdType.outcome.getValue()));
		if (eventIdType.typeCode != null)
			eventId.getEventTypeCode().add(createCodedValueType(AuditCodeMappings.getCodeMapping(eventIdType.typeCode)));
	}

	/** 
	 * Used internally to fill the JAXB code.
	 * 
	 * @param mapping
	 * @return the {@link CodedValueType}
	 * @throws JAXBException
	 */
	private CodedValueType createCodedValueType(AuditCodeMappings mapping)
		throws JAXBException
	{
		CodedValueType roll = messageFactory.createCodedValueType();
		roll.setCode(mapping.codeValue);
		if (mapping.codingScheme != null) roll.setCodeSystemName(mapping.codingScheme);
		if (mapping.codeMeaning != null) roll.setDisplayName(mapping.codeMeaning);
		return roll;
	}

}
