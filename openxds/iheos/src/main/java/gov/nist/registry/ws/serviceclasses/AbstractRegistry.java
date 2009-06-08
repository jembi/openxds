package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.XdsValidationException;
import gov.nist.registry.common2.exception.XdsWSException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.XdsCommon;
import gov.nist.registry.ws.AdhocQueryRequest;
import gov.nist.registry.ws.ContentValidationService;
import gov.nist.registry.ws.SubmitObjectsRequest;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;

public abstract class AbstractRegistry extends XdsService implements
ContentValidationService {

	abstract protected void validateWS(boolean isSQ) throws XdsWSException;
	abstract protected short getXdsVersion(); 
	abstract protected void validateQueryTransaction(OMElement sor) throws XdsValidationException;
	abstract protected void validateSubmitTransaction(OMElement sor) throws XdsValidationException;
	abstract public boolean runContentValidationService(Metadata m, Response response) throws MetadataException;
	abstract public String getServiceName();
	abstract protected void validateQueryInputDecoration(OMElement sor, AdhocQueryRequest a) throws XdsValidationException;
	abstract protected void decorateQueryOutput(OMElement sor, AdhocQueryRequest a, OMElement result) throws XdsValidationException ;

	protected String getRTransactionName(OMElement ahqr) {
		OMElement ahq = MetadataSupport.firstChildWithLocalName(ahqr, "AdhocQuery") ;
		OMElement sql = MetadataSupport.firstChildWithLocalName(ahqr, "SQLQuery") ;

		if (ahq != null)
			return "SQ";
		else if (sql != null)
			return "SQL";
		else if (ahqr.getLocalName().equals("SubmitObjectsRequest"))
			return "SubmitObjectsRequest";
		else
			return "Unknown";
	}
	
	protected boolean isSQ(OMElement ahqr) {
		return MetadataSupport.firstChildWithLocalName(ahqr, "AdhocQuery") != null;
	}

	protected boolean isSQL(OMElement ahqr) {
		return MetadataSupport.firstChildWithLocalName(ahqr, "SQLQuery") != null;
	}


	public OMElement SubmitObjectsRequest(OMElement sor) throws AxisFault {
		try {
			OMElement startup_error = beginTransaction(getRTransactionName(sor), sor, XdsService.registry_actor);
			if (startup_error != null)
				return startup_error;
			log_message.setTestMessage(getRTransactionName(sor));

			validateWS(false);

			validateSubmitTransaction(sor);

			SubmitObjectsRequest s = new SubmitObjectsRequest(log_message, getXdsVersion(), getMessageContext());
			OMElement result = s.submitObjectsRequest(sor, this);
			endTransaction(s.getStatus());
			return result;
		} catch (Exception e) {
			return endTransaction(sor, e, XdsService.registry_actor, "");
		}
	}

	public OMElement AdhocQueryRequest(OMElement ahqr) throws AxisFault {
		OMElement startup_error = beginTransaction(getRTransactionName(ahqr), ahqr, XdsService.registry_actor);
		if (startup_error != null)
			return startup_error;
		log_message.setTestMessage(getRTransactionName(ahqr));

		String type = getRTransactionName(ahqr);

		AdhocQueryRequest a = new AdhocQueryRequest(log_message, getMessageContext(), isSecure(), XdsCommon.xds_b);

		a.setServiceName(service_name);
		
		try {
			validateWS(type.equals("SQ"));

			validateQueryTransaction(ahqr);

			validateQueryInputDecoration(ahqr, a);

			OMElement result = a.adhocQueryRequest(ahqr);
			
			decorateQueryOutput(ahqr, a, result);
			
			endTransaction(a.getStatus());

			return result;
		} catch (Exception e) {
			return endTransaction(ahqr, e, XdsService.registry_actor, "");
		}
		
	}



}
