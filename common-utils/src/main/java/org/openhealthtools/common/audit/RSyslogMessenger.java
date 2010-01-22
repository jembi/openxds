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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.beepcore.beep.core.BEEPError;
import org.beepcore.beep.core.BEEPException;
import org.beepcore.beep.core.BEEPInterruptedException;
import org.beepcore.beep.core.Channel;
import org.beepcore.beep.core.OutputDataStream;
import org.beepcore.beep.core.Session;
import org.beepcore.beep.core.StringOutputDataStream;
import org.beepcore.beep.lib.Reply;
import org.beepcore.beep.transport.tcp.TCPSessionCreator;

/** 
 * This is the secure logging message implementation.
 * 
 * It required the beepcore java library, but implements its own rsyslog layer.
 * Beep is the transport mechanism (RFCs 3080 and 3081), and rsyslog is the 
 * message mechanism (RFC 3195).  <p />
 * 
 * There are concessions for broken log servers.  HIPAAT can't accept messages over
 * 1030, and needs the xml escaped out.  The Quovadx server need the xml in a cdata.
 *
 * @author Josh Flachsbart
 * @version 1.0 - Nov 23, 2005
 */
public class RSyslogMessenger implements IMessageTransmitter {
	private static final Log log = LogFactory.getLog(RSyslogMessenger.class);
	
	private Session session;
	private Channel channel;

	private int remotePort = 6001;
	private String remoteHostName = "localhost";
	private String remoteType;
	private String localHostName;
	private String localFqdName;
	private String localIp;
	
	private AuditTrailDescription desc;

	private final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd HH:mm:ss");
	int facility = 1;
	Severity severity = Severity.Info;

	/** 
	 * Loads up a connection to the specified logging server.
	 * 
	 * This really really should use the standard configuration file.
	 * Honestly a bunch of other stuff should be here as well, like 
	 * local host name, and all the garbage that needs to go into the
	 * ATNA messages.
	 */
	public RSyslogMessenger(AuditTrailDescription description) {
		remoteHostName = description.getHost();
		remotePort = description.getPort();
		remoteType = description.getServer();
			
		localHostName = description.getName();
		localFqdName = description.getFqdn();
		localIp = description.getIp();
		
		desc = description;
	}
	
	/* (non-Javadoc)
	 * @see com.misyshealthcare.connect.audit.IMessageTransmitter#sendMessage(java.lang.String)
	 */
	public void sendMessage(String message) {
		sendMessage(message, severity);
	}
	
	
	public void sendMessage(String message, Severity severity) {
		// Initiate connection.
		startBeepConnection();

		OutputDataStream output = formatMessage(message, remoteType, severity);

		Reply reply = null;
		if (!remoteType.equalsIgnoreCase("HIPAAT"))
			reply = new Reply();

		try {
			channel.getSession();
			channel.sendMSG(output, reply);
			log.debug("Sent messasge.");
		} catch (BEEPException e) {
			log.error("Unable to send message.", e);
		}

		readResponse(reply);
		
        close();
	}

	public void setDefaultSeverity(Severity severity) {
		this.severity = severity;
	}

	/** Used to do the inital negotiation.  This is where you would add TLS if you desired.
	 * Ostensibly one channel could be used for multiple message
	 * but we don't do that right now since no servers support it.
	 * First open the session.  Then using that session start a channel with the cooked protocol. <p />
	 * 
	 * If we ever wanted to maintain the connection we would have to make this and close puublic.
	 */
	private void startBeepConnection() {
		try {
			session = TCPSessionCreator.initiate(remoteHostName, remotePort);//, new ProfileRegistry());
			//session = org.beepcore.beep.profile.tls.TLSProfile.getDefaultInstance().startTLS((TCPSession) session);
			
			String profileUri = "http://xml.resource.org/profiles/syslog/COOKED";
			String iam = " <iam fqdn='" + localFqdName + "' ip='" + localIp + "' type='device' /> ";
			channel = session.startChannel(profileUri, false, iam);
		} catch (BEEPError e) {
			if (e.getCode() == 550) {
				log.error("Host does not support cooked profile.", e);
			} else {
            		log.error("Unable to open channel.", e);
			}
		} catch (BEEPException e) {
			log.error("Unable to open channel.", e);
		}
		log.debug("Opened channel successfully.");
	}
	
	private void close() {
		try {
			channel.close();
			session.close();
		} catch (BEEPException e) {
			log.error("Unable to close channel.", e);
		}
		log.debug("Closed channel and session successfully.");
	}
	
	private OutputDataStream formatMessage(String message, String format, Severity severity) {
//		OutputDataStream output = null;
		
		String entry = null;
		String header = "<entry facility='" + facility + "' severity='" + severity.value() + "' timestamp='" + formatter.format(new Date()) + "' hostname='" + localFqdName + "' tag='" + localHostName + "'>";
		String tail =  "</entry>\r\n";

		if (format == null) {
			entry = header + message + tail;
		} else if (format.equalsIgnoreCase("QUOVADX")) {
			entry = header + "<![CDATA[" + message + "]]>" + tail;
		} else if (format.equalsIgnoreCase("HIPAAT")) {
			message = message.replaceAll("<", "&lt;");
			entry = header + message + tail;
//			try {
				// NOTE: this is to break the message up into multiple parts, however it is possible that
				// in order to do this, you need to call send multiple times.  If this is true, HIPAAT will
				// need entirely its own function.  In fact testing seems to show this to be the case, so 
				// we are commenting this out for now and using the standard method.
//				output = new OutputDataStream(new MimeHeaders("application/beep+xml"));
//				int maxFrameSize = 1024;
//				for (int i = 0; i * maxFrameSize < entry.length(); i++) {
//					int start = i * maxFrameSize;
//					int end = (i+1) * maxFrameSize;
//					if (end > entry.length()) end = entry.length();
//					output.add(new BufferSegment(entry.substring(start, end).getBytes("UTF-8")));
//				}
//				output.setComplete();
//			} catch (Exception e) { log.error("Problem with encoding string."); }
		}

		StringOutputDataStream output = new StringOutputDataStream(entry);
		output.setContentType("application/beep+xml");

		return output;
	}
	
	/** This should be more interesting.
	 * TODO: Make this deal with different types of responses in a more interesting manner.
	 * @param reply
	 */
	private void readResponse(Reply reply) {
		if (reply == null) return;
		try {
			if (reply.hasNext()) {
				java.io.InputStream is = reply.getNextReply().getDataStream().getInputStream();
				int input = is.read();
				String fullInput = new String();
				// Read the data in the reply
				while (input != -1) {
					fullInput += (char) input;
					input = is.read();
				}
				log.debug("Reply from " + remoteHostName + ": Message: " + fullInput);
			} else {
				log.debug("No reply from host.");
			}

		} catch (BEEPInterruptedException e) {
			log.error("Interrupted while reading BEEP reply.", e);
		} catch (IOException e) {
			log.error("Unable to read BEEP reply.", e);
		}
	}
	
	public AuditTrailDescription getAuditTrailDescription() {
		return desc;
	}

	/** For testing only. */
	public static void main(String[] argv)
    {
		AuditTrailDescription description = new AuditTrailDescription("MISYS", "hosp.misyshealthcare.com", AuditTrailDescription.RELIABLE, "10.0.1.101", "10.0.1.4", 1516, "HIPAAT");
		String message = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><AuditMessage>\r\n " +
		"<EventIdentification EventOutcomeIndicator=\"0\" EventDateTime=\"2005-12-08T16:05:00\" EventActionCode=\"E\">\r\n" +
			"<EventID codeSystem=\"DCM\" code=\"110100\" displayName=\"Application Activity\" />\r\n" +
			"<EventTypeCode codeSystem=\"DCM\" code=\"110120\" displayName=\"Application Start\" />\r\n" +
		"</EventIdentification>\r\n " +
		"<ActiveParticipant NetworkAccessPointID=\"Misys\" NetworkAccessPointTypeCode=\"1\" UserID=\"Internal Process\" UserIsRequestor=\"true\">\r\n  " +
			"<RoleIDCode codeSystem=\"DCM\" code=\"110151\" displayName=\"Application Launcher\" />\r\n" +
		"</ActiveParticipant>\r\n" +
		"<ActiveParticipant NetworkAccessPointID=\"Misys\" UserName=\"SecureNode\" NetworkAccessPointTypeCode=\"1\" UserID=\"916\" UserIsRequestor=\"false\">\r\n  " +
			"<RoleIDCode codeSystem=\"DCM\" code=\"110150\" displayName=\"Application\" />\r\n " +
		"</ActiveParticipant>\r\n " +
		"<AuditSourceIdentification AuditSourceID=\"Misys\" />\r\n" +
			"</AuditMessage>\r\n";
		IMessageTransmitter rsm = new RSyslogMessenger(description);
//		log.setLevel(Level.ALL);
		rsm.sendMessage(message);
    }

}
