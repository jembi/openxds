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
package org.openhealthtools.openxds;

import org.openhealthexchange.openpixpdq.ihe.configuration.IheConfigurationException;
import org.openhealthtools.openxds.configuration.XdsConfigurationLoader;

/**
 * This class manages the stand alone XDS server startup.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 *
 */
public class XdsServer {

	/**
	 * The main method to start up XDS Registry or Repository server.
	 * 
	 * @param args For server startup, it is expected to have 2 arguments.
	 *        The first is "startup"; the second one is the full file 
	 *        path to IheActors.xml.  
	 *        <p>
	 */
	public static void main(String[] args) {
		if (args.length != 2 ||		    
		    (args.length == 2 && !args[0].equalsIgnoreCase("startup")) ) {
			printUsage();
			return ;
		}

		if (args.length == 2 && args[0].equalsIgnoreCase("startup") ) {
		    //Start up the servers
			XdsConfigurationLoader loader = XdsConfigurationLoader.getInstance();
			String actorFile = args[1];
	        try {
	            loader.loadConfiguration(actorFile, true);
	        } catch (IheConfigurationException e) {
	            e.printStackTrace();
	        }
		} 

	}
	
	/**
	 * Prints the usage of how to start up this XDS server.
	 */
	private static void printUsage() {
		System.out.println("*********************************************************");
		System.out.println("WRONG USAGE: The XDS server expects 2 arguments.");
		System.out.println("To start up the server: ");
	    System.out.println("   java XdsServer startup <full path of IheActors.xml>");
		System.out.println("*********************************************************");		
	}
}
