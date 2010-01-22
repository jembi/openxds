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

package org.openhealthtools.common.audit;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

/** 
 * Sends the message to a given BSD logger.
 *
 * The original idea was to use Log4J's bsd logger, but
 * that ended up being way too complex, so I just rolled
 * my own.  Note that if your message is over 1024, you might
 * be hosed since many BSD loggers only take 1024 messages. <p />
 * 
 * You will need to do something to keep this thread safe.  The
 * simple solution (and the one currently implemented) is to 
 * generate a new instance of this class every time you make
 * a new message.  That is how it currently works.  Otherwise
 * the IheAuditTrail will have to do a bunch more work
 * to keep the single instance synchronized.  (Not to mention
 * that there might be different audit servers used by different
 * actors.)
 *
 * @author Josh Flachsbart
 * @version 1.0 - Nov 20, 2005
 */
class BsdMessenger implements IMessageTransmitter {
	private static final int FACILITY = 10; // CP ATNA_157, using value of 10(security/authorization messages) 
	private final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd HH:mm:ss "); //BSD syslog date format.

	private Severity defaultSeverity = Severity.Notice;

	private InetAddress address = null;
	private int port = 514;
	private String localHostName;
	private String messageHostName;
	
	private AuditTrailDescription desc;

	/** 
	 * Use to create a "connection" to a BSD audit repository.
	 * 
	 * Note that BSD syslog uses UDP not TCP, so it isn't really
	 * a "connection" and as such this is thread safe if all 
	 * the threads use the same connection info and default 
	 * severity.
	 * 
	 * @param description All the info needed to connect to the audit repository.
	 */
	public BsdMessenger(AuditTrailDescription description) {
		try {
			messageHostName = description.getHost();
			localHostName = description.getName();
			address = InetAddress.getByName(messageHostName);
			port = description.getPort();
			desc = description;
		} catch (UnknownHostException e) {
			IheAuditTrail.log.error("Problem resolving host name for BSD logging ATNA messeger.", e);
		}
	}
	
	/** 
	 * Sends BSD message using the default facility and level. 
	 */
	public void sendMessage(String message) {
		sendMessage(message, defaultSeverity);
	}

	/** 
	 * Sends BSD message using a givien facility and level. 
	 */
	public void sendMessage(String message, Severity severity) {
		Date now = new Date();
		int PRI = (FACILITY * 8) + severity.value();
		String completeMessage = "<" + PRI + ">" + formatter.format(now) + localHostName + ": " + message;
        int length = completeMessage.length();
		//if (length > 1024) IheAuditTrail.LOG.warn("Data might be truncated, BSD messages longer than 1024 are dangerous.");
        //IHE define the max message length to be 32768, ITI v2 page 175
        if (length > 32768) {
            IheAuditTrail.log.error("BSD messages cannot be longer than 32768.");
            return;
        }
        try {
			byte[] messageBytes = completeMessage.getBytes("UTF-8");
			DatagramPacket packet = new DatagramPacket(messageBytes, length, address, port);
			DatagramSocket socket = new DatagramSocket();
			socket.send(packet);
			socket.close();
		} catch (Exception e) {
			IheAuditTrail.log.error("Problem sending BSD log message.", e);
		}
	}

	/** 
	 * Sets the severity to be used for non specific messages. 
	 */
	public void setDefaultSeverity(Severity severity) {
		defaultSeverity = severity;
	}

	/**
	 * Gets the audit trail description
	 */
	public AuditTrailDescription getAuditTrailDescription() {
		return desc;
	}
}