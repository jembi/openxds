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

import org.apache.log4j.Logger;
import org.openhealthexchange.openpixpdq.ihe.impl_v2.hl7.HL7Server;
import org.openhealthexchange.openxds.XdsActor;
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
public class XdsRegistry extends XdsActor implements IXdsRegistry {
    /** Logger for problems during SOAP exchanges */
    private static Logger log = Logger.getLogger(XdsRegistry.class);
    /** The connection description to this XDS Registry PIX server */
	private IConnectionDescription pixFeedConnection = null;

    /** The XDS Registry PIX Server */
    private HL7Server server = null;
    /** The XDS Registry Patient Manager*/
    private IXdsRegistryPatientManager patientManager = null;

    @Override
	public void start() {
        //call the super one to initiate standard start process
        super.start();
        //now begin the local start, initiate pix manager server
        LowerLayerProtocol llp = LowerLayerProtocol.makeLLP(); // The transport protocol
        server = new HL7Server(pixFeedConnection, llp, new PipeParser());
        Application pixFeed  = new PixFeedHandler(this);
        
        //Admission of in-patient into a facility
        server.registerApplication("ADT", "A01", pixFeed);  
        //Registration of an out-patient for a visit of the facility
        server.registerApplication("ADT", "A04", pixFeed);  
        //Pre-admission of an in-patient
        server.registerApplication("ADT", "A05", pixFeed);   
        //Update patient information
        server.registerApplication("ADT", "A08", pixFeed);  
        //Merge patients
        server.registerApplication("ADT", "A40", pixFeed);  
        //now start the Pix Manager server
        log.info("Starting XDS Registry: " + this.getName() );
        server.start();
    }

    @Override
    public void stop() {
        //now end the local stop, stop the pix manager server
        server.stop();

        //call the super one to initiate standard stop process
        super.stop();
        
        log.info("XDS Registry stopped: " + this.getName() );

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
	 * port etc) which are needed for this XDS Registry to talk to the PIX Source or Manager.
	 * 
	 * @return the connection of PIX Source/Manager
	 */
	public IConnectionDescription getPixFeedConnection() {
		return pixFeedConnection;
	}

}
