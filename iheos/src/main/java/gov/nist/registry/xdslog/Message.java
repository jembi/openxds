package gov.nist.registry.xdslog;


import gov.nist.registry.common2.logging.LogMessage;
import gov.nist.registry.common2.logging.LoggerException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;


/**
 * Class describing a message to log in database. It contains several action log, read
 *  and delete a message.
 * @author jbmeyer
 *
 */
public class Message implements LogMessage {
	
	private String messageID      ; 

	private MainTable mainMessage ;

	private Hashtable < String , Vector<GenericTable> > miscVectors ;
	private HashSet <String> tableList ;
	
	private Connection connection ;
	@SuppressWarnings("unused")
	private String companyName = null ;
	
	@SuppressWarnings("unused")
	private Message() 
	{}
	/**
	 * 
	 * @param c Connection. The database connection. 
	 * @param id String. The identifier of the message.
	 * @throws LoggerException
	 */
	public Message( Connection c , String id ) throws LoggerException 
	{
  	  connection = c ;
	  
  	  mainMessage = new MainTable( connection   ) ;
	  mainMessage.setMessageId(id) ;

	  miscVectors = new Hashtable<String, Vector<GenericTable>> ( ) ;
	  tableList = GenericTable.ListTable( connection ) ;
	  messageID = id ;
	}
	/**
	 * 
	 * @param c Connection. The database connection. 
	 * @param id String. The identifier of the message.
	 * @throws LoggerException
	 */
	public Message( Connection c  ) throws LoggerException 
	{
  	  connection = c ;
	  
  	  mainMessage = new MainTable( connection   ) ;
	  

	  miscVectors = new Hashtable<String, Vector<GenericTable>> ( ) ;
	  tableList = GenericTable.ListTable( connection ) ;
 
	}
    public void setTimeStamp ( Timestamp timestamp  )
    {
    	mainMessage.setTimestamp(timestamp) ;
    }
    public void setSecure ( boolean isSecure  )
    {
    	mainMessage.setSecure(isSecure) ;
    }

	public void setTestMessage ( String testMessage )
	{
		mainMessage.setTest( testMessage ) ;
	}
	
	public void setPass ( boolean pass )
	{
		mainMessage.setPass( pass ) ;
	}
	public void setIP ( String ip ) throws LoggerException
	{
		String ipStr = ip;
		try 
		{
			ipStr = InetAddress.getByName(ip).getHostAddress() ;
		} catch (UnknownHostException e) { }
		mainMessage.setIpAddress(ipStr) ;
	}
	
	/***
	 * 
	 * @param companyName String. Make a pair in the IP table between an IP address and a company name. If this pair doesn't exist
	 * in the IP table, it's logged, if it exists, the pair is updated with the new company name.
	 * @throws LoggerException
	 */
	public void setCompany ( String companyName ) throws LoggerException
	{
		if ( mainMessage.getIpAddress() == null )
			 throw new LoggerException( "Message:setCompany ( String companyName ):: Cannot set company name , the current IP adress associated is null") ;
		try 
		{
			IpCompanyTable ip = new IpCompanyTable( connection ) ;

			ip.updateIp( mainMessage.getIpAddress() , companyName ) ;
			
			this.companyName = companyName ;
			
		} catch (SQLException e) {
			 throw new LoggerException( "Message:setCompany ( String companyName ):: " + e.getMessage() ) ;

		}
	}

	/**
	 * Generic function creating a pair &lt; parameter name , parameter value &gt; for the current messageID. <br />
	 * If the Table Name doesn't exist , it is created before writing the message ( in the writeMessage method ).
	 * The parameter is stored in a hashtable &lt; tableName , Vector &lt;GenericTable &gt; &gt; 
	 * 
	 * @param tableName, table name wherein the parameter name and value are logged
	 * @param name , parameter name
	 * @param value ,parameter value
	 * @throws LoggerException
	 */
	public void addParam ( String tableName , String name , String value ) throws LoggerException
	{
		if ( tableName !=null )
		{
			tableName = tableName.trim().replace(' ', '_').toLowerCase() ;
			GenericTable newGenericTable = new GenericTable( this , tableName.trim()   ) ;
			newGenericTable.setMessageID(messageID) ;
			newGenericTable.setParameterName ( name  ) ;
			newGenericTable.setParameterValue( value ) ;
			
			if ( miscVectors.containsKey(tableName))
			{
				Vector <GenericTable> genTable= miscVectors.get(tableName) ;
				genTable.add(newGenericTable) ;
			}
			else
			{
				Vector <GenericTable> genTable= new Vector<GenericTable>() ;
				genTable.add(newGenericTable) ;
				miscVectors.put(tableName , genTable) ;
			}
		}
		else
			throw new LoggerException("TableName is null") ;
	}
	
	/**
	 * Used for xdsTestLog. Same as addParam ( "http" ,  name ,  value ) ;
	 * @param name
	 * @param value
	 * @throws LoggerException
	 */
	public void addHTTPParam( String name , String value ) throws LoggerException
	{
		addParam ( "http" ,  name ,  value ) ;
	}
	/**
	 * Used for xdsTestLog. Same as addParam ( "soap" ,  name ,  value ) ;
	 * @param name
	 * @param value
	 * @throws LoggerException
	 */
	public void addSoapParam( String name , String value ) throws LoggerException
	{
		addParam ( "soap" ,  name ,  value ) ;
	}
	
	/**
	 * Used for xdsTestLog. Same as addParam ( "error" ,  name ,  value ) ;
	 * @param name
	 * @param value
	 * @throws LoggerException
	 */
	public void addErrorParam( String name , String value ) throws LoggerException
	{
		addParam ( "error" ,  name ,  value ) ;
	}
	
	/**
	 * Used for xdsTestLog. Same as addParam ( "other" ,  name ,  value ) ;
	 * @param name
	 * @param value
	 * @throws LoggerException
	 */
	public void addOtherParam ( String name , String value ) throws LoggerException
	{
		addParam ( "other" ,  name ,  value ) ;
	}
	
	/**
	 * Read the message with the current MessageID and store all data in the mainMessage attribute and the hashmap miscVector
	 * @throws LoggerException
	 */
	public void readMessage() throws LoggerException 
	{
		 if ( messageID == null )
			 throw new LoggerException("Message:readMessage()  messageID is null " ) ;
		 
		 mainMessage.readFromDB( messageID ) ;
		 
		 Iterator< String > it = tableList.iterator() ;
		
		 while ( it.hasNext() )
		 {
			 String currentTable = it.next() ;
			 if ( !currentTable.equals("main") && !currentTable.equals("ip"))
			 {
				 GenericTable gt = new GenericTable( this , currentTable ) ;
				 Vector <GenericTable> vectGenTable = gt.readFromDB( messageID ) ;
				 
				 miscVectors.put( currentTable , vectGenTable ) ;
				 
			 }
			 
		 }
	}
	
    /** 
     * Write the message in the database 
     * @throws LoggerException
     */
	public void writeMessage( ) throws LoggerException
	{	 
		synchronized(this)
		{
			
		//	if ( companyName ==null ) setCompany("Unknown") ;
			
			try
			{
				 mainMessage.writeToDB() ;
			}
			catch( LoggerException l ){}
			
			
			Set < Entry <String, Vector<GenericTable>>> set = miscVectors.entrySet() ;
			Iterator < Entry <String, Vector<GenericTable>>> it = set.iterator() ;
			while ( it.hasNext() )
			{
				Entry <String, Vector<GenericTable>> currentElement = it.next() ;
				Vector<GenericTable> v = currentElement.getValue() ;
				
				for ( int i = 0 ; i < v.size() ; i++ )
				{
					v.elementAt(i).writeToDB() ;
				}
				
				v = null ;
				currentElement = null ;
			}
		}
	}
	
	public void deleteMessage() throws LoggerException
	{	
	    // No need more because of delete cascade
		 mainMessage.deleteMessage( messageID  ) ;
	}
	
	
	/**
	 * Method used to display the message in the xds log reader. This method format the message in XML displaying first the 
	 * list of table ( nodes ) available and then the content of the message.
	 * @return
	 */
	
	public String toXml()
	{
		StringBuffer buff = new StringBuffer() ;
		
		StringBuffer buffNodeNames = new StringBuffer() ;
		buffNodeNames.append("<Nodes>") ;
		buffNodeNames.append("<Node name='mainMessage' />") ;
		buff.append(mainMessage.toXml()) ;
		
		Iterator< String > it = tableList.iterator() ;
			
		while ( it.hasNext() )
		{
		   String currentTable = it.next() ;
			if ( !currentTable.equals("main") && !currentTable.equals("ip"))
			{
				Vector <GenericTable > v = miscVectors.get(currentTable) ;
				buffNodeNames.append("<Node name='"+ currentTable + "' />") ;
				buff.append( "<"+ currentTable +">" ) ;
				for ( int i = 0 ; i < v.size() ; i++ )
				{
					buff.append( v.elementAt(i).toXml() ) ;
				}
				buff.append( "</"+ currentTable +">" ) ;
			}
		}
		
		buffNodeNames.append( "</Nodes>" ) ;
		
		
		
		return "<message number='"+   messageID  +"'>" + buffNodeNames.toString() + buff.toString() + "</message>" ;
	}
	
	public HashMap<String, HashMap<String, Object>> toHashMap()
	{
		HashMap<String, HashMap<String, Object>> values = new HashMap<String, HashMap<String, Object>>();
				
		values.put("main", mainMessage.toHashMap());
		
		Iterator< String > it = tableList.iterator() ;
			
		while ( it.hasNext() )
		{
		   String currentTable = it.next() ;
			if ( !currentTable.equals("main") && !currentTable.equals("ip"))
			{
				Vector <GenericTable > v = miscVectors.get(currentTable) ;
				HashMap<String, Object> thisTable = new HashMap<String, Object>();
				for ( int i = 0 ; i < v.size() ; i++ )
				{
					String[] parm = v.elementAt(i).toStringArray();
					String key = parm[0].replaceAll(" ", "_");
					String value = parm[1];
					
					Object oldValueObject = thisTable.get(key);
					if (oldValueObject == null) {
						thisTable.put(key, value);
					} else {
						if (oldValueObject instanceof String) {
							ArrayList<String> newValue = new ArrayList<String>();
							newValue.add((String)oldValueObject);
							newValue.add(value);
							thisTable.put(key, newValue);
						} else {
							ArrayList<String> newValue = (ArrayList<String>) oldValueObject;
							newValue.add(value);
							thisTable.put(key, newValue);
						}
					}
				}
				values.put(currentTable, thisTable);
			}
		}
		
		return values;
		
		
		//return "<message number='"+   messageID  +"'>" + buffNodeNames.toString() + buff.toString() + "</message>" ;
	}
	
	public String toJSon()
	{
		StringBuffer buff = new StringBuffer() ;
		
		buff.append("{\"message\" : { \n" +
			       "\"number\": \""+   messageID  +"\" , \n  " ) ;
		buff.append ( "\"table\": \n\t["    ) ;
			      
		
		buff.append( mainMessage.toJSon() ) ;
		
		Iterator< String > it = tableList.iterator() ;
			
		while ( it.hasNext() )
		{
		   String currentTable = it.next() ;
			if ( !currentTable.equals("main") && !currentTable.equals("ip"))
			{
				Vector <GenericTable > v = miscVectors.get(currentTable) ;
				buff.append( ",\n{\"name\" : \"" + currentTable +"\",\n" ) ;
				buff.append( "\"values\" : [" ) ;
				for ( int i = 0 ; i < v.size() ; i++ )
				{
					buff.append( v.elementAt(i).toJSon() ) ;
					if ( i < v.size() ) buff.append(",\n") ;
				}
				buff.append("]\n}") ;
			  	
				 
			}
		}
		buff.append("]\n}\n}") ;
				
		return  buff.toString() ;
	}
	
	 
	public Connection getConnection() {
		return connection;
	}
	public HashSet<String> getTableList() {
		return tableList;
	}
	public void setTableList(HashSet<String> tableList) {
		this.tableList = tableList;
	}
	public String getMessageID() {
		return messageID;
	}
	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	

}
