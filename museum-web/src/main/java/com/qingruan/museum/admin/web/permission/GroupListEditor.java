package com.qingruan.museum.admin.web.permission;

import java.beans.PropertyEditorSupport;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qingruan.museum.admin.service.permission.GroupManager;
import com.qingruan.museum.admin.utils.Collections3;
import com.qingruan.museum.dao.entity.AccessGroup;

/**
 * 用于转换用户表单中复杂对象Group的checkbox的关联。
 */
@Component
public class GroupListEditor extends PropertyEditorSupport {

	@Autowired
	private GroupManager groupManager;

	/**
	 * Back From Page
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		String[] ids = StringUtils.split(text, ",");
		Set<AccessGroup> groups = new HashSet<AccessGroup>();
		for (String id : ids) {
			AccessGroup group = groupManager.getAccessGroup(Long.valueOf(id));
			groups.add(group);
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
