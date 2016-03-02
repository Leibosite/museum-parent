package com.qingruan.museum.netty.httpsqs;

import static io.netty.buffer.Unpooled.copiedBuffer;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;

import io.netty.buffer.ByteBuf;
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
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.qingruan.museum.domain.models.enums.MonitorDataType;
import com.qingruan.museum.engine.MuseumEngine;
import com.qingruan.museum.engine.service.rest.RestfulAppService;
import com.qingruan.museum.engine.service.rest.RestfulEngineService;
import com.qingruan.museum.engine.service.rest.RestfulService;

//TODO:重构Dispatcher
@Slf4j
@Service
public class HttpSQSHandler extends ChannelInboundHandlerAdapter {

	private static RestfulService restfulService;
	private static RestfulAppService restfulAppService;
	private static RestfulEngineService restfulEngineService;
	private static RestfulAction action;

	static {		
		ApplicationContext context = MuseumEngine.applicationContextGuardian.GetAppContext();
		restfulService = (RestfulService)context.getBean(RestfulService.class);
		restfulAppService = (RestfulAppService) context.getBean(RestfulAppService.class);
		restfulEngineService = (RestfulEngineService) context.getBean(RestfulEngineService.class);
		action = new RestfulAction(restfulEngineService);
	}

	final static String SENSOR_UPGRADE_MSG = "sensor_upgrade_msg";
	final static String AREA_DATA_MSG = "area_data_msg";
	final static String CONSTANT_TH_MSG = "constant_th_msg";
	final static String WEATHER_STATION_MSG = "weatherStationMsg";
	final static String AIR_CLEANER_MSG = "airCleanerMsg";
	final static String OVER_VIEW = "overView";
	final static String REAL_TIME = "realTime";
	final static String HISTORY = "history";
	final static String MUSEUM_OVERVIEW_DATA = "museum_overview_data";
	final static String SHOW_ROOM_DETAIL = "show_room_detail";
	final static String MONITOR_OBJECT_DATA = "monitor_object_data";

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		if (msg instanceof FullHttpRequest) {
			handleHttpRequest(ctx, (FullHttpRequest) msg);
		}
	}

	// TODO:需要在此处实现Dispatcher功能
	/**
	 * HttpSQS 全部用GET请求
	 * 
	 * @param ctx
	 * @param req
	 */
	public void handleHttpRequest(final ChannelHandlerContext ctx,
			FullHttpRequest req) {
		log.info("####### HTTP RESQUEST #######");

//		if (req.getMethod() != HttpMethod.GET) {
//			ByteBuf content = req.content();
//			FullHttpResponse res = new DefaultFullHttpResponse(
//					HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, content);
//			sendHttpResponse(ctx, req, res);
//			return;
//		}

		ByteBuf content = null;

		try {
			log.info("####### HTTP RESQUEST #######");

			String uri = req.getUri();
			log.info("URL is: " + uri);

			String[] uriList = uri.split("/");
			log.info("URL length is: " + uriList.length);

			log.info("URL length is: " + uriList.length);

			if (uriList.length >= 3 && AREA_DATA_MSG.equals(uriList[1])
					&& !"".equals(uriList[2])) {
				content = HttpSQSResultSet.getAreaDataMsg(uriList[2]);
			} else if (uriList.length >= 3
					&& CONSTANT_TH_MSG.equals(uriList[1])
					&& !"".equals(uriList[2])) {

				content = restfulService.getConstantThMsg(uriList[2]);
			} else if (uriList.length >= 3
					&& SENSOR_UPGRADE_MSG.equals(uriList[1])
					&& !"".equals(uriList[2])) {
				log.info("uriList[2] is: " + uriList[2]);
				content = HttpSQSResultSet.getConstantThMsg(uriList[2]);
			} else if (uriList.length >= 3
					&& WEATHER_STATION_MSG.equals(uriList[1])
					&& !"".equals(uriList[2])) {

				log.info("-------------handleHttpRequest{weather msg}--------------url");
				log.info(uriList[2]);
				// 气象站数据有三类：数据概况、实时数据、历史数据
				switch (uriList[2]) {
				case OVER_VIEW:
					Long weatherStationId = Long.parseLong(uriList[3]);
					content = restfulService
							.getOverViewWeatherData(weatherStationId);
					break;
				case REAL_TIME:
					Long weatherStationId1 = Long.parseLong(uriList[3]);
					content = restfulService
							.getRealtimeWeatherData(weatherStationId1);

					break;
				case HISTORY:
					log.info("-------------handleHttpRequest{weather msg}-{history}--------------url is");
					log.info(uriList[3]);
					String param = uriList[3];
					if (StringUtils.isBlank(param))
						// TODO:
						content = null;
					try {
						String[] params = param.split("&");
						log.info("-------------handleHttpRequest{weather msg}-{history}--------------param.split[] is");
						log.info(params.toString());
						if (params == null || params.length != 4)
							content = null;

						Long id = Long.parseLong(params[0].split("=")[1]);
						String paramArray[] = params[1].split("=");
						if (paramArray == null | paramArray.length != 2)
							content = null;

						String granulariy = paramArray[1];

						paramArray = params[2].split("=");
						if (paramArray == null | paramArray.length != 2)
							content = null;

						String type = paramArray[1];
						MonitorDataType monitorDataType = null;
						try {
							monitorDataType = MonitorDataType.valueOf(type);
						} catch (Exception e) {
							content = null;
						}

						paramArray = params[3].split("=");
						if (paramArray == null | paramArray.length != 2)
							content = null;
						Long timeStamp = Long.parseLong(paramArray[1]);

						content = restfulService.getHistoryWeatherData(id,
								granulariy, monitorDataType, timeStamp);

						break;

					} catch (Exception e) {
						// TODO: handle exception
					}

				default:
					break;
				}

				// content = HttpSQSResultSet.getAreaDataMsg(uriList[2]);
			} else if (uriList.length >= 3
					&& AIR_CLEANER_MSG.equals(uriList[1])
					&& !"".equals(uriList[2])) {

				log.info("-------------handleHttpRequest{air cleaner msg}--------------url");
				log.info(uriList[2]);
				// 气象站数据有三类：数据概况、实时数据、历史数据
				switch (uriList[2]) {
				case OVER_VIEW:
					Long airCleanerId = Long.parseLong(uriList[3]);
					content = restfulService
							.getOverViewAirCleanerData(airCleanerId);

					break;
				case REAL_TIME:
					Long weatherStationId1 = Long.parseLong(uriList[3]);
					content = restfulService
							.getRealtimeAirCleanerData(weatherStationId1);

					break;
				case HISTORY:
					log.info("-------------handleHttpRequest{weather msg}-{history}--------------url is");
					log.info(uriList[3]);
					String param = uriList[3];
					if (StringUtils.isBlank(param))
						content = null;
					try {
						String[] params = param.split("&");
						log.info("-------------handleHttpRequest{weather msg}-{history}--------------param.split[] is");
						log.info(params.toString());
						if (params == null || params.length != 4)
							content = null;

						Long id = Long.parseLong(params[0].split("=")[1]);
						String granulariy = params[1].split("=")[1];
						MonitorDataType monitorDataType = MonitorDataType
								.valueOf(params[2].split("=")[1]);

						Long timeStamp = Long
								.parseLong(params[3].split("=")[1]);
						content = restfulService.getHistoryAirCleanerData(id,
								granulariy, monitorDataType, timeStamp);

						break;

					} catch (Exception e) {
						// TODO: handle exception
					}

				default:
					break;
				}

				// content = HttpSQSResultSet.getAreaDataMsg(uriList[2]);
			} else if (uriList.length >= 3
					&& MUSEUM_OVERVIEW_DATA.equals(uriList[1])
					&& "overview".equals(uriList[2])) {

				String data = restfulAppService.fetchMuseumOverviewData();
				content = copiedBuffer(data, CharsetUtil.UTF_8);
			} else if (uriList.length >= 3
					&& SHOW_ROOM_DETAIL.equals(uriList[1])
					&& !"".equals(uriList[2])) {
				int id = Integer.valueOf(uriList[2]);
				// double score = Double.valueOf(uriList[3]);
				String data = restfulAppService.fetchShowroomDetailData(id);
				content = copiedBuffer(data, CharsetUtil.UTF_8);

			} else if (uriList.length >= 4
					&& MONITOR_OBJECT_DATA.equals(uriList[1])
					&& !"".equals(uriList[2]) && !"".equals(uriList[3])) {
				int id = Integer.valueOf(uriList[2]);
				int objectType = Integer.valueOf(uriList[3]);
				String data = restfulAppService.fetchMonitorDataByObjectType(
						objectType, id);
				content = copiedBuffer(data, CharsetUtil.UTF_8);
			}

			else {

				String s = "http://127.0.0.1:8090";
				String stringUrl = req.getUri();
				stringUrl = s + stringUrl;
				URL urlObject = new URL(stringUrl);
				String query = urlObject.getQuery();

				if (null == query) {
					content = HttpSQSResultSet.getErrorMsg();
					FullHttpResponse res = new DefaultFullHttpResponse(
							HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
							content);
					res.headers().set("Content-Type",
							"text/json; charset=UTF-8");
					sendHttpResponse(ctx, req, res);
					return;
				}
				MultiMap<String> mapValues = new MultiMap<String>();
				UrlEncoded.decodeTo(query, mapValues, "UTF-8", 100);
				String path = urlObject.getPath();
				// 获取单例的action
				// RestfulAction action = HttpSQSServer.action;

//				RestfulAction action = (RestfulAction) MuseumEngine.applicationContextGuardian
//						.GetAppContext().getBean(RestfulAction.class);

				Object object = HttpSQSServer.urlAndActionMethondMapingMap
						.get(path);
				if (null == object) {
					content = HttpSQSResultSet.getErrorMsg();
				}
				if (object instanceof String) {
					String menthodName = (String) object;
					Method method = RestfulAction.class.getMethod(menthodName,Map.class);
					try {
						Object invokeRseult = method.invoke(action, mapValues);
						if (invokeRseult instanceof ByteBuf) {
							content = (ByteBuf) invokeRseult;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				

				}

			}
			

			FullHttpResponse res = new DefaultFullHttpResponse(
					HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
			
			res.headers().set("Access-Control-Allow-Origin", "*");
			res.headers().set("Access-Control-Allow-Methods", "POST");
			res.headers().set("Access-Control-Allow-Methods", "GET");
			res.headers().set("Access-Control-Allow-Headers",
					"x-requested-with,content-type");

			res.headers().set("Content-Type", "text/json; charset=UTF-8");
			sendHttpResponse(ctx, req, res);

		} catch (Exception e) {

			content = copiedBuffer(e.toString(), CharsetUtil.UTF_8);
			FullHttpResponse res = new DefaultFullHttpResponse(
					HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

			res.headers().set("Access-Control-Allow-Origin", "*");
			res.headers().set("Access-Control-Allow-Methods", "POST");
			res.headers().set("Access-Control-Allow-Methods", "GET");
			res.headers().set("Access-Control-Allow-Headers",
					"x-requested-with,content-type");

			res.headers().set("Content-Type", "text/json; charset=UTF-8");
			sendHttpResponse(ctx, req, res);
			log.error(e.getLocalizedMessage());
		}

	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) {
		// 在浏览器与websocket server之间断开时，要把相应的通道移除

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// 在浏览器与websocket server之间的通道发生错误之后，要把发生错误的通道移除

	}

	private void sendHttpResponse(ChannelHandlerContext ctx,
			FullHttpRequest req, FullHttpResponse res) {
		// Generate an error page if response status code is not OK (200).
		HttpHeaders.setContentLength(res, res.content().readableBytes());

		// Send the response and close the connection if necessary.
		ChannelFuture f = ctx.channel().writeAndFlush(res);

		if (!HttpHeaders.isKeepAlive(req) || res.getStatus().code() != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}
}
