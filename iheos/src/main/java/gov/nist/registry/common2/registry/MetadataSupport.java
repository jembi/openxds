package gov.nist.registry.common2.registry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;

public class MetadataSupport {
	static public OMFactory om_factory = OMAbstractFactory.getOMFactory();

	static public String ebRSns2_uri = "urn:oasis:names:tc:ebxml-regrep:registry:xsd:2.1";
	static public OMNamespace ebRSns2 =  om_factory.createOMNamespace(ebRSns2_uri, "rs");

	static public String ebRIMns2_uri = "urn:oasis:names:tc:ebxml-regrep:rim:xsd:2.1";
	static public OMNamespace ebRIMns2 = om_factory.createOMNamespace(ebRIMns2_uri, "rim");

	static public String ebQns2_uri = "urn:oasis:names:tc:ebxml-regrep:query:xsd:2.1";
	static public OMNamespace ebQns2 =   om_factory.createOMNamespace(ebQns2_uri, "query");

	static public boolean isV2Namespace(String ns) { return ns != null && (ns.equals(ebRSns2_uri) || ns.equals(ebRIMns2_uri) || ns.equals(ebQns2_uri)); }
	static public boolean isV2Namespace(OMNamespace ns) { return isV2Namespace(ns.getNamespaceURI()); }

	static public String ebRSns3_uri = "urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0";
	static public OMNamespace ebRSns3 =  om_factory.createOMNamespace(ebRSns3_uri, "rs");

	static public String ebRIMns3_uri = "urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0";
	static public OMNamespace ebRIMns3 = om_factory.createOMNamespace(ebRIMns3_uri, "rim");

	static public String ebQns3_uri = "urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0";
	static public OMNamespace ebQns3 =   om_factory.createOMNamespace(ebQns3_uri, "query");

	static public String ebLcm3_uri = "urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0";
	static public OMNamespace ebLcm3 =   om_factory.createOMNamespace(ebLcm3_uri, "lcm");

	static public String xdsB_uri = "urn:ihe:iti:xds-b:2007";
	static public OMNamespace xdsB =   om_factory.createOMNamespace(xdsB_uri, "xdsb");

	static public String xdsB_eb_assoc_namespace_uri = "urn:oasis:names:tc:ebxml-regrep:AssociationType";
	static public String xdsB_ihe_assoc_namespace_uri = "urn:ihe:iti:2007:AssociationType";
	
	static public String ws_addressing_namespace_uri = "http://www.w3.org/2005/08/addressing";
	static public OMNamespace ws_addressing_namespace = om_factory.createOMNamespace(ws_addressing_namespace_uri, "wsa");
	static public String oasis_wsnb2_namespace_uri = "http://docs.oasis-open.org/wsn/b-2";
	static public OMNamespace oasis_wsnb2_namespace = om_factory.createOMNamespace(oasis_wsnb2_namespace_uri, "wsnt");



	static public boolean isV3Namespace(String ns) { return ns != null && (ns.equals(ebRSns3_uri) || ns.equals(ebRIMns3_uri) || ns.equals(ebQns3_uri) || ns.equals(ebLcm3_uri) || ns.equals(xdsB_uri)); }
	static public boolean isV3Namespace(OMNamespace ns) { return isV3Namespace(ns.getNamespaceURI()); }

	static public OMNamespace xml_namespace =   om_factory.createOMNamespace("http://www.w3.org/XML/1998/namespace", "xml");

	static public String status_type_namespace ="urn:oasis:names:tc:ebxml-regrep:StatusType:";
	static public String response_status_type_namespace = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:";
	static public String error_severity_type_namespace = "urn:oasis:names:tc:ebxml-regrep:ErrorSeverityType:";
	static public String association_type_namespace = "urn:oasis:names:tc:ebxml-regrep:AssociationType:";
													
	public static QName name_qname = new QName("Name");

	public static QName object_type_qname = new QName("objectType");

	public static QName return_type_qname = new QName("returnType");

	public static QName slot_name_qname = new QName("name");

	public static QName value_qname = new QName("value");

	public static QName identificationscheme_qname = new QName("identificationScheme");

	public static QName classificationscheme_qname   = new QName("classificationScheme");

	public static QName noderepresentation_qname = new QName("nodeRepresentation");

	public static QName classificationnode_qname = new QName("classificationNode");

	public static QName id_qname = new QName("id");

	public static QName registry_object_qname = new QName("registryObject");

	public static QName association_type_qname = new QName("associationType");

	public static QName source_object_qname = new QName("sourceObject");

	public static QName target_object_qname = new QName("targetObject");

	public static QName classified_object_qname = new QName("classifiedObject");

	public static QName mime_type_qname = new QName("mimeType");

	public static QName home_qname = new QName("home");

	public static QName code_qname = new QName("code");

	public static QName codingscheme_qname = new QName("codingScheme");

	public static QName classscheme_qname = new QName("classScheme");

	public static QName status_qname = new QName("status");

	public static QName severity_qname = new QName("severity");

	public static QName home_community_id_qname = new QName(xdsB.getNamespaceURI(), "HomeCommunityId");
	
	public static QName code_context_qname = new QName("codeContext");
	
	public static QName error_code_qname = new QName("errorCode");
	
	public static QName location_qname = new QName("location");
	
	public static QName expected_error_message_qname = new QName("ExpectedErrorMessage");

	public static OMNamespace soapns = om_factory.createOMNamespace("http://www.w3.org/2003/05/soap-envelope", "soapenv");
	public static OMNamespace wsans = om_factory.createOMNamespace("http://www.w3.org/2005/08/addressing", "wsa");

	public static QName objectRefQName = new QName("ObjectRef", MetadataSupport.ebRIMns2_uri);


	// Stored Query query ids

	public static String SQ_FindDocuments = "urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d";
	public static String SQ_FindSubmissionSets = "urn:uuid:f26abbcb-ac74-4422-8a30-edb644bbc1a9";
	public static String SQ_FindFolders = "urn:uuid:958f3006-baad-4929-a4de-ff1114824431";
	public static String SQ_GetAll = "urn:uuid:10b545ea-725c-446d-9b95-8aeb444eddf3";
	public static String SQ_GetDocuments = "urn:uuid:5c4f972b-d56b-40ac-a5fc-c8ca9b40b9d4";
	public static String SQ_GetFolders = "urn:uuid:5737b14c-8a1a-4539-b659-e03a34a5e1e4";
	public static String SQ_GetAssociations = "urn:uuid:a7ae438b-4bc2-4642-93e9-be891f7bb155";
	public static String SQ_GetDocumentsAndAssociations = "urn:uuid:bab9529a-4a10-40b3-a01f-f68a615d247a";
	public static String SQ_GetSubmissionSets = "urn:uuid:51224314-5390-4169-9b91-b1980040715a";
	public static String SQ_GetSubmissionSetAndContents = "urn:uuid:e8e3cb2c-e39c-46b9-99e4-c12f57260b83";
	public static String SQ_GetFolderAndContents = "urn:uuid:b909a503-523d-4517-8acf-8e5834dfc4c7";
	public static String SQ_GetFoldersForDocument = "urn:uuid:10cae35a-c7f9-4cf5-b61e-fc3278ffb578";
	public static String SQ_GetRelatedDocuments = "urn:uuid:d90e5407-b356-4d91-a89f-873917b4b0e6";
	
	public static boolean isSQId(String id) {
		if (id == null) return false;
		if (id.equals(SQ_FindDocuments)) return true;
		if (id.equals(SQ_FindSubmissionSets)) return true;
		if (id.equals(SQ_FindFolders)) return true;
		if (id.equals(SQ_GetAll)) return true;
		if (id.equals(SQ_GetDocuments)) return true;
		if (id.equals(SQ_GetFolders)) return true;
		if (id.equals(SQ_GetAssociations)) return true;
		if (id.equals(SQ_GetDocumentsAndAssociations)) return true;
		if (id.equals(SQ_GetSubmissionSets)) return true;
		if (id.equals(SQ_GetSubmissionSetAndContents)) return true;
		if (id.equals(SQ_GetFolderAndContents)) return true;
		if (id.equals(SQ_GetFoldersForDocument)) return true;
		if (id.equals(SQ_GetRelatedDocuments)) return true;
		return false;
	}
	
	public static String SQ_action = "urn:ihe:iti:2007:RegistryStoredQuery";
	public static String MPQ_action = "urn:ihe:iti:2009:MultiPatientStoredQuery";
	
	// multi-patient stored query ids
	public static String SQ_FindDocumentsForMultiplePatients = "urn:uuid:3d1bdb10-39a2-11de-89c2-2f44d94eaa9f";
	public static String SQ_FindFoldersForMultiplePatients = "urn:uuid:50d3f5ac-39a2-11de-a1ca-b366239e58df";

	public static boolean isMPQId(String id) {
		if (id == null) return false;
		if (id.equals(SQ_FindDocumentsForMultiplePatients)) return true;
		if (id.equals(SQ_FindFoldersForMultiplePatients)) return true;
		return false;
	}
	
	// uuids defined 

	public static String XDSSubmissionSet_patientid_uuid = "urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446";
	public static String XDSDocumentEntry_patientid_uuid = "urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427";
	public static String XDSFolder_patientid_uuid = "urn:uuid:f64ffdf0-4b97-4e06-b79f-a52b38ec2f8a";

	public static String XDSSubmissionSet_uniqueid_uuid = "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8";
	public static String XDSDocumentEntry_uniqueid_uuid = "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab";
	public static String XDSFolder_uniqueid_uuid = "urn:uuid:75df8f67-9973-4fbe-a900-df66cefecc5a";

	public static String XDSSubmissionSet_author_uuid = "urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d";
	public static String XDSDocumentEntry_author_uuid = "urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d";

	public static String XDSSubmissionSet_sourceid_uuid="urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832";

	public static String XDSDocumentEntry_objectType_uuid="urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1";

	public static String XDSSubmissionSet_classification_uuid = "urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd";
	public static String XDSFolder_classification_uuid = "urn:uuid:d9d542f3-6cc4-48b6-8870-ea235fbc94c2";

	public static String XDSDocumentEntry_formatCode_uuid="urn:uuid:a09d5840-386c-46f2-b5ad-9c3699a4309d";
	public static String XDSDocumentEntry_classCode_uuid="urn:uuid:41a5887f-8865-4c09-adf7-e362475b143a";
	public static String XDSDocumentEntry_psCode_uuid="urn:uuid:cccf5598-8b07-4b77-a05e-ae952c785ead";
	public static String XDSDocumentEntry_hcftCode_uuid="urn:uuid:f33fb8ac-18af-42cc-ae0e-ed0b0bdb91e1";
	public static String XDSDocumentEntry_eventCode_uuid="urn:uuid:2c6b8cb7-8b2a-4051-b291-b1ae6a575ef4";
	public static String XDSDocumentEntry_confCode_uuid="urn:uuid:f4f85eac-e6cb-4883-b524-f2705394840f";
	
	public static String XDSAssociationDocumentation_uuid = "urn:uuid:abd807a3-4432-4053-87b4-fd82c643d1f3";

	// XDS error codes	
	public static String XDSMissingDocument = "XDSMissingDocument";
	public static String XDSMissingDocumentMetadata = "XDSMissingDocumentMetadata";
	public static String XDSRegistryNotAvailable = "XDSRegistryNotAvailable";
	public static String XDSRegistryError = "XDSRegistryError";
	public static String XDSRepositoryError = "XDSRepositoryError";
	public static String XDSRepositoryWrongRepositoryUniqueId = "XDSRepositoryWrongRepositoryUniqueId";
	public static String XDSRegistryDuplicateUniqueIdInMessage = "XDSRegistryDuplicateUniqueIdInMessage";
	public static String XDSRepositoryDuplicateUniqueIdInMessage = "XDSRepositoryDuplicateUniqueIdInMessage";
	public static String XDSDuplicateUniqueIdInRegistry = "XDSDuplicateUniqueIdInRegistry";
	public static String XDSNonIdenticalHash = "XDSNonIdenticalHash";
	public static String XDSRegistryBusy = "XDSRegistryBusy";
	public static String XDSRepositoryBusy  = "XDSRepositoryBusy";
	public static String XDSRegistryOutOfResources = "XDSRegistryOutOfResources";
	public static String XDSRepositoryOutOfResources = "XDSRepositoryOutOfResources";
	public static String XDSRegistryMetadataError = "XDSRegistryMetadataError";
	public static String XDSRepositoryMetadataError = "XDSRepositoryMetadataError";
	public static String XDSTooManyResults = "XDSTooManyResults";
	public static String XDSExtraMetadataNotSaved = "XDSExtraMetadataNotSaved";
	public static String XDSUnknownPatientId = "XDSUnknownPatientId";
	public static String XDSPatientIdDoesNotMatch = "XDSPatientIdDoesNotMatch";
	public static String XDSResultNotSinglePatient = "XDSResultNotSinglePatient";
	public static String XDSUnknownStoredQuery = "XDSUnknownStoredQuery";
	public static String XDSStoredQueryMissingParam = "XDSStoredQueryMissingParam";
	public static String XDSStoredQueryParamNumber = "XDSStoredQueryParamNumber";
	public static String XDSSqlError = "XDSSqlError";

	public static String XDSUnknownRepositoryId = "XDSUnknownRepositoryId";

	public static String XDSMissingHomeCommunityId = "XDSMissingHomeCommunityId";
	public static String XDSUnknownCommunity = "XDSUnknownCommunity";
	public static String XDSUnavailableCommunity = "XDSUnavailableCommunity";


	public static QName response_option_qname = new QName("ResponseOption");
	public static QName adhoc_query_qname = new QName("AdhocQuery");
	
	// DSUB stuff
	
	public static String wsnt_ns_uri = "http://docs.oasis-open.org/wsn/b-2";
	public static OMNamespace wsnt_ns =  om_factory.createOMNamespace(wsnt_ns_uri, "wsnt");
	
	public static String dsub_ns_uri = "urn:ihe:iti:dsub:2009";
	public static OMNamespace dsub_ns =  om_factory.createOMNamespace(dsub_ns_uri, "dsub");
	public static String dsub_subscribe_action = "http://docs.oasis-open.org/wsn/bw-2/NotificationProducer/SubscribeRequest";
	public static String dsub_subscribe_response_action = "http://docs.oasis-open.org/wsn/bw-2/NotificationProducer/SubscribeResponse";

	public static OMElement firstChildWithLocalName(OMElement ele, String localName) {
		for (Iterator it=ele.getChildElements(); it.hasNext(); ) {
			OMElement child = (OMElement) it.next();
			if (child.getLocalName().equals(localName))
				return child;
		}
		return null;
	}

	public static OMElement firstChildWithLocalNameEndingWith(OMElement ele, String localNameSuffix) {
		for (Iterator it=ele.getChildElements(); it.hasNext(); ) {
			OMElement child = (OMElement) it.next();
			if (child.getLocalName().endsWith(localNameSuffix))
				return child;
		}
		return null;
	}

	public static List<OMElement> childrenWithLocalName(OMElement ele, String localName) {
		List<OMElement> al = new ArrayList<OMElement>();
		for (Iterator it=ele.getChildElements(); it.hasNext(); ) {
			OMElement child = (OMElement) it.next();
			if (child.getLocalName().equals(localName))
				al.add(child);
		}
		return al;
	}

	public static List<String> childrenLocalNames(OMElement ele) {
		List<String> al = new ArrayList<String>();
		for (Iterator it=ele.getChildElements(); it.hasNext(); ) {
			OMElement child = (OMElement) it.next();
				al.add(child.getLocalName());
		}
		return al;
	}

	/**
	 * Get child of ele with matching name and id attribute.
	 * @param ele
	 * @param localName
	 * @param id
	 * @return
	 */
	public static OMElement getChild(OMElement ele, String localName, String id) {
		for (Iterator it=ele.getChildElements(); it.hasNext(); ) {
			OMElement child = (OMElement) it.next();
			if (child.getLocalName().equals(localName)) {
				String idAttVal = child.getAttributeValue(MetadataSupport.id_qname);
				if (idAttVal != null && idAttVal.equals(id))
					return child;
			}
		}
		return null;
	}

	public static List<OMElement> decendentsWithLocalName(OMElement ele, String localName) {
		return decendentsWithLocalName(ele, localName, -1);
	}

		public static List<OMElement> decendentsWithLocalName(OMElement ele, String localName, int depth) {
		List<OMElement> al = new ArrayList<OMElement>();
		if (ele == null || localName == null)
			return al;
		decendentsWithLocalName1(al, ele, localName, depth);
		return al;
	}

	private static void decendentsWithLocalName1(List<OMElement> decendents, OMElement ele, String localName, int depth) {
		if (depth == 0)
			return;
		for (Iterator it=ele.getChildElements(); it.hasNext(); ) {
			OMElement child = (OMElement) it.next();
			if (child.getLocalName().equals(localName))
				decendents.add(child);
			decendentsWithLocalName1(decendents, child, localName, depth - 1);
		}
	}
	
	public static OMElement createElement(String localName, OMNamespace ns) {
		return om_factory.createOMElement(localName, ns);
	}

	public static OMElement addChild(String localName, OMNamespace ns, OMElement parent) {
		return om_factory.createOMElement(localName, ns, parent);
	}

}