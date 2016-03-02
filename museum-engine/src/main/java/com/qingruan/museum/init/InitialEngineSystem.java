/**
 2014年11月28日
 14cells
 
 */
package com.qingruan.museum.init;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import com.qingruan.museum.engine.exception.ExceptionLog;
import com.qingruan.museum.engine.listener.AdminToEngineMsgListener;
import com.qingruan.museum.engine.listener.NettyToEngineMsgListener;
import com.qingruan.museum.framework.esb.redispubsub.RedisPubSender;
import com.qingruan.museum.framework.esb.redispubsub.RedisSubReceiver;
import com.qingruan.museum.framework.esb.redispushpop.RedisMQBlpopReceiver;
import com.qingruan.museum.framework.esb.redispushpop.RedisMQBroker;
import com.qingruan.museum.framework.esb.redispushpop.RedisMQPushSender;
import com.qingruan.museum.netty.httpsqs.HttpSQSServer;
import com.qingruan.museum.netty.httpsqs.RestfulAction;

/**
 * @author tommy
 * 
 */
@Getter
@Setter
@Slf4j
public class InitialEngineSystem implements Serializable {
	private static final long serialVersionUID = -1200100000000001L;
	@Autowired
	private RedisMQBroker redisMQBroker;

	@Autowired
	private HttpSQSServer httpSQSServer;

	@Autowired
	private RedisSubReceiver redisSubReceiver;

	@Autowired
	private RedisPubSender redisPubSender;
	
	@Autowired
	private RedisMQPushSender redisMQPushSender;

	public InitialEngineSystem() {
	}

	private String message;

	/**
	 * 初始化方法
	 */
	public void init() {
		log.info("----------------------------Museum-Engine-System is starting-------------------------------");

		if (redisMQBroker == null)
			System.out.println("Redis消息队列代理为空");
		else {

			try {
				RedisMQBlpopReceiver redisMQBlpopReceiver = new RedisMQBlpopReceiver();
				registeredListener(redisMQBlpopReceiver);

				registeredRuleMsgListener(redisSubReceiver);
				redisMQBroker.startListenRedisMQ(redisMQBlpopReceiver);
				redisMQBroker.startListenRedisSub(redisSubReceiver);
			} catch (Exception e) {
				log.error(ExceptionLog.getErrorStack(e));
				throw new RuntimeException();
			}

		}

		if (httpSQSServer == null)
			System.out.println("httpSQSServer为空");
		else {

			try {
				httpSQSServer.runServer();
			} catch (Exception e) {
				log.error(ExceptionLog.getErrorStack(e));
				throw new RuntimeException();
			}

		}

		log.info("规则引擎已经启动");

		System.out.println("规则引擎已经启动");
		System.out.println("消息系统已经启动");
		log.info("----------------------------Museum-Engine-System is started-------------------------------");
		//TODO:
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				int i = 0;
//				while(true){
////					String json = "{\"msgHeader\":{\"sendFrom\":\"ENGINE\",\"sendTo\":\"NETTY\",\"timeStamp\":1449135898761,\"priority\":0,\"deliveryMode\":\"PTP\",\"qos\":0},\"msgProperty\":{\"cmdType\":\"ANSWER\",\"applicationType\":\"SENSOR\"},\"msgBody\":{\"sensorMsgType\":\"TEXT\",\"sensorAppType\":\"ENGINE_POST_SENSOR_MAC_LIST\",\"sensorVersion\":\"V2\",\"qos\":0,\"data\":{\"mtmNetInfo\":{\"topologyID\":\"0061\",\"sensorNetInfos\":[{\"sensorMacAddr\":\"11AA1E0BC5D5\",\"rssi\":0},{\"sensorMacAddr\":\"12AA1E0BC4D5\",\"rssi\":0},{\"sensorMacAddr\":\"13AA1E0BC3D5\",\"rssi\":0},{\"sensorMacAddr\":\"14AA1E0BC2D5\",\"rssi\":0},{\"sensorMacAddr\":\"15AA1E0BC1D5\",\"rssi\":0},{\"sensorMacAddr\":\"16AA1E9BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"17AA1E8BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"18AA1E7BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"19AA1E6BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"20AA1E5BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"21AA1E4BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"22AA1E3BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"23AA1E2BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"24AA1E1BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"67B677A899EE\",\"rssi\":0},{\"sensorMacAddr\":\"26AA8E0BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"27AA7E0BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"28AA6E0BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"29AA5E0BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"30AA4E0BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"31AA3E0BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"32AA2E0BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"0F154C9700E2\",\"rssi\":0},{\"sensorMacAddr\":\"348A1E0BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"357A1E0BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"366A1E0BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"375A1E0BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"354A1E0BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"283A1E0BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"392A1E0BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"401A1E0BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"41AA1E0BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"42AA1E0BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"43AA1E0BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"44AA1E0BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"45AA1E0BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"46AA1E0BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"47AA1E0BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"48AA1E0BC7D5\",\"rssi\":0},{\"sensorMacAddr\":\"10A11E0BC6D5\",\"rssi\":0}],\"sensorNetTopology\":{}}}},\"klassName\":\"com.qingruan.museum.msg.sensor.SensorMsg\"}";
////					redisMQPushSender.sendEngineToNetty(json);	
//					String json2 = "{\"number\":"+i+"}";
//					redisMQPushSender.sendEngineToNetty(json2);	
//					try {
//						Thread.sleep(2000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					i++;
//				}
//			}
//		}).start();
	}

	/**
	 * 关闭方法
	 */
	public void destory() {
		log.info("----------------------------httpSQSServer 被关闭-------------------------------");
		if (httpSQSServer == null)
			System.out.println("协议栈-Netty为空");
		else
			httpSQSServer.shutDownHttpSQSServer();
		;
	}

	/**
	 * 注册Engine接收Netty消息
	 * 
	 * @param redisMQBlpopReceiver
	 */
	protected void registeredListener(RedisMQBlpopReceiver redisMQBlpopReceiver) {
		try {
			System.out.println("开始注册消息队列NettyToEngineMsgListener");
			redisMQBlpopReceiver
					.setMessageListener(new NettyToEngineMsgListener());
			System.out.println("结束注册消息队列NettyToEngineMsgListener");
		} catch (Exception e) {

			e.printStackTrace();
			throw new RuntimeException(
					"{registeredListener runtime exception!}");
		}

	}

	/**
	 * 注册Engine接收rule-admin pub的消息
	 * 
	 * @param redisSubReceiver
	 */
	protected void registeredRuleMsgListener(RedisSubReceiver redisSubReceiver) {
		try {
			System.out.println("开始注册消息队列AdminToEngineMsgListener");

			redisSubReceiver.setSubcribeListener(
					new AdminToEngineMsgListener(), "ENGINE_RULE_UPDATE");
			System.out.println("结束注册消息队列AdminToEngineMsgListener");
		} catch (Exception e) {

			e.printStackTrace();
			throw new RuntimeException(
					"{registeredListener runtime exception!}");
		}

	}
}
