package com.qingruan.museum.admin.web.alarm;

import java.io.IOException;
import java.util.Date;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket")
public class AlarmWebSocketController {
	@OnMessage
	public void onMessage(String message, Session session) throws IOException,
			InterruptedException {
		System.out.println("收到了: " + message);
		session.getBasicRemote().sendText("这是第一个服务器信息");
		int sentMessages = 0;
		while (true) {
			Thread.sleep(2000);
			session.getBasicRemote().sendText("接收的信息" + new Date());
		}
	}

	@OnOpen
	public void onOpen() {
		System.out.println("客户端连接");
	}

	@OnClose
	public void onClose() {
		System.out.println("连接关闭");
	}
}