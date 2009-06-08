package gov.nist.registry.ws.config;

import gov.nist.registry.common2.exception.XdsConfigurationException;
import gov.nist.registry.common2.registry.Properties;
import gov.nist.registry.ws.serviceclasses.XdsService;

import java.util.ArrayList;

public class Repository {
	static public String getBaseDirectory() {
		return Properties.loader().getString("repository_base_dir");
	}

	static public String getBaseUri() throws XdsConfigurationException {
		return "http://" + 
		Properties.loader().getString("repository_machine_name") + ":" + 
		Properties.loader().getString("repository_port") + 
		Properties.loader().getString("repository_base_uri");
	}

	// these need updating each year
	static public String getRegisterTransactionEndpoint(short xds_version) {
		if (xds_version == 3)
			return "http://localhost:9080/" + XdsService.technicalFramework + "/services/xdsregistryb";
		return "http://localhost:9080/" + XdsService.technicalFramework + "/services/xdsregistryainternal";
	}

	static public String getRepositoryUniqueId() {
		return Properties.loader().getString("repository_unique_id");
	}

}
