package com.github.liyiorg.viewblock.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
* <p>Title: ViewblockCollection.java</p>
* <p>Description: </p>
* @author liuemc
* @date 2014年1月10日
* @since 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ViewblockCollection {
	/**
	 * 分组名称
	 * @return name
	 */
	String name() default "";
	
	/**
	 * spring bean name,default use by class
	 * @return spring name
	 */
	String spring() default "";
}
