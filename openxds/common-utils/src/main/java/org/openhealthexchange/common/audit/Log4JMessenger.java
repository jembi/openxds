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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/** A messenger that uses Log4j to send the audit trail messages.
 * 
 * This is looking to the future, if log4j ever implements all the
 * required features (e.g. bsd syslog and rsyslog.)  Then you could
 * use a single configuration file AtnaAuditTrailConfig.xml to 
 * configure all your ATNA logging needs.  For now it is pretty
 * much just used for spitting the ATNA logging out to the screen.
 *
 * @author Josh Flachsbart
 * @version 1.0 - Nov 14, 2005
 */
class Log4JMessenger implements IMessageTransmitter {
		
	private static Logger log4Messenger;
	private AuditTrailDescription desc;

	public Log4JMessenger(AuditTrailDescription description) {
		desc = description;
		log4Messenger = Logger.getLogger(Log4JMessenger.class);
		log4Messenger.setLevel(Level.ALL);
	}

	public void sendMessage(String message) {
		if (log4Messenger != null) log4Messenger.debug(message);
		else IheAuditTrail.LOG.error("Unable to send Log4J ATNA audit message");
	}

	public void sendMessage(String message, Severity severity) {
		sendMessage(message);
	}

	public void setDefaultSeverity(Severity severity) {
	}

	public AuditTrailDescription getAuditTrailDescription() {
		return desc;
	}
}