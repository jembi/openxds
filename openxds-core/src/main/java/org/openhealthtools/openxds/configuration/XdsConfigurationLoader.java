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

package org.openhealthtools.openxds.configuration;

import java.io.File;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.openhealthtools.openexchange.actorconfig.ActorConfigurationLoader;
import org.openhealthtools.openexchange.actorconfig.ActorDescriptionLoader;
import org.openhealthtools.openexchange.actorconfig.AuditBroker;
import org.openhealthtools.openexchange.actorconfig.IActorDescription;
import org.openhealthtools.openexchange.actorconfig.IBrokerController;
import org.openhealthtools.openexchange.actorconfig.IheConfigurationException;
import org.openhealthtools.openexchange.actorconfig.Transactions;
import org.openhealthtools.openexchange.actorconfig.TransactionsSet;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.audit.IheAuditTrail;
import org.openhealthtools.openexchange.log.IMesaLogger;
import org.openhealthtools.openxds.XdsBroker;
import org.openhealthtools.openxds.XdsFactory;
import org.openhealthtools.openxds.registry.XdsRegistryImpl;
import org.openhealthtools.openxds.registry.api.XdsRegistryPatientService;
import org.openhealthtools.openxds.repository.XdsRepositoryImpl;
import org.openhealthtools.openxds.xca.XcaIGImpl;
import org.openhealthtools.openxds.xca.XcaRGImpl;

/**
 * This class loads an Actor configuration file and initializes all of the
 * appropriate OpenXDS actors within the XdsBroker and AuditBroker.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class XdsConfigurationLoader extends ActorConfigurationLoader {

	/* Logger for debugging messages */
	private static final Log log = LogFactory.getLog(XdsConfigurationLoader.class);
	
	/* Singleton instance */
	private static XdsConfigurationLoader instance = null;
	
	//Actor Type Constants
	public static final String XDSREGISTRY     = "XdsRegistry";
	public static final String XDSREPOSITORY   = "XdsRepository";
	public static final String XCARG           = "XcaRG";
	public static final String XCAIG           = "XcaIG";
	//Connection Type Constants
	public static final String SERVER     = "Server";
	public static final String PIXSERVER  = "PixServer";
	public static final String REGISTRY   = "Registry";
	public static final String REPOSITORY = "Repository";
	//TransactionsSet Type - RespondingGateway
	public static final String RESPONDINGGATEWAY = "RespondingGateway";
	
	/**
	 * Gets the singleton instance for this class.
	 * 
	 * @return the singleton ConfigurationLoader
	 */
	public static synchronized XdsConfigurationLoader getInstance() {
		if (instance == null) instance = new XdsConfigurationLoader();
		return instance;
	}
	
	@Override
	protected void destroyAllActors() {
		// Create a controller that will reset all IHE actors
		IheBrokerController controller = new IheBrokerController();
		// Apply it to the AuditBroker
		AuditBroker abroker = AuditBroker.getInstance();
		abroker.unregisterAuditSources(controller);
		// Apply it to the XdsBroker
		XdsBroker xdsBroker = XdsBroker.getInstance();
		xdsBroker.unregisterXdsRegistries(controller);
		xdsBroker.unregisterXdsRepositories(controller);
		xdsBroker.unregisterXcaIG(controller);
		xdsBroker.unregisterXcaRG(controller);
        // Okay, nothing is installed
		actorsInstalled.clear();
	}
	
	@Override
	protected boolean validateActor(IActorDescription actor, File configFile) throws IheConfigurationException {
		String actorName = actor.getName();
		// Make sure we got out a valid definition
		if (actor.getType().equalsIgnoreCase(XDSREGISTRY)) {
			if (actor.getConnectionDescriptionsByType(SERVER).size() != 1) 
				throw new IheConfigurationException("Actor '" + actorName + "' must specify one valid '" + SERVER + "' Connection in configuration file \"" + configFile.getAbsolutePath() + "\"");
			
			if (actor.getConnectionDescriptionsByType(PIXSERVER).size() != 1) 
				throw new IheConfigurationException("Actor '" + actorName + "' must specify one valid '" + PIXSERVER + "' Connection in configuration file \"" + configFile.getAbsolutePath() + "\"");
		} 
		else if (actor.getType().equalsIgnoreCase(XDSREPOSITORY)) {
			if (actor.getConnectionDescriptionsByType(SERVER).size() != 1) 
				throw new IheConfigurationException("Actor '" + actorName + "' must specify one valid '" + SERVER + "' Connection in configuration file \"" + configFile.getAbsolutePath() + "\"");
			
			if (actor.getConnectionDescriptionsByType(REGISTRY).size() != 1) 
				throw new IheConfigurationException("Actor '" + actorName + "' must specify one valid '" + REGISTRY + "' Connection in configuration file \"" + configFile.getAbsolutePath() + "\"");
		}
	    else if (actor.getType().equalsIgnoreCase(XCARG)) {
			if (actor.getConnectionDescriptionsByType(SERVER).size() != 1) 
				throw new IheConfigurationException("Actor '" + actorName + "' must specify one valid '" + SERVER + "' Connection in configuration file \"" + configFile.getAbsolutePath() + "\"");
			
			if (actor.getConnectionDescriptionsByType(REGISTRY).size() != 1) 
				throw new IheConfigurationException("Actor '" + actorName + "' must specify one valid '" + REGISTRY + "' Connection in configuration file \"" + configFile.getAbsolutePath() + "\"");
			
			if (actor.getConnectionDescriptionsByType(REPOSITORY).size() != 1) 
				throw new IheConfigurationException("Actor '" + actorName + "' must specify one valid '" + REPOSITORY + "' Connection in configuration file \"" + configFile.getAbsolutePath() + "\"");
	    }
		else if (actor.getType().equalsIgnoreCase(XCAIG)) {
			TransactionsSet respondingGatewaySet = actor.getTransactionSet(RESPONDINGGATEWAY);
			if (actor.getConnectionDescriptionsByType(SERVER).size() != 1) 
				throw new IheConfigurationException("Actor '" + actorName + "' must specify one valid '" + SERVER + "' Connection in configuration file \"" + configFile.getAbsolutePath() + "\"");
			
			// At least a registry or responding gateway has to be defined.
			if (actor.getConnectionDescriptionsByType(REGISTRY).isEmpty() && respondingGatewaySet == null)
				throw new IheConfigurationException("Actor '" + actorName + "' must specify one valid '" + REGISTRY + "' Connection or Responding Gateway in configuration file \"" + configFile.getAbsolutePath() + "\"");				
			
			// At least a repository or responding gateway has to be defined.
			if (actor.getConnectionDescriptionsByType(REPOSITORY).isEmpty() && respondingGatewaySet == null)
				throw new IheConfigurationException("Actor '" + actorName + "' must specify one valid '" + REPOSITORY + "' Connection or Responding Gateway in configuration file \"" + configFile.getAbsolutePath() + "\"");
			
			if (respondingGatewaySet != null) {
				for (Transactions transactions : respondingGatewaySet.getAllTransactions() ) {
					if (transactions.getQuery() == null)
						throw new IheConfigurationException("RespondingGateway Transactions must specify a valid '" + QUERY + "' attribute");
					if (transactions.getRetrieve() == null)
						throw new IheConfigurationException("RespondingGateway Transactions must specify a valid '" + RETRIEVE + "' attribute");
				}	
			}
		}
		else if (actor.getType().equalsIgnoreCase(SECURENODE)) {
			if (actor.getAuditLogConnection().isEmpty())
				throw new IheConfigurationException("Actor '" + actorName + "' must specify a valid '" + AUDITTRAIL + "' element");
		}
		else {
			throw new IheConfigurationException("Actor '" + actorName + "' must specify a valid actor type");			
		}
		
		return true;
	}
	

	@Override
	protected boolean createIheActor(IActorDescription actor, Collection<IConnectionDescription> auditLogs, IMesaLogger logger, File configFile) 
	throws IheConfigurationException {
		boolean okay = false;
		IheAuditTrail auditTrail = null;
		
		// Build a new audit trail if there are any connections to audit repositories.
		if (!auditLogs.isEmpty()) { 
			auditTrail = new IheAuditTrail(actor.getName(), auditLogs);
		}
		
		// Actually create the actor
		if (actor.getType().equalsIgnoreCase(SECURENODE)) {
			if (auditTrail != null) {
				AuditBroker broker = AuditBroker.getInstance();
				broker.registerAuditSource(auditTrail);
				okay = true;
			}
		}
		else if (actor.getType().equalsIgnoreCase(XDSREGISTRY)) {
			IConnectionDescription xdsRegistryServerConnection = actor.getConnectionDescriptionByType(SERVER);
			IConnectionDescription pixRegistryServerConnection = actor.getConnectionDescriptionByType(PIXSERVER);
			XdsRegistryImpl xdsRegistry = new XdsRegistryImpl(pixRegistryServerConnection, xdsRegistryServerConnection, auditTrail);

			XdsRegistryPatientService patientManager = XdsFactory.getXdsRegistryPatientService();
			xdsRegistry.registerPatientManager(patientManager);

			if (xdsRegistry != null) {
	            XdsBroker broker = XdsBroker.getInstance();
	            broker.registerXdsRegistry(xdsRegistry);
	            okay = true;
	        }
		}
		else if (actor.getType().equalsIgnoreCase(XDSREPOSITORY)) {
			IConnectionDescription repositoryServerConnection = actor.getConnectionDescriptionByType(SERVER);
			IConnectionDescription registryClientConnection = actor.getConnectionDescriptionByType(REGISTRY);

			XdsRepositoryImpl xdsRepository = new XdsRepositoryImpl(repositoryServerConnection, registryClientConnection, auditTrail);
			if (repositoryServerConnection != null) {
	            XdsBroker broker = XdsBroker.getInstance();
	            broker.registerXdsRepository(xdsRepository);
	            okay = true;
	        }
		}
		else if (actor.getType().equalsIgnoreCase(XCARG)) {
			IConnectionDescription rgServerConnection = actor.getConnectionDescriptionByType(SERVER);
			IConnectionDescription registryClientConnection = actor.getConnectionDescriptionByType(REGISTRY);
			IConnectionDescription reposiotryClientConnection = actor.getConnectionDescriptionByType(REPOSITORY);
			
			XcaRGImpl xcaRG = new XcaRGImpl(rgServerConnection, registryClientConnection, reposiotryClientConnection, auditTrail);
	        if (xcaRG != null) {
	            XdsBroker broker = XdsBroker.getInstance();
	            broker.registerXcaRG(xcaRG);
	            okay = true;
	        }
		}
		else if (actor.getType().equalsIgnoreCase(XCAIG)) {
			IConnectionDescription igServerConnection = actor.getConnectionDescriptionByType(SERVER);
			IConnectionDescription registryClientConnection = actor.getConnectionDescriptionByType(REGISTRY);
			IConnectionDescription reposiotryClientConnection = actor.getConnectionDescriptionByType(REPOSITORY);
			TransactionsSet respondingGateways = actor.getTransactionSet(RESPONDINGGATEWAY);
			
			XcaIGImpl xcaIG = new XcaIGImpl(igServerConnection, registryClientConnection, reposiotryClientConnection, respondingGateways, auditTrail);
	        if (xcaIG != null) {
	            XdsBroker broker = XdsBroker.getInstance();
	            broker.registerXcaIG(xcaIG);
	            okay = true;
	        }
		}
		
        // Record this installation, if it succeeded
		if (okay) actorsInstalled.add(actor.getName()); 
		return okay;
	}

	@Override
	protected String getHumanReadableActorType(String type, File configFile) throws IheConfigurationException {
		if (type.equalsIgnoreCase("SecureNode")) {
			return "Audit Record Repository";
		} else if (type.equals("XdsRegistry")) {
            return "OpenXDS XDS Registry";
        } else if (type.equals("XdsRepository")) {
            return "OpenXDS XDS Repository";
	    } else if (type.equals("XcaRG")) {
	        return "OpenXDS XCA Responding Gateway";
	    }else if (type.equals("XcaIG")) {
	        return "OpenXDS XCA Initiating Gateway";
	    }
		else {
			ActorDescriptionLoader.throwIheConfigurationException("Invalid actor type '" + type + "'", configFile);
			return null;
		}
	}
	
//	/**
//	 * Gets a human understandable connection description.
//	 * 
//	 * @param description the description
//	 * @param connection the connection
//	 * @return a human understandable description of this connection
//	 */
//	protected String getHumanConnectionDescription(String description, IConnectionDescription connection) {
//		StringBuffer sb = new StringBuffer();
//		String hostName = connection.getHostname();
//		int port = connection.getPort();
//		if (description != null)  {
//			// "description host:port (TLS)"
//			sb.append(description);
//			if (connection.isSecure()) sb.append(" (TLS)");
//			if (hostName != null) {
//				sb.append(' ');
//				sb.append(hostName);
//				if (port >= 0) {
//					sb.append(':');
//					sb.append(port);
//				}
//			}
//		} else if (hostName != null) {
//			// "host:port (TLS)"
//			sb.append(hostName);
//			if (port >= 0) {
//				sb.append(':');
//				sb.append(port);
//			}
//			if (connection.isSecure()) sb.append(" (TLS)");
//		}
//		// Done
//		return sb.toString();
//	}

	/**
	 * An implementation of a broker controller that will unregister and IHE actor.
	 * 
	 */
	public class IheBrokerController implements IBrokerController {

		/**
		 * Whether to unregister of a give actor or not
		 */
		public boolean shouldUnregister(Object actor) {
			// Unregister any IHE Actor
			if (actor instanceof IheAuditTrail) return true;
            if (actor instanceof XdsRegistryImpl) return true;
            if (actor instanceof XdsRepositoryImpl) return true;
            if (actor instanceof XcaRGImpl) return true;
            if (actor instanceof XcaIGImpl) return true;

            return false;
		}
	}
	
	/** testing only **/
	public static void main(String args[]) {
		XdsConfigurationLoader.getInstance().setLoggingFile("testfile.txt", null, null);
		Logger log = Logger.getLogger("mylogger");
		log.fatal("**my fatal error**");
		log.error("**my error error**");
		log.warn("**my warn error**");
		log.info("**my info error**");
		log.debug("**my debug error**");
	}
}
