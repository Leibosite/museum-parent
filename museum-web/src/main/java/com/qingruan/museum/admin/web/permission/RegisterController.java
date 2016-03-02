package com.qingruan.museum.admin.web.permission;

import java.io.PrintWriter;

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

import com.qingruan.museum.admin.service.permission.UserManager;
import com.qingruan.museum.admin.utils.RequestParamsUtil;
import com.qingruan.museum.admin.web.permission.form.AccessUserQueryForm;
import com.qingruan.museum.dao.entity.AccessUser;

@Controller
@RequestMapping(value = "/register")
public class RegisterController {
	
	@Autowired
	private UserManager userManager;
	
	/**
	 * 跳转到注册页面
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String registerForm() {
		return "register";
	}
	
	
	/**
	 * 检测用户名是否存在
	 * 
	 * @param request
	 */
	@RequestMapping(value = "/checkUser")
	public void checkUser(HttpServletRequest request,PrintWriter out) {
		String loginName = request.getParameter("loginName");
		AccessUser au = userManager.findUserByLoginName(loginName);
		System.out.println(au.getLoginName());
		if(au!=null){
			out.write("1");
		}
	}
	
	/**
	 * 注册，保存到数据库
	 * @param accessUser
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/save")
	public String save(@ModelAttribute("accessUser") AccessUser accessUser, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
	//TODO:加密密码
//		String encryptPass = AES.parseByte2HexStr(AES.encrypt(accessUser.getPassword()));
		accessUser.setPassword(accessUser.getPassword());
		userManager.saveAccessUser(accessUser);
		return "login";
	}
}
