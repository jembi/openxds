/* Copyright 2009 Misys PLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License. 
 */

package org.openhealthexchange.common.audit.jaxb.impl.runtime;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Implemented by the generated code to unmarshall an object
 * from unmarshaller events.
 * 
 * <p>
 * AbstractUnmarshallingEventHandlerImpl throws a SAXException when a problem is encountered
 * and that problem is not reported. It is the responsibility of the caller
 * of this interface to report it to the client's ValidationEventHandler
 * and re-wrap it into UnmarshalException.
 * 
 * @author
 *     Kohsuke Kawaguchi (kohsuke.kawaguchi@sun.com)
 */
public interface UnmarshallingEventHandler {
    
    /**
     * Returns the content-tree object for which this unmarshaller
     * is working for.
     */
    Object owner();

    //
    // event handlers
    //
    void enterElement(String uri, String local, String qname, Attributes atts) throws SAXException;
    void leaveElement(String uri, String local, String qname ) throws SAXException;
    void text(String s) throws SAXException;
    void enterAttribute(String uri, String local, String qname ) throws SAXException;
    void leaveAttribute(String uri, String local, String qname ) throws SAXException;
    void leaveChild(int nextState) throws SAXException;
}
