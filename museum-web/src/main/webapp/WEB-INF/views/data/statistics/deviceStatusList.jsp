<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<head>
<title><fmt:message
		key="pageTitle.data_visualization_statistics_device_status" /></title>
<link rel="stylesheet"
	href="${staticFile}/bootstrap-table/css/rwd-table.min.css?v=5.0.4">
<link rel="stylesheet"
	href="${staticFile}/bootstrap-table/css/docs.min.css?v=5.0.4">
<style type="text/css">
.word_break {
	width: 100%;
	word-break: keep-all;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

#changeColor td {
	background-color: #F5F5F5;
}
</style>

</head>
<body>

	<div id="js-ajax-repalce-block" style="position: relative">
		<h3 style="position: relative; color: #666666">
			<fmt:message
				key="pageTitle.data_visualization_statistics_device_status" />
			<span
				style="color: green; font-size: 14px; left: 250px; position: absolute;">当前统计结果条数:
				${totalCount} </span>
		</h3>
		<hr />
		<!--搜索区域-->
		<form:form class="well-small well form-inline search-form-style"
			method="get" modelAttribute="queryForm" id="queryForm">

			<form:input path="startTime" placeholder="开始时间"
				cssClass="time_picker" />
			<form:input path="endTime" placeholder="结束时间" cssClass="time_picker" />

			<form:select path="deviceId" data-placeholder="默认全部监测站和监测点"
				cssClass="chzn-autoselect-device">
				<form:option value=""></form:option>
				<c:if test="${not empty deviceId}">
					<form:option value="${device.id}" label="${device.name}"></form:option>
				</c:if>
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

				<!--<form:input path="deviceNumber" placeholder="设备编号" maxlength="15" />-->
				<form:select path="deviceType" placeholder="deviceType"
					cssClass="input js-select">
					<form:option value="">选择设备类型-默认全部</form:option>
					<form:options items="${deviceTypeMap}" />
				</form:select>
				<form:select path="deviceStatus" placeholder="deviceStatus"
					cssClass="input js-select">
					<form:option value="">选择设备状态-默认全部</form:option>
					<form:options items="${deviceStatusMap}" />
				</form:select>
				<a title="高级搜索"
					href="javascript:$('#advancedSearchForm').slideToggle(500);$('#advancsdSearchSpan').toggleClass('icon-minus-sign');">
					<span id="advancsdSearchSpan" class="icon-plus-sign icon-white"></span>
				</a>
			</div>

			<!--高级搜索-->
			<div style="display: none; margin-top: 10px" id="advancedSearchForm">

				<!-- <a title="添加选项">
					<span style="cursor: pointer;" id="advancedSearch" class="icon-plus-sign icon-white"></span>
				</a> -->
			</div>

			<script>
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
		<div class="table-responsive" data-pattern="priority-columns">
			<table id="tech-companies-1"
				class="table table-small-font table-bordered">
				<thead>
					<tr>
						<th><fmt:message key="th.time" /></th>

						<th data-priority="1"><fmt:message
								key="th.monitoring_station" /></th>
						<th data-priority="3"><fmt:message key="th.monitoring_point" /></th>
						<th data-priority="3"><fmt:message key="th.device_type" /></th>
						<th data-priority="3"><fmt:message key="th.device_status" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${pageObjects.content}" var="dataRecord">
						<tr id="changeColor">
							<td><c:out value="${dataRecord.dateTime}" /></td>
							<td><c:out value="${dataRecord.device.name}" /></td>
							<td><c:out value="${dataRecord.device.name}" /></td>
							<td><c:out value="${dataRecord.deviceType}" /></td>
							<td><c:out value="${dataRecord.deviceStatus}" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
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

		<%@ include file="../../component/pagination.jsp"%>
	</div>
</body>