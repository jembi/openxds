package gov.nist.registry.common2.testsupport;

import gov.nist.registry.common2.exception.ExceptionUtil;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.io.Io;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class UniqueIdAllocator extends IdAllocator {

	String getUniqueIdBase() throws IOException {
		return Io.stringFromFile(new File(uniqueid_base_file)).trim();
	}

	String getUniqueIdIndex() throws IOException {
		return Io.stringFromFile(new File(uniqueid_index_file)).trim();
	}

	void putUniqueIdIndex(String index) throws IOException {
		PrintStream ps = new PrintStream(new File(uniqueid_index_file));
		ps.print(index);
		ps.close();
	}

	String allocateUniqueId() throws IOException {
		String uniqueid_base = getUniqueIdBase();
		int uniqueid_index = Integer.parseInt(getUniqueIdIndex()) + 1;
		putUniqueIdIndex(String.valueOf(uniqueid_index));
		return uniqueid_base + uniqueid_index;
	}

	String allocate() throws XdsInternalException {
		try {
		return allocateUniqueId();
		} catch (Exception e) {
			throw new XdsInternalException(ExceptionUtil.exception_details(e));
		}
	}



}
