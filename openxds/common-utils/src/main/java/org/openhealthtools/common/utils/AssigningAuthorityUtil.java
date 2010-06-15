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
package org.openhealthtools.common.utils;

import java.util.List;

import org.apache.log4j.Logger;
import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;

import com.misyshealthcare.connect.net.Identifier;


/**
 * This class contains the utility methods for 
 * assigning authority.
 *
 * @author Wenzhi Li
 */
public class AssigningAuthorityUtil {

    private static final Logger log = Logger.getLogger(AssigningAuthorityUtil.class);

    /**
     * Reconciles authority with the ConnectionDescritpion configuration. An authority
     * can have NameSpace and/or UniversalId/UniversalIdType. For example, in the data source such as
     * database, if an authority is represented by NameSpace only, while in the xml configuration, the authority is configured
     * with both NameSpace and UnviersalId/UniversalIdType. The authority in the datasource has to be mapped
     * to the authority configured in the XML files.
     *
     * @param authority The authority
     * @param connection
     * @param adapter the adapter from where to get the domains
     * @return The authority according the configuration
     */
    public static Identifier reconcileIdentifier(Identifier authority, IConnectionDescription connection) {
        List<org.openhealthtools.openexchange.actorconfig.net.Identifier> identifiers = connection.getAllIdentifiersByType("domain");
        for (org.openhealthtools.openexchange.actorconfig.net.Identifier idNew : identifiers) {
    
        	//TODO: Fix the Identifier type
        	//Temporary conversion during the library migration
        	Identifier idOld = new Identifier(idNew.getAuthorityNameString(), idNew.getUniversalId(), idNew.getUniversalIdType()); 
            if ( idOld.equals(authority) ) {
                return idOld;
            }
        }
        //no identifier is found, just return the original authority
        return authority;
    }
    

    /**
     * Validates whether an ID domain is valid against the connection configuration.
     *
     * @param id the feed or request ID domain to be validated
     * @param connection
     * @param adapter the adapter from where to get the domains
     * @return <code>true</code> if the idDomain is valid.
     */
    public static boolean validateDomain(Identifier id, IConnectionDescription connection) {
         if (id == null) return  false;

         List<org.openhealthtools.openexchange.actorconfig.net.Identifier> identifiers = connection.getAllIdentifiersByType("domain");
         for (org.openhealthtools.openexchange.actorconfig.net.Identifier idNew : identifiers) {
        	    
         	//TODO: Fix the Identifier type
         	//Temporary conversion during the library migration
         	Identifier idOld = new Identifier(idNew.getNamespaceId(), idNew.getUniversalId(), idNew.getUniversalIdType()); 
             if ( idOld.equals(id) ) {
                return true;
             }
         }
         if (log.isInfoEnabled()) {
	         log.info("Failed to validate domain: "+ id.getNamespaceId() + "," +
	                 id.getUniversalId() + "," + id.getUniversalIdType());
	         log.info("List of known domains:");
	         for (org.openhealthtools.openexchange.actorconfig.net.Identifier identifier : identifiers) {
	             log.info("  Domain: "+ identifier.getNamespaceId() + "," +
	                 identifier.getUniversalId() + "," + identifier.getUniversalIdType());
	         }
         }
         return false;
    }
    
}