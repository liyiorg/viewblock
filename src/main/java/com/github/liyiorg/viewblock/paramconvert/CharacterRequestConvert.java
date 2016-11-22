package com.github.liyiorg.viewblock.paramconvert;

import javax.servlet.ServletRequest;

import com.github.liyiorg.viewblock.exception.ViewBlockRequiredParameter;

public class CharacterRequestConvert extends Convert {

	public CharacterRequestConvert() {
		super();
	}

	public CharacterRequestConvert(Object nullvalue) {
		super(nullvalue);
	}

	@Override
	public Object convert(ServletRequest request, String param, String def, boolean required)
			throws ViewBlockRequiredParameter {
		if (param != null) {
			String object = request.getParameter(param);
			if (required && (isEmpty(object) || isEmptyDef(def))) {
				throw new ViewBlockRequiredParameter("Required long parameter '" + param + "' is not present");
			}
			try {
				if (!isEmpty(object)) {
					return object.charAt(0);
				} else if (!isEmptyDef(def)) {
					return def.charAt(0);
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
		if (!isEmpty(tagValue) && tagValue instanceof String) {
			return tagValue.toString().charAt(0);
		} else if (!isEmpty(tagValue)) {
			return tagValue;
		} else if (!isEmptyDef(def)) {
			return def.charAt(0);
		}
		return nullvalue;
	}

}
