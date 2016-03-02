package com.qingruan.museum.admin.service.permission;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.qingruan.museum.admin.web.permission.form.PermissionQueryForm;
import com.qingruan.museum.dao.entity.AccessPermission;
import com.qingruan.museum.dao.repository.AccessPermissionDao;
import com.qingruan.museum.framework.spring.data.jpa.util.SpecificationUtil;

//Spring Bean的标识.
@Component
@Transactional(readOnly = true)
public class PermissionManager {
	private static Logger logger = LoggerFactory
			.getLogger(PermissionManager.class);

	@Autowired(required = false)
	private ShiroDbRealm shiroRealm;

	@Autowired
	private AccessPermissionDao accessPermissionDao;

	public AccessPermission getAccessPermission(Long id) {
		return accessPermissionDao.findOne(id);
	}

	public List<Permission> getSubPermissions(Long id) {
		List<Permission> res = new ArrayList<Permission>();
		List<AccessPermission> temp = accessPermissionDao.findOne(id)
				.getSubPermission();
		for (AccessPermission permission : temp) {
			res.add(new WildcardPermission(permission.getValue()));
		}
		return res;
	}

	/**
	 * 
	 * @param permissionValue
	 * @return
	 */
	public AccessPermission getSpecAccessPermission(String permissionValue) {
		List<AccessPermission> s = accessPermissionDao.findByValueOrderByPriorityAsc(permissionValue);
		return CollectionUtils.isNotEmpty(s) ? s.get(0) : null;
	}
	
	/**
	 * 用于显示权限列表
	 * 
	 * @param superPermissionValue
	 * @return
	 */
	public List<AccessPermission> getSubPermissions(String superPermissionValue) {
		Subject currentUser = SecurityUtils.getSubject();
		
		List<AccessPermission> s = accessPermissionDao.findByValueOrderByPriorityAsc(superPermissionValue);
		List<AccessPermission> hasPermission = new ArrayList<AccessPermission>();
		if (CollectionUtils.isNotEmpty(s)) {
			AccessPermission p = s.get(0);
			for (AccessPermission sub_p : p.getSubPermission()) {
				if (currentUser.isPermitted(new WildcardPermission(sub_p.getValue()))) {
					hasPermission.add(sub_p);
				}
			}
		}

		return hasPermission;
	}

	public List<AccessPermission> getAllAccessPermissions() {
		return (List<AccessPermission>) accessPermissionDao.findAll(new Sort(
				Direction.ASC, "id"));
	}

	public List<AccessPermission> getAccessPermissionsByName(String name) {
		return accessPermissionDao.findByDisplayname(name);
	}

	public List<AccessPermission> getAccessPermissionsByNameLike(String name) {
		return accessPermissionDao.findByDisplaynameLike(name);
	}

	/**
	 * 分页查询方法
	 * 
	 * @param form
	 * @param pageable
	 * @return
	 */
	public Page<AccessPermission> getPaged(PermissionQueryForm form,
			Pageable pageable) {
		return accessPermissionDao
				.findAll(Specifications.where(SpecificationUtil.like(AccessPermission.class,"displayname", form.getDisplayname())), pageable);
	}

	public Page<AccessPermission> getAllAccessPermissions(Pageable pageable) {
		return (Page<AccessPermission>) accessPermissionDao.findAll(pageable);
	}

	public Page<AccessPermission> getAllAccessPermissionByName(String name,
			Pageable pageable) {
		return accessPermissionDao.findByDisplayname(name, pageable);
	}

	/**
	 * 
	 * other methods
	 */
	@Transactional(readOnly = false)
	public void saveAccessPermission(AccessPermission entity) {
		accessPermissionDao.save(entity);
	}

	@Transactional(readOnly = false)
	public void deleteAccessPermission(Long id) {
		accessPermissionDao.delete(id);
	}

	public void setShiroRealm(ShiroDbRealm shiroRealm) {
		this.shiroRealm = shiroRealm;
	}

	public void setAccessPermissionDao(AccessPermissionDao accessPermissionDao) {
		this.accessPermissionDao = accessPermissionDao;
	}

}
