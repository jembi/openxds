package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.XdsInternalException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.common.ws.server.IheHTTPServer;
import org.openhealthtools.openxds.xca.api.XcaIG;

import com.misyshealthcare.connect.net.IConnectionDescription;

public class Ig {
	private final static Log logger = LogFactory.getLog(Ig.class);

	private static XcaIG actor = null;

	static ExecutorService exec = Executors.newFixedThreadPool( 10 ); 
	static String home = XdsService.properties.getString("home.community.id");
	
	static Map<String, IConnectionDescription> rgQueryMap = Collections.synchronizedMap(new HashMap<String, IConnectionDescription>());
	static Map<String, IConnectionDescription> rgRetrieveMap = Collections.synchronizedMap(new HashMap<String, IConnectionDescription>());
        
	public OMElement AdhocQueryRequest(OMElement ahqr) throws AxisFault {
		XcaRegistry reg = new XcaRegistry();
		
		return reg.AdhocQueryRequest(ahqr);
	}
	
	public OMElement RetrieveDocumentSetRequest(OMElement rdsr) throws AxisFault {
		XcaRepository rep = new XcaRepository();
		
		return rep.RetrieveDocumentSetRequest(rdsr);
	}
	
	public void setMessageContextIn(MessageContext inMessage) {
	}
	
	synchronized static XcaIG getActor() {
		if (actor == null) {
			IheHTTPServer httpServer = (IheHTTPServer)MessageContext.getCurrentMessageContext().getTransportIn().getReceiver();
			try {
				actor = (XcaIG)httpServer.getIheActor();
				if (actor == null) {
					throw new XdsInternalException("Cannot find XcaIG actor configuration.");			
				}
			} catch (XdsInternalException e) {
				logger.fatal("Internal Error getting XcaIG actor configuration: " + e.getMessage());
			}
			
			//Init Responding Gateway Query map
			List<IConnectionDescription> rgQueryClientConnections = actor.getRGQueryClientConnections();
			if (rgQueryClientConnections == null || rgQueryClientConnections.size() == 0) {
				logger.warn("No XcaIG XcaRGQueryClient connection is configured");
			} else {
				for (IConnectionDescription rgQueryConnectgion : rgQueryClientConnections) {
					String homeId = rgQueryConnectgion.getProperty("homeId");
					if (homeId == null)
						logger.error("Missing the required homeId property in XcaRGQueryClient connection configuration: " + rgQueryConnectgion.getDescription());
					else
						rgQueryMap.put(homeId, rgQueryConnectgion);
				}
			}
			
			//Init Responding Gateway Retrieve map
			List<IConnectionDescription> rgRetrieveClientConnections = actor.getRGRetrieveClientConnections();
			if (rgRetrieveClientConnections == null || rgRetrieveClientConnections.size() == 0) {
				logger.warn("No XcaIG XcaRGRetrieveClient connection is configured");
			} else {
				for (IConnectionDescription rgRetrieveConnectgion : rgRetrieveClientConnections) {
					String homeId = rgRetrieveConnectgion.getProperty("homeId");
					if (homeId == null)
						logger.error("Missing the required homeId property in XcaRGRetrieveClient connection configuration: " + rgRetrieveConnectgion.getDescription());
					else
						rgRetrieveMap.put(homeId, rgRetrieveConnectgion);
				}
			}
			
		}			
		return actor;
	}
}
