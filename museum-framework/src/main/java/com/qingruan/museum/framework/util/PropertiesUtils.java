package com.qingruan.museum.framework.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertiesUtils {

	private static URL url = PropertiesUtils.class.getResource("/application.properties");


	/**
	 * 根据变量读取配置文件的值
	 * 
	 * @param key
	 *            变量名称
	 * @return String
	 */
	public static String readValue(String key) {
		Properties props = new Properties();
		try {
			String u = URLDecoder.decode(url.getFile(), "utf-8");
			InputStream in = new BufferedInputStream(new FileInputStream(u));
			props.load(in);
			String value = props.getProperty(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static List<HashMap<String,Object>> readIpAddressesFromConfigFile(String key) throws Exception {
		String ipAddresses = readValue(key);//"gateway.addr.urls"
		String[] addrs = ipAddresses.split(",");
		Pattern pattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\:\\d+");
		List<HashMap<String,Object>> ipList = new ArrayList<HashMap<String,Object>>();
		for (String string : addrs) {
			Matcher matcher = pattern.matcher(string);
			boolean isMatch= matcher.matches();
			if(isMatch){
				String[] ipDivide = string.split(":");
				int port = Integer.valueOf(ipDivide[1]);
				if(port>=7500 && port <=65535 ){
					HashMap<String, Object> ipAndPort = new HashMap<String, Object>();
					ipAndPort.put("ip", ipDivide[0]);
					ipAndPort.put("port",ipDivide[1]);
					ipList.add(ipAndPort);
				}else{
					throw new Exception("请您把端口设置在7500到65536之间");
				}
			}else{
				throw new Exception("您的ip地址的格式不对，请注意检查ip格式，例如127.0.0.1:10001");
			}
		}
		
		return ipList;
	}
	

	public static void readProperties() {
		Properties props = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(url
					.getFile()));
			props.load(in);
			Enumeration<?> en = props.propertyNames();
			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				String Property = props.getProperty(key);
				System.out.println(key + Property);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 写配置文件变量值
	 * 
	 * @param parameterName
	 *            变量名
	 * @param parameterValue
	 *            值
	 */
	public static void writeProperties(String parameterName,
			String parameterValue) {
		Properties prop = new Properties();
		try {
			InputStream fis = new FileInputStream(url.getFile());
			prop.load(fis);
			OutputStream fos = new FileOutputStream(url.getFile());
			prop.setProperty(parameterName, parameterValue);
			prop.store(fos, "Update '" + parameterName + "' value");
		} catch (IOException e) {
			System.err.println("Visit " + url.getFile() + " for updating "
					+ parameterName + " value error");
		}
	}
}
