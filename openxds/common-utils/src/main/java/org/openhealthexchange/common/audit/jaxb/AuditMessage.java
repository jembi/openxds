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
 * Java content class for AuditMessage element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/J:/workspace/connect_refactor_ihe/src/com/misyshealthcare/connect/ihe/audit/AuditMessage.xsd line 4)
 * <p>
 * <pre>
 * &lt;element name="AuditMessage">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="EventIdentification" type="{}EventIdentificationType"/>
 *           &lt;element name="ActiveParticipant" maxOccurs="unbounded">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;extension base="{}ActiveParticipantType">
 *                 &lt;/extension>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="AuditSourceIdentification" type="{}AuditSourceIdentificationType" maxOccurs="unbounded"/>
 *           &lt;element name="ParticipantObjectIdentification" type="{}ParticipantObjectIdentificationType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface AuditMessage
    extends javax.xml.bind.Element, org.openhealthexchange.common.audit.jaxb.AuditMessageType
{


}
