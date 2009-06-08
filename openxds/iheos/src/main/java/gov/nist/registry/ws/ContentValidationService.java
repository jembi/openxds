package gov.nist.registry.ws;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.Response;

public interface ContentValidationService {

	public boolean runContentValidationService(Metadata m, Response response) throws MetadataException;
}
