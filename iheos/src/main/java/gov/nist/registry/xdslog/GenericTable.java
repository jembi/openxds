package gov.nist.registry.xdslog;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.json.JSONArray;


/**
 * A generic table is a table containing 3 fields :
 *  <ui>
 *    <li>messageid</li>
 *    <li>name</li>
 *    <li>value</li>
 *   </ui>
 *  This table allow to store for a message , several parameters with their values
 * @author jbmeyer
 *
 */

public class GenericTable extends AbstractLogTable {
 	
	public static String MESSAGE_ID = "messageid" ;
	public static String NAME       = "name"      ;
	public static String VALUE      = "value"     ;
	
    private String writeSqlCommand = null ;
    private String readSqlCommand  = null ;
    private String createTableSqlCommand = null ;
    private String deleteMessageCommand  = null ;
  
    private PreparedStatement readPreparedStatement   ;
    private PreparedStatement writePreparedStatement  ;
    private PreparedStatement deletePreparedStatement ;
	
	private String      parameterName  ;
	private String      parameterValue ;

	
	private GenericTable(){}
	
	/**
	 * Initiate the generic table in the specified database ( Connection c ) , with a specified name
	 * @param c
	 * @param inTableName
	 * @throws LoggerException 
	 * @throws SQLException
	 */
	public  GenericTable ( Message m , String inTableName  ) throws LoggerException
	{
		tableName = inTableName ;
		database = m.getConnection() ;

		if ( createTableSqlCommand == null ) createTableSqlCommand    = "create table "+ tableName + " ( " + MESSAGE_ID +" varchar(255) not null REFERENCES " +  MainTable.TABLE_NAME + "("+ MainTable.MESSAGE_ID +") ON DELETE CASCADE ON UPDATE CASCADE , "+ NAME +" varchar(255) not null , " + VALUE +" text   );" ;
		if ( readSqlCommand        == null ) readSqlCommand           = "select " + MESSAGE_ID +" , " + NAME +" ," + VALUE + " FROM " + tableName + " where "+ MESSAGE_ID + " = ? ;"  ;
		if ( writeSqlCommand       == null ) writeSqlCommand          = "insert into "+ tableName + " values (?,?,?);"   ; 
		if ( deleteMessageCommand  == null ) deleteMessageCommand     = "delete FROM "+  inTableName +" WHERE "+ MESSAGE_ID + " =? ;" ;
		
		try 
		{
			if ( database== null || database.isClosed() )
				throw new LoggerException("Database null or closed") ;

			if ( readPreparedStatement   == null ) readPreparedStatement    = database.prepareStatement(readSqlCommand )      ;
			if ( writePreparedStatement  == null ) writePreparedStatement   = database.prepareStatement(writeSqlCommand )     ;
			if ( deletePreparedStatement == null ) deletePreparedStatement  = database.prepareStatement(deleteMessageCommand) ;
           
			if ( !m.getTableList().contains(inTableName) ) 
				{
				  createTable( createTableSqlCommand ) ;
				  m.setTableList( this.listTable() ) ;
				}
		} 
		catch ( SQLException sqlException ) 
		{
			throw new LoggerException("Database problem (SqlException ) " + sqlException.getMessage()) ;			
		}
	}
	
	/**
	 * Read a message in the detabase and return an array of parameters and values
	 * @param inMessageId
	 * @return GenericTable[], an array of generic table containing the messageId, parameters names and values
	 * @throws SQLException
	 */
	public Vector<GenericTable> readFromDB(String inMessageId) throws LoggerException
	{
		Vector<GenericTable> vector = null ;
		if ( inMessageId!=null )
		{
			vector = new Vector<GenericTable>() ;
			try
			{
			readPreparedStatement.setString( 1 , inMessageId ) ;
			ResultSet res = readPreparedStatement.executeQuery() ;
				while ( res.next() )
				{
					GenericTable gt = new GenericTable() ;
					gt.setParameterName( res.getString(2)  ) ;
					gt.setParameterValue( res.getString(3) ) ;			   
					vector.add( gt ) ;
				}
			}
			catch ( SQLException sqlException )
			{
				throw new LoggerException("Database problem (SqlException ) " + sqlException.getMessage()) ;		
			}
			
		}
		return vector ;	
		
	}
	
	public void setReadSqlCommand(String sqlCommand) 
	{  
		readSqlCommand = sqlCommand ;
	}

	public void setWriteSqlCommand(String sqlCommand) 
	{
		writeSqlCommand = sqlCommand ;
	}


	/**
	 *  write the parameter and its value for the current message. The current name and value are used.
	 */
	public int writeToDB( )  throws LoggerException {
		if ( writePreparedStatement!= null )
		{
			try
			{
			  if ( messageId == null )
				  	throw new LoggerException( "MaintTable:writeToDB() : messageId is null" ) ;

			  writePreparedStatement.setString( 1 , messageId      ) ;
			  writePreparedStatement.setString( 2 , parameterName  ) ;
			  writePreparedStatement.setString( 3 , parameterValue ) ;
			
			  writePreparedStatement.execute() ;
			}
			catch ( SQLException sqlException )
			{
				throw new LoggerException("Database problem (SqlException ) " + sqlException.getMessage() + "\n" + writePreparedStatement.toString() ) ;
			}
			return 0;
		}
		else
		return -1;
	}

	public void setMessageID( String messageID)
	{
		messageId = messageID ;
	}
	public String getMessageID ( String messageID )
	{
		return messageId ;
	}
	public String getParameterValue() {
		return parameterValue;
	}
	public void setParameterValue( String parameterValue ) {
		this.parameterValue = parameterValue;
	}
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String toString( )
	{
		return parameterName + ":" + parameterValue + "\n" ;
	}
	public String toXml( )
	{
		return "<node name=\""+   parameterName  +":\" xvalue=\""+  parameterValue  + "\" />" ;
	}
	
	public String[] toStringArray() {
		String[] vals = { parameterName, parameterValue };
		return vals;
	}
	
	public String toJSon( )
	{
	    
		JSONArray array = new JSONArray() ;
		array.put(parameterName) ;
		array.put(parameterValue) ;
		return array.toString(); 
	}
	 
 
	
	public String getCreateTableSqlCommand() {
		return createTableSqlCommand;
	}

	public String getDeleteMessageCommand() {
		return deleteMessageCommand;
	}

	public String getReadSqlCommand() {
		return readSqlCommand;
	}

	public String getWriteSqlCommand() {
		return writeSqlCommand;
	}

	public void deleteMessage(String messageId) {
		deleteMessage( messageId , deletePreparedStatement ) ;		
	}


}
