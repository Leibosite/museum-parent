package com.qingruan.museum.admin.web.task;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.drools.core.time.impl.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.qingruan.museum.admin.params.BusinessStaticParams;
import com.qingruan.museum.admin.service.common.SysLogService;
import com.qingruan.museum.admin.service.task.TaskManager;
import com.qingruan.museum.admin.utils.RequestParamsUtil;
import com.qingruan.museum.admin.web.task.form.TaskQueryForm;
import com.qingruan.museum.dao.entity.Task;
import com.qingruan.museum.framework.util.ExceptionLogUtil;

@Slf4j
@Controller
@RequestMapping("/pma/task")
public class TaskController {
	public static final int DELETE_TIMER = 0;
	public static final int ADD_TIMER = 1;

	@Autowired
	private TaskManager taskManager;

	@Autowired
	private SysLogService sysLogService;
	
	@Autowired
	private HttpServletRequest request;
	

//	@Autowired
//	private RarScheduleService taskScheduleService;

	boolean isCreate;

	@ModelAttribute("statusMap")
	public Map<String, String> getStatusMap() {
		return BusinessStaticParams.getStatusMap();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(@ModelAttribute("queryForm") TaskQueryForm queryForm,
			@RequestParam(required = false, value = "page") Integer page,
			Model model, HttpServletRequest request) {
		sysLogService.operatingStartLog(request);
		Page<Task> taskInfos = taskManager.getPaged(queryForm.getName(),
				RequestParamsUtil.getPageRequest(page));
		model.addAttribute("pageObjects", taskInfos);
		model.addAttribute("queryForm", queryForm);
		model.addAttribute("pageUrl", RequestParamsUtil.getCurrentURL(request));
		sysLogService.operatingSuccessLog(request);
		return "pma/task/list";
	}

	@RequestMapping(value = "/create")
	public String create(Model model) {
		sysLogService.operatingStartLog(request);
		model.addAttribute("task", new Task());
		model.addAttribute("action", "create");
//		model.addAttribute("allSubUserFilters", subUserFilterManager.getAll());
		isCreate = true;
		sysLogService.operatingSuccessLog(request);
		return "/pma/task/edit";
	}

	@RequestMapping(value = "/edit/{id}")
	public String edit(@PathVariable("id") Long id, Model model) {
		sysLogService.operatingStartLog(request);
		Task task = taskManager.getById(id);
		model.addAttribute("task", task);
		model.addAttribute("action", "edit");
//		model.addAttribute("allSubUserFilters", subUserFilterManager.getAll());

		isCreate = false;
		sysLogService.operatingSuccessLog(request);
		return "/pma/task/edit";
	}

	@RequestMapping(value = "/save")
	public String save(@ModelAttribute("task") Task task,
			RedirectAttributes redirectAttributes) {
		sysLogService.operatingStartLog(request);
//		if (task.getSubUserFilter() != null
//				&& task.getSubUserFilter().getId() == null) {
//			task.setSubUserFilter(null);
//		}

//		if (isCreate == false) {
//			taskScheduleService.remove(task.getId());
//		}

		// 非签约用户
//		if (task.getSubUserFilter().getId() == -1) {
//			task.setSubUserFilter(null);
//			task.setIsForNoSubscribe(1);
//		} else {
//			task.setIsForNoSubscribe(null);
//			task.setSessionReleaseCause(null);
//		}

		taskManager.saveTask(task);
		if (task.getStatus().equals("1"))
//			taskScheduleService.addScheduleIntoTask(task.getId());
		sysLogService.operatingSuccessLog(request);
		return "redirect:list";
	}

	@RequestMapping(value = "/delete/{id}")
	public String delete(@PathVariable("id") Long id,
			RedirectAttributes redirectAttributes) {
		sysLogService.operatingStartLog(request);
		if (id != null) {
			taskManager.deleteOne(id);
//			taskScheduleService.remove(id);
		}
		sysLogService.operatingSuccessLog(request);
		return "redirect:/pma/task/list";
	}

	@RequestMapping(value = "disable/{id}")
	public String disable(@PathVariable("id") Long id,
			RedirectAttributes redirectAttributes) {
		sysLogService.operatingStartLog(request);
		if (id != null) {
			Task task = taskManager.getById(id);

			if (task.getStatus().equals("1")) {
				taskManager.disableTaskInfo(id);
//				taskScheduleService.remove(id);
			}
		}
		sysLogService.operatingSuccessLog(request);
		return "redirect:/pma/task/list";
	}

	@RequestMapping(value = "restore/{id}")
	public String enable(@PathVariable("id") Long id,
			RedirectAttributes redirectAttributes) {
		sysLogService.operatingStartLog(request);
		if (id != null) {
			Task task = taskManager.getById(id);

			if (task.getStatus().equals("0")) {
				taskManager.enableTaskInfo(id);
//				taskScheduleService.addScheduleIntoTask(id);
			}
		}
		sysLogService.operatingSuccessLog(request);
		return "redirect:/pma/task/list";
	}

	@RequestMapping(value = "/cronCheck", method = RequestMethod.POST)
	public void checkTimmer(HttpServletResponse response,
			@RequestParam(value = "cronExpress") String timmer) {
		sysLogService.operatingStartLog(request);
		try {
			if (timmer != null && StringUtils.isNotBlank(timmer)) {
				response.getWriter().print(
						CronExpression.isValidExpression(timmer));
			} else {
				response.getWriter().print(true);
			}
			sysLogService.operatingSuccessLog(request);
		} catch (Exception e) {
			sysLogService.operatingFailLog(request, e.getMessage());
			log.error(ExceptionLogUtil.getErrorStack(e));
		}
	}

}
