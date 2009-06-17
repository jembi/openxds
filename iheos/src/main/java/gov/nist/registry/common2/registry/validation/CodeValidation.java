package gov.nist.registry.common2.registry.validation;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.Classification;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.RegistryErrorList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import org.apache.axiom.om.OMElement;

import com.misyshealthcare.connect.net.CodeSet;
import com.misyshealthcare.connect.net.IConnectionDescription;
import com.misyshealthcare.connect.net.Identifier;


//this gets invoked from both Validator.java and directly from Repository.  Should optimize the implementation so that codes.xml
//gets cached in memory.
public class CodeValidation {
	Metadata m;
	RegistryErrorList rel;
	boolean is_submit;
	boolean xds_b;
	IConnectionDescription connection;

	ArrayList<Identifier> assigning_authorities;
	HashMap<String, String> mime_map;  // mime => ext
	HashMap<String, String> ext_map;   // ext => mime

	public CodeValidation(Metadata m, boolean is_submit, boolean xds_b, 
			RegistryErrorList rel, IConnectionDescription connection) 
	throws XdsInternalException {
		this.m = m;
		this.rel = rel;
		this.is_submit = is_submit;
		this.xds_b = xds_b;
		this.connection = connection;

		loadCodes();
	}

	// this is used for easy access to mime lookup
	public CodeValidation(IConnectionDescription connection) throws XdsInternalException {
		this.connection = connection;
		loadCodes();
	}


	void loadCodes() throws XdsInternalException {
		assigning_authorities = new ArrayList<Identifier>();
		Identifier aa = connection.getIdentifier("AssigningAuthority");
		this.assigning_authorities.add(aa);
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
		CodeSet mimeTypeCodeSet = connection.getCodeSet("mimeType");		
		if (mimeTypeCodeSet == null) throw new XdsInternalException("CodeValidation.java: Configuration Error: Cannot find mime type table");

		mime_map = new HashMap<String, String>();
		ext_map = new HashMap<String, String>();

		Set<String> mimeTypes = mimeTypeCodeSet.getCodeSetKeys();
		for (String mimeType : mimeTypes) {
			String ext = mimeTypeCodeSet.getExt(mimeType);
			if (ext == null) throw new XdsInternalException("CodeValidation.java: Configuration Error: Cannot find ext for mime type:" + mimeType );
			mime_map.put(mimeType, ext);
			ext_map.put(ext, mimeType);
		}
	}

	public ArrayList<Identifier> getAssigningAuthorities() {
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
		for ( String codeType : connection.getAllCodeTypeNames()){
			CodeSet codeSet = connection.getCodeSet(codeType);
			String code_scheme = codeSet.getCodingScheme(code);
			if (codeSet.containsCode(code) &&
			     (code_scheme == null || code_scheme.equals(coding_scheme) )
			) {
				val("Coding of " + code_scheme, null);
				return;
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


