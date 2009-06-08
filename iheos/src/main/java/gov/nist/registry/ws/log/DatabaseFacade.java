/*
 * DatabaseFacade.java
 *
 * Created on October 8, 2004, 1:27 PM
 */

package gov.nist.registry.ws.log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 *
 * @author  andrew
 */
// Logging information into a PostgreSQL database.  Not finished!


public class DatabaseFacade {
    
    private JdbcConnection connection = null;
    
    private static String hostname = null;
    
    private static DatabaseFacade instance = null;
    
    // Eventually move these out to a different, mediator class.
    
    static final String idCol = "id";
    
    static final String baseTable = "base";
    static final String baseTimestampCol = "timestamp";
    static final String baseIpCol = "ip";
    
    static final String soapTable = "soap";
    static final String soapMetadataCol = "metadata";
    static final String soapAttachmentCountCol = "attachmentCount";
    
    static final String testTable = "test";
    static final String testTestCol = "test";
    static final String testPassCol = "pass";
    static final String testMessageCol = "message";
    
    static final String httpTable = "http";
    static final String httpHeaderCol = "header";
    static final String httpValueCol = "value";
    
    static final String bodyTable = "body";
    static final String bodyHttpBody = "httpbody";
    
    static final String errorTable = "error";
    static final String errorName = "name";
    static final String errorDescription = "description";
    
    static final String messageTable = "message";
    static final String messageName = "name";
    static final String messageMessage = "message";
    
    static final String attachmentTable = "attachment";
    static final String attachmentNumber = "number";
    static final String attachmentContenttype = "contenttype";
    static final String attachmentHash = "hash";
    static final String attachmentSize = "size";
    
    static final String companyTable = "company";
    static final String ipCompany = "ip";
    static final String companyCompany = "company";
    
    
    /** Creates a new instance of DatabaseFacade */
    private DatabaseFacade() throws java.sql.SQLException {
        DatabaseFacade.setHostname("localhost");
        this.setConnection(new JdbcConnection(DatabaseFacade.getHostname()));
    }
    private DatabaseFacade(String hostname) throws java.sql.SQLException {
        DatabaseFacade.setHostname(hostname);
        this.setConnection(new JdbcConnection(DatabaseFacade.getHostname()));
    }
    static public DatabaseFacade getInstance() throws java.sql.SQLException { // only called on servlet side
        if (instance == null)
            instance = new DatabaseFacade();
        instance.affirmConnection();
        return instance;
    }
    static public DatabaseFacade getNewInstance() throws java.sql.SQLException { // only called by log reader
        if(instance != null)
            instance.closeConnection();
        instance = new DatabaseFacade(DatabaseFacade.getHostname());
        return instance;
    }
    public boolean affirmConnection() throws java.sql.SQLException {
        
        if(connection == null) {
            this.setConnection(new JdbcConnection());
            return false;
        }
        try {
            if (!connection.isAlive()) {
                connection.close();
                this.setConnection(new JdbcConnection());
                return false;
            }
        } catch (Exception e) {
            this.setConnection(new JdbcConnection());
            return false;
        }
        
        return true;
        
    }
    public void resetConnection(String hostname) throws java.sql.SQLException {
        DatabaseFacade.setHostname(hostname);
        this.setConnection(new JdbcConnection(DatabaseFacade.getHostname()));
    }
//    public boolean addBaseEntry(BaseEntry base) throws java.sql.SQLException {
//        
//        StringBuffer sb = new StringBuffer();
//        sb.append("INSERT INTO ");
//        sb.append(baseTable);
//        sb.append(" ");
//        sb.append("(" + this.idCol + "," + this.baseTimestampCol + "," + this.baseIpCol + ")");
//        sb.append(" VALUES ");
//        sb.append("('" + base.getId() + "','" + base.getTimestamp().getTime() + "','" + base.getIp() + "');");
//        
//        
//        int i = 0;
//        
//        i = this.getConnection().executeUpdate(sb.toString());
//        
//        if (i == 0) return false;
//        
//        return true;
//        
//    }
    
    public boolean doesIpExist(String ip) {
        
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT COUNT (\"" + this.companyCompany + "\") ");
        sb.append("FROM " + this.companyTable + " ");
        sb.append("WHERE " + this.ipCompany + " = '" + ip + "';");
        ResultSet result = null;
        int i = 0;
        try {
            result = connection.executeQuery(sb.toString());
            result.next();
            i = result.getInt("count");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        if (i > 0)
            return true;
        return false;
    }
    
    public String getCompanyFromIp(String ip) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT " + this.companyCompany + " ");
        sb.append("FROM " + this.companyTable + " ");
        sb.append("WHERE " + this.ipCompany + " = '" + ip + "';");
        ResultSet result = null;
        String company = null;
        try {
            result = connection.executeQuery(sb.toString());
            result.next();
            company = result.getString(this.companyCompany);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return company;
    }
    
    public boolean addCompanyEntry(CompanyEntry company) {
        
        if(this.doesIpExist(company.getIp()))
            this.removeCompanyEntry(company);
        
        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO ");
        sb.append(this.companyTable);
        sb.append(" ");
        sb.append("(" + this.ipCompany + "," + this.companyCompany + ")");
        sb.append(" VALUES ");
        sb.append("('" + company.getIp() + "','" + company.getCompany() + "');");
        
        int i = 0;
        
        try {
            i = this.getConnection().executeUpdate(sb.toString());
        } catch (java.sql.SQLException e) {
            return false;
        }
        if (i == 0) return false;
        
        return true;
    }
    
    public boolean removeCompanyEntry(CompanyEntry company) {
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE ");
        sb.append("FROM " + this.companyTable + " ");
        sb.append("WHERE " + this.ipCompany + " = '" + company.getIp()  + "';");
        
        int i = 0;
        
        try {
            i = this.getConnection().executeUpdate(sb.toString());
        } catch (java.sql.SQLException e) {
            return false;
        }
        if (i == 0) return false;
        
        return true;
        
    }
    
//    public boolean addHttpEntry(HttpEntry http) throws java.sql.SQLException {
//        //      this.affirmConnection();
//        String id = http.getId();
//        Hashtable headers = http.getHeaders();
//        boolean returnValue = true;
//        
//        Enumeration headerNames = headers.keys();
//        while(headerNames.hasMoreElements()) {
//            String headerName = (String) headerNames.nextElement();
//            String headerValue = (String) headers.get(headerName);
//            if(this.addSingleHttpEntry(id,headerName,headerValue) == false)
//                returnValue = false;
//        }
//        return returnValue;
//        
//    }
//    public boolean addBodyEntry(BodyEntry body) {
//        //     this.affirmConnection();
//        StringBuffer sb = new StringBuffer();
//        sb.append("INSERT INTO ");
//        sb.append(this.bodyTable);
//        sb.append(" ");
//        sb.append("(" + this.idCol + "," + this.bodyHttpBody + ")");
//        sb.append(" VALUES ");
//        sb.append("('" + body.getId() + "','" + body.getBody() + "')");
//        
//        int i = 0;
//        try {
//            i = this.getConnection().executeUpdate(sb.toString());
//            
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//        if (i == 0)
//            return false;
//        return true;
//    }
//    public boolean addTestEntry(TestEntry test) throws java.sql.SQLException {
//        //   this.affirmConnection();
//        StringBuffer sb = new StringBuffer();
//        sb.append("INSERT INTO ");
//        sb.append(this.testTable);
//        sb.append(" ");
//        sb.append("(" + this.idCol + "," + this.testTestCol + "," + this.testPassCol + "," + this.testMessageCol + ")");
//        sb.append(" VALUES ");
//        sb.append("('" + test.getId() + "'," + test.getTest() + "," + test.isPass() + ",'" + test.getMessage() + "');");
//        
//        int i = 0;
//        try {
//            i = this.getConnection().executeUpdate(sb.toString());
//            
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//        if (i == 0)
//            return false;
//        return true;
//        
//    }
//    
//    public boolean addErrorEntry(ErrorEntry error) {
//        //    this.affirmConnection();
//        StringBuffer sb = new StringBuffer();
//        sb.append("INSERT INTO ");
//        sb.append(this.errorTable);
//        sb.append(" ");
//        sb.append("(" + this.idCol + "," + this.errorName + "," + this.errorDescription + ")");
//        sb.append(" VALUES ");
//        sb.append("('" + error.getId() + "','" + error.getName() + "','" + error.getDescription() + "');");
//        
//        int i = 0;
//        try {
//            i = this.getConnection().executeUpdate(sb.toString());
//            
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//        if (i == 0)
//            return false;
//        return true;
//    }
//    public boolean addSoapEntry(SoapEntry soap) {
//        //    this.affirmConnection();
//        StringBuffer sb = new StringBuffer();
//        sb.append("INSERT INTO ");
//        sb.append(this.soapTable);
//        sb.append(" ");
//        sb.append("(" + this.idCol + "," + this.soapMetadataCol + "," + this.soapAttachmentCountCol + ")");
//        sb.append(" VALUES ");
//        sb.append("('" + soap.getId() + "','" + soap.getMetadata() + "'," + soap.getNumberAttachments() + ");");
//        
//        int i = 0;
//        try {
//            i = this.getConnection().executeUpdate(sb.toString());
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//        if(i == 0)
//            return false;
//        return true;
//    }
//    
//    public boolean addAttachmentEntry(AttachmentEntry attachment) {
//        //     this.affirmConnection();
//        StringBuffer sb = new StringBuffer();
//        sb.append("INSERT INTO ");
//        sb.append(this.attachmentTable);
//        sb.append(" ");
//        sb.append("(" + this.idCol + "," + this.attachmentNumber + "," + this.attachmentContenttype + ",");
//        sb.append(this.attachmentHash + "," + this.attachmentSize + ")");
//        sb.append(" VALUES ");
//        sb.append("('" + attachment.getId() + "'," + attachment.getNumber() + ",'" + attachment.getContenttype() + "','");
//        sb.append(attachment.getHash() + "'," + attachment.getSize() + ");");
//        
//        int i = 0;
//        try {
//            i = this.getConnection().executeUpdate(sb.toString());
//            
//        } catch(java.sql.SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//        if (i == 0)
//            return false;
//        return true;
//    }
//    
//    public boolean addMessageEntry(MessageEntry message) {
//        //    this.affirmConnection();
//        StringBuffer sb = new StringBuffer();
//        
//        sb.append("INSERT INTO ");
//        sb.append(this.messageTable);
//        sb.append(" ");
//        sb.append("(" + this.idCol + "," + this.messageName + "," + this.messageMessage + ")");
//        sb.append(" VALUES ");
//        sb.append("('" + message.getId() + "','" + message.getName() + "','" + message.getMessage() + "');");
//        
//        int i = 0;
//        try {
//            i = this.getConnection().executeUpdate(sb.toString());
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//        if (i == 0)
//            return false;
//        return true;
//        
//    }
//    
//    public boolean addSingleHttpEntry(String id, String headerName, String headerValue) {
//        //    this.affirmConnection();
//        StringBuffer sb = new StringBuffer();
//        sb.append("INSERT INTO ");
//        sb.append(httpTable);
//        sb.append(" ");
//        sb.append("(" + this.idCol + "," + this.httpHeaderCol + "," + this.httpValueCol + ")");
//        sb.append(" VALUES ");
//        sb.append("('" + id + "','" + headerName + "','" + headerValue + "');");
//        
//        int i = 0;
//        
//        try {
//            i = this.getConnection().executeUpdate(sb.toString());
//            
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//            return false;
//            
//        }
//        if( i == 0) return false;
//        
//        return true;
//        
//        
//    }
//    
//    public ResultSet getBaseEntries(String ipFilter, int secondsFilter) {
//        //    this.affirmConnection();
//        StringBuffer sb = new StringBuffer();
//        sb.append("SELECT * ");
//        sb.append("FROM " + this.baseTable + " ");
//        
//        
//        if((ipFilter != null && !ipFilter.equals("")
//        || secondsFilter > 0)) {
//            sb.append("WHERE ");
//            if(ipFilter != null && !ipFilter.equals("")) {
//                sb.append(this.baseIpCol + " = '" + ipFilter + "' ");
//                if(secondsFilter > 0)
//                    sb.append("AND ");
//            }
//            if(secondsFilter > 0) {
//                double time = new Date().getTime() - (secondsFilter * 1000);
//                sb.append(this.baseTimestampCol + " > " + time);
//            }
//        }
//        
//        
//        
//        ResultSet result = null;
//        try {
//            result = this.getConnection().executeQuery(sb.toString());
//        } catch (java.sql.SQLException e) {
//            return null;
//        }
//        return result;
//    }
//    
    public Collection getIpListFromCompany(String companyName) {
        
        //    this.affirmConnection();
        StringBuffer sb = new StringBuffer();
        
        sb.append("SELECT DISTINCT " + this.ipCompany + " ");
        sb.append("FROM " + this.companyTable + " ");
        sb.append("WHERE " + this.companyCompany + " = '" + companyName + "';");
        
        ResultSet result = null;
        Collection ipList = new ArrayList();
        try {
            result = connection.executeQuery(sb.toString());
            while(result.next()) {
                ipList.add(result.getString(this.ipCompany));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ipList;
    }
    public Hashtable getIpCompany() {
        //      this.affirmConnection();
        StringBuffer sb = new StringBuffer();
        
        sb.append("SELECT * ");
        sb.append("FROM " + this.companyTable + " ");
        
        ResultSet result = null;
        Hashtable localCompanies = new Hashtable();
        try {
            result = connection.executeQuery(sb.toString());
            while(result.next()) {
                String ip = result.getString(this.ipCompany);
                String companyName = result.getString(this.companyCompany);
                
                if(!localCompanies.containsKey(ip))
                    localCompanies.put(ip,companyName);
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return localCompanies;
        
        
    }
    
    public ResultSet getBaseAndTestEntries(String ipFilter, int secondsFilter, String testFilter,String companyName) {
        //    this.affirmConnection();
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT b.id, b.ip, b.timestamp, t.test, t.pass ");
        sb.append("FROM " + this.baseTable + " b LEFT OUTER JOIN " + this.testTable + " t USING (" + this.idCol + ")");
        //        sb.append("WHERE b." + this.idCol + " = t." + this.idCol);
        
        if((ipFilter != null && !ipFilter.equals(""))
        || secondsFilter > 0
        || (testFilter != null && !testFilter.equals(""))
        || (companyName != null && !companyName.equals("")) && !companyName.equalsIgnoreCase("All")) {
            sb.append(" WHERE ");
            if(ipFilter != null && !ipFilter.equals("")) {
                sb.append(this.baseIpCol + " Like '" + ipFilter + "' ");
                if(secondsFilter > 0 || (testFilter != null && !testFilter.equals("") || (companyName != null && !companyName.equals("") && !companyName.equalsIgnoreCase("All"))) )
                    sb.append("AND ");
            }
            if(secondsFilter > 0) {
                double time = new Date().getTime() - (secondsFilter * 1000);
                sb.append(this.baseTimestampCol + " > " + time + " ");
                if(testFilter != null && !testFilter.equals("") || (companyName != null && !companyName.equals("") && !companyName.equalsIgnoreCase("All")))
                    sb.append("AND ");
            }
            if(testFilter != null && !testFilter.equals("")) {
                sb.append("t." + this.testTestCol + " = '" + testFilter + "' ");
                if(companyName != null && !companyName.equals("") && !companyName.equalsIgnoreCase("All"))
                    sb.append("AND ");
            }
            if(companyName != null && !companyName.equals("") && !companyName.equalsIgnoreCase("All")) {
                
                Collection ipList = this.getIpListFromCompany(companyName);
                sb.append("(");
                Iterator it = ipList.iterator();
                while(it.hasNext()) {
                    String ip = (String) it.next();
                    sb.append("b.ip = '" + ip + "' ");
                    if(it.hasNext())
                        sb.append(" OR ");
                }
                sb.append(")");
                
            }
            
        }
        sb.append(" ORDER BY b.timestamp");
        sb.append(";");
        
        ResultSet result = null;
        try {
            result = this.getConnection().executeQuery(sb.toString());
        } catch (java.sql.SQLException e) {
            
            return null;
        }
        return result;
    }
    
    public ResultSet getHttpHeaders(String id) {
        //      this.affirmConnection();
        StringBuffer sb = new StringBuffer();
        
        sb.append("SELECT * ");
        sb.append("FROM " + this.httpTable + " ");
        sb.append("WHERE id = '" + id + "';");
        ResultSet result = null;
        try {
            result = this.getConnection().executeQuery(sb.toString());
        } catch (java.sql.SQLException e) {
            return null;
        }
        return result;
        
    }
    
    
    public String getHttpBody(String id) {
        //   this.affirmConnection();
        StringBuffer sb = new StringBuffer();
        
        sb.append("SELECT * ");
        sb.append("FROM " + this.bodyTable + " ");
        sb.append("WHERE id = '" + id + "';");
        ResultSet result = null;
        String body = null;
        
        try {
            result = this.getConnection().executeQuery(sb.toString());
            if (result.next() == false) return "";
            body = result.getString(this.bodyHttpBody);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return "";
        }
        return body;
    }
    
    public void closeConnection() {
        try {
            this.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Hashtable getErrors(String id) {
        //     this.affirmConnection();
        if (id == null || id.equals("")) return null;
        StringBuffer sb = new StringBuffer();
        
        sb.append("SELECT * ");
        sb.append("FROM " + this.errorTable + " ");
        sb.append("WHERE id = '" + id + "'");
        ResultSet result = null;
        Hashtable errors = new Hashtable();
        try {
            result = this.getConnection().executeQuery(sb.toString());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        try {
            while(result.next()) {
                String errorName = result.getString(this.errorName);
                String errorDescription = result.getString(this.errorDescription);
                
                errors.put(errorName, errorDescription);
                
                
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return errors;
    }
    
    public Collection getTestMessages(String id) {
        //    this.affirmConnection();
        if (id == null || id.equals("")) return null;
        StringBuffer sb = new StringBuffer();
        
        sb.append("SELECT message ");
        sb.append("FROM " + this.testTable + " ");
        sb.append("WHERE id = '" + id + "';");
        
        ResultSet result = null;
        Collection messages = new ArrayList();
        try {
            result = this.getConnection().executeQuery(sb.toString());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return messages;
        }
        try {
            while(result.next()) {
                String message = result.getString(this.testMessageCol);
                StringTokenizer token = new StringTokenizer(message,"\n");
                while (token.hasMoreTokens()) {
                    messages.add(token.nextToken());
                }
                
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return messages;
        
    }
    public Collection getTestEntries(String id) {
        //     this.affirmConnection();
        if (id == null || id.equals("")) return null;
        StringBuffer sb = new StringBuffer();
        
        sb.append("SELECT " + this.idCol + "," + this.testTestCol + "," + this.testPassCol + "," + this.testMessageCol + " ");
        sb.append("FROM " + this.testTable + " ");
        sb.append("WHERE id = '" + id + "';");
        
        ResultSet result = null;
        Collection testEntries = new ArrayList();
        try {
            result = this.getConnection().executeQuery(sb.toString());
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return testEntries;
        }
        try {
            while(result.next()) {
                int testNumber = result.getInt(this.testTestCol);
                boolean pass = result.getBoolean(this.testPassCol);
                String message = result.getString(this.testMessageCol);
                if(pass)
                    testEntries.add(testNumber + ": Success! " + message);
                else
                    testEntries.add(testNumber + ": Failure! " + message);
                
            }
            
            
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return testEntries;
    }
    
    public Collection getAllCompanyNames() {
        //      this.affirmConnection();
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT DISTINCT " + this.companyCompany + " ");
        sb.append("FROM " + this.companyTable + ";");
        
        Collection companyNames = new ArrayList();
        ResultSet result = null;
        try {
            result = this.getConnection().executeQuery(sb.toString());
            
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return companyNames;
        }
        try {
            while(result.next()) {
                companyNames.add(new String(result.getString(this.companyCompany)));
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return companyNames;
    }
    
    public Collection getAllIpAddresses() {
        //     this.affirmConnection();
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT DISTINCT " + this.baseIpCol + " ");
        sb.append("FROM " + this.baseTable + ";");
        
        Collection ipAddresses = new ArrayList();
        ResultSet result = null;
        try {
            result = this.getConnection().executeQuery(sb.toString());
            
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return ipAddresses;
        }
        try {
            while(result.next()) {
                ipAddresses.add(new String(result.getString(this.baseIpCol)));
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return ipAddresses;
    }
//    public Collection getLongMessages(String id) {
//        //     this.affirmConnection();
//        StringBuffer sb = new StringBuffer();
//        sb.append("SELECT " + this.messageName + ", " + this.messageMessage + " ");
//        sb.append("FROM " + this.messageTable + " ");
//        sb.append("WHERE " + this.idCol + " = '" + id + "';");
//        Collection longMessages = new ArrayList();
//        ResultSet result = null;
//        try {
//            result = this.getConnection().executeQuery(sb.toString());
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//            return longMessages;
//        }
//        try {
//            while(result.next()) {
//                String messageName = result.getString(this.messageName);
//                String messageMessage = result.getString(this.messageMessage);
//                longMessages.add(new VisualLog(id,messageName + ": " + messageMessage));
//            }
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//        }
//        return longMessages;
//    }
//    public String getLongMessage(String id, String name) {
//        //     this.affirmConnection();
//        StringBuffer sb = new StringBuffer();
//        sb.append("SELECT " + this.messageMessage + " ");
//        sb.append("FROM " + this.messageTable + " ");
//        sb.append("WHERE " + this.idCol + " = '" + id + "' ");
//        sb.append("AND " + this.messageName + " = '" + name + "';");
//        String message = new String();
//        ResultSet result = null;
//        
//        try {
//            result = this.getConnection().executeQuery(sb.toString());
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//            return message;
//        }
//        try {
//            result.next();
//            message = result.getString(this.messageMessage);
//        } catch (java.sql.SQLException e) {
//            
//            e.printStackTrace();
//        }
//        return message;
//    }
//    
//    public Collection getAttachments(String id) {
//        //     this.affirmConnection();
//        StringBuffer sb = new StringBuffer();
//        sb.append("SELECT " + this.attachmentNumber + "," + this.attachmentContenttype + "," + this.attachmentHash + ",");
//        sb.append(this.attachmentSize + " ");
//        sb.append("FROM " + this.attachmentTable + " ");
//        sb.append("WHERE " + this.idCol + " = '" + id + "';");
//        
//        ResultSet result = null;
//        Collection attachments = new ArrayList();
//        
//        try {
//            result = this.getConnection().executeQuery(sb.toString());
//        }  catch (java.sql.SQLException e) {
//            e.printStackTrace();
//            return attachments;
//        }
//        try {
//            while(result.next()) {
//                int number = result.getInt(this.attachmentNumber);
//                String contenttype = result.getString(this.attachmentContenttype);
//                String hash = result.getString(this.attachmentHash);
//                int size = result.getInt(this.attachmentSize);
//                String description = (number + 1) + " " + contenttype + " Hash: " + hash + " Size (in bytes): " + size;
//                attachments.add(new VisualLog(id,description));
//            }
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//        }
//        return attachments;
//        
//        
//        
//    }
    
    
    public boolean removeById(String id) {
        String beginning = "DELETE FROM ";
        String end = " WHERE " + this.idCol + " = '" + id + "';";
        
        StringBuffer baseDelete = new StringBuffer(beginning);
        baseDelete.append(this.baseTable);
        baseDelete.append(end);
        
        StringBuffer bodyDelete = new StringBuffer(beginning);
        bodyDelete.append(this.bodyTable);
        bodyDelete.append(end);
        
        StringBuffer errorDelete = new StringBuffer(beginning);
        errorDelete.append(this.errorTable);
        errorDelete.append(end);
        
        StringBuffer soapDelete = new StringBuffer(beginning);
        soapDelete.append(this.soapTable);
        soapDelete.append(end);
        
        StringBuffer testDelete = new StringBuffer(beginning);
        testDelete.append(this.testTable);
        testDelete.append(end);
        
        StringBuffer httpDelete = new StringBuffer(beginning);
        httpDelete.append(this.httpTable);
        httpDelete.append(end);
        
        StringBuffer messageDelete = new StringBuffer(beginning);
        messageDelete.append(this.messageTable);
        messageDelete.append(end);
        
        StringBuffer attachmentDelete = new StringBuffer(beginning);
        attachmentDelete.append(this.attachmentTable);
        attachmentDelete.append(end);
        try {
            this.getConnection().executeUpdate(baseDelete.toString());
            this.getConnection().executeUpdate(bodyDelete.toString());
            this.getConnection().executeUpdate(errorDelete.toString());
            this.getConnection().executeUpdate(soapDelete.toString());
            this.getConnection().executeUpdate(testDelete.toString());
            this.getConnection().executeUpdate(httpDelete.toString());
            this.getConnection().executeUpdate(messageDelete.toString());
            this.getConnection().executeUpdate(attachmentDelete.toString());
        } catch (java.sql.SQLException e) {
            return false;
        }
        return true;
        
    }
    
    /**
     * Getter for property this.getConnection().
     * @return Value of property this.getConnection().
     */
    public gov.nist.registry.ws.log.JdbcConnection getConnection() {
        //        this.affirmConnection();
        return connection;
    }
    
    /**
     * Setter for property this.getConnection().
     * @param this.getConnection() New value of property this.getConnection().
     */
    public void setConnection(gov.nist.registry.ws.log.JdbcConnection connection) {
        this.connection = connection;
    }
    
    static public void setHostname(String hostname) {
        DatabaseFacade.hostname = hostname;
        
    }
    static public String getHostname() {
        return DatabaseFacade.hostname;
    }
    
    
}

