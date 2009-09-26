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
package org.openhealthtools.common.audit.jaxb.impl;

public class AuditMessageTypeImpl implements org.openhealthtools.common.audit.jaxb.AuditMessageType, com.sun.xml.bind.JAXBObject, org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallableObject, org.openhealthtools.common.audit.jaxb.impl.runtime.XMLSerializable, org.openhealthtools.common.audit.jaxb.impl.runtime.ValidatableObject
{

    protected com.sun.xml.bind.util.ListImpl _AuditSourceIdentification;
    protected com.sun.xml.bind.util.ListImpl _ParticipantObjectIdentification;
    protected com.sun.xml.bind.util.ListImpl _ActiveParticipant;
    protected org.openhealthtools.common.audit.jaxb.EventIdentificationType _EventIdentification;
    public final static java.lang.Class version = (org.openhealthtools.common.audit.jaxb.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (org.openhealthtools.common.audit.jaxb.AuditMessageType.class);
    }

    protected com.sun.xml.bind.util.ListImpl _getAuditSourceIdentification() {
        if (_AuditSourceIdentification == null) {
            _AuditSourceIdentification = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _AuditSourceIdentification;
    }

    public java.util.List getAuditSourceIdentification() {
        return _getAuditSourceIdentification();
    }

    protected com.sun.xml.bind.util.ListImpl _getParticipantObjectIdentification() {
        if (_ParticipantObjectIdentification == null) {
            _ParticipantObjectIdentification = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _ParticipantObjectIdentification;
    }

    public java.util.List getParticipantObjectIdentification() {
        return _getParticipantObjectIdentification();
    }

    protected com.sun.xml.bind.util.ListImpl _getActiveParticipant() {
        if (_ActiveParticipant == null) {
            _ActiveParticipant = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _ActiveParticipant;
    }

    public java.util.List getActiveParticipant() {
        return _getActiveParticipant();
    }

    public org.openhealthtools.common.audit.jaxb.EventIdentificationType getEventIdentification() {
        return _EventIdentification;
    }

    public void setEventIdentification(org.openhealthtools.common.audit.jaxb.EventIdentificationType value) {
        _EventIdentification = value;
    }

    public org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallingEventHandler createUnmarshaller(org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallingContext context) {
        return new org.openhealthtools.common.audit.jaxb.impl.AuditMessageTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(org.openhealthtools.common.audit.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_AuditSourceIdentification == null)? 0 :_AuditSourceIdentification.size());
        int idx2 = 0;
        final int len2 = ((_ParticipantObjectIdentification == null)? 0 :_ParticipantObjectIdentification.size());
        int idx3 = 0;
        final int len3 = ((_ActiveParticipant == null)? 0 :_ActiveParticipant.size());
        context.startElement("", "EventIdentification");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _EventIdentification), "EventIdentification");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _EventIdentification), "EventIdentification");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _EventIdentification), "EventIdentification");
        context.endElement();
        while (idx3 != len3) {
            context.startElement("", "ActiveParticipant");
            int idx_2 = idx3;
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _ActiveParticipant.get(idx_2 ++)), "ActiveParticipant");
            context.endNamespaceDecls();
            int idx_3 = idx3;
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _ActiveParticipant.get(idx_3 ++)), "ActiveParticipant");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _ActiveParticipant.get(idx3 ++)), "ActiveParticipant");
            context.endElement();
        }
        while (idx1 != len1) {
            context.startElement("", "AuditSourceIdentification");
            int idx_4 = idx1;
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _AuditSourceIdentification.get(idx_4 ++)), "AuditSourceIdentification");
            context.endNamespaceDecls();
            int idx_5 = idx1;
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _AuditSourceIdentification.get(idx_5 ++)), "AuditSourceIdentification");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _AuditSourceIdentification.get(idx1 ++)), "AuditSourceIdentification");
            context.endElement();
        }
        while (idx2 != len2) {
            context.startElement("", "ParticipantObjectIdentification");
            int idx_6 = idx2;
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _ParticipantObjectIdentification.get(idx_6 ++)), "ParticipantObjectIdentification");
            context.endNamespaceDecls();
            int idx_7 = idx2;
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _ParticipantObjectIdentification.get(idx_7 ++)), "ParticipantObjectIdentification");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _ParticipantObjectIdentification.get(idx2 ++)), "ParticipantObjectIdentification");
            context.endElement();
        }
    }

    public void serializeAttributes(org.openhealthtools.common.audit.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_AuditSourceIdentification == null)? 0 :_AuditSourceIdentification.size());
        int idx2 = 0;
        final int len2 = ((_ParticipantObjectIdentification == null)? 0 :_ParticipantObjectIdentification.size());
        int idx3 = 0;
        final int len3 = ((_ActiveParticipant == null)? 0 :_ActiveParticipant.size());
        while (idx3 != len3) {
            idx3 += 1;
        }
        while (idx1 != len1) {
            idx1 += 1;
        }
        while (idx2 != len2) {
            idx2 += 1;
        }
    }

    public void serializeURIs(org.openhealthtools.common.audit.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_AuditSourceIdentification == null)? 0 :_AuditSourceIdentification.size());
        int idx2 = 0;
        final int len2 = ((_ParticipantObjectIdentification == null)? 0 :_ParticipantObjectIdentification.size());
        int idx3 = 0;
        final int len3 = ((_ActiveParticipant == null)? 0 :_ActiveParticipant.size());
        while (idx3 != len3) {
            idx3 += 1;
        }
        while (idx1 != len1) {
            idx1 += 1;
        }
        while (idx2 != len2) {
            idx2 += 1;
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (org.openhealthtools.common.audit.jaxb.AuditMessageType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000\'com.sun.msv.grammar."
+"trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/gr"
+"ammar/NameClass;xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000s"
+"q\u0000~\u0000\u0000ppsq\u0000~\u0000\bpp\u0000sr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xq\u0000~\u0000\u0001ppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001cc"
+"om.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011j"
+"ava.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000 com.sun.msv.gramm"
+"ar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\txq\u0000~\u0000\u0003"
+"q\u0000~\u0000\u0014psr\u00002com.sun.msv.grammar.Expression$AnyStringExpression"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u0013\u0001q\u0000~\u0000\u0018sr\u0000 com.sun.msv.grammar.AnyName"
+"Class\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xpsr\u00000com.sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u0000\u0019q\u0000~\u0000\u001esr\u0000#com.sun.msv.grammar.SimpleNameClass"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNamet\u0000\u0012Ljava/lang/String;L\u0000\fnamespaceURIq"
+"\u0000~\u0000 xq\u0000~\u0000\u001bt\u0000Bcom.misyshealthcare.connect.ihe.audit.jaxb.Even"
+"tIdentificationTypet\u0000+http://java.sun.com/jaxb/xjc/dummy-ele"
+"mentssq\u0000~\u0000\u000eppsq\u0000~\u0000\u0015q\u0000~\u0000\u0014psr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002"
+"L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000\"com.sun.m"
+"sv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype"
+".xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xs"
+"d.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSData"
+"typeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000 L\u0000\btypeNameq\u0000~\u0000 L\u0000\nwh"
+"iteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt"
+"\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0005QNamesr\u00005com.sun.msv.da"
+"tatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.su"
+"n.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.s"
+"un.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003"
+"ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000"
+" L\u0000\fnamespaceURIq\u0000~\u0000 xpq\u0000~\u00001q\u0000~\u00000sq\u0000~\u0000\u001ft\u0000\u0004typet\u0000)http://www."
+"w3.org/2001/XMLSchema-instanceq\u0000~\u0000\u001esq\u0000~\u0000\u001ft\u0000\u0013EventIdentificat"
+"iont\u0000\u0000sq\u0000~\u0000\u0010ppsq\u0000~\u0000\bpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\bpp\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0010q\u0000~\u0000\u0014p"
+"sq\u0000~\u0000\u0015q\u0000~\u0000\u0014pq\u0000~\u0000\u0018q\u0000~\u0000\u001cq\u0000~\u0000\u001esq\u0000~\u0000\u001ft\u0000Qcom.misyshealthcare.conn"
+"ect.ihe.audit.jaxb.AuditMessageType.ActiveParticipantTypeq\u0000~"
+"\u0000#sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0015q\u0000~\u0000\u0014pq\u0000~\u0000)q\u0000~\u00009q\u0000~\u0000\u001esq\u0000~\u0000\u001ft\u0000\u0011ActiveParticip"
+"antq\u0000~\u0000>sq\u0000~\u0000\u0010ppsq\u0000~\u0000\bpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\bpp\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0010q\u0000~\u0000"
+"\u0014psq\u0000~\u0000\u0015q\u0000~\u0000\u0014pq\u0000~\u0000\u0018q\u0000~\u0000\u001cq\u0000~\u0000\u001esq\u0000~\u0000\u001ft\u0000Hcom.misyshealthcare.co"
+"nnect.ihe.audit.jaxb.AuditSourceIdentificationTypeq\u0000~\u0000#sq\u0000~\u0000"
+"\u000eppsq\u0000~\u0000\u0015q\u0000~\u0000\u0014pq\u0000~\u0000)q\u0000~\u00009q\u0000~\u0000\u001esq\u0000~\u0000\u001ft\u0000\u0019AuditSourceIdentifica"
+"tionq\u0000~\u0000>sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0010q\u0000~\u0000\u0014psq\u0000~\u0000\bq\u0000~\u0000\u0014p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\bpp\u0000s"
+"q\u0000~\u0000\u000eppsq\u0000~\u0000\u0010q\u0000~\u0000\u0014psq\u0000~\u0000\u0015q\u0000~\u0000\u0014pq\u0000~\u0000\u0018q\u0000~\u0000\u001cq\u0000~\u0000\u001esq\u0000~\u0000\u001ft\u0000Ncom.m"
+"isyshealthcare.connect.ihe.audit.jaxb.ParticipantObjectIdent"
+"ificationTypeq\u0000~\u0000#sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0015q\u0000~\u0000\u0014pq\u0000~\u0000)q\u0000~\u00009q\u0000~\u0000\u001esq\u0000~\u0000\u001ft"
+"\u0000\u001fParticipantObjectIdentificationq\u0000~\u0000>q\u0000~\u0000\u001esr\u0000\"com.sun.msv.g"
+"rammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/g"
+"rammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.E"
+"xpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL"
+"\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\u0017\u0001pq\u0000~\u0000?"
+"q\u0000~\u0000Lq\u0000~\u0000Zq\u0000~\u0000\u0006q\u0000~\u0000\u0012q\u0000~\u0000Dq\u0000~\u0000Qq\u0000~\u0000_q\u0000~\u0000\fq\u0000~\u0000Aq\u0000~\u0000Nq\u0000~\u0000\\q\u0000~\u0000$"
+"q\u0000~\u0000Hq\u0000~\u0000Uq\u0000~\u0000cq\u0000~\u0000\u000fq\u0000~\u0000Cq\u0000~\u0000Pq\u0000~\u0000^q\u0000~\u0000Yq\u0000~\u0000\u0007q\u0000~\u0000\u0005x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public static class ActiveParticipantTypeImpl
        extends org.openhealthtools.common.audit.jaxb.impl.ActiveParticipantTypeImpl
        implements org.openhealthtools.common.audit.jaxb.AuditMessageType.ActiveParticipantType, com.sun.xml.bind.JAXBObject, org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallableObject, org.openhealthtools.common.audit.jaxb.impl.runtime.XMLSerializable, org.openhealthtools.common.audit.jaxb.impl.runtime.ValidatableObject
    {

        public final static java.lang.Class version = (org.openhealthtools.common.audit.jaxb.impl.JAXBVersion.class);
        private static com.sun.msv.grammar.Grammar schemaFragment;

        private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
            return (org.openhealthtools.common.audit.jaxb.AuditMessageType.ActiveParticipantType.class);
        }

        public org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallingEventHandler createUnmarshaller(org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallingContext context) {
            return new org.openhealthtools.common.audit.jaxb.impl.AuditMessageTypeImpl.ActiveParticipantTypeImpl.Unmarshaller(context);
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
            return (org.openhealthtools.common.audit.jaxb.AuditMessageType.ActiveParticipantType.class);
        }

        public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
            if (schemaFragment == null) {
                schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000pp"
+"sr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com."
+"sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.gramm"
+"ar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean"
+"\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000\'com.sun.msv.grammar.trex.ElementP"
+"attern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/grammar/NameCla"
+"ss;xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUnd"
+"eclaredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003q\u0000~\u0000\u0011p\u0000sq\u0000~\u0000\u0000ppsq"
+"\u0000~\u0000\u0012pp\u0000sq\u0000~\u0000\u000bppsq\u0000~\u0000\rq\u0000~\u0000\u0011psr\u0000 com.sun.msv.grammar.Attribute"
+"Exp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\u0013xq\u0000~\u0000\u0003q\u0000~\u0000\u0011psr\u00002co"
+"m.sun.msv.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"q\u0000~\u0000\u0003sq\u0000~\u0000\u0010\u0001q\u0000~\u0000\u001dsr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.su"
+"n.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q"
+"\u0000~\u0000\u001eq\u0000~\u0000#sr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L"
+"\u0000\tlocalNamet\u0000\u0012Ljava/lang/String;L\u0000\fnamespaceURIq\u0000~\u0000%xq\u0000~\u0000 t\u0000"
+"9com.misyshealthcare.connect.ihe.audit.jaxb.CodedValueTypet\u0000"
+"+http://java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000\u000bppsq\u0000~\u0000\u001aq\u0000"
+"~\u0000\u0011psr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/r"
+"elaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/ms"
+"v/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000\"com.sun.msv.datatype.xsd.Qname"
+"Type\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicTyp"
+"e\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000"
+"\fnamespaceUriq\u0000~\u0000%L\u0000\btypeNameq\u0000~\u0000%L\u0000\nwhiteSpacet\u0000.Lcom/sun/m"
+"sv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http://www.w3.org/2"
+"001/XMLSchemat\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.WhiteSpace"
+"Processor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.Wh"
+"iteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expres"
+"sion$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.uti"
+"l.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000%L\u0000\fnamespaceURIq\u0000~\u0000%"
+"xpq\u0000~\u00006q\u0000~\u00005sq\u0000~\u0000$t\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema"
+"-instanceq\u0000~\u0000#sq\u0000~\u0000$t\u0000\nRoleIDCodet\u0000\u0000q\u0000~\u0000#sq\u0000~\u0000\u000bppsq\u0000~\u0000\u001aq\u0000~\u0000\u0011"
+"psq\u0000~\u0000+ppsr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z"
+"\u0000\risAlwaysValidxq\u0000~\u00000q\u0000~\u00005t\u0000\u0006stringsr\u00005com.sun.msv.datatype."
+"xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00008\u0001q\u0000~\u0000;sq\u0000~\u0000"
+"<q\u0000~\u0000Iq\u0000~\u00005sq\u0000~\u0000$t\u0000\u0011AlternativeUserIDq\u0000~\u0000Cq\u0000~\u0000#sq\u0000~\u0000\u000bppsq\u0000~\u0000"
+"\u001aq\u0000~\u0000\u0011pq\u0000~\u0000Fsq\u0000~\u0000$t\u0000\u0014NetworkAccessPointIDq\u0000~\u0000Cq\u0000~\u0000#sq\u0000~\u0000\u000bpps"
+"q\u0000~\u0000\u001aq\u0000~\u0000\u0011psq\u0000~\u0000+ppsr\u0000)com.sun.msv.datatype.xsd.EnumerationF"
+"acet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0006valuest\u0000\u000fLjava/util/Set;xr\u00009com.sun.msv.da"
+"tatype.xsd.DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*co"
+"m.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFace"
+"tFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/dataty"
+"pe/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatyp"
+"e/xsd/ConcreteType;L\u0000\tfacetNameq\u0000~\u0000%xq\u0000~\u00002q\u0000~\u0000Cpq\u0000~\u00009\u0000\u0000sr\u0000)c"
+"om.sun.msv.datatype.xsd.UnsignedByteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\"com.s"
+"un.msv.datatype.xsd.ShortType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000+com.sun.msv.data"
+"type.xsd.IntegerDerivedType\u0099\u00f1]\u0090&6k\u00be\u0002\u0000\u0001L\u0000\nbaseFacetsq\u0000~\u0000Zxq\u0000~"
+"\u00000q\u0000~\u00005t\u0000\funsignedByteq\u0000~\u00009sr\u0000*com.sun.msv.datatype.xsd.MaxI"
+"nclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatype.xsd.RangeFa"
+"cet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\nlimitValuet\u0000\u0012Ljava/lang/Object;xq\u0000~\u0000Xppq\u0000~\u0000"
+"9\u0000\u0000sr\u0000*com.sun.msv.datatype.xsd.UnsignedShortType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xr\u0000 com.sun.msv.datatype.xsd.IntType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000_q\u0000~\u00005t\u0000"
+"\runsignedShortq\u0000~\u00009sq\u0000~\u0000bppq\u0000~\u00009\u0000\u0000sr\u0000(com.sun.msv.datatype.x"
+"sd.UnsignedIntType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000!com.sun.msv.datatype.xsd.Lo"
+"ngType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000_q\u0000~\u00005t\u0000\u000bunsignedIntq\u0000~\u00009sq\u0000~\u0000bppq\u0000~\u00009"
+"\u0000\u0000sr\u0000)com.sun.msv.datatype.xsd.UnsignedLongType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr"
+"\u0000$com.sun.msv.datatype.xsd.IntegerType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000_q\u0000~\u00005"
+"t\u0000\funsignedLongq\u0000~\u00009sq\u0000~\u0000bppq\u0000~\u00009\u0000\u0000sr\u0000/com.sun.msv.datatype."
+"xsd.NonNegativeIntegerType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000qq\u0000~\u00005t\u0000\u0012nonNegati"
+"veIntegerq\u0000~\u00009sr\u0000*com.sun.msv.datatype.xsd.MinInclusiveFacet"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000cppq\u0000~\u00009\u0000\u0000sq\u0000~\u0000qq\u0000~\u00005t\u0000\u0007integerq\u0000~\u00009sr\u0000,com."
+"sun.msv.datatype.xsd.FractionDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005scalex"
+"r\u0000;com.sun.msv.datatype.xsd.DataTypeWithLexicalConstraintFac"
+"etT\u0090\u001c>\u001azb\u00ea\u0002\u0000\u0000xq\u0000~\u0000Yppq\u0000~\u00009\u0001\u0000sr\u0000#com.sun.msv.datatype.xsd.Num"
+"berType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00000q\u0000~\u00005t\u0000\u0007decimalq\u0000~\u00009q\u0000~\u0000\u0080t\u0000\u000efraction"
+"Digits\u0000\u0000\u0000\u0000q\u0000~\u0000zt\u0000\fminInclusivesr\u0000)com.sun.msv.datatype.xsd.I"
+"ntegerValueType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0005valueq\u0000~\u0000%xr\u0000\u0010java.lang.Number\u0086"
+"\u00ac\u0095\u001d\u000b\u0094\u00e0\u008b\u0002\u0000\u0000xpt\u0000\u00010q\u0000~\u0000vt\u0000\fmaxInclusivesq\u0000~\u0000\u0084t\u0000\u0014184467440737095"
+"51615q\u0000~\u0000rq\u0000~\u0000\u0088sr\u0000\u000ejava.lang.Long;\u008b\u00e4\u0090\u00cc\u008f#\u00df\u0002\u0000\u0001J\u0000\u0005valuexq\u0000~\u0000\u0085\u0000\u0000"
+"\u0000\u0000\u00ff\u00ff\u00ff\u00ffq\u0000~\u0000mq\u0000~\u0000\u0088sr\u0000\u0011java.lang.Integer\u0012\u00e2\u00a0\u00a4\u00f7\u0081\u00878\u0002\u0000\u0001I\u0000\u0005valuexq\u0000~"
+"\u0000\u0085\u0000\u0000\u00ff\u00ffq\u0000~\u0000hq\u0000~\u0000\u0088sr\u0000\u000fjava.lang.ShorthM7\u00134`\u00daR\u0002\u0000\u0001S\u0000\u0005valuexq\u0000~\u0000\u0085"
+"\u0000\u00ffq\u0000~\u0000`t\u0000\u000benumerationsr\u0000\u0011java.util.HashSet\u00baD\u0085\u0095\u0096\u00b8\u00b74\u0003\u0000\u0000xpw\f\u0000\u0000\u0000"
+"\u0010?@\u0000\u0000\u0000\u0000\u0000\u0003sq\u0000~\u0000\u008f\u0000\u0002sq\u0000~\u0000\u008f\u0000\u0001sq\u0000~\u0000\u008f\u0000\u0003xq\u0000~\u0000;sq\u0000~\u0000<t\u0000\u0014unsignedByte"
+"-derivedq\u0000~\u0000Csq\u0000~\u0000$t\u0000\u001aNetworkAccessPointTypeCodeq\u0000~\u0000Cq\u0000~\u0000#sq"
+"\u0000~\u0000\u001appq\u0000~\u0000Fsq\u0000~\u0000$t\u0000\u0006UserIDq\u0000~\u0000Csq\u0000~\u0000\u000bppsq\u0000~\u0000\u001aq\u0000~\u0000\u0011psq\u0000~\u0000+pps"
+"r\u0000$com.sun.msv.datatype.xsd.BooleanType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00000q\u0000~\u0000"
+"5t\u0000\u0007booleanq\u0000~\u00009q\u0000~\u0000;sq\u0000~\u0000<q\u0000~\u0000\u00a3q\u0000~\u00005sq\u0000~\u0000$t\u0000\u000fUserIsRequesto"
+"rq\u0000~\u0000Cq\u0000~\u0000#sq\u0000~\u0000\u000bppsq\u0000~\u0000\u001aq\u0000~\u0000\u0011pq\u0000~\u0000Fsq\u0000~\u0000$t\u0000\bUserNameq\u0000~\u0000Cq\u0000"
+"~\u0000#sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTa"
+"blet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-c"
+"om.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005co"
+"untB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/Expressi"
+"onPool;xp\u0000\u0000\u0000\u0011\u0001pq\u0000~\u0000\u000fq\u0000~\u0000\u00a7q\u0000~\u0000\u0019q\u0000~\u0000\u0016q\u0000~\u0000)q\u0000~\u0000\u0018q\u0000~\u0000\fq\u0000~\u0000Sq\u0000~\u0000O"
+"q\u0000~\u0000\u009eq\u0000~\u0000\u0007q\u0000~\u0000Dq\u0000~\u0000\nq\u0000~\u0000\u0006q\u0000~\u0000\tq\u0000~\u0000\bq\u0000~\u0000\u0005x"));
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
                return org.openhealthtools.common.audit.jaxb.impl.AuditMessageTypeImpl.ActiveParticipantTypeImpl.this;
            }

            public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
                throws org.xml.sax.SAXException
            {
                int attIdx;
                outer:
                while (true) {
                    switch (state) {
                        case  1 :
                            revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        case  0 :
                            attIdx = context.getAttribute("", "AlternativeUserID");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                                return ;
                            }
                            attIdx = context.getAttribute("", "NetworkAccessPointID");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                                return ;
                            }
                            attIdx = context.getAttribute("", "NetworkAccessPointTypeCode");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                                return ;
                            }
                            attIdx = context.getAttribute("", "UserID");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                                return ;
                            }
                            break;
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
                        case  1 :
                            revertToParentFromLeaveElement(___uri, ___local, ___qname);
                            return ;
                        case  0 :
                            attIdx = context.getAttribute("", "AlternativeUserID");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                                return ;
                            }
                            attIdx = context.getAttribute("", "NetworkAccessPointID");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                                return ;
                            }
                            attIdx = context.getAttribute("", "NetworkAccessPointTypeCode");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                                return ;
                            }
                            attIdx = context.getAttribute("", "UserID");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                                return ;
                            }
                            break;
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
                        case  1 :
                            revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                            return ;
                        case  0 :
                            if (("AlternativeUserID" == ___local)&&("" == ___uri)) {
                                spawnHandlerFromEnterAttribute((((org.openhealthtools.common.audit.jaxb.impl.ActiveParticipantTypeImpl)org.openhealthtools.common.audit.jaxb.impl.AuditMessageTypeImpl.ActiveParticipantTypeImpl.this).new Unmarshaller(context)), 1, ___uri, ___local, ___qname);
                                return ;
                            }
                            if (("NetworkAccessPointID" == ___local)&&("" == ___uri)) {
                                spawnHandlerFromEnterAttribute((((org.openhealthtools.common.audit.jaxb.impl.ActiveParticipantTypeImpl)org.openhealthtools.common.audit.jaxb.impl.AuditMessageTypeImpl.ActiveParticipantTypeImpl.this).new Unmarshaller(context)), 1, ___uri, ___local, ___qname);
                                return ;
                            }
                            if (("NetworkAccessPointTypeCode" == ___local)&&("" == ___uri)) {
                                spawnHandlerFromEnterAttribute((((org.openhealthtools.common.audit.jaxb.impl.ActiveParticipantTypeImpl)org.openhealthtools.common.audit.jaxb.impl.AuditMessageTypeImpl.ActiveParticipantTypeImpl.this).new Unmarshaller(context)), 1, ___uri, ___local, ___qname);
                                return ;
                            }
                            if (("UserID" == ___local)&&("" == ___uri)) {
                                spawnHandlerFromEnterAttribute((((org.openhealthtools.common.audit.jaxb.impl.ActiveParticipantTypeImpl)org.openhealthtools.common.audit.jaxb.impl.AuditMessageTypeImpl.ActiveParticipantTypeImpl.this).new Unmarshaller(context)), 1, ___uri, ___local, ___qname);
                                return ;
                            }
                            break;
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
                        case  1 :
                            revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                            return ;
                        case  0 :
                            attIdx = context.getAttribute("", "AlternativeUserID");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                                return ;
                            }
                            attIdx = context.getAttribute("", "NetworkAccessPointID");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                                return ;
                            }
                            attIdx = context.getAttribute("", "NetworkAccessPointTypeCode");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                                return ;
                            }
                            attIdx = context.getAttribute("", "UserID");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
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
                            case  1 :
                                revertToParentFromText(value);
                                return ;
                            case  0 :
                                attIdx = context.getAttribute("", "AlternativeUserID");
                                if (attIdx >= 0) {
                                    context.consumeAttribute(attIdx);
                                    context.getCurrentHandler().text(value);
                                    return ;
                                }
                                attIdx = context.getAttribute("", "NetworkAccessPointID");
                                if (attIdx >= 0) {
                                    context.consumeAttribute(attIdx);
                                    context.getCurrentHandler().text(value);
                                    return ;
                                }
                                attIdx = context.getAttribute("", "NetworkAccessPointTypeCode");
                                if (attIdx >= 0) {
                                    context.consumeAttribute(attIdx);
                                    context.getCurrentHandler().text(value);
                                    return ;
                                }
                                attIdx = context.getAttribute("", "UserID");
                                if (attIdx >= 0) {
                                    context.consumeAttribute(attIdx);
                                    context.getCurrentHandler().text(value);
                                    return ;
                                }
                                break;
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
            super(context, "-------------");
        }

        protected Unmarshaller(org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return org.openhealthtools.common.audit.jaxb.impl.AuditMessageTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  1 :
                        attIdx = context.getAttribute("", "EventActionCode");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        attIdx = context.getAttribute("", "EventDateTime");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  7 :
                        attIdx = context.getAttribute("", "AuditEnterpriseSiteID");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        attIdx = context.getAttribute("", "AuditSourceID");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  0 :
                        if (("EventIdentification" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        break;
                    case  4 :
                        attIdx = context.getAttribute("", "AlternativeUserID");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        attIdx = context.getAttribute("", "NetworkAccessPointID");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        attIdx = context.getAttribute("", "NetworkAccessPointTypeCode");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        attIdx = context.getAttribute("", "UserID");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  9 :
                        if (("AuditSourceIdentification" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 7;
                            return ;
                        }
                        if (("ParticipantObjectIdentification" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 10;
                            return ;
                        }
                        state = 12;
                        continue outer;
                    case  10 :
                        attIdx = context.getAttribute("", "ParticipantObjectDataLifeCycle");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        attIdx = context.getAttribute("", "ParticipantObjectID");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  3 :
                        if (("ActiveParticipant" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 4;
                            return ;
                        }
                        break;
                    case  6 :
                        if (("ActiveParticipant" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 4;
                            return ;
                        }
                        if (("AuditSourceIdentification" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 7;
                            return ;
                        }
                        break;
                    case  12 :
                        if (("ParticipantObjectIdentification" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 10;
                            return ;
                        }
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
                    case  5 :
                        if (("ActiveParticipant" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  1 :
                        attIdx = context.getAttribute("", "EventActionCode");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("", "EventDateTime");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
                    case  7 :
                        attIdx = context.getAttribute("", "AuditEnterpriseSiteID");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("", "AuditSourceID");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
                    case  4 :
                        attIdx = context.getAttribute("", "AlternativeUserID");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("", "NetworkAccessPointID");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("", "NetworkAccessPointTypeCode");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("", "UserID");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  10 :
                        attIdx = context.getAttribute("", "ParticipantObjectDataLifeCycle");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("", "ParticipantObjectID");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
                    case  8 :
                        if (("AuditSourceIdentification" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  2 :
                        if (("EventIdentification" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  11 :
                        if (("ParticipantObjectIdentification" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  12 :
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
                    case  1 :
                        if (("EventActionCode" == ___local)&&("" == ___uri)) {
                            _EventIdentification = ((org.openhealthtools.common.audit.jaxb.impl.EventIdentificationTypeImpl) spawnChildFromEnterAttribute((org.openhealthtools.common.audit.jaxb.impl.EventIdentificationTypeImpl.class), 2, ___uri, ___local, ___qname));
                            return ;
                        }
                        if (("EventDateTime" == ___local)&&("" == ___uri)) {
                            _EventIdentification = ((org.openhealthtools.common.audit.jaxb.impl.EventIdentificationTypeImpl) spawnChildFromEnterAttribute((org.openhealthtools.common.audit.jaxb.impl.EventIdentificationTypeImpl.class), 2, ___uri, ___local, ___qname));
                            return ;
                        }
                        break;
                    case  7 :
                        if (("AuditEnterpriseSiteID" == ___local)&&("" == ___uri)) {
                            _getAuditSourceIdentification().add(((org.openhealthtools.common.audit.jaxb.impl.AuditSourceIdentificationTypeImpl) spawnChildFromEnterAttribute((org.openhealthtools.common.audit.jaxb.impl.AuditSourceIdentificationTypeImpl.class), 8, ___uri, ___local, ___qname)));
                            return ;
                        }
                        if (("AuditSourceID" == ___local)&&("" == ___uri)) {
                            _getAuditSourceIdentification().add(((org.openhealthtools.common.audit.jaxb.impl.AuditSourceIdentificationTypeImpl) spawnChildFromEnterAttribute((org.openhealthtools.common.audit.jaxb.impl.AuditSourceIdentificationTypeImpl.class), 8, ___uri, ___local, ___qname)));
                            return ;
                        }
                        break;
                    case  4 :
                        if (("AlternativeUserID" == ___local)&&("" == ___uri)) {
                            _getActiveParticipant().add(((org.openhealthtools.common.audit.jaxb.impl.AuditMessageTypeImpl.ActiveParticipantTypeImpl) spawnChildFromEnterAttribute((org.openhealthtools.common.audit.jaxb.impl.AuditMessageTypeImpl.ActiveParticipantTypeImpl.class), 5, ___uri, ___local, ___qname)));
                            return ;
                        }
                        if (("NetworkAccessPointID" == ___local)&&("" == ___uri)) {
                            _getActiveParticipant().add(((org.openhealthtools.common.audit.jaxb.impl.AuditMessageTypeImpl.ActiveParticipantTypeImpl) spawnChildFromEnterAttribute((org.openhealthtools.common.audit.jaxb.impl.AuditMessageTypeImpl.ActiveParticipantTypeImpl.class), 5, ___uri, ___local, ___qname)));
                            return ;
                        }
                        if (("NetworkAccessPointTypeCode" == ___local)&&("" == ___uri)) {
                            _getActiveParticipant().add(((org.openhealthtools.common.audit.jaxb.impl.AuditMessageTypeImpl.ActiveParticipantTypeImpl) spawnChildFromEnterAttribute((org.openhealthtools.common.audit.jaxb.impl.AuditMessageTypeImpl.ActiveParticipantTypeImpl.class), 5, ___uri, ___local, ___qname)));
                            return ;
                        }
                        if (("UserID" == ___local)&&("" == ___uri)) {
                            _getActiveParticipant().add(((org.openhealthtools.common.audit.jaxb.impl.AuditMessageTypeImpl.ActiveParticipantTypeImpl) spawnChildFromEnterAttribute((org.openhealthtools.common.audit.jaxb.impl.AuditMessageTypeImpl.ActiveParticipantTypeImpl.class), 5, ___uri, ___local, ___qname)));
                            return ;
                        }
                        break;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  10 :
                        if (("ParticipantObjectDataLifeCycle" == ___local)&&("" == ___uri)) {
                            _getParticipantObjectIdentification().add(((org.openhealthtools.common.audit.jaxb.impl.ParticipantObjectIdentificationTypeImpl) spawnChildFromEnterAttribute((org.openhealthtools.common.audit.jaxb.impl.ParticipantObjectIdentificationTypeImpl.class), 11, ___uri, ___local, ___qname)));
                            return ;
                        }
                        if (("ParticipantObjectID" == ___local)&&("" == ___uri)) {
                            _getParticipantObjectIdentification().add(((org.openhealthtools.common.audit.jaxb.impl.ParticipantObjectIdentificationTypeImpl) spawnChildFromEnterAttribute((org.openhealthtools.common.audit.jaxb.impl.ParticipantObjectIdentificationTypeImpl.class), 11, ___uri, ___local, ___qname)));
                            return ;
                        }
                        break;
                    case  12 :
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
                    case  1 :
                        attIdx = context.getAttribute("", "EventActionCode");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("", "EventDateTime");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
                    case  7 :
                        attIdx = context.getAttribute("", "AuditEnterpriseSiteID");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("", "AuditSourceID");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
                    case  4 :
                        attIdx = context.getAttribute("", "AlternativeUserID");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("", "NetworkAccessPointID");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("", "NetworkAccessPointTypeCode");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("", "UserID");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  10 :
                        attIdx = context.getAttribute("", "ParticipantObjectDataLifeCycle");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("", "ParticipantObjectID");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
                    case  12 :
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
                        case  1 :
                            attIdx = context.getAttribute("", "EventActionCode");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            attIdx = context.getAttribute("", "EventDateTime");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            break;
                        case  7 :
                            attIdx = context.getAttribute("", "AuditEnterpriseSiteID");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            attIdx = context.getAttribute("", "AuditSourceID");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            break;
                        case  4 :
                            attIdx = context.getAttribute("", "AlternativeUserID");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            attIdx = context.getAttribute("", "NetworkAccessPointID");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            attIdx = context.getAttribute("", "NetworkAccessPointTypeCode");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            attIdx = context.getAttribute("", "UserID");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            break;
                        case  9 :
                            state = 12;
                            continue outer;
                        case  10 :
                            attIdx = context.getAttribute("", "ParticipantObjectDataLifeCycle");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            attIdx = context.getAttribute("", "ParticipantObjectID");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            break;
                        case  12 :
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
