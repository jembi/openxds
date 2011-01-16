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

package org.openhealthtools.openxds.registry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.actorconfig.IActorDescription;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.audit.IheAuditTrail;
import org.openhealthtools.openexchange.config.PropertyFacade;
import org.openhealthtools.openxds.BaseIheActor;
import org.openhealthtools.openxds.common.XdsConstants;
import org.openhealthtools.openxds.configuration.XdsConfigurationLoader;
import org.openhealthtools.openxds.registry.api.XdsRegistry;
import org.openhealthtools.openxds.registry.api.XdsRegistryPatientService;

import ca.uhn.hl7v2.app.Application;
import ca.uhn.hl7v2.llp.LowerLayerProtocol;
import ca.uhn.hl7v2.parser.PipeParser;

/**
 * This class represents an XDS Registry actor.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class XdsRegistryImpl extends BaseIheActor implements XdsRegistry {
    /** Logger for problems */
    private static Log log = LogFactory.getLog(XdsRegistryImpl.class);
    /** The connection description of this PIX Registry server */
	private IConnectionDescription pixRegistryConnection = null;

    /** The PIX Registry Server */
    private HL7Server pixServer = null;
    /** The XDS Registry Patient Manager*/
    private XdsRegistryPatientService patientManager = null;

    /**
     * Creates a new XdsRegistry actor.
     *
     * @param actorDescription the actor description of this Registry actor
     * @param auditTrail the audit log client  
     */
     public XdsRegistryImpl(IActorDescription actorDescription, IheAuditTrail auditTrail) {
    	 super(actorDescription, auditTrail);   
         this.pixRegistryConnection = actorDescription.getConnectionDescriptionByType(XdsConfigurationLoader.PIXSERVER);
    }

    
    @Override
	public void start() {
        //call the super one to initiate standard start process
        super.start();

        //start the PIX Registry server
        if (initPixRegistry()) 
            log.info("PIX Registry started: " + pixRegistryConnection.getDescription() );        	
        else
            log.fatal("PIX Registry failed to start: " + pixRegistryConnection.getDescription() );        	
    }

	/**
	 * Starts a PIX Registry Server to accept PIX Feed messages. 
	 * 
     * @return <code>true</code> if the initialization is successful; 
     * 		   otherwise <code>false</code>.
	 */
    private boolean initPixRegistry() {
		boolean isSuccess = false;
		try {
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
	        //now start the Pix Registry Server
	        pixServer.start();
	        isSuccess = true;
		}catch(Exception e) {
        	log.fatal("Failed to start the PIX Registry server", e);			
		}
        return isSuccess;
    }
    
//	private void initializeOpenEMPI() {
//		XdsFactory.getInstance().getBean("context");
//		org.openhie.openempi.context.Context.startup();
//		org.openhie.openempi.context.Context.authenticate("admin", "admin");
//	}	
   
    @Override
    public void stop() {
        //stop the PIX Server first
        pixServer.stop();
        log.info("PIX Registry stopped: " + pixRegistryConnection.getDescription() );

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
    public void registerPatientManager(XdsRegistryPatientService patientManager) {
       this.patientManager = patientManager;
    }
    
    /**
     * Gets the patient manager for this <code>XdsRegistry</code>
     * 
     * @return the patient manager
     */
    XdsRegistryPatientService getPatientManager() {
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
		sb.append("/services/DocumentRegistry");

		return sb.toString();
	}
	
}
