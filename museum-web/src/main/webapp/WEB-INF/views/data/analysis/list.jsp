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

</head>
<body>
	<div id="js-ajax-repalce-block" style="position: relative">
		<h3 style="position: relative">
			<fmt:message key="pageTitle.data_visualization_statistics" />
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
			<a title="更多选项..."
				href="javascript:$('#more').slideToggle(500);$('.icon-plus-sign').toggleClass('icon-minus-sign');">
				<span class="icon-plus-sign icon-white"></span>
			</a>
			<button type="submit" style="margin-left: 4px" class="btn">
				<i class="icon-search"></i>
				<fmt:message key="btn.search" />
			</button>

			<div style="display: none; margin-top: 10px;" id="more">
				<form:select path="areaId" data-placeholder="默认全部区域"
					cssClass="chzn-autoselect-area">
					<form:option value=""></form:option>
					<c:if test="${not empty areaId}">
						<form:option value="${repoArea.id}" label="${repoArea.name}"></form:option>
					</c:if>
				</form:select>
			</div>


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
			</script>
		</form:form>

		<table style="background-color: white; table-layout: fixed;"
			class="table table-striped table-bordered" id="list-table">
			<thead>
				<tr>
					<th><fmt:message key="th.time" /></th>
					<th><fmt:message key="th.areaName" /></th>
					<th><fmt:message key="th.monitoring_point" /></th>
					<th><fmt:message key="th.temperature_data" /></th>
					<th><fmt:message key="th.humidity_data" /></th>
					<th><fmt:message key="th.lighting_data" /></th>
					<th><fmt:message key="th.uvIntensity_data" /></th>
					<th><fmt:message key="th.sulfurDioxide_data" /></th>
					<th><fmt:message key="th.carbonDioxide_data" /></th>
					<th><fmt:message key="th.voc_data" /></th>
					<th><fmt:message key="th.formalclehyde_data" /></th>
					<th><fmt:message key="th.pm02_5_data" /></th>
					<th><fmt:message key="th.pm10_0_data" /></th>
					<th><fmt:message key="th.pm1_0_data" /></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${pageObjects.content}" var="dataRecord">
					<tr>
						<td class="word_break"><c:out
								value="${dataRecord.dataTime}" /></td>
						<td class="word_break"><c:out
								value="${dataRecord.device.repoArea.name}" /></td>
						<td class="word_break"><c:out
								value="${dataRecord.temperature}" /></td>
						<td class="word_break"><c:out value="${dataRecord.humidity}" /></td>
						<td class="word_break"><c:out
								value="${dataRecord.illuminance}" /></td>
						<td class="word_break"><c:out
								value="${dataRecord.uvIntensity}" /></td>
						<td class="word_break"><c:out
								value="${dataRecord.sulfurDioxide}" /></td>
						<td class="word_break"><c:out
								value="${dataRecord.carbonDioxide}" /></td>
						<td class="word_break"><c:out value="${dataRecord.tvoc}" /></td>
						<td class="word_break"><c:out
								value="${dataRecord.formaldehyde}" /></td>
						<td class="word_break"><c:out value="${dataRecord.pm2_5}" /></td>
						<td class="word_break"><c:out value="${dataRecord.pm10}" /></td>
						<td class="word_break"><c:out value="${dataRecord.pm1_0}" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
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