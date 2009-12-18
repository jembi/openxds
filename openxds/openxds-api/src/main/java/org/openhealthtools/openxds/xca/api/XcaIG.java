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
package org.openhealthtools.openxds.xca.api;

import com.misyshealthcare.connect.net.IConnectionDescription;

/**
 * This interface defines the operations of an XCA Responding Gateway actor.
 * 
 * @author <a href="mailto:Anilkumar.reddy@misys.com">Anil Kumar</a>
 *
 */
public interface XcaIG {
	 /**
	 * Starts this XCA Initiating Gateway actor.  Do any initialization and logging that
	 * might be needed.
	 */
	public void start();

	/**
	 * Stops this XCA Initiating Gateway actor.  Do any de-initialization and logging that
	 * might be needed.
	 *
	 */
	public void stop();

    /**
	 * Gets an informative name for this XCA Initiating Gateway actor for use in error
	 * and log messages.
	 *
	 * @return An informative name of this XDS Repository actor
	 */
	public String getName();

	/**
	 * Gets the client side Registry <code>IConnectionDescription</code> of this actor.
	 * 
	 * @return the client side Registry connection
	 */
	public IConnectionDescription getRegistryClientConnection();	

	/**
	 * Gets the client side Repository <code>IConnectionDescription</code> of this actor.
	 * 
	 * @return the client side Repository connection
	 */
	public IConnectionDescription getRepositoryClientConnection();	
	
}
