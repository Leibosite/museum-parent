/**
 2015年1月20日
 14cells
 
 */
package com.qingruan.museum.api.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
	/**
	 * "yyyyMMdd HH:mm"
	 */
	public static final String DEFAULT_SHORT_DATE_TIME_FORMAT = "yyyyMMdd HH:mm";

	/**
	 * "yyyyMM"
	 */
	public static final String DEFAULT_SHORT_YEAR_MONTH_FORMAT = "yyyyMM";
	/**
	 * "yyyyMMdd"
	 */
	public static final String DEFAULT_SHORT_DATE_FORMAT = "yyyyMMdd";
	/**
	 * "yyyy-MM-dd"
	 */
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	/**
	 * "yyyy-MM-dd HH:mm"
	 */
	public static final String DEFAULT_DATETIME_FORMAT_EX = "yyyy-MM-dd HH:mm";
	/**
	 * "yyyy-MM-dd HH:mm:ss"
	 */
	public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * "yyyy-MM-dd HH:mm:ss"
	 */
	public static final String DEFAULT_INTEGER_FORMAT = "yyyyMMddHHmmss";
	/**
	 * "HH:mm:ss"
	 */
	public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

	public static final String BCD_TIME_FORMAT = "yyMMddHHmmss";

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static String currentTime() {
		SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
		return df.format(new Date());
	}

	public static String bcdTimeString() {
		SimpleDateFormat df = new SimpleDateFormat(BCD_TIME_FORMAT);
		return df.format(new Date());
	}

	public static Date currentDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);

		try {

			Date date = sdf.parse(DEFAULT_DATETIME_FORMAT);
			return date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
