package com.qingruan.museum.framework.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

public class TextUtil {

	/**
	 * 16进制字符串转汉字
	 * 
	 * @param hexs
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String hexToGBK(String hexs)
			throws UnsupportedEncodingException {

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < hexs.length(); i = i + 2) {
			sb.append("%").append(hexs.subSequence(i, i + 2));
		}
		return URLDecoder.decode(sb.toString(), "GBK");
	}

	/**
	 * Ascii2Hex
	 * 
	 * @param oldStr
	 * @return
	 */
	public static String ascii2Hex(String oldStr) {
		byte[] bytes = oldStr.getBytes();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			System.out.println(bytes[i]);
		}
		return sb.toString();
	}
	
	public static String hex2Ascii(String hexString){
		 StringBuilder builder = new StringBuilder();
		
		 for(int i =0;i<hexString.length()/2;i++){
			 String code = hexString.substring(i*2,i*2+2);
			 char asciiCode = (char)(Integer.parseInt(code, 16));
			 builder.append(asciiCode);
		 }
		 return builder.toString();
	}

	/**
	 * String a = "计算机！"; byte[] b = a.getBytes("GBK"); String n =
	 * encodeHex(b).replaceAll(" ", "");
	 * 
	 * @param bytes
	 *            byte[]
	 * @return 得到二进制字符串
	 */
	public static final String encodeHex(byte[] bytes) {
		StringBuffer buff = new StringBuffer(bytes.length * 2);
		String b;
		for (int i = 0; i < bytes.length; i++) {
			b = Integer.toHexString(bytes[i]);
			buff.append(b.length() > 2 ? b.substring(6, 8) : b);
			buff.append(" ");
		}
		return buff.toString();
	}

	/**
	 * 字符串不到定长长度右补0 OK
	 * 
	 * @param str
	 *            输入字符串
	 * @param bytes
	 *            定长长度
	 * @return 长度相等直接输出，长度小于定长长度右补0输出
	 * @throws UnsupportedEncodingException
	 */
	public static String octetString(String str, int bytes)
			throws UnsupportedEncodingException {
		
		byte[] b = str.getBytes("GBK");
		String n = encodeHex(b).replaceAll(" ", "");
		int len = n.length() / 2;
		if (len < bytes) {
			StringBuffer sb = new StringBuffer(n);
			while (len < bytes) {
				sb.append("00");// 右补0
				len++;
			}
			return sb.toString();
		}
		return n;
	}

	/**
	 * 10进制数字转16进制字符串，字符串长度最大为20 OK
	 * 
	 * @param value
	 *            10进制数字
	 * @param bytes
	 *            字节数
	 * @return 16进制格式的字符串
	 */
	public static String intToHex(int value, int bytes) {
		String m = "00000000000000000000" + Integer.toHexString(value);
		return m.substring(m.length() - bytes * 2, m.length());
	}

	/***
	 * 10进制数字转16进制字符串，字符串长度最大为20 OK
	 * 
	 * @param value
	 *            10进制数字
	 * @param bytes
	 *            定长长度,字节数
	 * @return 16进制格式的字符串
	 */
	public static String intToHex(long value, int bytes) {
		String m = "00000000000000000000" + Long.toHexString(value);
		return m.substring(m.length() - bytes * 2, m.length());
	}

	/**
	 * 返回十六进制数的二进制形式
	 * 
	 * @param hex
	 * @return
	 */
	public static String convertHexToBin(String hex) {
		StringBuffer buff = new StringBuffer();
		int i;
		for (i = 0; i < hex.length(); i++) {
			buff.append(getBin(hex.substring(i, i + 1)));
		}
		return buff.toString();
	}

	/**
	 * 得到北京时间的UTC时间
	 * 
	 * @param date
	 *            Date
	 * @return String UTC时间
	 */
	public static String getUTC(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss.SSS");
		TimeZone timezone = TimeZone.getTimeZone("GMT-0");
		format.setTimeZone(timezone);
		return format.format(date);
	}

	private static String getBin(String hex) {
		int i;
		for (i = 0; i < hexs.length && !hex.toLowerCase().equals(hexs[i]); i++)
			;
		return bins[i];
	}

	/**
	 * 时间字符串转long
	 * 
	 * @param inVal
	 *            2001-10-10 10:10:10
	 * @return long
	 */
	public static long fromDateStringToLong(String inVal) {
		Date date = null;
		SimpleDateFormat inputFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss"); 
		try {
			date = inputFormat.parse(inVal); // 将字符型转换成日期型
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		return date.getTime(); // 返回毫秒数
	}
	
	/**
	 * 时间字符串转long字符串
	 * 
	 * @param inVal
	 *            2001-10-10 10:10:10
	 * @return long
	 */
	public static String fromDateStringToString(String inVal) {
		Date date = null;
		SimpleDateFormat inputFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss"); 
		try {
			date = inputFormat.parse(inVal); // 将字符型转换成日期型
			//System.out.println(date.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println(Long.toHexString( date.getTime() /1000));
		return Long.toHexString( date.getTime() /1000); // 返回毫秒数
	}

	/**
	 * long转日期 
	 * @param inVal
	 * @return String
	 */
	public static String fromLongToDateString(long inVal) {
		SimpleDateFormat inputFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		// 输入的是相对时间的 秒差
		Date date1 = new Date(inVal * 1000);
		return inputFormat.format(date1);
	}

	/**
	 * 得到一个时间的hex数字
	 * 
	 * @param strtime
	 * @return String
	 */
	public static String getHexUtc(String strtime) {
		String ret = "";
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d1 = df.parse(strtime);// "2010-1-10 9:7:54"
			// 这个时间应该减去8小时
			Date d2 = df.parse("1970-1-1 8:0:0");
			long diff = (d1.getTime() - d2.getTime()) / 1000;
			ret = TextUtil.intToHex(diff, 8);
		} catch (Exception e) {
		}
		return ret;
	}

	private static String[] hexs = new String[] { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	private static String[] bins = new String[] { "0000", "0001", "0010",
			"0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010",
			"1011", "1100", "1101", "1110", "1111" };

	/**
	 * 字符串转16进制 OK
	 * 
	 * @param s
	 * @return
	 */
	public static String toHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		return str;
	}
	
	

	/**
	 * 16进制转GBK OK
	 * 
	 * @param theHex
	 *            C0EEB0D7CAC7CAABC8CB
	 * @return String 李白是诗人
	 */
	public static String hex2Str(String theHex) {
		String theRst = "";
		byte[] theByte = new byte[theHex.length() / 2];

		try {
			for (int i = 0; i < theHex.length(); i += 2) {
				theByte[i / 2] = Integer.decode(
						"0X" + theHex.substring(i, i + 2)).byteValue();
			}
			theRst = new String(theByte, 0, theByte.length, "GBK");
		} catch (Exception Ue) {
			Ue.printStackTrace();
		}
		return theRst;
	}

	/**
	 * GBK转16进制 OK
	 * 
	 * @param theStr
	 *            李白是诗人
	 * @return String C0EEB0D7CAC7CAABC8CB
	 */
	public static String str2Hex(String theStr) {
		byte[] bytes;
		String result = "";
		int tmp;
		String tmpStr;
		try {
			String delSpace = theStr.replace(" ", "");
			bytes = delSpace.getBytes("GBK");
			for (int i = 0; i < bytes.length; i++) {
				if (bytes[i] < 0) {
					tmp = 256 + bytes[i];
					tmpStr = Integer.toHexString(tmp).toUpperCase();
					result += tmpStr;
				} else {
					tmpStr = Integer.toHexString(bytes[i]).toUpperCase();

					result += tmpStr.length() == 1 ? "0" + tmpStr : tmpStr;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * BCD　时间转成　2000-00-00形式
	 * 
	 * @param s
	 * @return
	 */
	public static String getDatetime(String datetime) {
		String date = datetime.substring(0, 6);
		String time = datetime.substring(6);
		StringBuffer sb = new StringBuffer("20");
		sb.append(date.substring(0, 2));
		sb.append("-").append(date.substring(2, 4));
		sb.append("-").append(date.substring(4));
		sb.append(" ");
		sb.append(time.substring(0, 2));
		sb.append(":").append(time.substring(2, 4));
		if (datetime.length() == 12) {
			sb.append(":").append(time.substring(4));
		}
		String stime=sb.toString();
		return stime;
	}
	
	/**
	 * 把HashMap转换成json字符串
	 */
	public static String alarmJson(HashMap<String,String> map){
		
		if(map.isEmpty())
			return "{}";
		
		
		Iterator<Entry<String, String>> msgs = map.entrySet().iterator();
		StringBuffer json = new StringBuffer();
		json.append("{");
		while(msgs.hasNext()){
			Map.Entry<String, String> info = msgs.next();
			String key = info.getKey();
			String value = info.getValue();
			json.append("\""+key+"\"");
			json.append(":");
			json.append("\""+value+"\"");
			json.append(",");
			
		}
		json.deleteCharAt(json.length()-1);
		json.append("}");
		
		return json.toString();
	}

}
