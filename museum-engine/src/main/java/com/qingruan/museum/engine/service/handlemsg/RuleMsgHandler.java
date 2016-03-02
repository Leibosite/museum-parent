package com.qingruan.museum.engine.service.handlemsg;

import org.springframework.stereotype.Service;

import com.qingruan.museum.engine.MuseumEngine;
import com.qingruan.museum.engine.service.rule.core.RuleCore;
import com.qingruan.museum.msg.ruleupdate.RuleUpdateMsg;
import com.qingruan.museum.msg.ruleupdate.RuleUpdateMsg.ModifiedType;

/**
 * 监听RuleMsg重新加载规则引擎
 * 
 * @author tommy
 * 
 */
@Service
public class RuleMsgHandler {

	/**
	 * 接收到规则更新通知
	 * 
	 * @param org
	 * @throws Exception
	 */
	public void onReceive(Object org) throws Exception {

		RuleCore ruleCore = MuseumEngine.applicationContextGuardian
				.getRuleCore();

		RuleUpdateMsg msg = (RuleUpdateMsg) org;
		ModifiedType modifiedType = msg.getModifiedType();
		switch (modifiedType) {
		case Rule_Add:
		case Rule_Update:
			ruleCore.updateSingleRule(msg.getCategory(), msg.getPolicyId());

			break;

		case Rule_Delete:
			ruleCore.deleteSingleRule(msg.getCategory(), msg.getPolicyName());

			break;

		case PolicyGroup_Delete:
			ruleCore.clearCategoryRules(msg.getCategory());

			break;

		case Reload_Rules:
			ruleCore.reloadCategoryRules(msg.getCategory());

			break;

		default:
			break;
		}
	}
}
