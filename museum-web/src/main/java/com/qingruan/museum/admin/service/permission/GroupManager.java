package com.qingruan.museum.admin.service.permission;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.qingruan.museum.admin.web.permission.form.AccessGroupQueryForm;
import com.qingruan.museum.dao.entity.AccessGroup;
import com.qingruan.museum.dao.repository.AccessGroupDao;
import com.qingruan.museum.framework.spring.data.jpa.util.SpecificationUtil;

//Spring Bean的标识.
@Component
@Transactional(readOnly = true)
public class GroupManager {
	private static Logger logger = LoggerFactory.getLogger(GroupManager.class);

	@Autowired
	private AccessGroupDao accessGroupDao;

	@Autowired(required = false)
	private ShiroDbRealm shiroRealm;

	public AccessGroup getAccessGroup(Long id) {
		return accessGroupDao.findOne(id);
	}

	public List<AccessGroup> getAllAccessGroups() {
		return (List<AccessGroup>) accessGroupDao.findAll();
	}

	/**
	 * 分页查询方法
	 * 
	 * @param form
	 * @param pageable
	 * @return
	 */
	public Page<AccessGroup> getPaged(AccessGroupQueryForm form,
			Pageable pageable) {
		return accessGroupDao.findAll(Specifications
				.where(SpecificationUtil.like(AccessGroup.class,"name", form.getName())),
				pageable);
	}

	public Page<AccessGroup> getAllAccessGroups(Pageable pageable) {
		return (Page<AccessGroup>) accessGroupDao.findAll(pageable);
	}

	public Page<AccessGroup> getAllAccessGroupByName(String name,
			Pageable pageable) {
		return accessGroupDao.findByName(name, pageable);
	}

	/**
	 * 
	 * other methods
	 */
	@Transactional(readOnly = false)
	public void saveAccessGroup(AccessGroup entity) {
		accessGroupDao.save(entity);
		shiroRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public void deleteAccessGroup(Long id) {
		accessGroupDao.delete(id);
		shiroRealm.clearAllCachedAuthorizationInfo();
	}

	public void setShiroRealm(ShiroDbRealm shiroRealm) {
		this.shiroRealm = shiroRealm;
	}

	public void setAccessGroupDao(AccessGroupDao accessGroupDao) {
		this.accessGroupDao = accessGroupDao;
	}
}