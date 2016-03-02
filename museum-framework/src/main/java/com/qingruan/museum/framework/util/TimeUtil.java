/**
 2015年2月4日
 14cells
 
 */
package com.qingruan.museum.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.qingruan.museum.framework.util.GlobalParameter.TimeFormat;

/**
 * @author Tommy
 * 
 */
public class TimeUtil {
	
	/**
	 * "yyyyMMdd HH:mm"
	 */
	public static final String DEFAULT_SHORT_DATE_TIME_FORMAT = "yyyyMMdd HH:mm";

	/**
	 * "yyyyMM"
	 */
	public static final String DEFAULT_SHORT_YEAR_MONTH_FORMAT = "yyyy-MM";
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
	
	public static final String SPLIT_TIME_FORMAT = "yy-MM-dd-HH-mm-ss";
	public static final String DEFAULT_SHORT_YEAR_FORMAT = "yyyy";

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	
	
	/**
	 * 返回至1970年的毫秒数时间,如返回<2015-01-18 21:52:03>
	 * 
	 * @param timeStamp
	 * @return
	 */
	public static String getStringTime(Long timeStamp, TimeFormat format) {
		if (timeStamp == null)
			return null;
		else {
			String val = "";

			switch (format) {
			case YEAR:
				val = "yyyy";
				break;

			case YEAR_MONTH:
				val = "yyyy-MM";
				break;
			case YEAR_MONTH_DAY:
				val = "yyyy-MM-dd";
				break;
			case YEAR_MONTH_DAY_HOUR:
				val = "yyyy-MM-dd HH";
				break;
			case YEAR_MONTH_DAY_HOUR_MIN:
				val = "yyyy-MM-dd HH:mm";
				break;
			case YEAR_MONTH_DAY_HOUR_MIN_SEC:
				val = "yyyy-MM-dd HH:mm:ss";
				break;
			case DAY:
				val = "dd";
				break;
			case HOUR:
				val = "HH";
				break;
			case MONTH:
				val = "MM";
				break;
			default:
				break;
			}

			SimpleDateFormat sdf = new SimpleDateFormat(val);
			return sdf.format(timeStamp);
		}

	}
	
	public static String currentTime() {
		SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
		return df.format(new Date());
	}

	public static String bcdTimeString() {
		SimpleDateFormat df = new SimpleDateFormat(BCD_TIME_FORMAT);
		return df.format(new Date());
	}
	
	
	public static long timestampByHourMinuteSecond(String time){
		
//		StringBuilder builder = new StringBuilder();
//		String bcdTime = bcdTimeString();
//		builder.append(bcdTime.substring(0, 6));
//		builder.append(time);
		SimpleDateFormat df = new SimpleDateFormat(time);
		long timestamp = 0;
		try {
			Date date = df.parse(time);
			timestamp = date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return timestamp;
	}
	
	public static String hexTimeString(){
		//System.currentTimeMillis()+500
		String hexTime = Long.toHexString((System.currentTimeMillis()+500)/1000);
		
		if(hexTime.length()<8){
			int len = 8-hexTime.length();
			for(int i=0;i<len;i++){
				hexTime = "0"+hexTime;
			}
		}
		
		return hexTime.toString().toUpperCase();
	}
	
	
	public static String longSecondToDate(long timestamp){
		SimpleDateFormat df = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
		return df.format(new Date(timestamp*1000));
	}

	public static Date currentDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);

		try {
			Date date = sdf.parse(DEFAULT_DATETIME_FORMAT);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}
	public static Long getLongTimeByFormat(String time,String timeFormat){
		SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
		Date date;
		try {
			date = sdf.parse(time);
			return date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	// TODO 测试主函数
	public static void main(String[] args) {
		
		System.out.println(TimeUtil.hexTimeString());
		
		Long currentTime=System.currentTimeMillis();
		System.out.println(currentTime);
		String time=getStringTime(currentTime,TimeFormat.YEAR_MONTH_DAY);
		System.out.println(time);
		String time1=getStringTime(currentTime,TimeFormat.YEAR_MONTH_DAY_HOUR);
		System.out.println(time1);
		
		Long startTime=TimeUtil.getLongTimeByFormat(time,DEFAULT_DATE_FORMAT);
		System.out.println(startTime);
	}
}
