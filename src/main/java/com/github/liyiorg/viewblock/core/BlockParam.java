package com.github.liyiorg.viewblock.core;

public class BlockParam {

	private Integer index;

	private String name;

	private Object value;

	public BlockParam() {
		super();
	}

	public BlockParam(Integer index, Object value) {
		super();
		this.index = index;
		this.value = value;
	}

	public BlockParam(String name, Object value) {
		super();
		this.name = name;
		this.value = value;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
