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

package org.openhealthtools.common.audit.jaxb.impl.runtime;

import javax.xml.bind.UnmarshallerHandler;
import javax.xml.bind.ValidationEvent;

import org.xml.sax.SAXException;

/**
 * Unified event handler that processes
 * both the SAX events and error events.
 * 
 * <p>
 * This interface refines {@link ContentHandler} as follows:
 * <ol>
 *  <li>element names and attribute names must be {@link String#intern()}ed.
 *  <li>namespace prefix and uris must be {@link String#intern()}ed.
 * </ol>
 */
public interface SAXUnmarshallerHandler extends UnmarshallerHandler {
    
    /**
     * Reports an error to the user, and asks if s/he wants
     * to recover. If the canRecover flag is false, regardless
     * of the client instruction, an exception will be thrown.
     * 
     * Only if the flag is true and the user wants to recover from an error,
     * the method returns normally.
     * 
     * The thrown exception will be catched by the unmarshaller.
     */
    void handleEvent( ValidationEvent event, boolean canRecover ) throws SAXException;
}
