<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>

<table style="background-color:white" class="table table-striped table-bordered" id="list-table">
  <thead>
    <tr>
    <th>Name</th>
    <th>Description</th>
    <th>Operation</th>
  </tr>
  </thead>
  
  <tbody>
  	<c:forEach items="${pageObjects.content}" var="permission">
   <tr>
     <td>${permission.displayname}</td>
     <td>${permission.desp}</td>
     <td>
     <a class="badge badge-info" href="edit/${permission.id}">Edit</a>
     <a class="badge badge-important"  href="${ctx}/system/permission/delete/${permission.id}">Delete</a>
     </td>
   </tr>
    </c:forEach>
  </tbody>
</table>
<%@ include file="../../component/pagination.jsp" %>


