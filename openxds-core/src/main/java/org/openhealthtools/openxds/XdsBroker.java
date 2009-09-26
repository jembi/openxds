/**
 *  Copyright (c) 2009 Misys Open Source Solutions (MOSS) and others
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
 *
 */
package org.openhealthtools.openxds;

import java.util.ArrayList;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.openhealthexchange.openpixpdq.ihe.IPdSupplier;
import org.openhealthexchange.openpixpdq.ihe.IPixManager;
import org.openhealthtools.openxds.registry.api.IXdsRegistry;
import org.openhealthtools.openxds.repository.api.IXdsRepository;

import com.misyshealthcare.connect.base.IBrokerController;

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
	private static final Logger log = Logger.getLogger(XdsBroker.class);
		
    /** A list of all known xds registries */
    private Vector<IXdsRegistry> xdsRegistries = new Vector<IXdsRegistry>();
    /** A list of all known xds repositories */
    private Vector<IXdsRepository> xdsRepositories = new Vector<IXdsRepository>();

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
	 * Registers a new XDS Registry.  This method
	 * is typically called when an XDS Registry server is started.
	 *
	 * @param xdsRegistry an XDS Registry
	 * @return <code>true</code> if this XDS Registry was successfully added
	 */
    public synchronized boolean registerXdsRegistry(IXdsRegistry xdsRegistry) {
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
    public synchronized boolean registerXdsRepository(IXdsRepository xdsRepository) {
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
		ArrayList<IXdsRegistry> removed = new ArrayList<IXdsRegistry>();
		// Find all the sources to remove
		for (IXdsRegistry actor: xdsRegistries) {
			if ((controller == null) || controller.shouldUnregister(actor)) {
				removed.add(actor);
			}
		}
		if (removed.isEmpty()) return false;
		// Remove them
		xdsRegistries.removeAll(removed);
		// Stop them all too
		for (IXdsRegistry actor: removed) actor.stop();
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
 		ArrayList<IXdsRepository> removed = new ArrayList<IXdsRepository>();
 		// Find all the sources to remove
 		for (IXdsRepository actor: xdsRepositories) {
 			if ((controller == null) || controller.shouldUnregister(actor)) {
 				removed.add(actor);
 			}
 		}
 		if (removed.isEmpty()) return false;
 		// Remove them
 		xdsRepositories.removeAll(removed);
 		// Stop them all too
 		for (IXdsRepository actor: removed) actor.stop();
 		return true;
 	}
	
}
