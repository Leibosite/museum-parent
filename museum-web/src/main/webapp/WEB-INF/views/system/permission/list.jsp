<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>

<head>
	<title><fmt:message key="pageTitle.permissions.to.view"></fmt:message></title>
</head>

<body>
<div id="js-ajax-repalce-block" style="position:relative">
   <h2 style="position:relative"><fmt:message key="pageTitle.access.permission.list"></fmt:message>
	   	 <span class="oper-block-title btn-group">
	   	 	<a class="btn btn-mini btn-primary" href="create">
	     		<span class="icon-plus-sign icon-white"></span>
	     	</a>
	     </span>
	     </h2>
    <hr/>
    
   <form:form class="well-small well form-inline search-form-style" method="get" modelAttribute="queryForm">
    <h4 class="search-title margin-top-bottom-small"><fmt:message key="pageTitle.filter.access.permission"></fmt:message></h4>
    <form:input path="displayname" placeholder="Name" cssClass="input-small"/>
    <button type="submit" style="margin-left:4px" class="btn"><i class="icon-search"></i><fmt:message key="btn.search"></fmt:message></button>
 	 </form:form>

    <table style="background-color:white" class="table table-striped table-bordered" id="list-table">
      <thead>
        <tr>
	      <th><fmt:message key="th.name"></fmt:message></th>
	      <th><fmt:message key="th.customize_standard_desp"></fmt:message></th>
	      <th><fmt:message key="th.operation"></fmt:message></th>
	    </tr>
      </thead>
      
      <tbody>
      	<c:forEach items="${pageObjects.content}" var="permission">
	      <tr>
	        <td>${permission.displayname}</td>
	        <td>${permission.desp}</td>
	        <td>
	        <a class="badge badge-info" href="edit/${permission.id}"><fmt:message key="btn.edit"></fmt:message></a>
	        <a class="badge badge-important"  href="${ctx}/system/permission/delete/${permission.id}"><fmt:message key="btn.delete"></fmt:message></a>
	        </td>
	      </tr>
        </c:forEach>
      </tbody>
    </table>

	 <%@ include file="../../component/pagination.jsp" %>
	 </div>
</body>

