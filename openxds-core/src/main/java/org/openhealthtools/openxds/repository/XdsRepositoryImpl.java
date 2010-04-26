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

package org.openhealthtools.openxds.repository;

import gov.nist.registry.common2.registry.Properties;

import java.io.File;
import java.net.URL;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.description.TransportInDescription;
import org.apache.axis2.engine.ListenerManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.common.utils.UnZip;
import org.openhealthtools.common.ws.server.IheHTTPServer;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.audit.IheAuditTrail;
import org.openhealthtools.openxds.BaseIheActor;
import org.openhealthtools.openxds.registry.XdsRegistryImpl;
import org.openhealthtools.openxds.repository.api.XdsRepository;
import org.openhealthtools.openxds.repository.api.XdsRepositoryService;

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

    /** The XDS Repository Server */    
    IheHTTPServer repositoryServer = null;

    /** The XDS Repository Manager*/
    private XdsRepositoryService repositoryManager = null;


    /**
     * Creates a new XdsRepository actor.
     *
     * @param repositoryConnection the connection description of this Repository server
     * 		to accept Provide and Register Document Set and Retrieve Document Set transactions 
     */
     public XdsRepositoryImpl(IConnectionDescription repositoryServerConnection, IConnectionDescription registryClientConnection, IheAuditTrail auditTrail) {
    	 super(repositoryServerConnection, auditTrail);
         this.connection = repositoryServerConnection;
         this.registryClientConnection = registryClientConnection;
    }

    
    @Override
	public void start() {
        //call the super one to initiate standard start process
        super.start();

        //start the Repository server
        if (initXdsRepository()) 
            log.info("XDS Repository started: " + connection.getDescription() );        	
        else
            log.fatal("XDS Repository initialization failed: " + connection.getDescription() );        	
    }
    
    private boolean initXdsRepository() {
		boolean isSuccess = false;
        try {
	        String axis2repopath = null;
	        String axis2xmlpath = null;	        	
	        String repo = Properties.loader().getString("axis2.repo.dir");
	        URL repoPath = XdsRegistryImpl.class.getResource(repo);
		    if (repoPath != null) {
			        axis2repopath = repoPath.getPath();
			        axis2xmlpath = repoPath.getPath() +"/axis2.xml";
		    } else  if (new File(repo).exists()) {
		        axis2repopath = repo;
		        axis2xmlpath = repo +"/axis2.xml";	        
	        } else {
		        URL axis2repo = XdsRegistryImpl.class.getResource("/axis2repository");
		        URL axis2xml = XdsRegistryImpl.class.getResource("/axis2repository/axis2.xml");
		        axis2repopath = axis2repo.getPath();
		        axis2xmlpath = axis2xml.getPath();
		        if(axis2repopath.contains(".jar")){
		        	UnZip zip =new UnZip();
				    zip.unZip(axis2repopath,repo);
				    axis2repopath = repo;
				    axis2xmlpath = repo +"/axis2.xml"; 	
			    }
	        }
	        ConfigurationContext configctx = ConfigurationContextFactory
	        .createConfigurationContextFromFileSystem(axis2repopath, axis2xmlpath);
	        repositoryServer = new IheHTTPServer(configctx, this); 		
	
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
	        isSuccess = true;
        }catch(AxisFault e) {
        	log.fatal("Failed to start the XDS Repository server", e);			
        }

        return isSuccess;
    }
    
    @Override
    public void stop() {
        //stop the Repository Server
        repositoryServer.stop();
        log.info("XDS Repository stopped: " + connection.getDescription() );

        //call the super one to initiate standard stop process
        super.stop();
    }

    /**
     * Registers an {@link XdsRepositoryService} which delegates repository item insertion,
     * retrieving and deletion from this XDS Repository actor to the 
     * underneath repository manager implementation.
     *
     * @param repositoryManager the {@link XdsRepositoryService} to be registered
     */
    public void registerRepositoryManager(XdsRepositoryService repositoryManager) {
       this.repositoryManager = repositoryManager;
    }
    
    /**
     * Gets the {@link XdsRepositoryService} for this <code>XdsRegistry</code>
     * 
     * @return an {@link XdsRepositoryService} instance
     */
    XdsRepositoryService getRepositoryManager() {
    	return this.repositoryManager;
    }    
    
	/**
	 * Gets the client side Registry <code>IConnectionDescription</code> of this actor.
	 * 
	 * @return the client side Registry connection
	 */
	public IConnectionDescription getRegistryClientConnection() {
		return registryClientConnection;
	}

}
