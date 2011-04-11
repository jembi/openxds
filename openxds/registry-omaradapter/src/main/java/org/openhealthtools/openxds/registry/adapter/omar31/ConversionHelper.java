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

package org.openhealthtools.openxds.registry.adapter.omar31;

import java.lang.reflect.UndeclaredThrowableException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;

/**
 * This class is the helper class for the Registry Adapter
 * 
 * @author <a href="mailto:anilkumar.reddy@misys.com">Anil kumar</a>
 *
 */
public class ConversionHelper {
	
	private static ConversionHelper instance = null;
    public org.oasis.ebxml.registry.bindings.rim.ObjectFactory rimFac;
    public org.oasis.ebxml.registry.bindings.rs.ObjectFactory rsFac;
    public org.oasis.ebxml.registry.bindings.lcm.ObjectFactory lcmFac;
    public org.oasis.ebxml.registry.bindings.query.ObjectFactory queryFac;
    public org.oasis.ebxml.registry.bindings.cms.ObjectFactory cmsFac;
    
  //NameSpaces
    private static final String XDS_REGISTRY_V3_NAMESPACE = "urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0";
    private static final String XDS_QUERY_V3_NAMESPACE = "urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0" ;
    private static final String XDS_RIM_V3_NAMESPACE = "urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0";
    private static final String XDS_LCM_V3_NAMESPACE = "urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0";
    private static final String XMLSCHEMA_INSTANCE_XSI = "http://www.w3.org/2001/XMLSchema-instance";
  //  private static final String XDS_b_REGISTRY_SCHEMA_LOCATION = "urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0 ../schema/ebRS/lcm.xsd";
    private static final String XDS_b = "urn:ihe:iti:xds-b:2007";
    public JAXBContext jaxbContext = null;
    public OMFactory omFactory = null;
    protected OMNamespace nsLcm = null;
    protected OMNamespace nsRim = null;
    protected OMNamespace nsXsi = null;
    protected OMNamespace nsRs = null;
    protected OMNamespace nsQuery = null;
    protected OMNamespace ns =null;

    /**
     * Class Constructor. Protected and only used by getInstance()
     *
     */
    protected ConversionHelper() {
        try {
            getJAXBContext();
            omFactory();
            namespace();
            rimFac = new org.oasis.ebxml.registry.bindings.rim.ObjectFactory();
            rsFac = new org.oasis.ebxml.registry.bindings.rs.ObjectFactory();
            lcmFac = new org.oasis.ebxml.registry.bindings.lcm.ObjectFactory();
            queryFac = new org.oasis.ebxml.registry.bindings.query.ObjectFactory();
            cmsFac = new org.oasis.ebxml.registry.bindings.cms.ObjectFactory();
        } catch (JAXBException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

	/**
	 * Gets the singleton instance.
	 *
	 * @return the singleton instance
	 */
	public synchronized static ConversionHelper getInstance() {
		if (instance == null) {
			instance = new ConversionHelper();
		}

		return instance;
	}

	/**
	 * Get JAXBContext with OMAR namespaces
	 * @return
	 * @throws JAXBException
	 */
	private JAXBContext getJAXBContext() throws JAXBException {
		if (jaxbContext == null) {
			jaxbContext = JAXBContext
					.newInstance(
							"org.oasis.ebxml.registry.bindings.rim:org.oasis.ebxml.registry.bindings.rs:org.oasis.ebxml.registry.bindings.lcm:org.oasis.ebxml.registry.bindings.query:org.oasis.ebxml.registry.bindings.cms",
							this.getClass().getClassLoader());
		}
		return jaxbContext;
	}

	/**
	 * Get Unmarsheller object.
	 * @return <code>Unmarsherller</code>
	 * @throws JAXBException
	 */
	public Unmarshaller getUnmarsheller() throws JAXBException {
		return jaxbContext.createUnmarshaller();
	}

	/**
	 * Gets OMFactory
	 * @return OMFactory
	 */
	public OMFactory omFactory() {
		if (omFactory == null)
			omFactory = OMAbstractFactory.getOMFactory();
		return omFactory;
	}
	
	/**
	 * Create name spaces
	 */
	public void namespace(){
		 nsLcm = omFactory.createOMNamespace(XDS_LCM_V3_NAMESPACE, "lcm");
		 nsRim = omFactory.createOMNamespace(XDS_RIM_V3_NAMESPACE, "rim");
		 nsXsi = omFactory.createOMNamespace(XMLSCHEMA_INSTANCE_XSI, "xsi");
		 nsRs = omFactory.createOMNamespace(XDS_REGISTRY_V3_NAMESPACE, "rs");
		 nsQuery = omFactory.createOMNamespace(XDS_QUERY_V3_NAMESPACE, "query");
		 ns = omFactory.createOMNamespace(XDS_b, null);
		
	}
	
}
