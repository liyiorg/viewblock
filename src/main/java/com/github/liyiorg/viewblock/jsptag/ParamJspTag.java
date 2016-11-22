package com.github.liyiorg.viewblock.jsptag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.github.liyiorg.viewblock.core.BlockParam;

public class ParamJspTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7331075593301674667L;

	private Integer index;

	private String name;

	private Object value;

	@Override
	public int doStartTag() throws JspException {
		if (this.getParent() instanceof BlockJspTag) {
			BlockJspTag bjt = (BlockJspTag) this.getParent();
			BlockParam bp = new BlockParam();
			bp.setIndex(index);
			bp.setName(name);
			bp.setValue(value);
			bjt.addParam(bp);
		}
		return SKIP_BODY;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
