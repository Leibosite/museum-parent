<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<head>
<title><fmt:message
		key="pageTitle.evaluation_reference_standard_edit" /></title>
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

	<form:form id="inputForm" modelAttribute="standard"
		action="${ctx}/evaluation/standard/save" method="post"
		class="form-horizontal">
		<c:if test="${not empty standard.id}">
			<form:hidden path="id" />
		</c:if>
		<div class="well-small well well-bgwhite">
			<li>
				<label><fmt:message key="pojo.Standard.name" /></label> 
				<form:input path="name" placeholder="Name" cssClass="input" width="100px"/>
			</li>
			<li>
				<label><fmt:message key="pojo.Standard.desp" /></label> 
				<form:input path="desp" placeholder="Desp" cssClass="input"/>
			</li>

		</div>
		<div class="form-actions">
			<button type="submit" class="btn btn-large btn-primary">
				<fmt:message key="btn.submit" />
			</button>
			<a class="btn btn-large" href="${ctx}/evaluation/standard/list?urlType=return">
				<fmt:message key="btn.cancel" />
			</a>
		</div>
	</form:form>
	<script type="text/javascript">
		$("#inputForm").validate();
	</script>
</body>