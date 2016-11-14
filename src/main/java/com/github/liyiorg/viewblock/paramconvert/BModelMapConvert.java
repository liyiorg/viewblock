package com.github.liyiorg.viewblock.paramconvert;

import javax.servlet.ServletRequest;

import com.github.liyiorg.viewblock.core.BModelMap;
import com.github.liyiorg.viewblock.exception.ViewBlockRequiredParameter;

public class BModelMapConvert extends Convert{

	@Override
	public Object convert(ServletRequest request, String parma,String def,boolean required)throws ViewBlockRequiredParameter{
		return new BModelMap();
	}
	
}
