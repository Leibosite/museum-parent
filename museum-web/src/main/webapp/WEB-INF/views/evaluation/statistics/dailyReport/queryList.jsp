<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<head>
<title><fmt:message key="pageTitle.evaluation_statistics_dailyReport" /></title>

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
		<h2 >
			<span
				style="color: black; font-size: 20px; left: 250px;">请选择相应的区域以及时间
			 </span>
		</h2>
		<hr />
			<!--搜索区域-->
		<form:form class="well-small well form-inline search-form-style"
			method="get" modelAttribute="queryForm" id="queryForm">

			<form:select path="areaId"  data-placeholder=""
					cssClass="input js-select">
					<form:option value="">--全部区域--</form:option>					
					<form:options items="${areaIdMap}" itemValue="id" itemLabel="name"  />
				</form:select>
			<form:input type="text" path="time" placeholder="选择时间" cssClass="time_picker" />
			
			<button type="button"  class="btn btn-success" onclick="location.href='list'">			
				确定
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
		<p style="font-weight: bolder;font-size: 20px"> 点击确定，默认查看 马踏飞燕展厅、丝绸之路展厅、书画展厅、陶瓷库房、青铜器库房 今日环境监测统计</p>
		
			<script type="text/javascript">
			$(function() {
				$('.time_picker').datetimepicker({
					language : 'zh-CN',
					format : 'yyyy-mm',
					startView : 3, // 设置视图最小粒度 3为月份 4为年
					minView : 3,   // 设置最小检索单元 3为月份
					autoClose : true,
				});
			});
		</script>

	</div>
</body>