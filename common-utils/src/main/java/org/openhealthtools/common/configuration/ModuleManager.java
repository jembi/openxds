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
package org.openhealthtools.common.configuration;

import org.apache.log4j.Logger;
import org.openhealthtools.openxds.registry.api.XdsRegistryLifeCycleManager;
import org.openhealthtools.openxds.registry.api.XdsRegistryPatientManager;
import org.openhealthtools.openxds.registry.api.XdsRegistryQueryManager;
import org.openhealthtools.openxds.repository.api.XdsRepositoryItem;
import org.openhealthtools.openxds.repository.api.XdsRepositoryManager;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This class manages each module Spring initialization.
 *
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 *
 */
public class ModuleManager {
	
	private static final Logger LOGGER = Logger.getLogger(ModuleManager.class);

	private static final ModuleManager SINGLETON = new ModuleManager();

	private ConfigurableApplicationContext applicationContext;

	private ModuleManager() {
		super();
		initializeSpring();
	}

	/**
	 * Singleton 
	 *
	 * @return the Singleton of ModuleManager
	 */
	public static ModuleManager getInstance() {
		return SINGLETON;
	}

	private void initializeSpring() {
		try {
		this.applicationContext = new ClassPathXmlApplicationContext(getConfigLocations());
		
		//add a shutdown hook for the above context... 
		this.applicationContext.registerShutdownHook();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String[] getConfigLocations() {
        return new String[] {
                "classpath:/applicationContext-resources.xml",
//                "classpath:/applicationContext-dao.xml",
                "classpath*:/applicationContext.xml", // for modular projects
//                "classpath*:/applicationContext-service.xml",
//               "classpath*:/applicationContext-dao.xml",// for web projects
                "classpath*:/repository.cfg.xml",// for openxds repository modular projects
                "classpath*:/externalidentifier.cfg.xml" // for openxds-registry-adapter-omar projects.
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
	 * The factory method to get {@link XdsRegistryLifeCycleManager}
	 * 
	 * @return the singleton {@link XdsRegistryLifeCycleManager} instance
	 */
	public static XdsRegistryLifeCycleManager getXdsRegistryLifeCycleManager() {
		return (XdsRegistryLifeCycleManager)getInstance().getBean("registryLifeCycleManager");
	}

	/**
	 * The factory method to get {@link XdsRegistryQueryManager}
	 * 
	 * @return the singleton {@link XdsRegistryQueryManager} instance
	 */
	public static XdsRegistryQueryManager getXdsRegistryQueryManager() {
		return(XdsRegistryQueryManager)getInstance().getBean("registryQueryManager");
	}
	
	/**
	 * The factory method to get {@link XdsRegistryPatientManager}
	 * 
	 * @return the singleton {@link XdsRegistryPatientManager} instance
	 */
	public static XdsRegistryPatientManager getXdsRegistryPatientManager() {
		return (XdsRegistryPatientManager)getInstance().getBean("registryPatientManager");
	}

	/**
	 * The factory method to get {@link XdsRepositoryManager}
	 * 
	 * @return the singleton {@link XdsRepositoryManager} instance
	 */
	public static XdsRepositoryManager getXdsRepositoryManager() {
		return (XdsRepositoryManager)getInstance().getBean("repositoryManager");		
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

}
