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

package org.openhealthtools.openxds;

import org.openhealthtools.openexchange.actorconfig.IActorDescription;
import org.openhealthtools.openexchange.audit.IheAuditTrail;


/**
 *
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 *
 */
public class BaseIheActor {
	  
	/** The IHE Audit Trail for this actor. */
	private IheAuditTrail auditTrail = null;
	
	
	/** The actor description of this actor*/
	protected IActorDescription actorDescription = null;
	
	/**
	 * Creates a new IHE Actor.
	 * 
	 */
	public BaseIheActor() {		 
	}
	
	/**
	 * Creates a new IHE Actor.
	 * 
	 */
	public BaseIheActor(IActorDescription actorDescription) {
		this.actorDescription = actorDescription;
	}

	/**
	 * Creates a new HL7 Actor
	 * 
	 */
	public BaseIheActor(IActorDescription actorDescription, IheAuditTrail auditTrail) {
		this.auditTrail = auditTrail;
		this.actorDescription = actorDescription;
	}
	
	/**
	 *  Starts this actor. It must be called once for each actor when the program starts. 
	 */  
	public void start() {
		//if (auditTrail != null) auditTrail.start();  
	}

	/**
	 * Stops this actor. It must be called once for each actor just before the program quits. 
	 */ 
	public void stop() {
		//if (auditTrail != null) auditTrail.stop();  	  
	}
  
	/**
	 * Returns a useful name for this Actor so that it can be put into
	 * debugging and logging messages.
	 * 
	 * @returns a useful name for this Actor
	 */
	public String getName() {
		if (actorDescription != null) {
			return actorDescription.getDescription();
		} else {
			return "unnamed";
		}
	}


	/**
	 * Gets the main <code>IActorDescription</code> of this actor.
	 * 
	 * @return the main connection
	 */
	public IActorDescription getActorDescription() {
		return this.actorDescription;
	}
	
	/**
	 * Gets the Audit Trail of this actor.
	 * 
	 * @return the auditTrail
	 */
	public IheAuditTrail getAuditTrail() {
		return auditTrail;
	}

}
