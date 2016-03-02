package com.qingruan.museum.admin.params;

import java.util.LinkedHashMap;
import java.util.Map;

public class UserInfoStaticParams {
	/**
	 * 
	 * @description 用户访问权限
	 * @version currentVersion(1.0)
	 * @author hli
	 * @createtime 2012-12-10 下午1:35:23
	 */
	


	public static final Integer ONLINE = 1;
	public static final Integer UNONLINE = 0;


	public static Map<Integer, String> getStatusmap() {
	    return _statusMap;
	}
	private static final Map<Integer, String> _statusMap = new LinkedHashMap<Integer, String>();
	static {
		_statusMap.put(ONLINE, "在线");
		_statusMap.put(UNONLINE, "离线");
		_statusMap.put(null, "离线");
	}


}
