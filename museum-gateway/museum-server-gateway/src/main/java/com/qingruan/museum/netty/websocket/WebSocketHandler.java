package com.qingruan.museum.netty.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

import java.util.Iterator;

import com.qingruan.museum.netty.tcpserver.CommonData;

public class WebSocketHandler extends ChannelInboundHandlerAdapter{
	
	private static final String WEBSOCKET_PATH = "/websocket";
	
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		if(msg instanceof FullHttpRequest){
			handleHttpRequest(ctx, (FullHttpRequest)msg);
		}else if (msg instanceof TextWebSocketFrame) {
			handleWebSocketFrame(ctx,(TextWebSocketFrame)msg);
		}
	}
	
	public static void pushMsg(String msg){
		Iterator<Channel> websockets = CommonData.browserChannels.iterator();
		while(websockets.hasNext()){
			Channel webSocketChannel = websockets.next();
			if (webSocketChannel.isActive()) {
				webSocketChannel.writeAndFlush(new TextWebSocketFrame(msg));
			}	
		}
	}
	
	//用这个方法向外推送信息
    private void handleWebSocketFrame(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        ctx.channel().writeAndFlush(frame);
    }
	
	public void handleHttpRequest(final ChannelHandlerContext ctx,FullHttpRequest req){
		if(req.getMethod() != HttpMethod.GET){	
			ByteBuf content = req.content();
			FullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, content);
			sendHttpResponse(ctx, req, res);
            return;
		}
		
		if(req.getUri().equals("/")){
			ByteBuf content = WebSocketTestPage.getContent(getWebSocketLocation(req));
			FullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
			res.headers().set("Content-Type", "text/html; charset=UTF-8");
			sendHttpResponse(ctx, req, res);
			return;
		}
		
//		System.out.println("Uri" + req.getUri());
//   	 	System.out.println("Values.UPGRADE : " + req.headers().get("Connection"));
//   	 	System.out.println("Names.UPGRADE :" + req.headers().get("Upgrade"));
		
   	 	if(req.getUri().equals("/websocket")){
			WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
	                getWebSocketLocation(req), null, true);
	        WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);  //这里会根据不同的websocket版本来安排不同的握手handler
	        
	        if (handshaker == null) {
	            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());;
	        } else {
	        	//其实这里在将用于建立连接的http报文发送回去之后，会将前面添加的http部分的handler都移除，然后加上用于decode和encode针对websocket帧的handler
	            ChannelFuture handshakeFuture = handshaker.handshake(ctx.channel(), req);   //这里说白了就是进行握手，向客户端返回用于建立连接的报文
	            handshakeFuture.addListener(new ChannelFutureListener() {
	                @Override
	                public void operationComplete(ChannelFuture future) throws Exception {
	                    if (!future.isSuccess()) {
	                        ctx.fireExceptionCaught(future.cause());
	                    } else {
	                    	//用于激活握手已经完成的事件，可以让用户的代码收到通知
	                        ctx.fireUserEventTriggered(WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE);
	                        //把已经握手的建立好的websocket通道缓存起来
	                        CommonData.browserChannels.add(ctx.channel());
	                    }
	                }
	            });
	        }
   	 	}
	}
	
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx){
		//在浏览器与websocket server之间断开时，要把相应的通道移除
		boolean isExist = CommonData.browserChannels.contains(ctx.channel());
		if(isExist){
			CommonData.browserChannels.remove(ctx.channel());
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
		//在浏览器与websocket server之间的通道发生错误之后，要把发生错误的通道移除
		boolean isExist = CommonData.browserChannels.contains(ctx.channel());
		if(isExist){
			CommonData.browserChannels.remove(ctx.channel());
		}
	}
	
	private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        // Generate an error page if response status code is not OK (200).     
        HttpHeaders.setContentLength(res,res.content().readableBytes());
        
        // Send the response and close the connection if necessary.
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        
        if (!HttpHeaders.isKeepAlive(req) || res.getStatus().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }
	
	private String getWebSocketLocation(FullHttpRequest req) {
        return "ws://" + req.headers().get("Host")+ WEBSOCKET_PATH;
    }

}
