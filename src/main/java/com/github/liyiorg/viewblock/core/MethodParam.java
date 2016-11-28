package com.github.liyiorg.viewblock.core;

import com.github.liyiorg.viewblock.paramconvert.Convert;

public class MethodParam {

	private String name;
	private Object defValue;
	private boolean required;
	private Convert convert;
	private int index;
	private Class<?> typeClass;
	private boolean modelAttribute;

	public MethodParam() {
		super();
	}

	public MethodParam(String name, Object defValue, boolean required, Convert convert, int index, Class<?> typeClass,
			boolean modelAttribute) {
		super();
		this.name = name;
		this.defValue = defValue;
		this.required = required;
		this.convert = convert;
		this.index = index;
		this.typeClass = typeClass;
		this.modelAttribute = modelAttribute;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getDefValue() {
		return defValue;
	}

	public void setDefValue(Object defValue) {
		this.defValue = defValue;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public Convert getConvert() {
		return convert;
	}

	public void setConvert(Convert convert) {
		this.convert = convert;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Class<?> getTypeClass() {
		return typeClass;
	}

	public void setTypeClass(Class<?> typeClass) {
		this.typeClass = typeClass;
	}

	public boolean isModelAttribute() {
		return modelAttribute;
	}

	public void setModelAttribute(boolean modelAttribute) {
		this.modelAttribute = modelAttribute;
	}

}
