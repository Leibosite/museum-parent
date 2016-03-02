<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<head>
<title><fmt:message key="pageTitle.evaluation_statistics_data" /></title>

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
			<!--搜索区域-->
		<form:form class="well-small well form-inline search-form-style"
			method="get" modelAttribute="queryForm" id="queryForm">
			
				<form:select path="areaId"  data-placeholder=""
					cssClass="input js-select">
					<form:option value="">--全部区域--</form:option>					
					<form:options items="${areaIdMap}" itemValue="id" itemLabel="name"  />
				</form:select>
				
				<form:select path="dataType"  placeholder="DataType"
					cssClass="input js-select">
					<form:option value="">数据类型</form:option>
					<form:options items="${dataTypeMap}" />
				</form:select>
				
			<form:select path="granularity" placeholder="Granularity"
				cssClass="input js-select">
				<form:option value="">时间粒度</form:option>
				<form:options items="${timeGranularityMap}" />
			</form:select>
			
			<form:input path="startTime" placeholder="开始时间" cssClass="time_picker" />
			<form:input path="endTime" placeholder="结束时间"  cssClass="time_picker" />
					
			<button type="submit" style="margin-left: 4px" class="btn">
				<i class="icon-search"></i>
				<fmt:message key="btn.search" />
			</button>
			<script>
				$("#queryForm").validate({
					rules : {
						imsi : "digits",
						e164 : "digits"
					},
					messages : {
						imsi : "请输入数字",
						e164 : "请输入数字"
					}
				})

				$(function() {
					$(".chzn-autoselect-area").ajaxChosen({
						original_chosen_option : {
							allow_single_deselect : true
						},
						method : 'GET',
						url : '${ctx}/evaluation/statistics/datafetch/area',
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
					<th><fmt:message key="th.time_data" /></th>
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
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${pageObjects.content}" var="dataStatisticsModel">
				<tr>
						<td class="word_break"><c:out value="${dataStatisticsModel.time}" /></td>
						<td class="word_break"><c:out value="${dataStatisticsModel.temperatureData}" /></td>
						<td class="word_break"><c:out value="${dataStatisticsModel.humidityData}" /></td>
						<td class="word_break"><c:out value="${dataStatisticsModel.lightingData}" /></td>
						<td class="word_break"><c:out value="${dataStatisticsModel.uvIntensityData}" /></td>
						<td class="word_break"><c:out value="${dataStatisticsModel.sulfurDioxideData}" /></td>
						<td class="word_break"><c:out value="${dataStatisticsModel.carbonDioxideData}" /></td>
						<td class="word_break"><c:out value="${dataStatisticsModel.vocData}" /></td>
						<td class="word_break"><c:out value="${dataStatisticsModel.formalclehydeData}" /></td>
						<td class="word_break"><c:out value="${dataStatisticsModel.pm02_5_Data}" /></td>
						<td class="word_break"><c:out value="${dataStatisticsModel.pm10_0_Data}" /></td>						
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

		<%@ include file="../../../component/pagination.jsp"%>
	</div>
	

</body>