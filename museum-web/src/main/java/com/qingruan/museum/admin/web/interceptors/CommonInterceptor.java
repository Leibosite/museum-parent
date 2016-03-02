package com.qingruan.museum.admin.web.interceptors;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * ClassName:CommonInterceptor <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2014-3-27 下午3:15:13 <br/>
 * 
 * @author tommy
 * @version
 * @since JDK 1.7
 * @see
 */

public class CommonInterceptor implements HandlerInterceptor {

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

	/**
	 * 访问controller之前调用
	 * 
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object arg2) throws Exception {
		// 是返回页面的标识
		String urlType = request.getParameter("urlType");
		String[] types = request.getServletPath().split("/");
		String type = types[types.length - 2];
		if ("return".equals(urlType)) {
			request.removeAttribute("urlType");
			// 改变request参数
			Map<String, String[]> attributes = (Map<String, String[]>) request
					.getSession().getAttribute(type);
			HttpServletRequestWrapper mysw = new ParameterRequestWrapper(
					request, attributes);
			mysw.getRequestDispatcher(request.getServletPath()).forward(mysw,
					response);
			return false;
		} else {
			Map<String, String[]> mp = request.getParameterMap();
			Map<String, String[]> copymp = new HashMap<String, String[]>();
			if(mp != null)
				copymp.putAll(mp);
			request.getSession().setAttribute(type, copymp);
			return true;
		}
	}

}
