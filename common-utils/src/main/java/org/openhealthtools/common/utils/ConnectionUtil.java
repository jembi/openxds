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

package org.openhealthtools.common.utils;

import org.apache.commons.httpclient.protocol.Protocol;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.actorconfig.net.SecureConnectionDescription;
import org.openhealthtools.openexchange.actorconfig.net.SecureSocketFactory;


/**
 * The helper class to manipulate IConnectionDescription objects to
 * get the web services url and transport protocol etc.
 *  
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class ConnectionUtil {
	/**
	 * Assembles a web service end point based on the given <code>ConnectionDescription</code>
	 * 
	 * @param connection the ConnectionDescription
	 * @return the URL string of the web service
	 */
	public static String getTransactionEndpoint(IConnectionDescription connection) {
		if (connection == null)
			return null;
		
		String host = connection.getHostname();
		if (host == null)
			host = "localhost";
		
		int port = connection.getPort();
		boolean isSecure = connection.isSecure();
		String url = "http://";
		if(isSecure) {
			url="https://";
		}
		
		String path = connection.getUrlPath();
		if (!path.startsWith("/")) 
			path = "/" + path;
		
		url+= host + ":" + port + connection.getUrlPath();
		return url;
	}

	/**
	 * Creates a customized protocol, e.g. https. This protocol can be used
	 * to replaced the default protocol used by Axis2.
	 * 
	 * @param connection the ConnectionDescription
	 * @return a Protocol object. Null is returned if no customization is needed,
	 * and a default protocol is supposed to be used.
	 */
	public static Protocol getProtocol(IConnectionDescription connection) {
		Protocol protocol = null;
		if (connection.isSecure()) {
			protocol = new Protocol("https", new SecureSocketFactory((SecureConnectionDescription)connection), connection.getPort());			
		}
		return protocol;		
	}
}
