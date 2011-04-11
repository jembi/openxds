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

package org.openhealthtools.openxds.registry.api;

import org.openhealthtools.openexchange.actorconfig.IheActor;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;


/**
 * This interface defines the operations of an XDS Registry actor.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public interface XdsRegistry extends IheActor {
	/**
	 * Gets the connection for the PIX Feed. The connection provides the details (such as 
	 * port etc) which are needed for this PIX Registry to talk to the PIX Source or Manager.
	 * 
	 * @return the connection of PIX Source/Manager
	 */
	public IConnectionDescription getPixRegistryConnection();

	/**
	 * Gets the web service end point of this Registry.
	 * 
	 * @param isSecure whether to get secure end point or not.
	 * @return
	 */
	public String getServiceEndpoint(boolean isSecure);

}
