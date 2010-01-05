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
package org.openhealthtools.openxds.xca;

import gov.nist.registry.common2.service.AppendixV;
import gov.nist.registry.common2.soap.Soap;
import gov.nist.registry.ws.serviceclasses.XdsService;

import org.apache.axiom.om.OMElement;
import org.apache.commons.httpclient.protocol.Protocol;
import org.openhealthtools.common.utils.ConnectionUtil;

import com.misyshealthcare.connect.net.IConnectionDescription;

/**
 *
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class SoapCall implements Runnable {
	private IConnectionDescription connection;
	private OMElement request;
	private String action;
	private String homeId;
	private boolean mtom;
	private Aggregator ag;
	private XdsService service;
	
	public SoapCall(IConnectionDescription connection, OMElement request, 
			String action, String homeId, boolean mtom, Aggregator ag, XdsService service) {
		this.connection = connection;
		this.request = request;
		this.action = action;
		this.homeId = homeId;
		this.mtom = mtom;
		this.ag = ag;
	}
	
	public void run() {
		try {
			Protocol protocol = ConnectionUtil.getProtocol(connection);
			String epr = ConnectionUtil.getTransactionEndpoint(connection);
			Soap soap = new Soap();
			soap.soapCall(request, protocol, epr, mtom, true, true, action, null);
			ag.store(homeId, soap.getResult());
		}catch(Exception e){
			String msg = "Fail to get a response from the community " + homeId + " - " + e.getMessage();
			OMElement response = service.start_up_error(request, e, AppendixV.REGISTRY_ACTOR, msg);
			service.generateLogMessage(response);
			ag.store(homeId, response);
		}
	}
}
