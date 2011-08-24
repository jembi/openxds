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

package org.openhealthtools.openxds.registry.api;

import java.util.Map;


/**
 * This class defines a registry query context that are required for
 * query operations.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class RegistryQueryContext {

	/**Whether the ReponseOption is LeafClass; otherwise it is ObjectRef.*/
	private boolean isLeafClass;

	/**
	 * Constructor
	 * 
	 * @param isLeafClass whether this query returns a LeafClass for
	 * 		  the query result. 
	 */
	public RegistryQueryContext(boolean isLeafClass) {
		super();
		this.isLeafClass = isLeafClass;
	}

	/**
	 * @return the isLeafClass
	 */
	public boolean isLeafClass() {
		return isLeafClass;
	}

	/**
	 * @param isLeafClass the isLeafClass to set
	 */
	public void setLeafClass(boolean isLeafClass) {
		this.isLeafClass = isLeafClass;
	}

	
}
