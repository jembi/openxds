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

<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/table.css" type="text/css" />
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

