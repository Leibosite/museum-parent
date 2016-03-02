package com.qingruan.museum.admin.web.pma;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.qingruan.museum.admin.service.common.SysLogService;
import com.qingruan.museum.admin.service.permission.PermissionManager;
import com.qingruan.museum.admin.service.pma.PolicyGroupManager;
import com.qingruan.museum.admin.utils.RequestParamsUtil;
import com.qingruan.museum.admin.web.pma.form.PolicyGroupQueryForm;
import com.qingruan.museum.dao.entity.AccessPermission;
import com.qingruan.museum.dao.entity.PolicyGroup;

@Controller
@RequestMapping("pma/policygroup")
public class PmaPolicyGroupController {
	@Autowired
	private PolicyGroupManager policyGroupManager;

	@Autowired
	private SysLogService sysLogService;

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PermissionManager permissionManager;

	@ModelAttribute("subPermission")
	public List<AccessPermission> getSubAccessPermissions() {
		return permissionManager.getSubPermissions("monitoring");

	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String create(ModelMap model) {
		sysLogService.operatingStartLog(request);
		model.put("policyGroup", new PolicyGroup());
		model.addAttribute("action", "create");
		sysLogService.operatingSuccessLog(request);
		return "/pma/policygroup/edit";
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(@ModelAttribute("policyGroup") PolicyGroup policyGroup,
			RedirectAttributes redirectAttributes) {
		sysLogService.operatingStartLog(request);
		policyGroupManager.savePolicyGroup(policyGroup);
		redirectAttributes.addFlashAttribute("message",
				"创建" + policyGroup.getGroupName() + "成功");
		sysLogService.operatingSuccessLog(request);
		return "redirect:list";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(
			@ModelAttribute("queryForm") PolicyGroupQueryForm queryForm,
			@RequestParam(required = false, value = "page") Integer page,
			Model model, HttpServletRequest request) {
		sysLogService.operatingStartLog(request);
		Page<PolicyGroup> policyGroups;
		if (page == null) {
			page = 1;
		}
		policyGroups = policyGroupManager.getPaged(queryForm.getPolicyGroup(),
				RequestParamsUtil.getPageRequest(page));

		model.addAttribute("pageObjects", policyGroups);
		model.addAttribute("queryForm", queryForm);
		model.addAttribute("pageUrl", RequestParamsUtil.getCurrentURL(request));
		sysLogService.operatingSuccessLog(request);
		return "pma/policygroup/list";
	}

	@RequestMapping("edit/{id}")
	public String edit(@PathVariable("id") Long id, Model model) {
		sysLogService.operatingStartLog(request);
		model.addAttribute("policyGroup", policyGroupManager.getPolicyGroup(id));
		model.addAttribute("action", "edit");
		sysLogService.operatingSuccessLog(request);
		return "pma/policygroup/edit";
	}

	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id,
			RedirectAttributes redirectAttributes) {
		sysLogService.operatingStartLog(request);
		policyGroupManager.deletePolicyGroup(id);
		redirectAttributes.addFlashAttribute("message", "删除成功");
		sysLogService.operatingSuccessLog(request);
		return "redirect:/pma/policygroup/list";
	}

}
