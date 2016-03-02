<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<head>
<title><fmt:message key="pageTitle.evaluation_entire_evaluation" /></title>

<style type="text/css">
.word_break {
	width: 100%;
	word-break: keep-all;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}
</style>
<script type="text/javascript"
	src="${staticFile}/js/highcharts/highcharts.js"></script>
</head>
<body>
	<div id="js-ajax-repalce-block" style="position: relative">
		<hr />
		<!--搜索区域-->
		<form:form class="well-small well form-inline search-form-style"
			method="get" modelAttribute="queryForm" id="queryForm">

			<form:select path="granularity" placeholder="Granularity"
				cssClass="input js-select">
				<form:option value="">--Please Select--</form:option>
				<form:options items="${timeGranularityMap}" />
			</form:select>
			<form:input path="startTime" placeholder="开始时间"
				cssClass="time_picker" />
			<form:input path="endTime" placeholder="结束时间" cssClass="time_picker" />
		
			<button type="submit" style="margin-left: 4px" class="btn">
				<i class="icon-search"></i>
				<fmt:message key="btn.search" />
			</button>
            <div><input type ="hidden" id="msg" value='${msg}' /></div>

<script>
				//点击按钮切换图标
				var po=$("#msg").val();
				
				var options = eval("("+po+")");

				$(document)
						.ready(
								function() {
									
								var chart = new Highcharts.Chart(options);

									$("button.change")
											.click(
													function() {
														var type = $(this)
																.html();

														options.chart.type = type;
														chart = new Highcharts.Chart(
																options);
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
	<%@ include file="../../../component/pagination.jsp"%>

</body>