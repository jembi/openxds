package gov.nist.registry.ws;

import static org.junit.Assert.assertTrue;
import gov.nist.registry.common2.registry.MetadataSupport;

import java.io.IOException;
import java.util.Iterator;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMText;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.openhealthtools.common.utils.OMUtil;

/**
 * 
 * @author arnouten
 */
public class ProvideAndRegisterDocumentSetTest {
	
	/**
	 * The 'document' node of a p&r might contain a whitespace node before the actual document data. This occurred, for example,
	 * in a provide by ISC at the 2010 European Connectathon. This did not result in an error, but in an empty document to be
	 * 'successfully' submitted.
	 * 
	 *  The p&r handler was modified to ignore whitespace nodes. This unittest tests this.
	 * 
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	@Test
	public void testWhitespaceBeforeDocument() throws IOException, XMLStreamException
	{
		String message = IOUtils.toString(ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/submit_document.xml"));
		String documentString = IOUtils.toString(ProvideAndRegisterDocumentSetTest.class.getResourceAsStream("/data/referral_summary.xml"));
		//replace document and submission set uniqueId variables with actual uniqueIds. 
		message = message.replace("$XDSDocumentEntry.uniqueId", "2.16.840.1.113883.3.65.2." + System.currentTimeMillis());
		message = message.replace("$XDSSubmissionSet.uniqueId", "1.3.6.1.4.1.21367.2009.1.2.108." + System.currentTimeMillis());
		//message = message.replace("$patientId", patientId);
		//replace the document uuid.
		//String uuid = getUUID();
		//message = message.replace("$doc1", uuid);

		for (Boolean includeWhitespace : new Boolean[] { false, true })
		{
			OMElement request = OMUtil.xmlStringToOM(message);
			request = addOneDocument(request, documentString, "doc1", includeWhitespace);
	
			for (OMElement document : MetadataSupport.childrenWithLocalName(request, "Document")) {
				OMText text = ProvideAndRegisterDocumentSet.getBinaryNode(document);
				assertTrue("Blank document. Included whitespace: " + includeWhitespace, StringUtils.isNotBlank(text.getText()));
			}
		}
	}
	
	protected OMElement addOneDocument(OMElement request, String document, String documentId, boolean includeWhitespace) throws IOException {
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace ns = fac.createOMNamespace("urn:ihe:iti:xds-b:2007" , null);
		OMElement docElem = fac.createOMElement("Document", ns);
		docElem.addAttribute("id", documentId, null);

        // A string, turn it into an StreamSource
	    DataSource ds = new ByteArrayDataSource(document, "text/xml"); 
		DataHandler handler = new DataHandler(ds);
		 
        OMText binaryData = fac.createOMText(handler, true);
        if (includeWhitespace)
        {
	        /** The whitespace */
	        docElem.addChild(fac.createOMText("\n"));
        }
        docElem.addChild(binaryData);

        Iterator iter = request.getChildrenWithLocalName("SubmitObjectsRequest");
        OMElement submitObjectsRequest = null;
        for (;iter.hasNext();) {
        	submitObjectsRequest = (OMElement)iter.next();
        	if (submitObjectsRequest != null)
        		break;
        }
        submitObjectsRequest.insertSiblingAfter(docElem);
        return request;
	}
}
