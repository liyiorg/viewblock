package com.github.liyiorg.viewblock.paramconvert;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.github.liyiorg.viewblock.core.BModelMap;
import com.github.liyiorg.viewblock.core.MethodParam;
import com.github.liyiorg.viewblock.exception.ViewBlockRequiredParameter;

public class BModelMapConvert extends Convert {

	@Override
	public Object convert(ServletRequest servletRequest, ServletResponse servletResponse, MethodParam methodParam)
			throws ViewBlockRequiredParameter {
		return new BModelMap();
	}

	@Override
	public Object convert(Object tagValue, MethodParam methodParam) throws ViewBlockRequiredParameter {
		return null;
	}

}
