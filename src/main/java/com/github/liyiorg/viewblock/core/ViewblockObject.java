package com.github.liyiorg.viewblock.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.liyiorg.viewblock.annotation.BRequestParam;
import com.github.liyiorg.viewblock.exception.ViewBlockRequiredParameter;
import com.github.liyiorg.viewblock.paramconvert.BModelMapConvert;
import com.github.liyiorg.viewblock.paramconvert.Convert;
import com.github.liyiorg.viewblock.paramconvert.DoubleRequestConvert;
import com.github.liyiorg.viewblock.paramconvert.FloatRequestConvert;
import com.github.liyiorg.viewblock.paramconvert.HttpServletRequestConvert;
import com.github.liyiorg.viewblock.paramconvert.HttpSessionConvert;
import com.github.liyiorg.viewblock.paramconvert.IntegerRequestConvert;
import com.github.liyiorg.viewblock.paramconvert.LongRequestConvert;
import com.github.liyiorg.viewblock.paramconvert.NullRequestConvert;
import com.github.liyiorg.viewblock.paramconvert.ServletRequestConvert;
import com.github.liyiorg.viewblock.paramconvert.StringRequestConvert;
import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.Paranamer;

public class ViewblockObject {
	
	private static Logger logger = LoggerFactory.getLogger(ViewblockObject.class);
	
	private String name;
	
	private Object object;
	
	private Method method;
	
	private MethodParam[] methodParams;
	
	private boolean isvoid;
	
	private String template;
	
	private Integer modelMapParamIndex;
	
	private String className;
	
	public class MethodParam {
		String name;
		String def;
		boolean required;
		Convert convert;

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
	private void initialMethod(){
		isvoid = method.getReturnType().getName().equals("void");
		methodParams = new MethodParam[method.getParameterTypes().length];
		Paranamer paranamer = new BytecodeReadingParanamer();
		String[] parameterNames = paranamer.lookupParameterNames(method);
		int i = 0;
		Annotation[][] pas = method.getParameterAnnotations();
		for(Class<?> ptype : method.getParameterTypes()){
			if(ptype.getName().equals("javax.servlet.ServletRequest")){
				methodParams[i] = new MethodParam(parameterNames[i],null,new ServletRequestConvert());
			}else if(ptype.getName().equals("javax.servlet.http.HttpServletRequest")){
				methodParams[i] = new MethodParam(parameterNames[i],null,new HttpServletRequestConvert());
			}else if(ptype.getName().equals("javax.servlet.http.HttpSession")){
				methodParams[i] = new MethodParam(parameterNames[i],null,new HttpSessionConvert());
			}else if(ptype.getName().equals("viewblock.core.BModelMap")){
				if(modelMapParamIndex != null){
					logger.error("view block only set one BModelMap");
				}
				modelMapParamIndex = i;
				methodParams[i] = new MethodParam(null,null,new BModelMapConvert());
			}else{
				BRequestParam p = null;
				for(Annotation a : pas[i]){
					if(a.annotationType().getName().equals("viewblock.annotation.BRequestParam")){
						p = (BRequestParam)a;
						break;
					}
				}
				//RequestParam 注解
				if(p!=null){
					String param;
					if(!"".equals(p.value())){
						param = p.value();
					}else{
						param = parameterNames[i];
					}
					String def = p.defaultValue();
					if(ptype.getName().equals("java.lang.String")){
						methodParams[i] = new MethodParam(param,def,new StringRequestConvert());
						methodParams[i].required = p.required();
					}else if(ptype.getName().equals("java.lang.Integer")||ptype.getName().equals("int")){
						methodParams[i] = new MethodParam(param,def,new IntegerRequestConvert());
						methodParams[i].required = p.required();
					}else if(ptype.getName().equals("java.lang.Long")||ptype.getName().equals("long")){
						methodParams[i] = new MethodParam(param,def,new LongRequestConvert());
						methodParams[i].required = p.required();
					}else if(ptype.getName().equals("java.lang.Double")||ptype.getName().equals("double")){
						methodParams[i] = new MethodParam(param,def,new DoubleRequestConvert());
						methodParams[i].required = p.required();
					}else if(ptype.getName().equals("java.lang.Float")||ptype.getName().equals("float")){
						methodParams[i] = new MethodParam(param,def,new FloatRequestConvert());
						methodParams[i].required = p.required();
					}
				}else{
					if(ptype.getName().equals("java.lang.String")){
						methodParams[i] = new MethodParam(null,null,new StringRequestConvert());
					}else if(ptype.getName().equals("java.lang.Integer")||ptype.getName().equals("int")){
						methodParams[i] = new MethodParam(null,null,new IntegerRequestConvert());
					}else if(ptype.getName().equals("java.lang.Long")||ptype.getName().equals("int")){
						methodParams[i] = new MethodParam(null,null,new LongRequestConvert());
					}else if(ptype.getName().equals("java.lang.Double")||ptype.getName().equals("double")){
						methodParams[i] = new MethodParam(null,null,new DoubleRequestConvert());
					}else if(ptype.getName().equals("java.lang.Float")||ptype.getName().equals("float")){
						methodParams[i] = new MethodParam(null,null,new FloatRequestConvert());
					}else{
						methodParams[i] = new MethodParam(null,null,new NullRequestConvert());
					}
				}
			}
			i++;
		}
	}
	
	
	/**
	 * 方法执行
	 * @param servletRequest
	 * @return
	 * @throws ViewBlockRequiredParameter 
	 */
	public BModelAndView invoke(ServletRequest servletRequest) throws ViewBlockRequiredParameter{
		Object[] args = null;
		if(methodParams!=null){
			args = new Object[methodParams.length];
			int i=0;
			try {
				for(MethodParam mp : methodParams){
					Object o = mp.convert.convert(servletRequest,mp.name,mp.def,mp.required);
					args[i++] = o;
				}
			} catch (ViewBlockRequiredParameter e) {
				throw new ViewBlockRequiredParameter("VIEWBLOCK class: " + this.className + " name:["+this.getName()+"] " + e.getMessage());
			}
		}
		try {
			Object ro = null; 
			if(args!=null){
				ro = method.invoke(object, args);
			}else{
				ro = method.invoke(object);
			}
			if(isvoid&&template!=null){
				BModelAndView modelAndView = new BModelAndView();
				modelAndView.setName(template);
				modelAndView.setModelMap(modelMapParamIndex==null?new BModelMap():(BModelMap)args[modelMapParamIndex]);
				return modelAndView;
			}else if(ro!=null){
				if(ro instanceof BModelAndView){
					return (BModelAndView)ro;
				}else if(ro instanceof BModelMap){
					BModelAndView modelAndView = new BModelAndView();
					modelAndView.setName(template);
					modelAndView.setModelMap((BModelMap)ro);
					return modelAndView;
				}else if(ro instanceof String){
					BModelAndView modelAndView = new BModelAndView();
					modelAndView.setName(ro.toString());
					modelAndView.setModelMap(modelMapParamIndex==null?new BModelMap():(BModelMap)args[modelMapParamIndex]);
					return modelAndView;
				}
			}
		}catch (IllegalArgumentException e) {
			
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
