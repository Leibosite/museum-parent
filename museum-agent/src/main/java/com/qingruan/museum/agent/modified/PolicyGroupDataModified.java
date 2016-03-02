package com.qingruan.museum.agent.modified;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.museum.dao.entity.PolicyGroup;
import com.qingruan.museum.framework.esb.redispushpop.RedisMQPushSender;
import com.qingruan.museum.msg.ruleupdate.RuleUpdateMsg;
import com.qingruan.museum.msg.ruleupdate.RuleUpdateMsg.ModifiedType;

@Service
public class PolicyGroupDataModified extends
		AbstractDataModifiedDealer<PolicyGroup> {
	@Autowired
	private RedisMQPushSender redisMQSender;
	@Override
	public Class<PolicyGroup> getObjectType() {
		return PolicyGroup.class;
	}

	@Override
	public void onDelete(Object entity, Serializable id) {
		PolicyGroup group = (PolicyGroup) entity;

		RuleUpdateMsg msg = new RuleUpdateMsg();
		msg.category = group.getGroupName();
		msg.modifiedType = ModifiedType.PolicyGroup_Delete;
		redisMQSender.sendAdminToEngine(msg);
//		publishCenter.sendMsg(msg);
	}

	@Override
	public void onInsert(Object entity, Serializable id) {
		PolicyGroup group = (PolicyGroup) entity;
		RuleUpdateMsg msg = new RuleUpdateMsg();
		msg.category = group.getGroupName();
		msg.modifiedType = ModifiedType.Reload_Rules;

		redisMQSender.sendAdminToEngine(msg);
	}
	
	@Override
	public void onUpdate(Object entity, Object[] oldStates, Object[] newStates,
			String[] propertyNames, int[] dirtyProperties) {
		Integer groupNameChange = checkSpecifiedPropertyChange(propertyNames, dirtyProperties, PolicyGroup.GROUP_NAME_MARK);
		
		if (groupNameChange == null) {
			return;
		}
		
		RuleUpdateMsg msg = new RuleUpdateMsg();
		msg.category = (String) oldStates[groupNameChange];
		msg.modifiedType = RuleUpdateMsg.ModifiedType.PolicyGroup_Delete;		
		redisMQSender.sendAdminToEngine(msg);
		
		msg.category = (String) newStates[groupNameChange];
		msg.modifiedType = RuleUpdateMsg.ModifiedType.Reload_Rules;
		redisMQSender.sendAdminToEngine(msg);
	}
}
