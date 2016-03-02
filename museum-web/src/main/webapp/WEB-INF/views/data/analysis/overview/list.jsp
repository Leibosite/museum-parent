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
<script type="text/javascript" src="${staticFile}/js/highcharts/highcharts.js"></script>
<script type="text/javascript" src="${staticFile}/js/highcharts/highcharts-more.js"></script>
</head>
<body>

	<div id="js-ajax-repalce-block" style="position: relative">

		<!--搜索区域-->
		<form:form class="well-small well form-inline search-form-style"
			method="get" modelAttribute="queryForm" id="queryForm">

			<form:input path="startTime" placeholder="时间" cssClass="time_picker" />

			<form:select path="areaId" data-placeholder="默认全部区域"
				cssClass="chzn-autoselect-area">
				<form:option value=""></form:option>
				<c:if test="${not empty areaId}">
					<form:option value="${repoArea.id}" label="${repoArea.name}"></form:option>
				</c:if>
			</form:select>
			<!--  
			<form:select path="granularity" placeholder="Granularity"
				cssClass="input js-select">
				<form:option value="">时间粒度</form:option>
				<form:options items="${timeGranularityMap}" />
			</form:select>
			-->
			<button type="submit" style="margin-left: 4px" class="btn">
				<i class="icon-search"></i>
				<fmt:message key="btn.search" />
			</button>
			<div>
				<input type="hidden" id="msg" value='${msg}' />
			</div>

		</form:form>

		<div id="container" style="min-width: 800px; height: 400px">
			<p>图形区域</p>

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
		//点击按钮切换图标
		var po = $("#msg").val();
		
		var options = eval("(" + po + ")");
		

		$(document).ready(function() {

			var chart = new Highcharts.Chart(options);
		});
	</script>
</body>