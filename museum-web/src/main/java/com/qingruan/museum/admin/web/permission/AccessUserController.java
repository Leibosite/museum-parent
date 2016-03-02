package com.qingruan.museum.admin.web.permission;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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

import com.qingruan.museum.admin.service.permission.GroupManager;
import com.qingruan.museum.admin.service.permission.PermissionManager;
import com.qingruan.museum.admin.service.permission.UserManager;
import com.qingruan.museum.admin.utils.RequestParamsUtil;
import com.qingruan.museum.admin.web.permission.form.AccessUserQueryForm;
import com.qingruan.museum.dao.entity.AccessPermission;
import com.qingruan.museum.dao.entity.AccessUser;

@Slf4j
@Controller
@RequestMapping(value = "/system/user")
public class AccessUserController {
	@Autowired
	private UserManager userManager;

	@Autowired
	private GroupManager groupManager;

	@Autowired
	private GroupListEditor groupListEditor;
	@Autowired
	private PermissionManager permissionManager;

	@InitBinder
	public void initBinder(WebDataBinder b) {
		b.registerCustomEditor(Set.class, "accessGroups", groupListEditor);
	}
	@ModelAttribute("subPermission")
	public List<AccessPermission> getSubAccessPermissions() {
		return permissionManager.getSubPermissions("system");
				
	}
	@RequestMapping(value = "/list")
	public String list(
			@ModelAttribute("queryForm") AccessUserQueryForm queryForm,
			@RequestParam(required = false, value = "page") Integer page,
			Model model, HttpServletRequest request) {
		
		Page<AccessUser> accessUsers;
		accessUsers = userManager.getPaged(queryForm,
				RequestParamsUtil.getPageRequest(page));
		
		model.addAttribute("pageObjects", accessUsers);
		model.addAttribute("queryForm", queryForm);
		model.addAttribute("pageUrl", RequestParamsUtil.getCurrentURL(request));
	   System.out.println("no restart");
		return "/system/user/list";
	}

	@RequestMapping(value = "/create")
	public String create(Model model, HttpServletRequest request) {
	
		model.addAttribute("user", new AccessUser());
		model.addAttribute("allGroups", groupManager.getAllAccessGroups());
		model.addAttribute("action", "create");
	
		return "/system/user/edit";
	}

	@RequestMapping(value = "/edit/{id}")
	public String edit(@PathVariable("id") Long id, Model model, HttpServletRequest request){
	
		AccessUser accessUser = userManager.getAccessUser(id);
		try{
			//TODO:解密密码
//			String decrypt = new String(AES.decrypt(AES.parseHexStr2Byte(accessUser.getPassword())));
			accessUser.setPassword(accessUser.getPassword());
		}catch (Exception e) {
		
			log.error(e.getMessage());
		}
		model.addAttribute("user", accessUser);
		model.addAttribute("allGroups", groupManager.getAllAccessGroups());
		model.addAttribute("action", "edit");
	
		return "/system/user/edit";
	}

	@RequestMapping(value = "/save")
	public String save(@ModelAttribute("accessUser") AccessUser accessUser, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
	//TODO:加密密码
//		String encryptPass = AES.parseByte2HexStr(AES.encrypt(accessUser.getPassword()));
		accessUser.setPassword(accessUser.getPassword());
		userManager.saveAccessUser(accessUser);
	
		return "redirect:list";
	}

	@RequestMapping(value = "delete/{id}")
	public String delete(@PathVariable("id") Long id, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
	
		userManager.deleteAccessUser(id);
	
		return "redirect:/system/user/list";
	}

}
