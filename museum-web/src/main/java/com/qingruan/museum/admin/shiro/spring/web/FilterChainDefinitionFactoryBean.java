package com.qingruan.museum.admin.shiro.spring.web;

import java.text.MessageFormat;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

/**
 * 
 * @author tommy
 */
@Slf4j
@Setter
public class FilterChainDefinitionFactoryBean implements FactoryBean<Section> {
	private static boolean isDebugEnabled = log.isDebugEnabled();
	
	/** 默认premission字符串 */
	public static final String PREMISSION_STRING = "perms[\"{0}\"]";
	/** 默认role字符串 */
	public static final String ROLE_STRING = "roles[\"{0}\"]";

	private String beforFilterChainDefinitions;
	private String afterFilterChainDefinitions;
	private Resource[] filterChainDefinitionLocations;

	@Override
	public Section getObject() throws Exception {
		Ini ini = new Ini();
		// 加载默认的url
		ini.load(beforFilterChainDefinitions);
		Ini.Section section = ini.getSection(Ini.DEFAULT_SECTION_NAME);

		// 处理xml配置文件配置的路径.
		if (filterChainDefinitionLocations != null) {
			for (Resource resource : this.filterChainDefinitionLocations) {
				AppUrlPermission appUrlPermission = AppUrlPermission.parseAppUrlPermissions(resource.getInputStream());
				if (appUrlPermission != null) {
					for (UrlPermission permission : appUrlPermission.getUrlPermissions()) {
						if (StringUtils.isBlank(permission.getPath())) {
							continue;
						}
						
						StringBuffer filterChain = new StringBuffer();
						boolean isBlank = true;
						
						if (StringUtils.isNotBlank(permission.getFilter())) {
							isBlank = false;
							filterChain.append(permission.getFilter());
						}
						
						if (StringUtils.isNotBlank(permission.getRoles())) {
							if (!isBlank) {
								filterChain.append(",");
							} else {
								isBlank = false;
							}
							
							filterChain.append(MessageFormat.format(ROLE_STRING, permission.getRoles()));
						}
						
						if (StringUtils.isNotBlank(permission.getPermissions())) {
							if (!isBlank) {
								filterChain.append(",");
							} else {
								isBlank = false;
							}
							
							filterChain.append(MessageFormat.format(PREMISSION_STRING, permission.getPermissions()));
						}
						
						if (!isBlank) {
							section.put(permission.getPath(), filterChain.toString());
							if (isDebugEnabled) {
								log.debug("{} = {}", permission.getPath(), filterChain.toString());
							}
						}
					}
				}
			}
		}
		
		return section;
	}

	@Override
	public Class<?> getObjectType() {
		return Section.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

}
