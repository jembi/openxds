/**
 *  Copyright (c) 2009-2010 Misys Open Source Solutions (MOSS) and others
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
 *    -
 */

package org.openhealthtools.openxds.log;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * Class describing a message to log in database. It contains several action
 * log, read and delete a message.
 * 
 * @author <a href="mailto:venkata.pragallapati@misys.com">Venkat</a>
 * 
 */
@Entity
@Table(name = "main")
public class Message implements LogMessage{

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column(name = "messageID")
	private String messageID;

	@Column(name = "is_secure")
	private boolean secure;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ip")
	private CompanyDetails companyDetails;

	@Column(name = "timereceived")
	private Date timeReceived;

	@Lob
	@Column(name = "test", length=1048576)
	private String test;

	@Column(name = "pass")
	private boolean pass;

	@OneToMany(mappedBy="message", cascade = CascadeType.ALL)
	List<HttpMessage> httpMessages = new ArrayList<HttpMessage>();

	@OneToMany(mappedBy="message", cascade = CascadeType.ALL)
	List<SoapMessage> soapMessages = new ArrayList<SoapMessage>();

	@OneToMany(mappedBy="message", cascade = CascadeType.ALL)
	List<OtherMessage> otherMessages = new ArrayList<OtherMessage>();

	@OneToMany(mappedBy="message", cascade = CascadeType.ALL)
	List<ErrorMessage> errorMessages = new ArrayList<ErrorMessage>();

	@Transient
	private Hashtable<String, Vector> miscVectors;

	@Transient
	private HashSet<String> tableList;

	public Message() {
	}

	public Date getTimeReceived() {
		return timeReceived;
	}

	public void setTimeReceived(Date timeReceived) {
		this.timeReceived = timeReceived;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public List<HttpMessage> getHttpMessages() {
		return httpMessages;
	}

	public void setHttpMessages(List<HttpMessage> httpMessages) {
		this.httpMessages = httpMessages;
	}

	public List<SoapMessage> getSoapMessages() {
		return soapMessages;
	}

	public void setSoapMessages(List<SoapMessage> soapMessages) {
		this.soapMessages = soapMessages;
	}

	public List<OtherMessage> getOtherMessages() {
		return otherMessages;
	}

	public void setOtherMessages(List<OtherMessage> otherMessages) {
		this.otherMessages = otherMessages;
	}

	public List<ErrorMessage> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(List<ErrorMessage> errorMessages) {
		this.errorMessages = errorMessages;
	}

	public boolean isSecure() {
		return secure;
	}

	public boolean isPass() {
		return pass;
	}

	public void setTimeStamp(Timestamp timestamp) {
		this.timeReceived = timestamp;
	}

	public void setSecure(boolean isSecure) {
		this.secure = isSecure;
	}

	public void setTestMessage(String testMessage) {
		this.test = testMessage;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}

	public void setIP(String ip) throws LoggerException {
		String ipStr = ip;
		try {
			ipStr = InetAddress.getByName(ip).getHostAddress();
		} catch (UnknownHostException e) {
		}
		CompanyDetails detials = getCompanyDetails();
		if (detials == null)
			detials = new CompanyDetails();
		detials.setIp(ipStr);
		setCompanyDetails(detials);
	}

	/***
	 * 
	 * @param companyName
	 *            String. Make a pair in the IP table between an IP address and
	 *            a company name. If this pair doesn't exist in the IP table,
	 *            it's logged, if it exists, the pair is updated with the new
	 *            company name.
	 * @throws LoggerException
	 */
	public void setCompany(String companyName) throws LoggerException {
		CompanyDetails detials = getCompanyDetails();
		if (detials == null || detials.getIp() == null)
			throw new LoggerException(
					"Message:setCompany ( String companyName ):: Cannot set company name , the current IP adress associated is null");
		
		if(companyName == null)
			companyName = "Unknown";

		detials.setCompanyName(companyName);
	}

	/**
	 * Generic function creating a pair &lt; parameter name , parameter value
	 * &gt; for the current messageID. <br />
	 * If the Table Name doesn't exist , it is created before writing the
	 * message ( in the writeMessage method ). The parameter is stored in a
	 * hashtable &lt; tableName , Vector &lt;GenericTable &gt; &gt;
	 * 
	 * @param messageType
	 *            , table name wherein the parameter name and value are logged
	 * @param name
	 *            , parameter name
	 * @param value
	 *            ,parameter value
	 * @throws LoggerException
	 */
	public void addParam(String messageType, String name, String value)
			throws LoggerException {
		if (messageType != null) {
			if ("http".equalsIgnoreCase(messageType)) {
				addHTTPParam(name, value);
			} else if ("soap".equalsIgnoreCase(messageType)) {
				addSoapParam(name, value);
			} else if ("error".equalsIgnoreCase(messageType)) {
				addErrorParam(name, value);
			} else if ("other".equalsIgnoreCase(messageType)) {
				addOtherParam(name, value);
			}
		} else
			throw new LoggerException("messageType is null");
	}

	/**
	 * Used for xdsTestLog. Same as addParam ( "http" , name , value ) ;
	 * 
	 * @param name
	 * @param value
	 * @throws LoggerException
	 */
	public void addHTTPParam(String name, String value) throws LoggerException {
		HttpMessage httpMessage = new HttpMessage(name, value);
		httpMessage.setMessage(this);
		httpMessages.add(httpMessage);
	}

	/**
	 * Used for xdsTestLog. Same as addParam ( "soap" , name , value ) ;
	 * 
	 * @param name
	 * @param value
	 * @throws LoggerException
	 */
	public void addSoapParam(String name, String value) throws LoggerException {
		SoapMessage soapMessage = new SoapMessage(name, value);
		soapMessage.setMessage(this);
		soapMessages.add(soapMessage);
	}

	/**
	 * Used for xdsTestLog. Same as addParam ( "error" , name , value ) ;
	 * 
	 * @param name
	 * @param value
	 * @throws LoggerException
	 */
	public void addErrorParam(String name, String value) throws LoggerException {
		ErrorMessage errorMessage = new ErrorMessage(name, value);
		errorMessage.setMessage(this);
		errorMessages.add(errorMessage);
	}

	/**
	 * Used for xdsTestLog. Same as addParam ( "other" , name , value ) ;
	 * 
	 * @param name
	 * @param value
	 * @throws LoggerException
	 */
	public void addOtherParam(String name, String value) throws LoggerException {
		OtherMessage message = new OtherMessage(name, value);
		message.setMessage(this);
		otherMessages.add(message);
	}

	/**
	 * Method used to display the message in the xds log reader. This method
	 * format the message in XML displaying first the list of table ( nodes )
	 * available and then the content of the message.
	 * 
	 * @return
	 */

	public String toXml() {
		//TODO: need to update 
		/*StringBuffer buff = new StringBuffer();

		StringBuffer buffNodeNames = new StringBuffer();
		buffNodeNames.append("<Nodes>");
		buffNodeNames.append("<Node name='mainMessage' />");
		buff.append(mainMessage.toXml());

		Iterator<String> it = tableList.iterator();

		while (it.hasNext()) {
			String currentTable = it.next();
			if (!currentTable.equals("main") && !currentTable.equals("ip")) {
				Vector v = miscVectors.get(currentTable);
				buffNodeNames.append("<Node name='" + currentTable + "' />");
				buff.append("<" + currentTable + ">");
				for (int i = 0; i < v.size(); i++) {
					buff.append(v.elementAt(i).toXml());
				}
				buff.append("</" + currentTable + ">");
			}
		}

		buffNodeNames.append("</Nodes>");

		return "<message number='" + messageID + "'>"
				+ buffNodeNames.toString() + buff.toString() + "</message>";*/
		
		return null;
	}

	public HashMap<String, HashMap<String, Object>> toHashMap() {
		//TODO: need to update
		HashMap<String, HashMap<String, Object>> values = new HashMap<String, HashMap<String, Object>>();

		/*values.put("main", mainMessage.toHashMap());

		Iterator<String> it = tableList.iterator();

		while (it.hasNext()) {
			String currentTable = it.next();
			if (!currentTable.equals("main") && !currentTable.equals("ip")) {
				Vector v = miscVectors.get(currentTable);
				HashMap<String, Object> thisTable = new HashMap<String, Object>();
				for (int i = 0; i < v.size(); i++) {
					String[] parm = null;//v.elementAt(i).toStringArray();
					String key = parm[0].replaceAll(" ", "_");
					String value = parm[1];

					Object oldValueObject = thisTable.get(key);
					if (oldValueObject == null) {
						thisTable.put(key, value);
					} else {
						if (oldValueObject instanceof String) {
							ArrayList<String> newValue = new ArrayList<String>();
							newValue.add((String) oldValueObject);
							newValue.add(value);
							thisTable.put(key, newValue);
						} else {
							ArrayList<String> newValue = (ArrayList<String>) oldValueObject;
							newValue.add(value);
							thisTable.put(key, newValue);
						}
					}
				}
				values.put(currentTable, thisTable);
			}
		}*/

		return values;

		// return "<message number='"+ messageID +"'>" +
		// buffNodeNames.toString() + buff.toString() + "</message>" ;
	}

	public String toJSon() {
		StringBuffer buff = new StringBuffer();
		//TODO: need to update
		/*buff.append("{\"message\" : { \n" + "\"number\": \"" + messageID
				+ "\" , \n  ");
		buff.append("\"table\": \n\t[");

		// buff.append(mainMessage.toJSon());

		Iterator<String> it = tableList.iterator();

		while (it.hasNext()) {
			String currentTable = it.next();
			if (!currentTable.equals("main") && !currentTable.equals("ip")) {
				Vector v = miscVectors.get(currentTable);
				buff.append(",\n{\"name\" : \"" + currentTable + "\",\n");
				buff.append("\"values\" : [");
				for (int i = 0; i < v.size(); i++) {
					//buff.append(v.elementAt(i).toJSon());
					if (i < v.size())
						buff.append(",\n");
				}
				buff.append("]\n}");

			}
		}
		buff.append("]\n}\n}");*/

		return buff.toString();
	}

	public HashSet<String> getTableList() {
		return tableList;
	}

	public void setTableList(HashSet<String> tableList) {
		this.tableList = tableList;
	}

	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public void setCompanyDetails(CompanyDetails companyDetails) {
		this.companyDetails = companyDetails;
	}

	public CompanyDetails getCompanyDetails() {
		return companyDetails;
	}

}
