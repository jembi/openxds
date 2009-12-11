package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.XdsValidationException;
import gov.nist.registry.common2.registry.Properties;
import gov.nist.registry.common2.registry.RegistryErrorList;

import org.apache.axiom.om.OMElement;

public class XcaRepository extends RepositoryB {
	static String home = Properties.loader().getString("home.community.id");

	protected void validateRequest(OMElement rdsr, RegistryErrorList rel)
	throws XdsValidationException {
		
		new RG().verifyHomeOnRetrieve(rdsr, rel, home);
	}
}
