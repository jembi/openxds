package gov.nist.registry.common2.datatypes;

import java.util.Calendar;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Hl7Date {
	public String now() {
		StringBuilder sb = new StringBuilder();
		// Send all output to the Appendable object sb
		Formatter formatter = new Formatter(sb, Locale.US);
		Calendar c = new GregorianCalendar();
		formatter.format("%s%02d%02d%02d%02d%02d", 
				c.get(Calendar.YEAR), 
				c.get(Calendar.MONTH)+1, 
				c.get(Calendar.DAY_OF_MONTH),
				c.get(Calendar.HOUR_OF_DAY),
				c.get(Calendar.MINUTE),
				c.get(Calendar.SECOND));
		return sb.toString();
	}

	// useful for testing
	public String lastyear() {
		StringBuilder sb = new StringBuilder();
		// Send all output to the Appendable object sb
		Formatter formatter = new Formatter(sb, Locale.US);
		Calendar c = new GregorianCalendar();
		formatter.format("%s%02d%02d%02d%02d%02d", 
				c.get(Calendar.YEAR)-1, 
				c.get(Calendar.MONTH)+1, 
				c.get(Calendar.DAY_OF_MONTH),
				c.get(Calendar.HOUR_OF_DAY),
				c.get(Calendar.MINUTE),
				c.get(Calendar.SECOND));
		return sb.toString();
	}



}
