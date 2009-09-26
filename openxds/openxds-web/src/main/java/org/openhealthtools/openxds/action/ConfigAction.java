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
package org.openhealthtools.openxds.action;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthexchange.openpixpdq.ihe.configuration.IheActorDescription;
import org.openhealthtools.openxds.configuration.XdsConfigurationLoader;

public class ConfigAction extends BaseAction {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6988013154589164235L;
	private String action = null;
	private String configFile = null;
	private String logfile = null;
	private String[] actors = null;
    protected transient final Log log = LogFactory.getLog(getClass());
    
	public String execute() throws Exception {
		return SUCCESS;
	}
	

	@SuppressWarnings("unchecked")
	public String configuration() {
		try {
			if (getAction() == null || getAction().equals("")) {
				configFile = null;
				logfile = null;
				actors = null;
				List<IheActorDescription> l = (Vector) XdsConfigurationLoader.getInstance().getActorDescriptions();
				
				Collections.sort(l, new compareTypes());
				getRequest().setAttribute("ActorList", l);
				String[] sList = new String[l.size()];
				int x = 0;
				for (IheActorDescription ida : l) {
					if (ida.isInstalled()) {
						sList[x++] = ida.getId();
					}
				}
				setActors(sList);
				return SUCCESS;
			}
			if (getAction().equalsIgnoreCase("LoadActors")) {
				//First reset the config settings before loading
				XdsConfigurationLoader.getInstance().resetConfiguration(null, null);
				XdsConfigurationLoader.getInstance().loadConfiguration(getConfigFile(), false);
				List<IheActorDescription> l = (Vector) XdsConfigurationLoader.getInstance().getActorDescriptions();
				Collections.sort(l, new compareTypes());
				getRequest().setAttribute("ActorList", l);
				String[] sList = new String[l.size()];
				int x = 0;
				for (IheActorDescription ida : l) {
					if (ida.isInstalled()) {
						sList[x++] = ida.getId();
					}
				}
				getRequest().setAttribute("Actors", sList);
				setActors(sList);
				return SUCCESS;
			} else if (getAction().equalsIgnoreCase("Start")) {
				List<Object> lString = new LinkedList<Object>();
				StringBuffer selectedActors = new StringBuffer();
				for (String s : getActors()) {
					if (selectedActors.length() > 0)
						selectedActors.append(",");
					selectedActors.append(s);
					lString.add(s);
				}
				String sLogFile = getLogfile();
				if (sLogFile != null && !sLogFile.equals("")) {
					XdsConfigurationLoader.getInstance().resetConfiguration(lString, sLogFile);
				} else {
					XdsConfigurationLoader.getInstance().resetConfiguration(lString, null);
				}
				List<IheActorDescription> l = (Vector) XdsConfigurationLoader.getInstance().getActorDescriptions();
				Collections.sort(l, new compareTypes());
				getRequest().setAttribute("ActorList", l);
				return SUCCESS;
			} else if (getAction().equalsIgnoreCase("stopall")) {
				XdsConfigurationLoader.getInstance().resetConfiguration(null, null);
				List<IheActorDescription> l = (Vector) XdsConfigurationLoader.getInstance().getActorDescriptions();
				Collections.sort(l, new compareTypes());
				getRequest().setAttribute("ActorList", l);
				setActors(null);
				return SUCCESS;
			}

		} catch (Exception e) {
			return null;
		}
		return SUCCESS;
	}


	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public String getLogfile() {
		return logfile;
	}

	public void setLogfile(String logfile) {
		this.logfile = logfile;
	}

	public String[] getActors() {
		return actors;
	}

	public void setActors(String[] actors) {
		this.actors = actors;
	}

	@SuppressWarnings("unchecked")
	private class compareTypes implements Comparator {

		public int compare(Object first, Object second) {
			try {
				IheActorDescription f = (IheActorDescription) first;
				IheActorDescription s = (IheActorDescription) second;
				return f.getType().compareToIgnoreCase(s.getType());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return 0;
		}
	}
}
