package viewblock.paramconvert;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import viewblock.exception.ViewBlockRequiredParameter;

public class HttpServletRequestConvert extends Convert{

	@Override
	public Object convert(ServletRequest request, String parma,String def,boolean required)throws ViewBlockRequiredParameter {
		return (HttpServletRequest)request;
	}
	
	
}
