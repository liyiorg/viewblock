package com.github.liyiorg.viewblock.exception;

public class ViewBlockSpringBeanNotFindException extends ViewBlockException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4227388327681092715L;
	
	private String paramName;

	public ViewBlockSpringBeanNotFindException(String message) {
		super(message);
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	
	
}
