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

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.helpers.PrintConversionEventImpl;
import javax.xml.bind.helpers.ValidationEventImpl;
import javax.xml.bind.helpers.ValidationEventLocatorImpl;

import org.xml.sax.SAXException;

import com.sun.xml.bind.Messages;
import com.sun.xml.bind.serializer.AbortSerializationException;
import com.sun.xml.bind.util.ValidationEventLocatorExImpl;

/**
 * 
 * @author
 *     Kohsuke Kawaguchi (kohsuke.kawaguchi@sun.com)
 */
public class Util {
    /**
     * Reports a print conversion error while marshalling.
     */
    public static void handlePrintConversionException(
        Object caller, Exception e, XMLSerializer serializer ) throws SAXException {
        
        if( e instanceof SAXException )
            // assume this exception is not from application.
            // (e.g., when a marshaller aborts the processing, this exception
            //        will be thrown) 
            throw (SAXException)e;
        
        String message = e.getMessage();
        if(message==null) {
            message = e.toString();
        }
        
        ValidationEvent ve = new PrintConversionEventImpl(
            ValidationEvent.ERROR, message,
            new ValidationEventLocatorImpl(caller), e );
        serializer.reportError(ve);
    }
    
    /**
     * Reports that the type of an object in a property is unexpected.  
     */
    public static void handleTypeMismatchError( XMLSerializer serializer,
            Object parentObject, String fieldName, Object childObject ) throws AbortSerializationException {
        
         ValidationEvent ve = new ValidationEventImpl(
            ValidationEvent.ERROR, // maybe it should be a fatal error.
            Messages.format(Messages.ERR_TYPE_MISMATCH,
                getUserFriendlyTypeName(parentObject),
                fieldName,
                getUserFriendlyTypeName(childObject) ),
            new ValidationEventLocatorExImpl(parentObject,fieldName) );
         
        serializer.reportError(ve);
    }
    
    private static String getUserFriendlyTypeName( Object o ) {
        if( o instanceof ValidatableObject )
            return ((ValidatableObject)o).getPrimaryInterface().getName();
        else
            return o.getClass().getName();
    }
}
