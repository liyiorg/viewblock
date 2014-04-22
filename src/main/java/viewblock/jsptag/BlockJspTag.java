package viewblock.jsptag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import viewblock.core.ViewblockExec;


public class BlockJspTag extends TagSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -207131629344877739L;
	
	private String name;
	private boolean async;
	private String asName;
	
	
	@Override
	public int doStartTag() throws JspException {
		ViewblockExec exec = new ViewblockExec(pageContext.getRequest(),pageContext.getResponse());
		if(!async){
			try {
				pageContext.getOut().flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			exec.print(name);
		}else{
			exec.asyncExec(name,asName);
		}
		return SKIP_BODY;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAsync(boolean async) {
		this.async = async;
	}

	public void setAsName(String asName) {
		this.asName = asName;
	}

	
	
}
