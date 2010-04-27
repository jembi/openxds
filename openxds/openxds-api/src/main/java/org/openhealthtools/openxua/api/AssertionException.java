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

package org.openhealthtools.openxua.api;

/**
* 
* @author <a href="mailto:venkata.pragallapati@misys.com">Venkat</a>
* 
*/
public class AssertionException extends XuaException {

	private static final long serialVersionUID = 1L;

	public AssertionException() {
	}

	public AssertionException(String message) {
		super(message);
	}

	public AssertionException(Throwable cause) {
		super(cause);
	}

	public AssertionException(String message, Throwable cause) {
		super(message, cause);
	}

}
