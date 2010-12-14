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
package org.openhealthtools.openxds.webapp.servlet;

import java.io.File;
import java.net.URL;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.actorconfig.IheConfigurationException;
import org.openhealthtools.openexchange.config.BootStrapProperties;
import org.openhealthtools.openexchange.config.ConfigurationException;
import org.openhealthtools.openexchange.config.PropertyFacade;
import org.openhealthtools.openxds.XdsBroker;
import org.openhealthtools.openxds.XdsConstants;
import org.openhealthtools.openxds.configuration.XdsConfigurationLoader;

/**
 *The starting servlet.Main functionality is load properties
 *
 *@author <a href="mailto:anilkumar.reddy@misys.com">Anil</a>
 */
 public class ConfigServlet extends HttpServlet  {
	private static Log log = LogFactory.getLog(ConfigServlet.class);   
   
	public ConfigServlet() {
	} 
	
	/* 
	 * Destroys all Actors 
	 */
	public void destroy() {
		try {
			XdsConfigurationLoader loader = XdsConfigurationLoader.getInstance();
			loader.resetConfiguration(null);
		} catch (IheConfigurationException e) {
			log.error("Failed to destroy OpenXDS actor configuration.", e);
		}
	}   	
	
	public void init() {
		configProperties();
		configActors();
	}   
	
	private void configProperties() {
		try {
			String[] propertyFiles = BootStrapProperties.getPropertyFiles(new String[]{"openxds.properties"});
			PropertyFacade.loadProperties(propertyFiles);
		}catch(ConfigurationException e) {
			log.error("Failed to load OpenXDS properties.", e);
		}
	}
	
	private void configActors() {
  		String actorDir = PropertyFacade.getString(XdsConstants.IHE_ACTORS_DIR);
   	    String actorFile = null;
   	    File dir = new File(actorDir);
        URL repoPath = this.getClass().getClassLoader().getResource(actorDir);
        if (dir.exists()) {
        	actorFile = dir.getAbsolutePath();
        }else if (repoPath != null) {
       	    actorFile = repoPath.getPath();
        }else {
        	log.info(XdsConstants.IHE_ACTORS_DIR + " does not exist: " + actorDir);
        }
        
       try {
    	   	if (actorFile != null){
		    	 //remove the current . folder from the path
		       	actorFile = actorFile.replace(File.separator+"."+File.separator, File.separator);
		       	actorFile = actorFile + File.separator + "IheActors.xml";
    	   	}else{
    		   actorFile = System.getProperty(XdsConstants.IHE_ACTORS_FILE);
    	   	}
    	    
			if(actorFile != null){
				
			    //Start up the actors
				XdsConfigurationLoader loader = XdsConfigurationLoader.getInstance();

	        	log.info("Loading actor configuration from " + actorFile);
	        	
				loader.loadConfiguration(actorFile, true);
			}
        }catch (IheConfigurationException e) {
           log.fatal("Failed to load OpenXDS actor configuration", e);
		}catch (Exception e) {
           log.fatal("Failed to load OpenXDS actor configuration", e);
		}
		
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		//do nothing
	}
}