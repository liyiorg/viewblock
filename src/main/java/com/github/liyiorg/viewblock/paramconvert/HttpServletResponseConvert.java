package com.github.liyiorg.viewblock.paramconvert;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.github.liyiorg.viewblock.core.MethodParam;
import com.github.liyiorg.viewblock.exception.ViewBlockRequiredParameter;

public class HttpServletResponseConvert extends Convert {

	@Override
	public Object convert(ServletRequest servletRequest, ServletResponse servletResponse, MethodParam methodParam)
			throws ViewBlockRequiredParameter {
		return (HttpServletResponse) servletResponse;
	}

	@Override
	public Object convert(Object tagValue, MethodParam methodParam) throws ViewBlockRequiredParameter {
		return null;
	}

}
