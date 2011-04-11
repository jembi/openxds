/**
 *  Copyright (c) 2009-2011 Misys Open Source Solutions (MOSS) and others
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

package org.openhealthtools.openxds.dsub;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.opendsub.DsubException;
import org.openhealthtools.opendsub.auditlog.AuditContext;
import org.openhealthtools.opendsub.auditlog.DsubAuditService;
import org.openhealthtools.opendsub.producer.DsubPublisher;
import org.openhealthtools.openexchange.actorconfig.Configuration;
import org.openhealthtools.openexchange.actorconfig.IheConfigurationException;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;
import org.openhealthtools.openexchange.audit.IheAuditTrail;
import org.openhealthtools.openxds.common.ConnectionUtil;
import org.openhealthtools.openxds.registry.api.XdsRegistry;

import proto.notification.wsa.EndpointReference;

/**
 * This class is used to handle DSUB document metadata publishing to
 * a DSUB broker.  
 *
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 * 
 */
public class Publisher {
	private static Log log = LogFactory.getLog(Publisher.class);
	
	private static Publisher instance = null;
	private DsubPublisher dsubPublisher = null;

	private Publisher() {
		dsubPublisher = new DsubPublisher();
		dsubPublisher.start();
		
        Runtime.getRuntime().addShutdownHook(new ShutdownHook(dsubPublisher));
	}
	
	public synchronized static Publisher getInstance() {
		if (instance == null) {
			instance = new Publisher();
		}
		return instance;
	}
	
	public void publish(OMElement message, XdsRegistry actor) {
		try {
			IConnectionDescription brokerConnection = Configuration.getConnection(actor.getActorDescription(), "DsubBroker", false);
			 
			if (brokerConnection == null) 
			    return;
			 
			String brokerUrl = ConnectionUtil.getTransactionEndpoint(brokerConnection);
			EndpointReference brokerEndpoint = new EndpointReference(brokerUrl);
			
			String producerUrl = actor.getServiceEndpoint(brokerConnection.isSecure());
			EndpointReference producerEndpoint = new EndpointReference(producerUrl);
			
			log.info("Calling Dsub broker: " + brokerUrl);
			if (log.isDebugEnabled()) {
				log.debug("Publishing message: " + message.toString());
			}
			
			Protocol protocol = ConnectionUtil.getProtocol(brokerConnection);
			dsubPublisher.receive(message, protocol, producerEndpoint, brokerEndpoint);
			
	        AuditContext context = new AuditContext(null);
    		String localIP = (String)MessageContext.getCurrentMessageContext().getProperty(MessageContext.TRANSPORT_ADDR);
            context.setLocalIP(localIP);
    		context.setRemoteIP(Integer.toString(brokerConnection.getPort()));
            //context.setReplyto(null);
	        DsubAuditService auditService = new DsubAuditService();
	        
			IheAuditTrail auditLog = (IheAuditTrail)actor.getAuditTrail();
	        auditService.logPublish(auditLog, context, message);

		}catch(IheConfigurationException e) {
			log.error("Failed to configure DocumentMetadataNotificationBroker Connection." , e);
		}catch(DsubException e) {
			log.error("Failed to publish to DocumentMetadataNotificationBroker" , e);
		}catch(Exception e) {
			log.error("Failed to publish to DocumentMetadataNotificationBroker" , e);
		}
	}
	
    private static class ShutdownHook extends Thread {
        private DsubPublisher dsubPublisher = null;

        ShutdownHook(DsubPublisher dsubPublisher) {
            this.dsubPublisher = dsubPublisher;
        }

        public void run() {
            try {
                this.dsubPublisher.stop();
            } catch (Exception e) {
                log.error("Failed to stop DsubPublisher.", e);
            }
        }
    }

}