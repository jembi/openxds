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

package org.openhealthtools.common.audit;

import com.misyshealthcare.connect.base.audit.AuditCodeMappings.AuditEventIds;
import com.misyshealthcare.connect.base.audit.AuditCodeMappings.AuditTypeCodes;
import com.misyshealthcare.connect.base.audit.AuditCodeMappings.EventActionCode;
import com.misyshealthcare.connect.base.audit.AuditCodeMappings.SecurityAlertType;
import com.misyshealthcare.connect.base.audit.AuditCodeMappings.SuccessCode;

/** 
 * Used internally to define the different possible events.
 * 
 * See the audit code mappings file to know what all the different codes
 * mean.
 *   
 * @author Josh Flachsbart
 * @version 1.0 - Nov 17, 2005
 */
public class EventId {
	public AuditEventIds eventId;
	public AuditTypeCodes typeCode;
	public EventActionCode actionCode; // Normally 'C' create 'R' read 'U' update 'D' delete 'E'
	public SuccessCode outcome;
	
	public EventId(AuditEventIds eventId, AuditTypeCodes typeCode, EventActionCode actionCode, SuccessCode outcome) {
		this.eventId = eventId;
		this.typeCode = typeCode;
		this.actionCode = actionCode;
		this.outcome = outcome;
	}

	public EventId(AuditEventIds eventId, SecurityAlertType typeCode, EventActionCode actionCode, SuccessCode outcome) {
		this.eventId = eventId;
		this.typeCode = typeCode.getValue();
		this.actionCode = actionCode;
		this.outcome = outcome;
	}
}
