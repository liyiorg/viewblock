package com.github.liyiorg.viewblock.paramconvert;

import javax.servlet.ServletRequest;

import com.github.liyiorg.viewblock.exception.ViewBlockRequiredParameter;

public class StringRequestConvert extends Convert {

	@Override
	public Object convert(ServletRequest request, String parma, String def, boolean required)
			throws ViewBlockRequiredParameter {
		if (parma != null) {
			String object = request.getParameter(parma);
			if (required && (isEmpty(object) || isEmptyDef(def))) {
				throw new ViewBlockRequiredParameter("Required String parameter '" + parma + "' is not present");
			}
			if (object != null) {
				return object;
			} else if (!isEmptyDef(def)) {
				return def;
			}
		}
		return null;
	}

	@Override
	public Object convert(String param, Object tagValue, String def, boolean required)
			throws ViewBlockRequiredParameter {
		if (required && (isEmpty(tagValue) || isEmptyDef(def))) {
			throw new ViewBlockRequiredParameter("Required long parameter '" + param + "' is not present");
		}
		if (tagValue != null) {
			return tagValue.toString();
		} else if (!isEmptyDef(def)) {
			return def;
		}
		return null;
	}

}
