package gov.nist.registry.common2.registry;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class Properties {
	java.util.Properties properties = null;
	private final static Log logger = LogFactory.getLog(Properties.class);
	private static Properties properties_object = null;
	
	private Properties() {  init(); }
	
	public static Properties loader() {
		if (properties_object == null) properties_object = new Properties();
		return properties_object;
	}
	
	void init() {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("openxds.properties");
		if (is == null) { logger.fatal("Cannot load openxds.properties" ); return; }
		properties = new java.util.Properties();
		try {
			properties.load(is);
		}
		catch (Exception e) {
			logger.fatal(exception_details(e));
		}
	}

	public String getString(String name) {
		return properties.getProperty(name);
	}
	
	public boolean getBoolean(String name) {
		return (properties.getProperty(name).equals("false")) ? false : true;
	}

	public int getInteger(String name, int defaultVal) {
		int ret = -1;
    	try {
    		ret = Integer.parseInt(properties.getProperty(name));        
    	}catch(Exception e) {
    		logger.warn("The " + name + " property cannot be parsed", e);
    		//Set to the default value 
    		ret = defaultVal; 
    	}
    	return ret;
	}
	
	public static String exception_details(Exception e) {
		if (e == null) 
			return "";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		e.printStackTrace(ps);

		return "Exception thrown: " + e.getClass().getName() + "\n" + e.getMessage() + "\n" + new String(baos.toByteArray());
	}

}
