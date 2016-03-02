package com.qingruan.museum.admin.web.alarm.history;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.qingruan.museum.admin.service.alarm.history.HistoryAlarmManager;
import com.qingruan.museum.admin.service.common.SysLogService;
import com.qingruan.museum.admin.utils.RequestParamsUtil;
import com.qingruan.museum.admin.web.alarm.history.form.AlarmQueryForm;
import com.qingruan.museum.dao.entity.Alarm;

/**
 * 历史告警
 * 
 * @author tommy
 * 
 */
@Controller
@RequestMapping("/monitor/alarm/history")
public class HistoryAlarmController {
	@Autowired
	private HistoryAlarmManager historyAlarmManager;
	@Autowired
	private SysLogService sysLogService;

	@Autowired
	private HttpServletRequest request;
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(
			@ModelAttribute("queryForm") AlarmQueryForm alarmQueryForm,
			@RequestParam(required = false, value = "page") Integer page,
			Model model, HttpServletRequest request) {
		sysLogService.operatingStartLog(request);

		Page<Alarm> policys = historyAlarmManager.getPaged(
				alarmQueryForm.getName(), alarmQueryForm.getStatus(),
				alarmQueryForm.getStartTime(), alarmQueryForm.getEndTime(),
				RequestParamsUtil.getPageRequest(page));
		model.addAttribute("pageObjects", policys);
		model.addAttribute("pageUrl", RequestParamsUtil.getCurrentURL(request));

		model.addAttribute("queryForm", alarmQueryForm);
		model.addAttribute("page", page == null ? 1 : page);

		sysLogService.operatingSuccessLog(request);
		return "monitor/historyalarm/list";
	}

	@RequestMapping(value = "/delete")
	public String deleteAlarm(RedirectAttributes redirectAttributes,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "id", required = true) Long id) {
		sysLogService.operatingStartLog(request);
		historyAlarmManager.deleteAlarm(id);
		redirectAttributes.addFlashAttribute("message", "删除成功");
		sysLogService.operatingSuccessLog(request);
		return "redirect:/monitor/alarm/history/list";
	}


	@RequestMapping(value = "disable")
	public String disable(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "id") Long id,
			RedirectAttributes redirectAttributes) {
		sysLogService.operatingStartLog(request);
		if (id != null) {
			historyAlarmManager.disable(id);
			sysLogService.operatingSuccessLog(request);
		}
		return "redirect:/monitor/alarm/history/list";
	}

	@RequestMapping(value = "restore")
	public String enable(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "id") Long id,
			RedirectAttributes redirectAttributes) {
		sysLogService.operatingStartLog(request);
		if (id != null) {
			historyAlarmManager.enable(id);
			sysLogService.operatingSuccessLog(request);
		}
		return "redirect:/monitor/alarm/history/list";
	}
}
