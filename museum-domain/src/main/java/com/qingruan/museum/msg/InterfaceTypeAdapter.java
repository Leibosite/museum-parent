package com.qingruan.museum.msg;

import java.lang.reflect.Type;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.qingruan.museum.msg.MsgHeader;
import com.qingruan.museum.msg.MsgProperty;
import com.qingruan.museum.msg.MuseumMsg;

public class InterfaceTypeAdapter implements JsonDeserializer<MuseumMsg>{
	
	@Override
	public MuseumMsg deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		MuseumMsg museumMsg = new MuseumMsg();
		try {
			if (json.isJsonObject()) {
	            JsonObject jsonObject = json.getAsJsonObject();
	            
	            JsonElement e1 = jsonObject.get("msgBody");
	            JsonElement klassElement = jsonObject.get("klassName");
	            if (e1 != null && klassElement!=null) {
	            	
	            	String klassName = klassElement.toString(); 
	            	klassName = klassName.substring(1, klassName.length()-1);
	            	 Class<?> klass = Class.forName(klassName);
					
					Type type = (Type)klass;
	            	MsgBody msgBody = context.deserialize(e1,type); 
	            	museumMsg.setMsgBody(msgBody);
	            } else {
	            	museumMsg.setMsgBody(null);
	            }
	            
	            JsonElement e2 = jsonObject.get("msgHeader");
	            if (e2 != null) {
	            	@SuppressWarnings("serial")
					Type type = new TypeToken<MsgHeader>() {}.getType();
					MsgHeader msgHeader = context.deserialize(e2,type);
					museumMsg.setMsgHeader(msgHeader);
	            } else {
	            	museumMsg.setMsgHeader(new MsgHeader());;
	            }
	            JsonElement e3 = jsonObject.get("msgProperty");
	            if (e3 != null) {
	            	@SuppressWarnings("serial")
					Type type = new TypeToken<MsgProperty>() {}.getType();
					MsgProperty msgProperty = context.deserialize(e3,type); 
					museumMsg.setMsgProperty(msgProperty);
	            } else {
	            	museumMsg.setMsgProperty(new MsgProperty());
	            }
			} 
		} catch (ClassNotFoundException e) {
			return museumMsg;
		}
		return museumMsg;
	}

}
