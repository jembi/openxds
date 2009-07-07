/**
 *  Copyright © 2009 Misys plc, Sysnet International, Medem and others
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
 *     Misys plc - Initial API and Implementation
 */
package org.openhealthexchange.openxds.registry.adapter.omar31;

import java.math.BigInteger;

import javax.xml.registry.RegistryException;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.apache.log4j.Logger;
import org.freebxml.omar.common.BindingUtility;
import org.freebxml.omar.common.IterativeQueryParams;
import org.freebxml.omar.server.common.ServerRequestContext;
import org.freebxml.omar.server.query.sql.SQLQueryProcessor;
import org.freebxml.omar.server.security.authentication.AuthenticationServiceImpl;
import org.oasis.ebxml.registry.bindings.rim.RegistryObjectListType;
import org.openhealthexchange.common.utils.OMUtil;
import org.openhealthexchange.openxds.registry.api.IXdsRegistryQueryManager;
import org.openhealthexchange.openxds.registry.api.RegistryQueryException;
import org.openhealthexchange.openxds.registry.api.RegistrySQLQueryContext;
import org.openhealthexchange.openxds.registry.api.RegistryStoredQueryContext;

/**
 * The Registry Query Manager which defines the operations to 
 * query XDS Registry objects.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class XdsRegistryQueryManager implements IXdsRegistryQueryManager {
	private static final Logger log = Logger.getLogger(XdsRegistryQueryManager.class);

    public OMElement storedQuery(RegistryStoredQueryContext context)  throws RegistryQueryException {
    	//TODO:
    	return null;
    }
    
    public OMElement sqlQuery(RegistrySQLQueryContext context)  throws RegistryQueryException {
        OMElement ret = null;
        
    	String sql = context.getSql();
    	boolean returnLeafClass = context.isLeafClass();
    	if (sql == null || sql.equals("")) {
        	throw new RegistryQueryException("Invalid SQL query");    		
    	}
        if (log.isDebugEnabled()) {
            log.debug("Invoking SQL Query: " + sql);            	
        }
    	
        SQLQueryProcessor qp = SQLQueryProcessor.getInstance();
        org.oasis.ebxml.registry.bindings.query.ResponseOption responseOption = null;
        try {
            responseOption =
            BindingUtility.getInstance().queryFac.createResponseOption();
            responseOption.setReturnComposedObjects(true);
            if (returnLeafClass) {
            	responseOption.setReturnType(org.oasis.ebxml.registry.bindings.query.ReturnType.LEAF_CLASS);            	
            }else{
            	responseOption.setReturnType(org.oasis.ebxml.registry.bindings.query.ReturnType.OBJECT_REF);
            }
        }
        catch (javax.xml.bind.JAXBException e) {
        	throw new RegistryQueryException("Failed to create ResponseOption", e);
        }

        ServerRequestContext src = null;
        RegistryObjectListType rolt = null;
        IterativeQueryParams paramHolder = new IterativeQueryParams(0, -1);
        try {
        	src = new ServerRequestContext("XdsRegistryQueryManager.sqlQuery", null);
        	//TODO: use correct user
//            rolt = qp.executeQuery(src, AuthenticationServiceImpl.getInstance().farrukh, sql, responseOption, paramHolder);
            rolt = qp.executeQuery(src, AuthenticationServiceImpl.getInstance().registryGuest, sql, responseOption, paramHolder);
                       
        } catch (RegistryException e) {
        	try {
        		src.rollback();
        	}catch(RegistryException re) {
            	throw new RegistryQueryException("Failed to rollback", re);        		
        	}
        	throw new RegistryQueryException("Failed to create ResponseOption", e);
        }

        try {
	        org.oasis.ebxml.registry.bindings.query.AdhocQueryResponse ahqr = BindingUtility.getInstance().queryFac.createAdhocQueryResponse();
	        ahqr.setRegistryObjectList(rolt);
	        ahqr.setStatus(BindingUtility.CANONICAL_RESPONSE_STATUS_TYPE_ID_Success);
	        ahqr.setStartIndex(BigInteger.valueOf(paramHolder.startIndex));
	        ahqr.setTotalResultCount(BigInteger.valueOf(paramHolder.totalResultCount));
	        
	        String response = BindingUtility.getInstance().marshalObject(ahqr);
            if (log.isDebugEnabled()) {
                log.debug("SQL Response:\n" + response);            	
            }
	        ret = OMUtil.xmlStringToOM(response);
        }catch (javax.xml.bind.JAXBException e) {
        	throw new RegistryQueryException("Failed to create AdhocQueryResponse", e);	    	
	    }catch(XMLStreamException e) {
        	throw new RegistryQueryException("Could not create XMLStream from AdhocQueryResponse", e);	    	
	    }

        try {
        	src.commit();       
        }catch(RegistryException re) {
        	throw new RegistryQueryException("Failed to commmit", re);        		        	
        }
        //Finally return the result
    	return ret;        
    }


}
