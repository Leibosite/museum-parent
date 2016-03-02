<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<head>
<title>数据分析</title>
<script type="text/javascript" src="http://cdn.hcharts.cn/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="http://cdn.hcharts.cn/highcharts/highcharts.js"></script>
<script type="text/javascript" src="http://cdn.hcharts.cn/highcharts/exporting.js"></script>
<script type="text/javascript" src="http://cdn.hcharts.cn/highcharts/data.js"></script>   
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
	
		$(".change").click(function(){
			var type = $(this).html();
			var options=$("#container").highcharts().options;
			options.chart.type=type;
		    new Highcharts.Chart(options);
		});
});	 	
	
var i = 1;
$(function(){
	 
	 $('#advancedSearch').click(function(){
		 if(i < 10) {  
			 //<span title='删除' style='cursor:pointer;' id=del"+i+" class='icon-plus-sign icon-white icon-minus-sign' onclick='delAdvanced(this.id)'></span>
			 $('#advancedSearchForm').append("<div style='margin-top:10px' id='ss'><select style='width:70px'><option>SO2</option><option>CO2</option></select> <input type='text' placeholder='开始区间' style='width:55px'/> <input type='text' placeholder='结束区间' style='width:55px'/> <button id=del"+i+" class='btn btn-small btn-danger' type='button' onclick='delAdvanced(this.id)'><i class='icon-remove icon-white'></i>删除</button></div>"); 
			 i++;
		 }else{
			 alert("最多10个");
		 }
	 });
});

function delAdvanced(id){ 
	$("#"+id).parent().remove();
	i--;
}
</script>
 
</head>
<body>
	<div id="js-ajax-repalce-block" style="position: relative">
		<!--普通搜索-->
		<form class="well-small well form-inline search-form-style" method="get" id="queryForm">
			<select data-placeholder="默认全部区域">
				<option>--Please Location--</option>
				<option>区域一</option>
				<option>区域二</option>
				<option>区域三</option>
			</select> 
			
			<select data-placeholder="默认全部区域">
				<option>--Please Devices--</option>
				<option>设备一</option>
				<option>设备二</option>
				<option>设备三</option>
			</select>
			
			<input type="text" placeholder="设备编号" /> 
			
			<a title="更多选项..." href="javascript:$('#more').slideToggle(500);$('#averageRetrieval').toggleClass('icon-minus-sign');$('#advancedSearchForm').css('display','none');$('#advancsdSearchSpan').attr('class','icon-plus-sign icon-white')">
				<span id="averageRetrieval" class="icon-plus-sign icon-white"></span>
			</a>
			<button type="submit" style="margin-left: 4px" class="btn">
				<i class="icon-search"></i> 检索
			</button>

			<!-- 隐藏的DIV -->
			<div style="display: none; margin-top: 10px;" id="more">
				<input path="startTime" placeholder="开始时间" cssClass="time_picker" />
				<input path="endTime" placeholder="结束时间" cssClass="time_picker" />
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
				<a title="高级搜索" href="javascript:$('#advancedSearchForm').slideToggle(500);$('#advancsdSearchSpan').toggleClass('icon-minus-sign');">
					<span id="advancsdSearchSpan" class="icon-plus-sign icon-white"></span>
				</a>
			</div>
			
			<!--高级搜索-->
			<div style="display: none; margin-top: 10px"  id="advancedSearchForm">
				<select data-placeholder="默认全部区域" style="width: 70px">
					<option>CO2</option>
					<option>SO2</option>
				</select> 
				
				<input type="text" placeholder="开始区间" style="width: 55px"/>
				<input type="text" placeholder="结束区间" style="width: 55px" /> 
				<!-- <a title="添加选项">
					<span style="cursor: pointer;" id="advancedSearch" class="icon-plus-sign icon-white"></span>
				</a> -->
				<button id="advancedSearch" class="btn btn-primary" type="button">
				<i class='icon-plus icon-white'></i>
				添加选项</button>
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
	
	<div id="container" style="min-width: 800px; height: 400px">
		<p>图形区域</p>
	</div>
	<div style="margin: 10px 0px 10px 20px;">
		点击按钮切换图表：
		<button class="change">line</button>
		<button class="change">spline</button>
		<button class="change">pie</button>
		<button class="change">area</button>
		<button class="change">column</button>
		<button class="change">areaspline</button>
		<button class="change">bar</button>
		<button class="change">scatter</button>
	</div>
	<table id="datatable" style="text-align: center; margin: auto" border="1" width="99%" cellpadding="5" cellspacing="0">
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
</body>