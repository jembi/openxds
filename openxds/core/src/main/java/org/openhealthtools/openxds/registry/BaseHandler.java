/**
 *  Copyright (c) 2009-2011 Misys Open Source Solutions (MOSS) and others
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

package org.openhealthtools.openxds.registry;

import java.net.InetAddress;

import org.openhealthtools.openexchange.actorconfig.IheConfigurationException;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.datamodel.Identifier;
import org.openhealthtools.openpixpdq.common.PixPdqException;

import ca.uhn.hl7v2.app.ApplicationException;


/**
 * The base class of all handlers. It provides some common 
 * methods available to all handlers.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class BaseHandler {
	
	/** The connection description of the actor for this handler */
	protected IConnectionDescription connection;
	
	/**
	 * Default constructor
	 */
	protected BaseHandler() {
	}
	/**
	 * Constructor
	 * 
	 * @param connection the connection description of the actor 
	 */
	protected BaseHandler(IConnectionDescription connection) {
		this.connection = connection;
	}
	
	/**
	 * Gets the application name of this PIX/PDQ server. If the server receives messages from
	 * a source application, then the application name is the same as ReceivingApplication, configurable
	 * through the XML actor configuration files.
	 * 
	 * @return the application name of this PIX/PDQ server
	 * @throws ApplicationException
	 */
	protected Identifier getServerApplication() throws PixPdqException {
		Identifier ret = null;
		try {
			ret = getIdentifier(connection,
					"ReceivingApplication", true);
		} catch (IheConfigurationException e) {
			throw new PixPdqException(
					"Missing receivingApplication for connection "
							+ connection.getDescription(), e);
		}
		return ret;		
	}
    
	/**
	 * Gets the facility name of this PIX/PDQ server. If the server receives messages from
	 * a source application, then the facility name is the same as ReceivingFacility, configurable
	 * through the XML actor configuration files.
	 * 
	 * @return the facility name of this PIX/PDQ server
	 * @throws ApplicationException
	 */
	protected Identifier getServerFacility() throws PixPdqException {
		Identifier ret = null;
		try {
			ret = getIdentifier(connection,
					"ReceivingFacility", true);
		} catch (IheConfigurationException e) {
			throw new PixPdqException(
					"Missing ReceivingFacility for connection "
							+ connection.getDescription(), e);
		}
		return ret;				
	}
	
	private static String ip = null;
	static {
		try {
			InetAddress addr = InetAddress.getLocalHost();
		    ip = addr.getHostAddress();
		}catch(Exception e) {
			//just ignore it.
		}
	}
	
	private static int staticCounter=0;
	private static final int nBits=4;
	
	/**
	 * Generates a new unique message id.
	 * 
	 * @return a message id
	 */
	protected static synchronized String getMessageControlId() {
		String prefix = "OpenPIXPDQ"; 
		if (ip != null)	prefix += ip;
		long temp = (System.currentTimeMillis() << nBits) | (staticCounter++ & 2^nBits-1);
		String id = prefix + "." + temp;
		return id;
	}

	/**
	 * Gets an identifier description from the connection this
	 * actor is using.
	 * 
	 * @param connection the connection description being used
	 * @param name the name of the identifier in the configuration
	 * @param isRequired <code>true</code> if this identifier must exist
	 * @return the identifier
	 * @throws IheConfigurationException If this identifier must be in the configuration and it is not
	 */
	public static Identifier getIdentifier(IConnectionDescription connection, String name, boolean isRequired) throws IheConfigurationException {
		if (connection == null)
			throw new IheConfigurationException("Invalid connection description (NULL)");
		if (name == null)
			throw new IheConfigurationException("Invalid identifier name (NULL)");
		Identifier identifier = connection.getIdentifier(name);
		
		if ((identifier == null) && isRequired) {
			throw new IheConfigurationException("No identifier '" + name + "' defined for connection \"" + connection.getDescription() + "\"");
		}
		
		return identifier;
	}
	
}
