/**
 * 
 */
package org.appops.shared.client.core;

/**
 * @author Debasish Padhy Created it on 15-Aug-2012
 *
 */

import java.io.Serializable;

/**
 * This is a common base of all typed pojos in an appops application.
 * 
 * @author Debasish - deba@ensarm.com Created on Feb 8, 2012 - 10:11:40 AM
 * 
 */
@SuppressWarnings("serial")
public interface Entity extends Serializable {

	public <T extends Serializable> T getPropertyValue(final String name);

	/**
	 * expects a value and creates a property instance wrapper internally
	 * 
	 * @param name
	 * @param t
	 */
	public <T extends Serializable> void setPropertyValue(final String name, T t);

	/**
	 * @return the partial - defaults to true
	 * 
	 *         Indicates if the entity is a partial representation of the actual persistent entity
	 * 
	 */
	public boolean isPartial();

	/**
	 * @param partial
	 *            the partial to set
	 */
	public void setPartial(final boolean partial);

	public EntityType getType();

	public void setType(EntityType t);

	public boolean isDirty();

	public void setDirty(boolean dirty);

}
