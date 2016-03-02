package com.qingruan.museum.admin.web.permission;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.qingruan.museum.admin.utils.CommonUtils;
/**
 * 
 * @author tommy
 *
 */
@Controller
@Slf4j
public class LoginController {

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login(HttpServletRequest request) {
		String ip = CommonUtils.getIpAddr(request);
		log.info("[] on museum_admin({}) authenticate starting......", ip);
		return "login";
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String fail(
			@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String userName,
			Model model,HttpServletRequest request) {
		String ip = CommonUtils.getIpAddr(request);
		log.info("{} on museum_admin({}) authenticate failed.",userName,ip);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM,
				userName);
		return "login";
	} 
}
