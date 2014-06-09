/**
 * 
 */
package org.appops.shared.client.core;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * @author Debasish Padhy Created it on 14-Aug-2012
 * 
 */
public class Space {

	private String spaceName ;
	private Space parent ;
	public Space(final String name){
		spaceName = checkNotNull(name);
	}

	public Space(final String string , final Space prt) {
		setSpaceName(string);
		setParent(prt);
	}



	public Space getParent(){
		return parent ;
	}

	public String getSpaceName(){

		return spaceName ;

	}

	protected void setParent(final Space prt) {
		parent = checkNotNull(prt);
	}

	protected void setSpaceName(final String name){
		spaceName = checkNotNull(name);
	}

}
