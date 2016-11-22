package com.github.liyiorg.viewblock.paramconvert;

import javax.servlet.ServletRequest;

import com.github.liyiorg.viewblock.exception.ViewBlockRequiredParameter;

public class OtherRequestConvert extends Convert {

	@Override
	public Object convert(ServletRequest request, String param, String def, boolean required)
			throws ViewBlockRequiredParameter {
		return null;
	}

	@Override
	public Object convert(String param, Object tagValue, String def, boolean required)
			throws ViewBlockRequiredParameter {
		return tagValue;
	}

}
