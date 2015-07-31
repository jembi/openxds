package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.io.Io;
import gov.nist.registry.common2.xml.Util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.xml.parsers.FactoryConfigurationError;

import junit.framework.TestCase;

import org.apache.axiom.om.OMElement;

public class TestValidatorTest2 extends TestCase {
	File testFile;
	ZipFile zip;
	
	public void setUp() {
		testFile = new File("/Users/bill/dev/xds/testdata/testValidationSupport/zip/Archive.zip");
		try {
			zip = new ZipFile(testFile);
		} catch (ZipException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public void testSize() {
		List<ZipEntry> content = getContentEntries(zip);
		//showEntries(content);
		assertTrue("size is " + content.size() , content.size() == 2);
	}
	
	public void testTopElement() throws XdsInternalException, FactoryConfigurationError, IOException {
		List<ZipEntry> content = getContentEntries(zip);
		for (int i=0; i<content.size(); i++) {
			ZipEntry entry = content.get(i);
			String entryName = entry.toString();
			InputStream is = zip.getInputStream(entry);
			String entryString = Io.getStringFromInputStream(is);
			System.out.println("entryString = " + entryString.substring(0, min(entryString.length(), 25)));
			OMElement entryElement = Util.parse_xml(entryString);
			assertTrue("name is " + entryElement.getLocalName(),
					entryElement.getLocalName().equals("TestResults"));
		}
	}
	
	int min(int a, int b) { return (a>b) ? b : a; }
	
	void showEntries() {
		for (Enumeration<ZipEntry> en=(Enumeration<ZipEntry>) zip.entries(); en.hasMoreElements(); ) {
			ZipEntry entry = en.nextElement();
			String name = entry.getName();
			System.out.println("Entry: " + name);
		}
	}

	void showEntries(List<ZipEntry> entries) {
		for (int i=0; i<entries.size(); i++) {
			ZipEntry entry = entries.get(i);
			String name = entry.getName();
			System.out.println("Entry: " + name);
		}
	}

	List<ZipEntry> getContentEntries(ZipFile zip) {
		ArrayList<ZipEntry> contentEntries = new ArrayList<ZipEntry>();
		for (Enumeration<ZipEntry> en=(Enumeration<ZipEntry>) zip.entries(); en.hasMoreElements(); ) {
			ZipEntry entry = en.nextElement();
			String name = entry.getName();
			if (name.startsWith("__"))
				continue;
			contentEntries.add(entry);
		}
		return contentEntries;
	}

}
