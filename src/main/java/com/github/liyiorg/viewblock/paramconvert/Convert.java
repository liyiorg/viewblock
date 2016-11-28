package com.github.liyiorg.viewblock.paramconvert;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.github.liyiorg.viewblock.annotation.ValueConstants;
import com.github.liyiorg.viewblock.core.MethodParam;
import com.github.liyiorg.viewblock.exception.ViewBlockRequiredParameter;

public abstract class Convert {

	protected Object nullvalue;

	public Convert() {
	}

	protected Convert(Object nullvalue) {
		this.nullvalue = nullvalue;
	}

	public abstract Object convert(ServletRequest servletRequest, ServletResponse servletResponse, MethodParam methodParam)
			throws ViewBlockRequiredParameter;

	public abstract Object convert(Object tagValue, MethodParam methodParam) throws ViewBlockRequiredParameter;

	protected boolean isEmpty(Object value) {
		return value == null || "".equals(value);
	}

	protected boolean isEmptyDef(Object value) {
		return value == null || ValueConstants.DEFAULT_NONE.equals(value);
	}
}
