package gov.nist.registry.ws.sq.ebxmlrr21;

import gov.nist.registry.common2.exception.ExceptionUtil;
import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.MetadataValidationException;
import gov.nist.registry.common2.exception.XMLParserException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.exception.XdsNonIdenticalHashException;
import gov.nist.registry.common2.registry.And;
import gov.nist.registry.common2.registry.BackendRegistry;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataParser;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.SQCodeAnd;
import gov.nist.registry.common2.registry.SQCodeOr;
import gov.nist.registry.common2.registry.SQCodedTerm;
import gov.nist.registry.common2.registry.storedquery.StoredQuerySupport;
import gov.nist.registry.ws.evs.Evs;
import gov.nist.registry.ws.sq.RegistryValidations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openxds.log.LoggerException;

public class EbXML21QuerySupport implements RegistryValidations {
	private static final Log logger = LogFactory.getLog(EbXML21QuerySupport.class);

	StoredQuerySupport sqs;
	BackendRegistry br;
	boolean where = false;


	public EbXML21QuerySupport(StoredQuerySupport storedQuerySupport) {
		sqs = storedQuerySupport;
		br = new BackendRegistry(sqs.response, sqs.log_message);
		init();
	}

	public StringBuffer getQuery() { return sqs.query; }
	private void setQuery(StringBuffer buf) { sqs.query = buf; }

	// sql specific
	public List<String> getIdsFromRegistryResponse(OMElement rr) {
		List<String> ids = new ArrayList<String>();

		OMElement ahqr = MetadataSupport.firstChildWithLocalName(rr, "AdhocQueryResponse") ;
		if (ahqr == null)
			return ids;

		OMElement sqr = MetadataSupport.firstChildWithLocalName(ahqr, "SQLQueryResult") ;
		if (sqr == null)
			return ids;

		for (Iterator<OMElement> it=sqr.getChildElements(); it.hasNext();) {
			OMElement ele = (OMElement) it.next();
			if (sqs.return_leaf_class && ele.getLocalName().equals("ObjectRef"))
				continue;
			ids.add(ele.getAttributeValue(MetadataSupport.id_qname));
		}

		return ids;
	}

	public List<String> getIdsFromAdhocQueryResponse(OMElement rr) {
		List<String> ids = new ArrayList<String>();
		
		OMElement rol = MetadataSupport.firstChildWithLocalName(rr, "RegistryObjectList") ;
		if (rol == null)
			return ids;
		
		for (Iterator<OMElement> it=rol.getChildElements(); it.hasNext();) {
			OMElement ele = (OMElement) it.next();
			if (sqs.return_leaf_class && ele.getLocalName().equals("ObjectRef"))
				continue;
			ids.add(ele.getAttributeValue(MetadataSupport.id_qname));			
		}
		
		return ids;
	}

	// sql
	public String declareClassifications(List<String> names) {
		StringBuffer buf = new StringBuffer();

		for (String name : names) 
			buf.append(", Classification " + name + "\n"); 

		return buf.toString();
	}
	
	public String declareClassifications(SQCodedTerm term) {
		if (term instanceof SQCodeOr)
			return declareClassifications((SQCodeOr) term);
		if (term instanceof SQCodeAnd)
			return declareClassifications((SQCodeAnd) term);
		return null;
	}

	public String declareClassifications(SQCodeOr or) {
		StringBuffer buf = new StringBuffer();

		buf.append(", Classification " + or.getCodeVarName() + "\n"); 
		buf.append(", Slot " + or.getSchemeVarName() + "\n"); 

		return buf.toString();
	}

	public String declareClassifications(SQCodeAnd and) {
		StringBuffer buf = new StringBuffer();

		for (String name : and.getCodeVarNames())
			buf.append(", Classification " + name + "\n"); 
		
		for (String name : and.getSchemeVarNames())
			buf.append(", Slot " + name + "\n"); 

		return buf.toString();
	}

	// sql
	public List<String> query_for_object_refs() throws XMLParserException, LoggerException, XdsException {
		return br.queryForObjectRefs(getQuery().toString());
	}

	// sql
	public OMElement query() throws XdsException, LoggerException {
		String q = getQuery().toString();
		if (sqs.log_message != null)
			sqs.log_message.addOtherParam("raw query", q);
		return br.query(q, sqs.return_leaf_class);
	}

	// sql
	public OMElement query(boolean leaf_class) throws XdsException, LoggerException {
		String q = getQuery().toString();
		if (sqs.log_message != null)
			sqs.log_message.addOtherParam("raw query", q);
		return br.query(q, leaf_class  );
	}

	// sql
	public void init() {
		setQuery(new StringBuffer());
	}

	// sql
	public void where() {
		where = true;
		String x = "WHERE";
		getQuery().append(x);
	}

	public void and() {
		if (!where)
			a("AND");
		where = false;
	}

	// sql
	public void a(String x) {
		where = false;
		getQuery().append(x);
	}

	// sql
	public void a_quoted(String x) {
		where = false;
		getQuery().append("'");
		getQuery().append(x);
		getQuery().append("'");
	}

	// sql
	public void n() {
		getQuery().append("\n");
	}

	// sql
	public void a(List list) throws MetadataException {
		getQuery().append("(");
		boolean first_time = true;
		for (int i=0; i<list.size(); i++) {
			if ( !first_time) 
				getQuery().append(",");
			Object o = list.get(i);
			if (o instanceof String) 
				getQuery().append("'" + (String) o + "'");
			else if (o instanceof Integer) 
				getQuery().append(((Integer) o).toString());
			else {
				String trace = null;
				try {
					throw new Exception("foo");
				} catch (Exception e) {
					trace = ExceptionUtil.exception_details(e);
				}
				throw new MetadataException("Parameter value " + o + " cannot be decoded: \n" + trace);
			}
			first_time = false;
		}
		getQuery().append(")");
	}

	// sql
	public OMElement getDocById(String uuid) throws XdsException, LoggerException {
		ArrayList<String> ids = new ArrayList<String>();
		ids.add(uuid);
		return getDocById(ids);
	}

	// sql
	public OMElement getDocById(List<String> uuids) throws XdsException,
	LoggerException {
		init();

		select("eo");

		a("FROM ExtrinsicObject eo");  n();
		a("WHERE ");  n();
		a("	  eo.id IN "); a(uuids);  n(); 

		return query(sqs.return_leaf_class);
	}

	public OMElement getRegistryObjectById(List<String> uuids) throws XdsException,
	LoggerException {
		init();

		select("eo");

		a("FROM RegistryObject eo");  n();
		a("WHERE ");  n();
		a("	  eo.id IN "); a(uuids);  n(); 

		return query(sqs.return_leaf_class);
	}

	// sql
	public OMElement getDocByUid(String uid)  throws XdsException,
	LoggerException {
		ArrayList<String> uids = new ArrayList<String>();
		uids.add(uid);
		return getDocByUid(uids);
	}

	// sql
	public OMElement getDocByUid(List<String> uids) throws XdsException,
	LoggerException {
		init();

		select("eo");

		a("FROM ExtrinsicObject eo, ExternalIdentifier ei"); n();
		a("WHERE "); n();
		a("  ei.registryObject=eo.id AND"); n();
		a("  ei.identificationScheme='urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab' AND"); n();
		a("  ei.value IN "); a(uids);  n();

		return query(sqs.return_leaf_class);
	}

	// sql
	public String getDocIdFromUid(String uid)  throws XdsException,
	LoggerException {
		boolean rlc = sqs.return_leaf_class;
		sqs.return_leaf_class = false;
		OMElement result = getDocByUid(uid);
		Metadata metadata = MetadataParser.parseNonSubmission(result);
		List<OMElement> obj_refs = metadata.getObjectRefs();
		if (obj_refs.size() == 0)
			return null;

		sqs.return_leaf_class = rlc;

		return metadata.getId(obj_refs.get(0));
	}

	// sql
	public OMElement getAssociations(List<String> source_or_target_ids, List<String> assoc_types) throws XdsException,
	LoggerException {

		List<String> a_types = assoc_types;
//No need to strict away namespace for associationTypes for V3		
//		List<String> a_types = new ArrayList<String>();
//		if (assoc_types != null) {
//			for (String type : assoc_types) {
//				String[] parts = type.split(":");
//				if (parts.length > 1) 
//					a_types.add(parts[parts.length -1]);
//				else
//					a_types.add(type);
//			}
//		}
		init();

		select("a");

		a("FROM Association a");  n();
		a("WHERE ");  n();
		a("	  (a.sourceObject IN "); a(source_or_target_ids); a(" OR");  n(); 
		a("	  a.targetObject IN "); a(source_or_target_ids); a(" )"); n(); 
		if (assoc_types != null) {
			a("   AND a.associationType IN "); a(a_types); n();
		}

		return query(sqs.return_leaf_class);
	}

	/**
	 * Add code selection to restrict selection of DocumentEntries or SubmissionSets. When used
	 * to build SQL queries, the internal query buffer is appended with additional SELECT clauses
	 * to add restrictions to the query. Other implementations use other internal mechanisms.
	 * @param code_var         temporary variable name for the code value,  Used in building SQL queries
	 * @param code_scheme_var  temporary variable name for the classification scheme
	 * @param code_class_uuid  UUID of the classification scheme
	 * @param codes            collection of code values to match against
	 * @param schemes          collection of coding schemes to match against
	 * @throws MetadataException
	 */
	public void addCode(String code_var, String code_scheme_var, String code_class_uuid,
			List codes, List schemes) throws MetadataException {
		if (codes != null)  {                        a("AND ("); a(code_var);    a(".classifiedobject = doc.id AND "); n(); }
		if (codes != null)  {                        a("  ");    a(code_var);    a(".classificationScheme = '"); a(code_class_uuid); a("' AND "); n(); }  
		if (codes != null)  {                        a("  ");    a(code_var);    a(".nodeRepresentation IN "); a(codes);  a(" )"); n();                 }            

		if (schemes != null) {                  a("AND ("); a(code_scheme_var); a(".parent = "); a(code_var); a(".id AND   "); n();     }
		if (schemes != null) {                  a("  "); a(code_scheme_var); a(".name = 'codingScheme' AND   "); n();                    }                       
		if (schemes != null) {                  a("  "); a(code_scheme_var); a(".value IN "); a(schemes); a(" )"); n();                   }           
	}

	/**
	 * Add code selection to restrict selection of DocumentEntries. When used
	 * to build SQL queries, the internal query buffer is appended with additional SELECT clauses
	 * to add restrictions to the query. Other implementations use other internal mechanisms.
	 * @param code_vars - sql vars. Multiple per query param if AND logic invoked 
	 * @param code_scheme_var
	 * @param code_class_uuid
	 * @param codes - list of codes to select on (OR logic) OR And structure containing lists of codes (OR structure)
	 * @param schemes - code schemes
	 * @throws MetadataException
	 */
	public void addCode(List<String> code_vars, String code_scheme_var, String code_class_uuid, 
			List<Object> codes, List schemes) throws MetadataException {
		if (codes == null)
			return;

		if (code_vars.size() == 1) {
			if (codes instanceof And)
				throw new MetadataException("StoredQuery.add_code(): code_vars.size()==1 but codes have type And");
			String code_var = code_vars.get(0);
			and(); a(" ("); a(code_var);    a(".classifiedobject = doc.id AND "); n(); 
			a("  ");    a(code_var);    a(".classificationScheme = '"); a(code_class_uuid); a("' AND "); n(); 
			a("  ");    a(code_var);    a(".nodeRepresentation IN "); a(codes);  a(" )"); n();          

			if (schemes != null) {                  
				and(); a(" ("); a(code_scheme_var); a(".parent = "); a(code_var); a(".id AND   "); n();     
				a("  "); a(code_scheme_var); a(".name = 'codingScheme' AND   "); n();                                          
				a("  "); a(code_scheme_var); a(".value IN "); a(schemes); a(" )"); n();   
			}           
		} else {
			if ( !(codes instanceof And))
				throw new MetadataException("StoredQuery.add_code():  code_vars.size() > 1 but codes not of type And");

			for (int i=0; i<code_vars.size(); i++) {
				String code_var = code_vars.get(i);
				ArrayList<String> codes2 = (ArrayList<String>) codes.get(i);

				and(); a(" ("); a(code_var);    a(".classifiedobject = doc.id AND "); n(); 
				a("  ");    a(code_var);    a(".classificationScheme = '"); a(code_class_uuid); a("' AND "); n();  
				a("  ");    a(code_var);    a(".nodeRepresentation IN "); a(codes2);  a(" )"); n();                           
			}
		}
	}
	
	public void addCode(SQCodedTerm term) throws MetadataException {
		addCode(term, "doc");
	}
	
	public void addCode(SQCodedTerm term, String owner_var) throws MetadataException {
		if (term instanceof SQCodeOr)
			addCode((SQCodeOr) term, owner_var);
		if (term instanceof SQCodeAnd)
			addCode((SQCodeAnd) term);
	}
	
	void addCode(SQCodeOr term) throws MetadataException {
		addCode(term, "doc");
	}

	public void addCode(SQCodeOr term, String owner_var) throws MetadataException {
		and(); a(" ("); a(term.getCodeVarName());    a(".classifiedobject = " + owner_var + ".id AND "); n(); 
		a("  ");    a(term.getCodeVarName());    a(".classificationScheme = '"); a(term.classification); a("' AND "); n(); 
		a("  ");    a(term.getCodeVarName());    a(".nodeRepresentation IN "); a(term.getCodes());  a(" )"); n();          

		and(); a(" ("); a(term.getSchemeVarName()); a(".parent = "); a(term.getCodeVarName()); a(".id AND   "); n();     
		a("  "); a(term.getSchemeVarName()); a(".name = 'codingScheme' AND   "); n();                                          
		a("  "); a(term.getSchemeVarName()); a(".value IN "); a(term.getSchemes()); a(" )"); n();   
	}

	public void addCode(SQCodeAnd term) throws MetadataException {
		for (SQCodeOr or : term.getCodeOrs()) {
			addCode(or);
		}
	}

	// times come in as numeric values but convert them to string values to avoid numeric overflow
	public void addTimes(String att_name, String from_var, String to_var,
			String from_limit, String to_limit, String var_name) {
		if (from_limit != null)  { and(); a(" (");  a(from_var); a(".parent = " + var_name + ".id AND ");      n();      }                                         
		if (from_limit != null)  {  a("  ");a(from_var);  a(".name = '"); a(att_name); a("' AND     ");      n(); }                                              
		if (from_limit != null)  { a("  "); a(from_var); a(".value >= "); a_quoted(from_limit); a(" ) "); n(); }

		if (to_limit != null)    { and(); a(" (");  a(to_var); a(".parent = " + var_name + ".id AND ");      n();         }                                      
		if (to_limit != null)   { a("  "); a(to_var);  a(".name = '"); a(att_name); a("' AND     ");      n(); }                                              
		if (to_limit != null)   { a("  "); a(to_var); a(".value < "); a_quoted(to_limit); a(" ) "); n();            }                  
	}

	public OMElement getRpByUid(String uid, String identification_scheme) throws XdsException,
	LoggerException {
		init();

		select("ss");

		a("FROM RegistryPackage ss, ExternalIdentifier uniq"); n();
		a("WHERE");  n();
		a("  uniq.registryObject = ss.id AND");  n();
		a("  uniq.identificationScheme = '" + identification_scheme + "' AND");  n();
		a("  uniq.value = '" + uid + "'");
		return query();
	}

	public OMElement getRpByUid(List<String> uids, String identification_scheme) throws XdsException,
	LoggerException {
		init();

		select("ss");

		a("FROM RegistryPackage ss, ExternalIdentifier uniq"); n();
		a("WHERE");  n();
		a("  uniq.registryObject = ss.id AND");  n();
		a("  uniq.identificationScheme = '" + identification_scheme + "' AND");  n();
		a("  uniq.value IN "); a(uids);
		return query();
	}

	public OMElement getAssocById(String assoc_id)
	throws XdsException, LoggerException {
		init();

		select("as");  

		a("FROM Association as");  n();
		a("WHERE ");  n();
		a("	  as.id = '" + assoc_id +"'");  n(); 

		return query();
	}

	public OMElement getAssocById(List<String> assoc_ids)
	throws XdsException, LoggerException {
		init();

		select("ass");  

		a("FROM Association ass");  n();
		a("WHERE ");  n();
		a("	  ass.id IN "); a(assoc_ids);  n(); 

		return query();
	}

	private void select(String varName) {
		if (sqs.return_leaf_class)
			a("SELECT * ");
		else {
			a("SELECT "); a(varName); a(".id ");
		}
	}


	public OMElement getRpById(String rp_id, String identification_scheme)
	throws XdsException, LoggerException {
		init();

		select("rp");

		a("FROM RegistryPackage rp, ExternalIdentifier uniq");  n();
		a("WHERE ");  n();
		a("	  rp.id = '" + rp_id + "' AND");  n(); 
		a("   uniq.registryObject = rp.id AND");  n();
		a("   uniq.identificationScheme = '" + identification_scheme + "' ");  n();

		return query();
	}

	public OMElement getRpById(List<String> rp_id, String identification_scheme)
	throws XdsException, LoggerException {
		init();

		select("ss");

		a("FROM RegistryPackage ss, ExternalIdentifier uniq");  n();
		a("WHERE ");  n();
		a("	  ss.id IN "); a(rp_id); a(" AND");  n(); 
		a("   uniq.registryObject = ss.id");  n();
		if (identification_scheme != null) {
			a("  AND   uniq.identificationScheme = '" + identification_scheme + "' ");  n();
		}

		return query();
	}

	/**
	 * Load LeafClass form for all objects.  New Metadata object returned.  
	 * @param m - metadata to fill in with LeafClass objects. Unaltered.
	 * @return new Metadata object
	 * @throws LoggerException 
	 * @throws XdsException 
	 */
	public Metadata convertToLeafClass(Metadata metadata) throws XdsException, LoggerException {
		Metadata m = metadata.mkClone();
		List<String> objectRefIds = m.getObjectRefIds();
		// these could reference any type of object.  Only interested in:
		// RegistryPackage, ExtrinsicObject, Association

		List<String> idsToRemove = new ArrayList<String>();
		for (String id : objectRefIds) {
			if (m.containsObject(id))
				idsToRemove.add(id);
			if ( ! m.isUuid(id))
				throw new MetadataException("Cannot load object with id " + id + " from Registry, this id is not in UUID format (probabaly a symbolic name)");
		}

		objectRefIds.removeAll(idsToRemove);

		// nothing left to load
		if (objectRefIds.size() == 0)
			return m;

		m.clearObjectRefs();

		boolean discard_duplicates = true;
		boolean origLeafClass = sqs.return_leaf_class;

		sqs.return_leaf_class = true;

		m.addMetadata(getDocById(objectRefIds), discard_duplicates);
		m.addMetadata(getRpById(objectRefIds, null), discard_duplicates);
		m.addMetadata(getAssocById(objectRefIds), discard_duplicates);

		sqs.return_leaf_class = origLeafClass;
		return m;
	}

	public Metadata convertToObjectRefs(Metadata metadata, boolean ignoreExistingObjectRefs) throws XdsInternalException, MetadataException, MetadataValidationException {
		Metadata m = metadata.mkClone();

		if (ignoreExistingObjectRefs)
			m.clearObjectRefs();

		List<OMElement> leafClasses = m.getAllLeafClasses();
		List<String> ids = m.getIdsForObjects(leafClasses);
		m.mkObjectRefs(ids);		
		m.clearLeafClassObjects();

		return m;
	}

	public OMElement getObjectsByUuid(List<String> uuids) throws XdsException, LoggerException {
		if (uuids.size() == 0)
			return null;

		init();

		select("ro");

		a("FROM RegistryObject ro");  n();
		a("WHERE ");  n();
		a("	  ro.id IN "); a(uuids);  n();

		return query();
	}

	public OMElement getFolByUuid(String uuid) throws XdsException, LoggerException 
	{ return getRpById(uuid, "urn:uuid:75df8f67-9973-4fbe-a900-df66cefecc5a"); }

	public OMElement getFolByUuid(List<String> uuid) throws XdsException, LoggerException 
	{ return getRpById(uuid, "urn:uuid:75df8f67-9973-4fbe-a900-df66cefecc5a"); }


	public OMElement getFolByUid(String uid) throws XdsException, LoggerException 
	{ return getRpByUid(uid, "urn:uuid:75df8f67-9973-4fbe-a900-df66cefecc5a"); }

	public OMElement getFolByUid(List<String> uid) throws XdsException, LoggerException 
	{ return getRpByUid(uid, "urn:uuid:75df8f67-9973-4fbe-a900-df66cefecc5a"); }

	public OMElement getSsByUuid(String uuid) throws XdsException, LoggerException 
	{ return getRpById(uuid, "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8"); }


	public OMElement getSsByUid(String uid) throws XdsException, LoggerException 
	{ return getRpByUid(uid, "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8"); }

	public OMElement getRPDocs(String rp_uuid, List<String> format_codes, List<String> conf_codes)
	throws XdsException, LoggerException {
		init();

		select("doc");

		a("FROM ExtrinsicObject doc, Association a");  n();
		if (conf_codes != null) { a(", Classification conf");  n();  }
		if (format_codes != null){a(", Classification fmtCode");  n();  }
		if (false)     {          a(", Slot adomain");   }

		a("WHERE");   n();                                                                                     
		a("   a.sourceObject = '" + rp_uuid + "' AND");  n();                                                  
		a("   a.associationType = '"+ addAssociationTypeNamespace("HasMember") +"' AND");       n();                                                     
		a("   a.targetObject = doc.id ");   n();

		if (conf_codes != null && conf_codes.size() > 0) {
			a("  AND (");       n();
			a("   conf.classificationScheme = 'urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f' AND");  n();         
			a("   conf.classifiedObject = doc.id AND");             n();
			a("   conf.nodeRepresentation IN ");  a(conf_codes);  n();
			a(")");                         n();
		}

		if (format_codes != null && format_codes.size() > 0) {
			a("AND (");  n();
			a("  fmtCode.classifiedObject = doc.id AND");   n();  
			a("  fmtCode.classificationScheme = 'urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d' AND ");  n();
			a("  fmtCode.nodeRepresentation IN "); a(format_codes); n();
			a(" ) ");  n();
		}		
		if (false) {	
			a("  AND (adomain.name='AffinityDomain' AND adomain.parent=doc.id AND adomain.value=$affinitydomain) ");
		}

		return query();
	}

	public OMElement getRPDocs(String rp_uuid, SQCodedTerm format_codes, SQCodedTerm conf_codes)
	throws XdsException, LoggerException {
		init();

		select("doc");

		a("FROM ExtrinsicObject doc, Association a");  n();
		if (conf_codes != null) { a(declareClassifications(conf_codes));  n();  }
		if (format_codes != null){a(declareClassifications(format_codes));  n();  }
		if (false)     {          a(", Slot adomain");   }

		a("WHERE");   n();                                                                                     
		a("   a.sourceObject = '" + rp_uuid + "' AND");  n();                                                  
		a("   a.associationType = '"+ addAssociationTypeNamespace("HasMember") +"' AND");       n();                                                     
		a("   a.targetObject = doc.id ");   n();

		addCode(conf_codes);
		addCode(format_codes);
		if (false) {	
			a("  AND (adomain.name='AffinityDomain' AND adomain.parent=doc.id AND adomain.value=$affinitydomain) ");
		}

		return query();
	}

	public OMElement getFolDocs(String fol_uuid, List<String> format_codes, List<String> conf_codes) 
	throws XdsException, LoggerException {
		return getRPDocs(fol_uuid, format_codes, conf_codes);
	}

	public OMElement getFolDocs(String fol_uuid, SQCodedTerm format_codes, SQCodedTerm conf_codes) 
	throws XdsException, LoggerException {
		return getRPDocs(fol_uuid, format_codes, conf_codes);
	}

	public OMElement getSsDocs(String fol_uuid, List<String> format_codes, List<String> conf_codes) 
	throws XdsException, LoggerException {
		return getRPDocs(fol_uuid, format_codes, conf_codes);
	}

	public OMElement getSsDocs(String fol_uuid, SQCodedTerm format_codes, SQCodedTerm conf_codes) 
	throws XdsException, LoggerException {
		return getRPDocs(fol_uuid, format_codes, conf_codes);
	}

	public OMElement getRegistryPackageAssocs(List<String> package_uuids, List<String> content_uuids) throws XdsException,
	LoggerException {
		init();

		select("ass");

		a("FROM Association ass"); n();
		a("WHERE"); n();
		a("   ass.associationType = '"+ addAssociationTypeNamespace("HasMember") +"' AND"); n();
		a("   ass.sourceObject IN (");
		for (int i=0; i<package_uuids.size(); i++) {
			if (i > 0) a(",");
			a("'" + (String) package_uuids.get(i) + "'");
		}
		a(") AND"); n();
		a("ass.targetObject IN (" );
		for (int i=0; i<content_uuids.size(); i++) {
			if (i > 0) a(",");
			a("'" + (String) content_uuids.get(i) + "'");
		}
		a(")");
		n();

		return query();
	}

	public OMElement getRegistryPackageAssocs(List<String> package_uuids) throws XdsException,
	LoggerException {
		if (package_uuids == null || package_uuids.size() == 0)
			return null;

		init();

		select("ass");

		a("FROM Association ass"); n();
		a("WHERE"); n();
		a("   ass.associationType = '"+ addAssociationTypeNamespace("HasMember") +"' AND"); n();
		a("   ass.sourceObject IN (");
		for (int i=0; i<package_uuids.size(); i++) {
			if (i > 0) a(",");
			a("'" + (String) package_uuids.get(i) + "'");
		}
		a(")");
		n();

		return query();
	}

	public OMElement getFoldersForDocument(String uuid)  throws XdsException,
	LoggerException {
		init();

		select("fol");

		a("FROM RegistryPackage fol, ExternalIdentifier uniq, Association a");  n();
		a("WHERE"); n();

		a("   a.associationType = '"+ addAssociationTypeNamespace("HasMember") +"' AND"); n();
		a("   a.targetObject = '" + uuid + "' AND"); n();
		a("   a.sourceObject = fol.id AND"); n();
		a("   uniq.registryObject = fol.id AND");  n();
		a("   uniq.identificationScheme = '" + "urn:uuid:75df8f67-9973-4fbe-a900-df66cefecc5a" + "' ");  n();

		return query();
	}

	public OMElement get_submissionsets_of_contents(List<String> contents_uuids) throws XdsException,
	LoggerException {
		init();

		select("rp");

		a("FROM RegistryPackage rp, Association a, ExternalIdentifier ei"); n();
		a("WHERE ");  n();
		a("	  a.sourceObject = rp.id AND");  n(); 
		a("   a.associationType = '"+ addAssociationTypeNamespace("HasMember")+"' AND"); n();
		a("	  a.targetObject IN "); a(contents_uuids);  a(" AND"); n(); 
		a("   ei.registryObject = rp.id AND"); n();
		a("   ei.identificationScheme = 'urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446'"); n();

		return query(sqs.return_leaf_class);
	}
	
	private String addAssociationTypeNamespace(String type) {
		return MetadataSupport.association_type_namespace + type; 
	}
	private String addStatusTypeNamespace(String type) {
		return MetadataSupport.status_type_namespace + type; 
	}
	
	
	public OMElement get_associations(String type, List<String> froms, List<String> tos)
	throws XdsException, LoggerException {
		init();

		select("a");

		a("FROM Association a"); n();
		a("WHERE ");  n();
		a("  a.associationType = '" + addAssociationTypeNamespace(type) + "' AND"); n();
		a("  a.sourceObject IN"); a(froms); a(" AND"); n();
		a("  a.targetObject IN"); a(tos); n();


		return query(sqs.return_leaf_class);
	}

	public OMElement getSsFolders(String ss_uuid) throws XdsException,
	LoggerException {
		init();

		select("fol");

		a("FROM RegistryPackage fol, Association a"); n();
		a("WHERE"); n();
		a("   a.associationType = '"+ addAssociationTypeNamespace("HasMember") +"' AND"); n();
		a("   a.sourceObject = '" + ss_uuid + "' AND"); n();
		a("   a.targetObject = fol.id "); n();

		return query();
	}

	public List<String> validateApproved(List<String> uuids)  throws  XdsException,LoggerException {
		init();
		a("SELECT * FROM ExtrinsicObject eo"); n();
		a("WHERE"); n();
		a("  eo.status = '"+ addStatusTypeNamespace("Approved") +"' AND"); n();
		a("  eo.id IN "); a(uuids);  n();

		List<String> results1 = query_for_object_refs();

		init();
		a("SELECT * FROM RegistryPackage rp"); n();
		a("WHERE"); n();
		a("  rp.status = '"+ addStatusTypeNamespace("Approved") +"' AND"); n();
		a("  rp.id IN "); a(uuids);  n();

		List<String> results = query_for_object_refs();

		results.addAll(results1); 

		List<String> missing = null;
		for (int i=0; i<uuids.size(); i++) {
			String uuid = (String) uuids.get(i);
			if ( !results.contains(uuid)) {
				if (missing == null)
					missing = new ArrayList<String>();
				missing.add(uuid);
			}
		}
		return missing;
	}

	/**
	 * Validate uids found in metadata are proper. Uids of Folder and Submission Set may not
	 * be already present in registry.  Uid of DocumentEntry objects may be present if hash and
	 * size match. 
	 * @param metadata
	 * @throws MetadataValidationException - on all metadata errors
	 * @throws XdsNonIdenticalHashException - if uid found in registry with different hash 
	 * @throws LoggerException - on error writing to Test Log
	 * @throws XdsException - on low level interface errors
	 * @throws XMLParserException 
	 */
	public void validateProperUids(Metadata metadata)  throws LoggerException, XMLParserException, XdsException {

		// uid_hash is uid => hash (null for non documents)
		HashMap<String, List<String>> uid_hash = metadata.getUidHashMap();

		List<String> uids = new ArrayList<String>(); // all uids in metadata
		uids.addAll(uid_hash.keySet());

		List<String> uuids = getUuidForUid(uids);

		if (uuids.size() == 0)
			return;

		// at least one uniqueId is already present in the registry. If it is from a document
		// and the hash and size are the same then it is ok.  Otherwise it is an error.

		sqs.forceLeafClassQueryType();
		OMElement offendingObjects = getObjectsByUuid(uuids);   // LeafClass for offending objects
		sqs.restoreOriginalQueryType();

		if (offendingObjects == null) 
			throw new XdsInternalException("RegistryObjectValidator.validateProperUids(): could not retrieve LeafClass for ObjectRef obtained from registry: UUIDs were " + uuids);

		Metadata m = MetadataParser.parseNonSubmission(offendingObjects);
		List<String> dup_uids = new ArrayList<String>();
		HashMap<String, OMElement> dup_objects = m.getUidMap();
		dup_uids.addAll(dup_objects.keySet());


		sqs.log_message.addOtherParam("dup uuids", uuids.toString());
		sqs.log_message.addOtherParam("dup uids", dup_uids.toString());

		// Generate error messages that are object type specific
		dupUidErrorException(metadata, 
				dup_uids, 
				"SubmissionSet", 
				metadata.getSubmissionSetIds(), 
				MetadataSupport.XDSSubmissionSet_uniqueid_uuid);

		dupUidErrorException(metadata, 
				dup_uids, 
				"Folder", 
				metadata.getFolderIds(), 
				MetadataSupport.XDSFolder_uniqueid_uuid);

		HashMap<String, OMElement> docs_submit_uid_map = metadata.getUidMap(metadata.getExtrinsicObjects());
		for (String doc_uid : docs_submit_uid_map.keySet()) {
			if (dup_uids.contains(doc_uid)) {
				OMElement reg_obj = dup_objects.get(doc_uid);
				String type = reg_obj.getLocalName();
				if ( !type.equals("ExtrinsicObject")) 
					throw new MetadataValidationException("Document uniqueId " + 
							doc_uid + 
					" already present in the registry on a non-document object");
				OMElement sub_obj = docs_submit_uid_map.get(doc_uid);
				String sub_hash = m.getSlotValue(sub_obj, "hash", 0);
				String reg_hash = m.getSlotValue(reg_obj, "hash", 0);

				if (logger.isDebugEnabled()) {
					logger.debug("doc_uid " + doc_uid);
					logger.debug("sub_hash " + sub_hash);
					logger.debug("reg_hash " + reg_hash);
				}
				
				if (sub_hash != null && reg_hash != null && !sub_hash.equals(reg_hash))
					throw new XdsNonIdenticalHashException(
							"UniqueId " + doc_uid + " exists in both the submission and Registry and the hash value is not the same: " +
							"Submission Hash Value = " + sub_hash + " and " +
							"Registry Hash Value = " + reg_hash
					);

			}
		}

	}

	private void dupUidErrorException(Metadata metadata, List<String> dup_uids,
			String type, List<String> ids, String type_uniqueId_type)
	throws MetadataException, MetadataValidationException {
		for (String suuid : ids) {
			String sid = metadata.getExternalIdentifierValue(suuid, type_uniqueId_type);
			if (dup_uids.contains(sid)) {
				throw new MetadataValidationException(type + " uniqueId " + 
						sid + 
				" already present in the registry");

			}
		}
	}

	private List<String> getUuidForUid(List<String> uids)
	throws MetadataException, XdsException, XMLParserException, LoggerException {
		List<String> uid_id_schemes = new ArrayList<String>();
		uid_id_schemes.add(MetadataSupport.XDSFolder_uniqueid_uuid);
		uid_id_schemes.add(MetadataSupport.XDSSubmissionSet_uniqueid_uuid);
		uid_id_schemes.add(MetadataSupport.XDSDocumentEntry_uniqueid_uuid);

		init();
		a("SELECT ro.id from RegistryObject ro, ExternalIdentifier ei"); n();
		a("WHERE"); n();
		a(" ei.registryobject = ro.id AND "); n(); 
		a("  ei.identificationScheme IN "); a(uid_id_schemes); a(" AND"); n();
		a("  ei.value IN "); a(uids); n();

		// these uuids identify objects that carry one of the uids passed in in the map
		List<String> uuids = query_for_object_refs();
		return uuids;
	}


}
