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
package org.openhealthtools.common.audit.jaxb;


/**
 * Java content class for ParticipantObjectIdentificationType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/J:/workspace/connect_refactor_ihe/src/com/misyshealthcare/connect/ihe/audit/AuditMessage.xsd line 203)
 * <p>
 * <pre>
 * &lt;complexType name="ParticipantObjectIdentificationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ParticipantObjectIDTypeCode">
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
 *                       &lt;enumeration value="10"/>
 *                       &lt;enumeration value="11"/>
 *                       &lt;enumeration value="12"/>
 *                       &lt;enumeration value=""/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="ParticipantObjectName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="ParticipantObjectQuery" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;/choice>
 *         &lt;element name="ParticipantObjectDetail" type="{}TypeValuePairType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ParticipantObjectDataLifeCycle">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedByte">
 *             &lt;enumeration value="1"/>
 *             &lt;enumeration value="2"/>
 *             &lt;enumeration value="3"/>
 *             &lt;enumeration value="4"/>
 *             &lt;enumeration value="5"/>
 *             &lt;enumeration value="6"/>
 *             &lt;enumeration value="7"/>
 *             &lt;enumeration value="8"/>
 *             &lt;enumeration value="9"/>
 *             &lt;enumeration value="10"/>
 *             &lt;enumeration value="11"/>
 *             &lt;enumeration value="12"/>
 *             &lt;enumeration value="13"/>
 *             &lt;enumeration value="14"/>
 *             &lt;enumeration value="15"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="ParticipantObjectID" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ParticipantObjectSensitivity" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ParticipantObjectTypeCode">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedByte">
 *             &lt;enumeration value="1"/>
 *             &lt;enumeration value="2"/>
 *             &lt;enumeration value="3"/>
 *             &lt;enumeration value="4"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="ParticipantObjectTypeCodeRole">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedByte">
 *             &lt;enumeration value="1"/>
 *             &lt;enumeration value="2"/>
 *             &lt;enumeration value="3"/>
 *             &lt;enumeration value="4"/>
 *             &lt;enumeration value="5"/>
 *             &lt;enumeration value="6"/>
 *             &lt;enumeration value="7"/>
 *             &lt;enumeration value="8"/>
 *             &lt;enumeration value="9"/>
 *             &lt;enumeration value="10"/>
 *             &lt;enumeration value="11"/>
 *             &lt;enumeration value="12"/>
 *             &lt;enumeration value="13"/>
 *             &lt;enumeration value="14"/>
 *             &lt;enumeration value="15"/>
 *             &lt;enumeration value="16"/>
 *             &lt;enumeration value="17"/>
 *             &lt;enumeration value="18"/>
 *             &lt;enumeration value="19"/>
 *             &lt;enumeration value="20"/>
 *             &lt;enumeration value="21"/>
 *             &lt;enumeration value="22"/>
 *             &lt;enumeration value="23"/>
 *             &lt;enumeration value="24"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ParticipantObjectIdentificationType {


    /**
     * Gets the value of the participantObjectTypeCodeRole property.
     * 
     */
    short getParticipantObjectTypeCodeRole();

    /**
     * Sets the value of the participantObjectTypeCodeRole property.
     * 
     */
    void setParticipantObjectTypeCodeRole(short value);

    /**
     * Gets the value of the participantObjectID property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getParticipantObjectID();

    /**
     * Sets the value of the participantObjectID property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setParticipantObjectID(java.lang.String value);

    /**
     * Gets the value of the participantObjectIDTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link org.openhealthtools.common.audit.jaxb.ParticipantObjectIdentificationType.ParticipantObjectIDTypeCodeType}
     */
    org.openhealthtools.common.audit.jaxb.ParticipantObjectIdentificationType.ParticipantObjectIDTypeCodeType getParticipantObjectIDTypeCode();

    /**
     * Sets the value of the participantObjectIDTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openhealthtools.common.audit.jaxb.ParticipantObjectIdentificationType.ParticipantObjectIDTypeCodeType}
     */
    void setParticipantObjectIDTypeCode(org.openhealthtools.common.audit.jaxb.ParticipantObjectIdentificationType.ParticipantObjectIDTypeCodeType value);

    /**
     * Gets the value of the ParticipantObjectDetail property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ParticipantObjectDetail property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParticipantObjectDetail().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link org.openhealthtools.common.audit.jaxb.TypeValuePairType}
     * 
     */
    java.util.List getParticipantObjectDetail();

    /**
     * Gets the value of the participantObjectName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getParticipantObjectName();

    /**
     * Sets the value of the participantObjectName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setParticipantObjectName(java.lang.String value);

    /**
     * Gets the value of the participantObjectQuery property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    byte[] getParticipantObjectQuery();

    /**
     * Sets the value of the participantObjectQuery property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    void setParticipantObjectQuery(byte[] value);

    /**
     * Gets the value of the participantObjectDataLifeCycle property.
     * 
     */
    short getParticipantObjectDataLifeCycle();

    /**
     * Sets the value of the participantObjectDataLifeCycle property.
     * 
     */
    void setParticipantObjectDataLifeCycle(short value);

    /**
     * Gets the value of the participantObjectTypeCode property.
     * 
     */
    short getParticipantObjectTypeCode();

    /**
     * Sets the value of the participantObjectTypeCode property.
     * 
     */
    void setParticipantObjectTypeCode(short value);

    /**
     * Gets the value of the participantObjectSensitivity property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getParticipantObjectSensitivity();

    /**
     * Sets the value of the participantObjectSensitivity property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setParticipantObjectSensitivity(java.lang.String value);


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/J:/workspace/connect_refactor_ihe/src/com/misyshealthcare/connect/ihe/audit/AuditMessage.xsd line 206)
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
     *             &lt;enumeration value="10"/>
     *             &lt;enumeration value="11"/>
     *             &lt;enumeration value="12"/>
     *             &lt;enumeration value=""/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface ParticipantObjectIDTypeCodeType
        extends org.openhealthtools.common.audit.jaxb.CodedValueType
    {


    }

}
