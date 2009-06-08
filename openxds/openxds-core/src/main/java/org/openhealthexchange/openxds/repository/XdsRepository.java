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

import org.apache.log4j.Logger;
import org.openhealthexchange.openxds.XdsActor;
import org.openhealthexchange.openxds.repository.api.IXdsRepository;
import org.openhealthexchange.openxds.repository.api.IXdsRepositoryManager;

/**
 * This class represents an XDS Repository actor.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class XdsRepository extends XdsActor implements IXdsRepository {
    /** Logger for problems during SOAP exchanges */
    private static Logger log = Logger.getLogger(XdsRepository.class);

    /** The XDS Repository Manager*/
    private IXdsRepositoryManager repositoryManager = null;

    @Override
    public void start() {
        //call the super one to initiate standard start process
        super.start();
    }

    @Override
    public void stop() {

        //call the super one to initiate standard stop process
        super.stop();
        
        log.info("XDS Repository stopped: " + this.getName() );

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
