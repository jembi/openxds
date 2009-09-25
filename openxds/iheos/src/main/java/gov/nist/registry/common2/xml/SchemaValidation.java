/*
 * SchemaValidation.java
 */

package gov.nist.registry.common2.xml;

import gov.nist.registry.common2.MetadataTypes;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.Properties;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.StringReader;
import org.apache.axiom.om.OMElement;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;

public class SchemaValidation implements MetadataTypes {

	public static String validate(OMElement ele, int metadataType)  throws XdsInternalException {
		return validate_local(ele, metadataType);
	}

	// The only known use case for localhost validation failing is when this is called from
	// xdstest2 in which case it is trying to call home to reference the schema files.
	// What is really needed is a configuration parm that points the reference to the local filesystem
	// and include the schema files in the xdstest2tool environment.

	// port 80 does not exist for requests on-machine (on the server). only requests coming in from
	// off-machine go through the firewall where the port translation happens.

	// even though this says validate_local, it is used by all requests
	public static String validate_local(OMElement ele, int metadataType)  throws XdsInternalException {
		String msg;
		msg = SchemaValidation.run(ele.toString(), metadataType);
		return msg;
	}


	// empty string as result means no errors
	static private String run(String metadata, int metadataType) throws XdsInternalException {


		MyErrorHandler errors = null;
		DOMParser p = null;
		String localSchema =null;
		String SchemaLoc = Properties.loader().getString("XDSSchemaDir");
		//localSchema = CommonProperties.getInstance().getProperty("XDSSchemaDir");
		
		File file =new File(SchemaLoc);
		try{
			localSchema = file.getCanonicalPath();
		}catch (Exception e) {
		    throw new XdsInternalException("I/O exception occured while getting the canonical path");
		}
  		// Decode schema location
		String schemaLocation;
		switch (metadataType) {
		case METADATA_TYPE_Rb:
			schemaLocation = "urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0 " + 
			((localSchema == null) ? 
					"/schema/v3/lcm.xsd":
					localSchema + "/schema/v3/lcm.xsd");
			break;
		case METADATA_TYPE_PR:
		case METADATA_TYPE_R:
			schemaLocation = "urn:oasis:names:tc:ebxml-regrep:registry:xsd:2.1 " +
			((localSchema == null) ?
			"/schema/v2/rs.xsd" :
			localSchema + "/schema/v2/rs.xsd");
			break;
		case METADATA_TYPE_Q:
			schemaLocation =
			"urn:oasis:names:tc:ebxml-regrep:query:xsd:2.1 " +
			((localSchema == null) ?
			"/schema/v2/query.xsd " :
			localSchema + "/schema/v2/query.xsd "	) + 
			
			"urn:oasis:names:tc:ebxml-regrep:registry:xsd:2.1 " +
			((localSchema == null) ?
			"/schema/v2/rs.xsd" :
			localSchema + "/schema/v2/rs.xsd" ) ;
			
			break;
		case METADATA_TYPE_SQ:
			schemaLocation = "urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0 " + 
			((localSchema == null) ?
			"/schema/v3/query.xsd " : 
			localSchema + "/schema/v3/query.xsd "  ) +
			
			"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0 " + 
			((localSchema == null) ?
			"/schema/v3/rs.xsd" :
			localSchema + "/schema/v3/rs.xsd" );
			break;
		case METADATA_TYPE_RET:
			schemaLocation = "urn:ihe:iti:xds-b:2007 " + 
			((localSchema == null) ?
			"/schema/v3/XDS.b_DocumentRepository.xsd " :
				localSchema + "/schema/v3/XDS.b_DocumentRepository.xsd ") +
			
			"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0 " + 
			((localSchema == null) ?
			"/schema/v3/rs.xsd" :
			localSchema + "/schema/v3/rs.xsd"	);
			break;
		case AUDIT_LOG:
			schemaLocation = "noNamespaceSchemaLocation " + 
			((localSchema == null) ?
			"/schema/audit/healthcare-security-audit.xsd " :
				localSchema + "/schema/audit/healthcare-security-audit.xsd ");
			break;
		default:
			throw new XdsInternalException("SchemaValidation: invalid metadata type = " + metadataType);
		}

		schemaLocation += " http://schemas.xmlsoap.org/soap/envelope/ " + 
		((localSchema == null) ?
		"/schema/v3/soap.xsd" :
			localSchema + 	"/schema/v3/soap.xsd");

		// build parse to do schema validation
		try {
			p=new DOMParser();
		} catch (Exception e) {
			throw new XdsInternalException("DOMParser failed: " + e.getMessage());
		}
		try {        
			p.setFeature( "http://xml.org/sax/features/validation", true );
			p.setFeature("http://apache.org/xml/features/validation/schema", true);
			p.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation",
					schemaLocation);
			errors = new MyErrorHandler();
			errors.setSchemaFile(schemaLocation);
			p.setErrorHandler( errors );
		} catch (SAXException e) {
			throw new XdsInternalException("SchemaValidation: error in setting up parser property: SAXException thrown with message: " 
					+ e.getMessage());             
		}

		// run parser and collect parser and schema errors
		try {
			// translate urn:uuid: to urn_uuid_ since the colons really screw up schema stuff
			String metadata2 = metadata.replaceAll("urn:uuid:", "urn_uuid_");
			InputSource is = new InputSource(new StringReader(metadata2));
			p.parse(is);
		} catch (Exception e) {
			throw new XdsInternalException("SchemaValidation: XML parser/Schema validation error: " + 
					exception_details(e));
		}
		String errs = errors.getErrors();
//		if (errs.length() != 0) {
//		errs = errs + "\n" + metadata.substring(1,500);
//		}
		return errs;

	}
	protected static String exception_details(Exception e) {
		if (e == null)
			return "No stack trace available";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		e.printStackTrace(ps);

		return "Exception thrown: " + e.getClass().getName() + "\n" + e.getMessage() + "\n" + new String(baos.toByteArray());
	}



}