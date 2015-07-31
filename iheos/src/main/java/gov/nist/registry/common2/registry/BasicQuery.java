package gov.nist.registry.common2.registry;

import gov.nist.registry.common2.exception.MetadataException;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BasicQuery {
	protected final static Log logger = LogFactory.getLog(BasicQuery.class);
	
	public void secure_URI(Metadata metadata) throws MetadataException {
		for (OMElement doc : metadata.getExtrinsicObjects()) {
			int updated = 0;
			for (int sl=0; sl<10; sl++) {
				String uri_value = metadata.getSlotValue(doc, "URI", sl);
				if (uri_value == null) break;
				boolean save = false;
				if (uri_value.indexOf("http:") != -1) {
					updated++;
					save = true;
					uri_value = uri_value.replaceAll("http", "https");
				}
				if (uri_value.indexOf("9080") != -1) {
					updated++;
					save = true;
					uri_value = uri_value.replaceAll("9080", "9443");
				}
				if (save) {
					metadata.setSlotValue(doc, "URI", sl, uri_value);
				}
				if (updated >= 2) break;
			}
		}
	}

}
