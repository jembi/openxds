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
package org.openhealthexchange.openxds.webapp.action;

import org.openhealthexchange.openxds.Constants;
import org.openhealthexchange.openxds.model.Address;
import org.openhealthexchange.openxds.model.User;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import org.subethamail.wiser.Wiser;
import org.apache.struts2.ServletActionContext;
import org.springframework.security.context.SecurityContextHolder;

public class SignupActionTest extends BaseActionTestCase {
    private SignupAction action;

    public void setSignupAction(SignupAction action) {
        this.action = action;
    }
    
    public void testDisplayForm() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(null, "GET", "/signup.html");
        ServletActionContext.setRequest(request);
        assertEquals("input", action.execute());
    }  
    
    public void testExecute() throws Exception {
        User user = new User();
        user.setUsername("self-registered");
        user.setPassword("Password1");
        user.setConfirmPassword("Password1");
        user.setFirstName("First");
        user.setLastName("Last");
        
        Address address = new Address();
        address.setCity("Denver");
        address.setProvince("CO");
        address.setCountry("USA");
        address.setPostalCode("80210");        
        user.setAddress(address);
        
        user.setEmail("self-registered@raibledesigns.com");
        user.setWebsite("http://raibledesigns.com");
        user.setPasswordHint("Password is one with you.");
        action.setUser(user);

        // set mock response so setting cookies doesn't fail
        ServletActionContext.setResponse(new MockHttpServletResponse());
        
        // start SMTP Server
        Wiser wiser = new Wiser();
        wiser.setPort(getSmtpPort());
        wiser.start();
        
        assertEquals("success", action.save());
        assertFalse(action.hasActionErrors());
        
        // verify an account information e-mail was sent
        wiser.stop();
        assertTrue(wiser.getMessages().size() == 1);

        // verify that success messages are in the session
        assertNotNull(action.getSession().getAttribute(Constants.REGISTERED));

        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
