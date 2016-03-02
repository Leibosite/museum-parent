package com.qingruan.museum.engine.service.rule.core;

import java.util.List;

public class KnowledgeBaseDecl {
	private String identifier;

	private String globleDitto;

	private List<String> globles;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getGlobleDitto() {
		return globleDitto;
	}

	public void setGlobleDitto(String globleDitto) {
		this.globleDitto = globleDitto;
	}

	public List<String> getGlobles() {
		return globles;
	}

	public void setGlobles(List<String> globles) {
		this.globles = globles;
	}

}
