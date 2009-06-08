package gov.nist.registry.common2.exception;

public class XDSRegistryOutOfResourcesException extends Exception {
	
	public XDSRegistryOutOfResourcesException(String msg) {
		super(msg);
	}

	public XDSRegistryOutOfResourcesException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
