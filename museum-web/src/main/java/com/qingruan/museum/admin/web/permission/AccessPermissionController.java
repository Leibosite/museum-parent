package com.qingruan.museum.admin.web.permission;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qingruan.museum.admin.service.permission.PermissionManager;
import com.qingruan.museum.admin.utils.RequestParamsUtil;
import com.qingruan.museum.admin.web.permission.form.PermissionQueryForm;
import com.qingruan.museum.dao.entity.AccessPermission;

@Controller
@RequestMapping(value = "/system/permission")
public class AccessPermissionController {
	@Autowired
	private PermissionManager permissionManager;

	@RequestMapping(value = "/list")
	public String list(
			@ModelAttribute("queryForm") PermissionQueryForm queryForm,
			@RequestParam(required = false, value = "page") Integer page,
			Model model, HttpServletRequest request) {
	
		Page<AccessPermission> accessPermissions = permissionManager.getPaged(
				queryForm, RequestParamsUtil.getPageRequest(page));

		model.addAttribute("pageObjects", accessPermissions);
		model.addAttribute("queryForm", queryForm);
		model.addAttribute("pageUrl", RequestParamsUtil.getCurrentURL(request));
	
		return "/system/permission/list";
	}

	@RequestMapping(value = "/welcome")
	public String welcome(
			@ModelAttribute("queryForm") PermissionQueryForm queryForm,
			@RequestParam(required = false, value = "page") Integer page,
			Model model, HttpServletRequest request) {
	
		if (request.getParameterMap().size() > 0) {
			model.addAttribute("hasParams", true);
			Page<AccessPermission> accessPermissions = permissionManager
					.getPaged(queryForm, RequestParamsUtil.getPageRequest(page));

			model.addAttribute("pageObjects", accessPermissions);
			model.addAttribute("queryForm", queryForm);
			model.addAttribute("pageUrl",
					RequestParamsUtil.getCurrentURL(request));

		}
	
		return "/system/permission/welcome";
	}

	@RequestMapping(value = "searchList")
	public String searchList(
			@ModelAttribute("queryForm") PermissionQueryForm queryForm,
			@RequestParam(required = false, value = "page") Integer page,
			Model model, HttpServletRequest request) {
	
		Page<AccessPermission> accessPermissions = permissionManager.getPaged(
				queryForm, RequestParamsUtil.getPageRequest(page));

		model.addAttribute("pageObjects", accessPermissions);
		model.addAttribute("queryForm", queryForm);
		model.addAttribute("pageUrl", RequestParamsUtil.getCurrentURL(request));
	
		return "/system/permission/searchList";
	}

	@RequestMapping(value = "fetchPermissionList")
	public void fetchPermissionList(
			@RequestParam(value = "term", required = false) String name, HttpServletRequest request,
			HttpServletResponse response) throws JsonGenerationException,
			JsonMappingException, IOException {
	
		name = name + "%";
		PermissionQueryForm form = new PermissionQueryForm();
		form.setDisplayname(name);
		List<AccessPermission> a_list = permissionManager
				.getAccessPermissionsByNameLike(name);
		Map<Long, String> values = new HashMap<Long, String>();
		for (AccessPermission a : a_list) {
			values.put(a.getId(), a.getDisplayname());
		}
		ObjectMapper mapper = new ObjectMapper();
		String responseString = mapper.writeValueAsString(values);
		response.getWriter().write(responseString);
		
	}

	@RequestMapping(value = "/create")
	public String create(Model model,HttpServletRequest request) {
	
		model.addAttribute("aPer", new AccessPermission());
		model.addAttribute("action", "create");
	
		return "/system/permission/edit";
	}

	@RequestMapping(value = "/edit/{id}")
	public String edit(@PathVariable("id") Long id, Model model,HttpServletRequest request) {
		
		AccessPermission accessPermission = permissionManager
				.getAccessPermission(id);
		model.addAttribute("aPer", accessPermission);
		model.addAttribute("action", "edit");

		return "/system/permission/edit";
	}

	@RequestMapping(value = "/save")
	public String save(
			@ModelAttribute("accessPermission") AccessPermission accessPermission,
			RedirectAttributes redirectAttributes,HttpServletRequest request) {
	
		if (accessPermission.getSuperPermission().getId() == null) {
			accessPermission.setSuperPermission(null);
		}
		permissionManager.saveAccessPermission(accessPermission);
	
		return "redirect:/system/permission/list";
	}

	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id,HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
	
		permissionManager.deleteAccessPermission(id);

		return "redirect:/system/permission/list";
	}

}
