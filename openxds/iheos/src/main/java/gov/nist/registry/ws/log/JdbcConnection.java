/*
 * JdbcConnection.java
 *
 * Created on October 4, 2004, 12:53 PM
 */

package gov.nist.registry.ws.log;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.LogFactory;



/**
 *
 * @author  andrew
 */
// Logging information into a PostgreSQL database.  Not finished!

public class JdbcConnection {
    
	private static final org.apache.commons.logging.Log log = LogFactory.getLog(JdbcConnection.class);
    
    private String hostname = null;
    private static Connection con;
    private static Statement stmt;
    private boolean successfulConnection = false;
    
    public JdbcConnection() throws SQLException {
        this.setHostname("localhost");
        this.initialize();
    }
    
    /** Creates a new instance of JdbcConnection */
    public JdbcConnection(String hostname) throws SQLException {
        this.setHostname(hostname);
        this.initialize();
    }
    private void initialize() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://" + this.getHostname() + "/logs";
            log.info("Connecting to postres on url " + url);
            try {
                con = DriverManager.getConnection(url, "postgres", "");
            } catch (Exception e) {
                con = DriverManager.getConnection(url, "logs", "xdslogs");
            }
            stmt = con.createStatement();
            successfulConnection = true;
        } catch (ClassNotFoundException e) {
            log.error(e);
            e.printStackTrace();
            successfulConnection = false;
        }
        
    }
    public void close() throws SQLException {
        con.close();
    }
    
    
    public ResultSet executeQuery(String sql) throws SQLException {
        ResultSet result = null;
        result = stmt.executeQuery(sql);
        return result;
    }
    
    public int executeUpdate(String sql) throws SQLException {
        int i = 0;
        i = stmt.executeUpdate(sql);
        return i;
    }
    public boolean isAlive() throws SQLException {
        return !con.isClosed();
    }
    
    /**
     * Getter for property hostname.
     * @return Value of property hostname.
     */
    public java.lang.String getHostname() {
        return hostname;
    }
    
    /**
     * Setter for property hostname.
     * @param hostname New value of property hostname.
     */
    public void setHostname(java.lang.String hostname) {
        this.hostname = hostname;
    }
    
    public static String makeSafe(String input) {
        String output = input.replaceAll("'", "\"");
        return output;
    }
    
}
