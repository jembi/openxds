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

public class EventIdentificationTypeImpl implements org.openhealthtools.common.audit.jaxb.EventIdentificationType, com.sun.xml.bind.JAXBObject, org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallableObject, org.openhealthtools.common.audit.jaxb.impl.runtime.XMLSerializable, org.openhealthtools.common.audit.jaxb.impl.runtime.ValidatableObject
{

    protected org.openhealthtools.common.audit.jaxb.CodedValueType _EventID;
    protected java.math.BigInteger _EventOutcomeIndicator;
    protected java.util.Calendar _EventDateTime;
    protected java.lang.String _EventActionCode;
    protected com.sun.xml.bind.util.ListImpl _EventTypeCode;
    public final static java.lang.Class version = (org.openhealthtools.common.audit.jaxb.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (org.openhealthtools.common.audit.jaxb.EventIdentificationType.class);
    }

    public org.openhealthtools.common.audit.jaxb.CodedValueType getEventID() {
        return _EventID;
    }

    public void setEventID(org.openhealthtools.common.audit.jaxb.CodedValueType value) {
        _EventID = value;
    }

    public java.math.BigInteger getEventOutcomeIndicator() {
        return _EventOutcomeIndicator;
    }

    public void setEventOutcomeIndicator(java.math.BigInteger value) {
        _EventOutcomeIndicator = value;
    }

    public java.util.Calendar getEventDateTime() {
        return _EventDateTime;
    }

    public void setEventDateTime(java.util.Calendar value) {
        _EventDateTime = value;
    }

    public java.lang.String getEventActionCode() {
        return _EventActionCode;
    }

    public void setEventActionCode(java.lang.String value) {
        _EventActionCode = value;
    }

    protected com.sun.xml.bind.util.ListImpl _getEventTypeCode() {
        if (_EventTypeCode == null) {
            _EventTypeCode = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _EventTypeCode;
    }

    public java.util.List getEventTypeCode() {
        return _getEventTypeCode();
    }

    public org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallingEventHandler createUnmarshaller(org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallingContext context) {
        return new org.openhealthtools.common.audit.jaxb.impl.EventIdentificationTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(org.openhealthtools.common.audit.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx5 = 0;
        final int len5 = ((_EventTypeCode == null)? 0 :_EventTypeCode.size());
        context.startElement("", "EventID");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _EventID), "EventID");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _EventID), "EventID");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _EventID), "EventID");
        context.endElement();
        while (idx5 != len5) {
            context.startElement("", "EventTypeCode");
            int idx_2 = idx5;
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _EventTypeCode.get(idx_2 ++)), "EventTypeCode");
            context.endNamespaceDecls();
            int idx_3 = idx5;
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _EventTypeCode.get(idx_3 ++)), "EventTypeCode");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _EventTypeCode.get(idx5 ++)), "EventTypeCode");
            context.endElement();
        }
    }

    public void serializeAttributes(org.openhealthtools.common.audit.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx5 = 0;
        final int len5 = ((_EventTypeCode == null)? 0 :_EventTypeCode.size());
        if (_EventActionCode!= null) {
            context.startAttribute("", "EventActionCode");
            try {
                context.text(((java.lang.String) _EventActionCode), "EventActionCode");
            } catch (java.lang.Exception e) {
                org.openhealthtools.common.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        context.startAttribute("", "EventDateTime");
        try {
            context.text(com.sun.msv.datatype.xsd.DateTimeType.theInstance.serializeJavaObject(((java.util.Calendar) _EventDateTime), null), "EventDateTime");
        } catch (java.lang.Exception e) {
            org.openhealthtools.common.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endAttribute();
        context.startAttribute("", "EventOutcomeIndicator");
        try {
            context.text(javax.xml.bind.DatatypeConverter.printInteger(((java.math.BigInteger) _EventOutcomeIndicator)), "EventOutcomeIndicator");
        } catch (java.lang.Exception e) {
            org.openhealthtools.common.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endAttribute();
        while (idx5 != len5) {
            idx5 += 1;
        }
    }

    public void serializeURIs(org.openhealthtools.common.audit.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx5 = 0;
        final int len5 = ((_EventTypeCode == null)? 0 :_EventTypeCode.size());
        while (idx5 != len5) {
            idx5 += 1;
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (org.openhealthtools.common.audit.jaxb.EventIdentificationType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000\'com.sun.msv."
+"grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/su"
+"n/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq"
+"\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\tpp\u0000sr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000"
+"~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000 com.sun.m"
+"sv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~"
+"\u0000\nxq\u0000~\u0000\u0003q\u0000~\u0000\u0015psr\u00002com.sun.msv.grammar.Expression$AnyStringEx"
+"pression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u0014\u0001q\u0000~\u0000\u0019sr\u0000 com.sun.msv.grammar"
+".AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expression$EpsilonExpressi"
+"on\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u0000\u001aq\u0000~\u0000\u001fsr\u0000#com.sun.msv.grammar.SimpleN"
+"ameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNamet\u0000\u0012Ljava/lang/String;L\u0000\fnames"
+"paceURIq\u0000~\u0000!xq\u0000~\u0000\u001ct\u00009com.misyshealthcare.connect.ihe.audit.j"
+"axb.CodedValueTypet\u0000+http://java.sun.com/jaxb/xjc/dummy-elem"
+"entssq\u0000~\u0000\u000fppsq\u0000~\u0000\u0016q\u0000~\u0000\u0015psr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L"
+"\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000\"com.sun.ms"
+"v.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype."
+"xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd"
+".ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatat"
+"ypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000!L\u0000\btypeNameq\u0000~\u0000!L\u0000\nwhi"
+"teSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000"
+" http://www.w3.org/2001/XMLSchemat\u0000\u0005QNamesr\u00005com.sun.msv.dat"
+"atype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun"
+".msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.su"
+"n.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003p"
+"psr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000!"
+"L\u0000\fnamespaceURIq\u0000~\u0000!xpq\u0000~\u00002q\u0000~\u00001sq\u0000~\u0000 t\u0000\u0004typet\u0000)http://www.w"
+"3.org/2001/XMLSchema-instanceq\u0000~\u0000\u001fsq\u0000~\u0000 t\u0000\u0007EventIDt\u0000\u0000sq\u0000~\u0000\u000fp"
+"psq\u0000~\u0000\u0011q\u0000~\u0000\u0015psq\u0000~\u0000\tq\u0000~\u0000\u0015p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\tpp\u0000sq\u0000~\u0000\u000fppsq\u0000~\u0000\u0011q\u0000~"
+"\u0000\u0015psq\u0000~\u0000\u0016q\u0000~\u0000\u0015pq\u0000~\u0000\u0019q\u0000~\u0000\u001dq\u0000~\u0000\u001fsq\u0000~\u0000 q\u0000~\u0000#q\u0000~\u0000$sq\u0000~\u0000\u000fppsq\u0000~\u0000\u0016"
+"q\u0000~\u0000\u0015pq\u0000~\u0000*q\u0000~\u0000:q\u0000~\u0000\u001fsq\u0000~\u0000 t\u0000\rEventTypeCodeq\u0000~\u0000?q\u0000~\u0000\u001fsq\u0000~\u0000\u000fp"
+"psq\u0000~\u0000\u0016q\u0000~\u0000\u0015psq\u0000~\u0000\'ppsr\u0000)com.sun.msv.datatype.xsd.Enumeratio"
+"nFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0006valuest\u0000\u000fLjava/util/Set;xr\u00009com.sun.msv."
+"datatype.xsd.DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*"
+"com.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFa"
+"cetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/data"
+"type/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datat"
+"ype/xsd/ConcreteType;L\u0000\tfacetNameq\u0000~\u0000!xq\u0000~\u0000.q\u0000~\u0000?psr\u00005com.su"
+"n.msv.datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq"
+"\u0000~\u00004\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\ri"
+"sAlwaysValidxq\u0000~\u0000,q\u0000~\u00001t\u0000\u0006stringq\u0000~\u0000X\u0001q\u0000~\u0000Zt\u0000\u000benumerationsr\u0000"
+"\u0011java.util.HashSet\u00baD\u0085\u0095\u0096\u00b8\u00b74\u0003\u0000\u0000xpw\f\u0000\u0000\u0000\u0010?@\u0000\u0000\u0000\u0000\u0000\u0005t\u0000\u0001Dt\u0000\u0001Rt\u0000\u0001Ut\u0000\u0001"
+"Ct\u0000\u0001Exq\u0000~\u00007sq\u0000~\u00008t\u0000\u000estring-derivedq\u0000~\u0000?sq\u0000~\u0000 t\u0000\u000fEventActionC"
+"odeq\u0000~\u0000?q\u0000~\u0000\u001fsq\u0000~\u0000\u0016ppsq\u0000~\u0000\'ppsr\u0000%com.sun.msv.datatype.xsd.Da"
+"teTimeType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000)com.sun.msv.datatype.xsd.DateTimeBa"
+"seType\u0014W\u001a@3\u00a5\u00b4\u00e5\u0002\u0000\u0000xq\u0000~\u0000,q\u0000~\u00001t\u0000\bdateTimeq\u0000~\u00005q\u0000~\u00007sq\u0000~\u00008q\u0000~\u0000m"
+"q\u0000~\u00001sq\u0000~\u0000 t\u0000\rEventDateTimeq\u0000~\u0000?sq\u0000~\u0000\u0016ppsq\u0000~\u0000\'ppsq\u0000~\u0000Pq\u0000~\u0000?p"
+"q\u0000~\u00005\u0000\u0000sr\u0000$com.sun.msv.datatype.xsd.IntegerType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr"
+"\u0000+com.sun.msv.datatype.xsd.IntegerDerivedType\u0099\u00f1]\u0090&6k\u00be\u0002\u0000\u0001L\u0000\nb"
+"aseFacetsq\u0000~\u0000Txq\u0000~\u0000,q\u0000~\u00001t\u0000\u0007integerq\u0000~\u00005sr\u0000,com.sun.msv.data"
+"type.xsd.FractionDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005scalexr\u0000;com.sun.m"
+"sv.datatype.xsd.DataTypeWithLexicalConstraintFacetT\u0090\u001c>\u001azb\u00ea\u0002\u0000"
+"\u0000xq\u0000~\u0000Sppq\u0000~\u00005\u0001\u0000sr\u0000#com.sun.msv.datatype.xsd.NumberType\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000,q\u0000~\u00001t\u0000\u0007decimalq\u0000~\u00005q\u0000~\u0000|t\u0000\u000efractionDigits\u0000\u0000\u0000\u0000q\u0000"
+"~\u0000vq\u0000~\u0000\\sq\u0000~\u0000]w\f\u0000\u0000\u0000\u0010?@\u0000\u0000\u0000\u0000\u0000\u0004sr\u0000)com.sun.msv.datatype.xsd.Int"
+"egerValueType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0005valueq\u0000~\u0000!xr\u0000\u0010java.lang.Number\u0086\u00ac\u0095"
+"\u001d\u000b\u0094\u00e0\u008b\u0002\u0000\u0000xpt\u0000\u00010sq\u0000~\u0000\u0080t\u0000\u00014sq\u0000~\u0000\u0080t\u0000\u00018sq\u0000~\u0000\u0080t\u0000\u000212xq\u0000~\u00007sq\u0000~\u00008t\u0000\u000f"
+"integer-derivedq\u0000~\u0000?sq\u0000~\u0000 t\u0000\u0015EventOutcomeIndicatorq\u0000~\u0000?sr\u0000\"c"
+"om.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lc"
+"om/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.m"
+"sv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rst"
+"reamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;x"
+"p\u0000\u0000\u0000\u000f\u0001pq\u0000~\u0000Aq\u0000~\u0000\u0013q\u0000~\u0000Fq\u0000~\u0000\rq\u0000~\u0000Cq\u0000~\u0000\u0006q\u0000~\u0000\u0007q\u0000~\u0000%q\u0000~\u0000Iq\u0000~\u0000Mq\u0000~"
+"\u0000\u0010q\u0000~\u0000Eq\u0000~\u0000@q\u0000~\u0000\u0005q\u0000~\u0000\bx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends org.openhealthtools.common.audit.jaxb.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallingContext context) {
            super(context, "----------------");
        }

        protected Unmarshaller(org.openhealthtools.common.audit.jaxb.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return org.openhealthtools.common.audit.jaxb.impl.EventIdentificationTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  12 :
                        if (("EventTypeCode" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 13;
                            return ;
                        }
                        state = 15;
                        continue outer;
                    case  3 :
                        attIdx = context.getAttribute("", "EventDateTime");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 6;
                            continue outer;
                        }
                        break;
                    case  10 :
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
                        attIdx = context.getAttribute("", "EventActionCode");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case  9 :
                        if (("EventID" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 10;
                            return ;
                        }
                        break;
                    case  13 :
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
                    case  15 :
                        if (("EventTypeCode" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 13;
                            return ;
                        }
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  6 :
                        attIdx = context.getAttribute("", "EventOutcomeIndicator");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText3(v);
                            state = 9;
                            continue outer;
                        }
                        break;
                }
                super.enterElement(___uri, ___local, ___qname, __atts);
                break;
            }
        }

        private void eatText1(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _EventDateTime = ((java.util.Calendar) com.sun.msv.datatype.xsd.DateTimeType.theInstance.createJavaObject(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value), null));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _EventActionCode = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _EventOutcomeIndicator = javax.xml.bind.DatatypeConverter.parseInteger(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
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
                    case  14 :
                        if (("EventTypeCode" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 15;
                            return ;
                        }
                        break;
                    case  12 :
                        state = 15;
                        continue outer;
                    case  3 :
                        attIdx = context.getAttribute("", "EventDateTime");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 6;
                            continue outer;
                        }
                        break;
                    case  10 :
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
                        attIdx = context.getAttribute("", "EventActionCode");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case  13 :
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
                    case  15 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  11 :
                        if (("EventID" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  6 :
                        attIdx = context.getAttribute("", "EventOutcomeIndicator");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText3(v);
                            state = 9;
                            continue outer;
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
                    case  12 :
                        state = 15;
                        continue outer;
                    case  3 :
                        if (("EventDateTime" == ___local)&&("" == ___uri)) {
                            state = 4;
                            return ;
                        }
                        break;
                    case  10 :
                        if (("codeSystem" == ___local)&&("" == ___uri)) {
                            _EventID = ((org.openhealthtools.common.audit.jaxb.impl.CodedValueTypeImpl) spawnChildFromEnterAttribute((org.openhealthtools.common.audit.jaxb.impl.CodedValueTypeImpl.class), 11, ___uri, ___local, ___qname));
                            return ;
                        }
                        if (("codeSystemName" == ___local)&&("" == ___uri)) {
                            _EventID = ((org.openhealthtools.common.audit.jaxb.impl.CodedValueTypeImpl) spawnChildFromEnterAttribute((org.openhealthtools.common.audit.jaxb.impl.CodedValueTypeImpl.class), 11, ___uri, ___local, ___qname));
                            return ;
                        }
                        if (("code" == ___local)&&("" == ___uri)) {
                            _EventID = ((org.openhealthtools.common.audit.jaxb.impl.CodedValueTypeImpl) spawnChildFromEnterAttribute((org.openhealthtools.common.audit.jaxb.impl.CodedValueTypeImpl.class), 11, ___uri, ___local, ___qname));
                            return ;
                        }
                        break;
                    case  0 :
                        if (("EventActionCode" == ___local)&&("" == ___uri)) {
                            state = 1;
                            return ;
                        }
                        state = 3;
                        continue outer;
                    case  13 :
                        if (("codeSystem" == ___local)&&("" == ___uri)) {
                            _getEventTypeCode().add(((org.openhealthtools.common.audit.jaxb.impl.CodedValueTypeImpl) spawnChildFromEnterAttribute((org.openhealthtools.common.audit.jaxb.impl.CodedValueTypeImpl.class), 14, ___uri, ___local, ___qname)));
                            return ;
                        }
                        if (("codeSystemName" == ___local)&&("" == ___uri)) {
                            _getEventTypeCode().add(((org.openhealthtools.common.audit.jaxb.impl.CodedValueTypeImpl) spawnChildFromEnterAttribute((org.openhealthtools.common.audit.jaxb.impl.CodedValueTypeImpl.class), 14, ___uri, ___local, ___qname)));
                            return ;
                        }
                        if (("code" == ___local)&&("" == ___uri)) {
                            _getEventTypeCode().add(((org.openhealthtools.common.audit.jaxb.impl.CodedValueTypeImpl) spawnChildFromEnterAttribute((org.openhealthtools.common.audit.jaxb.impl.CodedValueTypeImpl.class), 14, ___uri, ___local, ___qname)));
                            return ;
                        }
                        break;
                    case  15 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  6 :
                        if (("EventOutcomeIndicator" == ___local)&&("" == ___uri)) {
                            state = 7;
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
                    case  12 :
                        state = 15;
                        continue outer;
                    case  3 :
                        attIdx = context.getAttribute("", "EventDateTime");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 6;
                            continue outer;
                        }
                        break;
                    case  10 :
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
                    case  0 :
                        attIdx = context.getAttribute("", "EventActionCode");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case  13 :
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
                    case  8 :
                        if (("EventOutcomeIndicator" == ___local)&&("" == ___uri)) {
                            state = 9;
                            return ;
                        }
                        break;
                    case  2 :
                        if (("EventActionCode" == ___local)&&("" == ___uri)) {
                            state = 3;
                            return ;
                        }
                        break;
                    case  5 :
                        if (("EventDateTime" == ___local)&&("" == ___uri)) {
                            state = 6;
                            return ;
                        }
                        break;
                    case  15 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  6 :
                        attIdx = context.getAttribute("", "EventOutcomeIndicator");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText3(v);
                            state = 9;
                            continue outer;
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
                            eatText2(value);
                            state = 2;
                            return ;
                        case  12 :
                            state = 15;
                            continue outer;
                        case  3 :
                            attIdx = context.getAttribute("", "EventDateTime");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText1(v);
                                state = 6;
                                continue outer;
                            }
                            break;
                        case  10 :
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
                        case  0 :
                            attIdx = context.getAttribute("", "EventActionCode");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText2(v);
                                state = 3;
                                continue outer;
                            }
                            state = 3;
                            continue outer;
                        case  13 :
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
                        case  15 :
                            revertToParentFromText(value);
                            return ;
                        case  4 :
                            eatText1(value);
                            state = 5;
                            return ;
                        case  7 :
                            eatText3(value);
                            state = 8;
                            return ;
                        case  6 :
                            attIdx = context.getAttribute("", "EventOutcomeIndicator");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText3(v);
                                state = 9;
                                continue outer;
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
