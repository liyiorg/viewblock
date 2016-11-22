package com.github.liyiorg.viewblock.paramconvert;

import javax.servlet.ServletRequest;

import com.github.liyiorg.viewblock.exception.ViewBlockRequiredParameter;

public class DoubleRequestConvert extends Convert {

	public DoubleRequestConvert() {
		super();
	}

	public DoubleRequestConvert(Object nullvalue) {
		super(nullvalue);
	}

	@Override
	public Object convert(ServletRequest request, String param, String def, boolean required)
			throws ViewBlockRequiredParameter {
		if (param != null) {
			String object = request.getParameter(param);
			if (required && (isEmpty(object) || isEmptyDef(def))) {
				throw new ViewBlockRequiredParameter("Required double parameter '" + param + "' is not present");
			}
			try {
				if (!isEmpty(object)) {
					return Double.valueOf(object);
				} else if (!isEmptyDef(def)) {
					return Double.valueOf(def);
				}
			} catch (NumberFormatException e) {
				throw new ViewBlockRequiredParameter(e.getMessage());
			}
		}
		return nullvalue;
	}

	@Override
	public Object convert(String param, Object tagValue, String def, boolean required)
			throws ViewBlockRequiredParameter {
		if (required && (isEmpty(tagValue) || isEmptyDef(def))) {
			throw new ViewBlockRequiredParameter("Required long parameter '" + param + "' is not present");
		}
		if (!isEmpty(tagValue)
				&& (tagValue instanceof String || tagValue instanceof Integer || tagValue instanceof Long)) {
			return Double.valueOf(tagValue.toString());
		} else if (!isEmpty(tagValue)) {
			return tagValue;
		} else if (!isEmptyDef(def)) {
			return Double.valueOf(def);
		}
		return nullvalue;
	}
}
