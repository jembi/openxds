package gov.nist.registry.ws.test;

import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.ws.DocumentTypes;
import junit.framework.TestCase;

import org.openhealthtools.openexchange.actorconfig.net.IConnectionDescription;


public class DocumentTypesTest extends TestCase {
    //TODO: Init connection
	private IConnectionDescription connection;
	
	public void test_file_extension() throws XdsInternalException {
		DocumentTypes dt = new DocumentTypes(connection);
		try {
			String file_ext = dt.fileExtension("text/plain");
			assert(file_ext.equals("txt"));
		}
		catch (Exception e) {
			fail();
		}
	}
	
	public void test_mime_type()  throws XdsInternalException {
		DocumentTypes dt = new DocumentTypes(connection);
		try {
			String mime_type = dt.mimeType("txt");
			assert(mime_type.equals("text/plain"));
		}
		catch (Exception e) {
			fail();
		}
	}

}
