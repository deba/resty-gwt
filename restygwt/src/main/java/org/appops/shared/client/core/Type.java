/**
 * 
 */
package org.appops.shared.client.core;

import java.io.Serializable;

/**
 * @author Debasish Padhy Created it on 15-Aug-2012 All type instances are created from this
 */

public interface Type extends Serializable {

	public Service getService();

	public String getName();

	/**
	 * this method returns an instance of the actual Type this Type represents. Critical to overcome GWT / JavaScript
	 * limitations. how ? Makes a server call gets an instance from the server
	 * 
	 * Or will need a client side wiring for pojos available through GWT.create(class) and if not creates an instance of
	 * Entity with this type set
	 * 
	 * @return
	 */

	public String getSignature();

}
