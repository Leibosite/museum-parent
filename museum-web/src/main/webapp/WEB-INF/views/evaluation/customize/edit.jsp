<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<head>
<title><fmt:message
		key="pageTitle.evaluation_reference_customize_standard_edit" /></title>

</head>
<body>
	<h2 style="position: relative">
		<c:if test="${action=='create'}">
			<fmt:message key="th.create" />
		</c:if>
		<c:if test="${action=='edit'}">
			<fmt:message key="th.edit" />
		</c:if>
		<fmt:message key="pageTitle.evaluation_reference_standard" />
	</h2>
	<hr />

	<form:form id="inputForm" modelAttribute="customizedStandard"
		action="${ctx}/evaluation/customize/save" method="post"
		class="form-horizontal">
		<c:if test="${not empty customizedStandard.id}">
			<form:hidden path="id" />
		</c:if>
		<div class="well-small well well-bgwhite">
			<li><label><fmt:message
						key="pojo.CustomizedStandard.name" /></label> <form:input path="name" /></li>
			<li><label><fmt:message
						key="pojo.CustomizedStandard.desp" /></label> <form:input path="desp" /></li>
			<li><label><fmt:message
						key="pojo.CustomizedStandard.area_name" /></label> <form:select
					path="area.id" cssClass="chzn-autoselect-area">
					<form:option value=""></form:option>
					<form:options items="${allAreas}" itemLabel="name" itemValue="id" />
				</form:select> <fmt:message key="required" /></li>
			<li><label><fmt:message
						key="pojo.CustomizedStandard.standard_name" /></label> <form:select
					path="standard.id" cssClass="chzn-autoselect-standard">
					<form:option value=""></form:option>
					<form:options items="${allStandards}" itemLabel="name" itemValue="id" />
				</form:select> <fmt:message key="required" /></li>
			<%-- <li><label><fmt:message key="pojo.CustomizedStandard.area_name" /></label> <form:select
					path="area" cssClass="js-select required">
					<form:option value="">-请选择数据类型-</form:option>
					<form:options items="${areaMap}" />
				</form:select> <fmt:message key="required" /></li>
			<li><label><fmt:message key="pojo.CustomizedStandard.standard_name" /></label> <form:input
					path="standard" /></li> --%>

		</div>
		<div class="form-actions">
			<button type="submit" class="btn btn-large btn-primary">
				<fmt:message key="btn.submit" />
			</button>
			<a class="btn btn-large"
				href="${ctx}/evaluation/customize/list?urlType=return"><fmt:message
					key="btn.cancel" /></a>
		</div>
	</form:form>
	<script type="text/javascript">
		$("#inputForm").validate();
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
			$(".chzn-autoselect-standard")
					.ajaxChosen(
							{
								original_chosen_option : {
									allow_single_deselect : true
								},
								method : 'GET',
								url : '${ctx}/evaluation/customize/fetchCustomizedStandardList',
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
</body>