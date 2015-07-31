package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.xml.Util;

import java.io.File;

import junit.framework.TestCase;

import org.apache.axiom.om.OMElement;


public class TestValidatorTest extends TestCase {
	TestValidator tval;
	
	public void setUp() { tval = new TestValidator(false); }
	
	OMElement read(File file) throws XdsInternalException {
		return Util.parse_xml(file);
	}
	
	public void testXdstest1() throws Exception {
		tval.validate(read(new File("/Users/bill/dev/xds/testdata/testValidationSupport/xdstestExample1.xml")));
		assertTrue(tval.success);
	}

	public void testXdstest2() throws Exception {
		tval.validate(read(new File("/Users/bill/dev/xds/testdata/testValidationSupport/xdstestExample2.xml")));
		assertFalse(tval.success);
	}

	public void testXdstest3() throws Exception {
		tval.validate(read(new File("/Users/bill/dev/xds/testdata/testValidationSupport/xdstestExample3.xml")));
		assertFalse(tval.success);
	}

	public void testXdstest4() throws Exception {
		tval.validate(read(new File("/Users/bill/dev/xds/testdata/testValidationSupport/xdstestExample4.xml")));
		assertTrue(tval.success);
	}

	public void testXdstest5() throws Exception {
		tval.validate(read(new File("/Users/bill/dev/xds/testdata/testValidationSupport/xdstestExample5.xml")));
		assertFalse(tval.success);
	}

	public void testXdstest6() throws Exception {
		tval.validate(read(new File("/Users/bill/dev/xds/testdata/testValidationSupport/xdstestExample6.xml")));
		assertFalse(tval.success);
	}

	public void testXdstest7() throws Exception {
		tval.validate(read(new File("/Users/bill/dev/xds/testdata/testValidationSupport/xdstestExample7.xml")));
		assertTrue(tval.success);
	}

	public void testXdstest8() throws Exception {
		tval.validate(read(new File("/Users/bill/dev/xds/testdata/testValidationSupport/xdstestExample8.xml")));
		assertFalse(tval.success);
	}

	public void testXdstest9() throws Exception {
		tval.validate(read(new File("/Users/bill/dev/xds/testdata/testValidationSupport/xdstestExample9.xml")));
		assertFalse(tval.success);
	}
}
