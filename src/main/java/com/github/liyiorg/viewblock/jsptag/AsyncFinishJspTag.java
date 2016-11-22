package com.github.liyiorg.viewblock.jsptag;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.github.liyiorg.viewblock.core.AsyncCompleteFlag;
import com.github.liyiorg.viewblock.core.ViewblockExec;

public class AsyncFinishJspTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3335640132046803017L;

	@Override
	public int doStartTag() throws JspException {
		if (pageContext.getRequest().isAsyncStarted()) {
			ViewblockExec ve = new ViewblockExec(pageContext.getRequest(), pageContext.getResponse());
			Map<String,AsyncCompleteFlag> map = ve.getAsyncCompleteFlag();
			if(map != null){
				for(AsyncCompleteFlag acf : map.values()){
					synchronized (acf) {
						if(!acf.isComplete()){
							try {
								acf.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			pageContext.getRequest().getAsyncContext().complete();
		}
		return SKIP_BODY;
	}

}
