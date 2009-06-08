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
package org.openhealthexchange.openxds.registry.api;

/**
 * This exception is generated when there is a problem
 * with XDS Registry life cycle operations
 *
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class RegistryLifeCycleException extends Exception {
	
	private static final long serialVersionUID = 8029331164655858930L;
				
	/**
	 * Creates a new RegistryLifeCycleException.
	 * 
	 * @param msg a description of the problem
	 */
	public RegistryLifeCycleException(String msg) {
		super(msg);
	}

	/**
	 * Creates a new RegistryLifeCycleException.
	 * 
	 * @param msg a description of the problem
	 * @param cause the embedded Throwable
	 */
    public RegistryLifeCycleException(String msg, Throwable cause){
        super(msg, cause);
    }

    
	/**
	 * Creates a new RegistryLifeCycleException.
	 * 
	 * @param cause the embedded Throwable
	 */
    public RegistryLifeCycleException(Throwable cause) {
        super(cause);
    }

}
