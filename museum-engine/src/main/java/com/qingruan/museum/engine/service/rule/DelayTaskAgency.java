package com.qingruan.museum.engine.service.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationContext;

import com.qingruan.museum.engine.exception.ExceptionLog;
import com.qingruan.museum.engine.framework.ApplicationContextGuardian;
import com.qingruan.museum.engine.service.rule.action.BusinessService;
import com.qingruan.museum.pma.meta.MethodModel;
import com.qingruan.museum.session.IpcanContext;

/**
 * 
 * @author tommy
 * 
 */
@Slf4j
public class DelayTaskAgency {
	public ArrayList<DelayTask> delayTasks;

	/**
	 * 第一个参数是冲突解决器， 第二个参数是需要冲突解决的任务集合
	 * 
	 */
	private Map<Class<?>, List<DelayTask>> conflictTasks;

	public Map<MethodModel, List<DelayTask>> conflicts;

	private Map<BusinessService, List<DelayTask>> batchTasks;

	@Setter
	@Getter
	private IpcanContext ipcanContext;

	@Setter
	@Getter
	private List<Long> effectPolicies;

	public static ApplicationContext applicationContext = ApplicationContextGuardian
			.getInstance().GetAppContext();

	// 业务日志记录
	// private static BusinessBehaviorRecorder businessBehaviorRecorder =
	// (BusinessBehaviorRecorder) applicationContext
	// .getBean(BusinessBehaviorRecorder.class);

	public DelayTaskAgency() {
		delayTasks = new ArrayList<DelayTask>();

		conflictTasks = new HashMap<Class<?>, List<DelayTask>>();

		batchTasks = new HashMap<BusinessService, List<DelayTask>>();

		effectPolicies = new ArrayList<Long>();

		conflicts = new HashMap<MethodModel, List<DelayTask>>();
	}

	public DelayTaskAgency(int capacity) {
		delayTasks = new ArrayList<DelayTask>();

		delayTasks.ensureCapacity(capacity);
	}

	public void addDelayTask(DelayTask delayTask) {
		delayTasks.add(delayTask);
	}

	public void addConflictTask(Class<?> clazz, DelayTask conflictTask) {
		log.debug("addConflictTask --------------------- start");

		List<DelayTask> conflicts = conflictTasks.get(clazz);
		if (conflicts == null) {
			conflicts = new ArrayList<DelayTask>();
			conflictTasks.put(clazz, conflicts);
		}

		/*
		 * 去重
		 */
		if (!taskExist(clazz, conflictTask)) {
			conflicts.add(conflictTask);
		}

		log.debug("addConflictTask --------------------- end");
	}

	public boolean taskExist(Class<?> clazz, DelayTask conflictTask) {
		List<DelayTask> conflicts = conflictTasks.get(clazz);

		return conflicts.contains(conflictTask);
	}

	public void addPatchTask(BusinessService businessService,
			DelayTask delayTask) {

		log.debug("addPatchTask --------------------- start");
		List<DelayTask> batchs = batchTasks.get(businessService);
		if (batchs == null) {
			batchs = new ArrayList<DelayTask>();
			batchTasks.put(businessService, batchs);
		}

		batchs.add(delayTask);
		log.debug("addPatchTask --------------------- end");
	}

	public void execute() {

		log.info("execute ------------------------------ start");

		/*
		 * 记录某用户，已匹配某策略
		 */
		//TODO
//		if (ipcanContext != null) {
//			log.debug("ipcanContext is not null.");
//
//			SubUser subUser = ipcanContext.getRuntimeInfo().getSubUser();
//
//			if (subUser != null && subUser.getId() != null) {
//				log.debug("subUserId is not null.");
//
//				businessBehaviorRecorder.recordPoliciesEffect(subUser.getId(),
//						effectPolicies);
//			}
//		}

		if (delayTasks.isEmpty()) {
			log.info("the delayTask is empty");
			return;
		}

		for (DelayTask delayTask : delayTasks) {
			BusinessService businessService = (BusinessService) applicationContext
					.getBean(delayTask.getBusinessServiceName());
			delayTask.setBusinessService(businessService);
			// TODO execute method
			delayTask.getBusinessService().execute(delayTask, this);
		}

		log.debug("execute ----------------------- end");
	}

	public void solveConflict() {
		log.info("solveConflict --------------------------- start");

		if (conflictTasks.isEmpty()) {
			log.info("conflictTasks is empty");

			return;
		}

		Iterator<Entry<Class<?>, List<DelayTask>>> iterator = conflictTasks
				.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<Class<?>, List<DelayTask>> conflict = iterator.next();
			solveSingleConflict(conflict.getKey(), conflict.getValue());
		}

		log.info("solveConflict --------------------------------- end");
	}

	private void solveSingleConflict(Class<?> solverClass, List<DelayTask> tasks) {
		// log.info("solveSingleConflict --------------------------- start");
		//
		// ConflictSolver conflictSolver = null;
		//
		// try {
		// conflictSolver = (ConflictSolver) applicationContext
		// .getBean(solverClass);
		// } catch (Exception e) {
		// ExceptionLog.logException(e, log);
		// }
		//
		// if (conflictSolver == null) {
		// log.info("conflictSolver whose class type is {} dose not exist.",
		// solverClass.getName());
		//
		// return;
		// }
		//
		// List<DelayTask> nonconflictTasks = conflictSolver.solve(tasks);
		//
		// /*
		// * 解决不同conflict方法处理了同一个task，保证一个task被加到批处理集合之后，不要重复加
		// */
		// for (DelayTask nonconflictTask : nonconflictTasks) {
		// if (nonconflictTask.isBatched()) {
		// continue;
		// }
		//
		// addPatchTask(nonconflictTask.getBusinessService(), nonconflictTask);
		// nonconflictTask.setBatched(true);
		// }
		//
		// log.info("solveSingleConflict --------------------------------- end");
	}

	public void executeBatchTasks() {

		log.info("executeBatchTasks --------------------------- start");
		if (batchTasks.isEmpty()) {
			log.info("batchTask is empty");
			log.info("executeBatchTasks --------------------------- end");
			return;
		}

		Iterator<Entry<BusinessService, List<DelayTask>>> iterator = batchTasks
				.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<BusinessService, List<DelayTask>> batchTask = iterator.next();

			executeSingleBatchTask(batchTask.getKey(), batchTask.getValue());
		}

		log.info("executeBatchTasks --------------------------------- end");
	}

	private void executeSingleBatchTask(BusinessService businessService,
			List<DelayTask> tasks) {
		log.info("executeSingleBatchTask --------------------------- start");

		try {
			businessService.executeBatchTasks(tasks);
		} catch (Exception e) {
			log.error(ExceptionLog.getErrorStack(e));
		}

		log.info("executeSingleBatchTask --------------------------------- end");
	}

	public void addEffectPolicy(Long policyId) {
		effectPolicies.add(policyId);
	}
}
