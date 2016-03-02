package com.qingruan.museum.agent.modified;

import java.io.Serializable;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qingruan.museum.dao.entity.PolicyGroup;
import com.qingruan.museum.dao.entity.PolicyWrapper;
import com.qingruan.museum.dao.repository.PolicyGroupDao;
import com.qingruan.museum.framework.esb.redispubsub.RedisPubSender;
import com.qingruan.museum.framework.jackson.JSONUtil;
import com.qingruan.museum.msg.ruleupdate.RuleUpdateMsg;
import com.qingruan.museum.pma.model.Policy;

@Service
@Slf4j
public class PolicyDataModified extends
		AbstractDataModifiedDealer<PolicyWrapper> {
	@Autowired
	private PolicyGroupDao policyGroupDao;
	@Autowired
	private RedisPubSender redisPubSender;

	@Override
	public Class<PolicyWrapper> getObjectType() {
		return PolicyWrapper.class;
	}

	@Override
	public void onInsert(Object entity, Serializable id) {
		PolicyWrapper wrapper = (PolicyWrapper) entity;
		Policy policy = this.toPolicy(wrapper);

		if (checkPolicyEnable(policy.getStatus()) == false) {
			return;
		}

		PolicyGroup group = policyGroupDao.findOne(wrapper.getPolicyGroupId());
		System.out.println("插入新的Policy");
		sendUpdateRuleMsg(group, wrapper.getId());
	}

	@Override
	public void onDelete(Object entity, Serializable id) {
		PolicyWrapper wrapper = (PolicyWrapper) entity;
		Policy policy = this.toPolicy(wrapper);

		if (checkPolicyEnable(policy.getStatus()) == false) {
			return;
		}

		PolicyGroup group = policyGroupDao.findOne(wrapper.getPolicyGroupId());

		if (group == null) {
			return;
		}

		sendDeleteRuleMsg(group, policy.getPolicyName());
	}

	@Override
	public void onUpdate(Object entity, Object[] oldStates, Object[] newStates,
			String[] propertyNames, int[] dirtyProperties) {
		Integer policyContentChange = checkSpecifiedPropertyChange(
				propertyNames, dirtyProperties,
				PolicyWrapper.POLICY_CONTENT_MARK);
		if (policyContentChange == null) {
			return;
		}

		PolicyWrapper policyWrapper = (PolicyWrapper) entity;

		Policy oldPolicy = this
				.toPolicy((String) oldStates[policyContentChange]);
		Policy newPolicy = this
				.toPolicy((String) newStates[policyContentChange]);

		PolicyGroup group = policyGroupDao.findOne(policyWrapper
				.getPolicyGroupId());

		if (oldPolicy.getStatus() != newPolicy.getStatus()) {
			if (newPolicy.getStatus()) {
				sendUpdateRuleMsg(group, policyWrapper.getId());
			} else {
				sendDeleteRuleMsg(group, oldPolicy.getPolicyName());
			}

			return;
		}

		if (checkPolicyEnable(newPolicy.getStatus()) == false) {
			return;
		}

		if (newPolicy.getPolicyName().equals(oldPolicy.getPolicyName()) == false) {
			sendDeleteRuleMsg(group, oldPolicy.getPolicyName());
		}

		sendUpdateRuleMsg(group, policyWrapper.getId());
	}

	/**
	 * 发送规则更新消息
	 * 
	 * @param group
	 * @param policyId
	 */
	public void sendUpdateRuleMsg(PolicyGroup group, Long policyId) {
		RuleUpdateMsg msg = new RuleUpdateMsg();
		msg.category = group.getGroupName();
		msg.policyId = policyId;
		msg.modifiedType = RuleUpdateMsg.ModifiedType.Rule_Update;
		log.info("发送规则更新消息到Engine" + msg.toString());
		redisPubSender.publishMessageByChannel("ENGINE_RULE_UPDATE", JSONUtil.serialize(msg));
	}

	/**
	 * 发送规则删除消
	 * 
	 * @param group
	 * @param policyName
	 */
	public void sendDeleteRuleMsg(PolicyGroup group, String policyName) {
		RuleUpdateMsg msg = new RuleUpdateMsg();
		msg.category = group.getGroupName();
		msg.policyName = policyName;
		msg.modifiedType = RuleUpdateMsg.ModifiedType.Rule_Delete;
		redisPubSender.publishMessageByChannel("ENGINE_RULE_UPDATE", JSONUtil.serialize(msg));
	}

	private Policy toPolicy(PolicyWrapper policyWrapper) {
		return toPolicy(policyWrapper.getPolicyContent());
	}

	private Policy toPolicy(String policyContent) {
		return JSONUtil.deserialize(policyContent, Policy.class);
	}

	private boolean checkPolicyEnable(Boolean policyStatus) {
		if (policyStatus == null || policyStatus == false) {
			return false;
		}

		return true;
	}
}
