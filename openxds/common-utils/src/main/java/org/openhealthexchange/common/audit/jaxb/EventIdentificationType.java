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
 * Java content class for EventIdentificationType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/J:/workspace/connect_refactor_ihe/src/com/misyshealthcare/connect/ihe/audit/AuditMessage.xsd line 25)
 * <p>
 * <pre>
 * &lt;complexType name="EventIdentificationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EventID" type="{}CodedValueType"/>
 *         &lt;element name="EventTypeCode" type="{}CodedValueType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="EventActionCode">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="C"/>
 *             &lt;enumeration value="R"/>
 *             &lt;enumeration value="U"/>
 *             &lt;enumeration value="D"/>
 *             &lt;enumeration value="E"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="EventDateTime" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="EventOutcomeIndicator" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *             &lt;enumeration value="0"/>
 *             &lt;enumeration value="4"/>
 *             &lt;enumeration value="8"/>
 *             &lt;enumeration value="12"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface EventIdentificationType {


    /**
     * Gets the value of the eventID property.
     * 
     * @return
     *     possible object is
     *     {@link org.openhealthexchange.common.audit.jaxb.CodedValueType}
     */
    org.openhealthexchange.common.audit.jaxb.CodedValueType getEventID();

    /**
     * Sets the value of the eventID property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openhealthexchange.common.audit.jaxb.CodedValueType}
     */
    void setEventID(org.openhealthexchange.common.audit.jaxb.CodedValueType value);

    /**
     * Gets the value of the eventOutcomeIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getEventOutcomeIndicator();

    /**
     * Sets the value of the eventOutcomeIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setEventOutcomeIndicator(java.math.BigInteger value);

    /**
     * Gets the value of the eventDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getEventDateTime();

    /**
     * Sets the value of the eventDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setEventDateTime(java.util.Calendar value);

    /**
     * Gets the value of the eventActionCode property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getEventActionCode();

    /**
     * Sets the value of the eventActionCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setEventActionCode(java.lang.String value);

    /**
     * Gets the value of the EventTypeCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the EventTypeCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEventTypeCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link org.openhealthexchange.common.audit.jaxb.CodedValueType}
     * 
     */
    java.util.List getEventTypeCode();

}
