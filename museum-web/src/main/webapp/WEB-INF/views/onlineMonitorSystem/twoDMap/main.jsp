<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>二维平面图</title>
<%-- <%@ include file="<%=basePath %>/common/taglibs.jsp"%>
<%@ include file="<%=basePath %>/common/scripts.jsp"%>
<%@ include file="<%=basePath %>/common/styles.jsp"%> --%>

<%@ include file="../../common/taglibs.jsp"%>
<%@ include file="../../common/scripts.jsp"%>
<%@ include file="../../common/styles.jsp"%>

<style type="text/css">
body{
	font-family: tahoma, 'microsoft yahei', 微软雅黑;
}
ul, li {
	margin: 0;
	padding: 0;
	list-style: none;
}

#alarmContent {
	border: 1px solid #ddd;
	height: 1px;
	width: 100%;
	overflow: hidden;
}

.ulCss li {
	height: 20px;
	line-height: 30px;
	font-size: 12px;
	text-align: center;
}

.span10 {
	position: relative;
}

#map {
	float: left;
	z-index: 0;
	position: absolute;
	top: 0px;
	left: 0px;
}

#model {
	width: 100%;
	height: 100%;
	background-color: #CCC;
	float: left;
	z-index: 2;
	position: absolute;
	top: 0px;
	left: 0px;
	opacity: 0.5;
	display: none;
	border-radius: 5px;
}

#contentParent {
	position: absolute;
	z-index: 3;
	top: 20px;
	left: 50px;
	width: 90%;
	height: 90%;
	display: none;
	margin: auto;
}

.leftListName p{
	
	margin-top: 10px;
	padding-left: 20px;	
	padding-top: 5px;
	padding-bottom: 5px; 
	font-size: 16px;
	cursor: pointer;
	/* background-color: #DEDDE3; */
} 
#mdtitle{
	width: 100%; 
	height: 4%;
	background: #CCC; 
	border-radius: 5px;
	font-size: 20px; 
	padding-top: 8px;
}
#mdtitle span{
	padding-left: 25px
}
ul, ol{
	padding:0px;
	margin: 1px 0 0px 0px;
}
</style>
<script type="text/javascript">
	//滚动告警信息
	var i = 0;
	$(function() {
		$("#map").load("map");
		
		window.setInterval(function() {
			i++;
			$("#alarmContent").animate({
				scrollTop : (i * 30) + "px"
			}, 'slow');
		}, 2000);
	});

	//显示model
	function showModel(href) {
		if (href == "/onlineMonitorSystem/devices/list") {
			$("#titleName").html("设备");
			$("#devices").css("backgroundColor", "#DEDDE3");
			$("#locations").css("backgroundColor", "");
			$("#culturalRelics").css("backgroundColor", "");
			$("#warns").css("backgroundColor", "");
			$("#deviceBattery").css("backgroundColor", "");
		}
		if (href == "/onlineMonitorSystem/locations/list") {
			$("#titleName").html("区域");
			$("#devices").css("backgroundColor", "");
			$("#locations").css("backgroundColor", "#DEDDE3");
			$("#culturalRelics").css("backgroundColor", "");
			$("#warns").css("backgroundColor", "");
			$("#deviceBattery").css("backgroundColor", "");
		}
		if (href == "/onlineMonitorSystem/culturalRelics/list") {
			$("#titleName").html("文物");
			$("#devices").css("backgroundColor", "");
			$("#locations").css("backgroundColor", "");
			$("#culturalRelics").css("backgroundColor","#DEDDE3");
			$("#warns").css("backgroundColor", "");
			$("#deviceBattery").css("backgroundColor", "");
		}
		if (href == "/onlineMonitorSystem/warns/list") {
			$("#titleName").html("告警日志");
			$("#devices").css("backgroundColor", "");
			$("#locations").css("backgroundColor", "");
			$("#culturalRelics").css("backgroundColor", "");
			$("#warns").css("backgroundColor", "#DEDDE3");
			$("#deviceBattery").css("backgroundColor", "");
		}
		if (href == "/onlineMonitorSystem/deviceBattery/list") {
			$("#titleName").html("设备电量");
			$("#devices").css("backgroundColor", "");
			$("#locations").css("backgroundColor", "");
			$("#culturalRelics").css("backgroundColor", "");
			$("#warns").css("backgroundColor", "");
			$("#deviceBattery").css("backgroundColor", "#DEDDE3");
		}
		$("#model").fadeIn(300,function() {
			$("#contentParent").fadeIn(300); 
			$("#contentDiv").load("${ctx}" + href);
		});
	}
	//关闭model
	function closeModel() {
		$("#contentParent").fadeOut(300, function() {
			$("#model").fadeOut(300);
		});
		$("#devices").css("backgroundColor", "");
		$("#locations").css("backgroundColor", "");
		$("#culturalRelics").css("backgroundColor", "");
		$("#warns").css("backgroundColor", "");
		$("#deviceBattery").css("backgroundColor", "");
	}

	//改变地图位置楼层事件
	function changeFloor(value) {
		$("#showFloor").html(value);
	}
</script>
</head>
<body>
	<input id="ctx" type="hidden" value="${ctx}">
	<div class="container" style="height: 100%; width: 95%">
		<div class="row-fluid">
			<div class="span12"
				style="background-color: #EBEAEF; border-radius: 5px; margin: auto; margin-top: 10px; margin-bottom: 10px">
				<a href="#" style="margin-left: 20px; margin-right: 20px">您现在所在的位置是：在线监测地图系统>>检测地图
				</a> 甘肃省博物馆>><span id="showFloor">一楼</span> 
				<span style="margin-left: 600px;margin-top: 5px">地图位置</span> 
				<select style="line-height: inherit;margin-top: 5px" onchange="changeFloor(value)">
					<option value="一楼">一楼</option>
					<option value="二楼">二楼</option>
					<option value="三楼">三楼</option>
					<option value="四楼">四楼</option>
				</select>
			</div>
		</div>

		<div class="row-fluid">
			<div class="span2" style="height: 900px;">

				<div class="well" style="padding: 8px 0; margin-bottom: 0; height: 48%; background-color: #EBEAEF; margin-bottom: 12px">
					
					<div class="leftListName">
						<P  id="devices" onclick="showModel('/onlineMonitorSystem/devices/list')"><a>设备</a></P>
						<P  id="locations"      onclick="showModel('/onlineMonitorSystem/locations/list')"><a>区域</a></P>
						<P  id="culturalRelics" onclick="showModel('/onlineMonitorSystem/culturalRelics/list')"><a>文物</a></P>
						<P  id="warns" onclick="showModel('/onlineMonitorSystem/warns/list')"><a>告警日志</a></P>
						<P  id="deviceBattery" onclick="showModel('/onlineMonitorSystem/deviceBattery/list')"><a>设备电量</a></P>
					</div>  
					
				</div>

				<div
					style="background-color: #EBEAEF; height: 48%; border-radius: 5px;">
					<div id="alarmContent"
						style="height: 350px; text-align: center; border: 0">
						<ul class="ulCss" id="messages"></ul>
					</div>
					<hr />
					<P>
						<a style="margin-left:20px;margin-right: 20px">历史记录</a> <img
							alt="" src="${ctx}/static/d3/img/redIcon.png"><span
							class="badge badge-important">99+</span> <img alt=""
							src="${ctx}/static/d3/img/yellowIcon.png"><span
							class="badge badge-warning">10</span>
					</P>

				</div>
			</div>
			<div class="span10" style="height: 900px; border-radius: 5px;">
				<!--页面加载时加载地图-->
				<!--<div id="map"></div>--> 
				<div src="" id="map" style="width: 100%; height: 100%; border: 0px"></div>

				<!--model层-->
				<div id="model"></div>

				<!--model层上面-->
				<div id="contentParent">
					<!--关闭按钮-->
					<div id="mdtitle">
						<span id="titleName"></span>
						<img onclick="closeModel()"  src="<%=basePath %>/static/d3/img/closeBtn.png" style="float: right;width: 20px;height: 20px; margin-right: 5px;cursor: pointer;">
					</div>

					<!--加载jsp页面-->
					<div src="" id="contentDiv" style="width: 100%; height: 100%;background-color: white;"></div>
				</div>

			</div>
		</div>
	</div>

	<script type="text/javascript">
		//ws://10.0.1.67:8989/websocket                          ws://localhost:8080/museum-web/websocket
		var webSocket = new WebSocket('ws://127.0.0.1:8989/websocket');

		webSocket.onerror = function(event) {
			onError(event)
		};

		webSocket.onopen = function(event) {
			onOpen(event)
		};

		webSocket.onmessage = function(event) {
			onMessage(event)
		};

		function onMessage(event) {
			i++;
			document.getElementById('messages').innerHTML += '<li lang='+'2012-2-2'+'>接收的'
					+ event.data + "</li>";
		}

		/* (a>b)?true:false */
		function onOpen(event) {
			/* document.getElementById('messages').innerHTML = '连接建立'; */
		}

		function onError(event) {
			/* alert(event.data); */
		}

		function start() {
			webSocket.send('hello');
			return false;
		}
	</script>
</body>
</html>
