/**
 *  Copyright (c) 2009-2011 Misys Open Source Solutions (MOSS) and others
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

package org.openhealthtools.openxds.registry.adapter.omar31;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

/**
 * This class implements builders for the various Axiom OMElement contents
 * objects needed in submissions to XDS registry and repository connections.
 * 
 * @author LiW
 * @version 2.1 - Jan 12, 2008
 */
public class RimAxiom {

    private static final Log myLog =  LogFactory.getLog(RimAxiom.class);

    private static final String XDS_RIM_V3_NAMESPACE = "urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0";
    private static final String HAS_MEMBER   = "urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember";
    private static final String RPLC         = "urn:oasis:names:tc:ebxml-regrep:AssociationType:RPLC";
    //private static final int RIM_SHORT_NAME = 64;
    private static final int RIM_LONG_NAME = 128;
    private static OMNamespace nsRim = OMAbstractFactory.getOMFactory().createOMNamespace(XDS_RIM_V3_NAMESPACE, "rim");
    
    private OMElement root = null;
    private String id = null;
    private Log log = null;

    /**
     * Create a new XML builder.
     *
     * @param id The ID to assign to the ExtrinsicObject being built
     * @param connection The connection description for where this XML will be sent
     */
    RimAxiom(String id, Log log) {
        this.id = id;
        this.log = log;
    }

    /**
     * Get a new instance of an XML builder for ebRIM XDSDocumentEntry metadata.
     *
     * @param id The id for the ExtrinsicObject representing the metadata
     * @param connection The connection to which this metadata will be sent
     * @param log The log to use when reporting warnings and errors
     * @return The ebRIM XML builder
     */
    public static RimAxiom newXdsDocumentEntryBuilder(String id, Log log) {
        RimAxiom xml = new RimAxiom(id, log);
		OMFactory fac = OMAbstractFactory.getOMFactory();
        xml.root = fac.createOMElement("ExtrinsicObject", nsRim);
        xml.root.addAttribute("id", id, null);
        return xml;
    }

    /**
     * Get a new instance of an XML builder for ebRIM XDSSubmissionSet metadata.
     *
     * @param id The id for the RegistryPackage representing the metadata
     * @param connection The connection to which this metadata will be sent
     * @param log The log to use when reporting warnings and errors
     * @return The ebRIM XML builder
     */
    public static RimAxiom newXdsSubmissionSetBuilder(String id, Log log) {
        RimAxiom xml = new RimAxiom(id, log);
   
		OMFactory fac = OMAbstractFactory.getOMFactory();
        xml.root = fac.createOMElement("RegistryPackage", nsRim);
        xml.root.addAttribute("id", id, null);
        return xml;
    }

    /**
     * Get the OMElement built by this builder.
     *
     * @return The XML Axiom OMElemnt document
     */
    public OMElement getRootElement() {
        return root;
    }

    /**
     * Add an attribute to the root of this ebRIM object.
     *
     * @param name The name of the attribute
     * @param value The value of the attribute
     * @param isRequired True if this attribute is required to be present
     * @throws XdsRimException When a required attribute has the value null
     */
    public void addAttribute(String name, String value, boolean isRequired) throws XdsRimException {
        if (value != null) {       
            root.addAttribute(name, value, null);
        } else if (isRequired) {
            throw new XdsRimException("Required attribute \"" + name + "\" not supplied.");
        }
    }

    /**
     * Add a name to this ebRIM object.
     *
     * @param name The name to add
     * @param isRequired True if this name is required to be present
     * @throws XdsRimException When a required name has the value null
     */
    public void addName(String name, boolean isRequired) throws XdsRimException {
        if (name != null) {
            addRimNameElement(root, name, nsRim);
        } else if (isRequired) {
            throw new XdsRimException("Required Name not supplied.");
        }
    }

    /**
     * Add a description to this ebRIM object.
     *
     * @param comment The description to add
     * @param isRequired True if this description is required to be present
     * @throws XdsRimException When a required description has the value null
     */
    public void addDescription(String comment, boolean isRequired) throws XdsRimException {
        if (comment != null) {
            addRimDescriptionElement(root, comment, nsRim);
        } else if (isRequired) {
            throw new XdsRimException("Required Description not supplied.");
        }
    }

    /**
     * Add a single valued slot to this ebRIM object.
     *
     * @param name The slot name
     * @param value The slot value
     * @param isRequired True if this slot must have a value in this ebRIM object
     * @throws XdsRimException If this slot is required but has a null value
     */
    public void addSlot(String name, String value, boolean isRequired) throws XdsRimException {
        if (value != null) {
            addRimSlotElement(root, name, value, nsRim);
        } else if (isRequired) {
            throw new XdsRimException("Required slot \"" + name + "\" not supplied.");
        }
    }

    /**
     * Add a multi-valued slot to this ebRIM.
     *
     * @param name The slot name
     * @param values The slots values
     * @param isRequired True if this slot must have a value in this ebRIM object
     * @throws XdsRimException If this slot is required but has no value
     */
    public void addSlot(String name, List<String> values, boolean isRequired)  throws XdsRimException {
        if (values != null) {
            addRimSlotElement(root, name, values, nsRim);
        } else if (isRequired) {
            throw new XdsRimException("Required slot \"" + name + "\" not supplied.");
        }
    }

    /**
     * Add a classification to this ebRIM object.
     *
     * @param codeType The name of the code type to use for this classification
     * @param value The code value
     * @param classificationScheme The classification scheme for this code type
     * @param classId The classification id 
     * @param isRequired True if this classification must be in this ebRIM object
     * @throws XdsRimException When the classification value does correspond to a known code
     */
    public void addClassification(String displayName, String codingScheme, String value, String classificationScheme, String classId, boolean isRequired) throws XdsRimException {
        if (value == null) {
            if (isRequired)
                throw new XdsRimException("Required classification \"" + codingScheme + "\" not supplied.");
        } else {
                addRimClassificationElement(root, classId, id, value, classificationScheme, displayName, codingScheme, nsRim);
            }
     }

    /**
     * Add a multi-valued classification to this ebRIM object.
     *
     * @param codeType The name of the code type to use for this classification
     * @param values A collection of code values
     * @param classificationScheme The classification scheme for this code type
     * @param classId The classification id 
     * @param isRequired True if this classification must be in this ebRIM object
     * @throws XdsRimException  When the classification value does correspond to a known code
     */
    public void addClassification(String displayname,String coadingscheme, Collection<String> values, String classificationScheme, String classId, boolean isRequired) throws XdsRimException {
        if (values != null) {
            for (String value: values) {
                addClassification(displayname,coadingscheme, value, classificationScheme, classId, isRequired);
            }
        } else if (isRequired) {
            throw new XdsRimException("Required classification \"" + coadingscheme + "\" not supplied.");
        }
    }

    /**
     * Add a multi-fielded classification to this ebRIM object.
     * 
     * @param mainField The main field name
     * @param mainValue The value of main field
     * @param otherFields The name and values of other fields.
     * @param classificationScheme The classification scheme for this code type
     * @param classId The classification id
     * @param isRequired True if this classification must be in this ebRIM object
     * @throws XdsRimException  When the classification value does correspond to a known code
     */
    public void addClassification(String mainField, String mainValue, Map<String, List<String>> otherFields,
                                  String classificationScheme, String classId, boolean isRequired) throws XdsRimException {
        if( mainField != null && mainValue != null) {
            addRimClassificationElement(root, classId, id, classificationScheme, mainField, mainValue, otherFields, nsRim);
        } else if (isRequired) {
            throw new XdsRimException("Required classification \"" + mainField + "\" not supplied.");
        }
    }

    /**
     * Add an external identifier to this ebRIM object.
     *
     * @param name The name of the identifier
     * @param value The value
     * @param identificationScheme The identification scheme this identifier belongs to
     * @param registryObject The object id of registry, for example doc id.
     * @param id The id of this ExternalIndentifier
     * @param isRequired True if this identifier must be present in this ebRIM object
     * @throws XdsRimException When this identifier is required but has the value null
     */
    public void addExternalIdentifier(String name, String value, String identificationScheme, String registryObject, String id, boolean isRequired) throws XdsRimException  {
        if (value != null) {
            addRimExternalIdentifierElement(root, value, identificationScheme, name, registryObject, id, nsRim);
        } else if (isRequired) {
            throw new XdsRimException("Required external identifier \"" + name + "\" not supplied.");
        }
    }

    /* -------------------------------------------------------------------------------------- */

    /**
     * Add a new ebXML Axiom element of the given type and add it to the parent as
     * a child element.
     *
     * @param parent The parent element this should be added to
     * @param tagName The element tag name
     * @param rimNameSpace the rim name space of the root element
     * @return The created element
     */
    public static OMElement addRimElement(OMElement parent, String tagName, OMNamespace rimNameSpace)  {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMElement child = fac.createOMElement(tagName, rimNameSpace);        
    	parent.addChild(child);
        return child;
    }

    /**
     * Add a new ebXML Name element to the parent element.
     *
     * @param parent The parent element this should be added to
     * @param name The name data to put in the element
     * @param rimNameSpace the rim name space of the root element
     * @return The new name element
     */
    public static OMElement addRimNameElement(OMElement parent, String name, OMNamespace rimNameSpace)  {
        OMElement nameElement = addRimElement(parent, "Name", rimNameSpace);
        OMElement localized = addRimElement(nameElement, "LocalizedString", rimNameSpace);
        localized.addAttribute("value", trimRimString(name, RIM_LONG_NAME), null);
        return nameElement;
    }

    /**
     * Add a new ebXML Description element to the parent element.
     *
     * @param parent The parent element this should be added to
     * @param comment The comment data to put in the element
     * @param rimNameSpace the rim name space of the root element
     * @return The new name element
     */
    public static OMElement addRimDescriptionElement(OMElement parent, String comment, OMNamespace rimNameSpace){
        OMElement descElement = addRimElement(parent, "Description", rimNameSpace);
        OMElement localized = addRimElement(descElement, "LocalizedString", rimNameSpace);
        localized.addAttribute("value", trimRimString(comment, RIM_LONG_NAME), null);
        return descElement;
    }

    /**
     * Add a new ebXML Slot element to the parent element.
     *
     * @param parent The parent element this should be added to
     * @param name The name of the slot
     * @param value The value of the slot
     * @param rimNameSpace the rim name space of the root element
     * @return The new slot element
     */
    public static OMElement addRimSlotElement(OMElement parent, String name, String value, OMNamespace rimNameSpace) {
        OMElement slotElement = addRimElement(parent, "Slot", rimNameSpace);
        slotElement.addAttribute("name", name, null);
        OMElement valueList = addRimElement(slotElement, "ValueList", rimNameSpace);
        OMElement valueNode = addRimElement(valueList, "Value", rimNameSpace);
        OMFactory fac = OMAbstractFactory.getOMFactory();
        valueNode.addChild(fac.createOMText(trimRimString(value, RIM_LONG_NAME)));
        return slotElement;
    }

    /**
     * Add a new ebXML Slot element to the parent element.
     *
     * @param parent The parent element this should be added to
     * @param name The name of the slot
     * @param values The collection of values to put into the slot value list
     * @param rimNameSpace the rim name space of the root element
     * @return The new slot element
     */
    public static OMElement addRimSlotElement(OMElement parent, String name, List<String> values, OMNamespace rimNameSpace) {
        OMElement slotElement = addRimElement(parent, "Slot", rimNameSpace);
        slotElement.addAttribute("name", name, null);
        OMElement valueList = addRimElement(slotElement, "ValueList", rimNameSpace);
        OMFactory fac = OMAbstractFactory.getOMFactory();
        for (String value: values) {
        	if (value == null) continue;
            OMElement valueNode = addRimElement(valueList, "Value", rimNameSpace);
            valueNode.addChild(fac.createOMText(trimRimString(value, RIM_LONG_NAME)));
        }
        return slotElement;
    }

    /**
     * Add a new ebXML Classification element to the parent element.
     *
     * @param parent The parent element this should be added to
     * @param classId The classification id
     * @param id The id of the registry object being classified
     * @param value The classification code asigned ot the registry object
     * @param scheme The classification scheme/axis that the code applies to
     * @param display A human-friendly display name for the code value
     * @param codingScheme The coding scheme from which the code value is drawn
     * @param rimNameSpace the rim name space of the root element
     * @return The new classification element
     */
    public static OMElement addRimClassificationElement(OMElement parent, String classId, String id, String value,
                       String scheme, String display, String codingScheme, OMNamespace rimNameSpace) {
        OMElement classElement = addRimElement(parent, "Classification", rimNameSpace);
        classElement.addAttribute("id", classId, null);
        classElement.addAttribute("classificationScheme", scheme, null);
        classElement.addAttribute("classifiedObject", id, null);
        classElement.addAttribute("nodeRepresentation", value, null);
        addRimSlotElement(classElement, "codingScheme", codingScheme, rimNameSpace);
        addRimNameElement(classElement, display, rimNameSpace);
        return classElement;
    }

    /**
     * Add a new ebXML Classification element with multiple slots to the parent element.
     *
     * @param parent The parent element this should be added to
     * @param classId The classification id 
     * @param id The id of the registry object being classified
     * @param value The classification code asigned ot the registry object
     * @param scheme The classification scheme/axis that the code applies to
     * @param mainField The single valued main slot field
     * @param mainValue The single display value of the main field.
     * @param otherFields The multiple valued other slot fields.
     * @param codingScheme The coding scheme from which the code value is drawn
     * @param rimNameSpace the rim name space of the root element
     * @return The new classification element
     */
    public static OMElement addRimClassificationElement(OMElement parent, String classId, String id, String scheme,
             String mainField, String mainValue, Map<String, List<String>> otherFields, OMNamespace rimNameSpace)   {

        OMElement classElement = addRimElement(parent, "Classification", rimNameSpace);
        classElement.addAttribute("id", classId, null);
        classElement.addAttribute("classificationScheme", scheme, null);
        classElement.addAttribute("classifiedObject", id, null);
        classElement.addAttribute("nodeRepresentation", "", null);

        addRimSlotElement(classElement, mainField, mainValue, rimNameSpace);

        Set<Map.Entry<String, List<String>>> entries = otherFields.entrySet();
        for (Map.Entry<String, List<String>> entry : entries) {
            String key = entry.getKey();
            List<String> values = entry.getValue();
            if (values != null && values.size() > 0) {
                addRimSlotElement(classElement, key, values, rimNameSpace);
            }
        }
        return classElement;
    }

    /**
     * Add a new ebXML Classification element to the parent element.
     *
     * @param parent The parent element to get the classification
     * @param classId The classification id
     * @param id The id of the ebRIM object being classified
     * @param ref The registry reference of the internal classification scheme
     * @param rimNameSpace the rim name space of the root element
     * @return The new classification element
     */
    public static OMElement addRimClassificationElement(OMElement parent, String classId, String id, String ref, OMNamespace rimNameSpace) {
    	OMElement classElement = addRimElement(parent, "Classification", rimNameSpace);
        classElement.addAttribute("id", classId, null);
        classElement.addAttribute("classifiedObject", id, null);
        classElement.addAttribute("classificationNode", ref, null);
        return classElement;
    }

    /**
     * Add a new ebXML ExternalIdentifier element to the parent element.
     *
     * @param parent The parent element this should be added to
     * @param value The value to give to this identifier
     * @param scheme The identification scheme that this identifier is drawn from
     * @param display A human-friendly display name for the type of this identifier
     * @param registryObject The object id of registry, for example doc id.
     * @param id The id of this ExternalIndentifier
     * @param rimNameSpace the rim name space of the root element
     * @return The new external identifier element
     */
    public static OMElement addRimExternalIdentifierElement(OMElement parent, String value, String scheme,
                              String display, String registryObject, String id, OMNamespace rimNameSpace) {
    	OMElement identifierElement = addRimElement(parent, "ExternalIdentifier", rimNameSpace);
        identifierElement.addAttribute("id", id, null);
        identifierElement.addAttribute("registryObject", registryObject, null);
    	identifierElement.addAttribute("identificationScheme", scheme, null);
        identifierElement.addAttribute("value", value, null);
        addRimNameElement(identifierElement, display, rimNameSpace);
        return identifierElement;
    }

    /**
     * Add a new ebXML Association element that connects a document entry to a submission set.
     *
     * @param parent The parent element this should be added to
     * @param setId The id of the submission set
     * @param docId The id of the document entry
     * @param rimNameSpace the rim name space of the root element
     * @return The new association element
     */
    public static OMElement addRimSubmissionDocumentAssociationElement(OMElement parent,
                                                                         String setId, String docId, OMNamespace rimNameSpace)  {
        OMElement assocElement = addRimElement(parent, "Association", rimNameSpace);
        assocElement.addAttribute("id", "as-hasmember", null);
        assocElement.addAttribute("associationType", HAS_MEMBER, null);
        assocElement.addAttribute("sourceObject", setId, null);
        assocElement.addAttribute("targetObject", docId, null);
        addRimSlotElement(assocElement, "SubmissionSetStatus", "Original", rimNameSpace);
        return assocElement;
    }

    /**
     * Add a new ebXML Association element that connects a document entry to the document it is replacing.
     *
     * @param parent The parent element this should be added to
     * @param newId The id of the new document
     * @param oldId The id of the document being replaced
     * @param rimNameSpace the rim name space of the root element
     * @return The new association element
     */
    public static OMElement addRimDocumentReplacementAssociationElement(OMElement parent,
                                                                          String newId, String oldId, OMNamespace rimNameSpace) {
    	OMElement assocElement = addRimElement(parent, "Association", rimNameSpace);
        assocElement.addAttribute("id", "as-replace", null);
        assocElement.addAttribute("associationType", RPLC, null);
        assocElement.addAttribute("sourceObject", newId, null);
        assocElement.addAttribute("targetObject", oldId, null);
        return assocElement;
    }

    /**
     * Trim a string to the length specified in the RIM V.2 spec.
     *
     * @param input The input string
     * @param size The maximum allowed length
     * @return The input, trimmed to the specified length, if necssary
     */
    private static String trimRimString(String input, int size) {
        if (input == null) return null;
        if (input.length() <= size) {
            return input;
        } else {
            myLog.warn("Trimming ebXML RIM string to " + size + "characters: '" + input + "'");
            return input.substring(0, size);
        }
    }

}
