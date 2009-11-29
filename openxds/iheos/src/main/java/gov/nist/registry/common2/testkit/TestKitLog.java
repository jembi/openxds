package gov.nist.registry.common2.testkit;


import java.io.File;

public class TestKitLog {
	File testLog;
	File testKit;
	
	public TestKitLog(File testLogBaseDir, File testkitBaseDir) throws Exception {
		testLog = testLogBaseDir;
		testKit = testkitBaseDir;
		
		if ( !testLog.isDirectory() )
			throw new Exception("TestLog: log directory " + testLog + " does not exist");
	}
	
	/**
	 * Return log file and as a side effect create directory structure necessary to store it.
	 * @param testPlan
	 * @return
	 * @throws Exception 
	 */
	public File getLogFile(File testPlan) throws Exception {
		String relativePath = TestSpec.getLogicalPath(testPlan.getParentFile(), testKit);
		
		File path  = new File(testLog + File.separator + relativePath + File.separator + "log.xml");
		//System.out.println("testspec is " + testPlan);
		path.getParentFile().mkdirs();
		
		return path;
	}
}
