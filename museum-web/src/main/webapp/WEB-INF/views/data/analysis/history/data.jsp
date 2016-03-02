<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<%@ include file="/WEB-INF/views/common/scripts.jsp"%> 
<head>
<title>数据分析</title>
<script type="text/javascript" src="http://cdn.hcharts.cn/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="http://cdn.hcharts.cn/highcharts/highcharts.js"></script>
<script type="text/javascript" src="http://cdn.hcharts.cn/highcharts/exporting.js"></script>
<script type="text/javascript" src="http://cdn.hcharts.cn/highcharts/data.js"></script>   
<script type="text/javascript" src="${staticFile}/js/JQ.MoveBox.js"></script>
<script type="text/javascript">
	$(document).ready(function(){ 
		$('#container').highcharts({
			data: {
				table: document.getElementById('datatable')
			},
			chart: {
				type: 'column'
			},
			title: {
				text: '标题'
			},
			yAxis: {
				allowDecimals: false,
				title: {
					text: '水果'
				}
			},
			credits:{
				enabled:false
			},
			tooltip: {
				formatter: function() { 
					return '<b>'+ this.series.name +'</b><br/>'+
						this.y +' '+ this.x; 
				}
			}
		});
});	 	

function delAdvanced(id){ 
	$("#"+id).parent().remove();
	i--;
}

function showModel(){
	/* $('#showHighCharts').show(); */
	$('#one').show();
	$('#two').show();
	$("#showHighCharts").fadeIn(300);
}
function closeModel(){
	$('#showHighCharts').fadeOut(300);
	$('#one').hide();
	$('#twos').hide();
} 
function zoom(id,rate){
	 if(rate==1.25){
		$('#showHighCharts').css('top','0px');
		$('#showHighCharts').css('left','0px');
		$('#showHighCharts').css('width','100%');
		$('#showHighCharts').css('height','100%');	
		$('#container').css('width','100%');	
		$('#container').css('height','95%');	
		var options=$("#container").highcharts().options;
		options.chart.type='column';
	    new Highcharts.Chart(options);
	 }else{
		$('#showHighCharts').css('width','700px');
		$('#showHighCharts').css('height','400px');	
		$('#container').css('width','700px');	
		$('#container').css('height','370px');	
		var options=$("#container").highcharts().options;
		options.chart.type='column';
	    new Highcharts.Chart(options);
	 }
}

</script>
<style type="text/css">
	#showHighCharts{
		border-radius: 5px;
		width: 700px;
		height:400px;
		position:absolute; 
		border-style: solid; 
		border-width: 4px;
		border-color: #CCC; 
		cursor:pointer;
		display:none;
		z-index:2; 
	}
	#container{
		width: 700px; 
		height: 370px;
	}
	#titleName{
		margin-left: 10px;
		line-height:2;
		font-size: 15px;
	} 
	#iconI{
		float: right; 
		margin-right: 5px
	}
	#iconI i{
		cursor: pointer;
	} 
	#one{
		width: 100%; 
		height: 950px;
		position: absolute; 
		top:0px; 
		left: 0px; 
		display: none;
	}
	#two{
		width: 100%; 
		height: 100%; 
		position:relative;
		display: none;
	}
</style>
</head>
<body>
	<div id="js-ajax-repalce-block">
		<!--普通搜索-->
		<form class="well-small well form-inline search-form-style" method="get" id="queryForm">
			<input path="startTime" placeholder="开始时间" cssClass="time_picker" />
			<input path="endTime" placeholder="结束时间" cssClass="time_picker" />
			
			<select data-placeholder="默认全部区域">
				<option>--Please Location--</option>
				<option>区域一</option>
				<option>区域二</option>
				<option>区域三</option>
			</select> 
			  
			
			<a title="更多选项..." href="javascript:$('#more').slideToggle(500);$('#averageRetrieval').toggleClass('icon-minus-sign');$('#advancedSearchForm').css('display','none');$('#advancsdSearchSpan').attr('class','icon-plus-sign icon-white')">
				<span id="averageRetrieval" class="icon-plus-sign icon-white"></span>
			</a>
			<button type="submit" style="margin-left: 4px" class="btn">
				<i class="icon-search"></i> 检索
			</button>

			<!-- 隐藏的DIV -->
			<div style="display: none; margin-top: 10px;" id="more">
				
				<select data-placeholder="默认全部区域">
					<option value="">--请选择时间粒度--</option>
					<option>YEAR</option>
					<option>MONTH</option>
					<option>DAY</option>
				</select> <select data-placeholder="默认全部区域">
					<option value="">--Please Object--</option>
					<option>CO2</option>
					<option>SO2</option>
				</select>
				 
			</div>
			 
		</form>
		
		
		<!-- 时间 -->
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
	</div>
	  
	<div>
		<table class="table table-bordered" style="width:99%; margin:auto" id="datatable" >
		<thead>
			<tr>
				<th></th>
				<th>张三</th>
				<th>李四</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<th>苹果</th>
				<td>3</td>
				<td>4</td>
			</tr>
			<tr>
				<th>梨</th>
				<td>2</td>
				<td>10</td>
			</tr>
			<tr>
				<th>李子</th>
				<td>5</td>
				<td>11</td>
			</tr>
			<tr>
				<th>香蕉</th>
				<td>1</td>
				<td>1</td>
			</tr>
			<tr>
				<th>橘子</th>
				<td>2</td>
				<td>4</td>
			</tr>
		</tbody>
	</table>
	<button style="margin-top: 15px; margin-left: 5px" class="btn btn-primary" type="button" onclick="showModel()"><i class='icon-th icon-white'></i>查看图表</button>
</div>
<div id="one">
	<div id="two">
		<div id="showHighCharts">
			<div style="width: 100%; height:30px; background-color:#CCC; ">
				<span id="titleName">图表区域</span>
				<span id="iconI">
					<i class="icon-plus" onclick="zoom('showHighCharts',1.25)" title="放大"></i>
					<i class="icon-minus" onclick="zoom('showHighCharts',0.8)" title="缩小"></i>
					<i onclick="closeModel()" class="icon-remove" title="关闭"></i> 
				</span>
			</div>
			 <div id="container"></div> 
		</div>
	</div>
</div>


<script type="text/javascript">
	$("#showHighCharts").MoveBox();
</script>
</body>