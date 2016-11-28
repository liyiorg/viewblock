package com.github.liyiorg.viewblock.paramconvert;

import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;

import com.github.liyiorg.viewblock.core.MethodParam;
import com.github.liyiorg.viewblock.exception.ViewBlockRequiredParameter;

public class OtherRequestConvert extends Convert {

	private static ConvertUtilsBean convertUtilsBean1 = new ConvertUtilsBean();
	private static ConvertUtilsBean convertUtilsBean2 = new ConvertUtilsBean();

	static {
		convertUtilsBean1.register(true, true, 0);
		convertUtilsBean2.register(false, true, 0);
	}

	@Override
	public Object convert(ServletRequest servletRequest, ServletResponse servletResponse, MethodParam methodParam)
			throws ViewBlockRequiredParameter {

		// ModelAttribute
		if (methodParam.isModelAttribute()) {
			HashMap map = new HashMap();
			Enumeration names = servletRequest.getParameterNames();
			while (names.hasMoreElements()) {
				String name = (String) names.nextElement();
				map.put(name, servletRequest.getParameterValues(name));
			}
			try {
				Object bean = methodParam.getTypeClass().newInstance();
				BeanUtils.populate(bean, map);
				return bean;
			} catch (Exception e) {
				throw new ViewBlockRequiredParameter(
						"Required int parameter '" + methodParam.getName() + "' ModelAttribute ERROR");
			}
		} else {
			String object = servletRequest.getParameter(methodParam.getName());
			if (methodParam.isRequired() && (isEmpty(object) || isEmptyDef(methodParam.getDefValue()))) {
				throw new ViewBlockRequiredParameter(
						"Required int parameter '" + methodParam.getName() + "' is not present");
			}
			String[] params = servletRequest.getParameterValues(methodParam.getName());
			try {
				if (params != null && ((params.length == 1 && !"".equals(params[0]) || params.length > 0))) {
					if (convertUtilsBean1.lookup(methodParam.getTypeClass()) != null) {
						return convertUtilsBean1.convert(params.length == 1 ? params[0] : params,
								methodParam.getTypeClass());
					}
				} else if (!isEmptyDef(methodParam.getDefValue())) {
					return methodParam.getDefValue();
				}
			} catch (NumberFormatException e) {
				throw new ViewBlockRequiredParameter(e.getMessage());
			}
			if (convertUtilsBean1.lookup(methodParam.getTypeClass()) != null) {
				return convertUtilsBean2.convert("", methodParam.getTypeClass());
			} else {
				return null;
			}
		}
	}

	@Override
	public Object convert(Object tagValue, MethodParam methodParam) throws ViewBlockRequiredParameter {
		Converter converter = ConvertUtils.lookup(methodParam.getTypeClass());
		if (converter != null) {
			if (tagValue != null) {
				return convertUtilsBean1.convert(tagValue, methodParam.getTypeClass());
			} else if (methodParam.getDefValue() != null) {
				return methodParam.getDefValue();
			}
			return convertUtilsBean2.convert("", methodParam.getTypeClass());
		}
		return tagValue;
	}

}
