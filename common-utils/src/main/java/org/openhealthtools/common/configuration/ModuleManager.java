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
import org.openhealthtools.openxds.registry.api.IXdsRegistryLifeCycleManager;
import org.openhealthtools.openxds.registry.api.IXdsRegistryPatientManager;
import org.openhealthtools.openxds.registry.api.IXdsRegistryQueryManager;
import org.openhealthtools.openxds.repository.api.IXdsRepositoryItem;
import org.openhealthtools.openxds.repository.api.IXdsRepositoryManager;
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
	 * The factory method to get {@link IXdsRegistryLifeCycleManager}
	 * 
	 * @return the singleton {@link IXdsRegistryLifeCycleManager} instance
	 */
	public static IXdsRegistryLifeCycleManager getXdsRegistryLifeCycleManager() {
		return (IXdsRegistryLifeCycleManager)getInstance().getBean("registryLifeCycleManager");
	}

	/**
	 * The factory method to get {@link IXdsRegistryQueryManager}
	 * 
	 * @return the singleton {@link IXdsRegistryQueryManager} instance
	 */
	public static IXdsRegistryQueryManager getXdsRegistryQueryManager() {
		return(IXdsRegistryQueryManager)getInstance().getBean("registryQueryManager");
	}
	
	/**
	 * The factory method to get {@link IXdsRegistryPatientManager}
	 * 
	 * @return the singleton {@link IXdsRegistryPatientManager} instance
	 */
	public static IXdsRegistryPatientManager getXdsRegistryPatientManager() {
		return (IXdsRegistryPatientManager)getInstance().getBean("registryPatientManager");
	}

	/**
	 * The factory method to get {@link IXdsRepositoryManager}
	 * 
	 * @return the singleton {@link IXdsRepositoryManager} instance
	 */
	public static IXdsRepositoryManager getXdsRepositoryManager() {
		return (IXdsRepositoryManager)getInstance().getBean("repositoryManager");		
	}

	/**
	 * The factory method to get {@link IXdsRepositoryItem}. RepositoryItem is non-singleton 
	 * bean.
	 * 
	 * @return an {@link IXdsRepositoryItem} instance
	 */
	public static IXdsRepositoryItem getXdsReposiotryItem() {
		return (IXdsRepositoryItem)getInstance().getBean("repositoryItem");
	}

}
