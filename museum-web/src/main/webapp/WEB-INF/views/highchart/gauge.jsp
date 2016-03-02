<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>兰州博物馆空气质量指数</title>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/scripts.jsp"%>
<%@ include file="../common/styles.jsp"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>

<script type="text/javascript" src="${staticFile}/js/highcharts/highcharts.js"></script>
<script type="text/javascript" src="${staticFile}/js/highcharts/modules/exporting.js"></script>
<script type="text/javascript" src="${staticFile}/js/highcharts/highcharts-more.js"></script>
<script type="text/javascript">
	$(function() {
		$('#container').highcharts({
			chart : {
				type : 'gauge',
				plotBackgroundColor : null,
				plotBackgroundImage : null,
				plotBorderWidth : 0,
				plotShadow : false
			},
			title : {
				text : '兰州博物馆空气质量指数'
			},
			credits : {
				enabled : false
			},
			pane : {
				startAngle : -150,
				endAngle : 150,
				background : [ {
					backgroundColor : {
						linearGradient : {
							x1 : 0,
							y1 : 0,
							x2 : 0,
							y2 : 1
						},
						stops : [ [ 0, '#FFF' ], [ 1, '#333' ] ]
					},
					borderWidth : 0,
					outerRadius : '109%'
				}, {
					backgroundColor : {
						linearGradient : {
							x1 : 0,
							y1 : 0,
							x2 : 0,
							y2 : 1
						},
						stops : [ [ 0, '#333' ], [ 1, '#FFF' ] ]
					},
					borderWidth : 1,
					outerRadius : '107%'
				}, {
				// default background
				}, {
					backgroundColor : '#DDD',
					borderWidth : 0,
					outerRadius : '105%',
					innerRadius : '103%'
				} ]
			},

			// the value axis
			yAxis : {
				min : 0,
				max : 500,
				minorTickInterval : 'auto',
				minorTickWidth : 1,
				minorTickLength : 0,
				minorTickPosition : 'inside',
				minorTickColor : 'red',

				tickPixelInterval : 50,
				tickWidth : 2,
				tickPosition : 'inside',
				tickLength : 0,
				tickColor : 'yellow',
				labels : {
					step : 2,
					rotation : 'auto'
				},
				title : {
					text : ''
				},
				plotBands : [ {
					from : 0,
					to : 50,
					color : 'green' // 优
				}, {
					from : 50,
					to : 150,
					color : 'blue'//良
				}, {
					from : 150,
					to : 300,
					color : 'yellow'//轻度污染
				}, {
					from : 300,
					to : 400,
					color : 'black'//中度污染
				}, {
					from : 400,
					to : 500,
					color : 'red'//严重污染
				} ]
			},
			tooltip : {
				formatter : function() {
					s = this.y;
					if (this.y < 50) {
						s += '优';
					} else if (this.y < 150) {
						s += '良';
					} else if (this.y < 300) {
						s += '轻度污染';
					} else if (this.y < 400) {
						s += '中度污染';
					} else if (this.y < 500) {
						s += '严重污染';
					}
					return s;
				}
			},
			series : [ {
				data : [ 175 ]
			} ]
		},
		// Add some life
		function(chart) {
			if (!chart.renderer.forExport) {
				/* setInterval(function () {
				     var point = chart.series[0].points[0],
				         newVal,
				         inc = Math.round((Math.random() - 0.5) * 20);
				     
				     newVal = point.y + inc;
				     if (newVal < 0 || newVal > 200) {
				         newVal = point.y - inc;
				     }
				     
				     point.update(newVal);
				     
				 }, 3000);*/
			}
		});
	});
</script>
</head>
<body>
<div id="container" style="min-width:700px;height:400px"></div>
<div style="width:100%; height:150px;">
	<div style="width:250px; height:30px;margin:auto"> 
		<div style="width:10px; height:10px; background-color:green; float:left;margin-top: 5px;"></div><span style="float:left; font-size:12px; margin-right: 5px;">优</span>
		<div style="width:10px; height:10px; background-color:blue; float:left;margin-top: 5px;"></div><span style="float:left; font-size:12px; margin-right: 5px;">良</span>
		<div style="width:10px; height:10px; background-color:yellow; float:left;margin-top: 5px;"></div><span style="float:left; font-size:12px; margin-right: 5px;">轻度污染</span>
		<div style="width:10px; height:10px; background-color:black; float:left;margin-top: 5px;"></div><span style="float:left; font-size:12px; margin-right: 5px;">中度污染</span>
		<div style="width:10px; height:10px; background-color:red; float:left;margin-top: 5px;"></div><span style="float:left; font-size:12px; margin-right: 5px;">严重污染</span>
	</div>
	<div style="width:150px; height:100px; margin:auto; font-size:12px">
		<span>PM2.5[细颗粒物] 488</span><br />
		<span>PM10[可吸入颗粒物] 508</span><br />
		<span>SO2[二氧化硫] 96</span><br />
		<span>NO2[二氧化氮] 253</span><br />
	</div>
</div>
</body>
</html>