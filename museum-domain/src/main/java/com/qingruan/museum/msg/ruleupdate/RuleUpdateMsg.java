package com.qingruan.museum.msg.ruleupdate;

import lombok.Data;

/**
 * 规则引擎更新消息
 * 
 * @author tommy
 * 
 */
@Data
public class RuleUpdateMsg {

	public String category;

	public Long policyId;

	public String policyName;

	public ModifiedType modifiedType;

	public static enum ModifiedType {
		Rule_Add, Rule_Update, Rule_Delete, PolicyGroup_Delete, Reload_Rules
	}

}
