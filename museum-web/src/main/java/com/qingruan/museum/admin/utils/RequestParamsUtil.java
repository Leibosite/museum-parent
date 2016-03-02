package com.qingruan.museum.admin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qingruan.museum.framework.util.ExceptionLogUtil;

@Slf4j
public class RequestParamsUtil {

	public static ObjectMapper mapper = new ObjectMapper();

	public static PageRequest getPageRequest(Integer page) {
		if (page == null) {
			page = SysStaticParam.firstPage;
		} else {
			page = page - 1;
		}
		return new PageRequest(page, SysStaticParam.pageSize, Direction.ASC,
				"id");
	}
	public static PageRequest getPageRequestUpdateStamp(Integer page) {
		if (page == null) {
			page = SysStaticParam.firstPage;
		} else {
			page = page - 1;
		}
		return new PageRequest(page, SysStaticParam.pageSize, Direction.DESC,
				"updateStamp");
	}

	//
	// public static Page<?> GetPagedList(CommonService commonService, Integer
	// page) {
	// return commonService.getPagedList(getPageRequest(page));
	// }

	public static String getCurrentURL(HttpServletRequest request) {
		StringBuffer paramString = new StringBuffer("?");
		paramString.append("ajax=true");
		if(request.getParameterMap()!=null){
		Iterator it = request.getParameterMap().keySet().iterator();
		while (it.hasNext()) {
			String param = (String) it.next();
			if (param.equals("page") || param.equals("ajax"))
				continue;
			Object value = request.getParameterMap().get(param.toString());
			paramString.append("&" + param + "=" + ((String[]) value)[0]);

		}
	       }
		String currenturl = request.getRequestURI() + paramString;

		return currenturl;
	}

	public static final String DATE_FMT = "yyyy-MM-dd'T'hh:mm:ss";
	public static final String DATE_TIME="yyyy-MM-dd HH:mm:ss";//24 小时制
	public static final String DAY_FMT = "yyyy-MM-dd";
	public static final String TIME_FMT = "hh:mm:ss";

	public static Date getDateFromDayAndTime(String day, String time) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FMT);
			Date date;
			date = sdf.parse(day.trim() + "T" + time.trim());
			return date;
		} catch (ParseException e) {
			log.error(ExceptionLogUtil.getErrorStack(e));
			return null;
		}
	}

	public static String getDayStringFromDate(Date date) {

		SimpleDateFormat sdf = new SimpleDateFormat(DAY_FMT);
		return sdf.format(date);

	}

	public static String getTimeStringFromDate(Date date) {

		SimpleDateFormat sdf = new SimpleDateFormat(TIME_FMT);
		return sdf.format(date);

	}
	public static String getDateTimeStringFromDate(Date date) {

		SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME);
		return sdf.format(date);

	}
}
