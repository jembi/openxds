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

package org.openhealthtools.openxds.xca;

import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.logging.LogMessage;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.RegistryErrorList;
import gov.nist.registry.common2.registry.RegistryResponse;
import gov.nist.registry.common2.registry.RetrieveDocumentSetResponse;

import java.util.List;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class RetrieveAggregator extends Aggregator {
    private Log log = LogFactory.getLog( RetrieveAggregator.class );
    
    private RetrieveDocumentSetResponse rdsResponse;
    
    /**
     * @param numRequests the total number of requests.
     */
   public RetrieveAggregator(int numRequests, LogMessage logMessage) throws XdsException {
        super(numRequests, logMessage);
        
        response = new RegistryResponse(RegistryErrorList.version_3, rel);
		response.setIsXCA();
		
        rdsResponse = new RetrieveDocumentSetResponse((RegistryResponse)response);		
    }
    
    protected String checkNullResult(OMElement result) {
		if (result == null) {
			return "Null RetrieveDocumentSetResponse";
		}
		OMElement registryResponse = MetadataSupport.firstChildWithLocalName(result, "RegistryResponse");
		if (registryResponse == null) {
			return "Null RegistryResponse";
		}
		
		String status = registryResponse.getAttributeValue(MetadataSupport.status_qname);
		if (status == null) {
			return "Null status";
		}
		return null;	    	
    }
    
    protected OMElement getResponseElementWithStatus(OMElement result) {
    	//the result is RetrieveDocumentSetResponse which contains a RegistryResponse
		OMElement registryResponse = MetadataSupport.firstChildWithLocalName(result, "RegistryResponse");
		return  registryResponse;    	
    }

    protected void addResult(OMElement result, String homeId) throws XdsInternalException {
		List<OMElement> docResponseList = MetadataSupport.childrenWithLocalName(result, "DocumentResponse"); 
		for (OMElement docResponse : docResponseList) {			
			if (log.isDebugEnabled()) {
				log.debug("docResponse is \n" + docResponse.toString());
			}
			
			docResponse.setNamespace(MetadataSupport.xdsB);
			
			OMElement elemHome = MetadataSupport.firstChildWithLocalName(docResponse, "HomeCommunityId");
			if (elemHome == null || elemHome.getText() == null || elemHome.getText().equals("")) 
				MetadataSupport.addChild("HomeCommunityId", MetadataSupport.xdsB, docResponse); 
			else if (!homeId.equals(elemHome.getText())) {
				response.error("Invalid home returned " + elemHome +", expected home is " + homeId);
			}
			rdsResponse.addDocumentResponse(docResponse);
		}    	
    }
    
    public RetrieveDocumentSetResponse getRetrieveDocumentSetResponse() {
    	return this.rdsResponse;
    }
}

