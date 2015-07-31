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

package org.openhealthtools.openxds.registry.adapter.omar31;

/**
 * This class represents and exception that occurs while
 * processing an XdsRegistryObject.
 * 
 * @author Jim Firby
 * @version 2.0 - Nov 6, 2005
 */
public class XdsRimException extends Exception {

	private static final long serialVersionUID = -40188658159928982L;

	/**
	 * Create a new XdsRegistryObjectException.
	 * 
	 * @param string A description of the problem
	 */
	public XdsRimException(String string) {
		super(string);
	}
	
	/**
	 * Create a new XdsRegistryObjectException.
	 * 
	 * @param string A description of the problem
	 * @param e The reason for the problem
	 */
	public XdsRimException(String string, Throwable e) {
		super(string, e);
	}
	
}
