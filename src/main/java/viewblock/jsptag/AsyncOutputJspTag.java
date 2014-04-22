package viewblock.jsptag;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import viewblock.core.ViewblockExec;


public class AsyncOutputJspTag extends TagSupport{
	
	
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
		TimeoutAsyncListener timeoutAsyncListener = new TimeoutAsyncListener();
		pageContext.getRequest().getAsyncContext().addListener(timeoutAsyncListener);
		while(!timeoutAsyncListener.isTimeout()){
			Map<String, String> map = viewblockExec.getAsyncContentMap();
			if(map!=null){
				String content = map.get(name);
				if(content!=null){
					try {
						pageContext.getOut().print(content);
						break;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		try {
			pageContext.getOut().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	public void setName(String name) {
		this.name = name;
	}




	
}
