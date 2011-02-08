package gov.nist.registry.ws.serviceclasses;

import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsValidationException;
import gov.nist.registry.common2.exception.XdsWSException;
import gov.nist.registry.common2.registry.Metadata;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.RegistryErrorList;
import gov.nist.registry.common2.registry.RegistryResponse;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.RetrieveDocumentSetResponse;
import gov.nist.registry.common2.registry.XdsCommon;
import gov.nist.registry.common2.service.AppendixV;
import gov.nist.registry.ws.ContentValidationService;
import gov.nist.registry.ws.ProvideAndRegisterDocumentSet;
import gov.nist.registry.ws.RetrieveDocumentSet;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.openhealthtools.openexchange.syslog.LoggerException;

abstract public class AbstractRepository extends XdsService  implements ContentValidationService {
	boolean optimize_retrieve = true;
	String alternateRegistryEndpoint = null;

	public AbstractRepository() {
		super();
	}
	
	void optimize_retrieve(boolean opt) {
		optimize_retrieve = opt;
	}
	
	abstract protected String getPnRTransactionName();
	abstract protected void validateWS() throws XdsWSException;
	abstract protected short getXdsVersion(); 
	abstract protected void validateRetTransaction(OMElement rds) throws XdsValidationException;
	abstract protected void validatePnRTransaction(OMElement sor) throws XdsValidationException;
	abstract protected String getRetTransactionName();
	abstract public boolean runContentValidationService(Metadata m, Response response) throws MetadataException;
	abstract protected void validateRequest(OMElement rdsr, RegistryErrorList rel)  throws XdsValidationException;
	abstract public String getServiceName();

	protected void setAlternateRegistryEndpoint(String endpoint) {
		alternateRegistryEndpoint = endpoint;
	}
	
	public OMElement SubmitObjectsRequest(OMElement sor) throws AxisFault {
		return DocumentRepository_ProvideAndRegisterDocumentSet_b(sor);
	}

	public OMElement DocumentRepository_ProvideAndRegisterDocumentSet_b(OMElement sor) throws AxisFault {
		try {
			OMElement startup_error = beginTransaction(getPnRTransactionName(), sor, AppendixV.REPOSITORY_ACTOR);
			if (startup_error != null)
				return startup_error;
			if(log_message != null)	
				log_message.setTestMessage(getPnRTransactionName());

			validateWS();

			
			validatePnRTransaction(sor);
			
			ProvideAndRegisterDocumentSet s = new ProvideAndRegisterDocumentSet(log_message, getXdsVersion(), getMessageContext());

			if (s.xds_version == s.xds_b) {
				mustBeMTOM();
			}

			
			if (alternateRegistryEndpoint != null)
				s.setRegistryEndPoint(alternateRegistryEndpoint);
			
			OMElement result = s.provideAndRegisterDocumentSet(sor, this);

			endTransaction(s.getStatus());
			return result;
		} catch (Exception e) {
			return endTransaction(sor, e, AppendixV.REPOSITORY_ACTOR, "");
		}
	}
	
	public OMElement DocumentRepository_RetrieveDocumentSet(OMElement rdsr) throws AxisFault {
		try {
			OMElement startup_error = beginTransaction(getRetTransactionName(), rdsr, AppendixV.REPOSITORY_ACTOR);
			if (startup_error != null)
				return startup_error;
			if(log_message != null)
				log_message.setTestMessage(getRetTransactionName());
			
			validateWS();
			
			mustBeMTOM();

			validateRetTransaction(rdsr);
			
			OMNamespace ns = rdsr.getNamespace();
			String ns_uri =  ns.getNamespaceURI();
			if (ns_uri == null || ! ns_uri.equals(MetadataSupport.xdsB.getNamespaceURI())) {
				OMElement res = this.start_up_error(rdsr, "AbstractRepository.java", AppendixV.REPOSITORY_ACTOR, "Invalid namespace on RetrieveDocumentSetRequest (" + ns_uri + ")", true);
				endTransaction(false);
				return res;
			}
			
			RegistryErrorList rel = new RegistryErrorList(RegistryErrorList.version_3,  true /* log */);
			
			validateRequest(rdsr, rel);
			
			if (rel.has_errors()) {
				OMElement response = new RetrieveDocumentSetResponse(new RegistryResponse(RegistryErrorList.version_3, rel)).getResponse();
				addOther("Response", response.toString());
				endTransaction(false);
				return response;
			}


			RetrieveDocumentSet s = new RetrieveDocumentSet(log_message, XdsCommon.xds_b, getMessageContext());
			
			OMElement result = processRetrieveDocumentSet(s, rdsr);
			
			endTransaction(s.getStatus());
			return result;
		} catch (Exception e) {
			return endTransaction(rdsr, e, AppendixV.REPOSITORY_ACTOR, "");
		}
	}

	protected OMElement processRetrieveDocumentSet(RetrieveDocumentSet s, OMElement rdsr) throws XdsException, LoggerException {
		return s.retrieveDocumentSet(rdsr, this, true /* optimize */, this);
	}

}