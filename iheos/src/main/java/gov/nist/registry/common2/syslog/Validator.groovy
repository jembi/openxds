package gov.nist.registry.common2.syslog

class Validator {
	def msg
	def errors = []
	
	Validator(String syslog_msg_xml) {
		msg = new XmlParser().parseText(syslog_msg_xml)
	}
	
	def run() {
		try {
			if ( !hasEvent() )
				throw new ValidatorException ("Cannot validate - no Event structure found")
			if ( !hasSingleEvent() )
				throw new ValidatorException ("Cannot validate - multiple Event structures found")
			if (msg.EventIdentification[0].EventTypeCode.size() == 0)
				throw new ValidatorException ("Cannot validate - no EventTypeCode found")
			switch (msg.EventIdentification[0].EventTypeCode[0].'@code') {
			case 'ITI-14' :
				validateITI14()
				break;
			}
		}
		catch (ValidatorException e) {
			errors << e.getMessage()
		}
	}
	
	def error(msg) { errors << msg }
	
	String getErrorReport() { (errors.size == 0) ? '' : 'Syslog Validation Errors:\n' + errors.join('\n') }
	
	def hasEvent() {
		msg.EventIdentification.size() > 0
	}
	
	def hasSingleEvent() {
		msg.EventIdentification.size() == 1
	}
	
	// Register.a
	def validateITI14() {
		validateEvent('ITI-14')
		
		if (msg.ActiveParticipants.size() != 2)
			throw new ValidatorException("Must have 2 ActiveParticipants - found ${msg.ActiveParticipants.size()}")
		def source = findSource()
		if (source == null) throw new ValidatorException('No Source ActiveParticipant')
		validateParticipant(source, true)
		def dest = findDest()
		if (dest == null) thow new ValidatorException('No Destination ActiveParticipant')
		validateParticipant(dest, false)
	}
	
	def validateParticipant(index, userIsRequestor) {
		def role = (userIsRequestor) ? 'Source' : 'Dest'
		def userId = msg.ActiveParticipant[index].'@UserID'
		if (userIsRequestor && msg.ActiveParticipant[index].'@UserIsRequestor' != 'true')
			errors << 'UserIsRequestor must be true for ActiveParticipant (Source)'
		if (!userIsRequestor && msg.ActiveParticipant[index].'@UserIsRequestor' != 'false')
			errors << 'UserIsRequestor must be false for ActiveParticipant (Dest)'
			
		if (!userIsRequestor) {
			if (userId == null)
				errors << "ActiveParticipant (${role}) UserID missing"
		}
		if (userId != null && !userId.startsWith('http'))
			errors << "UserID must be and endpoint (start with http) - ${userID} found"
		
		if (userIsRequestor && msg.ActiveParticipant[index].'@AlternateUserID' in [null, ''])
			errors << 'AlternateUserID required for ActiveParticipant (Source)'
			
		if (msg.ActiveParticipant[index].RoleIDCode.size() == 1) {
			if (msg.ActiveParticipant[index].RoleIDCode[0].'@codeSystemName' != 'DCM')
				errors << "ActiveParticipant (${role})RoleIDCode codeSystemName must be DCM"
		} else {
			errors << "Multiple ActiveParticipant (${role}) RoleIDCode structures found"
		}
			
		def networkAccessPointTypeCode = msg.ActiveParticipant[index].'@NetworkAccessPointTypeCode'
		if ((networkAccessPointTypeCode in ['1', '2']) == false)
			errors << "ActiveParticipant (${role}) must have NetworkAccessPointTypeCode of 1 or 2"
		if (msg.ActiveParticipant[index].'@NetworkAccessPointID' in [null, ''])
			errors << "ActiveParticipant (${role}) must have NetworkAccessPointID	"
	}
	
	def validateEvent(eventId) {
		if (msg.EventIdentification.size() == 0)
			throw new ValidatorException("Bad Event Structure: No Event")
		if (msg.EventIdentification.size() > 1)
			throw new ValidatorException("Bad Event Structure: Multiple Event")
		if (msg.EventIdentification[0].'@EventActionCode' != 'R')
			errors << "Bad Event Structure: Invalid or no EventActionCode"
		if (msg.EventIdentification[0].'@EventDateTime' == null)
			errors << "Bad Event Structure: No EventDateTime"
		if (msg.EventIdentification[0].'@EventOutcomeIndicator' != '0' && msg.EventIdentification.'@EventOutcomeIndicator' != '1')
			errors << "Bad Event Structure: Invalid or no EventOutcomeIndicator"
			
		if (msg.EventIdentification[0].EventID.size() == 0)
			throw new ValidatorException("Bad Event Structure: No EventID")
		if (msg.EventIdentification[0].EventID.size() > 1)
			throw new ValidatorException("Bad Event Structure: Multiple EventID")
		if (msg.EventIdentification[0].EventID[0].'@code' != '110106' ||
				msg.EventIdentification[0].EventID[0].'@codeSystemName' != 'DCM')
			errors << "Bad Event Structure: Invalid or no EventID"
			
		if (msg.EventIdentification[0].EventTypeCode.size() == 0)
			throw new ValidatorException("Bad Event Structure: No EventTypeCode")
		if (msg.EventIdentification[0].EventTypeCode.size() > 1)
			throw new ValidatorException("Bad Event Structure: Multiple EventTypeCode")
		if (msg.EventIdentification[0].EventTypeCode[0].'@code' != eventId ||
				msg.EventIdentification[0].EventTypeCode[0].'@codeSystemName' != 'IHE Transactions')
			errors << "Bad Event Structure: Invalid or no EventTypeCode"
	}
	
	def findSource() {
		def index
		(0..<msg.ActiveParticipant.size()).each {
			if (msg.ActiveParticipant[it].RoleIDCode.size() > 0 &&
			    msg.ActiveParticipant[it].RoleIDCode[0].'@code' == '110153')
				index = it
		}
		return index
	}

	def findDest() {
		def index
		(0..<msg.ActiveParticipant.size()).each {
			if (msg.ActiveParticipant[it].RoleIDCode.size() > 0 &&
			    msg.ActiveParticipant[it].RoleIDCode[0].'@code' == '110152')
				index = it
		}
		return index
	}
}


class ValidatorException extends Exception {
	ValidatorException(String message) {
		super(message)
	}
}