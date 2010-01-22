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

public class AuditSourceIdentificationTypeImpl implements org.openhealthtools.common.audit.jaxb.AuditSourceIdentificationType, com.sun.xml.bind.JAXBObject, org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallableObject, org.openhealthtools.common.audit.jaxb.impl.runtime.XMLSerializable, org.openhealthtools.common.audit.jaxb.impl.runtime.ValidatableObject
{

    protected java.lang.String _AuditEnterpriseSiteID;
    protected java.lang.String _AuditSourceID;
    protected com.sun.xml.bind.util.ListImpl _AuditSourceTypeCode;
    public final static java.lang.Class version = (org.openhealthtools.common.audit.jaxb.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (org.openhealthtools.common.audit.jaxb.AuditSourceIdentificationType.class);
    }

    public java.lang.String getAuditEnterpriseSiteID() {
        return _AuditEnterpriseSiteID;
    }

    public void setAuditEnterpriseSiteID(java.lang.String value) {
        _AuditEnterpriseSiteID = value;
    }

    public java.lang.String getAuditSourceID() {
        return _AuditSourceID;
    }

    public void setAuditSourceID(java.lang.String value) {
        _AuditSourceID = value;
    }

    protected com.sun.xml.bind.util.ListImpl _getAuditSourceTypeCode() {
        if (_AuditSourceTypeCode == null) {
            _AuditSourceTypeCode = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _AuditSourceTypeCode;
    }

    public java.util.List getAuditSourceTypeCode() {
        return _getAuditSourceTypeCode();
    }

    public org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallingEventHandler createUnmarshaller(org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallingContext context) {
        return new org.openhealthtools.common.audit.jaxb.impl.AuditSourceIdentificationTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(org.openhealthtools.common.audit.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx3 = 0;
        final int len3 = ((_AuditSourceTypeCode == null)? 0 :_AuditSourceTypeCode.size());
        while (idx3 != len3) {
            context.startElement("", "AuditSourceTypeCode");
            int idx_0 = idx3;
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _AuditSourceTypeCode.get(idx_0 ++)), "AuditSourceTypeCode");
            context.endNamespaceDecls();
            int idx_1 = idx3;
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _AuditSourceTypeCode.get(idx_1 ++)), "AuditSourceTypeCode");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _AuditSourceTypeCode.get(idx3 ++)), "AuditSourceTypeCode");
            context.endElement();
        }
    }

    public void serializeAttributes(org.openhealthtools.common.audit.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx3 = 0;
        final int len3 = ((_AuditSourceTypeCode == null)? 0 :_AuditSourceTypeCode.size());
        if (_AuditEnterpriseSiteID!= null) {
            context.startAttribute("", "AuditEnterpriseSiteID");
            try {
                context.text(((java.lang.String) _AuditEnterpriseSiteID), "AuditEnterpriseSiteID");
            } catch (java.lang.Exception e) {
                org.openhealthtools.common.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        context.startAttribute("", "AuditSourceID");
        try {
            context.text(((java.lang.String) _AuditSourceID), "AuditSourceID");
        } catch (java.lang.Exception e) {
            org.openhealthtools.common.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endAttribute();
        while (idx3 != len3) {
            idx3 += 1;
        }
    }

    public void serializeURIs(org.openhealthtools.common.audit.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx3 = 0;
        final int len3 = ((_AuditSourceTypeCode == null)? 0 :_AuditSourceTypeCode.size());
        while (idx3 != len3) {
            idx3 += 1;
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (org.openhealthtools.common.audit.jaxb.AuditSourceIdentificationType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsr\u0000\u001dcom.sun.msv.grammar.ChoiceEx"
+"p\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000"
+"\u0002xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000\'com.s"
+"un.msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001f"
+"Lcom/sun/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv.grammar.Eleme"
+"ntExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000\fcontentModel"
+"q\u0000~\u0000\u0002xq\u0000~\u0000\u0003q\u0000~\u0000\rp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u000epp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\tq\u0000~\u0000\rpsr\u0000 c"
+"om.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tname"
+"Classq\u0000~\u0000\u000fxq\u0000~\u0000\u0003q\u0000~\u0000\rpsr\u00002com.sun.msv.grammar.Expression$Any"
+"StringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\f\u0001q\u0000~\u0000\u0019sr\u0000 com.sun.msv"
+".grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.Name"
+"Class\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expression$Epsilon"
+"Expression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u0000\u001aq\u0000~\u0000\u001fsr\u0000#com.sun.msv.grammar"
+".SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNamet\u0000\u0012Ljava/lang/String;"
+"L\u0000\fnamespaceURIq\u0000~\u0000!xq\u0000~\u0000\u001ct\u0000`org.openhealthexchange.common"
+".audit.jaxb.AuditSourceIdentificationType.AuditSourceTypeCod"
+"eTypet\u0000+http://java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000\u0007pps"
+"q\u0000~\u0000\u0016q\u0000~\u0000\rpsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000"
+"\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom"
+"/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000\"com.sun.msv.datatype.xs"
+"d.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.BuiltinAt"
+"omicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000!L\u0000\btypeNameq\u0000~\u0000!L\u0000\nwhiteSpacet\u0000.Lco"
+"m/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http://www.w"
+"3.org/2001/XMLSchemat\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.Whi"
+"teSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype"
+".xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar"
+".Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun."
+"msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000!L\u0000\fnamespaceU"
+"RIq\u0000~\u0000!xpq\u0000~\u00002q\u0000~\u00001sq\u0000~\u0000 t\u0000\u0004typet\u0000)http://www.w3.org/2001/XM"
+"LSchema-instanceq\u0000~\u0000\u001fsq\u0000~\u0000 t\u0000\u0013AuditSourceTypeCodet\u0000\u0000q\u0000~\u0000\u001fsq\u0000"
+"~\u0000\u0007ppsq\u0000~\u0000\u0016q\u0000~\u0000\rpsq\u0000~\u0000\'ppsr\u0000#com.sun.msv.datatype.xsd.String"
+"Type\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~\u0000,q\u0000~\u00001t\u0000\u0006stringsr\u00005com.s"
+"un.msv.datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"q\u0000~\u00004\u0001q\u0000~\u00007sq\u0000~\u00008q\u0000~\u0000Eq\u0000~\u00001sq\u0000~\u0000 t\u0000\u0015AuditEnterpriseSiteIDq\u0000~"
+"\u0000?q\u0000~\u0000\u001fsq\u0000~\u0000\u0016ppq\u0000~\u0000Bsq\u0000~\u0000 t\u0000\rAuditSourceIDq\u0000~\u0000?sr\u0000\"com.sun.m"
+"sv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/m"
+"sv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.gramm"
+"ar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVers"
+"ionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\t\u0001pq"
+"\u0000~\u0000\u000bq\u0000~\u0000@q\u0000~\u0000\u0006q\u0000~\u0000\u0015q\u0000~\u0000\u0012q\u0000~\u0000%q\u0000~\u0000\u0014q\u0000~\u0000\bq\u0000~\u0000\u0005x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public static class AuditSourceTypeCodeTypeImpl
        extends org.openhealthtools.common.audit.jaxb.impl.CodedValueTypeImpl
        implements org.openhealthtools.common.audit.jaxb.AuditSourceIdentificationType.AuditSourceTypeCodeType, com.sun.xml.bind.JAXBObject, org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallableObject, org.openhealthtools.common.audit.jaxb.impl.runtime.XMLSerializable, org.openhealthtools.common.audit.jaxb.impl.runtime.ValidatableObject
    {

        public final static java.lang.Class version = (org.openhealthtools.common.audit.jaxb.impl.JAXBVersion.class);
        private static com.sun.msv.grammar.Grammar schemaFragment;

        private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
            return (org.openhealthtools.common.audit.jaxb.AuditSourceIdentificationType.AuditSourceTypeCodeType.class);
        }

        public org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallingEventHandler createUnmarshaller(org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallingContext context) {
            return new org.openhealthtools.common.audit.jaxb.impl.AuditSourceIdentificationTypeImpl.AuditSourceTypeCodeTypeImpl.Unmarshaller(context);
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
            return (org.openhealthtools.common.audit.jaxb.AuditSourceIdentificationType.AuditSourceTypeCodeType.class);
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
+"Set\u00baD\u0085\u0095\u0096\u00b8\u00b74\u0003\u0000\u0000xpw\f\u0000\u0000\u0000\u0010?@\u0000\u0000\u0000\u0000\u0000\tt\u0000\u00013t\u0000\u00015t\u0000\u00017t\u0000\u00012t\u0000\u00019t\u0000\u00014t\u0000\u00018t\u0000"
+"\u00016t\u0000\u00011xq\u0000~\u0000\u001epsq\u0000~\u0000!t\u0000\u0004codeq\u0000~\u0000%sr\u0000\"com.sun.msv.grammar.Expre"
+"ssionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/Expre"
+"ssionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPoo"
+"l$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$L"
+"com/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\t\u0001pq\u0000~\u0000Cq\u0000~\u0000\bq\u0000~\u00004q\u0000"
+"~\u0000\u0005q\u0000~\u0000\u0007q\u0000~\u0000\'q\u0000~\u00000q\u0000~\u0000\tq\u0000~\u0000\u0006x"));
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
                return org.openhealthtools.common.audit.jaxb.impl.AuditSourceIdentificationTypeImpl.AuditSourceTypeCodeTypeImpl.this;
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
                                spawnHandlerFromEnterAttribute((((org.openhealthtools.common.audit.jaxb.impl.CodedValueTypeImpl)org.openhealthtools.common.audit.jaxb.impl.AuditSourceIdentificationTypeImpl.AuditSourceTypeCodeTypeImpl.this).new Unmarshaller(context)), 1, ___uri, ___local, ___qname);
                                return ;
                            }
                            if (("codeSystemName" == ___local)&&("" == ___uri)) {
                                spawnHandlerFromEnterAttribute((((org.openhealthtools.common.audit.jaxb.impl.CodedValueTypeImpl)org.openhealthtools.common.audit.jaxb.impl.AuditSourceIdentificationTypeImpl.AuditSourceTypeCodeTypeImpl.this).new Unmarshaller(context)), 1, ___uri, ___local, ___qname);
                                return ;
                            }
                            if (("code" == ___local)&&("" == ___uri)) {
                                spawnHandlerFromEnterAttribute((((org.openhealthtools.common.audit.jaxb.impl.CodedValueTypeImpl)org.openhealthtools.common.audit.jaxb.impl.AuditSourceIdentificationTypeImpl.AuditSourceTypeCodeTypeImpl.this).new Unmarshaller(context)), 1, ___uri, ___local, ___qname);
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
            super(context, "----------");
        }

        protected Unmarshaller(org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return org.openhealthtools.common.audit.jaxb.impl.AuditSourceIdentificationTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        attIdx = context.getAttribute("", "AuditSourceID");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 6;
                            continue outer;
                        }
                        break;
                    case  6 :
                        if (("AuditSourceTypeCode" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 7;
                            return ;
                        }
                        state = 9;
                        continue outer;
                    case  7 :
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
                    case  0 :
                        attIdx = context.getAttribute("", "AuditEnterpriseSiteID");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case  9 :
                        if (("AuditSourceTypeCode" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 7;
                            return ;
                        }
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                }
                super.enterElement(___uri, ___local, ___qname, __atts);
                break;
            }
        }

        private void eatText1(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _AuditSourceID = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _AuditEnterpriseSiteID = value;
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
                    case  3 :
                        attIdx = context.getAttribute("", "AuditSourceID");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 6;
                            continue outer;
                        }
                        break;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  7 :
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
                    case  0 :
                        attIdx = context.getAttribute("", "AuditEnterpriseSiteID");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case  8 :
                        if (("AuditSourceTypeCode" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  9 :
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
                    case  3 :
                        if (("AuditSourceID" == ___local)&&("" == ___uri)) {
                            state = 4;
                            return ;
                        }
                        break;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  7 :
                        if (("codeSystem" == ___local)&&("" == ___uri)) {
                            _getAuditSourceTypeCode().add(((org.openhealthtools.common.audit.jaxb.impl.AuditSourceIdentificationTypeImpl.AuditSourceTypeCodeTypeImpl) spawnChildFromEnterAttribute((org.openhealthtools.common.audit.jaxb.impl.AuditSourceIdentificationTypeImpl.AuditSourceTypeCodeTypeImpl.class), 8, ___uri, ___local, ___qname)));
                            return ;
                        }
                        if (("codeSystemName" == ___local)&&("" == ___uri)) {
                            _getAuditSourceTypeCode().add(((org.openhealthtools.common.audit.jaxb.impl.AuditSourceIdentificationTypeImpl.AuditSourceTypeCodeTypeImpl) spawnChildFromEnterAttribute((org.openhealthtools.common.audit.jaxb.impl.AuditSourceIdentificationTypeImpl.AuditSourceTypeCodeTypeImpl.class), 8, ___uri, ___local, ___qname)));
                            return ;
                        }
                        if (("code" == ___local)&&("" == ___uri)) {
                            _getAuditSourceTypeCode().add(((org.openhealthtools.common.audit.jaxb.impl.AuditSourceIdentificationTypeImpl.AuditSourceTypeCodeTypeImpl) spawnChildFromEnterAttribute((org.openhealthtools.common.audit.jaxb.impl.AuditSourceIdentificationTypeImpl.AuditSourceTypeCodeTypeImpl.class), 8, ___uri, ___local, ___qname)));
                            return ;
                        }
                        break;
                    case  0 :
                        if (("AuditEnterpriseSiteID" == ___local)&&("" == ___uri)) {
                            state = 1;
                            return ;
                        }
                        state = 3;
                        continue outer;
                    case  9 :
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
                    case  3 :
                        attIdx = context.getAttribute("", "AuditSourceID");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 6;
                            continue outer;
                        }
                        break;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  7 :
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
                    case  2 :
                        if (("AuditEnterpriseSiteID" == ___local)&&("" == ___uri)) {
                            state = 3;
                            return ;
                        }
                        break;
                    case  0 :
                        attIdx = context.getAttribute("", "AuditEnterpriseSiteID");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case  9 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  5 :
                        if (("AuditSourceID" == ___local)&&("" == ___uri)) {
                            state = 6;
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
                        case  3 :
                            attIdx = context.getAttribute("", "AuditSourceID");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText1(v);
                                state = 6;
                                continue outer;
                            }
                            break;
                        case  6 :
                            state = 9;
                            continue outer;
                        case  7 :
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
                            eatText2(value);
                            state = 2;
                            return ;
                        case  0 :
                            attIdx = context.getAttribute("", "AuditEnterpriseSiteID");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText2(v);
                                state = 3;
                                continue outer;
                            }
                            state = 3;
                            continue outer;
                        case  9 :
                            revertToParentFromText(value);
                            return ;
                        case  4 :
                            eatText1(value);
                            state = 5;
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
