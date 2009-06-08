package gov.nist.registry.common2.testsupport;

import gov.nist.registry.common2.exception.ExceptionUtil;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.io.Io;

import java.io.File;
import java.io.IOException;

public class SourceIdAllocator extends IdAllocator {

	public String allocate() throws XdsInternalException {
		try {
			return getFromFile();
		} catch (Exception e) {
			throw new XdsInternalException(ExceptionUtil.exception_details(e));
		}

	}
	
	String getFromFile() throws IOException {
		return Io.stringFromFile(new File(sourceId_file)).trim();
	}
}
