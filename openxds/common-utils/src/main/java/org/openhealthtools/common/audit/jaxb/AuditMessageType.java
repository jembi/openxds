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
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/J:/workspace/connect_refactor_ihe/src/com/misyshealthcare/connect/ihe/audit/AuditMessage.xsd line 5)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EventIdentification" type="{}EventIdentificationType"/>
 *         &lt;element name="ActiveParticipant" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;extension base="{}ActiveParticipantType">
 *               &lt;/extension>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="AuditSourceIdentification" type="{}AuditSourceIdentificationType" maxOccurs="unbounded"/>
 *         &lt;element name="ParticipantObjectIdentification" type="{}ParticipantObjectIdentificationType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface AuditMessageType {


    /**
     * Gets the value of the AuditSourceIdentification property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the AuditSourceIdentification property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuditSourceIdentification().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link org.openhealthtools.common.audit.jaxb.AuditSourceIdentificationType}
     * 
     */
    java.util.List getAuditSourceIdentification();

    /**
     * Gets the value of the ParticipantObjectIdentification property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ParticipantObjectIdentification property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParticipantObjectIdentification().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link org.openhealthtools.common.audit.jaxb.ParticipantObjectIdentificationType}
     * 
     */
    java.util.List getParticipantObjectIdentification();

    /**
     * Gets the value of the ActiveParticipant property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ActiveParticipant property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getActiveParticipant().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link org.openhealthtools.common.audit.jaxb.AuditMessageType.ActiveParticipantType}
     * 
     */
    java.util.List getActiveParticipant();

    /**
     * Gets the value of the eventIdentification property.
     * 
     * @return
     *     possible object is
     *     {@link org.openhealthtools.common.audit.jaxb.EventIdentificationType}
     */
    org.openhealthtools.common.audit.jaxb.EventIdentificationType getEventIdentification();

    /**
     * Sets the value of the eventIdentification property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.openhealthtools.common.audit.jaxb.EventIdentificationType}
     */
    void setEventIdentification(org.openhealthtools.common.audit.jaxb.EventIdentificationType value);


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/J:/workspace/connect_refactor_ihe/src/com/misyshealthcare/connect/ihe/audit/AuditMessage.xsd line 10)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;extension base="{}ActiveParticipantType">
     *     &lt;/extension>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface ActiveParticipantType
        extends org.openhealthtools.common.audit.jaxb.ActiveParticipantType
    {


    }

}
