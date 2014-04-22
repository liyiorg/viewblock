package viewblock.paramconvert;

import javax.servlet.ServletRequest;

import viewblock.exception.ViewBlockRequiredParameter;

public abstract class Convert {

	public abstract Object convert(ServletRequest request,String parma,String def,boolean required) throws ViewBlockRequiredParameter;
}
