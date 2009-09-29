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
package org.openhealthtools.openxds.registry.api;

import org.apache.axiom.om.OMElement;


/**
 * This interface defines the operations to manipulate XDS Registry
 * objects.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 *
 */
public interface XdsRegistryLifeCycleService {
    /**
     * Submits a list of Registry objects to the underneath Registry storage service. All 
     * the objects will be saved to the Registry database if successful. The available
     * Registry objects for submission are ExtrinsicObject, RegistryPackage,  Classification 
     * and Association etc. 
     * <p>
     * An example of the submission is as follows:
     * <pre>			
     * <lcm:SubmitObjectsRequest xmlns="urn:ihe:iti:xds-b:2007" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:lcm="urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0" xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0" xmlns:rs="urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0">
     * 	 <rim:RegistryObjectList>
     * 		<rim:ExtrinsicObject id="Document01" mimeType="text/xml" objectType="urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1">
	 *			......
	 *		</rim:ExtrinsicObject>
	 *		<rim:RegistryPackage id="SubmissionSet01">
	 *	    	......
	 *	   	</rim:RegistryPackage>
	 *		<rim:Classification id="cl10" classifiedObject="SubmissionSet01" classificationNode="urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd"/>
	 *		<rim:Association id="as01" associationType="urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember" sourceObject="SubmissionSet01" targetObject="Document01">
	 *			......
	 *		</rim:Association>
	 *	  </rim:RegistryObjectList>
 	 *	</lcm:SubmitObjectsRequest>
     * </pre>
     *
     * @param request the request that contains a list of registry objects to be submitted. 
     * @param context the {@link RegistryLifeCycleContext}
     * @return a RegistryResponse in the format of {@link OMElement} 
     * @throws RegistryLifeCycleException if the submission fails
     */
    public OMElement submitObjects(OMElement request, RegistryLifeCycleContext context)  throws RegistryLifeCycleException;

	/**
	 * Merges two patients together because they have been found to be
	 * the same patient.  The first argument describes the surviving patient 
	 * demographics; the second argument represents the patient to be merged
	 * with the surviving patient. After successful merge, the registry 
	 * objects such as ExtrinsicObject and RegistryPackage of the merge patient
	 * will be moved to the surviving patient.
	 * 
	 * @param survivingPatient the surviving patient id
	 * @param mergePatient the id of patient to be replaced, and merged with the surviving patient
     * @param context the {@link RegistryLifeCycleContext}
	 * @throws RegistryLifeCycleException when there is trouble merging the patients
	 */
	public void mergePatients(String survivingPatient, String mergePatient, 
			RegistryLifeCycleContext context) throws RegistryLifeCycleException;

	
    /**
     * Approves one or more previously submitted objects. All the objects will be changed to 
     * the Approved status if successful. The registry objects to be approved are
     * ExtrinsicObject, RegistryPackage,  Classification and Association etc. 
     * <p>
     * An example of the approve submission is as follows:
     * <pre>			
     * <lcm:ApproveObjectsRequest xmlns="urn:ihe:iti:xds-b:2007" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:lcm="urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0" xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0" xmlns:rs="urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0">
     *   ......
 	 * </lcm:ApproveObjectsRequest>
     * </pre>
     *
     * @param request the request that contains a list of registry objects to be approved. 
     * @param context the {@link RegistryLifeCycleContext}
     * @return a RegistryResponse in the format of {@link OMElement} 
     * @throws RegistryLifeCycleException if any object approval fails
     */    
	public OMElement approveObjects(OMElement request, RegistryLifeCycleContext context) throws RegistryLifeCycleException;

    /**
     * Deprecates one or more previously submitted objects. All the objects will be changed to 
     * the Deprecated status if successful.   
     * <p>
     * An example of the deprecate submission is as follows:
     * <pre>			
     * <lcm:DeprecateObjectsRequest xmlns="urn:ihe:iti:xds-b:2007" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:lcm="urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0" xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0" xmlns:rs="urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0">
     *   ......
 	 * </lcm:DeprecateObjectsRequest>
     * </pre>
     *
     * @param request the request that contains a list of registry objects to be deprecated 
     * @param context the {@link RegistryLifeCycleContext}
     * @return a RegistryResponse in the format of {@link OMElement} 
     * @throws RegistryLifeCycleException if any object deprecation fails
     */    
	public OMElement deprecateObjects(OMElement request, RegistryLifeCycleContext context) throws RegistryLifeCycleException;

}
