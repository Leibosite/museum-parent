package com.qingruan.museum.admin.shiro.spring.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.digester3.binder.DigesterLoader;
import org.apache.commons.digester3.xmlrules.FromXmlRulesModule;
import org.springframework.core.io.ClassPathResource;
import org.xml.sax.SAXException;

import com.qingruan.museum.framework.util.ExceptionLogUtil;

/**
 * 
 * @author tommy
 */
@Slf4j
@Getter
@Setter
public class AppUrlPermission {

	private String name;
	private List<UrlPermission> urlPermissions = new ArrayList<UrlPermission>();

	public void addUrlPermission(UrlPermission urlPermission) {
		this.urlPermissions.add(urlPermission);
	}
	
	public static AppUrlPermission parseAppUrlPermissions(InputStream inputStream) {
		DigesterLoader loader = DigesterLoader.newLoader(new FromXmlRulesModule() {
			protected void loadRules() {
				loadXMLRules(AppUrlPermission.class.getResourceAsStream("app-url-permission.rule.xml"));
			}
		});
		
		try {
			return loader.newDigester().parse(inputStream);
		} catch (IOException e) {
			log.error(ExceptionLogUtil.getErrorStack(e));
		} catch (SAXException e) {
			log.error(ExceptionLogUtil.getErrorStack(e));
		}
		
		return null;
	}
	
	public static void main(String args[]) throws IOException, SAXException {
		AppUrlPermission urlPermissions = AppUrlPermission.parseAppUrlPermissions(new ClassPathResource("classpath:/META-INF/app-url-permission.rule.xml").getInputStream());
		for (UrlPermission permission : urlPermissions.getUrlPermissions()) {
			System.out.println(permission.getPath());
			System.out.println(permission.getFilter());
			System.out.println(permission.getRoles());
			System.out.println(permission.getPermissions());
		}
	}
}
