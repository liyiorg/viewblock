package com.github.liyiorg.viewblock.paramconvert;

import javax.servlet.ServletRequest;

import com.github.liyiorg.viewblock.exception.ViewBlockRequiredParameter;

public class FloatRequestConvert extends Convert{

	@Override
	public Object convert(ServletRequest request, String parma,String def,boolean required) throws ViewBlockRequiredParameter{
		if(parma!=null){
			String object = request.getParameter(parma);
			if((object==null||"".equals(object))&&required){
				throw new ViewBlockRequiredParameter("Required float parameter '"+parma+"' is not present");
			}
			try {
				if(object!=null&&!"".equals(object)){
					return Float.valueOf(object);
				}else if(!"".equals(def)){
					return Float.valueOf(def);
				}
			} catch (NumberFormatException e) {
				throw new ViewBlockRequiredParameter(e.getMessage());
			}
		}
		return 0f;
	}
	
}
