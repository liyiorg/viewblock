package viewblock.paramconvert;

import javax.servlet.ServletRequest;

import viewblock.exception.ViewBlockRequiredParameter;

public class DoubleRequestConvert extends Convert{

	@Override
	public Object convert(ServletRequest request,String parma,String def,boolean required)throws ViewBlockRequiredParameter{
		if(parma!=null){
			String object = request.getParameter(parma);
			if((object==null||"".equals(object))&&required){
				throw new ViewBlockRequiredParameter("Required double parameter '"+parma+"' is not present");
			}
			try {
				if(object!=null&&!"".equals(object)){
					return Double.valueOf(object);
				}else if(!"".equals(def)){
					return Double.valueOf(def);
				}
			} catch (NumberFormatException e) {
				throw new ViewBlockRequiredParameter(e.getMessage());
			}
		}
		return 0;
	}
	
}
