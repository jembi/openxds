/**
 *  Copyright (c) 2009 Misys Open Source Solutions (MOSS) and others
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
 *
 */
package org.openhealthtools.common.audit.jaxb.impl.runtime;

import javax.xml.bind.JAXBException;
import javax.xml.bind.ValidationEvent;

import org.xml.sax.SAXException;

import com.sun.xml.bind.unmarshaller.InterningXMLReader;

/**
 * Filter {@link SAXUnmarshallerHandler} that interns all the Strings
 * in the SAX events. 
 * 
 * @author
 *     Kohsuke Kawaguchi (kohsuke.kawaguchi@sun.com)
 */
final class InterningUnmarshallerHandler extends InterningXMLReader implements SAXUnmarshallerHandler {
    
    private final SAXUnmarshallerHandler core;
    
    InterningUnmarshallerHandler( SAXUnmarshallerHandler core ) {
        super();
        setContentHandler(core);
        this.core = core;
    }
    
    public void handleEvent(ValidationEvent event, boolean canRecover) throws SAXException {
        core.handleEvent(event,canRecover);
    }

    public Object getResult() throws JAXBException, IllegalStateException {
        return core.getResult();
    }

}
