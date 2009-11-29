package gov.nist.registry.common2.registry;

import gov.nist.registry.common2.exception.ExceptionUtil;
import gov.nist.registry.common2.io.Sha1Bean;

import java.util.List;
import java.util.Map;

import org.apache.axiom.om.OMElement;


public class RetrieveA {
	String uri = null;
	protected byte[] data = null;
	protected String content_type = null;
	protected Metadata m = null;
	StringBuffer errors = null;
	protected String id = null;
	Map<String, List<String>> header_fields;
	
	public Map<String, List<String>> getHeaderFields() {
		return this.header_fields;
	}

	
	public RetrieveA(String uri) {
		this.uri = uri;
	}

	public RetrieveA(String id, Metadata m) {
		this.m = m;
		this.id = id;
	}
	
	String parseContentType(String content_type) {
		if (content_type == null)
			return "";
		String[] parts = content_type.split(";");
		if (parts.length == 0 || parts.length == 1 || parts.length == -1)
			return content_type.trim();
		return parts[0].trim();
	}

	public byte[] retrieve() {
		if (this.uri != null) {
			try {
				//TODO: retrieve the document content
//				HttpClient hc = new HttpClient();
//				this.data = hc.httpGetBytes(this.uri);
//				this.content_type = parseContentType(hc.getContentType());
//				this.header_fields = hc.getHeaderFields();
			} 
			catch (Exception e) {
				errors().append(ExceptionUtil.exception_details(e, "Failed to read document from URI: " + this.uri));
			}
			return this.data;
		}
		else if (this.m != null && this.id != null) {
			try {
				//this.uri = this.extract_uri();
				this.uri = m.getURIAttribute(m.getObjectById(this.id));
			} catch (Exception e) {
				errors().append(e.getMessage() + "\n");
			}
			return retrieve();
		}
		else {
			errors().append("URI not set OR id and Metadata not set OR id not found in metadata - cannot retrieve document\n");
			return null;
		}
	}

//	private String extract_uri() throws MetadataException {
//		if (this.m == null) {
//			errors().append("Cannot extract URI from metadata, metadata not set\n");
//			return null;
//		}
//		if (this.id == null) {
//			errors().append("Cannot extract URI from metadata, id not set\n");
//			return null;
//		}
//		Vector<String> slots = new Vector<String>(25);
//		for (int i=0; i<25; i++) {
//			String part = m.getSlotValue(this.id, "URI", i);
//			if (part == null || part.equals(""))
//				break;
//			slots.add(part);
//		}
//		String uri =  assemble_uri(slots);
//		if (uri.length() == 0) {
//			errors().append("Cannot find URI slot for document " + this.id);
//			return null;
//		}
//		return uri;
//	}
	
	public String get_uri() {
		return this.uri;
	}
	
//	private String assemble_uri(Vector<String> slots) throws MetadataException {
//		StringBuffer buf = new StringBuffer();
//		Vector<String> parts = new Vector<String>(25);
//		for (int i=0; i<25; i++) parts.add(null);
//
//		
//		for (int i=0; i<slots.size(); i++) {
//			String slot_val = slots.get(i);
//			if (slot_val == null)
//				break;
//			String[] pieces = slot_val.split("\\|");
//			System.out.println("pieces length = " + pieces.length);
//			if (i == 0 && pieces.length == 1) 
//				return slot_val;
//			if (pieces.length != 2)
//				throw new MetadataException("Cannot parse URI attribute value " + slot_val + " must contain two parts separated by the '|' character");
//			int index = 0;
//			try {
//				index = Integer.parseInt(pieces[0]);
//			} catch (Exception e) {
//				throw new MetadataException("Cannot parse URI attribute value " + slot_val + " , value before '|' must be integer");
//			}
//			if (index >= 25)
//				throw new MetadataException("Cannot parse URI attribute value " + slot_val + " , ordering prefix <" + index + "> is not reasonable");
//			parts.set(index, pieces[1]);
//		}
//		
//		for (int i=1; i<parts.size(); i++) {
//			String piece = parts.get(i);
//			if (piece == null)
//				break;
//			buf.append(piece);
//		}
//		
//		return buf.toString();
//	}
	
	public RetrieveA() { }
	
//	private void test_1() throws MetadataException {
//		Vector<String> v = new Vector<String>(25);
//		for (int i=0; i<25; i++) v.add(null);
//		
//		v.set(0, "http://nist1");
//		
//		String uri = assemble_uri(v);
//		if ( ! uri.equals("http://nist1")) { System.out.println("test_1 failed: " + uri); }
//		System.out.println("uri is " + uri);
//	}
//
//	private void test_2() throws MetadataException {
//		Vector<String> v = new Vector<String>(25);
//		for (int i=0; i<25; i++) v.add(null);
//		
//		v.set(0, "1|http://");
//		v.set(1, "2|nist1");
//		
//		String uri = assemble_uri(v);
//		if ( ! uri.equals("http://nist1")) { System.out.println("test_2 failed: " + uri); }
//		System.out.println("uri is " + uri);
//	}
//
//	private void test_3() throws MetadataException {
//		Vector<String> v = new Vector<String>(25);
//		for (int i=0; i<25; i++) v.add(null);
//		
//		v.set(0, "1|http://");
//		v.set(1, "2|nist");
//		v.set(2, "3|1");
//		
//		String uri = assemble_uri(v);
//		if ( ! uri.equals("http://nist1")) { System.out.println("test_3 failed: " + uri); }
//		System.out.println("uri is " + uri);
//	}

	public byte[] retrieve_and_validate() {
		if (this.m == null) {
			errors().append("Cannot validate document, metadata not set\n");
			return data;
		}
		if (this.id == null) {
			errors().append("Cannot validate document, id not set\n");
			return data;
		}
		retrieve();
	
		validate();
	
		return data;
	}

	protected void validate() {
		if (this.data == null) {
			errors().append("No data\n");
			return;
		}
		
		String hash = null;
		int size = 0;
		OMElement eo = null;
	
		try {
			hash = m.getSlotValue(this.id, "hash", 0).toLowerCase();
			size = Integer.parseInt(m.getSlotValue(this.id, "size", 0));
			eo = m.getObjectById(this.id);
		} catch (Exception e) {
			errors().append(e.getMessage() + "\n");
		}
		if (eo == null) {
			errors().append("ExtrinsicObject with id " + id + " does not exist in metadata - cannot validate document\n");
			return;
		}
		String mime_type = eo.getAttributeValue(MetadataSupport.mime_type_qname);
	
		if (mime_type == null || mime_type.equals("")) {
			errors().append("Cannot extract mime_type from metadata\n");
			mime_type = "";
		}
	
		if ( !mime_type.equals(content_type)) {
			errors().append("Mime Type mismatch: metadata has " + mime_type + " and retrieve content type has " + this.content_type + "\n");
		}
	
		String data_hash = null;
		try {
			data_hash = sha1(this.data);
		} catch (Exception e) { errors().append("Error computing hash: " + e.getMessage() + "\n"); }
		if (data_hash == null || !data_hash.equals(hash)) {
			errors().append("Hash mismatch: metadata has " + hash + " and retrieve content has " + data_hash + "\n");
		}
	
		
		if (this.data != null && size != this.data.length) {
			errors().append("Size mismatch: metadata has " + size + " and retrieve content has " + this.data.length + "\n");
		}
	}

	public String get_content_type() {
		return content_type;
	}

	public byte[] get_content() {
		return data;
	}

	protected StringBuffer errors() {
		if (errors == null)
			errors = new StringBuffer();
		return errors;
	}

	public boolean has_errors() {
		return (errors != null);
	}

	public String get_errors() {
		if ( has_errors()) 
			return errors.toString();
		return null;
	}

	public String sha1(byte[] buf) throws Exception {
		Sha1Bean sb = new Sha1Bean();
		sb.setByteStream(buf);
		return sb.getSha1String();
	}

//	static public void main(String[] argv) {
//		RetrieveA ra = new RetrieveA();
//		try {
//			ra.test_1();
//			ra.test_2();
//			ra.test_3();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//	}


}
