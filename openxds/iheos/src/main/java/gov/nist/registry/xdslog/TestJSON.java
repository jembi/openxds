package gov.nist.registry.xdslog;

import org.json.JSONException;
import org.json.JSONStringer;

public class TestJSON
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
	try
	{
	    String myString = new JSONStringer()
	         .object()
	             .key("JSON")
	             .value("Hello\\\", World!")
	         .endObject()
	         .toString();
	    System.out.println(myString) ;
	} catch (JSONException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

}
