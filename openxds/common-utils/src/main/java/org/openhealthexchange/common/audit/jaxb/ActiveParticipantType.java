/* Copyright 2009 Misys PLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License. 
 */


package org.openhealthexchange.common.audit.jaxb;


/**
 * Java content class for ActiveParticipantType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/J:/workspace/connect_refactor_ihe/src/com/misyshealthcare/connect/ihe/audit/AuditMessage.xsd line 168)
 * <p>
 * <pre>
 * &lt;complexType name="ActiveParticipantType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element name="RoleIDCode" type="{}CodedValueType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="AlternativeUserID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="NetworkAccessPointID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="NetworkAccessPointTypeCode">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedByte">
 *             &lt;enumeration value="1"/>
 *             &lt;enumeration value="2"/>
 *             &lt;enumeration value="3"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="UserID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="UserIsRequestor" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="UserName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ActiveParticipantType {


    /**
     * Gets the value of the userIsRequestor property.
     * 
     */
    boolean isUserIsRequestor();

    /**
     * Sets the value of the userIsRequestor property.
     * 
     */
    void setUserIsRequestor(boolean value);

    /**
     * Gets the value of the userID property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getUserID();

    /**
     * Sets the value of the userID property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setUserID(java.lang.String value);

    /**
     * Gets the value of the networkAccessPointTypeCode property.
     * 
     */
    short getNetworkAccessPointTypeCode();

    /**
     * Sets the value of the networkAccessPointTypeCode property.
     * 
     */
    void setNetworkAccessPointTypeCode(short value);

    /**
     * Gets the value of the alternativeUserID property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAlternativeUserID();

    /**
     * Sets the value of the alternativeUserID property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAlternativeUserID(java.lang.String value);

    /**
     * Gets the value of the networkAccessPointID property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getNetworkAccessPointID();

    /**
     * Sets the value of the networkAccessPointID property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setNetworkAccessPointID(java.lang.String value);

    /**
     * Gets the value of the RoleIDCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the RoleIDCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRoleIDCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link org.openhealthexchange.common.audit.jaxb.CodedValueType}
     * 
     */
    java.util.List getRoleIDCode();

    /**
     * Gets the value of the userName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getUserName();

    /**
     * Sets the value of the userName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setUserName(java.lang.String value);

}
