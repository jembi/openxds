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

package org.openhealthtools.openxds.xca;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.actorconfig.IActorDescription;
import org.openhealthtools.openexchange.actorconfig.Transactions;
import org.openhealthtools.openexchange.actorconfig.TransactionsSet;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.audit.IheAuditTrail;
import org.openhealthtools.openxds.BaseIheActor;
import org.openhealthtools.openxds.configuration.XdsConfigurationLoader;
import org.openhealthtools.openxds.xca.api.XcaIG;

/**
 * This class represents an XCA Initiating Gateway actor.
 * 
 * @author <a href="mailto:Anilkumar.reddy@misys.com">Anil kumar</a>
 */
public class  XcaIGImpl extends BaseIheActor implements XcaIG {
    /** Logger for problems during SOAP exchanges */
    private static Log log = LogFactory.getLog(XcaIGImpl.class);

    /**The client side of XDS Registry connection*/
	private IConnectionDescription registryClientConnection = null;
    /**The client side of XDS Repository connection*/
	private IConnectionDescription repositoryClientConnection = null;
    /**The client side of XCA Responding Gateway Query connections*/
	private Map<String, IConnectionDescription> rgQueryClientConnections = null;
    /**The client side of XCA Responding Gateway Retrieve connections*/
	private Map<String, IConnectionDescription> rgRetrieveClientConnections = null;

    /**
     * Creates a new XCA Responding Gateway actor.
     *
     */
     public XcaIGImpl(IActorDescription actorDescription, IheAuditTrail auditTrail) {
    	 super(actorDescription, auditTrail);
         this.registryClientConnection = actorDescription.getConnectionDescriptionByType(XdsConfigurationLoader.REGISTRY);
         this.repositoryClientConnection = actorDescription.getConnectionDescriptionByType(XdsConfigurationLoader.REPOSITORY);
         TransactionsSet respondingGateways = actorDescription.getTransactionSet(XdsConfigurationLoader.RESPONDINGGATEWAY);

         Collection<Transactions> transactions =  respondingGateways.getAllTransactions();
         for (Transactions transaction : transactions) {
        	 IConnectionDescription query = transaction.getQuery();
        	 IConnectionDescription retrieve = transaction.getRetrieve();
        	 if (query != null) {
        		 if (this.rgQueryClientConnections == null) {
        			 this.rgQueryClientConnections = new HashMap<String, IConnectionDescription>();
        		 }
        		 this.rgQueryClientConnections.put(transaction.getId(), query);
        	 }
        	 if (retrieve != null) {
        		 if (this.rgRetrieveClientConnections == null) {
        			 this.rgRetrieveClientConnections = new HashMap<String, IConnectionDescription>();
        		 }
        		 this.rgRetrieveClientConnections.put(transaction.getId(), retrieve);
        	 }
         }
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

	public Map<String, IConnectionDescription> getRGQueryClientConnections() {
    	return rgQueryClientConnections;
    }

	public Map<String, IConnectionDescription> getRGRetrieveClientConnections() {
    	return rgRetrieveClientConnections;
    }
	
	public IConnectionDescription getRegistryClientConnection() {
		return registryClientConnection;
	}

	public IConnectionDescription getRepositoryClientConnection() {
		return repositoryClientConnection;
	}

}
