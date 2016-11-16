package com.github.liyiorg.viewblock.jsptag;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.liyiorg.viewblock.core.AsyncCompleteFlag;
import com.github.liyiorg.viewblock.core.ViewblockExec;


public class AsyncOutputJspTag extends TagSupport{
	
	private static Logger logger = LoggerFactory.getLogger(AsyncOutputJspTag.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3335640132046803017L;
	
	private ViewblockExec viewblockExec;
	
	private String name;

	@Override
	public int doStartTag() throws JspException {
		try {
			pageContext.getOut().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		viewblockExec = new ViewblockExec(pageContext.getRequest(),pageContext.getResponse());
		Map<String,AsyncCompleteFlag> map = viewblockExec.getAsyncCompleteFlag();
		if(map != null){
			AsyncCompleteFlag acf = map.get(name);
			if(pageContext.getRequest().isAsyncSupported() || pageContext.getRequest().isAsyncStarted()){
				synchronized (acf){
					//wait complete
					try {
						if(!acf.isComplete()){
							acf.wait();
						}
						pageContext.getOut().print(acf.getContent());
						pageContext.getOut().flush();
						acf.setOutputed(true);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}else{
				logger.warn("Con't AsyncSupported!");
				try {
					pageContext.getOut().print(acf.getContent());
					pageContext.getOut().flush();
					acf.setOutputed(true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	
		return SKIP_BODY;
	}

	public void setName(String name) {
		this.name = name;
	}




	
}
