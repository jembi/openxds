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
 * Java content class for AuditSourceIdentificationType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/J:/workspace/connect_refactor_ihe/src/com/misyshealthcare/connect/ihe/audit/AuditMessage.xsd line 93)
 * <p>
 * <pre>
 * &lt;complexType name="AuditSourceIdentificationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AuditSourceTypeCode" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{}CodedValueType">
 *                 &lt;attribute name="code" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;enumeration value="1"/>
 *                       &lt;enumeration value="2"/>
 *                       &lt;enumeration value="3"/>
 *                       &lt;enumeration value="4"/>
 *                       &lt;enumeration value="5"/>
 *                       &lt;enumeration value="6"/>
 *                       &lt;enumeration value="7"/>
 *                       &lt;enumeration value="8"/>
 *                       &lt;enumeration value="9"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="AuditEnterpriseSiteID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="AuditSourceID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface AuditSourceIdentificationType {


    /**
     * Gets the value of the auditEnterpriseSiteID property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAuditEnterpriseSiteID();

    /**
     * Sets the value of the auditEnterpriseSiteID property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAuditEnterpriseSiteID(java.lang.String value);

    /**
     * Gets the value of the auditSourceID property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAuditSourceID();

    /**
     * Sets the value of the auditSourceID property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAuditSourceID(java.lang.String value);

    /**
     * Gets the value of the AuditSourceTypeCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the AuditSourceTypeCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuditSourceTypeCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link org.openhealthexchange.common.audit.jaxb.AuditSourceIdentificationType.AuditSourceTypeCodeType}
     * 
     */
    java.util.List getAuditSourceTypeCode();


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/J:/workspace/connect_refactor_ihe/src/com/misyshealthcare/connect/ihe/audit/AuditMessage.xsd line 97)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{}CodedValueType">
     *       &lt;attribute name="code" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;enumeration value="1"/>
     *             &lt;enumeration value="2"/>
     *             &lt;enumeration value="3"/>
     *             &lt;enumeration value="4"/>
     *             &lt;enumeration value="5"/>
     *             &lt;enumeration value="6"/>
     *             &lt;enumeration value="7"/>
     *             &lt;enumeration value="8"/>
     *             &lt;enumeration value="9"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface AuditSourceTypeCodeType
        extends org.openhealthexchange.common.audit.jaxb.CodedValueType
    {


    }

}
