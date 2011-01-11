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
package org.openhealthtools.openxds.common;

/**
 * This class defines the constants for XDS.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class XdsConstants {
	
	//**********************************************************************
	// Configuration Property Names
	//**********************************************************************
    //Actor configuration parameters
	public static final String IHE_ACTORS_DIR  = "ihe.actors.dir";
	public static final String IHE_ACTORS_FILE = "ihe.actors.file";
	//App Server properties
	public static final String HOST            = "host";
	public static final String PORT            = "port";
	public static final String TLS_PORT        = "tls.port";
	public static final String KEY_STORE       = "key.store";
	public static final String KEY_STORE_PW    = "key.store.password";
	public static final String TRUST_STORE     = "trust.store";
	public static final String TRUST_STORE_PW  = "trust.store.password";
	
	public static final String WEB_APP_CONTEXT = "context.path";
	public static final String WEB_APP_ROOT    = "webapp.root";
	//Regitry properties
	public static final String DOC_SOURCE_IDS    = "document.source.ids";
	//Gateway properties
	public static final String IG_THREADS_NUMBER = "ig.threads.number";
	public static final String HOME_COMMUNITY_ID = "home.community.id";

	
	//**********************************************************************
	// Keywords
	//**********************************************************************
	public static final String REGISTRY_ACTORS   = "XDSREGISTRY_ACTORS";
	public static final String REPOSITORY_ACTORS = "XDSREPOSITORY_ACTORS";
	public static final String RG_ACTORS         = "XCA_RG_ACTORS";
	public static final String IG_ACTORS         = "XCA_IG_ACTORS";
	
}
