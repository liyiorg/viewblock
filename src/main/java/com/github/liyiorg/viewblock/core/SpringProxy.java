package com.github.liyiorg.viewblock.core;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringProxy {
	
	private static ApplicationContext applicationContext;
	
	public static void initial(ServletContext sc){
		applicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);
	}
	
	/**
	 * 获取spring bean
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public static <T> T  getBean(Class<T> clazz){
		return applicationContext.getBean(clazz);
	}
	
	/**
	 * 获取spring bean
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	public static <T> T  getBean(String name){
		return (T)applicationContext.getBean(name);
	}
	
}
