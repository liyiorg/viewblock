package viewblock.resolve;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * jsp view resolve
* <p>Title: JspViewResolve.java</p>
* <p>Description: </p>
* @author liuemc
* @date 2014年1月10日
* @since 1.0
 */
public class JspViewResolve implements ViewResolve{

	private ServletRequest servletRequest;
	private ServletResponse servletResponse;
	private boolean print;
	private static String TEMPLATE_DIR;
	
	
	public JspViewResolve(ServletRequest servletRequest,ServletResponse servletResponse,boolean print){
		this.servletRequest = servletRequest;
		this.servletResponse = servletResponse;
		this.print = print;
	}
	
	public static void initial(String template){
		TEMPLATE_DIR = template;
	}
	
	public String resolve(Map<String, Object> model, String template) {
		try {
			if(model!=null){
				for(String key : model.keySet()){
					servletRequest.setAttribute(key, model.get(key));
				}
			}
			String t;
			if(template.startsWith(".")){
				t = template;
			}else{
				t = TEMPLATE_DIR+"/"+template;
			}
			if(print){
				servletRequest.getRequestDispatcher(t).include(servletRequest,servletResponse);
			}else{
				JspHttpServletResponseWraper wrapperResponse = new JspHttpServletResponseWraper((HttpServletResponse)servletResponse);
				servletRequest.getRequestDispatcher(t).include(servletRequest,wrapperResponse);
				return wrapperResponse.getContent();
			}
		} catch (ServletException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return null;
	}

	
}
