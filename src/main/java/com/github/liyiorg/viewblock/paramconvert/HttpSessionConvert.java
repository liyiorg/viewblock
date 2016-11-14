package com.github.liyiorg.viewblock.paramconvert;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.github.liyiorg.viewblock.exception.ViewBlockRequiredParameter;

public class HttpSessionConvert extends Convert{

	@Override
	public Object convert(ServletRequest request, String parma,String def,boolean required)throws ViewBlockRequiredParameter {
		return ((HttpServletRequest)request).getSession();
	}
	
	
}
