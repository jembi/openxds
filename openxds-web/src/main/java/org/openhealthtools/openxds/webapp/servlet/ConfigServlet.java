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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openhealthexchange.openpixpdq.ihe.PatientBroker;
import org.openhealthtools.openexchange.config.BootStrapProperties;
import org.openhealthtools.openexchange.config.ConfigurationException;
import org.openhealthtools.openexchange.config.PropertyFacade;

/**
 *The starting servlet.Main functionality is load properties
 *
 *@author <a href="mailto:anilkumar.reddy@misys.com">Anil</a>
 */
 public class ConfigServlet extends HttpServlet  {
   static final long serialVersionUID = 1L;
   
	public ConfigServlet() {
	} 
	
	/* 
	 * Destroys all Actors 
	 */
	public void destroy() {

	}   	 	  	  	  
	public void init() throws ServletException {
		try {
			String[] propertyFiles = BootStrapProperties.getPropertyFiles(new String[]{"openxds.properties"});
			PropertyFacade.loadProperties(propertyFiles);
		}catch(ConfigurationException e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}   
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		//do nothing
	}
}