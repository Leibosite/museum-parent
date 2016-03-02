package com.qingruan.museum.netty.httpsqs;

import static io.netty.buffer.Unpooled.copiedBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

import com.google.gson.Gson;
import com.qingruan.framework.redis.ShardedJedisSentinelPoolTemplate;
import com.qingruan.framework.redis.ShardedJedisSentinelPoolTemplate.ShardedJedisSentinelPoolCallback;
import com.qingruan.museum.domain.model.DomainBase;
import com.qingruan.museum.engine.framework.ApplicationContextGuardian;
import com.qingruan.museum.framework.jackson.JSONUtil;
import com.qingruan.museum.msg.notification.AreaDataMsg;

@Component("httpSQSResultSet")
public class HttpSQSResultSet {
	
	@Autowired
	private static ShardedJedisSentinelPoolTemplate jedisSentinelTemplate;
	private static Gson gson = new Gson();
	private final static String HTTPSQS = "http_simple_queue_service";
	
	static{
		final ApplicationContextGuardian applicationContextGuardian = ApplicationContextGuardian
				.getInstance();
		jedisSentinelTemplate = (ShardedJedisSentinelPoolTemplate) applicationContextGuardian
				.GetAppContext().getBean(ShardedJedisSentinelPoolTemplate.class);
	}
		
	public static ByteBuf getAreaDataMsg(final String arg){
		
		if("list".equals(arg)){
			String jsonString = jedisSentinelTemplate.run(HTTPSQS, new ShardedJedisSentinelPoolCallback<String>() {

				@Override
				public String execute(Jedis paramJedis) {
					Set<String> areaDataMsg = paramJedis.keys(AreaDataMsg.class.getName()+":*");
					String[] keys =new String[areaDataMsg.size()];
					areaDataMsg.toArray(keys);
					List<String> areaDataList = paramJedis.mget(keys);
					List<AreaDataMsg> msgsList = new ArrayList<AreaDataMsg>();
					for (String string : areaDataList) {
						AreaDataMsg areaDataMsgObject = gson.fromJson(string, AreaDataMsg.class);
						msgsList.add(areaDataMsgObject);
					}
					
					String areaDataJson = gson.toJson(msgsList);
					return areaDataJson;
				}
			});
			
			ByteBuf content = copiedBuffer(jsonString,CharsetUtil.UTF_8);
			return content;
			
		}else{
			
			String jsonString = jedisSentinelTemplate.run(HTTPSQS, new ShardedJedisSentinelPoolCallback<String>() {

				@Override
				public String execute(Jedis paramJedis) {
					String areaDataMsg = paramJedis.get(AreaDataMsg.class.getName()+":"+arg);
					if(areaDataMsg==null){
						return "NO_DATA";
					}

					return areaDataMsg;
				}
			});
			ByteBuf content = copiedBuffer(jsonString,CharsetUtil.UTF_8);
			return content;
		}	
	}
	
	public static ByteBuf getErrorMsg(){
		DomainBase domainBase=new DomainBase();
		domainBase.setResultCode(-1);
		domainBase.setMsg("error in server ,please wait!");
		ByteBuf content = copiedBuffer(JSONUtil.serialize(domainBase),CharsetUtil.UTF_8);
		return content;
	}
	
	public static ByteBuf getConstantThMsg(final String arg){
		//TODO
		DomainBase domainBase=new DomainBase();
		domainBase.setResultCode(2001);
		domainBase.setMsg("success");
		ByteBuf content = copiedBuffer(JSONUtil.serialize(domainBase),CharsetUtil.UTF_8);
		return content;
	}
}
