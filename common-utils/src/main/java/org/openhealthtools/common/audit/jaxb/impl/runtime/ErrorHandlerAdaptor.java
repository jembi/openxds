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

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.bind.helpers.ValidationEventImpl;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sun.xml.bind.validator.Locator;

/**
 * Receives errors through {@link ErrorHandler} and reports to the
 * {@link SAXUnmarshallerHandler}.
 * 
 * @author
 *     Kohsuke Kawaguchi (kohsuke.kawaguchi@sun.com)
 */
public class ErrorHandlerAdaptor implements ErrorHandler {
    
    /** the client event handler that will receive the validation events */
    private final SAXUnmarshallerHandler host;
    
    /** the locator object responsible for filling in the validation event
     *  location info **/
    private final Locator locator;
   
    public ErrorHandlerAdaptor(
        SAXUnmarshallerHandler _host, Locator locator ) {
        this.host = _host;
        this.locator = locator;
    }
    
    public void error(SAXParseException exception) 
        throws SAXException {
            
        propagateEvent( ValidationEvent.ERROR, exception );
    }
    
    public void warning(SAXParseException exception) 
        throws SAXException {
            
        propagateEvent( ValidationEvent.WARNING, exception );
    }
    
    public void fatalError(SAXParseException exception) 
        throws SAXException {
            
        propagateEvent( ValidationEvent.FATAL_ERROR, exception );
    }
    
    private void propagateEvent( int severity, SAXParseException saxException ) 
        throws SAXException {
            
        // get location info:
        //     sax locators simply use the location info embedded in the 
        //     sax exception, dom locators keep a reference to their DOMScanner
        //     and call back to figure out where the error occurred.
        ValidationEventLocator vel = 
            locator.getLocation( saxException );

        ValidationEventImpl ve = 
            new ValidationEventImpl( severity, saxException.getMessage(), vel  );

        Exception e = saxException.getException();
        if( e != null ) {
            ve.setLinkedException( e );
        } else {
            ve.setLinkedException( saxException );
        }
        
        // call the client's event handler.
        host.handleEvent( ve, severity!=ValidationEvent.FATAL_ERROR );
    }
}
