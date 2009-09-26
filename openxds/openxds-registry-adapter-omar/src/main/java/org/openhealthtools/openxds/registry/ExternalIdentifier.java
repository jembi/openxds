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
package org.openhealthtools.openxds.registry;

import java.io.Serializable;
/*
 * @author <a href="mailto:Rasakannu.Palaniyandi@misys.com">Raja</a>
 */
@SuppressWarnings("serial") 	// Suppress the static serial version - Spring recommended
public class ExternalIdentifier implements Serializable{
	
	private String id ;
    private String home;
    private String lid;
    private String objecttype;
    private String status;
    private String versionname;
    private String comment;
    private String registryobject;
    private String identificationscheme;
    private String value;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHome() {
		return home;
	}
	public void setHome(String home) {
		this.home = home;
	}
	public String getLid() {
		return lid;
	}
	public void setLid(String lid) {
		this.lid = lid;
	}
	public String getObjecttype() {
		return objecttype;
	}
	public void setObjecttype(String objecttype) {
		this.objecttype = objecttype;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getVersionname() {
		return versionname;
	}
	public void setVersionname(String versionname) {
		this.versionname = versionname;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getRegistryobject() {
		return registryobject;
	}
	public void setRegistryobject(String registryobject) {
		this.registryobject = registryobject;
	}
	public String getIdentificationscheme() {
		return identificationscheme;
	}
	public void setIdentificationscheme(String identificationscheme) {
		this.identificationscheme = identificationscheme;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}
