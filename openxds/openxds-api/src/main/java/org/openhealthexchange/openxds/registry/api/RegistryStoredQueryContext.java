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

import java.util.Map;

/**
 * This classes defines a registry context for a Registry Sotred query.  
 *   
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class RegistryStoredQueryContext extends RegistryQueryContext {

	private String queryId;
	private Map<String, Object> queryParameters;
	
	/**
	 * Constructor
	 * 
	 * @param queryId
	 * @param queryParameters
	 */
	public RegistryStoredQueryContext(String queryId, Map queryParameters, boolean isLeafClass) {
		super(isLeafClass);
		this.queryId = queryId;
		this.queryParameters = queryParameters;
	}

	/**
	 * Gets queryId.
	 *
	 * @return the queryId
	 */
	public String getQueryId() {
		return queryId;
	}

	/**
	 * Gets queryParameters.
	 *
	 * @return the queryParameters
	 */
	public Map<String, Object> getQueryParameters() {
		return queryParameters;
	}
	
	
	
}
