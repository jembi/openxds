package gov.nist.registry.common2.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ExceptionUtil {

	static public String exception_details(Exception e, String message) {
		if (e == null)
			return "No stack trace available";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		e.printStackTrace(ps);

		String emessage = e.getMessage();
		if (emessage == null)
			emessage = "No Message";

		return "Exception thrown: " + e.getClass().getName() + "\n" + 
		((message != null) ? message + "\n" : "") +
		emessage.replaceAll("<", "&lt;") + "\n" + new String(baos.toByteArray());
	}

	static public String exception_details(Exception e) {
		return exception_details(e, null);
	}

	static public String exception_details(Exception e, int numLines) {
		return firstNLines(exception_details(e), numLines);
	}
	
	static public String exception_local_stack(Exception e) {
		StringBuffer buf = new StringBuffer();

		String[] lines = exception_details(e).split("\n");
		for (int i=0; i<lines.length; i++) {
			String line = lines[i];
			if (line.indexOf("gov.nist") != -1)
				buf.append(line).append("\n");
		}
		
		return buf.toString();
	}

	static public String here(String message) {
		try {
			throw new Exception(message);
		} catch (Exception e) {
			return exception_details(e, message);
		}
	}

	static public String firstNLines(String string, int n) {
		int startingAt = 0;
		for (int i=0; i<n; i++) {
			if (startingAt != -1)
				startingAt = string.indexOf('\n', startingAt + 1) + 1;
		}
		if (startingAt == -1) return string;
		return string.substring(0, startingAt);
	}

}
