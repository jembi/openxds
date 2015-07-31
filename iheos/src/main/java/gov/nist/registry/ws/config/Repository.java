package gov.nist.registry.ws.config;

import gov.nist.registry.common2.exception.XdsConfigurationException;
import gov.nist.registry.ws.serviceclasses.XdsService;

import org.openhealthtools.openexchange.config.PropertyFacade;
import org.openhealthtools.openxds.XdsFactory;
import org.openhealthtools.openxds.repository.api.XdsRepositoryService;

public class Repository {
	static public String getBaseDirectory() {
		return PropertyFacade.getString("repository_base_dir");
	}

	static public String getBaseUri() throws XdsConfigurationException {
		return "http://" + 
		PropertyFacade.getString("repository_machine_name") + ":" + 
		PropertyFacade.getString("repository_port") + 
		PropertyFacade.getString("repository_base_uri");
	}

	// these need updating each year
	static public String getRegisterTransactionEndpoint(short xds_version) {
		if (xds_version == 3)
			return XdsService.registerBEndpoint;
		return XdsService.registerAEndpoint;
	}

	
//	/**
//	 * Get the Endpoint of this SOAP message.
//	 * 
//	 * @return the URL string of the web service
//	 */
//	static public String getRegisterTransactionEndpoint(IConnectionDescription connection) {
//		String host = connection.getHostname();
//		int port = connection.getPort();
//		boolean isSecure = connection.isSecure();
//		String url = "http://";
//		if(isSecure) {
//			url="https://";
//		}
//		 
//		url+= host + ":" + port + connection.getUrlPath();
//		return url;
//	}
	
	static public String getRepositoryUniqueId() {
		XdsRepositoryService rm = XdsFactory.getXdsRepositoryService();
		return rm.getRepositoryUniqueId();
	}

}
