package com.github.liyiorg.viewblock.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.liyiorg.viewblock.annotation.BRequestParam;
import com.github.liyiorg.viewblock.exception.ViewBlockRequiredParameter;
import com.github.liyiorg.viewblock.paramconvert.BModelMapConvert;
import com.github.liyiorg.viewblock.paramconvert.BooleanRequestConvert;
import com.github.liyiorg.viewblock.paramconvert.CharacterRequestConvert;
import com.github.liyiorg.viewblock.paramconvert.Convert;
import com.github.liyiorg.viewblock.paramconvert.DoubleRequestConvert;
import com.github.liyiorg.viewblock.paramconvert.FloatRequestConvert;
import com.github.liyiorg.viewblock.paramconvert.HttpServletRequestConvert;
import com.github.liyiorg.viewblock.paramconvert.HttpSessionConvert;
import com.github.liyiorg.viewblock.paramconvert.IntegerRequestConvert;
import com.github.liyiorg.viewblock.paramconvert.LongRequestConvert;
import com.github.liyiorg.viewblock.paramconvert.OtherRequestConvert;
import com.github.liyiorg.viewblock.paramconvert.ServletRequestConvert;
import com.github.liyiorg.viewblock.paramconvert.StringRequestConvert;
import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.Paranamer;

public class ViewblockObject {

	private char char_def;

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

	public class MethodParam {
		String name;
		String def;
		boolean required;
		Convert convert;
		int index;

		public MethodParam(String name, String def, Convert convert) {
			super();
			this.name = name;
			this.def = def;
			this.convert = convert;
		}
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
		for (Class<?> ptype : method.getParameterTypes()) {
			MethodParam mp = null;
			String paramName = parameterNames[i];
			if (ptype.getName().equals("javax.servlet.ServletRequest")) {
				mp = new MethodParam(paramName, null, new ServletRequestConvert());
			} else if (ptype.getName().equals("javax.servlet.http.HttpServletRequest")) {
				mp = new MethodParam(paramName, null, new HttpServletRequestConvert());
			} else if (ptype.getName().equals("javax.servlet.http.HttpSession")) {
				mp = new MethodParam(paramName, null, new HttpSessionConvert());
			} else if (ptype.getName().equals("com.github.liyiorg.viewblock.core.BModelMap")) {
				if (modelMapParamIndex != null) {
					logger.error("view block only set one BModelMap");
				}
				modelMapParamIndex = i;
				mp = new MethodParam(null, null, new BModelMapConvert());
			} else {
				BRequestParam p = null;
				for (Annotation a : pas[i]) {
					if (a.annotationType().getName().equals("com.github.liyiorg.viewblock.annotation.BRequestParam")) {
						p = (BRequestParam) a;
						break;
					}
				}
				String def = null;
				boolean required = false;
				// RequestParam 注解
				if (p != null) {
					if (!"".equals(p.value())) {
						paramName = p.value();
					}
					def = p.defaultValue();
					required = p.required();
				}
				if (ptype.getName().equals("java.lang.String")) {
					mp = new MethodParam(paramName, def, new StringRequestConvert());
					mp.required = required;
				} else if (ptype.getName().equals("int")) {
					mp = new MethodParam(paramName, def, new IntegerRequestConvert(0));
					mp.required = required;
				} else if (ptype.getName().equals("java.lang.Integer")) {
					mp = new MethodParam(paramName, def, new IntegerRequestConvert());
					mp.required = required;
				} else if (ptype.getName().equals("long")) {
					mp = new MethodParam(paramName, def, new LongRequestConvert(0));
					mp.required = required;
				} else if (ptype.getName().equals("java.lang.Long")) {
					mp = new MethodParam(paramName, def, new LongRequestConvert());
					mp.required = required;
				} else if (ptype.getName().equals("double")) {
					mp = new MethodParam(paramName, def, new DoubleRequestConvert(0d));
					mp.required = required;
				} else if (ptype.getName().equals("java.lang.Double")) {
					mp = new MethodParam(paramName, def, new DoubleRequestConvert());
					mp.required = required;
				} else if (ptype.getName().equals("float")) {
					mp = new MethodParam(paramName, def, new FloatRequestConvert(0f));
					mp.required = required;
				} else if (ptype.getName().equals("java.lang.Float")) {
					mp = new MethodParam(paramName, def, new FloatRequestConvert());
					mp.required = required;
				} else if (ptype.getName().equals("boolean")) {
					mp = new MethodParam(paramName, def, new BooleanRequestConvert(false));
					mp.required = required;
				} else if (ptype.getName().equals("java.lang.Boolean")) {
					mp = new MethodParam(paramName, def, new BooleanRequestConvert());
					mp.required = required;
				} else if (ptype.getName().equals("char")) {
					mp = new MethodParam(paramName, def, new CharacterRequestConvert(char_def));
					mp.required = required;
				} else if (ptype.getName().equals("java.lang.Character")) {
					mp = new MethodParam(paramName, def, new CharacterRequestConvert());
					mp.required = required;
				} else {
					// others param
					mp = new MethodParam(paramName, null, new OtherRequestConvert());
					mp.required = required;
				}

			}
			mp.index = i;
			methodParamMap.put(mp.name, mp);
			methodParamList.add(mp);
			i++;
		}
	}

	/**
	 * 方法执行
	 * 
	 * @param servletRequest
	 *            servletRequest
	 * @param params
	 *            tag params or servlet params
	 * @return BModelAndView
	 * @throws ViewBlockRequiredParameter
	 */
	public BModelAndView invoke(ServletRequest servletRequest, List<BlockParam> params)
			throws ViewBlockRequiredParameter {
		Object[] args = null;
		// set default method param
		if (methodParamMap != null) {
			// build params
			args = new Object[methodParamMap.size()];
			int i = 0;
			try {
				for (MethodParam mp : methodParamMap.values()) {
					Object o = mp.convert.convert(servletRequest, mp.name, mp.def, mp.required);
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
						args[bp.getIndex()] = mp.convert.convert(mp.name, bp.getValue(), mp.def, mp.required);
					} else {
						logger.error("param index error:{}", bp.getIndex());
					}
				} else if (bp.getName() != null) {
					if (methodParamMap.get(bp.getName()) != null) {
						MethodParam mp = methodParamMap.get(bp.getName());
						args[mp.index] = mp.convert.convert(mp.name, bp.getValue(), mp.def, mp.required);
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
