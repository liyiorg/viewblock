package viewblock.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import viewblock.exception.ViewBlockException;
import viewblock.exception.ViewBlockRequiredParameter;
import viewblock.resolve.FreemarkerViewResolve;
import viewblock.resolve.JspViewResolve;
import viewblock.resolve.ViewResolve;

public class ViewblockExec {
	
	private ServletRequest request;
	private ServletResponse response;
	private static final String ASYNC_BLOCK_COMPLETE_FLAG = "ASYNC_BLOCK_COMPLETE_FLAG";
	private static final String ASYNC_BLOCK_COMPLETE_CONTENT = "ASYNC_BLOCK_COMPLETE_CONTENT";
	
	private ViewblockExec(){}

	public ViewblockExec(ServletRequest request,ServletResponse response){
		this.request = request;
		this.response = response;
	}
	
	
	/**
	 *  Async Validate
	 */
	private boolean asyncValidate() {
		if(!request.isAsyncSupported()){
			request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
		}
		
		return request.isAsyncSupported();
	}
	
	
	
	public Map<String,String> getAsyncContentMap(){
		Object obj = request.getAsyncContext().getRequest().getAttribute(ASYNC_BLOCK_COMPLETE_CONTENT);
		if(obj != null){
			return (Map<String,String>)obj;
		}
		return null;
	}
	
	/**
	 * 获取异步块内容
	 * @param name
	 * @return
	 */
	public String getAsyncContent(String name){
		Map<String,String> map = getAsyncContentMap();
		if(map!=null){
			return map.get(name);
		}
		return null;
	}

	
	public void addAsyncContent(String blockName,String asName,String content){
		Map<String, String> map = getAsyncContentMap();
		if(map == null){
			map = new HashMap<String, String>();
			request.getAsyncContext().getRequest().setAttribute(ASYNC_BLOCK_COMPLETE_CONTENT,map);
		}
		map.put(blockName+(asName==null?"":"["+asName+"]"), content);
	}
	
	private void removeAsyncContent(Map<String, String> map,String key){
		map.remove(key);
	}
	
	
	private Map<String,Boolean> getAsyncFlagMap(){
		Object obj = request.getAsyncContext().getRequest().getAttribute(ASYNC_BLOCK_COMPLETE_FLAG);
		if(obj != null){
			return (Map<String,Boolean>)obj;
		}
		return null;
	}
	
	private void addAsyncFlag(String key){
		Map<String, Boolean> map = getAsyncFlagMap();
		if(map == null){
			map = new HashMap<String, Boolean>();
			request.getAsyncContext().getRequest().setAttribute(ASYNC_BLOCK_COMPLETE_FLAG,map);
		}
		map.put(key, false);
	}
	
	private void removeAsyncFlag(Map<String, Boolean> map,String key){
		map.put(key, true);
	}

	/**
	 * 异步执行
	 * @param name block name
	 */
	public void asyncExec(String name){
		asyncExec(name,null);
	}
	
	/**
	 * 异步执行
	 * @param name   block name
	 * @param asName index name
	 */
	public void asyncExec(String name,String asName){
		if(asyncValidate()){
			if(!request.isAsyncStarted()){
				request.startAsync();
			}
			request.getAsyncContext().setTimeout(request.getAsyncContext().getTimeout()+1000);
			final String key = UUID.randomUUID().toString();
			addAsyncFlag(key);
			final String tname = name;
			final String tasName = asName;
			request.getAsyncContext().start(new Runnable() {
				public void run() {
					String text = exec(tname,tasName,true,false);
					Map<String, Boolean> map = getAsyncFlagMap();
					removeAsyncFlag(map,key);
					addAsyncContent(tname,tasName,text);
				}
			});
		}
	}
	
	public String content(String name){
		return exec(name,null,false,false);
	}
	
	public void print(String name){
		exec(name,null,false,true);
	}
	
	/**
	 * 
	 * @param name  block name
	 * @param asName 别名
	 * @param async
	 * @param print
	 * @return
	 */
	private String exec(String name,String asName,boolean async,boolean print){
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
