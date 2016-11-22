package com.github.liyiorg.viewblock.jsptag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.github.liyiorg.viewblock.core.BlockParam;
import com.github.liyiorg.viewblock.core.ViewblockExec;

public class BlockJspTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -207131629344877739L;

	private String name;
	private boolean async;
	private String asName;
	private List<BlockParam> params;

	protected void addParam(BlockParam blockParam) {
		if (params == null) {
			params = new ArrayList<BlockParam>();
		}
		params.add(blockParam);
	}

	@Override
	public int doStartTag() throws JspException {
		return EVAL_PAGE;
	}

	@Override
	public int doEndTag() throws JspException {
		ViewblockExec exec = new ViewblockExec(pageContext.getRequest(), pageContext.getResponse());
		if (!async) {
			try {
				pageContext.getOut().flush();
			} catch (IOException e) {

				e.printStackTrace();
			}
			exec.print(name, params);
		} else {
			exec.asyncExec(name, asName, params);
		}
		return EVAL_PAGE;
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
