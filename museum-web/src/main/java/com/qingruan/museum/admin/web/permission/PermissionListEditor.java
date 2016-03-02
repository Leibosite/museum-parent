package com.qingruan.museum.admin.web.permission;

import java.beans.PropertyEditorSupport;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qingruan.museum.admin.service.permission.PermissionManager;
import com.qingruan.museum.admin.utils.Collections3;
import com.qingruan.museum.dao.entity.AccessPermission;

@Component
public class PermissionListEditor extends PropertyEditorSupport {

	@Autowired
	public PermissionManager m;

	/**
	 * Back From Page
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		String[] ids = StringUtils.split(text, ",");
		Set<AccessPermission> groups = new HashSet<AccessPermission>();
		for (String id : ids) {
			AccessPermission permission = m.getAccessPermission(Long
					.valueOf(id));
			groups.add(permission);
		}
		setValue(groups);
	}

	/**
	 * Set to page
	 */
	@Override
	public String getAsText() {
		return Collections3.extractToString((Set) getValue(), "id", ",");
	}
}
