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
package org.openhealthexchange.openxds.registry.api;

import org.apache.axiom.om.OMElement;


/**
 * This interface defines the operations to query XDS Registry
 * objects.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public interface IXdsRegistryQueryManager {
    /**
     * Sends a Registry Stored Query (AdhocQueryRequest) to the underneath Registry 
     * storage service. The available Registry objects for query are ExtrinsicObject, 
     * and RegistryPackage etc. 
     * <p>
     * An example of the returned objects from the query is as follows:
     * <pre>			
	 *	<query:AdhocQueryResponse xsi:schemaLocation="urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0 ../schema/ebRS/query.xsd" status="Success" xmlns:query="urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:rim="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0">
	 *		<rim:RegistryObjectList>
	 *			<rim:ExtrinsicObject id="urn:uuid:08a15a6f-5b4a-42de-8f95-89474f83abdf" isOpaque="false" mimeType="text/xml" objectType="urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1" status="urn:oasis:names:tc:ebxml-regrep:StatusType:Approved" xmlns:q="urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0">
 	 *			 ......
 	 *			</rim:ExtrinsicObject>
	 *		</rim:RegistryObjectList>
	 *	</query:AdhocQueryResponse>
     * </pre>
     *
     * @context context the context of this registry stored query which includes
     * 			those data such as queryId and query parameters etc.
     * 
     * @return a list of registry objects in the format of {@link OMElement}.
     * @throws RegistryQueryException if the query is failed
     */
    public OMElement storedQuery(RegistryStoredQueryContext context)  throws RegistryQueryException;

    public OMElement sqlQuery(RegistrySQLQueryContext context)  throws RegistryQueryException;
}
