/**
 *  Copyright (c) 2009-2010 Misys Open Source Solutions (MOSS) and others
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  Contributors:
 *    Misys Open Source Solutions - initial API and implementation
 *    -
 */

package org.openhealthtools.openxds.webapp.action;

import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.RegistryUtility;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openxds.webapp.control.HttpUtils;
import org.openhealthtools.openxds.webapp.control.PagedQueryContents;
import org.openhealthtools.openxds.webapp.control.QueryContents;
import org.openhealthtools.openxds.webapp.control.QueryControl;
import org.openhealthtools.openxds.webapp.control.RetrieveBQueryContents;
import org.openhealthtools.openxds.webapp.control.Xdsview;

/**
 * This class supports querying the RegistryStoredQuery
 * 
 * @author <a href="anilkumar.reddy@misys.com">Anil Kumar</a>
 */
public class ViewAction extends BaseAction {
	
	private String id = null;
	private String action = null;
	boolean isPid = false;
	private String queryType = null;
	QueryControl queryControl= null;
	PrintWriter _writer = null;
	String verb;
	Map<String, String[]> parms;
	String message = "";
	String cntl = null;
	protected transient final Log log = LogFactory.getLog(getClass());
	//private static final String repositoryUrl = "http://localhost:8020/axis2/services/xdsrepositoryb";
	private static final String registryUrl = "http://localhost:8010/axis2/services/xdsregistryb";
	
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	public String query() throws Exception{
		if(getAction()=="" && getId() =="" || getAction()== null && getId() == null){
			return SUCCESS;
		}else{
			String queryType = parseQueryInput();
			if(queryType.equalsIgnoreCase("Doc") ||queryType.equalsIgnoreCase("SS")||queryType.equalsIgnoreCase("Fol")) 
			{
				clear_session();
			}else if(queryType.equalsIgnoreCase("Clear")){
				clear_session();
				return INPUT; 
			}
			runQuery(queryType, isPid);
			refresh();
		}
		log.debug("Parameter id="+ id + "queried for" +  action);
		return INPUT;
	}
	
	public String innerquery() throws Exception {
		getId();
		getAction();
		queryControl = get_query_control();
		if (queryControl == null) {
			queryControl = new QueryControl();
			queryControl.setEndpoint(registryUrl);
			setQueryControl(queryControl);
		}
		String[] verb_array = getRequest().getParameterValues("verb");
		verb = verb_array[0];
		parms = getRequest().getParameterMap();

		String x;
		x = getId();
		if (x != null) currentId(x);
		
		parseQueryInput();

		

		log.info("GET id=" + currentId() + 
				" raw_id=" + rawId() + 
				" verb=" + verb);

		boolean refresh = true;
		
		try {
		if (verb == null ) {

		}
		else if (verb.equals("showSS")) {
			String id = getParm("id");
			if (id == null)
				message("id is a required parameter");
			String registry = getParm("registry");
			if (registry == null)
				message("registry is a required parameter");
			if (id != null && registry != null) {
				rawId(currentId());
				runQuery("SS",isPid);
				refresh();
				return SUCCESS;
					
			}
		  }
		} catch (Exception e) {
			throw new ServletException("",e);
		}
		Xdsview xv = null;
		if (parms != null && queryControl != null)
			xv = queryControl.displayDetail(verb, parms, h());
		log.debug(xv.getOc().getBuf());
		StringBuffer page = xv.getOc().getBuf();
		getRequest().setAttribute("page", page);
	return SUCCESS;
	}
	
	void message(String m) {
		if (message == null || message.equals(""))
			message = m;
		else 
			message = message + "<br />" + m;
	}
	
	String last_part(String parts) {
		String[] part_a = parts.split("/");
		if (part_a.length == 0) return parts;
		return part_a[part_a.length - 1];
	}
	
	String getParm(String parmName) {
		String[] parmVals = getRequest().getParameterValues(parmName);
		if (parmVals != null && parmVals.length > 0)
			return parmVals[0];
		return null;
	}
	private String parseQueryInput() {
		String rawId = rawId(getId());
		if (rawId == null) rawId = rawId("");
		rawId = rawId(rawId.replaceAll("&amp;", "&"));

		log.debug("raw_id = " + rawId);

		currentId(rawId.trim());

		isPid = rawId.contains("^^^");

		String queryType = getAction();
		if (queryType == null) queryType = "";

		return queryType;
	}
	
	QueryContents runQuery(String query_type, boolean is_pid) throws Exception {
		
		queryControl = get_query_control();
		if (queryControl == null) {
			queryControl = new QueryControl();
			queryControl.setEndpoint(registryUrl);
			setQueryControl(queryControl);
		}
		String[] verb_array = getRequest().getParameterValues("verb");
		if(verb_array != null)
		verb = verb_array[0];
		String[] cntl_array = getRequest().getParameterValues("cntl");
		int cntl_i = 0;
		if (cntl_array != null && cntl_array.length != 0) {
			cntl = cntl_array[0];
			cntl_i = Integer.parseInt(cntl);
		}
		if (query_type.equals("SS")) {
			if (is_pid) {
				
				return queryControl.queryFindSS(currentId());
			} else {
				return queryControl.queryGetSSandContents(queryControl.singleton(currentId()));
			}
		} else if (query_type.equals("Doc")) {
			if (is_pid) {
				queryControl.leafClassQuery(true);
				return queryControl.queryFindDoc(currentId());
			  }
			else
				queryControl.leafClassQuery(true);
				return queryControl.queryGetDocuments(queryControl.singleton(currentId()));
		} 
		else if (query_type.equals("Fol")) {
			if (is_pid) {
				return queryControl.queryFindFol(currentId());
			} else {
				return queryControl.queryGetFolandContents(queryControl.singleton(currentId()));
			}
		}
		else if (verb.equals("related") && currentId() != null) {
			return queryControl.queryGetRelated(queryControl.singleton(currentId()));
		} 
		else if (verb.equals("ssac") && currentId() != null) {
			return queryControl.queryGetSSandContents(queryControl.singleton(currentId()));
		}
		else if (verb.equals("ss") && currentId() != null) {
			return queryControl.queryGetSubmissionSets(queryControl.singleton(currentId()));
		}
		else if (query_type.equals("nextpage") && cntl != null) {
			QueryContents qc = queryControl.getQueryContents(cntl_i);
			if (qc instanceof PagedQueryContents) {
				PagedQueryContents paged = (PagedQueryContents) qc;
				paged.next();
			}
		}
		else if (query_type.equals("display_doc") && cntl != null) {
			QueryContents qc = queryControl.getQueryContents(cntl_i);
			
			if (qc instanceof RetrieveBQueryContents) {
				RetrieveBQueryContents ret_b = (RetrieveBQueryContents) qc;
				getResponse().setContentType(ret_b.get_content_type());
				byte[] data = ret_b.get_content();
				if (data == null)
					throw new ServletException("ViewerServlet.doGet(): get_content() returned null");
				OutputStream os;
				try {
					os = getResponse().getOutputStream();
				}
				catch (IOException e) {
					throw new ServletException(RegistryUtility.exception_details(e));
				}
				if (os == null)
					throw new ServletException("ViewerServlet.doGet(): getOutputStream() return null");
				try {
					os.write(data, 0, data.length);
				} catch (Exception e) {
					throw new ServletException(RegistryUtility.exception_details(e));
				}
			}
		}
		else if (verb.equals("delete") && cntl != null) {
			queryControl.deleteQueryContents(cntl_i);
		}
		else if (query_type.equals("clear")) {
			clear_session();
		}
		
		return null;
	}
	
	QueryControl get_query_control() {
		queryControl = (QueryControl) getSession().getAttribute("queryControl");
		return queryControl;
	}
	void setQueryControl(QueryControl qc) {
		queryControl = qc;
		getSession().setAttribute("queryControl", qc);
	}
	
	String rawId() { 
		String id = (String) getSession().getAttribute("rawId");
		if (id == null) id = "";
		return id;
	}
	String rawId(String value) {
		getSession().setAttribute("rawId", value);
		return value;
	}
	
	String currentId(String value) {
		getSession().setAttribute("id", value);
		return value;
	}
	String currentId() { return (String) getSession().getAttribute("id"); }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	void clear_session() {
		setQueryControl(null);
		getSession().removeAttribute("queryControl");  // just in case

	}
	
	private HttpUtils refresh()  throws ServletException {
		log.info("refresh:" + message);
		h().head("XDS Viewer");
		h().open("table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\"");
		h().open("tr");
		h().o("<td colspan=\"2\" bgcolor=\"#dcdcd6\">");

		if (queryControl == null) System.out.println("refresh: query_control is null");
		Metadata m = (queryControl != null) ? queryControl.getMetadata() : new Metadata();
		//sq_panel(m);
		
		h().clos("td");
		h().clos("tr");
		if (message != null && !message.equals("")) {
			h().o("<tr span=\"2\">");
			h().o("<td>");
			h().o(message);
			h().o("</td>");
		}

		h().open("tr");

		displayStructure();

		h().o("<td  valign=\"top\">");
		//detail
		if (parms != null && queryControl != null)
			queryControl.displayDetail(verb, parms, h()); 

		h().o("</td>");
		h().o("</tr>");
		h().clos("table");
		h().tail();
		close();
		return h();
	}
	
	private void displayStructure()  throws ServletException {
		System.out.println("displayStructure");
		// structure
		h().open("td width=\"100%\" bgcolor=\"#dcdcd6\" valign=\"top\"");
		try {
			if (queryControl != null) {
				int i = 0;
				Xdsview xv = new Xdsview(h());
				for (QueryContents qc : queryControl.getAllQueryContents()) {
					System.out.println("displayStructure: " + qc.getClass().getName());
					xv.displayOutline(queryControl, i);
					i++;
				}
			} else
				System.out.println("No Query Control");
		} catch (Exception e) {
			h().alert(e.getClass().getName() + ": " + e.getMessage());
			e.printStackTrace();
		}
		h().clos("td");
	}
	
	/*private void displayStructure(Map<String, Object> ids){
		try {
			if (queryControl != null) {
				int i = 0;
				for (QueryContents qc : queryControl.getAllQueryContents()) {
					System.out.println("displayStructure: " + qc.getClass().getName());
					qc.displayStructure(queryControl, i, ids);
					i++;
				}
			} else
				log.info("No Query Control");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	void sq_panel(Metadata m)   throws ServletException {
		h().post_form("/openxds-web/innerquery.action", null);

		h().open("table");
		
		h().open("tr");
		h().open("td"); o("id: "); h().clos("td");
		h().open("td");
		h().input("text", "id", rawId(), null, "size=\"65\"");

		//		o("Patient ID?");
		//		h().input("checkbox", "is_pid", "", null, (is_pid)?"checked=\"true\"" : "");
		o("PatientId or UniqueId or UUID");
		h().clos("td");
		h().clos("tr");


		h().open("tr");
		h().open("td"); o("Query For:"); h().clos("td");
		h().open("td");
		h().input("submit", "action", "SS", null, "");
		h().input("submit", "action", "Doc", null, "");
		h().input("submit", "action", "Fol", null, "");
		h().input("submit", "action", "Clear", null, "");
		h().clos("td");
		h().clos("tr");

		h().clos("table");



		h().end_form();
		h().hr();

	}

	public void o(Object o) { try { h().o(o); } catch (Exception e) {} }

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
}
