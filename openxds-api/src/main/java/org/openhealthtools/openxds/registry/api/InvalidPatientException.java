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

package org.openhealthtools.openxds.registry.api;
/**
 * This exception is generated when there is a problem,
 * when merging patient documents
 *
 * @author <a href="mailto:Rasakannu.Palaniyandi@misys.com">Raja</a>
 *
 */
public class InvalidPatientException extends Exception{
	
		
		private static final long serialVersionUID = 6245801853319110230L;

		/**
		 * Creates a new InvalidPatientException.
		 * 
		 * @param e a description of the problem
		 */
		public InvalidPatientException(Exception e) {
			super(e);
		}

		/**
		 * Creates a new InvalidPatientException.
		 * 
		 * @param msg a description of the problem
		 * @param cause the embedded Throwable
		 */
	    public InvalidPatientException(String msg, Throwable cause){
	        super(msg, cause);
	    }

	    
		/**
		 * Creates a new InvalidPatientException.
		 * 
		 * @param cause the embedded Throwable
		 */
	    public InvalidPatientException(Throwable cause) {
	        super(cause);
	    }
	    
	    /**
		 * Creates a new InvalidPatientException.
		 * 
		 * @param msg a description of the problem
		 */

		public InvalidPatientException(String msg) {
			super(msg);
		}
		
	}


