package com.qingruan.museum.admin.service.pma;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.qingruan.museum.dao.entity.PolicyGroup;
import com.qingruan.museum.dao.repository.PolicyGroupDao;
import com.qingruan.museum.framework.spring.data.jpa.util.SpecificationUtil;

@Slf4j
@Component
@Transactional(readOnly = true)
public class PolicyGroupManager {

	@Autowired
	private PolicyGroupDao policyGroupDao;

	public PolicyGroup getPolicyGroup(Long id) {
		return policyGroupDao.findOne(id);
	}

	/**
	 * 分页查询方法
	 * 
	 * @param form
	 * @param pageable
	 * @return
	 */
	public Page<PolicyGroup> getPaged(String groupName, Pageable pageable) {

		return policyGroupDao.findAll(SpecificationUtil.like(PolicyGroup.class,
				"groupName", groupName), pageable);

	}

	/**
	 * edit operation
	 * 
	 */
	@Transactional(readOnly = false)
	public void savePolicyGroup(PolicyGroup entity) {
		if (entity.getId() == null) {
			policyGroupDao.save(entity);
			log.info("save policygroup {}", entity.toString());
			return;
		}

		PolicyGroup _policyGroup = policyGroupDao.findOne(entity.getId());
		if (_policyGroup == null) {
			return;
		}

		_policyGroup.setGroupName(entity.getGroupName());
		_policyGroup.setDesp(entity.getDesp());
		policyGroupDao.save(_policyGroup);
		log.info("save policygroup {}", _policyGroup.toString());
	}

	@Transactional(readOnly = false)
	public void deletePolicyGroup(Long id) {
		policyGroupDao.delete(id);
		log.info("delete policyGroup {}", id);
	}

	public List<PolicyGroup> getAllPolicyGroup() {
		return (List<PolicyGroup>) policyGroupDao.findAll();
	}

}