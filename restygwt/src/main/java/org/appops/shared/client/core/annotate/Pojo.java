package org.appops.shared.client.core.annotate;

import java.lang.annotation.Retention;

/**
 * @author komal@ensarm.com
 * 
 *         This annotation is applied to pojos that will be then regenerated to
 *         work with entity.
 */

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Pojo {

	/***
	 * The friendlyName attribute allows to provide a friendly name for the pojo
	 * while being regenerated.
	 * 
	 * @return
	 */
	String friendly() default "";

}
