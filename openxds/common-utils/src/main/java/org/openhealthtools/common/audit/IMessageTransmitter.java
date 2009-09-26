/**
 *  Copyright (c) 2009 Misys Open Source Solutions (MOSS) and others
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
 *
 */
package org.openhealthtools.common.audit;

/** 
 * Interface for sending messages.
 * 
 * This should only be used by the IheAuditTrail base class.  It should
 * configure one for each of the sudit reporitories it is configured for.
 * Then when a actor instance sends the appropriate message, the base class
 * will call each of the message transmitters in turn.
 *
 * @author Josh Flachsbart
 * @version 1.0 - Nov 10, 2005
 */
interface IMessageTransmitter {
	/** Send a message using this transmitter using the default facility and level. */
	public void sendMessage(String message);
	/** Send a message using this transmitter using the given level. */
	public void sendMessage(String message, Severity severity);
	/** Set the default level for the transmitter. */
	public void setDefaultSeverity(Severity severity);
	/** Get the description of this connection. */
	public AuditTrailDescription getAuditTrailDescription();
	
	/** Severity levels used in both RFC 3164 (BSD Syslog) and RFC 3195 (Rsyslog) */
	public enum Severity {
		Emergency(0), // System is unusable
		Alert(1),     // Sction must be taken immediately
		Critical(2),  // Critical conditions
		Error(3),     // Error conditions
		Warning(4),   // Warning conditions
		Notice(5),    // Normal, but significant condition
		Info(6),      // Informational messages
		Debug(7);     // Debugging messages
		
		Severity(int value) { this.value = value; }
	    private final int value;
	    public int value() { return value; }
	}
}