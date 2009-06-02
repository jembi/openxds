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
<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="activeUsers.title"/></title>
    <meta name="heading" content="<fmt:message key='activeUsers.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>
<body id="activeUsers"/>

<p><fmt:message key="activeUsers.message"/></p>

<div class="separator"></div>

<input type="button" onclick="location.href='mainMenu.html'" value="<fmt:message key="button.done"/>"/>

<display:table name="applicationScope.userNames" id="user" cellspacing="0" cellpadding="0"
    defaultsort="1" class="table" pagesize="50" requestURI="">
  
    <display:column property="username" escapeXml="true" style="width: 30%" titleKey="user.username" sortable="true"/>
    <display:column titleKey="activeUsers.fullName" sortable="true">
        <c:out value="${user.firstName} ${user.lastName}" escapeXml="true"/>
        <c:if test="${not empty user.email}">
        <a href="mailto:<c:out value="${user.email}"/>">
            <img src="<c:url value="/images/iconEmail.gif"/>" 
                alt="<fmt:message key="icon.email"/>" class="icon"/></a>
        </c:if>
    </display:column>
        
    <display:setProperty name="paging.banner.item_name" value="user" />
    <display:setProperty name="paging.banner.items_name" value="users" />
</display:table>
