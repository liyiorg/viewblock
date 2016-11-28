package com.github.liyiorg.viewblock.paramconvert;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.github.liyiorg.viewblock.core.MethodParam;
import com.github.liyiorg.viewblock.exception.ViewBlockRequiredParameter;

public class HttpSessionConvert extends Convert {

	@Override
	public Object convert(ServletRequest servletRequest, ServletResponse servletResponse, MethodParam methodParam)
			throws ViewBlockRequiredParameter {
		return ((HttpServletRequest) servletRequest).getSession();
	}

	@Override
	public Object convert(Object tagValue, MethodParam methodParam) throws ViewBlockRequiredParameter {
		return null;
	}

}
