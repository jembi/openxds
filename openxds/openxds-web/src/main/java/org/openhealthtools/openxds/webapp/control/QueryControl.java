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

package org.openhealthtools.openxds.webapp.control;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.xml.Util;
import gov.nist.registry.common2.xml.XmlFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.axiom.om.OMElement;

/**
 * Class to control the query objects. 
 * @author <a href="anilkumar.reddy@misys.com">Anil Kumar</a>
 */
public class QueryControl {
	String endpoint;
	// uuid => <query_type, queryContents>
	// in case of error, uuid could be uid instead
//	HashMap<String, HashMap<String, QueryContents>> map;
	ArrayList<QueryContents> queryContents;
	// repositoryUniqueId -> [secure endpoint, unsecure endpoint]
	public HashMap<String, ArrayList<String>> repositories; 
	Metadata metadata = null;  // contents of all queries combined
	// query types
	static final String ssac="ssac";  // GetSubmissionSetAndContents
	boolean leafClassQuery = true;
	String queryAction = "urn:ihe:iti:2007:RegistryStoredQuery";
	String queryResultAction = "urn:ihe:iti:2007:RegistryStoredQueryResponse";
	
	/*public String getHome(String id) throws MetadataException {
		String home = null;
		for (QueryContents qc : queryContents) {
			String h = qc.getHome(id);
			if (h == null) 
				continue;
			if (h.equals("") && home == null) {
				home = h;
				continue;
			}
			if (home == null)
				home = h;
			else if ( !home.equals(h)) {
				// oops, two homes for this object
				throw new MetadataException("Multiple homeCommunityIds for object " +
						id + 
						" " + home + " and " + h);
			}
			
		}
		return home;
	}*/
	
	public void leafClassQuery(boolean newvalue) {
		leafClassQuery = newvalue;
	}
	

	public QueryControl() {
//		map = new HashMap<String, HashMap<String, QueryContents>>();
		metadata = new Metadata();
		queryContents = new ArrayList<QueryContents>();
	}

	public QueryControl(QueryContents qc) {
		this.endpoint = null;
//		map = null;
		metadata = qc.getMetadata();
		queryContents = new ArrayList<QueryContents>();
		queryContents.add(qc);
	}

	void logException(Exception e, QueryContents qc) {
		StackTraceElement[] st = e.getStackTrace();
		StringBuffer buf = new StringBuffer();
		for (StackTraceElement ste : st) {
			buf.append(ste.toString());
			buf.append("\n");
		}
		qc.addFatalError(buf.toString());
	}

	public QueryControl(String filecontents) {
		this.endpoint = null;
//		this.map = null;
		this.metadata = new Metadata();
		this.queryContents = new ArrayList<QueryContents>();
		OMElement ele = null;
		try {
			ele = Util.parse_xml(filecontents);
		}
		catch (Exception e) {
			QueryContents qc = new BasicQueryContents();
			qc.setQueryType("BadResults");
			logException(e, qc);
			try {
				this.addQueryContents(qc);
			}
			catch (Exception e1 ) { logException(e1, qc); }
		}

		System.out.println("file input parse complete: root is " + ele.getLocalName());

		if (ele.getLocalName().equals("TestResults")) {
			OMElement fatalele = MetadataSupport.firstChildWithLocalName(ele, "FatalError");
			if (fatalele != null) {
				System.out.println("fatal error: " + fatalele.getText());
				String fatalmessage = fatalele.getText();
				if (fatalmessage != null && !fatalmessage.equals("")) {
					QueryContents qc = new BasicQueryContents();
					qc.addFatalError(fatalmessage);
					qc.setQueryType("FatalError");
					try {
						this.addQueryContents(qc);
					} catch (Exception e) { logException(e,qc); }
				}
			}
			for(OMElement ts : MetadataSupport.childrenWithLocalName(ele, "TestStep")) {

				try {
					String id = ts.getAttributeValue(MetadataSupport.id_qname);
					String step_status = ts.getAttributeValue(MetadataSupport.status_qname);
					OMElement trans_output = find_transaction_output(ts);
					if (trans_output == null) {
						throw new Exception("Cannot find transaction output from step " + id);
					}
					OMElement input = MetadataSupport.firstChildWithLocalName(trans_output, "InputMetadata");
					if (input == null) {
						throw new Exception("Cannot find InputMetadata from step " + id);
					}
					OMElement result = MetadataSupport.firstChildWithLocalName(trans_output, "Result");
					if (result == null) {
						throw new Exception("Cannot find Result from step " + id);
					}
					OMElement result_1 = result.getFirstElement();
					if (result_1 == null) {
						throw new Exception("Cannot find content of Result from step " + id);
					}
					String result_status = result_1.getAttributeValue(MetadataSupport.status_qname);
					OMElement expected_status_ele = MetadataSupport.firstChildWithLocalName(ts, "ExpectedStatus");
					String expected_status = "none";
					if (expected_status_ele == null) {
						throw new Exception("Cannot find ExpectedStatus from step " + id);
					} else {
						expected_status = expected_status_ele.getText();
					}
					LogFileQueryContents qc = new LogFileQueryContents();
					qc.setQueryType("Step " + id);
					qc.expected_status = expected_status;
					qc.reference_id = id;
					qc.step_status = step_status;
					qc.input = (input != null) ? input.getFirstElement() : null;
					qc.result = (result != null) ? result.getFirstElement() : null;
					qc.teststep = ts;
					qc.findMetadata();
					System.out.println("Found step " + id);
					//qc.validate_inputs();
					if (qc.getMetadata() == null)
						System.out.println("Did not find metadata in step");
					else
						System.out.println("Found metadata in step");
					try {
						this.addQueryContents(qc);
					}
					catch (Exception e1 ) {  logException(e1, qc); }

				}
				catch (Exception e) {
					QueryContents qc = new BasicQueryContents();
					qc.setQueryType("BadResults");
					logException(e, qc);
					System.out.println("log exception: " + e.getMessage());
					try {
						this.addQueryContents(qc);
					}
					catch (Exception e1 ) { logException(e1, qc); }
				}

			}
		} else {
			QueryContents qc = new SSandContentsQueryContents();
			qc.parseMetadataFromLogFile(ele);
			try {
				this.addQueryContents(qc);
			} catch (Exception e ) { logException(e, qc); }
		}
	}

	public void deleteQueryContents(int i) {
		if (i < this.queryContents.size())
			this.queryContents.set(i, null);
	}

	OMElement find_transaction_output(OMElement stepele) {
		for (Iterator<OMElement> it=stepele.getChildElements(); it.hasNext(); ) {
			OMElement child = it.next();
			String name = child.getLocalName();
			if (name.endsWith("Transaction"))
				return child;
		}
		return null;
	}



	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public boolean hasEndpoint() { return endpoint != null; }

	boolean is_uuid(String id) {
		return id.startsWith("urn:uuid:");
	}

//	void add_results_for_object(String uuid, String query_type, QueryContents contents) {
//		HashMap<String, QueryContents> contents_map = map.get(uuid);
//		if (contents_map == null) {
//			contents_map = new HashMap<String, QueryContents>();
//			map.put(uuid, contents_map);
//		}
//		contents_map.put(query_type, contents);
//		queryContents.add(contents);
//	}
//
//	public QueryContents get_results_for_object(String uuid, String query_type) {
//		HashMap<String, QueryContents> contents_map = map.get(uuid);
//		if (contents_map == null)
//			return null;
//		return contents_map.get(query_type);
//	}

	public ArrayList<QueryContents> getAllQueryContents() {
		return queryContents;
	}

	public QueryContents getQueryContents(int i) {
		if (i < 0 || i >= queryContents.size()) return null;
		return queryContents.get(i);
	}
	
	public int getQueryContentsIndex(QueryContents contents) {
		for (int i=0; i<queryContents.size(); i++) {
			if (queryContents.get(i) == contents)
				return i;
		}
		return -1;
	}

	boolean is_endpoint_secure() {
		return endpoint.startsWith("https");
	}

	public QueryContents queryFindFol(String pid) throws MetadataValidationException, MetadataException {
		FolQueryContents queryContents = new FolQueryContents();
		queryContents.setQueryType("Find Fol for");
		runQuery(singleton(pid), new FindFolders(), queryContents, queryAction, queryResultAction);


		Metadata m = queryContents.getMetadata();
		boolean object_refs = m.isObjectRefsOnly();
		if (object_refs) {
			queryContents.init(this);
			queryContents.type_ss();
			queryContents.preload();
		}

		return queryContents;
	}

	public QueryContents queryFindSS(String pid) throws MetadataValidationException, MetadataException {
		SSQueryContents queryContents = new SSQueryContents();
		queryContents.setQueryType("Find SS for");
		runQuery(singleton(pid), new FindSubmissionSets(), queryContents, queryAction, queryResultAction);


		Metadata m = queryContents.getMetadata();
		boolean object_refs = m.isObjectRefsOnly();
		if (object_refs) {
			queryContents.init(this);
			queryContents.type_ss();
			queryContents.preload();
		}

		return queryContents;
	}

	public QueryContents queryFindDoc(String pid) throws MetadataValidationException, MetadataException {
		FindDocsQueryContents queryContents = new FindDocsQueryContents();
		queryContents.setQueryType("Find Docs for");
		if (leafClassQuery) 
			runQuery(singleton(pid), new FindDocuments(true, queryContents), queryContents, queryAction, queryResultAction);
		else
			runQuery(singleton(pid), new FindDocuments(false, queryContents), queryContents, queryAction, queryResultAction);

		Metadata m = queryContents.getMetadata();
		boolean object_refs = m.isObjectRefsOnly();
		if (object_refs) {
			queryContents.init(this);
			queryContents.type_doc();
			queryContents.preload();
		}
			
		// remember returned homeCommunityId for later if it is present
		List<OMElement> objects = m.getMajorObjects();

		return queryContents;
	}

	// id may be id or uid
	public QueryContents queryGetDocuments(ArrayList<String> ids) throws MetadataException {
		QueryContents queryContents = new DocsQueryContents();
		queryContents.setQueryType("Documents");
		runQuery(ids, new GetDocument(), queryContents, queryAction, queryResultAction);

		return queryContents;

	}

	// id may be id or uid
	public QueryContents queryGetSSandContents(ArrayList<String> ids) throws MetadataException  {
		QueryContents queryContents = new SSandContentsQueryContents();
		queryContents.setQueryType("SSandContents");
		runQuery(ids, new GetSubmissionSetAndContents(), queryContents, queryAction, queryResultAction);

		return queryContents;
	}

	// id may be id or uid
	public QueryContents queryGetFolandContents(ArrayList<String> ids) throws MetadataException  {
		QueryContents queryContents = new FolandContentsQueryContents();
		queryContents.setQueryType("FolandContents");
		runQuery(ids, new GetFolderAndContents(), queryContents, queryAction, queryResultAction);

		return queryContents;
	}


	/*public QueryContents retrieve_b(ArrayList<String> ids) throws MetadataException {
		RetrieveBQueryContents queryContents = new RetrieveBQueryContents();
		queryContents.setQueryType("Retrieve.b");
		queryContents.secure(is_endpoint_secure());
		queryContents.setInitialEvidence(ids);
		queryContents.setEndpoint(this.endpoint);
		
		System.out.println("Ret.b: XCA? " + isXca() + " id = " + ids.get(0) + " home = " + getHome(ids.get(0)));
		
		RetrieveBEngine ret = new RetrieveBEngine();
		ret.setIsXca(isXca());
		ret.retrieve(ids.get(0), queryContents, getMetadata(), getRepositories(), getHome(ids.get(0)));

		try {
			Metadata save = queryContents.getMetadata();
			queryContents.setMetadata(null);
			this.addQueryContents(queryContents);
			queryContents.setMetadata(save);
		}
		catch (Exception e) {
			queryContents.addException(e);
		}
		return queryContents;
	}*/
	
	/*String getSingleHomeForIds(ArrayList<String> ids) throws MetadataException {
		String home = null;
		
		for (String id : ids) {
			String h = getHome(id);
			if (h == null)
				continue;
			if (h.equals(""))
				continue;
			if (home == null) { 
				home = h;
				continue;
			}
			if ( ! home.equals(h)) 
				throw new MetadataException("Trying to issue single SQ for ids " +
						ids + " but multiple homeCommunityId values found");
		}
		
		return home;
	}*/

	private void runQuery(ArrayList<String> ids, Sq query, QueryContents queryContents, String action, String returnAction) throws MetadataException {
		OMElement result = null;
		
		try {
			result = query.run(endpoint, ids, action, returnAction);
			queryContents.parseMetadataFromRegistryResponse(result);
			this.addQueryContents(queryContents);
		}
		catch (Exception e) {
			queryContents.addException(e.getClass(), e.getMessage());
			try {
				this.addQueryContents(queryContents);
			} catch (Exception e1) {}
		}


		Metadata m = queryContents.getMetadata();
		String uuid;
		String id = ids.get(0);
		if ( m != null) {
			uuid = (is_uuid(id)) ? id : m.getSubmissionSetId();
		} else {
			uuid = id;  // best we can do
		}

		queryContents.setReferenceId(uuid);
	}


	public QueryContents queryGetSubmissionSets(ArrayList<String> ids) throws MetadataException {
		QueryContents queryContents = new SSQueryContents();
		queryContents.setQueryType("Get SS of");
		runQuery(ids, new GetSubmissionSet(), queryContents, queryAction, queryResultAction);

		Metadata m = queryContents.getMetadata();
		if (m.getSubmissionSets().size() == 0) 
			queryContents.addError("No SubmissionSet returned");
		if (m.getAssociations().size() == 0) 
			queryContents.addError("No Association returned");
		if (m.getSubmissionSets().size() > 1) 
			queryContents.addError("Multiple SubmissionSets returned");
		if (m.getAssociations().size() > 1) 
			queryContents.addError("Multiple Associations returned");

		return queryContents;
	}

	public QueryContents queryGetRelated(ArrayList<String> ids) throws MetadataException {
		QueryContents queryContents = new RelatedQueryContents();
		queryContents.setQueryType("Get related to");
		runQuery(ids, new GetRelatedDocuments(), queryContents, queryAction, queryResultAction);

		return queryContents;
	}

	void addQueryContents(QueryContents qc) throws MetadataValidationException, MetadataException {
		queryContents.add(qc);
		if (qc.getMetadata() == null)
			qc.setMetadata(new Metadata());
		else
			metadata.addMetadata(qc.getMetadata(), false /* discard_duplicates */);
	}

	public String structures() {
		StringBuffer buf = new StringBuffer();

		buf.append(metadata.structure() + "\n");  

		for (int i=0; i<this.queryContents.size(); i++) {
			QueryContents qc = this.queryContents.get(i);
			buf.append(i + " " + qc.getMetadata().structure() + "\n");
		}

		return buf.toString();
	}

	public String structure() {
		return metadata.structure();
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public Xdsview displayDetail(String verb, Map<String, String[]> parms, HttpUtils h) {
		String localid = getParm(parms, "id");
		String cntl = getParm(parms, "cntl");
		int cntl_i = 0;
		if (cntl != null)
			cntl_i = Integer.parseInt(cntl);

		if (localid != null && !localid.equals("")) {
			try {
				Metadata m;
				if (cntl != null) {
					QueryContents qc = getQueryContents(cntl_i);
					m = qc.getMetadata();
				} else
					m = this.getMetadata();
				System.out.println("Structure " + m.structure());
				System.out.println("verb " + verb);
				System.out.println("id " + localid);
				System.out.println("docids " + m.getExtrinsicObjectIds());
				Xdsview xv = new Xdsview(m, h);
				xv.start();
				if (verb.equals("details")) {
					xv.displayRegistryObject(m, localid);
				} 
				else if (verb.equals("xml")) {
					xv.displayXml(m, localid);
				}
				xv.end();
				return xv;
			}
			catch (Exception e) {
				h.alert(e.getClass().getName() + ": " + e.getMessage());
				e.printStackTrace();
			}
		} 
		else if (verb.equals("errors") && cntl !=null) {
			QueryContents qc = this.getQueryContents(Integer.parseInt(cntl));
			if (qc != null) {
				display_arraylist(h, qc.getErrors());
			}

		} 
		else if (verb.equals("exceptions") && cntl !=null) {
			QueryContents qc = this.getQueryContents(Integer.parseInt(cntl));
			if (qc != null) {
				display_arraylist(h, qc.getExceptions());
			}

		} 
		else if (verb.equals("fatal") && cntl !=null) {
			QueryContents qc = this.getQueryContents(Integer.parseInt(cntl));
			if (qc != null) {
				display_arraylist(h, qc.getFatalErrors());
			}
		}
		else if (verb.equals("queryresponsexml") && cntl !=null) {
			QueryContents qc = this.getQueryContents(Integer.parseInt(cntl));
			if (qc != null) {
				OMElement ele = qc.getResultXml();
				Xdsview xv = new Xdsview(h);
				xv.start();
				if (ele != null)
					xv.out(XmlFormatter.htmlize(ele.toString(), false));
				else
					xv.out("null");
				xv.end();
				return xv;
			}
		}
		else if (verb.equals("queryrequestxml") && cntl !=null) {
			QueryContents qc = this.getQueryContents(Integer.parseInt(cntl));
			if (qc != null) {
				OMElement ele = qc.getRequestXml();
				Xdsview xv = new Xdsview(h);
				xv.start();
				if (ele != null)
					xv.out(XmlFormatter.htmlize(ele.toString(), false));
				else
					xv.out("null");
				xv.end();
				return xv;
			}
		}
		else if (verb.equals("inputxml")) {
			Xdsview xv = new Xdsview(h);
			xv.start();
			LogFileQueryContents qc = (LogFileQueryContents) this.getQueryContents(cntl_i);
			qc.displayInput(xv);
			xv.end();
			return xv;
		}
		else if (verb.equals("resultxml")) {
			Xdsview xv = new Xdsview(h);
			xv.start();
			LogFileQueryContents qc = (LogFileQueryContents) this.getQueryContents(cntl_i);
			qc.displayResult(xv);
			xv.end();
			return xv;
		}
		else if (verb.equals("stepxml")) {
			Xdsview xv = new Xdsview(h);
			xv.start();
			LogFileQueryContents qc = (LogFileQueryContents) this.getQueryContents(cntl_i);
			qc.displayTestStep(xv);
			xv.end();
			return xv;
		}
		return null;
	}

	private String getParm(Map<String, String[]> parms, String parm_name) {
		String value;
		String[] id_array = (String[]) parms.get(parm_name);
		if (id_array != null && id_array.length > 0)
			value = id_array[0];
		else {
			value = null;
		}
		return value;
	}

	void display_arraylist(HttpUtils h, ArrayList<String> al) {
		for (String s : al) {
			h.o(s);
			h.br();
		}
	}

	public ArrayList<String> singleton(String value) {
		ArrayList<String> al = new ArrayList<String>(1);
		al.add(value);
		return al;
	}

	public void setRepositories(HashMap<String, ArrayList<String>> repositories) {
		this.repositories = repositories;
	}

	public HashMap<String, ArrayList<String>> getRepositories() {
		return repositories;
	}


}
