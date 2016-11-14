package com.github.liyiorg.viewblock.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.liyiorg.viewblock.exception.ViewBlockSpringBeanNotFindException;
import com.github.liyiorg.viewblock.resolve.FreemarkerViewResolve;
import com.github.liyiorg.viewblock.resolve.JspViewResolve;

public class ViewblockFilter implements Filter{

	private static final Logger logger = LoggerFactory.getLogger(ViewblockFilter.class);
	
	private static final String DEFAULT_CONFIG_FILE = "viewblock_default.properties";	//默认配置
	
	private static final String CONFIG_FILE = "viewblock.properties";	//配置属性文件
	
	public void destroy() {
		// TODO Auto-generated method stub
	}
	

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		filterChain.doFilter(request, response);
	}

	public void init(FilterConfig config) throws ServletException {
		//Load base config
		Properties properties = loadViewblockConfig(config);
		ViewblockConfig.margerProperties(properties);
		
		
		if(ViewblockConfig.jsp_template!=null){
			JspViewResolve.initial(ViewblockConfig.jsp_template);
		}

		if(ViewblockConfig.freemarker){
			
			FreemarkerViewResolve.initial(
					config.getServletContext(),
					ViewblockConfig.freemarker_template,
					ViewblockConfig.freemarker_delay,
					ViewblockConfig.freemarker_encode);
		}
		
		if(ViewblockConfig.spring){
			ViewblockFactory.setUseSpring(true);
			SpringProxy.initial(config.getServletContext());
		}
		
		if(ViewblockConfig.pack_scan != null){
			try {
				ViewblockFactory.scanBlock(ViewblockConfig.pack_scan);
			} catch (ViewBlockSpringBeanNotFindException e) {
				
				e.printStackTrace();
			}
		}else{
			logger.error("not set block view pack");
		}
	}
	
	/**
	 * Load config
	 * @param config
	 * @return
	 */
	private Properties loadViewblockConfig(FilterConfig config){
		Properties properties = new Properties();
		try {
			InputStream baseConfigInputStream = this.getClass().getClassLoader().getResourceAsStream(DEFAULT_CONFIG_FILE);
			properties.load(baseConfigInputStream);
			baseConfigInputStream.close();
			String config_properties = config.getInitParameter("config_properties");
			if(config_properties!=null){
				logger.debug("Load viewblock config_properties:{}",config_properties);
				Reader reader = new StringReader(config_properties);
				properties.load(reader);
				reader.close();
			}else {
				String propsfilePath = new File(Thread.currentThread().getContextClassLoader().getResource("/").getFile()).getParent()+"/"+CONFIG_FILE;
				File file = new File(propsfilePath);
				if(file.exists()){
					logger.info("Load viewblock.properties on location:{}",propsfilePath);
					InputStream inputStream = new FileInputStream(file);
					properties.load(this.getClass().getClassLoader().getResourceAsStream(CONFIG_FILE));
					inputStream.close();
				}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		return properties;
	}
	

}
