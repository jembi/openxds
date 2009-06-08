package gov.nist.registry.ws;

import gov.nist.registry.common2.exception.XdsInternalException;

import java.io.File;
import java.util.Collection;

public class RepositoryAccess  {

	String doc_id;
	Collection<String> file_types;
	File dir;
	String ext;

	public RepositoryAccess(String doc_id, File dir) throws XdsInternalException {
		this.doc_id = doc_id;
		file_types = (new DocumentTypes()).getFileTypes();
		this.dir = dir;
		this.ext = null;
	}

	public File find() {
		System.out.println("Known file types are: " + file_types);
		for (String ext : file_types) {
			File file = new File(dir, doc_id + "." + ext);
			if (file.exists()) {
				this.ext = ext;
				System.out.println("Repository File " + file.getPath() + " found");
				return file;
			} else
				System.out.println("Repository File " + file.getPath() + " does not exist");
		}
		return null;
	}
	
	public String getExt() {
		return this.ext;
	}

}
