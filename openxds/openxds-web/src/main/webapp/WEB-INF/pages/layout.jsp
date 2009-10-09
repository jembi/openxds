<%@ include file="/common/taglibs.jsp"%>
<%@ page language="java"%>
<html>
<head>
<link rel="stylesheet" href="<c:url value='/css/tabs.css'/>" type="text/css" />
<link rel="stylesheet" href="<c:url value='/css/table.css'/>" type="text/css" />
<link rel="stylesheet" href="<c:url value='/css/jmesa.css'/>" type="text/css"/>

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
