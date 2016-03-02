<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<head>
<title><fmt:message key="pageTitle.evaluation_statistics_alarm" /></title>
<link rel="stylesheet" href="${staticFile}/bootstrap-table/css/rwd-table.min.css?v=5.0.4">
<link rel="stylesheet" href="${staticFile}/bootstrap-table/css/docs.min.css?v=5.0.4">
<style type="text/css">
.word_break {
	width: 100%;
	word-break: keep-all;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}
#changeColor td{
	background-color:#F5F5F5;
}
</style>

</head>
<body>
	<div id="js-ajax-repalce-block" style="position: relative">
		<h2 style="position: relative">
			<fmt:message key="pageTitle.evaluation_statistics_alarm" />
			<span
				style="color: green; font-size: 14px; left: 250px; position: absolute;">当前统计结果条数:
				${totalCount} </span>
		</h2>
		<hr />
			<!--搜索区域-->
		<form:form class="well-small well form-inline search-form-style"
			method="get" modelAttribute="queryForm" id="queryForm">

			<form:input path="startTime" placeholder="开始时间" cssClass="time_picker" />
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
		<div class="table-responsive" data-pattern="priority-columns">
                   <table id="tech-companies-1" class="table table-small-font table-bordered">
                      <thead>
                         <tr>
                            <th><fmt:message key="th.time" /></th>
							<th><fmt:message key="th.temperature_alarm" /></th>
							<th><fmt:message key="th.humidity_alarm" /></th>
							<th><fmt:message key="th.lighting_alarm" /></th>
							<th><fmt:message key="th.uvIntensity_alarm" /></th>
							<th><fmt:message key="th.sulfurDioxide_alarm" /></th>
							<th><fmt:message key="th.carbonDioxide_alarm" /></th>
							<th><fmt:message key="th.voc_alarm" /></th>
							<th><fmt:message key="th.formalclehyde_alarm" /></th>
							<th><fmt:message key="th.pm_alarm" /></th>
							<th><fmt:message key="th.danger_coefficient" /></th>
							<th><fmt:message key="th.total_alarm" /></th>
                         </tr>
                      </thead>
                      <tbody>
                        <c:forEach items="${pageObjects.content}" var="alarmStatisticsModel">
							<tr id="changeColor">
								<td class="word_break"><c:out value="${alarmStatisticsModel.time}" /></td>
								<td class="word_break"><c:out value="${alarmStatisticsModel.temperatureAlarm}" /></td>
								<td class="word_break"><c:out value="${alarmStatisticsModel.humidityAlarm}" /></td>
								<td class="word_break"><c:out value="${alarmStatisticsModel.lightingAlarm}" /></td>
								<td class="word_break"><c:out value="${alarmStatisticsModel.uvIntensityAlarm}" /></td>
								<td class="word_break"><c:out value="${alarmStatisticsModel.sulfurDioxideAlarm}" /></td>
								<td class="word_break"><c:out value="${alarmStatisticsModel.carbonDioxideAlarm}" /></td>
								<td class="word_break"><c:out value="${alarmStatisticsModel.vocAlarm}" /></td>
								<td class="word_break"><c:out value="${alarmStatisticsModel.formalclehydeAlarm}" /></td>
								<td class="word_break"><c:out value="${alarmStatisticsModel.pmAlarm}" /></td>
								<td class="word_break"><c:out value="${alarmStatisticsModel.dangerCoefficient}" /></td>
								<td class="word_break"><c:out value="${alarmStatisticsModel.totalAlarm}" /></td>
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

		<%@ include file="../../../component/pagination.jsp"%>
	</div>
</body>