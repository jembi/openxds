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

package org.openhealthtools.openxds.xca;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.actorconfig.IActorDescription;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.audit.IheAuditTrail;
import org.openhealthtools.openxds.BaseIheActor;
import org.openhealthtools.openxds.configuration.XdsConfigurationLoader;
import org.openhealthtools.openxds.xca.api.XcaRG;


/**
 * This class represents an XCA Responding Gateway actor.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class XcaRGImpl extends BaseIheActor implements XcaRG {
    /** Logger for problems during SOAP exchanges */
    private static Log log = LogFactory.getLog(XcaRGImpl.class);

    /**The client side of XDS Registry connection*/
	private IConnectionDescription registryClientConnection = null;
    /**The client side of XDS Repository connection*/
	private IConnectionDescription repositoryClientConnection = null;

    /**
     * Creates a new XCA Responding Gateway actor.
     *
     */
     public XcaRGImpl(IActorDescription actorDescription, IheAuditTrail auditTrail) {
    	 super(actorDescription, auditTrail);
         this.registryClientConnection = actorDescription.getConnectionDescriptionByType(XdsConfigurationLoader.REGISTRY);
         this.repositoryClientConnection = actorDescription.getConnectionDescriptionByType(XdsConfigurationLoader.REPOSITORY);
    }

    
    @Override
	public void start() {
        //call the super one to initiate standard start process
        super.start();
    }
    
    @Override
    public void stop() {
        //call the super one to initiate standard stop process
        super.stop();
    }

	/**
	 * Gets the client side Registry <code>IConnectionDescription</code> of this actor.
	 * 
	 * @return the client side Registry connection
	 */
	public IConnectionDescription getRegistryClientConnection() {
		return registryClientConnection;
	}

	/**
	 * Gets the client side Repository <code>IConnectionDescription</code> of this actor.
	 * 
	 * @return the client side Repository connection
	 */
	public IConnectionDescription getRepositoryClientConnection() {
		return repositoryClientConnection;
	}

}
