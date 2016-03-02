package com.qingruan.museum.netty.websocket;

import static io.netty.buffer.Unpooled.*;
import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

public class WebSocketTestPage {
	 private static final String NEWLINE = "\r\n";

	    public static ByteBuf getContent(String webSocketLocation) {	    	
	        
	    	return copiedBuffer(
	        "<html><head><title>Web Socket Test</title></head>" + NEWLINE +
	        "<body>" + NEWLINE +
	        "<script type=\"text/javascript\">" + NEWLINE +
	        "var socket;" + NEWLINE +
	        "if (window.WebSocket) {" + NEWLINE +
	        "  socket = new WebSocket(\"" + webSocketLocation + "\");" + NEWLINE +
	        "  socket.onmessage = function(event) { alert(event.data); };" + NEWLINE +
	        "  socket.onopen = function(event) { alert(\"Web Socket opened!\"); };" + NEWLINE +
	        "  socket.onclose = function(event) { alert(\"Web Socket closed.\"); };" + NEWLINE +
	        "} else {" + NEWLINE +
	        "  alert(\"Your browser does not support Web Socket.\");" + NEWLINE +
	        "}" + NEWLINE +
	        "" + NEWLINE +
	        "function send(message) {" + NEWLINE +
	        "  if (!window.WebSocket) { return; }" + NEWLINE +
	        "  if (socket.readyState == WebSocket.OPEN) {" + NEWLINE +
	        "    socket.send(message);" + NEWLINE +
	        "  } else {" + NEWLINE +
	        "    alert(\"The socket is not open.\");" + NEWLINE +
	        "  }" + NEWLINE +
	        "}" + NEWLINE +
	        "</script>" + NEWLINE +
	        "<form onsubmit=\"return false;\">" + NEWLINE +
	        "<input type=\"text\" name=\"message\" value=\"Hello, World!\"/>" +
	        "<input type=\"button\" value=\"Send Web Socket Data\" onclick=\"send(this.form.message.value)\" />" + NEWLINE +
	        "</form>" + NEWLINE +
	        "</body>" + NEWLINE +
	        "</html>" + NEWLINE,
	        CharsetUtil.US_ASCII);
	    }
}
