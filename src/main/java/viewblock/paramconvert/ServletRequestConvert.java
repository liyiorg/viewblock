package viewblock.paramconvert;

import javax.servlet.ServletRequest;

import viewblock.exception.ViewBlockRequiredParameter;

public class ServletRequestConvert extends Convert{

	@Override
	public Object convert(ServletRequest request, String parma,String def,boolean required) throws ViewBlockRequiredParameter{
		return request;
	}
	
	
}
