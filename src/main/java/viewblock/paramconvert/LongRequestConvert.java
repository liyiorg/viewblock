package viewblock.paramconvert;

import javax.servlet.ServletRequest;

import viewblock.exception.ViewBlockRequiredParameter;

public class LongRequestConvert extends Convert{

	@Override
	public Object convert(ServletRequest request, String parma,String def,boolean required) throws ViewBlockRequiredParameter{
		if(parma!=null){
			String object = request.getParameter(parma);
			if((object==null||"".equals(object))&&required){
				throw new ViewBlockRequiredParameter("Required long parameter '"+parma+"' is not present");
			}
			try {
				if(object!=null&&!"".equals(object)){
					return Long.valueOf(object);
				}else if(!"".equals(def)){
					return Long.valueOf(def);
				}
			} catch (NumberFormatException e) {
				throw new ViewBlockRequiredParameter(e.getMessage());
			}
		}
		return 0;
	}
	
}
