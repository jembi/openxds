/* Copyright 2009 Misys PLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License. 
 */
package org.openhealthtools.openxds.registry;

import java.io.Serializable;

/**
 * @author <a href="mailto:Rasakannu.Palaniyandi@misys.com">Raja</a>
 */

public class PersonIdentifier implements Serializable{
	
	private static final long serialVersionUID = 6202229271840160669L;
	private String patientId;
      private String assigningAuthority;
      private String survivingPatientId;
      private String registryPatientId;
      private boolean deleted;
      private boolean merged;

	public String getAssigningAuthority() {
		return assigningAuthority;
	}
	public void setAssigningAuthority(String assigningAuthority) {
		this.assigningAuthority = assigningAuthority;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getSurvivingPatientId() {
		return survivingPatientId;
	}
	public void setSurvivingPatientId(String survivingPatientId) {
		this.survivingPatientId = survivingPatientId;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public boolean isMerged() {
		return merged;
	}
	public void setMerged(boolean merged) {
		this.merged = merged;
	}
	public String getRegistryPatientId() {
		return registryPatientId;
	}
	public void setRegistryPatientId(String registryPatientId) {
		this.registryPatientId = registryPatientId;
	}
      

}
