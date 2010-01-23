package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.XdsValidationException;
import gov.nist.registry.common2.exception.XdsWSException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.RegistryErrorList;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.XdsCommon;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;

public class RepositoryB extends AbstractRepository  {
	
	public RepositoryB() {
		super();
	}

	public boolean runContentValidationService(Metadata m, Response response) throws MetadataException{
		return true;
	}

	@Override
	public String getServiceName() {
		// TODO Auto-generated method stub
		return null;
	}

	protected String getPnRTransactionName() {
		return "PnR.b";
	}

	protected String getRetTransactionName() {
		return "RET.b";
	}
	
	protected void validateWS() throws XdsWSException {
		checkSOAP12();
//		if (isAsync())
//			throw new XdsWSException("Asynchronous web service request not acceptable on this endpoint" + 
//					" - replyTo is " + getMessageContext().getReplyTo().getAddress());
	}

	protected short getXdsVersion() {
		return XdsCommon.xds_b;
	}

	protected void validatePnRTransaction(OMElement sor) throws XdsValidationException {
		forceForcedError();
		OMNamespace ns = sor.getNamespace();
		String ns_uri =  ns.getNamespaceURI();
		if (ns_uri == null || ! ns_uri.equals(MetadataSupport.xdsB.getNamespaceURI())) 
			throw new XdsValidationException("Invalid namespace on " + sor.getLocalName() + " (" + ns_uri + ")");
	}

	protected void validateRetTransaction(OMElement rds) throws XdsValidationException {
		forceForcedError();
		OMNamespace ns = rds.getNamespace();
		String ns_uri =  ns.getNamespaceURI();
		if (ns_uri == null || ! ns_uri.equals(MetadataSupport.xdsB.getNamespaceURI())) 
			throw new XdsValidationException("Invalid namespace on " + rds.getLocalName() + " (" + ns_uri + ")");
	}

	protected void validateRequest(OMElement rdsr)
			throws XdsValidationException {
	}

	protected void validateRequest(OMElement rdsr, RegistryErrorList rel)
			throws XdsValidationException {
	}



}
