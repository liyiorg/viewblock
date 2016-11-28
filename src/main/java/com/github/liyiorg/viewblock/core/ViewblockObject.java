package com.github.liyiorg.viewblock.core;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.liyiorg.viewblock.annotation.BModelAttribute;
import com.github.liyiorg.viewblock.annotation.BRequestParam;
import com.github.liyiorg.viewblock.annotation.ValueConstants;
import com.github.liyiorg.viewblock.exception.ViewBlockRequiredParameter;
import com.github.liyiorg.viewblock.paramconvert.BModelMapConvert;
import com.github.liyiorg.viewblock.paramconvert.Convert;
import com.github.liyiorg.viewblock.paramconvert.HttpServletRequestConvert;
import com.github.liyiorg.viewblock.paramconvert.HttpServletResponseConvert;
import com.github.liyiorg.viewblock.paramconvert.HttpSessionConvert;
import com.github.liyiorg.viewblock.paramconvert.InputStreamConvert;
import com.github.liyiorg.viewblock.paramconvert.OtherRequestConvert;
import com.github.liyiorg.viewblock.paramconvert.OutputStreamConvert;
import com.github.liyiorg.viewblock.paramconvert.ReaderConvert;
import com.github.liyiorg.viewblock.paramconvert.ServletRequestConvert;
import com.github.liyiorg.viewblock.paramconvert.ServletResponseConvert;
import com.github.liyiorg.viewblock.paramconvert.WriterConvert;
import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.Paranamer;

public class ViewblockObject {

	private static Logger logger = LoggerFactory.getLogger(ViewblockObject.class);

	private String name;

	private Object object;

	private Method method;

	private Map<String, MethodParam> methodParamMap;

	private List<MethodParam> methodParamList;

	private boolean isvoid;

	private String template;

	private Integer modelMapParamIndex;

	private String className;

	private static Map<Class<?>, Convert> CONVERT_MAP = new HashMap<Class<?>, Convert>();

	static {
		CONVERT_MAP.put(ServletRequest.class, new ServletRequestConvert());
		CONVERT_MAP.put(HttpServletRequest.class, new HttpServletRequestConvert());
		CONVERT_MAP.put(ServletResponse.class, new ServletResponseConvert());
		CONVERT_MAP.put(HttpServletResponse.class, new HttpServletResponseConvert());
		CONVERT_MAP.put(HttpSession.class, new HttpSessionConvert());

		CONVERT_MAP.put(OutputStream.class, new OutputStreamConvert());
		CONVERT_MAP.put(Writer.class, new WriterConvert());
		CONVERT_MAP.put(InputStream.class, new InputStreamConvert());
		CONVERT_MAP.put(Reader.class, new ReaderConvert());

		CONVERT_MAP.put(BRequestParam.class, new OtherRequestConvert());

		CONVERT_MAP.put(BModelMap.class, new BModelMapConvert());
	}

	/**
	 * 初始化 方法参数定义
	 */
	private void initialMethod() {
		isvoid = method.getReturnType().getName().equals("void");
		if (method.getParameterTypes().length == 0) {
			return;
		}
		methodParamMap = new LinkedHashMap<String, MethodParam>();
		methodParamList = new ArrayList<MethodParam>();
		Paranamer paranamer = new BytecodeReadingParanamer();
		String[] parameterNames = paranamer.lookupParameterNames(method);
		Annotation[][] pas = method.getParameterAnnotations();
		int i = 0;
		for (Class<?> typeClass : method.getParameterTypes()) {
			MethodParam mp = new MethodParam();
			String name = parameterNames[i];

			Convert convert = CONVERT_MAP.get(typeClass);
			if (typeClass.equals(BModelMap.class)) {
				if (modelMapParamIndex != null) {
					logger.error("view block only set one BModelMap");
				}
				modelMapParamIndex = i;
			} else if (convert == null) {
				convert = CONVERT_MAP.get(BRequestParam.class);
				for (Annotation a : pas[i]) {
					if (a.annotationType().equals(BRequestParam.class)) {
						BRequestParam p = (BRequestParam) a;
						boolean required = false;
						Object defValue = null;
						// RequestParam 注解
						if (p != null) {
							if (!"".equals(p.value())) {
								name = p.value();
							}
							if (!p.defaultValue().equals(ValueConstants.DEFAULT_NONE)) {
								if (ConvertUtils.lookup(typeClass) != null) {
									defValue = ConvertUtils.convert(p.defaultValue(), typeClass);
								}
							}
							required = p.required();
						}

						// others param
						mp.setRequired(required);
						mp.setDefValue(defValue);
						break;
					} else if (a.annotationType().equals(BModelAttribute.class)) {
						mp.setModelAttribute(true);
						break;
					}
				}
			}
			mp.setName(name);
			mp.setIndex(i);
			mp.setConvert(convert);
			mp.setTypeClass(typeClass);
			methodParamMap.put(mp.getName(), mp);
			methodParamList.add(mp);
			i++;
		}
	}

	/**
	 * 方法执行
	 * 
	 * @param servletRequest
	 *            servletRequest
	 * @param servletResponse
	 *            servletResponse
	 * @param params
	 *            tag params or servlet params
	 * @return BModelAndView
	 * @throws ViewBlockRequiredParameter
	 */
	public BModelAndView invoke(ServletRequest servletRequest, ServletResponse servletResponse, List<BlockParam> params)
			throws ViewBlockRequiredParameter {
		Object[] args = null;
		// set default method param
		if (methodParamMap != null) {
			// build params
			args = new Object[methodParamMap.size()];
			int i = 0;
			try {
				for (MethodParam mp : methodParamMap.values()) {
					Object o = mp.getConvert().convert(servletRequest, servletResponse, mp);
					args[i++] = o;
				}
			} catch (ViewBlockRequiredParameter e) {
				throw new ViewBlockRequiredParameter(
						"VIEWBLOCK class: " + this.className + " name:[" + this.getName() + "] " + e.getMessage());
			}
		}
		// set tag params
		if (params != null && args != null) {
			for (BlockParam bp : params) {
				if (bp.getIndex() != null) {
					if (bp.getIndex() >= 0 && bp.getIndex() < methodParamList.size()) {
						MethodParam mp = methodParamList.get(bp.getIndex());
						/*
						 * args[bp.getIndex()] =
						 * mp.convert.convert(mp.paramClass, mp.name,
						 * bp.getValue(), mp.def, mp.required);
						 */
						args[bp.getIndex()] = mp.getConvert().convert(bp.getValue(), mp);
					} else {
						logger.error("param index error:{}", bp.getIndex());
					}
				} else if (bp.getName() != null) {
					if (methodParamMap.get(bp.getName()) != null) {
						MethodParam mp = methodParamMap.get(bp.getName());
						args[mp.getIndex()] = mp.getConvert().convert(bp.getValue(), mp);
					} else {
						logger.error("no param name:{}", bp.getName());
					}
				}
			}
		}

		try {
			Object ro = null;
			if (args != null && methodParamMap != null) {
				if (args.length == methodParamMap.size()) {
					try {
						ro = method.invoke(object, args);
					} catch (java.lang.IllegalArgumentException e) {
						logger.error("VIEWBLOCK exec error by name:{},please check param type!", name);
						e.printStackTrace();
					}
				} else {
					logger.error("VIEWBLOCK params length error!");
				}
			} else {
				ro = method.invoke(object);
			}
			if (isvoid && template != null) {
				BModelAndView modelAndView = new BModelAndView();
				modelAndView.setName(template);
				modelAndView.setModelMap(
						modelMapParamIndex == null ? new BModelMap() : (BModelMap) args[modelMapParamIndex]);
				return modelAndView;
			} else if (ro != null) {
				if (ro instanceof BModelAndView) {
					return (BModelAndView) ro;
				} else if (ro instanceof BModelMap) {
					BModelAndView modelAndView = new BModelAndView();
					modelAndView.setName(template);
					modelAndView.setModelMap((BModelMap) ro);
					return modelAndView;
				} else if (ro instanceof String) {
					BModelAndView modelAndView = new BModelAndView();
					modelAndView.setName(ro.toString());
					modelAndView.setModelMap(
							modelMapParamIndex == null ? new BModelMap() : (BModelMap) args[modelMapParamIndex]);
					return modelAndView;
				}
			}
		} catch (IllegalArgumentException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		} catch (InvocationTargetException e) {

			e.printStackTrace();
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
		initialMethod();
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

}
