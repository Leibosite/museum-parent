<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>电量曲线</title>
<script type="text/javascript" src="${staticFile}/js/highstock/highstock.js"></script>
<script type="text/javascript" src="${staticFile}/js/highstock/exporting.js"></script>
<script type="text/javascript">

	function searchDevices() {
		var seriesOptions = [], 
			yAxisOptions = [], 
			seriesCounter = 0, 
			names = [ '电量' ], 
			colors = Highcharts.getOptions().colors;
		
		$.each(names, function(i, name) {
			$.ajax({
				type : "Post",
				dataType : "JSON",
				url : '${ctx}/data/analysis/power/getJson',
				async : false,//同步
				data : {
					startTime : $('#stratTime').val(),
					endTime : $('#endTime').val(),
					deviceId : $('#devicesId').val()
				},
				success : function(result) {
					
					var data = result.data;
					seriesOptions[i] = {
						name : name,
						data : data
					};
					
					seriesCounter++;
					
					if (seriesCounter == names.length) {
						 Highcharts.setOptions({
					            lang:{
					                rangeSelectorZoom:''
					            }
					      });
						
						$('#container').highcharts('StockChart',{
							chart : {
								backgroundColor: '', 
							},

							rangeSelector : {
								/* inputDateFormat: '%Y-%m-%d %H:%M', */
								inputDateFormat: '%Y-%m-%d',
			            		inputEditDateFormat: '%Y-%m-%d',
			                    inputWidth: '120', //这里的内容生效需要修改highstock.js的大概找到码大概 19540行左右
			                    inputDateParser: function(value) {
			                    	value = value.split(/[:-\s]/);
			                    	 return Date.UTC(
			     		                    parseInt(value[0]),
			     		                    parseInt(value[1])-1,
			     		                    parseInt(value[2]),
			     		                    parseInt(value[3]),
			     		                    parseInt(value[4]),
			     		                    0
			     		             );
			                    },
			                    buttonTheme: { // styles for the buttons
			                        fill: 'none',
			                        stroke: 'none',
			                        'stroke-width': 0,
			                        r: 8,
			                        style: {
			                            color: 'red',
			                            fontWeight: 'bold'
			                        },
			                        states: {
			                            hover: {
			                            },
			                            select: {
			                                fill: 'green',
			                                style: {
			                                    color: 'yellow'
			                                }
			                            }
			                        }
			                    },
			                    selected : 3,
			                    buttons:[{
			                    	type:'second',
			                    	count:1,
			                    	text:'1秒'
			                    },{
			                    	type:'minute',
			                    	count:1,
			                    	text:'1分钟'
			                    },{
			                    	type:'minute',
			                    	count:60,
			                    	text:'1小时'
			                    },{
			                    	type: 'day',
			     				    count: 1,
			     				    text: '1天'
			                    },{
			                    	type: 'day',
			     				    count: 7,
			     				    text: '1周'
			                    },{
			                    	type: 'day',
			     				    count: 15,
			     				    text: '半月'
			                    },{
			                    	type: 'month',
			     				    count: 1,
			     				    text: '1月'
			                    },{
			                    	type: 'month',
			     				    count: 2,
			     				    text: '2月'
			                    },,{
			                    	type: 'month',
			     				    count: 3,
			     				    text: '3月'
			                    },{
			                    	type: 'month',
			     				    count: 6,
			     				    text: '半年'
			                    },{
			                    	type: 'all',
			    				    text: '所有'
			                    }],
			                 	// inputBoxBorderColor:'red',
				                inputStyle:{
				                    color:'black'
				                },
				                labelStyle:{
				                    color:'black'
				                }
							},
							xAxis : {
								labels: {  
		                            style: { 
		                            	color: 'black',
		                                fontSize: '12px', 
		                                fontFamily: 'Verdana, sans-serif', 
		                            }
		                        },  
							}, 
							navigator:{
								xAxis: {
								    tickWidth: 0,
								    lineWidth: 0,
								    gridLineWidth: 1,
								    tickPixelInterval: 200,
								    labels: {
								        align: 'left',
								        style: {
								            color: 'black'
								        },
								        x: 3,
								        y: -4
								    }
								}
							},
							yAxis: {
						    	max:100,
						    	min:0,
						    	labels: {  
		                            style: { 
		                            	color: 'black',
		                                fontSize: '12px', 
		                                fontFamily: 'Verdana, sans-serif', 
		                            }
		                        },  
						    },
							credits : {
								enabled : false
							},

							tooltip : {
								pointFormat : '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b><br/>',
								valueDecimals : 2, 
							},
							plotOptions:{
								series: {
									lineWidth :1
								}
							},
							colors:['red'],
							series : seriesOptions
						});
					}
				}, error: function (a, b, c) {
                    alert(a);
                    alert(b);
                    alert(c);
                }
			});
		});
	}

	// create the chart when all data is loaded
	function createChart() {
		
	} 
</script>
</head>
<body>
	<form:form class="well-small well form-inline search-form-style" method="get" modelAttribute="queryForm" id="queryForm">
		<form:input path="startTime" id="stratTime" placeholder="开始时间" cssClass="time_picker" />
		<form:input path="endTime" id="endTime" placeholder="结束时间" cssClass="time_picker" />
		<form:select path="deviceId" id="devicesId" data-placeholder="默认全部监测点" cssClass="chzn-autoselect-device">
			<form:option value=""></form:option>
			<c:if test="${not empty deviceId}">
				<form:option value="${device.id}" label="${device.name}"></form:option>
			</c:if>
		</form:select>  
		
		<a class="btn" href="#" onclick="searchDevices()">
			<i class="icon-search"></i> 检索
		</a>
		
		<script type="text/javascript">
			$(function() {
				$(".chzn-autoselect-device").ajaxChosen({
					original_chosen_option : {
						allow_single_deselect : true
					},
					method : 'GET',
					url : '${ctx}/common/area/fetchDeviceList',
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
	</form:form>
	<script type="text/javascript">
		$(function() {
			$('.time_picker').datetimepicker({
				language : 'zh-CN',
				startView : 1,
				format : 'yyyy-mm-dd hh:ii',
				minuteStep : 10,
				maxView : 4
			});
		});
	</script>
	<div id="container" style="height: 500px; min-width: 310px"></div>
</body>
</html>