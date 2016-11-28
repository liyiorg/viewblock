package com.github.liyiorg.viewblock.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BModelAttribute {

	/**
	 * The name of the model attribute to bind to.
	 * <p>
	 * The default model attribute name is inferred from the declared attribute
	 * type (i.e. the method parameter type or method return type), based on the
	 * non-qualified class name: e.g. "orderAddress" for class
	 * "mypackage.OrderAddress", or "orderAddressList" for
	 * "List&lt;mypackage.OrderAddress&gt;".
	 */
	//String value() default "";

}