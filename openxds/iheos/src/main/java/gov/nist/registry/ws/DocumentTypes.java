package gov.nist.registry.ws;

import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.validation.CodeValidation;

import java.util.Collection;

public class DocumentTypes {
	CodeValidation cv;

	// the values in the class must also exist in tomcat1/conf/war.xml
	// documentation exists in xdsref/codes/codes.xml
	
	public DocumentTypes() throws XdsInternalException {
		cv = new CodeValidation();
	}

	public String mimeType(String file_ext)  {
		return cv.getMimeTypeForExt(file_ext);
	}

	public String fileExtension(String mime_type) {
		return cv.getExtForMimeType(mime_type);
	}

	public Collection<String> getFileTypes() {
		return cv.getKnownFileExtensions();
	}

}
