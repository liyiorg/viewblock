package viewblock.paramconvert;

import javax.servlet.ServletRequest;

import viewblock.exception.ViewBlockRequiredParameter;

public class StringRequestConvert extends Convert{

	@Override
	public Object convert(ServletRequest request, String parma,String def,boolean required)throws ViewBlockRequiredParameter{
		if(parma!=null){
			String object = request.getParameter(parma);
			if(object == null&&required){
				throw new ViewBlockRequiredParameter("Required String parameter '"+parma+"' is not present");
			}
			if(object!=null){
				return object;
			}else if(!"".equals(def)){
				return def;
			}
		}
		return null;
	}
	
}
