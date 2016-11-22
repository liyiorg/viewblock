package com.github.liyiorg.viewblock.paramconvert;

import javax.servlet.ServletRequest;

import com.github.liyiorg.viewblock.annotation.ValueConstants;
import com.github.liyiorg.viewblock.exception.ViewBlockRequiredParameter;

public abstract class Convert {

	protected Object nullvalue;

	public Convert() {
	}

	protected Convert(Object nullvalue) {
		this.nullvalue = nullvalue;
	}

	public abstract Object convert(ServletRequest request, String param, String def, boolean required)
			throws ViewBlockRequiredParameter;

	public abstract Object convert(String param, Object tagValue, String def, boolean required)
			throws ViewBlockRequiredParameter;

	protected boolean isEmpty(Object value) {
		return value == null || "".equals(value);
	}

	protected boolean isEmptyDef(Object value) {
		return value == null || ValueConstants.DEFAULT_NONE.equals(value);
	}
}
