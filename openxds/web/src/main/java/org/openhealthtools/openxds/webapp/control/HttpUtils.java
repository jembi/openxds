/**
 *  Copyright (c) 2009-2011 Misys Open Source Solutions (MOSS) and others
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  Contributors:
 *    Misys Open Source Solutions - initial API and implementation
 *    -
 */

package org.openhealthtools.openxds.webapp.control;

import java.io.PrintWriter;

/**
 * Utility class to create html script. 
 * @author <a href="anilkumar.reddy@misys.com">Anil Kumar</a>
 */
public class HttpUtils {
	private PrintWriter writer;
	private StringBuffer buf;

	public HttpUtils(PrintWriter writer) {
		this.writer = writer;
		buf = new StringBuffer();
	} 
	
	public void close() {
		writer.print(buf.toString());
		writer.close();
		writer = null;
		buf = null;
	}
	
	public void flush() {
		writer.print(buf.toString());
		buf = new StringBuffer();
	}
	
	public void alert(String alert) {
		writer.print("Alert: " + alert + "<br />");
	}
	
	public void o(Object o)  {
		buf.append(o);
		buf.append("\n");
	}
	
	public void open(String s) {
		o("<" + s + ">");
	}

	public void open_w_att(String s, String attname, String attvalue) {
		o("<" + s + " " + attname + "=\"" + attvalue +  "\">");
	}

	public void clos(String s) {
		o("</" + s + ">");
	}
	
	public void tag(String s) {
		o("<" + s + "/>");
	}
	
	public void bold(String s) {
		open("b");
		o(s);
		clos("b");
	}
	
	public void head(String title)   {
		o("<html><head><title>" + title + "</title>");
		open("style");
		o(".indent0 { text-indent: 0em; font-size: 100%}");
		o(".indent1 { text-indent: 1em; font-size: 100% }");
		o(".indent2 { text-indent: 2em; font-size: 100% }");
		clos("style");
		o("</head><body>");
		flush();
	}
	
	void indent0(String content) {
		div("indent0", content);
	}
	
	void indent1(String content) {
		div("indent1", content);
	}
	
	void indent2(String content) {
		div("indent2", content);
	}
	
	void div(String classx, String content) {
		open_w_att("div", "class", classx);
		o(content);
		clos("div");
	}

	public void tail()   {
		o("</body></html>");	
	}

	public void input(String type, String name, String value, String text, String other)   {
		if (text == null || text.equals("")) {
			o("<input type=\"" + type + "\" name = \"" + name + "\" value=\"" + value + "\" " + other + "/>");
		} else {
			o("<input type=\"" + type + "\" name = \"" + name + "\" value=\"" + value + "\" " + other + ">" + text + "</input>");			
		}
	}

	void label(String value)   {
		o("<label>" + value + "</label>");
	}

	public void br()  { o("<br />"); }

	void get_form(String action)   {
		o("<form action=\"" + action + "\" " + "method=\"get\">");
	}

	public void post_form(String action, String enc_type)   {
		o("<form action=\"" + action + "\" " + "method=\"post\" " + "namespace=\"/\" " + "name=\"searchForm\" "+
				((enc_type != null) ? "enctype=\"" + enc_type + "\"" : "") +
				">");
	}

	public void end_form()  { o("</form>"); }

	public void hr()  { o("<hr />"); }

	public void h1(String value)  { o("<h1>" + value + "</h1>"); } 

	public void h2(String value)   { o("<h2>" + value + "</h2>"); }
	
	String link(String display, String target) { return("<a href=\"" + target + "\" target=\"content\">" + display + "</a>"); }

	public StringBuffer getBuf() {
		return buf;
	}

	public void setBuf(StringBuffer buf) {
		this.buf = buf;
	}

	public PrintWriter getWriter() {
		return writer;
	}

	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}

}
