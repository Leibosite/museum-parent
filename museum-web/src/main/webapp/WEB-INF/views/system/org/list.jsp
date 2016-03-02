<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<head>
	<title>PCRF-Navi</title>
</head>

<body>
<div id="js-ajax-repalce-block" style="position:relative">
   <h2 style="position:relative">Organization Information List
	   	 <span class="oper-block-title btn-group">
	   	 	<a class="btn btn-mini btn-primary" href="create">
	     		<span class="icon-plus-sign icon-white"></span></a>
	     </span>
	     </h2>
    <hr/>
    <form:form class="well-small well form-inline search-form-style" method="get" modelAttribute="queryForm">
      <h4 class="search-title margin-top-bottom-small">Filter Organization</h4>
      <form:input path="name" placeholder="Name" cssClass="input-small"/>
      <form:select path="status" placeholder="Status" cssClass="input-small js-select">
      	<form:option value="">-please select-</form:option>
	    <form:options items="${statusMap}"/>
      </form:select>
      
      <button type="submit" style="margin-left:4px" class="btn"><i class="icon-search"></i>Search</button>
    </form:form>
	
    <table style="background-color:white" class="table table-striped table-bordered" id="list-table">
      <thead>
      	<tr>
	      <th>Organization Name</th>
	      <th>Status</th>
	      <th>Operation</th>
	    </tr>
      </thead>
      <tbody>
      	<c:forEach items="${pageObjects.content}" var="orgInfo">
	      <tr>
	        <td>${orgInfo.name}</td>
	        <td>${statusMap[orgInfo.status]}</td>
	        <td><a class="badge badge-info" href="edit/${orgInfo.id}">Edit</a>
	        <c:if test="${orgInfo.status==1}">
	        <a class="badge badge-warning" href="${ctx}/system/org/disable/${orgInfo.id}">Disable</a>
	        </c:if>
	        <c:if test="${orgInfo.status==0}">
	        <a class="badge badge-success" href="${ctx}/system/org/restore/${orgInfo.id}">Restore</a>
	        </c:if>
	        <a class="badge badge-important" href="${ctx}/system/org/delete/${orgInfo.id}">Delete</a></td>
	      </tr>
        </c:forEach>
      </tbody>
    </table>

    <%@ include file="../../component/pagination.jsp" %>
</div>
</body>