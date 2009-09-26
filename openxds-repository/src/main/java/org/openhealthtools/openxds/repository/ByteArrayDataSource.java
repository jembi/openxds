/*
 * ====================================================================
 *
 * This code is subject to the freebxml License, Version 1.1
 *
 * Copyright (c) 2001 - 2003 freebxml.org.  All rights reserved.
 *
 * $Header: /cvsroot/ebxmlrr/omar/src/java/org/freebxml/omar/server/repository/hibernate/ByteArrayDataSource.java,v 1.2 2007/03/21 14:24:34 farrukh_najmi Exp $
 * ====================================================================
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
