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

package org.openhealthtools.openxds.repository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;


/**
 * A DataSource for byte array
 *
 * @author  Diego Ballve / Digital Artefacts
 */
public class ByteArrayDataSource implements DataSource {
    
    byte bytes[];
    String contentType;
    
    public ByteArrayDataSource(byte bytes[], String contentType) {
        this.bytes = bytes;
        this.contentType = contentType;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(bytes);
    }
    
    public String getName() {
        return "Unknown";
    }
    
    public OutputStream getOutputStream() throws java.io.IOException {
        // not required, do not expose
        throw new UnsupportedOperationException("ByteArrayDataSource.getOutputStream()");
    }
    
}
