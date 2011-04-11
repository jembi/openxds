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

package org.openhealthtools.openxds.repository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.actorconfig.IActorDescription;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.audit.IheAuditTrail;
import org.openhealthtools.openexchange.config.PropertyFacade;
import org.openhealthtools.openxds.BaseIheActor;
import org.openhealthtools.openxds.common.XdsConstants;
import org.openhealthtools.openxds.configuration.XdsConfigurationLoader;
import org.openhealthtools.openxds.repository.api.XdsRepository;

/**
 * This class represents an XDS Repository actor.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class XdsRepositoryImpl extends BaseIheActor implements XdsRepository {
    /** Logger for problems during SOAP exchanges */
    private static Log log = LogFactory.getLog(XdsRepositoryImpl.class);

    /**The client side of XDS Registry connection*/
	private IConnectionDescription registryClientConnection = null;

    /**
     * Creates a new XdsRepository actor.
     *
     * @param actorDescription the actor description of this Repository actor
     * @param auditTrail the audit log client  
     */
     public XdsRepositoryImpl(IActorDescription actorDescription, IheAuditTrail auditTrail) {
    	 super(actorDescription, auditTrail);
         this.registryClientConnection = actorDescription.getConnectionDescriptionByType(XdsConfigurationLoader.REGISTRY);
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

	public String getServiceEndpoint(boolean isSecure) {
		StringBuffer sb = new StringBuffer();
		String host = PropertyFacade.getString(XdsConstants.HOST, "localhost");
		if (isSecure) {
			sb.append("https://");
			sb.append(host);
			sb.append(":");
			int port = PropertyFacade.getInteger(XdsConstants.TLS_PORT, 8011);
			sb.append(port);
		} else {
			sb.append("http://");
			sb.append(host);
			sb.append(":");
			int port = PropertyFacade.getInteger(XdsConstants.PORT, 8010);
			sb.append(port);
		}		
		
		sb.append("/");
		String context = PropertyFacade.getString(XdsConstants.WEB_APP_CONTEXT, "openxds");
		sb.append(context);
		sb.append("/services/DocumentRepository");

		return sb.toString();
	}

}
