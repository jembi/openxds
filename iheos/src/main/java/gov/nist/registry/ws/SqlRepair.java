package gov.nist.registry.ws;

import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.MetadataSupport;

import java.util.Iterator;
import java.util.List;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.xpath.AXIOMXPath;
import org.jaxen.JaxenException;

public class SqlRepair {
	int start_at_word = 0;
	
	public String returnType(OMElement query) {
		
		OMElement response_option_ele = MetadataSupport.firstChildWithLocalName(query, "ResponseOption");
		if (response_option_ele == null)
			return null;
		String return_type = response_option_ele.getAttributeValue(MetadataSupport.return_type_qname);
		return return_type;
	}
	
	public void repair(OMElement query) throws XdsInternalException, SqlRepairException {
		OMElement query_element = find_sql_query_element(query);
		String sql_text = query_element.getText().trim();
		
		OMElement response_option_ele = MetadataSupport.firstChildWithLocalName(query, "ResponseOption");
		if (response_option_ele == null)
			throw new SqlRepairException("Cannot find ResponseOption element");
		String return_type = response_option_ele.getAttributeValue(MetadataSupport.return_type_qname);
		if (return_type == null) 
			throw new SqlRepairException("Cannot find returnType option of ResponseOption");
		
		if (!return_type.equals("ObjectRef") && !return_type.equals("LeafClass") )
			throw new SqlRepairException("Invalid returnType: only ObjectRef or LeafClass are acceptable");

		String x = sql_text.replaceAll("\\(", " ( ");
		x = x.replaceAll("\\)", " ) ");
		String[] words = x.split("[ \t\n]");

		boolean repaired = true;
		int i = 0;
		start_at_word = 0;
		for ( ; i<20 && repaired; i++) {
//			System.out.println("fix?");
			 repaired = repair_once(words, return_type);
//			 if (repaired) {
//				 System.out.print("Fixed: ");
//				 for (int j=0; j<words.length; j++) System.out.print(" " + words[j]);
//				 System.out.println("");
//			 }
		}
		if (i >= 20)
			throw new SqlRepairException("SQL repair failed - cannot forward to backend registry");

		StringBuffer buf = new StringBuffer();
		for (int j=0; j<words.length; j++ ) {
			buf.append(" " + words[j]);
		}
		
		query_element.setText(buf.toString());
	}

	private boolean repair_once(String[] words, String return_type)
			throws SqlRepairException {
		
		
		int select_i = -1;
		int from_i = -1;
		
//		System.out.println("starting at " + start_at_word);
		
		for (int i=start_at_word; i<words.length && (select_i == -1 || from_i == -1); i++) {
			if ( select_i == -1 && words[i].equalsIgnoreCase("select")) {
				select_i = i;
			}
			if ( from_i == -1 && words[i].equalsIgnoreCase("from")) {
				from_i = i;
			}
		}
		
		if (start_at_word > 0 && select_i == -1)
			return false;   // done
		
		// for next time through loop
		start_at_word = from_i + 1;
		
		if (select_i == -1) 
			throw new SqlRepairException("SqlRepair: SQL keyword SELECT not found");
		if (from_i == -1) 
			throw new SqlRepairException("SqlRepair: SQL keyword FROM not found");
		if (from_i < select_i) {
			throw new SqlRepairException("SqlRepair: SQL keyword FROM found before keyword SELECT");
		}
		int range_from = select_i + 1;
		int range_to = from_i - 1;
		
		if (range_to < range_from)
			throw new SqlRepairException("SqlRepair: Cannot find SELECT variable");
		
		for (int i=range_from; i<=range_to; i++) {
			String word = words[i];
			if (word.equals("*")) 
				throw new SqlRepairException("SqlRepair: XDS does not allow SQL queries using SELECT *, must be SELECT something.id" );
			if (word.equalsIgnoreCase("distinct"))
				continue;
		}
		
		boolean fixed = false;
//		System.out.println("select_i = " + select_i + " from_i = " + from_i + " returnType = " + return_type);
		for (int i=select_i+1; i< from_i; i++ ) {
			String word = words[i];
			if (word.endsWith(".id") && return_type.equals("LeafClass")) {
				words[i] = "*";
				fixed = true;
			}
		}
		return fixed;
		
//		StringBuffer buf = new StringBuffer();
//		for (int i=0; i<words.length; i++) {
//			buf.append(words[i]);
//			buf.append(" ");
//		}
//		String repaired_sql_text = buf.toString();
//		System.out.println(repaired_sql_text.substring(0, 80));
//		return repaired_sql_text;
	}
	
	public String get_query_text(OMElement query) throws XdsInternalException, SqlRepairException {
		OMElement element = find_sql_query_element(query);
		return element.getText();
	}

	public OMElement find_sql_query_element(OMElement query) throws XdsInternalException, SqlRepairException {
		try {
			AXIOMXPath xpathExpression = new AXIOMXPath ("//*[local-name() = 'SQLQuery']");
			List nodeList = xpathExpression.selectNodes(query); 
			Iterator it = nodeList.iterator();
			if (! it.hasNext())
				throw new SqlRepairException("Cannot find SQLQuery element in query");
			OMElement q = (OMElement) it.next();
			return q;
		} catch (JaxenException e) {
			throw new XdsInternalException("Jaxen Exception inside SQLRepair:find_sql_query_element");
		}
	}
	
}
