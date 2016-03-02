package com.qingruan.museum.admin.web.pma;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.qingruan.museum.admin.service.common.SysLogService;
import com.qingruan.museum.admin.service.pma.PolicyGroupManager;
import com.qingruan.museum.admin.service.pma.PolicyManager;
import com.qingruan.museum.admin.utils.RequestParamsUtil;
import com.qingruan.museum.admin.web.pma.form.PolicyQueryForm;
import com.qingruan.museum.framework.jackson.JSONUtil;
import com.qingruan.museum.pma.model.Constraint;
import com.qingruan.museum.pma.model.Execution;
import com.qingruan.museum.pma.model.Policy;

@Controller
@RequestMapping("pma/policy")
public class PmaPolicyController {
	@Autowired
	private PolicyManager policyManager;

	@Autowired
	private PolicyGroupManager policyGroupManager;
	
	@Autowired
	private SysLogService sysLogService;
	
	@Autowired
	private HttpServletRequest request;

	@RequestMapping(value = "show/{groupId}")
	public String show(@PathVariable("groupId") Long groupId,
			@ModelAttribute("queryForm") PolicyQueryForm queryForm,
			@RequestParam(required = false, value = "page") Integer page,
			Model model, HttpServletRequest request) {
		sysLogService.operatingStartLog(request);
		if (groupId != null) {
			// List<Policy> resultList =
			// policyManager.getByPolicyGroupId(groupId);
			// Page<Policy> policy;
			// if (resultList.size() > 0) {
			// policy = new PageImpl<Policy>(resultList,
			// RequestParamsUtil.getPageRequest(page),
			// resultList.size());
			// } else {
			// policy = new PageImpl<Policy>(resultList);
			// }
			Page<Policy> policys = policyManager.getPaged(groupId,
					queryForm.getPolicyName(), queryForm.getStatus(),
					queryForm.getStartTime(), queryForm.getEndTime(),
					RequestParamsUtil.getPageRequest(page));
			model.addAttribute("pageObjects", policys);
			model.addAttribute("pageUrl",
					RequestParamsUtil.getCurrentURL(request));
			model.addAttribute("name",
					policyGroupManager.getPolicyGroup(groupId).getGroupName());
			model.addAttribute("queryForm", queryForm);
			model.addAttribute("page",page == null ? 1 : page);
			model.addAttribute("id", groupId);
		}
		sysLogService.operatingSuccessLog(request);
		return "pma/policygroup/listPolicy";
	}

	// ====================================================================
	// add policy
	@RequestMapping(value = "addPolicy")
	public String addPolicy(@RequestParam(value = "groupId") Long groupId,
			@RequestParam(value = "id") Long id,String page, Model model) {
		sysLogService.operatingStartLog(request);
		if (id != null) {
			Policy p = policyManager.getPolicy(id);
			model.addAttribute("policy", p);
			model.addAttribute("name",
					policyGroupManager.getPolicyGroup(groupId).getGroupName());
			model.addAttribute("policyJson", JSONUtil.serialize(p));
		} else {
			model.addAttribute("policy", new Policy());
			model.addAttribute("name",
					policyGroupManager.getPolicyGroup(groupId).getGroupName());
		}
		model.addAttribute("groupId", groupId);
		model.addAttribute("page", page);
		sysLogService.operatingSuccessLog(request);
		return "pma/policygroup/editPolicy";
	}

	@RequestMapping(value = "savePolicy")
	public String savePolicy(@ModelAttribute("policy") Policy policy,
			RedirectAttributes redirectAttributes,
			@RequestParam(value = "groupId", required = false) Long groupId,
			@RequestParam(value = "page", required = false) Integer page) {
		sysLogService.operatingStartLog(request);
		validatePolicy(policy);

		// save
		policyManager.savePolicy(policy);
		System.out.println("保存到数据库中的Policy Json："+ JSONUtil.serialize(policy));
		sysLogService.operatingSuccessLog(request);
		return "redirect:/pma/policy/show/" + groupId + "?page=" + page;
	}

	@RequestMapping(value = "deletePolicy")
	public String deletePolicy(RedirectAttributes redirectAttributes,
			@RequestParam(value = "groupId", required = true) Long groupId,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "id", required = true) Long id) {
		sysLogService.operatingStartLog(request);
		policyManager.deletePolicy(id);
		redirectAttributes.addFlashAttribute("message", "删除成功");
		sysLogService.operatingSuccessLog(request);
		return "redirect:/pma/policy/show/" + groupId+"?page="+page;
	}

	private void validatePolicy(Policy policy) {
		List<Constraint> cList = new ArrayList<Constraint>();
		for (Constraint c : policy.getConstraints()) {
			if (StringUtils.isNotBlank(c.getName())) {
				cList.add(c);
			}
		}
		policy.setConstraints(cList);

		List<Execution> eList = new ArrayList<Execution>();
		for (Execution e : policy.getExecutions()) {
			if (StringUtils.isNotBlank(e.getName())) {
				eList.add(e);
			}
		}
		policy.setExecutions(eList);
	}

	@RequestMapping(value = "disable")
	public String disable(@RequestParam(value = "groupId") Long groupId,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "id") Long id,
			RedirectAttributes redirectAttributes) {
		sysLogService.operatingStartLog(request);
		if (id != null) {
			policyManager.disable(id);
			sysLogService.operatingSuccessLog(request);
		}
		return "redirect:/pma/policy/show/" + groupId+"?page="+page;
	}

	@RequestMapping(value = "restore")
	public String enable(@RequestParam(value = "groupId") Long groupId,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "id") Long id,
			RedirectAttributes redirectAttributes) {
		sysLogService.operatingStartLog(request);
		if (id != null) {
			policyManager.enable(id);
			sysLogService.operatingSuccessLog(request);
		}
		return "redirect:/pma/policy/show/" + groupId+"?page="+page;
	}
}
