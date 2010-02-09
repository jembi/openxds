package gov.nist.registry.ws;

import gov.nist.registry.common2.MetadataTypes;
import gov.nist.registry.common2.exception.ExceptionUtil;
import gov.nist.registry.common2.exception.MetadataException;
import gov.nist.registry.common2.exception.SchemaValidationException;
import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.exception.XdsFormatException;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.logging.LogMessage;
import gov.nist.registry.common2.logging.LoggerException;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.registry.RegistryUtility;
import gov.nist.registry.common2.registry.RetrieveMultipleResponse;
import gov.nist.registry.common2.registry.XdsCommon;
import gov.nist.registry.common2.service.AppendixV;
import gov.nist.registry.ws.config.Repository;
import gov.nist.registry.ws.serviceclasses.XdsService;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMText;
import org.apache.axis2.context.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.common.audit.IheAuditTrail;
import org.openhealthtools.common.audit.ParticipantObject;
import org.openhealthtools.common.ihe.IheActor;
import org.openhealthtools.common.ws.server.IheHTTPServer;
import org.openhealthtools.openxds.XdsFactory;
import org.openhealthtools.openxds.repository.api.RepositoryException;
import org.openhealthtools.openxds.repository.api.RepositoryRequestContext;
import org.openhealthtools.openxds.repository.api.XdsRepositoryItem;
import org.openhealthtools.openxds.repository.api.XdsRepositoryService;

import com.misyshealthcare.connect.base.audit.ActiveParticipant;
import com.misyshealthcare.connect.base.audit.AuditCodeMappings;
import com.misyshealthcare.connect.net.IConnectionDescription;
import com.misyshealthcare.connect.util.Pair;

public class RetrieveDocumentSet extends XdsCommon {
    ContentValidationService validater;
    String registry_endpoint = null;
    MessageContext messageContext;
    boolean optimize = true;
    IConnectionDescription connection = null;
    /* The IHE Audit Trail for this actor. */
    private IheAuditTrail auditLog = null;
    private final static Log logger = LogFactory.getLog(RetrieveDocumentSet.class);

	public RetrieveDocumentSet(LogMessage log_message, short xds_version, MessageContext messageContext) {
        this.log_message = log_message;
        this.messageContext = messageContext;
		transaction_type = RET_transaction;
		IheHTTPServer httpServer = (IheHTTPServer) messageContext.getTransportIn().getReceiver();
        try {
            IheActor actor = httpServer.getIheActor();
            if (actor == null) {
                throw new XdsInternalException("Cannot find XdsRepository actor configuration.");
            }
            connection = actor.getConnection();
            if (connection == null) {
                throw new XdsInternalException("Cannot find Server connection configuration.");
            }
            auditLog = actor.getAuditTrail();
            init(new RetrieveMultipleResponse(), xds_version, messageContext);
        }

        catch (XdsInternalException e) {
            logger.fatal(logger_exception_details(e));
            response.add_error("XDSRepositoryError", e.getMessage(), ExceptionUtil.exception_details(e), log_message);
        }
    }

    public OMElement retrieveDocumentSet(OMElement rds, ContentValidationService validater, boolean optimize, XdsService service) throws SchemaValidationException, XdsInternalException {
        this.validater = validater;
        this.optimize = optimize;

        OMNamespace ns = rds.getNamespace();
        String ns_uri = ns.getNamespaceURI();
        if (ns_uri == null || !ns_uri.equals(MetadataSupport.xdsB.getNamespaceURI())) {
			return service.start_up_error(rds, "RetrieveDocumentSet.java", AppendixV.REPOSITORY_ACTOR, "Invalid namespace on RetrieveDocumentSetRequest (" + ns_uri + ")", true);
        }

        try {
            RegistryUtility.schema_validate_local(rds, MetadataTypes.METADATA_TYPE_RET);
        } catch (Exception e) {
			return service.start_up_error(rds, "RetrieveDocumentSet.java", AppendixV.REPOSITORY_ACTOR, "Schema validation errors:\n" + e.getMessage(), true);
        }

        ArrayList<OMElement> retrieve_documents = null;

        try {
            retrieve_documents = retrieve_documents(rds);
        }
        catch (XdsFormatException e) {
            response.add_error("XDSRepositoryError", "SOAP Format Error: " + e.getMessage(), RegistryUtility.exception_trace(e), log_message);
        }
        catch (MetadataException e) {
            response.add_error("XDSRepositoryError", "Request Validation Error:\n " + e.getMessage(), RegistryUtility.exception_details(e), log_message);
        }
        catch (XdsException e) {
            response.add_error("XDSRepositoryError", e.getMessage(), RegistryUtility.exception_details(e), log_message);
            logger.fatal(logger_exception_details(e));
        }


        OMElement registry_response = null;
        try {
            registry_response = response.getResponse();
        } catch (XdsInternalException e) {
            logger.fatal(logger_exception_details(e));
            try {
                log_message.addErrorParam("Internal Error", "Error generating response from Ret.b");
            }
            catch (LoggerException e1) {
                logger.fatal(logger_exception_details(e1));
            }
        }

        //OMElement rdsr = MetadataSupport.om_factory.createOMElement("RetrieveDocumentSetResponse", MetadataSupport.xds_b);

        //rdsr.addChild(registry_response);

        if (retrieve_documents != null)
            for (OMElement ret_doc : retrieve_documents)
                registry_response.addChild(ret_doc);

        this.log_response();

        return response.getRoot();
    }

    ArrayList<OMElement> retrieve_documents(OMElement rds) throws MetadataException, XdsException {
        ArrayList<OMElement> document_responses = new ArrayList<OMElement>();
        ArrayList<Pair> doclist = new ArrayList<Pair>();
        for (OMElement doc_request : MetadataSupport.childrenWithLocalName(rds, "DocumentRequest")) {
            //HashMap<String, String> docMap = new HashMap<String, String>();
            Pair doc = new Pair();
            String rep_id = null;
            String doc_id = null;
            String home = null;

            try {
                rep_id = MetadataSupport.firstChildWithLocalName(doc_request, "RepositoryUniqueId").getText();
                if (rep_id == null || rep_id.equals(""))
                    throw new Exception("");
            }
            catch (Exception e) {
                throw new MetadataException("Cannot extract RepositoryUniqueId from DocumentRequest");
            }

            try {
                doc_id = MetadataSupport.firstChildWithLocalName(doc_request, "DocumentUniqueId").getText();
                if (doc_id == null || doc_id.equals("")) throw new Exception("");
            }
            catch (Exception e) {
                throw new MetadataException("Cannot extract DocumentUniqueId from DocumentRequest");
            }

            OMElement home_ele = MetadataSupport.firstChildWithLocalName(doc_request, "HomeCommunityId");
            if (home_ele != null)
                home = home_ele.getText();
            doc._first = doc_id;
            doc._second = rep_id;
            doclist.add(doc);
            OMElement document_response = retrieve_document(rep_id, doc_id, home);

            if (document_response != null)
                document_responses.add(document_response);
        }
        
        auditLog(doclist, AuditCodeMappings.AuditTypeCodes.RetrieveDocumentSet);
        
        return document_responses;
    }

    OMElement retrieve_document(String rep_id, String doc_id, String home) throws XdsException {
        XdsRepositoryItem repositoryItem;
        XdsRepositoryService rm = XdsFactory.getXdsRepositoryService();
        if (!rep_id.equals(rm.getRepositoryUniqueId())) {
            response.add_error(MetadataSupport.XDSRepositoryWrongRepositoryUniqueId, "Repository Unique ID in request " +
                    rep_id +
                    " does not match this repository's id " +
                    Repository.getRepositoryUniqueId(),
                    RegistryUtility.exception_details(null), log_message);
            return null;
        }
        
        try {
            RepositoryRequestContext context = new RepositoryRequestContext();
            context.setConnection(connection);
            repositoryItem = rm.getRepositoryItem(doc_id, context);
        } catch (RepositoryException e) {
            throw new XdsException("Cannot find repository item for document id, " + doc_id);
        }
        
        if (repositoryItem == null || repositoryItem.getDataHandler() == null)
            throw new XdsException("Document is not found in Repository");

        OMText t = MetadataSupport.om_factory.createOMText(repositoryItem.getDataHandler(), optimize);
        System.out.println("OPTIMIZE IS " + optimize);
        t.setOptimize(optimize);
        OMElement document_response = MetadataSupport.om_factory.createOMElement("DocumentResponse", MetadataSupport.xdsB);

        if (home != null && !home.equals("")) {
            OMElement home_ele = MetadataSupport.om_factory.createOMElement("HomeCommunityId", MetadataSupport.xdsB);
            home_ele.addChild(MetadataSupport.om_factory.createOMText(home));
            document_response.addChild(home_ele);
        }
        OMElement repid_ele = MetadataSupport.om_factory.createOMElement("RepositoryUniqueId", MetadataSupport.xdsB);
        repid_ele.addChild(MetadataSupport.om_factory.createOMText(rep_id));
        document_response.addChild(repid_ele);

        OMElement docid_ele = MetadataSupport.om_factory.createOMElement("DocumentUniqueId", MetadataSupport.xdsB);
        docid_ele.addChild(MetadataSupport.om_factory.createOMText(doc_id));
        document_response.addChild(docid_ele);

        OMElement mimetype_ele = MetadataSupport.om_factory.createOMElement("mimeType", MetadataSupport.xdsB);
        mimetype_ele.addChild(MetadataSupport.om_factory.createOMText(repositoryItem.getMimeType()));
        document_response.addChild(mimetype_ele);

        OMElement document_ele = MetadataSupport.om_factory.createOMElement("Document", MetadataSupport.xdsB);
        document_ele.addChild(t);
        document_response.addChild(document_ele);


        return document_response;
    }

    /**
     * Audit Logging of PDQ Query Message.
     *
     * @param patients  the patients returned
     * @param hl7Header the message header from the request
     * @param queryTag  the query tag from the MSA segment of the PDQ request
     * @param qpd       the QPD segment of the PDQ request
     * @throws MetadataException
     */
    private void auditLog(ArrayList<Pair> doclist, AuditCodeMappings.AuditTypeCodes eventTypeCode) throws MetadataException {
        if (auditLog == null)
            return;

        String replyto = getMessageContext().getReplyTo().getAddress();
        String remoteIP = (String) getMessageContext().getProperty(MessageContext.REMOTE_ADDR);
        String localIP = (String) getMessageContext().getProperty(MessageContext.TRANSPORT_ADDR);

        ActiveParticipant source = new ActiveParticipant();
        source.setUserId(replyto);
        source.setAccessPointId(remoteIP);

        ActiveParticipant dest = new ActiveParticipant();
        dest.setAccessPointId(localIP);
        //TODO: Needs to be improved
        String userid = "http://" + connection.getHostname() + ":" + connection.getPort() + "/axis2/services/xdsrepositoryb";
        dest.setUserId(userid);
        //Document Info
        Collection<ParticipantObject> docs = new ArrayList<ParticipantObject>();
        for (Pair doc : doclist) {
            ParticipantObject docObj = new ParticipantObject();
            docObj.setId(doc._first.toString());
            docObj.setDetail(new Pair("RepositoryUniqueId", doc._second.toString()));
            docs.add(docObj);
        }
        //Finally Log it.
        auditLog.logRepositoryQuery(source, dest, docs, eventTypeCode);
    }


}
