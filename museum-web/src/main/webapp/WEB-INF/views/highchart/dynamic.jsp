	<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>websocket动态推送数据</title>
<script type="text/javascript" src="${staticFile}/js/highcharts/highcharts.js"></script>
<script type="text/javascript" src="${staticFile}/js/highcharts/modules/exporting.js"></script>
<script type="text/javascript" src="${staticFile}/js/highcharts/themes/sand-signika.js"></script>
<script type="text/javascript">
	var chart;
	$(function() {
		$(document).ready(

				function() {

					Highcharts.setOptions({ //设置主题  只需要引入    gray.js   即可                                                
						global : {
							useUTC : false
						}
					});

					chart = new Highcharts.Chart(
							{
								chart : {
									renderTo : 'testHighchart',
									type : 'spline', //设置类型  曲线   饼图  柱状                                             
									animation : Highcharts.svg, // don't animate in old IE               
									marginRight : 10,
									backgroundColor: '#8F8F8F',
									events : {
										load : function() {
											var series = this.series[0];
											/* setInterval(function() {
												var x = (new Date()).getTime(), // current time         
												y = Math.random();
												series.addPoint([ x, y ], true,
														true);
											}, 1000); */
										}
									}
									
								},
								title : {
									text : $('#MonitoringObject').val()+'实时监测指标',
									style:{
										color: 'white',
										fontSize: '16px'
									}
									
								},
								credits : {
									enabled : false,
								},
								xAxis : {
									type : 'datetime',
									tickPixelInterval : 150,
								  	labels: { 
			                            rotation: 0, 
			                            style: { 
			                            	color: 'white',
			                                fontSize: '13px', 
			                                fontFamily: 'Verdana, sans-serif', 
			                            }
			                        }
								},
								yAxis : {
									title : {
										text : ''
									},
									plotLines : [ {
										value : 0,
										width : 1,
										color : '#808080'
									} ],
									labels: { 
			                            rotation: 0, 
			                            style: { 
			                            	color: 'white',
			                                fontSize: '13px', 
			                                fontFamily: 'Verdana, sans-serif', 
			                            }
			                        }
								},
								tooltip : {
									formatter : function() {
										return '<b>'
												+ this.series.name
												+ '</b><br/>'
												+ Highcharts.dateFormat(
														'%Y-%m-%d %H:%M:%S',
														this.x)
												+ '<br/>'
												+ Highcharts.numberFormat(
														this.y, 2);
									}
								},
								legend : {
									enabled : false  
									//  下面的标示                                                 
								},
								exporting : {
									enabled : false
								//导出                                                  
								},
								colors:['yellow'],
								series : [ {
									name : $('#MonitoringObject').val(), 
									data : (function() {
										// generate an array of random data                             
										var data = [], time = (new Date())
												.getTime(), i;

										for (i = -19; i <= 0; i++) {
											data.push({
												x : time + i * 1000,
												y : Math.random() * 100
											});
										}
										return data;
									})()
								} ]
							});
				});
	});
	 
</script>

</head>
<body>
	<form:form class="well-small well form-inline search-form-style"
		method="get" modelAttribute="queryForm" id="queryForm">

		<form:select path="areaId" data-placeholder="默认全部区域"
			cssClass="chzn-autoselect-area">
			<form:option value=""></form:option>
			<c:if test="${not empty areaId}">
				<form:option value="${repoArea.id}" label="${repoArea.name}"></form:option>
			</c:if>
		</form:select>
		<form:select path="monitorObjectScope" placeholder="MonitorObjectType" id="MonitoringObject" cssClass="input js-select">
			<form:option value="">选择监测对象-默认全部</form:option>
			<form:options items="${monitorObjectMap}" />
		</form:select>
		<button type="submit" style="margin-left: 4px" class="btn" onclick="showObject()">
			<i class="icon-search"></i>
			<!-- <fmt:message key="btn.search" /> -->
			检索
		</button>
</form:form>
		<div class="container">
			<div id="testHighchart" style="min-width: 700px; height: 400px;"></div>
		</div>
	





	<script type="text/javascript">
		function showObject(){
				
		}
		$(function() {
			$.ajax({
				type : "Post", 
				url : '${pageContext.request.contextPath}/websocket/run' 
			});
			  
			var webSocket = new WebSocket('ws://127.0.0.1:8989/websocket');
			//var webSocket = new WebSocket('ws://localhost:8080/museum-web/webscoket/run');

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

				/* document.getElementById('messages').innerHTML += '<li lang='+'2012-2-2'+'>接收的'
								+ event.data + "</li>"; */
				//$('#container').highcharts.Series.addPoint([ Math.random(),
				//event.data ], true, true); 
				console.info(event.data);
				var data = JSON.parse(event.data);//解析字符串 
				console.info(data);
				var series = chart.series[0];
				var x = (new Date()).getTime(); // current time         
				var y = Math.random() * 100;
				series.addPoint([ data.x, data.y ], true, true);
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
		})
		//获取区域
		$(function() {
			$(".chzn-autoselect-area").ajaxChosen({
				original_chosen_option : {
					allow_single_deselect : true
				},
				method : 'GET',
				url : '${ctx}/common/area/fetchAreaList',
				dataType : 'json'
			}, function(data) {
				var terms = {};
				$.each(data, function(i, val) {
					terms[i] = val;
				});
				return terms;
			});
		});
	</script>
</body>
</html>