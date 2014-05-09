package org.appops.shared.core;
/**
 * 
 */


import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;


/**
 * @author Debasish Padhy Created it on 15-Aug-2012
 * 
 * This new style of Property creation e.g.
 * 
 * Property<String> str = new Property<String>() ;
 * 
 * This allows for any value types to be treated as properties.
 * 
 * Type of a single property instance is always set only once in its lifetime i.e. the first time you set the value to a valid value.
 * 
 */
@SuppressWarnings("serial")
public class Property<T extends Serializable> implements Serializable {

	protected T value = null ;
	private String name = null ;
	private boolean dirty = false ;
	private Entity parent = null ;

	Property(){

	}

	public Property(final String nm, final T vl){
		setName(nm);
		setValue(vl);
	}

	public Property(final T vl){
		value = vl;
	}

	public String getName() {
		return name;
	}


	public Entity getParent() {
		return parent;
	}


	public T getValue() {
		return value;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(final boolean dirty) {
		this.dirty = dirty;
	}

	public void setName(final String name) {
		this.name = checkNotNull(name, "Null property names are not allowed" );
	}

	public void setParent(final Entity parent) {
		this.parent =  checkNotNull(parent, "Null parent entity for a property not allowed" );
	}

	public void setValue(final T val) {

		if (value.equals(val))
			return ;
		else
			value = val ;

		setDirty(true);
	}
}
