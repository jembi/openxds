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
import org.openhealthtools.openxds.XdsConstants;
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
	
	public static XcaIG getActor() throws XdsInternalException {
		AxisServlet server = (AxisServlet)MessageContext.getCurrentMessageContext().getTransportIn().getReceiver();
		Collection<XcaIG> actors = (Collection<XcaIG>)server.getServletConfig().getServletContext().getAttribute(XdsConstants.IG_ACTORS);

		if (actors == null || actors.isEmpty() ) {
			throw new XdsInternalException("No XcaIG Actor is configured.");
		}
		
		if (actors.size() == 1) { 
			return actors.iterator().next();
		}

		//Select one that is most appropriate
		XcaIG ret = null;
		for (XcaIG actor : actors) {
			boolean isRegistrySecure = actor.getRegistryClientConnection() instanceof SecureConnection;
			boolean isReposiotrySecure = actor.getRepositoryClientConnection() instanceof SecureConnection;
			//TODO: revisit the logics to verify the secure actor
			boolean isSecureActor = isRegistrySecure && isReposiotrySecure;
			
			boolean isSecure = MessageContext.getCurrentMessageContext().getTo().toString().indexOf("https://") != -1;
			ret = actor;
			if ( isSecure && isSecureActor ||
				!isSecure && !isSecureActor) {
				    return actor;
				}
		} 
		return ret;
	}
}
