package gov.nist.registry.ws.test;

import gov.nist.registry.common2.exception.ExceptionUtil;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.io.Io;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.xml.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.apache.axiom.om.OMElement;

public class RetrieveDocumentSetTest extends TestCase {
	HashMap<String, String> header_fields;
	String query_response;   

	String eol = "\r\n";

	StringBuffer parse_log;

	public enum MsgLevel { INFO (0), WARN (1), ERROR (2), FATAL (3), EXCEPT(4); 
	private final int value; 
	MsgLevel(int value) { this.value = value; }
	public boolean printable(MsgLevel l) { return l.value >= value; }
	};

	MsgLevel level = MsgLevel.ERROR;
	int error_count = 0;
	void info(String message) {	 if (level.printable(MsgLevel.INFO))   parse_log.append("\n[info ] " + message); }
	void warn(String message) {	 if (level.printable(MsgLevel.WARN))   parse_log.append("\n[warn ] " + message); }
	void error(String message) { if (level.printable(MsgLevel.ERROR)) {parse_log.append("\n[error] " + message); error_count++; }}
	void fatal(String message) throws Exception { if (level.printable(MsgLevel.EXCEPT)) {parse_log.append("\n[fatal] " + message); throw new Exception("See error log"); }}
	void except(Exception e) { parse_log.append("\n[Exception] " + ExceptionUtil.exception_details(e)); }
	void resetErrors() { error_count = 0; }
	boolean hasErrors() { return error_count != 0; }

	public String getLog() { return parse_log.toString(); }

	public RetrieveDocumentSetTest() {
		parse_log = new StringBuffer();
	}

	class NoMessage extends Message {
		String mtom_errors, xop_errors, errors;

		public NoMessage(String mtom_errors, String xop_errors, String errors) {
			super();

			this.mtom_errors = mtom_errors;
			this.xop_errors = xop_errors;
			this.errors = errors;
		}
	}

	public Message call(File sendSoapMessageFile, String host, String port, String service) throws Exception {

		try {
			OMElement sendMessage = Util.parse_xml(sendSoapMessageFile);

			basic_call(host, port, service, sendMessage.toString());

			if (query_response.length() == 0)
				fail("empty response");

			info("Response Message starts here");

			Message message = new Message(header_fields, query_response);
			info("Message is " + message.toString());

			resetErrors();
			XopMessage xop = null;
			try { xop = new XopMessage(message); } catch (Exception e) { }
			if ( xop != null && !hasErrors())
				return xop;

			StringBuffer xop_parse_log = parse_log;

			resetErrors();
			MtomMessage mtom = null;
			try { mtom = new MtomMessage(message); } catch (Exception e) { }
			if ( mtom != null && !hasErrors())
				return mtom;

			StringBuffer mtom_parse_log = parse_log;
			return new NoMessage(mtom_parse_log.toString(), xop_parse_log.toString(), "");


		} catch (XdsInternalException e) {
			fail("RetrieveDocumentSetTest:send() failed : " + ExceptionUtil.exception_details(e));
			return new NoMessage("","", ExceptionUtil.exception_details(e));
		}
	}
	private void basic_call(String host, String port, String service, String body)
	throws Exception {
		URL url;
		HttpURLConnection conn = null;
		OutputStream os = null;

		try {
			url = new URL("http", host, Integer.parseInt(port), service);
			conn = (HttpURLConnection) url.openConnection();

			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "text/html, text/xml");
			conn.setRequestProperty("Content-Type", "application/soap+xml; charset=UTF-8; action=\"urn:ihe:iti:2007:RetrieveDocumentSet\"");
			conn.connect();
			os = conn.getOutputStream();

			os.write(body.getBytes());

		} catch (Exception e) {  except(e); }

		InputStream in = null;

		try {
			String encoding =conn.getContentEncoding();
			if (encoding == null) {
				in = (FilterInputStream) conn.getInputStream();
			} else {
				Object o = conn.getContent();
				in = (FilterInputStream) o;
			}
		} catch (java.io.IOException e) {
			int code = conn.getResponseCode();
			//System.out.println("ERROR: code: " + String.valueOf(code) + " message: " + conn.getResponseMessage());
			InputStream is = conn.getErrorStream();
			if (is == null) {
				String msg = conn.getResponseMessage();
				URL url2 = conn.getURL();
				fatal("Error retieving content of " + url2.toString() + "; response was " + msg);
			} else {
				StringBuffer b = new StringBuffer();
				byte[] by = new byte[256];
				while ( is.read(by, 0, 256) > 0 )
					b.append(new String(by));  // get junk at end, should be sensitive to number of bytes read
				//System.out.println(new String(b));
				fatal("ERROR: HttpClient: code: " + String.valueOf(code) + " message: " + conn.getResponseMessage() +
						"\n" + new String(b) + "\n");
			}
		}





		Map<String, List<String>> x_header_fields = conn.getHeaderFields();
		header_fields = new HashMap<String, String>();
		Set<String> entrySet = x_header_fields.keySet();
//		int i = entrySet.size();
//		String x = "";
//		ArrayList<String> foo = new ArrayList<String>();
		for (String key : entrySet) {
			if (key == null) continue;
			List<String> values = x_header_fields.get(key);
			String value = values.get(0);
			header_fields.put(key.toLowerCase(), value);
//			x += key + "\t" + value + "\n";
		}

		query_response = Io.getStringFromInputStream(in);

		info("Start of raw data");
		info("Received headers: " + header_fields.toString());
		info("Received body: " + query_response);
		info("End of raw data");
	}

	class Header {
		String name;
		String value;
		HashMap<String, String> parms;
		boolean is_valid;

		public String toString() {
			String answer = name + ": " + value;
			if (parms != null)
				for (String parmname : parms.keySet()) {
					answer += "; " + parmname + "=" +  parms.get(parmname);
				}
			return answer;
		}

		public Header(String s) {
			parms = new HashMap<String, String>();
			is_valid = false;
			String h[] = s.split(":", 2);
			if (h.length != 2) return;
			name = h[0].trim().toLowerCase();
			String values[] = h[1].split(";");

			for (int i=0; i<values.length; i++) {
				if (i == 0) {
					value = values[i].trim();
					info("header " + name + " = " + value);
					is_valid = true;
					continue;
				}
				String  pieces[] = values[i].split("=");
				if (pieces.length == 2) {
					String parm_name = pieces[0].trim().toLowerCase();
					String parm_value = pieces[1].trim();
					if (parm_value.length() > 1 && parm_value.charAt(0) == '"' && parm_value.charAt(parm_value.length()-1) == '"') {
						parm_value = parm_value.substring(1, parm_value.length() - 1);
					}

					parms.put(parm_name, parm_value);
					info("\t" + parm_name + " = " + parm_value);
				}
			}
			info("New Header: " + toString());
		}

		public Header(String name, String parts) {
			this.name = name;
			parms = new HashMap<String, String>();
			is_valid = false;
			// all the data is in parts[0], strange
			String[] values = parts.split(";");
			for (int i=0; i<values.length; i++) {
				if (i == 0) {
					value = values[i].trim();
					info("header " + name + " = " + value);
					is_valid = true;
					continue;
				}
				String apart = values[i];
				if (apart != null) {
					String pieces[] = apart.split("=");
					if (pieces.length == 2) {
						String parm_name = pieces[0].trim().toLowerCase();
						String parm_value = pieces[1].trim();
						if (parm_value.length() > 1 && parm_value.charAt(0) == '"' && parm_value.charAt(parm_value.length()-1) == '"') {
							parm_value = parm_value.substring(1, parm_value.length() - 1);
						}

						parms.put(parm_name, parm_value);
						info("\t" + parm_name + " = " + parm_value);
					}
				}
			}
			info("New Header: " + toString());
		}
	}

	class Part {
		HashMap<String, Header> headers;
		String body;
		boolean is_valid = false;
		String content_id;

		public String toString() {
			String answer = "Part: \n";
			for (String headername : headers.keySet()) {
				answer += headers.get(headername).toString() + "\n";
			}
			return answer;
		}

		public Part(String input, int part_headers_start_at, int body_ends_at) {
			parse(input, part_headers_start_at, body_ends_at);
		}

		public Part(String input) {
			int start = 0;
			if (input.startsWith("HTTP")) {
				start = input.indexOf(eol) + eol.length();
				info("Skipping HTTP line");
			}
			parse(input, start, input.length()-1);
		}

		public Part(HashMap<String, String> in_headers, String body) {
			headers = new HashMap<String, Header>();

			for (String header_name : in_headers.keySet()) {
				String header_value = in_headers.get(header_name);
				addHeader(header_name, header_value);
			}

			warn("Part:");
			warn("in_headers: " + in_headers.toString());
			warn("headers: " + headers.values().toString());

			this.content_id = getHeaderValue("Content-ID");
			this.body = body;
			this.is_valid = true;
			warn("Body size is " + this.body.length());
			info("Part is valid");
		}

		private void parse(String input, int part_headers_start_at, int body_ends_at) {
			headers = new HashMap<String, Header>();

			if (input.indexOf(eol) == -1) {
				error("No eol characters found");
				return;
			}

			int part_headers_end_at = input.indexOf(eol+eol, part_headers_start_at);
			info("Found part headers ending at " + part_headers_end_at);
			if (part_headers_end_at == -1) return;
			int body_starts_at = part_headers_end_at + (eol+eol).length();
			info("Body start at " + body_starts_at);
			if (body_starts_at == -1) return;

			parseHeaders(input.substring(part_headers_start_at, part_headers_end_at));

			content_id = getHeaderValue("Content-ID");

			body = input.substring(body_starts_at, body_ends_at);

			is_valid = true;
			info("Part is valid");
		}

		void addHeader(String s) {
			Header h = new Header(s);
			if (h.is_valid)
				headers.put(h.name, h);
		}

		void addHeader(String s, String values) {
			Header h = new Header(s, values);
			if (h.is_valid)
				headers.put(h.name, h);
		}

		private void parseHeaders(String headers_text) {
			String headers[] = headers_text.split(eol);
			for (int i=0; i<headers.length; i++) {
				addHeader(headers[i]);
			}
		}

		public boolean hasHeader(String name) { return headers.get(name) != null; }

		public String getHeaderValue(String name) { 
			try {
				return headers.get(name.toLowerCase()).value;
			} catch (Exception e) { 
				return null; 
			}
		}

		public String getHeaderParmValue(String name, String parm) {
			try {
				Header hdr = headers.get(name.toLowerCase());
				String val = hdr.parms.get(parm.toLowerCase());
				return val;
			} catch (Exception e) {
				return null;
			}
		}

		public String getHeadersAsString() {
			return headers.toString();
		}
	}

	class Multipart {

		ArrayList<Part> parts;

		public String toString() {
			String answer = "Multipart: ";
			for (Part p : parts) {
				answer += "  " + p.content_id;
			}
			return answer;
		}

		public Multipart(String input, String boundary) {
			parts = new ArrayList<Part>();
			if ( !boundary.startsWith("--")) boundary = "--" + boundary;

			int index = 0;

			while(true) {
				int boundary_starts_at = input.indexOf(boundary, index);
				int next_boundary_starts_at = input.indexOf(boundary, boundary_starts_at + boundary.length());
				info("Found boundary starting at " + boundary_starts_at);
				if (boundary_starts_at == -1) break;

				int part_headers_start_at = endOfLine(input, boundary_starts_at);
				info("Found part headers starting at " + part_headers_start_at);
				if (part_headers_start_at == -1) break;

				info("Next boundary starts at " + next_boundary_starts_at);
				if (next_boundary_starts_at == -1) break;
				int body_ends_at = next_boundary_starts_at - 1;
				info("Body ends at " + body_ends_at);

				Part part = new Part(input, part_headers_start_at, body_ends_at);
				if (part.is_valid)
					parts.add(part);

				index = next_boundary_starts_at;
			}

		}

		public Part getPart(int i) { return parts.get(i); }

		public Part getPartByContentId(String id) {
			id = "<" + id + ">";
			for (int i=0; i<parts.size(); i++) {
				Part part = parts.get(i);
				if (part.content_id != null && part.content_id.equals(id))
					return part;
			}
			return null;
		}

	}

	class Message {
		Part all;
		String boundary;
		Multipart mp;
		String start;
		Part start_part;

		public Message() {
		}

		public Message(Message m) {
			all = m.all;
			boundary = m.boundary;
			mp = m.mp;
			start = m.start;
			start_part = m.start_part;
		}

		public String toString() {
			String answer = "Message:" 
				+ "\n\tBoundary = " + boundary 
				+ "\n\tMultipart = " + ((mp == null) ? "null" : mp.toString())
				+ "\n\tStart = " + start 
				+ "\n\tall = " + all.toString();
			return answer;
		}

		public Message(String input) { parse(input); }

		public Message(String input, String _eol) { parse(input, _eol); }

		public Message(HashMap<String, String> headers, String body) {   
			info("Parsing entire message using Part algorithm");
			all = new Part(headers, body);
			warn("Created Message from Part - part is " + all.toString());

			parseAsMultipart(body);
		}

		public void parse(String input, String _eol) { eol = _eol; parse(input); }

		public void parse(String input) {
			info("Parsing entire message using Part algorithm");
			all = new Part(input);

			parseAsMultipart(input);
		}

		public void parseAsMultipart(String input) {
			boundary = all.getHeaderParmValue("Content-Type", "boundary");

			mp = (boundary != null) ? new Multipart(input, boundary) : null;

			if (boundary != null && mp.parts.size() == 0) {
				error("Message has a boundary header but parser found no Parts");
				mp = null;
			}

			if (mp != null) {
				start = all.getHeaderParmValue("Content-Type", "start");
				info("Start is " + start);

				if (start != null) {
					start_part = mp.getPartByContentId(start);
					if (start_part == null) 
						error("Header has start parameter but no matching Part was found");
					else
						info("Start Part found by start parameter in header");
				} else {
					start_part = mp.getPart(0);
					info("First Part used as start part");
				}

			}


			if (mp == null) info("Message is not a mulitpart");
			else info("Message is a multipart with " + mp.parts.size() + " parts");
		}

		public String getBody() {
			if (mp == null) 
				return all.body;
			else 
				return start_part.body;
		}

		public Part getPartByContentId(String id) {
			return mp.getPartByContentId(id);
		}
	}

	class MtomMessage extends Message  {

		public MtomMessage(String input) throws Exception {
			super(input);

			validate();
		}

		public MtomMessage(String input, String _eol) throws Exception  {
			super(input, _eol);

			validate();
		}

		public MtomMessage(Message m) throws Exception {
			super(m);

			validate();
		}

		private void validate() throws Exception {
			if (boundary != null || mp != null) {
				fatal("MTomMessage: should not have a boundary part in Content-Type header; Should not parse as multi-part");
			} else {
				String body = getBody();
				OMElement soap_message = null;
				try {  soap_message = Util.parse_xml(body); }
				catch (Exception e) { fatal("MtomMessage: SOAP message does not parse as XML"); }

				List<OMElement> soapBodies = MetadataSupport.decendentsWithLocalName(soap_message, "RetrieveDocumentSetResponse");
				if (soapBodies.size() > 0) {
					List<OMElement> documents = MetadataSupport.decendentsWithLocalName(soapBodies.get(0), "Document");
					for (OMElement document : documents) {
						List<OMElement> includes = MetadataSupport.childrenWithLocalName(document, "Include");
						if (includes.size() != 0) 
							error("MtomMessage: should not have Include element inside Document element - looks like MTOM/XOP instead of MTOM");
					}
				}
			}
		}
	}

	class XopMessage extends Message {

		public XopMessage(String input) throws Exception {
			super(input);

			validate();
		}

		public XopMessage(String input, String _eol) throws Exception {
			super(input, _eol);

			validate();
		}

		public XopMessage(Message m) throws Exception {
			super(m);

			validate();
		}


		private void validate() throws Exception {
			if (boundary == null || mp == null) {
				fatal("XopMessage: should have a boundary part in Content-Type header and should parse as multi-part");
			} else {
				String body = getBody();
				OMElement soap_message = null;
				try {  soap_message = Util.parse_xml(body); }
				catch (Exception e) { fatal("XopMessage: SOAP message does not parse as XML"); }

				List<OMElement> soapBodies = MetadataSupport.decendentsWithLocalName(soap_message, "RetrieveDocumentSetResponse");
				if (soapBodies.size() > 0) {
					List<OMElement> documents = MetadataSupport.decendentsWithLocalName(soapBodies.get(0), "Document");
					for (OMElement document : documents) {
						List<OMElement> includes = MetadataSupport.childrenWithLocalName(document, "Include");
						if (includes.size() == 0) 
							fatal("XopMessage: should have Include element inside Document element - looks like MTOM instead of MTOM/XOP");
						else if (includes.size() > 1)
							fatal("XopMessage: should have only one Include element inside Document element.");
						OMElement include = includes.get(0);
						String href = include.getAttributeValue(new QName("href"));
						if (href == null || href.equals(""))
							fatal("XopMessage: no href attribute inside Include element");
						if (href.startsWith("cid:"))
							href = href.replaceFirst("cid:", "");
						else
							error("XopMessage: href must start with 'cid'");
						Part doc = this.getPartByContentId(href);
						if (doc == null)
							error("XopMessage: Part with content id of " + href + " not found in message");
					}
				}
			}
		}
	}



	int endOfLine(String line, int fromIndex) {
		int nexteol = line.indexOf(eol, fromIndex);
		if (nexteol == -1) return -1;
		return  nexteol + eol.length();
	}

	public void xtest_parseMultipart1() {
		String boundary = "abc";
		String in = "";
		Multipart mp = new Multipart(in, boundary);
		assertTrue(mp.parts.size() == 0);
	}

	public void xtest_parseMultipart2() {
		String boundary = "abc";
		String boundary2 = "--" + boundary;
		String in = boundary2 + eol + "a: aa" + eol + eol + "the body" + boundary2;
		Multipart mp = new Multipart(in, boundary);
		assertTrue(mp.parts.size() == 1);
		assertTrue(mp.getPart(0).hasHeader("a"));
		assertFalse(mp.getPart(0).hasHeader("v"));
	}

	public void xtest_parseMultipart3() {
		String boundary = "abc";
		String boundary2 = "--" + boundary;
		String in = boundary2 + eol + "a: aa" + eol + eol + "the body" + boundary2 + eol;
		Multipart mp = new Multipart(in, boundary);
		assertTrue(mp.parts.size() == 1);
	}

	public void xtest_parseMultipart4() {
		String boundary = "abc";
		String boundary2 = "--" + boundary;
		String in = boundary2 + eol + "a: aa" + eol + "b: aa" + eol + eol + "the body\nmore body" + boundary2;
		Multipart mp = new Multipart(in, boundary);
		assertTrue(mp.parts.size() == 1);
	}

	public void xtest_parseMultipart5() {
		String boundary = "abc";
		String boundary2 = "--" + boundary;
		eol = "\n";
		String in = boundary2 + eol + "a: aa" + eol + "b: aa" + eol + eol + "the body\nmore body" + boundary2;
		Multipart mp = new Multipart(in, boundary);
		assertTrue(mp.parts.size() == 1);
	}

	public void xtest_parseMultipart6() {
		String boundary = "abc";
		String boundary2 = "--" + boundary;
		String in = "" +
		boundary2 + eol + 
		"a: aa" + eol + 
		"b: aa" + eol + 
		eol + 
		"the body\nmore body" 
		+ boundary2 +
		"a: aa" + eol + 
		"b: aa" + eol + 
		eol + 
		"the body\nmore body" 
		+ boundary2
		;
		Multipart mp = new Multipart(in, boundary);
		assertTrue(mp.parts.size() == 2);
	}

	public void xtest_parseMultipart7() throws Exception {
		String boundary = "MIMEBoundaryurn_uuid_3BE45FAC62CF0568A81199380869988";
		String boundary2 = "--" + boundary;
		eol = "\n";
		String in = Io.getStringFromInputStream(new FileInputStream(new File("/Users/bill/dev/xds/testdata/RetrieveDocumentSet/sample1.txt")));
		Multipart mp = new Multipart(in, boundary);
		assertTrue("found " + mp.parts.size() + "  parts - expecting 2\n" + getLog(), mp.parts.size() == 2);
	}

	public void xtest_parseMultipart8() throws Exception {
		eol = "\n";
		String in = Io.getStringFromInputStream(new FileInputStream(new File("/Users/bill/dev/xds/testdata/RetrieveDocumentSet/sample1.txt")));

		Part all = new Part(in);
		String boundary = all.getHeaderParmValue("Content-Type", "boundary");

		assertNotNull(all.getHeadersAsString(), boundary);

		Multipart mp = new Multipart(in, boundary);
		assertTrue("found " + mp.parts.size() + "  parts - expecting 2\n" + getLog(), mp.parts.size() == 2);
	}

	public void xtest_parseMultipart9() throws Exception {
		resetErrors();
		String in = Io.getStringFromInputStream(new FileInputStream(new File("/Users/bill/dev/xds/testdata/RetrieveDocumentSet/sample1.txt")));

		Message msg = new Message(in, "\n");

		System.out.println(this.parse_log.toString());

		assertTrue(msg.mp != null);
		assertTrue("found " + msg.mp.parts.size() + "  parts - expecting 2\n" + getLog(), msg.mp.parts.size() == 2);

		assertTrue(msg.start.equals("0.urn:uuid:3BE45FAC62CF0568A81199380869989@apache.org"));
		assertTrue(msg.mp.getPartByContentId("0.urn:uuid:3BE45FAC62CF0568A81199380869989@apache.org") != null);

		assertFalse(msg.start_part == null);

		assertFalse(hasErrors());
	}

	public void xtest_parseMultipart10() throws Exception {
		resetErrors();
		String in = Io.getStringFromInputStream(new FileInputStream(new File("/Users/bill/dev/xds/testdata/RetrieveDocumentSet/sample1.txt")));

		new XopMessage(in, "\n");

		System.out.println(this.parse_log.toString());
		assertFalse(hasErrors());
	}

	public void xtest_parseMultipart11() throws Exception {
		resetErrors();
		String in = Io.getStringFromInputStream(new FileInputStream(new File("/Users/bill/dev/xds/testdata/RetrieveDocumentSet/sample2.txt")));

		new MtomMessage(in, "\n");

		System.out.println(this.parse_log.toString());
		assertFalse(hasErrors());
	}

	public void xtest_parseMultipart12() throws Exception {
		resetErrors();
		String local = "";
		Message m = null;
		try {
			m = call(new File("/Users/bill/dev/xds/testdata/RetrieveDocumentSet/sample3.txt"), 
					"localhost", 
					"9080", 
			"/axis2xop/services/xdsrepositoryb");
		} catch (Exception e) {
			local = ExceptionUtil.exception_details(e);
		}

//		OutputStream os = new FileOutputStream("/Users/bill/tmp/out.txt");
//		PrintWriter pw = new PrintWriter(os);
		PrintStream pw = System.out;
		if (m instanceof NoMessage) {
			NoMessage n = (NoMessage) m;
			pw.println("\n\nXOP\n\n" + n.xop_errors);
			pw.println("\n\nMTOM\n\n" + n.mtom_errors);
			pw.println("\n\nOTHER\n\n" + n.errors);
		}
		if (! local.equals(""))
			pw.println("\n\nLOCAL\n\n" + local);
		pw.flush();
//		os.flush();
		pw.close();
//		os.close();


		assertTrue(m.getClass().getName(), m instanceof XopMessage);
	}

	public void test_parseMultipart13() throws Exception {
		resetErrors();
		String local = "";
		Message m = null;
		try {
			m = call(new File("/Users/bill/dev/xds/testdata/RetrieveDocumentSet/sample3.txt"), 
					"localhost", 
					"9080", 
			"/axis2/services/xdsrepositoryb");
		} catch (Exception e) {
			local = ExceptionUtil.exception_details(e);
		}

//		OutputStream os = new FileOutputStream("/Users/bill/tmp/out.txt");
//		PrintWriter pw = new PrintWriter(os);
		PrintStream pw = System.out;
		if (m instanceof NoMessage) {
			NoMessage n = (NoMessage) m;
			pw.println("\n\nXOP\n\n" + n.xop_errors);
			pw.println("\n\nMTOM\n\n" + n.mtom_errors);
			pw.println("\n\nOTHER\n\n" + n.errors);
		}
		if (! local.equals(""))
			pw.println("\n\nLOCAL\n\n" + local);
		pw.flush();
//		os.flush();
		pw.close();
//		os.close();


		assertTrue(m.getClass().getName(), m instanceof MtomMessage);
	}


}

