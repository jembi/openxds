package gov.nist.registry.ws.test;

import gov.nist.registry.common2.MetadataTypes;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.AdhocQueryResponse;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.xml.SchemaValidation;
import gov.nist.registry.ws.serviceclasses.Xds;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import junit.framework.TestCase;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.openhealthtools.openxds.log.LogMessage;

public class XdsTest extends TestCase {

	public void test_response_schema() throws XdsInternalException {
		AdhocQueryResponse response = null;
		try {
			response = new AdhocQueryResponse(Response.version_2);
		} catch (XdsInternalException e) {
			System.out.println("Internal Error: " + e.getMessage());
		}
		response.add_warning("testing", "in my nose", "everywhere", (LogMessage)null);
		OMElement response_ele = response.getResponse();

		String schema_messages = null;
		try {
			schema_messages = SchemaValidation.validate(response_ele, MetadataTypes.METADATA_TYPE_R);
		} catch (Exception e) {
			fail("Schema Validation threw exception: " + e.getMessage() + "\n" + response_ele.toString());
		}
		if (schema_messages != null && schema_messages.length() > 0)
			fail("Schema messages: " + schema_messages);
	}

	public void test_1() {
		OMElement x = null;
		Xds xds = new Xds();
		OMElement y = xds.echo(x);
		assertTrue(y == x);
	}

	public void test_backend_sql_query() throws XdsInternalException {
		String filename = "/Users/bill/iheos/workspace_prod/xds/testdata/sql_query_1.xml";
		OMElement documentElement = parse_xml_file(filename);
		assertTrue("Root is " + documentElement.getLocalName(), documentElement.getLocalName().equals("AdhocQueryRequest"));

//TODO: 		
//		HttpClientInfo info;
//		info = new HttpClientInfo();
//		info.setRestHost("localhost");
//		info.setRestPort(9080);
//		info.setRestService("/ebxmlrr/registry/rest");
//
//		HttpClientBean httpBean;
//		httpBean = new HttpClientBean();
//		httpBean.setHttpClientInfo(info);
//		httpBean.setMetadata(documentElement.toString());

//		String response_string = httpBean.getQueryResponse();
//
//		OMElement response = parse_xml_string(response_string);
//		assertTrue("Response is '" + response.getLocalName() + "' but should be 'RegistryResponse'", response.getLocalName().equals("RegistryResponse"));
//
//		assertTrue("Status is '" + response.getAttributeValue(new QName("status")) + 
//				"' but should be 'Success'", response.getAttributeValue(new QName("status")).equals("Success"));

		//assertTrue("Response is " + response.toString(), false);
	}

	public void test_backend_submit() throws XdsInternalException {
		String filename = "/Users/bill/iheos/workspace_prod/xds/testdata/submit_1.xml";
		OMElement documentElement = parse_xml_file(filename);
		assertTrue("Root is " + documentElement.getLocalName(), documentElement.getLocalName().equals("SubmitObjectsRequest"));

//TODO:		
//		HttpClientInfo info;
//		info = new HttpClientInfo();
//		info.setRestHost("localhost");
//		info.setRestPort(9080);
//		info.setRestService("/ebxmlrr/registry/rest");
//
//		HttpClientBean httpBean;
//		httpBean = new HttpClientBean();
//		httpBean.setHttpClientInfo(info);
//		httpBean.setUsernamePassword("regop:regop");
//		httpBean.setMetadata(documentElement.toString());
//		httpBean.setReadOnly(false);

//		String response_string = httpBean.getQueryResponse();
//		if (response_string.length() == 0)
//			fail("empty response");

//		OMElement response = parse_xml_string(response_string);
//		assertTrue("Expected 'RegistryResponse' got '" + response.getLocalName() + "' instead", response.getLocalName().equals("RegistryResponse"));
//
//		assertTrue("Status is '" + response.getAttributeValue(new QName("status")) + 
//				"' but should be 'Success'", response.getAttributeValue(new QName("status")).equals("Success"));
	}

	private OMElement parse_xml_file(String filename) throws FactoryConfigurationError {
		File infile = new File(filename);

//		create the parser
		XMLStreamReader parser=null;

		try {
			parser = XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream(infile));
		} catch (XMLStreamException e) {
			assertTrue("Could not create XMLStreamReader from " + filename, false);
		} catch (FileNotFoundException e) {
			assertTrue("Could not find input file " + filename, false);
		}

//		create the builder
		StAXOMBuilder builder = new StAXOMBuilder(parser);

//		get the root element (in this case the envelope)
		OMElement documentElement =  builder.getDocumentElement();	
		assertFalse("No document element", documentElement == null);
		return documentElement;
	}

	OMElement parse_xml_string(String input_string) {
		byte[] ba = input_string.getBytes();

//		create the parser
		XMLStreamReader parser=null;

		try {
			parser = XMLInputFactory.newInstance().createXMLStreamReader(new ByteArrayInputStream(ba));
		} catch (XMLStreamException e) {
			assertTrue("Could not create XMLStreamReader from " + "input stream", false);
		}
//		create the builder
		StAXOMBuilder builder = new StAXOMBuilder(parser);

//		get the root element (in this case the envelope)
		OMElement documentElement =  builder.getDocumentElement();

		return documentElement;
	}
}
