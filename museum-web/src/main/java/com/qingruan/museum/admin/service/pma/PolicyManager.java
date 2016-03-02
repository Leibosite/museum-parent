package com.qingruan.museum.admin.service.pma;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.qingruan.museum.admin.service.spec.PolicySpecifications;
import com.qingruan.museum.admin.utils.BusinessRecordUtil;
import com.qingruan.museum.dao.entity.PolicyGroup;
import com.qingruan.museum.dao.entity.PolicyWrapper;
import com.qingruan.museum.dao.repository.PolicyGroupDao;
import com.qingruan.museum.dao.repository.PolicyWrapperDao;
import com.qingruan.museum.framework.jackson.JSONUtil;
import com.qingruan.museum.pma.model.Policy;

@Slf4j
@Component
@Transactional(readOnly = true)
public class PolicyManager {

	@Autowired
	private PolicyWrapperDao policyWrapperDao;

	@Autowired
	private PolicyGroupDao policyGroupDao;
//	@Autowired
//	private PolicyDataModified policyDataModified;

	public Policy getPolicy(Long id) {
		// 避免报空指针异常
		if (policyWrapperDao.findOne(id) != null) {
			Policy policy = JSONUtil.deserialize(policyWrapperDao.findOne(id)
					.getPolicyContent(), Policy.class);
			policy.setId(id);
			return policy;
		} else
			return null;

	}

	/**
	 * edit operation
	 * 
	 */
	@Transactional(readOnly = false)
	public void savePolicy(Policy entity) {
		PolicyWrapper policy = new PolicyWrapper();
		policy.setPolicyGroupId(entity.getPolicyGroup().getId());
		// policy.setPolicyGroup(entity.getPolicyGroup());
		policy.setPolicyContent(JSONUtil.serialize(entity));
		policy.setId(entity.getId());
		policy.setDesp(entity.getDesp());
		// 避免空指针
		if (entity.getStatus() != null && entity.getStatus())
			policy.setStatus(1);
		else
			policy.setStatus(0);

		policyWrapperDao.save(policy);

		PolicyGroup policyGroup = policyGroupDao.findOne(entity
				.getPolicyGroup().getId());
		if (policyGroup != null) {
            log.info("policy group is{}" + policyGroup.toString());
//			policyDataModified.sendUpdateRuleMsg(policyGroup, entity.getId());

			log.info("save policy {}", JSONUtil.serialize(entity));

		}

	}

	@Transactional(readOnly = false)
	public void deletePolicy(Long id) {
		PolicyWrapper policyWrapper = policyWrapperDao.findOne(id);
		if (policyWrapper != null) {
			PolicyGroup group = policyGroupDao.findOne(policyWrapper
					.getPolicyGroupId());
			Policy policy = this.toPolicy(policyWrapper);

			if (group == null) {
				return;
			}

//			policyDataModified.sendDeleteRuleMsg(group, policy.getPolicyName());
		}

		policyWrapperDao.delete(id);

		log.info("delete policy {}", id);
	}

	@Transactional(readOnly = false)
	public void disable(Long id) {
		Policy policy = this.getPolicy(id);
		policy.setStatus(false);
		this.savePolicy(policy);
		PolicyGroup policyGroup = policyGroupDao.findOne(policy
				.getPolicyGroup().getId());
		policy.setPolicyGroup(policyGroup);
		log.info("disable policy {}", JSONUtil.serialize(policy));
	}

	@Transactional(readOnly = false)
	public void enable(Long id) {
		Policy policy = this.getPolicy(id);
		policy.setStatus(true);
		this.savePolicy(policy);
		PolicyGroup policyGroup = policy.getPolicyGroup();
		policyGroup = policyGroupDao.findOne(policyGroup.getId());
		policy.setPolicyGroup(policyGroup);
		log.info("enable policy {}", JSONUtil.serialize(policy));
	}

	public List<Policy> getByPolicyGroupId(Long policyGroupId) {
		// PolicyGroup policyGroup=policyGroupDao.findOne(policyGroupId);
		List<PolicyWrapper> jsonPolicies = policyWrapperDao
				.findByPolicyGroupId(policyGroupId);
		List<Policy> policies = new ArrayList<Policy>();
		for (PolicyWrapper p : jsonPolicies) {
			Policy policy = JSONUtil.deserialize(p.getPolicyContent(),
					Policy.class);
			policy.setId(p.getId());
			policies.add(policy);
		}

		return policies;
	}

	public Page<Policy> getPaged(Long policyGroupId, String policyName,
			Integer status, String start, String end, Pageable pageable) {

		// int skip = pageable.getPageNumber() * pageable.getPageSize();
		// int limit = pageable.getPageSize();

		// String query = "%";
		// if (policyName != null) {
		// query = "%policyName%" + policyName + "%salience%";
		// }
		// List<PolicyWrapper> jsonPolicies = policyDao
		// .findByPolicyGroupIdAndPolicyContent(policyGroupId, query);
		// TODO:
		Long startL = BusinessRecordUtil.convertTimeString2Long(start);
		Long endL = BusinessRecordUtil.convertTimeString2Long(end);

		Page<PolicyWrapper> jsonPolicies = policyWrapperDao.findAll(
				PolicySpecifications.policySepc(policyGroupId, policyName,
						status, startL, endL), pageable);

		List<Policy> policies = new ArrayList<Policy>();

		for (PolicyWrapper p : jsonPolicies) {
			Policy policy = JSONUtil.deserialize(p.getPolicyContent(),
					Policy.class);
			policy.setId(p.getId());
			policies.add(policy);
		}

		Page<Policy> page = new PageImpl<Policy>(policies, pageable,
				jsonPolicies.getTotalElements());

		return page;
	}

	public List<Policy> findByNameLike(String name) {
		List<PolicyWrapper> jsonPolicies = policyWrapperDao
				.findByPolicyContentLike(name);
		List<Policy> policies = new ArrayList<Policy>();

		for (PolicyWrapper pw : jsonPolicies) {
			Policy policy = JSONUtil.deserialize(pw.getPolicyContent(),
					Policy.class);
			policy.setId(pw.getId());

			policies.add(policy);
		}

		return policies;
	}

	private Policy toPolicy(PolicyWrapper policyWrapper) {
		return toPolicy(policyWrapper.getPolicyContent());
	}

	private Policy toPolicy(String policyContent) {
		return JSONUtil.deserialize(policyContent, Policy.class);
	}

}