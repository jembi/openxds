package gov.nist.registry.xdslog;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database connection class. Load the postgres driver and create a connection
 * @author jbmeyer
 *
 */
public class DatabaseConnection {

	protected Connection database ;  

	public DatabaseConnection( Connection c ) throws SQLException
	{
		database = c ;

	}
	public DatabaseConnection( String url, String user, String password ) throws SQLException
	{
		database = DatabaseConnection.ConnectDatabase(url,user,password) ;

	}
	public static Connection ConnectDatabase(String url , String login,String password)
	{
		//Loading the Driver        
		try
		{
			Class.forName("org.postgresql.Driver");
		}
		catch (ClassNotFoundException e)
		{
			System.err.println(e.getMessage()) ;
		}
		// Get a connection to the PostgreSQL database.
		Connection dataBase = null ;
		try
		{
			dataBase = DriverManager.getConnection(url, login,password);
			System.out.println("The connection to the database was  succesfully opened.");
		}
		catch (SQLException e)
		{
			System.err.println(e.getMessage()) ;
		}

		return dataBase ;
	}

	public boolean ifDatabaseExist( String databaseName ) throws Exception
	{
		if( database != null )
		{
			Statement st = database.createStatement();
			ResultSet result = st.executeQuery("select count(*) from pg_database where datname='" + databaseName + "';" ) ;
			result.next() ;
			if (result.getInt(1)== 0 ) throw new Exception ("Database "+ databaseName +" not found" ) ;


		}
		else return false ;

		return true ;
	}
	public void closeDataBase( ) throws SQLException
	{
		database.close() ;
	}              

	public Connection getDatabase() {
		return database;
	}


}
