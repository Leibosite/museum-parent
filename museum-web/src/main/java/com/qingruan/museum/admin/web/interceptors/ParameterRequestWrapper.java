package com.qingruan.museum.admin.web.interceptors;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * request 包装类，用于修改请求参数
 * 
 * @author tommy
 * 
 */
public class ParameterRequestWrapper extends HttpServletRequestWrapper {
	private Map params = new HashMap();

	public ParameterRequestWrapper(HttpServletRequest request, Map newParams) { // 构造方法
		super(request);
		this.params = newParams;
	}

	public Map getParameterMap() {
		return params;
	}

	public Enumeration getParameterNames() {
		if(params != null){
			Vector l = new Vector(params.keySet());
			return l.elements();
		}else
			 return null;
		
	}

	public String[] getParameterValues(String name) {
		if(params == null)
			return null;
		Object v = params.get(name);
		if (v == null) {
			return null;
		} else if (v instanceof String[]) {
			return (String[]) v;
		} else if (v instanceof String) {
			return new String[] { (String) v };
		} else {
			return new String[] { v.toString() };
		}
	}

	public String getParameter(String name) {
		if (params == null)
			return null;
		Object v = params.get(name);
		if (v == null) {
			return null;
		} else if (v instanceof String[]) {
			String[] strArr = (String[]) v;
			if (strArr.length > 0) {
				return strArr[0];
			} else {
				return null;
			}
		} else if (v instanceof String) {
			return (String) v;
		} else {
			return v.toString();
		}
	}
}