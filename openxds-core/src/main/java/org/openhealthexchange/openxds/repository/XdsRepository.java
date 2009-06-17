/**
 *  Copyright © 2009 Misys plc, Sysnet International, Medem and others
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
 *     Misys plc - Implementation
 */
package org.openhealthexchange.openxds.repository;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.description.TransportInDescription;
import org.apache.axis2.engine.ListenerManager;
import org.apache.log4j.Logger;
import org.openhealthexchange.common.ws.server.IheHTTPServer;
import org.openhealthexchange.openxds.XdsActor;
import org.openhealthexchange.openxds.repository.api.IXdsRepository;
import org.openhealthexchange.openxds.repository.api.IXdsRepositoryManager;

import com.misyshealthcare.connect.net.IConnectionDescription;

/**
 * This class represents an XDS Repository actor.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class XdsRepository extends XdsActor implements IXdsRepository {
    /** Logger for problems during SOAP exchanges */
    private static Logger log = Logger.getLogger(XdsRepository.class);

	private IConnectionDescription repositoryServerConnection = null;
	private IConnectionDescription registryClientConnection = null;

    /** The XDS Repository Server */    
    IheHTTPServer repositoryServer = null;

    /** The XDS Repository Manager*/
    private IXdsRepositoryManager repositoryManager = null;


    /**
     * Creates a new XdsRepository actor.
     *
     * @param repositoryConnection the connection description of this Repository server
     * 		to accept Provide and Register Document Set and Retrieve Document Set transactions 
     */
     public XdsRepository(IConnectionDescription repositoryServerConnection, IConnectionDescription registryClientConnection) {
         this.repositoryServerConnection = repositoryServerConnection;
         this.registryClientConnection = registryClientConnection;
    }

    
    @Override
	public void start() {
        //call the super one to initiate standard start process
        super.start();

        //initiate the Repository server
        try {
	        //TODO: move this to a global location, and get the repository path
	        String repository = "C:\\tools\\axis2-1.4.1\\repository";        
	        ConfigurationContext configctx = ConfigurationContextFactory
	        .createConfigurationContextFromFileSystem(repository, null);
	        repositoryServer = new IheHTTPServer(configctx, repositoryServerConnection); 		
	
	        Runtime.getRuntime().addShutdownHook(new IheHTTPServer.ShutdownThread(repositoryServer));
	        repositoryServer.start();
	        ListenerManager listenerManager = configctx .getListenerManager();
	        TransportInDescription trsIn = new TransportInDescription(Constants.TRANSPORT_HTTP);
	        trsIn.setReceiver(repositoryServer); 
	        if (listenerManager == null) {
	            listenerManager = new ListenerManager();
	            listenerManager.init(configctx);
	        }
	        listenerManager.addListener(trsIn, true);
        }catch(AxisFault e) {
        	log.error("Failed to initiate the Repository server", e);
        }
        log.info("XDS Repository Server started: " + repositoryServerConnection.getDescription() );
        
    }
    

    @Override
    public void stop() {
        //stop the Repository Server
        repositoryServer.stop();
        log.info("XDS Repository Server stopped: " + repositoryServerConnection.getDescription() );

        //call the super one to initiate standard stop process
        super.stop();
    }

    /**
     * Registers an {@link IXdsRepositoryManager} which delegates repository item insertion,
     * retrieving and deletion from this XDS Repository actor to the 
     * underneath repository manager implementation.
     *
     * @param repositoryManager the {@link IXdsRepositoryManager} to be registered
     */
    public void registerRepositoryManager(IXdsRepositoryManager repositoryManager) {
       this.repositoryManager = repositoryManager;
    }
    
    /**
     * Gets the {@link IXdsRepositoryManager} for this <code>XdsRegistry</code>
     * 
     * @return an {@link IXdsRepositoryManager} instance
     */
    IXdsRepositoryManager getRepositoryManager() {
    	return this.repositoryManager;
    }    
    

}
