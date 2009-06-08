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
 *     Misys plc - Initial API and Implementation
 */
package org.openhealthexchange.openxds.registry.adapter.omar31;

import org.apache.axiom.om.OMElement;
import org.openhealthexchange.openxds.registry.api.IXdsRegistryLifeCycleManager;
import org.openhealthexchange.openxds.registry.api.RegistryLifeCycleContext;
import org.openhealthexchange.openxds.registry.api.RegistryLifeCycleException;

/**
 * This class adapts to the freebXML Omar 3.1 registry and 
 * defines the operations to manipulate XDS Registry
 * objects.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 *
 */
public class XdsRegistryLifeCycleManager implements IXdsRegistryLifeCycleManager {
	
	public OMElement submitObjects(OMElement request, RegistryLifeCycleContext context)  throws RegistryLifeCycleException {
		//TODO: implement
		return null;
	}

	public void mergePatients(String survivingPatient, String mergePatient, 
			RegistryLifeCycleContext context) throws RegistryLifeCycleException {
		//TODO: implement
	}

	
	public OMElement approveObjects(OMElement request, RegistryLifeCycleContext context) throws RegistryLifeCycleException {
		//TODO: implement
		return null;
	}

	public OMElement deprecateObjects(OMElement request, RegistryLifeCycleContext context) throws RegistryLifeCycleException {
		//TODO: implement
		return null;		
	}

}
