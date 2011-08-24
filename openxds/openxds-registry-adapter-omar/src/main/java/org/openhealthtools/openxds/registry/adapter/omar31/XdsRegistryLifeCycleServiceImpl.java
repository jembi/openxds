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

package org.openhealthtools.openxds.registry.adapter.omar31;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freebxml.omar.common.CommonRequestContext;
import org.freebxml.omar.common.spi.LifeCycleManager;
import org.freebxml.omar.common.spi.LifeCycleManagerFactory;
import org.freebxml.omar.common.spi.RequestContext;
import org.freebxml.omar.server.security.authentication.AuthenticationServiceImpl;
import org.oasis.ebxml.registry.bindings.rs.RegistryRequestType;
import org.oasis.ebxml.registry.bindings.rs.RegistryResponse;
import org.openhealthexchange.openpixpdq.data.PatientIdentifier;
import org.openhealthtools.openxds.XdsFactory;
import org.openhealthtools.openxds.registry.api.RegistryLifeCycleContext;
import org.openhealthtools.openxds.registry.api.RegistryLifeCycleException;
import org.openhealthtools.openxds.registry.api.XdsRegistryLifeCycleService;
import org.openhealthtools.openxds.registry.api.XdsRegistryPatientService;
import org.openhealthtools.openxds.registry.dao.MergePatientDao;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.misyshealthcare.connect.net.Identifier;

/**
 * This class adapts to the freebXML Omar 3.1 registry and 
 * defines the operations to manipulate XDS Registry
 * objects.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 * @author <a href="mailto:anilkumar.reddy@misys.com">Anil kumar</a>
 *
 */
public class XdsRegistryLifeCycleServiceImpl implements XdsRegistryLifeCycleService {
	private static Log log = LogFactory.getLog(XdsRegistryLifeCycleServiceImpl.class);
	protected static LifeCycleManager lcm = LifeCycleManagerFactory.getInstance().getLifeCycleManager();
	protected static ConversionHelper helper = ConversionHelper.getInstance();
	MergePatientDao mergePatientDao =null;
	public OMElement submitObjects(OMElement request, RegistryLifeCycleContext context)  throws RegistryLifeCycleException {
		RequestContext omarContext;
		RegistryResponse omarResponse = null;
		OMElement response;
		
		final String contextId = "org:openhealthexchange:openxds:registry:adapter:omar31:XdsRegistryLifeCycleManager:submitObjects:context";
		try {
			InputStream is = new ByteArrayInputStream(request.toString().getBytes("UTF-8"));
			Object registryRequest = helper.getUnmarsheller().unmarshal(is);
			//Creating context with request.
			omarContext = new CommonRequestContext(contextId,(RegistryRequestType) registryRequest);
			//Adding RegistryOperator role for the user.
			omarContext.setUser(AuthenticationServiceImpl.getInstance().registryOperator);
			
			// Sending request to OMAR methods.
			omarResponse = lcm.submitObjects(omarContext);
			//Create RegistryResponse as OMElement
			response = helper.omFactory().createOMElement("RegistryResponse", helper.nsRs);
			response.declareNamespace(helper.nsRs);
			response.declareNamespace(helper.nsXsi);
			response.addAttribute("status", omarResponse.getStatus(), null);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new RegistryLifeCycleException(e.getMessage());
		}

		return response;
	}
    @Transactional(propagation = Propagation.REQUIRES_NEW)
	public void mergePatients(String survivingPatient, String mergePatient, 
			RegistryLifeCycleContext context) throws RegistryLifeCycleException {
    	 try {
			 mergePatientDao.mergeDocument(survivingPatient, mergePatient);
			} catch (Exception e) {
				throw new RegistryLifeCycleException(e);
			}
	}

	
	public OMElement approveObjects(OMElement request, RegistryLifeCycleContext context) throws RegistryLifeCycleException {
		RequestContext omarContext;
		RegistryResponse omarResponse = null;
		InputStream is;
		OMElement response;
		final String contextId = "org:openhealthexchange:openxds:registry:adapter:omar31:XdsRegistryLifeCycleManager:approveObjects:context";
		try {
			is = new ByteArrayInputStream(request.toString().getBytes("UTF-8"));
			Object registryRequest = helper.getUnmarsheller().unmarshal(is);
			//Creating context with request.
			omarContext = new CommonRequestContext(contextId,(RegistryRequestType) registryRequest);
			//Adding RegistryOperator role for the user.
			omarContext.setUser(AuthenticationServiceImpl.getInstance().registryOperator);
			// Sending request to OMAR methods.
			omarResponse = lcm.approveObjects(omarContext);
			// 
			response = helper.omFactory().createOMElement("RegistryResponse", helper.nsRs);
			response.declareNamespace(helper.nsRs);
			response.declareNamespace(helper.nsXsi);
			response.addAttribute("status", omarResponse.getStatus(), null);
		
		}  catch (Exception e) {
			e.printStackTrace();
			throw new RegistryLifeCycleException(e.getMessage());
		}

		return response;
	}

	public OMElement deprecateObjects(OMElement request, RegistryLifeCycleContext context) throws RegistryLifeCycleException {
		RequestContext omarContext;
		RegistryResponse omarResponse = null;
		InputStream is;
		OMElement response;
		final String contextId = "org:openhealthexchange:openxds:registry:adapter:omar31:XdsRegistryLifeCycleManager:deprecateObjects:context";
		try {
			is = new ByteArrayInputStream(request.toString().getBytes("UTF-8"));
			Object registryRequest = helper.getUnmarsheller().unmarshal(is);
			//Creating context with request.
			omarContext = new CommonRequestContext(contextId,(RegistryRequestType) registryRequest);
			//Adding RegistryOperator role for the user.
			omarContext.setUser(AuthenticationServiceImpl.getInstance().registryOperator);
			// Sending request to OMAR methods.
			omarResponse = lcm.deprecateObjects(omarContext);
			
			response = helper.omFactory().createOMElement("RegistryResponse", helper.nsRs);
			response.declareNamespace(helper.nsRs);
			response.declareNamespace(helper.nsXsi);
			response.addAttribute("status", omarResponse.getStatus(), null);
		
		}  catch (Exception e) {
			e.printStackTrace();
			throw new RegistryLifeCycleException(e.getMessage());
		}

		return response;
	}
	public MergePatientDao getMergePatientDao() {
		return mergePatientDao;
	}
	public void setMergePatientDao(MergePatientDao mergePatientDao) {
		this.mergePatientDao = mergePatientDao;
	}
    private PatientIdentifier getPatientIdentifier(String patientId){
	    	Identifier assigningAuthority = null;
	    	String[] patient = patientId.split("\\^");
	    	String patId = patient[0];
	    	String[] assignAuth = patient[3].split("\\&");
	    	assigningAuthority =  new Identifier(assignAuth[0], assignAuth[1], assignAuth[2]);
	    	PatientIdentifier identifier =new PatientIdentifier();
	    	identifier.setId(patId);
	    	identifier.setAssigningAuthority(assigningAuthority);
	    	return identifier;
	}

}
