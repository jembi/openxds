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