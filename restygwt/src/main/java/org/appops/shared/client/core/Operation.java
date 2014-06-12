/**
 * 
 */
package org.appops.shared.client.core;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.annotations.GwtCompatible;

/**
 * @author Debasish Padhy Created it on 16-Aug-2012 IsServiceOperation objects are immutable
 */

@SuppressWarnings("serial")
@GwtCompatible
public class Operation implements Serializable {

	private static Logger logger = Logger.getLogger(Operation.class.getName());

	private HashMap<String, Serializable> parameters;

	/**
	 * Complete signature of the operation
	 */
	private String signature;
	private String name;

	private Service service;
	private Interface parent;

	/**
	 * 
	 * @param full
	 *            expects an fully qualified operation name e.g. interface.operation . Parses and separates the
	 *            interface and operation names
	 * 
	 *            Throws Exception if either is null
	 * 
	 */

	public Operation() {

	}

	public Operation(final String name, final Interface inter) {

	}

	public Operation(final String op, final String inter, final String serv) {

	}

	public String checkValidInterfaceName(final String in) {

		checkNotNull(in);

		if (in.isEmpty() || in.lastIndexOf(' ') != -1 || in.lastIndexOf('.') != -1)
			throw new IllegalArgumentException("Not a valid interface name");
		else return in;

	}

	public String checkValidOperationName(final String op) {

		checkNotNull(op);

		if (op.isEmpty() || op.lastIndexOf(' ') != -1 || op.lastIndexOf('.') != -1)
			throw new IllegalArgumentException("Not a valid operation name");

		else return op;

	}

	/*@SuppressWarnings("unchecked")
	@Override
	public Operation clone() {
		final Operation clone = new Operation(this.getName(), this.parent);
		try {

			if (parameters != null) {
				final HashMap<String, Serializable> map = (HashMap<String, Serializable>) parameters.clone();
				if (map != null)
					clone.setParameters(map);
			}
		}
		catch (final Exception e) {
			logger.log(Level.WARNING, "Exception in cloning Operation - " + clone.getInterfaceName() + clone.getName());
			;
		}
		return clone;
	}*/

	public String getFullyQualifiedName() {
		return getInterfaceName() + "." + getName();
	}

	public String getInterfaceName() {
		return signature;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public HashMap<String, Serializable> getParameters() {
		return parameters;
	}

	public Interface getParentInterface() {
		return parent;
	}

	public Service getService() {
		return service;

	}

	public String getSignature() {
		return signature;
	}

	public void setInterface(final String in) {
		signature = in;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setParameters(final HashMap<String, Serializable> parameters) {
		this.parameters = parameters;
	}

}
