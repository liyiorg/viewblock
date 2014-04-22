package viewblock.jsptag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;


public class AsyncFinishJspTag extends TagSupport{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3335640132046803017L;

	@Override
	public int doStartTag() throws JspException {
		if(pageContext.getRequest().isAsyncStarted()){
			pageContext.getRequest().getAsyncContext().complete();
		}
		return SKIP_BODY;
	}




	
}
