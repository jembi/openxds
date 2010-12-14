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


import java.util.Collection;

import org.apache.axis2.context.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.actorconfig.net.SecureConnection;
import org.openhealthtools.openexchange.config.ConfigurationException;
import org.openhealthtools.openexchange.config.SpringFacade;
import org.openhealthtools.openxds.registry.api.XdsRegistry;
import org.openhealthtools.openxds.registry.api.XdsRegistryLifeCycleService;
import org.openhealthtools.openxds.registry.api.XdsRegistryPatientService;
import org.openhealthtools.openxds.registry.api.XdsRegistryQueryService;
import org.openhealthtools.openxds.repository.api.XdsRepository;
import org.openhealthtools.openxds.repository.api.XdsRepositoryItem;
import org.openhealthtools.openxds.repository.api.XdsRepositoryService;
import org.openhealthtools.openxds.xca.api.XcaIG;
import org.openhealthtools.openxds.xca.api.XcaRG;
import org.springframework.context.ApplicationContext;

/**
 * This class manages each module Spring initialization.
 *
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 *
 */
public class XdsFactory {
	
	private static final Log log = LogFactory.getLog(XdsFactory.class);

	private static final XdsFactory SINGLETON = new XdsFactory();

	private ApplicationContext applicationContext;

	private XdsFactory() {
		super();
		try {
			SpringFacade.loadSpringConfig( getConfigLocations() );
			applicationContext = SpringFacade.getApplicationContext();
		}catch(ConfigurationException e){
			e.printStackTrace();
		}
	}

	/**
	 * Singleton 
	 *
	 * @return the Singleton of XdsFactory
	 */
	public static XdsFactory getInstance() {
		return SINGLETON;
	}
	
	private String[] getConfigLocations() {
        return new String[] {
               "classpath*:/*applicationContext.xml" // for modular projects
            };
    }

	/**
	 * Gets spring beans object of the given bean name.
	 * 
	 * @param beanName the bean name
	 * @return the bean object
	 */
	public Object getBean(String beanName) {
		return this.applicationContext.getBean(beanName);
	}

	/**
	 * The factory method to get {@link XdsRegistryLifeCycleService}
	 * 
	 * @return the singleton {@link XdsRegistryLifeCycleService} instance
	 */
	public static XdsRegistryLifeCycleService getXdsRegistryLifeCycleService() {
		return (XdsRegistryLifeCycleService)getInstance().getBean("registryLifeCycleService");
	}

	/**
	 * The factory method to get {@link XdsRegistryQueryService}
	 * 
	 * @return the singleton {@link XdsRegistryQueryService} instance
	 */
	public static XdsRegistryQueryService getXdsRegistryQueryService() {
		return(XdsRegistryQueryService)getInstance().getBean("registryQueryService");
	}
	
	/**
	 * The factory method to get {@link XdsRegistryPatientService}
	 * 
	 * @return the singleton {@link XdsRegistryPatientService} instance
	 */
	public static XdsRegistryPatientService getXdsRegistryPatientService() {
		return (XdsRegistryPatientService)getInstance().getBean("registryPatientService");
	}

	/**
	 * The factory method to get {@link XdsRepositoryService}
	 * 
	 * @return the singleton {@link XdsRepositoryService} instance
	 */
	public static XdsRepositoryService getXdsRepositoryService() {
		return (XdsRepositoryService)getInstance().getBean("repositoryService");		
	}

	/**
	 * The factory method to get {@link XdsRepositoryItem}. RepositoryItem is non-singleton 
	 * bean.
	 * 
	 * @return an {@link XdsRepositoryItem} instance
	 */
	public static XdsRepositoryItem getXdsReposiotryItem() {
		return (XdsRepositoryItem)getInstance().getBean("repositoryItem");
	}

	/**
	 * Gets an appropriate repository actor.
	 * 
	 * @return
	 */
	public static XdsRepository getRepositoryActor() {
		Collection<XdsRepository> actors = XdsBroker.getInstance().getXdsRepositories();
		if (actors == null || actors.isEmpty()) {
			return null;
		}
		
		if (actors.size() == 1) { 
			return actors.iterator().next();
		}

		//Select one that is most appropriate
		XdsRepository ret = null;
		for (XdsRepository actor : actors) {
			//The only client connection is Registry Connection
			boolean isSecureActor = actor.getRegistryClientConnection() instanceof SecureConnection;
			
			ret = actor;
			
			boolean isSecure = isSecure();
			if ( isSecure && isSecureActor ||
			  	!isSecure && !isSecureActor) {
			    return actor;
			}
		} 
		return ret;
	}
	
	/**
	 * Gets an appropriate registry actor.
	 * 
	 * @param secure whether the registry actor needs to be secure
	 * @return
	 */
	public static XdsRegistry getRegistryActor() {

		Collection<XdsRegistry> actors = XdsBroker.getInstance().getXdsRegistries();
		if (actors == null || actors.isEmpty()) {
			return null;
		}
		
		if (actors.size() == 1) { 
			return actors.iterator().next();
		}
		
		//Select one that is most appropriate
		XdsRegistry ret = null;
		for (XdsRegistry actor : actors) {
			
			//The only client connection is PixRegistry Connection
			boolean isSecureActor = actor.getPixRegistryConnection() instanceof SecureConnection;
			
			ret = actor;
			
			boolean isSecure = isSecure();
			if ( isSecure && isSecureActor ||
			  	!isSecure && !isSecureActor) {
			    return actor;
			}
		} 
		return ret;
	}
	
	public static XcaRG getRGActor() {
		Collection<XcaRG> actors = XdsBroker.getInstance().getXcaRG();

		if (actors == null || actors.isEmpty() ) {
			return null;
		}
		
		if (actors.size() == 1) { 
			return actors.iterator().next();
		}

		//Select one that is most appropriate
		XcaRG ret = null;
		for (XcaRG actor : actors) {
			boolean isRegistrySecure = actor.getRegistryClientConnection() instanceof SecureConnection;
			boolean isReposiotrySecure = actor.getRepositoryClientConnection() instanceof SecureConnection;
			//TODO: revisit the logics to verify the secure actor
			boolean isSecureActor = isRegistrySecure && isReposiotrySecure;
			
			boolean isSecure = isSecure();
			
			ret = actor;
			if ( isSecure && isSecureActor ||
			  	!isSecure && !isSecureActor) {
			    return actor;
			}
		} 
		return ret;
	}

	public static XcaIG getIGActor() {
		Collection<XcaIG> actors = XdsBroker.getInstance().getXcaIG();

		if (actors == null || actors.isEmpty() ) {
			return null;
		}
		
		if (actors.size() == 1) { 
			return actors.iterator().next();
		}

		//Select one that is most appropriate
		XcaIG ret = null;
		for (XcaIG actor : actors) {
			boolean isRegistrySecure = actor.getRegistryClientConnection() instanceof SecureConnection;
			boolean isReposiotrySecure = actor.getRepositoryClientConnection() instanceof SecureConnection;
			//TODO: revisit the logics to verify the secure actor
			boolean isSecureActor = isRegistrySecure && isReposiotrySecure;
			
			boolean isSecure = isSecure();

			ret = actor;
			if ( isSecure && isSecureActor ||
				!isSecure && !isSecureActor) {
				    return actor;
				}
		} 
		return ret;
	}
	
	private static boolean isSecure() {
		boolean isSecure = MessageContext.getCurrentMessageContext().getTo()
					.toString().indexOf("https://") != -1;		
		
		if (log.isInfoEnabled()) {
			log.info(" is Secure = " + isSecure);
		}
		
		return isSecure;
	}
	
}
