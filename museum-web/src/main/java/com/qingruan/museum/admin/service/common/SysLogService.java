package com.qingruan.museum.admin.service.common;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import com.qingruan.museum.admin.service.permission.ShiroDbRealm.ShiroUser;
import com.qingruan.museum.admin.utils.CommonUtils;
import com.qingruan.museum.admin.utils.RequestParamsUtil;
/**
 * 日志
 * @author tommy
 *
 */
@Service
@Slf4j
public class SysLogService {
	private String SUCCESS = "successfully";
	private String FAILED = "failed";

	public void operatingStartLog(HttpServletRequest request) {
		Map<String, String[]> map = request.getParameterMap();
		String params = "{";
		if (map != null) {
			Set<Entry<String, String[]>> sets = map.entrySet();

			for (Entry<String, String[]> entry : sets) {
				String value = "[";
				for (String v : entry.getValue()) {
					value = value + v + ",";

				}
				value = value + "]";
				params = params + entry.getKey() + ":" + value + ",";
			}

		}
		params = params + "}";
		String url = RequestParamsUtil.getCurrentURL(request);
		String urll = "";
		if (url.contains("?")) {
			urll = url.split("\\?")[0];
		}
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		String ip = CommonUtils.getIpAddr(request);
		log.info("{} on museum_admin({}) execute {} {} starting......",
				user.getLoginName(), ip, urll, params);
	}

	private void operatingResultLog(HttpServletRequest request, String result,
			String reason) {
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		Map<String, String[]> map = request.getParameterMap();
		String ip = CommonUtils.getIpAddr(request);
		String params = "{";
		if (map != null) {
			Set<Entry<String, String[]>> sets = map.entrySet();
			for (Entry<String, String[]> entry : sets) {
				String value = "[";
				for (String v : entry.getValue()) {
					value = value + v + ",";
				}
				value = value + "]";
				params = params + entry.getKey() + ":" + value + ",";
			}

		}
		params = params + "}";
		String url = RequestParamsUtil.getCurrentURL(request);
		String urll = "";
		if (url.contains("?")) {
			urll = url.split("\\?")[0];
		}
		if (SUCCESS.equals(result)) {
			log.info("{} on museum_admin({}) execute {} {} {}. ",
					user.getLoginName(), ip, urll, params, result);
		}
		if (FAILED.equals(result)) {
			log.info(
					"{} on museum_admin({}) execute {} {} {}. (The reason is {})",
					user.getLoginName(), ip, urll, params, result, reason);
		}
	}

	public void operatingSuccessLog(HttpServletRequest request) {
		operatingResultLog(request, "successfully", "");
	}

	public void operatingFailLog(HttpServletRequest request, String reason) {
		operatingResultLog(request, "failed", reason);
	}

}
