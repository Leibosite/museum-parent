<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<head>
<title><fmt:message
		key="pageTitle.evaluation_reference_atomic_standard_list" /></title>

</head>
<body>
	<div id="js-ajax-repalce-block" style="position: relative">

		<h2 style="position: relative">
			<c:out value="${name}" />
			<fmt:message
				key="pageTitle.evaluation_reference_atomic_standard_list" />
			<span class="oper-block-title btn-group"> <a
				class="btn btn-mini btn-primary"
				href="${ctx}/evaluation/standard/addAtomicStandard?groupId=${id}&page=${page}&id=">
					<span class="icon-plus-sign icon-white"></span>
			</a>
			</span>
		</h2>
		<hr />
		<!--search banner  start  -->
		<form:form class="well-small well form-inline search-form-style"
			method="get" modelAttribute="queryForm">

			<form:input path="name" placeholder="atomic_standard_name"
				cssClass="input" />
			<form:select path="status" placeholder="Status"
				cssClass="input js-select">
				<form:option value="">--Please Select--</form:option>
				<form:option value="0">Disable</form:option>
				<form:option value="1">Enable</form:option>
			</form:select>

			<button type="submit" style="margin-left: 4px" class="btn">
				<i class="icon-search"></i>
				<fmt:message key="btn.search" />
			</button>
		</form:form>

		<!--search banner   end -->
		<table style="background-color: white"
			class="table table-striped table-bordered" id="list-table">
			<thead>
				<tr>

					<th><fmt:message key="th.name" /></th>
					<th><fmt:message key="th.desp" /></th>
					<th><fmt:message key="th.monitorObjectType" /></th>
					<th><fmt:message key="th.idealValue" /></th>
					<th><fmt:message key="th.idealMin" /></th>
					<th><fmt:message key="th.idealMax" /></th>
					<th><fmt:message key="th.acceptMin" /></th>
					<th><fmt:message key="th.acceptMax" /></th>
					<th><fmt:message key="th.fluctuatuionCoefficient" /></th>
					<th><fmt:message key="th.operation" /></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${pageObjects.content}" var="atomicStandard">
					<tr>
						<td><c:out value="${atomicStandard.name}" /></td>
						<td><c:out value="${atomicStandard.desp}" /></td>
						<td><c:out value="${atomicStandard.monitorObjectType.value}" /></td>
						<td><c:out value="${atomicStandard.idealValue}" /></td>
						<td><c:out value="${atomicStandard.idealMin}" /></td>
						<td><c:out value="${atomicStandard.idealMax}" /></td>
						<td><c:out value="${atomicStandard.acceptMin}" /></td>
						<td><c:out value="${atomicStandard.acceptMax}" /></td>
						<td><c:out value="${atomicStandard.fluctuatuionCoefficient}" /></td>
						<td width="165px"><a class="badge badge-info"
							href="${ctx}/evaluation/standard/addAtomicStandard?groupId=${id}&id=${atomicStandard.id}&page=${page}"><fmt:message
									key="btn.edit" /></a> <a class="badge badge-important delete-item"
							href="${ctx}/evaluation/standard/deleteAtomicStandard?groupId=${id}&id=${atomicStandard.id}&page=${page}"><fmt:message
									key="btn.delete" /></a> 
							<!--
							<c:if test="${atomicStandard.status == true}">
								<a class="badge badge-warning"
									href="${ctx}/evaluation/standard/disable?groupId=${id}&id=${atomicStandard.id}&page=${page}"><fmt:message
										key="btn.disable" /></a>
							</c:if> 
							<c:if test="${atomicStandard.status == false || empty atomicStandard.status}">
								<a class="badge badge-success"
									href="${ctx}/evaluation/standard/restore?groupId=${id}&id=${atomicStandard.id}&page=${page}"><fmt:message
										key="btn.enable" /></a>
							</c:if>
							  -->
							</td>
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
		<a class="btn btn-large"
			href="${ctx}/evaluation/standard/list?urlType=return"><fmt:message
				key="btn.return_to_list" /></a>
	</div>
</body>