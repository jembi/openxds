package org.openhealthexchange.openxds;

import org.openhealthexchange.openxds.App;

import junit.framework.TestCase;

public class AppTest extends TestCase {
    public void testGetHello() throws Exception {
        assertEquals("Hello", App.getHello());
    }
}
