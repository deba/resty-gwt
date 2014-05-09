package org.appops.shared.core;


/**
 * Soon the id will be ignored and deprecated
 * 
 * @author debasish
 *
 */

@SuppressWarnings("serial")
public class Service  {

	private String serviceName ;

	private Interface[] interfaces ;

	// Interface list will be auto injected through assisted inject
	public Interface[] getInterfaces() throws Exception{

		if (interfaces == null)
			throw new Exception("Interface list not initialized") ;
		else
			return interfaces ;
	}

	public Service ( final String name ){
		setServiceName(name);
		// TODO find out interfaces through injection on the client and on server
	}

	Service() {
		// Cannot be used
	}


	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(final String serviceName) {
		this.serviceName = serviceName;
	}

}
