package com.qingruan.museum.admin.params;

import java.util.LinkedHashMap;
import java.util.Map;

import com.qingruan.museum.domain.enums.device.DeviceStatus;
import com.qingruan.museum.domain.models.enums.DeviceType;
import com.qingruan.museum.domain.models.enums.MonitorDataType;

public class BusinessStaticParams {
	/**
	 * 
	 * @description 用户访问权限
	 * @version currentVersion(1.0)
	 * @author Tommy
	 * @createtime 2012-12-10 下午1:35:23
	 */
	public enum Permission {

		CONFIG("config");
		/**
		 * 
		 * Constructor Method
		 * 
		 * @param value
		 */
		private Permission(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		private String value;

	}

	public static final String OPERATION_ALLOW = "ALLOW";
	public static final String OPERATION_NOT_ALLOW = "NOT_ALLOW";

	public static final Integer STATUS_DISABLED = 0;
	public static final Integer STATUS_ENABLED = 1;

	public static final String ANY = "0";
	public static final String HTTP = "1";
	public static final String WAP1 = "2";
	public static final String WAP2 = "3";
	public static final String SMTP = "4";
	public static final String POP3 = "5";
	public static final String IMAP4 = "6";
	public static final String FTP = "7";
	public static final String RTSP = "8";
	public static final String MMS = "9";
	public static final String OTHER = "99";
	// 时间粒度 开始
	public static final String TEN_YEAR = "4";
	public static final String YEAR = "0";
	public static final String MONTH = "1";
	public static final String DAY = "2";
	public static final String HOUR = "3";

	// 时间粒度 结束

	// 数据类型
	public static final String MIN = "0";
	public static final String MAX = "1";
	public static final String AVERAGE = "2";
	public static final String ALL = "3";

	// 状态
	public static final String ENABLE = "0";
	public static final String DISABLE = "1";

	private static final Map<String, String> _protocolTypeMap = new LinkedHashMap<String, String>();
	static {
		_protocolTypeMap.put(ANY, "ANY");
		_protocolTypeMap.put(HTTP, "HTTP");
		_protocolTypeMap.put(WAP1, "WAP1");
		_protocolTypeMap.put(WAP2, "WAP2");
		_protocolTypeMap.put(SMTP, "SMTP");
		_protocolTypeMap.put(IMAP4, "IMAP4");
		_protocolTypeMap.put(POP3, "POP3");
		_protocolTypeMap.put(FTP, "FTP");
		_protocolTypeMap.put(RTSP, "RTSP");
		_protocolTypeMap.put(MMS, "MMS");
		_protocolTypeMap.put(OTHER, "OTHER");
	}

	public static Map<String, String> getProtocolTypeMap() {
		return _protocolTypeMap;
	}

	/**
	 *  时间粒度
	 * 
	 * @return
	 */
	public static Map<String, String> getTimeGranularityMap() {
		return _timeGranularityMap;
	}

	private static final Map<String, String> _timeGranularityMap = new LinkedHashMap<String, String>();
	static {
		_timeGranularityMap.put(TEN_YEAR, "十年");
		_timeGranularityMap.put(YEAR, "年");
		_timeGranularityMap.put(MONTH, "月");
		_timeGranularityMap.put(DAY, "天");
	}

	public static final Map<String, String> getDataTypeMap() {
		return _dataTypeMap;
	}

	private static final Map<String, String> _dataTypeMap = new LinkedHashMap<String, String>();
	static {
		_dataTypeMap.put(MIN, "最小值");
		_dataTypeMap.put(MAX, "最大值");
		_dataTypeMap.put(AVERAGE, "平均值");
		_dataTypeMap.put(ALL, "全部");
	}

	public static final Map<String, String> getStatusMap() {
		return _statusMap;
	}

	private static final Map<String, String> _statusMap = new LinkedHashMap<String, String>();
	static {
		_statusMap.put(ENABLE, "激活");
		_statusMap.put(DISABLE, "失活");
	}

	public static final Map<DeviceStatus, String> getDeviceStatusMap() {
		return _deviceStatusMap;
	}

	private static final Map<DeviceStatus, String> _deviceStatusMap = new LinkedHashMap<DeviceStatus, String>();
	static {
		_deviceStatusMap.put(DeviceStatus.CONNECTED,
				DeviceStatus.CONNECTED.value());
		_deviceStatusMap.put(DeviceStatus.DISCONNECT,
				DeviceStatus.DISCONNECT.value());
		_deviceStatusMap
				.put(DeviceStatus.DEMAGED, DeviceStatus.DEMAGED.value());
		_deviceStatusMap
				.put(DeviceStatus.DEMAGED, DeviceStatus.DEMAGED.value());

	}

	public static final Map<DeviceType, String> getDeviceTypeMap() {
		return _deviceTypeMap;
	}

	private static final Map<DeviceType, String> _deviceTypeMap = new LinkedHashMap<DeviceType, String>();
	static {
		_deviceTypeMap.put(DeviceType.MONITORING_POINT_ONE,
				DeviceType.MONITORING_POINT_ONE.value());
		_deviceTypeMap.put(DeviceType.MONITORING_STATION,
				DeviceType.MONITORING_STATION.value());

	}

	public static final Map<MonitorDataType, String> getMonitorObjectMap() {
		return _monitorObjectMap;
	}

	private static final Map<MonitorDataType, String> _monitorObjectMap = new LinkedHashMap<MonitorDataType, String>();
	static {

		_monitorObjectMap.put(MonitorDataType.CO2,
				MonitorDataType.CO2.value());
		_monitorObjectMap.put(MonitorDataType.SO2,
				MonitorDataType.SO2.value());
		_monitorObjectMap.put(MonitorDataType.HUMIDITY,
				MonitorDataType.HUMIDITY.value());
		_monitorObjectMap.put(MonitorDataType.TEMPERATURE,
				MonitorDataType.TEMPERATURE.value());
		_monitorObjectMap.put(MonitorDataType.LIGHTING,
				MonitorDataType.LIGHTING.value());
		_monitorObjectMap.put(MonitorDataType.PM10,
				MonitorDataType.PM10.value());
		_monitorObjectMap.put(MonitorDataType.PM1_0,
				MonitorDataType.PM1_0.value());
		_monitorObjectMap.put(MonitorDataType.PM2_5,
				MonitorDataType.PM2_5.value());

		_monitorObjectMap.put(MonitorDataType.TVOC,
				MonitorDataType.TVOC.value());
		_monitorObjectMap.put(MonitorDataType.UV,
				MonitorDataType.UV.value());

	}
}
