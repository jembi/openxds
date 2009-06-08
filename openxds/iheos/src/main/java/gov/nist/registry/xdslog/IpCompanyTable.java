package gov.nist.registry.xdslog;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Class storing a pair &lt; IP Address , Company Name &gt; in database.
 * @author jbmeyer
 *
 */
public class IpCompanyTable extends AbstractLogTable {

	
	public static String IP_ADDRESS = "ip" ;
	public static String COMPANY    = "company_name"      ;
	public static String EMAIL      = "email" ;

	public static String TABLE_NAME = "ip" ;
	
	private String      ipAddress      ;
	private String      companyName    ;
	private String      email          ;
	
    public static String writeSqlCommand = null ;
    public static String readSqlCommand  = null ;
    public static String createTableSqlCommand = null ;
    public static String deleteMessageCommand  = null ;
  
    private static PreparedStatement readPreparedStatement   ;
    private static PreparedStatement writePreparedStatement  ;


	private static String writeSqlCommand2  ;
	private static String updateSqlCommand2  ;
	
	private PreparedStatement writePreparedStatement2  ;
	private PreparedStatement updatePreparedStatement2 ;
	
	@SuppressWarnings("unused")
	private IpCompanyTable(){}
	public  IpCompanyTable ( Connection c  ) throws SQLException
	{
		tableName = TABLE_NAME ;
		database = c ;

		createTableSqlCommand    = "create table "+ tableName + " ( " + IP_ADDRESS +" varchar(100) not null PRIMARY KEY  , "+ COMPANY +" varchar(255) not null default 'Unknown' ," + EMAIL +" varchar(60) not null default 'Unknown'   );" ;
		readSqlCommand           = "select " + IP_ADDRESS +" , " + COMPANY + "," + EMAIL + " FROM " + tableName + " where "+ IP_ADDRESS + " = ? ;"  ;
		writeSqlCommand          = "insert into "+ tableName + " values (?,? ,?);"    ; 
		writeSqlCommand2         = "insert into ip ( ip ) values ( ?  );"           ;
		updateSqlCommand2        = "UPDATE ip SET company_name = ? WHERE ip = ? ;" ;
		
		if ( database== null || database.isClosed() )
			throw new SQLException("Database null or closed") ;
		readPreparedStatement   = database.prepareStatement(readSqlCommand )   ;
		writePreparedStatement  = database.prepareStatement(writeSqlCommand )  ;
		writePreparedStatement2 = database.prepareStatement(writeSqlCommand2)  ;
		updatePreparedStatement2= database.prepareStatement(updateSqlCommand2) ;
	}
	void readToDB(String ipAddress) throws SQLException 
	{
		if ( ipAddress != null )
		{
	
			readPreparedStatement.setString( 1 , ipAddress ) ;
			ResultSet res = readPreparedStatement.executeQuery() ;
			
			res.next() ;
			
			ipAddress   = res.getString(1)  ;
			companyName	= res.getString(2)  ;
		}
				
	}
	void readToDB( ) throws SQLException {
       if ( ipAddress !=null )
       {
    		readPreparedStatement.setString( 1 , ipAddress ) ;
			ResultSet res = readPreparedStatement.executeQuery() ;
			
			res.next() ;
			
			ipAddress   = res.getString(1)  ;
			companyName = res.getString(2)  ;
			email       = res.getString(3)  ;
       }
	}

	void setReadSqlCommand(String sqlCommand) 
	{  
		readSqlCommand = sqlCommand ;
	}

	void setWriteSqlCommand(String sqlCommand) 
	{
		writeSqlCommand = sqlCommand ;
	}

	/**
	 *  Write the ipAddress and the company Name in the table IP
	 * @param inIPAdress
	 * @param inCompany
	 * @return 0 if everything is ok , -1 if not
	 * @throws LoggerException
	 */
	int writeToDB( String inIPAdress, String inCompany ) throws LoggerException {
		if ( writePreparedStatement!= null )
		{
			try {
				writePreparedStatement.setString( 1 , inIPAdress      ) ;			
				writePreparedStatement.setString( 2 , inCompany       ) ;
				writePreparedStatement.setString( 3 , "Unknown"       ) ;
				writePreparedStatement.execute() ;
			    } catch (SQLException e) 
				{
				  throw new LoggerException ( "IpCompagnyTable:writeToDB( String inIPAdress, String inCompany) : (" + e.getErrorCode() + " )" + e.getMessage() ) ; 
				}
			return 0;
		}
		else
		return -1;
	}
	/**
	 *  Write the ipAddress and the company Name in the table IP
	 * @param inIPAdress
	 * @param inCompany
	 * @param inEmail
	 * @return 0 if everything is ok , -1 if not
	 * @throws LoggerException
	 */
	int writeToDB( String inIPAdress, String inCompany , String inEmail ) throws LoggerException {
		if ( writePreparedStatement!= null )
		{
			try {
				writePreparedStatement.setString( 1 , inIPAdress      ) ;			
				writePreparedStatement.setString( 2 , inCompany       ) ;
				writePreparedStatement.setString( 3 , inEmail         ) ;
				writePreparedStatement.execute() ;
			    } catch (SQLException e) 
				{
				  throw new LoggerException ( "IpCompagnyTable:writeToDB( String inIPAdress, String inCompany , String inEmail) : (" + e.getErrorCode() + " )" + e.getMessage() ) ; 
				}
			return 0;
		}
		else
		return -1;
	}
	/**
	 *  Write the ipAddress in the table IP with a default value "Unknown" for the company name
	 * @param inIPAdress
	 * @param inCompany
	 * @return 0 if everything is ok , -1 if not
	 * @throws LoggerException
	 */
	int writeToDB( String inIPAdress )  throws LoggerException {
		if ( writePreparedStatement2!= null )
		{
		try {
			writePreparedStatement2.setString( 1 , inIPAdress      ) ;
			writePreparedStatement2.execute() ;
			} catch (SQLException e) 
			{
			  throw new LoggerException ( "IpCompagnyTable:writeToDB( String inIPAdress ) : (" + e.getErrorCode() + " )" + e.getMessage() ) ; 
			}
			return 0;
		}
		else
		return -1;
	}
	
	/**
	 *  Write the current ipAddress  and company name in the table IP
	 * @param inIPAdress
	 * @param inCompany
	 * @return 0 if everything is ok , -1 if not
	 * @throws LoggerException
	 */

	int writeToDB() throws LoggerException {
		if ( writePreparedStatement!= null )
		{
			if ( ipAddress!=null && companyName!=null)
			{
			 try
			 {
			 writePreparedStatement.setString( 1 , ipAddress      ) ;
			 writePreparedStatement.setString( 2 , companyName    ) ;
			 writePreparedStatement.setString( 3 , email          ) ;
			 writePreparedStatement.execute() ;
			 
			 }
			 catch( SQLException sql)
			 {
				 throw new LoggerException("IPcompanyTable:writeToDB() problem : (" + sql.getErrorCode() + " )" + sql.getMessage()  ) ;
			 }
			}
			return 0;
		}
		else
		return -1;
	}
	
	/**
	 *  Update the ipAddress  with company name in the table IP. If the ip address doesn't exist, it is created
	 * @param inIPAdress
	 * @param inCompany
	 * @return 0 if everything is ok , -1 if not
	 * @throws LoggerException
	 */
	int updateIp( String inIPAdress, String inCompany ) throws LoggerException {
		if ( updatePreparedStatement2 != null )
		{
		 try
		 {
			if ( ipExist(inIPAdress) )
			{
			updatePreparedStatement2.setString( 1 , inCompany    ) ;
			updatePreparedStatement2.setString( 2 , inIPAdress   ) ;

			updatePreparedStatement2.execute() ;
			}
			else
			{
				writeToDB( inIPAdress , inCompany ) ;
			}
		 }
		 catch( SQLException sql)
		 {
			 throw new LoggerException("IPcompanyTable:updateIp( String inIPAdress, String inCompany ) problem : (" + sql.getErrorCode() + " )" + sql.getMessage()  ) ;
		 }
			return 0;
		}
		else
		return -1;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	/** 
	 * Test if the current ipAddress exists
	 * @param ipAddress
	 * @return
	 * @throws LoggerException
	 */
	public boolean ipExist ( String ipAddress   ) throws LoggerException {
	  
			if ( tableName!=null  )
			{
			 /*if ( tableExist( tableName  ) )
			 {*/
				 try {
					if ( database==null || database.isClosed() ) 
							throw new LoggerException("Database null or closed") ;
			
				 
						String sqlRequest = "SELECT count(*) from " + TABLE_NAME + " where  "+ IP_ADDRESS +"='"+ ipAddress +"' ;" ;
		
						Statement st = database.createStatement() ;
					    ResultSet res = st.executeQuery( sqlRequest ) ;
			
					    res.next() ;
					    if ( res.getInt(1) == 0 ) return false ;
					    else if ( res.getInt(1) > 0 ) return true ;   
				} catch (SQLException e) 
				{
					 throw new LoggerException("IPcompanyTable:ipExist( String ipAddress ) problem : (" + e.getErrorCode() + " )" + e.getMessage()  ) ;
				}
				  
			/* }
			 else return false ;*/
			}
			
			return false ;
	  }
	  public static boolean IpExist ( Connection c , String ipAddress   ) throws LoggerException 
	  {
			
			/* Reduction of the database calls
			 *  if ( TableExist( c , inTableName  ) )
			 {*/
				 try {
					if ( c==null || c.isClosed() ) 
							throw new LoggerException("Database null or closed") ;
			
				 
				   String sqlRequest = "SELECT count(*) from " + TABLE_NAME + " where  "+ IP_ADDRESS +"='"+ ipAddress +"' ;" ;
		
						Statement st = c.createStatement() ;
					    ResultSet res = st.executeQuery( sqlRequest ) ;
			
					    res.next() ;
					    if ( res.getInt(1) == 0 ) return false ;
					    else if ( res.getInt(1) > 0 ) return true ;   
				} catch (SQLException e) {
					 throw new LoggerException("IPcompanyTable: IpExist ( Connection c , String ipAddress   ) problem : (" + e.getErrorCode() + " )" + e.getMessage()  ) ;
				}
			
			return false ;
	  }
}
