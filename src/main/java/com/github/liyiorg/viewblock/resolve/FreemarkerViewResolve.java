package com.github.liyiorg.viewblock.resolve;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 
* <p>Title: FreemarkerViewResolve.java</p>
* <p>Description: </p>
* @author liuemc
* @date 2014年1月10日
* @since 1.0
 */
public class FreemarkerViewResolve implements ViewResolve{
	
	private static Configuration  configuration = new Configuration();
	private static String TEMPLATE_DIR = "WEB-INF/block";
	private static String ENCODE = "UTF-8";
	private static Logger logger = LoggerFactory.getLogger(FreemarkerViewResolve.class);
	
	
	
	/**
	 * 初始化
	 * @param servletContext 初始化模板路径必要参数
	 * @param dir 模板目录
	 * @param templateUpdateDelay templateUpdateDelay
	 * @param encode encode
	 */
	public static void initial(ServletContext servletContext,String dir,int templateUpdateDelay,String encode){
		String tdir = TEMPLATE_DIR;
		if(dir != null){
			tdir = dir;
		}
		logger.info("view block freemarker dir:{}",tdir);
		configuration.setServletContextForTemplateLoading(servletContext,dir);
		configuration.setObjectWrapper(new DefaultObjectWrapper());
		configuration.setEncoding(Locale.getDefault(),encode==null?ENCODE:encode);
		configuration.setTemplateUpdateDelay(templateUpdateDelay);
	}
	
	/**
	 * 模板解析
	 * @param model model
	 * @param template template
	 * @return content
	 */
	public String resolve(Map<String, Object> model,String template){
		try {
			Template t = configuration.getTemplate(template);
			if(t!=null){
				if(model==null){
					model = new HashMap<String, Object>();
				}
				StringWriter stringWriter = new StringWriter();
				t.process(model, stringWriter);
				if(logger.isDebugEnabled()){
					logger.debug(stringWriter.getBuffer().toString());
				}
				return stringWriter.getBuffer().toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
