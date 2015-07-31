package gov.nist.registry.common2.testkit;

import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.io.LinesOfFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.axiom.om.OMElement;

public class TestSpec implements Iterator<File> {

	File testkit;
	File logdir;
	String section;  // examples, tests etc
	String testNum;
	List<File> testPlans;
	List<LogFile> testPlanLogs;
	Iterator<File> testPlansIterator;
	String[] sections;
	
	public static final String[] defaultSections = new String [] { "tests", "testdata", "examples", "internal", "play",
		"selftest", "development", "testkit"};
	static final String testPlanFileName = "testplan.xml";

	public String toString() { return "[TestSpec: testkit=" + testkit + " section=" + section +
		" testnum=" + testNum +
		" subtests= " + testPlansToString() +
		"]";
	}
	
	public void addTestPlanLog(LogFile lf) {
		if (testPlanLogs == null)
			testPlanLogs = new ArrayList<LogFile>();
		testPlanLogs.add(lf);
	}
	
	public void resetLogs() {
		testPlanLogs = new ArrayList<LogFile>();
	}
	
	public List<LogFile> getTestPlanLogs() {
		return testPlanLogs;
	}
	
	public void setLogDir(File logdir) {
		System.out.println("TestSpec#setLogDir " + logdir.toString());
		this.logdir = logdir;
	}

	String testPlansToString() {
		StringBuffer buf = new StringBuffer();

		if (testPlans == null) {
			buf.append("null");
			return buf.toString();
		}

		buf.append("[");
		boolean first = true;
		for (Iterator<File> it=testPlans.iterator(); it.hasNext(); ) {
			File tp = it.next();
			if (!first)
				buf.append(", ");
			first=false;
			String parentdir = tp.getParent();
			int lastSlash = parentdir.lastIndexOf(File.separatorChar);
			if (lastSlash == -1)
				buf.append(parentdir);
			else
				buf.append(parentdir.substring(lastSlash+1));
		}
		buf.append("]");

		return buf.toString();
	}

	public TestSpec(File testkit) {
		this.testkit = testkit;
		sections = defaultSections;
		reset();
	}

	/**
	 * Remove from the path of a test, the testkit prefix. Useful for creating same
	 * directory structure in different place for output files.
	 * @param testPath
	 * @param testkit
	 * @return relative path
	 * @throws Exception
	 */
	static public String getLogicalPath(File testPath, File testkit) throws Exception {
		String testkitpath = testkit.toString();
		String testpath = testPath.toString();

		if ( ! testpath.startsWith(testkitpath))
			throw new Exception("Path does not target contents of testkit");

		String diff = testpath.substring(testkitpath.length());
		if (diff.charAt(0) == '/')
			return diff.substring(1);
		return diff;
	}


	public void reset() {
		if (testPlans != null)
			testPlansIterator = testPlans.iterator();
	}

	File simpleTestDir() throws Exception {
		File file = new File(getTestDir() + File.separator + testPlanFileName);
		if ( !file.exists())
			throw new Exception("Test plan " + file + " does not exist");
		return file;
	}

	public TestSpec(File testkit, String testNum) throws Exception {
		this.testkit = testkit;
		this.testNum = testNum;
		sections = defaultSections;
		verifyCurrentTestExists(testkit, testNum);
		testPlans = getTestPlans();
		reset();
	}

	public TestSpec(File testkit, String testNum, String[] sections) throws Exception {
		this.testkit = testkit;
		this.testNum = testNum;
		this.sections = sections;
		verifyCurrentTestExists(testkit, testNum);
		testPlans = getTestPlans();
		reset();
	}

	private void verifyCurrentTestExists(File testkit, String testNum)
			throws Exception {
		for (int i=0; i<sections.length; i++) {
			section = sections[i];
			if (exists())
				break;
		}
		if ( ! exists() )
			throw new Exception("TestSpec (testkit=" + testkit + " testNum=" + testNum + ", no " + testPlanFileName + " files found");
	}

	static public void listTestKitContents(File testkit) throws IOException {
		TestSpec ts = new TestSpec(testkit);

		for (int i=0; i<defaultSections.length; i++) {
			ts.section = defaultSections[i];
			File sectionDir = ts.getSectionDir();
			if ( !sectionDir.exists())
				continue;
			System.out.println("======================  " +  ts.section + "  ======================");

			String[] files = sectionDir.list();
			if (files == null)
				continue;
			for (int j=0; j<files.length; j++) {
				if (files[j].startsWith("."))
					continue;
				File file = new File(sectionDir + File.separator + files[j]);
				if ( !file.isDirectory()) 
					continue;
				ts.testNum = file.getName();
				if (ts.isTestDir()) {
					File readme = ts.getReadme();
					String firstline = "";
					if (readme.exists() && readme.isFile())
						firstline = firstLineOfFile(readme);
					System.out.println(ts.testNum + "\t" + firstline.trim());
				}
			}
		}
	}

	static public Map<String, String> getTestKitReadMe(File testkit) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		TestSpec ts = new TestSpec(testkit);

		for (int i=0; i<defaultSections.length; i++) {
			ts.section = defaultSections[i];
			File sectionDir = ts.getSectionDir();
			if ( !sectionDir.exists())
				continue;

			String[] files = sectionDir.list();
			if (files == null)
				continue;
			for (int j=0; j<files.length; j++) {
				if (files[j].startsWith("."))
					continue;
				File file = new File(sectionDir + File.separator + files[j]);
				if ( !file.isDirectory()) 
					continue;
				ts.testNum = file.getName();
				if (ts.isTestDir()) {
					File readme = ts.getReadme();
					String firstline = "";
					if (readme.exists() && readme.isFile())
						firstline = firstLineOfFile(readme);
					map.put(ts.testNum, firstline.trim());
				}
			}
		}
		return map;
	}

	static String firstLineOfFile(File file) throws IOException {
		LinesOfFile lof = new LinesOfFile(file);
		return lof.next();
	}

	public File getReadme() {
		return new File(getTestDir() + File.separator + "readme.txt");
	}

	public String getReadmeFirstLine() throws IOException {
		return new LinesOfFile(getReadme()).next();
	}

	File getSectionDir() {
		return new File(testkit + File.separator + section);
	}

	public File getTestDir() {
		return new File(testkit + File.separator + section + File.separatorChar + testNum);
	}

	public File getTestLogDir() {
		return new File(logdir + File.separator + section + File.separatorChar + testNum);
	}

	public boolean exists() {
		return getTestDir().isDirectory();
	}

	public List<File> getTestPlans() throws Exception {

		File index = new File(getTestDir() + File.separator + "index.idx");
		if (index.exists()) 
			return getTestPlansFromIndex(index);
		else 
			return getTestPlanFromDir(getTestDir());

	}
	
	public List<File> getTestLogs() throws Exception {
		List<File> logfiles = new ArrayList<File>();
		File index = new File(getTestDir() + File.separator + "index.idx");
		if (index.exists()) 
			return getTestLogsFromIndex(index);
		else 
			logfiles.add(new File(getTestLogDir().toString() + File.separatorChar + "log.xml"));
		
		return logfiles;
	}

	List<File> getTestPlansFromIndex(File index) throws Exception {
		List<File> plans = new ArrayList<File>();
		File testdir = getTestDir();

		for (LinesOfFile lof = new LinesOfFile(index); lof.hasNext(); ) {
			String dir = lof.next().trim();
			if (dir.length() ==0)
				continue;
			File path = new File(testdir + File.separator + dir + File.separatorChar + testPlanFileName);
			if ( ! path.exists() )
				throw new Exception("TestSpec " + toString() + " references sub-directory " + dir + 
						" which does not exist or does not contain a " + testPlanFileName + " file");
			plans.add(path);
		}
		return plans;
	}

	List<File> getTestLogsFromIndex(File index) throws Exception {
		List<File> logs = new ArrayList<File>();

		for (LinesOfFile lof = new LinesOfFile(index); lof.hasNext(); ) {
			String dir = lof.next().trim();
			if (dir.length() ==0)
				continue;
			File path = new File(logdir + File.separator + dir + File.separatorChar + "log.xml");
			if ( ! path.exists() )
				throw new Exception("TestSpec " + toString() + " references the section " + dir + 
						" - no log file exists ( file " + path.toString() + " does not exist");
			logs.add(path);
		}
		return logs;
	}

	public void selectSections(List<String> sectionNames) throws Exception {
		testPlans = getTestPlansFromSectionList(sectionNames);
		reset();
	}

	List<File> getTestPlansFromSectionList(List<String> sections) throws Exception {
		List<File> plans = new ArrayList<File>();
		File testdir = getTestDir();

		for (String sectionName : sections ) {
			String dir = sectionName;
			File path = new File(testdir + File.separator + dir + File.separatorChar + testPlanFileName);
			if ( ! path.exists() )
				throw new Exception("Test Section " + dir + 
						" has been requested but does not exist or does not contain a " + testPlanFileName + " file");
			plans.add(path);
		}
		return plans;
	}

	List<File> getTestPlanFromDir(File dir) throws Exception {
		List<File> plans = new ArrayList<File>();

		File path = new File(dir + File.separator + testPlanFileName);
		if ( ! path.exists() ) 
			throw new Exception("TestSpec " + toString() + " does not have index.idx or " + testPlanFileName + " file");
		plans.add(path);

		return plans;
	}

	public boolean isTestDir() {
		if ( new File(getTestDir() + File.separator + "index.idx").exists())
			return true;
		if ( new File(getTestDir() + File.separator + testPlanFileName).exists())
			return true;
		return false;
	}

	public void validateTestPlans() throws XdsInternalException {
		for (File tp : testPlans) {
			if ( !tp.exists())
				throw new XdsInternalException("TestPlan file " + tp + " does not exist");
		}
	}

	public boolean hasNext() {
		return testPlansIterator.hasNext();
	}

	public File next() {
		return testPlansIterator.next();
	}

	public void remove() {
	}

	public String getTestNum() {
		return testNum;
	}
}

