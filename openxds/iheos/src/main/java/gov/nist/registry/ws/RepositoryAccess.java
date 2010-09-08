package gov.nist.registry.ws;

import gov.nist.registry.common2.exception.XdsInternalException;

import java.io.File;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.actorconfig.IActorDescription;


public class RepositoryAccess  {

	private static final Log log = LogFactory.getLog(RepositoryAccess.class);
	String doc_id;
	Collection<String> file_types;
	File dir;
	String ext;

	public RepositoryAccess(String doc_id, File dir, IActorDescription actorDescription) throws XdsInternalException {
		this.doc_id = doc_id;
		file_types = (new DocumentTypes(actorDescription)).getFileTypes();
		this.dir = dir;
		this.ext = null;
	}

	public File find() {
		log.info("Known file types are: " + file_types);
		for (String ext : file_types) {
			File file = new File(dir, doc_id + "." + ext);
			if (file.exists()) {
				this.ext = ext;
				log.info("Repository File " + file.getPath() + " found");
				return file;
			} else
				log.info("Repository File " + file.getPath() + " does not exist");
		}
		return null;
	}
	
	public String getExt() {
		return this.ext;
	}

}
