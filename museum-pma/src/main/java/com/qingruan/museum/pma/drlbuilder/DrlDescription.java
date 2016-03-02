package com.qingruan.museum.pma.drlbuilder;

/**
 * DRL脚本对象
 * 
 * @author tommy
 * 
 */
public class DrlDescription {
	private String category;// 所属规则库

	private String content;// 脚本内容

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
