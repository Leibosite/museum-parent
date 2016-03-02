package com.qingruan.museum.admin.web.permission;

import java.io.IOException;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.qingruan.museum.admin.params.BusinessStaticParams;
import com.qingruan.museum.admin.service.permission.GroupManager;
import com.qingruan.museum.admin.service.permission.PermissionManager;
import com.qingruan.museum.admin.utils.RequestParamsUtil;
import com.qingruan.museum.admin.web.permission.form.AccessGroupQueryForm;
import com.qingruan.museum.dao.entity.AccessGroup;
import com.qingruan.museum.framework.util.ExceptionLogUtil;

@Slf4j
@Controller
@RequestMapping(value = "/system/group")
public class AccessGroupController {
	@Autowired
	private PermissionManager permissionManager;
	@Autowired
	private GroupManager groupManager;

	@Autowired
	private PermissionListEditor permissionSetEditor;

	@InitBinder
	public void initBinder(WebDataBinder b) {
		b.registerCustomEditor(Set.class, "accessPermissions",
				permissionSetEditor);
	}

	@RequestMapping(value = "/list")
	public String list(
			@ModelAttribute("queryForm") AccessGroupQueryForm queryForm,
			@RequestParam(required = false, value = "page") Integer page,
			Model model, HttpServletRequest request) {
	
		Page<AccessGroup> accessGroups;
		accessGroups = groupManager.getPaged(queryForm,
				RequestParamsUtil.getPageRequest(page));

		model.addAttribute("pageObjects", accessGroups);
		model.addAttribute("queryForm", queryForm);
		model.addAttribute("pageUrl", RequestParamsUtil.getCurrentURL(request));
	
		return "/system/group/list";
	}

	@RequestMapping(value = "/hasAssociation")
	public void hasAssociation(@RequestParam("groupId") Long groupId,
			HttpServletResponse response) {
		AccessGroup accessGroup = groupManager.getAccessGroup(groupId);

		try {
			if (accessGroup.getAccessPermissions().size() > 0) {
				response.getWriter().write(
						BusinessStaticParams.OPERATION_NOT_ALLOW);
			} else {
				response.getWriter()
						.write(BusinessStaticParams.OPERATION_ALLOW);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(ExceptionLogUtil.getErrorStack(e));
		}

	}

	@RequestMapping(value = "/create")
	public String create(Model model,HttpServletRequest request) {
	
		model.addAttribute("aGroup", new AccessGroup());
		model.addAttribute("allAPers",
				permissionManager.getAllAccessPermissions());
		model.addAttribute("action", "create");

		return "/system/group/edit";
	}

	@RequestMapping(value = "/edit/{id}")
	public String edit(@PathVariable("id") Long id, Model model,HttpServletRequest request) {
	
		AccessGroup accessGroup = groupManager.getAccessGroup(id);
		model.addAttribute("aGroup", accessGroup);
		model.addAttribute("allAPers",
				permissionManager.getAllAccessPermissions());
		model.addAttribute("action", "edit");
	
		return "/system/group/edit";
	}

	@RequestMapping(value = "/save")
	public String save(@ModelAttribute("accessGroup") AccessGroup accessGroup,HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
	
		groupManager.saveAccessGroup(accessGroup);
	
		return "redirect:list";
	}

	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id,HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
	
		groupManager.deleteAccessGroup(id);
	
		return "redirect:/system/group/list";
	}

}
