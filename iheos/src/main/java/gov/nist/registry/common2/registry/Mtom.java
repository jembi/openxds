package gov.nist.registry.common2.registry;

import gov.nist.registry.common2.exception.XdsIOException;
import gov.nist.registry.common2.io.Io;

import java.io.IOException;
import java.io.InputStream;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMText;

import org.apache.commons.codec.binary.Base64;

public class Mtom {
	private OMElement document;
	private String content_type;
	private byte[] contents;
	private boolean xop;
	
	public boolean isOptimized() { return xop; }

	public void decode(OMElement document) throws XdsIOException, IOException {
		this.document = document;
		OMText binaryNode = (OMText) document.getFirstOMChild();
		//System.out.println("isOptimized: " + binaryNode.isOptimized());

		xop = binaryNode.isOptimized(); 
		
		if (xop) {
			javax.activation.DataHandler datahandler = (javax.activation.DataHandler) binaryNode.getDataHandler();
			InputStream is = null;
			try {
				is = datahandler.getInputStream();
				contents = Io.getBytesFromInputStream(is);
			}
			catch (IOException e) {
				throw new XdsIOException("Error accessing XOP encoded document content from message");
			}
			this.content_type = datahandler.getContentType();
		} else {
			String base64 = binaryNode.getText();
			contents = Base64.decodeBase64(base64.getBytes());
			this.content_type = null;
		}

	}

	public String getContent_type() {
		return content_type;
	}

	public byte[] getContents() {
		return contents;
	}
	
}
