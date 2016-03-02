package com.qingruan.museum.engine.service.rule.core;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.extern.slf4j.Slf4j;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatelessKnowledgeSession;

import com.qingruan.museum.engine.exception.ExceptionLog;

@Slf4j
public class KnowledgeBaseUnit {
	private List<String> globles;

	private KnowledgeBase knowledgeBase;

	private Map<String, Object> globleObjects = new HashMap<String, Object>();

	public KnowledgeBaseUnit() {
		knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
	}

	public List<String> getGlobles() {
		return globles;
	}

	public void setGlobles(List<String> globles) {
		this.globles = globles;
	}

	public KnowledgeBase getKnowledgeBase() {
		return knowledgeBase;
	}

	public void setKnowledgeBase(KnowledgeBase knowledgeBase) {
		this.knowledgeBase = knowledgeBase;
	}

	public Map<String, Object> getGlobleObjects() {
		return globleObjects;
	}

	public void addGlobleObject(String identifer, Object globleObject) {
		globleObjects.put(identifer, globleObject);
	}

	/**
	 * 生成StatelessKnowledgeSession
	 * @return
	 * 		StatelessKnowledgeSession
	 * */
	public StatelessKnowledgeSession generateStatelessKnowledgeSession() {
		StatelessKnowledgeSession statelessKnowledgeSession = null;

		statelessKnowledgeSession = knowledgeBase
				.newStatelessKnowledgeSession();

		for (Entry<String, Object> entry : globleObjects.entrySet()) {
			statelessKnowledgeSession.setGlobal(entry.getKey(),
					entry.getValue());
		}

		return statelessKnowledgeSession;
	}

	/**
	 * 根据规则内容为knowledgeBase添加规则包
	 * @param
	 * 		content	规则内容
	 * */
	public void loadKnowledgeBaseWithContent(String content) {
		log.debug("loadKnowledgeBaseWithContent--------------start.");
		
		//生成KnowledgePackage集合
		Collection<KnowledgePackage> knowledgePackages = generateKnowledgePackageWithContent(content);
		if (knowledgePackages == null) {
			log.info("loadKnowledgeBaseWithContent------------end. content is invalid");

			return;
		}

		//添加规则包
		knowledgeBase.addKnowledgePackages(knowledgePackages);

		log.debug("loadKnowledgeBaseWithContent ---------- out.");
	}

	/**
	 * 根据规则内容加载规则
	 * @param
	 * 		content	规则内容
	 * */
	public void updateKnowledgeBaseWithContent(String content) {
		log.debug("updateKnowledgeBaseWithContent--------------start.");

		loadKnowledgeBaseWithContent(content);

		log.debug("updateKnowledgeBaseWithContent ---------- out.");
	}

	/**
	 * 根据规则内容生成KnowledgePackage
	 * @param
	 * 		content	规则内容
	 * @return
	 * 		Collection<KnowledgePackage>	通过KnowledgeBuilder.getKnowledgePackages()获取
	 * */
	private Collection<KnowledgePackage> generateKnowledgePackageWithContent(String content) {
		//获取KnowledgeBuilder
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		
		Reader reader = new StringReader(content);
		
		//加载DRL规则
		kbuilder.add(ResourceFactory.newReaderResource(reader), ResourceType.DRL);
		
		//检查规则是否有错误
		if (kbuilder.hasErrors()) {
			printKnowledgeBuilderErrors(kbuilder.getErrors());

			return null;
		}

		//返回Collection<KnowledgePackage>
		return kbuilder.getKnowledgePackages();
	}

	//打印规则错误
	private void printKnowledgeBuilderErrors(
			KnowledgeBuilderErrors knowledgeBuilderErrors) {
		log.info(knowledgeBuilderErrors.toString());
	}

	public void removeSingleRule(String ruleName) {
		removeSingleRule(knowledgeBase, ruleName);
	}

	private void removeSingleRule(KnowledgeBase knowledgeBase, String ruleName) {
		log.debug("removeSingleRule-----------start.");
		Collection<KnowledgePackage> knowledgePackages = knowledgeBase
				.getKnowledgePackages();

		for (KnowledgePackage knowledgePackage : knowledgePackages) {
			if (knowledgePackage.getName().equals("defaultpkg") == false) {
				continue;
			}

			try {
				knowledgeBase.removeRule(knowledgePackage.getName(), ruleName);
			} catch (Exception e) {
				log.error(ExceptionLog.getErrorStack(e));
			}

		}

		log.debug("removeSingleRule -------- out.");
	}

	public void clearKnowledgeBase() {
		clearKnowledgeBase(knowledgeBase);
	}

	private void clearKnowledgeBase(KnowledgeBase knowledgeBase) {
		Collection<KnowledgePackage> knowledgePackages = knowledgeBase
				.getKnowledgePackages();
		for (KnowledgePackage knowledgePackage : knowledgePackages) {
			knowledgeBase.removeKnowledgePackage(knowledgePackage.getName());
		}
	}
}
