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

package org.openhealthtools.openxds;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.openhealthtools.openexchange.config.BootStrapProperties;
import org.openhealthtools.openexchange.config.ConfigurationException;
import org.openhealthtools.openexchange.config.PropertyFacade;

/**
 * This class manages the stand alone XDS server startup.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 *
 */
public class XdsServer {

	private static final Log log = LogFactory.getLog(XdsServer.class);

    private static Server server = null;

	/**
	 * The main method to start up XDS Registry or Repository server.
	 * 
	 * @param args For server startup, it is expected to have 2 arguments.
	 *        The first is "startup"; the second one is the full file 
	 *        path to IheActors.xml.  
	 *        <p>
	 */
	public static void main(String[] args) {		
        XdsServer xdsServer = new XdsServer();
        try {
        	xdsServer.startContainer();
        } catch (Exception e) {
            log.error("Failed to start the OpenXDS container", e);
            System.exit(1);
        }

        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
	}

    public void startContainer() throws Exception {
    	if (server != null)
    		return ;
    	
        //Load all the properties of this application.       
		try {
			String[] propertyFiles = BootStrapProperties.getPropertyFiles(new String[]{"jetty.properties"});
			PropertyFacade.loadProperties(propertyFiles);
		}catch(ConfigurationException e) {
            log.error("Failed to load OpenXDS properties", e);
		}
        // Create and Start Jetty Container
        server = new Server();

        List<Connector> connectors = new ArrayList<Connector>();
        Connector connector = createNonSecureConnector();
        connectors.add(connector);

        server.setConnectors(connectors.toArray(new Connector[connectors.size()]));

        String context = PropertyFacade.getString(XdsConstants.WEB_APP_CONTEXT);
        String root = PropertyFacade.getString(XdsConstants.WEB_APP_ROOT);

        WebAppContext webappContext = new WebAppContext(root, "/" + context);
        //webappContext.setResourceBase(root);

        server.setHandler(webappContext);

        server.start();
    }
	
    private Connector createNonSecureConnector() {
        Connector connector = new SocketConnector();
        String hostname = PropertyFacade.getString(XdsConstants.HOST, "localhost");

        int port = PropertyFacade.getInteger(XdsConstants.PORT);

        connector.setHost(hostname);
        connector.setPort(port);

        return connector;
    }

    private static class ShutdownHook extends Thread {
		
		public void run() {
			if (server != null && server.isStarted()) {
				try {
					server.stop();
				}catch(Exception e) {
					log.error("Failed to stop Jetty container.", e);
				}
			}
		}
	}
}
