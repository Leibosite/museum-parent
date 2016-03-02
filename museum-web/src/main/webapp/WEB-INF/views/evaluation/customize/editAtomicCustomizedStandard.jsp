<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<head>
<title><fmt:message
		key="pageTitle.evaluation_reference_atomic_standard_edit" /></title>

</head>
<body>
	<h2 style="position: relative">
		<c:if test="${empty atomicStandard.id}">
			<fmt:message key="th.create" />
		</c:if>
		<c:if test="${not empty atomicStandard.id}">
			<fmt:message key="th.edit" /> ${atomicStandard.name}</c:if>
	</h2>
	<hr>
	<!--include includes/form-rule-->
	<form:form id="inputForm" modelAttribute="atomicStandard"
		action="${ctx}/evaluation/customize/saveAtomicCustomizedStandard?groupId=${groupId}&page=${page}"
		method="post" class="form-horizontal">
		<c:if test="${not empty atomicStandard.id}">
			<form:hidden path="id" />
		</c:if>
		<div class="well-small well well-bgwhite">
			<li><label><fmt:message key="pojo.Standard.name" /></label> <form:input
					path="name" /></li>
			<li><label><fmt:message key="pojo.Standard.desp" /></label> <form:input
					path="desp" /></li>
			<li><label><fmt:message
						key="pojo.Standard.monitorObjectType" /></label> <form:select
					path="monitorObjectType" cssClass="js-select required">
					<form:option value="">-请选择数据类型-</form:option>
					<form:options items="${monitorObjectMap}" />
				</form:select> <fmt:message key="required" />
			</li>
			<li><label><fmt:message key="pojo.Standard.idealValue" /></label>
				<form:input path="idealValue" /></li>
			<li><label><fmt:message key="pojo.Standard.idealMin" /></label>
				<form:input path="idealMin" /></li>
			<li><label><fmt:message key="pojo.Standard.idealMax" /></label>
				<form:input path="idealMax" /></li>
			<li><label><fmt:message key="pojo.Standard.acceptMin" /></label>
				<form:input path="acceptMin" /></li>
			<li><label><fmt:message key="pojo.Standard.acceptMax" /></label>
				<form:input path="acceptMax" /></li>
			<li><label><fmt:message
						key="pojo.Standard.fluctuatuionCoefficient" /></label> <form:input
					path="fluctuatuionCoefficient" /></li>

		</div>
		<div class="form-actions">
			<button type="submit" class="btn btn-large btn-primary">
				<fmt:message key="btn.submit" />
			</button>
			<a class="btn btn-large"
				href="${ctx}/evaluation/customize/show/${groupId}?page=${page}"><fmt:message
					key="btn.cancel" /></a>
		</div>
	</form:form>
	<script type="text/javascript">
		$("#inputForm").validate();
	</script>
</body>