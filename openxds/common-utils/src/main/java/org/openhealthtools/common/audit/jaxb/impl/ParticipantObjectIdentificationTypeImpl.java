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

package org.openhealthtools.common.audit.jaxb.impl;

public class ParticipantObjectIdentificationTypeImpl implements org.openhealthtools.common.audit.jaxb.ParticipantObjectIdentificationType, com.sun.xml.bind.JAXBObject, org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallableObject, org.openhealthtools.common.audit.jaxb.impl.runtime.XMLSerializable, org.openhealthtools.common.audit.jaxb.impl.runtime.ValidatableObject
{

    protected boolean has_ParticipantObjectTypeCodeRole;
    protected short _ParticipantObjectTypeCodeRole;
    protected java.lang.String _ParticipantObjectID;
    protected org.openhealthtools.common.audit.jaxb.ParticipantObjectIdentificationType.ParticipantObjectIDTypeCodeType _ParticipantObjectIDTypeCode;
    protected com.sun.xml.bind.util.ListImpl _ParticipantObjectDetail;
    protected java.lang.String _ParticipantObjectName;
    protected byte[] _ParticipantObjectQuery;
    protected boolean has_ParticipantObjectDataLifeCycle;
    protected short _ParticipantObjectDataLifeCycle;
    protected boolean has_ParticipantObjectTypeCode;
    protected short _ParticipantObjectTypeCode;
    protected java.lang.String _ParticipantObjectSensitivity;
    public final static java.lang.Class version = (org.openhealthtools.common.audit.jaxb.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (org.openhealthtools.common.audit.jaxb.ParticipantObjectIdentificationType.class);
    }

    public short getParticipantObjectTypeCodeRole() {
        return _ParticipantObjectTypeCodeRole;
    }

    public void setParticipantObjectTypeCodeRole(short value) {
        _ParticipantObjectTypeCodeRole = value;
        has_ParticipantObjectTypeCodeRole = true;
    }

    public java.lang.String getParticipantObjectID() {
        return _ParticipantObjectID;
    }

    public void setParticipantObjectID(java.lang.String value) {
        _ParticipantObjectID = value;
    }

    public org.openhealthtools.common.audit.jaxb.ParticipantObjectIdentificationType.ParticipantObjectIDTypeCodeType getParticipantObjectIDTypeCode() {
        return _ParticipantObjectIDTypeCode;
    }

    public void setParticipantObjectIDTypeCode(org.openhealthtools.common.audit.jaxb.ParticipantObjectIdentificationType.ParticipantObjectIDTypeCodeType value) {
        _ParticipantObjectIDTypeCode = value;
    }

    protected com.sun.xml.bind.util.ListImpl _getParticipantObjectDetail() {
        if (_ParticipantObjectDetail == null) {
            _ParticipantObjectDetail = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _ParticipantObjectDetail;
    }

    public java.util.List getParticipantObjectDetail() {
        return _getParticipantObjectDetail();
    }

    public java.lang.String getParticipantObjectName() {
        return _ParticipantObjectName;
    }

    public void setParticipantObjectName(java.lang.String value) {
        _ParticipantObjectName = value;
    }

    public byte[] getParticipantObjectQuery() {
        return _ParticipantObjectQuery;
    }

    public void setParticipantObjectQuery(byte[] value) {
        _ParticipantObjectQuery = value;
    }

    public short getParticipantObjectDataLifeCycle() {
        return _ParticipantObjectDataLifeCycle;
    }

    public void setParticipantObjectDataLifeCycle(short value) {
        _ParticipantObjectDataLifeCycle = value;
        has_ParticipantObjectDataLifeCycle = true;
    }

    public short getParticipantObjectTypeCode() {
        return _ParticipantObjectTypeCode;
    }

    public void setParticipantObjectTypeCode(short value) {
        _ParticipantObjectTypeCode = value;
        has_ParticipantObjectTypeCode = true;
    }

    public java.lang.String getParticipantObjectSensitivity() {
        return _ParticipantObjectSensitivity;
    }

    public void setParticipantObjectSensitivity(java.lang.String value) {
        _ParticipantObjectSensitivity = value;
    }

    public org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallingEventHandler createUnmarshaller(org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallingContext context) {
        return new org.openhealthtools.common.audit.jaxb.impl.ParticipantObjectIdentificationTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(org.openhealthtools.common.audit.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx4 = 0;
        final int len4 = ((_ParticipantObjectDetail == null)? 0 :_ParticipantObjectDetail.size());
        context.startElement("", "ParticipantObjectIDTypeCode");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _ParticipantObjectIDTypeCode), "ParticipantObjectIDTypeCode");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _ParticipantObjectIDTypeCode), "ParticipantObjectIDTypeCode");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _ParticipantObjectIDTypeCode), "ParticipantObjectIDTypeCode");
        context.endElement();
        if ((_ParticipantObjectName!= null)&&(_ParticipantObjectQuery == null)) {
            context.startElement("", "ParticipantObjectName");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _ParticipantObjectName), "ParticipantObjectName");
            } catch (java.lang.Exception e) {
                org.openhealthtools.common.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        } else {
            if ((_ParticipantObjectName == null)&&(_ParticipantObjectQuery!= null)) {
                context.startElement("", "ParticipantObjectQuery");
                context.endNamespaceDecls();
                context.endAttributes();
                try {
                    context.text(com.sun.msv.datatype.xsd.Base64BinaryType.save(((byte[]) _ParticipantObjectQuery)), "ParticipantObjectQuery");
                } catch (java.lang.Exception e) {
                    org.openhealthtools.common.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
                }
                context.endElement();
            }
        }
        while (idx4 != len4) {
            context.startElement("", "ParticipantObjectDetail");
            int idx_6 = idx4;
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _ParticipantObjectDetail.get(idx_6 ++)), "ParticipantObjectDetail");
            context.endNamespaceDecls();
            int idx_7 = idx4;
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _ParticipantObjectDetail.get(idx_7 ++)), "ParticipantObjectDetail");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _ParticipantObjectDetail.get(idx4 ++)), "ParticipantObjectDetail");
            context.endElement();
        }
    }

    public void serializeAttributes(org.openhealthtools.common.audit.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx4 = 0;
        final int len4 = ((_ParticipantObjectDetail == null)? 0 :_ParticipantObjectDetail.size());
        if (has_ParticipantObjectDataLifeCycle) {
            context.startAttribute("", "ParticipantObjectDataLifeCycle");
            try {
                context.text(javax.xml.bind.DatatypeConverter.printShort(((short) _ParticipantObjectDataLifeCycle)), "ParticipantObjectDataLifeCycle");
            } catch (java.lang.Exception e) {
                org.openhealthtools.common.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        context.startAttribute("", "ParticipantObjectID");
        try {
            context.text(((java.lang.String) _ParticipantObjectID), "ParticipantObjectID");
        } catch (java.lang.Exception e) {
            org.openhealthtools.common.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endAttribute();
        if (_ParticipantObjectSensitivity!= null) {
            context.startAttribute("", "ParticipantObjectSensitivity");
            try {
                context.text(((java.lang.String) _ParticipantObjectSensitivity), "ParticipantObjectSensitivity");
            } catch (java.lang.Exception e) {
                org.openhealthtools.common.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        if (has_ParticipantObjectTypeCode) {
            context.startAttribute("", "ParticipantObjectTypeCode");
            try {
                context.text(javax.xml.bind.DatatypeConverter.printShort(((short) _ParticipantObjectTypeCode)), "ParticipantObjectTypeCode");
            } catch (java.lang.Exception e) {
                org.openhealthtools.common.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        if (has_ParticipantObjectTypeCodeRole) {
            context.startAttribute("", "ParticipantObjectTypeCodeRole");
            try {
                context.text(javax.xml.bind.DatatypeConverter.printShort(((short) _ParticipantObjectTypeCodeRole)), "ParticipantObjectTypeCodeRole");
            } catch (java.lang.Exception e) {
                org.openhealthtools.common.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        while (idx4 != len4) {
            idx4 += 1;
        }
    }

    public void serializeURIs(org.openhealthtools.common.audit.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx4 = 0;
        final int len4 = ((_ParticipantObjectDetail == null)? 0 :_ParticipantObjectDetail.size());
        while (idx4 != len4) {
            idx4 += 1;
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (org.openhealthtools.common.audit.jaxb.ParticipantObjectIdentificationType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000pp"
+"sq\u0000~\u0000\u0000ppsr\u0000\'com.sun.msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001ecom.sun."
+"msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttribut"
+"esL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\fpp\u0000sr\u0000\u001dcom.sun."
+"msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com.sun.msv.gram"
+"mar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z"
+"\u0000\u0005valuexp\u0000psr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000"
+"\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\rxq\u0000~\u0000\u0003q\u0000~\u0000\u0018psr\u00002com.sun.msv.gramma"
+"r.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u0017\u0001q\u0000~\u0000"
+"\u001csr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun."
+"msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Ex"
+"pression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u0000\u001dq\u0000~\u0000\"sr\u0000#com"
+".sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNamet\u0000\u0012Lj"
+"ava/lang/String;L\u0000\fnamespaceURIq\u0000~\u0000$xq\u0000~\u0000\u001ft\u0000ncom.misyshealth"
+"care.connect.ihe.audit.jaxb.ParticipantObjectIdentificationT"
+"ype.ParticipantObjectIDTypeCodeTypet\u0000+http://java.sun.com/ja"
+"xb/xjc/dummy-elementssq\u0000~\u0000\u0012ppsq\u0000~\u0000\u0019q\u0000~\u0000\u0018psr\u0000\u001bcom.sun.msv.gra"
+"mmar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatyp"
+"e;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000"
+"\u0003ppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com."
+"sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun"
+".msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.dat"
+"atype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000$L\u0000\bty"
+"peNameq\u0000~\u0000$L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSp"
+"aceProcessor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0005QNamesr"
+"\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expression$NullSetExpression\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002"
+"L\u0000\tlocalNameq\u0000~\u0000$L\u0000\fnamespaceURIq\u0000~\u0000$xpq\u0000~\u00005q\u0000~\u00004sq\u0000~\u0000#t\u0000\u0004ty"
+"pet\u0000)http://www.w3.org/2001/XMLSchema-instanceq\u0000~\u0000\"sq\u0000~\u0000#t\u0000\u001b"
+"ParticipantObjectIDTypeCodet\u0000\u0000sq\u0000~\u0000\u0012ppsq\u0000~\u0000\u0012ppsq\u0000~\u0000\fq\u0000~\u0000\u0018p\u0000s"
+"q\u0000~\u0000\u0000ppsq\u0000~\u0000*ppsr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~\u0000/q\u0000~\u00004t\u0000\u0006stringsr\u00005com.sun.msv.dat"
+"atype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00007\u0001q\u0000~\u0000"
+":sq\u0000~\u0000;q\u0000~\u0000Jq\u0000~\u00004sq\u0000~\u0000\u0012ppsq\u0000~\u0000\u0019q\u0000~\u0000\u0018pq\u0000~\u0000-q\u0000~\u0000=q\u0000~\u0000\"sq\u0000~\u0000#t\u0000"
+"\u0015ParticipantObjectNameq\u0000~\u0000Bq\u0000~\u0000\"sq\u0000~\u0000\fpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000*ppsr\u0000"
+")com.sun.msv.datatype.xsd.Base64BinaryType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com"
+".sun.msv.datatype.xsd.BinaryBaseType\u00a7\u00ce\u000e\u0097^\u00afW\u0011\u0002\u0000\u0000xq\u0000~\u0000/q\u0000~\u00004t\u0000"
+"\fbase64Binaryq\u0000~\u00008q\u0000~\u0000:sq\u0000~\u0000;q\u0000~\u0000Xq\u0000~\u00004sq\u0000~\u0000\u0012ppsq\u0000~\u0000\u0019q\u0000~\u0000\u0018pq"
+"\u0000~\u0000-q\u0000~\u0000=q\u0000~\u0000\"sq\u0000~\u0000#t\u0000\u0016ParticipantObjectQueryq\u0000~\u0000Bsq\u0000~\u0000\u0012ppsq"
+"\u0000~\u0000\u0014q\u0000~\u0000\u0018psq\u0000~\u0000\fq\u0000~\u0000\u0018p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\fpp\u0000sq\u0000~\u0000\u0012ppsq\u0000~\u0000\u0014q\u0000~\u0000\u0018p"
+"sq\u0000~\u0000\u0019q\u0000~\u0000\u0018pq\u0000~\u0000\u001cq\u0000~\u0000 q\u0000~\u0000\"sq\u0000~\u0000#t\u0000<com.misyshealthcare.conn"
+"ect.ihe.audit.jaxb.TypeValuePairTypeq\u0000~\u0000\'sq\u0000~\u0000\u0012ppsq\u0000~\u0000\u0019q\u0000~\u0000\u0018"
+"pq\u0000~\u0000-q\u0000~\u0000=q\u0000~\u0000\"sq\u0000~\u0000#t\u0000\u0017ParticipantObjectDetailq\u0000~\u0000Bq\u0000~\u0000\"sq"
+"\u0000~\u0000\u0012ppsq\u0000~\u0000\u0019q\u0000~\u0000\u0018psq\u0000~\u0000*ppsr\u0000)com.sun.msv.datatype.xsd.Enume"
+"rationFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0006valuest\u0000\u000fLjava/util/Set;xr\u00009com.sun"
+".msv.datatype.xsd.DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000"
+"\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000"
+"\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv"
+"/datatype/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/"
+"datatype/xsd/ConcreteType;L\u0000\tfacetNameq\u0000~\u0000$xq\u0000~\u00001q\u0000~\u0000Bpq\u0000~\u00008"
+"\u0000\u0000sr\u0000)com.sun.msv.datatype.xsd.UnsignedByteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr"
+"\u0000\"com.sun.msv.datatype.xsd.ShortType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000+com.sun.m"
+"sv.datatype.xsd.IntegerDerivedType\u0099\u00f1]\u0090&6k\u00be\u0002\u0000\u0001L\u0000\nbaseFacetsq\u0000"
+"~\u0000sxq\u0000~\u0000/q\u0000~\u00004t\u0000\funsignedByteq\u0000~\u00008sr\u0000*com.sun.msv.datatype.x"
+"sd.MaxInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatype.xsd."
+"RangeFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\nlimitValuet\u0000\u0012Ljava/lang/Object;xq\u0000~\u0000"
+"qppq\u0000~\u00008\u0000\u0000sr\u0000*com.sun.msv.datatype.xsd.UnsignedShortType\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000 com.sun.msv.datatype.xsd.IntType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000x"
+"q\u0000~\u00004t\u0000\runsignedShortq\u0000~\u00008sq\u0000~\u0000{ppq\u0000~\u00008\u0000\u0000sr\u0000(com.sun.msv.dat"
+"atype.xsd.UnsignedIntType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000!com.sun.msv.datatype"
+".xsd.LongType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000xq\u0000~\u00004t\u0000\u000bunsignedIntq\u0000~\u00008sq\u0000~\u0000{"
+"ppq\u0000~\u00008\u0000\u0000sr\u0000)com.sun.msv.datatype.xsd.UnsignedLongType\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xr\u0000$com.sun.msv.datatype.xsd.IntegerType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~"
+"\u0000xq\u0000~\u00004t\u0000\funsignedLongq\u0000~\u00008sq\u0000~\u0000{ppq\u0000~\u00008\u0000\u0000sr\u0000/com.sun.msv.da"
+"tatype.xsd.NonNegativeIntegerType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u008aq\u0000~\u00004t\u0000\u0012no"
+"nNegativeIntegerq\u0000~\u00008sr\u0000*com.sun.msv.datatype.xsd.MinInclusi"
+"veFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000|ppq\u0000~\u00008\u0000\u0000sq\u0000~\u0000\u008aq\u0000~\u00004t\u0000\u0007integerq\u0000~\u00008s"
+"r\u0000,com.sun.msv.datatype.xsd.FractionDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000"
+"\u0005scalexr\u0000;com.sun.msv.datatype.xsd.DataTypeWithLexicalConstr"
+"aintFacetT\u0090\u001c>\u001azb\u00ea\u0002\u0000\u0000xq\u0000~\u0000rppq\u0000~\u00008\u0001\u0000sr\u0000#com.sun.msv.datatype."
+"xsd.NumberType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000/q\u0000~\u00004t\u0000\u0007decimalq\u0000~\u00008q\u0000~\u0000\u0099t\u0000\u000ef"
+"ractionDigits\u0000\u0000\u0000\u0000q\u0000~\u0000\u0093t\u0000\fminInclusivesr\u0000)com.sun.msv.datatyp"
+"e.xsd.IntegerValueType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0005valueq\u0000~\u0000$xr\u0000\u0010java.lang."
+"Number\u0086\u00ac\u0095\u001d\u000b\u0094\u00e0\u008b\u0002\u0000\u0000xpt\u0000\u00010q\u0000~\u0000\u008ft\u0000\fmaxInclusivesq\u0000~\u0000\u009dt\u0000\u001418446744"
+"073709551615q\u0000~\u0000\u008bq\u0000~\u0000\u00a1sr\u0000\u000ejava.lang.Long;\u008b\u00e4\u0090\u00cc\u008f#\u00df\u0002\u0000\u0001J\u0000\u0005valuex"
+"q\u0000~\u0000\u009e\u0000\u0000\u0000\u0000\u00ff\u00ff\u00ff\u00ffq\u0000~\u0000\u0086q\u0000~\u0000\u00a1sr\u0000\u0011java.lang.Integer\u0012\u00e2\u00a0\u00a4\u00f7\u0081\u00878\u0002\u0000\u0001I\u0000\u0005va"
+"luexq\u0000~\u0000\u009e\u0000\u0000\u00ff\u00ffq\u0000~\u0000\u0081q\u0000~\u0000\u00a1sr\u0000\u000fjava.lang.ShorthM7\u00134`\u00daR\u0002\u0000\u0001S\u0000\u0005valu"
+"exq\u0000~\u0000\u009e\u0000\u00ffq\u0000~\u0000yt\u0000\u000benumerationsr\u0000\u0011java.util.HashSet\u00baD\u0085\u0095\u0096\u00b8\u00b74\u0003\u0000\u0000"
+"xpw\f\u0000\u0000\u0000 ?@\u0000\u0000\u0000\u0000\u0000\u000fsq\u0000~\u0000\u00a8\u0000\u000fsq\u0000~\u0000\u00a8\u0000\u0004sq\u0000~\u0000\u00a8\u0000\bsq\u0000~\u0000\u00a8\u0000\u000bsq\u0000~\u0000\u00a8\u0000\u0003sq\u0000~"
+"\u0000\u00a8\u0000\u0007sq\u0000~\u0000\u00a8\u0000\fsq\u0000~\u0000\u00a8\u0000\u0002sq\u0000~\u0000\u00a8\u0000\rsq\u0000~\u0000\u00a8\u0000\tsq\u0000~\u0000\u00a8\u0000\u0006sq\u0000~\u0000\u00a8\u0000\u0001sq\u0000~\u0000\u00a8\u0000\u000e"
+"sq\u0000~\u0000\u00a8\u0000\nsq\u0000~\u0000\u00a8\u0000\u0005xq\u0000~\u0000:sq\u0000~\u0000;t\u0000\u0014unsignedByte-derivedq\u0000~\u0000Bsq\u0000~"
+"\u0000#t\u0000\u001eParticipantObjectDataLifeCycleq\u0000~\u0000Bq\u0000~\u0000\"sq\u0000~\u0000\u0019ppq\u0000~\u0000Gsq"
+"\u0000~\u0000#t\u0000\u0013ParticipantObjectIDq\u0000~\u0000Bsq\u0000~\u0000\u0012ppsq\u0000~\u0000\u0019q\u0000~\u0000\u0018pq\u0000~\u0000Gsq\u0000~"
+"\u0000#t\u0000\u001cParticipantObjectSensitivityq\u0000~\u0000Bq\u0000~\u0000\"sq\u0000~\u0000\u0012ppsq\u0000~\u0000\u0019q\u0000~"
+"\u0000\u0018psq\u0000~\u0000*ppsq\u0000~\u0000oq\u0000~\u0000Bpq\u0000~\u00008\u0000\u0000q\u0000~\u0000yq\u0000~\u0000yq\u0000~\u0000\u00aasq\u0000~\u0000\u00abw\f\u0000\u0000\u0000\u0010?@\u0000"
+"\u0000\u0000\u0000\u0000\u0004sq\u0000~\u0000\u00a8\u0000\u0002sq\u0000~\u0000\u00a8\u0000\u0004sq\u0000~\u0000\u00a8\u0000\u0001sq\u0000~\u0000\u00a8\u0000\u0003xq\u0000~\u0000:sq\u0000~\u0000;t\u0000\u0014unsigned"
+"Byte-derivedq\u0000~\u0000Bsq\u0000~\u0000#t\u0000\u0019ParticipantObjectTypeCodeq\u0000~\u0000Bq\u0000~\u0000"
+"\"sq\u0000~\u0000\u0012ppsq\u0000~\u0000\u0019q\u0000~\u0000\u0018psq\u0000~\u0000*ppsq\u0000~\u0000oq\u0000~\u0000Bpq\u0000~\u00008\u0000\u0000q\u0000~\u0000yq\u0000~\u0000yq\u0000"
+"~\u0000\u00aasq\u0000~\u0000\u00abw\f\u0000\u0000\u0000@?@\u0000\u0000\u0000\u0000\u0000\u0018sq\u0000~\u0000\u00a8\u0000\u000fsq\u0000~\u0000\u00a8\u0000\bsq\u0000~\u0000\u00a8\u0000\u0017sq\u0000~\u0000\u00a8\u0000\u0010sq\u0000~\u0000"
+"\u00a8\u0000\u0007sq\u0000~\u0000\u00a8\u0000\u0016sq\u0000~\u0000\u00a8\u0000\tsq\u0000~\u0000\u00a8\u0000\u0015sq\u0000~\u0000\u00a8\u0000\u0006sq\u0000~\u0000\u00a8\u0000\u0001sq\u0000~\u0000\u00a8\u0000\u000esq\u0000~\u0000\u00a8\u0000\u0018s"
+"q\u0000~\u0000\u00a8\u0000\u0004sq\u0000~\u0000\u00a8\u0000\u0013sq\u0000~\u0000\u00a8\u0000\u000bsq\u0000~\u0000\u00a8\u0000\u0012sq\u0000~\u0000\u00a8\u0000\u0003sq\u0000~\u0000\u00a8\u0000\fsq\u0000~\u0000\u00a8\u0000\u0011sq\u0000~\u0000"
+"\u00a8\u0000\u0002sq\u0000~\u0000\u00a8\u0000\rsq\u0000~\u0000\u00a8\u0000\u0014sq\u0000~\u0000\u00a8\u0000\nsq\u0000~\u0000\u00a8\u0000\u0005xq\u0000~\u0000:sq\u0000~\u0000;t\u0000\u0014unsignedBy"
+"te-derivedq\u0000~\u0000Bsq\u0000~\u0000#t\u0000\u001dParticipantObjectTypeCodeRoleq\u0000~\u0000Bq\u0000"
+"~\u0000\"sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTa"
+"blet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-c"
+"om.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005co"
+"untB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/Expressi"
+"onPool;xp\u0000\u0000\u0000\u001b\u0001pq\u0000~\u0000_q\u0000~\u0000\bq\u0000~\u0000\u0016q\u0000~\u0000dq\u0000~\u0000\u0010q\u0000~\u0000aq\u0000~\u0000(q\u0000~\u0000Nq\u0000~\u0000Z"
+"q\u0000~\u0000hq\u0000~\u0000\u0013q\u0000~\u0000cq\u0000~\u0000^q\u0000~\u0000Sq\u0000~\u0000Fq\u0000~\u0000\tq\u0000~\u0000\u00c3q\u0000~\u0000\nq\u0000~\u0000\u000bq\u0000~\u0000\u0007q\u0000~\u0000D"
+"q\u0000~\u0000\u0005q\u0000~\u0000\u00c7q\u0000~\u0000\u00d4q\u0000~\u0000\u0006q\u0000~\u0000Cq\u0000~\u0000lx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public static class ParticipantObjectIDTypeCodeTypeImpl
        extends org.openhealthtools.common.audit.jaxb.impl.CodedValueTypeImpl
        implements org.openhealthtools.common.audit.jaxb.ParticipantObjectIdentificationType.ParticipantObjectIDTypeCodeType, com.sun.xml.bind.JAXBObject, org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallableObject, org.openhealthtools.common.audit.jaxb.impl.runtime.XMLSerializable, org.openhealthtools.common.audit.jaxb.impl.runtime.ValidatableObject
    {

        public final static java.lang.Class version = (org.openhealthtools.common.audit.jaxb.impl.JAXBVersion.class);
        private static com.sun.msv.grammar.Grammar schemaFragment;

        private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
            return (org.openhealthtools.common.audit.jaxb.ParticipantObjectIdentificationType.ParticipantObjectIDTypeCodeType.class);
        }

        public org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallingEventHandler createUnmarshaller(org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallingContext context) {
            return new org.openhealthtools.common.audit.jaxb.impl.ParticipantObjectIdentificationTypeImpl.ParticipantObjectIDTypeCodeTypeImpl.Unmarshaller(context);
        }

        public void serializeBody(org.openhealthtools.common.audit.jaxb.impl.runtime.XMLSerializer context)
            throws org.xml.sax.SAXException
        {
            super.serializeBody(context);
        }

        public void serializeAttributes(org.openhealthtools.common.audit.jaxb.impl.runtime.XMLSerializer context)
            throws org.xml.sax.SAXException
        {
            super.serializeAttributes(context);
        }

        public void serializeURIs(org.openhealthtools.common.audit.jaxb.impl.runtime.XMLSerializer context)
            throws org.xml.sax.SAXException
        {
            super.serializeURIs(context);
        }

        public java.lang.Class getPrimaryInterface() {
            return (org.openhealthtools.common.audit.jaxb.ParticipantObjectIdentificationType.ParticipantObjectIDTypeCodeType.class);
        }

        public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
            if (schemaFragment == null) {
                schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000 com."
+"sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameCla"
+"sst\u0000\u001fLcom/sun/msv/grammar/NameClass;xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv."
+"grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Data"
+"type;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq"
+"\u0000~\u0000\u0003ppsr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\ri"
+"sAlwaysValidxr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fna"
+"mespaceUrit\u0000\u0012Ljava/lang/String;L\u0000\btypeNameq\u0000~\u0000\u0015L\u0000\nwhiteSpace"
+"t\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http:/"
+"/www.w3.org/2001/XMLSchemat\u0000\u0006stringsr\u00005com.sun.msv.datatype."
+"xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.d"
+"atatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0001sr\u00000com.sun.msv"
+".grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001b"
+"com.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0015L\u0000\fna"
+"mespaceURIq\u0000~\u0000\u0015xpq\u0000~\u0000\u0019q\u0000~\u0000\u0018sr\u0000#com.sun.msv.grammar.SimpleNam"
+"eClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0015L\u0000\fnamespaceURIq\u0000~\u0000\u0015xr\u0000\u001dco"
+"m.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004codet\u0000\u0000sr\u0000\u001dcom.su"
+"n.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsq\u0000~\u0000\nsr\u0000\u0011java.lan"
+"g.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000pq\u0000~\u0000\u0010sq\u0000~\u0000!t\u0000\u000bdisplayNameq\u0000~"
+"\u0000%sr\u00000com.sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000)\u0001q\u0000~\u0000.sq\u0000~\u0000&ppsq\u0000~\u0000\nq\u0000~\u0000*pq\u0000~\u0000\u0010sq\u0000~\u0000!t\u0000\fori"
+"ginalTextq\u0000~\u0000%q\u0000~\u0000.sq\u0000~\u0000&ppsq\u0000~\u0000\nq\u0000~\u0000*psq\u0000~\u0000\rppsr\u0000(com.sun.m"
+"sv.datatype.xsd.WhiteSpaceFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.da"
+"tatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012nee"
+"dValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype/xsd/XSDat"
+"atypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/Concre"
+"teType;L\u0000\tfacetNameq\u0000~\u0000\u0015xq\u0000~\u0000\u0014q\u0000~\u0000%t\u0000\u0003OIDsr\u00005com.sun.msv.dat"
+"atype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001b\u0000\u0000q\u0000~"
+"\u0000\u0017q\u0000~\u0000\u0017t\u0000\nwhiteSpaceq\u0000~\u0000\u001esq\u0000~\u0000\u001fq\u0000~\u0000<q\u0000~\u0000%sq\u0000~\u0000!t\u0000\ncodeSystem"
+"q\u0000~\u0000%q\u0000~\u0000.sq\u0000~\u0000&ppsq\u0000~\u0000\nq\u0000~\u0000*pq\u0000~\u0000\u0010sq\u0000~\u0000!t\u0000\u000ecodeSystemNameq\u0000"
+"~\u0000%q\u0000~\u0000.sq\u0000~\u0000\nppsq\u0000~\u0000\rppsr\u0000)com.sun.msv.datatype.xsd.Enumera"
+"tionFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0006valuest\u0000\u000fLjava/util/Set;xr\u00009com.sun.m"
+"sv.datatype.xsd.DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000x"
+"q\u0000~\u00008q\u0000~\u0000%pq\u0000~\u0000\u001c\u0000\u0000q\u0000~\u0000\u0017q\u0000~\u0000\u0017t\u0000\u000benumerationsr\u0000\u0011java.util.Hash"
+"Set\u00baD\u0085\u0095\u0096\u00b8\u00b74\u0003\u0000\u0000xpw\f\u0000\u0000\u0000 ?@\u0000\u0000\u0000\u0000\u0000\rt\u0000\u00013t\u0000\u00017t\u0000\u00012t\u0000\u00016t\u0000\u00011t\u0000\u000210t\u0000\u00015t"
+"\u0000\u000211t\u0000\u00019t\u0000\u00014t\u0000\u00018t\u0000\u000212q\u0000~\u0000%xq\u0000~\u0000\u001epsq\u0000~\u0000!t\u0000\u0004codeq\u0000~\u0000%sr\u0000\"com.s"
+"un.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/s"
+"un/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.g"
+"rammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstream"
+"VersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000"
+"\t\u0001pq\u0000~\u0000\u0007q\u0000~\u00000q\u0000~\u0000Cq\u0000~\u00004q\u0000~\u0000\u0005q\u0000~\u0000\'q\u0000~\u0000\u0006q\u0000~\u0000\bq\u0000~\u0000\tx"));
            }
            return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
        }

        public class Unmarshaller
            extends org.openhealthtools.common.audit.jaxb.impl.runtime.AbstractUnmarshallingEventHandlerImpl
        {


            public Unmarshaller(org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallingContext context) {
                super(context, "--");
            }

            protected Unmarshaller(org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallingContext context, int startState) {
                this(context);
                state = startState;
            }

            public java.lang.Object owner() {
                return org.openhealthtools.common.audit.jaxb.impl.ParticipantObjectIdentificationTypeImpl.ParticipantObjectIDTypeCodeTypeImpl.this;
            }

            public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
                throws org.xml.sax.SAXException
            {
                int attIdx;
                outer:
                while (true) {
                    switch (state) {
                        case  0 :
                            attIdx = context.getAttribute("", "codeSystem");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                                return ;
                            }
                            attIdx = context.getAttribute("", "codeSystemName");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                                return ;
                            }
                            attIdx = context.getAttribute("", "code");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                                return ;
                            }
                            break;
                        case  1 :
                            revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                            return ;
                    }
                    super.enterElement(___uri, ___local, ___qname, __atts);
                    break;
                }
            }

            public void leaveElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
                throws org.xml.sax.SAXException
            {
                int attIdx;
                outer:
                while (true) {
                    switch (state) {
                        case  0 :
                            attIdx = context.getAttribute("", "codeSystem");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                                return ;
                            }
                            attIdx = context.getAttribute("", "codeSystemName");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                                return ;
                            }
                            attIdx = context.getAttribute("", "code");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                                return ;
                            }
                            break;
                        case  1 :
                            revertToParentFromLeaveElement(___uri, ___local, ___qname);
                            return ;
                    }
                    super.leaveElement(___uri, ___local, ___qname);
                    break;
                }
            }

            public void enterAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
                throws org.xml.sax.SAXException
            {
                int attIdx;
                outer:
                while (true) {
                    switch (state) {
                        case  0 :
                            if (("codeSystem" == ___local)&&("" == ___uri)) {
                                spawnHandlerFromEnterAttribute((((org.openhealthtools.common.audit.jaxb.impl.CodedValueTypeImpl)org.openhealthtools.common.audit.jaxb.impl.ParticipantObjectIdentificationTypeImpl.ParticipantObjectIDTypeCodeTypeImpl.this).new Unmarshaller(context)), 1, ___uri, ___local, ___qname);
                                return ;
                            }
                            if (("codeSystemName" == ___local)&&("" == ___uri)) {
                                spawnHandlerFromEnterAttribute((((org.openhealthtools.common.audit.jaxb.impl.CodedValueTypeImpl)org.openhealthtools.common.audit.jaxb.impl.ParticipantObjectIdentificationTypeImpl.ParticipantObjectIDTypeCodeTypeImpl.this).new Unmarshaller(context)), 1, ___uri, ___local, ___qname);
                                return ;
                            }
                            if (("code" == ___local)&&("" == ___uri)) {
                                spawnHandlerFromEnterAttribute((((org.openhealthtools.common.audit.jaxb.impl.CodedValueTypeImpl)org.openhealthtools.common.audit.jaxb.impl.ParticipantObjectIdentificationTypeImpl.ParticipantObjectIDTypeCodeTypeImpl.this).new Unmarshaller(context)), 1, ___uri, ___local, ___qname);
                                return ;
                            }
                            break;
                        case  1 :
                            revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                            return ;
                    }
                    super.enterAttribute(___uri, ___local, ___qname);
                    break;
                }
            }

            public void leaveAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
                throws org.xml.sax.SAXException
            {
                int attIdx;
                outer:
                while (true) {
                    switch (state) {
                        case  0 :
                            attIdx = context.getAttribute("", "codeSystem");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                                return ;
                            }
                            attIdx = context.getAttribute("", "codeSystemName");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                                return ;
                            }
                            attIdx = context.getAttribute("", "code");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                                return ;
                            }
                            break;
                        case  1 :
                            revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                            return ;
                    }
                    super.leaveAttribute(___uri, ___local, ___qname);
                    break;
                }
            }

            public void handleText(final java.lang.String value)
                throws org.xml.sax.SAXException
            {
                int attIdx;
                outer:
                while (true) {
                    try {
                        switch (state) {
                            case  0 :
                                attIdx = context.getAttribute("", "codeSystem");
                                if (attIdx >= 0) {
                                    context.consumeAttribute(attIdx);
                                    context.getCurrentHandler().text(value);
                                    return ;
                                }
                                attIdx = context.getAttribute("", "codeSystemName");
                                if (attIdx >= 0) {
                                    context.consumeAttribute(attIdx);
                                    context.getCurrentHandler().text(value);
                                    return ;
                                }
                                attIdx = context.getAttribute("", "code");
                                if (attIdx >= 0) {
                                    context.consumeAttribute(attIdx);
                                    context.getCurrentHandler().text(value);
                                    return ;
                                }
                                break;
                            case  1 :
                                revertToParentFromText(value);
                                return ;
                        }
                    } catch (java.lang.RuntimeException e) {
                        handleUnexpectedTextException(value, e);
                    }
                    break;
                }
            }

        }

    }

    public class Unmarshaller
        extends org.openhealthtools.common.audit.jaxb.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallingContext context) {
            super(context, "---------------------------");
        }

        protected Unmarshaller(org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return org.openhealthtools.common.audit.jaxb.impl.ParticipantObjectIdentificationTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  24 :
                        if (("ParticipantObjectDetail" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 22;
                            return ;
                        }
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  16 :
                        attIdx = context.getAttribute("", "codeSystem");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        attIdx = context.getAttribute("", "codeSystemName");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        attIdx = context.getAttribute("", "code");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  18 :
                        if (("ParticipantObjectName" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 25;
                            return ;
                        }
                        if (("ParticipantObjectQuery" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 19;
                            return ;
                        }
                        state = 21;
                        continue outer;
                    case  21 :
                        if (("ParticipantObjectDetail" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 22;
                            return ;
                        }
                        state = 24;
                        continue outer;
                    case  12 :
                        attIdx = context.getAttribute("", "ParticipantObjectTypeCodeRole");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 15;
                            continue outer;
                        }
                        state = 15;
                        continue outer;
                    case  9 :
                        attIdx = context.getAttribute("", "ParticipantObjectTypeCode");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 12;
                            continue outer;
                        }
                        state = 12;
                        continue outer;
                    case  3 :
                        attIdx = context.getAttribute("", "ParticipantObjectID");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText3(v);
                            state = 6;
                            continue outer;
                        }
                        break;
                    case  22 :
                        attIdx = context.getAttribute("", "type");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  15 :
                        if (("ParticipantObjectIDTypeCode" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 16;
                            return ;
                        }
                        break;
                    case  0 :
                        attIdx = context.getAttribute("", "ParticipantObjectDataLifeCycle");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText4(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case  6 :
                        attIdx = context.getAttribute("", "ParticipantObjectSensitivity");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText5(v);
                            state = 9;
                            continue outer;
                        }
                        state = 9;
                        continue outer;
                }
                super.enterElement(___uri, ___local, ___qname, __atts);
                break;
            }
        }

        private void eatText1(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ParticipantObjectTypeCodeRole = javax.xml.bind.DatatypeConverter.parseShort(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_ParticipantObjectTypeCodeRole = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ParticipantObjectTypeCode = javax.xml.bind.DatatypeConverter.parseShort(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_ParticipantObjectTypeCode = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ParticipantObjectID = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ParticipantObjectDataLifeCycle = javax.xml.bind.DatatypeConverter.parseShort(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_ParticipantObjectDataLifeCycle = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText5(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ParticipantObjectSensitivity = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  17 :
                        if (("ParticipantObjectIDTypeCode" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 18;
                            return ;
                        }
                        break;
                    case  26 :
                        if (("ParticipantObjectName" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 21;
                            return ;
                        }
                        break;
                    case  20 :
                        if (("ParticipantObjectQuery" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 21;
                            return ;
                        }
                        break;
                    case  24 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  16 :
                        attIdx = context.getAttribute("", "codeSystem");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("", "codeSystemName");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("", "code");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  21 :
                        state = 24;
                        continue outer;
                    case  23 :
                        if (("ParticipantObjectDetail" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 24;
                            return ;
                        }
                        break;
                    case  12 :
                        attIdx = context.getAttribute("", "ParticipantObjectTypeCodeRole");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 15;
                            continue outer;
                        }
                        state = 15;
                        continue outer;
                    case  9 :
                        attIdx = context.getAttribute("", "ParticipantObjectTypeCode");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 12;
                            continue outer;
                        }
                        state = 12;
                        continue outer;
                    case  3 :
                        attIdx = context.getAttribute("", "ParticipantObjectID");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText3(v);
                            state = 6;
                            continue outer;
                        }
                        break;
                    case  22 :
                        attIdx = context.getAttribute("", "type");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
                    case  0 :
                        attIdx = context.getAttribute("", "ParticipantObjectDataLifeCycle");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText4(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case  6 :
                        attIdx = context.getAttribute("", "ParticipantObjectSensitivity");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText5(v);
                            state = 9;
                            continue outer;
                        }
                        state = 9;
                        continue outer;
                }
                super.leaveElement(___uri, ___local, ___qname);
                break;
            }
        }

        public void enterAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  24 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  16 :
                        if (("codeSystem" == ___local)&&("" == ___uri)) {
                            _ParticipantObjectIDTypeCode = ((org.openhealthtools.common.audit.jaxb.impl.ParticipantObjectIdentificationTypeImpl.ParticipantObjectIDTypeCodeTypeImpl) spawnChildFromEnterAttribute((org.openhealthtools.common.audit.jaxb.impl.ParticipantObjectIdentificationTypeImpl.ParticipantObjectIDTypeCodeTypeImpl.class), 17, ___uri, ___local, ___qname));
                            return ;
                        }
                        if (("codeSystemName" == ___local)&&("" == ___uri)) {
                            _ParticipantObjectIDTypeCode = ((org.openhealthtools.common.audit.jaxb.impl.ParticipantObjectIdentificationTypeImpl.ParticipantObjectIDTypeCodeTypeImpl) spawnChildFromEnterAttribute((org.openhealthtools.common.audit.jaxb.impl.ParticipantObjectIdentificationTypeImpl.ParticipantObjectIDTypeCodeTypeImpl.class), 17, ___uri, ___local, ___qname));
                            return ;
                        }
                        if (("code" == ___local)&&("" == ___uri)) {
                            _ParticipantObjectIDTypeCode = ((org.openhealthtools.common.audit.jaxb.impl.ParticipantObjectIdentificationTypeImpl.ParticipantObjectIDTypeCodeTypeImpl) spawnChildFromEnterAttribute((org.openhealthtools.common.audit.jaxb.impl.ParticipantObjectIdentificationTypeImpl.ParticipantObjectIDTypeCodeTypeImpl.class), 17, ___uri, ___local, ___qname));
                            return ;
                        }
                        break;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  21 :
                        state = 24;
                        continue outer;
                    case  12 :
                        if (("ParticipantObjectTypeCodeRole" == ___local)&&("" == ___uri)) {
                            state = 13;
                            return ;
                        }
                        state = 15;
                        continue outer;
                    case  9 :
                        if (("ParticipantObjectTypeCode" == ___local)&&("" == ___uri)) {
                            state = 10;
                            return ;
                        }
                        state = 12;
                        continue outer;
                    case  3 :
                        if (("ParticipantObjectID" == ___local)&&("" == ___uri)) {
                            state = 4;
                            return ;
                        }
                        break;
                    case  22 :
                        if (("type" == ___local)&&("" == ___uri)) {
                            _getParticipantObjectDetail().add(((org.openhealthtools.common.audit.jaxb.impl.TypeValuePairTypeImpl) spawnChildFromEnterAttribute((org.openhealthtools.common.audit.jaxb.impl.TypeValuePairTypeImpl.class), 23, ___uri, ___local, ___qname)));
                            return ;
                        }
                        break;
                    case  0 :
                        if (("ParticipantObjectDataLifeCycle" == ___local)&&("" == ___uri)) {
                            state = 1;
                            return ;
                        }
                        state = 3;
                        continue outer;
                    case  6 :
                        if (("ParticipantObjectSensitivity" == ___local)&&("" == ___uri)) {
                            state = 7;
                            return ;
                        }
                        state = 9;
                        continue outer;
                }
                super.enterAttribute(___uri, ___local, ___qname);
                break;
            }
        }

        public void leaveAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  14 :
                        if (("ParticipantObjectTypeCodeRole" == ___local)&&("" == ___uri)) {
                            state = 15;
                            return ;
                        }
                        break;
                    case  8 :
                        if (("ParticipantObjectSensitivity" == ___local)&&("" == ___uri)) {
                            state = 9;
                            return ;
                        }
                        break;
                    case  24 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  16 :
                        attIdx = context.getAttribute("", "codeSystem");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("", "codeSystemName");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("", "code");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  21 :
                        state = 24;
                        continue outer;
                    case  12 :
                        attIdx = context.getAttribute("", "ParticipantObjectTypeCodeRole");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 15;
                            continue outer;
                        }
                        state = 15;
                        continue outer;
                    case  9 :
                        attIdx = context.getAttribute("", "ParticipantObjectTypeCode");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 12;
                            continue outer;
                        }
                        state = 12;
                        continue outer;
                    case  3 :
                        attIdx = context.getAttribute("", "ParticipantObjectID");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText3(v);
                            state = 6;
                            continue outer;
                        }
                        break;
                    case  22 :
                        attIdx = context.getAttribute("", "type");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
                    case  2 :
                        if (("ParticipantObjectDataLifeCycle" == ___local)&&("" == ___uri)) {
                            state = 3;
                            return ;
                        }
                        break;
                    case  5 :
                        if (("ParticipantObjectID" == ___local)&&("" == ___uri)) {
                            state = 6;
                            return ;
                        }
                        break;
                    case  0 :
                        attIdx = context.getAttribute("", "ParticipantObjectDataLifeCycle");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText4(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case  6 :
                        attIdx = context.getAttribute("", "ParticipantObjectSensitivity");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText5(v);
                            state = 9;
                            continue outer;
                        }
                        state = 9;
                        continue outer;
                    case  11 :
                        if (("ParticipantObjectTypeCode" == ___local)&&("" == ___uri)) {
                            state = 12;
                            return ;
                        }
                        break;
                }
                super.leaveAttribute(___uri, ___local, ___qname);
                break;
            }
        }

        public void handleText(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                try {
                    switch (state) {
                        case  4 :
                            eatText3(value);
                            state = 5;
                            return ;
                        case  25 :
                            eatText6(value);
                            state = 26;
                            return ;
                        case  13 :
                            eatText1(value);
                            state = 14;
                            return ;
                        case  24 :
                            revertToParentFromText(value);
                            return ;
                        case  16 :
                            attIdx = context.getAttribute("", "codeSystem");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            attIdx = context.getAttribute("", "codeSystemName");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            attIdx = context.getAttribute("", "code");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            break;
                        case  19 :
                            eatText7(value);
                            state = 20;
                            return ;
                        case  18 :
                            state = 21;
                            continue outer;
                        case  21 :
                            state = 24;
                            continue outer;
                        case  12 :
                            attIdx = context.getAttribute("", "ParticipantObjectTypeCodeRole");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText1(v);
                                state = 15;
                                continue outer;
                            }
                            state = 15;
                            continue outer;
                        case  9 :
                            attIdx = context.getAttribute("", "ParticipantObjectTypeCode");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText2(v);
                                state = 12;
                                continue outer;
                            }
                            state = 12;
                            continue outer;
                        case  3 :
                            attIdx = context.getAttribute("", "ParticipantObjectID");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText3(v);
                                state = 6;
                                continue outer;
                            }
                            break;
                        case  22 :
                            attIdx = context.getAttribute("", "type");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            break;
                        case  7 :
                            eatText5(value);
                            state = 8;
                            return ;
                        case  0 :
                            attIdx = context.getAttribute("", "ParticipantObjectDataLifeCycle");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText4(v);
                                state = 3;
                                continue outer;
                            }
                            state = 3;
                            continue outer;
                        case  1 :
                            eatText4(value);
                            state = 2;
                            return ;
                        case  10 :
                            eatText2(value);
                            state = 11;
                            return ;
                        case  6 :
                            attIdx = context.getAttribute("", "ParticipantObjectSensitivity");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText5(v);
                                state = 9;
                                continue outer;
                            }
                            state = 9;
                            continue outer;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

        private void eatText6(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ParticipantObjectName = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText7(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ParticipantObjectQuery = com.sun.msv.datatype.xsd.Base64BinaryType.load(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
