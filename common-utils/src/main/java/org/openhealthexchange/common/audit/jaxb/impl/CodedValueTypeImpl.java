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


package org.openhealthexchange.common.audit.jaxb.impl;

public class CodedValueTypeImpl implements org.openhealthexchange.common.audit.jaxb.CodedValueType, com.sun.xml.bind.JAXBObject, org.openhealthexchange.common.audit.jaxb.impl.runtime.UnmarshallableObject, org.openhealthexchange.common.audit.jaxb.impl.runtime.XMLSerializable, org.openhealthexchange.common.audit.jaxb.impl.runtime.ValidatableObject
{

    protected java.lang.String _DisplayName;
    protected java.lang.String _OriginalText;
    protected java.lang.String _Code;
    protected java.lang.String _CodeSystemName;
    protected java.lang.String _CodeSystem;
    public final static java.lang.Class version = (org.openhealthexchange.common.audit.jaxb.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (org.openhealthexchange.common.audit.jaxb.CodedValueType.class);
    }

    public java.lang.String getDisplayName() {
        return _DisplayName;
    }

    public void setDisplayName(java.lang.String value) {
        _DisplayName = value;
    }

    public java.lang.String getOriginalText() {
        return _OriginalText;
    }

    public void setOriginalText(java.lang.String value) {
        _OriginalText = value;
    }

    public java.lang.String getCode() {
        return _Code;
    }

    public void setCode(java.lang.String value) {
        _Code = value;
    }

    public java.lang.String getCodeSystemName() {
        return _CodeSystemName;
    }

    public void setCodeSystemName(java.lang.String value) {
        _CodeSystemName = value;
    }

    public java.lang.String getCodeSystem() {
        return _CodeSystem;
    }

    public void setCodeSystem(java.lang.String value) {
        _CodeSystem = value;
    }

    public org.openhealthexchange.common.audit.jaxb.impl.runtime.UnmarshallingEventHandler createUnmarshaller(org.openhealthexchange.common.audit.jaxb.impl.runtime.UnmarshallingContext context) {
        return new org.openhealthexchange.common.audit.jaxb.impl.CodedValueTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(org.openhealthexchange.common.audit.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeAttributes(org.openhealthexchange.common.audit.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (_CodeSystem!= null) {
            context.startAttribute("", "codeSystem");
            try {
                context.text(((java.lang.String) _CodeSystem), "CodeSystem");
            } catch (java.lang.Exception e) {
                org.openhealthexchange.common.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        if (_CodeSystemName!= null) {
            context.startAttribute("", "codeSystemName");
            try {
                context.text(((java.lang.String) _CodeSystemName), "CodeSystemName");
            } catch (java.lang.Exception e) {
                org.openhealthexchange.common.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        context.startAttribute("", "code");
        try {
            context.text(((java.lang.String) _Code), "Code");
        } catch (java.lang.Exception e) {
            org.openhealthexchange.common.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endAttribute();
        if (_DisplayName!= null) {
            context.startAttribute("", "displayName");
            try {
                context.text(((java.lang.String) _DisplayName), "DisplayName");
            } catch (java.lang.Exception e) {
                org.openhealthexchange.common.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        if (_OriginalText!= null) {
            context.startAttribute("", "originalText");
            try {
                context.text(((java.lang.String) _OriginalText), "OriginalText");
            } catch (java.lang.Exception e) {
                org.openhealthexchange.common.audit.jaxb.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
    }

    public void serializeURIs(org.openhealthexchange.common.audit.jaxb.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (org.openhealthexchange.common.audit.jaxb.CodedValueType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000 com.sun.msv."
+"grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClasst\u0000\u001fLco"
+"m/sun/msv/grammar/NameClass;xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.grammar."
+"DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006"
+"exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr"
+"\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysV"
+"alidxr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com"
+".sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceU"
+"rit\u0000\u0012Ljava/lang/String;L\u0000\btypeNameq\u0000~\u0000\u0014L\u0000\nwhiteSpacet\u0000.Lcom/"
+"sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http://www.w3."
+"org/2001/XMLSchemat\u0000\u0006stringsr\u00005com.sun.msv.datatype.xsd.Whit"
+"eSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype."
+"xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0001sr\u00000com.sun.msv.grammar"
+".Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun."
+"msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0014L\u0000\fnamespaceU"
+"RIq\u0000~\u0000\u0014xpq\u0000~\u0000\u0018q\u0000~\u0000\u0017sr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0014L\u0000\fnamespaceURIq\u0000~\u0000\u0014xr\u0000\u001dcom.sun.ms"
+"v.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004codet\u0000\u0000sr\u0000\u001dcom.sun.msv.gr"
+"ammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsq\u0000~\u0000\tsr\u0000\u0011java.lang.Boolea"
+"n\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000pq\u0000~\u0000\u000fsq\u0000~\u0000 t\u0000\u000bdisplayNameq\u0000~\u0000$sr\u00000co"
+"m.sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000"
+"~\u0000\u0003sq\u0000~\u0000(\u0001q\u0000~\u0000-sq\u0000~\u0000%ppsq\u0000~\u0000\tq\u0000~\u0000)pq\u0000~\u0000\u000fsq\u0000~\u0000 t\u0000\foriginalTex"
+"tq\u0000~\u0000$q\u0000~\u0000-sq\u0000~\u0000%ppsq\u0000~\u0000\tq\u0000~\u0000)psq\u0000~\u0000\fppsr\u0000(com.sun.msv.datat"
+"ype.xsd.WhiteSpaceFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.x"
+"sd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCh"
+"eckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeImp"
+"l;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType;L"
+"\u0000\tfacetNameq\u0000~\u0000\u0014xq\u0000~\u0000\u0013q\u0000~\u0000$t\u0000\u0003OIDsr\u00005com.sun.msv.datatype.xs"
+"d.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001a\u0000\u0000q\u0000~\u0000\u0016q\u0000~\u0000\u0016t"
+"\u0000\nwhiteSpaceq\u0000~\u0000\u001dsq\u0000~\u0000\u001eq\u0000~\u0000;q\u0000~\u0000$sq\u0000~\u0000 t\u0000\ncodeSystemq\u0000~\u0000$q\u0000~"
+"\u0000-sq\u0000~\u0000%ppsq\u0000~\u0000\tq\u0000~\u0000)pq\u0000~\u0000\u000fsq\u0000~\u0000 t\u0000\u000ecodeSystemNameq\u0000~\u0000$q\u0000~\u0000-"
+"sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTable"
+"t\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com."
+"sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005count"
+"B\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionP"
+"ool;xp\u0000\u0000\u0000\b\u0001pq\u0000~\u0000Bq\u0000~\u0000&q\u0000~\u0000\u0005q\u0000~\u0000\bq\u0000~\u0000\u0007q\u0000~\u0000\u0006q\u0000~\u00003q\u0000~\u0000/x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends org.openhealthexchange.common.audit.jaxb.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(org.openhealthexchange.common.audit.jaxb.impl.runtime.UnmarshallingContext context) {
            super(context, "----------------");
        }

        protected Unmarshaller(org.openhealthexchange.common.audit.jaxb.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return org.openhealthexchange.common.audit.jaxb.impl.CodedValueTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  9 :
                        attIdx = context.getAttribute("", "displayName");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 12;
                            continue outer;
                        }
                        state = 12;
                        continue outer;
                    case  6 :
                        attIdx = context.getAttribute("", "code");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 9;
                            continue outer;
                        }
                        break;
                    case  12 :
                        attIdx = context.getAttribute("", "originalText");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText3(v);
                            state = 15;
                            continue outer;
                        }
                        state = 15;
                        continue outer;
                    case  15 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  0 :
                        attIdx = context.getAttribute("", "codeSystem");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText4(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case  3 :
                        attIdx = context.getAttribute("", "codeSystemName");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText5(v);
                            state = 6;
                            continue outer;
                        }
                        state = 6;
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
                _DisplayName = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Code = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _OriginalText = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _CodeSystem = com.sun.xml.bind.WhiteSpaceProcessor.collapse(value);
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText5(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _CodeSystemName = value;
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
                    case  9 :
                        attIdx = context.getAttribute("", "displayName");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 12;
                            continue outer;
                        }
                        state = 12;
                        continue outer;
                    case  6 :
                        attIdx = context.getAttribute("", "code");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 9;
                            continue outer;
                        }
                        break;
                    case  12 :
                        attIdx = context.getAttribute("", "originalText");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText3(v);
                            state = 15;
                            continue outer;
                        }
                        state = 15;
                        continue outer;
                    case  15 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  0 :
                        attIdx = context.getAttribute("", "codeSystem");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText4(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case  3 :
                        attIdx = context.getAttribute("", "codeSystemName");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText5(v);
                            state = 6;
                            continue outer;
                        }
                        state = 6;
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
                    case  9 :
                        if (("displayName" == ___local)&&("" == ___uri)) {
                            state = 10;
                            return ;
                        }
                        state = 12;
                        continue outer;
                    case  6 :
                        if (("code" == ___local)&&("" == ___uri)) {
                            state = 7;
                            return ;
                        }
                        break;
                    case  12 :
                        if (("originalText" == ___local)&&("" == ___uri)) {
                            state = 13;
                            return ;
                        }
                        state = 15;
                        continue outer;
                    case  15 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  0 :
                        if (("codeSystem" == ___local)&&("" == ___uri)) {
                            state = 1;
                            return ;
                        }
                        state = 3;
                        continue outer;
                    case  3 :
                        if (("codeSystemName" == ___local)&&("" == ___uri)) {
                            state = 4;
                            return ;
                        }
                        state = 6;
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
                    case  9 :
                        attIdx = context.getAttribute("", "displayName");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 12;
                            continue outer;
                        }
                        state = 12;
                        continue outer;
                    case  14 :
                        if (("originalText" == ___local)&&("" == ___uri)) {
                            state = 15;
                            return ;
                        }
                        break;
                    case  6 :
                        attIdx = context.getAttribute("", "code");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText2(v);
                            state = 9;
                            continue outer;
                        }
                        break;
                    case  12 :
                        attIdx = context.getAttribute("", "originalText");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText3(v);
                            state = 15;
                            continue outer;
                        }
                        state = 15;
                        continue outer;
                    case  11 :
                        if (("displayName" == ___local)&&("" == ___uri)) {
                            state = 12;
                            return ;
                        }
                        break;
                    case  15 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  8 :
                        if (("code" == ___local)&&("" == ___uri)) {
                            state = 9;
                            return ;
                        }
                        break;
                    case  2 :
                        if (("codeSystem" == ___local)&&("" == ___uri)) {
                            state = 3;
                            return ;
                        }
                        break;
                    case  0 :
                        attIdx = context.getAttribute("", "codeSystem");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText4(v);
                            state = 3;
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case  5 :
                        if (("codeSystemName" == ___local)&&("" == ___uri)) {
                            state = 6;
                            return ;
                        }
                        break;
                    case  3 :
                        attIdx = context.getAttribute("", "codeSystemName");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText5(v);
                            state = 6;
                            continue outer;
                        }
                        state = 6;
                        continue outer;
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
                        case  9 :
                            attIdx = context.getAttribute("", "displayName");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText1(v);
                                state = 12;
                                continue outer;
                            }
                            state = 12;
                            continue outer;
                        case  4 :
                            eatText5(value);
                            state = 5;
                            return ;
                        case  10 :
                            eatText1(value);
                            state = 11;
                            return ;
                        case  7 :
                            eatText2(value);
                            state = 8;
                            return ;
                        case  6 :
                            attIdx = context.getAttribute("", "code");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText2(v);
                                state = 9;
                                continue outer;
                            }
                            break;
                        case  12 :
                            attIdx = context.getAttribute("", "originalText");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText3(v);
                                state = 15;
                                continue outer;
                            }
                            state = 15;
                            continue outer;
                        case  13 :
                            eatText3(value);
                            state = 14;
                            return ;
                        case  15 :
                            revertToParentFromText(value);
                            return ;
                        case  1 :
                            eatText4(value);
                            state = 2;
                            return ;
                        case  0 :
                            attIdx = context.getAttribute("", "codeSystem");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText4(v);
                                state = 3;
                                continue outer;
                            }
                            state = 3;
                            continue outer;
                        case  3 :
                            attIdx = context.getAttribute("", "codeSystemName");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText5(v);
                                state = 6;
                                continue outer;
                            }
                            state = 6;
                            continue outer;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

    }

}
