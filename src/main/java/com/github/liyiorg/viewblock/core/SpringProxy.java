package com.github.liyiorg.viewblock.core;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringProxy {

	private static ApplicationContext applicationContext;

	public static void initial(ServletContext sc) {
		applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
	}

	/**
	 * get bean by class
	 * 
	 * @param clazz
	 *            class
	 * @return bean
	 */
	public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

	/**
	 * get bean by name
	 * 
	 * @param name
	 *            name
	 * @return bean
	 */
	public static <T> T getBean(String name) {
		return (T) applicationContext.getBean(name);
	}

}
