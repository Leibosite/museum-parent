package com.qingruan.museum.admin.web.sms;

import javax.servlet.http.HttpServletRequest;

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

import com.qingruan.museum.admin.service.common.SysLogService;
import com.qingruan.museum.admin.service.sms.SmsTemplateManager;
import com.qingruan.museum.admin.utils.RequestParamsUtil;
import com.qingruan.museum.admin.web.sms.form.TemplateQueryForm;
import com.qingruan.museum.dao.entity.SmsTemplate;

@Controller
@RequestMapping("/sms/template")
public class SMSTemplateController {

	@Autowired
	private SmsTemplateManager templateManager;
	
	@Autowired
	private SysLogService sysLogService;
	
	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(
			@ModelAttribute("queryForm") TemplateQueryForm queryForm,
			@RequestParam(required = false, value = "page") Integer page,
			Model model, HttpServletRequest request) {
		sysLogService.operatingStartLog(request);
		Page<SmsTemplate> infos = templateManager.getPaged(queryForm.getTemplate(),
				RequestParamsUtil.getPageRequest(page));

		model.addAttribute("pageObjects", infos);
		model.addAttribute("queryForm", queryForm);
		model.addAttribute("pageUrl", RequestParamsUtil.getCurrentURL(request));
		sysLogService.operatingSuccessLog(request);
		return "/sms/template/list";
	}

	@RequestMapping(value = "edit")
	public String edit(@RequestParam(value = "id", required = false) Long id, Model model) {
		sysLogService.operatingStartLog(request);
		if (id != null) {
			model.addAttribute("template", templateManager.getOne(id));
			model.addAttribute("action", "edit");
		}
		sysLogService.operatingSuccessLog(request);
		return "sms/template/editBaseInfo";
	}

	@RequestMapping(value = "create")
	public String create(Model model) {
		sysLogService.operatingStartLog(request);
		model.addAttribute("template", new SmsTemplate());
		model.addAttribute("action", "create");
		sysLogService.operatingSuccessLog(request);
		return "sms/template/editBaseInfo";
	}

	@RequestMapping(value = "save")
	public String save(@ModelAttribute("template") SmsTemplate template,
			RedirectAttributes redirectAttributes) {
		sysLogService.operatingStartLog(request);
		templateManager.save(template);
		sysLogService.operatingSuccessLog(request);
		return "redirect:list";
		
	}

	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id,
			RedirectAttributes redirectAttributes) {
		sysLogService.operatingStartLog(request);
		if (id != null) {
			templateManager.delete(id);
		}
		sysLogService.operatingSuccessLog(request);
		return "redirect:/sms/template/list";
	}

}
