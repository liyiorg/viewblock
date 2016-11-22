package com.github.liyiorg.viewblock.core;

/**
 * 异步完成标识
 * 
 * @author SLYH
 *
 */
public class AsyncCompleteFlag {

	private boolean complete; // 已获取内容

	private String content; // 内容

	private boolean outputed; // 已输出

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isOutputed() {
		return outputed;
	}

	public void setOutputed(boolean outputed) {
		this.outputed = outputed;
	}

}
