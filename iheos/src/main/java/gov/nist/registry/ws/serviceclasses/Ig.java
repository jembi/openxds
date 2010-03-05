package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.Properties;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.common.ihe.IheActor;
import org.openhealthtools.common.ws.server.IheHTTPServer;
import org.openhealthtools.openxds.xca.api.XcaIG;

import com.misyshealthcare.connect.net.IConnectionDescription;

public class Ig {
	private final static Log logger = LogFactory.getLog(Ig.class);

    private static int numOfThreads = Properties.loader().getInteger("ig.threads.number", 5);
	static ExecutorService exec = Executors.newFixedThreadPool( numOfThreads ); 

	static String home = XdsService.properties.getString("home.community.id");
	
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
	
	public static XcaIG getActor() {
		XcaIG actor = null;
		IheHTTPServer httpServer = (IheHTTPServer)MessageContext.getCurrentMessageContext().getTransportIn().getReceiver();
		try {
			actor = (XcaIG)httpServer.getIheActor();
			if (actor == null) {
				throw new XdsInternalException("Cannot find XcaIG actor configuration.");			
			}
		} catch (XdsInternalException e) {
			logger.fatal("Internal Error getting XcaIG actor configuration: " + e.getMessage());
		}
		return actor;
	}
}
