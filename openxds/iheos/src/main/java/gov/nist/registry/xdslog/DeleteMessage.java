package gov.nist.registry.xdslog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.FactoryConfigurationError;


public class DeleteMessage
{
	public static String LOG_PARAMETERS_FILE = "./gov/nist/registry/xdslog/ParamFile.txt" ;
	
    public static Connection ConnectDatabase(String url, String login,
            String password) {
        //Loading the Driver        
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        // Get a connection to the PostgreSQL database.
        Connection dataBase = null;
        try {
            dataBase = DriverManager.getConnection(url, login, password);
            System.out.println("The connection to the database was  succesfully opened.");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return dataBase;
    }
    
    public static void usage()
    {
        System.out.println ( "DeleteMessage [Options] \n" ) ;
        System.out.println ( "-d --date YYYY-MM-DD    : Delete all messages received during this date. The date MUST be formed as yyyy-MM-DD \n") ;
        System.out.println ( "-f --from YYYY-MM-DD    : should be used with the option -t or --to. Delete all messages between these to dates. If the -t (--to) is not" ) ; 
        System.out.println ( "                       specified all messages form that date to now are deleted") ;
        System.out.println ( "-h --help               : Display this help message") ;
        System.out.println ( "-n --number N           : Delete the message specified by its number." ) ; 
        System.out.println ( "-t --to YYYY-MM-DD      : Delete all messages received before that date.") ; 
        System.out.println ( "-o --older numberOfDays : Delete all messages received older than 'number' days") ; 

    }
    
    public static String parseArgsToSqlCommand( String[] args )
    {
        StringBuffer sqlCommand = new StringBuffer ( "select messageid from main where  " ) ;
        boolean firstArgument = true ;
        String fromValue = null ;
        String toValue   = null ;
        for ( int i = 0 ; i < args.length ; i++ )
        {
            if ( args[i].equals("-n") || args[i].equals( "--number") )
            {
                
                if ( i++ < args.length  ) 
                {
                    if ( firstArgument == false ) sqlCommand.append( " and " ) ;
                    firstArgument = false ;                   
                    sqlCommand.append( "messageid='" ) ;
                    sqlCommand.append(args[i]) ;
                    sqlCommand.append( "';" ) ;
                }                
            }
            if ( args[i].equals("-f") || args[i].equals("--from") )
            {
                if ( i++ < args.length  ) 
                {              
                    fromValue = args[i] ;
                }                                
            }
            if ( args[i].equals( "-t") || args[i].equals("--to") )
            {
                if ( i++ < args.length  ) 
                {              
                    toValue = args[i] ;
                }                                
            }
            if ( args[i].equals("-d") || args[i].equals("--date") )
            {
                if ( i++ < args.length  ) 
                {            
                    if ( firstArgument == false ) sqlCommand.append( " and " ) ;
                    firstArgument = false ;                   
                    sqlCommand.append( "timereceived like '" ) ;
                    sqlCommand.append(args[i]) ;
                    sqlCommand.append( "%';");
                    
                }                                
            }
            if ( args[i].equals("-o") || args[i].equals("--older") )
            {
                if ( i++ < args.length  ) 
                {            
                    Date d = new Date() ;
                    long milliseconds = d.getTime() -( new Long(args[i])*86400000L) ;  
                    d.setTime(milliseconds) ;
                    
                    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd") ;
                   
                    toValue =   simpleDate.format( d ) ;
                    System.out.println("Deleting messages before : " + toValue ) ;                        
                }                                
            }
            if (  args[i].equals("-h") || args[i].equals("--help") )
            {
                usage() ;
                System.exit(0) ;
            }
        }
        if ( fromValue == null && toValue!=null )
        {
            if ( firstArgument == false ) sqlCommand.append(" and ") ;
            firstArgument = false ;
            sqlCommand.append( "timereceived <= '" ) ;
            sqlCommand.append( toValue ) ;
            sqlCommand.append( "' ;" ) ;
        }
        else if ( fromValue != null && toValue==null  )
        {
            if ( firstArgument == false ) sqlCommand.append(" and ") ;
            firstArgument = false ;
            sqlCommand.append( "timereceived >= '" ) ;
            sqlCommand.append( fromValue ) ;
            sqlCommand.append( "' ;" ) ;
        }
        else if (  fromValue != null && toValue!=null  )
        {
            if ( firstArgument == false ) sqlCommand.append(" and ") ;
            firstArgument = false ;
            sqlCommand.append( "timereceived >= '" ) ;
            sqlCommand.append( fromValue ) ;
            sqlCommand.append( "'" ) ;
            sqlCommand.append(" and ") ;
            sqlCommand.append( "timereceived <= '" ) ;
            sqlCommand.append( toValue ) ;
            sqlCommand.append( "' ;" ) ;
        }
        
        System.out.println( sqlCommand.toString()) ;
        return sqlCommand.toString() ;
    }
    
   
    public static void main ( String[] args )
    {
	Date d = new Date();
	System.out.println("Beggining at " + d ) ;
        String sqlCommand = parseArgsToSqlCommand(args) ;
        int numberDeleted = 0 ;
        try
        {
    		Log log  = new Log( "/workspace/xdsLogV3/gov/nist/registry/xdslog/ParamFile.txt"   ) ;    
          
            Statement st =  log.getConnection().createStatement()       ;
            ResultSet result = st.executeQuery( sqlCommand ) ;
            
            while ( result.next() )
            {
                System.out.println( "Deleting Message number " + result.getString( 1 ) + "..." ) ;
                log.deleteMessage( result.getString( 1 )  ) ;
                numberDeleted++ ;
            }
            
        log.close() ;
           
        System.out.println("Deleted " + numberDeleted + (numberDeleted==1? " message" : " messages") ) ;
        Date date2 = new Date() ;
        System.out.println( "Date End " + date2 + " " + new Long((date2.getTime()-d.getTime())/(24000)) + " minutes" ) ;
        }
      
        catch (FactoryConfigurationError e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (LoggerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

    }

}
