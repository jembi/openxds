package gov.nist.registry.common2.registry;

import java.util.HashMap;
import java.util.Map;

public class SoapActionFactory {
	
	public final static String pnr_b_async_action = "urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bAsync";
	public final static String pnr_b_action       = "urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-b";
	public final static String ret_b_action       = "urn:ihe:iti:2007:RetrieveDocumentSet";
	public final static String ret_b_async_action = "urn:ihe:iti:2007:RetrieveDocumentSetAsync";
	public final static String anon_action        = "urn:anonOutInOp";
	
	
	private static final Map<String, String> actions =
		new HashMap<String, String>()
		{
		
		     {
		    	 put(pnr_b_action,       "urn:ihe:iti:2007:ProvideAndRegisterDocumentSet-bResponse");
		    	 put(pnr_b_async_action, "urn:ihe:iti:2007:DocumentSource_ProvideAndRegisterDocumentSet-bAsyncResponse");
		    	 put("urn:ihe:iti:2007:RegisterDocumentSet-b",           "urn:ihe:iti:2007:RegisterDocumentSet-bResponse");
		    	 put(ret_b_action,       "urn:ihe:iti:2007:RetrieveDocumentSetResponse");
		    	 put(ret_b_async_action, "urn:ihe:iti:2007:RetrieveDocumentSetAsyncResponse");
		    	 put("urn:ihe:iti:2007:RegistryStoredQuery",             "urn:ihe:iti:2007:RegistryStoredQueryResponse");
		    	 put("urn:ihe:iti:2007:RegistryStoredQueryAsync",        "urn:ihe:iti:2007:RegistryStoredQueryAsyncResponse");
		    	 put("urn:ihe:iti:2007:CrossGatewayRetrieve",            "urn:ihe:iti:2007:CrossGatewayRetrieveResponse");
		    	 put("urn:ihe:iti:2007:CrossGatewayQuery",               "urn:ihe:iti:2007:CrossGatewayQueryResponse");
		    	 put("urn:ihe:iti:2007:CrossGatewayQueryAsync",          "urn:ihe:iti:2007:CrossGatewayQueryAsyncResponse");
		     }
		
		};

	static public String getResponseAction(String requestAction) {
		if (requestAction == null) return null;
		return actions.get(requestAction);
	}
}
