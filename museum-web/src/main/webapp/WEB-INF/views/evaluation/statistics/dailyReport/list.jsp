<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" import="java.util.Date"%>

<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<head>
<title><fmt:message
		key="pageTitle.evaluation_statistics_dailyReport" /></title>

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
		<h2>
			<span style="color: black; font-size: 26px; left: 250px;">今日博物馆监测报告
			</span>
		</h2>
		<table class="table table-bordered"
			style="text-align: center; width: 100%; margin: auto;" border="1"
			modelAttribute="totalMuseum" id="totalMuseum">
			<tbody>
				<tr style="text-align: center;">
					<th>时间</th>
					<td><fmt:formatDate value="${totalMuseum.dateTime}" pattern="yyyy年MM月dd日" /> </td>
					<th>参考标准</th>
					<td>GB2312/2006</td>
					<th rowspan="2" style="line-height: 4; text-align: center;">博物馆总评分</th>
					<td rowspan="2" style="line-height: 4; text-align: center;"><c:out
							value="${totalMuseum.score}" /></td>
				</tr>
				<tr>
					<th>编号</th>
					<td><c:out value="${totalMuseum.timeStamp}" /></td>
					<th>检测内容</th>
					<td>博物馆全天数据</td>
				</tr>

			</tbody>
		</table>

		<table
			style="text-align: center; background-color: white; width: 100%; margin: auto;"
			class="table table-bordered" id="list-table" border="1">
			<caption>
				<span style="font-weight: bold; font-size: 20px;">博物馆日均环境值</span>
			</caption>
			<thead>
				<tr>
					<th style="text-align: center;"><fmt:message key="th.areaName" /></th>
					<th><fmt:message key="th.status" /></th>
					<th><fmt:message key="th.data_type" /></th>
					<th><fmt:message key="th.temperature_data" /></th>
					<th><fmt:message key="th.humidity_data" /></th>-
					<th><fmt:message key="th.lighting_data" /></th>
					<th><fmt:message key="th.uvIntensity_data" /></th>
					<th><fmt:message key="th.sulfurDioxide_data" /></th>
					<th><fmt:message key="th.carbonDioxide_data" /></th>
					<th><fmt:message key="th.voc_data" /></th>
					<th><fmt:message key="th.formalclehyde_data" /></th>
					<th><fmt:message key="th.pm02_5_data" /></th>
					<th><fmt:message key="th.pm10_0_data" /></th>
					<th><fmt:message key="th.score" /></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${pageObjects.content}" var="dailyDataReportModel">
					<tr>
						<td rowspan="3" style="line-height: 8; text-align: center;"><c:out
								value="${dailyDataReportModel.areaName}" /></td>
						<td rowspan="3" style="line-height: 8; text-align: center;"><c:out
								value="${dailyDataReportModel.areaStatus}" /></td>
						<td width="45px" style="text-align: center;"><fmt:message
								key="th.ideal" /></td>
						<td width="37px">20-23</td>
						<td width="37px">20-23</td>
						<td width="37px">20-23</td>
						<td width="37px">20-23</td>
						<td width="37px">20-23</td>
						<td width="37px">20-23</td>
						<td width="37px">20-23</td>
						<td width="37px">20-23</td>
						<td width="37px">20-23</td>
						<td width="37px">20-23</td>
						<td></td>
					</tr>
					<tr>
						<td style="text-align: center;"><fmt:message key="th.accept" /></td>
						<td>18-25</td>
						<td>18-25</td>
						<td>18-25</td>
						<td>18-25</td>
						<td>18-25</td>
						<td>18-25</td>
						<td>18-25</td>
						<td>18-25</td>
						<td>18-25</td>
						<td>18-25</td>
						<td></td>
					</tr>
					<tr>
						<td style="text-align: center;"><fmt:message key="th.current" /></td>
						<td><c:out value="${dailyDataReportModel.temperatureData}" /></td>
						<td><c:out value="${dailyDataReportModel.humidityData}" /></td>
						<td><c:out value="${dailyDataReportModel.lightingData}" /></td>
						<td><c:out value="${dailyDataReportModel.uvIntensityData}" /></td>
						<td><c:out value="${dailyDataReportModel.sulfurDioxideData}" /></td>
						<td><c:out value="${dailyDataReportModel.carbonDioxideData}" /></td>
						<td><c:out value="${dailyDataReportModel.vocData}" /></td>
						<td><c:out value="${dailyDataReportModel.formalclehydeData}" /></td>
						<td><c:out value="${dailyDataReportModel.pm02_5_Data}" /></td>
						<td><c:out value="${dailyDataReportModel.pm10_0_Data}" /></td>
						<td><c:out value="${dailyDataReportModel.score}" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<%@ include file="../../../component/pagination.jsp"%>
		<table
			style="text-align: center; background-color: white; width: 100%; margin: auto;"
			class="table table-bordered">
			<tr>
				<td rowspan="3"
					style="font-size: 20px; font-weight: bolder; text-align: center; line-height: 5">监测总结：</td>
				<td>今日博物馆环境监测情况良好</td>
			</tr>

			<tr>
				<td>但要注意马踏飞燕展厅的温湿度控制！</td>
			</tr>
			<tr>
				<td>建议打开空调，控制温度在25左右。</td>
			</tr>
			<tr style="font-weight: bolder;">
				<td></td>
				<td style="float: right; margin-right: 180px">负责人签名：</td>
			</tr>
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


	</div>

</body>
