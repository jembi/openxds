package gov.nist.registry.xdslog;

import gov.nist.registry.common2.logging.LoggerException;



public class Test2 {

	public static void main(String args[])
	{
		Log log = null;
	       Message m;
	       System.out.println("Test Log test");
	       try {
	           log = new Log("jdbc:postgresql://localhost/logs", "postgres", "1qaz!QAZ") ;
	       } catch (LoggerException e) {
	           System.out.println("LoggerException: new Log: " + e.getMessage());
	           e.printStackTrace();
	           return;
	       }

	       try {
	           m = log.createMessage("254.254.254.254") ;
	       } catch (LoggerException e) {
	           System.out.println("LoggerException: createMessage: " + e.getMessage());
	           e.printStackTrace();
	           return;
	       }
	       try {
	           m.setIP("100.100.100.100");
	           m.setSecure(false);
	           m.setCompany("III");
	           m.setTestMessage("Test");   // test number
	       } catch (LoggerException e) {
	           System.out.println("LoggerException: set: " + e.getMessage());
	           e.printStackTrace();
	           return;
	       }

	       try {
	           m.addParam("Error", "Non-Error", "ha ha");
	       } catch (LoggerException e) {
	           System.out.println("LoggerException: addparam: " + e.getMessage());
	           e.printStackTrace();
	           return;
	       }

	       try {
	           m.setPass(true);
	           log.writeMessage(m);
	       } catch (LoggerException e) {
	           System.out.println("LoggerException: write: " + e.getMessage());
	           e.printStackTrace();
	           return;
	       }

	       try {
	           System.out.println("closing");
	           log.close();
	           System.out.println("closed");
	           log = null;
	           m = null ;
	       } catch (LoggerException e) {
	           System.out.println("LoggerException: close: " + e.getMessage());
	           e.printStackTrace();
	           return;
	       }

	       System.gc() ; 
	       try {
	           log = new Log("jdbc:postgresql://localhost/logs", "postgres", "1qaz!QAZ"  ) ;
	           
	       
	       } catch (LoggerException e) {
	           System.out.println("LoggerException: new Log 2nd try: " + e.getMessage());
	           e.printStackTrace();
	           return;
	       }  
	       try {
	           m = log.createMessage("254.254.254.254") ;
	       } catch (LoggerException e) {
	           System.out.println("LoggerException: create message 2nd try: " + e.getMessage());
	           e.printStackTrace();
	           return;
	       }
	       try {
	           m.setIP("100.100.100.101");
	           m.setSecure(false);
	           m.setCompany("WWW");
	           m.setTestMessage("Test");   // test number

	           m.addParam("Error", "Non-Error", "ha ha");

	           m.setPass(true);
	       } catch (LoggerException e) {
	           System.out.println("LoggerException: 2nd try: " + e.getMessage());
	           e.printStackTrace();
	           return;
	       }
	      try {
	           log.writeMessage(m);
	       } catch (LoggerException e) {
	           System.out.println("LoggerException: write message 2nd try: " + e.getMessage());
	           e.printStackTrace();
	           return;
	       }
	       try {
	           System.out.println("closing");
	           log.close();
	           System.out.println("closed");
	           log = null;
	           System.out.println("Test Log test done");
	       } catch (LoggerException e) {
	           System.out.println("LoggerException: close 2nd try: " + e.getMessage());
	           e.printStackTrace();
	           return;
	       }

	}
}
