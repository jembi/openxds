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

package org.openhealthtools.common.ihe;

import org.openhealthtools.common.audit.IheAuditTrail;

import com.misyshealthcare.connect.net.IConnectionDescription;

/**
 *
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 *
 */
public interface IheActor {
	
	/**
	 *  Starts this actor. It must be called once for each actor when the program starts. 
	 */  
	public void start();

	/**
	 * Stops this actor. It must be called once for each actor just before the program quits. 
	 */ 
	public void stop();
  
	/**
	 * Returns a useful name for this Actor so that it can be put into
	 * debugging and logging messages.
	 * 
	 * @returns a useful name for this Actor
	 */
	public String getName(); 
	
	/**
	 * Gets the main <code>IConnectionDescription</code> of this actor.
	 * 
	 * @return the main connection
	 */
	public IConnectionDescription getConnection();
	
	/**
	 * Gets the Audit Trail of this actor.
	 * 
	 * @return the auditTrail
	 */
	public IheAuditTrail getAuditTrail();

}
