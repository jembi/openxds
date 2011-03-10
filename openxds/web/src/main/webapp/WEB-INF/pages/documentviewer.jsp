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
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>XDS Document Viewer</title>
<style>
.indent0 {
	text-indent: 0em;
	font-size: 100%
}

.indent1 {
	text-indent: 1em;
	font-size: 100%
}

.indent2 {
	text-indent: 2em;
	font-size: 100%
}
</style>

<link rel="stylesheet" href="<%= request.getContextPath() %>/css/table.css" type="text/css" />
</head>
<body>

<table class="search" width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td colspan="2">
		<s:form name="searchForm" action="query1" method="post" validate="true" target="left">
		<table width="100%" cellpadding="0" cellspacing="0">
			<thead class="TableTS">
				<th colspan="5" class="TableTS">Document Query</th>
			</thead>	
			<tr><td></td></tr>
			<tr>
		 <td class="search">
				 <s:text name="id:  "></s:text>
				 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				 <input type="text" name = "id" value="" size="65"/>PatientId or UniqueId or UUID
				 </td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr>
				<td>
					<s:text name="Query For:  "></s:text>
					 <s:submit name="action" key="button.Doc" theme="simple"></s:submit>
				    <s:submit name="action" key="button.SS" theme="simple" ></s:submit>				   
				    <s:submit name="action" key="button.Fol" theme="simple" ></s:submit>
				 	<s:submit name="action" key="button.clear" theme="simple"></s:submit>
				</td>
			</tr>
		</table>
		</s:form>
		<hr />
		</td>
	</tr>
	
</table>
</body>
</html>

