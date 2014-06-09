package org.appops.shared.client.core;

import java.io.Serializable;
import static com.google.common.base.Preconditions.checkNotNull;

public class Interface implements Serializable{

	private String name ;

	private Operation[] ops = null ;

	private Service service = null ;

	public Interface ( final String nm, final Service parent ){
		name = checkNotNull(nm, "Interface constructor - Name cannot be null") ;
		service = checkNotNull(parent, "Interface constructor - Service cannot be null") ;
	}

	public String getName(){
		return name ;
	}


	public Operation[] getOperations() throws Exception{

		if (ops == null)
			throw new Exception("Operation list not initialized") ;

		return ops ;
	}

	public Service getService(){
		return service ;
	}

	public void setName(final String interName) {
		this.name = interName;
	}

	public void setOperations(final Operation[] op){
		ops = op ;
	}

	public boolean isMarker() throws Exception{
		return getOperations().length == 0 ? true : false ;
	}
}
