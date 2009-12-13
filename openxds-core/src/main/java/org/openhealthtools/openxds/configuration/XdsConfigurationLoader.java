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
package org.openhealthtools.openxds.configuration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.openhealthexchange.openpixpdq.ihe.configuration.Configuration;
import org.openhealthexchange.openpixpdq.ihe.configuration.IheActorDescription;
import org.openhealthexchange.openpixpdq.ihe.configuration.IheConfigurationException;
import org.openhealthexchange.openpixpdq.ihe.log.IMesaLogger;
import org.openhealthexchange.openpixpdq.ihe.log.IMessageStoreLogger;
import org.openhealthexchange.openpixpdq.ihe.log.Log4jLogger;
import org.openhealthtools.common.audit.IheAuditTrail;
import org.openhealthtools.openxds.XdsBroker;
import org.openhealthtools.openxds.XdsFactory;
import org.openhealthtools.openxds.registry.XdsRegistryImpl;
import org.openhealthtools.openxds.registry.api.XdsRegistryPatientService;
import org.openhealthtools.openxds.repository.XcaRGImpl;
import org.openhealthtools.openxds.repository.XdsRepositoryImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.misyshealthcare.connect.base.AuditBroker;
import com.misyshealthcare.connect.base.IBrokerController;
import com.misyshealthcare.connect.base.codemapping.ICodeMappingManager;
import com.misyshealthcare.connect.net.ConnectionFactory;
import com.misyshealthcare.connect.net.IConnectionDescription;
import com.misyshealthcare.connect.util.LibraryConfig;
import com.misyshealthcare.connect.util.OID;
import com.misyshealthcare.connect.util.LibraryConfig.ILogContext;
import com.misyshealthcare.connect.util.LibraryConfig.IPatientIdConverter;

/**
 * This class loads an IHE Actor configuration file and initializes all of the
 * appropriate actors within the DocumentBroker, XdsBroker and AuditBroker.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class XdsConfigurationLoader {

	/* Logger for debugging messages */
	private static final Log log = LogFactory.getLog(XdsConfigurationLoader.class);
	
	/* Logger for IHE Actor message traffic */
	private static final Log4jLogger iheLog = new Log4jLogger();
	
	/* Singleton instance */
	private static XdsConfigurationLoader instance = null;
	
	/* The configuration file to load */
	//private File configFile = null;
	
	/* Current root logger appender */
	Appender currentAppender = null;
	
	/* The actor definitions loaded by the config file */
	private Vector<ActorDescription> actorDefinitions = null;
	
	/* The IDs of the actors last installed */
	/* This is a very weak set, it may not match the actual set of installed actors */
	private Vector<String> actorsInstalled = new Vector<String>();
		
	/**
	 * Gets the singleton instance for this class.
	 * 
	 * @return the singleton ConfigurationLoader
	 */
	public static synchronized XdsConfigurationLoader getInstance() {
		if (instance == null) instance = new XdsConfigurationLoader();
		return instance;
	}
	
	/**
	 * Loads the supplied configuration file and
	 * creates all of the IHE actors that it defines.
	 * 
	 * @param filename the name of the configuration file
	 * @param oidRoot the OID root
     * @param oidSource the OID.OidSource interface which provides a unique oid id.
	 * @param styleSheetLocation the style sheet location for CDA transformation
	 * @param patientQueryDesignProps the query design properties
	 * @param codeMappingManager the CodeMappingManager to be used for data transformation
	 * @param logContext the LogContext to be used for audit logging
	 * @return True if the configuration file was processed successfully
	 * @throws IheConfigurationException When there is something wrong with the specified configuration
	 */
	public boolean loadConfiguration(String filename, String oidRoot, OID.OidSource oidSource, String styleSheetLocation, Properties patientQueryDesignProps, ICodeMappingManager codeMappingManager, ILogContext logContext) throws IheConfigurationException {
		if (filename == null) return false;
		return loadConfiguration(new File(filename), true, oidRoot, oidSource, styleSheetLocation, patientQueryDesignProps, codeMappingManager, logContext);
	}
	
	/**
	 * Loads the supplied configuration file and
	 * create all of the IHE actors that it defines.
	 * 
	 * @param file The configuration file
	 * @param oidRoot the OID root
     * @param oidSource the OID.OidSource interface which provides a unique oid id.
	 * @param styleSheetLocation the style sheet location for CDA transformation
	 * @param patientQueryDesignProps the query design properties
	 * @param codeMappingManager the CodeMappingManager to be used for data transformation
	 * @param logContext the LogContext to be used for audit logging
	 * @return True if the configuration file was processed successfully
	 * @throws IheConfigurationException When there is something wrong with the specified configuration
	 */
	public boolean loadConfiguration(File file, String oidRoot, OID.OidSource oidSource, String styleSheetLocation, Properties patientQueryDesignProps, ICodeMappingManager codeMappingManager, ILogContext logContext) throws IheConfigurationException {
		if (file == null) return false;
		return loadConfiguration(file, true, oidRoot, oidSource, styleSheetLocation, patientQueryDesignProps, codeMappingManager, logContext);
	}
	
	/**
	 * Loads the supplied configuration file.  If the argument is
	 * 'true', then create an initialize all of the IHE actors in the file.  If the
	 * argument is 'false', save the actors away for GUI access.
	 * 
	 * @param filename the name of the configuration file
	 * @param autoInstallActors If 'true' create the actors in this configuration, else store them up
	 * @param oidRoot the OID root
     * @param oidSource the OID.OidSource interface which provides a unique oid id.
	 * @param styleSheetLocation the style sheet location for CDA transformation
	 * @param patientQueryDesignProps the query design properties
	 * @param codeMappingManager the CodeMappingManager to be used for data transformation
	 * @param logContext the LogContext to be used for audit logging
	 * @return 'true' if the configuration was loaded successfully
	 * @throws IheConfigurationException When there is a problem with the configuration file
	 */
	public boolean loadConfiguration(String filename, boolean autoInstallActors, String oidRoot, OID.OidSource oidSource, String styleSheetLocation,
         Properties patientQueryDesignProps, ICodeMappingManager codeMappingManager, ILogContext logContext) throws IheConfigurationException {
		return loadConfiguration(new File(filename), autoInstallActors, oidRoot, oidSource, styleSheetLocation, patientQueryDesignProps, codeMappingManager, logContext);
	}

	/**
	 * Loads the supplied configuration file.  If the argument is
	 * 'true', then create an initialize all of the IHE actors in the file.  If the
	 * argument is 'false', save the actors away for GUI access.
	 * 
	 * @param filename the name of the configuration file
	 * @param autoInstallActors If 'true' create the actors in this configuration, else store them up
	 * @return 'true' if the configuration was loaded successfully
	 * @throws IheConfigurationException When there is a problem with the configuration file
	 */
	public boolean loadConfiguration(String filename, boolean autoInstallActors) throws IheConfigurationException {
		return loadConfiguration(filename, autoInstallActors, null, null, null, null, null, null);
	}
    /**
	 * Loads the supplied configuration file.  If the argument is
	 * 'true', then create an initialize all of the IHE actors in the file.  If the
	 * argument is 'false', save the actors away for GUI access.
	 *
	 * @param file the configuration file
	 * @param autoInstallActors If 'true' create the actors in this configuration, else store them up
	 * @param oidRoot the OID root
     * @param oidSource the OID.OidSource interface which provides a unique oid id.
	 * @param styleSheetLocation the style sheet location for CDA transformation
	 * @param patientQueryDesignProps the query design properties
	 * @param codeMappingManager the CodeMappingManager to be used for data transformation
	 * @param logContext the LogContext to be used for audit logging
	 * @return 'true' if the configuration was loaded successfully
	 * @throws IheConfigurationException When there is a problem with the configuration file
	 */
	public synchronized boolean loadConfiguration(File file, boolean autoInstallActors, String oidRoot, OID.OidSource oidSource, String styleSheetLocation, Properties patientQueryDesignProps, ICodeMappingManager codeMappingManager, ILogContext logContext) throws IheConfigurationException {
		return loadConfiguration(file, autoInstallActors, true, oidRoot, oidSource, styleSheetLocation, patientQueryDesignProps, codeMappingManager, logContext);
	}
    /**
	 * Loads the supplied configuration file.  If the argument is
	 * 'true', then create an initialize all of the IHE actors in the file.  If the
	 * argument is 'false', save the actors away for GUI access.
	 *
	 * @param file the configuration file
	 * @param autoInstallActors If 'true' create the actors in this configuration, else store them up
	 * @param oidRoot the OID root
     * @param oidSource the OID.OidSource interface which provides a unique oid id.
	 * @param styleSheetLocation the style sheet location for CDA transformation
	 * @param patientQueryDesignProps the query design properties
	 * @param codeMappingManager the CodeMappingManager to be used for data transformation
	 * @param logContext the LogContext to be used for audit logging
 	 * @param pidConvert the patientIdConverter to be used to convert the sourcePatientId to the PixLocalPatientId
 	 * @return 'true' if the configuration was loaded successfully
	 * @throws IheConfigurationException When there is a problem with the configuration file
	 */	
	public synchronized boolean loadConfiguration(File file, boolean autoInstallActors, String oidRoot, OID.OidSource oidSource, String styleSheetLocation, Properties patientQueryDesignProps, ICodeMappingManager codeMappingManager, ILogContext logContext, IPatientIdConverter pidConverter) throws IheConfigurationException {
		return loadConfiguration(file, autoInstallActors, true, oidRoot, oidSource, styleSheetLocation, patientQueryDesignProps, codeMappingManager, logContext);
	}     
    /**
	 * Loads the supplied configuration file.  If the argument is
	 * 'true', then create an initialize all of the IHE actors in the file.  If the
	 * argument is 'false', save the actors away for GUI access.
	 * 
	 * @param file the configuration file
	 * @param autoInstallActors If 'true' create the actors in this configuration, else store them up
     * @param reset whether to reset actorDefinitions or resetAllBrokers
	 * @param oidRoot the OID root
     * @param oidSource the OID.OidSource interface which provides a unique oid id
	 * @param styleSheetLocation the stylesheet location for CDA transformation
	 * @param patientQueryDesignProps the query design properties
	 * @param codeMappingManager the CodeMappingManager to be used for data transformation
	 * @param logContext the LogContext to be used for audit logging
	 * @param pidConvert the patientIdConverter to be used to convert the sourcePatientId to the PixLocalPatientId
	 * @return 'true' if the configuration was loaded successfully
	 * @throws IheConfigurationException When there is a problem with the configuration file
	 */
	private boolean loadConfiguration(File file, boolean autoInstallActors, boolean reset, String oidRoot, 
			    OID.OidSource oidSource, String styleSheetLocation, Properties patientQueryDesignProps, 
			    ICodeMappingManager codeMappingManager, ILogContext logContext) throws IheConfigurationException {
		LibraryConfig libConfig = LibraryConfig.getInstance();
		libConfig.setOidRoot(oidRoot);
        libConfig.setOidSource(oidSource);
        libConfig.setStyleSheetLocation(styleSheetLocation);
		libConfig.setPatientQueryDesignProps(patientQueryDesignProps);
		libConfig.setCodeMappingManager(codeMappingManager);
		libConfig.setLogContext(logContext);
		if(libConfig.getPatientIdConverter()==null) {
			libConfig.setPatientIdConverter(LibraryConfig.DefaultPatientIdConverter.getInstance());
		}
		boolean okay = true;
		// Reset the list of loaded actors
		if (reset) actorDefinitions = new Vector<ActorDescription>();
		// If we are auto-installing, reset all the brokers
		if (autoInstallActors && reset) resetAllBrokers();
		// Make sure we have a configuration file
		File configFile = file;
		if (configFile == null) {
			throw new IheConfigurationException("No file given to configuration loader");
		} else if (!configFile.exists()) {
			throw new IheConfigurationException("The configuration file \"" + configFile.getAbsolutePath() + "\" does not exist");
		}
		// Create a builder factory and a builder, and get the configuration document.
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		Document configuration = null;
		try {
			configuration = factory.newDocumentBuilder().parse(configFile);
		} catch (SAXException e) {
			// An XML exception
			throw new IheConfigurationException("Invalid XML in configuration file '" + configFile.getAbsolutePath() + "'", e);
		} catch (IOException e) {
			// A problem reading the file
			throw new IheConfigurationException("Cannot read configuration file '" + configFile.getAbsolutePath() + "'", e);
		} catch (ParserConfigurationException e) {
			// No XML implementation
			throw new IheConfigurationException("No XML implementation to process configuration file '" + configFile.getAbsolutePath() + "'", e);
		}
		// Get the list of XML elements in the configuration file
		NodeList configurationElements = configuration.getDocumentElement().getChildNodes();
		// Load all the connection definitions first
		for (int elementIndex = 0; elementIndex < configurationElements.getLength(); elementIndex++) {
			Node element = configurationElements.item(elementIndex);
			if (element instanceof Element) {
				// See what type of element it is
				String name = element.getNodeName();
				if (name.equalsIgnoreCase("CONNECTIONFILE")) {
					// An included connection file, load it
					if (!processConnectionFile((Element) element, configFile)) okay = false;
				} else if (name.equalsIgnoreCase("SECURECONNECTION") || name.equalsIgnoreCase("STANDARDCONNECTION")) {
					// An included connection, load it
					if (!ConnectionFactory.loadConnectionDescriptionsFromXmlNode(element, configFile)) {
						throwIheConfigurationException("Error loading configuration file '" + configFile.getAbsolutePath() + "'", configFile);
						okay = false;
					}
				}
			}
		}
		// If all the connection files loaded okay, define the various actors
		if (okay) {
			for (int elementIndex = 0; elementIndex < configurationElements.getLength(); elementIndex++) {
				Node element = configurationElements.item(elementIndex);
				if (element instanceof Element) {
					// See what type of element it is
					String name = element.getNodeName();
                    if (name.equalsIgnoreCase("ACTORFILE")) {
                        if (!processActorFile((Element) element, autoInstallActors, configFile, false)) okay = false;
                    } else if (name.equalsIgnoreCase("ACTOR")) {
						// An IHE actor definition
						if (!processActorDefinition((Element) element, autoInstallActors, configFile)) okay = false;
					}
				}
			}
		}
		// Done
		return true;
	}
	
	/** Sets the default file to write to for the root.
	 * Also sets the level.  Note: if level is null 
	 * the rool level will be set to INFO.  Note that
	 * if there already is a file appender set this way
	 * it will be removed before the new one is added.
	 * All other appenders are left untouched.
	 * 
	 * @param fullPathToLogFile the Path to append the log to.
	 * @param level the Level to log, null for INFO.
	 * @param pattern Some other pattern to use for logging.  null ok.
	 */
	public synchronized void setLoggingFile(String fullPathToLogFile, Level level, String pattern) {
		Logger root = Logger.getRootLogger();
		try {
			if (pattern == null)
				pattern =  "Milliseconds since program start: %r %n" +
					"Date of message: %d %n" +
					//"Classname of caller: %C %n" +
					"Location: %l %n" +
					"Message: %m %n %n";
			if (currentAppender != null) root.removeAppender(currentAppender);
			if (fullPathToLogFile != null) {
				currentAppender = new FileAppender(new PatternLayout(pattern), fullPathToLogFile);
				if (currentAppender != null) root.addAppender(currentAppender);
				if (level == null) level = Level.INFO;
				root.setLevel(level);
			}
		} catch(Exception e) { log.error("Unable to set output file for logger: " + fullPathToLogFile, e); }
	}
	
	/**
	 * Gets the actor descriptions loaded in the configuration.
	 * 
	 * @return the actor descriptions
	 */
	public synchronized Collection<IheActorDescription> getActorDescriptions() {
		Vector<IheActorDescription> actors = new Vector<IheActorDescription>();
		if (actorDefinitions != null) {
			for (ActorDescription actor: actorDefinitions) {
				actor.isInstalled = actorsInstalled.contains(actor.id);
				actors.add(actor);
			}
		}
		return actors;
	}
	
	/**
	 * Resets the current IHE brokers to use the actors described in the list passed
	 * in.  These may be actor descriptions objects or the IDs of actor description
	 * objects.  This call will not do any logging.
	 * 
	 * @param actorDescriptions the actor descriptions or IDs to use to define the actors
	 * @return 'true' if the actors were created and initialized successfully
	 * @throws IheConfigurationException When there is a problem with the configuration file
	 */
	public synchronized boolean resetConfiguration(Collection<Object> actorDescriptions) throws IheConfigurationException {
		return resetConfiguration(actorDescriptions, null);
	}
	/**
	 * Resets the current IHE brokers to use the actors described in the list passed
	 * in.  These may be actor descriptions objects or the IDs of actor description
	 * objects.
	 * 
	 * @param actorDescriptions the actor descriptions or IDs to use to define the actors
	 * @param logFilename the log file to install for this set of actors
	 * @return 'true' if the actors were created and initialized successfully
	 * @throws IheConfigurationException When there is a problem with the configuration file
	 */
	public synchronized boolean resetConfiguration(Collection<Object> actorDescriptions, String logFilename) throws IheConfigurationException {  
		return resetConfiguration(actorDescriptions, logFilename, null);
	}

	/**
	 * Resets the current IHE brokers to use the actors described in the list passed
	 * in.  These may be actor descriptions objects or the IDs of actor description
	 * objects.
	 * 
	 * @param actorDescriptions the actor descriptions or IDs to use to define the actors
	 * @param logFilename the log file to install for this set of actors
     * @param mesaLog the mesa log used for mesa tests
	 * @return 'true' if the actors were created and initialized successfully
	 * @throws IheConfigurationException When there is a problem with the configuration file
	 */
	public synchronized boolean resetConfiguration(Collection<Object> actorDescriptions, String logFilename, IMesaLogger mesaLog) throws IheConfigurationException {
		// Reset all the brokers
		resetAllBrokers();
		// Setup the log
		log.info("Log file closed.");
		setLoggingFile(logFilename, null, null);
		log.info("Log file opened.");
		// Jump out if nothing to start up
		if (actorDescriptions == null) return true;
		// First, map all the supplied actor/actor names into descriptions
		ArrayList<ActorDescription> actors = new ArrayList<ActorDescription>();
		for (Object thing: actorDescriptions) {
			if (thing instanceof ActorDescription) {
				// Its an actor
				actors.add((ActorDescription) thing);
			} else if (thing instanceof String) {
				// Its an actor name
				ActorDescription actor = getDescriptionById((String) thing);
				if (actor != null) actors.add(actor);
			}
		}
		// Second, pull out the connections from any Secure Node actors and the source from the XdsDocumentConsumer
		ArrayList<IConnectionDescription> auditConnections = new ArrayList<IConnectionDescription>();
		IConnectionDescription source = null;
		for (ActorDescription actor: actors) {
			if (actor.actorType.equalsIgnoreCase("SecureNode")) {
				// This is an audit repository, save its connections for all actors to use
				Collection<IConnectionDescription> logConnections = actor.logConnections;
				if (logConnections != null) auditConnections.addAll(logConnections);
			} else if (actor.actorType.equalsIgnoreCase("XdsDocumentConsumer")) {
				// This actor gets documents, save it as a replace query source
				if (actor.sourceConnection != null) source = actor.sourceConnection;
			}
		}
		// Third, create all the actors
		boolean okay = true;
		IMesaLogger log = mesaLog==null ? iheLog : mesaLog;
		for (ActorDescription actor: actors) {
			Collection<IConnectionDescription> actorAudit = actor.logConnections;
			if ((actorAudit == null) || actorAudit.isEmpty()) actorAudit = auditConnections;
			IConnectionDescription sourceConnection = actor.sourceConnection;
			if (actor.actorType.equalsIgnoreCase("XdsDocumentSource")) {
				if (sourceConnection == null) sourceConnection = source;
			}
			
			if(actor.actorType.equalsIgnoreCase("SecureNode")) {
				// Build a new audit trail if there are any connections to audit repositories.
				ArrayList<IConnectionDescription> xdsAuditConnection = ((XdsAuditActorDescription)actor).getXdsAuditConnection();
				if (!createXdsAuditActor(actor.id, xdsAuditConnection, log, null))
					okay = false;
			}
			//XDS Registry
			if(actor.actorType.equalsIgnoreCase("XdsRegistry")) {
				IConnectionDescription xdsRegistryConnection = ((XdsRegistryActorDescription)actor).getXdsRegistryConnection();
				IConnectionDescription pixRegistryConnection = ((XdsRegistryActorDescription)actor).getPixRegistryConnection();
				if (!createXdsRegistryActor(actor.id, xdsRegistryConnection, pixRegistryConnection, 
						actorAudit, log, null))
					okay = false;
			}
			//XDS Repository
			if(actor.actorType.equalsIgnoreCase("XdsRepository")) {
				IConnectionDescription xdsRepositoryServerConnection = ((XdsRepositoryActorDescription)actor).getXdsRepositoryServerConnection();
				IConnectionDescription xdsRegistryClientConnection = ((XdsRepositoryActorDescription)actor).getXdsRegistryClientConnection();
				if (!createXdsRepositoryActor(actor.id, xdsRepositoryServerConnection, xdsRegistryClientConnection, 
						actorAudit, log, null))
					okay = false;
			}
			//XCA Responding Gateway
			if(actor.actorType.equalsIgnoreCase("XcaRG")) {
				IConnectionDescription xcaRGServerConnection = ((XcaRGActorDescription)actor).getXcaRGServerConnection();
				IConnectionDescription xdsRegistryClientConnection = ((XcaRGActorDescription)actor).getXdsRegistryClientConnection();
				IConnectionDescription xdsRepositoryClientConnection = ((XcaRGActorDescription)actor).getXdsRepositoryClientConnection();
				if (!createXcaRGActor(actor.id, xcaRGServerConnection, xdsRegistryClientConnection, xdsRepositoryClientConnection, actorAudit, log, null))
					okay = false;
			}
			
		}
		// Done
		return okay;
	}
	
	/**
	 * Clears all brokers and stops all active actors.
	 */
	public void resetAllBrokers() {
		// Create a controller that will reset all IHE actors
		IheBrokerController controller = new IheBrokerController();
		// Apply it to the AuditBroker
		AuditBroker abroker = AuditBroker.getInstance();
		abroker.unregisterAuditSources(controller);
		// Apply it to the XdsBroker
		XdsBroker xdsBroker = XdsBroker.getInstance();
		xdsBroker.unregisterXdsRegistries(controller);
		xdsBroker.unregisterXdsRepositories(controller);
        // Okay, nothing is installed
		actorsInstalled.clear();
	}
	
	/**
	 * Looks up an actor description given an ID.
	 * 
	 * @param id the actor description ID
	 * @return the actor description, if there is one
	 */
	public ActorDescription getDescriptionById(String id) {
		if (id == null) return null;
		for (ActorDescription actor: actorDefinitions) {
			if (id.equalsIgnoreCase(actor.getId())) return actor;
		}
		return null;
	}
	
	/**
	 * Processes a ConnectionFile element.  This element will specify a file that includes
	 * definitions of various connections that can be used by the IHE actors.
	 * 
	 * @param element the XML DOM element defining the connection file
	 * @return <code>true</code> if the file was loaded successfully
	 * @throws IheConfigurationException When there is a problem with the configuration
	 */
	private boolean processConnectionFile(Element element, File configFile) throws IheConfigurationException {
		boolean okay = false;
		// Get out the file name
		String filename = getAttributeValue(element, "file");
		if (filename == null) filename = getAttributeValue(element, "name");
		if (filename == null) filename = getNodeAsText(element);
		if (filename != null) {
			// Got the connection file name, load it
			File includeFile = new File(configFile.getParentFile(), filename);
			if (ConnectionFactory.loadConnectionDescriptionsFromFile(includeFile)) {
				okay = true;
			} else {
				throwIheConfigurationException("Error loading connection file \"" + filename + "\"", configFile);
			}
		} else {
			// No connection file name given
			logConfigurationWarning("Missing attribute 'name' in 'ConnectionFile' definition", configFile);
		}
		// Done
		return okay;
	}

    private boolean processActorFile(Element element, boolean autoInstall, File configFile, boolean resetActorDefinition) throws IheConfigurationException {
        boolean okay = false;
        // Get out the file name
        String filename = getAttributeValue(element, "file");
        if (filename == null) filename = getAttributeValue(element, "name");
        if (filename == null) filename = getNodeAsText(element);
        if (filename != null) {
            // Got the actor file name, load it
            File includeFile = new File(configFile.getParentFile(), filename);
            if (loadConfiguration(includeFile, autoInstall, false, LibraryConfig.getInstance().getOidRoot(),
                   LibraryConfig.getInstance().getOidSource(), LibraryConfig.getInstance().getStyleSheetLocation(),
                   LibraryConfig.getInstance().getPatientQueryDesignProps(), LibraryConfig.getInstance().getCodeMappingManager(),
                   LibraryConfig.getInstance().getLogContext())) {
                okay = true;
            } else {
                throwIheConfigurationException("Error loading actor file \"" + filename + "\"", configFile);
            }
        } else {
            // No connection file name given
            logConfigurationWarning("Missing attribute 'name' in 'ActorFile' definition", configFile);
        }
        // Done
        return okay;
    }

    /**
	 * Processes an Actor element.  This element will specify a single IHE actor
	 * and the connection(s) it should use.
	 * 
	 * @param element the XML DOM element defining the actor
	 * @return True if the actor was created successfully
	 * @throws IheConfigurationException When there is a problem with the configuration
	 */
	private boolean processActorDefinition(Element element, boolean autoInstall, File configFile) throws IheConfigurationException {
		boolean okay = false;
		// Get out the actor name and type
		String actorName = getAttributeValue(element, "name");
		if (actorName == null)
			throwIheConfigurationException("Missing attribute 'name' in 'Actor' definition", configFile);
		String actorType = getAttributeValue(element, "type");
		if (actorType == null)
			throwIheConfigurationException("Missing attribute 'type' in 'Actor' definition", configFile);
		// Process the definition
		okay = processIheActorDefinition(actorType, actorName, element, autoInstall, configFile);
		return okay;
	}
	
	/**
	 * Processes an Actor element to extract the parameters and create and install the appropriate
	 * object.
	 * 
	 * @param actorType the type of the actor to create
	 * @param actorName the name for this actor within the configuration file
	 * @param definition the XML DOM element defining the actor
	 * @return <code>true</code> if the actor was create successfully
	 * @throws IheConfigurationException When there is a problem with the configuration
	 */
	private boolean processIheActorDefinition(String actorType, String actorName, Element definition, boolean autoInstall, File configFile) throws IheConfigurationException {
		// Parse out the following information
		String description = null;
		IConnectionDescription sourceConnection = null;
		IConnectionDescription consumerConnection = null;
		ArrayList<IConnectionDescription> logConnections = new ArrayList<IConnectionDescription>();
		//Define a PIXRegistry server side connection, so that ITI-8 PIX Feed messages can be forwarded to the XDS Registry 
		IConnectionDescription pixRegistryServerConnection = null;		
		//Define a XDSRegistry server side connection so that ITI-42 and ITI-18  messages can be forwarded to the XDS Registry
		IConnectionDescription xdsRegistryServerConnection = null;
		//Define a XDSRepository server side connection so that ITI-41 and ITI-43  messages can be forwarded to the XDS Repository server
		IConnectionDescription xdsRepositoryServerConnection = null;
		//Define a XDSRegistry client side connection so that ITI-42 and ITI-38 messages can be forwarded to the XDS Registry server
		IConnectionDescription xdsRegistryClientConnection = null;
		//Define a XCARG server side connection so that ITI-38 & ITI-39 messages can be forwarded to the XCA Responding Gateway server
		IConnectionDescription xcaRGServerConnection = null;
		//Define a XDSRepository client side connection so that ITI-39 messages can be forwarded to the XDS Repository server
		IConnectionDescription xdsRepositoryClientConnection = null;
		
		// Look at each child node in turn
		NodeList elements = definition.getChildNodes();
		for (int elementIndex = 0; elementIndex < elements.getLength(); elementIndex++) {
			Node element = elements.item(elementIndex);
			if (element instanceof Element) {
				// See what type of element it is
				String kind = element.getNodeName();
				if (kind.equalsIgnoreCase("CONNECTION")) {
					// It is a connection element, get out the connection description
					String sourceName = getAttributeValue(element, "source");
					String consumerName = getAttributeValue(element, "consumer");
					if ((sourceName == null) && (consumerName == null) ) {
						logConfigurationWarning("Connection element with no 'source' or 'consumer' attribute", configFile);
					} else {
						// Pull out the source connection
						if (sourceName != null) {
							if (sourceConnection != null) {
								logConfigurationWarning("Duplicate 'source' connection attributes", configFile);
							} else {
								sourceConnection = ConnectionFactory.getConnectionDescription(sourceName);
								if (sourceConnection == null) {
									throwIheConfigurationException("Connection '" + sourceName + "' in actor '" + actorName + "' is not defined", configFile);
								}
							}
						}
						// Pull out the consumer connection
						if (consumerName != null) {
							if (consumerConnection != null) {
								logConfigurationWarning("Duplicate 'consumer' connection attributes", configFile);
							} else {
								consumerConnection = ConnectionFactory.getConnectionDescription(consumerName);
								if (consumerConnection == null) {
									throwIheConfigurationException("Connection '" + consumerName + "' in actor '" + actorName + "' is not defined", configFile);
								}
							}
						}					
					}
					
				} else if (kind.equalsIgnoreCase("AUDITTRAIL")) {
					// An ATNA logger definition
					String logName = getAttributeValue(element, "consumer");
					if (logName == null) {
						logConfigurationWarning("AuditTrail element with no 'consumer' attribute", configFile);
					}
					IConnectionDescription logConnection = ConnectionFactory.getConnectionDescription(logName);
					if (logConnection == null) {
						throwIheConfigurationException("AuditTrail connection '" + logName + "' in actor '" + actorName + "' is not defined", configFile);
					}
					logConnections.add(logConnection);
					
				} else if (kind.equalsIgnoreCase("XDSREGISTRY")) {
					// For XDS Registry, define a XDSRegistry connection for transactions ITI-42 Register Document Set and ITI-18 stored query
					String xdsRegistryName = getAttributeValue(element, "connection");
					if (xdsRegistryName == null) {
						throwIheConfigurationException("XdsRegistry element with no 'connection' attribute", configFile);
					}
					xdsRegistryServerConnection = ConnectionFactory.getConnectionDescription(xdsRegistryName);
					if (xdsRegistryServerConnection == null) {
						throwIheConfigurationException("XdsRegistry connection '" + xdsRegistryName + "' in actor '" + actorName + "' is not defined", configFile);
					}
				} else if (kind.equalsIgnoreCase("PIXREGISTRY")) {
					// For PIX Registry, define a PIX Registry Connection for Transaction ITI-8
					String pixRegistryName = getAttributeValue(element, "connection");
					if (pixRegistryName == null) {
						throwIheConfigurationException("PixRegistry element with no 'connection' attribute", configFile);
					}
				    pixRegistryServerConnection = ConnectionFactory.getConnectionDescription(pixRegistryName);
					if (pixRegistryServerConnection == null) {
						throwIheConfigurationException("PixRegistry connection '" + pixRegistryName + "' in actor '" + actorName + "' is not defined", configFile);
					}					
				}  else if (kind.equalsIgnoreCase("XDSREPOSITORY")) {
					// For XDS Repository, define a XDSRepository connection for transactions ITI-41, ITI-42 and ITI-43.
					String xdsRepositoryName = getAttributeValue(element, "connection");
					if (xdsRepositoryName == null) {
						throwIheConfigurationException("XdsRepository element with no 'connection' attribute", configFile);
					}
					xdsRepositoryServerConnection = ConnectionFactory.getConnectionDescription(xdsRepositoryName);
					if (xdsRepositoryServerConnection == null) {
						throwIheConfigurationException("XdsRepository connection '" + xdsRepositoryName + "' in actor '" + actorName + "' is not defined", configFile);
					}
				} else if (kind.equalsIgnoreCase("XDSREGISTRYCLIENT")) {
					// For XDS Repository or XCA Responding Gateway, define a XDSRegistry client side connection for transaction ITI-42.
					String xdsRegistryClientName = getAttributeValue(element, "connection");
					if (xdsRegistryClientName == null) {
						throwIheConfigurationException("XdsRegistryClient element with no 'connection' attribute", configFile);
					}
					xdsRegistryClientConnection = ConnectionFactory.getConnectionDescription(xdsRegistryClientName);
					if (xdsRegistryClientConnection == null) {
						throwIheConfigurationException("XdsRegistryClient connection '" + xdsRegistryClientName + "' in actor '" + actorName + "' is not defined", configFile);
					}
				} else if (kind.equalsIgnoreCase("XDSREPOSITORYCLIENT")) {
					// For XCA Gateway, define a XDSRepository client side connection for transaction ITI-39.
					String xdsRepositoryClientName = getAttributeValue(element, "connection");
					if (xdsRepositoryClientName == null) {
						throwIheConfigurationException("XdsRepositoryClient element with no 'connection' attribute", configFile);
					}
					xdsRepositoryClientConnection = ConnectionFactory.getConnectionDescription(xdsRepositoryClientName);
					if (xdsRepositoryClientConnection == null) {
						throwIheConfigurationException("XdsRepositoryClient connection '" + xdsRepositoryClientName + "' in actor '" + actorName + "' is not defined", configFile);
					}
				} else if (kind.equalsIgnoreCase("XCARG")) {
					// For XCA Responding Gateway, define a XCA RG server connection for transactions ITI-38 & 39
					String xcaRGName = getAttributeValue(element, "connection");
					if (xcaRGName == null) {
						throwIheConfigurationException("XcdRG element with no 'connection' attribute", configFile);
					}
					xcaRGServerConnection = ConnectionFactory.getConnectionDescription(xcaRGName);
					if (xcaRGServerConnection == null) {
						throwIheConfigurationException("XcaRG connection '" + xcaRGName + "' in actor '" + actorName + "' is not defined", configFile);
					}
				} else if (kind.equalsIgnoreCase("DESCRIPTION")) {
					// A description of this actor for GUI presentation
					description = getAttributeValue(element, "value");
					if (description == null) description = getNodeAsText(element);
					
				} else {
					// Not an element we know about
					logConfigurationWarning("Unknown actor XML element '" + kind + "'", configFile);
				}
			}
		}
		// Allow for some sloppiness in Secure Nodes
		if (actorType.equalsIgnoreCase("SecureNode")) {
			boolean warn = false;
			if (sourceConnection != null) {
				warn = true;
				logConnections.add(sourceConnection);
			}
			if (consumerConnection != null) {
				warn = true;
				logConnections.add(consumerConnection);
			}
			if (warn)
				log.warn("Actor '" + actorName + "' specifies a connection instead of an auditTrail in configuration file \"" + configFile.getAbsolutePath() + "\"");
		}
		// Make sure we got out a valid definition
		if (actorType.equalsIgnoreCase("XDSREGISTRY")) {
			if (pixRegistryServerConnection==null)
				throw new IheConfigurationException("Actor '" + actorName + "' must specify a valid PixRegistry in configuration file \"" + configFile.getAbsolutePath() + "\"");
			if (xdsRegistryServerConnection==null)
				throw new IheConfigurationException("Actor '" + actorName + "' must specify a valid XdsRegistry in configuration file \"" + configFile.getAbsolutePath() + "\"");
		} else if (actorType.equalsIgnoreCase("XDSREPOSITORY")) {
			if (xdsRepositoryServerConnection==null)
				throw new IheConfigurationException("Actor '" + actorName + "' must specify a valid XdsRepository in configuration file \"" + configFile.getAbsolutePath() + "\"");
			if (xdsRegistryClientConnection==null)
				throw new IheConfigurationException("Actor '" + actorName + "' must specify a valid XdsRegistryClient in configuration file \"" + configFile.getAbsolutePath() + "\"");
		} else if (actorType.equalsIgnoreCase("XCARG")) {
			if (xcaRGServerConnection==null)
				throw new IheConfigurationException("Actor '" + actorName + "' must specify a valid XcaRG in configuration file \"" + configFile.getAbsolutePath() + "\"");
			if (xdsRegistryClientConnection==null)
				throw new IheConfigurationException("Actor '" + actorName + "' must specify a valid XdsRegistryClient in configuration file \"" + configFile.getAbsolutePath() + "\"");
			if (xdsRepositoryClientConnection==null)
				throw new IheConfigurationException("Actor '" + actorName + "' must specify a valid XdsRepositoryClient in configuration file \"" + configFile.getAbsolutePath() + "\"");
		}			
		if (actorType.equalsIgnoreCase("SecureNode") && logConnections.isEmpty())
			throw new IheConfigurationException("Actor '" + actorName + "' must specify a valid auditTrail in configuration file \"" + configFile.getAbsolutePath() + "\"");
			// Actually create the actor
		if (autoInstall) {
			if (actorType.equalsIgnoreCase("XDSREGISTRY")) {
				return createXdsRegistryActor(actorName,xdsRegistryServerConnection, pixRegistryServerConnection, logConnections, null, configFile);
			} else if (actorType.equalsIgnoreCase("XDSREPOSITORY")) {
				return createXdsRepositoryActor(actorName,xdsRepositoryServerConnection, xdsRegistryClientConnection, logConnections, null, configFile);
			} else if (actorType.equalsIgnoreCase("XCARG")) {
				return createXcaRGActor(actorName, xcaRGServerConnection, xdsRegistryClientConnection, xdsRepositoryClientConnection, logConnections, null, configFile);
			} else 
				return true;
		} else {			
			ActorDescription actor = initActor(actorType);
			actor.id = actorName;
			actor.type = getHumanActorTypeString(actorType, configFile);
			actor.actorType = actorType;
			actor.sourceConnection = sourceConnection;
			actor.consumerConnection = consumerConnection;
			actor.logConnections = logConnections;
			if (xdsRegistryServerConnection != null) {
				actor.description = getHumanConnectionDescription(description, xdsRegistryServerConnection);
			} else if (xdsRepositoryServerConnection != null) {
				actor.description = getHumanConnectionDescription(description, xdsRepositoryServerConnection);
			} else if (xcaRGServerConnection != null) {
				actor.description = getHumanConnectionDescription(description, xcaRGServerConnection);
			} else if (actorType.equalsIgnoreCase("SecureNode") && !logConnections.isEmpty()) {
				actor.description = getHumanConnectionDescription(description, logConnections.get(0));
			} else {
				actor.description = actorName;
			}
//TODO: 
//			if (actor instanceof PixManagerActorDescription) {
//				((PixManagerActorDescription)actor).pixConsumerConnections = pixConsumerConnections;
//				((PixManagerActorDescription)actor).xdsRegistryConnection = xdsRegistryConnection;
//			}
			
			if (actor instanceof XdsRegistryActorDescription) {
				((XdsRegistryActorDescription)actor).xdsRegistryConnection = xdsRegistryServerConnection;
				((XdsRegistryActorDescription)actor).pixRegistryConnection = pixRegistryServerConnection;				
			}else if (actor instanceof XdsRepositoryActorDescription) {
				((XdsRepositoryActorDescription)actor).xdsRepositoryServerConnection = xdsRepositoryServerConnection;
				((XdsRepositoryActorDescription)actor).xdsRegistryClientConnection = xdsRegistryClientConnection;				
			}else if (actor instanceof XcaRGActorDescription) {
				((XcaRGActorDescription)actor).xcaRGServerConnection = xcaRGServerConnection;
				((XcaRGActorDescription)actor).xdsRegistryClientConnection = xdsRegistryClientConnection;				
				((XcaRGActorDescription)actor).xdsRepositoryClientConnection = xdsRepositoryClientConnection;
			}else if (actor instanceof XdsAuditActorDescription){
				((XdsAuditActorDescription)actor).xdsAuditConnection = logConnections;
			}
			
			actorDefinitions.add(actor);			
			return true;
		}
	}
	
	/**
	 * Gets a human understandable actor type name. 
	 * 
	 * @param type the actor type
	 * @param configFile the configuration file
	 * @return a human understandable actor type name
	 * @throws IheConfigurationException
	 */
	private String getHumanActorTypeString(String type, File configFile) throws IheConfigurationException {
		if (type.equalsIgnoreCase("SecureNode")) {
			return "Audit Record Repository";
		} else if (type.equals("XdsRegistry")) {
            return "OpenXDS XDS Registry";
        } else if (type.equals("XdsRepository")) {
            return "OpenXDS XDS Repository";
	    } else if (type.equals("XcaRG")) {
	        return "OpenXDS XCA Responding Gateway";
	    }
		else {
			throwIheConfigurationException("Invalid actor type '" + type + "'", configFile);
			return null;
		}
	}
	
	/**
	 * Gets a human understandable connection description.
	 * 
	 * @param description the description
	 * @param connection the connection
	 * @return a human understandable description of this connection
	 */
	private String getHumanConnectionDescription(String description, IConnectionDescription connection) {
		StringBuffer sb = new StringBuffer();
		String hostName = connection.getHostname();
		int port = connection.getPort();
		if (description != null)  {
			// "description host:port (TLS)"
			sb.append(description);
			if (connection.isSecure()) sb.append(" (TLS)");
			if (hostName != null) {
				sb.append(' ');
				sb.append(hostName);
				if (port >= 0) {
					sb.append(':');
					sb.append(port);
				}
			}
		} else if (hostName != null) {
			// "host:port (TLS)"
			sb.append(hostName);
			if (port >= 0) {
				sb.append(':');
				sb.append(port);
			}
			if (connection.isSecure()) sb.append(" (TLS)");
		}
		// Done
		return sb.toString();
	}
	
	
	/**
	 * Creates an IHE XDS AuditTrail actor and install it into the AuditBroker.
	 * 
	 * @param name the name of the actor to create (used in audit messages)
	 * @param xdsAuditConnection The description of the connection of the XDS
     * 			AuditTail in the affinity domain
	 * @param auditConnections the audit trail connections this actor should log to
	 * @param logger the IHE actor message logger to use for this actor, null means no message logging
	 * @return <code>true</code> if the actor is created successfully
	 * @throws IheConfigurationException When there is a problem with the configuration
	 */
	private boolean createXdsAuditActor(String name, Collection<IConnectionDescription> auditConnections, IMesaLogger logger, File configFile) throws IheConfigurationException {
		boolean okay = false;
		IheAuditTrail auditTrail = null;
		// Build a new audit trail if there are any connections to audit repositories.
		if (!auditConnections.isEmpty()) auditTrail = new IheAuditTrail(name, auditConnections);

		if (auditTrail != null) {
			AuditBroker broker = AuditBroker.getInstance();
			broker.registerAuditSource(auditTrail);
			okay = true;
		}
		// Record this installation, if it succeeded
		if (okay) actorsInstalled.add(name);
		return okay;
	}
	
	/**
	 * Creates an IHE XDS Registry actor and install it into the DocumentBroker.
	 * 
	 * @param name the name of the actor to create (used in audit messages)
	 * @param xdsRegistryConnection The description of the connection of the XDS
     * 			Registry in the affinity domain
	 * @param pixRegistryConnection The description of the connection of the PIX
	 * 			Registry in the affinity domain
	 * @param auditConnections the audit trail connections this actor should log to
	 * @param logger the IHE actor message logger to use for this actor, null means no message logging
	 * @return <code>true</code> if the actor is created successfully
	 * @throws IheConfigurationException When there is a problem with the configuration
	 */
	private boolean createXdsRegistryActor(String name, IConnectionDescription xdsRegistryConnection, 
			IConnectionDescription pixRegistryConnection, Collection<IConnectionDescription> auditConnections,			
			IMesaLogger logger, File configFile) throws IheConfigurationException {
		boolean okay = false;
		IheAuditTrail auditTrail = null;		
		// Build a new audit trail if there are any connections to audit repositories.
		if (!auditConnections.isEmpty()) auditTrail = new IheAuditTrail(name, auditConnections);
		XdsRegistryPatientService patientManager = XdsFactory.getXdsRegistryPatientService();
		XdsRegistryImpl xdsRegistry = new XdsRegistryImpl(pixRegistryConnection, xdsRegistryConnection, auditTrail);
		xdsRegistry.registerPatientManager(patientManager);
	    if (xdsRegistry != null) {
            XdsBroker broker = XdsBroker.getInstance();
            broker.registerXdsRegistry(xdsRegistry);
            okay = true;
        }
		// Record this installation, if it succeeded
		if (okay) actorsInstalled.add(name);
		return okay;
	}

	/**
	 * Creates an IHE XDS Registry actor and install it into the DocumentBroker.
	 * 
	 * @param name the name of the actor to create (used in audit messages)
	 * @param xdsRepositoryServerConnection The description of the connection of the XDS
     * 			Repository in the affinity domain
	 * @param xdsRegistryClientConnection The description of the client side connection of
	 * 			the XDS Registry in the affinity domain
	 * @param auditConnections the audit trail connections this actor should log to
	 * @param logger the IHE actor message logger to use for this actor, null means no message logging
	 * @return <code>true</code> if the actor is created successfully
	 * @throws IheConfigurationException When there is a problem with the configuration
	 */
	private boolean createXdsRepositoryActor(String name, IConnectionDescription xdsRepositoryServerConnection, 
			IConnectionDescription xdsRegistryClientConnection, Collection<IConnectionDescription> auditConnections,			
			IMesaLogger logger, File configFile) throws IheConfigurationException {
		boolean okay = false;
		IheAuditTrail auditTrail = null;
		// Build a new audit trail if there are any connections to audit repositories.
		if (!auditConnections.isEmpty()) auditTrail = new IheAuditTrail(name, auditConnections);

		XdsRepositoryImpl xdsRepository = new XdsRepositoryImpl(xdsRepositoryServerConnection, xdsRegistryClientConnection, auditTrail);
        if (xdsRepositoryServerConnection != null) {
            XdsBroker broker = XdsBroker.getInstance();
            broker.registerXdsRepository(xdsRepository);
            okay = true;
        }
		// Record this installation, if it succeeded
		if (okay) actorsInstalled.add(name);
		return okay;
	}
	
	/**
	 * Creates an IHE XCA Responding Gateway actor and install it into the DocumentBroker.
	 * 
	 * @param name the name of the actor to create (used in audit messages)
	 * @param xcaRGServerConnection The description of the connection of the XCA
     * 			Gateway in the affinity domain
	 * @param xdsRegistryClientConnection The description of the client side connection of
	 * 			the XDS Registry in the affinity domain
	 * @param xdsRepositoryClientConnection The description of the client side connection of
	 * 			the XDS Repository in the affinity domain
	 * @param auditConnections the audit trail connections this actor should log to
	 * @param logger the IHE actor message logger to use for this actor, null means no message logging
	 * @return <code>true</code> if the actor is created successfully
	 * @throws IheConfigurationException When there is a problem with the configuration
	 */
	private boolean createXcaRGActor(String name, IConnectionDescription xcaRGServerConnection, IConnectionDescription xdsRegistryClientConnection, 
			IConnectionDescription xdsRepositoryClientConnection, Collection<IConnectionDescription> auditConnections,			
			IMesaLogger logger, File configFile) throws IheConfigurationException {
		boolean okay = false;
		IheAuditTrail auditTrail = null;
		// Build a new audit trail if there are any connections to audit repositories.
		if (!auditConnections.isEmpty()) auditTrail = new IheAuditTrail(name, auditConnections);

		XcaRGImpl xcaRG = new XcaRGImpl(xcaRGServerConnection, xdsRegistryClientConnection, xdsRepositoryClientConnection, auditTrail);
        if (xcaRGServerConnection != null) {
            XdsBroker broker = XdsBroker.getInstance();
            broker.registerXcaRG(xcaRG);
            okay = true;
        }
		// Record this installation, if it succeeded
		if (okay) actorsInstalled.add(name);
		return okay;
	}

	/**
     * Gets an instance of a Class. This method try to instantiate by newInstance first. If it
     * does not exist, it will try to invoke getInstance();
     *
     * @param c the class whose object is to be instantiated
     * @return an Instance of Class c
     * @throws Exception If the object cannot be instantiated
     */
    private Object getObjectInstance(Class c) throws Exception {
        Object ret = null;
        try {
            //try new instance first
            ret = c.newInstance();
        } catch (InstantiationException e) {
           //try getInstance() method
            Method method = c.getMethod("getInstance");
            ret = method.invoke(null);
        }
        return ret;
    }
 
    /**
	 * Logs a configuration file warning.
	 * 
	 * @param message the warning to log
	 */
	private void logConfigurationWarning(String message, File configFile) {
		String filename = null;
		if (configFile != null) filename = configFile.getAbsolutePath();
		String warning = message;
		if (filename != null) warning = message + " in configuration file \"" + filename + "\"";
		log.warn(warning);
	}

	/**
	 * Throws a new IheConfigurationException.
	 * 
	 * @param message the message to include in the exception
	 * @throws IheConfigurationException The exception
	 */
	private void throwIheConfigurationException(String message, File configFile) throws IheConfigurationException {
		String filename = null;
		if (configFile != null) filename = configFile.getAbsolutePath();
		String error = message;
		if (filename != null) error = message + " in configuration file \"" + filename + "\"";
		log.error(error);
		throw new IheConfigurationException(message);
	}
	
	/**
	 * Gets an attribute value
	 * 
	 * @param node the XML DOM node holding the attribute
	 * @param name the name of the attribute
	 * @return the value of the attribute
	 */
	private String getAttributeValue(Node node, String name) {
		NamedNodeMap attributes = node.getAttributes();
		if (attributes == null) return null;
		Node attribute = attributes.getNamedItem(name);
		if (attribute == null) return null;
		return attribute.getNodeValue();
	}
	
	/**
	 * Gets the text included within an XML DOM element
	 * 
	 * @param node the XML DOM node holding the text
	 * @return the text
	 */
	private String getNodeAsText(Node node) {
		if (!node.hasChildNodes()) return null;
		Text nodeTextContents = (Text) node.getFirstChild();
		return nodeTextContents.getData();
	}	
	
	/**
	 * An implementation of the IheActorDescription class to be used by 
	 * GUI elements and other things.
	 * 
	 * @author Jim Firby
	 * @version 1.0 - Jan 11, 2006
	 */
	public class ActorDescription implements IheActorDescription {
		
		protected String id = null;
		protected String type = null;
		protected String description = null;
		protected boolean isInstalled = false;
		
		protected String actorType = null;
		protected IConnectionDescription sourceConnection = null;
		protected IConnectionDescription consumerConnection = null;
		protected Collection<IConnectionDescription> logConnections = null;

		public String getDescription() {
			return description;
		}

		public String getId() {
			return id;
		}

		public String getType() {
			return type;
		}
		
		public String getActorType() {
			return actorType;
		}
		
		public boolean isInstalled() {
			return isInstalled;
		}
		
		/**
		 * Gets the connection description of this actor.
		 * 
		 * @return the connection description
		 */
		public IConnectionDescription getConnection() {
			if (sourceConnection != null) return sourceConnection;
			else return consumerConnection;
		}
		
		/**
		 * Gets a collection of audit trail log connections for this actor.
		 * 
		 * @return a collection of audit trail log connections
		 */
		public Collection<IConnectionDescription> getLogConnection() {
			return logConnections;
		}
		
	}
	
	/**
	 * An implementation of the IheActorDescription class to be used by 
	 * XDS Registry Actor
	 * 
	 * @author Wenzhi Li
	 */
	public class XdsAuditActorDescription extends ActorDescription {
		/** Defines the XDS Registry Connection */
		private ArrayList<IConnectionDescription> xdsAuditConnection = null;
		

		/**
		 * Gets the connection for the XDS Audit Trail. The connect provides the details such as host name 
		 * and port etc which are needed for this XDS Audit Trail to talk to the XDS Repositories and XDS Registries.
		 * 
		 * @return the connection of XDS AuditTrail 
		 */
		public ArrayList<IConnectionDescription> getXdsAuditConnection() {
			return xdsAuditConnection;
		}
		
	}

	
	/**
	 * An implementation of the IheActorDescription class to be used by 
	 * XDS Registry Actor
	 * 
	 * @author Wenzhi Li
	 */
	public class XdsRegistryActorDescription extends ActorDescription {
		/** Defines the XDS Registry Connection */
		private IConnectionDescription xdsRegistryConnection = null;
		
		/** Defines the XDS Registry PIX Connection */
		private IConnectionDescription pixRegistryConnection = null;

		/**
		 * Gets the connection for the XDS Registry. The connect provides the details such as host name 
		 * and port etc which are needed for this XDS Registry to talk to the XDS Repositories and XDS Consumers.
		 * 
		 * @return the connection of XDS Registry
		 */
		public IConnectionDescription getXdsRegistryConnection() {
			return xdsRegistryConnection;
		}
		
		/**
		 * Gets the connection for the PIX Registry. The connect provides the details such as host name 
		 * and port etc which are needed for this PIX Registry to talk to the PIX Source
		 * 
		 * @return the connection of PIX Registry
		 */
		public IConnectionDescription getPixRegistryConnection() {
			return pixRegistryConnection;
		}
	}

	/**
	 * An implementation of the IheActorDescription class to be used by 
	 * XDS Repository Actor
	 * 
	 * @author Wenzhi Li
	 */
	public class XdsRepositoryActorDescription extends ActorDescription {
		/** Defines the server side of XDS Repository Connection */
		private IConnectionDescription xdsRepositoryServerConnection = null;
		
		/** Defines the client side of XDS Registry Connection */
		private IConnectionDescription xdsRegistryClientConnection = null;

		/**
		 * Gets the connection for the XDS Repository server. 
		 * 
		 * @return the connection of XDS Repository for 
		 */
		public IConnectionDescription getXdsRepositoryServerConnection() {
			return xdsRepositoryServerConnection;
		}
		
		/**
		 * Gets the connection for the XDS Registry client.  
		 * 
		 * @return the connection for XDS Registry client such as XDS Repository
		 */
		public IConnectionDescription getXdsRegistryClientConnection() {
			return xdsRegistryClientConnection;
		}
	}
	
	/**
	 * An implementation of the IheActorDescription class to be used by 
	 * XCA Responding Gateway Actor
	 * 
	 * @author Wenzhi Li
	 */
	public class XcaRGActorDescription extends ActorDescription {
		/** Defines the server side of XCA Responding Gateway Connection */
		private IConnectionDescription xcaRGServerConnection = null;
		
		/** Defines the client side of XDS Registry Connection */
		private IConnectionDescription xdsRegistryClientConnection = null;

		/** Defines the client side of XDS Repository Connection */
		private IConnectionDescription xdsRepositoryClientConnection = null;
		
		/**
		 * Gets the connection for the XCA Responding Gateway server. 
		 * 
		 * @return the connection of XCA Responding Gateway  
		 */
		public IConnectionDescription getXcaRGServerConnection() {
			return xcaRGServerConnection;
		}
		
		/**
		 * Gets the connection for the XDS Registry client.  
		 * 
		 * @return the connection for XDS Registry client
		 */
		public IConnectionDescription getXdsRegistryClientConnection() {
			return xdsRegistryClientConnection;
		}
		
		/**
		 * Gets the connection for the XDS Repository client.  
		 * 
		 * @return the connection for XDS Repository client
		 */
		public IConnectionDescription getXdsRepositoryClientConnection() {
			return xdsRepositoryClientConnection;
		}
	}

	/**
	 * Initiates this actor.
	 * 
	 * @param actorType the type of the actor to be initiated
	 * @return an instance of ActorDescription
	 */
    private ActorDescription initActor(String actorType) {
    	if (actorType.equalsIgnoreCase("SecureNode")) 
    		return new XdsAuditActorDescription();
    	if (actorType.equalsIgnoreCase("XdsRegistry")) 
    		return new XdsRegistryActorDescription();
    	if (actorType.equalsIgnoreCase("XdsRepository")) 
    		return new XdsRepositoryActorDescription();
    	if (actorType.equalsIgnoreCase("XcaRG")) 
    		return new XcaRGActorDescription();
    	else 
    	    return new ActorDescription();  
    }
	
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
