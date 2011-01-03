package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.XdsInternalException;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.AxisServlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.openexchange.actorconfig.net.SecureConnection;
import org.openhealthtools.openexchange.config.PropertyFacade;
import org.openhealthtools.openxds.common.XdsConstants;
import org.openhealthtools.openxds.registry.api.XdsRegistry;
import org.openhealthtools.openxds.xca.api.XcaIG;

public class Ig {
	private final static Log logger = LogFactory.getLog(Ig.class);

    private static int numOfThreads = PropertyFacade.getInteger(XdsConstants.IG_THREADS_NUMBER, 5);
	static ExecutorService exec = Executors.newFixedThreadPool( numOfThreads ); 

	static String home = PropertyFacade.getString(XdsConstants.HOME_COMMUNITY_ID);
	
	public OMElement AdhocQueryRequest(OMElement ahqr) throws AxisFault {
		XcaRegistry reg = new XcaRegistry();
		
		return reg.DocumentRegistry_RegistryStoredQuery(ahqr);
	}
	
	public OMElement RetrieveDocumentSetRequest(OMElement rdsr) throws AxisFault {
		XcaRepository rep = new XcaRepository();
		
		return rep.DocumentRepository_RetrieveDocumentSet(rdsr);
	}
	
	public void setMessageContextIn(MessageContext inMessage) {
	}
	
}
