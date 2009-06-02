<%--


     Copyright (C) 2009 SYSNET International <support@sysnetint.com>

     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
     implied. See the License for the specific language governing
     permissions and limitations under the License.


--%>
<% if (request.getAttribute("struts.valueStack") != null) { %>
<%-- ActionError Messages - usually set in Actions --%>
<s:if test="hasActionErrors()">
    <div class="error" id="errorMessages">    
      <s:iterator value="actionErrors">
        <img src="<c:url value="/images/iconWarning.gif"/>"
            alt="<fmt:message key="icon.warning"/>" class="icon" />
        <s:property/><br />
      </s:iterator>
   </div>
</s:if>

<%-- FieldError Messages - usually set by validation rules --%>
<s:if test="hasFieldErrors()">
    <div class="error" id="errorMessages">    
      <s:iterator value="fieldErrors">
          <s:iterator value="value">
            <img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon" />
             <s:property/><br />
          </s:iterator>
      </s:iterator>
   </div>
</s:if>
<% } %>

<%-- Success Messages --%>
<c:if test="${not empty messages}">
    <div class="message" id="successMessages">    
        <c:forEach var="msg" items="${messages}">
            <img src="<c:url value="/images/iconInformation.gif"/>"
                alt="<fmt:message key="icon.information"/>" class="icon" />
            <c:out value="${msg}"/><br />
        </c:forEach>
    </div>
    <c:remove var="messages" scope="session"/>
</c:if>

<%-- Error Messages (on JSPs, not through Struts --%>
<c:if test="${not empty errors}">
    <div class="error" id="errorMessages">
        <c:forEach var="error" items="${errors}">
            <img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon" />
            <c:out value="${error}"/><br />
        </c:forEach>
    </div>
    <c:remove var="errors" scope="session"/>
</c:if>