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

package org.openhealthtools.openxds.log;

import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class allowing to connect the logger to the database, to create message, to
 * delete it and to read it
 * 
 * @author <a href="mailto:venkata.pragallapati@misys.com">Venkat</a>
 * 
 */
public class LogImpl extends HibernateDaoSupport implements Log {
	private static final org.apache.commons.logging.Log log = LogFactory
			.getLog(LogImpl.class);


	public LogImpl() throws LoggerException {
	}

	/**
	 * Create a message with message ID made with the IPAddress and the current
	 * timestamp in millisecond
	 * 
	 * @param ipAddress
	 * @return
	 * @throws LoggerException
	 */
	public Message createMessage(String ipAddress) throws LoggerException {
		Timestamp t = new Timestamp(new GregorianCalendar().getTimeInMillis());

		Message m = new Message();
		m.setIP(ipAddress);
		m.setTimeReceived(t);

		return m;

	}

	/**
	 * Create an empty message
	 * 
	 * @return
	 * @throws LoggerException
	 */
	public Message createMessage() throws LoggerException {
		Timestamp t = new Timestamp(new GregorianCalendar().getTimeInMillis());

		Message m = new Message();
		m.setTimeStamp(t);

		return m;
	}

	/**
	 * Same as writeMessage. Calls the writeMessage method to log the message in
	 * database
	 * 
	 * @param m
	 * @throws LoggerException
	 */
	public void logMessage(LogMessage m) throws LoggerException {
		if (m != null)
			writeMessage(m);
	}

	/**
	 * Same as writeMessage. Calls the writeMessage method to log the message in
	 * database
	 * 
	 * @param m
	 * @throws LoggerException
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void writeMessage(LogMessage message) throws LoggerException {
		if (message != null)
			getHibernateTemplate().saveOrUpdate((Message)message);
	}

	public void deleteMessage(LogMessage m) throws LoggerException {
		if (m != null)
			getHibernateTemplate().delete((Message)m);

	}

	public void deleteMessage(String messageID) throws LoggerException {
		if (messageID != null) {
			Message m = new Message();
			m.setMessageID(messageID);
			deleteMessage(m);
		}
	}

	public Message readMessage(String messageID) throws LoggerException {
		Message m = null;
		if (messageID != null) {
			List<Message> messages = getHibernateTemplate().find(
					"from Message m where m.messageID = ? ",
					new String[] { messageID });
			if (messages != null && !messages.isEmpty())
				m = messages.get(0);
		}
		return m;
	}
}
