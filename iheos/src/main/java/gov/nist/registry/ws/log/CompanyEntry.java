/*
 * CompanyEntry.java
 *
 * Created on November 17, 2004, 4:28 PM
 */

package gov.nist.registry.ws.log;

/**
 *
 * @author  mccaffrey
 */
public class CompanyEntry {
    
    String ip;
    String company;
    
    DatabaseFacade db = null;
    
    /** Creates a new instance of CompanyEntry */
    public CompanyEntry() throws java.sql.SQLException {
        db = DatabaseFacade.getInstance();
    }
    
    public CompanyEntry(String ip, String company) throws java.sql.SQLException {
        db = DatabaseFacade.getInstance();
        this.setIp(ip);
        this.setCompany(company);
        
        db.addCompanyEntry(this);
    }
    
    /**
     * Getter for property ip.
     * @return Value of property ip.
     */
    public java.lang.String getIp() {
        return ip;
    }
    
    /**
     * Setter for property ip.
     * @param ip New value of property ip.
     */
    public void setIp(java.lang.String ip) {
        this.ip = ip;
    }
    
    /**
     * Getter for property company.
     * @return Value of property company.
     */
    public java.lang.String getCompany() {
        return company;
    }
    
    /**
     * Setter for property company.
     * @param company New value of property company.
     */
    public void setCompany(java.lang.String company) {
        this.company = company;
    }
    
}
