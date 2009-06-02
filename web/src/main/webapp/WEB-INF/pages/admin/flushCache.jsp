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
<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="flushCache.title"/></title>
    <meta name="heading" content="<fmt:message key='flushCache.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<cache:flush/>
<div class="message">
    <img src="<c:url value="/images/iconInformation.gif"/>"
        alt="<fmt:message key="icon.information"/>" class="icon" />
    <fmt:message key="flushCache.message"/>
</div>
<script type="text/javascript">
    window.setTimeout("history.back()", 2000);
</script>