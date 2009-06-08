package gov.nist.registry.common2.registry.validation;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.XdsInternalException;

import gov.nist.registry.common2.io.Io;
import gov.nist.registry.common2.registry.Classification;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.RegistryErrorList;
import gov.nist.registry.common2.testsupport.TestConfig;
import gov.nist.registry.common2.xml.Util;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;


//this gets invoked from both Validator.java and directly from Repository.  Should optimize the implementation so that codes.xml
//gets cached in memory.
public class CodeValidation {
	Metadata m;
	RegistryErrorList rel;
	boolean is_submit;
	boolean xds_b;
	OMElement codes;
	ArrayList<String> assigning_authorities;
	HashMap<String, String> mime_map;  // mime => ext
	HashMap<String, String> ext_map;   // ext => mime

	public CodeValidation(Metadata m, boolean is_submit, boolean xds_b, RegistryErrorList rel) throws XdsInternalException {
		this.m = m;
		this.rel = rel;
		this.is_submit = is_submit;
		this.xds_b = xds_b;

		loadCodes();
	}

	// this is used for easy access to mime lookup
	public CodeValidation() throws XdsInternalException {
		loadCodes();
	}


	void loadCodes() throws XdsInternalException {
		String fileCodesLocation = System.getenv("XDSCodesFile");
		String localCodesLocation = "http://localhost:9080/xdsref/codes/codes.xml";
		String globalCodesLocation = "http://ihexds.nist.gov:9080/xdsref/codes/codes.xml";
		String codes_string = null;
		String from = null;

		if (fileCodesLocation != null) {
			try {
				codes_string = Io.getStringFromInputStream(new FileInputStream(new File(fileCodesLocation)));
				from = fileCodesLocation;
			}
			catch (Exception e) {
				throw new XdsInternalException("Env Var XDSCodesFile exists but codes.xml file cannot be loaded.", e);
			}
		}
		else {

			try {
				//TODO:
				//codes_string = HttpClient.httpGet(localCodesLocation);
				from = localCodesLocation;
			}
			catch (Exception e1) {
				try {
					//TODO:
					//codes_string = HttpClient.httpGet(globalCodesLocation);
					from = globalCodesLocation;
				}
				catch (Exception e) {
					throw new XdsInternalException("CodeValidation: Unable to retrieve code configuration file " + globalCodesLocation);
				}
			}
		}
		if (codes_string == null) 
			throw new XdsInternalException("CodeValidation.init(): GET codes.xml returned NULL from " + from);
		if (codes_string.equals("")) 
			throw new XdsInternalException("CodeValidation.init(): GET codes.xml returned enpty from " + from);

		if (TestConfig.verbose)
			System.out.println("Codes loaded from " + from);
		
		codes = Util.parse_xml(codes_string);
		if (codes == null)
			throw new XdsInternalException("CodeValidation: cannot parse code configuration file from " + from);

		assigning_authorities = new ArrayList<String>();
		for (OMElement aa_ele : MetadataSupport.childrenWithLocalName(codes, "AssigningAuthority")) 
		{
			this.assigning_authorities.add(aa_ele.getAttributeValue(MetadataSupport.id_qname));
		}

		build_mime_map();
	}

	public boolean isValidMimeType(String mime_type) {
		return mime_map.containsKey(mime_type);
	}

	public Collection<String> getKnownFileExtensions() {
		return ext_map.keySet();
	}

	public String getMimeTypeForExt(String ext) {
		return ext_map.get(ext);
	}

	public String getExtForMimeType(String mime_type) {
		return mime_map.get(mime_type);
	}

	private void build_mime_map() throws XdsInternalException {
		QName name_att_qname = new QName("name");
		QName code_att_qname = new QName("code");
		QName ext_att_qname = new QName("ext");
		OMElement mime_type_section = null;
		for(Iterator it=codes.getChildrenWithName(new QName("CodeType")); it.hasNext();  ) {
			OMElement ct = (OMElement) it.next();
			if (ct.getAttributeValue(name_att_qname).equals("mimeType")) {
				mime_type_section = ct;
				break;
			}
		}
		if (mime_type_section == null) throw new XdsInternalException("CodeValidation.java: Configuration Error: Cannot find mime type table");

		mime_map = new HashMap<String, String>();
		ext_map = new HashMap<String, String>();

		for(Iterator it=mime_type_section.getChildElements(); it.hasNext();  ) {
			OMElement code_ele = (OMElement) it.next();
			String mime_type = code_ele.getAttributeValue(code_att_qname);
			String ext = code_ele.getAttributeValue(ext_att_qname);
			mime_map.put(mime_type, ext);
			ext_map.put(ext, mime_type);
		}
	}

	public ArrayList<String> getAssigningAuthorities() {
		return assigning_authorities;
	}

	public void run() throws MetadataException, XdsInternalException {
		ArrayList<String> all_object_ids = m.getObjectIds(m.getAllObjects());

		for (String obj_id : all_object_ids) {
			ArrayList<OMElement> classifications = m.getClassifications(obj_id);

			for (OMElement cl_ele : classifications) {

				Classification cl = new Classification(cl_ele);
				validate(cl);
			}
		}

		for (OMElement doc_ele : m.getExtrinsicObjects()) {
			String mime_type = doc_ele.getAttributeValue(MetadataSupport.mime_type_qname);
			if ( !isValidMimeType(mime_type)) {
				err("Mime type, " + mime_type + ", is not available in this Affinity Domain");
			} else {
				val("Mime type " + mime_type, null);
			}

			String objectType = doc_ele.getAttributeValue(MetadataSupport.object_type_qname);
			if ( !objectType.equals(MetadataSupport.XDSDocumentEntry_objectType_uuid)) {
				err("XDSDocumentEntry has incorrect objectType, found " + objectType + ", must be " + MetadataSupport.XDSDocumentEntry_objectType_uuid);
			} else {
				val("XDSDocumentEntry.objectType", null);
			}
		}
	}



	void validate(Classification cl) {
		String classification_scheme = cl.getClassificationScheme();

		if (classification_scheme == null) {
			String classification_node = cl.getClassificationNode();
			if (classification_node == null || classification_node.equals("")) {
				err("classificationScheme missing", cl);
				return ;
			} else
				return;
		}
		if (classification_scheme.equals(MetadataSupport.XDSSubmissionSet_author_uuid))
			return;
		if (classification_scheme.equals(MetadataSupport.XDSDocumentEntry_author_uuid))
			return;
		String code = cl.getCodeValue();
		String coding_scheme = cl.getCodeScheme();

		if (code == null) {
			err("code (nodeRepresentation attribute) missing", cl);
			return ;
		}
		if (coding_scheme == null) {
			err("codingScheme (Slot codingScheme) missing", cl);
			return;
		}
		for (OMElement code_type : MetadataSupport.childrenWithLocalName(codes, "CodeType")) {
			String class_scheme = code_type.getAttributeValue(MetadataSupport.classscheme_qname);

			// some codes don't have classScheme in their definition
			if (class_scheme != null && !class_scheme.equals(classification_scheme))
				continue;

			for (OMElement code_ele : MetadataSupport.childrenWithLocalName(code_type, "Code")) {
				String code_name = code_ele.getAttributeValue(MetadataSupport.code_qname);
				String code_scheme = code_ele.getAttributeValue(MetadataSupport.codingscheme_qname);
				if ( 	code_name.equals(code) && 
						(code_scheme == null || code_scheme.equals(coding_scheme) )
				) {
					val("Coding of " + code_scheme, null);
					return;
				}
			}
		}
		val("Coding of " + coding_scheme, " (" + code + ") Not Found");
		err("The code, " + code + ", is not found in the configuration for the Affinity Domain", cl);
	}

	void val(String topic, String msg ) {
		if (msg == null) msg = "Ok";
		rel.add_validation(topic, msg, "CodeValidation.java");
	}

	void err(String msg, Classification cl) {
		rel.add_error(MetadataSupport.XDSRegistryMetadataError, cl.identifying_string() + ": " + msg, "CodeValidation.java", null);
	}

	void err(String msg) {
		rel.add_error(MetadataSupport.XDSRegistryMetadataError, msg, "CodeValidation.java", null);
	}


}


