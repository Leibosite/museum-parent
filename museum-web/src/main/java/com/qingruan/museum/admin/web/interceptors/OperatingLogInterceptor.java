package com.qingruan.museum.admin.web.interceptors;
/**
 * Project Name:pcrf-admin
 * File Name:CommonInterceptor.java
 * Package Name:com.baoyun.pcrf.admin.web.interceptors
 * Date:2014-3-27下午3:15:13
 *
 *//*

package com.baoyun.pcrf.admin.web.interceptors;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.SecurityUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.baoyun.pcrf.admin.common.CommonUtils;
import com.baoyun.pcrf.admin.permission.ShiroDbRealm.ShiroUser;
import com.baoyun.pcrf.admin.web.util.RequestParamsUtil;

*//**
 * 为了增加操作维护日志的拦截器。
 * 
 * @author brad
 * 
 *//*
@Slf4j
public class OperatingLogInterceptor implements HandlerInterceptor {
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {

	}

	*//**
	 * 访问controller之前调用
	 * 
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object)
	 *//*
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object arg2) throws Exception {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		String ip = CommonUtils.getIpAddr(request);
		String url = RequestParamsUtil.getCurrentURL(request);
		Map<String, String[]> map = request.getParameterMap();
		Set<Entry<String, String[]>> sets=map.entrySet();
		String params = "";
		for (Entry<String, String[]> entry : sets) {
			params = params + entry.getKey()+":"+entry.getValue()+ ",";
		}
		
		 * $user on pcrf_admin(ip) authenticate starting...... $user on
		 * pcrf_admin(ip) authenticate successfully/failed. (The reason is xxx)
		 * $user on pcrf_admin(ip) execute add/show/mo/show_xxx parameters
		 * starting...... $user on pcrf_admin(ip) execute add/show/mo/show_xxx
		 * parameters successfully/failed. (The reason is xxx)
		 
		if (user != null) {
			
			 * log.info("{} on pcrf_admin({}) authenticate starting......",
			 * user.getLoginName() + "-" + user.getName(), ip);
			 
			String urll="";
			if(url.contains("?")){
				urll=url.split("\\?")[0];
			}
			log.info("{} on pcrf_admin({}) execute {} [{}] starting......",
					user.getLoginName(), ip, urll, params);
		}

		return true;
	}
}
*/