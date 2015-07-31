package gov.nist.registry.common2.syslog

class ValidatorTest  extends GroovyTestCase {
	
	void testNoEvent() {
		def msg = '''
			<AuditMessage xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			    xsi:noNamespaceSchemaLocation="healthcare-security-audit.xsd">
		    </AuditMessage>
'''
		def v = new Validator(msg)
		assert v.hasEvent() == false
	}

	def msgEvent = '''
		<AuditMessage xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		    xsi:noNamespaceSchemaLocation="healthcare-security-audit.xsd">
<EventIdentification EventActionCode="R" EventDateTime="2007-12-31T20:04:43Z"
    EventOutcomeIndicator="0">
    <EventID code="110106" codeSystemName="DCM" displayName="Export"/>
    <EventTypeCode code="ITI-14" codeSystemName="IHE Transactions"
        displayName="Register Document Set"/>
</EventIdentification>
    <ActiveParticipant  UserID="http://192.168.253.23" AlternateUserID="56447"
        NetworkAccessPointTypeCode="2"
        NetworkAccessPointID="192.168.253.23"
        UserIsRequestor="true">
        <RoleIDCode code="110153" codeSystemName="DCM" displayName="Source"/>
    </ActiveParticipant>
    <ActiveParticipant  UserID="http://129.148.200.41:8080/xds"
        NetworkAccessPointTypeCode="2"
        NetworkAccessPointID="192.168.253.23"
        UserIsRequestor="false">
        <RoleIDCode code="110152" codeSystemName="DCM" displayName="Destination"/>
    </ActiveParticipant>
	    </AuditMessage>
'''

	
	void testHasEvent() {
		assert new Validator(msgEvent).hasEvent() == true
	}
	
	void testEvent() {
		def v = new Validator(msgEvent)
		v.validateEvent('ITI-14')
		assert v.errors == []
	}
	
	void testNoEventTypeCode() {
		def msg = '''
			<AuditMessage xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			    xsi:noNamespaceSchemaLocation="healthcare-security-audit.xsd">
	<EventIdentification EventActionCode="R" EventDateTime="2007-12-31T20:04:43Z"
	    EventOutcomeIndicator="0">
	    <EventID code="110106" codeSystemName="DCM" displayName="Export"/>
	</EventIdentification>
		    </AuditMessage>
	'''
		def v = new Validator(msg)
		v.run()
		assert v.errors[0].contains('no EventTypeCode')
	}
	
	void testEventNoDateTime() {
		def msg = '''
			<AuditMessage xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			    xsi:noNamespaceSchemaLocation="healthcare-security-audit.xsd">
	<EventIdentification EventActionCode="R" 
	    EventOutcomeIndicator="0">
	    <EventID code="110106" codeSystemName="DCM" displayName="Export"/>
    <EventTypeCode code="ITI-14" codeSystemName="IHE Transactions"
        displayName="Register Document Set"/>
	</EventIdentification>
		    </AuditMessage>
	'''
		def v = new Validator(msg)
		v.run()
		assert v.errors[0].contains('No EventDateTime')
	}
	
	void testFindSource() {
		assert new Validator(msgEvent).findSource() == 0
	}

	void testFindDest() {
		assert new Validator(msgEvent).findDest() == 1
	}
	
	void testSource() {
		def v = new Validator(msgEvent)
		v.validateParticipant(v.findSource(), true)
		assert v.errors == []
	}

	void testDest() {
		def v = new Validator(msgEvent)
		v.validateParticipant(v.findDest(), false)
		assert v.errors == []
	}
}