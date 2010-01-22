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

package org.openhealthtools.common.utils;

import java.io.ByteArrayInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;

/**
 * This utility class defines some help methods for OMElement.
 * 
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class OMUtil {

	/**
	 * Converts from an well-structured XML string to an OMElement.
	 *  
	 * @param input_string the input XML string to be converted
	 * @return an OMElement represents the XML string
	 * @throws XMLStreamException
	 */
	public static OMElement xmlStringToOM(String inputString) throws XMLStreamException {
		byte[] ba = inputString.getBytes();

//		create the parser
		XMLStreamReader parser = XMLInputFactory.newInstance().createXMLStreamReader(new ByteArrayInputStream(ba));
//		create the builder
		StAXOMBuilder builder = new StAXOMBuilder(parser);

//		get the root element (in this case the envelope)
		OMElement documentElement =  builder.getDocumentElement();

		return documentElement;
	}

}
