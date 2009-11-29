package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.service.AppendixV;
import gov.nist.registry.ws.EchoV2Metadata;
import gov.nist.registry.ws.EchoV3Metadata;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.OperationContext;
import org.apache.axis2.wsdl.WSDLConstants;

public class Xds extends XdsService {
	boolean pass;
	MessageContext inMesasgeContext = null;


	// This never gets called because we are using 
	// org.apache.axis2.receivers.RawXMLINOutMessageReceiver message receiver
	// which is too simple.  But, we cannot use
	// org.apache.axis2.rpc.receivers.RPCMessageReceiver because we need access
	// to the raw XML.  Maybe we can write a new message receiver that can do
	// both, later.
	public void setOperationContext(OperationContext opCtx)
	throws AxisFault {
		inMesasgeContext = opCtx.getMessageContext(
				WSDLConstants.MESSAGE_LABEL_IN_VALUE);
	}


	public OMElement echo(OMElement sor) {
		sor.build();
		return sor;
	}

//	public OMElement SubmitObjectsRequest(OMElement sor) {
//		try {
//			OMElement startup_error = begin_service("xds", sor, XdsService.registry_actor);
//			if (startup_error != null)
//				return startup_error;
//			log_message.setTestMessage("SOR");
//			SubmitObjectsRequest s = new SubmitObjectsRequest(log_message);
//			OMElement result = s.submitObjectsRequest(sor, true);
//			end_service();
//			return result;
//		} catch (Exception e) {
//			System.out.println("Exception: " + exception_details(e));
//			return null;
//		}
//	}

//	public OMElement ProvideAndRegisterDocumentSetRequest(OMElement sor) {
//		try {
//			OMElement startup_error = begin_service("xdsb", sor, XdsService.repository_actor);
//			if (startup_error != null)
//				return startup_error;
//			log_message.setTestMessage("PnR");
//			ProvideAndRegisterDocumentSet s = new ProvideAndRegisterDocumentSet(log_message);
//			OMElement result = s.provideAndRegisterDocumentSet(sor);
//			end_service();
//			return result;
//		} catch (Exception e) {
//			System.out.println("Exception: " + exception_details(e));
//			return null;
//		}
//	}


	public OMElement AdhocQueryRequest(OMElement ahqr) {
//		OMElement startup_error = begin_service("xds", ahqr, XdsService.registry_actor);
//		if (startup_error != null)
//			return startup_error;
//		log_message.setTestMessage("AHQR");
//		AdhocQueryRequest a = new AdhocQueryRequest(log_message);
//		OMElement result = a.adhocQueryRequest(ahqr);
//		end_service();
//		return result;
		return null;
	}

	public OMElement EchoV2Metadata(OMElement req) {
		OMElement startup_error = beginTransaction("xds", req, AppendixV.REGISTRY_ACTOR);
		if (startup_error != null)
			return startup_error;
		EchoV2Metadata e = new EchoV2Metadata();
		OMElement result  = e.echo(req);
		endTransaction(e.getStatus());
		return result;
	}

	public OMElement EchoV3Metadata(OMElement req) {
		OMElement startup_error = beginTransaction("xds", req, AppendixV.REGISTRY_ACTOR);
		if (startup_error != null)
			return startup_error;
		EchoV3Metadata e = new EchoV3Metadata();
		OMElement result = e.echo(req);
		endTransaction(e.getStatus());
		return result;
	}



//	public OMElement sample_response(OMElement sr) {
//	AdhocQueryResponse response = null;
//	try {
//	response = new AdhocQueryResponse(Response.version_2);
//	} catch (XdsInternalException e) {
//	System.out.println("Internal Error: " + e.getMessage());
//	}
//	response.addWarning("testing", "in my nose", "everywhere");
//	return response.getResponse();

//	}

}
