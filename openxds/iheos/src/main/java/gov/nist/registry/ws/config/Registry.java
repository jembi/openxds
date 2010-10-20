package gov.nist.registry.ws.config;

import gov.nist.registry.common2.exception.XdsException;
import gov.nist.registry.common2.registry.ErrorLogger;
import gov.nist.registry.common2.registry.Response;
import gov.nist.registry.common2.registry.storedquery.SqParams;
import gov.nist.registry.common2.registry.storedquery.StoredQuerySupport;
import gov.nist.registry.ws.configurations.ebxmlrr21.Ebxmlrr21StoredQueryFactory;
import gov.nist.registry.ws.sq.RegistryObjectValidator;
import gov.nist.registry.ws.sq.RegistryValidations;
import gov.nist.registry.ws.sq.StoredQueryFactory;
import gov.nist.registry.ws.sq.ebxmlrr21.EbXML21QuerySupport;
import gov.nist.registry.ws.sq.ebxmlrr21.EbXML21RegistryObjectValidator;

import org.apache.axiom.om.OMElement;
import org.openhealthtools.openexchange.syslog.LogMessage;
import org.openhealthtools.openexchange.syslog.LoggerException;

/**
 * This class configures the Registry actor by pointing to concrete implementations of key
 * sub-systems.  Each getter returns a factory object specialized in some aspect of the Registry 
 * actor implementation.
 * @author bill
 *
 */
public class Registry {

	/**
	 * Get a Stored Query Factory implementation for the current implementation.
	 * @param ahqr - the Stored Query request
	 * @return StoredQueryFactory object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	public static StoredQueryFactory getStoredQueryFactory(OMElement ahqr, Response response, LogMessage log_message) throws XdsException, LoggerException{
		return new Ebxmlrr21StoredQueryFactory(ahqr, response, log_message);
	}

	/**
	 * Get a Stored Query Factory implementation for the current implementation.
	 * @params ahqr - the Stored Query request
	 * @return StoredQueryFactory object
	 * @throws XdsException
	 * @throws LoggerException
	 */
	public static StoredQueryFactory getStoredQueryFactory(SqParams params, Response response, LogMessage log_message) throws XdsException, LoggerException{
		return new Ebxmlrr21StoredQueryFactory(params, response, log_message);
	}
	
	
	public static RegistryValidations getRegistryValidations(ErrorLogger response, LogMessage logMessage) throws LoggerException {
		StoredQuerySupport sqs = new StoredQuerySupport(response, logMessage);
		return new EbXML21QuerySupport(sqs);
	}
	
	public static RegistryObjectValidator getRegistryObjectValidator(StoredQuerySupport sqs) {
		return new EbXML21RegistryObjectValidator(sqs);
	}

}
