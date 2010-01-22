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
<%@ page language="java" %>

<link rel="stylesheet" href="<c:url value='/css/tabs.css'/>" type="text/css"/>
<script type="text/javascript" language="javascript" src="<c:url value="/scripts/tabs.js"/>"></script>
<html>
<head></head>
<body onload="javascript:loadApplication(0)">
<div class="header">
<table width="100%" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td class="name">OpenXDS</td>
		<td valign="bottom">
		<div class="tabs">
		<ul>
			<li id="headertab0"><a href="javascript:loadApplication(0)">DocumentQuery</a></li>
			<li id="headertab1"><a href="javascript:loadApplication(1)">Configuration</a></li>
		</ul>
		</div>

		</td>

		<td class="logo" align="right"><img src="images/logo_misys.gif" /></td>

	</tr>
	
</table>
</div>
</body>
</html>