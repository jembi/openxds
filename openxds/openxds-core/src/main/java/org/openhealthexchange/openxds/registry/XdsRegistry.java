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
package org.openhealthexchange.openxds.registry;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.description.TransportInDescription;
import org.apache.axis2.engine.ListenerManager;
import org.apache.log4j.Logger;
import org.openhealthexchange.common.configuration.ModuleManager;
import org.openhealthexchange.common.ihe.IheActor;
import org.openhealthexchange.common.ws.server.IheHTTPServer;
import org.openhealthexchange.common.audit.IheAuditTrail;
import org.openhealthexchange.openpixpdq.ihe.impl_v2.hl7.HL7Server;
import org.openhealthexchange.openxds.registry.api.IXdsRegistry;
import org.openhealthexchange.openxds.registry.api.IXdsRegistryPatientManager;

import ca.uhn.hl7v2.app.Application;
import ca.uhn.hl7v2.llp.LowerLayerProtocol;
import ca.uhn.hl7v2.parser.PipeParser;

import com.misyshealthcare.connect.net.IConnectionDescription;

/**
 * This class represents an XDS Registry actor.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class XdsRegistry extends IheActor implements IXdsRegistry {
    /** Logger for problems */
    private static Logger log = Logger.getLogger(XdsRegistry.class);
    /** The connection description of this PIX Registry server */
	private IConnectionDescription pixRegistryConnection = null;

    /** The XDS Registry PIX Server */
    private HL7Server pixServer = null;
    /** The XDS Registry Server */    
    IheHTTPServer registryServer = null;
    /** The XDS Registry Patient Manager*/
    private IXdsRegistryPatientManager patientManager = null;

    /**
     * Creates a new XdsRegistry actor.
     *
     * @param pixFeedConnection the connection description of this PIX server
     * @param registryConnection the connection description of this Registry server
     * 				to accept Register Document Set and Stored Query transactions 
     */
     public XdsRegistry(IConnectionDescription pixFeedConnection,
    		 IConnectionDescription registryConnection, IheAuditTrail auditTrail) {
    	 super(registryConnection, auditTrail);
         this.pixRegistryConnection = pixFeedConnection;
         this.connection = registryConnection;
    }

    
    @Override
	public void start() {
        //call the super one to initiate standard start process
        super.start();

        //1. First initiate the Registry server
        try {
	        //TODO: move this to a global location, and get the repository path
	        String repository = "C:\\tools\\axis2-1.4.1\\repository";        
	        ConfigurationContext configctx = ConfigurationContextFactory
	        .createConfigurationContextFromFileSystem(repository, null);
	        registryServer = new IheHTTPServer(configctx, this); 		
	
	        Runtime.getRuntime().addShutdownHook(new IheHTTPServer.ShutdownThread(registryServer));
	        registryServer.start();
	        ListenerManager listenerManager = configctx .getListenerManager();
	        TransportInDescription trsIn = new TransportInDescription(Constants.TRANSPORT_HTTP);
	        trsIn.setReceiver(registryServer); 
	        if (listenerManager == null) {
	            listenerManager = new ListenerManager();
	            listenerManager.init(configctx);
	        }
	        listenerManager.addListener(trsIn, true);
        }catch(AxisFault e) {
        	log.error("Failed to initiate the Registry server", e);
        }
        log.info("XDS Registry Server started: " + connection.getDescription() );
        
        //2. now initiate PIX Server
        LowerLayerProtocol llp = LowerLayerProtocol.makeLLP(); // The transport protocol
        pixServer = new HL7Server(pixRegistryConnection, llp, new PipeParser());
        Application pixFeed  = new PixFeedHandler(this);
        
        //Admission of in-patient into a facility
        pixServer.registerApplication("ADT", "A01", pixFeed);  
        //Registration of an out-patient for a visit of the facility
        pixServer.registerApplication("ADT", "A04", pixFeed);  
        //Pre-admission of an in-patient
        pixServer.registerApplication("ADT", "A05", pixFeed);   
        //Update patient information
        pixServer.registerApplication("ADT", "A08", pixFeed);  
        //Merge patients
        pixServer.registerApplication("ADT", "A40", pixFeed);  
        //now start the Pix Manager pixServer
        pixServer.start();
        log.info("XDS PIX Registry Server started: " + pixRegistryConnection.getDescription() );
        
        //3. initialize OpenEMPI
//		initializeOpenEMPI();

    }

	private void initializeOpenEMPI() {
		ModuleManager.getInstance().getBean("context");
//		org.openhie.openempi.context.Context.startup();
		org.openhie.openempi.context.Context.authenticate("admin", "admin");
	}
   
    @Override
    public void stop() {
        //stop the PIX Server first
        pixServer.stop();
        log.info("PIX Registry Server stopped: " + pixRegistryConnection.getDescription() );

        //stop the Registry Server
        registryServer.stop();
        log.info("XDS Registry Server stopped: " + connection.getDescription() );
        
        //call the super one to initiate standard stop process 
        super.stop();

    }

    /**
     * Registers an IXdsRegistryPatientManager which delegates patient creation,
     * merge and patient validation from this XDS Registry actor to the 
     * underneath patient manager implementation.
     *
     * @param patientManager the patient manager to be registered
     */
    public void registerPatientManager(IXdsRegistryPatientManager patientManager) {
       this.patientManager = patientManager;
    }
    
    /**
     * Gets the patient manager for this <code>XdsRegistry</code>
     * 
     * @return the patient manager
     */
    IXdsRegistryPatientManager getPatientManager() {
    	return this.patientManager;
    }    
    
	/**
	 * Gets the connection for the PIX Feed. The connection provides the details (such as 
	 * port etc) which are needed for this PIX Registry to talk to the PIX Source or Manager.
	 * 
	 * @return the connection of PIX Source/Manager
	 */
	public IConnectionDescription getPixRegistryConnection() {
		return pixRegistryConnection;
	}

}
