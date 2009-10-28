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
package org.openhealthtools.openxds.registry.patient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openxds.registry.api.XdsRegistryPatientService;
import org.openhealthtools.openxds.repository.api.XdsRepositoryItem;
import org.openhealthtools.openxds.repository.api.XdsRepositoryService;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This class manages each module Spring initialization.
 *
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 *
 */
public class ModuleManager {
	
	private static final Log log = LogFactory.getLog(ModuleManager.class);

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
             	"classpath*:/applicationContextTest.xml"	
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
	 * The factory method to get {@link XdsRegistryPatientService}
	 * 
	 * @return the singleton {@link XdsRegistryPatientService} instance
	 */
	public static XdsRegistryPatientService getXdsRegistryPatientService() {
		return (XdsRegistryPatientService)getInstance().getBean("registryPatientService");
	}

	
	
	
}
