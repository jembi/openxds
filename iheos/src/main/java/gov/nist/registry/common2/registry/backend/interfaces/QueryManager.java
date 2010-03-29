package gov.nist.registry.common2.registry.backend.interfaces;

import gov.nist.registry.common2.exception.XdsException;

import java.util.List;

import org.apache.axiom.om.OMElement;
import org.openhealthtools.openxds.log.LoggerException;

public interface QueryManager {

	public OMElement getDocByUuid(String uuid) throws XdsException, LoggerException;
	public OMElement getDocByUuid(List<String> uuids) throws XdsException,LoggerException;
	public OMElement getDocByUid(String uid)  throws XdsException,LoggerException;
	public OMElement getDocByUid(List<String> uids) throws XdsException, LoggerException;
	public String getDocIdFromUid(String uid)  throws XdsException, LoggerException;
	public OMElement getAssociations(List<String> uuids, List<String> assoc_types) throws XdsException, LoggerException;
	
	// questionable
	public OMElement getRpByUid(String uid, String identification_scheme) throws XdsException, LoggerException;
	public OMElement getRpByUid(List<String> uids, String identification_scheme) throws XdsException, LoggerException;
	public OMElement getRpByUuid(String ss_uuid, String identification_scheme) throws XdsException, LoggerException;
	public OMElement getRpByUuid(List<String> ss_uuid, String identification_scheme) throws XdsException, LoggerException;
	
	public OMElement getObjectsByUuid(List<String> uuids) throws XdsException, LoggerException;
	public OMElement getFolByUuid(String uuid) throws XdsException, LoggerException;
	public OMElement getFolByUuid(List<String> uuid) throws XdsException, LoggerException;
	public OMElement getFolByUid(String uid) throws XdsException, LoggerException;
	public OMElement getFolByUid(List<String> uid) throws XdsException, LoggerException;
	public OMElement getSsByUuid(String uuid) throws XdsException, LoggerException;
	public OMElement getSsByUid(String uid) throws XdsException, LoggerException;
	public OMElement getSsDocs(String ss_uuid, List<String> format_codes, List<String> conf_codes) throws XdsException, LoggerException;
	public OMElement getFolDocs(String fol_uuid, List<String> format_codes, List<String> conf_codes) throws XdsException, LoggerException;
	public OMElement getAssocs(List<String> package_uuids, List<String> content_uuids) throws XdsException, LoggerException;
	public OMElement getAssocs(List<String> package_uuids) throws XdsException,LoggerException;
	public OMElement getFoldersForDocument(String uuid)  throws XdsException,LoggerException;
}
