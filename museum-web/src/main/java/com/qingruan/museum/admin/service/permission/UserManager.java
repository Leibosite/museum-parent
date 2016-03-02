package com.qingruan.museum.admin.service.permission;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.qingruan.museum.admin.service.ServiceException;
import com.qingruan.museum.admin.web.permission.form.AccessUserQueryForm;
import com.qingruan.museum.dao.entity.AccessUser;
import com.qingruan.museum.dao.repository.AccessUserDao;
import com.qingruan.museum.framework.spring.data.jpa.util.SpecificationUtil;

//Spring Bean的标识.
@Slf4j
@Component
@Transactional(readOnly = true)
public class UserManager {
	@Autowired
	private AccessUserDao accessUserDao;

	@Autowired(required = false)
	private ShiroDbRealm shiroRealm;

	public AccessUser getAccessUser(Long id) {
		return accessUserDao.findOne(id);
	}

	public AccessUser findUserByLoginName(String loginName) {
		return accessUserDao.findByLoginName(loginName);
	}

	/**
	 * 判断是否超级管理员.
	 */
	private boolean isSupervisor(Long id) {
		return id == 1;
	}

	/**
	 * 分页查询方法
	 * 
	 * @param form
	 * @param pageable
	 * @return
	 */
	public Page<AccessUser> getPaged(AccessUserQueryForm form, Pageable pageable) {
		return accessUserDao
				.findAll(Specifications.where(SpecificationUtil.like(
						AccessUser.class, "loginName", form.getName())),
						pageable);
	}

	public Page<AccessUser> getAllAccessUsers(Pageable pageable) {
		return (Page<AccessUser>) accessUserDao.findAll(pageable);
	}

	public Page<AccessUser> getAllAccessUserByName(String name,
			Pageable pageable) {
		return accessUserDao.findByName(name, pageable);
	}

	@Transactional(readOnly = false)
	public void saveAccessUser(AccessUser entity) {
		accessUserDao.save(entity);
		// shiroRealm.clearCachedAuthorizationInfo(entity.getLoginName());
	}

	@Transactional(readOnly = false)
	public void deleteAccessUser(Long id) {
		if (isSupervisor(id)) {
			log.warn("操作员{}尝试删除超级管理员用户", SecurityUtils.getSubject()
					.getPrincipal());
			throw new ServiceException("不能删除超级管理员用户");
		}
		accessUserDao.delete(id);
	}

	
	public void setShiroRealm(ShiroDbRealm shiroRealm) {
		this.shiroRealm = shiroRealm;
	}

	public void setAccessUserDao(AccessUserDao accessUserDao) {
		this.accessUserDao = accessUserDao;
	}

	public AccessUser getByLoginName(String loginName) {
		return accessUserDao.findByLoginName(loginName);
	}
}
