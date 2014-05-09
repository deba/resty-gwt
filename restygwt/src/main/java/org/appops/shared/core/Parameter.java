package org.appops.shared.core;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;

@SuppressWarnings(value="serial")
public class Parameter<T extends Serializable> implements Serializable {

	private T value = null ;

	public Parameter(final T t){
		setValue(t);
	}

	public T getValue() {
		return value ;
	}

	public void setValue(final T t){
		value = checkNotNull(t , " Null parameter value has no meaning") ; ;
	}

}
