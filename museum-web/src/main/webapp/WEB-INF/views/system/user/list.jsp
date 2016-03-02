<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<head>
<title><fmt:message key="pageTitle.access_user" /></title>
</head>

<body>
	<div>
		 <h2 style="position: relative">
		 <fmt:message key="pageTitle.access_user_list" />
				<span class="oper-block-title btn-group">
					<a class="btn btn-mini btn-primary" onclick="location.href='create'">
						<i class="icon-plus-sign icon-white"></i> 
					</a>
				</span>
			</h2>
			<hr />

			<form:form class="well-small well form-inline search-form-style"
				method="get" modelAttribute="queryForm">
				<h4 class="search-title margin-top-bottom-small"><fmt:message key="pageTitle.filter_access_user" /></h4>
				<form:input path="name" placeholder="姓名" cssClass="input" />
				<button type="submit" style="margin-left: 4px"class="btn">
					<i class="icon-search"></i><fmt:message key="btn.search" />
				</button>
			</form:form>

			<table style="background-color: white"
				class="table table-striped table-bordered" id="list-table">
				<thead>
					<tr>
						<th><fmt:message key="th.login_name" /></th>
						<th><fmt:message key="th.user_name" /></th>
						<th><fmt:message key="th.email" /></th>
						<th>状态</th>
						<th><fmt:message key="th.operation" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${pageObjects.content}" var="user">
						<tr>
							<td>${user.loginName}</td>
							<td>${user.name}</td>
							<td>${user.email}</td>
							<td>
								<c:if test="${user.state == 0}">
						        	<a class="badge badge-warning" href="${ctx}/pma/policy/disable?groupId=${id}&id=${policy.id}&page=${page}">启用</a>
						        </c:if>
						        <c:if test="${user.state == 1}">
						        	<a class="badge badge-success" href="${ctx}/pma/policy/restore?groupId=${id}&id=${policy.id}&page=${page}">禁用</a>
						        </c:if> 
							</td>
							<td>
								<a class="badge badge-info editTemplate" onclick="location.href='edit/${user.id}'"><fmt:message key="btn.edit" /></a>
								<a class="badge badge-important delete-item" onclick="location.href='${ctx}/system/user/delete/${user.id}'"><fmt:message key="btn.delete" /></a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<%@ include file="../../component/pagination.jsp"%>
	</div>
</body>
