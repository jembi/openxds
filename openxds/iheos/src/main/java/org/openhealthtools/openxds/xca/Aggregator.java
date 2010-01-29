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
import gov.nist.registry.common2.logging.LoggerException;
import gov.nist.registry.common2.registry.AdhocQueryResponse;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.RegistryErrorList;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.xml.Util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public abstract class Aggregator {
    private Log log = LogFactory.getLog( Aggregator.class );
    /** The total number of requests associated with this Aggregator */
    protected int totalNumber;
    /** The number of requests whose results are available */
    protected int availableNumber;
    /** The number of requests whose results are failed to retrieve */
    protected int failureNumber;
    /**The max wait time in seconds before aggregation*/
    protected int maxWait = 30;
    /**The LogMessage*/
    protected LogMessage logMessage;
    /**The map to store the results from each home community
       Map<String(homeId), OMElement(result)> */
    protected Map<String, OMElement> results = Collections.synchronizedMap(new HashMap<String, OMElement>());
    /**RegistryErrorList object to store errors*/
    protected RegistryErrorList rel; 
    /**The final response */
    protected Response response;
    /**
     * @param numRequests the total number of requests.
     */
   Aggregator(int numRequests, LogMessage logMessage) throws XdsException {
        this.totalNumber = numRequests;
        this.availableNumber = 0;
        this.failureNumber = 0;
        this.logMessage = logMessage;
    	rel = new RegistryErrorList(RegistryErrorList.version_3,  true /* log */);
		rel.setIsXCA();
    }

   public synchronized void waitForAll() throws XdsInternalException, LoggerException {
        long start = System.currentTimeMillis();
        while( this.availableNumber + this.failureNumber < this.totalNumber ){
            log.debug("available/failed/total - " + this.availableNumber + "/" + this.failureNumber + "/"
                    + this.totalNumber + ", put to sleep");

            long now = System.currentTimeMillis();
            if ((now-start) > maxWait * 1000 ) {
            	String msg = "Request stoped after waiting for "+ maxWait +" seconds. Some communities did not respond.";
            	response.add_error( MetadataSupport.XDSUnavailableCommunity, msg, "Aggregator.java", logMessage);
                break;  //stop after for maxWait.
            }
            try {
                this.wait(3000); //by default, check when interrupted or every 3 seconds
            } catch (InterruptedException e) {}
        }
        aggregate();
		
	}
	
    private OMElement aggregate() throws XdsInternalException, LoggerException {
    	
    	for (String homeId : results.keySet()) {
    		OMElement result = results.get(homeId);
			logMessage.addOtherParam("Response from " + homeId, result.toString()) ;
    		
    		String error = checkNullResult( result );
    		
    		if (error != null) {
    			rel.add_error(MetadataSupport.XDSRegistryError, error + " from Responding Gateway " + homeId , "XcaRegistry.java", logMessage);	    			
    			continue;
    		}

    		OMElement responseElemWithStatus = getResponseElementWithStatus(result);
    		String status = responseElemWithStatus.getAttributeValue(MetadataSupport.status_qname);
    		//Add success result
    		if ( !status.equals(MetadataSupport.response_status_type_namespace + "Failure")) {
    			response.setHasSuccess();
				addResult(result, homeId);    			    		
    		}
			//Add error and/or warning
			OMElement registry_error_list = MetadataSupport.firstChildWithLocalName(responseElemWithStatus, "RegistryErrorList"); 
			if (registry_error_list != null) {
				rel.addRegistryErrorList(registry_error_list, logMessage);
    		}    			
    	}//for
		logMessage.addOtherParam("Result", response.getResponse().toString());
		
		return response.getResponse();
    }
    
    abstract protected String checkNullResult(OMElement result);    
    abstract protected OMElement getResponseElementWithStatus(OMElement result);
    abstract protected void addResult(OMElement result, String homeId) throws XdsInternalException;
    
    public Response getResponse() {
    	return this.response;
    }
    
    //Store the retrieved data from a responding gateway
    synchronized void store( String homeId, OMElement result ) {
    	if (log.isDebugEnabled()) {
    		log.debug("store result from homeId=" + homeId +  ", result=" + result );
    	}
    	results.put(homeId, result);

    	notifyAvailable();

    }

    synchronized void notifyAvailable(){
        this.availableNumber++;
        assert invariant();
        notify();
    }

    synchronized void notifyFailure(){
        this.failureNumber++;
        assert invariant();
        notify();
    }
    private boolean invariant(){
       return this.availableNumber + this.failureNumber <= this.totalNumber;
    }
}

