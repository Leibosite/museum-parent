<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<head>
	<title><fmt:message key="pageTitle.pma_policygroup_list"/></title> 

</head>
<body>
<div id="js-ajax-repalce-block" style="position:relative">
   <h2 style="position:relative"><fmt:message key="pageTitle.pma_policygroup_list" />
   		<shiro:hasPermission name="PolicyGroup:create">
	   	 <span class="oper-block-title btn-group">
	   	 	<a class="btn btn-mini btn-primary" href="create">
	     		<span class="icon-plus-sign icon-white"></span></a>
	     </span>
	     </shiro:hasPermission>
	     </h2>
    <hr/>
     <form:form class="well-small well form-inline search-form-style" method="get" modelAttribute="queryForm">
       <form:input path="policyGroup" placeholder="Name" cssClass="input"/>
      <button type="submit" style="margin-left:4px" class="btn"><i class="icon-search"></i><fmt:message key="btn.search" /></button>
    </form:form>
    <table style="background-color:white" class="table table-striped table-bordered" id="list-table">
      <thead>
      	<tr>
	       <th><fmt:message key="th.policyGroupName"/></th>
	       <th><fmt:message key="th.operation" /></th>
	    </tr>
      </thead>
      <tbody>
      	<c:forEach items="${pageObjects.content}" var="policygroup">
	      <tr>
	        <td><c:out value="${policygroup.groupName}"/></td>
	        <td>
	        	<shiro:hasPermission name="PolicyGroup:update">
	            <a class="badge badge-info" href="edit/${policygroup.id}"><fmt:message key="btn.edit" /></a>
	            </shiro:hasPermission>
	            <!--<shiro:hasPermission name="PolicyGroup:delete">
		        <a class="badge badge-important delete-item" href="delete/${policygroup.id}"><fmt:message key="btn.delete" /></a>
		        </shiro:hasPermission>-->
		        <shiro:hasPermission name="PolicyGroup:view">
		        <a class="badge badge-info" href="${ctx}/pma/policy/show/${policygroup.id}"><fmt:message key="btn.viewPolicy" /></a>
		        </shiro:hasPermission>
	        </td>
	      </tr>
        </c:forEach>
      </tbody>
    </table>
    <%@ include file="../../component/pagination.jsp" %>
</div>
</body>