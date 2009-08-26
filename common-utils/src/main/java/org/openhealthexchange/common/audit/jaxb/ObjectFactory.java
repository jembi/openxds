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
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.openhealthexchange.common.audit.jaxb package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
public class ObjectFactory
    extends org.openhealthexchange.common.audit.jaxb.impl.runtime.DefaultJAXBContextImpl
{

    private static java.util.HashMap defaultImplementations = new java.util.HashMap(16, 0.75F);
    private static java.util.HashMap rootTagMap = new java.util.HashMap();
    public final static org.openhealthexchange.common.audit.jaxb.impl.runtime.GrammarInfo grammarInfo = new org.openhealthexchange.common.audit.jaxb.impl.runtime.GrammarInfoImpl(rootTagMap, defaultImplementations, (org.openhealthexchange.common.audit.jaxb.ObjectFactory.class));
    public final static java.lang.Class version = (org.openhealthexchange.common.audit.jaxb.impl.JAXBVersion.class);

    static {
        defaultImplementations.put((org.openhealthexchange.common.audit.jaxb.ActiveParticipantType.class), "org.openhealthexchange.common.audit.jaxb.impl.ActiveParticipantTypeImpl");
        defaultImplementations.put((org.openhealthexchange.common.audit.jaxb.EventIdentificationType.class), "org.openhealthexchange.common.audit.jaxb.impl.EventIdentificationTypeImpl");
        defaultImplementations.put((org.openhealthexchange.common.audit.jaxb.AuditSourceIdentificationType.class), "org.openhealthexchange.common.audit.jaxb.impl.AuditSourceIdentificationTypeImpl");
        defaultImplementations.put((org.openhealthexchange.common.audit.jaxb.ParticipantObjectIdentificationType.class), "org.openhealthexchange.common.audit.jaxb.impl.ParticipantObjectIdentificationTypeImpl");
        defaultImplementations.put((org.openhealthexchange.common.audit.jaxb.TypeValuePairType.class), "org.openhealthexchange.common.audit.jaxb.impl.TypeValuePairTypeImpl");
        defaultImplementations.put((org.openhealthexchange.common.audit.jaxb.AuditMessageType.ActiveParticipantType.class), "org.openhealthexchange.common.audit.jaxb.impl.AuditMessageTypeImpl.ActiveParticipantTypeImpl");
        defaultImplementations.put((org.openhealthexchange.common.audit.jaxb.AuditMessageType.class), "org.openhealthexchange.common.audit.jaxb.impl.AuditMessageTypeImpl");
        defaultImplementations.put((org.openhealthexchange.common.audit.jaxb.ParticipantObjectIdentificationType.ParticipantObjectIDTypeCodeType.class), "org.openhealthexchange.common.audit.jaxb.impl.ParticipantObjectIdentificationTypeImpl.ParticipantObjectIDTypeCodeTypeImpl");
        defaultImplementations.put((org.openhealthexchange.common.audit.jaxb.AuditSourceIdentificationType.AuditSourceTypeCodeType.class), "org.openhealthexchange.common.audit.jaxb.impl.AuditSourceIdentificationTypeImpl.AuditSourceTypeCodeTypeImpl");
        defaultImplementations.put((org.openhealthexchange.common.audit.jaxb.CodedValueType.class), "org.openhealthexchange.common.audit.jaxb.impl.CodedValueTypeImpl");
        defaultImplementations.put((org.openhealthexchange.common.audit.jaxb.AuditMessage.class), "org.openhealthexchange.common.audit.jaxb.impl.AuditMessageImpl");
        rootTagMap.put(new javax.xml.namespace.QName("", "AuditMessage"), (org.openhealthexchange.common.audit.jaxb.AuditMessage.class));
    }

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openhealthexchange.common.audit.jaxb
     * 
     */
    public ObjectFactory() {
        super(grammarInfo);
    }

    /**
     * Create an instance of the specified Java content interface.
     * 
     * @param javaContentInterface
     *     the Class object of the javacontent interface to instantiate
     * @return
     *     a new instance
     * @throws JAXBException
     *     if an error occurs
     */
    public java.lang.Object newInstance(java.lang.Class javaContentInterface)
        throws javax.xml.bind.JAXBException
    {
        return super.newInstance(javaContentInterface);
    }

    /**
     * Get the specified property. This method can only be
     * used to get provider specific properties.
     * Attempting to get an undefined property will result
     * in a PropertyException being thrown.
     * 
     * @param name
     *     the name of the property to retrieve
     * @return
     *     the value of the requested property
     * @throws PropertyException
     *     when there is an error retrieving the given property or value
     */
    public java.lang.Object getProperty(java.lang.String name)
        throws javax.xml.bind.PropertyException
    {
        return super.getProperty(name);
    }

    /**
     * Set the specified property. This method can only be
     * used to set provider specific properties.
     * Attempting to set an undefined property will result
     * in a PropertyException being thrown.
     * 
     * @param value
     *     the value of the property to be set
     * @param name
     *     the name of the property to retrieve
     * @throws PropertyException
     *     when there is an error processing the given property or value
     */
    public void setProperty(java.lang.String name, java.lang.Object value)
        throws javax.xml.bind.PropertyException
    {
        super.setProperty(name, value);
    }

    /**
     * Create an instance of ActiveParticipantType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public org.openhealthexchange.common.audit.jaxb.ActiveParticipantType createActiveParticipantType()
        throws javax.xml.bind.JAXBException
    {
        return new org.openhealthexchange.common.audit.jaxb.impl.ActiveParticipantTypeImpl();
    }

    /**
     * Create an instance of EventIdentificationType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public org.openhealthexchange.common.audit.jaxb.EventIdentificationType createEventIdentificationType()
        throws javax.xml.bind.JAXBException
    {
        return new org.openhealthexchange.common.audit.jaxb.impl.EventIdentificationTypeImpl();
    }

    /**
     * Create an instance of AuditSourceIdentificationType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public org.openhealthexchange.common.audit.jaxb.AuditSourceIdentificationType createAuditSourceIdentificationType()
        throws javax.xml.bind.JAXBException
    {
        return new org.openhealthexchange.common.audit.jaxb.impl.AuditSourceIdentificationTypeImpl();
    }

    /**
     * Create an instance of ParticipantObjectIdentificationType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public org.openhealthexchange.common.audit.jaxb.ParticipantObjectIdentificationType createParticipantObjectIdentificationType()
        throws javax.xml.bind.JAXBException
    {
        return new org.openhealthexchange.common.audit.jaxb.impl.ParticipantObjectIdentificationTypeImpl();
    }

    /**
     * Create an instance of TypeValuePairType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public org.openhealthexchange.common.audit.jaxb.TypeValuePairType createTypeValuePairType()
        throws javax.xml.bind.JAXBException
    {
        return new org.openhealthexchange.common.audit.jaxb.impl.TypeValuePairTypeImpl();
    }

    /**
     * Create an instance of AuditMessageTypeActiveParticipantType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public org.openhealthexchange.common.audit.jaxb.AuditMessageType.ActiveParticipantType createAuditMessageTypeActiveParticipantType()
        throws javax.xml.bind.JAXBException
    {
        return new org.openhealthexchange.common.audit.jaxb.impl.AuditMessageTypeImpl.ActiveParticipantTypeImpl();
    }

    /**
     * Create an instance of AuditMessageType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public org.openhealthexchange.common.audit.jaxb.AuditMessageType createAuditMessageType()
        throws javax.xml.bind.JAXBException
    {
        return new org.openhealthexchange.common.audit.jaxb.impl.AuditMessageTypeImpl();
    }

    /**
     * Create an instance of ParticipantObjectIdentificationTypeParticipantObjectIDTypeCodeType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public org.openhealthexchange.common.audit.jaxb.ParticipantObjectIdentificationType.ParticipantObjectIDTypeCodeType createParticipantObjectIdentificationTypeParticipantObjectIDTypeCodeType()
        throws javax.xml.bind.JAXBException
    {
        return new org.openhealthexchange.common.audit.jaxb.impl.ParticipantObjectIdentificationTypeImpl.ParticipantObjectIDTypeCodeTypeImpl();
    }

    /**
     * Create an instance of AuditSourceIdentificationTypeAuditSourceTypeCodeType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public org.openhealthexchange.common.audit.jaxb.AuditSourceIdentificationType.AuditSourceTypeCodeType createAuditSourceIdentificationTypeAuditSourceTypeCodeType()
        throws javax.xml.bind.JAXBException
    {
        return new org.openhealthexchange.common.audit.jaxb.impl.AuditSourceIdentificationTypeImpl.AuditSourceTypeCodeTypeImpl();
    }

    /**
     * Create an instance of CodedValueType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public org.openhealthexchange.common.audit.jaxb.CodedValueType createCodedValueType()
        throws javax.xml.bind.JAXBException
    {
        return new org.openhealthexchange.common.audit.jaxb.impl.CodedValueTypeImpl();
    }

    /**
     * Create an instance of AuditMessage
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public org.openhealthexchange.common.audit.jaxb.AuditMessage createAuditMessage()
        throws javax.xml.bind.JAXBException
    {
        return new org.openhealthexchange.common.audit.jaxb.impl.AuditMessageImpl();
    }

}
