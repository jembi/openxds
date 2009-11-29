package gov.nist.registry.xdslog;

import gov.nist.registry.common2.logging.LoggerException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;

public class DeleteMessages
{

    public static void main ( String args[])
    {
 
	if ( args.length > 0 )
	{    
	      
        	GregorianCalendar calendar = new GregorianCalendar() ;
        	System.out.println( " now : " + calendar.getTimeInMillis() ) ;
        	DatabaseConnection c;
        	try
        	{
        	    c = new DatabaseConnection("jdbc:postgresql://127.0.0.1/log2","logs","xdslogs");
        	    ResultSet result = c.database.createStatement().executeQuery(  "select messageid from main order by messageid asc limit 1 ;" ) ;
        	    result.next() ;
        	    System.out.println("Last Message " + result.getString(1).substring(0,13) ) ;
        	    long lower = new Long(result.getString(1).substring(0,13)) ;
        	    
        	    //calculate n days before : 
        	    calendar.setTimeInMillis(calendar.getTimeInMillis() - ( 86400000* new Long	(args[0]) ) ) ;
            	    long dateFrom = calendar.getTimeInMillis() ;
            	    System.out.println(dateFrom) ;
            	    HashSet<String> set = AbstractLogTable.ListTable(c.getDatabase()) ;
            	      System.out.println("  " +lower/100000000 +  "  " + dateFrom/100000000) ;
            	    for ( long l = lower/100000000 ; l < (dateFrom/100000000)-1 ; l++   )
            	    {
            	       Iterator<String> it = set.iterator() ;
            	       while (  it.hasNext() )
            	       {
            		 String value = it.next() ;
            		if ( !value.equals("ip") )
            		{
            		  System.out.println( "delete from "+ value +" where messageid like '"+ l + "%';" ) ;
            		  c.getDatabase().createStatement().execute("delete from "+ value +" where messageid like '"+ l + "%';" ) ;
            		}
            	       }
            	     }
          
            	    System.out.println( "dateFrom " + dateFrom + " lower " + lower ) ;
            	    
            	    
            	    c.closeDataBase() ;
        	    c =  null ;
        	    
        	} catch (SQLException e)
        	{
        	    // TODO Auto-generated catch block
        	    e.printStackTrace();
        	} catch (LoggerException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	}
    }
}
