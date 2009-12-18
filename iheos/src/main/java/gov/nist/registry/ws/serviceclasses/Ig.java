package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.Properties;
import gov.nist.registry.common2.soap.Soap;
import gov.nist.registry.ws.ContentValidationService;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.common.ihe.IheActor;
import org.openhealthtools.common.utils.ConnectionUtil;
import org.openhealthtools.common.ws.server.IheHTTPServer;
import org.openhealthtools.openxds.xca.api.XcaIG;

import com.misyshealthcare.connect.net.IConnectionDescription;

public class Ig extends XcaRegistry implements ContentValidationService{
	private final static Log logger = LogFactory.getLog(Ig.class);
	boolean optimize = true;
	static String homeProperty;
	String home;

	static {
		homeProperty = Properties.loader().getString("home.community.id");
	}
    IConnectionDescription connection = null;
    IConnectionDescription registryClientConnection = null;
    IConnectionDescription repositoryClientConnection = null;
    
	public Ig() {
		home = homeProperty;  // allows sub-classes to override
		IheHTTPServer httpServer = (IheHTTPServer)getMessageContext().getTransportIn().getReceiver();
		try {
			IheActor actor = httpServer.getIheActor();
			if (actor == null) {
				throw new XdsInternalException("Cannot find XcaIG actor configuration.");			
			}
			connection = actor.getConnection();
			if (connection == null) {
				throw new XdsInternalException("Cannot find XcaIG connection configuration.");			
			}
			registryClientConnection = ((XcaIG)actor).getRegistryClientConnection();
			if (registryClientConnection == null) {
				throw new XdsInternalException("Cannot find XcaIG XdsRegistryClient connection configuration.");			
			}
			repositoryClientConnection = ((XcaIG)actor).getRepositoryClientConnection();
			if (repositoryClientConnection == null) {
				throw new XdsInternalException("Cannot find XcaIG XdsRepositoryClient connection configuration.");			
			}
		} catch (XdsInternalException e) {
			logger.fatal("Internal Error creating RegistryResponse: " + e.getMessage());
		}
	}

	public OMElement AdhocQueryRequest(OMElement ahqr) {
		//XcaRegistry reg = new XcaRegistry();
		OMElement result = null;
		try {
			Protocol protocol = ConnectionUtil.getProtocol(registryClientConnection);
			String epr = ConnectionUtil.getTransactionEndpoint(registryClientConnection);
			Soap soap = new Soap();
			soap.soapCall(ahqr, protocol, epr, false, true, true, "urn:ihe:iti:2007:RegistryStoredQuery", null);
				
			result = soap.getResult();
         } catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return result;
	}
	
	public OMElement RetrieveDocumentSetRequest(OMElement rdsr) throws AxisFault {
		XcaRepository rep = new XcaRepository();
		
		return rep.RetrieveDocumentSetRequest(rdsr);
	}
	
	public void setMessageContextIn(MessageContext inMessage) {
	}

}
