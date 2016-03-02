package com.qingruan.museum.pma.models;


public class RuleModelVo {
	private WhenVo when;
	private ThenVo then;
	private String ruleName;

	// private Map<String, Object> contextVaribles;

	public WhenVo getWhen() {
		return when;
	}

	public void setWhen(WhenVo when) {
		this.when = when;
	}

	public ThenVo getThen() {
		return then;
	}

	public void setThen(ThenVo then) {
		this.then = then;
	}

	// public Map<String, Object> getContextVaribles() {
	// return contextVaribles;
	// }
	//
	// public void setContextVaribles(Map<String, Object> contextVaribles) {
	// this.contextVaribles = contextVaribles;
	// }

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

}
