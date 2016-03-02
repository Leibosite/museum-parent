package com.qingruan.museum.netty.modbus;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import com.qingruan.museum.domain.models.enums.MonitorDataType;
import com.qingruan.museum.framework.jackson.JSONUtil;
import com.qingruan.museum.framework.util.EncoderUtil;
import com.qingruan.museum.gateway.MuseumGateway;
import com.qingruan.museum.msg.MsgHeader;
import com.qingruan.museum.msg.MsgHeader.DeliveryMode;
import com.qingruan.museum.msg.MsgProperty;
import com.qingruan.museum.msg.MsgProperty.ApplicationType;
import com.qingruan.museum.msg.MsgProperty.CmdType;
import com.qingruan.museum.msg.MuseumMsg;
import com.qingruan.museum.msg.sensor.MtmNetInfo;
import com.qingruan.museum.msg.sensor.SensorData;
import com.qingruan.museum.msg.sensor.SensorDataContent;
import com.qingruan.museum.msg.sensor.SensorMsg;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorAppType;
import com.qingruan.museum.msg.sensor.SensorMsg.SensorMsgType;
import com.qingruan.museum.msg.sensor.SensorNetInfo;

@Slf4j
public class ModbusDecoder extends ByteToMessageDecoder{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,List<Object> out) throws Exception {

		int msgLen = in.readableBytes();
		byte[] msg = (byte[]) null;
		String message = "";
		try{
			if ((in.isReadable()) && (msgLen > 0)) {
				msg = new byte[msgLen];
				in.readBytes(msg);	
				if(CRC16M.checkBuf(msg)){
					message = EncoderUtil.getHexStr(msg);
					String funcCoder = message.substring(2, 4);
					switch (funcCoder) {
					case "04":
					{
						double soilTemperature = Integer.valueOf(message.substring(6, 10), 16)/10.0;
						double soilHumidity = Integer.valueOf(message.substring(10, 14), 16)/10.0;
						double soilSalinity = Integer.valueOf(message.substring(14, 18), 16)/100.0;
						
						MuseumMsg museumMsg = new MuseumMsg();
						
						MsgHeader header = new MsgHeader();
						header.setDeliveryMode(DeliveryMode.PTP);
						header.setTimeStamp(System.currentTimeMillis());
						museumMsg.setMsgHeader(header);
						
						MsgProperty msgProperty = new MsgProperty();
						msgProperty.setApplicationType(ApplicationType.SENSOR);
						msgProperty.setCmdType(CmdType.ANSWER);
						museumMsg.setMsgProperty(msgProperty);
						
						SensorMsg soilMsg = new SensorMsg();
						
						soilMsg.setSensorAppType(SensorAppType.GATEWAY_POST_SENSOR_DATA);
						soilMsg.setSensorMsgType(SensorMsgType.TEXT);
						
						SensorData data = new SensorData();
						
						MtmNetInfo mtmNetInfos = new MtmNetInfo();
						mtmNetInfos.setStationMacAddr("000000000004");
						data.setMtmNetInfo(mtmNetInfos);
						
						SensorNetInfo info = new SensorNetInfo();
						info.setSensorMacAddr("0000000000"+message.substring(0, 2));
						data.setSensorNetInfo(info);
						
						List<SensorDataContent> soilDataContents = new ArrayList<SensorDataContent>();
						
						SensorDataContent content1 = new SensorDataContent();
						content1.setMonitorDataType(MonitorDataType.SOIL_TEMPERATURE);
						content1.setValue(soilTemperature);
						soilDataContents.add(content1);
						
						SensorDataContent content2 = new SensorDataContent();
						content2.setMonitorDataType(MonitorDataType.SOIL_HUMIDITY);
						content2.setValue(soilHumidity);
						soilDataContents.add(content2);
						
						SensorDataContent content3 = new SensorDataContent();
						content3.setMonitorDataType(MonitorDataType.SOIL_SALINITY);
						content3.setValue(soilSalinity);
						soilDataContents.add(content3);
						data.setDatas(soilDataContents);
						data.setTimeStamp(System.currentTimeMillis());
						
						
						soilMsg.setData(data);
						
						
						museumMsg.setMsgBody(soilMsg);
						String domainJson = JSONUtil.serialize(museumMsg);
						log.info(domainJson);
						MuseumGateway.redisMQPushSender.sendNettyToEngine(domainJson);
					}
						break;

					default:
						break;
					}
				}
			}
		}catch(Exception e){
			log.error(e.toString());
		}
		log.info("SOIL_Gataway_Receive_Data:" + message +"\r\n");
	}

}
