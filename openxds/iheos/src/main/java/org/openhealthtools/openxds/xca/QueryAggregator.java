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
import gov.nist.registry.common2.registry.AdhocQueryResponse;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.xml.Util;

import java.util.Collection;
import java.util.Iterator;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class QueryAggregator extends Aggregator {
    private Log log = LogFactory.getLog( QueryAggregator.class );
    
    /**
     * @param requestHomeIds the collection of request home ids
     */
   public QueryAggregator(final Collection<String> requestHomeIds, LogMessage logMessage) throws XdsException {
        super(requestHomeIds, logMessage);
        
		response = new AdhocQueryResponse(Response.version_3, rel);
    }
    
    protected String checkNullResult(OMElement result) {
		if (result == null) {
			return "Null AdhocQueryResponse";
		}
		
		String status = result.getAttributeValue(MetadataSupport.status_qname);
		if (status == null) {
			return "Null status";
		}
		
		return null;	    	
    }
    
    protected OMElement getResponseElementWithStatus(OMElement result) {
    	//the result is AdhocQueryResponse which contains a status attribute
		return  result;    	
    }

    protected void addResult(OMElement result, String homeId) throws XdsInternalException {
		OMElement registry_object_list = MetadataSupport.firstChildWithLocalName(result, "RegistryObjectList"); 
		for (Iterator it=registry_object_list.getChildElements(); it.hasNext(); ) {
			OMElement registry_object = (OMElement) it.next();

			OMElement registry_object_2 = Util.deep_copy(registry_object);
			
			if (log.isDebugEnabled()) {
				log.debug("registry_object2 is \n" + registry_object_2.toString());
			}
			
			registry_object_2.setNamespace(MetadataSupport.ebRIMns3);
			
			String objectHome = registry_object_2.getAttributeValue(MetadataSupport.home_qname);
			if (objectHome == null || objectHome.equals("")) 
				registry_object_2.addAttribute("home", homeId, null);
			else if (!homeId.equals(objectHome)) {
				response.error("Invalid home returned " + objectHome +", expected home is " + homeId);
			}
			response.addQueryResults(registry_object_2);
		}    	
    }

}

