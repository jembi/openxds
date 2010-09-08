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

package org.openhealthtools.openxds.repository.api;

import org.openhealthtools.openexchange.actorconfig.IActorDescription;


/**
 * This classes defines a repository context that are required for
 * the repository operations of the repository manager service.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class RepositoryRequestContext {
	
	private IActorDescription actorDescription;

	/**
	 * @return the actor description
	 */
	public IActorDescription getActorDescription() {
		return actorDescription;
	}

	/**
	 * @param connection the connection to set
	 */
	public void setActorDescription(IActorDescription actorDescription) {
		this.actorDescription = actorDescription;
	}
	
	
}
