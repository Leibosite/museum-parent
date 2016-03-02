package com.qingruan.museum.api.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author tommy
 */
@Slf4j
public class ExceptionLog {


//	public static void logException(Exception e, Logger logger) {
//		logger.error("\n" + getErrorStack(e, -1));
//	}
	
	public static String getErrorStack(Exception e){
		return getErrorStack(e, -1);
	}
	
	/**
	 * 获取Exception的堆栈新息。用于显示出错来源时使用。
	 * 
	 * @param e
	 *            Exception对象
	 * @param length
	 *            需要的信息长度，如果 <=0,表示全部信息
	 * @return String 返回该Exception的堆栈新息
	 * @author tommy
	 */
	public static String getErrorStack(Exception e, int length) {
		String error = null;
		if (e != null) {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				PrintStream ps = new PrintStream(baos);
				e.printStackTrace(ps);
				error = baos.toString();
				if (length > 0) {
					if (length > error.length()) {
						length = error.length();
					}
					error = error.substring(0, length);
				}
				baos.close();
				ps.close();
			} catch (Exception e1) {
				error = e.toString();
			}
		}
		return error;
	}
	

}
