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
 * This interface defines the operations of an XDS Registry actor.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public interface IXdsRegistry {
    /**
	 * Starts this XDS Registry actor.  Do any initialization and logging that
	 * might be needed.
	 */
	public void start();

	/**
	 * Stops this XDS Registry actor.  Do any de-initialization and logging that
	 * might be needed.
	 *
	 */
	public void stop();

    /**
	 * Gets an informative name for this XDS Registry actor for use in error
	 * and log messages.
	 *
	 * @return An informative name of this XDS Registry actor
	 */
	public String getName();

}
