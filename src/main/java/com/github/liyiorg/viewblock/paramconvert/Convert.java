package com.github.liyiorg.viewblock.paramconvert;

import javax.servlet.ServletRequest;

import com.github.liyiorg.viewblock.exception.ViewBlockRequiredParameter;

public abstract class Convert {

	public abstract Object convert(ServletRequest request,String parma,String def,boolean required) throws ViewBlockRequiredParameter;
}
