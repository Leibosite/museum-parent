package com.qingruan.museum.admin.web.websocket;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CommonData {
	public static ConcurrentHashMap<String, Channel> deviceChannels = new ConcurrentHashMap<String, Channel>();
	public static List<Channel> browserChannels = new ArrayList<Channel>();
}
