/**
 *  Copyright (c) 2009 Misys Open Source Solutions (MOSS) and others
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  Contributors:
 *    Misys Open Source Solutions - initial API and implementation
 *
 */
package org.openhealthtools.common.audit;

import com.misyshealthcare.connect.net.IConnectionDescription;
import com.misyshealthcare.connect.net.Identifier;
import com.misyshealthcare.connect.net.PropertySet;

/** 
 * The description of an audit trail connection.
 * 
 *
 * @author Josh Flachsbart
 * @version 1.0 - Dec 9, 2005
 */
public class AuditTrailDescription {
	public static final String BSD = "BSD SYSLOG";
	public static final String RELIABLE = "RELIABLE SYSLOG";
	public static final String LOG4J = "LOG4J LOGGER";
	
	private String localHostname;
	private String localIp;
	private String localName;
	
	private String applicationName = "OpenXDS";
	private String facilityName="MISYS PLC";
	private String alternateName;
	private String enterpriseSiteId;
	private String auditSourceId;
	
	private String auditTrailType = LOG4J;
	private String defaultIdEncoding;
	private String defaultNameEncoding;

	private String remoteHostname;
	private int remotePort;
	private String remoteType;
	
	/** 
	 * Generates a new audit trail description.
	 * 
	 * This constructor builds a new audit trail description from a 
	 * connection description.  It currently does not care if it is
	 * a secure connection or a standard connection.  It also relies
	 * on a set of properties for filling in additional fields
	 * required by the different audit messages.  These properites
	 * are grouped into two sets.  Additionally an asigning authority
	 * is needed for some id encodings, use &lt;AssigningAuthority ... for
	 * non local authorities, and &lt;LocalAssigningAuthority ... for the 
	 * default local authority.
	 * <dl>
	 * <dt>Type</dt>
	 * <dd>The type of the connection, BSD SYSLOG, RELIABLE SYSLOG or LOG4J LOGGER.  The default is the log4j logger.</dd>
	 * <dt>Server</dt>
	 * <dd>Some servers require different logging messages.  This is where this is described.  HIPAAT or QUOVADX, or nothing if using the standard server.</dd>
	 * <dt>Set: Localhost Entry: Site</dt>
	 * <dd>Required.  The enterprise site ID.</dd>
	 * <dt>Set: Localhost Entry: SOURCE</dt>
	 * <dd>Required.  The audit source ID.</dd>
	 * <dt>Set: Localhost Entry: FACILITY</dt>
	 * <dd>Required. The name of the facility. Default is MISYS PLC</dd>
	 * <dt>Set: Localhost Entry: APPNAME</dt>
	 * <dd>Required. The name of the application, OpenXDS by default.</dd>
	 * <dt>Set: Localhost Entry: AENAME</dt>
	 * <dd>The alternate name for the application.  Default is none.</dd>
	 * <dt>Set: Localhost Entry: HOSTNAME</dt>
	 * <dd>Required.  The fully qualified domain name of this machine (for this connection.)</dd>
	 * <dt>Set: Localhost Entry: IP</dt>
	 * <dd>Required.  The ip address of this machine (for this connection.)</dd>
	 * <dt>Set: Localhost Entry: MACHINENAME</dt>
	 * <dd>Required.  The name address of this machine.</dd>
	 * <dt>Set: ParticipantObject Entry: PatientIdEncoding</dt>
	 * <dd>The encoding to use for patient Id.  Default is standard, HL7 is a good option.</dd>
	 * <dt>Set: ParticipantObject Entry: PatientNameEncoding</dt>
	 * <dd>The encoding to use for patient name.  Default is standard, HL7 is a good option.</dd>
	 * </dl>
	 * 
	 */
	public AuditTrailDescription(IConnectionDescription auditRepository) {
		// Connection (not required if we are a log4j logger).
		remoteHostname = auditRepository.getHostname();
		remotePort = auditRepository.getPort();

		String temp = auditRepository.getProperty("TYPE");
		if (temp != null) auditTrailType = temp;
		remoteType = auditRepository.getProperty("SERVER");
		
		PropertySet set = auditRepository.getPropertySet("LocalHost");
		localHostname = set.getValue("HOSTNAME");
		localIp = set.getValue("IP");
		localName = set.getValue("MACHINENAME");
		enterpriseSiteId = set.getValue("SITE");
		auditSourceId = set.getValue("SOURCE");
		alternateName = set.getValue("AENAME");
		if (set.containsValue("APPNAME")) applicationName = set.getValue("APPNAME");
		if (set.containsValue("FACILITY")) facilityName = set.getValue("FACILITY");
		
		set = auditRepository.getPropertySet("ParticipantObject");
		defaultIdEncoding = set.getValue("PatientIdEncoding");
		defaultNameEncoding = set.getValue("PatientNameEncoding");
	}
	
	/** 
	 * Generates a new audit trail description.
	 * 
	 * @param name The local name of the auditing machine.
	 * @param fqdn The fully qualified domain name of the audited machine.
	 * @param type The string describing what kind of logger this is.
	 * @param ip The local ip address of the logging machine.
	 */
	public AuditTrailDescription(String name, String fqdn, String type, String ip, String location, String site) {
		localHostname = fqdn;
		localName = name;
		auditTrailType = type;
		localIp = ip;
		auditSourceId = location;
		enterpriseSiteId = site;
	}
	
	/** 
	 * Generates a new audit trail description.
	 * 
	 * @param name The local name of the auditing machine.
	 * @param fqdn The fully qualified domain name of the audited machine.
	 * @param type The string describing what kind of logger this is.
	 * @param ip The local ip address of the logging machine.
	 * @param host The address of the audit repository.
	 * @param port The port the repository is listening on.
	 */
	public AuditTrailDescription(String name, String fqdn, String type, String ip, String host, int port) {
		localHostname = fqdn;
		localName = name;
		auditTrailType = type;
		localIp = ip;
		remoteHostname = host;
		remotePort = port;
	}
	
	/** 
	 * Generates a new audit trail description.
	 * 
	 * @param name The local name of the auditing machine.
	 * @param fqdn The fully qualified domain name of the audited machine.
	 * @param type The string describing what kind of logger this is.
	 * @param ip The local ip address of the logging machine.
	 * @param host The address of the audit repository.
	 * @param port The port the repository is listening on.
	 * @param serverType The kind of rsyslog server since they are all broken: HIPAAT or QUOVADX or null
	 */
	public AuditTrailDescription(String name, String fqdn, String type, String ip, String host, int port, String serverType) {
		localHostname = fqdn;
		localName = name;
		auditTrailType = type;
		localIp = ip;
		remoteHostname = host;
		remotePort = port;
		remoteType = serverType;
	}
	
	public String getName() {
		return localName;
	}
	
	public String getFqdn() {
		return localHostname;
	}
	
	public String getType() {
		return auditTrailType;
	}
	
	public String getIp() {
		return localIp;
	}

	public int getPort() {
		return remotePort;
	}
	
	public String getHost() {
		return remoteHostname;
	}

	public String getServer() {
		return remoteType;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
	
	public String getFacilityName() {
		return facilityName;
	}
	public void setAlternateName(String alternateName) {
		this.alternateName = alternateName;
	}

	public String getAlternateName() {
		return alternateName;
	}

	public void setEnterpriseSiteId(String enterpriseSiteId) {
		this.enterpriseSiteId = enterpriseSiteId;
	}

	public String getEnterpriseSiteId() {
		return enterpriseSiteId;
	}

	public void setAuditSourceId(String auditSourceId) {
		this.auditSourceId = auditSourceId;
	}

	public String getAuditSourceId() {
		return auditSourceId;
	}
		
	public String getNameEncoding() {
		return defaultNameEncoding;
	}
	
	public String getIdEncoding() {
		return defaultIdEncoding;
	}
	
}