package com.github.liyiorg.viewblock.core;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.liyiorg.viewblock.exception.ViewBlockException;
import com.github.liyiorg.viewblock.exception.ViewBlockRequiredParameter;
import com.github.liyiorg.viewblock.resolve.FreemarkerViewResolve;
import com.github.liyiorg.viewblock.resolve.JspViewResolve;
import com.github.liyiorg.viewblock.resolve.ViewResolve;

public class ViewblockExec {
	
	private static Logger logger = LoggerFactory.getLogger(ViewblockExec.class);
	
	private ServletRequest request;
	private ServletResponse response;
	private static final String ASYNC_BLOCK_COMPLETE_FLAG = "ASYNC_BLOCK_COMPLETE_FLAG";
	
	private static boolean tomcat = false;
	
	private ViewblockExec(){}

	public ViewblockExec(ServletRequest request,ServletResponse response){
		this.request = request;
		this.response = response;
	}
	
	static{
		ClassLoader classLoader = ViewblockExec.class.getClassLoader().getParent();
		try {
			Class.forName("org.apache.catalina.connector.RequestFacade", false, classLoader);
			tomcat = true;
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
		}
	}
	
	
	
	/**
	 *  Async Validate
	 */
	private boolean asyncValidate() {
		//System.out.println(request.getClass().getName());
		//jetty org.eclipse.jetty.server.Request
		//tomcat org.apache.catalina.connector.RequestFacade
		if(tomcat && !request.isAsyncSupported()){
			//Set tomcat jsp ASYNC_SUPPORTED
			request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
		}
		return request.isAsyncSupported();
	}

	public Map<String,AsyncCompleteFlag> getAsyncCompleteFlag(){
		Object obj = request.getAttribute(ASYNC_BLOCK_COMPLETE_FLAG);
		if(obj != null){
			return (Map<String,AsyncCompleteFlag>)obj;
		}
		return null;
	}
	
	public void addAsyncCompleteFlag(String key){
		Map<String, AsyncCompleteFlag> map = getAsyncCompleteFlag();
		if(map == null){
			map = new HashMap<String, AsyncCompleteFlag>();
			request.setAttribute(ASYNC_BLOCK_COMPLETE_FLAG,map);
		}
		map.put(key, new AsyncCompleteFlag());
	}
	
	/**
	 * 异步执行
	 * @param name block name
	 */
	public void asyncExec(String name){
		asyncExec(name, null);
	}
	

	/**
	 * 异步执行
	 * @param name block name
	 * @param asName 别名
	 */
	public void asyncExec(String name,String asName){
		final String key = name + (asName == null?"":String.format("[%s]",asName));
		addAsyncCompleteFlag(key);
		if(asyncValidate() || request.isAsyncStarted()){	
			if(!request.isAsyncStarted()){
				request.startAsync();
			}
			final String tname = name;
			
			request.getAsyncContext().start(new Runnable() {
				public void run() {
					String content = exec(tname,true,false);
					AsyncCompleteFlag acf = getAsyncCompleteFlag().get(key);
					synchronized(acf){
						acf.setContent(content);
						acf.setComplete(true);
						acf.notifyAll();
					}
				}
			});
		}else{
			logger.warn("Con't AsyncSupported!");
			String content = exec(name,false,false);
			AsyncCompleteFlag acf = getAsyncCompleteFlag().get(key);
			acf.setContent(content);
			acf.setComplete(true);
		}
	}
	
	
	public String content(String name){
		return exec(name,false,false);
	}
	
	public void print(String name){
		exec(name,false,true);
	}
	
	/**
	 * 
	 * @param name  block name
	 * @param async
	 * @param print
	 * @return
	 */
	public String exec(String name,boolean async,boolean print){
		ViewblockObject blockObject = ViewblockFactory.getBlock(name);
		if(blockObject!=null){
			try {
				BModelAndView mav = blockObject.invoke(request);
				String tname = mav.getName();
				ViewResolve viewResolve = null;
				if(tname!=null&&tname.endsWith(".ftl")){
					viewResolve = new FreemarkerViewResolve();
				}else if(tname!=null&&tname.endsWith(".jsp")){
					if(async){
						viewResolve = new JspViewResolve(this.request.getAsyncContext().getRequest(),this.request.getAsyncContext().getResponse(),false);
					}else{
						viewResolve = new JspViewResolve(this.request,this.response,print);
					}
				}
				String returnStr = viewResolve.resolve(mav.getModelMap(),tname);
				if(returnStr != null && print){
					response.getWriter().print(returnStr);
				}
				return returnStr;
			} catch (ViewBlockRequiredParameter e) {
				e.printStackTrace();
			} catch (Exception e){
				e.printStackTrace();
			}
		}else{
			try {  
				throw new ViewBlockException("View block ["+name+"] not exists!");
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
		return null;
	}
}
