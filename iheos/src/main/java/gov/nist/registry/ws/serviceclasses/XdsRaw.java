package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.XdsCommon;
import gov.nist.registry.common2.service.AppendixV;
import gov.nist.registry.ws.ContentValidationService;
import gov.nist.registry.ws.SubmitObjectsRequest;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.OperationContext;
import org.apache.axis2.wsdl.WSDLConstants;

public class XdsRaw extends XdsService implements
ContentValidationService 
{
	MessageContext inMesasgeContext = null;

	
	public void setOperationContext(OperationContext opCtx)
	throws AxisFault {
		inMesasgeContext = opCtx.getMessageContext(
				WSDLConstants.MESSAGE_LABEL_IN_VALUE);
	}
	
	public OMElement SubmitObjectsRequest(OMElement sor) {
		OMElement start_error = beginTransaction("xdsraw", sor, AppendixV.REGISTRY_ACTOR);
		if (start_error != null)
			return start_error;
		SubmitObjectsRequest s = new SubmitObjectsRequest(log_message, XdsCommon.xds_b, getMessageContext());
		s.setSubmitRaw(true);
		s.setContentValidationService(this);
		OMElement result = s.submitObjectsRequest(sor);
		endTransaction(s.getStatus());
		return result;
	}
	
	 public boolean runContentValidationService(Metadata m, Response response) {
		 return true;
	 }




}
