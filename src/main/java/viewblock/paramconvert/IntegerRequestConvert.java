package viewblock.paramconvert;

import javax.servlet.ServletRequest;

import viewblock.exception.ViewBlockRequiredParameter;

public class IntegerRequestConvert extends Convert{

	@Override
	public Object convert(ServletRequest request, String parma,String def,boolean required)throws ViewBlockRequiredParameter {
		if(parma!=null){
			String object = request.getParameter(parma);
			if((object==null||"".equals(object))&&required){
				throw new ViewBlockRequiredParameter("Required int parameter '"+parma+"' is not present");
			}
			try {
				if(object!=null&&!"".equals(object)){
					return Integer.valueOf(object);
				}else if(!"".equals(def)){
					return Integer.valueOf(def);
				}
			} catch (NumberFormatException e) {
				throw new ViewBlockRequiredParameter(e.getMessage());
			}
		}
		return 0;
	}
	
}
