package com.qingruan.museum.netty.tcpserver;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qingruan.museum.domain.enums.ResultCode;
import com.qingruan.museum.domain.models.enums.DeviceType;
import com.qingruan.museum.framework.esb.redispushpop.RedisMQBlpopReceiver.EngineToNettyMessageListener;
import com.qingruan.museum.framework.util.EncoderUtil;
import com.qingruan.museum.gateway.common.netty.SendMessage;
import com.qingruan.museum.msg.InterfaceTypeAdapter;
import com.qingruan.museum.msg.MsgProperty.ApplicationType;
import com.qingruan.museum.msg.MuseumMsg;
import com.qingruan.museum.msg.constantth.ConstantThMsg;
import com.qingruan.museum.msg.constantth.ThData;
import com.qingruan.museum.msg.sensor.MtmNetInfo;
import com.qingruan.museum.msg.sensor.SensorData;
import com.qingruan.museum.msg.sensor.SensorHistoryRecord;
import com.qingruan.museum.msg.sensor.SensorMacAddrInfo;
import com.qingruan.museum.msg.sensor.SensorMsg;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorVersion;
import com.qingruan.museum.msg.sensor.SensorNetInfo;
import com.qingruan.museum.msg.sensor.SensorNetStruct;
import com.qingruan.museum.msg.sensor.SensorNetTopology;
import com.qingruan.museum.msg.sensor.SensorOperatingMode;
import com.qingruan.museum.msg.sensor.SensorOperatingMode.OperatingMode;
import com.qingruan.museum.msg.sensor.SensorStatusInfo;
import com.qingruan.museum.netty.protocol.down.DownDataPackage;
import com.qingruan.museum.netty.protocol.down.ThDownDataPackage;
import com.qingruan.museum.netty.protocol.up.ResolveData;

/**
 * 
 * @author jcy
 * @description: 接收Redis消息队列传递的消息
 */
@Slf4j
public class ReceiveEngineMessage implements EngineToNettyMessageListener {

	private Gson gson = new GsonBuilder().registerTypeAdapter(MuseumMsg.class,
			new InterfaceTypeAdapter()).create();

	@Override
	public void receiveMessage(Object message) {
		try {
			// System.out.println(message.toString() + "\n");
			MuseumMsg museumMsg = gson.fromJson(message.toString(),
					MuseumMsg.class);

			if (museumMsg.getMsgHeader() == null
					|| museumMsg.getMsgProperty() == null
					|| museumMsg.getMsgBody() == null) {
				log.error("message queue receive message is null or resolve error");
				return;
			}
			ApplicationType type = museumMsg.getMsgProperty()
					.getApplicationType();

			switch (type) {
			case SENSOR:
				// 处理采集设备相关的消息
				dealSensorData(museumMsg);
				break;
			case CONSTANT_TH:
				dealConstantTHData(museumMsg);
				break;
			case NEW_CONSTANT_TH:
				dealNewConstantTHData(museumMsg);
				break;
			case METEOROLOGICAL_STATION:
				dealMeteorologicalStationData(museumMsg);
				break;
			default:
				break;
			}

		} catch (Exception e) {
		}

	}

	private void dealNewConstantTHData(MuseumMsg museumMsg) {
		System.out.println("设定恒温恒湿设备%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		ConstantThMsg constantThMsg = (ConstantThMsg) museumMsg.getMsgBody();
		ThData thData = constantThMsg.getData();
		String masterGatewayID = thData.getMasterGatewayID();
		String presentMAC = thData.getPresentMAC();
		Long presentValue = thData.getPresentValue() * 100;
		Long topLimit = thData.getTopLimit() * 100;
		Long lowerLimit = thData.getLowerLimit() * 100;
		Long fluctuationValue = thData.getFluctuationValue() * 100;
		String path = thData.getPath();
		String macAddr = thData.getMacAddr();
		String deviceType = "A0";
		StringBuilder builder = new StringBuilder();
		builder.append("3A");
		builder.append("22");
		builder.append(path);
		builder.append(macAddr);
		builder.append("0014");
		builder.append(deviceType);
		StringBuilder builder2 = new StringBuilder();
		builder2.append("3D");
		builder2.append("02");
		builder2.append(presentMAC);
		builder2.append("08");
		String presentValueHex = EncoderUtil.long2HexStr(presentValue, 4);
		builder2.append(presentValueHex);
		String topLimitHex = EncoderUtil.long2HexStr(topLimit, 4);
		builder2.append(topLimitHex);
		String lowerLimitHex = EncoderUtil.long2HexStr(lowerLimit, 4);
		builder2.append(lowerLimitHex);
		String fluctuationValueHex = EncoderUtil.long2HexStr(fluctuationValue,
				4);
		builder2.append(fluctuationValueHex);
		String checkCode2 = EncoderUtil.int2HexStr(
				UnPack.xor(builder2.toString()), 2);
		builder2.append(checkCode2);
		builder2.append("EE");
		builder.append(builder2);
		String checkCode = EncoderUtil.int2HexStr(
				UnPack.xor(builder.toString()), 2);
		builder.append(checkCode);
		builder.append("EE");
		Channel channel = CommonData.deviceChannels.get(masterGatewayID);
		SendMessage.sendMsg(channel, builder.toString());
		System.out.println("设定恒温恒湿设备%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%结束");

	}

	private void dealSensorData(MuseumMsg museumMsg) {
		SensorMsg msgbody = (SensorMsg) museumMsg.getMsgBody();
		String stationMacAddress = null;
		if (null == msgbody) {
			log.error("msgbody is null");
			return;
		} else {
			SensorData data = msgbody.getData();
			if (null == data) {
				log.error("data is null");
				return;
			} else {
				MtmNetInfo mtmNetInfo = data.getMtmNetInfo();
				if (null == mtmNetInfo) {
					log.error("mtmNetInfoo is null");
					return;
				} else {
					stationMacAddress = mtmNetInfo.getStationMacAddr();
				}
			}
		}

		switch (msgbody.getSensorAppType()) {
		case ENGINE_GET_SENSOR_DATA: {
			List<SensorNetInfo> sensorList = null;
			SensorData data = msgbody.getData();
			MtmNetInfo mtmNetInfo = null;
			if (null == data) {
				log.error("data is null");
				return;
			} else {
				mtmNetInfo = data.getMtmNetInfo();

				if (null == mtmNetInfo) {
					log.error("mtmNetInfo is null");
					return;
				} else {
					sensorList = mtmNetInfo.getSensorNetInfos();
					if (null == sensorList) {
						log.error("sensorList is null");
					} else {

					}
				}

			}
			for (SensorNetInfo sensorNetInfos : sensorList) {
				DownDataPackage downSendData = new DownDataPackage("08",
						stationMacAddress);
				downSendData.setBeaconMacAddress(sensorNetInfos
						.getSensorMacAddr());
				ResolveData.addDownTask(downSendData);
			}
		}
			break;
		case ENGINE_GET_SENSOR_STATUS: {
			DownDataPackage downSendData = new DownDataPackage("07",
					stationMacAddress);
			ResolveData.addDownTask(downSendData);
		}
			break;
		case ENGINE_GET_SENSOR_UPGRADE: {
			DownDataPackage downSendData = new DownDataPackage("03",
					stationMacAddress);
			downSendData.setSensorMsg(msgbody);
			ResolveData.addDownTask(downSendData);
		}
			break;
		// 接受完整的MAClist请求ENGINE_POST_SENSOR_MAC_LIST，和GATEWAY_GET_SENSOR_MAC_LIST对应
		case ENGINE_POST_SENSOR_MAC_LIST: {
			if (SensorVersion.V1 == msgbody.getSensorVersion()) {
				DownDataPackage downPackage = new DownDataPackage("02",
						stationMacAddress);
				downPackage.setSensorMsg(msgbody);
				ResolveData.addDownTask(downPackage);
				Channel channel = CommonData.deviceChannels
						.get(stationMacAddress);
				downPackage.connectPackage();
				SendMessage.sendMsg(channel, downPackage.getDownSendData());
			} else {
				// 判断resultcode来表示
				if (null != msgbody.getResult()
						&& ResultCode.SUCCESS == msgbody.getResult().resultCode) {
					System.out.println("ENGINE_POST_SENSOR_MAC_LIST  接收到响应");
					// 初始化缓存，以路径为键 以多个SensorNetInfo的MacList
					LoadingCache<String, List<SensorNetInfo>> cahceBuilder = CacheBuilder
							.newBuilder()
							.build(new CacheLoader<String, List<SensorNetInfo>>() {
								@Override
								public List<SensorNetInfo> load(String key)
										throws Exception {
									return null;
								}

							});

					// 发送顺序表
					ArrayList<String> sequenceList = new ArrayList<>();
					SensorData data = msgbody.getData();
					MtmNetInfo mtmNetInfo = data.getMtmNetInfo();
					// sensorNetStruct真正的数据体 ：以主网关id为标示和下面多条路径为分割的的MAClist
					SensorNetStruct sensorNetStruct = mtmNetInfo
							.getSensorNetStruct();
					// 获取数据体的主网关id
					String masterGatewayId = sensorNetStruct
							.getMasterGatewayId();
					// 多个路径代表的MacList就是SensorMacAddrInfo
					List<SensorMacAddrInfo> sensorMacAddrInfos = sensorNetStruct
							.getSensorMacAddrInfos();
					int size = sensorMacAddrInfos.size();

					// 遍历多个路径代表的MacList就是SensorMacAddrInfo

					for (int i = 0; i < sensorMacAddrInfos.size(); i++) {
						// 第i条路径的路径所对应的数据实体
						SensorMacAddrInfo sensorMacAddrInfo = sensorMacAddrInfos
								.get(i);
						// 第i条路径标示 routerNetTopology
						String routerNetTopology = sensorMacAddrInfo
								.getRouterNetTopology();
						// 把第i条路径标示 routerNetTopology加入发送顺序表
						sequenceList.add(routerNetTopology);
						// 第i条路径对应的MacList实体
						List<SensorNetInfo> sensorNetInfos = sensorMacAddrInfo
								.getSensorNetInfos();
						// 以第i条路径为键 和 sensorNetInfos为值 放入缓存
						cahceBuilder.put(routerNetTopology, sensorNetInfos);
					}
					// 放入全局的缓存 顺序表 以主网关id为主键 主网关下的所有的路径的发送顺序的ArrayList
					CommonData.sequenceListMap.put(masterGatewayId,
							sequenceList);
					// 放入全局的缓存 macList实体
					CommonData.macList.put(masterGatewayId, cahceBuilder);
					// 初始化重新发送的缓存
					CommonData.reSent82PathNum.put(masterGatewayId, 0);
					// 开始82发送
					// 安每个主网关的顺序表发送 根据82的数量来发送
					new MacListSender().sendPathPackage(masterGatewayId);
				} else {
					log.error("net info error");
				}

			}
		}
			break;

		case ENGINE_UPDATE_SENSOR_MAC_LIST: {

			// 判断resultcode来表示
			if (null != msgbody.getResult()
					&& ResultCode.SUCCESS == msgbody.getResult().resultCode) {
				System.out.println("engine端推送 MacList ·····················");
				// 初始化缓存，以路径为键 以多个SensorNetInfo的MacList
				LoadingCache<String, List<SensorNetInfo>> cahceBuilder = CacheBuilder
						.newBuilder().build(
								new CacheLoader<String, List<SensorNetInfo>>() {
									@Override
									public List<SensorNetInfo> load(String key)
											throws Exception {
										return null;
									}

								});

				// 发送顺序表
				ArrayList<String> sequenceList = new ArrayList<>();
				SensorData data = msgbody.getData();
				MtmNetInfo mtmNetInfo = data.getMtmNetInfo();
				// sensorNetStruct真正的数据体 ：以主网关id为标示和下面多条路径为分割的的MAClist
				SensorNetStruct sensorNetStruct = mtmNetInfo
						.getSensorNetStruct();
				// 获取数据体的主网关id
				String masterGatewayId = sensorNetStruct.getMasterGatewayId();
				// 多个路径代表的MacList就是SensorMacAddrInfo
				List<SensorMacAddrInfo> sensorMacAddrInfos = sensorNetStruct
						.getSensorMacAddrInfos();

				// 遍历多个路径代表的MacList就是SensorMacAddrInfo

				for (int i = 0; i < sensorMacAddrInfos.size(); i++) {
					// 第i条路径的路径所对应的数据实体
					SensorMacAddrInfo sensorMacAddrInfo = sensorMacAddrInfos
							.get(i);
					// 第i条路径标示 routerNetTopology
					String routerNetTopology = sensorMacAddrInfo
							.getRouterNetTopology();
					// 把第i条路径标示 routerNetTopology加入发送顺序表
					sequenceList.add(routerNetTopology);
					// 第i条路径对应的MacList实体
					List<SensorNetInfo> sensorNetInfos = sensorMacAddrInfo
							.getSensorNetInfos();
					// 以第i条路径为键 和 sensorNetInfos为值 放入缓存
					cahceBuilder.put(routerNetTopology, sensorNetInfos);
				}
				// 放入全局的缓存 顺序表 以主网关id为主键 主网关下的所有的路径的发送顺序的ArrayList
				ConcurrentHashMap<String, ArrayList<String>> updateSequenceListMap = CommonData.updateSequenceListMap;
				ArrayList<String> put = updateSequenceListMap.put(
						masterGatewayId, sequenceList);
				// 放入全局的缓存 macList实体
				CommonData.updateMacList.put(masterGatewayId, cahceBuilder);
				// 初始化重新发送的缓存
				CommonData.reSent82PathNum.put(masterGatewayId, 0);
				// 开始82发送
				// 安每个主网关的顺序表发送 根据82的数量来发送
				new MacListSender().sendPathPackage(masterGatewayId);
			} else {
				log.error("net info error");
			}

		}
			break;
		case ENGINE_UPDATE_NET_TOPOLOCY_INFO: {
			// 判断resultcode来表示
			if (null != msgbody.getResult()
					&& ResultCode.SUCCESS == msgbody.getResult().resultCode) {
				System.out.println("ENGINE端推送 NET_TOPOLOCY ··············");
				// 发送顺序表
				ArrayList<String> sequenceList = new ArrayList<>();
				SensorData data = msgbody.getData();
				MtmNetInfo mtmNetInfo = data.getMtmNetInfo();
				// sensorNetStruct真正的数据体 ：以主网关id为标示和下面多条路径为分割的的MAClist
				SensorNetStruct sensorNetStruct = mtmNetInfo
						.getSensorNetStruct();
				SensorNetTopology sensorNetTopology = mtmNetInfo
						.getSensorNetTopology();
				List<String> routerNetTopology2 = sensorNetTopology
						.getRouterNetTopology();

				// 获取数据体的主网关id
				String masterGatewayId = sensorNetTopology.getMasterGatewayId();

				for (int i = 0; i < routerNetTopology2.size(); i++) {
					// 第i条路径的路径所对应的数据实体
					String routerNetTopology = routerNetTopology2.get(i);
					// 把第i条路径标示 routerNetTopology加入发送顺序表
					sequenceList.add(routerNetTopology);
				}
				// 放入全局的缓存 顺序表 以主网关id为主键 主网关下的所有的路径的发送顺序的ArrayList
				CommonData.netInfoMap.put(masterGatewayId, sequenceList);
				// *****************************************************

				new NetInfoSender().sendNetInfo(masterGatewayId);
			} else {
				log.error("net info error");
			}
		}
			break;
		case ENGINE_POST_NET_TOPOLOCY_INFO: {
			// 判断resultcode来表示
			if (null != msgbody.getResult()
					&& ResultCode.SUCCESS == msgbody.getResult().resultCode) {
				System.out.println("ENGINE端推送 NET_TOPOLOCY ··············");
				// 发送顺序表
				ArrayList<String> sequenceList = new ArrayList<>();
				SensorData data = msgbody.getData();
				MtmNetInfo mtmNetInfo = data.getMtmNetInfo();
				// sensorNetStruct真正的数据体 ：以主网关id为标示和下面多条路径为分割的的MAClist
				SensorNetStruct sensorNetStruct = mtmNetInfo
						.getSensorNetStruct();
				SensorNetTopology sensorNetTopology = mtmNetInfo
						.getSensorNetTopology();
				List<String> routerNetTopology2 = sensorNetTopology
						.getRouterNetTopology();

				// 获取数据体的主网关id
				String masterGatewayId = sensorNetTopology.getMasterGatewayId();

				for (int i = 0; i < routerNetTopology2.size(); i++) {
					// 第i条路径的路径所对应的数据实体
					String routerNetTopology = routerNetTopology2.get(i);
					// 把第i条路径标示 routerNetTopology加入发送顺序表
					sequenceList.add(routerNetTopology);
				}
				// 放入全局的缓存 顺序表 以主网关id为主键 主网关下的所有的路径的发送顺序的ArrayList
				CommonData.netInfoMap.put(masterGatewayId, sequenceList);
				// *****************************************************

				new NetInfoSender().sendNetInfo86(masterGatewayId);
			} else {
				log.error("net info error");
			}
		}
			break;
		case ENGINE_GET_DEVICE_STATUS: {
			System.out.println("ENGINE_GET_DEVICE_STATUS");
			// 向硬件发送消息
			SensorStatusInfo sensorStatusInfo = msgbody.getSensorStatusInfo();
			DeviceType deviceType = sensorStatusInfo.getDeviceType();
			String masterGatewayID = sensorStatusInfo.getMasterId();
			DetechDevice detechDevice = new DetechDevice();
			if (DeviceType.MASTER_GATEWAY == deviceType) {
				System.out.println("探测网关" + masterGatewayID);
				// 探测主网关
				detechDevice.detechDeviceMasterGateway(masterGatewayID);

			} else if (DeviceType.MONITORING_POINT_ONE == deviceType
					|| DeviceType.MONITORING_POINT_TWO == deviceType) {
				System.out.println("探测采集设备" + masterGatewayID);
				// 探测一二类采集设备
				String macAddr = sensorStatusInfo.getMacAddr();

				detechDevice.detechDeviceByMAC(masterGatewayID, macAddr);
				System.out.println();
			} else {
				System.out.println("探测路由网关或中继" + masterGatewayID);
				// 通过id来探测路由网关和中继
				String id = sensorStatusInfo.getId();
				detechDevice.detechDeviceByID(masterGatewayID, id);
			}
		}
			break;
		// case ENGINE_NOTIFY_GW_UPDATE_NET_INFO: {
		// String masterGetwayID = null;
		// new NetInfoSender().sendNetInfo(masterGetwayID );
		// }
		// break;
		case ENGINE_GET_HISTORY_RECORD: {
			SensorHistoryRecord sensorHistoryRecord = msgbody
					.getSensorHistoryRecord();
			String macAddr = sensorHistoryRecord.getMacAddr();
			String path = sensorHistoryRecord.getTopologyPath();
			String[] pathString = path.split("-");

			if (pathString.length == 3) {
				path = pathString[0] + pathString[1] + pathString[2];
			} else {
				path = "0000" + pathString[0] + pathString[1];
			}
			String masterGatewayID = pathString[0];
			System.out.println("masterGatewayID :" + masterGatewayID);
			System.out.println("path :" + path);
			System.out.println("macAddr :" + macAddr);
			new BeginHistoryData().beginHistoryData(masterGatewayID, path,
					macAddr);
		}
			break;
		case ENGINE_START_DEBUG_MODE: {
			SensorOperatingMode sensorOperatingMode = msgbody
					.getSensorOperatingMode();
			OperatingMode operatingMode = sensorOperatingMode
					.getOperatingMode();
			MessageHandler.operatingMode = operatingMode;
			System.out.println(MessageHandler.operatingMode);
		}
			break;
		case GATEWAY_REPORT_ALARM: {
			// 告警的返回 下一版 待定
			// GATEWAY_POST_SENSOR_STATUS 发送告警
		}
			break;

		default:
			break;
		}

	}

	private void dealConstantTHData(MuseumMsg museumMsg) {
		ConstantThMsg msgbody = (ConstantThMsg) museumMsg.getMsgBody();
		switch (msgbody.getThAppType()) {
		case ENGINE_GET_TH_PRESET_DATA: {
			ThDownDataPackage downSendData = new ThDownDataPackage(msgbody
					.getData().getThNetInfos().getPlcIpv4Addr());
			downSendData.readTHServerPresetValue();
		}
			break;
		case ENGINE_NOTIFY:

			break;
		case ENGINE_WRITE_TH_PRESET_DATA: {
			ThDownDataPackage downSendData = new ThDownDataPackage(msgbody
					.getData().getThNetInfos().getPlcIpv4Addr());
			downSendData.writeTHServerPresetValue(msgbody);
			;
		}
			break;

		default:
			break;
		}

	}

	private void dealMeteorologicalStationData(MuseumMsg museumMsg) {

	}
}
