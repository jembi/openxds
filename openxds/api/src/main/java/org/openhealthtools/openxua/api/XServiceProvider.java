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

package org.openhealthtools.openxua.api;

import org.apache.axiom.om.OMElement;

/**
* This interface defines the operations to validate user identity assertion
* 
* @author <a href="mailto:venkata.pragallapati@misys.com">Venkat</a>
* 
*/
public interface XServiceProvider {
	/**
	 * Validate X-User Assertion and return true if it is valid assertion otherwise invalid assertion 
	 * @param assertion
	 * @return
	 * @throws AssertionException
	 */
	public boolean validateToken(OMElement assertion)
			throws AssertionException;
	
}
