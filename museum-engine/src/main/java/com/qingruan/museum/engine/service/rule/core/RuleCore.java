package com.qingruan.museum.engine.service.rule.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.drools.runtime.StatelessKnowledgeSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.qingruan.museum.dao.entity.PolicyGroup;
import com.qingruan.museum.dao.entity.PolicyWrapper;
import com.qingruan.museum.dao.repository.PolicyGroupDao;
import com.qingruan.museum.dao.repository.PolicyWrapperDao;
import com.qingruan.museum.engine.exception.ExceptionLog;
import com.qingruan.museum.engine.framework.ApplicationContextGuardian;
import com.qingruan.museum.engine.service.rule.DelayTask;
import com.qingruan.museum.engine.service.rule.DelayTaskAgency;
import com.qingruan.museum.framework.jackson.JSONUtil;
import com.qingruan.museum.pma.drlbuilder.DrlDescription;
import com.qingruan.museum.pma.drlbuilder.FreeMarker;
import com.qingruan.museum.pma.meta.InterfaceModel;
import com.qingruan.museum.pma.meta.MethodModel;
import com.qingruan.museum.pma.model.Policy;
import com.qingruan.museum.pma.service.RuleExecuteHelper;
import com.qingruan.museum.pma.service.RuleModelContainer;

/**
 * 规则引擎核心类
 * */
@Slf4j
@NoArgsConstructor
public class RuleCore {

	@Autowired
	private PolicyGroupDao policyGroupDao;

	@Autowired
	private PolicyWrapperDao policyWrapperDao;

	// @Autowired
	// private DrlBuilder drlBuilder;

	@Autowired
	private FreeMarker drlBuilder;

	@Autowired
	private RuleModelContainer ruleModelContainer;
	// 业务行为记录
	// @Autowired
	// private BusinessBehaviorRecorder businessBehaviorRecorder;

	private List<KnowledgeBaseDecl> knowledgeBaseDecls;
	private Map<String, KnowledgeBaseUnit> knowledgeBases = new HashMap<String, KnowledgeBaseUnit>();

	public RuleCore(List<KnowledgeBaseDecl> knowledgeBaseDecls) {
		this.setKnowledgeBaseDecls(knowledgeBaseDecls);
	}

	/**
	 * 根据类别和策略ID更新Rule
	 * 
	 * @param category
	 *            类别
	 * @param policyId
	 *            策略ID
	 * */
	public void updateSingleRule(String category, Long policyId) {
		log.debug(
				"updateSingleRule--------------start. category: {}, policyId: {}",
				category, policyId);

		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			log.debug(ExceptionLog.getErrorStack(e));
		}

		PolicyWrapper policyWrapper = policyWrapperDao.findOne(policyId);

		updateSingleRule(category, policyWrapper, false);

		log.debug("updateSingleRule-------------------end.");
	}

	/**
	 * 更新规则
	 * 
	 * @param category
	 *            类别
	 * @param policyWrapper
	 *            对应数据表policy
	 * @param isInit
	 *            是否初始化
	 * */
	private void updateSingleRule(String category, PolicyWrapper policyWrapper,
			boolean isInit) {
		log.debug("updateSingleRule -------start.");

		if (policyWrapper == null) {
			log.error("updateSingleRule -------end. policy is null.");

			return;
		}

		Policy policy = JSONUtil.deserialize(policyWrapper.getPolicyContent(),
				Policy.class);

		if (policy.getStatus() == null || policy.getStatus() == false) {
			return;
		}

		KnowledgeBaseUnit knowledgeBaseUnit = findKnowledgeBaseUnitWithID(category);
		if (knowledgeBaseUnit == null) {
			log.error("updateSingleRule -------end. knowledgeBaseUnit is null. category = "
					+ category);

			return;
		}

		// 查询策略分组
		PolicyGroup policyGroup = policyGroupDao.findOne(policyWrapper
				.getPolicyGroupId());

		// 根据策略对象和策略分组对象生成DrlDescription
		DrlDescription drlDescription = drlBuilder.build(policy, policyGroup);

		if (drlDescription == null) {
			log.error("drlDescription is null. policyId = " + policy.getId()
					+ ", policyGroupId = " + policyGroup.getId());

			return;
		}

		if (isInit) {
			// 初始化时根据规则内容加载规则
			knowledgeBaseUnit.loadKnowledgeBaseWithContent(drlDescription
					.getContent());
		} else {
			// 非初始化时根据规则内容加载规则
			knowledgeBaseUnit.updateKnowledgeBaseWithContent(drlDescription
					.getContent());
		}

		log.debug("updateSingleRule -------end.");
	}

	/**
	 * 根据策略分组名称和策略名称删除策略
	 * 
	 * @param category
	 *            策略分组名称
	 * @param policyName
	 *            策略名称
	 * */
	public void deleteSingleRule(String category, String policyName) {
		log.debug(
				"deleteSingleRule--------------start. category: {}, policyName: {}.",
				category, policyName);

		KnowledgeBaseUnit knowledgeBaseUnit = findKnowledgeBaseUnitWithID(category);

		if (knowledgeBaseUnit == null) {
			log.warn("deleteSingleRule-------------end. knowledgeBaseUnit is null. category = "
					+ category);

			return;
		}

		knowledgeBaseUnit.removeSingleRule(policyName);

		log.debug("deleteSingleRule-----------------end.");
	}

	/**
	 * 根据规则分组名称重新加载规则
	 * 
	 * @param category
	 *            规则分组名称
	 * */
	public void reloadCategoryRules(String category) {
		log.debug("reloadCategoryRules------------start. category: {}",
				category);

		PolicyGroup matchedPolicyGroup = null;

		// 待优化部分
		List<PolicyGroup> policyGroups = policyGroupDao.findAll();

		if (policyGroups == null) {
			log.error("reloadCategoryRules---------end. policyGroupDao.findAll() result: policyGroups is null.");

			return;
		}

		// 待优化部分
		for (PolicyGroup policyGroup : policyGroups) {
			if (policyGroup.getGroupName().equals(category)) {
				matchedPolicyGroup = policyGroup;

				break;
			}
		}

		if (matchedPolicyGroup == null) {
			log.error("reloadCategoryRules---------end. can not find policyGroup matched with category = "
					+ category);

			return;
		}

		loadCategoryRules(matchedPolicyGroup);
		log.debug("reloadCategoryRules---------------end.");
	}

	/**
	 * 根据策略分组名称清除对应的规则
	 * 
	 * @param category
	 *            策略分组名称
	 * */
	public void clearCategoryRules(String category) {
		log.debug("clearCategoryRules-----------start. category: {}", category);

		KnowledgeBaseUnit knowledgeBaseUnit = findKnowledgeBaseUnitWithID(category);

		if (knowledgeBaseUnit == null) {
			log.warn("clearCategoryRules---------end. knowledgeBaseUnit is null. category = "
					+ category);

			return;
		}

		knowledgeBaseUnit.clearKnowledgeBase();

		log.debug("clearCategoryRules----------end.");
	}

	/**
	 * 根据ID查找KnowledgeBaseUnit对象
	 * 
	 * @param id
	 *            id
	 * */
	private KnowledgeBaseUnit findKnowledgeBaseUnitWithID(String id) {
		KnowledgeBaseUnit knowledgeBaseUnit = knowledgeBases.get(id);
		if (knowledgeBaseUnit == null) {
			log.warn("findKnowledgeBaseWithID: " + id
					+ " - knowledgeBaseUnit matched with id dose not exist.");
		}

		return knowledgeBaseUnit;
	}

	/**
	 * 执行规则
	 * 
	 * @param fact
	 *            事实
	 * @param knowledgeBaseId
	 *            knowledgeBaseId
	 * */
	// public void fire(Object fact, String knowledgeBaseId) {
	// log.debug("RuleCore:Fire ------------- Enter in.");
	//
	// KnowledgeBaseUnit knowledgeBaseUnit = knowledgeBases
	// .get(knowledgeBaseId);
	// if (knowledgeBaseUnit == null) {
	// log.info("RuleCore:Fire ------------end. knowledgeBaseUnit is null. knowledgeBaseId = "
	// + knowledgeBaseId);
	//
	// return;
	// }
	//
	// log.info("begin create knowledgesession, gxSessionId：" + gxSessionId);
	// // 生成StatelessKnowledgeSession
	// StatelessKnowledgeSession statelessKnowledgeSession = knowledgeBaseUnit
	// .generateStatelessKnowledgeSession();
	//
	// log.info("finish create knowledgesession, gxSessionId：" + gxSessionId);
	// // 执行规则
	// statelessKnowledgeSession.execute(fact);
	//
	// log.info("RuleCore:Fire ------------ end.");
	// }

	/**
	 * 执行Drools
	 * 
	 * @param fact
	 *            要执行的事实
	 * @param delayTaskAgency
	 *            DelayTaskAgency
	 * @param knowledgeBaseId
	 *            String
	 * */
	public void fire(Object fact, DelayTaskAgency delayTaskAgency,
			String knowledgeBaseId, String gxSessionId) {
		log.debug("RuleCore:Fire ------------- Enter in.");
		// 获取KnowledgeBaseUnit
		KnowledgeBaseUnit knowledgeBaseUnit = knowledgeBases
				.get(knowledgeBaseId);
		if (knowledgeBaseUnit == null) {
			log.error("Fire --------end. knowledgeBaseUnit is null. knowledgeBaseId = "
					+ knowledgeBaseId);

			return;
		}

		log.info("begin create konwledgeSession: " + gxSessionId);
		// 生成StatelessKnowledgeSession
		StatelessKnowledgeSession statelessKnowledgeSession = knowledgeBaseUnit
				.generateStatelessKnowledgeSession();

		log.info("finish create konwledgeSession: " + gxSessionId);

		log.info("begin execute face: " + gxSessionId);
		// 执行事实
		statelessKnowledgeSession.execute(Arrays.asList(fact, delayTaskAgency));
		log.info("finish execute face: " + gxSessionId);

		log.debug("Fire--------------------end.");
	}

	/**
	 * 执行Drools
	 * 
	 * @param fact
	 *            要执行的事实
	 * @param delayTaskAgency
	 *            DelayTaskAgency
	 * @param delayTaskSuperadder
	 *            DelayTaskSuperadder
	 * */
	public void fireAndExecute(Object fact, String knowledgeBaseId,
			DelayTaskSuperadder delayTaskSuperadder) {
		DelayTaskAgency delayTaskAgency = new DelayTaskAgency();
		// TODO:预处理
		// SubUser subUser = null;
		String gxSessionId = null;
		// if (fact instanceof IpcanContext) {
		// delayTaskAgency.setIpcanContext((IpcanContext) fact);
		// subUser = ((IpcanContext) fact).getRuntimeInfo().getSubUser();
		// gxSessionId = ((IpcanContext) fact).getBusinessInfo()
		// .getGxSessionId();
		// }

		fire(fact, delayTaskAgency, knowledgeBaseId, gxSessionId);

		if (delayTaskSuperadder != null) {
			delayTaskSuperadder.superadd(delayTaskAgency, fact);
		}

		log.info("begin conflict slove: " + gxSessionId);
		for (DelayTask dt : delayTaskAgency.delayTasks) {
			String interfaceName = RuleExecuteHelper.getInterfaceName(dt
					.getBusinessServiceName());
			String methodName = RuleExecuteHelper.getInterfaceMethod(dt
					.getBusinessServiceName());
			InterfaceModel i = ruleModelContainer.getInterfaceModels().get(
					interfaceName);
			MethodModel methodModel = i.getMethodModels().get(
					dt.getBusinessServiceName());
			Object cs = methodModel.getConflictTarget();
			// 如果cs的值是默认的Object.class则说明不需要做冲突检测，直接做
			if (cs == null) {
				i.getMethodAccess().invoke(i.getServiceBean(), methodName,
						dt.getParams().toArray());

				// 记录日志 policy_effective_record
				// TODO:记录的项
				// businessBehaviorRecorder.recordPolicyEffect(dt);

			}
			// 如果有冲突检测，先分组，再做
			else {
				Map<MethodModel, List<DelayTask>> conflictsMap = delayTaskAgency.conflicts;

				if (StringUtils.isNotBlank(methodModel.getDependParam())) {
					dt.setParamIndex(new Integer(methodModel.getDependParam()));
				}

				List<DelayTask> conflictList = conflictsMap.get(methodModel);
				// 如果该列表还未加载过，先初始化一下
				if (conflictList == null) {
					conflictList = new ArrayList<DelayTask>();
					conflictsMap.put(methodModel, conflictList);
				}

				conflictList.add(dt);

			}

		}

		// TODO:处理规则冲突
		// for (Entry<MethodModel, List<DelayTask>> conflictEntry :
		// delayTaskAgency.conflicts
		// .entrySet()) {
		// MethodModel m = conflictEntry.getKey();
		// ConflictSolver cs;
		// try {
		// // 获取相应的ConflictSolver来处理冲突, conflictService
		// cs = (ConflictSolver) m.getConflictTarget();
		// List<DelayTask> nonConflictTask = cs.solve(conflictEntry
		// .getValue(), delayTaskAgency.getIpcanContext()
		// .getBusinessInfo(), delayTaskAgency.getIpcanContext());
		// for (DelayTask dt : nonConflictTask) {
		// invokeServiceFromTasks(dt);
		// // 记录日志
		// // businessBehaviorRecorder.recordPolicyEffect(dt);
		// }
		// } catch (Exception e) {
		// log.error(ExceptionLog.getErrorStack(e));
		// }
		// }
		// TODO:
		log.info("finish conflict slove: " + gxSessionId);
		// 业务行为记录
		// if (subUser != null && subUser.getId() != null) {
		// List<Long> effectPolicy = delayTaskAgency.getEffectPolicies();
		//
		// for (Long policy : effectPolicy) {
		// businessBehaviorRecorder.recordPolicyEffect(subUser.getId(),
		// policy);
		// }
		// }

	}

	private void invokeServiceFromTasks(DelayTask dt) {
		String interfaceName = RuleExecuteHelper.getInterfaceName(dt
				.getBusinessServiceName());
		String methodName = RuleExecuteHelper.getInterfaceMethod(dt
				.getBusinessServiceName());
		InterfaceModel i = ruleModelContainer.getInterfaceModels().get(
				interfaceName);
		i.getMethodAccess().invoke(i.getServiceBean(), methodName,
				dt.getParams().toArray());
	}

	/**
	 * 执行Drools
	 * 
	 * @param fact
	 *            要执行的事实
	 * @param knowledgeBaseId
	 *            String
	 * */
	public void fireAndExecute(Object fact, String knowledgeBaseId) {
		fireAndExecute(fact, knowledgeBaseId, null);
	}

	public void constructKnowledgeBases() {
		log.debug("RuleCore:constructKnowledgeBases - enter in.");

		System.setProperty("drools.dialect.mvel.strict", "false");

		if (knowledgeBaseDecls != null) {
			for (KnowledgeBaseDecl knowledgeBaseDecl : knowledgeBaseDecls) {
				constructSingleKnowledgeBase(knowledgeBaseDecl);
			}
		} else {
			log.info("RuleCore:constructKnowledgeBases----knowledgeBaseDecls is null!");
		}

		loadAllRules();

		log.debug("RuleCore:constructKnowledgeBases------end.");
	}

	private void loadAllRules() {
		log.debug("loadAllRules - enter in.");

		Iterator<PolicyGroup> policyGroups = null;
		Iterable<PolicyGroup> iterable = policyGroupDao.findAll();
		if (iterable == null) {
			log.info("loadAllRules------end. policyGroups is null.");
			return;
		} else {
			policyGroups = iterable.iterator();
		}

		if (policyGroups == null) {
			log.info("loadAllRules------end. policyGroups is null.");
			return;
		}

		while (policyGroups.hasNext()) {
			loadCategoryRules(policyGroups.next());
		}

		log.info("loadAllRules------end.");
	}

	/**
	 * 根据策略分组加载策略
	 * 
	 * @param policyGroup
	 *            PolicyGroup
	 * */
	private void loadCategoryRules(PolicyGroup policyGroup) {
		log.debug("loadCategoryRules--------------start.");
		List<PolicyWrapper> policies = new ArrayList<PolicyWrapper>(
				policyGroup.getPolicies());
		if (policies == null || policies.isEmpty()) {
			log.info("loadCategoryRules--------end. {} has no policy",
					policyGroup.getGroupName());

			return;
		}

		for (PolicyWrapper policyWrapper : policies) {
			updateSingleRule(policyGroup.getGroupName(), policyWrapper, true);
		}

		log.debug("loadCategoryRules--------end.");
	}

	/**
	 * 生成knowledgeBaseUnit
	 * */
	private void constructSingleKnowledgeBase(
			KnowledgeBaseDecl knowledgeBaseDecl) {
		log.debug("constructSingleKnowledgeBase-------------start.");

		knowledgeBaseDecl.setIdentifier(knowledgeBaseDecl.getIdentifier()
				.toUpperCase());

		String knowledgeBaseId = knowledgeBaseDecl.getIdentifier();

		if (knowledgeBaseId == null) {
			log.info("constructSingleKnowledgeBase-----end. knowledgeBaseDecl.getIdentifier() is null.");
			return;
		}

		KnowledgeBaseUnit knowledgeBaseUnit = knowledgeBases
				.get(knowledgeBaseId);
		if (knowledgeBaseUnit != null) {
			log.info("constructSingleKnowledgeBase-----end. knowledgeBaseUnit is not null.");
			return;
		} else {
			knowledgeBaseUnit = constructKnowledgeBaseUnit();

			knowledgeBaseUnit.setGlobles(knowledgeBaseDecl.getGlobles());
		}

		prepareGlobleObjects(knowledgeBaseUnit, knowledgeBaseDecl);

		log.info(
				"RuleCore:constructSingleKnowledgeBase - put knowledgeBase: {}",
				knowledgeBaseId);
		knowledgeBases.put(knowledgeBaseId, knowledgeBaseUnit);

		log.debug("constructSingleKnowledgeBase-----------end.");
	}

	private KnowledgeBaseUnit constructKnowledgeBaseUnit() {
		KnowledgeBaseUnit knowledgeBaseUnit = new KnowledgeBaseUnit();
		return knowledgeBaseUnit;
	}

	private void prepareGlobleObjects(KnowledgeBaseUnit knowledgeBaseUnit,
			KnowledgeBaseDecl knowledgeBaseDecl) {
		log.debug("prepareGlobleObjects--------------start.");
		List<String> globles = null;
		if (knowledgeBaseDecl.getGlobleDitto() != null) {
			KnowledgeBaseUnit baseUnit = knowledgeBases.get(knowledgeBaseDecl
					.getGlobleDitto());
			if (baseUnit == null) {
				log.info("prepareGlobleObjects------------end. baseUnit is null.");
				return;
			}

			globles = baseUnit.getGlobles();
		} else {
			globles = knowledgeBaseDecl.getGlobles();
		}

		ApplicationContext appContext = ApplicationContextGuardian
				.getInstance().GetAppContext();

		for (String globle : globles) {
			globle = Character.toLowerCase(globle.charAt(0))
					+ globle.substring(1);

			Object globleObject = appContext.getBean(globle);
			if (globleObject == null) {
				log.info("globle object {} dose not exist.", globleObject);

				continue;
			}

			knowledgeBaseUnit.addGlobleObject(globle, globleObject);
		}

		log.debug("prepareGlobleObjects-------------end.");
	}

	public List<KnowledgeBaseDecl> getKnowledgeBaseDecls() {
		return knowledgeBaseDecls;
	}

	public void setKnowledgeBaseDecls(List<KnowledgeBaseDecl> knowledgeBaseDecls) {
		this.knowledgeBaseDecls = knowledgeBaseDecls;
	}
}