package com.qingruan.museum.pma.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.qingruan.museum.pma.meta.DomainModel;
import com.qingruan.museum.pma.meta.InterfaceModel;
import com.qingruan.museum.pma.util.ClassPathScanHandler;

@Getter
@Service
public class RuleModelContainer implements ApplicationContextAware,
		InitializingBean {

	private List<String> baseConstraintDomainModels = new ArrayList<String>();
	private List<String> baseConstraintInterfaceModels = new ArrayList<String>();
	private List<String> baseExecutionInterfaceModels = new ArrayList<String>();

	// use for back-end builder.
	private Map<String, DomainModel> domainModels = new HashMap<String, DomainModel>();
	private Map<String, InterfaceModel> interfaceModels = new HashMap<String, InterfaceModel>();

	private Map<String, List<String>> enumsValueMap = new HashMap<String, List<String>>();

	private static RuleModelsJsonWrapper rmj;

	public Map<String, DomainModel> getDomainModels() {
		if (domainModels == null || domainModels.size() == 0) {
			if (rmj != null) {
				domainModels = rmj.getAllDomainModels();
			}
		}
		return domainModels;
	}

	public Map<String, InterfaceModel> getInterfaceModels() {
		// if domain generator , comment if statement
		if (interfaceModels == null || interfaceModels.size() == 0) {
			if (rmj != null) {
				interfaceModels = rmj.getAllInterfaceModels();
			}
		}
		return interfaceModels;
	}

	public DomainModel findDomainModel(String name) {
		return this.getDomainModels().get(name);
	}

	public InterfaceModel findInterfaceModel(String name) {
		return this.getInterfaceModels().get(name);
	}

	@SuppressWarnings("rawtypes")
	public void setUpEnum(Class c) {
		List<String> enumCallList = new ArrayList<String>();
		for (Field s : c.getFields()) {
			enumCallList.add(c.getSimpleName() + "." + s.getName());
		}
		this.getEnumsValueMap().put(c.getSimpleName(), enumCallList);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// RuleModelBuildService rmb = new RuleModelBuildService();
		// ClassPathScanHandler handler = new ClassPathScanHandler();
		// Set<Class<?>> constraintMethod = handler.getPackageAllClasses(
		// "com.baoyun", true);
		// for (Class c : constraintMethod) {
		// rmb.ScanForPmaMeta(c,c.getcl);
		// }
		// RuleModelContainer rmc = rmb.ruleModelContainer;
		// RuleModelsJsonWrapper ruleModelWrapper = new RuleModelsJsonWrapper();
		// List<DomainModel> cd = new ArrayList<DomainModel>();
		// for (String s : rmc.getBaseConstraintDomainModels()) {
		// cd.add(rmc.getDomainModels().get(s));
		// }
		// ruleModelWrapper.setConstraintDomain(cd);
		//
		// List<InterfaceModel> ci = new ArrayList<InterfaceModel>();
		// for (String s : rmc.getBaseConstraintInterfaceModels()) {
		// ci.add(rmc.getInterfaceModels().get(s));
		// }
		// ruleModelWrapper.setConstraintInterface(ci);
		//
		// List<InterfaceModel> ei = new ArrayList<InterfaceModel>();
		// for (String s : rmc.getBaseExecutionInterfaceModels()) {
		// ei.add(rmc.getInterfaceModels().get(s));
		// }
		// ruleModelWrapper.setExecutionInterface(ei);
		//
		// ruleModelWrapper.setAllDomainModels(rmc.getDomainModels());
		// ruleModelWrapper.setAllInterfaceModels(rmc.getInterfaceModels());
		// rmj = ruleModelWrapper;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		RuleModelBuildService rmb = new RuleModelBuildService();
		ClassPathScanHandler handler = new ClassPathScanHandler();
		Set<Class<?>> constraintMethod = handler.getPackageAllClasses(
				"com.qingruan", true);
		for (Class c : constraintMethod) {
			rmb.ScanForPmaMeta(c, context);
		}
		RuleModelContainer rmc = rmb.ruleModelContainer;
		RuleModelsJsonWrapper ruleModelWrapper = new RuleModelsJsonWrapper();
		List<DomainModel> cd = new ArrayList<DomainModel>();
		for (String s : rmc.getBaseConstraintDomainModels()) {
			cd.add(rmc.getDomainModels().get(s));
		}
		ruleModelWrapper.setConstraintDomain(cd);

		List<InterfaceModel> ci = new ArrayList<InterfaceModel>();
		for (String s : rmc.getBaseConstraintInterfaceModels()) {
			ci.add(rmc.getInterfaceModels().get(s));
		}
		ruleModelWrapper.setConstraintInterface(ci);

		List<InterfaceModel> ei = new ArrayList<InterfaceModel>();
		for (String s : rmc.getBaseExecutionInterfaceModels()) {
			ei.add(rmc.getInterfaceModels().get(s));
		}
		ruleModelWrapper.setExecutionInterface(ei);

		ruleModelWrapper.setAllDomainModels(rmc.getDomainModels());
		ruleModelWrapper.setAllInterfaceModels(rmc.getInterfaceModels());
		rmj = ruleModelWrapper;

	}
}
