package gov.nist.registry.ws.wsbypass;

import gov.nist.registry.common2.exception.ExceptionUtil;
import gov.nist.registry.common2.exception.XdsInternalException;
import gov.nist.registry.common2.registry.MetadataSupport;
import gov.nist.registry.common2.xml.Util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.parsers.FactoryConfigurationError;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ServiceFinder {
	private static final Log log = LogFactory.getLog(ServiceFinder.class);
	File serviceFile = new File("/Users/bill/exp/xds/services-testng.xml");
	OMElement services;
	static QName nameQName = new QName("name");
	String methodName;
	Method meth;
	Class<?> clas;

	public ServiceFinder(String endpoint, String action) throws FactoryConfigurationError, Exception {
		services = Util.parse_xml(serviceFile);
		Class<?>[] parmTypes = { Class.forName("org.apache.axiom.om.OMElement") };
		clas = getClass(endpoint, action);
		String serviceName = getServiceName(endpoint);
		OMElement serviceEle = getServiceDef(serviceName);
		methodName = getMethodName(serviceEle, action);
		try {
		meth = clas.getMethod(methodName, parmTypes);
		} catch (NoSuchMethodException e) {
			log.error(ExceptionUtil.exception_details(e));
			throw new Exception("ServiceFinder: endpoint=" + endpoint + " action=" + action + " class=" + clas.getName() + " method=" + methodName, e);
		}
		//System.out.println("ServiceFinder: launching " + clas.getName() + "#" + meth.getName());
	}

	Class<?> getClass(String endpoint, String action) throws Exception {
		String endpointService = getServiceName(endpoint);
		OMElement serviceDefEle = getServiceDef(endpointService);
		String className = getServiceClassName(serviceDefEle);
		return Class.forName(className);
	}

	String getServiceName(String endpoint) throws Exception {
		String[] endpointParts = endpoint.split("/");
		if (endpointParts.length == 0)
			throw new Exception("Cannot extract service name from endpoint " + endpoint);
		String endpointService = endpointParts[endpointParts.length-1];
		return endpointService;
	}

	public OMElement invoke(OMElement in ) throws XdsInternalException   {
		Object[] args = { in };
		Object obj = null;
		try {
			obj = clas.newInstance();
		} catch (InstantiationException e1) {
			throw new XdsInternalException("Error creating instance of " + clas, e1);
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//System.out.println("obj is of class " + obj.getClass().getName());
		try {
			return (OMElement) meth.invoke(obj, args);
		} catch (IllegalArgumentException e) {
			throw new XdsInternalException("Error invoking " + meth + " on " + obj, e);
		} catch (IllegalAccessException e) {
			throw new XdsInternalException("Error invoking " + meth + " on " + obj, e);
		} catch (InvocationTargetException e) {
			throw new XdsInternalException("Error invoking " + meth + " on " + obj, e);
		} catch (Exception e) {
			throw new XdsInternalException("Error from invocation ", e);
		}
	}

	@SuppressWarnings("unchecked")
	OMElement getServiceDef(String serviceName) throws Exception {
		for (Iterator<OMElement> it=services.getChildElements(); it.hasNext(); ) {
			OMElement serviceEle = it.next();
			String name = serviceEle.getAttributeValue(nameQName);
			if (serviceName.equals(name))
				return serviceEle;
		}
		throw new Exception("No service defined for " + serviceName);
	}

	@SuppressWarnings("unchecked")
	String getServiceClassName(OMElement serviceEle) throws Exception {
		for (Iterator<OMElement> it=serviceEle.getChildElements(); it.hasNext(); ) {
			OMElement ele = it.next();
			if ( ! "parameter".equals(ele.getLocalName()))
				continue;
			String name = ele.getAttributeValue(nameQName);
			if ("ServiceClass".equals(name))
				return ele.getText();
		}
		throw new Exception("No ServiceClass defined for " + serviceEle.getAttributeValue(new QName("name")));
	}

	@SuppressWarnings("unchecked")
	String getMethodName(OMElement serviceEle, String action) throws Exception {
		for (Iterator<OMElement> it=serviceEle.getChildElements(); it.hasNext(); ) {
			OMElement ele = it.next();
			if ( !"operation".equals(ele.getLocalName()))
				continue;
			OMElement actionMappingEle = MetadataSupport.firstChildWithLocalName(ele, "actionMapping");
			if (actionMappingEle == null)
				continue;
			String actionMapping = actionMappingEle.getText();
			if (action.equals(actionMapping))
				return ele.getAttributeValue(nameQName);
		}
		throw new Exception("Cannot find method name for service = " + serviceEle.getAttributeValue(nameQName) + " action = " + action);
	}

}
