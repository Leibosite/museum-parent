package com.qingruan.museum.engine.service.rule.action;

import java.util.List;

import com.qingruan.museum.engine.service.rule.DelayTask;
import com.qingruan.museum.engine.service.rule.DelayTaskAgency;

public interface BusinessService {
	/**
	 * 标记是否是需要异步执行的方法
	 */
	public boolean isAsynExecute();

	/**
	 * 执行具体业务逻辑
	 * 
	 * @param delayTask
	 * @param delayTaskAgency
	 * @param semaphore
	 */
	public void execute(DelayTask delayTask, DelayTaskAgency delayTaskAgency);

	/**
	 * 执行批处理
	 * 
	 * @param tasks
	 */
	public void executeBatchTasks(List<DelayTask> tasks);
}
