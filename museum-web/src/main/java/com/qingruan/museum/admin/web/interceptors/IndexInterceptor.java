/**
 * Project Name:pcrf-admin
 * File Name:CommonInterceptor.java
 * Package Name:com.baoyun.pcrf.admin.web.interceptors
 * Date:2014-3-27下午3:15:13
 *
 */

package com.qingruan.museum.admin.web.interceptors;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.SecurityUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.qingruan.museum.admin.service.permission.ShiroDbRealm.ShiroUser;

/**
 * ClassName:CommonInterceptor <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2014-3-27 下午3:15:13 <br/>
 * 
 * @author tommy
 * @version
 * @since JDK 1.6
 * @see
 */
@Slf4j
public class IndexInterceptor implements HandlerInterceptor {

//	@Autowired
//	private UserManager userManager;

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

		// 判断用户密码是否过期
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
//		AccessUser accessUser = userManager.getByLoginName(user.getLoginName());
//		Long timeStamp = accessUser.getPasswordModifyStamp();
		Long timeStamp = null;

		if (timeStamp == null) {
			request.getSession().setAttribute("modifyPassword", "true");
			return true;
		}
		// 当前时间
		Calendar curCalendar = Calendar.getInstance();

		// 获取上次修改时间,默认周期为90天
		Calendar latestModifyCalendar = Calendar.getInstance();
		latestModifyCalendar.setTimeInMillis(timeStamp);
		latestModifyCalendar.add(Calendar.DAY_OF_YEAR, 90);

		// 向前推一个周期,默认周期为90天
		Calendar preCalendar = Calendar.getInstance();
		preCalendar.setTimeInMillis(timeStamp);
		preCalendar.roll(Calendar.DAY_OF_YEAR, -90);

		// 如果密码周期超了，提醒修改密码
		if (latestModifyCalendar.before(curCalendar)
				|| preCalendar.after(curCalendar)) {
			request.getSession().setAttribute("modifyPassword", "true");
			log.info("密码将要失效，请及时修改");
		}
		return true;

	}
}
