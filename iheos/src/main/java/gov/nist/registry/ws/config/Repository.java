package gov.nist.registry.ws.config;

import gov.nist.registry.common2.exception.XdsConfigurationException;
import gov.nist.registry.common2.registry.Properties;
import gov.nist.registry.ws.serviceclasses.XdsService;

import org.apache.commons.httpclient.protocol.Protocol;
import org.openhealthtools.common.configuration.ModuleManager;
import org.openhealthtools.openxds.repository.api.IXdsRepositoryManager;

import com.misyshealthcare.connect.net.IConnectionDescription;
import com.misyshealthcare.connect.net.SecureConnectionDescription;
import com.misyshealthcare.connect.net.SecureSocketFactory;

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

	
	/**
	 * Get the Endpoint of this SOAP message.
	 * 
	 * @return the URL string of the web service
	 */
	static public String getRegisterTransactionEndpoint(IConnectionDescription connection) {
		String host = connection.getHostname();
		int port = connection.getPort();
		boolean isSecure = connection.isSecure();
		String url = "http://";
		if(isSecure) {
			url="https://";
			//TODO: need to use local rather than global setting
			Protocol.registerProtocol("https", new Protocol("https", 
			  new SecureSocketFactory((SecureConnectionDescription)connection), port));
		}
		 
		url+= host + ":" + port + connection.getUrlPath();
		return url;
	}
	
	static public String getRepositoryUniqueId() {
		IXdsRepositoryManager rm = ModuleManager.getXdsRepositoryManager();
		return rm.getRepositoryUniqueId();
	}

}
