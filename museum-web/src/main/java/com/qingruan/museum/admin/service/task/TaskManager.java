package com.qingruan.museum.admin.service.task;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.qingruan.museum.dao.entity.Task;
import com.qingruan.museum.dao.repository.TaskDao;
import com.qingruan.museum.framework.spring.data.jpa.util.SpecificationUtil;
/**
 * 定时任务
 * @author tommy
 *
 */
@Slf4j
@Component
@Transactional(readOnly = true)
public class TaskManager {

	@Autowired
	private TaskDao taskDao;

	// find all
	public List<Task> getAllTasks() {
		return (List<Task>) taskDao.findAll();
	}

	public List<Task> getAllByNameLikes(String name) {
		return taskDao.findByNameLike(name);
	}

	// query paged
	public Page<Task> getPaged(String taskName, Pageable pageable) {
		return taskDao.findAll(
				SpecificationUtil.like(Task.class, "name", taskName), pageable);
	}

	public Task getById(Long id) {

		return taskDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void deleteOne(Long id) {
		taskDao.delete(id);
		log.info("delete one {}", id);
	}

	@Transactional(readOnly = false)
	public void saveTask(Task task) {
		taskDao.save(task);
		log.info("save one {}", task.toString());
	}

	@Transactional(readOnly = false)
	public void disableTaskInfo(Long id) {
		Task task = taskDao.findOne(id);
		task.setStatus("0");
		taskDao.save(task);
		// taskDao.disableTask(id);
		log.info("disable one {}", task.toString());
	}

	@Transactional(readOnly = false)
	public void enableTaskInfo(Long id) {
		Task task = taskDao.findOne(id);
		task.setStatus("1");
		taskDao.save(task);
		log.info("enable one {}", task.toString());
		// taskDao.enableTask(id);
	}


}
