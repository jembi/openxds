package gov.nist.registry.common2.service;


import gov.nist.registry.common2.registry.SoapActionFactory;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.engine.MessageReceiver;

public class XDSRawXMLInMessageReceiver extends   AbstractXDSRawXMLInMessageReceiver

implements MessageReceiver {
	
//	private static final Map<String, String> actions =
//		new HashMap<String, String>()
//		{
//		
//		     {
//		    	 put("urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b", "urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse");
//		    	 put("urn:ihe:iti:2007:RegisterDocumentSet-b",           "urn:ihe:iti:2007:RegisterDocumentSet-bResponse");
//		    	 put("urn:ihe:iti:2007:RetrieveDocumentSet",             "urn:ihe:iti:2007:RetrieveDocumentSetResponse");
//		    	 put("urn:ihe:iti:2007:RegistryStoredQuery",             "urn:ihe:iti:2007:RegistryStoredQueryResponse");
//		    	 put("urn:ihe:iti:2007:CrossGatewayRetrieve",            "urn:ihe:iti:2007:CrossGatewayRetrieveResponse");
//		    	 put("urn:ihe:iti:2007:CrossGatewayQuery",               "urn:ihe:iti:2007:CrossGatewayQueryResponse");
//		     }
//		
//		};

	public void validate_action(MessageContext msgContext) {
	}

}
