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

package org.openhealthtools.openxds.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.actorconfig.IBrokerController;
import org.openhealthtools.openxds.registry.api.XdsRegistry;
import org.openhealthtools.openxds.repository.api.XdsRepository;
import org.openhealthtools.openxds.xca.api.XcaIG;
import org.openhealthtools.openxds.xca.api.XcaRG;


/**
 * This class presents a single global <code>XdsBroker</code>
 * instance to the OpenXDS code.  It can simply be
 * initialized and then requested from any code whenever necessary.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class XdsBroker   {
	
	/** A single instance for this class */
	private static XdsBroker singleton = null;
	
	/** Log for problems encountered by the XdsBroker */
	private static final Log log = LogFactory.getLog(XdsBroker.class);
		
    /** A list of all known xds registries */
    private Vector<XdsRegistry> xdsRegistries = new Vector<XdsRegistry>();
    /** A list of all known xds repositories */
    private Vector<XdsRepository> xdsRepositories = new Vector<XdsRepository>();
    /** A list of all known xca responding gateway */
    private Vector<XcaRG> xcaRGs = new Vector<XcaRG>();
    /** A list of all known xca Initiating gateway */
    private Vector<XcaIG> xcaIGs = new Vector<XcaIG>();

	/**
	 * A private constructor for creating the singleton instance.
	 */
	private XdsBroker() {
		super();
	}
	
	/**
	 * Gets the single global instance of the <code>XdsBroker</code>.
	 * 
	 * @return the patient broker
	 */
	public static synchronized XdsBroker getInstance() {
		if (singleton == null) {
			singleton = new XdsBroker();
		}
		return singleton;
	}
 	
	/**
	 * Gets a collection of XDS Registry actors
	 * 
	 * @return the XdsRegistry Collection
	 */
	public Collection<XdsRegistry> getXdsRegistries(){
		return Collections.unmodifiableCollection(this.xdsRegistries);
	}
	
	/**
	 * Gets a collection of XDS Repository actors
	 * 
	 * @return the XdsRepository Collection
	 */
	public Collection<XdsRepository> getXdsRepositories(){
		return Collections.unmodifiableCollection(this.xdsRepositories);
	}
	
	/**
	 * Gets a collection of XCA Responding Gateway actors
	 * 
	 * @return the XCA Responding Gateway Collection
	 */
	public Collection<XcaRG> getXcaRG(){
		return Collections.unmodifiableCollection(this.xcaRGs);
	}
	
	/**
	 * Gets a collection of XCA Initiating Gateway actors
	 * 
	 * @return the XCA Initiating Gateway Collection
	 */
	public Collection<XcaIG> getXcaIG(){
		return Collections.unmodifiableCollection(this.xcaIGs);
	}
    /**
	 * Registers a new XDS Registry.  This method
	 * is typically called when an XDS Registry server is started.
	 *
	 * @param xdsRegistry an XDS Registry
	 * @return <code>true</code> if this XDS Registry was successfully added
	 */
    public synchronized boolean registerXdsRegistry(XdsRegistry xdsRegistry) {
		// If the xdsRegistry is new, add it to the list
		if ((xdsRegistry != null) && (!xdsRegistries.contains(xdsRegistry))) {
			xdsRegistry.start();
			xdsRegistries.add(xdsRegistry);
			return true;
		} else {
			return false;
		}
	}

    /**
	 * Registers a new XDS Repository.  This method
	 * is typically called when an XDS Repository server is started.
	 *
	 * @param xdsRepository an XDS Repository
	 * @return <code>true</code> if this XDS Repository was successfully added
	 */
    public synchronized boolean registerXdsRepository(XdsRepository xdsRepository) {
		// If the xdsRepository is new, add it to the list
		if ((xdsRepository != null) && (!xdsRepositories.contains(xdsRepository))) {
			xdsRepository.start();
			xdsRepositories.add(xdsRepository);
			return true;
		} else {
			return false;
		}
	}
 

    /**
	 * Unregisters the active XDS Registries specified by the controller.  If the
	 * controller is null unregister all XDS Registries.  An XDS Registry is stopped 
	 * when it is unregistered.
	 *
	 * @param controller the controller specifying which XDS Registry to unregister. All XDS
	 * Registries will be unregistered if it is null. 
	 * @return <code>true</code> if all Registries were actually unregistered
	 */
     public synchronized boolean unregisterXdsRegistries(IBrokerController controller) {
		ArrayList<XdsRegistry> removed = new ArrayList<XdsRegistry>();
		// Find all the sources to remove
		for (XdsRegistry actor: xdsRegistries) {
			if ((controller == null) || controller.shouldUnregister(actor)) {
				removed.add(actor);
			}
		}
		if (removed.isEmpty()) return false;
		// Remove them
		xdsRegistries.removeAll(removed);
		// Stop them all too
		for (XdsRegistry actor: removed) actor.stop();
		return true;
	}

    /**
 	 * Unregisters the active XDS Repositories specified by the controller.  If the
 	 * controller is null unregister all XDS Repositories.  An XDS Repository is stopped 
 	 * when it is unregistered.
 	 *
 	 * @param controller the controller specifying which XDS Repository to unregister. All XDS
 	 * Repositories will be unregistered if it is null. 
 	 * @return <code>true</code> if all Repositories were actually unregistered
 	 */
     public synchronized boolean unregisterXdsRepositories(IBrokerController controller) {
 		ArrayList<XdsRepository> removed = new ArrayList<XdsRepository>();
 		// Find all the sources to remove
 		for (XdsRepository actor: xdsRepositories) {
 			if ((controller == null) || controller.shouldUnregister(actor)) {
 				removed.add(actor);
 			}
 		}
 		if (removed.isEmpty()) return false;
 		// Remove them
 		xdsRepositories.removeAll(removed);
 		// Stop them all too
 		for (XdsRepository actor: removed) actor.stop();
 		return true;
 	}
	
     /**
 	 * Registers a new XCA Responding Gateway.  This method
 	 * is typically called when an XCA Gateway server is started.
 	 *
 	 * @param xcaRG an XCA Responding Gateway
 	 * @return <code>true</code> if this XCA Responding Gateway was successfully added
 	 */
     public synchronized boolean registerXcaRG(XcaRG xcaRG) {
 		// If the xcaRG is new, add it to the list
 		if ((xcaRG != null) && (!xcaRGs.contains(xcaRG))) {
 			xcaRG.start();
 			xcaRGs.add(xcaRG);
 			return true;
 		} else {
 			return false;
 		}
 	}

    /**
  	 * Unregisters the active XCA Responding Gateway specified by the controller.  If the
  	 * controller is null unregister all XCA Responding Gateway.  An XCA Responding Gateway is stopped 
  	 * when it is unregistered.
  	 *
  	 * @param controller the controller specifying which XCA Responding Gatway to unregister. All XDS
  	 * Responding Gateways will be unregistered if it is null. 
  	 * @return <code>true</code> if all Responding Gateways were actually unregistered
  	 */
      public synchronized boolean unregisterXcaRG(IBrokerController controller) {
  		ArrayList<XcaRG> removed = new ArrayList<XcaRG>();
  		// Find all the sources to remove
  		for (XcaRG actor: xcaRGs) {
  			if ((controller == null) || controller.shouldUnregister(actor)) {
  				removed.add(actor);
  			}
  		}
  		if (removed.isEmpty()) return false;
  		// Remove them
  		xcaRGs.removeAll(removed);
  		// Stop them all too
  		for (XcaRG actor: removed) actor.stop();
  		return true;
  	}
      
      /**
   	 * Registers a new XCA Initiating Gateway.  This method
   	 * is typically called when an XCA Gateway server is started.
   	 *
   	 * @param xcaRG an XCA Initiating Gateway
   	 * @return <code>true</code> if this XCA Initiating Gateway was successfully added
   	 */
       public synchronized boolean registerXcaIG(XcaIG xcaIG) {
   		// If the xcaRG is new, add it to the list
   		if ((xcaIG != null) && (!xcaIGs.contains(xcaIG))) {
   			xcaIG.start();
   			xcaIGs.add(xcaIG);
   			return true;
   		} else {
   			return false;
   		}
   	}

      /**
    	 * Unregisters the active XCA Initiating Gateway specified by the controller.  If the
    	 * controller is null unregister all XCA Initiating Gateway.  An XCA Initiating Gateway is stopped 
    	 * when it is unregistered.
    	 *
    	 * @param controller the controller specifying which XCA Initiating Gatway to unregister. All XDS
    	 * Initiating Gateways will be unregistered if it is null. 
    	 * @return <code>true</code> if all Initiating Gateways were actually unregistered
    	 */
        public synchronized boolean unregisterXcaIG(IBrokerController controller) {
    		ArrayList<XcaIG> removed = new ArrayList<XcaIG>();
    		// Find all the sources to remove
    		for (XcaIG actor: xcaIGs) {
    			if ((controller == null) || controller.shouldUnregister(actor)) {
    				removed.add(actor);
    			}
    		}
    		if (removed.isEmpty()) return false;
    		// Remove them
    		xcaIGs.removeAll(removed);
    		// Stop them all too
    		for (XcaIG actor: removed) actor.stop();
    		return true;
    	}
     
}
