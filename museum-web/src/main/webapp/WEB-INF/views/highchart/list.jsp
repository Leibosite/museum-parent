<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>测试Highcharts</title>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/scripts.jsp"%>
<%@ include file="../common/styles.jsp"%>

<script type="text/javascript" src="${staticFile}/highChart-js/highcharts.js"></script>
<script type="text/javascript" src="${staticFile}/highChart-js/modules/exporting.js"></script>
</head>
<body>
	<%-- <input type="hidden" id="getMsg" value="${msg }"/> --%>
	<div class="container">
		<div class="widget-box">
			<div class="widget-title">
				<span class="icon"> <i class="icon-bookmark"></i></span>
				<h5>Highcharts</h5>
			</div>
			<div class="widget-content" style="height: 650px;">
				<div id="container" style="min-width: 100px; height: 400px; margin: 0 auto"></div>
				<script type="text/javascript">
					var charts = null;
					$(function(){
						$.ajax({
							type : "Post",
							dataType : "JSON",
							url : '${pageContext.request.contextPath}/highchart/test',
							async : false,
							success:function(msg){
								charts = new Highcharts.Chart({
									chart :{
										renderTo : "container",
										backgroundColor : null,
										spacingRight : 20,
										type : 'column',
									},
									title:{
										text:'图表标题'    //指定图表标题
									},
									xAxis :{ 
										categories :msg.categories, 
									},
									yAxis:{
										min : 0,
										title :{
											text : '测试'
										}
									},
									tooltip :{
										pointFormat: '年龄： <b>{point.y:.1f} </b>',
										shared : true,
										useHTML : true
									},
									plotOptions :{
										column : {
											pointPadding : 0.2,
											borderWidth : 0
										}
									},
									series : msg.series
								});
							}
						});
					});
				</script>				
			</div>
		</div>
	</div>
</body>
</html>