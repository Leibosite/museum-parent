<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<head>
<title><fmt:message
		key="pageTitle.data_visualization_statistics" /></title>

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

		<!--搜索区域-->
		<form:form class="well-small well form-inline search-form-style"
			method="get" modelAttribute="queryForm" id="queryForm">

			<form:input path="startTime" placeholder="开始时间"
				cssClass="time_picker" />
			<form:input path="endTime" placeholder="结束时间" cssClass="time_picker" />


			<form:select path="monitorObjectType" placeholder="MonitorObjectType"
				cssClass="input js-select">
				<form:option value="">选择监测对象-默认全部</form:option>
				<form:options items="${monitorObjectMap}" />
			</form:select>
			<form:select path="areaId" data-placeholder="默认全部区域"
				cssClass="chzn-autoselect-area">
				<form:option value=""></form:option>
				<c:if test="${not empty areaId}">
					<form:option value="${repoArea.id}" label="${repoArea.name}"></form:option>
				</c:if>
			</form:select>
			<form:select path="ids" multiple="multiple" data-placeholder="比较区域"
				cssClass="chzn-autoselect-required">
				<form:option value=""></form:option>
				<form:options items="${allEreas}" itemLabel="name" itemValue="id" />
			</form:select>


			<a title="更多选项..."
				href="javascript:$('#more').slideToggle(500);$('#averageRetrieval').toggleClass('icon-minus-sign');$('#advancedSearchForm').css('display','none');$('#advancsdSearchSpan').attr('class','icon-plus-sign icon-white')">
				<span id="averageRetrieval" class="icon-plus-sign icon-white"></span>
			</a>
			<!--  
			<a title="更多选项..."
				href="javascript:$('#more').slideToggle(500);$('.icon-plus-sign').toggleClass('icon-minus-sign');">
				<span class="icon-plus-sign icon-white"></span>
			</a>
			-->
			<button type="submit" style="margin-left: 4px" class="btn">
				<i class="icon-search"></i>
				<fmt:message key="btn.search" />
			</button>
			<!-- 隐藏的DIV -->
			<div style="display: none; margin-top: 10px;" id="more">
				<form:select path="granularity" placeholder="Granularity"
					cssClass="input js-select">
					<form:option value="">时间粒度</form:option>
					<form:options items="${timeGranularityMap}" />
				</form:select>

			</div>
			<div>
				<input type="hidden" id="msg" value='${msg}' />
			</div>

		</form:form>

		<div id="container" style="min-width: 800px; height: 400px">
			<p>图形区域</p>

		</div>
		<div style="margin: 10px 0px 10px 10px;">
			点击按钮切换图表：
			<button class="change">line</button>
			<button class="change">spline</button>
			<button class="change">area</button>
			<button class="change">column</button>
			<button class="change">areaspline</button>
			<button class="change">bar</button>
			<button class="change">scatter</button>
		</div>


	</div>
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

	<script>
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
		$(function() {
			$(".chzn-autoselect-required").ajaxChosen({
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
		//点击按钮切换图标
		var po = $("#msg").val();
		var options = eval("(" + po + ")");

		$(document).ready(function() {

			var chart = new Highcharts.Chart(options);

			$("button.change").click(function() {
				var type = $(this).html();

				options.chart.type = type;
				chart = new Highcharts.Chart(options);
			});
		});
	</script>
</body>