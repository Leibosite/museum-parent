package com.qingruan.museum.admin.service.permission;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.qingruan.museum.dao.entity.AccessGroup;
import com.qingruan.museum.dao.entity.AccessPermission;
import com.qingruan.museum.dao.entity.AccessUser;

/**
 * 自实现用户与权限查询. 演示关系，密码用明文存储，因此使用默认 的SimpleCredentialsMatcher.
 */
@Slf4j
public class ShiroDbRealm extends AuthorizingRealm {
	@Autowired
	private UserManager userManager;

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	/**
	 * 认证回调函数, 登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		AccessUser accessUser = userManager.findUserByLoginName(token
				.getUsername());
		if (accessUser != null) {
			// String decryptPass = new String(AES.decrypt(AES
			// .parseHexStr2Byte(accessUser.getPassword())));
			String decryptPass = accessUser.getPassword();
			SimpleAuthenticationInfo result = new SimpleAuthenticationInfo(
					new ShiroUser(accessUser.getLoginName(),
							accessUser.getName()), decryptPass, getName());
			return result;
		} else {
			return null;
		}
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {

		ShiroUser shiroUser = (ShiroUser) principals.fromRealm(getName())
				.iterator().next();
		AccessUser accessUser = userManager.findUserByLoginName(shiroUser
				.getLoginName());
		log.info("{} on pcrf_admin() authenticate successed.",
				shiroUser.getLoginName());
		if (accessUser != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			// 基于Permission的权限信息
			info.addObjectPermissions(getObjectPermissions(accessUser));
			info.addStringPermissions(getStringPermissions(accessUser));
			info.addRoles(getStringRoles(accessUser));
			return info;
		} else {
			return null;
		}
	}

	public List<String> getStringRoles(AccessUser accessUser) {
		List<String> roles = new ArrayList<String>();
		for (AccessGroup group : accessUser.getAccessGroups()) {
			roles.add(group.getName().trim());
		}

		return roles;
	}

	public List<String> getStringPermissions(AccessUser accessUser) {
		List<String> permissionLists = Lists.newArrayList();

		for (AccessGroup accessGroup : accessUser.getAccessGroups()) {
			for (AccessPermission accPer : accessGroup.getAccessPermissions()) {
				permissionLists.add(accPer.getValue());
			}
		}
		return permissionLists;
	}

	public List<Permission> getObjectPermissions(AccessUser accessUser) {
		List<Permission> permissionLists = Lists.newArrayList();

		for (AccessGroup accessGroup : accessUser.getAccessGroups()) {
			for (AccessPermission accPer : accessGroup.getAccessPermissions()) {
				permissionLists.add(new WildcardPermission(accPer.getValue()));
			}
		}
		return permissionLists;
	}

	/**
	 * 更新用户授权信息缓存.
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(
				principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 清除所有用户授权信息缓存.
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}

	/**
	 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
	 */
	public static class ShiroUser implements Serializable {
		private static final long serialVersionUID = -17486023829637114L;
		private String loginName;
		private String name;

		public ShiroUser(String loginName, String name) {
			this.loginName = loginName;
			this.name = name;
		}

		public String getLoginName() {
			return loginName;
		}

		/**
		 * 本函数输出将作为默认的<shiro:principal/>输出.
		 */
		@Override
		public String toString() {
			return loginName;
		}

		public String getName() {
			return name;
		}
	}
}
