package com.github.liyiorg.viewblock.core;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Properties;


public class ViewblockConfig {

	protected static String pack_scan;
	
	protected static Boolean spring;
	
	protected static String jsp_template;
	
	protected static Boolean freemarker;
	
	protected static String freemarker_template;

	protected static Integer freemarker_delay;
	
	protected static String freemarker_encode;
	
	
	
	protected static void margerProperties(Properties properties){
		Field[] fields = ViewblockConfig.class.getDeclaredFields();
		ViewblockConfig vc = new ViewblockConfig();
		for(Object key : properties.keySet()){
			Object value = properties.get(key);
			if(value!=null&&!"".equals(value)){
				for(Field field : fields){
					try {
						if(field.getName().equals(key.toString())){
							field.set(vc,convertField(field.getGenericType(),value));
							break;
						}
					} catch (IllegalArgumentException e) {
						
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * Convert field
	 * @param type
	 * @param value
	 * @return obj
	 */
	private static Object convertField(Type type,Object value){
		if("class java.lang.Boolean".equals(type.toString())){
			return Boolean.valueOf(value.toString());
		}else if("class java.lang.Integer".equals(type.toString())){
			return Integer.valueOf(value.toString());
		}else if("class java.lang.Long".equals(type.toString())){
			return Long.valueOf(value.toString());
		}else if("class java.lang.Double".equals(type.toString())){
			return Double.valueOf(value.toString());
		}else{
			return value.toString();
		}
	}
}

