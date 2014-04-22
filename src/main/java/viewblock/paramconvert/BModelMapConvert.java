package viewblock.paramconvert;

import javax.servlet.ServletRequest;

import viewblock.core.BModelMap;
import viewblock.exception.ViewBlockRequiredParameter;

public class BModelMapConvert extends Convert{

	@Override
	public Object convert(ServletRequest request, String parma,String def,boolean required)throws ViewBlockRequiredParameter{
		return new BModelMap();
	}
	
}
