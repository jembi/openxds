/**
 *
 *  Copyright (C) 2009 SYSNET International <support@sysnetint.com>
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
 */
package org.openhealthexchange.openxds.webapp.filter;

import junit.framework.TestCase;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class StaticFilterTest extends TestCase {
    private StaticFilter filter = null;

    protected void setUp() throws Exception {
        filter = new StaticFilter();
        MockFilterConfig config = new MockFilterConfig();
        config.addInitParameter("includes", "/scripts/*");
        filter.init(config);
    }

    public void testFilterDoesntForwardWhenPathMatches() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/scripts/dojo/test.html");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertNull(chain.getForwardURL());
    }

    public void testFilterForwardsWhenPathDoesntMatch() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/editProfile.html");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertNotNull(chain.getForwardURL());
    }
}

