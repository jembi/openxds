<%@ include file="/common/taglibs.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.LinkedList"%>
<%@ page import="org.openhealthexchange.openpixpdq.ihe.configuration.*"%>
<html>
<link rel="stylesheet" href="<%= request.getContextPath() %>/styles/table.css" type="text/css" />
<body>
<table class="TableTS" cellpadding="0" cellspacing="0">
<tr><td>
<div class="Table">
<s:form name="configForm" action="config" method="post" validate="true">
	<table class="TableCON" cellpadding="0" cellspacing="0" >
		<thead class="TableTS">
			<th colspan="5" class="TableTS">IHE Configuration</th>
		</thead>
		<tr>
		<td class = "TableTS" >	
	  Load configuration file:
	</td>
			<td class="TableTS">
				<input type=file id= "config" name= "config" style="display: none;">
			<!-- 	<s:file id="config" name="config" size="0"	label="Load configuration file " /> -->
			</td>
			<td>
			<s:textfield size="80" id="configFile" name="configFile" theme="simple"></s:textfield>
			</td>
			<td>
				<input type=button onclick="config.disabled=false;config.click();configFile.value=config.value;config.disabled=true;" value="Browse" >
			</td>
			<td>
				<s:submit name="action" key="button.load" theme="simple" />
			</td>
		</tr>
<%
		String[] actors = (String[]) request.getAttribute("Actors");
    	List l = new LinkedList();
    	if (actors != null) {

    		for (int x = 0; x < actors.length; x++) {
    			l.add(actors[x]);
    		}
    	}
    	List lActors = (List) request.getAttribute("ActorList");
    	if (lActors != null && lActors.size() > 0) {
    		String sType = ((IheActorDescription) lActors.get(0)).getType();
    %>
    <div class="Table">
	 <table class = "TableCON">
  		<thead class = "TableTH">
  			<th colspan="3" class = "TableTH"> 
				<%=sType%>
		  	</th>
		</thead>
	<%
			for (int x = 0; x < lActors.size(); x++) {
				IheActorDescription iad = (IheActorDescription) lActors.get(x);
				if (!sType.equals(iad.getType())) {
			sType = iad.getType();
	%>
			<table class = "TableCON">
  				<thead class = "TableTH">
  					<th colspan="3" class = "TableTH"> 
						<%=sType%>
		  			</th>
				</thead>
			 
			<%
			 			}
			 			%>
		<tr class = "TableTS">
		<td class = "TableTS">
		<input type="checkbox" name="actors" value="<%=iad.getId()%>" 
		<%if (l.contains(iad.getId())){ %> checked="checked"<%}%> >
			<%=iad.getDescription()%>
		</td>
		</tr>
		<%
				}
				%>
	</table>
	 <table class = "TableCON"> 
	 	
		<tr class = "TableTS">
		<td class = "TableTS">
			Log file:</td>
			<td class="TableTS">
	<input type=file name=logbrowse style="display: none;">
	</td>
	<td>
	<s:textfield size="80" id="logfile" name="logfile" theme="simple"/>
		</td>
		<td class="TableTS">
  	<input type=button onClick="logbrowse.disabled=false;logbrowse.click();logfile.value=logbrowse.value;logbrowse.disabled=true;" value="Browse" align="left"> 
	</td>
		</tr>
	
		<tr class = "TableTS">
		<td class = "TableTS" align="left" colspan="2">
			<s:submit name="action" key="button.start" theme="simple"/>
		</td>
		<td class = "TableTS" align="center" colspan="2">
			<s:submit name="action" key="button.stopall" theme="simple"/>
		</td>
		</tr>	
		<%
	  	}
	  	%>
  	</table>
	</table>
	</div></table>
</s:form>
</div>
</td></tr></table>
</body>
</html>