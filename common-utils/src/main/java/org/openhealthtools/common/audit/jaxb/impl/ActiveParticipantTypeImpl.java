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

public class ActiveParticipantTypeImpl implements org.openhealthtools.common.audit.jaxb.ActiveParticipantType, com.sun.xml.bind.JAXBObject, org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallableObject, org.openhealthtools.common.audit.jaxb.impl.runtime.XMLSerializable, org.openhealthtools.common.audit.jaxb.impl.runtime.ValidatableObject
{

    protected boolean has_UserIsRequestor;
    protected boolean _UserIsRequestor;
    protected java.lang.String _UserID;
    protected boolean has_NetworkAccessPointTypeCode;
    protected short _NetworkAccessPointTypeCode;
    protected java.lang.String _AlternativeUserID;
    protected java.lang.String _NetworkAccessPointID;
    protected com.sun.xml.bind.util.ListImpl _RoleIDCode;
    protected java.lang.String _UserName;
    public final static java.lang.Class version = (org.openhealthtools.common.audit.jaxb.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (org.openhealthtools.common.audit.jaxb.ActiveParticipantType.class);
    }

    public boolean isUserIsRequestor() {
        if (!has_UserIsRequestor) {
            return javax.xml.bind.DatatypeConverter.parseBoolean(com.sun.xml.bind.DatatypeConverterImpl.installHook("true"));
        } else {
            return _UserIsRequestor;
        }
    }

    public void setUserIsRequestor(boolean value) {
        _UserIsRequestor = value;
        has_UserIsRequestor = true;
    }

    public java.lang.String getUserID() {
        return _UserID;
    }

    public void setUserID(java.lang.String value) {
        _UserID = value;
    }

    public short getNetworkAccessPointTypeCode() {
        return _NetworkAccessPointTypeCode;
    }

    public void setNetworkAccessPointTypeCode(short value) {
        _NetworkAccessPointTypeCode = value;
        has_NetworkAccessPointTypeCode = true;
    }

    public java.lang.String getAlternativeUserID() {
        return _AlternativeUserID;
    }

    public void setAlternativeUserID(java.lang.String value) {
        _AlternativeUserID = value;
    }

    public java.lang.String getNetworkAccessPointID() {
        return _NetworkAccessPointID;
    }

    public void setNetworkAccessPointID(java.lang.String value) {
        _NetworkAccessPointID = value;
    }

    protected com.sun.xml.bind.util.ListImpl _getRoleIDCode() {
        if (_RoleIDCode == null) {
            _RoleIDCode = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _RoleIDCode;
    }

    public java.util.List getRoleIDCode() {
        return _getRoleIDCode();
    }

    public java.lang.String getUserName() {
        return _UserName;
    }

    public void setUserName(java.lang.String value) {
        _UserName = value;
    }

    public org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallingEventHandler createUnmarshaller(org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallingContext context) {
        return new org.openhealthtools.common.audit.jaxb.impl.ActiveParticipantTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(org.openhealthtools.common.audit.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx6 = 0;
        final int len6 = ((_RoleIDCode == null)? 0 :_RoleIDCode.size());
        while (idx6 != len6) {
            context.startElement("", "RoleIDCode");
            int idx_0 = idx6;
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _RoleIDCode.get(idx_0 ++)), "RoleIDCode");
            context.endNamespaceDecls();
            int idx_1 = idx6;
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _RoleIDCode.get(idx_1 ++)), "RoleIDCode");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _RoleIDCode.get(idx6 ++)), "RoleIDCode");
            context.endElement();
        }
    }

    public void serializeAttributes(org.openhealthtools.common.audit.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx6 = 0;
        final int len6 = ((_RoleIDCode == null)? 0 :_RoleIDCode.size());
        if (_AlternativeUserID!= null) {
            context.startAttribute("", "AlternativeUserID");
            try {
                context.text(((java.lang.String) _AlternativeUserID), "AlternativeUserID");
            } catch (java.lang.Exception e) {
                org.openhealthtools.common.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        if (_NetworkAccessPointID!= null) {
            context.startAttribute("", "NetworkAccessPointID");
            try {
                context.text(((java.lang.String) _NetworkAccessPointID), "NetworkAccessPointID");
            } catch (java.lang.Exception e) {
                org.openhealthtools.common.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        if (has_NetworkAccessPointTypeCode) {
            context.startAttribute("", "NetworkAccessPointTypeCode");
            try {
                context.text(javax.xml.bind.DatatypeConverter.printShort(((short) _NetworkAccessPointTypeCode)), "NetworkAccessPointTypeCode");
            } catch (java.lang.Exception e) {
                org.openhealthtools.common.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        context.startAttribute("", "UserID");
        try {
            context.text(((java.lang.String) _UserID), "UserID");
        } catch (java.lang.Exception e) {
            org.openhealthtools.common.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endAttribute();
        if (has_UserIsRequestor) {
            context.startAttribute("", "UserIsRequestor");
            try {
                context.text(javax.xml.bind.DatatypeConverter.printBoolean(((boolean) _UserIsRequestor)), "UserIsRequestor");
            } catch (java.lang.Exception e) {
                org.openhealthtools.common.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        if (_UserName!= null) {
            context.startAttribute("", "UserName");
            try {
                context.text(((java.lang.String) _UserName), "UserName");
            } catch (java.lang.Exception e) {
                org.openhealthtools.common.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        while (idx6 != len6) {
            idx6 += 1;
        }
    }

    public void serializeURIs(org.openhealthtools.common.audit.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx6 = 0;
        final int len6 = ((_RoleIDCode == null)? 0 :_RoleIDCode.size());
        while (idx6 != len6) {
            idx6 += 1;
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (org.openhealthtools.common.audit.jaxb.ActiveParticipantType.class);
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
+"onPool;xp\u0000\u0000\u0000\u0011\u0001pq\u0000~\u0000\u009eq\u0000~\u0000\u000fq\u0000~\u0000\u0007q\u0000~\u0000\u0019q\u0000~\u0000\u0016q\u0000~\u0000Dq\u0000~\u0000)q\u0000~\u0000\u0018q\u0000~\u0000\f"
+"q\u0000~\u0000Oq\u0000~\u0000\nq\u0000~\u0000\bq\u0000~\u0000\u00a7q\u0000~\u0000\u0005q\u0000~\u0000\tq\u0000~\u0000\u0006q\u0000~\u0000Sx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends org.openhealthtools.common.audit.jaxb.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallingContext context) {
            super(context, "----------------------");
        }

        protected Unmarshaller(org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return org.openhealthtools.common.audit.jaxb.impl.ActiveParticipantTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  0 :
                        attIdx = context.getAttribute("", "AlternativeUserID");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case  9 :
                        attIdx = context.getAttribute("", "UserID");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 12;
                            continue outer;
                        }
                        break;
                    case  18 :
                        if (("RoleIDCode" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 19;
                            return ;
                        }
                        state = 21;
                        continue outer;
                    case  21 :
                        if (("RoleIDCode" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 19;
                            return ;
                        }
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  3 :
                        attIdx = context.getAttribute("", "NetworkAccessPointID");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText3(v);
                            state = 6;
                            continue outer;
                        }
                        state = 6;
                        continue outer;
                    case  15 :
                        attIdx = context.getAttribute("", "UserName");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText4(v);
                            state = 18;
                            continue outer;
                        }
                        state = 18;
                        continue outer;
                    case  6 :
                        attIdx = context.getAttribute("", "NetworkAccessPointTypeCode");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText5(v);
                            state = 9;
                            continue outer;
                        }
                        state = 9;
                        continue outer;
                    case  19 :
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
                    case  12 :
                        attIdx = context.getAttribute("", "UserIsRequestor");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText6(v);
                            state = 15;
                            continue outer;
                        }
                        state = 15;
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
                _AlternativeUserID = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _UserID = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _NetworkAccessPointID = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _UserName = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText5(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _NetworkAccessPointTypeCode = javax.xml.bind.DatatypeConverter.parseShort(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_NetworkAccessPointTypeCode = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText6(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _UserIsRequestor = javax.xml.bind.DatatypeConverter.parseBoolean(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_UserIsRequestor = true;
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
                    case  0 :
                        attIdx = context.getAttribute("", "AlternativeUserID");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case  9 :
                        attIdx = context.getAttribute("", "UserID");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 12;
                            continue outer;
                        }
                        break;
                    case  20 :
                        if (("RoleIDCode" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 21;
                            return ;
                        }
                        break;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  21 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  3 :
                        attIdx = context.getAttribute("", "NetworkAccessPointID");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText3(v);
                            state = 6;
                            continue outer;
                        }
                        state = 6;
                        continue outer;
                    case  15 :
                        attIdx = context.getAttribute("", "UserName");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText4(v);
                            state = 18;
                            continue outer;
                        }
                        state = 18;
                        continue outer;
                    case  6 :
                        attIdx = context.getAttribute("", "NetworkAccessPointTypeCode");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText5(v);
                            state = 9;
                            continue outer;
                        }
                        state = 9;
                        continue outer;
                    case  19 :
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
                    case  12 :
                        attIdx = context.getAttribute("", "UserIsRequestor");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText6(v);
                            state = 15;
                            continue outer;
                        }
                        state = 15;
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
                    case  0 :
                        if (("AlternativeUserID" == ___local)&&("" == ___uri)) {
                            state = 1;
                            return ;
                        }
                        state = 3;
                        continue outer;
                    case  9 :
                        if (("UserID" == ___local)&&("" == ___uri)) {
                            state = 10;
                            return ;
                        }
                        break;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  21 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  3 :
                        if (("NetworkAccessPointID" == ___local)&&("" == ___uri)) {
                            state = 4;
                            return ;
                        }
                        state = 6;
                        continue outer;
                    case  15 :
                        if (("UserName" == ___local)&&("" == ___uri)) {
                            state = 16;
                            return ;
                        }
                        state = 18;
                        continue outer;
                    case  6 :
                        if (("NetworkAccessPointTypeCode" == ___local)&&("" == ___uri)) {
                            state = 7;
                            return ;
                        }
                        state = 9;
                        continue outer;
                    case  19 :
                        if (("codeSystem" == ___local)&&("" == ___uri)) {
                            _getRoleIDCode().add(((org.openhealthtools.common.audit.jaxb.impl.CodedValueTypeImpl) spawnChildFromEnterAttribute((org.openhealthtools.common.audit.jaxb.impl.CodedValueTypeImpl.class), 20, ___uri, ___local, ___qname)));
                            return ;
                        }
                        if (("codeSystemName" == ___local)&&("" == ___uri)) {
                            _getRoleIDCode().add(((org.openhealthtools.common.audit.jaxb.impl.CodedValueTypeImpl) spawnChildFromEnterAttribute((org.openhealthtools.common.audit.jaxb.impl.CodedValueTypeImpl.class), 20, ___uri, ___local, ___qname)));
                            return ;
                        }
                        if (("code" == ___local)&&("" == ___uri)) {
                            _getRoleIDCode().add(((org.openhealthtools.common.audit.jaxb.impl.CodedValueTypeImpl) spawnChildFromEnterAttribute((org.openhealthtools.common.audit.jaxb.impl.CodedValueTypeImpl.class), 20, ___uri, ___local, ___qname)));
                            return ;
                        }
                        break;
                    case  12 :
                        if (("UserIsRequestor" == ___local)&&("" == ___uri)) {
                            state = 13;
                            return ;
                        }
                        state = 15;
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
                    case  0 :
                        attIdx = context.getAttribute("", "AlternativeUserID");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case  9 :
                        attIdx = context.getAttribute("", "UserID");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 12;
                            continue outer;
                        }
                        break;
                    case  8 :
                        if (("NetworkAccessPointTypeCode" == ___local)&&("" == ___uri)) {
                            state = 9;
                            return ;
                        }
                        break;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  21 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  3 :
                        attIdx = context.getAttribute("", "NetworkAccessPointID");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText3(v);
                            state = 6;
                            continue outer;
                        }
                        state = 6;
                        continue outer;
                    case  11 :
                        if (("UserID" == ___local)&&("" == ___uri)) {
                            state = 12;
                            return ;
                        }
                        break;
                    case  15 :
                        attIdx = context.getAttribute("", "UserName");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText4(v);
                            state = 18;
                            continue outer;
                        }
                        state = 18;
                        continue outer;
                    case  17 :
                        if (("UserName" == ___local)&&("" == ___uri)) {
                            state = 18;
                            return ;
                        }
                        break;
                    case  14 :
                        if (("UserIsRequestor" == ___local)&&("" == ___uri)) {
                            state = 15;
                            return ;
                        }
                        break;
                    case  6 :
                        attIdx = context.getAttribute("", "NetworkAccessPointTypeCode");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText5(v);
                            state = 9;
                            continue outer;
                        }
                        state = 9;
                        continue outer;
                    case  19 :
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
                    case  12 :
                        attIdx = context.getAttribute("", "UserIsRequestor");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText6(v);
                            state = 15;
                            continue outer;
                        }
                        state = 15;
                        continue outer;
                    case  5 :
                        if (("NetworkAccessPointID" == ___local)&&("" == ___uri)) {
                            state = 6;
                            return ;
                        }
                        break;
                    case  2 :
                        if (("AlternativeUserID" == ___local)&&("" == ___uri)) {
                            state = 3;
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
                        case  13 :
                            eatText6(value);
                            state = 14;
                            return ;
                        case  0 :
                            attIdx = context.getAttribute("", "AlternativeUserID");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText1(v);
                                state = 3;
                                continue outer;
                            }
                            state = 3;
                            continue outer;
                        case  9 :
                            attIdx = context.getAttribute("", "UserID");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText2(v);
                                state = 12;
                                continue outer;
                            }
                            break;
                        case  10 :
                            eatText2(value);
                            state = 11;
                            return ;
                        case  18 :
                            state = 21;
                            continue outer;
                        case  16 :
                            eatText4(value);
                            state = 17;
                            return ;
                        case  21 :
                            revertToParentFromText(value);
                            return ;
                        case  3 :
                            attIdx = context.getAttribute("", "NetworkAccessPointID");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText3(v);
                                state = 6;
                                continue outer;
                            }
                            state = 6;
                            continue outer;
                        case  15 :
                            attIdx = context.getAttribute("", "UserName");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText4(v);
                                state = 18;
                                continue outer;
                            }
                            state = 18;
                            continue outer;
                        case  4 :
                            eatText3(value);
                            state = 5;
                            return ;
                        case  1 :
                            eatText1(value);
                            state = 2;
                            return ;
                        case  6 :
                            attIdx = context.getAttribute("", "NetworkAccessPointTypeCode");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText5(v);
                                state = 9;
                                continue outer;
                            }
                            state = 9;
                            continue outer;
                        case  19 :
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
                        case  12 :
                            attIdx = context.getAttribute("", "UserIsRequestor");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText6(v);
                                state = 15;
                                continue outer;
                            }
                            state = 15;
                            continue outer;
                        case  7 :
                            eatText5(value);
                            state = 8;
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
