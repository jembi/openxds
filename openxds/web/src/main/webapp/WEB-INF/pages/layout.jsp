<%--

     Copyright (c) 2009-2010 Misys Open Source Solutions (MOSS) and others

     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
     implied. See the License for the specific language governing
     permissions and limitations under the License.

     Contributors:
       Misys Open Source Solutions - initial API and implementation
       -

--%>

<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java"%>
<html>
<head>
<link rel="stylesheet" href="<c:url value='/css/tabs.css'/>" type="text/css" />
<link rel="stylesheet" href="<c:url value='/css/table.css'/>" type="text/css" />


<script type="text/javascript" src="<c:url value="/scripts/grid.js"/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/jmesa.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/jquery-1.2.2.pack.js'/>"></script>
<script type="text/javascript" language="javascript" src="<c:url value="/scripts/tabs.js"/>"></script>
</head>
<body marginheight="0" marginwidth="0">
   <tiles:insertAttribute name="header"/>

   <tiles:insertAttribute name="body"/>

</body>
</html>
