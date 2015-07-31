/**
 *  Copyright (c) 2009-2010 Misys Open Source Solutions (MOSS) and others
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

package org.openhealthtools.openxds.log;


import java.sql.Timestamp;

/**
*
* @author <a href="mailto:venkata.pragallapati@misys.com">Venkat</a>
* 
*/

public interface LogMessage {
	public void setTimeStamp ( Timestamp timestamp  );
	public void setSecure ( boolean isSecure  );
	public void setTestMessage ( String testMessage );
	public void setPass ( boolean pass );
	public void setIP ( String ip ) throws LoggerException;
	public void setCompany ( String companyName ) throws LoggerException;
	public void addParam ( String messageType , String name , String value ) throws LoggerException;
	public void addHTTPParam( String name , String value ) throws LoggerException;
	public void addSoapParam( String name , String value ) throws LoggerException;
	public void addErrorParam( String name , String value ) throws LoggerException;
	public void addOtherParam ( String name , String value ) throws LoggerException;
	public String getMessageID();
}
