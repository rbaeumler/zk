package org.zkoss.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation to identify the name of a parameter of a method.
 * 
 * @see {@link Command}
 * @see {@link DefaultValue}
 * @author dennis
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
	/**
	 * name of the parameter
	 * @return name of the parameter
	 */
	String value();
	
//	String defaultValue() default "A-SPECIAL-STRING-FOR-NULL";
}