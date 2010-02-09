package gov.nist.registry.xdslog;


import gov.nist.registry.common2.logging.LogMessage;
import gov.nist.registry.common2.logging.LoggerException;
import gov.nist.registry.xds.log.BaseEntry;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Class allowing to connect the logger to the database, to create message, to delete it and to read it
 * @author jbmeyer
 *
 */
public class Log {
	private static final org.apache.commons.logging.Log log = LogFactory.getLog(Log.class);

	/**
	 * Path of the Log parameter file. This file contains the connection parameters. <br />
	 * it should be formated like that : <br />
	 * <b>&lt;?xml version="1.0"?&gt;</b>
     * <br/>
	 *	&lt;config&gt; <br/>
	 *	&nbsp;&nbsp;&nbsp;&nbsp;&lt;connection host ="127.0.0.1" db="logs" user="postgres" password="1qaz!QAZ" /&gt;<br/>
	 *	&lt;/config&gt;
     *
	 */
	public static String LOG_PARAMETERS_FILE = "/Users/bill/IheOs/workspace_prod/xdsLog/gov/nist/registry/xdslog/ParamFile.txt" ;	
	
	private DatabaseConnection connection = null ;
	
	MainTable mainMessage = null ;
	BaseEntry base        = null ;
	
	public Log() throws LoggerException
	{
		if ( connection == null  )
		    readConfigurationFile() ;
		
			init() ;
	}
	public Log(String parameterFile ) throws LoggerException
	{
		LOG_PARAMETERS_FILE = parameterFile ;
		if ( connection == null  )
		    readConfigurationFile() ;
		
			init() ;;
	}
	public Log ( Connection c ) throws LoggerException
	{
	    try
	    {
		connection = new DatabaseConnection( c ) ;
	    } catch (SQLException e)
	    {
		throw new LoggerException ( e.getMessage() ) ;
	    }
	}
	public Log(String urlConnection , String user , String password ) throws LoggerException
	{
		 if ( connection == null )
		 {
			try {
				connection = new DatabaseConnection( urlConnection ,  user , password ) ;
			} catch (SQLException e) 
			{
				throw new LoggerException(  e.getMessage() ) ;
			}
		 }
	       
		 init() ;
		
	}
	
	private void readConfigurationFile() throws LoggerException
	{
		   
        Document doc;
        try {
            DocumentBuilder builder =
                DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = builder.parse(new FileInputStream(LOG_PARAMETERS_FILE));
            
            Element el = (Element)doc.getFirstChild() ;
            
            NodeList nodelists = el.getChildNodes() ;
            
            for (int i=0; i<nodelists.getLength(); i++ ) {
                Node n = nodelists.item(i);
                Element e = null ;
                if (n.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                else
                {
                  e = (Element) n ;
                }
                
                if (n.getNodeName().equalsIgnoreCase("connection") == true ) 
                {
                	 
                	 log.info("connection new : " + "jdbc:postgresql://" + e.getAttribute("host") + "/"+  e.getAttribute("db") + " ," + e.getAttribute("user") + "," + e.getAttribute("password") ) ;
                	 if ( connection == null )
                	 {
						try {
							connection =  new DatabaseConnection("jdbc:postgresql://" + e.getAttribute("host") + "/"+  e.getAttribute("db") ,  e.getAttribute("user"), e.getAttribute("password")) ;
						} catch (SQLException e1) {
							throw new LoggerException( e1.getMessage() ) ;
						}
                	 }
                }
            
            }
            
        } catch (ParserConfigurationException e) {
            System.err.println("SyslogReliableCollector: Error parsing config\n" +
                    e.getMessage());
            return;
        } catch (SAXException e) {
            System.err.println("SyslogReliableCollector: Error parsing config\n" +
                    e.getMessage());
            return;
        } catch (IOException e) {
            System.err.println("SyslogReliableCollector: Error parsing config\n" +
                    e.getMessage());
            return;
        } 
	}
	/**
	 * Create the main Table and the ip table if needed.
	 * @throws LoggerException
	 */
	private void init() throws LoggerException 
	{
		IpCompanyTable ipTable;
		try {
			ipTable = new IpCompanyTable( connection.getDatabase()  );
		
			if ( !ipTable.tableExist() ) ipTable.createTable(IpCompanyTable.createTableSqlCommand) ;
			ipTable = null ;
			
			MainTable main = new MainTable(connection.getDatabase() ) ;
			// bill - changed MainTable to main in following line
			if ( !main.tableExist() ) main.createTable(main.createTableSqlCommand ) ;
            main = null ;
		} catch (SQLException e) {
			throw new LoggerException ( "log::init() " + e.getMessage() ) ;
		}
   }

	
	/**
	 * Create a message with message ID made with the IPAddress and the current timestamp in millisecond
	 * @param ipAddress
	 * @return
	 * @throws LoggerException
	 */
	public Message createMessage( String ipAddress ) throws LoggerException
	{
		Timestamp t = new Timestamp( new GregorianCalendar().getTimeInMillis())  ;
		String id = t.getTime() + ipAddress ;
        
		Message m = new Message( connection.getDatabase()  , id) ;
		m.setIP( ipAddress ) ;
		m.setTimeStamp( t ) ;
		
		return m  ;
		
	}
	/**
	 * Create an empty message
	 * @return
	 * @throws LoggerException
	 */
	public Message createMessage(  ) throws LoggerException
	{
		Timestamp t = new Timestamp( new GregorianCalendar().getTimeInMillis())  ;
	 
 
		Message m = new Message( connection.getDatabase()  ) ;
	 
		m.setTimeStamp( t ) ;
	
		
		return m  ;
		
	}
	
	/**
	 * Same as writeMessage. Calls the writeMessage method to log the message in
	 *  database
	 * @param m
	 * @throws LoggerException
	 */
    public void logMessage( Message m ) throws LoggerException
    {
    	if ( m != null )
    		m.writeMessage() ;
    }
	/**
	 * Same as writeMessage. Calls the writeMessage method to log the message in
	 *  database
	 * @param m
	 * @throws LoggerException
	 */
    public void writeMessage( LogMessage m ) throws LoggerException
    {

        if ( m != null )	
    		((Message)m).writeMessage() ;
    }

	public void deleteMessage ( Message m ) throws LoggerException
	{
		if ( m != null )
			m.deleteMessage() ;
			
	}
	public void deleteMessage ( String messageID ) throws LoggerException
	{
		if ( messageID != null )
		{
		Message m = new Message( connection.getDatabase()  , messageID ) ;
		deleteMessage( m ) ;
		}			
	}
    public Message readMessage ( String messageID ) throws LoggerException
    {
    	Message m = null ;
    	if ( messageID != null )
    	{
    	 m = new Message( connection.getDatabase()  , messageID ) ;
    	 m.readMessage() ;
    	}
    	return m ;
    }
	public void close ( ) throws LoggerException
	{
		try
		{	
		 
		  connection.closeDataBase() ;		  
		  connection = null ;
		  MainTable.CleanPreparedStatement() ;
		 
		}
		catch ( SQLException sql )
		{
			throw new LoggerException ( "Log::close() " + sql.getMessage() ) ;
		}
	}

	public Connection getConnection() {
		return connection.getDatabase() ;
	}
}
