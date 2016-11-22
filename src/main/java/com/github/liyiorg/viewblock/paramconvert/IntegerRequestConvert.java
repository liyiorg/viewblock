package com.github.liyiorg.viewblock.paramconvert;

import javax.servlet.ServletRequest;

import com.github.liyiorg.viewblock.exception.ViewBlockRequiredParameter;

public class IntegerRequestConvert extends Convert {

	public IntegerRequestConvert() {
		super();
	}

	public IntegerRequestConvert(Object nullvalue) {
		super(nullvalue);
	}

	@Override
	public Object convert(ServletRequest request, String param, String def, boolean required)
			throws ViewBlockRequiredParameter {
		if (param != null) {
			String object = request.getParameter(param);
			if (required && (isEmpty(object) || isEmptyDef(def))) {
				throw new ViewBlockRequiredParameter("Required int parameter '" + param + "' is not present");
			}
			try {
				if (!isEmpty(object)) {
					return Integer.valueOf(object);
				} else if (!isEmptyDef(def)) {
					return Integer.valueOf(def);
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
		if (!isEmpty(tagValue) && (tagValue instanceof String || tagValue instanceof Long)) {
			return Integer.valueOf(tagValue.toString());
		} else if (!isEmpty(tagValue)) {
			return tagValue;
		} else if (!isEmptyDef(def)) {
			return Integer.valueOf(def);
		}
		return nullvalue;
	}
}
