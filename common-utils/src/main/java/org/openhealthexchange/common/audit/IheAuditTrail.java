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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.openhealthexchange.openpixpdq.data.Patient;
import org.openhealthexchange.openpixpdq.data.PatientIdentifier;
import org.openhealthexchange.openpixpdq.data.PersonName;

import com.misyshealthcare.connect.base.audit.ActiveParticipant;
import com.misyshealthcare.connect.base.audit.AuditCodeMappings;
import com.misyshealthcare.connect.base.audit.IAuditTrail;
import com.misyshealthcare.connect.base.audit.AuditCodeMappings.ActiveParticipantIds;
import com.misyshealthcare.connect.base.audit.AuditCodeMappings.AuditEventIds;
import com.misyshealthcare.connect.base.audit.AuditCodeMappings.AuditSourceType;
import com.misyshealthcare.connect.base.audit.AuditCodeMappings.AuditTypeCodes;
import com.misyshealthcare.connect.base.audit.AuditCodeMappings.EventActionCode;
import com.misyshealthcare.connect.base.audit.AuditCodeMappings.NetworkAccessPointType;
import com.misyshealthcare.connect.base.audit.AuditCodeMappings.ParticipantObjectIdTypeCode;
import com.misyshealthcare.connect.base.audit.AuditCodeMappings.ParticipantObjectRoleCode;
import com.misyshealthcare.connect.base.audit.AuditCodeMappings.ParticipantObjectTypeCode;
import com.misyshealthcare.connect.base.audit.AuditCodeMappings.SecurityAlertType;
import com.misyshealthcare.connect.base.audit.AuditCodeMappings.SuccessCode;
import com.misyshealthcare.connect.net.ConnectionFactory;
import com.misyshealthcare.connect.net.IConnectionDescription;
import com.misyshealthcare.connect.net.Identifier;
import com.misyshealthcare.connect.net.StandardConnectionDescription;
import com.misyshealthcare.connect.util.LibraryConfig;

/**
 * The base implementation of an audit message class.  
 * 
 * Each IHE Actor requires its own implementation of this base class.
 * This base class, <em>can</em> be used independently, but that is not
 * the standard method, and will not be ATNA compliant. <p />
 * 
 * Implementations of the audit log class for specific IHE actors must be
 * instantiated when the actor starts, and stop must be called when the 
 * actor ends, and that instation must not be used again.  If the same actor 
 * restarts, a new instance must be made.  Calling stop will cause that 
 * specific instance of the audit trail to cease functioning. <p />
 * 
 * The following ATNA required messages are currently implemented by the given classes.
 * 
 * <dl>
 * <dt>Actor-start-stop
 * <dd>All actors implement this via the base class.  It is generally unnecessary to re implement it for the different actors.  Dicom Supp 95 "Application Activity"
 * <dt>Node-authentication-failure
 * <dd>Only for secure node TLS failure, not for user login failure.  Secure Node Actor.  Dicom Sup 95 "Security Alert"
 * <dt>Patient-record-event
 * <dd>Patient record created modified or accessed.  Document Consumer and Document Source.  Dicom Sup 95 "Patient Record"
 * <dt>Import
 * <dd>Generally used whenever a document is sent somewhere.  Document Source.  Dicom Sup 95 "Data Import"
 * <dt>Export
 * <dd>Patient record created recieved from somewhere.  Document Consumer.  Dicom Sup 95 "Data Export"
 * <dt>Procedure-record-event
 * <dd>Procedure record created accessed modified or deleted.  Document Consumer and Document Source.  Dicom Sup 95 "Procedure Record"
 * <dt>Security-administration
 * <dd>Change of security roles, user accounts, authentication ability, and other configuration changes.  In addition, user authentication, failure, and signoff.  Secure Node. Dicom Sup 95 "Security Alert"
 * </dl>
 *
 * @see AuditObjectFactory
 * @author Josh Flachsbart
 * @version 1.0 - Oct 27, 2005
 */
public class IheAuditTrail implements IAuditTrail {
	
	static Logger LOG = Logger.getLogger(IheAuditTrail.class);
	private String actorName;
	private List<IMessageTransmitter> messengers = null;

	/** 
	 * Handles creation of a logging instance for a given actor.
	 * 
	 * Must be called by implementing sub-classes with the appropriate
	 * actor name.  <em>Can</em> be used directlly, but will not produce
	 * ATNA compliant messages.
	 * 
	 * @param actorName Name of the ATNA actor that this will generate an audit trail for.
	 */
	public IheAuditTrail(String actorName, Iterable<IConnectionDescription> repositories) {
		this.actorName = actorName;
		// Prepare audit streams:
		messengers = new ArrayList<IMessageTransmitter>();
		for (IConnectionDescription repository: repositories) {
			AuditTrailDescription description = new AuditTrailDescription(repository);
			String type = description.getType();
			if (type.equalsIgnoreCase(AuditTrailDescription.BSD)) {
				messengers.add(new BsdMessenger(description));
                //messengers.add(new Log4JMessenger(description));
            } else if (type.equalsIgnoreCase(AuditTrailDescription.LOG4J)) {
				messengers.add(new Log4JMessenger(description));
			} else if (type.equalsIgnoreCase(AuditTrailDescription.RELIABLE)) {
				messengers.add(new RSyslogMessenger(description));
			}
		}
	}
	
	/** 
	 * Adds the required elements and format the message in XML and send it.
	 * 
	 * To send a message, add additional information that is required by the
	 * message, then call format and log to do the rest.  It will add the 
	 * audit source id, and active participant id for each audit repository
	 * (since they might have different local info, e.g. if they are in 
	 * different RHIOs) and send the message.
	 * 
p	 * @param factory
	 * @param requestor
	 */
	private void formatAndLog(IMessageTransmitter messenger, AuditObjectFactory factory, boolean requestor,
                              ActiveParticipantIds ourRole, AuditSourceType ... sourceTypes) throws JAXBException {
		AuditTrailDescription desc = messenger.getAuditTrailDescription();

        // add ourselves as an active participant:
		ActiveParticipant us = new ActiveParticipant(desc.getFacilityName()+"|" + desc.getApplicationName(), actorName, desc.getIp());
		us.setRequestor(requestor);
		us.role = ourRole; 
		us.setAccessPointTypeCode(NetworkAccessPointType.IPAddress);
		factory.addActiveParticipant(us);

        // add the source id:
        Set<AuditSourceType> auditSourceTypes = new HashSet<AuditSourceType>();
        if (sourceTypes != null) {
            for (AuditSourceType sourceType : sourceTypes) {
                if (sourceType != null)
                    auditSourceTypes.add( sourceType );
            }
        }                               
        factory.addAuditSourceId( desc.getEnterpriseSiteId(), desc.getAuditSourceId(), auditSourceTypes);

        // send the message.
		factory.sendAuditMessage();
	}

	/////**************   DATA HELPER FUNCTIONS   *********************

	public ActiveParticipant getUser() {
		ActiveParticipant application = null;
		LibraryConfig.ILogContext context = LibraryConfig.getInstance().getLogContext();

        if (context != null) {
        	application = new ActiveParticipant();
        	application.role = ActiveParticipantIds.Source;
        	application.setRequestor(true);
        	application.setUserId(context.getUserId());
        	application.setAltUserId(context.getUserSystem());
        	application.setUserName(context.getUserName());
        	application.setAccessPointId(context.getClientAddress());
        	application.setAccessPointTypeCode(NetworkAccessPointType.IPAddress);
        	application.setAuditSourceType(AuditSourceType.EndUserGui);
		}
		return application;
	}
	
	public ActiveParticipant getMedia(ActiveParticipantIds role, String mediaDescription) {
		ActiveParticipant media = new ActiveParticipant();
		media.role = role;
		media.setRequestor(false);
		media.setUserId(mediaDescription);
		media.setAltUserId(null);
		media.setAccessPointId(null);
		media.setAuditSourceType(null);
		return media;
	}
	
	
	/////**************   DICOM MESSAGES   *********************

	/** 
	 * DICOM Supp 95 message A.1.3.1 (p. 15): Application Activity <p />
	 * 
	 * Requires an event id with the parameters below and a single participant
	 * which is the application and a single audit source.  The participant and
	 * audit source are provided by the format and log function. <p />
	 * 
	 * Note that we could add the application launcher to this message.
	 * 
	 * @param message The type of application activity that it is.  Generally start or stop.
	 */
	protected void applicationActivity(AuditTypeCodes message) throws JAXBException {
		for (IMessageTransmitter messenger: messengers) {
			AuditObjectFactory factory = new AuditObjectFactory(messenger);
			factory.setEventIdType(new EventId(AuditEventIds.ApplicationActivity, message, EventActionCode.Execute, SuccessCode.Success));
			formatAndLog(messenger, factory, false, ActiveParticipantIds.Application);
		}
	}
	
	/** 
	 * DICOM Supp 95 message A.1.3.11 (p. 29): Patient Record <p />
	 * 
	 * @param source the information about the source of patient.  null if not available.
	 * @param patient the information about the patient
	 * @param typeCode the type of message 
	 * @param action What they did with the patient record.
	 */
	private void patientRecord(ActiveParticipant source, ParticipantObject patient, 
			AuditCodeMappings.AuditTypeCodes typeCode, EventActionCode action) throws JAXBException {
		for (IMessageTransmitter messenger : messengers) {
			AuditObjectFactory factory = new AuditObjectFactory(messenger);
			if (patient != null) {
				patient.typeCode = ParticipantObjectTypeCode.Person;
				patient.role = ParticipantObjectRoleCode.Patient;
				patient.idTypeCode = ParticipantObjectIdTypeCode.Patient;
				factory.addParticipantObject(patient);
			}
			
			factory.setEventIdType(new EventId(AuditEventIds.PatientRecord, typeCode, action, SuccessCode.Success));
			if (source != null) factory.addActiveParticipant(source);
			
			formatAndLog(messenger, factory, false, ActiveParticipantIds.Destination, source.getAuditSourceType());
		}
	}
	
	/** 
	 * DICOM Supp 95 message :Data Import <p />
	 * 
	 * @param source the information about the source of patient.  null if not available.
	 * @param patient the information about the patient
	 * @param typeCode the type of message 
	 * @param action What they did with the patient record.
	 */
	private void documentImport(ActiveParticipant source, ParticipantObject patient, ParticipantObject set, 
			AuditCodeMappings.AuditTypeCodes typeCode) throws JAXBException {
		for (IMessageTransmitter messenger : messengers) {
			AuditObjectFactory factory = new AuditObjectFactory(messenger);
			if (patient != null) {
				patient.typeCode = ParticipantObjectTypeCode.Person;
				patient.role = ParticipantObjectRoleCode.Patient;
				patient.idTypeCode = ParticipantObjectIdTypeCode.Patient;
				factory.addParticipantObject(patient);
			}
			if(set != null) factory.addParticipantObject(set);
			
			factory.setEventIdType(new EventId(AuditEventIds.Import, typeCode, EventActionCode.Create, SuccessCode.Success));
			if (source != null) factory.addActiveParticipant(source);
			
			formatAndLog(messenger, factory, false, ActiveParticipantIds.Destination, source.getAuditSourceType());
		}
	}
	
	/** 
	 * DICOM Supp 95 message A.1.3.4 (p. 19): Data Export <p />
	 * 
	 * This should be used when data leaves control of the system. (E.g. xdm/xdr) <p />
	 * 
	 * Requires an event id with the parameters below.  Also requires the 
	 * repository as an active participant, if known, and a description of the 
	 * patient accessed as a participant object.  This is the patient id
	 * and additional information about the document set affected if available.
	 * The SubmisstionSet is required. <p />
	 * 
	 * In addition, a single participant which is the application and a single 
	 * audit source.  The participant and audit source are provided by the 
	 * format and log function.
	 * 
	 * @param repository Information about the Repository which is source for Registry. 
	 * @param patient  the patient related to the Document
	 * @param set the SubmissionSet related to the Document
	 * @param eventActionCode  the {@link EventActionCode}
	 */
	protected void documentExport(ActiveParticipant repository, ParticipantObject patient, ParticipantObject set, AuditCodeMappings.AuditTypeCodes typeCode)
		throws JAXBException 
	{
		for (IMessageTransmitter messenger: messengers) {
			AuditObjectFactory factory = new AuditObjectFactory(messenger);
			if (patient != null) {
					patient.typeCode = ParticipantObjectTypeCode.Person;
					patient.role = ParticipantObjectRoleCode.Patient;
					patient.idTypeCode = ParticipantObjectIdTypeCode.Patient;
					factory.addParticipantObject(patient);
				}
			if(set != null) factory.addParticipantObject(set);
			// Note, AuditTypeCode is not used for data export.
			factory.setEventIdType(new EventId(AuditEventIds.Export, typeCode, EventActionCode.Read, SuccessCode.Success));
			factory.addActiveParticipant(repository);

            formatAndLog(messenger, factory, false, ActiveParticipantIds.Destination, repository.getAuditSourceType());
		}
	}
	
	/** 
	 * DICOM Supp95 message A1.3.13 (p. 33): Query <p />
	 * 
     * @param source the source information of the application that sends the message
     * @param patients the patients related to the PDQ Query message
     * @param query the PIX/PDQ Query information
	 */
	private void patientQuery(ActiveParticipant source, Collection<ParticipantObject> patients, ParticipantObject query) throws JAXBException {
		for (IMessageTransmitter messenger : messengers) {
			AuditObjectFactory factory = new AuditObjectFactory(messenger);
			AuditTypeCodes eventtype = null;
			if(query.idTypeCode == ParticipantObjectIdTypeCode.PIXQuery )
				eventtype=AuditCodeMappings.AuditTypeCodes.PixQuery;
			else
				eventtype=AuditCodeMappings.AuditTypeCodes.PDQQuery;
			
			factory.setEventIdType(new EventId(AuditEventIds.Query,
					eventtype , EventActionCode.Execute,
					SuccessCode.Success));
            if (source !=null ) {
				source.role = ActiveParticipantIds.Source;
				source.setAccessPointTypeCode(NetworkAccessPointType.IPAddress);
				source.setRequestor(true);
				factory.addActiveParticipant(source);
            }
            
			if (patients != null) {
				for(ParticipantObject patient: patients ) {
					patient.typeCode = ParticipantObjectTypeCode.Person;
					patient.role = ParticipantObjectRoleCode.Patient;
					patient.idTypeCode = ParticipantObjectIdTypeCode.Patient;
					factory.addParticipantObject(patient);
				}
			}
			if (query != null) {
				query.typeCode = ParticipantObjectTypeCode.SystemObject;
				query.role = ParticipantObjectRoleCode.Query;
				factory.addParticipantObject(query);
			}
			
			formatAndLog(messenger, factory, false, ActiveParticipantIds.Destination, source.getAuditSourceType());
		}
	}
	
	
	/** 
	 * DICOM Supp 95 message A.1.3.4 (p. 19): Data Export <p />
	 * 
	 * This should be used when data leaves control of the system. (E.g. xdm/xdr) <p />
	 * 
	 * Requires an event id with the parameters below.  Also requires the 
	 * doctor as an active participant, if known, and a description of the 
	 * documents accessed as a participant object.  This is the patient id
	 * and additional information about the document affected if available.
	 * The patient info is required. <p />
	 * 
	 * In addition, a single participant which is the application and a single 
	 * audit source.  The participant and audit source are provided by the 
	 * format and log function.
	 * 
	 * @param doctor Information about the doctor.  null if not available.
	 * @param patient needs to change.  This is the information about the patient.
	 * @param action What they did with the patient record.
	 */
	protected void dataExport(ActiveParticipant doctor, ActiveParticipant media, ParticipantObject patient)
		throws JAXBException 
	{
		for (IMessageTransmitter messenger: messengers) {
			AuditObjectFactory factory = new AuditObjectFactory(messenger);
			// Note, AuditTypeCode is not used for data export.
			factory.setEventIdType(new EventId(AuditEventIds.Export, (AuditTypeCodes) null, EventActionCode.Read, SuccessCode.Success));
			factory.addActiveParticipant(media);
			factory.addActiveParticipant(doctor);
			factory.addParticipantObject(patient);

            formatAndLog(messenger, factory, false, ActiveParticipantIds.Application, doctor.getAuditSourceType());
		}
	}
	
	/** 
	 * DICOM Supp 95 message A.1.3.4 (p. 19): Data Import <p />
	 * 
	 * This should be used when data was not in control of the system. (E.g. xdm/xdr) <p />
	 * 
	 * Requires an event id with the parameters below.  Also requires the 
	 * doctor as an active participant, if known, and a description of the 
	 * documents accessed as a participant object.  This is the patient id
	 * and additional information about the document affected if available.
	 * The patient info is required. <p />
	 * 
	 * In addition, a single participant which is the application and a single 
	 * audit source.  The participant and audit source are provided by the 
	 * format and log function.
	 * 
	 * @param doctor Information about the doctor.  null if not available.
	 * @param patient needs to change.  This is the information about the patient.
	 * @param action What they did with the patient record.
	 */
	protected void dataImport(ActiveParticipant doctor, ActiveParticipant media, ParticipantObject patient)
		throws JAXBException 
	{
		for (IMessageTransmitter messenger: messengers) {
			AuditObjectFactory factory = new AuditObjectFactory(messenger);
			// Note, AuditTypeCode is not used for data export.
			factory.setEventIdType(new EventId(AuditEventIds.Import, (AuditTypeCodes) null, EventActionCode.Create, SuccessCode.Success));
			factory.addActiveParticipant(doctor);
			factory.addActiveParticipant(media);
			factory.addParticipantObject(patient);

			formatAndLog(messenger, factory, false, ActiveParticipantIds.Application, doctor.getAuditSourceType());
		}
	}
	
	
	
	/** 
	 * DICOM Supp95 message A1.3.14 (p. 34): Security Alert <p />
	 * 
	 * This requires a single event id, the comprimised server if known, the
	 * reporting server (given by format and log) the identity of the reporting
	 * user (assumed to be machine only and therefore unknown) and the offending
	 * participants, if known.  We are generally the offending so we just leave
	 * ourselves out since we are already in there, however this is a spot for
	 * improvement in the future. <p />
	 * 
	 * In addition there appears to be a Participant object, but it is poorly defined
	 * and thus is not included here.  This should be changed in the future.
	 * 
	 * @param success Major error means that security has been comprimised.  
	 * Success means an informative alert only.  Others mean mitigation was possible.
	 * @param otherServer The comprimised server, if known.
	 */
	protected void securityAlert(SuccessCode success, ActiveParticipant otherServer) throws JAXBException {
		for (IMessageTransmitter messenger : messengers) {
			AuditObjectFactory factory = new AuditObjectFactory(messenger);		
			factory.setEventIdType(new EventId(AuditEventIds.SecurityAlert, SecurityAlertType.NodeAuthentication, EventActionCode.Execute, success));
			if (otherServer != null) {
				otherServer.setRequestor(false);
				factory.addActiveParticipant(otherServer);
			}
			// TODO Find out if participant object is required and add it if it is.
			formatAndLog(messenger, factory, true, ActiveParticipantIds.Application, (otherServer!=null)?otherServer.getAuditSourceType() : null );
		}
	}
	
	/** 
	 * DICOM Supp95 message A1.3.15 (p. 36): User Authentication <p />
	 * 
	 * This message records users logging into the system, as opposed to 
	 * security alerts which are for node's authenticating themselves.  The single
	 * event id is a login or logout, and whether it succeeded.  The user attempting
	 * to authenticate is a required active participant.  The enterprise wide
	 * authentication node (e.g. kerberos) is optional, but the actual authentication
	 * node is mandatory though included by format and log.
	 * 
 	 * @param user User authenticating.  Must not be null.
	 * @param isLogin True if user is logging in false if logging out.
	 * @param success Whether the loging was successful.
	 */
	protected void userAuthentication(ActiveParticipant user, AuditTypeCodes type, SuccessCode success) throws JAXBException {
		for (IMessageTransmitter messenger : messengers) {
			AuditObjectFactory factory = new AuditObjectFactory(messenger);
			factory.setEventIdType(new EventId(AuditEventIds.UserAuthentication, type, EventActionCode.Execute, success));
			if (user == null) user = getUser();
			if (user == null) throw new JAXBException("User not allowed to be null for authentication logging.");
			user.setRequestor(true);
			factory.addActiveParticipant(user);
			formatAndLog(messenger, factory, false, ActiveParticipantIds.Application, user.getAuditSourceType());
		}
	}	
	
	/////**************   ACTUAL MESSAGES   *********************
	
	/**	Sends actor start log message.  Must be called when actor is started. */
	public void start() {
		try {
			applicationActivity(AuditTypeCodes.ApplicationStart);
		} catch (JAXBException e) {
			LOG.error("Unable to log actor start for: " + actorName, e);
		}
	}
	
	/**	Sends actor stop log message.  Must be called when actor is finished. */
	public void stop() {
		try {
			applicationActivity(AuditTypeCodes.ApplicationStop);
		} catch (JAXBException e) {
			LOG.error("Unable to log actor stop for: " + actorName, e);
		}
	}
	
	/**
	 * Call when the node fails to authenticate itself with another node. 
	 * 
	 * Generally you don't log successes since there can be many of those.
	 * 
	 * Described in DICOM Supp95 A 1.3.14 as Security Alert.
	 * Described in ITI TF-2 p. 172 as Node-authentication-failure.
	 */
	public void nodeAuthenticationFailure(SuccessCode success, IConnectionDescription otherServer) {
		try {
			ActiveParticipant otherServerAP = new ActiveParticipant(otherServer);
			otherServerAP.role = ActiveParticipantIds.Destination;
			this.securityAlert(success, otherServerAP);
		} catch (JAXBException e) {
			LOG.error("Unable to log node authentication.", e);
		}
	}

	/** 
	 * Call when a user authenticates himself. 
	 * 
	 * Described in DICOM Supp95 A 1.3.15 as User Authentication.
	 * Described in ITI TF-2 p. 172 as Node-authentication-failure.
	 */
	public void userLogin(SuccessCode success, ActiveParticipant user) {
		try {
			if (user == null) user = getUser();
			if (user != null) {
                user.role = ActiveParticipantIds.Source;
                user.setRequestor(true);
                this.userAuthentication(user, AuditTypeCodes.Login, success);
            }
		} catch (JAXBException e) {
			LOG.error("Unable to log user authentication.", e);
		}
	}

	/** 
	 * Call when a user logs out. 
	 * 
	 * Described in DICOM Supp95 A 1.3.15 as User Authentication.
	 * Described in ITI TF-2 p. 172 as Node-authentication-failure.
	 */
	public void userLogout(SuccessCode success, ActiveParticipant user) {
		try {
			if (user == null) user = getUser();
			user.role = ActiveParticipantIds.Source;
			this.userAuthentication(user, AuditTypeCodes.Logout, success);
		} catch (JAXBException e) {
			LOG.error("Unable to log user authentication.", e);
		}
	}
	

	/** 
	 * Call when a record is imported from external media.
	 * 
	 * Described in DICOM Supp95 A 1.3.5 as Data Import.
	 * 
	 * @param patient The patient or document participant object.
	 * @param mediaDesc A string describing the media, e.g. the source e-mail address, or "USB Media", etc...
	 */
	public void recordImported(ParticipantObject patient, String mediaDesc) {
		try {
			ActiveParticipant user = getUser();
			user.role = ActiveParticipantIds.Destination;
			ActiveParticipant media = getMedia(ActiveParticipantIds.SourceMedia, mediaDesc);
			patient.role = ParticipantObjectRoleCode.Patient;
			patient.typeCode = ParticipantObjectTypeCode.Person;
			
			this.dataImport(user, media, patient);
		} catch (JAXBException e) {
			LOG.error("Unable to log record import.", e);
		}
	}
	
	/** 
	 * Call when a record is exported to external media.
	 * 
	 * Described in DICOM Supp95 A 1.3.4 as Data Export.
	 * 
	 * @param patient The patient or document participant object.
	 * @param mediaDesc A string describing the media, e.g. the destination e-mail address, or "USB Media", etc...
	 */
	public void recordExported(ParticipantObject patient, String mediaDesc) {
		try {
			ActiveParticipant user = getUser();
			ActiveParticipant media = getMedia(ActiveParticipantIds.DestinationMedia, mediaDesc);
			patient.role = ParticipantObjectRoleCode.Patient;
			patient.typeCode = ParticipantObjectTypeCode.Person;
			
			this.dataExport(user, media, patient);
		} catch (JAXBException e) {
			LOG.error("Unable to log record export.", e);
		}
	}
	
	/////**************   ILL DEFINED MESSAGES   *********************
	
    /**
     * Audit Logging of PIX Feed Messages. Call this method when processing PIX Create,
     * PIX Update and PIX Update Notification messages.
     * 
     * @param source the source information of the application that sends the message
     * @param patient the patient related to the PIX Feed message
     * @param eventActionCode the {@link EventActionCode}
     */
	public void logPixFeed(ActiveParticipant source, ParticipantObject patient, EventActionCode eventActionCode ) {
		try {
			if (patient == null)  //query is required
				throw new IllegalArgumentException("Required patient is missing");

			if (source != null) {
				source.role = ActiveParticipantIds.Source;
				source.setAccessPointTypeCode(NetworkAccessPointType.IPAddress);
				source.setRequestor(true);
			}
			
			patientRecord(source, patient, AuditTypeCodes.PatientIdentityFeed, 
					     eventActionCode);
		} catch (JAXBException e) {
			LOG.error("Unable to log patient identity feed", e);
		}		
	}

    /**
     * Audit Logging of PIX Query Messages. Call this method when processing PIX 
     * Query messages.
     * 
     * @param source the source information of the application that sends the message
     * @param patient the patient related to the PIX Query message
     * @param query the PIX Query information
     */
	public void logPixQuery(ActiveParticipant source, ParticipantObject patient, 
			ParticipantObject query) { 
		try {
			//Participant Objects for Patient Query
			if (query == null)  //query is required
				throw new IllegalArgumentException("Required query is missing");
			
			query.idTypeCode = ParticipantObjectIdTypeCode.PIXQuery;

			Collection patients = new ArrayList<ParticipantObject>();
			patients.add(patient);
			patientQuery(source, patients, query);
		} catch (JAXBException e) {
			LOG.error("Unable to log PIX Query", e);
		}		
	}

    /**
     * Audit Logging of PDQ Query Messages. Call this method when processing PDQ 
     * Query messages.
     * 
     * @param source the source information of the application that sends the message
     * @param patients the patients related to the PDQ Query message
     * @param query the PDQ Query information
     */
	public void logPdqQuery(ActiveParticipant source, Collection<ParticipantObject> patients, 
			ParticipantObject query) { 
		try {
			//Participant Objects for Patient Query
			if (query == null)  //query is required
				throw new IllegalArgumentException("Required query is missing");
				
			query.idTypeCode = ParticipantObjectIdTypeCode.PDQQuery;

			patientQuery(source, patients, query);
		} catch (JAXBException e) {
			LOG.error("Unable to log PDQ Query", e);
		}		
	}
	
    /**
     * Audit Logging of PIX Update Notification Messages. Call this method when processing
     * PIX Update Notification. 
     * 
     * @param destination the destination information of the application that receives the message
     * @param patients the patients related to this PIX Update Notification message
 	 */
	public void logPixUpdateNotification(ActiveParticipant destination, ParticipantObject patient) {
		try {
			if (patient == null)  //query is required
				throw new IllegalArgumentException("Required patient is missing");

			if (destination != null) {
				destination.role = ActiveParticipantIds.Destination;
				destination.setAccessPointTypeCode(NetworkAccessPointType.IPAddress);
				destination.setRequestor(false);
			}
			
			for (IMessageTransmitter messenger : messengers) {
				AuditObjectFactory factory = new AuditObjectFactory(messenger);
				patient.typeCode = ParticipantObjectTypeCode.Person;
				patient.role = ParticipantObjectRoleCode.Patient;
				patient.idTypeCode = ParticipantObjectIdTypeCode.Patient;
				factory.addParticipantObject(patient);

				factory.setEventIdType(new EventId(AuditEventIds.PatientRecord, 
						AuditCodeMappings.AuditTypeCodes.PixUpdateNotification, 
						EventActionCode.Read, SuccessCode.Success));
				if (destination != null) factory.addActiveParticipant(destination);
				
				formatAndLog(messenger, factory, true, ActiveParticipantIds.Source, destination.getAuditSourceType());
			}
		} catch (JAXBException e) {
			LOG.error("Unable to log PIX update notification", e);
		}				
	}
	
	 /**
		 * Audit Logging of Registry Query Messages. Call this method when
		 * processing Registry Query messages.
		 * 
		 * @param source
		 *            the source information of the application that sends the
		 *            message
		 * @param patients
		 *            the patients related to the PDQ Query message
		 * @param query
		 *            the Registry Query information
		 * @param isStoredQuery
		 *            boolean to define the query type
		 */
	public void logRegistryQuery(ActiveParticipant source, ParticipantObject patient, 
			ParticipantObject query, boolean isStoredQuery) { 
		try {
			if (query == null)  //query is required
				throw new IllegalArgumentException("Required query is missing");

			//patientQuery(source, patients, query);
			for (IMessageTransmitter messenger : messengers) {
				AuditObjectFactory factory = new AuditObjectFactory(messenger);
				AuditTypeCodes eventtype = null;
				if(isStoredQuery)
					eventtype=AuditCodeMappings.AuditTypeCodes.RegistryStoredQuery;
				else
					eventtype=AuditCodeMappings.AuditTypeCodes.RegistrySQLQuery;
				
				factory.setEventIdType(new EventId(AuditEventIds.Query,
						eventtype , EventActionCode.Execute,
						SuccessCode.Success));
	            if (source !=null ) {
					source.role = ActiveParticipantIds.Source;
					source.setAccessPointTypeCode(NetworkAccessPointType.IPAddress);
					source.setRequestor(true);
					factory.addActiveParticipant(source);
	            }
	            
				if (patient != null) {
						patient.typeCode = ParticipantObjectTypeCode.Person;
						patient.role = ParticipantObjectRoleCode.Patient;
						patient.idTypeCode = ParticipantObjectIdTypeCode.Patient;
						factory.addParticipantObject(patient);
				}
				if (query != null) {
					query.typeCode = ParticipantObjectTypeCode.SystemObject;
					query.role = ParticipantObjectRoleCode.Query;
					if(isStoredQuery){
						query.idTypeCode = AuditCodeMappings.ParticipantObjectIdTypeCode.RegistryStoredQuery;
					}else{
						query.idTypeCode =	AuditCodeMappings.ParticipantObjectIdTypeCode.RegistrySQLQuery;
					}
					factory.addParticipantObject(query);
				}
				
				formatAndLog(messenger, factory, false, ActiveParticipantIds.Destination, source.getAuditSourceType());
			}
		} catch (JAXBException e) {
			LOG.error("Unable to log Registry Query", e);
		}		
	}
	
	 /**
		 * Audit Logging of Repository Query Messages. Call this method when
		 * processing Repository Query messages.
		 * 
		 * @param source
		 *            the source information of the application that sends the
		 *            message
		 * @param document
		 *            the DocumentURI information
		 * @param eventTypeCode
		 *            the query type code
		 */
	public void logRepositoryQuery(ActiveParticipant source,  Collection<ParticipantObject> documents, AuditCodeMappings.AuditTypeCodes eventTypeCode) { 
		try {
			if (documents == null)  
				throw new IllegalArgumentException("Required DocumentURI is missing");

			//patientQuery(source, patients, query);
			for (IMessageTransmitter messenger : messengers) {
				AuditObjectFactory factory = new AuditObjectFactory(messenger);
				
				factory.setEventIdType(new EventId(AuditEventIds.Export, eventTypeCode, EventActionCode.Read, SuccessCode.Success));
	            if (source !=null ) {
					source.role = ActiveParticipantIds.Source;
					source.setAccessPointTypeCode(NetworkAccessPointType.IPAddress);
					source.setRequestor(false);
					factory.addActiveParticipant(source);
	            }
	            
				if (documents != null) {
					for(ParticipantObject document: documents ) {
					document.typeCode = ParticipantObjectTypeCode.SystemObject;
					document.role = ParticipantObjectRoleCode.Report;
					document.idTypeCode = AuditCodeMappings.ParticipantObjectIdTypeCode.ReportNumber;
					factory.addParticipantObject(document);
					}
				}
				
				formatAndLog(messenger, factory, true, ActiveParticipantIds.Destination, source.getAuditSourceType());
			}
		} catch (JAXBException e) {
			LOG.error("Unable to log Repository Query", e);
		}		
	}
	
	/**
	 * Audit Logging of XDS Registry Messages. Call this method when processing
	 * Register Document Set,
	 * 
	 * @param source
	 *            the source information of the application that sends the
	 *            message
	 * @param patient
	 *            the patient related to the Document
	 * @param eventActionCode
	 *            the {@link EventActionCode}
	 */
	public void logRegisterDocument(ActiveParticipant source, ParticipantObject patient, ParticipantObject set, AuditCodeMappings.AuditTypeCodes typeCode) {
		try {
			if (patient == null)  
				throw new IllegalArgumentException("Required patient is missing");

			if(set != null){
				set.typeCode = ParticipantObjectTypeCode.SystemObject;
				set.role = ParticipantObjectRoleCode.Job;
				set.idTypeCode = ParticipantObjectIdTypeCode.SubmissionSet;
			}
			if (source != null) {
				source.role = ActiveParticipantIds.Source;
				source.setAccessPointTypeCode(NetworkAccessPointType.IPAddress);
				source.setRequestor(true);
			}
			
			documentImport(source, patient, set, typeCode);
		} catch (JAXBException e) {
			LOG.error("Unable to log patient identity feed", e);
		}		
	}
	
	/**
	 * Audit Logging of XDS Repository Messages. Call this method when sending
	 * data to the registry,
	 * 
	 * @param source
	 *            the source information of the application that sends the
	 *            message
	 * @param patient
	 *            the patient related to the Document
	 * @param set
	 *            the SubmissionSet related to the Document
	 * @param eventActionCode
	 *            the {@link EventActionCode}
	 */
	public void logRopositoryDocument(ActiveParticipant source, ParticipantObject patient, ParticipantObject set, AuditCodeMappings.AuditTypeCodes typeCode) {
		try {
			if (set == null)  
				throw new IllegalArgumentException("Required SubmissionSet is missing");

			if(set != null){
				set.typeCode = ParticipantObjectTypeCode.SystemObject;
				set.role = ParticipantObjectRoleCode.Job;
				set.idTypeCode = ParticipantObjectIdTypeCode.SubmissionSet;
			}
			if (source != null) {
				source.role = ActiveParticipantIds.Source;
				source.setAccessPointTypeCode(NetworkAccessPointType.IPAddress);
				source.setRequestor(true);
			}
			
			documentExport(source, patient, set, typeCode);
		} catch (JAXBException e) {
			LOG.error("Unable to log patient identity feed", e);
		}		
	}
	
	/////**************   TESTING MESSAGES   *********************
		
	
	public static void main(String[] args) {
		// User
//		LogContext context = new LogContext();
//		context.setClientAddress("10.0.1.101");
//		context.setUserId("jones@sunroom.hosp.org");
//		context.setUserName("Dr. Jones");
//		LogManager.setLogContext(context);
//		LibraryConfig.getInstance().setLogContext(new TestLogContext());
		
		// Other Server
		StandardConnectionDescription otherServer = new StandardConnectionDescription();
		otherServer.setHostname("kitchen.hosp.org");
		otherServer.setName("OtherServer");
		// Patient
		Patient pD = new Patient();
		PatientIdentifier pid = new PatientIdentifier();
		pid.setId("misys-id-02344321183");
		Identifier aa = new Identifier("Test", "1.2.3.4.5", "ISO");
		pid.setAssigningAuthority(aa);
		pD.addPatientId(new PatientIdentifier());
		PersonName name = new PersonName();
		name.setFirstName("Susan");
		name.setLastName("Formaldehyde");		
		ParticipantObject patient = new ParticipantObject(pD);
		//ParticipantObject patient = new ParticipantObject("Susan Formaldehyde", "misys-id-02344321183");
		// Repositories
		ConnectionFactory.loadConnectionDescriptionsFromFile("J:\\workspace\\connect_refactor_connect\\src\\connectathon\\AuditRepositoryConnections.xml");
		ArrayList<IConnectionDescription> repositories = new ArrayList<IConnectionDescription>();
		repositories.add(ConnectionFactory.getConnectionDescription("log4j_audittrail"));
		//repositories.add(new AuditTrailDescription("MISYS", "hosp.misyshealthcare.com", AuditTrailDescription.LOG4J, "10.0.1.101", "Sun room", "Big Hospital"));

		IheAuditTrail log = new IheAuditTrail("Fake Actor", repositories);
		log.start();
		log.nodeAuthenticationFailure(SuccessCode.MinorFailure, otherServer);
		log.userLogin(SuccessCode.MinorFailure, log.getUser());
		log.userLogin(SuccessCode.Success, log.getUser());
		//log.patientQueryIssued(otherServer, patient, true);
		log.recordImported(patient, "USB Media");
		log.recordExported(patient, "USB Media");
		log.userLogout(SuccessCode.Success, log.getUser());
		log.stop();
	}
}
