package com.qingruan.museum.netty.gprs;

import com.qingruan.museum.framework.util.PropertiesUtils;

public class GPRSCommand {
	
	public enum Command{
		AT_OK,AT_SET_INIT,AT_HEAD,AT_SET_IP,AT_SET_LEN,AT_CLOSE,
		AT_SHUT,AT_SHUT_OK,AT_CONN_OK,AT_CONN_FA,AT_CONN_CLOSE,AT_SEND_OK,AT_SEND_FA
	}
	
	// 联通 UNINET 移动  CMNET
	public static String  AT_SET_INIT  ="AT+CIPCSGP=1,"+PropertiesUtils.readValue("gprsgateway.simtype").trim();
	public static String  AT_HEAD      ="AT+CIPHEAD=1";
	public static String  AT_SET_IP    ="AT+CIPSTART=\"TCP\","+PropertiesUtils.readValue("gprsgateway.connectip").trim();
	public static String  AT_SET_LEN   ="AT+CIPSEND=";
	public static String  AT_CLOSE     ="AT+CIPCLOSE=1";
	public static String  AT_SHUT      ="AT+CIPSHUT";
	public static String  AT_SHUT_OK   ="SHUT OK";
	public static String  AT_OK        ="OK";
	public static String  AT_ERROR     ="ERROR";
	public static String  AT_CONN_OK   ="CONNECT OK";
	public static String  AT_CONN_FA   ="CONNECT FAIL";
	public static String  AT_SEND_OK   ="SEND OK";
	public static String  AT_SEND_FA   ="SEND FAIL";
	public static String  AT_CLOSE_OK  ="CLOSE OK";
	public static String  AT_CONN_CLOSE="CLOSED";
	public static String  AT_DATA	   ="+IPD,";
	public static String  AT_END       ="\n";
}
