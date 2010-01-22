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

import org.openhealthexchange.openpixpdq.data.MessageHeader;

/**
 * This classes defines a registry patient context that are required for
 * the patient operations of the patient manager service.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class RegistryPatientContext {
	private MessageHeader messageHeader;

	/**
	 * Constructor
	 * 
	 * @param messageHeader
	 */
	public RegistryPatientContext(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}
	
	/**
	 * Gets messageHeader.
	 *
	 * @return the messageHeader
	 */
	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	/**
	 * Sets messageHeader.
	 *
	 * @param messageHeader the messageHeader to set
	 */
	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}
	
	
}
