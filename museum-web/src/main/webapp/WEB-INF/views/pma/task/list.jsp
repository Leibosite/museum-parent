<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<head>
	<title><fmt:message key="pageTitle.pma_task_list"/></title>

</head>
<body>
<div id="js-ajax-repalce-block" style="position:relative">
   <h2 style="position:relative"><fmt:message key="pageTitle.pma_task_list" /> 
    	<shiro:hasPermission name="Task:create">
	   	 <span class="oper-block-title btn-group">
	   	 	<a class="btn btn-mini btn-primary" href="create">
	     		<span class="icon-plus-sign icon-white"></span></a>
	     </span>
	     </shiro:hasPermission>
	     </h2>
    <hr/>
    <form:form class="well-small well form-inline search-form-style" method="get" modelAttribute="queryForm">
       <form:input path="name" placeholder="Name" cssClass="input"/>
      <button type="submit" style="margin-left:4px" class="btn"><i class="icon-search"></i><fmt:message key="btn.search" /></button>
    </form:form>
	
    <table style="background-color:white" class="table table-striped table-bordered" id="list-table">
      <thead>
      	<tr>
	       <th><fmt:message key="th.name" /></th>
	       <th><fmt:message key="th.timmer" /></th>
	       <th><fmt:message key="th.operation" /></th>
	    </tr>
      </thead>
      <tbody>
      	<c:forEach items="${pageObjects.content}" var="task">
	      <tr>
	        <td><c:out value="${task.name}"/></td>
	        <td>${task.timmer}</td>
	        <td>
	        <shiro:hasPermission name="Task:update">
	        <a class="badge badge-info" href="edit/${task.id}"><fmt:message key="btn.edit" /></a>
	        </shiro:hasPermission>
	        <c:if test="${task.status==1}">
	        <a class="badge badge-warning" href="${ctx}/pma/task/disable/${task.id}"><fmt:message key="btn.disable" /></a>
	        </c:if>
	        <c:if test="${task.status==0}">
	        <a class="badge badge-success" href="${ctx}/pma/task/restore/${task.id}"><fmt:message key="btn.enable" /></a>
	        </c:if>
	        <shiro:hasPermission name="Task:delete">
	        <a class="badge badge-important delete-item" href="delete/${task.id}"><fmt:message key="btn.delete" /></a>
	        </shiro:hasPermission>
	        </td>
	      </tr>
        </c:forEach>
      </tbody>
    </table>

    <%@ include file="../../component/pagination.jsp" %>
</div>
</body>